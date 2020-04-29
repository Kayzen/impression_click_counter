package com.kayzen.impcount.launcher;


//TODO-dgpatil-stats uncomment if required
//import com.applift.fcap.utils.StatisticsPrinter;
import com.applift.platform.commons.enums.Environment;
import com.applift.platform.commons.executor.BaseExecutor;
import com.applift.platform.commons.launcher.AbstractLauncher;
import com.applift.platform.commons.utils.Config;
import com.kayzen.impcount.executor.dataprocessor.BatchDataProcessorExecutor;
import com.kayzen.impcount.executor.kafka.KafkaSubscriberExecutorService;
import com.kayzen.impcount.executor.logreader.LogReaderExecutor;
import com.kayzen.impcount.model.SharedDataObject;
import com.kayzen.impcount.utils.Constants;
import com.kayzen.impcount.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImpCounterBatchIngestor extends AbstractLauncher {

  private static Logger logger = LoggerFactory.getLogger(ImpCounterBatchIngestor.class.getName());
  private static List<Thread> threads = new ArrayList<>();
  private static List<BaseExecutor> executors = new ArrayList<>();
  private static long startTimeMillis;
  private static boolean shutdown = false;

  public static void main(String[] args) {
    try {
      startTimeMillis = System.currentTimeMillis();
      options = new Options();
      options.addOption("ll", Constants.LOG_LOCATION, true, "Location of log files for reading.");
      options.addOption("lp", Constants.LOG_PATTERN, true, "Pattern of log files for reading.");
      options.addOption("env", Constants.ENVIRONMENT, true, "Choices: staging|production");
      options.addOption("bs", Constants.BATCH_SIZE, true, "Choices: true|false");
      options.addOption("ml", Constants.MAP_LOCATION, true, "");
      options.addOption("di", Constants.DATA_INGESTION_METHOD, true, "Choices: logs|kafka");
      options.addOption("ct", Constants.CAMPAIGN_TYPE, true, "Required campaign types cpi,cpc,cpm");

      // Validate the required inputs are missing or not.
      mandatoryParams = new String[]{Constants.ENVIRONMENT, Constants.BATCH_SIZE,
          Constants.MAP_LOCATION,Constants.DATA_INGESTION_METHOD,Constants.CAMPAIGN_TYPE};
      parseOptions(args);

      Environment environment = validateAndGetEnvironment();

      String mapLocation = cmdLine.getOptionValue(Constants.MAP_LOCATION);
      int batchSize = Integer.parseInt(cmdLine.getOptionValue(Constants.BATCH_SIZE));
      List<String> supportedCamTypes = Utils.addToArrayList(cmdLine.getOptionValue(Constants.CAMPAIGN_TYPE));
      List <Integer> supportedCamTypesInt = Utils.getsupportedTypesInt(supportedCamTypes);

      Config config = Config.getConfig(Constants.APPLICATION_CONF,environment);

      int numberOfReadThreads = config.getInt(Constants.NUMBER_OF_READ_THREADS);
      int numberOfWriteThreads = config.getInt(Constants.NUMBER_OF_WRITE_THREADS);
      String dbHostName = config.getString(Constants.DB_HOST_NAME);
      Utils.setLogger("",Constants.IMPRESSION_COUNTER);
      SharedDataObject.init(numberOfWriteThreads, mapLocation);

      //CODE BLOCK FOR DATA INGESTION
      String dataIngestionMethod = cmdLine.getOptionValue(Constants.DATA_INGESTION_METHOD);
      switch (dataIngestionMethod) {
        case "logs":
          String logPattern = cmdLine.getOptionValue(Constants.LOG_PATTERN);
          String logLocation = cmdLine.getOptionValue(Constants.LOG_LOCATION);
          startLogReader(logPattern, logLocation, numberOfReadThreads,
              numberOfWriteThreads,supportedCamTypesInt);
          break;
        case "kafka":
          startKafkaSubscriber(environment, numberOfReadThreads,
              numberOfWriteThreads,supportedCamTypesInt);
          break;
        default:
          System.err.println("Invalid dataIngestionMethod choose 'logs' or 'kafka'");
          printHelpAndExit();
      }

      startBatchProcessing(environment, numberOfWriteThreads, dbHostName, batchSize);

      //TODO-dgpatil-stats uncomment if required
      //startStatsPrinter(numberOfWriteThreads);

      addShutDownHook();

      for (Thread thread : threads) {
        thread.join();
      }

    } catch (Exception e) {
      logger.error("Exception in FPABatchIngestor main method. Exception:" + e);
    }
  }

  private static void addShutDownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      shutdown = true;
      for (BaseExecutor executor : executors) {
        executor.shutdown();
      }
      //TODO-dgpatil-stats uncomment if required
      //printFinalStats();
      SharedDataObject.close();
    }));
  }

  //TODO-dgpatil-stats uncomment if required
  /*
  private static void printFinalStats() {
    long timeTaken = System.currentTimeMillis() - startTimeMillis;
    double timeTakenSec = timeTaken / 1000.0;
    long mutations = SharedDataObject.statistics.get(Constants.STATS_TOTAL);
    double qps = mutations / timeTakenSec;
    logger.info("Total Time:" + String.format("%.2f", timeTakenSec) + " seconds. Total mutations:"
        + mutations
        + ". Effective QPS:" + String.format("%.2f", qps));
  }
   */

  private static void startBatchProcessing(Environment env, int numberOfWriteThreads,
      String dbHostName, int batchSize) {
    BatchDataProcessorExecutor dataProcessorExecutor = new BatchDataProcessorExecutor(
        numberOfWriteThreads,
        env, dbHostName, batchSize);
    Thread dataProcessorExecutorThread = new Thread(dataProcessorExecutor);
    threads.add(dataProcessorExecutorThread);
    executors.add(dataProcessorExecutor);
    dataProcessorExecutorThread.setDaemon(true);
    dataProcessorExecutorThread.start();
  }

  private static void startKafkaSubscriber(Environment env, int numberOfReadThreads,
      int numberOfWriteThreads,List<Integer> supportedCamTypes) throws Exception {
    Config config = Config.getConfig(Constants.APPLICATION_CONF,env);
    KafkaSubscriberExecutorService kafkaSubscriberExecutorService = new KafkaSubscriberExecutorService(
        config, numberOfWriteThreads, numberOfReadThreads,supportedCamTypes);
    Thread kafkaReaderExecutorThread = new Thread(kafkaSubscriberExecutorService);
    threads.add(kafkaReaderExecutorThread);
    executors.add(kafkaSubscriberExecutorService);
    kafkaReaderExecutorThread.setDaemon(true);
    kafkaReaderExecutorThread.start();
  }

  private static void startLogReader(String logPattern, String logLocation,
      int numberOfReadThreads, int numberOfWriteThreads,List<Integer> supportedCamTypes) throws Exception {
    if (logPattern == null || logLocation == null) {
      printHelpAndExit();
    }
    Pattern pattern = Pattern.compile(logPattern);
    LogReaderExecutor logReaderExecutor = new LogReaderExecutor(numberOfReadThreads,
        numberOfWriteThreads, logLocation, pattern,supportedCamTypes);
    Thread logReaderExecutorThread = new Thread(logReaderExecutor);
    threads.add(logReaderExecutorThread);
    executors.add(logReaderExecutor);
    logReaderExecutorThread.setDaemon(true);
    logReaderExecutorThread.start();
  }

//TODO-dgpatil-stats uncomment if required
  /*
  private static void startStatsPrinter(int numberOfWriteThreads) {
    StatisticsPrinter statisticsPrinter = new StatisticsPrinter(numberOfWriteThreads);
    Thread statisticsPrinterThread = new Thread(statisticsPrinter);
    statisticsPrinterThread.setDaemon(true);
    threads.add(statisticsPrinterThread);
    statisticsPrinterThread.start();
  }
   */
}
