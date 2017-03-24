package com.zxl.androidtools.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zxl.androidtools.R;

import java.util.List;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/1/22 10:17
 */

public class XListAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mData;

    public XListAdapter(Context mContext, List<String> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        XListHolder xListHolder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_xlist_test, null);
            xListHolder = new XListHolder();
            xListHolder.tv_tilte = (TextView) view.findViewById(R.id.tv_title);
            view.setTag(xListHolder);
        } else {
            xListHolder = (XListHolder) view.getTag();
        }
        xListHolder.tv_tilte.setText(mData.get(i));
        return view;
    }

    private class XListHolder {
        private TextView tv_tilte;
    }
}
