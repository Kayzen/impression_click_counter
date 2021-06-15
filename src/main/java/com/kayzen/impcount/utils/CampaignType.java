package com.kayzen.impcount.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CampaignType {
  CPM(Constants.CPM, 1),
  CPC(Constants.CPC, 2),
  CPI(Constants.CPI, 3);

  private String value;
  private int id;

  public static CampaignType getCampaignType(String ct) {
    switch (ct) {
      case Constants.CPM:
        return CPM;
      case Constants.CPC:
        return CPC;
      case Constants.CPI:
        return CPI;
    }
    return null;
  }
}
