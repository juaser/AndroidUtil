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
    @Bind(R.id.flowcharView_glitter)
    FlowCharView flowcharViewGlitter;
    @Bind(R.id.flowcharView_step)
    FlowCharView flowcharViewStep;
    @Bind(R.id.et_char)
    EditText etChar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_flowchar;
    }

    @Override
    public void initView() {
        flowcharViewGlitter.setmDuaration(1000)
                .setFlowModel(FlowCharView.MODEL_GLITTER)
                .setResourseString("zhangxinglei")
                .startAnimator();
        flowcharViewStep.setFlowModel(FlowCharView.MODEL_STEPBYSTEP)
                .setIsLooper(false)
                .setResourseString("zhangxinglei")
                .startAnimator();
    }

    @OnClick(R.id.flowcharView_glitter)
    void click() {
        String mStr = etChar.getText().toString().trim();
        if (!TextUtils.isEmpty(mStr)) {
            flowcharViewGlitter.setResourseString(mStr);
        }
    }

    @OnClick(R.id.tv_start)
    void start() {
        if (flowcharViewGlitter != null) {
            flowcharViewGlitter.startAnimator();
        }
    }

    @OnClick(R.id.tv_stop)
    void stop() {
        flowcharViewGlitter.stopAnimator();
    }
}
