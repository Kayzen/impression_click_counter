package com.kayzen.impcount.aerospike;


import com.kayzen.impcount.aerospike.api.Syncbatchupdate.SyncBatchUpdateRequest;
import java.sql.SQLException;

public interface AerospikeProcessor {

  void processMySQLData(MySQLAerospikeData mySQLAerospikeData) throws SQLException;

  SyncBatchUpdateRequest getBatchUpdateRequest(String type);

}
