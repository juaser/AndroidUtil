package com.zxl.androidtools.ui.customviews;

import android.text.TextUtils;
import android.widget.EditText;

import com.plugin.utils.base.BaseActivity;
import com.plugin.weight.flowchar.FlowCharView;
import com.zxl.androidtools.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Author: zxl
 * @Date: 16-12-28 下午11:45
 * @Description:
 */

public class FlowCharActivity extends BaseActivity {
    @Bind(R.id.flowcharView)
    FlowCharView flowcharView;
    @Bind(R.id.et_char)
    EditText etChar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_flowchar;
    }

    @Override
    public void initView() {
        flowcharView.setResourseString("zhang");
    }

    @OnClick(R.id.flowcharView)
    void click() {
        String mStr = etChar.getText().toString().trim();
        if (!TextUtils.isEmpty(mStr)) {
            flowcharView.setResourseString(mStr);
        }
    }

}
