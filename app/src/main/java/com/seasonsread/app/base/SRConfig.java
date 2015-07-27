package com.seasonsread.app.base;

import android.util.Log;

import com.seasonsread.app.util.LogUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class SRConfig {
  public static final boolean DEV_BUILD = true;


  private static final String DEFAULT_URL;
  static {
    if (DEV_BUILD) {

      DEFAULT_URL = Urls.BASIC_URL;
    
    } else {
      DEFAULT_URL = Urls.BASIC_URL;
    }
    try {
      DEFAULT_WEB_URL = new URL(DEFAULT_URL);
    } catch (MalformedURLException e) {
      DEFAULT_WEB_URL = null;
      LogUtils.e("parse Default URL error %s", e);
    }
  }

  public static URL DEFAULT_WEB_URL;
}
