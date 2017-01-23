package com.plugin.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.plugin.utils.manager.AppManager;

import java.util.List;

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

    public Dialog createNewDialog(Context mContext, View contentView) {
        Dialog dialog = new Dialog(mContext, R.style.dialog_show_style);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        int width = DisplayUtils.getInstance().getScreenWidth();
        int height = DisplayUtils.getInstance().getScreenHeight();
        dialog.setContentView(contentView, new LinearLayout.LayoutParams(width, height));
        return dialog;
    }

    /**
     * @param context                上下文对象
     * @param msg                    展示的内容
     * @param textLeft               左边按钮的内容
     * @param textRight              右边按钮的内容
     * @param cancelable             点击返回是否可以取消
     * @param canceledOntouchOUtside 点击边界之外的是否可以取消
     * @param onLeftClickListener    左边按钮点击回调
     * @param onRightClickListener   右边按钮点击回调
     */
    public void showOKCancleDialog(Context context, String msg, String textLeft, String textRight, boolean cancelable, boolean canceledOntouchOUtside,
                                   final View.OnClickListener onLeftClickListener, final View.OnClickListener onRightClickListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.layout_dialog_okcancel_btn, null);
        final Dialog dialog = new Dialog(context, R.style.dialog_show_style);
        dialog.setContentView(contentView);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(canceledOntouchOUtside);
        dialog.getWindow().setWindowAnimations(R.style.dialog_show_anim);// 效果
        dialog.show();
        TextView textView = (TextView) contentView.findViewById(R.id.tv_dialog_message);
        final TextView tvLeft = (TextView) contentView.findViewById(R.id.tv_dialog_left_btn);
        TextView tvRight = (TextView) contentView.findViewById(R.id.tv_dialog_right_btn);
        textView.setText(msg);
        tvLeft.setText(textLeft);
        tvRight.setText(textRight);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onLeftClickListener != null) {
                    onLeftClickListener.onClick(tvLeft);
                }
                dialog.dismiss();
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRightClickListener != null) {
                    onRightClickListener.onClick(tvLeft);
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * @param context                上下文对象
     * @param msg                    展示的内容
     * @param textOk                 按钮的内容
     * @param cancelable             点击返回是否可以取消
     * @param canceledOntouchOUtside 点击边界之外的是否可以取消
     * @param onOkListener           按钮点击回调
     */
    public void showOKDialog(Context context, String msg, String textOk, boolean cancelable, boolean canceledOntouchOUtside,
                             final View.OnClickListener onOkListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.layout_dialog_okcancel_btn, null);
        final Dialog dialog = new Dialog(context, R.style.dialog_show_style);
        dialog.setContentView(contentView);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(canceledOntouchOUtside);
        dialog.getWindow().setWindowAnimations(R.style.dialog_show_anim);// 效果
        dialog.show();
        TextView textView = (TextView) contentView.findViewById(R.id.tv_dialog_message);
        final TextView tvLeft = (TextView) contentView.findViewById(R.id.tv_dialog_left_btn);
        TextView tvRight = (TextView) contentView.findViewById(R.id.tv_dialog_right_btn);
        View line2 = contentView.findViewById(R.id.line2);
        tvRight.setVisibility(View.GONE);
        line2.setVisibility(View.GONE);
        tvLeft.setBackgroundDrawable(null);
        textView.setText(msg);
        tvLeft.setText(textOk);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onOkListener != null) {
                    onOkListener.onClick(tvLeft);
                }
                dialog.dismiss();
            }
        });
    }

    public void showBottomUnitSelectDialog(final Context context, final List<String> data, boolean cancelable, boolean canceledOntouchOUtside,
                                           final AdapterView.OnItemClickListener onItemClickListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.layout_dialog_bottom_select, null);
        final Dialog dialog = new Dialog(context, R.style.dialog_show_style);
        dialog.setContentView(contentView);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(canceledOntouchOUtside);
        dialog.getWindow().setWindowAnimations(R.style.dialog_show_anim);// 效果
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();

        TextView tvCancel = (TextView) contentView.findViewById(R.id.tv_cancle);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ListView lv = (ListView) contentView.findViewById(R.id.lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.dismiss();
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(adapterView, view, i, l);
                }
            }
        });
        lv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return data == null ? 0 : data.size();
            }

            @Override
            public Object getItem(int i) {
                return data.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                TextView textView = new TextView(context);
                textView.setText(data.get(i));
                textView.setPadding(DisplayUtils.getInstance().dp2px(10), DisplayUtils.getInstance().dp2px(15), DisplayUtils.getInstance().dp2px(15), DisplayUtils.getInstance().dp2px(10));
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(0xff333333);
                textView.setTextSize(16);
                return textView;
            }
        });

    }
}
