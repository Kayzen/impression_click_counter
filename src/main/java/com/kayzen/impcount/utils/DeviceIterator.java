package com.kayzen.impcount.utils;

import com.applift.platform.commons.db.MySqlDatabase;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeviceIterator {

  private static int queryBatch = Constants.HUNDRED_THOUSAND;
  private ResultSet devicesResultSet;
  private MySqlDatabase database;
  private int threadIndex;
  private int batchSize;
  private long startDeviceId;


  public DeviceIterator(MySqlDatabase database, int threadIndex, int batchSize) {
    this(database, threadIndex, batchSize, 0);
  }

  public DeviceIterator(MySqlDatabase database, int threadIndex, int batchSize,
      long startDeviceId) {
    this.database = database;
    this.threadIndex = threadIndex;
    this.batchSize = batchSize;
    this.devicesResultSet = null;
    this.startDeviceId = startDeviceId;
  }

  public boolean next() throws SQLException {
    return ((devicesResultSet != null && devicesResultSet.next()) || getNextResultSet());
  }

  public String getSha1() throws SQLException {
    return devicesResultSet.getString(1);
  }

  public long getDeviceId() throws SQLException {
    return devicesResultSet.getLong(2);
  }

  private boolean getNextResultSet() throws SQLException {
    close();
    long endDeviceId = startDeviceId + queryBatch;
    devicesResultSet = MySqlDbUtils
        .getDevicesFromDB(database, threadIndex, batchSize, startDeviceId, endDeviceId);
    startDeviceId = endDeviceId;
    return (devicesResultSet != null && devicesResultSet.next());
  }

  public void close() throws SQLException {
    if (!(devicesResultSet == null || devicesResultSet.isClosed())) {
      devicesResultSet.close();
    }
  }

}
