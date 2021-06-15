package com.kayzen.impcount.executor.dataprocessor;

import com.applift.platform.commons.enums.Environment;
import com.applift.platform.commons.executor.BaseExecutor;
import com.kayzen.impcount.model.SharedDataObject;
import com.kayzen.impcount.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

public class BatchDataProcessorExecutor implements Runnable, BaseExecutor {

  private static Logger logger = Logger.getLogger(BatchDataProcessorExecutor.class.getName());
  private int numberOfWriteThreads;
  private Environment environment;
  private String dbHostName;
  private int batchSize;
  private List<BatchDataProcessor> batchDataProcessors;
  private ExecutorService executor;

  public BatchDataProcessorExecutor(int numberOfWriteThreads, Environment env, String dbHostName,
      int batchSize) {
    this.numberOfWriteThreads = numberOfWriteThreads;
    this.environment = env;
    this.batchSize = batchSize;
    this.dbHostName = dbHostName;
    this.batchDataProcessors = new ArrayList<>();
  }

  @Override
  public void run() {
    long startTime = System.currentTimeMillis();
    SharedDataObject.setKeepReadingQueues(true);
    executor = Executors.newFixedThreadPool(numberOfWriteThreads);
    for (int i = 0; i < numberOfWriteThreads; i++) {
      logger.info("Starting DataProcessorExecutorOpt thread:" + i);
      BatchDataProcessor batchDataProcessor = new BatchDataProcessor(i, this.environment,
          this.dbHostName, this.batchSize);
      executor.execute(batchDataProcessor);
      batchDataProcessors.add(batchDataProcessor);
    }

    executor.shutdown(); // Disable new tasks from being submitted
    try {
      executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      logger.error("BatchDataProcessorExecutor interrupted", e);
    }
    //TODO-dgpatil-stats comment stat related stuff
    //SharedDataObject.printStatistics();
    logger.info("Finished all DataProcessorExecutorOpt threads.");
    long timeTaken = System.currentTimeMillis() - startTime;
    SharedDataObject.setKeepPrintingStats(false);
    logger.info("BatchDataProcessorExecutorOpt: Total Time taken:" + timeTaken);
  }

  public void shutdown() {
    logger.info("BatchDataProcessorExecutor: Received shutdown signal ");
    for (BatchDataProcessor batchDataProcessor : batchDataProcessors) {
      if (batchDataProcessor != null) {
        batchDataProcessor.shutdown();
      }
    }
    try {
      executor.awaitTermination(Constants.FIVE_MINUTES_MILLIS, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      logger.error("BatchDataProcessorExecutor interrupted", e);
    }
    for (BatchDataProcessor batchDataProcessor : batchDataProcessors) {
      if (batchDataProcessor != null) {
        batchDataProcessor.shutdown();
      }
    }
    logger.info("BatchDataProcessorExecutor: Ended shutdown ");
  }

}