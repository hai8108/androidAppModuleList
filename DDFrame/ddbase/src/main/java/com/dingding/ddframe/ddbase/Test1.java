package com.dingding.ddframe.ddbase;

import android.util.Log;

import com.dingding.ddframe.ddbase.net.HttpClient;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by zzz on 15/7/28.
 */
public class Test1 {

    static HttpClient httpClient;
    public final static String BaseUrl = "http://test.mbs.zufangzi.com/gms/";
    private final static String COMString = "common/commonApiController/";


    public static void main (String args[]) {
//        RecyclerView view = new RecyclerView();
//        httpClient = new HttpClient();
        hello("aaa", "bbb");
    }


    public static void hello(String... names) {
        Observable.from(names).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                try {
//                    String s2 = httpClient.SynchronousGet(BaseUrl + COMString + "getSystemTime.do");
//                    System.out.println("Hello " + s2 + "!");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.i("ddframe", s);
            }
        });
    }
}
