package com.plugin.utils.bean;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;

/**
 * @Description: 封装App信息的Bean类
 * @Author: zxl
 * @Date: 2016/12/14 17:11
 */

public class AppInfoBean {

    private String app_name;       //名称
    private Drawable app_icon;     //图标
    private String app_packageName;//包名
    private String app_versionName;//版本名
    private int app_versionCode;   //版本号
    private String[] app_premissions;//权限
    private ActivityInfo[] app_activityInfos;//activity集合
    private String app_signature;    //签名
    private String device_manufacture;//设备厂商
    private String device_type;       //设备型号
    private String device_unique_id;  //设备唯一ID

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public Drawable getApp_icon() {
        return app_icon;
    }

    public void setApp_icon(Drawable app_icon) {
        this.app_icon = app_icon;
    }

    public String getApp_packageName() {
        return app_packageName;
    }

    public void setApp_packageName(String app_packageName) {
        this.app_packageName = app_packageName;
    }

    public String getApp_versionName() {
        return app_versionName;
    }

    public void setApp_versionName(String app_versionName) {
        this.app_versionName = app_versionName;
    }

    public int getApp_versionCode() {
        return app_versionCode;
    }

    public void setApp_versionCode(int app_versionCode) {
        this.app_versionCode = app_versionCode;
    }

    public String[] getApp_premissions() {
        return app_premissions;
    }

    public void setApp_premissions(String[] app_premissions) {
        this.app_premissions = app_premissions;
    }

    public ActivityInfo[] getApp_activityInfos() {
        return app_activityInfos;
    }

    public void setApp_activityInfos(ActivityInfo[] app_activityInfos) {
        this.app_activityInfos = app_activityInfos;
    }

    public String getApp_signature() {
        return app_signature;
    }

    public void setApp_signature(String app_signature) {
        this.app_signature = app_signature;
    }

    public String getDevice_manufacture() {
        return device_manufacture;
    }

    public void setDevice_manufacture(String device_manufacture) {
        this.device_manufacture = device_manufacture;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDevice_unique_id() {
        return device_unique_id;
    }

    public void setDevice_unique_id(String device_unique_id) {
        this.device_unique_id = device_unique_id;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[" + "device_manufacture==" + device_manufacture + "      ");
        stringBuffer.append("device_type==" + device_type + "       ");
        stringBuffer.append("app_name==" + app_name + "     ");
        stringBuffer.append("app_packageName==" + app_packageName + "    ");
        stringBuffer.append("app_versionName==" + app_versionName + "    ");
        stringBuffer.append("app_versionCode==" + app_versionCode + "     ");
        stringBuffer.append("device_unique_id==" + device_unique_id + "    ");
//        stringBuffer.append("app_signature==" + app_signature + "    ");
        if (app_premissions == null) {
            stringBuffer.append("app_premissions==null" + "    ");
        } else {
            stringBuffer.append("app_premissions==" + app_premissions.length + "   ");
        }
        if (app_activityInfos == null) {
            stringBuffer.append("app_activityInfos==null" + "]");
        } else {
            stringBuffer.append("app_activityInfos==" + app_activityInfos.length + "]");
        }
        return stringBuffer.toString();
    }
}
