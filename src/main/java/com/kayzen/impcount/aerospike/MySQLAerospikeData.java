package com.kayzen.impcount.aerospike;

import com.applift.platform.commons.db.MySqlDatabase;
import com.applift.platform.commons.db.statement.MySQLPreparedStatement;
import com.kayzen.impcount.utils.AerospikeDataFields;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public abstract class MySQLAerospikeData {

  private Logger logger;
  MySqlDatabase database;
  int batchSize;
  ResultSet asResultSet;
  MySQLPreparedStatement preparedStatement;
  long currentMin = -1;
  long currentMax = -1;
  private long totalToBeUpdated;
  private long totalFetched;
  private long lastFetchTime;


  public MySQLAerospikeData(MySqlDatabase database, int batchSize, Logger logger) {
    this.database = database;
    this.batchSize = batchSize;
    this.totalFetched = 0;
    this.totalToBeUpdated = 0;
    preparedStatement = null;
    this.logger = logger;
  }

  public boolean next() throws SQLException {
    if ((asResultSet != null && asResultSet.next())) {
      long currentId = getId();
      if (currentMin == -1) {
        currentMin = currentMax = currentId;
      } else {
        if (currentMax + 1 == currentId) {
          currentMax++;
        } else {
          addToUpdateAeroData();
          currentMin = currentMax = currentId;
        }
      }
      totalFetched++;
      totalToBeUpdated++;
      return postNext();
    }
    return false;
  }

  public Long getId() throws SQLException {
    return asResultSet.getLong(AerospikeDataFields.ID.getValue());
  }

  public boolean nextResultSet() throws SQLException {
    close();
    long startTime = System.currentTimeMillis();
    getResultSet();
    lastFetchTime = System.currentTimeMillis() - startTime;
    if (asResultSet != null && asResultSet.isBeforeFirst()) {
//    if (asResultSet != null ) {
      setUpdatePreparedStatement();
      currentMax = currentMin = -1;
      return true;
    }
    return false;
  }

  void close() throws SQLException {
    if (!(asResultSet == null || asResultSet.isClosed())) {
      asResultSet.close();
    }
  }

  public void markProcessed() {
    if (currentMin != -1) {
      addToUpdateAeroData();
    }
    long startTime = System.currentTimeMillis();
    boolean status = markProcessedInDB();
    if (!status) {
      logger.error("Failed to mark data as processed" + preparedStatement);
    }
    long updateTime = System.currentTimeMillis() - startTime;
    StatsRecorder.updateMySQLStats(totalToBeUpdated, totalFetched, lastFetchTime, updateTime);
    this.totalFetched = 0;
    this.totalToBeUpdated = 0;
  }

  abstract void getResultSet();

  abstract void setUpdatePreparedStatement();

  abstract void addToUpdateAeroData();

  boolean markProcessedInDB() {
    return database.batchQuery(preparedStatement, 2);
  }

  boolean postNext() throws SQLException {
    return true;
  }
}
