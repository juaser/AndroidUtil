package com.zxl.androidtools.frg;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.plugin.utils.base.BaseFragment;
import com.zxl.androidtools.R;
import com.zxl.androidtools.ui.systemviews.TabHostActivity;

import butterknife.Bind;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/3/4
 */

public class TabHostFragment extends BaseFragment {
    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Override
    public int getLayoutId() {
        return R.layout.frg_tabhost;
    }

    @Override
    public void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String str = bundle.getString(TabHostActivity.TAB_FRAGMENTARGUMENTS, "");
            if (!TextUtils.isEmpty(str)) {
                tvTitle.setText(str);
            }
        }
    }
}
