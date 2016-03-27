package com.tq.rxjavademos.fragment;



import android.graphics.Color;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.tq.rxjavademos.R;
import com.tq.rxjavademos.adapter.ZhuangbiListAdapter;
import com.tq.rxjavademos.model.ZhuangbiImage;
import com.tq.rxjavademos.network.NetWork;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Rxjava的基本练习
 */
public class ElementaryFragment extends BaseFragment {

    private volatile static ElementaryFragment instance;
    @Bind(R.id.gridRv)
    RecyclerView gridRv;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    public static ElementaryFragment getInstance() {
        if (instance == null) {
            synchronized (CacheFragment.class) {
                if (instance == null) {
                    instance = new ElementaryFragment();
                }
            }
        }
        return instance;
    }

    ZhuangbiListAdapter adapter = new ZhuangbiListAdapter();
    Observer<List<ZhuangbiImage>> observer = new Observer<List<ZhuangbiImage>>() {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT);

        }

        @Override
        public void onNext(List<ZhuangbiImage> zhuangbiImages) {
            swipeRefreshLayout.setRefreshing(false);
            adapter.setImages(zhuangbiImages);
        }
    };

    @OnCheckedChanged({R.id.searchRb1, R.id.searchRb2, R.id.searchRb3, R.id.searchRb4})
    void onTagChecked(RadioButton searchRb, boolean checked) {
        if (checked) {
            unSubsribe();
            adapter.setImages(null);
            swipeRefreshLayout.setRefreshing(true);
            search(searchRb.getText().toString());
        }
    }

    private void search(String key) {
        subscription = NetWork.getZhuangbiApi()
                .search(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(observer);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_elementary, container, false);
        ButterKnife.bind(this, view);

        gridRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        gridRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_elementary;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_elementary;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
