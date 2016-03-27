package com.tq.rxjavademos.model;

/**
 * Created on 2016/3/27 20:55
 * Created by Author boobooL
 * 邮箱：boobooMX@163.com
 */
public class FakeToken {
    public String token;
    public boolean expired;

    public FakeToken() {
    }

    public FakeToken(boolean expired) {
        this.expired = expired;
    }
}
