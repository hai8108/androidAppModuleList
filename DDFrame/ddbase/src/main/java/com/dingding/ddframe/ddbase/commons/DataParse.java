package com.dingding.ddframe.ddbase.commons;


import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dingding.ddframe.ddbase.DDApplication;
import com.dingding.ddframe.ddbase.commons.enums.Req_Stastus;
import com.dingding.ddframe.ddbase.net.ResultFactory;
import com.dingding.ddframe.ddbase.net.ResultObject;
import com.dingding.ddframe.ddbase.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzz on 14-12-31.
 */
public class DataParse {


    public static void main(String[] args) {
        String s = "{\"message\":\"短信发送成功！\",\"error\":null,\"token\":null,\"data\":null,\"code\":100000}";
        ResultObject resultObject = parseNoClz(s);
    }

    private final static int SUCCESS = 100000;

    private static ResultObject getROwithNull() {
        ResultObject resultObject = ResultFactory.getro();
        resultObject.setCode(-1);
        resultObject.setSuccess(false);
        resultObject.setMessage("网络错误");
        return resultObject;
    }

    private static ResultObject getErrorRO(int code, String msg) {
        ResultObject resultObject = ResultFactory.getro();
        resultObject.setCode(code);
        resultObject.setSuccess(false);
        resultObject.setMessage(msg);
        return resultObject;
    }

    public static ResultObject parseObject(String response, Class<?> clz) {

        if (response == null) {
            return getROwithNull();
        }
        Map<String, Object> map = JSON.parseObject(response);
        Integer code = (Integer) map.get("code");
        String message = (String) map.get("message");
        checkToken(map);

        if (code != SUCCESS) {
            return getErrorRO(code, message);
        } else {
            ResultObject resultObject = ResultFactory.getro();
            Map<String, Object> data = (Map<String, Object>) map.get("data");
            String value = data.toString();
            resultObject.setMessage(message);
            Object o = JSON.parseObject(value, clz);
            resultObject.setSuccess(true);
            resultObject.setObject(o);
            resultObject.setCode(code);
            return resultObject;
        }
    }


    public static ResultObject parseObject(String response, String key) {
        if (response == null) {
            return getROwithNull();
        }
        Map<String, Object> map = JSON.parseObject(response);
        Integer code = (Integer) map.get("code");
        String message = (String) map.get("message");
        checkToken(map);

        if (code != SUCCESS) {
            return getErrorRO(code, message);
        } else {
            ResultObject resultObject = ResultFactory.getro();
            Map<String, Object> data = (Map<String, Object>) map.get("data");
            String value = data.toString();
            resultObject.setMessage(message);
            JSONObject o = JSON.parseObject(value);
            if (o.containsKey(key)) {
                resultObject.setObject(o.get(key));
            }
            resultObject.setSuccess(true);
            resultObject.setCode(code);
            return resultObject;
        }
    }

    public static ResultObject parseMap(String response) {
        if (response == null) {
            return getROwithNull();
        }
        Map<String, Object> map = JSON.parseObject(response);
        if (map == null)
            return getROwithNull();
        Integer code = (Integer) map.get("code");
        String message = (String) map.get("message");
        checkToken(map);
        if (code != SUCCESS) {
            return getErrorRO(code, message);
        } else {
            ResultObject resultObject = ResultFactory.getro();
            Map<String, Object> data = (Map<String, Object>) map.get("data");
            resultObject.setObject(data);
            resultObject.setSuccess(true);
            resultObject.setCode(code);
            return resultObject;
        }
    }

    public static ResultObject parseList(String response, String key, Class<?> clz) {
        if (response == null) {
            return getROwithNull();
        }
        Map<String, Object> map = JSON.parseObject(response);
        Integer code = (Integer) map.get("code");
        String message = (String) map.get("message");
        checkToken(map);
        Map<String, Object> data = (Map<String, Object>) map.get("data");

        if (code != SUCCESS) {
            return getErrorRO(code, message);
        } else {
            ResultObject resultObject = ResultFactory.getro();
            String value = data.get(key).toString();
            List<?> list = JSON.parseArray(value, clz);
            resultObject.setMessage(message);
            resultObject.setSuccess(true);
            resultObject.setObject(list);
            resultObject.setCode(code);
            return resultObject;
        }
    }

