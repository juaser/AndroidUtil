package com.zxl.androidtools.ui.systemviews;

import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.utils.log.LogUtils;
import com.zxl.androidtools.R;
import com.zxl.androidtools.adapter.CoordinaorRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @Description:
 * @Author: zxl
 * @Date: 13/9/16 AM10:21.
 */
public class CoordinatorLayoutActivity extends BaseAppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    private CoordinaorRecyclerAdapter adapter;
    private List<String> data;
    private ListView lv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_coordinatorlayout;
    }


    @Override
    public void initView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//显示左上角箭头
        actionBar.setHomeButtonEnabled(true);//能够点击
        collapsingToolbarLayout.setTitle("标题");
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.GREEN);//设置收缩后Toolbar上字体的颜色

        initRecycle();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                LogUtils.e("小箭头");
                Snackbar.make(recyclerview, "返回", Snackbar.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initRecycle() {
        data = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            data.add("条目" + i);
        }
        adapter = new CoordinaorRecyclerAdapter(this, data);
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
//        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
//        recyclerview.addItemDecoration(decoration);
        recyclerview.setAdapter(adapter);
    }
}
