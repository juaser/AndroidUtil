package com.plugin.utils;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * @description：
 * @author：zxl
 * @CreateTime 2016/8/22.
 */
public class SystemInfoUtils {
    private static Context mContext;
    private PackageManager pm;
    private static volatile SystemInfoUtils mSystemInfoUtils = null;
    private String packname = "";

    public SystemInfoUtils() {
        pm = mContext.getPackageManager();
        packname = mContext.getPackageName();
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
     * 获取程序 图标
     */
    public Drawable getAppIcon() {
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            e.printStackTrace();

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
            // TODO Auto-generated catch block
            e.printStackTrace();

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
            e.printStackTrace();

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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return null;
    }

    public String getPackname() {
        return packname;
    }
}
