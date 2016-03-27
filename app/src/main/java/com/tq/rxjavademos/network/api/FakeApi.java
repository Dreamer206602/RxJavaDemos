package com.tq.rxjavademos.network.api;

import android.support.annotation.NonNull;

import com.tq.rxjavademos.model.FakeThing;
import com.tq.rxjavademos.model.FakeToken;

import java.util.Random;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created on 2016/3/27 20:53
 * Created by Author boobooL
 * 邮箱：boobooMX@163.com
 */
public class FakeApi {


    Random random=new Random();
    public Observable<FakeToken>getFakeToken(@NonNull final String fakeAuth){
        return Observable.just(fakeAuth)
                .map(new Func1<String, FakeToken>() {
                    @Override
                    public FakeToken call(String s) {

                       int fakeNetWorkTimeCost=random.nextInt(500)+500;
                        try {
                            Thread.sleep(fakeNetWorkTimeCost);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        FakeToken fakeToken=new FakeToken();
                        fakeToken.token=createToken();
                        return fakeToken;
                    }
                });
    }

    private static String createToken() {
        return "fake_token_"+System.currentTimeMillis()%10000;
    }
    public Observable<FakeThing>getFakeData(FakeToken fakeToken){
        return Observable.just(fakeToken)
                .map(new Func1<FakeToken, FakeThing>() {
                    @Override
                    public FakeThing call(FakeToken fakeToken) {

                        int fakeNetworkTimeCost=random.nextInt(500)+500;

                        try {
                            Thread.sleep(fakeNetworkTimeCost);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(fakeToken.expired){
                            throw  new IllegalArgumentException("Token expired");
                        }

                        FakeThing fakeData=new FakeThing();
                        fakeData.id= (int) (System.currentTimeMillis()%1000);
                        fakeData.name="FAKE_USER_"+fakeData.id;
                        return fakeData;
                    }
                });
    }

}
