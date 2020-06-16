package com.kayzen.impcount.utils;

import com.google.common.base.Splitter;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class Constants {

  public static final String ENV_PRODUCTION = "production";
  public static final String ENV_STAGING = "staging";
  public static final String ENV_TEST = "test";

  public static final String NUMBER_OF_READ_THREADS = "numberOfReadThreads";
  public static final String NUMBER_OF_WRITE_THREADS = "numberOfWriteThreads";
  public static final String PROCESS_ONLY_DEVICES = "processOnlyDevices";
  public static final String ENVIRONMENT = "environment";
  public static final String KAFKA_CONFIG_LOCATION = "kafka";
  public static final String LOG_LOCATION = "logLocation";
  public static final String LOG_PATTERN = "logPattern";
  public static final String MAP_LOCATION = "mapLocation";
  public static final String BATCH_SIZE = "batchSize";
  public static final String DATA_INGESTION_METHOD = "dataIngestionMethod";
  public static final String DB_HOST_NAME = "dbHostName";


  public static final long FIVE_HUNDRED_MILLS = 500;
  public static final long ONE_SECOND_MILLS = 1_000;
  public static final long TWO_SECONDS_MILLS = 2_000;
  public static final long FIVE_SECONDS_MILLS = 5_000;
  public static final long TEN_SECONDS_MILLS = 10_000;
  public static final long THIRTY_SECONDS_MILLS = 30_000;
  public static final int SIXTY_SECONDS_MILLIS = 60_000;
  public static final int FIVE_MINUTES_MILLIS = 300_000;
  public static final long ONE_DAY_MILLS = 86_400_000;
  public static final int SHARED_OBJECT_QUEUE_SIZE = 5_000;
  public static final int HUNDRED_THOUSAND = 100_000;
  public static final int TEN_MILLION = 10_000_000;

  //TODO-dgpatil comment for testing
  public static final int FIFTY_MILLION = 50_000_000;
  public static final int TWO_HUNDRED_MILLION = 200_000_000;

  //TODO-dgpatil uncomment for testing
  //public static final int FIFTY_MILLION = 500;
  //public static final int TWO_HUNDRED_MILLION = 200;

  public static final long LONG_ZERO = 0l;
  public static final String CAMPAIGN_TYPE = "campaignType";
  public static final String CLICK = "click";
  public static final String IMPRESSION = "impression";
  public static Splitter lineSplitter = Splitter.on('\n').trimResults();


  public static final long KAFKA_WAIT_TIME_MILLS = FIVE_SECONDS_MILLS;
  public static final int KAFKA_OFFSET_ERROR = -1;
  public static final String LEADER_LOOKUP_CLIENT_ID = "impcount_leaderLookup";

  public static final String IMPRESSION_COUNTER = "impression_counter";
  public static final String MAPDB_RESTORER = "mapdbRestorer";
  public static final String AERO_IMPRESSION_COUNTER = "aero_impression_counter";
  public static final String IMPRESSION_COUNTER_LOG_FILENAME = "/logs/impression_counter";
  public static final String AERO_IMPRESSION_COUNTER_LOG_FILENAME = "/logs/aero_impression_counter";

  public static final String APPLICATION_CONF = "impressionCounterBatchIngestor.conf";
  public static final String AERO_APPLICATION_CONF = "aerospikeUpdater.conf";
  public static final String KAFKA = "kafka";
  public static final String AERO = "aero";

  public static final String TOPICLIST = "topic_list";
  public static final String BROKER_HOSTS = "brokerHosts";
  public static final String BROKER_PORTS = "brokerPorts";
  public static final String KAFKA_CONSUMER_TIMEOUT = "consumer_timeout";
  public static final String KAFKA_CONSUMER_BUFFER_SIZE = "consumer_buffer_size";
  public static final String KAFKA_CONSUMER_FETCH_SIZE = "consumer_fetch_size";
  public static final String KAFKA_CONSUMER_START_FROM_LATEST_COMMIT = "consumer_start_from_latest_commit";
  public static final String KAFKA_CONSUMER_START_FROM_OLDEST_COMMIT = "consumer_start_from_oldest_commit";
  public static final String KAFKA_CONSUMER_OFFSET_COMMIT_GROUP_ID = "consumer_offset_commit_group_id";
  public static final String KAFKA_CLIENT_SUFFIX = "client_suffix";

  public static final short OFFSET_COMMIT_VERSION = 0;
  public static final int CORRELATION_ID = 1;

  // Json Keys
  public static final String LOG_ENTRY_TYPE = "log_type";
  public static final String BID_ID = "bid_id";
  public static final String CAMPAIGN_ID = "campaign_id";
  public static final String DPID_SHA1 = "dpid_sha1";
  public static final String TIMESTAMP = "timestamp";
  public static final String MOBILE_OBJECT_ID = "mobile_object_id";
  public static final String ADV_ID = "advertiser_id";
  public static final String CAMPAIGN_TYPE_RAW = "campaign_type";

  public static final int eventTypeImpression = 2;
  public static final int eventTypeClick = 3;

  public static HashFunction hf = Hashing.murmur3_128();

  public static final String DEVICE_MAP_THREAD = "/DeviceMap_Thread-";
  public static final String MAP = ".map";

  public static final String IMP_BIDS_MAP_FILE = "/ImpressionBids-REPLACE_DATE.map";
  public static final String CLICK_BIDS_MAP_FILE = "/ClickBids-REPLACE_DATE.map";
  public static final String THREAD_PARTITION_MAP_FILE = "/ThreadPartition.map";

  public static final String CPM = "cpm";
  public static final String CPC = "cpc";
  public static final String CPI = "cpi";

  public static final String NO_OF_RETRIES = "noOfRetries";
  public static final String DCA_AERO_ENDPOINT = "dcaAeroSpikeEndPoint";
  public static final String HKG_AERO_ENDPOINT = "hkgAeroSpikeEndPoint";
  public static final String AMS_AERO_ENDPOINT = "amsAeroSpikeEndPoint";
  public static final String AERO_DCA = "dca";
  public static final String AERO_HKG = "hkg";
  public static final String AERO_AMS = "ams";
  public static final String PROXY_HOST = "proxyHost";
  public static final String PROXY_PORT = "proxyPort";

  public static final String SET = "deviceid";
  public static final String IMPRESSION_BIN = "imp_count";
  public static final String CLICK_BIN = "clk_count";
  public static final String APPEND_MAP = "map-append";
  public static final String REMOVE_MAP = "map-remove";

}
