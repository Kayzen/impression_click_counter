package com.kayzen.impcount.executor.kafka;

import com.kayzen.impcount.model.ImpressionObject;
import com.kayzen.impcount.model.ImpressionObjectProvider;
import com.kayzen.impcount.model.SharedDataObject;
import com.kayzen.impcount.utils.Constants;
import com.applift.platform.commons.utils.Config;
import com.google.common.base.Charsets;
import com.google.common.collect.Iterables;
import com.kayzen.impcount.utils.Utils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.ErrorMapping;
import kafka.common.OffsetAndMetadata;
import kafka.common.OffsetMetadataAndError;
import kafka.common.TopicAndPartition;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.OffsetCommitRequest;
import kafka.javaapi.OffsetCommitResponse;
import kafka.javaapi.OffsetFetchRequest;
import kafka.javaapi.OffsetFetchResponse;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.immutable.Stream.Cons;

public class KafkaSubscriber implements Runnable {

  private static final Logger logger = LoggerFactory.getLogger(KafkaSubscriber.class);
  private final String topic;
  private final int partition;
  private final List<String> brokersHost;
  private final List<Integer> brokersPort;
  private boolean shutdownConsumer = false;
  private SimpleConsumer consumer = null;
  private List<String> replicaBrokersHost;
  private List<Integer> replicaBrokersPort;
  private String leadBrokerHost;
  private Integer leadBrokerPort;
  private String clientName;
  private long latestOffset;
  private long committedOffset;
  private long lastCommittedTime;
  private Config config;
  private long keyHashed;
  private int queueIndex;
  private int numberOfWriteThreads;
  private int kafkaConsumerIdentifier;
  private List<Long> submittedOffsets;
  private List<Integer> supportedCamTypes;

  public KafkaSubscriber(String topic, int partition, Config config,
      int numberOfWriteThreads,
      int kafkaConsumerIdentifier,List<Integer> supportedCamTypes) throws Exception {
    this.config = config;
    this.brokersHost = Utils.addToArrayList(config.getString(Constants.BROKER_HOSTS));
    this.brokersPort = Utils.addIntToArrayList(config.getString(Constants.BROKER_PORTS));
    this.topic = topic;
    this.replicaBrokersHost = new ArrayList<>();
    this.replicaBrokersPort = new ArrayList<>();
    this.partition = partition;
    this.keyHashed = 0L;
    this.queueIndex = 0;
    this.numberOfWriteThreads = numberOfWriteThreads;
    this.kafkaConsumerIdentifier = kafkaConsumerIdentifier;
    this.supportedCamTypes = supportedCamTypes;
    submittedOffsets = new ArrayList<>(numberOfWriteThreads);
    for (int i = 0; i < numberOfWriteThreads; i++) {
      submittedOffsets.add(0L);
    }
    init();
  }

  private void init() throws Exception {
    fetchClientInfo();
    consumer = new SimpleConsumer(leadBrokerHost, leadBrokerPort, config.getInt(Constants.KAFKA_CONSUMER_TIMEOUT),
        config.getInt(Constants.KAFKA_CONSUMER_BUFFER_SIZE), clientName);
    if (config.getBoolean(Constants.KAFKA_CONSUMER_START_FROM_LATEST_COMMIT)) {
      latestOffset = getLastOffset(kafka.api.OffsetRequest.LatestTime());
    } else if (config.getBoolean(Constants.KAFKA_CONSUMER_START_FROM_OLDEST_COMMIT)) {
      // finds the beginning of the data in the logs and starts streaming and
      // start from there
      latestOffset = getLastOffset(kafka.api.OffsetRequest.EarliestTime());
    } else {
      latestOffset = getLastOffset();
    }

    if (latestOffset == Constants.KAFKA_OFFSET_ERROR) {
      String exceptionMessage = String
          .format(
              "init :: Exception encountered while fetching Kafka offset. For Topic : %s, Partition : %s, clientName : %s.",
              topic, partition, clientName);
      throw new IllegalArgumentException(exceptionMessage);
    }
    lastCommittedTime = System.currentTimeMillis();
    logger.info(String.format(
        "init :: Lead Broker Host : %s, clientName : %s and initialOffset : %s for Topic : %s, Partition : %d.",
        leadBrokerHost, clientName, latestOffset, topic, partition));
  }

