package com.tq.rxjavademos.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tq.rxjavademos.R;

import butterknife.OnClick;
import rx.Subscription;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {
    protected Subscription subscription;

//    @OnClick(R.id.tipBt)
//    void tip(){
//
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubsribe();
    }

    protected  void unSubsribe(){
        if(subscription!=null&& !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }
    protected abstract int getDialogRes();
    protected abstract int getTitleRes();
}
