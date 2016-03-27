package com.tq.rxjavademos.network.api;

import com.tq.rxjavademos.model.GankBeautyResult;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created on 2016/3/26 23:54
 * Created by Author boobooL
 * 邮箱：boobooMX@163.com
 */
public interface GankApi {
    @GET("data/福利/{number}/{page}")
    Observable<GankBeautyResult>getBeauties(@Path("number")int number,@Path("page")int page );
}
