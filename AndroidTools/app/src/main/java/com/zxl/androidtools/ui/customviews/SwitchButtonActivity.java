package com.zxl.androidtools.ui.customviews;

import android.widget.TextView;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.weight.choose.SwitchButton;
import com.plugin.weight.choose.ToggleButton;
import com.zxl.androidtools.R;

import butterknife.Bind;

/**
 * @description：
 * @author：zxl
 * @CreateTime 2016/8/22.
 */
public class SwitchButtonActivity extends BaseAppCompatActivity implements ToggleButton.OnToggleChanged, SwitchButton.OnChangeListener {
    @Bind(R.id.toggleBtn)
    ToggleButton toggleBtn;
    @Bind(R.id.tv_toggle_status)
    TextView tvToggleStatus;
    @Bind(R.id.switchBtn)
    SwitchButton switchBtn;
    @Bind(R.id.tv_switch_status)
    TextView tvSwitchStatus;

    @Override
    public int getLayoutId() {
        return R.layout.activity_switch;
    }

    /**
     * 初始化
     */
    public void initView() {
        toggleBtn.setToggleOn();
        tvToggleStatus.setText("开");
        switchBtn.setmSwitchOn(true);
        tvSwitchStatus.setText("开");
        toggleBtn.setOnToggleChanged(this);
        switchBtn.setOnChangeListener(this);
    }

    /**
     * ToggleButton监听
     */
    @Override
    public void onToggle(boolean on) {
        if (on) {
            tvToggleStatus.setText("开");
        } else {
            tvToggleStatus.setText("关");
        }
    }

    /**
     * SwitchButton监听
     */
    @Override
    public void onChange(SwitchButton sb, boolean state) {
        if (state) {
            tvSwitchStatus.setText("开");
        } else {
            tvSwitchStatus.setText("关");
        }
    }
}
