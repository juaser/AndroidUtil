package com.zxl.androidtools.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxl.androidtools.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: zxl
 * @Date: 18/9/16 PM4:25.
 */
public class CardViewpagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> datas;
    private List<CardView> cardViews;
    private float mBaseElevation;

    public CardViewpagerAdapter(Context mContext) {
        this.mContext = mContext;
        datas = new ArrayList<>();
        cardViews = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            datas.add("i==" + i);
            cardViews.add(null);
        }
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
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_card_viewpager, container, false);
        container.addView(view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);
        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }
        cardViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        cardViews.set(position, null);
    }

    public CardView getCardViewAt(int position) {
        return cardViews.get(position);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }
}