  private void fetchClientInfo() throws Exception {
    PartitionMetadata partitionMetadata = findLeader(brokersHost, brokersPort);
    if (partitionMetadata == null) {
      String exceptionMessage = String.format(
          "fetchClientInfo :: Can't find metadata for Topic : %s and Partition : %d. Exiting",
          topic, partition);
      logger.error(exceptionMessage);
      throw new Exception(exceptionMessage);
    }

    if (partitionMetadata.leader() == null) {
      String exceptionMessage = String.format(
          "fetchClientInfo :: Can't find Leader for Topic : %s and Partition : %d. Exiting", topic,
          partition);
      logger.error(exceptionMessage);
      throw new Exception(exceptionMessage);
    }

    leadBrokerHost = partitionMetadata.leader().host();
    leadBrokerPort = partitionMetadata.leader().port();
    clientName = getClientName();
  }

  @Override
  public void run() {
    logger.info(String
        .format("run :: Starting Kafka consumer for Topic : %s, Partition : %s.", topic,
            partition));
    while (!shutdownConsumer) {
      try {
        FetchResponse fetchResponse = fetchDataFromBroker();
        if (fetchResponse == null) {
          logger.info(String
              .format("run :: Exception occur during data fetch from broker. Kafka Consumer will "
                  + "retry for topic : %s, Partition : %d.", topic, partition));
          consumer = null;
          continue;
        }

        boolean isMessageReceived = false;
        for (MessageAndOffset messageAndOffset : fetchResponse.messageSet(topic, partition)) {
          long currentOffset = messageAndOffset.offset();
          if (currentOffset < latestOffset) {
            logger.info(String
                .format("run :: Found an old offset : %s, Expecting : %s : %s", currentOffset,
                    latestOffset, getKafkaMetaData()));
            continue;
          }
          latestOffset = messageAndOffset.nextOffset();
          ByteBuffer payload = messageAndOffset.message().payload();

          byte[] bytes = new byte[payload.limit()];
          payload.get(bytes);
          processLine(new String(bytes, "UTF-8"), latestOffset);
          isMessageReceived = true;
        }

        if (!isMessageReceived) {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException exception) {
            logger.warn(String.format(
                "run :: Exception Kafka consumer for Topic : %s, and Partition : %d, reason : %s.",
                topic, partition,
                exception.getLocalizedMessage()));
          }
        }
        if (lastCommittedTime < System.currentTimeMillis() - Constants.TEN_SECONDS_MILLS) {
          commit();
        }
      } catch (Exception exception) {
        logger.error(String.format(
            "run :: Exception in Kafka consumer for Topic : %s, Partition : %d, reason : %s.",
            topic, partition, exception.getLocalizedMessage()));
      }
    }
    closeConsumer();
  }

  void shutdown() {
    logger.info(String.format("shutdown :: Topic : %s, Partition : %s", topic, partition));
    shutdownConsumer = true;
  }

  private void closeConsumer() {
    commit();
    if (consumer != null) {
      consumer.close();
      consumer = null;
    }
    logger.info(String
        .format("closeConsumer :: Kafka Consumer is closed for Topic : %s, Partition : %s", topic,
            partition));
  }

  private PartitionMetadata findLeader(final List<String> brokersHost,
      final List<Integer> brokersPort) {
    PartitionMetadata returnMetaData = null;
    for (int brokerIndex = 0; brokerIndex < brokersHost.size(); brokerIndex++) {
      try {
        logger.debug(String.format(
              "findLeader :: Consumer looking up leader for Topic : %s, Partition : %s at broker : %s:%s",
              topic,
              partition, brokersHost.get(brokerIndex), brokersPort.get(brokerIndex)));
        SimpleConsumer consumer = new SimpleConsumer(brokersHost.get(brokerIndex),
            brokersPort.get(brokerIndex),
            config.getInt(Constants.KAFKA_CONSUMER_TIMEOUT), config.getInt(Constants.KAFKA_CONSUMER_BUFFER_SIZE),
            Constants.LEADER_LOOKUP_CLIENT_ID);
        returnMetaData = partitionMetadata(partition, topic,
            fetchMetaDataResponse(topic, consumer));
        if (setReplicaBrokerInfo(returnMetaData)) {
          break;
        }
      } catch (Exception exception) {
        logger
            .error(String
                .format(
                    "findLeader :: Exception during communication with Broker : %s to find Leader for Topic : %s, Partiton :%s, Reason : %s.",
                    brokersHost.get(brokerIndex), topic, partition,
                    exception.getLocalizedMessage()));
      } finally {
        if (consumer != null) {
          consumer.close();
        }
      }
    }
    return returnMetaData;
  }

  private PartitionMetadata partitionMetadata(int partition, String topic,
      kafka.javaapi.TopicMetadataResponse resp) {
    if (resp == null) {
      logger.error("PartitionMetadata :: Partition response metadata is null for topic = " + topic
          + ", and partition = " + partition);
      return null;
    }
    List<TopicMetadata> metaData = resp.topicsMetadata();
    for (TopicMetadata item : metaData) {
      for (PartitionMetadata part : item.partitionsMetadata()) {
        if (part.partitionId() == partition) {
          return part;
        }
      }
    }
    return null;
  }

  private kafka.javaapi.TopicMetadataResponse fetchMetaDataResponse(String topic,
      SimpleConsumer consumer) {
    if (topic == null || consumer == null) {
      return null;
    }

    List<String> topics = new ArrayList<>();
    topics.add(topic);
    TopicMetadataRequest topicMetadataRequest = new TopicMetadataRequest(topics);
    return consumer.send(topicMetadataRequest);
  }

  // add replica broker info to replicaBrokersHost
  private boolean setReplicaBrokerInfo(PartitionMetadata returnMetaData) {
    if (returnMetaData != null) {
      if (returnMetaData.replicas() != null && returnMetaData.replicas().size() > 0) {
        replicaBrokersHost.clear();
        replicaBrokersPort.clear();
        for (kafka.cluster.Broker replica : returnMetaData.replicas()) {
          replicaBrokersHost.add(replica.host());
          replicaBrokersPort.add(replica.port());
        }
        return true;
      } else {
        return false;
      }
    }
    return false;
  }

  private long getLastOffset(long whichTime) {
    TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
    Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<>();
    requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(whichTime, 1));
    kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(requestInfo,
        kafka.api.OffsetRequest.CurrentVersion(), clientName);
    OffsetResponse response = consumer.getOffsetsBefore(request);

    if (response.hasError()) {
      logger
          .error(String
              .format(
                  "getLastOffset :: parametrized method :: Exception while fetching last offset data the Broker for Topic : %s, Partition : %s,  Reason : %s.",
                  topic, partition, response.errorCode(topic, partition)));
      return 0;
    }
    long[] offsets = response.offsets(topic, partition);
    return offsets[0];
  }

  /*
   * Find the last commit offset and start from there. If last commit offset
   * info is not present we've yet to commit. Start to read from 0
   */
  private long getLastOffset() {
    TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
    List<TopicAndPartition> requestInfo = new ArrayList<>();
    requestInfo.add(topicAndPartition);
    OffsetFetchResponse offsetFetchResponse = consumer.fetchOffsets(new OffsetFetchRequest(config
        .getString(Constants.KAFKA_CONSUMER_OFFSET_COMMIT_GROUP_ID), requestInfo, Constants.OFFSET_COMMIT_VERSION,
        Constants.CORRELATION_ID,
        clientName));
    if (offsetFetchResponse == null) {
      logger
          .error(String
              .format(
                  "getLastOffset :: Exception while fetching last offset data the from Broker for Topic : %s, Partition : %s. Offset fetch response is null.",
                  topic, partition));
      return Constants.KAFKA_OFFSET_ERROR;
    }

    OffsetMetadataAndError result = offsetFetchResponse.offsets().get(topicAndPartition);
    short offsetFetchErrorCode = result.error();

    if (offsetFetchErrorCode == ErrorMapping.NotCoordinatorForConsumerCode()) {
      logger
          .error(String
              .format(
                  "getLastOffset :: Error encountered while fetching Kafka offset: NotCoordinatorForConsumerCode for Topic : %s, Partition : %s.",
                  topic, partition));
      return Constants.KAFKA_OFFSET_ERROR;
    } else if (offsetFetchErrorCode == ErrorMapping.OffsetsLoadInProgressCode()) {
      logger
          .error(String
              .format(
                  "getLastOffset :: Error encountered while fetching Kafka offset: OffsetsLoadInProgressCode for Topic : %s, Partition : %s.",
                  topic, partition));
      return Constants.KAFKA_OFFSET_ERROR;
    } else {
      long retrievedOffset = result.offset();
      if (retrievedOffset == Constants.KAFKA_OFFSET_ERROR) {
        logger
            .info(String
                .format(
                    "getLastOffset :: No commits found against Kafka queue for Topic : %s, Partition : %s. Setting read offset to 0",
                    topic, partition));
        return getLastOffset(kafka.api.OffsetRequest.EarliestTime());
      } else {
        return retrievedOffset;
      }
    }
  }

  private FetchResponse fetchDataFromBroker() {
    FetchResponse fetchResponse = null;
    int numberOfTry = 0;
    while (numberOfTry < 3) {
      numberOfTry++;
      try {
        if (consumer == null) {
          consumer = new SimpleConsumer(leadBrokerHost, leadBrokerPort,
              config.getInt(Constants.KAFKA_CONSUMER_TIMEOUT),
              config.getInt(Constants.KAFKA_CONSUMER_BUFFER_SIZE), clientName);
        }

        FetchRequest req = new FetchRequestBuilder().clientId(clientName)
            .addFetch(topic, partition, latestOffset, config.getInt(Constants.KAFKA_CONSUMER_FETCH_SIZE)).build();
        fetchResponse = consumer.fetch(req);
        if (fetchResponse.hasError()) {
          short code = fetchResponse.errorCode(topic, partition);
          if (code == ErrorMapping.OffsetOutOfRangeCode()) {
            long olderOffset = latestOffset;
            latestOffset = getLastOffset(kafka.api.OffsetRequest.LatestTime());
            logger
                .error(String
                    .format(
                        "fetchDataFromBroker :: Exception fetching data from the Broker : %s for Topic : %s, Partition : %s. Reason : Offset OutOfRange,"
                            + "Start fetching latest offset value based on timestamp=LatestTime(). OlderOffsetValue : %s, NewOffsetValue : %s",
                        leadBrokerHost, topic, partition, olderOffset, latestOffset));
            continue;
          }
          logger
              .error(String
                  .format(
                      "fetchDataFromBroker :: Exception while fetching data from the lead broker : %s for Topic : %s, Partition : %s, Reason : %s, numberOfTry : %s. "
                          + "Going to reset lead broker host for given topic and partition.",
                      leadBrokerHost, topic,
                      partition, code, numberOfTry));
          consumer.close();
          consumer = null;
          fetchResponse = null;
          leadBrokerHost = findNewLeader(leadBrokerHost, leadBrokerPort);
        } else {
          break;
        }
      } catch (Exception exception) {
        logger
            .error(String.format(
                "fetchDataFromBroker :: Exception while fetching data from the Broker for Topic : %s, Partition : %s. Reason : %s, numberOfTry : %s. Setting response as null "
                    + "and will retry if numberOfTry < 3.", topic, partition,
                exception.getLocalizedMessage(),
                numberOfTry));
        fetchResponse = null;
      }
    }
    return fetchResponse;
  }

  private String findNewLeader(String oldLeader, int oldPort) throws Exception {
    for (int i = 0; i <= 3; i++) {
      PartitionMetadata metadata = findLeader(replicaBrokersHost, replicaBrokersPort);
      if ((metadata != null) && (metadata.leader() != null)) {
        if (!oldLeader.equalsIgnoreCase(metadata.leader().host()) || i != 0) {
          // first time through checking whether the leader has changed. Giving
          // ZooKeeper 30 second to recover second time, assume the broker
          // did recover before fail-over, or it was a non-Broker issue.
          // Hence will goToSleep
          leadBrokerPort = metadata.leader().port();
          logger
              .info(String
                  .format(
                      "findNewLeader :: New leader found after broker failure for Topic : %s, Partition : %s, old leader : %s, new leader : %s.",
                      topic, partition, oldLeader, metadata.leader().host()));
          return metadata.leader().host();
        }
      }
      try {
        // wait till 30 sec before retry for leader because broker need some
        // time to elect new leader
        Thread.sleep(30000);
      } catch (InterruptedException ie) {
        logger.error("Interruption Exception",ie);
      }
    }
    String errorMessage = String.format(
        "findNewLeader :: Unable to find new leader after Broker failure for Topic : %s, Partition : %s. Exiting",
        topic, partition);
    logger.error(errorMessage);
    throw new Exception(errorMessage);
  }

  private void commit() {
    long minOffset = Long.MAX_VALUE;
    long maxOffset = 0;
    boolean useMaxOffset = true;
    for (int qIndex = 0; qIndex < numberOfWriteThreads; qIndex++) {
      List<Long> kafkaOffsetMap = SharedDataObject.kafkaOffsetPerProcessorBatch.get(qIndex);
      long offsetVal = kafkaOffsetMap.get(kafkaConsumerIdentifier);
      if (offsetVal < submittedOffsets.get(qIndex)) {
        minOffset = offsetVal < minOffset ? offsetVal : minOffset;
        useMaxOffset = false;
      } else {
        maxOffset = offsetVal > maxOffset ? offsetVal : maxOffset;
      }
    }
    long newOffset = useMaxOffset ? maxOffset : minOffset;
    if (newOffset > latestOffset) {
      logger.error("KafkaSubscriber: received invalid offset to commit. Client:" + getClientName()
          + " newOffset:"
          + newOffset + " latestOffset:" + latestOffset + " : " + getKafkaMetaData());
    }
    if (newOffset > committedOffset) {
      Map<TopicAndPartition, OffsetAndMetadata> requestInfo = new HashMap<>();
      requestInfo.put(new TopicAndPartition(topic, partition),
          new OffsetAndMetadata(newOffset, OffsetAndMetadata.NoMetadata(),
              OffsetAndMetadata.InvalidTime()));
      OffsetCommitRequest offsetCommitRequest = new OffsetCommitRequest(
          config.getString(Constants.KAFKA_CONSUMER_OFFSET_COMMIT_GROUP_ID),
          requestInfo, Constants.CORRELATION_ID, clientName, Constants.OFFSET_COMMIT_VERSION);
      OffsetCommitResponse response = consumer.commitOffsets(offsetCommitRequest);
      if (response.hasError()) {
        logger.error("commit :: Error encountered while committing offset " + latestOffset
            + " for Kafka. Commit error code: " + response
            .errorCode(new TopicAndPartition(topic, partition)));
      } else {
        logger.debug(String.format(
              "commit :: Consumer commit: Updating initial offset from %s to %s for Topic : %s, Partition : %s.",
              committedOffset, newOffset, topic, partition));
        committedOffset = newOffset;
        lastCommittedTime = System.currentTimeMillis();
      }
    } else {
      logger.info("KafkaSubscriber: " + getKafkaMetaData() +  " : no new commit offset");
      lastCommittedTime = System.currentTimeMillis();
    }
  }

  private void processLine(String multiLine, long latestOffset) {
    try {
      if (multiLine != null && !multiLine.isEmpty()) {
        String[] lines = Iterables.toArray(Constants.lineSplitter.split(multiLine), String.class);
        // Ignore empty line
        if (lines != null && lines.length > 0) {
          for (String line : lines) {
            ImpressionObject fCapObject = ImpressionObjectProvider
                .getFCapDataObject(line, latestOffset, kafkaConsumerIdentifier);
            if (fCapObject != null) {
              if (supportedCamTypes.contains(fCapObject.getCampaignType())) {
                if (fCapObject.getCampaignId() > 100_000) {
                  logger.debug("fCapObject:" + fCapObject.toString());

                  if (fCapObject.getDeviceIdSha1() != null && !fCapObject.getDeviceIdSha1()
                      .isEmpty()) {
                    try {
                      keyHashed = Constants.hf.newHasher()
                          .putString(fCapObject.getDeviceIdSha1(), Charsets.UTF_8)
                          .hash().asLong();
                      queueIndex = Math.abs((int) (keyHashed % this.numberOfWriteThreads));
                      SharedDataObject.sharedObjectQueues.get(queueIndex).put(fCapObject);
                      submittedOffsets.set(queueIndex, latestOffset);
                    } catch (InterruptedException e) {
                      logger.error("processLine :: " + this.topic + ":" + this.partition
                          + " .InterruptedException occurred in sharedBQueue.put()", e);
                    }
                  } else {
                    // If no device id sha1, nothing to do here
                    logger.debug(getKafkaMetaData() + " : no device id sha1 for line:" + line);
                  }
                } else {
                  logger.warn("Got data for old platform campaigns : " + fCapObject.getCampaignId());
                }
              } else {
                logger.debug("CampaignType is not supported for bidId : {}",fCapObject.getBidId());
              }
            } else {
              logger.warn(getKafkaMetaData() + " : fCapObject is null for line:" + line);
            }
          }
        } else {
          return;
        }
      }
    } catch (Exception e) {
      logger.error("processLine:: " + this.topic + ":" + this.partition
          + " .Exception occurred while creating DataObject ", e);
    }
  }

  private String getClientName() {
    return "Client_" + topic + "_" + partition + config.getString(Constants.KAFKA_CLIENT_SUFFIX);
  }

  public String getKafkaMetaData() {
    return topic + ":" + partition + ":" + latestOffset;
  }
}
