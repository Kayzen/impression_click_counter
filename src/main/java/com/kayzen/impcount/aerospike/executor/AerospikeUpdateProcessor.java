package com.kayzen.impcount.aerospike.executor;

import com.applift.platform.commons.db.DBContext;
import com.applift.platform.commons.db.MySqlDatabase;
import com.applift.platform.commons.enums.Environment;
import com.applift.platform.commons.utils.Config;
import com.kayzen.impcount.aerospike.AerospikeImpressionCountPProcessor;
import com.kayzen.impcount.aerospike.AerospikeProcessor;
import com.kayzen.impcount.aerospike.MySQLAeroImpressionCountData;
import com.kayzen.impcount.aerospike.MySQLAerospikeData;
import com.kayzen.impcount.aerospike.api.AerospikeRequest;
import com.kayzen.impcount.aerospike.api.Syncbatchupdate.SyncBatchUpdateRequest;
import com.kayzen.impcount.utils.Constants;
import com.kayzen.impcount.utils.Datacenter;
import java.io.Serializable;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AerospikeUpdateProcessor implements Serializable, Runnable {

  private static final long serialVersionUID = -863674547031919840L;
  private static Logger logger = LoggerFactory.getLogger(AerospikeUpdateProcessor.class.getName());
  private final String identifier;
  private MySqlDatabase database;
  private boolean shutdown;
  private MySQLAerospikeData mySQLAerospikeData;
  private AerospikeProcessor aerospikeProcessor;
  private Config aeroConfig;
  private int threadIndex;
  private Datacenter dataCenter;

  public AerospikeUpdateProcessor(MySqlDatabase database, int batchSize,
    Config aeroConfig, int threadIndex, Datacenter dc) {
    this.threadIndex = threadIndex;
    this.identifier = "AerospikeUpdateProcessor threadIndex:" + threadIndex;
    this.aeroConfig = aeroConfig;
    this.dataCenter = dc;
    try {
      this.database = database;
      mySQLAerospikeData = new MySQLAeroImpressionCountData(database, batchSize, threadIndex,dataCenter);
    } catch (Exception e) {
      logger.error("Exception occurred in constructor AerospikeUpdateProcessor. Exception:", e);
      System.exit(1);
    }
    logger.info("Starting" + identifier);
  }

  @Override
  public void run() {
    while (!shutdown) {
      try {
        if (mySQLAerospikeData.nextResultSet()) {
          aerospikeProcessor = new AerospikeImpressionCountPProcessor();
          while (mySQLAerospikeData.next()) {
            aerospikeProcessor.processMySQLData(mySQLAerospikeData);
          }

          //TODO-dgpatil uncomment this for testing
          //SyncBatchUpdateRequest syncBatchUpdateRequestImp = aerospikeProcessor.getBatchUpdateRequest(Constants.IMPRESSION);
          //SyncBatchUpdateRequest syncBatchUpdateRequestClick = aerospikeProcessor.getBatchUpdateRequest(Constants.CLICK);

          logger.info("Completed build aerospike data");
          //TODO-dgpatil comment beolw block for  aerospike data testing
          AerospikeRequest aerospikeRequestImp = new AerospikeRequest(aeroConfig, aerospikeProcessor.getBatchUpdateRequest(Constants.IMPRESSION),dataCenter);
          AerospikeRequest aerospikeRequestClick = new AerospikeRequest(aeroConfig, aerospikeProcessor.getBatchUpdateRequest(Constants.CLICK),dataCenter);
          if (aerospikeRequestImp.process() && aerospikeRequestClick.process()) {
            mySQLAerospikeData.markProcessed();
          }

        } else {
          logger.info(identifier + "Sleeping since no data available");
          Thread.sleep(Constants.FIVE_HUNDRED_MILLS);
        }
      } catch (SQLException | InterruptedException e) {
        logger.error("Exception while processing Aerospike data in " + identifier, e);
      }
    }
  }

  public void shutdown() {
    shutdown = true;
  }

}