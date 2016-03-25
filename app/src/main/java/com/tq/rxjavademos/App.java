package com.tq.rxjavademos;

import android.app.Application;

/**
 * Created by boobooL on 2016/3/25 0025
 * Created 邮箱 ：boobooMX@163.com
 */
public class App extends Application {
    private volatile static  App instance;
    public static App getInstance(){
        if (instance == null) {
            synchronized (App.class){
                if (instance==null){
                    instance=new App();
                }
            }
        }
        return instance;
    }

}
