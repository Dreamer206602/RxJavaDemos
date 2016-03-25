package com.tq.rxjavademos.network.api;



import com.tq.rxjavademos.model.ZhuangbiImage;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by boobooL on 2016/3/25 0025
 * Created 邮箱 ：boobooMX@163.com
 */
public interface ZhuangbiApi {

    @GET("search")
    Observable<List<ZhuangbiImage>> search(@Query("q") String query);


}

