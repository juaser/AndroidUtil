package com.zxl.androidtools;

import android.graphics.Color;
import android.widget.TextView;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.weight.image.HeartView;

import java.util.Random;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description: 制作一个可以颜色变化的心http://www.jianshu.com/p/9423ca99c303
 * @Author: zxl
 * @Date: 26/8/16.
 */
public class TestActivity extends BaseAppCompatActivity {
    @Bind(R.id.tv_test)
    TextView tvTest;
    @Bind(R.id.iv_test)
    HeartView ivTest;

    float scale = 1;
    private Random random = new Random();

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initView() {
        ivTest.addHeart();
    }

    @OnClick(R.id.tv_test)
    void click() {
        if (scale == 1) {
            scale = 1.1f;
        } else {
            scale = 1;
        }
        tvTest.animate().scaleX(scale);
        tvTest.animate().scaleY(scale);
    }

    @OnClick(R.id.iv_test)
    void heartClick() {
        ivTest.addHeart();
    }

    /**
     * 获得一个随机的颜色
     *
     * @param rate
     * @return
     */
    public int randomColor(int rate) {
        int red = random.nextInt(256) / rate, green = random.nextInt(256) / rate, blue = random.nextInt(256) / rate;
        return Color.rgb(red, green, blue);
    }
}
