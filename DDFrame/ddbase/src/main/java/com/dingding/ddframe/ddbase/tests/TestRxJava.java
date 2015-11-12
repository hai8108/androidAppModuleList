package com.dingding.ddframe.ddbase.tests;

import android.app.Activity;
import android.os.Bundle;

import com.dingding.ddframe.ddbase.commons.Entitys.GateWayInfos;
import com.dingding.ddframe.ddbase.commons.Entitys.User;
import com.dingding.ddframe.ddbase.net.ResultObject;
import com.dingding.ddframe.ddbase.rxjava.RxUtils;
import com.dingding.ddframe.ddbase.utils.L;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zzz on 15/8/25.
 */
public class TestRxJava extends Activity {

    private Subscription subscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = new User();
        Map map = new HashMap();

        /**
         * subscription 为句柄，需要在 ondestory 中取消回调
         *
         */
        subscription = AppObservable.bindActivity(TestRxJava.this,
                RxUtils.postEntity("urlPath", user, GateWayInfos.getInstance().getBase())
                 //具体的接口调用（俩种形式，Entity，和 map）
                 // postMap  形式接口
                        .map(RxUtils.getMap()))  //此过程为解析过程，需要和具体逻辑对应 ,提供了所有 DataParse的方法
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());

    }


    private Observer getObserver() {
        return new Observer<ResultObject>() {
            @Override
            public void onCompleted() {
                L.i(TestRxJava.class, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                L.i(TestRxJava.class, "onError");

            }

            @Override
            public void onNext(ResultObject ro) {
                L.i(TestRxJava.class, "onNext" );
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }
}
