package com.tq.rxjavademos.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2016/3/26 23:57
 * Created by Author boobooL
 * 邮箱：boobooMX@163.com
 */
public class GankBeautyResult {

    public boolean error;

    public  @SerializedName("results")List<GankBeauty>beauties;

}
