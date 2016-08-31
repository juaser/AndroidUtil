package com.plugin.utils;

import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.LinearLayout;

import com.plugin.utils.log.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * @Description: 通过反射做些操作
 * @Author: zxl
 * @Date: 31/8/16.
 */
public class ReflectUtils {
    /**
     * 获取泛型中的实例
     *
     * @param o
     * @param i
     * @param <T>
     * @return
     */
    public static <T> T getT(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass().getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            LogUtils.e("不能实例化类");
        } catch (IllegalAccessException e) {
            LogUtils.e("安全权限异常");
        } catch (ClassCastException e) {
            LogUtils.e("类强转异常");
        }
        return null;
    }

    /**
     * 通过反射来设置 tanLayout IndicatorWidth的宽度
     * @param width
     * @param tl
     */
    public void setIndicatorWidth(TabLayout tl, int width) {
        Class<?> tablayout = tl.getClass();
        Field tabStrip = null;
        LinearLayout ll_tab = null;
        try {
            tabStrip = tablayout.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
            ll_tab = (LinearLayout) tabStrip.get(tl);
            for (int i = 0; i < ll_tab.getChildCount(); i++) {
                View child = ll_tab.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                params.setMargins(width, 0, width, 0);
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (NoSuchFieldException e) {
            LogUtils.e("NoSuchFieldException异常");
        } catch (IllegalAccessException e) {
            LogUtils.e("IllegalAccessException异常");
        }
    }
}
