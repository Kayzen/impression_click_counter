package com.kayzen.impcount.aerospike;

import com.applift.platform.commons.db.MySqlDatabase;
import com.kayzen.impcount.utils.AerospikeDataFields;
import com.kayzen.impcount.utils.Datacenter;
import com.kayzen.impcount.utils.MySqlDbUtils;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class MySQLAeroImpressionCountData extends MySQLAerospikeData {

  private static Logger logger = Logger.getLogger(MySQLAeroImpressionCountData.class.getName());
  private String threadIndex;
  private Datacenter dataCenter;

  public MySQLAeroImpressionCountData(MySqlDatabase database, int batchSize, int threadIndex,Datacenter dc) {
    super(database, batchSize, logger);
    this.threadIndex = String.valueOf(threadIndex);
    dataCenter = dc;
  }

  @Override
  void getResultSet() {
    asResultSet = MySqlDbUtils.getAerospikeDeviceData(database, batchSize, threadIndex,dataCenter);
  }

  @Override
  void setUpdatePreparedStatement() {
    preparedStatement = MySqlDbUtils.getUpdateAerospikeDataPST(database, threadIndex,dataCenter);
  }

  @Override
  void addToUpdateAeroData() {
    MySqlDbUtils.updateAerospikeData(preparedStatement, currentMin, currentMax);
  }

  @Override
  boolean markProcessedInDB() {
    return database.batchQuery(preparedStatement, 2);
  }

  String getSha1() throws SQLException {
    return asResultSet.getString(AerospikeDataFields.DEVICE_ID_SHA1.getValue());
  }

  String getAction() throws SQLException {
    return asResultSet.getString(AerospikeDataFields.ACTION.getValue());
  }

  long getAppObjectId() throws SQLException {
    return asResultSet.getLong(AerospikeDataFields.APP_OBJECT_ID.getValue());
  }

  long getImpressionClickCount() throws SQLException {
    return asResultSet.getLong(AerospikeDataFields.IMPRESSION_CLICK_COUNT.getValue());
  }

  String getLogType() throws SQLException {
    return asResultSet.getString(AerospikeDataFields.LOG_TYPE.getValue());
  }


  private String getRowString() throws SQLException {
    return "MySQLAeroFPAData " + threadIndex + "{" +
      "id=" + getId() +
      ", sha1=" + getSha1() +
      ", action=" + getAction() +
      ", app_object_id=" + getAppObjectId() +
      ", impression_click_count=" + getImpressionClickCount() +
      ", logType=" + getLogType() +
      '}';
  }

}
