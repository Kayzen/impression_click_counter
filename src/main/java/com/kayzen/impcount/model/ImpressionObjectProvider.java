package com.kayzen.impcount.model;

import com.applift.platform.commons.utils.Config;
import com.applift.platform.commons.utils.DeviceIDUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kayzen.impcount.utils.Constants;
import com.kayzen.impcount.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImpressionObjectProvider {

  private static Logger logger = LoggerFactory.getLogger(ImpressionObjectProvider.class.getName());

  public static ImpressionObject getFCapDataObject(String line, long latestKafkaOffset,
      int kafkaConsumerIdentifier) {
    try {
      ImpressionObject fCapObject = null;
      if (line.isEmpty() || line.startsWith("#") || line.startsWith("LOGROTATE") ||
          !(line.startsWith("{") && line.endsWith("}"))) {
        logger.warn("IGNORE0-Ignoring Empty/Logrotate line : " + line);
        return null;
      }
      JsonObject rawJson = new JsonParser().parse(line).getAsJsonObject();
      int eventTypeId = -1;
      if (rawJson.has(Constants.LOG_ENTRY_TYPE)) {
        eventTypeId = rawJson.get(Constants.LOG_ENTRY_TYPE).getAsInt();
      }

      // Do only for IMP, CLK
      if (eventTypeId == Constants.eventTypeImpression || eventTypeId == Constants.eventTypeClick) {
        fCapObject = getFromImpOrClick(eventTypeId, line, rawJson, latestKafkaOffset, kafkaConsumerIdentifier);
      } else {
        logger.warn("IGNORE5-Invalid eventTypeId=" + eventTypeId);
        return null;
      }
      return fCapObject;
    } catch (Exception e) {
      logger.error("Exception occurred while processing Tuple", e);
    }
    return null;
  }

  private static ImpressionObject getFromImpOrClick(int eventTypeId, String line, JsonObject rawJson,
    long latestKafkaOffset, int kafkaConsumerIdentifier) {
    try {
      long appObjectId;
      String bidId = rawJson.get(Constants.BID_ID).getAsString();
      if(rawJson.get(Constants.MOBILE_OBJECT_ID) != null && rawJson.get(Constants.MOBILE_OBJECT_ID).getAsLong() != 0)
        appObjectId = rawJson.get(Constants.MOBILE_OBJECT_ID).getAsLong();
      else{
        appObjectId = rawJson.get(Constants.ADV_ID).getAsLong();
      }

      long bidTimestamp = Utils.getBidTimestamp(bidId);
      if (bidTimestamp == Constants.LONG_ZERO) {
        logger.info("Invalid BidId for eventTypeId:" + eventTypeId + ",values:" + line);
        return null;
      }

      String dpid_sha1 = rawJson.get(Constants.DPID_SHA1).getAsString();

      //if dpid_sha1 is empty then use user_agent & device_ip for generating dpid_sha1
      /*if ((dpid_sha1 == null) || dpid_sha1.isEmpty()) {
        String userAgent = rawJson.get(Constants.USER_AGENT).getAsString();
        String userIp = rawJson.get(Constants.USER_IP).getAsString();
        if ((userAgent != null && !userAgent.isEmpty()) && (userIp != null && !userIp.isEmpty())) {
          dpid_sha1 = DeviceIDUtils.getSha1(userAgent + userIp);
        }
      }*/
      if ((dpid_sha1 == null) || dpid_sha1.isEmpty()) {
        String final_dpidsha1 = rawJson.get(Constants.FINAL_DPIDSHA1).getAsString();
        if((final_dpidsha1 != null) && !final_dpidsha1.isEmpty()){
          dpid_sha1 =  final_dpidsha1;
        }
      }

      return new ImpressionObject(rawJson.get(Constants.CAMPAIGN_ID).getAsInt() // campaignId,
          , eventTypeId /*Event Type*/
          , dpid_sha1/* device id sha1 */
          , 0 /* thread_id */
          , 0 /* device_id_int */
          , bidId /* bidId */
          , rawJson.get(Constants.TIMESTAMP).getAsLong()/* logTimestamp in seconds */
          , bidTimestamp /* bidTimestamp in seconds */
          , appObjectId/*AppObject Id */
          , rawJson.get(Constants.CAMPAIGN_TYPE_RAW).getAsInt()/*campaign_type*/
          , latestKafkaOffset
          , kafkaConsumerIdentifier);
    } catch (Exception e) {
      logger.error("Exception occurred in getFromImpOrClick : ", e);
      return null;
    }
  }

}
