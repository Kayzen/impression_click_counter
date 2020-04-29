package com.kayzen.impcount.executor.dataprocessor;

import com.applift.platform.commons.db.DBContext;
import com.applift.platform.commons.db.MySqlDatabase;
import com.applift.platform.commons.db.statement.MySQLPreparedStatement;
import com.applift.platform.commons.enums.Environment;
import com.kayzen.impcount.model.ImpressionObject;
import com.kayzen.impcount.model.SharedDataObject;
import com.kayzen.impcount.utils.Constants;
import com.kayzen.impcount.utils.DeviceIterator;
import com.kayzen.impcount.utils.MySqlDbUtils;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import net.openhft.chronicle.map.ChronicleMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchDataProcessor implements Serializable, Runnable {

  private static final long serialVersionUID = -863674547031919840L;
  private static Logger logger = LoggerFactory.getLogger(BatchDataProcessor.class.getName());
  private LinkedBlockingQueue<ImpressionObject> sharedBQueue;
  private int threadIndex;
  private Environment environment;
  private String dbHostName;
  private int batchSize;
  private MySqlDatabase database;
  private long startTimeMillis;
  private long startBatchTimeMillis;
  private long currentTimeMillis;
  private long lastCheckTimeMs;
  private int numberOfBatches;
  private long totalObjectsProcessed;
  private long totalMutations;
  private int deviceCount;
  private int deviceCountBatch;

  private int cImpCount;
  private int cImpCountBatch;

  private int batchCount;
  private int pendingBufferSize;
  private long partitionCounter;
  private MySQLPreparedStatement devices;
  private MySQLPreparedStatement impressions;
  private ImpressionObject impressionObject;
  private ChronicleMap<String, Long> deviceMap;
  private List<Long> kafkaOffsets;

  public BatchDataProcessor(int threadIndex, Environment env, String dbHostName, int batchSize) {
    try {
      this.sharedBQueue = SharedDataObject.sharedObjectQueues.get(threadIndex);
      this.threadIndex = threadIndex;
      this.partitionCounter = SharedDataObject.getThreadPartitionCounterOpt(threadIndex);
      this.environment = env;
      this.dbHostName = dbHostName;
      this.batchSize = batchSize;
      this.totalObjectsProcessed = 0;
      this.totalMutations = 0;
      this.deviceCount = 0;
      this.cImpCount = 0;
      this.cImpCountBatch = 0;
      this.pendingBufferSize = 0;
      this.numberOfBatches = 0;
      this.lastCheckTimeMs = System.currentTimeMillis();
      this.startBatchTimeMillis = System.currentTimeMillis();
      this.deviceMap = SharedDataObject.deviceMaps.get(this.threadIndex);
      this.kafkaOffsets = null;
      init();
      getPSTs();
    } catch (Exception e) {
      logger.error("Exception occurred in constructor DataProcessor. Exception:", e);
      System.exit(1);
    }
    logger.info("Starting thread with threadIndex:" + threadIndex + ", partitionCounter:"
        + this.partitionCounter);
  }

  private void init() throws SQLException {
    DBContext dbContext = new DBContext(environment,dbHostName, LoggerFactory.getLogger(BatchDataProcessor.class));
    database = new MySqlDatabase(dbContext);
  }

  private void getPSTs() {
    this.devices = MySqlDbUtils.getAddDevicePST(database);
    this.impressions = MySqlDbUtils.getCampaignImpressionPST(database);
  }

  private void verifyAndUpdatePartitionCounter() {
    long mysqlCounter = MySqlDbUtils.getPartitionCounterFromDB(database, threadIndex);
    if (partitionCounter < mysqlCounter) {
      logger.warn("Inconsistency in MapDB Data for threadIndex:" + threadIndex + " mapDB at:"
          + partitionCounter
          + " MySQLCounter at:" + mysqlCounter + " Updating mapDB");
      DeviceIterator deviceIterator = new DeviceIterator(database, threadIndex, batchSize,
          partitionCounter);
      try {
        while (deviceIterator.next()) {
          partitionCounter++;
          deviceMap.put(deviceIterator.getSha1(), deviceIterator.getDeviceId());
          if (partitionCounter % batchSize == 0) {
            logger.warn("Updating MapDB Data for threadIndex:" + threadIndex + " mapDB at:"
                + partitionCounter
                + " MySQLCounter at:" + mysqlCounter);
          }
        }
      } catch (SQLException e) {
        logger.error(
            "Exception while updating MapDB Data for threadIndex:" + threadIndex + " mapDB at:"
                + partitionCounter + " MySQLCounter at:" + mysqlCounter, e);
        System.exit(1);
      }
      logger.info(
          "Updated MapDB Data for threadIndex:" + threadIndex + " mapDB at:" + partitionCounter
              + " MySQLCounter at:" + mysqlCounter);

    } else if (partitionCounter > mysqlCounter) {
      partitionCounter = mysqlCounter;
    }
  }

  @Override
  public void run() {
    verifyAndUpdatePartitionCounter();
    startTimeMillis = System.currentTimeMillis();
    while (!sharedBQueue.isEmpty() || SharedDataObject.keepReadingQueues) {
      try {
        currentTimeMillis = System.currentTimeMillis();
        if (sharedBQueue.size() > 0) {
          this.impressionObject = sharedBQueue.poll();
          if (impressionObject != null) {
            processObject(impressionObject);
            this.totalObjectsProcessed++;
            this.pendingBufferSize++;

            if (this.batchCount % this.batchSize == 0) {
              executeBatch();
              //TODO-dgpatil-stats comment stat related stuff
              //SharedDataObject.printStatistics();
            }
            if (totalObjectsProcessed % 5000 == 0) {
              writeLog();
            }
          }
        } else {
          if (currentTimeMillis > lastCheckTimeMs + Constants.TEN_SECONDS_MILLS) {
            lastCheckTimeMs = System.currentTimeMillis();
            executeBatch();
            //TODO-dgpatil-stats comment stat related stuff
            //SharedDataObject.printStatistics();
          }
          Thread.sleep(Constants.ONE_SECOND_MILLS);
        }
      } catch (Exception e) {
        logger.error("Exception occurred while processing DataProcessor. Exception:", e);
      }
    }
    while (this.pendingBufferSize != 0) {
      logger.info("Found pendingBufferSize:" + pendingBufferSize);
      executeBatch();
    }
    cleanUp();
    writeLogAll();
  }

  private void processObject(ImpressionObject impressionObject) {
    logger.debug(impressionObject.toString());
    checkAndAddDevice(impressionObject);
    handleImpressionObject(impressionObject);
    if (SharedDataObject.enableKafkaOffsets) {
      if (this.kafkaOffsets == null) {
        kafkaOffsets = SharedDataObject.kafkaOffsetPerProcessor.get(threadIndex);
      }
      this.kafkaOffsets
          .set(impressionObject.getKafkaConsumerIdentifier(), impressionObject.getLatestKafkaOffset());
    }
    this.batchCount++;
    impressionObject = null;
  }

  private void checkAndAddDevice(ImpressionObject impressionObject) {
    if (impressionObject.getDeviceIdSha1() != null && !impressionObject.getDeviceIdSha1().isEmpty()) {
      if (!deviceMap.containsKey(impressionObject.getDeviceIdSha1())) {
        this.partitionCounter++;
        impressionObject.setDeviceIdLong(partitionCounter);
        impressionObject.setThreadId(threadIndex);
        deviceMap.put(impressionObject.getDeviceIdSha1(), this.partitionCounter);
        if (MySqlDbUtils.addDevices(devices, impressionObject)) {
          this.deviceCountBatch++;
        }
      } else {
        impressionObject.setDeviceIdLong(deviceMap.get(impressionObject.getDeviceIdSha1()));
        impressionObject.setThreadId(threadIndex);
      }
    }
  }

  private void handleImpressionObject(ImpressionObject impressionObject) {
    //Check if we have already seen an event for this bid-id.
    if (impressionObject.getBidId() != null && !impressionObject.getBidId().isEmpty()) {
      if (!SharedDataObject.impBidsMap.containsKey(impressionObject.getBidId())) {
        SharedDataObject.impBidsMap.put(impressionObject.getBidId(), 0);
      } else {
        //Already considered the event from this bid-id.
        return;
      }
    }

    if (MySqlDbUtils.addCampaignImpressionData(impressions, impressionObject)) {
      this.cImpCountBatch++;
    }
  }

  private void executeBatch() {
    try {
      boolean isDeviceBatchExecuted = true;
      // creates devices
      if (this.deviceCountBatch > 0) {
        isDeviceBatchExecuted = database.batchQuery(this.devices, 2);
      }
      if (isDeviceBatchExecuted) {
        if (this.cImpCountBatch > 0) {
          database.batchQuery(impressions);
        }

        long timeTaken = System.currentTimeMillis() - this.startBatchTimeMillis;
        this.pendingBufferSize -= this.batchCount;
        this.batchCount = 0;
        this.numberOfBatches++;
        resetBatch(timeTaken);
        lastCheckTimeMs = System.currentTimeMillis();
        this.startBatchTimeMillis = System.currentTimeMillis();
        if (this.kafkaOffsets != null) {
          if (SharedDataObject.enableKafkaOffsets) {
            SharedDataObject.setKafkaOffset(this.threadIndex, this.kafkaOffsets);
          }
        }
      } else {
        logger
            .error("[CRITICAL] Batch Missed as devices query failed. Kafka Offsets last committed: "
                + SharedDataObject.kafkaOffsetPerProcessorBatch.get(threadIndex));
      }
    } catch (Exception e) {
      logger.error("Exception in executeBatch main method. Exception:", e);
    }
  }

  private void resetBatch(long timeTaken) {
    //TODO-dgpatil-stats comment stat related stuff
    //SharedDataObject.updateStatistics(this.cImpCountBatch, this.lImpCountBatch, this.cClkCountBatch, this.lClkCountBatch,
      //  this.cConvCountBatch, this.lConvCountBatch, this.deviceCountBatch, timeTaken);
    this.cImpCount += this.cImpCountBatch;
    this.deviceCount += this.deviceCountBatch;

    int totalMutationsBatch =
        this.deviceCountBatch + this.cImpCountBatch;
    this.totalMutations += totalMutationsBatch;
    this.deviceCountBatch = 0;

    this.cImpCountBatch = 0;

    logger.debug("Thread-" + this.threadIndex + " wrote batch of objects size:" + batchCount
          + " total mutations:"
          + totalMutationsBatch + " time taken by executeBatch:" + timeTaken + " sharedBQueue size:"
          + sharedBQueue.size());
    writeLog();
    getPSTs();
  }

  void shutdown() {
    logger
        .info("Shutdown Initiated for BatchDataProcessor-" + threadIndex + ". Data to be processed:"
            + sharedBQueue.size());
  }

  private void cleanUp() {
    try {
      database.close();
      SharedDataObject.setThreadPartitionCounterOpt(this.threadIndex, this.partitionCounter);
    } catch (Exception e) {
      logger.error("Thread-" + this.threadIndex + ". Error in executing database.close()", e);
    }
  }

  private void writeLog() {
    logger.debug("Thread-" + this.threadIndex + ",totalObjectsProcessed:" + totalObjectsProcessed
        + ",numberOfBatches:"
        + this.numberOfBatches + ",cImpCount:" + this.cImpCount + ",deviceCount:" + this.deviceCount
        + ",sharedBQueue.size():" + sharedBQueue.size());
  }

  private void writeLogAll() {
    logger.info("Thread-" + this.threadIndex + ",totalObjectsProcessed:" + totalObjectsProcessed
        + ",numberOfBatches:"
        + this.numberOfBatches + ",cImpCount:" + this.cImpCount + ",deviceCount:" + this.deviceCount
        + ",sharedBQueue.size():" + sharedBQueue.size());
    long timeTaken = System.currentTimeMillis() - startTimeMillis;
    double timeTakenSec = timeTaken / 1000.0;
    double qps = totalMutations / timeTakenSec;
    logger.info("Thread-" + this.threadIndex + ". Total Time:" + String.format("%.2f", timeTakenSec)
        + " seconds. Total mutations:" + totalMutations + ". QPS:" + String.format("%.2f", qps));
  }
}