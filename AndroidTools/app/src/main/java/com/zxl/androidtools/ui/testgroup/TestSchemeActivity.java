package com.zxl.androidtools.ui.testgroup;

import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;
import android.widget.TextView;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.zxl.androidtools.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description:
 * @Author: zxl
 * @Date: 6/9/16 上午11:17.
 */
public class TestSchemeActivity extends BaseAppCompatActivity {

    @Bind(R.id.tv_jump)
    TextView tvJump;
    @Bind(R.id.tv_jump_text)
    TextView tvJumpText;
    @Bind(R.id.et_scheme_authoeity)
    EditText etSchemeAuthoeity;
    @Bind(R.id.et_scheme_path)
    EditText etSchemePath;
    @Bind(R.id.et_scheme_query)
    EditText etSchemeQuery;
    @Bind(R.id.et_scheme_fragment)
    EditText etSchemeFragment;

    private String str_authority;
    private String str_path;
    private String str_query;
    private String str_fragment;

    private String str_jumpscheme = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_testscheme;
    }

    @Override
    public void initView() {

    }

    @OnClick(R.id.tv_jump)
    void jump() {
        getData();
        tvJumpText.setText("scheme:\n" + str_jumpscheme);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(str_jumpscheme));
        startActivity(intent);
    }

    /**
     * [scheme:][//authority][path][?query][#fragment]
     * 例如：http://www.java2s.com:8080/yourpath/fileName.htm?stove=10&path=32&id=4#harvic
     */
    public void getData() {
        str_authority = etSchemeAuthoeity.getText().toString().trim();
        str_path = etSchemePath.getText().toString().trim();
        str_query = etSchemeQuery.getText().toString().trim();
        str_fragment = etSchemeFragment.getText().toString().trim();
        str_jumpscheme = getString(R.string.str_scheme_testscheme) + "://" + str_authority + "/" + str_path + "?" + str_query + "#" + str_fragment;
    }
}
