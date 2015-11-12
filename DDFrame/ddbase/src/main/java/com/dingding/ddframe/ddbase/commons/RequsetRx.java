package com.dingding.ddframe.ddbase.commons;

import com.dingding.ddframe.ddbase.utils.L;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zzz on 15/8/21.
 */
public class RequsetRx {


        /*private Observable<Object> getObservable (String s){

            return Observable.just(s).map(new Func1<String, Object>() {
                @Override
                public Object call(String s) {
                    String s1 = null;
                    try {
                        s1 = client.SynchronousGet(BaseUrl + COMString + s);
                        L.i("observable", s1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return 1;
                }
            });
        }*/

    }
