package com.tq.rxjavademos.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tq.rxjavademos.R;
import com.tq.rxjavademos.adapter.ItemListAdapter;
import com.tq.rxjavademos.db.Data;
import com.tq.rxjavademos.model.Item;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * A simple {@link Fragment} subclass.
 */
public class CacheFragment extends BaseFragment {
    private volatile static CacheFragment instance;
    @Bind(R.id.loadingTimeTv)
    AppCompatTextView loadingTimeTv;
    @Bind(R.id.loadBt)
    AppCompatButton loadBt;
    @Bind(R.id.tipBt)
    Button tipBt;
    @Bind(R.id.cacheRv)
    RecyclerView cacheRv;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.clearMemoryCacheBt)
    AppCompatButton clearMemoryCacheBt;
    @Bind(R.id.clearMemoryAndDiskCacheBt)
    AppCompatButton clearMemoryAndDiskCacheBt;


    ItemListAdapter adapter = new ItemListAdapter();
    private long startingTime;

    @OnClick(R.id.clearMemoryCacheBt)
    void clearMemoryCache() {
        Data.getInstance().clearMemoryCache();
        adapter.setImages(null);
        Toast.makeText(getActivity(), R.string.memory_cache_cheard, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.clearMemoryAndDiskCacheBt)
    void clearMemoryAndDiskCache() {
        Data.getInstance().clearMemoryAndDiskCache();
        adapter.setImages(null);
        Toast.makeText(getActivity(), R.string.memory_cache_and_disk_cleared, Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.loadBt)
    void load() {
        swipeRefreshLayout.setRefreshing(true);
        startingTime = System.currentTimeMillis();
        unSubsribe();
        subscription = Data.getInstance()
                .subscribeData(new Observer<List<Item>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(),R.string.loading_failed,Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(List<Item> items) {

                        swipeRefreshLayout.setRefreshing(false);
                        int loadingTime= (int) (System.currentTimeMillis()-startingTime);
                        loadingTimeTv.setText(getString(R.string.loading_time_and_source,loadingTime,Data.getInstance().getDataSourceText()));

                        adapter.setImages(items);
                    }
                });
    }


    public static CacheFragment getInstance() {
        if (instance == null) {
            synchronized (CacheFragment.class) {
                if (instance == null) {
                    instance = new CacheFragment();
                }
            }
        }
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cache, container, false);
        ButterKnife.bind(this, view);

        cacheRv.setLayoutManager(new GridLayoutManager(getActivity(),2));
        cacheRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_cache;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_cache;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
