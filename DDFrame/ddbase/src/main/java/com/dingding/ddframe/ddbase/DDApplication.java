package com.dingding.ddframe.ddbase;


import android.app.Application;

/**
 * 全局Application
 */
public class DDApplication extends Application {
    private static DDApplication instance;

    public static DDApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
