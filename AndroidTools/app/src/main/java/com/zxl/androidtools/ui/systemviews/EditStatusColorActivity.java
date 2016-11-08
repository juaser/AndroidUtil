package com.zxl.androidtools.ui.systemviews;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.plugin.utils.DialogUtils;
import com.plugin.utils.base.BaseActivity;
import com.zxl.androidtools.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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

    /**
     * 小米手机状态栏 为小米官方提供的解决方案，主要为MIUI内置了可以修改状态栏的模式，支持Dark和Light两种模式。
     *  网上摘抄 未实验 http://blog.csdn.net/kongbaidepao/article/details/52137677
     * @param activity
     * @param darkmode
     * @return
     */
    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 魅族手机状态栏
     *
     * @param activity
     * @param dark
     * @return
     */
    public static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}