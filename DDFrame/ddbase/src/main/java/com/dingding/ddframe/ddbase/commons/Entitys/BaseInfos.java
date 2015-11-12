package com.dingding.ddframe.ddbase.commons.Entitys;

import java.util.LinkedHashMap;

/**
 * Created by zzz on 15/8/18.
 */
public interface BaseInfos {

    public String getTimestamp();

    public String getToken();

    public String getUuid();

    public String getDeviceName();

    public int getFrom();

    public String getDeviceId();

    public int getCityId();

    public String getAppVersion();

    public String getAppId();

    public String getAccountId();

    public int getAccountType();

    public LinkedHashMap getBase();
    
    public LinkedHashMap getNoLogin();
}
