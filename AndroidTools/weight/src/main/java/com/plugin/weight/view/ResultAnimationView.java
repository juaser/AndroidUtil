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
import android.view.animation.LinearInterpolator;

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
    private Path mPath, mPathDst, mPathFilled;
    private int mViewWidth;
    private Paint mPaint;
    private ValueAnimator mCircleAnimator;
    private PathMeasure mPathMeasure;
    private int mDuaration = 1000;//绘制从头到尾执行的时间
    private int mIndex = 0;

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
        mPathFilled = new Path();
        mLineWidth = dp2px(3);
        mViewWidth = dp2px(50);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GREEN);
        measured();
        startAnimator();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if(!hasWindowFocus){
            stopAnimation();
        }else {
            mPathDst.reset();
            mPathDst.addPath(mPath);
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mViewWidth, mViewWidth);
    }

    private int dp2px(int dp) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (scale * dp + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPathDst, mPaint);
    }


    public void measured() {
        mPath.reset();
        mPathDst.reset();
        mPath.addCircle(mViewWidth / 2, mViewWidth / 2, mViewWidth / 2 - mLineWidth, Path.Direction.CW);
        if (mResultType == RESULT_RIGHT) {
            mPaint.setColor(Color.GREEN);
            Path path = new Path();
            path.moveTo(mViewWidth / 4, mViewWidth / 2);
            path.lineTo(mViewWidth / 2, mViewWidth / 4 * 3);
            path.lineTo(mViewWidth / 4 * 3, mViewWidth / 4);
            mPath.addPath(path);
        } else if (mResultType == RESULT_WRONG) {
            mPaint.setColor(Color.RED);
            Path path = new Path();
            path.moveTo(mViewWidth / 4 * 3, mViewWidth / 4);
            path.lineTo(mViewWidth / 2, mViewWidth / 2);
            path.lineTo(mViewWidth / 4, mViewWidth / 4 * 3);
            mPath.addPath(path);

            Path path2 = new Path();
            path2.moveTo(mViewWidth / 4, mViewWidth / 4);
            path2.lineTo(mViewWidth / 2, mViewWidth / 2);
            path2.lineTo(mViewWidth / 4 * 3, mViewWidth / 4 * 3);
            mPath.addPath(path2);
        }
        mPathMeasure = new PathMeasure(mPath, false);
    }


    public ResultAnimationView setmResultType(int mResultType) {
        this.mResultType = mResultType;
        measured();
        return this;
    }

    public void switchAnimator() {
        if (mResultType == RESULT_WRONG) {
            setmResultType(RESULT_RIGHT);
        } else {
            setmResultType(RESULT_WRONG);
        }
        startAnimator();
    }

    public void startAnimator() {
        if (mCircleAnimator != null && mCircleAnimator.isRunning()) {
            mCircleAnimator.cancel();
        }
        mPathFilled.reset();
        mIndex = 0;
        mCircleAnimator = ValueAnimator.ofFloat(0, 1);
        mCircleAnimator.setInterpolator(new LinearInterpolator());//线性变化
        mCircleAnimator.setDuration(mDuaration);
        mCircleAnimator.setRepeatCount(ValueAnimator.INFINITE);//无线循环
        mCircleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                Path partPath = new Path();
                mPathMeasure.getSegment(0, mPathMeasure.getLength() * value, partPath, true);
                mPathDst.reset();
                mPathDst.addPath(mPathFilled);
                mPathDst.addPath(partPath);
                invalidate();
            }
        });
        mCircleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                mIndex++;
                getFillPath(mPath, mIndex);
                boolean isNext = mPathMeasure.nextContour();
                if (isNext) {
                    animation.setDuration(mDuaration);
                } else {
                    animation.cancel();
                }
                super.onAnimationRepeat(animation);

            }
        });
        mCircleAnimator.start();
    }


    public void getFillPath(Path pathSrc, int index) {
        mPathFilled.reset();
        Path path = new Path();
        path.addPath(pathSrc);
        PathMeasure pathMeasure = new PathMeasure(path, false);
        int i = 0;
        Path partPath = new Path();
        pathMeasure.getSegment(0, pathMeasure.getLength(), partPath, true);
        mPathFilled.addPath(partPath);
        while (pathMeasure.nextContour()) {
            i++;
            if (i < index) {
                pathMeasure.getSegment(0, pathMeasure.getLength(), partPath, true);
                mPathFilled.addPath(partPath);
            } else {
                break;
            }
        }
    }

    public void stopAnimation() {
        if (mCircleAnimator != null && mCircleAnimator.isRunning()) {
            mCircleAnimator.cancel();
        }
    }
}
