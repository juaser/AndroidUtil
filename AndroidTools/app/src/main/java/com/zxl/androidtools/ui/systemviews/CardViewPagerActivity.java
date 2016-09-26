package com.zxl.androidtools.ui.systemviews;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.utils.log.LogUtils;
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

    private float mLastOffset;
    private boolean goingLeft = false;//向左滑动
    private int nowPagePosition;//当前页面
    private int targetPagePosition;//目标页面
    private float realOffset;

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
        cardViewpager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        boolean goingLeft = mLastOffset < positionOffset;
        mLastOffset = positionOffset;
        if (goingLeft) {
            nowPagePosition = position;
            targetPagePosition = position + 1;
            realOffset = positionOffset;
        } else {
            nowPagePosition = position;
            targetPagePosition = position - 1;
            realOffset = 1 - positionOffset;
        }
        if (targetPagePosition < 0) {
            return;
        }
        LogUtils.e((goingLeft ? "   向左滑动" : "   向右滑动") + "   当前页面==" + nowPagePosition + "   目标页面==" + targetPagePosition + "    滑动的距离==" + realOffset);
    }

    @Override
    public void onPageSelected(int position) {
        View view = views.get(position);
        view.animate().scaleY(1.1f);
        view.animate().scaleX(1.1f);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
