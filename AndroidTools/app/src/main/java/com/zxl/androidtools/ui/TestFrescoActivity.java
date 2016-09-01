package com.zxl.androidtools.ui;

import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.plugin.utils.base.BaseActivity;
import com.zxl.androidtools.R;

import butterknife.Bind;

/**
 * @Description:
 * @Author: zxl
 * @Date: 1/9/16 下午5:50.
 */
public class TestFrescoActivity extends BaseActivity {
    @Bind(R.id.simpleview_1)
    SimpleDraweeView simpleview1;

    String path1 = "http://ww1.sinaimg.cn/mw600/6345d84ejw1dvxp9dioykg.gif";
    String path = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_testfresco;
    }

    @Override
    public void initView() {
        showGif();
    }

    public void showGif() {
        Uri uri = Uri.parse(path1);
        DraweeController draweeController =
                Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                        .build();
        simpleview1.setController(draweeController);
    }
}
