package com.plugin.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import com.plugin.utils.log.LogUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.plugin.utils.BuildConfig.VERSION_CODE;
import static com.plugin.utils.BuildConfig.VERSION_NAME;

/**
 * Android的系统方法
 * Created by zxl on 2016/11/4.
 */
public class SystemUtils {
    private static volatile SystemUtils mInstance = null;

    private SystemUtils() {
    }

    public static SystemUtils getInstance() {
        SystemUtils instance = mInstance;
        if (instance == null) {
            synchronized (SystemUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new SystemUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    //TODO 获取服务是否开启
    public boolean isRunningService(Context context, String className) {
        // 进程的管理者,活动的管理者
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
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

    //TODO app包名
    public String getAppPackageName(Context context) {
        return context.getPackageName();
    }

    //TODO app图标
    public Drawable getAppIcon(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
            return info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("PackageManager.NameNotFoundException");
        }
        return null;
    }

    //TODO app版本名
    public String getAppVersionName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("PackageManager.NameNotFoundException");
        }
        return null;
    }

    //TODO app版本号
    public int getAppVersionCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("PackageManager.NameNotFoundException");
        }
        return 0;
    }

    //TODO app名字
    public String getAppName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
            return info.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("PackageManager.NameNotFoundException");
        }
        return null;
    }

    //TODO app的权限
    public String[] getAppPremission(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            //获取到所有的权限
            return packinfo.requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("PackageManager.NameNotFoundException");
        }
        return null;
    }

    //TODO app的签名
    public String getAppSignature(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            return packinfo.signatures[0].toCharsString();
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("PackageManager.NameNotFoundException");
        }
        return null;
    }

    //TODO app Activity信息列表
    public ActivityInfo[] getAppActivityInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            return packinfo.activities;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("PackageManager.NameNotFoundException");
        }
        return null;
    }

    //TODO  获取设备MAC地址
    public String getMacAddress(Context context) {
        //需添加权限 android.permission.ACCESS_WIFI_STATE
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String macAddress = info.getMacAddress().replace(":", "");
        return macAddress == null ? "" : macAddress;
    }

    //TODO  获取设备MAC地址
    public String getMacAddressProcess() {
        //需添加权限 android.permission.ACCESS_WIFI_STATE
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

    //TODO 获取设备厂商
    public String getDeviceManufacture() {
        return Build.MANUFACTURER;
    }

    //TODO 获取设备型号，如MI2SC
    public String getDeviceType() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    //TODO 收集设备信息，用于信息统计分析
    public String collectDeviceInfoStr(Context context) {
        Properties prop = collectDeviceInfo(context);
        Set deviceInfos = prop.keySet();
        StringBuilder deviceInfoStr = new StringBuilder("{\n");
        for (Iterator iter = deviceInfos.iterator(); iter.hasNext(); ) {
            Object item = iter.next();
            deviceInfoStr.append("\t\t\t" + item + ":" + prop.get(item) + ", \n");
        }
        deviceInfoStr.append("}");
        return deviceInfoStr.toString();
    }

    public Properties collectDeviceInfo(Context context) {
        Properties mDeviceCrashInfo = new Properties();
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                mDeviceCrashInfo.put(VERSION_NAME, pi.versionName == null ? "not set" : pi.versionName);
                mDeviceCrashInfo.put(VERSION_CODE, pi.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Error while collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mDeviceCrashInfo.put(field.getName(), field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "Error while collect crash info", e);
            }
        }
        return mDeviceCrashInfo;
    }

    //TODO 主动回到Home，后台运行
    public void goHome(Context context) {
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(mHomeIntent);
    }

    //TODO  判断当前App处于前台还是后台状态
    public static boolean isApplicationBackground(Context context) {
        //权限 android.permission.GET_TASKS
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    //TODO 判断当前手机是否处于锁屏(睡眠)状态
    public boolean isSleeping(Context context) {
        KeyguardManager kgMgr = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return kgMgr.inKeyguardRestrictedInputMode();
    }

    //TODO 判断当前是否可用
    public boolean isNetAvailable(Context context) {
        //<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>  <uses-permission android:name="android.permission.INTERNET"/>
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                return true;
            }
        }
        return false;
    }
}
