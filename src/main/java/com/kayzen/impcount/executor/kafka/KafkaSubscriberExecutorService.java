package com.kayzen.impcount.executor.kafka;

import com.applift.platform.commons.executor.BaseExecutor;
import com.applift.platform.commons.utils.Config;
import com.kayzen.impcount.model.SharedDataObject;
import com.kayzen.impcount.utils.Constants;
import com.kayzen.impcount.utils.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.consumer.SimpleConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaSubscriberExecutorService implements Runnable, BaseExecutor {

  private static Logger logger = LoggerFactory.getLogger(KafkaSubscriberExecutorService.class.getName());
  private ExecutorService subscriberPool;
  private Map<String, List<KafkaSubscriber>> subscribers;
  private Config kafkaConfig;
  private int numberOfWriteThreads;
  private List<String> topics;
  private List<String> brokerHosts;
  private List<Integer> brokerPorts;
  private List<Integer> supportedCamTypes;

  public KafkaSubscriberExecutorService(Config config, int numberOfWriteThreads,
      int numberOfReadThread,List<Integer> supportedCamTypes)
      throws IOException {
    logger.info("Creating KafkaSubscriberExecutorService....");
    kafkaConfig = config.getSubConfig(Constants.KAFKA);
    this.numberOfWriteThreads = numberOfWriteThreads;
    this.supportedCamTypes = supportedCamTypes;
    this.subscribers = new HashMap<>();

    this.topics = Utils.addToArrayList(kafkaConfig.getString(Constants.TOPICLIST));
    brokerHosts = Utils.addToArrayList(kafkaConfig.getString(Constants.BROKER_HOSTS));
    brokerPorts = Utils.addIntToArrayList(kafkaConfig.getString(Constants.BROKER_PORTS));
    this.subscriberPool = Executors.newFixedThreadPool(numberOfReadThread);
    SharedDataObject.setEnableKafkaOffsets(true);
  }

  public void run() {
    createAndStartAllSubscribers();
  }

  private void createAndStartAllSubscribers() {
    logger.info("createAndStartSubscribers :: start creating kafka subscriber....");
    try {
      Map<String, Integer> partitionCountPerTopic = getPartitionCountPerTopic();
      if (!validPartitionInfo(partitionCountPerTopic)) {
        logger.error("createAndStartAllSubscribers :: invalid partition info for topics");
        return;
      }
      int totalPartitions = 0;
      for (Map.Entry<String, Integer> entry : partitionCountPerTopic.entrySet()) {
        totalPartitions += entry.getValue();
      }
      logger.info("createAndStartSubscribers :: totalPartitions:" + totalPartitions);
      SharedDataObject.initializeKafkaOffsets(totalPartitions);

      int kafkaConsumerIdentifier = 0;

      for (Map.Entry<String, Integer> entry : partitionCountPerTopic.entrySet()) {
        List<KafkaSubscriber> subscriberPerTopic = new ArrayList<>();
        for (int subscriberSeq = 0; subscriberSeq < entry.getValue();
            subscriberSeq++, kafkaConsumerIdentifier++) {
          logger.info(String.format(
              "createAndStartAllSubscribers :: Creating KafkaSubscriber for topic = %s, Partition = %d.",
              entry.getKey(), subscriberSeq));

          KafkaSubscriber kafkaSubscriber = new KafkaSubscriber(entry.getKey(), subscriberSeq,
              kafkaConfig, numberOfWriteThreads, kafkaConsumerIdentifier,supportedCamTypes);

          subscriberPool.submit(kafkaSubscriber);
          subscriberPerTopic.add(kafkaSubscriber);
          logger.info(String.format(
              "createAndStartAllSubscribers :: Created KafkaSubscriber for topic = %s, Partition = %d.",
              entry.getKey(), subscriberSeq));
        }
        subscribers.put(entry.getKey(), subscriberPerTopic);
      }
      logger.info("createAndStartSubscribers :: kafka subscribers were successfully created....");
      subscriberPool.shutdown(); // Disable new tasks from being submitted
      subscriberPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
      SharedDataObject.setKeepReadingQueues(false);
    } catch (Exception e) {
      logger.error(
          "createAndStartSubscribers :: Exception occurred during consumer start. Going to stop already started consumer. Exception = "
              , e);
      shutdown();
    }
  }

  public void shutdown() {
    logger.info("KafkaSubscriberExecutorService: Received shutdown signal ");
    for (Map.Entry<String, List<KafkaSubscriber>> entry : subscribers.entrySet()) {
      for (KafkaSubscriber subscriber : entry.getValue()) {
        if (subscriber != null) {
          subscriber.shutdown();
        }
      }
    }

    try {
      subscriberPool.awaitTermination(Constants.KAFKA_WAIT_TIME_MILLS, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      logger.error("KafkaSubscriberExecutorService interrupted", e);
    }
    SharedDataObject.setKeepReadingQueues(false);
    logger.info("KafkaSubscriberExecutorService: Ended shutdown ");
  }

  private boolean validPartitionInfo(Map<String, Integer> partitionInfo) {
    boolean partitionMapIsValid = true;
    for (String topic : topics) {
      if (!partitionInfo.containsKey(topic)) {
        logger.error(
            "validPartitionInfo :: Topic Partition information is not present. Topic = " + topic);
        partitionMapIsValid = false;
      } else if (partitionInfo.get(topic) <= 0) {
        logger
            .error("validPartitionInfo :: Number of partition is less then zero. Topic = " + topic);
        partitionMapIsValid = false;
      }
    }
    return partitionMapIsValid;
  }

  /*
  * 1. @topics, list of kafka topic
  * 2. @brokers, broker host and port where key = broker_host, value = broker_port
  * 3. find and return topic and number of partition.
  * */
  private Map<String, Integer> getPartitionCountPerTopic() {
    Map<String, Integer> topicWisePartitionCount = new HashMap<>();
    for (int brokerIndex = 0; brokerIndex < brokerHosts.size(); brokerIndex++) {
      SimpleConsumer consumer = null;
      try {
        consumer = new SimpleConsumer(brokerHosts.get(brokerIndex), brokerPorts.get(brokerIndex),
            kafkaConfig.getInt(Constants.KAFKA_CONSUMER_TIMEOUT), kafkaConfig.getInt(Constants.KAFKA_CONSUMER_BUFFER_SIZE),
            "metadatalookup");
        TopicMetadataRequest topicMetadataRequest = new TopicMetadataRequest(topics);
        kafka.javaapi.TopicMetadataResponse resp = consumer.send(topicMetadataRequest);
        List<TopicMetadata> metaData = resp.topicsMetadata();
        for (TopicMetadata item : metaData) {
          topicWisePartitionCount.put(item.topic(), item.partitionsMetadata().size());
        }
      } catch (Exception exception) {
        logger.info(String.format(
            "getPartitionCountPerTopic :: Communication error with broker = %s to find partition info for topics = %s. Exception = %s.",
            brokerHosts.get(brokerIndex), topics.toString(), exception.getLocalizedMessage()));
      } finally {
        if (consumer != null) {
          consumer.close();
        }
      }
    }
    return topicWisePartitionCount;
  }
}