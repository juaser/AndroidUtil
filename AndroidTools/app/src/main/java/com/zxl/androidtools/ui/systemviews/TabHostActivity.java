package com.zxl.androidtools.ui.systemviews;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.zxl.androidtools.R;
import com.zxl.androidtools.frg.TabHostFragment;

import java.util.HashMap;

import butterknife.Bind;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/3/4
 */

public class TabHostActivity extends BaseAppCompatActivity {

    @Bind(android.R.id.tabs)
    TabWidget tabWidget;
    @Bind(android.R.id.tabhost)
    TabHost tabHost;

    public static final String TAB_DASHBOARD = "TAB_DASHBOARD";
    public static final String TAB_WARN = "TAB_WARN";
    public static final String TAB_WORK = "TAB_WORK";
    public static final String TAB_TOOL = "TAB_TOOL";
    public static final String TAB_SETTING = "TAB_SETTING";
    public static final String TAB_FRAGMENTARGUMENTS = "TAB_FRAGMENTARGUMENTS";
    private TabManager tab_manager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_tabhost;
    }

    @Override
    public void initView() {
        initTab();
    }

    public void initTab() {
        tabHost.setup();
        tabWidget.setDividerDrawable(null);
        tab_manager = new TabManager(this, tabHost, android.R.id.tabcontent);
        addTab2Manager(R.layout.layout_tab_dashboard, TAB_DASHBOARD);
        addTab2Manager(R.layout.layout_tab_warn, TAB_WARN);
        addTab2Manager(R.layout.layout_tab_work, TAB_WORK);
        addTab2Manager(R.layout.layout_tab_tool, TAB_TOOL);
        addTab2Manager(R.layout.layout_tab_setting, TAB_SETTING);
    }

    public void addTab2Manager(int layout_id, String tag) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View tab = inflater.inflate(layout_id, tabWidget, false);
        Bundle bundle = new Bundle();
        bundle.putString(TAB_FRAGMENTARGUMENTS, tag);
        tab_manager.addTab(tabHost.newTabSpec(tag).setIndicator(tab), TabHostFragment.class, bundle);
    }

    public class TabManager implements TabHost.OnTabChangeListener {
        private FragmentActivity activity;
        private FragmentManager fm;
        private TabHost tab_host;
        private int res_id;
        private final HashMap<String, TabInfo> tab_map = new HashMap<String, TabInfo>();
        private TabInfo current_tab;
        private boolean is_restore_activity = false;

        public TabManager(FragmentActivity act, TabHost th, int resId) {
            initTabManager(act, th, resId, true);
        }

        private void initTabManager(FragmentActivity act, TabHost th, int resId, boolean is_restore_activity) {
            activity = act;
            fm = act.getSupportFragmentManager();
            tab_host = th;
            res_id = resId;
            tab_host.setOnTabChangedListener(this);
            this.is_restore_activity = is_restore_activity;
        }

        @Override
        public void onTabChanged(String tab_id) {
            TabInfo will_show_tab = tab_map.get(tab_id);
            if (current_tab == will_show_tab) {
                return;
            }
            boolean changed = false;
            FragmentTransaction ft = fm.beginTransaction();
            if (current_tab != null) {
                if (current_tab.fragment != null) {
                    ft.hide(current_tab.fragment);
                    changed = true;
                }
            }
            if (will_show_tab != null) {
                if (will_show_tab.fragment == null) {
                    will_show_tab.fragment = Fragment.instantiate(activity, will_show_tab.klass.getName(), will_show_tab.bundle);
                    ft.add(res_id, will_show_tab.fragment, will_show_tab.tag);
                } else {
                    Fragment fragment = fm.findFragmentByTag(will_show_tab.tag);
                    if (fragment == null) {
                        ft.add(res_id, will_show_tab.fragment, will_show_tab.tag);
                    } else {
                        ft.show(will_show_tab.fragment);
                    }
                }
                changed = true;
            }
            current_tab = will_show_tab;
            if (changed) {
                ft.commitAllowingStateLoss();
                fm.executePendingTransactions();
            }
        }

        public void addTab(TabHost.TabSpec tab_spec, Class<?> clss, Bundle args) {
            tab_spec.setContent(getTabContentFactory(activity));
            String tag = tab_spec.getTag();
            TabInfo info = new TabInfo(tag, clss, args);
            Fragment fragment = fm.findFragmentByTag(tag);
            if (fragment != null) {
                FragmentTransaction ft = fm.beginTransaction();
                if (is_restore_activity) {
                    info.fragment = fragment;
                    ft.hide(info.fragment);
                } else {
                    ft.remove(fragment);
                }
                ft.commit();
            } else {
                fragment = Fragment.instantiate(activity, info.klass.getName(), info.bundle);
                FragmentTransaction ft = fm.beginTransaction();
                info.fragment = fragment;
                ft.add(res_id, info.fragment, info.tag);
                if (is_restore_activity) {
                    ft.hide(info.fragment);
                } else {
                    ft.remove(fragment);
                }
                ft.commitAllowingStateLoss();
                fm.executePendingTransactions();
            }

            tab_map.put(tag, info);
            tab_host.addTab(tab_spec);
        }

        public Fragment getFragmentByTag(String tag) {
            return tab_map.get(tag).fragment;
        }

        private TabHost.TabContentFactory getTabContentFactory(final Context context) {
            return new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    View v = new View(context);
                    v.setMinimumWidth(0);
                    v.setMinimumHeight(0);
                    return v;
                }
            };
        }

        private class TabInfo {
            private final String tag;
            private final Class<?> klass;
            private final Bundle bundle;
            private Fragment fragment;

            TabInfo(String _tag, Class<?> _klass, Bundle _bundle) {
                tag = _tag;
                klass = _klass;
                bundle = _bundle;
            }
        }
    }
}
