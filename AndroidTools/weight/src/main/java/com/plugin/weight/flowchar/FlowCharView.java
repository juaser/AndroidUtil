package com.plugin.weight.flowchar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.plugin.utils.log.LogUtils;

import java.util.ArrayList;

/**
 * @Description: 流动的字符
 * @Author: zxl
 * @Date: 2016/12/28 18:06
 */

public class FlowCharView extends View {
    private int mPaddingLeft, mPaddingTop;//View的左，上padding
    private Path mPathDefault;//默认的线条路径
    private Path mPathFill;//填充的线条路径
    private Path mAnmiationPathFill;//动态显示的线条路径
    private int mColorBg = Color.BLACK;//View 的背景颜色
    private int mColorCharDefault = Color.GRAY;//线的默认颜色
    private int mColorCharFill = Color.WHITE;//线的填充颜色
    private int mLineWidth = 4;//线条的宽度
    private Paint mPaintDefault, mPaintFill;//默认，填充画笔
    private ArrayList<float[]> mPathList;//存储线条的集合
    private boolean isLooper = true;//线条的选择是否可以循环
    private int mFillSize = 1;//每次填充的线条数目
    private int mStartIndex = 0;//线条填充时的下标
    private float mScale = 1;//缩放比例
    private float mGapBetweenLetter = 30;//两个字符的间距
    private int mDuaration = 2000;//绘制从头到尾执行的时间
    private float mDefaultPathLength;//默认字符的长度
    private ValueAnimator mAnimator = null;

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
        mAnmiationPathFill = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(mColorBg);//设置背景色
        canvas.translate(mPaddingLeft, mPaddingTop);//平移画布
        canvas.drawPath(mPathDefault, mPaintDefault);//绘制前景
        canvas.drawPath(mAnmiationPathFill, mPaintFill);//绘制要填充的背景
    }

    public void setResourseString(String srcString) {
        mPathDefault.reset();
        mPathFill.reset();
        mStartIndex = 0;
        mPathList = FlowCharPathManager.getInstance().getPathList(srcString, mScale, mGapBetweenLetter);
        if (mPathList.size() == 0) {
            return;
        }
        mPathDefault = FlowCharPathManager.getInstance().getSrcPath(mPathList);
        mPathFill = FlowCharPathManager.getInstance().getFillPath(mPathList, mFillSize, mStartIndex, isLooper);
        mDefaultPathLength = getPathLength(mPathDefault);
        dealPathFill();
    }

    public float getPathLength(Path measurePath) {
        PathMeasure pathMeasure = new PathMeasure(mPathDefault, false);//false是路径不封闭
        float length = pathMeasure.getLength();
        while (pathMeasure.nextContour()) {
            length += pathMeasure.getLength();
        }
        return length;
    }

    public void dealPathFill() {
        if (mPathFill == null || mDefaultPathLength == 0) {
            return;
        }
        PathMeasure pathMeasure = new PathMeasure(mPathFill, false);
        float length = pathMeasure.getLength();//要绘制的路径
        long duarationPart = (long) (mDuaration * length / mDefaultPathLength);
        LogUtils.e("mStartIndex==" + mStartIndex + "duarationPart==" + duarationPart + "    length==" + length + "  mDefaultPathLength==" + mDefaultPathLength);
        loadAnimator(duarationPart, pathMeasure);
    }

    public void changeAnimatorPath() {
        mStartIndex++;
        mStartIndex = mStartIndex % mPathList.size();
        mPathFill = FlowCharPathManager.getInstance().getFillPath(mPathList, mFillSize, mStartIndex, isLooper);
        dealPathFill();
    }

    public void loadAnimator(long duration, final PathMeasure pathMeasure) {
        stopAnimator();
        mAnimator = ValueAnimator.ofFloat(0, 1f);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(duration);//设置需要绘制时间
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                mAnmiationPathFill.reset();
                //取出一部分放到mAnmiationPathFill路径里，true的意思是是否移动到开始的位置
                pathMeasure.getSegment(0, pathMeasure.getLength() * animatedValue, mAnmiationPathFill, true);
                invalidate();
                if (animatedValue == 1) {
                    changeAnimatorPath();
                }
            }
        });
        mAnimator.start();
    }

    public void stopAnimator() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }
}
