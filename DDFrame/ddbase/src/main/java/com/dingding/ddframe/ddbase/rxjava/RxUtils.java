package com.dingding.ddframe.ddbase.rxjava;

import com.dingding.ddframe.ddbase.commons.DataParse;
import com.dingding.ddframe.ddbase.net.HttpClient;
import com.dingding.ddframe.ddbase.net.ResultObject;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by zzz on 15/8/25.
 */
public class RxUtils {
    static HttpClient client = new HttpClient();

    public static void main(String[] args) {


        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("");
            }
        });
    }


    /**
     * 入参为 map 的请求。
     * @param path
     * @param map
     * @return
     */
    public static Observable<String> postMap(final String path, final Map map,final LinkedHashMap baseMap) {
        return Observable.just(path)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        try {
                            return client.postMap(path, map,baseMap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
    }


    /**
     * 入参为 EntityBean 形式的请求
     * @param path
     * @param o
     * @return
     */
    public static Observable<String> postEntity(final String path, final Object o,final LinkedHashMap baseMap) {
        return Observable.just(path)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        try {
                            return client.postEntity(path, o,baseMap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
    }


    /**
     * 对应 DataParse.parseObject
     * @param clz
     * @return
     */
    public static Func1<String , ResultObject> getObject(final Class<?> clz){
        return new Func1<String, ResultObject>() {
            @Override
            public ResultObject call(String s) {
                return DataParse.parseObject(s, clz);
            }
        };
    }

    /**
     * 对应 DataParse.parseObject
     * @param key
     * @return
     */
    public static Func1<String , ResultObject> getObject(final String key){
        return new Func1<String, ResultObject>() {
            @Override
            public ResultObject call(String s) {
                return DataParse.parseObject(s, key);
            }
        };
    }

    /**
     * 对应 DataParse.parseObject
     * @return
     */
    public static Func1<String , ResultObject> getMap(){
        return new Func1<String, ResultObject>() {
            @Override
            public ResultObject call(String s) {
                return DataParse.parseMap(s);
            }
        };
    }

    /**
     * 对应 DataParse.parseList
     * @param key
     * @param clz
     * @return
     */
    public static Func1<String , ResultObject> getList(final String key,final Class<?> clz){
        return new Func1<String, ResultObject>() {
            @Override
            public ResultObject call(String s) {
                return DataParse.parseList(s, key, clz);
            }
        };
    }

    /**
     * 对应 DataParse.parseObject
     * @return
     */
    public static Func1<String , ResultObject> getNoClz(){
        return new Func1<String, ResultObject>() {
            @Override
            public ResultObject call(String s) {
                return DataParse.parseNoClz(s);
            }
        };
    }

}
