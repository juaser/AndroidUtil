package com.plugin.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;

import com.plugin.utils.log.LogUtils;
import com.plugin.utils.manager.AppManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;

/**
 * @description： 系统的信息 包名，网络，WiFi信息，版本号，icon，设备信息
 * @author：zxl
 * @CreateTime 2016/8/22.
 */
public class SystemInfoUtils {
    private static Context mContext;
    private PackageManager pm;
    private static volatile SystemInfoUtils mSystemInfoUtils = null;
    private String packname = "";

    public SystemInfoUtils() {
        pm = getContext().getPackageManager();
        packname = getContext().getPackageName();
    }

    public static SystemInfoUtils getInstance(Context context) {
        mContext = context;
        SystemInfoUtils systemInfoUtils = mSystemInfoUtils;
        if (systemInfoUtils == null) {
            synchronized (SystemInfoUtils.class) {
                systemInfoUtils = mSystemInfoUtils;
                if (systemInfoUtils == null) {
                    systemInfoUtils = new SystemInfoUtils();
                    mSystemInfoUtils = systemInfoUtils;
                }
            }
        }
        return mSystemInfoUtils;
    }

    /**
     * 获取上下文对象
     *
     * @return
     */
    public Context getContext() {
        return AppManager.getInstance().getTop();
    }

    /**
     * 获取服务是否开启
     *
     * @param className 完整包名的服务类名
     * @return true: 是<br>false: 否
     */
    public boolean isRunningService(String className) {
        // 进程的管理者,活动的管理者
        ActivityManager activityManager = (ActivityManager)
                getContext().getSystemService(Context.ACTIVITY_SERVICE);
        // 获取正在运行的服务，最多获取1000个
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(1000);
        // 遍历集合
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            ComponentName service = runningServiceInfo.service;
            if (className.equals(service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取程序 图标
     */
    public Drawable getAppIcon() {
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("PackageManager.NameNotFoundException");
        }
        return null;
    }

    /**
     * 获取程序的版本号
     */
    public String getAppVersion(String packname) {
        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, 0);
            return packinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("PackageManager.NameNotFoundException");
        }
        return null;
    }


    /**
     * 获取程序的名字
     */
    public String getAppName() {
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("PackageManager.NameNotFoundException");
        }
        return null;
    }

    /**
     * 获取程序的权限
     */
    public String[] getAppPremission() {
        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_PERMISSIONS);
            //获取到所有的权限
            return packinfo.requestedPermissions;

        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("PackageManager.NameNotFoundException");
        }
        return null;
    }


    /**
     * 获取程序的签名
     */
    public String getAppSignature() {
        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_SIGNATURES);
            //获取到所有的权限
            return packinfo.signatures[0].toCharsString();
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("PackageManager.NameNotFoundException");
        }
        return null;
    }

    /**
     * 获取程序的Activity信息列表
     */
    public ActivityInfo[] getActivityInfo() {
        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_ACTIVITIES);
            //获取到所有的权限
            return packinfo.activities;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("PackageManager.NameNotFoundException");
        }
        return null;
    }

    /**
     * 获取app包名
     *
     * @return
     */
    public String getPackname() {
        return packname;
    }

    /**
     * 获取设备MAC地址
     * <p>需添加权限 android.permission.ACCESS_WIFI_STATE</p>
     *
     * @return MAC地址
     */
    public String getMacAddress() {
        WifiManager wifi = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String macAddress = info.getMacAddress().replace(":", "");
        return macAddress == null ? "" : macAddress;
    }

    /**
     * 获取设备MAC地址
     * <p>需添加权限 android.permission.ACCESS_WIFI_STATE</p>
     *
     * @return MAC地址
     */
    public String getMacAddressProcess() {
        String macAddress = null;
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader reader = new LineNumberReader(ir);
            macAddress = reader.readLine().replace(":", "");
        } catch (IOException ex) {
            LogUtils.e("IOException");
        }
        return macAddress == null ? "" : macAddress;
    }

    /**
     * 获取设备厂商，如Xiaomi
     *
     * @return 设备厂商
     */
    public String getManufacturer() {
        String MANUFACTURER = Build.MANUFACTURER;
        return MANUFACTURER;
    }

    /**
     * 获取设备型号，如MI2SC
     *
     * @return 设备型号
     */
    public String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }
}
