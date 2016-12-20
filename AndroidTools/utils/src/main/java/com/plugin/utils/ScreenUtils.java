package com.plugin.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;

import com.plugin.utils.log.LogUtils;
import com.plugin.utils.manager.AppManager;

/**
 * @Description: 屏幕相关的工具类
 * @Author: zxl
 * @Date: 1/9/16 上午10:56.
 */
public class ScreenUtils {

    private static volatile ScreenUtils mInstance = null;

    private ScreenUtils() {
    }

    public static ScreenUtils getInstance() {
        ScreenUtils instance = mInstance;
        if (instance == null) {
            synchronized (ScreenUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new ScreenUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    /**
     * @description: 获取上下文
     */
    public Context getContext() {
        return AppManager.getInstance().getTop();
    }

    /**
     * 获取屏幕的宽度px
     */
    public int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        return outMetrics.widthPixels;
    }

    /**
     * 获取屏幕的高度px
     */
    public int getScreenHeight() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        return outMetrics.heightPixels;
    }

    /**
     * 设置透明状态栏(api大于19方可使用)
     * 可在Activity的onCreat()中调用 需在顶部控件布局中加入以下属性让内容出现在状态栏之下
     * android:clipToPadding="true"
     * android:fitsSystemWindows="true"
     */
    public void setTransparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            Activity activity = (Activity) getContext();
            activity.getWindow().addFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (contentView.getChildCount() > 1) {
                contentView.getChildAt(1).setBackgroundColor(Color.argb(0, 0, 0, 0));
            } else {
                contentView.addView(createNewView(activity, Color.argb(0, 0, 0, 0)));
            }
        }
    }

    /**
     * 创建一个宽度充满全屏，高度是状态值大小的 背景颜色自定义的view
     */
    private View createNewView(Context context, int color) {
        View view = new View(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getHeightOfStatusBar());
        view.setLayoutParams(params);
        view.setBackgroundColor(color);
        return view;
    }

    /**
     * 隐藏状态栏
     * 也就是设置全屏，一定要在setContentView之前调用，否则报错
     * 此方法Activity可以继承AppCompatActivity
     * <p>启动的时候状态栏会显示一下再隐藏，比如QQ的欢迎界面</p>
     * <p>在配置文件中Activity加属性android:theme="@android:style/Theme.NoTitleBar.Fullscreen"</p>
     * <p>如加了以上配置Activity不能继承AppCompatActivity，会报错</p>
     *
     * @param activity activity
     */

    public void hideStatusBar(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 获取状态栏高度
     */
    public int getHeightOfStatusBar() {
        Resources resources = getContext().getResources();
        int height = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 获取虚拟导航栏的高度
     */
    public int getHeightOfNavigationBar() {
        Resources resources = getContext().getResources();
        int height = 0;
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0)
            height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 判断状态栏是否存在
     *
     * @return true: 存在<br>false: 不存在
     */
    public boolean isStatusBarExists() {
        Activity activity = (Activity) getContext();
        LayoutParams params = activity.getWindow().getAttributes();
        return (params.flags & LayoutParams.FLAG_FULLSCREEN) != LayoutParams.FLAG_FULLSCREEN;
    }

    /**
     * 获取ActionBar高度
     */
    public int getHeightOfActionBar() {
        try {
            TypedValue tv = new TypedValue();
            Activity activity = (Activity) getContext();
            if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                return TypedValue.complexToDimensionPixelSize(tv.data, getContext().getResources().getDisplayMetrics());
            }
        } catch (Exception e) {
            LogUtils.e("Exception" + e.getMessage());
        }
        return 0;
    }

    /**
     * 显示通知栏  需添加权限 android.permission.EXPAND_STATUS_BAR
     */
    public void showNotificationBar(boolean isSettingPanel) {
        String methodName = "";
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {//<=16
            methodName = "expand";
        } else {
            if (isSettingPanel) {
                methodName = "expandSettingsPanel";//打开设置
            } else {
                methodName = "expandNotificationsPanel";// 打开通知
            }
        }
        ReflectUtils.getInstance().invokePanels(getContext(), methodName);
    }

    /**
     * 隐藏通知栏  需添加权限 android.permission.EXPAND_STATUS_BAR
     */
    public void hideNotificationBar() {
        String methodName = "";
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {//<=16
            methodName = "collapse";
        } else {
            methodName = "collapsePanels";
        }
        ReflectUtils.getInstance().invokePanels(getContext(), methodName);
    }


    /**
     * 设置屏幕为横屏
     * <p>还有一种就是在Activity中加属性android:screenOrientation="landscape"</p>
     * <p>不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次</p>
     * <p>设置Activity的android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次</p>
     * <p>设置Activity的android:configChanges="orientation|keyboardHidden|screenSize"（4.0以上必须带最后一个参数）时
     * 切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法</p>
     */
    public void setLandscape() {
        Activity activity = (Activity) getContext();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     */
    public Bitmap captureWithStatusBar() {
        Activity activity = (Activity) getContext();
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth();
        int height = getScreenHeight();
        Bitmap bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏  需要用到上面获取状态栏高度getStatusBarHeight的方法
     */
    public Bitmap captureWithoutStatusBar() {
        Activity activity = (Activity) getContext();
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int statusBarHeight = getHeightOfStatusBar();
        int width = getScreenWidth();
        int height = getScreenHeight();
        Bitmap bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 判断是否锁屏
     *
     * @return true: 是<br>false: 否
     */
    public boolean isScreenLock() {
        try {
            KeyguardManager km = (KeyguardManager) getContext().getSystemService(Context.KEYGUARD_SERVICE);
            if (km != null)
                return km.inKeyguardRestrictedInputMode();
            else
                return true;
        } catch (Exception e) {
            LogUtils.e("Exception" + e.getMessage());
        }
        return true;
    }
}
