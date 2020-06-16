package com.kayzen.impcount.launcher;


import com.applift.platform.commons.enums.Environment;
import com.applift.platform.commons.executor.BaseExecutor;
import com.applift.platform.commons.launcher.AbstractLauncher;
import com.kayzen.impcount.executor.mapdb.MapDBRestorerExecutor;
import com.kayzen.impcount.model.SharedDataObject;
import com.kayzen.impcount.utils.Constants;
import com.kayzen.impcount.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.Options;

public class MapDBRestorer extends AbstractLauncher {

  private static Options options;

  private static List<Thread> threads = new ArrayList<>();
  private static List<BaseExecutor> executors = new ArrayList<>();

  public static void main(String[] args) throws Exception {
    options = new Options();
    options.addOption("wt", Constants.NUMBER_OF_WRITE_THREADS, true,
        "Number of threads to be used for writing to DB.");
    options.addOption("env", Constants.ENVIRONMENT, true, "Choices: staging|production");
    options.addOption("bs", Constants.BATCH_SIZE, true, "Choices: true|false");
    options.addOption("ml", Constants.MAP_LOCATION, true, "");
    options.addOption("dh", Constants.DB_HOST_NAME, true, "");
    options.addOption("do", Constants.PROCESS_ONLY_DEVICES, true, "Choices: true|false");

    // Validate the required inputs are missing or not.
    String[] mandatoryParams = new String[]{Constants.NUMBER_OF_WRITE_THREADS,
        Constants.ENVIRONMENT, Constants.BATCH_SIZE, Constants.MAP_LOCATION,
        Constants.DB_HOST_NAME, Constants.PROCESS_ONLY_DEVICES};

    Environment environment = validateAndGetEnvironment();

    int numberOfWriteThreads = Integer.parseInt(cmdLine.getOptionValue(
        Constants.NUMBER_OF_WRITE_THREADS));
    String mapLocation = cmdLine.getOptionValue(Constants.MAP_LOCATION);
    String dbHostName = cmdLine.getOptionValue(Constants.DB_HOST_NAME);
    int batchSize = Integer.parseInt(cmdLine.getOptionValue(Constants.BATCH_SIZE));
    boolean processOnlyDevices = Boolean
        .parseBoolean(cmdLine.getOptionValue(Constants.PROCESS_ONLY_DEVICES));
    Utils.setLogger("",Constants.MAPDB_RESTORER);
    SharedDataObject.init(numberOfWriteThreads, mapLocation);
    startMapDBRestore(environment, numberOfWriteThreads, dbHostName, batchSize, processOnlyDevices);
    //startStatsPrinter(numberOfWriteThreads);
    addShutDownHook();
    for (Thread thread : threads) {
      thread.join();
    }
  }

  private static void startMapDBRestore(Environment environment, int numberOfWriteThreads,
      String dbHostName, int batchSize, boolean processOnlyDevices) {
    MapDBRestorerExecutor mapDBRestorerExecutor = new MapDBRestorerExecutor(
        numberOfWriteThreads,
        environment, dbHostName, batchSize, processOnlyDevices);
    Thread mapDBRestorerExecutorThread = new Thread(mapDBRestorerExecutor);
    threads.add(mapDBRestorerExecutorThread);
    executors.add(mapDBRestorerExecutor);
    mapDBRestorerExecutorThread.setDaemon(true);
    mapDBRestorerExecutorThread.start();
  }

  /*
  private static void startStatsPrinter(int numberOfWriteThreads) {
    StatisticsPrinter statisticsPrinter = new StatisticsPrinter(numberOfWriteThreads);
    Thread statisticsPrinterThread = new Thread(statisticsPrinter);
    statisticsPrinterThread.setDaemon(true);
    statisticsPrinterThread.start();
    threads.add(statisticsPrinterThread);
  }
   */

  private static void addShutDownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      for (BaseExecutor executor : executors) {
        executor.shutdown();
      }
      SharedDataObject.close();
    }));
  }
}
