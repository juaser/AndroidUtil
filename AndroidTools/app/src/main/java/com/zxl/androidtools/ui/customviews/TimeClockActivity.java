package com.zxl.androidtools.ui.customviews;

import android.app.Service;
import android.os.PowerManager;

import com.plugin.utils.base.BaseActivity;
import com.zxl.androidtools.R;

/**
 * @Description: 自定义一个时钟
 * @Author: zxl
 * @Date: 2017/1/19 10:39
 */

public class TimeClockActivity extends BaseActivity {
    private PowerManager powerManager = null;
    private PowerManager.WakeLock mWakeLock = null;

    @Override
    public int getLayoutId() {
        return R.layout.activity_timeclock;
    }

    @Override
    public void initView() {
        keepScreenLight();
    }

    /**
     * 保持屏幕不锁屏有两种方法
     * 第一种需要在AndroidManifest.xml添加权限  <uses-permission android:name="android.permission.WAKE_LOCK" />
     * 第二种  onCreate() 添加方法 getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
     */
    public void keepScreenLight() {
        //不锁屏
        powerManager = (PowerManager) this.getSystemService(Service.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        //是否需计算锁的数量
        mWakeLock.setReferenceCounted(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //唤起
        mWakeLock.acquire();
    }

    @Override
    protected void onDestroy() {
        //释放
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        super.onDestroy();
    }
}