    public static ResultObject parseListHasTotal(String response, String key, Class<?> clz, String totalCount) {
        if (response == null) {
            return getROwithNull();
        }
        Map<String, Object> map = JSON.parseObject(response);
        Integer code = (Integer) map.get("code");
        String message = (String) map.get("message");
        checkToken(map);
        Map<String, Object> data = (Map<String, Object>) map.get("data");

        if (code != SUCCESS) {
            return getErrorRO(code, message);
        } else {
            ResultObject resultObject = ResultFactory.getro();
            Map<String, Object> map2 = JSON.parseObject(data.toString());
            Integer count = (Integer) map2.get(totalCount);
            Map<String, Object> listStr = new HashMap<String, Object>();
            if (map2.get(key) != null) {
                String value2 = map2.get(key).toString();
                List<?> list = JSON.parseArray(value2, clz);
                listStr.put("list", list);
            }
            listStr.put(totalCount, count);
            resultObject.setMessage(message);
            resultObject.setSuccess(true);
            resultObject.setObject(listStr);
            resultObject.setCode(code);
            return resultObject;
        }
    }


    private static <T> List<T> parseList(String response, Class<T> clz) {
        Map<String, Object> map = JSON.parseObject(response);
        Integer code = (Integer) map.get("code");
        String message = (String) map.get("message");
        checkToken(map);
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String value = data.get("key").toString();
        List<T> ts = JSON.parseObject(value, new TypeReference<List<T>>() {
        });
        return ts;
    }

    public static ResultObject parseListNoKey(String response, Class<?> clz) {
        if (response == null) {
            return getROwithNull();
        }
        JSONObject mJson = JSON.parseObject(response);
        Integer code = (Integer) mJson.get("code");
        String message = (String) mJson.get("message");
        checkToken(mJson);
        if (code != SUCCESS) {
            return getErrorRO(code, message);
        } else {
            ResultObject resultObject = ResultFactory.getro();
            List<?> list = JSON.parseArray(JSON.toJSONString(mJson), clz);
            resultObject.setMessage(message);
            resultObject.setSuccess(true);
            resultObject.setObject(list);
            resultObject.setCode(code);
            return resultObject;
        }
    }


    public static ResultObject parseAppId(String response) {
        if (response == null) {
            return getROwithNull();
        }
        JSONObject jsonObject = JSON.parseObject(response);
        Integer code = (Integer) jsonObject.get("code");
        String message = (String) jsonObject.get("message");
        String token = (String) jsonObject.get("token");
        if (!StringUtils.isNull(token)) {
            save(token);
        }

        Object data = jsonObject.get("data");

        if (code != SUCCESS) {
            return getErrorRO(code, message);
        } else {
            ResultObject resultObject = ResultFactory.getro();
            resultObject.setMessage(message);
            resultObject.setSuccess(true);
            String s = JSON.toJSONString(data);
            JSONObject jsonObject1 = JSON.parseObject(s);
            Long appId = (Long) jsonObject1.get("appId");
            resultObject.setObject(appId);
            resultObject.setCode(code);
            return resultObject;
        }

    }

    public static ResultObject parseNoClz(String response) {
        if (response == null) {
            return getROwithNull();
        }
        JSONObject jsonObject = JSON.parseObject(response);
        Integer code = (Integer) jsonObject.get("code");
        String message = (String) jsonObject.get("message");
        String token = (String) jsonObject.get("token");
        if (!StringUtils.isNull(token)) {
            save(token);
        }
        if (code != SUCCESS) {
            return getErrorRO(code, message);
        } else {
            ResultObject resultObject = ResultFactory.getro();
            resultObject.setMessage(message);
            resultObject.setSuccess(true);
            resultObject.setCode(code);
            return resultObject;
        }
    }

    public static <T> ResultObject parse2T(String response, String key, T t) {
        if (response == null) {
            return getROwithNull();
        }
        Map<String, Object> map = JSON.parseObject(response);
        Integer code = (Integer) map.get("code");
        String message = (String) map.get("message");
        checkToken(map);

        if (code != SUCCESS) {

            return getErrorRO(code, message);
        } else {
            ResultObject resultObject = ResultFactory.getro();
            Map<String, Object> data = (Map<String, Object>) map.get("data");
            T ti = (T) data.get(key);
            resultObject.setMessage(message);
            resultObject.setSuccess(true);
            resultObject.setObject(ti);
            resultObject.setCode(code);
            return resultObject;
        }
    }

    private static void checkToken(Map map) {
        if (map.containsKey("token")) {
            String token = (String) map.get("token");
            save(token);
        }
    }

    /**
     * 存 token 到本地
     */
    private static void save(String token) {
        if (!"".equals(token)) {
            SharedPreferences sp = DDApplication.getInstance().getSharedPreferences(Req_Stastus.BUTLER.getMsg(), Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("token", token);
            edit.commit();
        }
    }
}
