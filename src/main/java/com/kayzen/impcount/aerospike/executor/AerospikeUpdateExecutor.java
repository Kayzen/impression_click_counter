package com.kayzen.impcount.aerospike.executor;

import com.applift.platform.commons.db.DBContext;
import com.applift.platform.commons.db.MySqlDatabase;
import com.applift.platform.commons.enums.Environment;
import com.applift.platform.commons.executor.BaseExecutor;
import com.applift.platform.commons.utils.Config;
import com.kayzen.impcount.model.SharedDataObject;
import com.kayzen.impcount.utils.Constants;
import com.kayzen.impcount.utils.Datacenter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

public class AerospikeUpdateExecutor implements Runnable, BaseExecutor {

  private static Logger logger = Logger.getLogger(AerospikeUpdateExecutor.class.getName());
  private Environment environment;
  private String dbHostName;
  private int batchSize;
  private List<AerospikeUpdateProcessor> aerospikeUpdateProcessors;
  private ExecutorService executor;
  private Config aeroConfig;
  private int numberOfWriteThreads;

  public AerospikeUpdateExecutor(Environment environment, String dbHostName, int batchSize,
    Config aeroConfig, int numberOfWriteThreads) {
    this.environment = environment;
    this.batchSize = batchSize;
    this.dbHostName = dbHostName;
    this.aeroConfig = aeroConfig;
    this.aerospikeUpdateProcessors = new ArrayList<>();
    this.numberOfWriteThreads = numberOfWriteThreads;
  }

  @Override
  public void run() {
    long startTime = System.currentTimeMillis();
    SharedDataObject.setKeepReadingQueues(true);
    executor = Executors.newFixedThreadPool(numberOfWriteThreads * Datacenter.values().length);
    for (int i = 0; i < numberOfWriteThreads; i++) {
      MySqlDatabase database = null;
      try {
        DBContext dbContext = new DBContext(environment,dbHostName, LoggerFactory.getLogger(AerospikeUpdateProcessor.class));
        database = new MySqlDatabase(dbContext);
      } catch (Exception e) {
        logger.error("Exception occurred during DB connection . Exception:", e);
        System.exit(1);
      }
      for (Datacenter dc : Datacenter.values()) {
        logger.info("Starting AerospikeUpdateExecutor thread:" + i);
        AerospikeUpdateProcessor aerospikeUpdateProcessor = new AerospikeUpdateProcessor(database,
            batchSize, aeroConfig, i, dc);
        executor.execute(aerospikeUpdateProcessor);
        aerospikeUpdateProcessors.add(aerospikeUpdateProcessor);

      }
    }
    executor.shutdown(); // Disable new tasks from being submitted
    try {
      executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      logger.error("AerospikeUpdateExecutor interrupted", e);
    }
    logger.info("Finished all AerospikeUpdateExecutor threads.");
    long timeTaken = System.currentTimeMillis() - startTime;
    logger.info("AerospikeUpdateExecutor: Total Time taken:" + timeTaken);
  }

  public void shutdown() {
    logger.info("AerospikeUpdateExecutor: Received shutdown signal ");
    for (AerospikeUpdateProcessor aerospikeUpdateProcessor : aerospikeUpdateProcessors) {
      if (aerospikeUpdateProcessor != null) {
        aerospikeUpdateProcessor.shutdown();
      }
    }
    try {
      executor.awaitTermination(Constants.SIXTY_SECONDS_MILLIS, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      logger.error("AerospikeUpdateExecutor interrupted", e);
    }
    logger.info("AerospikeUpdateExecutor: Ended shutdown ");
  }

}