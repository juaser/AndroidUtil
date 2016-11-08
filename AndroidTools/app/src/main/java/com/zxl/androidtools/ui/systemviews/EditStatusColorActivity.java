package com.zxl.androidtools.ui.systemviews;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.plugin.utils.DialogUtils;
import com.plugin.utils.base.BaseActivity;
import com.zxl.androidtools.R;

import butterknife.OnClick;

/**
 * 修改状态栏颜色 API>=19
 * Created by zxl on 2016/11/8.
 */

public class EditStatusColorActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_editstatuscolor;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_status_green));
    }

    @Override
    public void initView() {

    }

    @OnClick(R.id.tv_top)
    void top() {
        DialogUtils.getInstance().showDialog(R.layout.layout_loading);
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View statusView = createStatusBarView(color);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            decorView.addView(statusView);
            //设置根布局参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
            if (rootView == null) {
                Log.e("TAG", "rootView==null");
                return;
            }
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    /**
     * 生成一个和状态栏大小相同的半透明矩形条
     *
     * @param color 状态栏颜色值
     * @return 状态栏矩形条
     */
    private View createStatusBarView(int color) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(this);
        // 获得状态栏高度
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int height = getResources().getDimensionPixelSize(resourceId);
        Log.e("TAG", "statusBarHright==" + height);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }
}
