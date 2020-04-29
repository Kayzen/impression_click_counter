package com.kayzen.impcount.executor.logreader;

import com.applift.platform.commons.executor.BaseExecutor;
import com.applift.platform.commons.utils.Config;
import com.kayzen.impcount.model.SharedDataObject;
import com.kayzen.impcount.utils.Constants;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogReaderExecutor implements Runnable, BaseExecutor {

  private static Logger logger = LoggerFactory.getLogger(LogReaderExecutor.class.getName());
  private String logLocation;
  private List<File> files;
  private final Pattern pattern;
  private int numberOfReadThreads;
  private int numberOfWriteThreads;
  private Map<String, LogFileReader> logFileReaders;
  private ExecutorService executor;
  private List<Integer> supportedCamTypes;

  public LogReaderExecutor(int numberOfReadThreads, int numberOfWriteThreads, String logLocation,
      Pattern pattern,List<Integer> supportedCamTypes) {
    this.numberOfReadThreads = numberOfReadThreads;
    this.logLocation = logLocation;
    this.pattern = pattern;
    this.numberOfWriteThreads = numberOfWriteThreads;
    this.logFileReaders = new HashMap<>();
    this.supportedCamTypes = supportedCamTypes;
  }

  @Override
  public void run() {
    long startTime = System.currentTimeMillis();
    getFileListToRead();
    executor = Executors.newFixedThreadPool(numberOfReadThreads);
    int threadIndex = 0;
    for (File file : this.files) {
      String fileName = file.toString();
      logger.info("Found file:" + fileName);
      LogFileReader logFileReader = new LogFileReader(fileName, threadIndex, numberOfWriteThreads,supportedCamTypes);
      executor.execute(logFileReader);
      logFileReaders.put(fileName, logFileReader);
      threadIndex++;
    }
    executor.shutdown(); // Disable new tasks from being submitted
    try {
      executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      logger.error("BatchDataProcessorExecutor interrupted", e);
    }
    logger.info("Finished all LogFileReader threads.");
    long timeTaken = System.currentTimeMillis() - startTime;
    logger.info("LogReaderExecutorService: Total Time taken:" + timeTaken);
  }

  private void getFileListToRead() {
    FilenameFilter filter = (dir, name) -> {
      boolean isCorrectFile = pattern.matcher(name).find();
      if (isCorrectFile) {
        String fileName = dir.getAbsolutePath() + "/" + name;
        if (fileName.contains("mapDb") || fileName.contains("req-acc")) {
          return false;
        }
        logger.info("Found log file : " + fileName);
        return true;
      }
      return false;
    };
    this.files = new ArrayList<>();
    String filePath = logLocation;
    logger.info("filePath:" + filePath);
    File[] matches = new File(filePath).listFiles(filter);
    if (matches != null && matches.length > 0) {
      this.files.addAll(Arrays.asList(matches));
    }
    logger.info(files.toString());
  }

  public void shutdown() {
    logger.info("Received shutdown signal ");
    for (LogFileReader logFileReader : logFileReaders.values()) {
      if (logFileReader != null) {
        logFileReader.shutdown();
      }
    }

    try {
      executor.awaitTermination(Constants.TWO_SECONDS_MILLS, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      logger.error("LogReaderExecutor interrupted", e);
    }
  }
}
