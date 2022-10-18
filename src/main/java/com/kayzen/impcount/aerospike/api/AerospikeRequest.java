package com.kayzen.impcount.aerospike.api;

import com.applift.platform.commons.http.AsyncHttpService;
import com.applift.platform.commons.utils.Config;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.googlecode.protobuf.format.JsonFormat;
import com.kayzen.impcount.aerospike.api.Syncbatchupdate.SyncBatchUpdateRequest;
import com.kayzen.impcount.utils.Constants;
import com.kayzen.impcount.utils.Datacenter;
import java.util.concurrent.CompletableFuture;

import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AerospikeRequest {

  private static final Logger logger = LoggerFactory.getLogger(AerospikeRequest.class.getName());

  private Config aeroConfig;
  private SyncBatchUpdateRequest protoSyncRequest;
  private Datacenter dc;

  public AerospikeRequest(Config confProperties, SyncBatchUpdateRequest syncBatchUpdateRequest,Datacenter dc) {
    aeroConfig = confProperties;
    protoSyncRequest = syncBatchUpdateRequest;
    this.dc = dc;
    //logger.info(protoSyncRequestToJson());
  }

  private byte[] requestToByteStream() {
    return protoSyncRequest.toByteArray();
  }

  public boolean process() {
    boolean returnStatus = false;
    if (protoSyncRequest == null) {
      return false;
    }
    if (protoSyncRequest.getRowsCount() == 0) {
      return true;
    }
    if (logger.isDebugEnabled()) {
      logger.debug("Sending Requests to Aerospike API");
      logger.debug("Aerospike-API Request Payload : " + protoSyncRequestToJson());
    }
    byte[] reqBody = requestToByteStream();
    int rowCount = protoSyncRequest.getRowsCount();
    int retries = aeroConfig.getInt(Constants.NO_OF_RETRIES);

    RequestProcessor requestProcessor = null;
    long startTime = System.currentTimeMillis();

    if(dc == Datacenter.DCA) {
      requestProcessor = new RequestProcessor(
          aeroConfig.getString(Constants.DCA_AERO_ENDPOINT), reqBody, rowCount, retries,
          Datacenter.DCA,aeroConfig);
    }else if(dc == Datacenter.HKG) {
      requestProcessor = new RequestProcessor(
          aeroConfig.getString(Constants.HKG_AERO_ENDPOINT), reqBody, rowCount, retries,
          Datacenter.HKG,aeroConfig);
    }else if(dc == Datacenter.AMS) {
      requestProcessor = new RequestProcessor(
          aeroConfig.getString(Constants.AMS_AERO_ENDPOINT), reqBody, rowCount, retries,
          Datacenter.AMS,aeroConfig);
    }

    if (requestProcessor != null) {
      CompletableFuture<Boolean> dataCenterBool = requestProcessor.process().thenApply((status) -> {
        if (status) {
          //TODO-dgpatil-stats commented stats
          //StatsRecorder.updateAerospikeStats(0, 0, rowCount,
            //  System.currentTimeMillis() - startTime, 0, 0);
        }
        return status;
      });

      returnStatus = dataCenterBool.join();
    }

    return returnStatus;
  }

  private String protoSyncRequestToJson() {
    return (new JsonFormat()).printToString(protoSyncRequest);
  }

  class RequestProcessor {

    private String requestUrl;
    private byte[] requestBody;
    private int rowCount;
    private int numRetries;
    private CompletableFuture<Boolean> responseValidated;
    private Datacenter identifier;
    private Config aeroConfig;

    private RequestProcessor(String url, byte[] body, int count, int retries, Datacenter identifier,
        Config aeroConfig) {
      this.requestUrl = url;
      this.requestBody = body;
      this.rowCount = count;
      this.numRetries = retries;
      this.responseValidated = new CompletableFuture<>();
      this.identifier = identifier;
      this.aeroConfig = aeroConfig;
    }

    CompletableFuture<Boolean> process() {
      return process(numRetries);
    }

    CompletableFuture<Boolean> process(int numRetries) {
      AsyncHttpService asyncHttpService = AsyncHttpService.getInstance();
      CompletableFuture<Response> res;
      String proxyHost = aeroConfig.getString(Constants.PROXY_HOST);
      int proxyPort = aeroConfig.getInt(Constants.PROXY_PORT);
      //if(identifier == Datacenter.DCA){
        res = asyncHttpService
            .sendPostRequest(requestUrl, requestBody, identifier.getValue());
        //logger.info(protoSyncRequestToJson());
     /* }else{
        res = asyncHttpService
            .sendPostRequest(requestUrl, requestBody, identifier.getValue(),proxyHost,proxyPort);
      }*/
          res.whenCompleteAsync((response, error) -> {
            if (error != null || response == null || !validResponse(response)) {
              if (numRetries > 0) {
                process(numRetries - 1);
              } else {
                if (error != null) {
                  logger.error("Aerospike Update Failed:" + identifier, error);
                } else if (response != null) {
                  logger.error("Aerospike Update Failed:" + identifier + response.toString());
                } else {
                  logger.error("Aerospike Update Failed:" + identifier + protoSyncRequestToJson());
                }
                responseValidated.complete(false);
              }
            } else {
              responseValidated.complete(true);
            }
          });
      return responseValidated;
    }

    boolean validResponse(Response response) {
      String respString = response.getResponseBody();
      if (logger.isDebugEnabled()) {
        logger.debug("Aerospike Response : " + response);
      }
      if (response.getStatusCode() != 200) {
        return false;
      }
      if (Strings.isNullOrEmpty(respString)) {
        return false;
      }
      Gson gson = new Gson();
      AerospikeResponse aerospikeResponse = gson.fromJson(respString, AerospikeResponse.class);
      return aerospikeResponse.isSuccessful(this.rowCount);
    }
  }
}
