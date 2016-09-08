package com.zxl.androidtools.ui.customviews;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.weight.wave.WaterWaveView;
import com.zxl.androidtools.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description:
 * @Author: zxl
 * @Date: 1/9/16 上午11:34.
 */
public class CircleWaveActivity extends BaseAppCompatActivity {

    @Bind(R.id.view_wave)
    WaterWaveView viewWave;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.et_wave_progress)
    EditText etWaveProgress;

    @Override
    public int getLayoutId() {
        return R.layout.activity_circlewave;
    }

    @Override
    public void initView() {

    }

    @OnClick(R.id.tv_submit)
    void submit() {
        String str_progress = etWaveProgress.getText().toString().trim();
        int integer_progress = 0;
        if (TextUtils.isEmpty(str_progress)) {
            etWaveProgress.setText("0");
            integer_progress = 0;
        } else {
            integer_progress = Integer.parseInt(str_progress);
            if (integer_progress > 100) {
                integer_progress = 100;
            }
        }
        viewWave.setProgress(integer_progress);
    }
}
