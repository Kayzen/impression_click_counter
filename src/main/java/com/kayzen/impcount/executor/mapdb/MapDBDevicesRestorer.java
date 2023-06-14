package com.kayzen.impcount.executor.mapdb;

import com.applift.platform.commons.db.DBContext;
import com.applift.platform.commons.db.MySqlDatabase;
import com.applift.platform.commons.enums.Environment;
import com.kayzen.impcount.model.SharedDataObject;
import com.kayzen.impcount.utils.DeviceIterator;
import com.kayzen.impcount.utils.MySqlDbUtils;
import java.io.Serializable;
import net.openhft.chronicle.map.ChronicleMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MapDBDevicesRestorer implements Serializable, Runnable, MapDBBaseRestorer {

  private static final long serialVersionUID = -863674547031919840L;
  private static Logger logger = LoggerFactory.getLogger(MapDBDevicesRestorer.class.getName());
  private int threadIndex;
  private Environment environment;
  private String dbHostName;
  private int batchSize;
  private MySqlDatabase database;
  private long partitionCounter;

  private boolean shutdown;
  private DeviceIterator deviceIterator;
  private ChronicleMap<String, Long> deviceMap;
  private long batchStartTime;
  private long lastCommitTimeMs;

  MapDBDevicesRestorer(int threadIndex, Environment environment, String dbHostName, int batchSize) {
    try {
      this.threadIndex = threadIndex;
      this.environment = environment;
      this.dbHostName = dbHostName;
      this.batchSize = batchSize;
      this.deviceMap = SharedDataObject.deviceMaps.get(threadIndex);
      DBContext dbContext = new DBContext(environment,dbHostName, LoggerFactory.getLogger(MapDBDevicesRestorer.class));
      this.database = new MySqlDatabase(dbContext);
    } catch (Exception e) {
      logger.error("Exception occurred in constructor DataProcessor. Exception:", e);
      System.exit(1);
    }
    logger.info("Starting thread with threadIndex:" + threadIndex + ", partitionCounter:"
        + this.partitionCounter);
  }

  @Override
  public void run() {
    try {
      batchStartTime = System.currentTimeMillis();
      deviceIterator = new DeviceIterator(database, threadIndex, batchSize);
      long mysqlCounter = MySqlDbUtils.getPartitionCounterFromDB(database, threadIndex);
      while (!shutdown && deviceIterator.next()) {
        partitionCounter++;
        deviceMap.put(deviceIterator.getSha1(), deviceIterator.getDeviceId());
        if (partitionCounter % batchSize == 0) {
          SharedDataObject.setThreadPartitionCounterOpt(this.threadIndex, this.partitionCounter);
          logger.warn("Updating MapDB Data for threadIndex:" + threadIndex + " mapDB at:"
              + partitionCounter
              + " MySQLCounter at:" + mysqlCounter);
        }
      }
    } catch (Exception e) {
      logger.error("Exception occurred while processing DataProcessor. Exception:", e);
    } finally {
      logger.info("Ending thread with threadIndex:" + threadIndex + ", partitionCounter:"
          + this.partitionCounter);
      cleanUp();
    }
  }

  /*
  private void updateStats(int count) {
    long batchTimeTaken = System.currentTimeMillis() - batchStartTime;
    SharedDataObject.updateStatistics(0, 0, 0, 0, 0, 0, count, batchTimeTaken);
    batchStartTime = System.currentTimeMillis();
  }
*/

  private void cleanUp() {
    try {
      if (database != null) {
        database.close();
      }
      if(deviceIterator!=null){
        deviceIterator.close();
      }
      SharedDataObject.setThreadPartitionCounterOpt(this.threadIndex, this.partitionCounter);
    } catch (Exception e) {
      logger.error("Thread-" + this.threadIndex + ". Error in executing database.close()", e);
    }
  }

  public void shutdown() {
    shutdown = true;
  }
}
