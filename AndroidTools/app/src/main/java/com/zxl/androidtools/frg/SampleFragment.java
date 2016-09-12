package com.zxl.androidtools.frg;

import android.widget.TextView;

import com.plugin.utils.base.BaseFragment;
import com.zxl.androidtools.R;
import com.zxl.androidtools.adapter.ViewPagerFragmentAdapter;

import butterknife.Bind;

/**
 * @Description:
 * @Author: zxl
 * @Date: 12/9/16 PM2:33.
 */
public class SampleFragment extends BaseFragment {
    @Bind(R.id.tv_sample)
    TextView tvSample;

    private int type = 0;

    @Override
    public int getLayoutId() {
        return R.layout.frg_sample;
    }

    @Override
    public void initView() {
        if (getArguments() != null) {
            type = getArguments().getInt(ViewPagerFragmentAdapter.Pager_TAG, 0);
        }
        tvSample.setText("页面" + type);
    }

}
