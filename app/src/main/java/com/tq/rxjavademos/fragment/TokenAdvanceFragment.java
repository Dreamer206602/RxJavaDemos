package com.tq.rxjavademos.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tq.rxjavademos.R;
import com.tq.rxjavademos.model.FakeThing;
import com.tq.rxjavademos.model.FakeToken;
import com.tq.rxjavademos.network.NetWork;
import com.tq.rxjavademos.network.api.FakeApi;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class TokenAdvanceFragment extends BaseFragment {

    private volatile static TokenAdvanceFragment instance;
    @Bind(R.id.tipBt)
    Button tipBt;
    @Bind(R.id.tokenTv)
    TextView tokenTv;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.requestBt)
    Button requestBt;
    @Bind(R.id.invalidateTokenBt)
    Button invalidateTokenBt;

    FakeToken cachedFakeToken=new FakeToken(true);

    boolean tokenUpdated;
    @OnClick(R.id.invalidateTokenBt)
     void invalidateToken(){
        cachedFakeToken.expired=true;
        Toast.makeText(getActivity(),R.string.token_destroyed,Toast.LENGTH_SHORT).show();;

    }

    @OnClick(R.id.requestBt)
    void upload(){
        tokenUpdated=false;
        swipeRefreshLayout.setRefreshing(true);
        unSubsribe();
        final FakeApi fakeApi= NetWork.getFakeApi();
        subscription= Observable.just(null)
                .flatMap(new Func1<Object, Observable<FakeThing>>() {
                    @Override
                    public Observable<FakeThing> call(Object o) {
                        return cachedFakeToken.token==null?
                                Observable.<FakeThing>error(new NullPointerException("Token is null"))
                                :fakeApi.getFakeData(cachedFakeToken);
                    }
                })
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                            @Override
                            public Observable<?> call(Throwable throwable) {
                                if(throwable instanceof  IllegalArgumentException || throwable instanceof  NullPointerException){
                                    return  fakeApi.getFakeToken("fake_auth_code")
                                            .doOnNext(new Action1<FakeToken>() {
                                                @Override
                                                public void call(FakeToken fakeToken) {
                                                    tokenUpdated=true;
                                                    cachedFakeToken.token=fakeToken.token;
                                                    cachedFakeToken.expired=fakeToken.expired;
                                                }
                                            });
                                }
                                return Observable.just(throwable);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FakeThing>() {
                    @Override
                    public void call(FakeThing fakeThing) {

                        swipeRefreshLayout.setRefreshing(false);
                        String token=cachedFakeToken.token;
                        if(tokenUpdated){
                            token+="("+getString(R.string.updated)+")";
                        }
                        tokenTv.setText(getString(R.string.got_token_and_data,token,fakeThing.id,fakeThing.name));

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(),R.string.loading_failed,Toast.LENGTH_SHORT).show();;
                    }
                });
    }


    public static TokenAdvanceFragment getInstance() {
        if (instance == null) {
            synchronized (CacheFragment.class) {
                if (instance == null) {
                    instance = new TokenAdvanceFragment();
                }
            }
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_token_advance, container, false);
        ButterKnife.bind(this, view);


        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_token_advances;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_token_advanced;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
