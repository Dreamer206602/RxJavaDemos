package com.tq.rxjavademos.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tq.rxjavademos.R;
import com.tq.rxjavademos.adapter.ItemListAdapter;
import com.tq.rxjavademos.model.Item;
import com.tq.rxjavademos.model.ZhuangbiImage;
import com.tq.rxjavademos.network.NetWork;
import com.tq.rxjavademos.util.GankBeautyResultToItemsMapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZipFragment extends BaseFragment {
    private volatile static ZipFragment instance;
    @Bind(R.id.zipLoadBt)
    Button zipLoadBt;
    @Bind(R.id.tipBt)
    Button tipBt;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    ItemListAdapter adapter = new ItemListAdapter();
    Observer<List<Item>> observer = new Observer<List<Item>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<Item> items) {

            swipeRefreshLayout.setRefreshing(false);
            adapter.setImages(items);
        }
    };
    @Bind(R.id.gridRv)
    RecyclerView gridRv;

    public static ZipFragment getInstance() {
        if (instance == null) {
            synchronized (CacheFragment.class) {
                if (instance == null) {
                    instance = new ZipFragment();
                }
            }
        }
        return instance;
    }

    @OnClick(R.id.zipLoadBt)
    void load() {
        swipeRefreshLayout.setRefreshing(true);
        unSubsribe();
        subscription = Observable.zip(NetWork.getGankApi().getBeauties(200, 1).map(GankBeautyResultToItemsMapper.getInstance()),
                NetWork.getZhuangbiApi().search("装逼"),
                new Func2<List<Item>, List<ZhuangbiImage>, List<Item>>() {
                    @Override
                    public List<Item> call(List<Item> gankItems, List<ZhuangbiImage> zhuangbiImages) {
                        List<Item> items = new ArrayList<Item>();
                        for (int i = 0; i < gankItems.size() / 2 && i < zhuangbiImages.size(); i++) {

                            items.add(gankItems.get(i * 2));
                            items.add(gankItems.get(i * 2 + 1));
                            Item zhuangbiItem = new Item();
                            ZhuangbiImage zhuangbiImage = zhuangbiImages.get(i);
                            zhuangbiItem.description = zhuangbiImage.description;
                            zhuangbiItem.imageUrl = zhuangbiImage.image_url;
                            items.add(zhuangbiItem);
                        }
                        return items;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zip, container, false);
        ButterKnife.bind(this, view);

        gridRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        gridRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
        swipeRefreshLayout.setRefreshing(false);
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_zip;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_zip;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
