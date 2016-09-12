package com.zxl.androidtools.ui.customviews;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.weight.progressbar.HorizontalProgressView;
import com.plugin.weight.progressbar.NumberProgressBar;
import com.zxl.androidtools.R;

import java.util.Timer;
import java.util.TimerTask;

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
    @Bind(R.id.numberPrgogessBar)
    NumberProgressBar numberPrgogessBar;

    private boolean isTimer = false;
    private Timer timer;

    @Override
    public int getLayoutId() {
        return R.layout.activity_progress;
    }

    @Override
    public void initView() {
        progressHorizontal.setProgress(50);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        numberPrgogessBar.incrementProgressBy(1);
                    }
                });
            }
        }, 0, 1000);
    }


    @OnClick(R.id.tv_timer)
    void btn2() {
        isTimer = !isTimer;
        numperBar.setStartTimer(isTimer);
        tvTimer.setText(isTimer ? getString(R.string.str_stop_timer) : getString(R.string.str_start_timer));
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}
