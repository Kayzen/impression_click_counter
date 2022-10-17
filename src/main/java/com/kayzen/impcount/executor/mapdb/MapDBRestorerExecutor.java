package com.kayzen.impcount.executor.mapdb;

import com.applift.platform.commons.enums.Environment;
import com.applift.platform.commons.executor.BaseExecutor;
import com.kayzen.impcount.model.SharedDataObject;
import com.kayzen.impcount.utils.Constants;
import com.kayzen.impcount.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MapDBRestorerExecutor implements Runnable, BaseExecutor {

  private static final int NUMBER_OF_NON_DEVICE_THREADS = 2;
  private static Logger logger = LoggerFactory.getLogger(MapDBRestorerExecutor.class.getName());
  private int numberOfWriteThreads;
  private Environment environment;
  private String dbHostName;
  private int batchSize;
  private boolean processOnlyDevices;
  private List<MapDBBaseRestorer> mapDBRestorers;
  private ExecutorService executor;

  public MapDBRestorerExecutor(int numberOfWriteThreads, Environment environment, String dbHostName,
      int batchSize, boolean processOnlyDevices) {
    this.numberOfWriteThreads = numberOfWriteThreads;
    this.environment = environment;
    this.batchSize = batchSize;
    this.dbHostName = dbHostName;
    this.processOnlyDevices = processOnlyDevices;
    this.mapDBRestorers = new ArrayList<>();
  }

  @Override
  public void run() {
    long startTime = System.currentTimeMillis();
    SharedDataObject.setKeepReadingQueues(true);
    if (processOnlyDevices) {
      executor = Executors.newFixedThreadPool(numberOfWriteThreads);
    } else {
      executor = Executors.newFixedThreadPool(numberOfWriteThreads + NUMBER_OF_NON_DEVICE_THREADS);
      addNonDeviceRestorers();
    }
    addDeviceRestorers();

    executor.shutdown(); // Disable new tasks from being submitted
    try {
      executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      logger.error("MapDBRestorerExecutor interrupted", e);
    }
    //SharedDataObject.printStatistics();
    //SharedDataObject.setKeepPrintingStats(false);
    logger.info("Finished all MapDBRestorerExecutor threads.");
    long timeTaken = System.currentTimeMillis() - startTime;
    logger.info("MapDBRestorerExecutor: Total Time taken: " + Utils.printDuration(timeTaken));
  }

  private void addDeviceRestorers() {
    for (int i = 0; i < numberOfWriteThreads; i++) {
      logger.info("Starting MapDBDevicesRestorer thread:" + i);
      MapDBDevicesRestorer mapDBDevicesRestorer = new MapDBDevicesRestorer(i, this.environment,
          this.dbHostName, this.batchSize);
      executor.execute(mapDBDevicesRestorer);
      mapDBRestorers.add(mapDBDevicesRestorer);
    }
  }

  public void shutdown() {
    logger.info("MapDBRestorerExecutor: Received shutdown signal ");
    for (MapDBBaseRestorer mapDBDevicesRestorer : mapDBRestorers) {
      if (mapDBDevicesRestorer != null) {
        mapDBDevicesRestorer.shutdown();
      }
    }
    try {
      executor.awaitTermination(Constants.SIXTY_SECONDS_MILLIS, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      logger.error("MapDBRestorerExecutor interrupted", e);
    }
    logger.info("MapDBRestorerExecutor: Ended shutdown ");
  }

  private void addNonDeviceRestorers() {
    return;
  }

}