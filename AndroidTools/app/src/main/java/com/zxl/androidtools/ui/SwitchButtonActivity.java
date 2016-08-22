package com.zxl.androidtools.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.plugin.weight.choose.SwitchButton;
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
    @Bind(R.id.switchBtn)
    SwitchButton switchBtn;
    @Bind(R.id.tv_switch_status)
    TextView tvSwitchStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);
        ButterKnife.bind(this);
        initView();
        setToggleClick();
        setSwitchClick();
    }

    public void initView() {
        toggleBtn.setToggleOn();
        tvToggleStatus.setText("开");
        switchBtn.setmSwitchOn(true);
        tvSwitchStatus.setText("开");
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

    public void setSwitchClick() {
        switchBtn.setOnChangeListener(new SwitchButton.OnChangeListener() {
            @Override
            public void onChange(SwitchButton sb, boolean state) {
                if (state) {
                    tvSwitchStatus.setText("开");
                } else {
                    tvSwitchStatus.setText("关");
                }
            }
        });
    }
}
