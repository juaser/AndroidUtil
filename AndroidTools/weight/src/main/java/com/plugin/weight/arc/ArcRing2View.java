package com.plugin.weight.arc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Description
 * @Created by zxl on 8/10/16.
 */

public class ArcRing2View extends View {
    private int view_width, view_height;//View的宽高
    private boolean isMeasured = false;//是否已经测量
    private float view_center_x, view_center_y;//圆的中心
    private float circle_radius;//半径
    private int padding = 10;//间距
    private Shader mShader;
    private int[] shape_color = new int[]{0xFF09F68C, 0xFFB0F44B, 0xFFE8DD30, 0xFFF1CA2E, 0xFFFF902F, 0xFFFF6433};
    private Paint paint;
    private Matrix mMatrix = new Matrix();
    private float mRotate;
    private int start = 135;//起始位置
    private int end = 405;//结束

    public ArcRing2View(Context context) {
        this(context, null, 0);
    }

    public ArcRing2View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcRing2View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        measure();
        mMatrix.setRotate(mRotate, view_center_x, view_center_y);
        mShader.setLocalMatrix(mMatrix);
        mRotate += 3;
        if (mRotate >= 360) {
            mRotate = 0;
        }
        invalidate();
        getArc(canvas);
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
            circle_radius = (view_width - 2 * padding) / 2;
            /**
             * SweepGradent：梯形渲染 第一个参数是说你要 从x轴的什么位置开始渐变， 第二个是y轴 ，第三个是渐变颜数组，第四个是位置，可以指定渐变的绝对位置
             */
            mShader = new SweepGradient(view_width / 2, view_height / 2, shape_color, null);
            paint.setShader(mShader);
            PathEffect effect = new DashPathEffect(new float[]{2, 3, 2, 3}, 1);
            paint.setPathEffect(effect);
        }
    }

    public void getArc(Canvas canvas) {
        RectF rect = new RectF(view_center_x - circle_radius, view_center_y - circle_radius, view_center_x + circle_radius, view_center_y + circle_radius);
        Path path = new Path();
        path.moveTo(view_center_x, view_center_y);
        path.lineTo((float) (view_center_x + circle_radius * Math.cos(start * Math.PI / 180))
                , (float) (view_center_y + circle_radius * Math.sin(start * Math.PI / 180)));
        path.lineTo((float) (view_center_x + circle_radius * Math.cos(end * Math.PI / 180))
                , (float) (view_center_y + circle_radius * Math.sin(end * Math.PI / 180)));
        path.addArc(rect, start, end - start);
        canvas.clipPath(path);
        canvas.drawCircle(view_center_x, view_center_y, 80, paint);
    }
}
