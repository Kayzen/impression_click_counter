package com.kayzen.impcount.launcher;

import com.applift.platform.commons.enums.Environment;
import com.applift.platform.commons.executor.BaseExecutor;
import com.applift.platform.commons.launcher.AbstractLauncher;
import com.applift.platform.commons.utils.Config;
import com.kayzen.impcount.aerospike.executor.AerospikeUpdateExecutor;
import com.kayzen.impcount.utils.Constants;
import com.kayzen.impcount.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.immutable.Stream.Cons;


public class AerospikeUpdater extends AbstractLauncher {

  private static final Logger logger = LoggerFactory.getLogger(AerospikeUpdater.class.getName());
  private static boolean shutdown;
  private static List<Thread> threads = new ArrayList<>();
  private static List<BaseExecutor> executors = new ArrayList<>();

  public static void main(String[] args) throws Exception {

    options = new Options();
    options.addOption("env", Constants.ENVIRONMENT, true, "Choices: staging|production");
    options.addOption("bs", Constants.BATCH_SIZE, true, "Choices: true|false");

    mandatoryParams = new String[]{Constants.ENVIRONMENT, Constants.BATCH_SIZE};
    parseOptions(args);

    Environment environment = validateAndGetEnvironment();
    Utils.setLogger("", Constants.AERO_IMPRESSION_COUNTER);

    Config config = Config.getConfig(Constants.AERO_APPLICATION_CONF,environment);

    String dbHostName = config.getString(Constants.DB_HOST_NAME);
    int numberOfWriteThreads = config.getInt(Constants.NUMBER_OF_WRITE_THREADS);
    int batchSize = Integer.parseInt(cmdLine.getOptionValue(Constants.BATCH_SIZE));
    shutdown = false;

    startAeroUpdater(environment, config.getSubConfig(Constants.AERO), dbHostName, batchSize, numberOfWriteThreads);
    addShutDownHook();
    //TODO-dgpatil-stats commenting stats code
    //startStatsPrinter();

    for (Thread thread : threads) {
      thread.join();
    }
  }

  private static void startAeroUpdater(Environment environment, Config aeroConfig,
    String dbHostName, int batchSize, int numberOfWriteThreads) {
    AerospikeUpdateExecutor aerospikeUpdateExecutor = new AerospikeUpdateExecutor(environment,
      dbHostName, batchSize, aeroConfig, numberOfWriteThreads);
    Thread aerospikeExecutorThread = new Thread(aerospikeUpdateExecutor);
    threads.add(aerospikeExecutorThread);
    executors.add(aerospikeUpdateExecutor);
    aerospikeExecutorThread.setDaemon(true);
    aerospikeExecutorThread.start();
  }

  private static void addShutDownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      logger.info("Shutdown Initiated");
      System.out.println("Shutdown Initiated");
      shutdown = true;
      for (BaseExecutor executor : executors) {
        executor.shutdown();
      }
      //TODO-dgpatil-stats commenting stats code
      //StatsRecorder.printStats();
    }));
  }

  //TODO-dgpatil-stats commenting stats code
  /*
  private static void startStatsPrinter() {
    Thread statisticsPrinterThread = new Thread(() -> {
      while (!shutdown) {
        try {
          Thread.sleep(Constants.FIVE_SECONDS_MILLS);
        } catch (InterruptedException ignored) {
        }
        StatsRecorder.printStats();
      }
    });
    statisticsPrinterThread.start();
  }
   */

}
