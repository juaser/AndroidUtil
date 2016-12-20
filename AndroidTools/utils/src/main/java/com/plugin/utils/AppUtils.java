package com.plugin.utils;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.plugin.utils.bean.AppInfoBean;
import com.plugin.utils.log.LogUtils;
import com.plugin.utils.manager.AppManager;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.plugin.utils.BuildConfig.VERSION_CODE;
import static com.plugin.utils.BuildConfig.VERSION_NAME;

/**
 * @description： app的信息
 * @author：zxl
 * @CreateTime 2016/8/22.
 */
public class AppUtils {

    private static volatile AppUtils mInstance = null;
    private AppInfoBean currentAppInfoBean = null;

    private AppUtils() {
    }

    public static AppUtils getInstance() {
        AppUtils instance = mInstance;
        if (instance == null) {
            synchronized (AppUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new AppUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
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
     * 安装App
     * <p>根据路径安装App</p>
     *
     * @param filePath 文件路径
     */
    public void installApp(String filePath) {
        installApp(new File(filePath));
    }

    /**
     * 安装App
     * <p>根据文件安装App</p>
     *
     * @param file 文件
     */
    public void installApp(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    /**
     * 卸载指定包名的App
     *
     * @param packageName 包名
     */
    public void uninstallApp(String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    /**
     * 获取当前App信息（名称，图标，包名，版本名，版本号,权限，activity集合，设备厂商，设备型号,签名）
     */
    public AppInfoBean getAppInfo() {
        if (currentAppInfoBean != null) {
            LogUtils.e("currentAppInfoBean != null");
            return currentAppInfoBean;
        }
        PackageManager packageManager = getContext().getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("PackageManager.NameNotFoundException");
        }
        currentAppInfoBean = saveAppInfo(packageManager, packageInfo);
        return currentAppInfoBean;
    }

    /**
     * 封装根据包管理器获取到的app信息
     */
    public AppInfoBean saveAppInfo(PackageManager packageManager, PackageInfo packageInfo) {
        if (packageManager != null && packageInfo != null) {
            AppInfoBean appInfoBean = new AppInfoBean();
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            appInfoBean.setApp_name(applicationInfo.loadLabel(packageManager).toString());//app名称
            appInfoBean.setApp_icon(applicationInfo.loadIcon(packageManager));//app图标
            appInfoBean.setApp_packageName(applicationInfo.packageName);//app包名
            appInfoBean.setApp_versionCode(packageInfo.versionCode);//app版本号
            appInfoBean.setApp_versionName(packageInfo.versionName);//appa版本名
            appInfoBean.setDevice_manufacture(Build.MANUFACTURER);//厂商
            appInfoBean.setDevice_type(Build.MODEL == null ? "" : Build.MODEL.trim().replaceAll("\\s*", ""));//设备类型
            appInfoBean.setDevice_unique_id(getDeviceUniqueId());//设备唯一标志吗
            try {
                PackageInfo packageInfo_activity = packageManager.getPackageInfo(applicationInfo.packageName, PackageManager.GET_ACTIVITIES);
                appInfoBean.setApp_activityInfos(packageInfo_activity.activities);//app的所有activity
            } catch (PackageManager.NameNotFoundException e) {
                LogUtils.e("PackageManager.NameNotFoundException----GET_ACTIVITIES");
            }
            try {
                PackageInfo packageInfo_permissions = packageManager.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);
                appInfoBean.setApp_premissions(packageInfo_permissions.requestedPermissions);//app需要的权限
            } catch (PackageManager.NameNotFoundException e) {
                LogUtils.e("PackageManager.NameNotFoundException----GET_PERMISSIONS");
            }
            try {
                PackageInfo packageInfo_signatures = packageManager.getPackageInfo(applicationInfo.packageName, PackageManager.GET_SIGNATURES);
                Signature[] signatures = packageInfo_signatures.signatures;
                if (signatures != null && signatures[0] != null) {
                    appInfoBean.setApp_signature(signatures[0].toCharsString());//app的签名
                } else {
                    appInfoBean.setApp_signature("no signature");
                }
                signatures = null;
            } catch (PackageManager.NameNotFoundException e) {
                LogUtils.e("PackageManager.NameNotFoundException----GET_SIGNATURES");
            }
            return appInfoBean;
        }
        return null;
    }

    /**
     * 获取所有已安装App信息
     */
    public List<AppInfoBean> getAllAppsInfo() {
        List<AppInfoBean> list = new ArrayList<>();
        PackageManager packageManager = getContext().getPackageManager();
        // 获取系统中安装的所有软件信息
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : installedPackages) {
            list.add(saveAppInfo(packageManager, packageInfo));
        }
        return list;
    }

    /**
     * 根据包名获取意图
     */
    private Intent getIntentByPackageName(String packageName) {
        return getContext().getPackageManager().getLaunchIntentForPackage(packageName);
    }

    /**
     * 根据包名判断App是否安装
     */
    public boolean isInstallApp(String packageName) {
        return getIntentByPackageName(packageName) != null;
    }

    /**
     * 打开指定包名的App
     */
    public boolean openAppByPackageName(String packageName) {
        Intent intent = getIntentByPackageName(packageName);
        if (intent != null) {
            getContext().startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * 打开指定包名的App应用信息界面
     */
    public void openAppInfo(String packageName) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        getContext().startActivity(intent);
    }

    /**
     * 可用来做App信息分享
     */
    public void shareAppInfo(String info) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, info);
        getContext().startActivity(intent);
    }

    /**
     * 判断当前App是否处于后台 需添加权限 android.permission.GET_TASKS 并且必须是系统应用该方法才有效
     */
    public boolean isAppBackground() {
        ActivityManager am = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(getContext().getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 主动回到Home，后台运行
     */
    public void goHome(Context context) {
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(mHomeIntent);
    }

    /**
     * 获取服务是否开启
     */
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

    /**
     * 判断当前手机是否处于锁屏(睡眠)状态
     */
    public boolean isSleeping(Context context) {
        KeyguardManager kgMgr = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return kgMgr.inKeyguardRestrictedInputMode();
    }

    /**
     * 收集设备信息，用于信息统计分析
     */
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

    /**
     * 设备唯一ID  <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     */
    public String getDeviceUniqueId() {
        try {
            TelephonyManager tm = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            LogUtils.e(tm.getDeviceId());
            return tm.getDeviceId();
        } catch (Exception e) {
            return "";
        }
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
}