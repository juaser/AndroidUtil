package com.plugin.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.plugin.utils.manager.AppManager;

/**
 * @Description:
 * @Author: zxl
 * @Date: 1/9/16 下午12:15.
 */
public class DialogUtils {
    private static volatile DialogUtils mInstance = null;

    private DialogUtils() {
    }

    public static DialogUtils getInstance() {
        DialogUtils instance = mInstance;
        if (instance == null) {
            synchronized (DialogUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new DialogUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    /**
     * @description: 获取上下文
     */
    public Context getContext() {
        return AppManager.getInstance().getTop();
    }

    /**
     * 展示dialog
     *
     * @param layoutId
     */
    public Dialog showDialog(int layoutId) {
        View view = LayoutInflater.from(getContext()).inflate(layoutId, null);
        final Dialog mDialog = new Dialog(getContext(), R.style.dialog_show_style);
        mDialog.setContentView(view);
        mDialog.setCancelable(true);//点击是否消失
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        mDialog.getWindow().setWindowAnimations(R.style.dialog_show_anim);// 效果
        mDialog.show();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        return mDialog;
    }
}
