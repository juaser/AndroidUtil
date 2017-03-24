package com.zxl.androidtools.ui.customviews;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.plugin.weight.xlist.XListView;
import com.zxl.androidtools.R;
import com.zxl.androidtools.adapter.XListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/1/21 19:02
 */

public class XListActivity extends AppCompatActivity {
    private XListView mLv;
    private XListAdapter mAdapter;
    private List<String> mData;
    private List<String> mSrcData;
    private int mPageNum = 10;
    public static final int DATA_LOCAL = 0x001;//处理已有的数据，加载第一页
    public static final int DATA_INIT = 0x002;//初始化数据
    public static final int DATA_MORE = 0x003;//加载更多数据

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xlist);
        initView();
        initData();
    }

    public void initView() {
        mLv = (XListView) findViewById(R.id.lv_test);
        mLv.setPullLoadEnable(true);
        mData = new ArrayList<>();
        mSrcData = new ArrayList<>();
        mAdapter = new XListAdapter(this, mData);
        mLv.setAdapter(mAdapter);
        mLv.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mMainHandler.sendEmptyMessageDelayed(DATA_INIT, 2000);
            }

            @Override
            public void onLoadMore() {
                mMainHandler.sendEmptyMessageDelayed(DATA_MORE, 2000);
            }
        });
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(XListActivity.this, mData.get(i - 1), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initData() {
        mSrcData.clear();
        for (int i = 0; i < 50; i++) {
            mSrcData.add("i==" + i);
        }
        mMainHandler.sendEmptyMessageDelayed(DATA_LOCAL, 1000);
    }

    public void loadLocalData() {
        List<String> data = new ArrayList<>();
        if (mSrcData.size() <= mPageNum) {
            data.addAll(mSrcData);
        } else {
            for (int i = 0; i < mPageNum; i++) {
                data.add(mSrcData.get(i));
            }
        }
        refresh(data);
    }

    public void loaderMoreData() {
        int currentSize = mData.size();
        int totalSize = mSrcData.size();
        if (currentSize == totalSize) {
            mLv.stopLoadMore();
            return;
        }
        if (currentSize < totalSize) {
            int endsize = currentSize + mPageNum;
            if (endsize > totalSize) {
                endsize = totalSize;
            }
            List<String> data = new ArrayList<>();
            for (int i = 0; i < endsize; i++) {
                data.add(mSrcData.get(i));
            }
            refresh(data);
        } else {
            mLv.stopLoadMore();
        }
    }


    public void refresh(List<String> data) {
        mData.clear();
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
        mLv.stopRefresh();
        mLv.stopLoadMore();
    }

    private Handler mMainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DATA_LOCAL:
                    loadLocalData();
                    break;
                case DATA_INIT:
                    initData();
                    break;
                case DATA_MORE:
                    loaderMoreData();
                    break;
            }
        }
    };

}
