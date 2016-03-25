package com.tq.rxjavademos;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.tq.rxjavademos.fragment.BaseFragment;
import com.tq.rxjavademos.fragment.CacheFragment;
import com.tq.rxjavademos.fragment.ElementaryFragment;
import com.tq.rxjavademos.fragment.MapFragment;
import com.tq.rxjavademos.fragment.TokenAdvanceFragment;
import com.tq.rxjavademos.fragment.TokenFragment;
import com.tq.rxjavademos.fragment.ZipFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(android.R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                BaseFragment baseFragment=null;
                switch (position){
                    case 0:
                        baseFragment=ElementaryFragment.getInstance();
                        break;
                    case 1:
                        baseFragment= MapFragment.getInstance();
                        break;
                    case 2:
                        baseFragment= ZipFragment.getInstance();
                        break;
                    case 3:
                        baseFragment= TokenFragment.getInstance();
                        break;
                    case 4:
                        baseFragment= TokenAdvanceFragment.getInstance();
                        break;
                    case 5:
                        baseFragment= CacheFragment.getInstance();
                        break;
                    default:
                       baseFragment=ElementaryFragment.getInstance();
                }
                return baseFragment;
            }
            @Override
            public int getCount() {
                return 6;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return getString(R.string.title_elementary);
                    case 1:
                        return getString(R.string.title_map);
                    case 2:
                        return getString(R.string.title_zip);
                    case 3:
                        return getString(R.string.title_token);
                    case 4:
                        return getString(R.string.title_token_advanced);
                    case 5:
                        return getString(R.string.title_cache);
                    default:
                        return getString(R.string.title_elementary);
                }
            }
        });
        tabs.setupWithViewPager(viewPager);



    }
}
