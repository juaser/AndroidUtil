package com.plugin.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.plugin.utils.log.LogUtils;
import com.plugin.utils.manager.AppManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @description： app的信息
 * @author：zxl
 * @CreateTime 2016/8/22.
 */
public class AppUtils {

    private static volatile AppUtils mInstance = null;

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
     * 封装App信息的Bean类
     */
    public static class AppInfo {

        private String name;
        private Drawable icon;
        private String packageName;
        private String versionName;
        private int versionCode;
        private boolean isSD;
        private boolean isUser;

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public boolean isSD() {
            return isSD;
        }

        public void setSD(boolean SD) {
            isSD = SD;
        }

        public boolean isUser() {
            return isUser;
        }

        public void setUser(boolean user) {
            isUser = user;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packagName) {
            this.packageName = packagName;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        /**
         * @param name        名称
         * @param icon        图标
         * @param packageName 包名
         * @param versionName 版本号
         * @param versionCode 版本Code
         * @param isSD        是否安装在SD卡
         * @param isUser      是否是用户程序
         */
        public AppInfo(String name, Drawable icon, String packageName,
                       String versionName, int versionCode, boolean isSD, boolean isUser) {
            this.setName(name);
            this.setIcon(icon);
            this.setPackageName(packageName);
            this.setVersionName(versionName);
            this.setVersionCode(versionCode);
            this.setSD(isSD);
            this.setUser(isUser);
        }

    /*@Override
    public String toString() {
        return getName() + "\n"
                + getIcon() + "\n"
                + getPackagName() + "\n"
                + getVersionName() + "\n"
                + getVersionCode() + "\n"
                + isSD() + "\n"
                + isUser() + "\n";
    }*/
    }

    /**
     * 获取当前App信息
     * <p>AppInfo（名称，图标，包名，版本号，版本Code，是否安装在SD卡，是否是用户程序）</p>
     *
     * @return 当前应用的AppInfo
     */
    public AppInfo getAppInfo() {
        PackageManager pm = getContext().getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(getContext().getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("PackageManager.NameNotFoundException");
        }
        return pi != null ? getBean(pm, pi) : null;
    }

    /**
     * 得到AppInfo的Bean
     *
     * @param pm 包的管理
     * @param pi 包的信息
     * @return AppInfo类
     */
    private AppInfo getBean(PackageManager pm, PackageInfo pi) {
        ApplicationInfo ai = pi.applicationInfo;
        String name = ai.loadLabel(pm).toString();
        Drawable icon = ai.loadIcon(pm);
        String packageName = pi.packageName;
        String versionName = pi.versionName;
        int versionCode = pi.versionCode;
        boolean isSD = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != ApplicationInfo.FLAG_SYSTEM;
        boolean isUser = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != ApplicationInfo.FLAG_SYSTEM;
        return new AppInfo(name, icon, packageName, versionName, versionCode, isSD, isUser);
    }

    /**
     * 获取所有已安装App信息
     * <p>AppInfo（名称，图标，包名，版本号，版本Code，是否安装在SD卡，是否是用户程序）</p>
     * <p>依赖上面的getBean方法</p>
     *
     * @return 所有已安装的AppInfo列表
     */
    public List<AppInfo> getAllAppsInfo() {
        List<AppInfo> list = new ArrayList<>();
        PackageManager pm = getContext().getPackageManager();
        // 获取系统中安装的所有软件信息
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo pi : installedPackages) {
            if (pi != null) {
                list.add(getBean(pm, pi));
            }
        }
        return list;
    }

    /**
     * 根据包名获取意图
     *
     * @param packageName 包名
     * @return 意图
     */
    private Intent getIntentByPackageName(String packageName) {
        return getContext().getPackageManager().getLaunchIntentForPackage(packageName);
    }

    /**
     * 根据包名判断App是否安装
     *
     * @param packageName 包名
     * @return true: 已安装<br>false: 未安装
     */
    public boolean isInstallApp(String packageName) {
        return getIntentByPackageName(packageName) != null;
    }

    /**
     * 打开指定包名的App
     *
     * @param packageName 包名
     * @return true: 打开成功<br>false: 打开失败
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
     *
     * @param packageName 包名
     */
    public void openAppInfo(String packageName) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        getContext().startActivity(intent);
    }

    /**
     * 可用来做App信息分享
     *
     * @param info 分享信息
     */
    public void shareAppInfo(String info) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, info);
        getContext().startActivity(intent);
    }

    /**
     * 判断当前App处于前台还是后台
     * <p>需添加权限 android.permission.GET_TASKS</p>
     * <p>并且必须是系统应用该方法才有效</p>
     *
     * @return true: 后台<br>false: 前台
     */
    public boolean isAppBackground() {
        ActivityManager am = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(getContext().getPackageName())) {
                return true;
            }
        }
        return false;
    }
}