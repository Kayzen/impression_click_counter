package com.kayzen.impcount.model;

import com.kayzen.impcount.utils.Utils;
import com.kayzen.impcount.utils.Constants;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SharedDataObject {

  private static Logger logger = LoggerFactory.getLogger(SharedDataObject.class.getName());
  public static List<LinkedBlockingQueue<ImpressionObject>> sharedObjectQueues;

  public static boolean keepReadingQueues;
  public static boolean keepPrintingStats;
  public static boolean enableKafkaOffsets;
  public static ConcurrentHashMap<String, Long> statistics;
  public static List<List<Long>> kafkaOffsetPerProcessorBatch;
  public static List<List<Long>> kafkaOffsetPerProcessor;
  private static int numberOfWriteThreads;
  public static int kafkaConsumerCount;


  public static ChronicleMap<Integer, Long> threadPartitionMap;

  public static List<ChronicleMap<String, Long>> deviceMaps;

  public static ChronicleMap<String, Integer> impBidsMap;


  public static void init(int size, String mapLocation) {
    try {
      logger.info("Calling init()");
      numberOfWriteThreads = size;
      sharedObjectQueues = new ArrayList<>();
      for (int i = 0; i < size; i++) {
        sharedObjectQueues
            .add(i, new LinkedBlockingQueue<ImpressionObject>(Constants.SHARED_OBJECT_QUEUE_SIZE));
      }

      Map<Integer, Future<ChronicleMap<String, Long>>> deviceMapsFutureHMap = new ConcurrentHashMap<>();
      ExecutorService executor = Executors.newFixedThreadPool(16);

      for (int i = 0; i < size; i++) {
        File deviceMapFile = new File(
            mapLocation + Constants.DEVICE_MAP_THREAD + i + Constants.MAP);
        ChronicleMapLoader<String, Long> chronicleMapLoader = new ChronicleMapLoader<String , Long>(
            deviceMapFile, "device-map-" + i, 40, Constants.FIFTY_MILLION){};
        deviceMapsFutureHMap.put(i, executor.submit(chronicleMapLoader));
      }

      Utils.removeFile(mapLocation + Constants.IMP_BIDS_MAP_FILE.replaceAll(
          "REPLACE_DATE", Utils.getCurrentDate(System.currentTimeMillis() - Constants.ONE_DAY_MILLS)));
      File impBidsMapFile = new File(
          mapLocation + Constants.IMP_BIDS_MAP_FILE.replaceAll(
              "REPLACE_DATE", Utils.getCurrentDate(System.currentTimeMillis())));
      ChronicleMapLoader<String, Integer> impChronicleMapLoader = new ChronicleMapLoader<String, Integer>(
          impBidsMapFile, "imp-bids-map", 100, Constants.TWO_HUNDRED_MILLION){};
      Future<ChronicleMap<String, Integer>> impBidsFutureMap = executor.submit(impChronicleMapLoader);

      executor.shutdown(); // Disable new tasks from being submitted

      deviceMaps = new ArrayList<>();
      for (int i = 0; i < size; i++) {
        if (deviceMapsFutureHMap.get(i) == null) {
          logger.error("Error loading chronicle map : " + i + " : Exiting...");
          System.exit(1);
        }
        ChronicleMap<String, Long> deviceMap = deviceMapsFutureHMap.get(i).get();
        if (deviceMap == null) {
          logger.error("Error loading device chronicle map " + i + " : Exiting...");
          System.exit(1);
        }
        logger.info("Loaded device map : " + i);
        deviceMaps.add(deviceMap);
      }

      impBidsMap = impBidsFutureMap.get();
      logger.info("Loaded imp map");
      if (impBidsMap == null) {
        logger.error("Error loading imp chronicle map : Exiting...");
        System.exit(1);
      }

      //TODO-dgpatil-stats comment stat related stuff
      /*
      keepPrintingStats = true;
      statistics = new ConcurrentHashMap<>();
      statistics.put(Constants.STATS_TOTAL, 0L);
      statistics.put(Constants.STATS_TOTAL_DEVICES, 0L);
      statistics.put(Constants.STATS_TOTAL_CAMPAIGN_IMPRESSIONS, 0L);
      statistics.put(Constants.STATS_TOTAL_LINEITEM_IMPRESSIONS, 0L);
      statistics.put(Constants.STATS_TOTAL_CAMPAIGN_CLICKS, 0L);
      statistics.put(Constants.STATS_TOTAL_LINEITEM_CLICKS, 0L);
      statistics.put(Constants.STATS_TOTAL_CAMPAIGN_CONVERSIONS, 0L);
      statistics.put(Constants.STATS_TOTAL_LINEITEM_CONVERSIONS, 0L);
      statistics.put(Constants.STATS_TOTAL_AEROSPIKE_DEVICE_DATA, 0L);
      statistics.put(Constants.STATS_EXECUTE_BATCH_TIME, 0L);
       */

      kafkaOffsetPerProcessorBatch = new ArrayList<>(size);
      kafkaOffsetPerProcessor = new ArrayList<>(size);
      File threadPartitionMapFile = new File(mapLocation + Constants.THREAD_PARTITION_MAP_FILE);
      threadPartitionMap = ChronicleMapBuilder
          .of(Integer.class, Long.class)
          .name("thread-partition-map")
          .entries(100)
          .createOrRecoverPersistedTo(threadPartitionMapFile);

    } catch (Exception e) {
      logger.error("Failed in init()", e);
      System.exit(1);
    }
    logger.info("Done init()");
  }

  public static void setKeepReadingQueues(boolean keepReadingQueues) {
    SharedDataObject.keepReadingQueues = keepReadingQueues;
  }

  public static void setKeepPrintingStats(boolean keepPrintingStats) {
    SharedDataObject.keepPrintingStats = keepPrintingStats;
  }

  //TODO-dgpatil-stats comment stat related stuff
  /*
  public static void updateStatistics(int cImpressions, int lImpressions, int cClicks, int lClicks,
                                      int cConversions, int lConversions, int devices, long timeTaken) {
    long value = 0;
    long total = 0;

    value = statistics.get(Constants.STATS_TOTAL_CAMPAIGN_IMPRESSIONS) + cImpressions;
    statistics.put(Constants.STATS_TOTAL_CAMPAIGN_IMPRESSIONS, value);
    total += cImpressions;

    value = statistics.get(Constants.STATS_TOTAL_LINEITEM_IMPRESSIONS) + lImpressions;
    statistics.put(Constants.STATS_TOTAL_LINEITEM_IMPRESSIONS, value);
    total += lImpressions;

    value = statistics.get(Constants.STATS_TOTAL_CAMPAIGN_CLICKS) + cClicks;
    statistics.put(Constants.STATS_TOTAL_CAMPAIGN_CLICKS, value);
    total += cClicks;

    value = statistics.get(Constants.STATS_TOTAL_LINEITEM_CLICKS) + lClicks;
    statistics.put(Constants.STATS_TOTAL_LINEITEM_CLICKS, value);
    total += lClicks;

    value = statistics.get(Constants.STATS_TOTAL_CAMPAIGN_CONVERSIONS) + cConversions;
    statistics.put(Constants.STATS_TOTAL_CAMPAIGN_CONVERSIONS, value);
    total += cConversions;

    value = statistics.get(Constants.STATS_TOTAL_LINEITEM_CONVERSIONS) + lConversions;
    statistics.put(Constants.STATS_TOTAL_LINEITEM_CONVERSIONS, value);
    total += lConversions;

    value = statistics.get(Constants.STATS_TOTAL_DEVICES) + devices;
    statistics.put(Constants.STATS_TOTAL_DEVICES, value);
    total += devices;

    value = statistics.get(Constants.STATS_TOTAL) + total;
    statistics.put(Constants.STATS_TOTAL, value);

    value = statistics.get(Constants.STATS_EXECUTE_BATCH_TIME) + timeTaken;
    statistics.put(Constants.STATS_EXECUTE_BATCH_TIME, value);
  }
*/
  public static void close() {
    logger.info("Inside SharedDataObjectOpt.close()");
    //TODO-dgpatil-stats comment stat related stuff
    //printStatistics();
    threadPartitionMap.close();

    for (int index = 0; index < deviceMaps.size(); index++) {
      closeDeviceMap(index);
    }

    impBidsMap.close();
    logger.info("Done executing SharedDataObjectOpt.close()");
  }

  public static long getThreadPartitionCounterOpt(int threadIndex) {
    if (!SharedDataObject.threadPartitionMap.containsKey(threadIndex)) {
      SharedDataObject.threadPartitionMap.put(threadIndex, 0L);
      return 0;
    } else {
      return SharedDataObject.threadPartitionMap.get(threadIndex);
    }
  }

  public static void setThreadPartitionCounterOpt(int threadIndex, long partitionCounter) {
    SharedDataObject.threadPartitionMap.put(threadIndex, partitionCounter);
  }

  private static void closeDeviceMap(int index) {
    deviceMaps.get(index).close();
  }

  public static void printStatistics() {
    logger.info(statistics.toString());
  }

  public static void setKafkaOffset(int threadIndex, List<Long> batchKafkaOffset) {
    for (int i = 0; i < kafkaConsumerCount; i++) {
      kafkaOffsetPerProcessorBatch.get(threadIndex).set(i, batchKafkaOffset.get(i));
    }
  }

  public static void setEnableKafkaOffsets(boolean enableKafkaOffsets) {
    SharedDataObject.enableKafkaOffsets = enableKafkaOffsets;
  }

  public static void initializeKafkaOffsets(int kafkaConsumerCount) {
    SharedDataObject.kafkaConsumerCount = kafkaConsumerCount;
    for (int i = 0; i < numberOfWriteThreads; i++) {
      List<Long> arr1 = new ArrayList<>(kafkaConsumerCount);
      List<Long> arr2 = new ArrayList<>(kafkaConsumerCount);
      for (int j = 0; j < kafkaConsumerCount; j++) {
        arr1.add(0L);
        arr2.add(0L);
      }
      kafkaOffsetPerProcessorBatch.add(arr1);
      kafkaOffsetPerProcessor.add(arr2);
    }
  }
}