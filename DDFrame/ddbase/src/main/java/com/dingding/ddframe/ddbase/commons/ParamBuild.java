package com.dingding.ddframe.ddbase.commons;

import com.alibaba.fastjson.JSON;
import com.dingding.ddframe.ddbase.commons.enums.Req_Stastus;
import com.dingding.ddframe.ddbase.utils.MurmurHash3;
import com.dingding.ddframe.ddbase.utils.Utils;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 走 GetWay 接入层 ，组装入参类
 * Created by zzz on 15/8/14.
 */
public class ParamBuild {

    private static final int BIT_CNT = 16;
    private static final String STUB = "0";
    public static void main(String[] str) {
        /*String param = "hellokitty";
        byte[] bytes = param.getBytes();
        MurmurHash3.LongPair longPair = new MurmurHash3.LongPair();
        MurmurHash3.murmurhash3_x64_128(bytes, 0, param.length(), 20150720, longPair);
        String mumuString = String.valueOf(Long.toHexString(longPair.val1)) + String.valueOf(Long.toHexString(longPair.val2));
        System.out.print(mumuString);*/
        LinkedHashMap baseMap = new LinkedHashMap();
        baseMap.put("accountId",1231231);
        baseMap.put("accountType",13123);
        baseMap.put("appId",12312312321312L);
        baseMap.put("deviceName","我是安卓");
        baseMap.put("from",1);

        Map param = new HashMap();
        param.put("timestamp",0);
        param.put("uuid",0);
        param.put("from",777);

        Map paramMap = new HashMap();
        paramMap.put("timestamp",0);
        paramMap.put("uuid",0);
        paramMap.put("from",0);
        paramMap.put("accountid","nnnnnn");
        paramMap.put("param",param);

        String s = JSON.toJSONString(paramMap);
        String s1 = paramMap.toString();

        String sign = getSign(paramMap, baseMap);
        String sss ="accountId=7060accountType=7appId=2000550695appVersion=21baseCityId=110000deviceId=355848064440657deviceName=samsungfrom=7location=34.44,43.23timestamp=1441529411754token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHBpcmUiOjE2NDA0MTMyNDh9.suK2YIOfiJ1EiqeIo-nnuMAHOjlfs_DDQGTRaMOtFLEuuid=274ab51b-c49f-44f6-9a61-413979345647{\"bizcircleId\":11,\"bizcircleName\":\"1.1立水桥\",\"buildingNo\":\"4号楼\",\"cityId\":310000,\"districtId\":1,\"districtName\":\"1区\",\"floor\":3,\"floorTotal\":30,\"id\":8,\"resblockAlias\":\"Vhhh\",\"resblockId\":3434343,\"resblockName\":\"武清红星国际广场\",\"resourses\":[{\"resourceName\":\"/storage/emulated/0/Pictures/Screenshots/Screenshot_2015-07-07-19-51-01.png\",\"resourcePath\":\"http://192.168.1.233/public/110000/2/2015/9/6/1fc72176-3739-4b03-9ed5-778193d25ce9.png\"},{\"resourceName\":\"/storage/emulated/0/Pictures/Screenshots/Screenshot_2015-07-07-19-50-28.png\",\"resourcePath\":\"http://192.168.1.233/public/110000/2/2015/9/6/872cf4a1-8f10-403f-9831-a15b07cb7b29.png\"}],\"roomNo\":\"301\",\"unit\":\"1单元\"}";
        String security = getSecurity(sss, Req_Stastus.MUM_KEY.getCode());

    }


    private static LinkedHashMap map = new LinkedHashMap();

    private void ParamBuild() {
    }

    public static String buildNoParams() {
        HashMap allMap = new HashMap();
        allMap.put("base", map);
        return allMap.toString();
    }

    public static String getSign(Object entity, LinkedHashMap baseMap) {
        String s = JSON.toJSONString(entity);
//        String entityString = Utils.sortMap(entityMap);
        String base = Utils.map2Param(baseMap);
        String param = base + s;
        return getSecurity(param, Req_Stastus.MUM_KEY.getCode());
    }

    public static String getSign(Map map, LinkedHashMap baseMap) {
        String s = JSON.toJSONString(map);
        String base = Utils.map2Param(baseMap);
        String param = base + s;

        return getSecurity(param, Req_Stastus.MUM_KEY.getCode());
    }

    public static String getSign(Map map, int seed, LinkedHashMap baseMap) {
        String s = JSON.toJSONString(map);
        String base = Utils.map2Param(baseMap);
        String param = base + s;
        String security = getSecurity(param, seed);
        return security;
    }


    public static String getSecurity(String key, int seed) {
        if (key == null) {
            throw new IllegalArgumentException("key is not allowed to be null");
        }
        byte[] bytes = null;
        try {
            bytes = key.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MurmurHash3.LongPair longPair = new MurmurHash3.LongPair();
        MurmurHash3.murmurhash3_x64_128(bytes, 0, bytes.length, seed, longPair);

        String high = Long.toHexString(longPair.val1);
        String low = Long.toHexString(longPair.val2);

        if (high.length() < BIT_CNT) {
            high = getZero(BIT_CNT - high.length()) + high;
        }
        if (low.length() < BIT_CNT) {
            low = getZero(BIT_CNT - low.length()) + low;
        }

        return high + low;

    }
    private static String getZero(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(STUB);
        }
        return sb.toString();
    }

    /**
     * 只有基础参数
     *
     * @param baseMap 基础参数
     * @return sign返回
     */
    public static String getSign(LinkedHashMap baseMap) {
        String base = Utils.map2Param(baseMap);
        return getSecurity(base, Req_Stastus.MUM_KEY.getCode());
    }


    private Map getParam(Object o) {
        Map mo = (Map) JSON.toJSON(o);
        return mo;
    }


}
