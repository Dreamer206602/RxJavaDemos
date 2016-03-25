package com.tq.rxjavademos.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tq.rxjavademos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ElementaryFragment extends BaseFragment {

    private volatile static ElementaryFragment instance;
    public static ElementaryFragment getInstance(){
        if (instance == null) {
            synchronized (CacheFragment.class){
                if (instance == null) {
                    instance=new ElementaryFragment();
                }
            }
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_elementary, container, false);
    }

    @Override
    protected int getDialogRes() {
        return 0;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_elementary;
    }
}
