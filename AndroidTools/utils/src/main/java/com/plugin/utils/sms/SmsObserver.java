package com.plugin.utils.sms;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

/**
 * @Description: 短信监听   <uses-permission android:name="android.permission.READ_SMS" />
 * @Author: zxl
 * @Date: 2016/12/13 18:10
 * 注册
 * smsObserver = new SmsObserver(this, mHandler);
 * Uri uri = Uri.parse("content://sms");
 * getContentResolver().registerContentObserver(uri, true, smsObserver);
 * 不用的时候注销
 * getContentResolver().unregisterContentObserver(smsObserver);
 */

public class SmsObserver extends ContentObserver {
    private Context mContext;
    private Handler mHandler;
    private String smsAddress;//获取手机号
    private String smsBody;// 获取短信内容
    public static final int FLAG_SMSOBSERVER_BODY = 0x001;

    public SmsObserver(Context mContext, Handler handler) {
        super(handler);
        this.mContext = mContext;
        this.mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        if (uri != null && !TextUtils.equals("content://sms/raw", uri.toString()) && !uri.toString().contains("content://sms/inbox")) {
            return;
        }
        // 按时间顺序排序短信数据库
        Cursor c = mContext.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, "date desc");
        if (c != null) {
            if (c.moveToFirst()) {
                smsAddress = c.getString(c.getColumnIndex("address"));
                smsBody = c.getString(c.getColumnIndex("body"));
                if (mHandler != null) {
                    Message message = mHandler.obtainMessage();
                    message.what = FLAG_SMSOBSERVER_BODY;
                    message.obj = smsBody;
                    mHandler.sendMessage(message);
                }
            }
        }
        if (c != null) {
            c.close();
        }
    }
}
