package com.plugin.weight.arc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Description
 * @Created by zxl on 8/10/16.
 */

public class ArcRing3View extends View {
    private Paint paint;
    private int view_width, view_height;//View的宽高
    private boolean isMeasured = false;//是否已经测量
    private float view_center_x, view_center_y;//圆的中心
    private float circle_radius;//半径
    private int padding = 10;//间距
    private int progress = 0;
    private float path_length;

    public ArcRing3View(Context context) {
        this(context, null, 0);
    }

    public ArcRing3View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcRing3View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        measure();
        getCircle(canvas);
        if (progress < path_length) {
            progress += 10;
        } else {
            progress = 0;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        }, 100);

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
        }
    }

    public void getCircle(Canvas canvas) {
        Path path = new Path();
        path.addCircle(view_center_x, view_center_y, circle_radius, Path.Direction.CW);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
        PathMeasure pathMeasure = new PathMeasure(path, false);
        path_length = pathMeasure.getLength();
        float[] pos = new float[2];
        pathMeasure.getPosTan(progress, pos, null);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(pos[0], pos[1], 5, paint);
    }
}
