package com.plugin.weight.flowchar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Description: 流动的字符
 * @Author: zxl
 * @Date: 2016/12/28 18:06
 */

public class FlowCharView extends View {
    private int mPaddingLeft, mPaddingTop;//View的左，上padding
    private Path mPathDefault;//默认的线条路径
    private Path mPathFill;//填充的线条路径
    private int mColorBg = Color.BLACK;//View 的背景颜色
    private int mColorCharDefault = 0xf0ffffff;//线的默认颜色
    private int mColorCharFill = 0xffffffff;//线的填充颜色
    private int mLineWidth = 4;//线条的宽度
    private Paint mPaintDefault, mPaintFill;//默认，填充画笔

    public FlowCharView(Context context) {
        this(context, null, 0);
    }

    public FlowCharView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowCharView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mPaintDefault = new Paint();
        mPaintDefault.setAntiAlias(true);
        mPaintDefault.setStyle(Paint.Style.STROKE);
        mPaintDefault.setColor(mColorCharDefault);
        mPaintDefault.setStrokeWidth(mLineWidth);
        mPaintDefault.setStrokeCap(Paint.Cap.ROUND);

        mPaintFill = new Paint();
        mPaintFill.setAntiAlias(true);
        mPaintFill.setStyle(Paint.Style.STROKE);
        mPaintFill.setColor(mColorCharFill);
        mPaintFill.setStrokeWidth(mLineWidth);
        mPaintFill.setStrokeCap(Paint.Cap.ROUND);
        mPathDefault = new Path();
        mPathFill = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
    }

    public void setResourseString(String srcString) {
        mPathDefault.reset();
        mPathDefault = FlowCharPathManager.getInstance().getSrcPath(srcString, 1, 30);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
