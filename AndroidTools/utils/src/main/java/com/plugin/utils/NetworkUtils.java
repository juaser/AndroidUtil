package com.plugin.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.plugin.utils.log.LogUtils;
import com.plugin.utils.manager.AppManager;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * @Description: 网络相关的工具类
 * @Author: zxl
 * @Date: 1/9/16 上午10:42.
 */
public class NetworkUtils {

    private static volatile NetworkUtils mInstance = null;

    private NetworkUtils() {
    }

    public static NetworkUtils getInstance() {
        NetworkUtils instance = mInstance;
        if (instance == null) {
            synchronized (NetworkUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new NetworkUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    public static final int NETWORK_WIFI = 0x001;    // wifi network
    public static final int NETWORK_2G = 0x002;    // "2G" networks
    public static final int NETWORK_3G = 0x003;    // "3G" networks
    public static final int NETWORK_4G = 0x004;    // "4G" networks
    public static final int NETWORK_UNKNOWN = 0x005;   // unknown network
    public static final int NETWORK_NO = 0x006;   // no network

    private static final int NETWORK_TYPE_GSM = 0x010;//16
    private static final int NETWORK_TYPE_TD_SCDMA = 0x011;//17
    private static final int NETWORK_TYPE_IWLAN = 0x012;//18

    /**
     * @description: 获取上下文
     */
    public Context getContext() {
        return AppManager.getInstance().getTop();
    }

    /**
     * 获取活动网路信息
     */
    private NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            return cm.getActiveNetworkInfo();
        }
        return null;
    }

    /**
     * 判断网络是否可用（必须可用，且已连接才是true）
     */
    public boolean isNetworkConnected() {
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected();
    }

    /**
     * 是否wifi连接
     */
    public boolean isWifiConnected(int network_type) {
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        }
        return false;
    }

    /**
     * 打开网络设置界面
     * <p>3.0以下打开设置界面</p>
     */

    public void openWirelessSettings() {
        if (android.os.Build.VERSION.SDK_INT > 10) {
            getContext().startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
        } else {
            getContext().startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    /**
     * 获取当前的网络类型(WIFI,2G,3G,4G) <p>需添加权限 android.permission.ACCESS_NETWORK_STATE</p>
     */
    public int getNetWorkType() {
        int currentNetWorkType = NETWORK_UNKNOWN;
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                currentNetWorkType = NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                int subtype = activeNetworkInfo.getSubtype();
                switch (subtype) {
                    case NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        currentNetWorkType = NETWORK_2G;
                        break;
                    case NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        currentNetWorkType = NETWORK_3G;
                        break;
                    case NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        currentNetWorkType = NETWORK_4G;
                        break;
                    default:
                        String subtypeName = activeNetworkInfo.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA") || subtypeName.equalsIgnoreCase("WCDMA") || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            currentNetWorkType = NETWORK_3G;
                        }
                }
            }
        } else {
            currentNetWorkType = NETWORK_NO;
        }
        return currentNetWorkType;
    }

    /**
     * 获取当前的网络类型(WIFI,2G,3G,4G)
     */
    public String getNetWorkTypeName() {
        int type = getNetWorkType();
        switch (type) {
            case NETWORK_WIFI:
                return "WIFI";
            case NETWORK_4G:
                return "4G";
            case NETWORK_3G:
                return "3G";
            case NETWORK_2G:
                return "2G";
            case NETWORK_NO:
                return "无网络";
            default:
                return "未知";
        }
    }

    /**
     * 获取移动网络运营商名称 如中国联通、中国移动、中国电信
     */
    public String getPhoneProvider() {
        String provider = "未知";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            String IMSI = telephonyManager.getSubscriberId();
            if (IMSI == null) {
                if (TelephonyManager.SIM_STATE_READY == telephonyManager.getSimState()) {
                    String operator = telephonyManager.getSimOperator();
                    if (operator != null) {
                        if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                            provider = "中国移动";
                        } else if (operator.equals("46001")) {
                            provider = "中国联通";
                        } else if (operator.equals("46003")) {
                            provider = "中国电信";
                        }
                    }
                }
            } else {
                if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                    provider = "中国移动";
                } else if (IMSI.startsWith("46001")) {
                    provider = "中国联通";
                } else if (IMSI.startsWith("46003")) {
                    provider = "中国电信";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provider;
    }

    /**
     * 获取wifi MAC地址
     */
    public String getMacWifiAddress() {
        String localMac = null;
        try {
            WifiManager wifi = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            if (wifi.isWifiEnabled()) {
                localMac = info.getMacAddress();
                if (localMac != null) {
                    localMac = localMac.replace(":", "-").toLowerCase();
                    return localMac;
                }
            }
        } catch (Exception e) {
            LogUtils.e("Exception" + e.getMessage());
        }
        return localMac == null ? "" : localMac;
    }

    /**
     * 获取设备MAC地址
     */
    public String getMacAddressProcess() {
        String macAddress = null;
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader reader = new LineNumberReader(ir);
            macAddress = reader.readLine().replace(":", "-").toLowerCase();
        } catch (Exception e) {
            LogUtils.e("Exception" + e.getMessage());
        }
        return macAddress == null ? "" : macAddress;
    }
}