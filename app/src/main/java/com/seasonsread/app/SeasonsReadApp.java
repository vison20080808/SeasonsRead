package com.seasonsread.app;

import android.app.Application;
import android.content.Context;

import com.seasonsread.app.manager.WebRequestManager;

/**
 * Created by ZhanTao on 1/9/15.
 */
public class SeasonsReadApp extends Application {
    private static Context mAppContext;
    @Override
    public void onCreate() {
        super.onCreate();
        WebRequestManager.init(this);
        mAppContext = getApplicationContext();
    }
    public static Context getAppContext(){
        return mAppContext;
    }
}
