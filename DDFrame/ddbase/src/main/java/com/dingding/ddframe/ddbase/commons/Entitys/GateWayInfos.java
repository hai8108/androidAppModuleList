package com.dingding.ddframe.ddbase.commons.Entitys;

import java.util.LinkedHashMap;

/**
 * Created by zzz on 15/8/18.
 */
public class GateWayInfos implements BaseInfos {

    static GateWayInfos infos ;
    public static GateWayInfos getInstance() {
        if (infos == null) {
            infos = new GateWayInfos();
        }
        return infos;
    }
    @Override
    public String getTimestamp() {
        return null;
    }

    @Override
    public String getToken() {
        return null;
    }

    @Override
    public String getUuid() {
        return null;
    }

    @Override
    public String getDeviceName() {
        return null;
    }

    @Override
    public int getFrom() {
        return 0;
    }

    @Override
    public String getDeviceId() {
        return null;
    }

    @Override
    public int getCityId() {
        return 0;
    }

    @Override
    public String getAppVersion() {
        return null;
    }

    @Override
    public String getAppId() {
        return null;
    }

    @Override
    public String getAccountId() {
        return "";
    }

    @Override
    public int getAccountType() {
        return 0;
    }

    @Override
    public LinkedHashMap getBase() {
        return null;
    }

    @Override
    public LinkedHashMap getNoLogin() {
        return null;
    }

}
