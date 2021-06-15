package com.kayzen.impcount.aerospike;

import org.apache.log4j.Logger;

public class StatsRecorder {

  private static final Logger logger = Logger.getLogger(StatsRecorder.class.getName());

  private static long totalMySQLUpdates = 0;
  private static long totalMySQLFetched = 0;
  private static long totalAeroUpdatesDCA = 0;
  private static long totalAeroUpdatesHKG = 0;
  private static long totalAeroUpdatesAMS = 0;
  private static long totalMySQLFetchTime = 0;
  private static long totalMySQLUpdateTime = 0;
  private static long totalAeroUpdateTimeDCA = 0;
  private static long totalAeroUpdateTimeHKG = 0;
  private static long totalAeroUpdateTimeAMS = 0;
  private static long lastMySQlUpdates = 0;
  private static long lastMySQLFetched = 0;
  private static long lastAeroUpdatesDCA = 0;
  private static long lastAeroUpdatesHKG = 0;
  private static long lastAeroUpdatesAMS = 0;
  private static long lastMySQLFetchTime = 0;
  private static long lastMySQLUpdateTime = 0;
  private static long lastAeroUpdateTimeDCA = 0;
  private static long lastAeroUpdateTimeHKG = 0;
  private static long lastAeroUpdateTimeAMS = 0;

  public static void updateAerospikeStats(long totalAeroUpdatesDCA, long totalAeroUpdateTimeDCA,
    long totalAeroUpdatesHKG, long totalAeroUpdateTimeHKG, long totalAeroUpdatesAMS, long totalAeroUpdateTimeAMS) {
    if(totalAeroUpdatesDCA>0) {
      StatsRecorder.lastAeroUpdatesDCA = totalAeroUpdatesDCA;
      StatsRecorder.lastAeroUpdateTimeDCA = totalAeroUpdateTimeDCA;
      StatsRecorder.totalAeroUpdatesDCA += totalAeroUpdatesDCA;
      StatsRecorder.totalAeroUpdateTimeDCA += totalAeroUpdateTimeDCA;
    }
    else if(totalAeroUpdatesHKG > 0){
      StatsRecorder.lastAeroUpdatesHKG = totalAeroUpdatesHKG;
      StatsRecorder.lastAeroUpdateTimeHKG = totalAeroUpdateTimeHKG;
      StatsRecorder.totalAeroUpdatesHKG += totalAeroUpdatesHKG;
      StatsRecorder.totalAeroUpdateTimeHKG += totalAeroUpdateTimeHKG;
    }
    else {
      StatsRecorder.lastAeroUpdatesAMS = totalAeroUpdatesAMS;
      StatsRecorder.lastAeroUpdateTimeAMS = totalAeroUpdateTimeAMS;
      StatsRecorder.totalAeroUpdatesAMS += totalAeroUpdatesAMS;
      StatsRecorder.totalAeroUpdateTimeAMS += totalAeroUpdateTimeAMS;
    }
  }

  static void updateMySQLStats(long totalMySQlUpdates, long totalMySQLFetched,
    long totalMySQLFetchTime, long totalMySQLUpdateTime) {
    StatsRecorder.lastMySQLFetched = totalMySQLFetched;
    StatsRecorder.lastMySQLFetchTime = totalMySQLFetchTime;
    StatsRecorder.lastMySQlUpdates = totalMySQlUpdates;
    StatsRecorder.lastMySQLUpdateTime = totalMySQLUpdateTime;
    StatsRecorder.totalMySQLFetched += totalMySQLFetched;
    StatsRecorder.totalMySQLFetchTime += totalMySQLFetchTime;
    StatsRecorder.totalMySQLUpdates += totalMySQlUpdates;
    StatsRecorder.totalMySQLUpdateTime += totalMySQLUpdateTime;
  }


  public static void printStats() {
    logger.info("Printing Stats" + "\n"
      + getTotalMysqlStats() + "\n" + getLatestMysqlStats() + "\n"
      + getTotalAeroStats() + "\n" + getLatestAeroStats());
  }

  static String getTotalMysqlStats() {
    if (totalMySQLFetchTime > 0 && totalMySQLUpdateTime > 0) {
      return "StatsRecorder: MySQL:   Total:{" +
        "MySQLFetched=" + totalMySQLFetched +
        ", MySQLUpdates=" + totalMySQLUpdates +
        ", MySQLFetchTime=" + totalMySQLFetchTime +
        ", MySQLUpdateTime=" + totalMySQLUpdateTime +
        ", MySQLFetchSpeed=" + (totalMySQLFetched * 1000) / totalMySQLFetchTime +
        ", MySQlUpdateSpeed=" + (totalMySQLUpdates * 1000) / totalMySQLUpdateTime +
        "}}";
    }
    return "StatsRecorder: MySQL:   Total:{NA}}";
  }

  static String getLatestMysqlStats() {
    long mysqlFetchTime = lastMySQLFetchTime;
    long mysqlUpdateTime = lastMySQLUpdateTime;
    if (mysqlFetchTime > 0 && mysqlUpdateTime > 0) {
      return "StatsRecorder: MySQL:  Latest:{" +
          "MySQLFetchSpeed=" + (lastMySQLFetched * 1000) / mysqlFetchTime +
          ", MySQlUpdateSpeed=" + (lastMySQlUpdates * 1000) / mysqlUpdateTime +
          "}}";
    } else {
      return "StatsRecorder: MySQL:  Latest:{NA}}";
    }
  }

  static String getTotalAeroStats() {
    if (totalAeroUpdateTimeDCA > 0 && totalAeroUpdateTimeHKG > 0 && totalAeroUpdateTimeAMS > 0) {
      return "StatsRecorder: Aero:   Total:{" +
        "DCATotal=" + totalAeroUpdatesDCA +
        ", HKGTotal=" + totalAeroUpdatesHKG +
        ", AMSTotal=" + totalAeroUpdatesAMS +
        ", totalAeroUpdateTimeDCA=" + totalAeroUpdateTimeDCA +
        ", totalAeroUpdateTimeHKG=" + totalAeroUpdateTimeHKG +
        ", totalAeroUpdateTimeAMS=" + totalAeroUpdateTimeAMS +
        ", AeroDCASpeed=" + (totalAeroUpdatesDCA * 1000) / totalAeroUpdateTimeDCA +
        ", AeroHKGSpeed=" + (totalAeroUpdatesHKG * 1000) / totalAeroUpdateTimeHKG +
        ", AeroAMSSpeed=" + (totalAeroUpdatesAMS * 1000) / totalAeroUpdateTimeAMS +
        "}}";
    }
    return "StatsRecorder: Aero:   Total:{NA}}";
  }

  static String getLatestAeroStats() {
    if (lastAeroUpdateTimeDCA > 0 && lastAeroUpdateTimeHKG > 0 && lastAeroUpdateTimeAMS > 0) {
      return "StatsRecorder: Aero:  Latest:{" +
        "AeroDCASpeed=" + (lastAeroUpdatesDCA * 1000) / lastAeroUpdateTimeDCA +
        ", AeroHKGSpeed=" + (lastAeroUpdatesHKG * 1000) / lastAeroUpdateTimeHKG +
        ", AeroAMSSpeed=" + (lastAeroUpdatesAMS * 1000) / lastAeroUpdateTimeAMS +
        "}}";
    } else {
      return "StatsRecorder: Aero:  Latest:{NA}}";
    }
  }
}