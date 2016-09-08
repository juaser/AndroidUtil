package com.plugin.weight.webview;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.plugin.utils.log.LogUtils;
import com.plugin.weight.progressbar.HorizontalProgressView;

/**
 * @Description:
 * @Author: zxl
 * @Date: 8/9/16 PM2:46.
 */
public class ProgressWebView extends WebView implements View.OnCreateContextMenuListener {

    public static HorizontalProgressView progressbar;
    public static boolean horizontalProgressBarEnable = true;
    public Context mContext;

    public ProgressWebView(Context context) {
        this(context, null, 0);
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public void init() {
        if (isInEditMode()) {
            return;
        }
        if (horizontalProgressBarEnable) {
            progressbar = new HorizontalProgressView(mContext);
            progressbar.setProgress_reached_color(Color.GREEN);
            addView(progressbar);
        }
        initWebView();
    }

    /**
     * LOAD_DEFAULT: 如果我们应用程序没有设置任何cachemode， 这个是默认的cache方式。加载一张网页会检查是否有cache，如果有并且没有过期则使用本地cache，否则从网络上获取。
     * LOAD_CACHE_ELSE_NETWORK: 使用cache资源，即使过期了也使用，如果没有cache才从网络上获取。
     * LOAD_NO_CACHE: 不使用cache 全部从网络上获取。
     * LOAD_CACHE_ONLY:  只使用cache上的内容。
     * settings.setBlockNetworkImage(false);//设置网页浏览为无图模式。true为无图模式。
     * settings.setSupportZoom(true);//支持缩放
     * settings.setUseWideViewPort(false);//设置可以缩放
     * settings.setBuiltInZoomControls(true);//设置显示缩放按钮 启用后，用户一旦触摸屏幕，就会出现缩放控制图标。这个图标过上几秒会自动消失，但在3.X系统上，如果图标自动消失前退出当前Activity的话，就会报异常。
     * settings.setLoadWithOverviewMode(true);//设置网页适应webView的大小。一般与setUseWideViewPort(true);一起使用。
     * settings.setCacheMode(WebSettings.LOAD_DEFAULT);//设值缓存的类型
     * settings.setAppCacheEnabled(false);//设置允许html5缓存。设置html5缓存路径。
     * settings.setDatabaseEnabled(false);//  设置启用数据库，用于离线缓存
     * settings.setAllowFileAccess(false);//设置允许访问文件。
     */
    public void initWebView() {
        //基本的设置
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);// 设置网页允许脚本。
        settings.setLoadsImagesAutomatically(true); //支持自动加载图片
        settings.setDisplayZoomControls(false);//隐藏webview缩放按钮

        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        setWebChromeClient(new WebChromeClient());//关联进度条
        setOnCreateContextMenuListener(this);
        setWebViewClient(new WebViewClient());
    }

    /**
     * 获取想要的连接
     *
     * @param contextMenu
     * @param view
     * @param contextMenuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        if (view instanceof WebView) {
            HitTestResult hitTestResult = ((WebView) view).getHitTestResult();
            if (hitTestResult != null) {
                int type = hitTestResult.getType();
                if (type == HitTestResult.IMAGE_TYPE || type == HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    String pictureUrl = hitTestResult.getExtra();
                    LogUtils.d("获取到图片地址pictureUrl=：" + pictureUrl);
                    if (!TextUtils.isEmpty(pictureUrl)) {
                        if (onPictureLongClickListener != null) {
                            onPictureLongClickListener.onPictureLongClick(pictureUrl);
                        }
                    }
                }
            }
        }
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (horizontalProgressBarEnable && progressbar != null) {
            LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
            lp.x = l;
            lp.y = t;
            progressbar.setLayoutParams(lp);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 和进度条进行关联
     */
    private class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (horizontalProgressBarEnable && progressbar != null) {
                if (newProgress == 100) {
                    progressbar.setVisibility(GONE);
                } else {
                    if (progressbar.getVisibility() == GONE)
                        progressbar.setVisibility(VISIBLE);
                    progressbar.setProgress_current(newProgress);
                }
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    /**
     * 设置浏览器标识
     */
    public void setUserAgent() {
        String ua = getSettings().getUserAgentString();
        getSettings().setUserAgentString(ua + "android");
    }

    private OnPictureLongClickListener onPictureLongClickListener;

    public interface OnPictureLongClickListener {
        void onPictureLongClick(String PictureUrl);
    }

    public OnPictureLongClickListener getOnPictureLongClickListener() {
        return onPictureLongClickListener;
    }

    public void setOnPictureLongClickListener(OnPictureLongClickListener onPictureLongClickListener) {
        this.onPictureLongClickListener = onPictureLongClickListener;
    }
}
