package com.zxl.androidtools.ui.customviews;

import com.plugin.utils.DialogUtils;
import com.plugin.utils.base.BaseAppCompatActivity;
import com.zxl.androidtools.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/1/19 17:41
 */

public class DialogActivity extends BaseAppCompatActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_dialog;
    }

    @Override
    public void initView() {

    }

    @OnClick(R.id.tv_dialog_okcancel)
    void clickOkCancel() {
        DialogUtils.getInstance().showOKCancleDialog(this, "测试", "取消", "确定", true, true, null, null);
    }

    @OnClick(R.id.tv_dialog_ok)
    void clickOk() {
        DialogUtils.getInstance().showOKDialog(this, "测试", "确定", true, true, null);
    }

    @OnClick(R.id.tv_bottom_cancel)
    void clickBottomCancle() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            data.add("i==" + i);
        }
        DialogUtils.getInstance().showBottomUnitSelectDialog(this, data, true, true, null);
    }
}
