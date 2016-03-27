package com.tq.rxjavademos.network;

import com.squareup.okhttp.OkHttpClient;
import com.tq.rxjavademos.network.api.GankApi;
import com.tq.rxjavademos.network.api.ZhuangbiApi;

import retrofit.CallAdapter;
import retrofit.Converter;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by boobooL on 2016/3/25 0025
 * Created 邮箱 ：boobooMX@163.com
 */
public class NetWork {
    private static ZhuangbiApi zhuangbiApi;
    private static GankApi gankApi;

    private static OkHttpClient okHttpClient=new OkHttpClient();
    private static Converter.Factory gsonConverterFactory= GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory= RxJavaCallAdapterFactory.create();


    public static ZhuangbiApi getZhuangbiApi(){
        if (zhuangbiApi == null) {
            Retrofit retrofit=new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://zhuangbi.info")
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .addConverterFactory(gsonConverterFactory)
                    .build();
            zhuangbiApi=retrofit.create(ZhuangbiApi.class);
        }
        return zhuangbiApi;
    }


    public static GankApi getGankApi() {
        if (gankApi == null) {
            Retrofit retrofit=new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://gank.io/api/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            gankApi=retrofit.create(GankApi.class);
        }
        return gankApi;
    }
}
