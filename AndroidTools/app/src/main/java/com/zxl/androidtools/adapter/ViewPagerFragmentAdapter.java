package com.zxl.androidtools.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @类说明：
 * @author：zxl
 * @CreateTime 2016/7/27.
 */
public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> titles;
    private String[] str_titles;
    public static final String Pager_TAG = "type";

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null) {
            return titles.get(position);
        }
        if (str_titles != null) {
            return str_titles[position];
        }
        return super.getPageTitle(position);
    }

    public ViewPagerFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public ViewPagerFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    public ViewPagerFragmentAdapter(FragmentManager fm, List<Fragment> fragments, String[] str_titles) {
        super(fm);
        this.fragments = fragments;
        this.str_titles = str_titles;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragments.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt(Pager_TAG, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
