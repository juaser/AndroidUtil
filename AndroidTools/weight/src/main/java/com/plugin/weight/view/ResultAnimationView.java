package com.plugin.weight.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/3/4
 */

public class ResultAnimationView extends View {
    private Context mContext;
    private int mLineWidth;//线宽
    public static final int RESULT_RIGHT = 1;//正确动画
    public static final int RESULT_WRONG = 2;//错误动画
    private int mResultType = RESULT_WRONG; //当前结果类型
    private boolean isMeasured = false;
    private Path mPath;
    private Path mPathDst;
    private int mViewWidth;
    private Paint mPaint;
    private ValueAnimator mCircleAnimator;
    private PathMeasure mPathMeasure;
    private int mDuaration = 2000;//绘制从头到尾执行的时间
    private float mDefaultPathLength;

    public ResultAnimationView(Context context) {
        this(context, null, 0);
    }

    public ResultAnimationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ResultAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public void init() {
        mPath = new Path();
        mPathDst = new Path();
        mLineWidth = dp2px(3);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GREEN);
    }


    /**
     * 固定写死了宽高，可重新手动调配
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(dp2px(50), dp2px(50));
    }

    private int dp2px(int dp) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (scale * dp + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        measured();
        canvas.drawPath(mPathDst, mPaint);
    }


    public void measured() {
        if (isMeasured) {
            return;
        }
        isMeasured = true;
        mViewWidth = getWidth();
        mPath.reset();
        mPath.addCircle(mViewWidth / 2, mViewWidth / 2, mViewWidth / 2 - mLineWidth, Path.Direction.CW);
        if (mResultType == RESULT_RIGHT) {
            mPaint.setColor(Color.GREEN);
            Path pathRight = new Path();
            pathRight.moveTo(mViewWidth / 4, getWidth() / 2);
            pathRight.lineTo(mViewWidth / 2, getWidth() / 4 * 3);
            pathRight.lineTo(mViewWidth / 4 * 3, getWidth() / 4);
            mPath.addPath(pathRight);
        } else if (mResultType == RESULT_WRONG) {
            mPaint.setColor(Color.RED);
            Path pathWrong1 = new Path();
            pathWrong1.moveTo(mViewWidth / 4 * 3, mViewWidth / 4);
            pathWrong1.lineTo(mViewWidth / 4, mViewWidth / 4 * 3);
            Path pathWrong2 = new Path();
            pathWrong2.moveTo(mViewWidth / 4, mViewWidth / 4);
            pathWrong2.lineTo(mViewWidth / 4 * 3, mViewWidth / 4 * 3);
            mPath.addPath(pathWrong1);
            mPath.addPath(pathWrong2);
        }
        mPathMeasure = new PathMeasure(mPath, false);
        mDefaultPathLength = getPathLength(mPath);
    }


    public ResultAnimationView setmResultType(int mResultType) {
        if (mCircleAnimator != null && mCircleAnimator.isRunning()) {
            mCircleAnimator.cancel();
        }
        this.mResultType = mResultType;
        isMeasured = false;
        mPath.reset();
        mPathDst.reset();
        return this;
    }

    public void startAnimator() {
        if (mCircleAnimator != null && mCircleAnimator.isRunning()) {
            return;
        }
        mCircleAnimator = ValueAnimator.ofFloat(0, 1);
        mCircleAnimator.setDuration(getPathDuration(mPathMeasure, mDuaration, mDefaultPathLength));
        mCircleAnimator.setRepeatCount(ValueAnimator.INFINITE);//无线循环
        mCircleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (mPathMeasure == null) {
                    valueAnimator.cancel();
                    return;
                }
                float value = (float) valueAnimator.getAnimatedValue();
                Path partPath = new Path();
                mPathMeasure.getSegment(0, mPathMeasure.getLength() * value, partPath, true);
                mPathDst.addPath(partPath);
                invalidate();
            }
        });
        mCircleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                if (mPathMeasure.nextContour()) {
                    long time=getPathDuration(mPathMeasure, mDuaration, mDefaultPathLength);
                    animation.setDuration(time);
                } else {
                    animation.cancel();
                }
                super.onAnimationRepeat(animation);
            }
        });
        mCircleAnimator.start();
        invalidate();
    }

    /**
     * 这个是为了测量其中一个轮廓/总轮廓 所用的时间
     */
    public long getPathDuration(PathMeasure pathMeasure, int totalDuaration, float totalLength) {
        if (totalLength == 0 || totalDuaration == 0 || pathMeasure == null) {
            return 0;
        }
        float length = pathMeasure.getLength();//要绘制的路径
        if (length >= totalLength) {
            return totalDuaration;
        }
        long duarationPart = (long) (totalDuaration * length / totalLength);
        return duarationPart;
    }

    public float getPathLength(Path measurePath) {
        PathMeasure pathMeasure = new PathMeasure(measurePath, false);//false是路径不封闭
        float length = pathMeasure.getLength();
        while (pathMeasure.nextContour()) {
            length += pathMeasure.getLength();
        }
        return length;
    }

    public void switchAnimator(){
        if(mResultType==RESULT_WRONG){
            setmResultType(RESULT_RIGHT);
        }else {
            setmResultType(RESULT_WRONG);
        }
        startAnimator();
    }
}
