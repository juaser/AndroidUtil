package com.zxl.androidtools.ui.systemviews;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plugin.utils.ReflectUtils;
import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.utils.log.LogUtils;
import com.plugin.weight.viewpager.NoScrollViewPager;
import com.zxl.androidtools.R;
import com.zxl.androidtools.adapter.ViewPagerFragmentAdapter;
import com.zxl.androidtools.frg.SampleFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description:
 * @Author: zxl
 * @Date: 12/9/16 PM2:19.
 */
public class TabLayoutActivity extends BaseAppCompatActivity {
    @Bind(R.id.tv_tablayout_top_scroll)
    TextView tvTablayoutTopScroll;
    @Bind(R.id.tv_tablayout_top_noscroll)
    TextView tvTablayoutTopNoscroll;
    @Bind(R.id.tv_tablayout_bottom_scroll)
    TextView tvTablayoutBottomScroll;
    @Bind(R.id.tab_top_scroll)
    TabLayout tabTopScroll;
    @Bind(R.id.viewpager_top_scroll)
    ViewPager viewpagerTopScroll;
    @Bind(R.id.layout_tablayout_top_scroll)
    LinearLayout layoutTablayoutTopScroll;
    @Bind(R.id.tab_top_noscroll)
    TabLayout tabTopNoscroll;
    @Bind(R.id.viewpager_top_noscroll)
    NoScrollViewPager viewpagerTopNoscroll;
    @Bind(R.id.layout_tablayout_top_noscroll)
    LinearLayout layoutTablayoutTopNoscroll;
    @Bind(R.id.viewpager_bottom_noscroll)
    NoScrollViewPager viewpagerBottomNoscroll;
    @Bind(R.id.tab_bottom_noscroll)
    TabLayout tabBottomNoscroll;
    @Bind(R.id.layout_tablayout_bottom_noscroll)
    LinearLayout layoutTablayoutBottomNoscroll;

    private List<View> views;
    private List<Fragment> fragments;

    private String[] titles_top_scroll = {"顶部1", "顶部2", "顶部3", "顶部4", "顶部5", "顶部6", "顶部7"};
    private String[] titles_top_noscroll = {"顶部1", "顶部2", "顶部3"};
    private String[] titles_bottom_scroll = {"底部1", "底部2", "底部3", "底部4"};

    private List<TabLayout.Tab> tabs;

    private ViewPagerFragmentAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_tablayout;
    }

    @Override
    public void initView() {
        views = new ArrayList<>();
        views.add(layoutTablayoutTopScroll);
        views.add(layoutTablayoutTopNoscroll);
        views.add(layoutTablayoutBottomNoscroll);
    }


    @OnClick(R.id.tv_tablayout_top_scroll)
    void top_scroll() {
        if (layoutTablayoutTopScroll.getVisibility() != View.VISIBLE) {
            LogUtils.e(getString(R.string.str_tablayouy_top_scroll));
            layoutTablayoutTopScroll.setVisibility(View.VISIBLE);
            layoutTablayoutTopNoscroll.setVisibility(View.GONE);
            layoutTablayoutBottomNoscroll.setVisibility(View.GONE);
            initTab(titles_top_scroll, tabTopScroll, viewpagerTopScroll);
        }
    }


    @OnClick(R.id.tv_tablayout_top_noscroll)
    void top_noscroll() {
        if (layoutTablayoutTopNoscroll.getVisibility() != View.VISIBLE) {
            LogUtils.e(getString(R.string.str_tablayouy_top_noscroll));
            layoutTablayoutTopNoscroll.setVisibility(View.VISIBLE);
            layoutTablayoutTopScroll.setVisibility(View.GONE);
            layoutTablayoutBottomNoscroll.setVisibility(View.GONE);
            initTab(titles_top_noscroll, tabTopNoscroll, viewpagerTopNoscroll);
            ReflectUtils.getInstance().setIndicatorWidth(tabTopNoscroll, 40);
        }
    }

    @OnClick(R.id.tv_tablayout_bottom_scroll)
    void bottom_scroll() {
        if (layoutTablayoutBottomNoscroll.getVisibility() != View.VISIBLE) {
            LogUtils.e(getString(R.string.str_tablayouy_bottom_noscroll));
            layoutTablayoutBottomNoscroll.setVisibility(View.VISIBLE);
            layoutTablayoutTopScroll.setVisibility(View.GONE);
            layoutTablayoutTopNoscroll.setVisibility(View.GONE);
            initTab(titles_bottom_scroll, tabBottomNoscroll, viewpagerBottomNoscroll);
        }
    }

    public void initTab(String[] title, TabLayout tabLayout, ViewPager viewPager) {
        fragments = new ArrayList<>();
        int num = title.length;
        for (int i = 0; i < num; i++) {
            fragments.add(new SampleFragment());
        }
        adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragments, title);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabs = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            tabs.add(tabLayout.getTabAt(i));
            //tabs.get(i).setIcon(R.mipmap.ic_launcher)//tab图标
        }
    }
}
