package com.tq.rxjavademos.db;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.tq.rxjavademos.App;
import com.tq.rxjavademos.R;
import com.tq.rxjavademos.model.Item;
import com.tq.rxjavademos.network.NetWork;
import com.tq.rxjavademos.util.GankBeautyResultToItemsMapper;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by boobooL on 2016/3/28 0028
 * Created 邮箱 ：boobooMX@163.com
 */
public class Data {
    private static Data instance;
    private static final int DATA_SOURCE_MEMORY = 1;
    public static final int DATA_SOURCE_DISK = 2;
    public static final int DATA_SOURCE_NETWORK = 3;

    @IntDef({DATA_SOURCE_MEMORY, DATA_SOURCE_DISK, DATA_SOURCE_NETWORK})
    @interface DataSource {
    }

    BehaviorSubject<List<Item>> cache;
    private int dataSource;

    private Data() {
    }

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    private void setDataSource(@DataSource int dataSource) {
        this.dataSource = dataSource;
    }

    public String getDataSourceText() {
        int dataSourceTextRes;
        switch (dataSource) {
            case DATA_SOURCE_MEMORY:
                dataSourceTextRes = R.string.data_source_memory;
                break;
            case DATA_SOURCE_DISK:
                dataSourceTextRes = R.string.data_source_disk;
                break;
            case DATA_SOURCE_NETWORK:
                dataSourceTextRes = R.string.data_source_network;
                break;
            default:
                dataSourceTextRes = R.string.data_source_network;
        }
        return App.getInstance().getString(dataSourceTextRes);
    }

    public void loadFromNetwork() {
        NetWork.getGankApi()
                .getBeauties(100, 1)
                .subscribeOn(Schedulers.io())
                .map(GankBeautyResultToItemsMapper.getInstance())
                .doOnNext(new Action1<List<Item>>() {
                    @Override
                    public void call(List<Item> items) {
                        DataBase.getInstance().writeItems(items);
                    }
                })
                .subscribe(new Action1<List<Item>>() {
                    @Override
                    public void call(List<Item> items) {
                        cache.onNext(items);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    public Subscription subscribeData(@NonNull Observer<List<Item>> observer) {
        if (cache == null) {
            cache = BehaviorSubject.create();
            Observable.create(new Observable.OnSubscribe<List<Item>>() {
                @Override
                public void call(Subscriber<? super List<Item>> subscriber) {
                    List<Item> items = DataBase.getInstance().readItems();
                    if (items == null) {
                        setDataSource(DATA_SOURCE_NETWORK);
                        loadFromNetwork();
                    } else {
                        setDataSource(DATA_SOURCE_DISK);
                        subscriber.onNext(items);
                    }
                }
            }).subscribeOn(Schedulers.io())
                    .subscribe(cache);

        } else {
            setDataSource(DATA_SOURCE_MEMORY);
        }
        return cache.observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void clearMemoryCache() {
        cache = null;
    }

    public void clearMemoryAndDiskCache() {
        clearMemoryCache();
        DataBase.getInstance().delete();
    }


}
