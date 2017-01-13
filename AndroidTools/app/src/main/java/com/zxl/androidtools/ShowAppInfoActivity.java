package com.zxl.androidtools;

import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.plugin.utils.AppUtils;
import com.plugin.utils.DisplayUtils;
import com.plugin.utils.NetworkUtils;
import com.plugin.utils.base.BaseAppCompatActivity;
import com.zxl.androidtools.adapter.ShowAppInfoRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/1/3 12:15
 */

public class ShowAppInfoActivity extends BaseAppCompatActivity {
    @Bind(R.id.recycler)
    RecyclerView recycler;

    private List<String> data;
    private ShowAppInfoRecyclerAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_showappinfo;
    }

    @Override
    public void initView() {
        data = new ArrayList<>();
        adapter = new ShowAppInfoRecyclerAdapter(data, this);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        initData();
    }

    public void initData() {
        data.clear();
        AppUtils appUtils = AppUtils.getInstance();
        data.add("厂商：" + appUtils.getDeviceManufactur());
        data.add("型号：" + appUtils.getDeviceType());
        data.add("网络运营商："+ NetworkUtils.getInstance().getPhoneProvider());
        data.add("当前网络类型："+ NetworkUtils.getInstance().getNetWorkTypeName());
        data.add("设备唯一ID：" + appUtils.getDeviceUniqueId());
        data.add("app名称：" + appUtils.getCurrentAppName());
        data.add("app包名：" + appUtils.getCurrentAppPackageName());
        data.add("Android版本：" + Build.VERSION.SDK_INT);
        data.add("app版本号：" + appUtils.getCurrentAppVersionCode() + "   app版本名:" + appUtils.getCurrentAppVersionName());
        data.add("屏幕宽：" + DisplayUtils.getInstance().getScreenWidth() + "   屏幕高：" + DisplayUtils.getInstance().getScreenHeight() + "  density:" + DisplayUtils.getInstance().density());
        data.add("状态栏高度：" + DisplayUtils.getInstance().getHeightOfStatusBar());
        data.add("ActioBar高度：" + DisplayUtils.getInstance().getHeightOfActionBar());
        data.add("底部虚拟键高度：" + DisplayUtils.getInstance().getHeightOfNavigationBar());
        adapter.notifyDataSetChanged();
    }
}
