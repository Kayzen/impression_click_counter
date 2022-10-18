package com.kayzen.impcount.aerospike;

import com.kayzen.impcount.aerospike.api.Binvalue.BinData;
import com.kayzen.impcount.aerospike.api.Syncbatchupdate.SyncBatchUpdateRequest;
import com.kayzen.impcount.aerospike.api.Syncbatchupdate.SyncBatchUpdateRequest.row;
import com.kayzen.impcount.utils.Constants;
import com.kayzen.impcount.utils.Utils;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AerospikeImpressionCountPProcessor implements AerospikeProcessor {

  private static final Logger logger = LoggerFactory.getLogger(AerospikeImpressionCountPProcessor.class.getName());
  //Map<String, List<String>> aerospikeDataMap;
  private Map<String,Map<Long,Long>> addMapImp;
  private Map<String,Map<Long,Long>> removeMapImp;
  private Map<String,Map<Long,Long>> addMapClick;
  private Map<String,Map<Long,Long>> removeMapClick;
  private List<row> clickRows;
  private List<row> impRows;


  public AerospikeImpressionCountPProcessor() {
    impRows = new LinkedList<>();
    clickRows = new LinkedList<>();
    addMapImp = new HashMap<>();
    removeMapImp = new HashMap<>();
    addMapClick = new HashMap<>();
    removeMapClick = new HashMap<>();
  }

  @Override
  public void processMySQLData(MySQLAerospikeData mySQLAerospikeData) throws SQLException {
    MySQLAeroImpressionCountData mySQLData = (MySQLAeroImpressionCountData) mySQLAerospikeData;
    if(mySQLData.getLogType().equals(Constants.IMPRESSION)) {
      addToMap(mySQLData,addMapImp,removeMapImp);
    }else if (mySQLData.getLogType().equals(Constants.CLICK)) {
      addToMap(mySQLData,addMapClick,removeMapClick);
    }
  }

  private void addToMap(MySQLAeroImpressionCountData mySQLData, Map<String, Map<Long, Long>> addMap,
      Map<String, Map<Long, Long>> removeMap) throws SQLException {
    String key = mySQLData.getSha1();
    String action = mySQLData.getAction();
    long appObjectId = mySQLData.getAppObjectId();
    if (action.equals("add")) {
      if (removeMap.containsKey(key) && removeMap.get(key).containsKey(appObjectId)) {
        removeMap.get(key).remove(appObjectId);
      }
      if (addMap.get(key) == null) {
        addMap.put(key, new HashMap<>());
      }
      addMap.get(key).put(appObjectId, mySQLData.getImpressionClickCount());
    } else if (action.equals("remove")) {
      if (addMap.containsKey(key) && addMap.get(key).containsKey(appObjectId)) {
        addMap.get(key).remove(appObjectId);
      }
      if (removeMap.get(key) == null) {
        removeMap.put(key, new HashMap<>());
      }
      removeMap.get(key).put(appObjectId, mySQLData.getImpressionClickCount());
    }
  }

  @Override
  public SyncBatchUpdateRequest getBatchUpdateRequest(String type) {
    String binName = null;
    List<row> rows = null;
    if(type.equals(Constants.IMPRESSION)){
      buildRows(addMapImp,removeMapImp,impRows);
      binName = Constants.IMPRESSION_BIN;
      rows = impRows;
    }else if (type.equals(Constants.CLICK)){
      buildRows(addMapClick,removeMapClick,clickRows);
      binName = Constants.CLICK_BIN;
      rows = clickRows;
    }


    return SyncBatchUpdateRequest.newBuilder()
        .addAllRows(rows)
        .setGroup(Constants.SET)
        .setColumn(binName)
        .setReqcount(rows.size())
        .build();
  }

  private void buildRows(
      Map<String, Map<Long, Long>> addMap,
      Map<String, Map<Long, Long>> removeMap,
      List<row> rows) {
    Set<String> appendSet = new HashSet<>();
    Set<String> removeSet = new HashSet<>();

    for(Entry<String,Map<Long,Long>> entry : addMap.entrySet()) {
      appendSet.clear();
      String key = entry.getKey();
      for (Entry<Long,Long> valueEntry : entry.getValue().entrySet()) {
        appendSet.add(valueEntry.getKey() + ":" + valueEntry.getValue());
      }
      String appendString = Utils.join(appendSet);
      rows.add(row.newBuilder()
          .setKey(key)
          .setOption(Constants.APPEND_MAP)
          .setValue(BinData.newBuilder().setValue(appendString)).build());
    }


    for(Entry<String,Map<Long,Long>> entry : removeMap.entrySet()) {
      removeSet.clear();
      String key = entry.getKey();
      for (Entry<Long,Long> valueEntry : entry.getValue().entrySet()){
        removeSet.add(valueEntry.getKey() + ":" + valueEntry.getValue());
      }
      String removeString = Utils.join(removeSet);
      rows.add(row.newBuilder()
          .setKey(key)
          .setOption(Constants.REMOVE_MAP)
          .setValue(BinData.newBuilder().setValue(removeString)).build());
    }
  }

  @AllArgsConstructor
  @Getter
  private static class CounterData {
    private long impressionCount;
    private long clickCount;
  }
}
