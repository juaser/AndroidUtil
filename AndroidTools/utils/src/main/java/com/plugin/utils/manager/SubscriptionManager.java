package com.plugin.utils.manager;

import java.util.HashMap;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @description： Subscription管理器
 * @author：zxl
 * @CreateTime 2016/8/22.
 */
public class SubscriptionManager {
    private final HashMap<Object, CompositeSubscription> SubscriptionMap = new HashMap<>();
    private static SubscriptionManager instance = new SubscriptionManager();

    private SubscriptionManager() {
    }

    /**
     * 单一实例
     */
    public static SubscriptionManager get() {
        return instance;
    }

    /**
     * 把发布者加入生命周期控制，当activity销毁的时候，会自动停止它的发布。防止内存溢出异常
     */
    public void addSubscription(Object object, Subscription s) {
        CompositeSubscription subscriptionList = SubscriptionMap.get(object);
        if (subscriptionList == null) {
            subscriptionList = new CompositeSubscription();
            SubscriptionMap.put(object, subscriptionList);
        }
        subscriptionList.add(s);
    }

    /**
     * 移除发布者，会停止其发布运行
     */
    public void removeSubscription(Object object, Subscription s) {
        CompositeSubscription subscriptionList = SubscriptionMap.get(object);
        if (subscriptionList != null) {
            subscriptionList.remove(s);
        }
    }

    /**
     * 清空发布者list，列表中发布者会全部停止
     */
    public void clearSubscription(Object object) {
        CompositeSubscription subscriptionList = SubscriptionMap.get(object);
        if (subscriptionList != null) {
            subscriptionList.clear();
        }
        SubscriptionMap.remove(object);
    }

    /**
     * 完全退出应用程序
     */
    public void AppExit() {
        try {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
