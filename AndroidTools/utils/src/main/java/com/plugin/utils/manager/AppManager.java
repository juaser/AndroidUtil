package com.plugin.utils.manager;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.plugin.utils.log.LogUtils;

import java.util.Stack;

/**
 * @类说明： activity 管理类
 * @author：zxl
 * @CreateTime 2016/7/27.
 */
public class AppManager {
    final Stack<Activity> activityStack = new Stack<>();
    Application application;

    public Activity getTop() {
        Activity top = null;
        synchronized (AppManager.class) {
            try {
                top = activityStack.peek();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return top;
    }

    public void finishTop() {
        synchronized (AppManager.class) {
            try {
                Activity activity = activityStack.pop();
                if (activity != null) {
                    activity.finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void add(Activity activity) {
        activityStack.add(activity);
        ld("ActivityStack==add---" + activityStack.size());
    }

    public void remove(Activity activity) {
        activityStack.remove(activity);
        ld("ActivityStack==remove---" + activityStack.size());
    }

    public Application getApp() {
        return application;
    }

    public void setApp(Application application) {
        this.application = application;
    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例没有绑定关系，
     * 而且只有被调用到才会装载，从而实现了延迟加载
     */
    private static class SingletonHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static final AppManager instance = new AppManager();
    }

    /**
     * 私有化构造方法
     */
    private AppManager() {
    }

    public static AppManager getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 即可跳转,跳转只需类名即可
     */
    public void jump(Class T) {
        Activity activity = getTop();
        if (activity != null) {
            activity.startActivity(new Intent(activity, T));
        }
    }

    public void le(String msg) {
        LogUtils.e(msg);
    }

    public void ld(String msg) {
        LogUtils.d(msg);
    }

}
