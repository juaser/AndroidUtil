package com.zxl.androidtools.ui;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.TextView;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.zxl.androidtools.R;

import butterknife.Bind;

/**
 * @Description: 通过scheme 启动此Activity  [scheme:][//authority][path][?query][#fragment]
 * @Author: zxl
 * @Date: 6/9/16 上午11:02.
 */
public class TestSchemeResultActivity extends BaseAppCompatActivity {
    @Bind(R.id.tv_text)
    TextView tvText;

    private String str_authority;
    private String str_path;
    private String str_query;
    private String str_fragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_testscheme_result;
    }

    @Override
    public void initView() {
        doIntent(getIntent());
    }

    public void doIntent(Intent intent) {
        if (null != intent && null != intent.getAction() && null != intent.getScheme()
                && TextUtils.equals(intent.getScheme(), getString(R.string.str_scheme_testscheme))) {
            Uri schemeUri = intent.getData();
            str_authority = schemeUri.getAuthority();
            str_path = schemeUri.getPath();
            str_query = schemeUri.getQuery();
            str_fragment = schemeUri.getFragment();
            tvText.setText("str_authority==" + str_authority +
                    "\nstr_path==" + str_path +
                    "\nstr_query==" + str_query +
                    "\nstr_fragment==" + str_fragment);
        } else {
            tvText.setText("不是通过Scheme跳转");
        }
    }
}
