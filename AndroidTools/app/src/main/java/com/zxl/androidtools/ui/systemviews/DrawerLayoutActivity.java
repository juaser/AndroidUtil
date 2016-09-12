package com.zxl.androidtools.ui.systemviews;

import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.zxl.androidtools.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description:
 * @Author: zxl
 * @Date: 12/9/16 PM5:09.
 */
public class DrawerLayoutActivity extends BaseAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.tv_switch)
    TextView tvSwitch;
    @Bind(R.id.navigation)
    NavigationView navigation;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_drawerlayout;
    }

    @Override
    public void initView() {
        navigation.setNavigationItemSelectedListener(this);
    }

    @OnClick(R.id.tv_switch)
    void switchs() {
        if (isOpen()) {
            close();
        } else {
            opne();
        }
    }

    @Override
    public void onBackPressed() {
        if (isOpen()) {
            close();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setCheckable(true);
        close();
        switch (item.getItemId()) {
            case R.id.nav_home:
                Snackbar.make(tvSwitch, "Home", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.nav_messages:
                Snackbar.make(tvSwitch, "Messages", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.nav_friends:
                Snackbar.make(tvSwitch, "Friends", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.nav_discussion:
                Snackbar.make(tvSwitch, "Discussion", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.nav_sub_item1:
                Snackbar.make(tvSwitch, "Sub item 1", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.nav_sub_item2:
                Snackbar.make(tvSwitch, "Sub item 2", Snackbar.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    void opne() {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    void close() {
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    boolean isOpen() {
        return drawerLayout.isDrawerOpen(Gravity.LEFT);
    }
}
