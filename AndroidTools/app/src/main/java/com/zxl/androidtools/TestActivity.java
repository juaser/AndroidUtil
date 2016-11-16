package com.zxl.androidtools;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.plugin.utils.PathUtils;
import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.utils.image.ImageUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description:
 * @Author: zxl
 * @Date: 26/8/16.
 */
public class TestActivity extends BaseAppCompatActivity {
    @Bind(R.id.tv_test)
    TextView tvTest;
    @Bind(R.id.iv_test)
    ImageView ivTest;

    float scale = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initView() {
        Bitmap bitmap2 = ImageUtils.getInstance().getSmallBitmapFromResourse(this, R.drawable.ic_antumn_leaves);
        ImageUtils.getInstance().saveSimpleBitmap(bitmap2, PathUtils.getInstance().getImageDir() + File.separator + System.currentTimeMillis() + ".png");
        ivTest.setImageBitmap(bitmap2);
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
}
