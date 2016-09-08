package com.zxl.androidtools.ui.customviews;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.weight.progressbar.HorizontalProgressView;
import com.zxl.androidtools.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description:
 * @Author: zxl
 * @Date: 8/9/16 AM9:46.
 */
public class ProgessActivity extends BaseAppCompatActivity {
    @Bind(R.id.numperBar)
    HorizontalProgressView numperBar;
    @Bind(R.id.tv_timer)
    TextView tvTimer;
    @Bind(R.id.progress_horizontal)
    ProgressBar progressHorizontal;

    private boolean isTimer = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_progress;
    }

    @Override
    public void initView() {
        progressHorizontal.setProgress(50);
    }


    @OnClick(R.id.tv_timer)
    void btn2() {
        isTimer = !isTimer;
        numperBar.setStartTimer(isTimer);
        tvTimer.setText(isTimer ? getString(R.string.str_stop_timer) : getString(R.string.str_start_timer));
    }
}
