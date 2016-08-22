package com.zxl.androidtools.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.plugin.weight.choose.ToggleButton;
import com.zxl.androidtools.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @description：
 * @author：zxl
 * @CreateTime 2016/8/22.
 */
public class SwitchButtonActivity extends Activity {
    @Bind(R.id.toggleBtn)
    ToggleButton toggleBtn;
    @Bind(R.id.tv_toggle_status)
    TextView tvToggleStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);
        ButterKnife.bind(this);
        setToggleClick();
        toggleBtn.setToggleOn();
        tvToggleStatus.setText("开");
    }

    public void setToggleClick() {
        toggleBtn.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    tvToggleStatus.setText("开");
                } else {
                    tvToggleStatus.setText("关");
                }
            }
        });
    }
}
