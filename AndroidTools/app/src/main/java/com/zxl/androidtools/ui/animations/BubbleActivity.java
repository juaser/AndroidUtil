package com.zxl.androidtools.ui.animations;

import android.graphics.Color;

import com.plugin.utils.base.BaseActivity;
import com.plugin.weight.image.BubbleView;
import com.plugin.weight.image.HeartImageView;
import com.zxl.androidtools.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2016/11/17 14:46
 */

public class BubbleActivity extends BaseActivity {
    @Bind(R.id.view_heart)
    HeartImageView viewHeart;
    @Bind(R.id.view_bubble)
    BubbleView viewBubble;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bubble;
    }

    @Override
    public void initView() {
        viewHeart.setColor(Color.RED);
    }

    @OnClick(R.id.view_heart)
    void click() {
        viewBubble.addView();
    }
}
