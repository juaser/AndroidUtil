package com.plugin.weight.arc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Description
 * @Created by zxl on 8/10/16.
 */

public class ArcRingView extends View {
    private int view_width, view_height;//View的宽高
    private boolean isMeasured = false;//是否已经测量
    private Shader mShader;//渐变器
    private float view_center_x, view_center_y;//圆的中心
    private float circle_radius;//半径
    private int paint_width = 20;//画笔宽度
    private Paint paint;
    private int[] shape_color = new int[]{0xFF09F68C, 0xFFB0F44B, 0xFFE8DD30, 0xFFF1CA2E, 0xFFFF902F, 0xFFFF6433};

    private Paint paint_triangle;//画个三角形
    private int paint_triangle_color = Color.BLACK;
    private int paint_triangle_width = 1;
    private Path path_triangle;
    private int interval_width = 5;//分割线的宽度
    private RectF rect;//画弧线的范围
    private int start = 135;//起始位置
    private int degree = 270;//滑过多少度数

    private int progress = 0;


    public ArcRingView(Context context) {
        this(context, null, 0);
    }

    public ArcRingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcRingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(paint_width);
        paint.setAntiAlias(true);
        paint.setDither(true);

        paint_triangle = new Paint();
        paint_triangle.setColor(paint_triangle_color);
        paint_triangle.setStyle(Paint.Style.FILL);
        paint_triangle.setStrokeWidth(paint_triangle_width);
        paint_triangle.setAntiAlias(true);
        paint_triangle.setDither(true);

        path_triangle = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        measure();
        canvas.drawArc(rect, start, degree, false, paint);
        canvas.drawBitmap(createTriangleBitmap(), 0, 0, paint);
        if (progress < 270) {
            progress += 10;

        } else {
            progress = 0;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        }, 500);
    }

    public Bitmap createTriangleBitmap() {
        Bitmap trianle = Bitmap.createBitmap(view_width, view_height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(trianle);
        //通过旋转来操作三角形位置
        canvas.save();
        canvas.rotate(start + progress, view_center_x, view_center_y);
        canvas.drawPath(path_triangle, paint_triangle);//三角形
        canvas.restore();
        return trianle;
    }

    /**
     * 测量数据
     */
    public void measure() {
        if (!isMeasured) {
            isMeasured = true;
            view_width = getMeasuredWidth();
            view_height = getMeasuredHeight();
            view_center_x = view_width / 2;
            view_center_y = view_height / 2;
            circle_radius = (view_width - 2 * paint_width) / 2;//上下左右各自保留一个线的宽度
            /**
             * SweepGradent：梯形渲染 第一个参数是说你要 从x轴的什么位置开始渐变， 第二个是y轴 ，第三个是渐变颜数组，第四个是位置，可以指定渐变的绝对位置
             */
            mShader = new SweepGradient(view_width / 2, view_height / 2, shape_color, null);
            paint.setShader(mShader);
            initTrianglePath();
            initArc();
        }
    }

    /**
     * 三角形的路径
     */
    public void initTrianglePath() {
        path_triangle.reset();
        path_triangle.moveTo(view_center_x + circle_radius, view_center_y);//移动到圆的最右边 为三角形的顶点
        path_triangle.lineTo(view_center_x + circle_radius + paint_width, view_center_y + interval_width);
        path_triangle.lineTo(view_center_x + circle_radius + paint_width, view_center_y - interval_width);
        path_triangle.close();
    }

    /**
     * 弧线的路径
     */
    public void initArc() {
        rect = new RectF(view_center_x - circle_radius, view_center_y - circle_radius, view_center_x + circle_radius, view_center_y + circle_radius);
        //弧线的长度
        float path_length = (float) (2 * Math.PI * circle_radius * degree / 360);
        //一共有六个渐变颜色,用白线分割
        float part_length = (path_length - (shape_color.length - 1) * interval_width) / shape_color.length;
        /**
         * DashPathEffect是PathEffect类的一个子类,可以使paint画出类似虚线的样子,并且可以任意指定虚实的排列方式。
         * 代码中的float数组,必须是偶数长度,且>=2,指定了多少长度的实线之后再画多少长度的空白。如(new float[]{2, 5, 2, 10},1),绘制长度2的实线,再绘制长度5的空白,再绘制长度2的实线,再绘制长度10的空白,依次重复。1是起始位置的偏移量。
         */
        PathEffect effect = new DashPathEffect(new float[]{part_length, interval_width}, 1);
        paint.setPathEffect(effect);
    }
}
