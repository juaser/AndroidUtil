package com.zxl.androidtools.ui.systemviews;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.zxl.androidtools.R;

/**
 * @Description: 沉浸式页面
 * @Author: zxl
 * @Date: 18/9/16 PM12:06.
 */
public class ImmersiveActivity extends BaseAppCompatActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_immersive;
    }

    @Override
    public void initView() {
        if (Build.VERSION.SDK_INT >= 19) {//沉浸式 4.4+
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * SYSTEM_UI_FLAG_FULLSCREEN  全屏
     * SYSTEM_UI_FLAG_HIDE_NAVIGATION 导航栏 隐藏
     * <p/>
     * SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
     * SYSTEM_UI_FLAG_LAYOUT_STABLE 表示会让应用的主体内容占用系统状态栏的空间
     * SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION 会让应用的主体内容占用系统导航栏的空间
     * <p/>
     * SYSTEM_UI_FLAG_IMMERSIVE_STICKY 让系统栏在一段时间后自动隐藏
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    /**
     * 状态栏透明
     */
    public void test1() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
