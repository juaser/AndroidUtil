package com.plugin.weight.calendar;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plugin.weight.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @类说明:
 * @author:zxl on 16-7-31.
 */
public class KCalendarPop extends PopupWindow {
    public static KCalendarPop pop;
    String date = null;// 设置默认选中的日期  格式为 “2014-04-05” 标准DATE格式

    public KCalendarPop(Context mContext, View parent, final onCompleteListener mCompleteListener) {
        View view = View.inflate(mContext, R.layout.popupwindow_calendar, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.calendar_fade_in));
        LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.calendar_push_bottom_in_1));
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        update();

        final TextView popupwindow_calendar_month = (TextView) view.findViewById(R.id.popupwindow_calendar_month);
        final KCalendar calendar = (KCalendar) view.findViewById(R.id.popupwindow_calendar);
        Button popupwindow_calendar_bt_enter = (Button) view.findViewById(R.id.popupwindow_calendar_bt_enter);
        popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年" + calendar.getCalendarMonth() + "月");
        if (date != null) {
            int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
            int month = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
            popupwindow_calendar_month.setText(years + "年" + month + "月");
            calendar.showCalendar(years, month);
            calendar.setCalendarDayBgColor(date, R.drawable.ic_calendar_date_focused);
        }
        List<String> list = new ArrayList<String>(); //设置标记列表
        list.add("2014-04-01");
        list.add("2014-04-02");
        calendar.addMarks(list, 0);
        //监听所选中的日期
        calendar.setOnCalendarClickListener(new KCalendar.OnCalendarClickListener() {

            public void onCalendarClick(int row, int col, String dateFormat) {
                int month = Integer.parseInt(dateFormat.substring(
                        dateFormat.indexOf("-") + 1,
                        dateFormat.lastIndexOf("-")));

                if (calendar.getCalendarMonth() - month == 1//跨年跳转
                        || calendar.getCalendarMonth() - month == -11) {
                    calendar.lastMonth();

                } else if (month - calendar.getCalendarMonth() == 1 //跨年跳转
                        || month - calendar.getCalendarMonth() == -11) {
                    calendar.nextMonth();

                } else {
                    calendar.removeAllBgColor();
                    calendar.setCalendarDayBgColor(dateFormat,
                            R.drawable.ic_calendar_date_focused);
                    date = dateFormat;//最后返回给全局 date
                }
            }
        });

        //监听当前月份
        calendar.setOnCalendarDateChangedListener(new KCalendar.OnCalendarDateChangedListener() {
            public void onCalendarDateChanged(int year, int month) {
                popupwindow_calendar_month
                        .setText(year + "年" + month + "月");
            }
        });

        //上月监听按钮
        RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) view
                .findViewById(R.id.popupwindow_calendar_last_month);
        popupwindow_calendar_last_month
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        calendar.lastMonth();
                    }

                });

        //下月监听按钮
        RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) view
                .findViewById(R.id.popupwindow_calendar_next_month);
        popupwindow_calendar_next_month
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        calendar.nextMonth();
                    }
                });

        //关闭窗口
        popupwindow_calendar_bt_enter
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        Log.e("选中日期", "date==" + date);
                        if (mCompleteListener != null) {
                            mCompleteListener.complete(date);
                        }
                        dismiss();
                    }
                });
    }

    public interface onCompleteListener {
        void complete(String date);
    }
}
