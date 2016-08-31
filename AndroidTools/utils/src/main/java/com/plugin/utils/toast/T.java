package com.plugin.utils.toast;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.plugin.utils.manager.AppManager;

/**
 * @description： Toast
 * @author：zxl
 * @CreateTime 2016/8/22.
 */
public class T {
    // Toast
    private static Toast toast;

    /**
     * 短时间显示Toast
     */
    public static void s(Object value) {
        String message = String.valueOf(value);
        if (null == toast) {
            toast = Toast.makeText(AppManager.getInstance().getTop(), message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 短时间显示Toast
     *
     * @param value
     */
    public static void showShort(Object value) {
        String message = String.valueOf(value);
        if (null == toast) {
            toast = Toast.makeText(AppManager.getInstance().getTop(), String.valueOf(message), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 20);
        } else {
            toast.setText(message);
        }
        toast.show();
    }


    /**
     * 自定义显示Toast时间
     *
     * @param value
     * @param duration
     */
    public static void showCustom(Object value, int duration) {
        String message = String.valueOf(value);
        if (null == toast) {
            toast = Toast.makeText(AppManager.getInstance().getTop(), String.valueOf(message), duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * Hide the toast, if any.
     */
    public static void hideToast() {
        if (null != toast) {
            toast.cancel();
        }
    }

    /**
     * 带图片消息提示
     *
     * @param ImageResourceId
     * @param text
     * @param duration
     */
    public static void ImageToast(int ImageResourceId, CharSequence text, int duration) {
        Activity activity = AppManager.getInstance().getTop();
        //创建一个Toast提示消息
        toast = Toast.makeText(activity, text, duration);
        //设置Toast提示消息在屏幕上的位置
        toast.setGravity(Gravity.CENTER, 0, 0);
        //获取Toast提示消息里原有的View
        View toastView = toast.getView();
        //创建一个ImageView
        ImageView img = new ImageView(activity);
        img.setImageResource(ImageResourceId);
        //创建一个LineLayout容器
        LinearLayout ll = new LinearLayout(activity);
        //向LinearLayout中添加ImageView和Toast原有的View
        ll.addView(img);
        ll.addView(toastView);
        //将LineLayout容器设置为toast的View
        toast.setView(ll);
        //显示消息
        toast.show();
    }
}
