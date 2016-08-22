package com.plugin.weight.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @description： 右下角带对号的TextView
 * @author：zxl
 * @CreateTime 2016/8/22.
 */
public class PigeonTextView extends TextView {
    private Paint paint_normal, paint_select, paint_pigeon;
    private boolean isSelect = false;
    private int width, height;
    private int stroke_width = 2;
    private Path path_triangle;
    private int color_normal = 0xffd8d8d8;
    private int color_select= 0xffff4576;
    private int color_pigeon=0xffffffff;
    private int color_backgroup = 0xffffffff;

    public PigeonTextView(Context context) {
        this(context, null, 0);
    }

    public PigeonTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PigeonTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        this.setBackgroundColor(color_backgroup);
        this.setPadding(20, 10, 20, 10);
        this.setSingleLine();
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        path_triangle = new Path();
        paint_normal = new Paint();
        paint_select = new Paint();
        paint_pigeon = new Paint();
        initPaint(paint_normal, color_normal, stroke_width);
        initPaint(paint_select, color_select, stroke_width);
        initPaint(paint_pigeon, color_pigeon, stroke_width);
    }

    public void initPaint(Paint paint, int color, int w) {
        paint.setColor(color);
        paint.setStrokeWidth(w);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getViewWH();
        if (!isSelect) {
            drawBorder(canvas, paint_normal);
        } else {
            drawBorder(canvas, paint_select);
            drawTriangle(canvas, paint_select);
            drawPigeon(canvas, paint_pigeon);
        }

    }

    /**
     * 绘制外边框
     *
     * @param paint
     */
    public void drawBorder(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(stroke_width, stroke_width, width - stroke_width, stroke_width, paint);
        canvas.drawLine(stroke_width, stroke_width, stroke_width, height - stroke_width, paint);
        canvas.drawLine(width - stroke_width, stroke_width, width - stroke_width, height - stroke_width, paint);
        canvas.drawLine(stroke_width, height - stroke_width, width - stroke_width, height - stroke_width, paint);
    }

    /**
     * 绘制一个对号
     */
    public void drawPigeon(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        DrawPoint point1 = new DrawPoint();
        DrawPoint point2 = new DrawPoint();
        DrawPoint point3 = new DrawPoint();
        point1.x = (float) (width - Math.sqrt(stroke_width));
        point1.y = (float) (3 * height / 4 + Math.sqrt(stroke_width));
        point2.x = (float) (width - (height / 4 - Math.sqrt(stroke_width)));
        point2.y = (float) (height - Math.sqrt(stroke_width));
        point3.x = (float) (width - (height / 2 - (height / 4 + stroke_width * stroke_width) / 2));
        point3.y = (float) (height - (height / 4 + stroke_width * stroke_width) / 2);
        canvas.drawLine(point1.x, point1.y, point2.x, point2.y, paint);
        canvas.drawLine(point2.x, point2.y, point3.x, point3.y, paint);
    }

    /**
     * 绘制三角块
     */
    public void drawTriangle(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        path_triangle.reset();
        path_triangle.moveTo(width - height / 2 - stroke_width, height - stroke_width);// 此点为多边形的起点
        path_triangle.lineTo(width - stroke_width, height / 2 - stroke_width);
        path_triangle.lineTo(width - stroke_width, height - stroke_width);
        path_triangle.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path_triangle, paint);
    }

    private void getViewWH() {
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    public void setIsSelect(boolean isSelects) {
        if (isSelect != isSelects) {
            this.isSelect = isSelects;
            invalidate();
        }
    }

    public boolean isSelect() {
        return isSelect;
    }

    public class DrawPoint {
        public float x;
        public float y;

        @Override
        public String toString() {
            return "DrawPoint{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
