package com.zxl.androidtools.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.plugin.utils.SystemUtils;
import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.utils.log.LogUtils;
import com.zxl.androidtools.R;
import com.zxl.androidtools.adapter.MainRecyclerAdapter;
import com.zxl.androidtools.inter.OnClickPositonListerner;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @Description:
 * @Author: zxl
 * @Date: 6/9/16 上午9:53.
 */
public class CustomViewActivity extends BaseAppCompatActivity implements OnClickPositonListerner {

    @Bind(R.id.recycler)
    RecyclerView recycler;

    private List<String> activity_labname;//activity别名
    private List<String> activity_packagename;//activity的路径:包名+类名
    private MainRecyclerAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        getList();
        setRecycler();
    }

    /**
     * 获取Acticity清单
     */
    public void getList() {
        activity_labname = new ArrayList<>();
        activity_packagename = new ArrayList<>();
        ActivityInfo[] activities = SystemUtils.getInstance().getAppActivityInfo(this);
        LogUtils.e("activity.size==" + activities.length);
        if (activities != null) {
            for (ActivityInfo activityInfo : activities) {
                if (activityInfo.labelRes != 0) {
                    if (activityInfo.name.contains(getString(R.string.group_activity_customview))) {
                        activity_labname.add(getString(activityInfo.labelRes));
                        activity_packagename.add(activityInfo.name);
                    }
                }
            }
        }
    }

    /**
     * 设置RecycleView
     */
    public void setRecycler() {
        adapter = new MainRecyclerAdapter(activity_labname, this);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListerner(this);
    }

    @Override
    public void clickPosition(int position) {
        jump(position);
    }

    public void jump(int position) {
        Intent intent = new Intent();
        intent.setClassName(getPackageName(), activity_packagename.get(position));
        startActivity(intent);
    }
}
