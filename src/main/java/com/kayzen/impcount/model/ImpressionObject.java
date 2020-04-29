package com.kayzen.impcount.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ImpressionObject {
  private int campaignId;
  private String deviceIdSha1;
  private int threadId;
  private long deviceIdLong;
  private String bidId;
  private long mobileObjectId;
  private long logTimestamp;
  private long bidTimestamp;
  private long latestKafkaOffset;
  private int kafkaConsumerIdentifier;
  private int campaignType;
  private int logType;

  public ImpressionObject(int campaignId,int logType,String deviceIdSha1, int thread_id,
      long deviceIdLong, String bidId, long logTimestamp, long bidTimestamp,long mobileObjectId,
      int campaignType, long latestKafkaOffset, int kafkaConsumerIdentifier) {
    this.campaignId = campaignId;
    this.deviceIdSha1 = deviceIdSha1;
    this.threadId = thread_id;
    this.deviceIdLong = deviceIdLong;
    this.bidId = bidId;
    this.mobileObjectId = mobileObjectId;
    this.logTimestamp = logTimestamp;
    this.bidTimestamp = bidTimestamp;
    this.latestKafkaOffset = latestKafkaOffset;
    this.kafkaConsumerIdentifier = kafkaConsumerIdentifier;
    this.campaignType = campaignType;
    this.logType = logType;
  }

  @Override
  public String toString() {
    return "FCapObject{" +
            "campaignId=" + campaignId +
            ", deviceIdSha1='" + deviceIdSha1 + '\'' +
            ", threadId=" + threadId +
            ", deviceIdLong=" + deviceIdLong +
            ", bidId='" + bidId + '\'' +
            ", logTimestamp=" + logTimestamp +
            ", bidTimestamp=" + bidTimestamp +
            ", latestKafkaOffset=" + latestKafkaOffset +
            ", kafkaConsumerIdentifier=" + kafkaConsumerIdentifier +
            '}';
  }
}