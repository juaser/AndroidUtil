package com.plugin.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Created by zxl on 10/10/16.
 */

public class PermissionUtils {
    private static volatile PermissionUtils mInstance = null;
    public static final int REQUEST_CODE_PERMISSON = 2020; //权限请求码

    private PermissionUtils() {
    }

    public static PermissionUtils getInstance() {
        PermissionUtils instance = mInstance;
        if (instance == null) {
            synchronized (PermissionUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new PermissionUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    public boolean isRequest() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
    //权限组 只要一个申请成功,权限组内的其他权限也是授权成功的
    /**
     * 日历权限
     */
    public String[] dangerous_permission_calendar = {Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR};
    /**
     * 摄像头权限
     */
    public String[] dangerous_permission_camera = {Manifest.permission.CAMERA};
    /**
     * 联系人权限
     */
    public String[] dangerous_permission_contacts = {Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS};
    /**
     * 定位权限
     */
    public String[] dangerous_permission_locationp = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    /**
     * 录音权限
     */
    public String[] dangerous_ermission_microphone = {Manifest.permission.RECORD_AUDIO};
    /**
     * 电话权限
     */
    public String[] dangerous_permission_phone = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.USE_SIP,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.ADD_VOICEMAIL};
    /**
     * 传感器权限
     */
    public String[] dangerous_permission_sensors = {Manifest.permission.BODY_SENSORS};
    /**
     * 短信权限
     */
    public String[] dangerous_permission_sms = {Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.RECEIVE_WAP_PUSH,
            Manifest.permission.RECEIVE_MMS};
    /**
     * 读写权限
     */
    public String[] dangerous_permission_storage = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 获取需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    public List<String> getDeniedPermissions(Context context, String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                needRequestPermissonList.add(permission);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 所有权限是否都已授权
     *
     * @param context
     * @param permissions
     * @return
     */
    public boolean isGantedAllPermission(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 申请权限
     *
     * @param context
     * @param permissions
     */
    public void applyNeedPermission(Context context, String[] permissions) {
        ActivityCompat.requestPermissions((Activity) context, permissions, REQUEST_CODE_PERMISSON);
    }

    /**
     * 显示提示对话框
     */
    protected void showTipsDialog(final Context context) {
        new AlertDialog.Builder(context).setTitle("提示信息")
                .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings(context);
                    }
                }).show();
    }

    /**
     * 启动当前应用设置页面
     */
    private void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }
}
