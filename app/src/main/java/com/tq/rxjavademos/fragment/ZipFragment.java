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
public class ZipFragment extends BaseFragment {
    private volatile static ZipFragment instance;
    public static ZipFragment getInstance(){
        if (instance == null) {
            synchronized (CacheFragment.class){
                if (instance == null) {
                    instance=new ZipFragment();
                }
            }
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_zip, container, false);
    }

    @Override
    protected int getDialogRes() {
        return 0;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_zip;
    }
}
