package com.kayzen.impcount.utils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import com.google.common.base.Splitter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

  private static Logger logger = Logger.getLogger(Utils.class.getName());
  private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
  private static Splitter dashSplitter = Splitter.on("-").omitEmptyStrings().trimResults();
  public static final long SECONDS_IN_A_DAY = 86400L;

  public static String generateUUID() {
    return UUID.randomUUID().toString();
  }

  public static String removeCommas(String stringToReplace) {
    if (stringToReplace != null && stringToReplace.contains(",")) {
      return stringToReplace.replaceAll(",", "");
    }
    return stringToReplace;
  }

  public static String getProjectBaseDir() {
    return new File("").getAbsolutePath();
  }


  public static void setLogger(String projectBaseLocation,String project) {
    String projectBaseDirectory = getProjectBaseDir(projectBaseLocation);
    String logName = projectBaseDirectory + Constants.IMPRESSION_COUNTER_LOG_FILENAME;
    if(project.equals(Constants.IMPRESSION_COUNTER)){
      logName = projectBaseDirectory + Constants.IMPRESSION_COUNTER_LOG_FILENAME;
    }else if (project.equals(Constants.AERO_IMPRESSION_COUNTER)){
      logName = projectBaseDirectory + Constants.AERO_IMPRESSION_COUNTER_LOG_FILENAME;
    }
    System.setProperty("log.name", logName);

    LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    ContextInitializer ci = new ContextInitializer(lc);
    lc.reset();
    try {
      //I prefer autoConfig() over JoranConfigurator.doConfigure() so I wouldn't need to find the file myself.
      ci.autoConfig();
    } catch (JoranException e) {
      // StatusPrinter will try to log this
      e.printStackTrace();
    }
    StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
    System.out.println("Logs of this run can be found at:" + logName);
  }

  public static String getProjectBaseDir(String location) {
    if (StringUtils.isNotBlank(location)) {
      return location;
    }
    return new File("").getAbsolutePath();
  }

  public static String printDuration(long milliseconds) {
    long seconds = milliseconds / 1000;
    long s = seconds % 60;
    long m = (seconds / 60) % 60;
    long h = (seconds / (60 * 60)) % 24;
    return String.format("%d hours, %02d minutes, %02d seconds", h, m, s);
  }

  public static int getIntForString(String str) {
    try {
      if (str.length() > 0) {
        Integer parsedInt = Integer.parseInt(str);
        if (parsedInt != null) {
          return parsedInt;
        }
      }
    } catch (Exception e) {
      logger.error("Malformed input. Can't fetch integer from: " + str, e);
    }
    return 0;
  }

  public static long getLongForString(String str) {
    try {
      if (str.length() > 0) {
        Long parsedLong = Long.parseLong(str);
        if (parsedLong != null) {
          return parsedLong;
        }
      }
    } catch (Exception e) {
      logger.error("Malformed input. Can't fetch long from:" + str, e);
    }
    return 0;
  }

  public static long getBidTimestamp(String bidId) {
    if (bidId == null || bidId.trim().isEmpty()) {
      logger.error(" bidId found null/empty ");
      return Constants.LONG_ZERO;
    }
    List<String> fields = dashSplitter.splitToList(bidId);
    if (fields.size() >= 6) {
      if (validBidId(bidId)) {
        return Long.parseLong(fields.get(1));
      }
    } else {
      logger.debug("Wrong bidId passed to validateAndGetFromBidId:" + bidId);
      return Constants.LONG_ZERO;
    }
    return Constants.LONG_ZERO;
  }

  public static boolean validBidId(String bidId) {
    // Bid id is of form :
    // 98069767-1425554357-T5Vrj-LR_54ac808c400db9.06251081-1-3070-38293
    //
    int length = bidId.length();
    int firstDashIndex = bidId.indexOf("-");
    String hash = bidId.substring(0, firstDashIndex);
    String bidMinusHash = bidId.substring(firstDashIndex + 1, length);
    int hashFromBid = getIntForString(hash);
    if (matchBidIdAndHash(hashFromBid, bidMinusHash)) {
      return true;
    } else {
      logger.debug("Wrong bidId passed to validateAndGetFromBidId:" + bidId);
      return false;
    }
  }

  public static long getBidTime(String bidId) {
    // Bid id is of form :
    // 98069767-1425554357-T5Vrj-LR_54ac808c400db9.06251081-1-3070-38293
    //
    List<String> fields = dashSplitter.splitToList(bidId);
    if (fields.size() >= 6) {
      return Long.parseLong(fields.get(1));
    }
    return Constants.LONG_ZERO;
  }

  private static boolean matchBidIdAndHash(int hashFromBid, String bidMinusHash) {
    if (hashFromBid != getBidIdHash(bidMinusHash)) {
      if (bidMinusHash.contains(" - ")) {
        String replaced = bidMinusHash.replace(" - ", "/");
        // logger.info("replaced bidId:" + replaced);
        if (hashFromBid != getBidIdHash(replaced)) {
          replaced = bidMinusHash.replace(" - ", "//");
          // logger.info("replaced bidId:" + replaced);
          if (hashFromBid != getBidIdHash(replaced)) {
            return false;
          }
        }
      } else {
        return false;
      }
    }
    return true;
  }

  private static long getBidIdHash(String bidMinusHash) {
    long hash = 0;
    long prime = 1000000009;
    char[] charArray = bidMinusHash.toCharArray();
    for (int i = 0; i < bidMinusHash.length(); i++) {
      // logger.info("charArray:" + charArray[i]+ ":" + (int)charArray[i]);
      hash = (hash * 65599 + (int) charArray[i]) % prime;
      // logger.info("hash:" + hash);
    }
    return hash;
  }

  public static long computeWhenToExpire(long timestamp, int days) {
    // in secs: convert days to secs,add to start timestamp
    return timestamp + (days * SECONDS_IN_A_DAY);
  }

  // 719528 = mysql to_days(19700101) => unix timestamp 1
  public static int timestampToDays(long timestamp) {
    return 719528 + (int) (timestamp / SECONDS_IN_A_DAY);
  }

  public static String join(Set<String> campaignIds) {
    if (campaignIds == null || campaignIds.size() == 0) {
      return "";
    }
    String campaignId = campaignIds.stream()
        .map(i -> i.toString())
        .collect(Collectors.joining(","));
    return campaignId;
  }

  public static String getCurrentDate(long ts) {
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    Date date = new Date(ts);
    return formatter.format(date);
  }

  public static void removeFile(String filePath) throws IOException {
    File file = new File(filePath);
    Files.deleteIfExists(file.toPath());
  }

  public static List<String> addToArrayList(String string) {
    ArrayList<String> list = new ArrayList<>();
    if (string != null && !string.isEmpty()) {
      String[] values = string.split(",");
      if (values.length > 0) {
        list.addAll(Arrays.asList(values));
      }
    }
    return list;
  }

  public static List<Integer> addIntToArrayList(String string) {
    ArrayList<Integer> list = new ArrayList<>();
    if (string != null && !string.isEmpty()) {
      String[] values = string.split(",");
      if (values.length > 0) {
        for (String number : values) {
          try {
            list.add(Integer.parseInt(number.trim()));
          } catch (Exception e) {
            logger.error("Error occurred in converting to integer:" + number);
          }
        }
      }
    }
    return list;
  }

  public static List<Integer> getsupportedTypesInt(List<String> supportedCamTypes) {
    List<Integer> supportedTypeInt = new ArrayList<>();
    for(String ct : supportedCamTypes){
      supportedTypeInt.add(CampaignType.getCampaignType(ct).getId());
    }
    return supportedTypeInt;
  }
}