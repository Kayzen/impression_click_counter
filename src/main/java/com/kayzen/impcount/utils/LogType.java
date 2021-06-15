package com.kayzen.impcount.utils;

import jdk.nashorn.internal.objects.annotations.Getter;

public enum LogType {
  IMPRESSION(Constants.IMPRESSION),
  CLICK(Constants.CLICK);

  String logType;
  LogType(String logType) {
    this.logType = logType;
  }

  public static LogType getLogType(int id) {
    switch (id) {
      case 2:
        return IMPRESSION;
      case 3:
        return CLICK;
    }
    return null;
  }

}
