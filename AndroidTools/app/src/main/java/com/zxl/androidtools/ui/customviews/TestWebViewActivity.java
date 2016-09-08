package com.zxl.androidtools.ui.customviews;

import android.support.design.widget.Snackbar;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.weight.webview.ProgressWebView;
import com.zxl.androidtools.R;

import butterknife.Bind;

/**
 * @Description:
 * @Author: zxl
 * @Date: 8/9/16 PM3:03.
 */
public class TestWebViewActivity extends BaseAppCompatActivity implements ProgressWebView.OnPictureLongClickListener {

    @Bind(R.id.webview)
    ProgressWebView webview;

    private String path = "https://www.baidu.com/";

    @Override
    public int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    public void initView() {
        webview.loadUrl(path);
        webview.setOnPictureLongClickListener(this);
    }

    @Override
    public void onPictureLongClick(String PictureUrl) {
        Snackbar.make(webview, PictureUrl, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
