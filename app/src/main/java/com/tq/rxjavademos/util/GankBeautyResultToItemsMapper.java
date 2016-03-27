package com.tq.rxjavademos.util;

import com.tq.rxjavademos.model.GankBeauty;
import com.tq.rxjavademos.model.GankBeautyResult;
import com.tq.rxjavademos.model.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.functions.Func1;

/**
 * Created on 2016/3/27 00:03
 * Created by Author boobooL
 * 邮箱：boobooMX@163.com
 */
public class GankBeautyResultToItemsMapper implements Func1<GankBeautyResult,List<Item>> {

    private static GankBeautyResultToItemsMapper INSTANCE=new GankBeautyResultToItemsMapper();
    public static GankBeautyResultToItemsMapper getInstance(){
        return INSTANCE;
    }

    public GankBeautyResultToItemsMapper() {
    }

    @Override
    public List<Item> call(GankBeautyResult gankBeautyResult) {
        List<GankBeauty>gankBeauties=gankBeautyResult.beauties;
        List<Item>items=new ArrayList<>(gankBeauties.size());
        SimpleDateFormat inputFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
        SimpleDateFormat outputFormat=new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        for(GankBeauty gankBeauty:gankBeauties){
            Item item=new Item();
            try {
                Date date=inputFormat.parse(gankBeauty.createdAt);
                item.description=outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                item.description="unknown date";
            }
            item.imageUrl=gankBeauty.url;
            items.add(item);
        }
        return items;
    }
}
