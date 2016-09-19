package com.zxl.androidtools.ui.systemviews;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.zxl.androidtools.R;
import com.zxl.androidtools.adapter.CardViewpagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @Description:
 * @Author: zxl
 * @Date: 18/9/16 PM4:23.
 */
public class CardViewPagerActivity extends BaseAppCompatActivity implements ViewPager.OnPageChangeListener {
    @Bind(R.id.card_viewpager)
    ViewPager cardViewpager;

    private CardViewpagerAdapter adapter;
    private List<View> views;
    private List<String> datas;

    @Override
    public int getLayoutId() {
        return R.layout.activity_card_viewpager;
    }

    @Override
    public void initView() {
        datas = new ArrayList<>();
        views = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            datas.add("i==" + i);
            View view = LayoutInflater.from(this).inflate(R.layout.item_card_viewpager, null);
            views.add(view);
        }
        adapter = new CardViewpagerAdapter(this, views, datas);
        cardViewpager.setAdapter(adapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
