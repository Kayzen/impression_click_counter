package com.kayzen.impcount.utils;

import com.applift.platform.commons.db.MySqlDatabase;
import com.applift.platform.commons.db.statement.MySQLPreparedStatement;
import com.kayzen.impcount.model.ImpressionObject;
import java.sql.ResultSet;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySqlDbUtils {

  // Begin of device section
  private static final String addDeviceSQL = "INSERT IGNORE INTO devices (thread_id, device_id, device_id_sha1) VALUES(?,?,?)";
  // Begin of insert into campaign_impression_fcap_data
  private static final String addImpressionDataSQL = "INSERT INTO device_impressions_daily (day_int, campaign_id,logType, thread_id, device_id, app_object_id) VALUES(?,?,?,?,?,?)";

  private static final String partitionCounterQuery = "select max(device_id) from devices where thread_id = REPLACE_THREAD_ID";
  private static final String devicesReadQuery =
      "SELECT device_id_sha1, device_id FROM devices WHERE thread_id = REPLACE_THREAD_ID "
          + "and device_id>REPLACE_START_DEVICE_ID and device_id<=REPLACE_END_DEVICE_ID";
  private static final String aerospikeDataReadQuery =
      "select a.id,a.app_object_id, d.device_id_sha1,a.imp_click_count,a.logType,a.action"
          + " from aerospike_impression_count_data_REPLACE_THREAD_ID a, devices d "
          + " where a.thread_id = d.thread_id and a.device_id = d.device_id "
          + " AND a.REPLACE_DATA_CENTER_status=\"pending\" ORDER BY ID limit %d";
  private static final String aerospikeDataUpdateQuery = "UPDATE aerospike_impression_count_data_REPLACE_THREAD_ID SET REPLACE_DATA_CENTER_status = 'processed' WHERE id >= ? AND id <= ?";
  public static final String REPLACE_THREAD_ID = "REPLACE_THREAD_ID";
  public static final String REPLACE_START_DEVICE_ID = "REPLACE_START_DEVICE_ID";
  public static final String REPLACE_END_DEVICE_ID = "REPLACE_END_DEVICE_ID";
  public static final String REPLACE_DATA_CENTER = "REPLACE_DATA_CENTER";

  private static Logger logger = LoggerFactory.getLogger(MySqlDbUtils.class.getName());

  public static MySQLPreparedStatement getAddDevicePST(MySqlDatabase database) {
    try {
      return new MySQLPreparedStatement(database, addDeviceSQL);
    } catch (Exception e) {
      logger.error("getAddDevicePST() :: Exception while retrieving PST:", e);
      return null;
    }
  }

  public static boolean addDevices(MySQLPreparedStatement pst, ImpressionObject impressionObject) {
    try {
      pst.setInt(1, impressionObject.getThreadId());
      pst.setLong(2, impressionObject.getDeviceIdLong());
      pst.setString(3, impressionObject.getDeviceIdSha1());
      pst.addBatch();
    } catch (Exception e) {
      logger.error("addDevices() :: Exception while updating db :", e);
      return false;
    }
    return true;
  }

  public static long getPartitionCounterFromDB(MySqlDatabase database, int threadIndex) {
    try {
      Statement statement = database.createReadStatement();
      String query = partitionCounterQuery.replaceAll(REPLACE_THREAD_ID,
        String.valueOf(threadIndex));
      ResultSet resultSet = statement.executeQuery(query);
      if (resultSet.next()) {
        return resultSet.getLong(1);
      }
      return 0;
    } catch (Exception e) {
      logger.error("getPartitionCounterFromDB() :: Exception while retrieving sources:", e);
      return 0;
    }
  }

  public static ResultSet getDevicesFromDB(MySqlDatabase database, int threadIndex, int batchSize,
    long startDeviceId,
    long endDeviceId) {
    try {
      Statement statement = database.createReadStatement();
      statement.setFetchSize(batchSize);
      String query = devicesReadQuery
        .replaceAll(REPLACE_THREAD_ID, String.valueOf(threadIndex))
        .replaceAll(REPLACE_START_DEVICE_ID, String.valueOf(startDeviceId))
        .replaceAll(REPLACE_END_DEVICE_ID, String.valueOf(endDeviceId));
      return statement.executeQuery(query);
    } catch (Exception e) {
      logger.error("getDevicesFromDB() :: Exception while retrieving devices:", e);
      return null;
    }
  }

  public static MySQLPreparedStatement getCampaignImpressionPST(MySqlDatabase database) {
    try {
      return new MySQLPreparedStatement(database, addImpressionDataSQL);
    } catch (Exception e) {
      logger.error("getCampaignImpressionPST() :: Exception while retrieving PST:", e);
      return null;
    }
  }

  public static boolean addCampaignImpressionData(MySQLPreparedStatement pst,
    ImpressionObject impressionObject) {
    String logType = null;
    if(LogType.getLogType(impressionObject.getLogType()) != null){
      logType = LogType.getLogType(impressionObject.getLogType()).logType;
    }
    try {
      pst.setInt(1, Utils.timestampToDays(impressionObject.getLogTimestamp()));
      pst.setInt(2, impressionObject.getCampaignId());
      pst.setString(3,logType);
      pst.setInt(4, impressionObject.getThreadId());
      pst.setLong(5, impressionObject.getDeviceIdLong());
      pst.setLong(6, impressionObject.getMobileObjectId());
      pst.addBatch();
    } catch (Exception e) {
      logger.error("addCampaignImpressionData() :: Exception while updating db :", e);
      return false;
    }
    return true;
  }

  public static ResultSet getAerospikeDeviceData(MySqlDatabase database, int batchSize,
    String threadIndex,Datacenter dc) {
    try {
      Statement statement = database.createReadStatement();
      statement.setFetchSize(batchSize);
      String query = String.format(aerospikeDataReadQuery, batchSize)
        .replaceAll(REPLACE_THREAD_ID, threadIndex)
        .replaceAll(REPLACE_DATA_CENTER,dc.getValue());
      return statement.executeQuery(query);
    } catch (Exception e) {
      logger.error("getAerospikeDeviceData() :: Exception while retrieving aerospike data:", e);
      return null;
    }
  }

  public static MySQLPreparedStatement getUpdateAerospikeDataPST(MySqlDatabase database,
    String threadIndex,Datacenter dc) {
    try {
      String query = aerospikeDataUpdateQuery
        .replaceAll(REPLACE_THREAD_ID, threadIndex)
          .replaceAll(REPLACE_DATA_CENTER,dc.getValue());
      return new MySQLPreparedStatement(database, query);
    } catch (Exception e) {
      logger.error("getUpdateSourcesEOFMetaPST() :: Exception while retrieving PST:", e);
      return null;
    }
  }

  public static void updateAerospikeData(MySQLPreparedStatement pst, long startId, long endId) {
    try {
      pst.setLong(1, startId);
      pst.setLong(2, endId);
      pst.addBatch();
    } catch (Exception e) {
      logger.error("updateAerospikeData() :: Exception while updating db :", e);
    }
  }

  public static ResultSet getQueryResults(MySqlDatabase database, String query) {
    try {
      Statement statement = database.createReadStatement();
      return statement.executeQuery(query);
    } catch (Exception e) {
      logger.error("getQueryResults() :: Exception while retrieving data:", e);
      return null;
    }
  }
}