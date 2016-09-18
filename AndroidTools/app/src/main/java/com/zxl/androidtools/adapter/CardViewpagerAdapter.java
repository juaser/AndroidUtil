package com.zxl.androidtools.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @Description:
 * @Author: zxl
 * @Date: 18/9/16 PM4:25.
 */
public class CardViewpagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<View> views;
    private List<String> datas;

    public CardViewpagerAdapter(Context mContext, List<View> views, List<String> datas) {
        this.mContext = mContext;
        this.views = views;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);
        container.addView(views.get(position));
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }
}
