package com.kayzen.impcount.executor.logreader;

import com.applift.platform.commons.utils.Config;
import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.kayzen.impcount.model.ImpressionObject;
import com.kayzen.impcount.model.ImpressionObjectProvider;
import com.kayzen.impcount.model.SharedDataObject;
import com.kayzen.impcount.utils.Constants;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogFileReader implements Runnable {

  private static Logger logger = LoggerFactory.getLogger(LogFileReader.class.getName());
  private static HashFunction hf = Hashing.murmur3_128();
  private long keyHashed;
  private String fileToRead;
  private int threadIndex;
  private int queueIndex;
  private int numberOfWriteThreads;
  private int count;
  private long lastCheckTimeMs;
  private List<Integer> supportedCamTypes;


  private volatile boolean shutdown = false;

  public LogFileReader(String fileToRead, int threadIndex, int numberOfWriteThreads,
      List<Integer> supportedCamTypes) {
    this.fileToRead = fileToRead;
    this.threadIndex = threadIndex;
    this.keyHashed = 0L;
    lastCheckTimeMs = System.currentTimeMillis();
    this.count = 0;
    this.queueIndex = 0;
    this.numberOfWriteThreads = numberOfWriteThreads;
    this.supportedCamTypes = supportedCamTypes;
  }

  public void shutdown() {
    logger.info("Received shutdown signal for:" + fileToRead);
    shutdown = true;
  }

  @Override
  public void run() {
    while (!shutdown) {
      try {
        logger.info(this.threadIndex + " .Processing file:" + this.fileToRead);
        processFile(this.fileToRead);
      } catch (Exception e) {
        logger.error("Exception occurred while processing LogFileReader.", e);
      }
    }
    logger.info("-Thread-" + this.threadIndex + ". Done. Total Processed:" + this.count
        + ". This thread will shutdown.");
  }

  private void processFile(String fileName) {
    try {
      FileInputStream inputStream = null;
      Scanner sc = null;
      try {
        inputStream = new FileInputStream(fileName);
        sc = new Scanner(inputStream, "UTF-8");
        while (sc.hasNextLine() && !shutdown) {
          String line = sc.nextLine();
          if (!line.isEmpty() && !line.startsWith("#")) {
            this.count++;
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis > lastCheckTimeMs + Constants.SIXTY_SECONDS_MILLIS) {
              lastCheckTimeMs = System.currentTimeMillis();
              logger.info(this.threadIndex + " .File-" + this.fileToRead + " .Processed so far:" + count);
            }
            processLine(line);
          }
        }
        // note that Scanner suppresses exceptions
        if (sc.ioException() != null) {
          throw sc.ioException();
        }
      } finally {
        if (inputStream != null) {
          inputStream.close();
        }
        if (sc != null) {
          sc.close();
        }
      }
      logger.info(this.threadIndex + " .Finished file .Processed so far:" + count);
      logger.info(this.threadIndex + ". Shutting down thread");
      shutdown();
    } catch (IOException e) {
      logger.info(this.threadIndex + " .IOException occurred in processFile", e);
    }
  }

  private void processLine(String line) {
    try {
      if (line != null && !line.isEmpty()) {
        ImpressionObject fCapObject = ImpressionObjectProvider
            .getFCapDataObject(line, 0, 0);
        if (fCapObject != null) {
          if (supportedCamTypes.contains(fCapObject.getCampaignType())) {
            if (fCapObject.getDeviceIdSha1() != null && !fCapObject.getDeviceIdSha1().isEmpty()) {
              try {
                keyHashed = hf.newHasher().putString(fCapObject.getDeviceIdSha1(), Charsets.UTF_8)
                    .hash().asLong();
                queueIndex = (int) (keyHashed % this.numberOfWriteThreads);
                SharedDataObject.sharedObjectQueues.get(Math.abs(queueIndex)).put(fCapObject);
              } catch (InterruptedException e) {
                logger
                    .error(this.threadIndex + " .InterruptedException occurred in sharedBQueue.put()",
                        e);
              }
            } else {
              // If no device id sha1, nothing to do here
              return;
            }
          } else {
            //If campaign type not supported
            return;
          }
        } else {
          // Object formed is null
          return;
        }
      }
    } catch (Exception e) {
      logger.error(this.threadIndex + " .Exception occurred while creating DataObject ", e);
    }
  }
}