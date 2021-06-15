package com.kayzen.impcount.utils;

public enum AerospikeDataFields {
  ID("id"),
  APP_OBJECT_ID("app_object_id"),
  ACTION("action"),
  IMPRESSION_CLICK_COUNT("imp_click_count"),
  LOG_TYPE("logType"),
  DEVICE_ID_SHA1("device_id_sha1");

  private String value;
  private AerospikeDataFields(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
