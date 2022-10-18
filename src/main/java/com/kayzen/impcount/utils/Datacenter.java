package com.kayzen.impcount.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Datacenter {
  DCA(Constants.AERO_DCA, 1),
  HKG(Constants.AERO_HKG, 2),
  AMS(Constants.AERO_AMS, 3);

  private String value;
  private int id;

  public static Datacenter getDatacenter(String dc) {
    switch (dc) {
      case Constants.AERO_DCA:
        return DCA;
      case Constants.AERO_HKG:
        return HKG;
      case Constants.AERO_AMS:
        return AMS;
    }
    return null;
  }
}
