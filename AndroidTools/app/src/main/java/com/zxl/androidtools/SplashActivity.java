package com.zxl.androidtools;

import android.content.Intent;
import android.graphics.Color;
import android.widget.ImageView;

import com.plugin.utils.TimeUtils;
import com.plugin.utils.base.BaseActivity;
import com.plugin.weight.flowchar.FlowCharView;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/1/3 11:30
 */

public class SplashActivity extends BaseActivity {
    @Bind(R.id.iv_splash)
    ImageView ivSplash;
    @Bind(R.id.flowcharview_splash)
    FlowCharView flowcharviewSplash;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        String currentTime = TimeUtils.getInstance().getCurrentString(new SimpleDateFormat("yyyy-MM-dd"));
        flowcharviewSplash.setmColorBg(Color.TRANSPARENT)
                .setIsLooper(false)
                .setResourseString(currentTime)
                .setFlowModel(FlowCharView.MODEL_STEPBYSTEP)
                .setmDuaration(2000)
                .setOnFinishedListner(new FlowCharView.OnFinishedListener() {
                    @Override
                    public void finished() {
                        jump();
                    }
                })
                .startAnimator();
    }

    @OnClick(R.id.flowcharview_splash)
    void click() {
        flowcharviewSplash.stopAnimator();
        jump();
    }

    public void jump() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
