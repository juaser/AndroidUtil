package com.plugin.weight.point;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @description： 横向的绘制多个小圆点
 * @author：zxl
 * @CreateTime 2016/4/15.
 */
public class HorizontalPointView extends View {
    /**
     * 小圆点被选中的位置
     */
    public int point_setlet = 0;
    /**
     * 存储小圆点的集合
     */
    public List<PointMeasure.DrawCirclePoint> points;
    /**
     * 控件的宽高
     */
    private int width, height;
    /**
     * 画选中小圆点和默认小圆点的画笔
     */
    private Paint paint_normal, paint_select;
    /**
     * 画选中小圆点和默认小圆点的颜色
     */
    private int color_normal, color_select;
    /**
     * 小圆点半径
     */
    private int circle_radius = 10;
    /**
     * 小圆点间隔距离
     */
    private int circle_steps = 15;
    /**
     * 是否测量小圆点轨迹
     */
    private boolean isMeasured = true;
    /**
     * 小圆点个数
     */
    private int count = 0;

    public HorizontalPointView(Context context) {
        this(context, null, 0);
    }

    public HorizontalPointView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化画笔配置
     */
    public void init() {
        points = new ArrayList<>();
        paint_normal = new Paint();
        paint_select = new Paint();
        color_normal = Color.parseColor("#d8d8d8");
        color_select = Color.parseColor("#29c475");
        initPaint(paint_normal, color_normal, Paint.Style.FILL);
        initPaint(paint_select, color_select, Paint.Style.FILL);
    }

    /**
     * 初始化画笔
     */
    public void initPaint(Paint paint, int color, Paint.Style style) {
        paint.setColor(color);
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(style);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isMeasured) {
            width = getMeasuredWidth();
            height = getMeasuredHeight();
            Log.e(getClass().getName(), "count--" + count + "----width---" + width + "----height---" + height);
            isMeasured = false;
            points.clear();
            points.addAll(new PointMeasure().getPoints(width, height, count, circle_radius, circle_steps));
        }
        drawCircle(canvas);
    }

    /**
     * 根据路径绘制小圆点
     *
     * @param canvas
     */
    public void drawCircle(Canvas canvas) {
        for (int i = 0, size = points.size(); i < size; i++) {
            if (point_setlet == i) {
                canvas.drawCircle(points.get(i).x, points.get(i).y, circle_radius, paint_select);
            } else {
                canvas.drawCircle(points.get(i).x, points.get(i).y, circle_radius, paint_normal);
            }
        }
    }

    /**
     * 刷新
     */
    public void refresh(int sum) {
        this.count = sum;
        isMeasured = true;
        this.point_setlet = 0;
        invalidate();
    }

    public void setSelect(int select) {
        this.point_setlet = select;
        invalidate();
    }

    /**
     * 测量Point的圆心坐标
     * Created by Administrator on 2016/4/15.
     */
    public class PointMeasure {
        public PointMeasure() {
        }

        public List<DrawCirclePoint> getPoints(int width, int height, int count, int circle_radius, int circle_steps) {
            List<DrawCirclePoint> points = new ArrayList<>();
            if (count > 0) {
                if (count % 2 == 0) {
                    /**
                     * 剩下一半的个数
                     */
                    int part_count = count / 2;
                    for (int i = 0; i < count / 2; i++) {
                        DrawCirclePoint point_left = new DrawCirclePoint();
                        point_left.x = width / 2 - circle_radius / 2 - circle_steps / 2 - (part_count - i - 1) * (circle_steps + circle_radius);
                        point_left.y = height - 2 * circle_radius;
                        points.add(point_left);
                    }
                    for (int i = count / 2; i < count; i++) {
                        DrawCirclePoint point_right = new DrawCirclePoint();
                        point_right.x = width / 2 + circle_radius / 2 + circle_steps / 2 + (i - part_count) * (circle_steps + circle_radius);
                        point_right.y = height - 2 * circle_radius;
                        points.add(point_right);
                    }
                } else {
                    /**
                     * 剩下一半的个数
                     */
                    int part_count = (count - 1) / 2;
                    if (part_count != 0) {
                        for (int i = 0; i < count; i++) {
                            if (i < part_count) {
                                DrawCirclePoint point_left = new DrawCirclePoint();
                                point_left.x = width / 2 - (part_count - i) * (circle_radius + circle_steps);
                                point_left.y = height - 2 * circle_radius;
                                points.add(point_left);
                            } else if (i == part_count) {
                                DrawCirclePoint point_center = new DrawCirclePoint();
                                point_center.x = width / 2;
                                point_center.y = height - 2 * circle_radius;
                                points.add(point_center);
                            } else {
                                DrawCirclePoint point_right = new DrawCirclePoint();
                                point_right.x = width / 2 + (i - part_count) * (circle_radius + circle_steps);
                                point_right.y = height - 2 * circle_radius;
                                points.add(point_right);
                            }
                        }
                    } else {
                        DrawCirclePoint point_center = new DrawCirclePoint();
                        point_center.x = width / 2;
                        point_center.y = height - 10;
                        points.add(point_center);
                    }
                }
            }
            return points;
        }

        private class DrawCirclePoint {
            public float x;
            public float y;

            @Override
            public String toString() {
                return "DrawCirclePoint{" +
                        "x=" + x +
                        ", y=" + y +
                        '}';
            }
        }
    }

}
