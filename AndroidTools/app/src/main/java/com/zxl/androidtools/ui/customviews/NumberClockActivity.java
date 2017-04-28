package com.zxl.androidtools.ui.customviews;

import android.app.Service;
import android.graphics.Color;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;

import com.plugin.utils.TimeUtils;
import com.plugin.weight.dynamicweather.DynamicWeatherView;
import com.plugin.weight.dynamicweather.RainTypeImpl;
import com.plugin.weight.flowchar.NumberClockView;
import com.zxl.androidtools.R;
import com.zxl.androidtools.ui.systemviews.FullScreenActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description: 数字时钟
 * @Author: zxl
 * @Date: 2017/1/19 17:12
 */

public class NumberClockActivity extends FullScreenActivity {

    private PowerManager powerManager = null;
    private PowerManager.WakeLock mWakeLock = null;
    @Bind(R.id.numberclock)
    NumberClockView mNumberClockView;
    @Bind(R.id.dynamic_weather_view)
    DynamicWeatherView mDynamicWeatherView;

    private int count = 0;
    private long lastTime = 0;
    private long currentTime = 0;
    private long distance;

    private boolean isWeather = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_numberclock;
    }

    @Override
    public void initView() {
        keepScreenLight();
        mDynamicWeatherView.setType(new RainTypeImpl(this, mDynamicWeatherView));
        mDynamicWeatherView.stopDraw();
    }

    @OnClick(R.id.numberclock)
    void click() {
        if (isChangeBg()) {
            if (isWeather) {
                mDynamicWeatherView.stopDraw();
                mNumberClockView.setmColorBg(Color.BLACK);
            } else {
                mNumberClockView.setmColorBg(0x00000000);
                mDynamicWeatherView.startDraw();
            }
            isWeather = !isWeather;
        }
    }

    @OnClick(R.id.v_set_light)
    void setLight() {
        setWindowMaxLight();
    }

    @OnClick(R.id.v_remove_light)
    void removeLight() {
        removeWindowMaxLight();
    }

    public boolean isChangeBg() {
        currentTime = TimeUtils.getInstance().getCurrentTimeStamp();
        distance = currentTime - lastTime;
        if (distance < 500) {
            count++;
        } else {
            count = 0;
        }
        lastTime = currentTime;
        if (count == 3) {
            count = 0;
            return true;
        }
        return false;
    }

    public void keepScreenLight() {
        //不锁屏
        powerManager = (PowerManager) this.getSystemService(Service.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        //是否需计算锁的数量
        mWakeLock.setReferenceCounted(false);
    }

    public void setWindowMaxLight() {
        Window window = getWindow();
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
        windowLayoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
        window.setAttributes(windowLayoutParams);
    }

    public void removeWindowMaxLight() {
        Window window = getWindow();
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
        windowLayoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        window.setAttributes(windowLayoutParams);
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
