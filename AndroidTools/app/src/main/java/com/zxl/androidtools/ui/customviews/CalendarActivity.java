package com.zxl.androidtools.ui.customviews;

import android.widget.TextView;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.weight.calendar.KCalendarPop;
import com.zxl.androidtools.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description:
 * @Author: zxl
 * @Date: 6/9/16 上午10:40.
 */
public class CalendarActivity extends BaseAppCompatActivity {
    @Bind(R.id.tv_calendar)
    TextView tvCalendar;
    @Bind(R.id.tv_calendar_date)
    TextView tvCalendarDate;

    @Override
    public int getLayoutId() {
        return R.layout.activity_calendar;
    }

    @Override
    public void initView() {

    }

    @OnClick(R.id.tv_calendar)
    void show() {
        new KCalendarPop(this, tvCalendar, new KCalendarPop.onCompleteListener() {
            @Override
            public void complete(String date) {
                tvCalendarDate.setText(date);
            }
        });
    }

}
