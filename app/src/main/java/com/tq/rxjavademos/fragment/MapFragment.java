package com.tq.rxjavademos.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tq.rxjavademos.R;
import com.tq.rxjavademos.adapter.ItemListAdapter;
import com.tq.rxjavademos.model.Item;
import com.tq.rxjavademos.network.NetWork;
import com.tq.rxjavademos.util.GankBeautyResultToItemsMapper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * MapFragment
 */
public class MapFragment extends BaseFragment {

    public volatile static MapFragment instance;
    @Bind(R.id.pageTv)
    TextView pageTv;
    @Bind(R.id.previousPageBt)
    AppCompatButton previousPageBt;
    @Bind(R.id.nextPageBt)
    AppCompatButton nextPageBt;
    @Bind(R.id.tipBt)
    Button tipBt;
    @Bind(R.id.gridRv)
    RecyclerView gridRv;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private int page = 0;

    public static MapFragment getInstance() {
        if (instance == null) {
            synchronized (CacheFragment.class) {
                if (instance == null) {
                    instance = new MapFragment();
                }
            }
        }
        return instance;
    }


    ItemListAdapter adapter = new ItemListAdapter();
    Observer<List<Item>> observer = new Observer<List<Item>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(),R.string.loading_failed,Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onNext(List<Item> items) {
            swipeRefreshLayout.setRefreshing(false);
            pageTv.setText(getString(R.string.page_with_number, page));
            adapter.setImages(items);


        }
    };


    @OnClick(R.id.previousPageBt)
    void previousPage(){
        loadPage(--page);
        if(page==1){
            previousPageBt.setEnabled(false);
        }
    }

    @OnClick(R.id.nextPageBt)
    void nextPage(){
        loadPage(++page);
        if(page==2){
            previousPageBt.setEnabled(true);
        }
    }

    private void loadPage(int page){
        swipeRefreshLayout.setRefreshing(true);
        unSubsribe();
        subscription= NetWork.getGankApi()
                .getBeauties(10,page)
                .map(GankBeautyResultToItemsMapper.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);

        gridRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        gridRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_map;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_map;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
