package com.plugin.weight.flowchar;

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
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/1/19 17:12
 */

public class NumberClockView extends View {

    public static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat("HH:mm:ss");
    private int mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;//View的左，上,右,下padding
    private Path mPathDefault;//默认的线条路径
    private Path mAnmiationPathFill;//动态显示的线条路径
    private String currentTimeStr = "";
    private Paint mPaintDefault, mPaintFill;//默认，填充画笔
    private int mColorCharDefault = Color.GRAY;//线的默认颜色
    private int mColorCharFill = Color.WHITE;//线的填充颜色
    private int mLineWidth = 4;//线条的宽度
    private int mColorBg = Color.BLACK;//View 的背景颜色
    private ArrayList<float[]> mPathList;//存储线条的集合
    private float mScale = 1.5f;//缩放比例
    private float mGapBetweenLetter = 30;//两个字符的间距
    private int mDuaration = 1000;//绘制从头到尾执行的时间
    private ValueAnimator mAnimator = null;
    private int flowModel = 0;
    public static final int MODEL_GLITTER = 0;//闪烁效果，每次界面显示一个线条
    public static final int MODEL_STEPBYSTEP = 1;//逐步效果,显示填充效果，之前的也保留下来
    private int mStartIndex = 0;
    private boolean isLooper = true;
    private OnFinishedListener onFinishedListener;
    private int mViewWidth, mViewHeight;//测量的View的宽高
    private float mPathMaxWidth, mPathMaxHeight;
    private boolean isMeasured = false;

    public NumberClockView(Context context) {
        this(context, null);
    }

    public NumberClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberClockView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mAnmiationPathFill = new Path();
        initResource();
    }

    public void initResource() {
        currentTimeStr = convertTimeStamp2String();
        setResourseString(currentTimeStr);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            stopAnimator();
        } else {
            initResource();
            startAnimator();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        measured();
        canvas.drawColor(mColorBg);//设置背景色
        canvas.translate(mPaddingLeft, mPaddingTop);//平移画布
        canvas.drawPath(mPathDefault, mPaintDefault);//绘制前景
        canvas.drawPath(mAnmiationPathFill, mPaintFill);//绘制要填充的背景
    }

    public void measured() {
        if (isMeasured) {
            return;
        }
        isMeasured = true;
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        if (mViewWidth < mPathMaxWidth) {
            mPaddingLeft = 0;
        } else {
            mPaddingLeft = (int) (mViewWidth - mPathMaxWidth) / 2;
        }

        if (mViewHeight < mPathMaxHeight) {
            mPaddingTop = 0;
        } else {
            mPaddingTop = (int) (mViewHeight - mPathMaxHeight) / 2;
        }
        Log.e("TAG" + currentTimeStr, "(" + mViewWidth + "," + mViewHeight + ") (" + mPathMaxWidth + "," + mPathMaxHeight + " ) (" + mPaddingLeft + "," + mPaddingTop + ")");
    }

    public void setResourseString(String srcString) {
        mPathDefault.reset();
        mAnmiationPathFill.reset();
        mPathList = NumberPathManager.getInstance().getPathList(srcString, mScale, mGapBetweenLetter);
        mPathDefault = NumberPathManager.getInstance().getSrcPath(mPathList);
        mPathMaxHeight = NumberPathManager.getInstance().getPathHeigth(mPathList);
        mPathMaxWidth = NumberPathManager.getInstance().getPathWidth(mPathList);
        isMeasured = false;
        loadAnimator(mPathDefault);
    }

    /**
     * 开启动画，先停止之前的动画，然后通过监听动画的进度来进行下一步的刷新动作
     */
    public void loadAnimator(final Path pathSrc) {
        stopAnimator();
        Path pathFill = pathSrc;//重新赋值一个路径方便操作
        final float mDefaultPathLength = getPathLength(mPathDefault);
        final PathMeasure pathMeasure = new PathMeasure(pathFill, false);
        mStartIndex = 0;//第一个轮廓
        final Path pathPart = new Path();//要动态显示的部分
        mAnimator = ValueAnimator.ofFloat(0, 1f);
        mAnimator.setInterpolator(new LinearInterpolator());//线性变化
        mAnimator.setDuration(getPathDuration(pathMeasure, mDuaration, mDefaultPathLength));//设置需要绘制时间
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);//无线循环
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                mAnmiationPathFill.reset();
                pathPart.reset();
                //取出一部分放到mAnmiationPathFill路径里，true的意思是是否移动到开始的位置
                pathMeasure.getSegment(0, pathMeasure.getLength() * animatedValue, pathPart, true);//取出其中一部分路径放到pathPart上
                if (flowModel == MODEL_STEPBYSTEP) {
                    //如果是逐步效果，则需要加上之前的路径
                    mAnmiationPathFill.addPath(NumberPathManager.getInstance().getFillPath(mPathList, mStartIndex + 1, 0, false));
                }
                mAnmiationPathFill.addPath(pathPart);
                invalidate();
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                boolean isNext = pathMeasure.nextContour();
                if (!isNext && !isLooper) {
                    animation.cancel();
                    if (onFinishedListener != null) {
                        onFinishedListener.finished();
                    }
                } else {
                    if (!isNext) {
                        animation.cancel();
                        initResource();
                    }
                }
                super.onAnimationRepeat(animation);
            }
        });
        mAnimator.start();
    }

    public void stopAnimator() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }

    public void startAnimator() {
        if (mAnimator != null && !mAnimator.isRunning()) {
            mAnimator.start();
        }
    }

    /**
     * 计算path路径的总长度，因为路径有可能不是连续的，
     * 所谓连续就是 在设置路径只有一个moveTo,n个lineTo，若有多个moveTo 就代表有多个轮廓 需要用path.nextContour 跳到下个轮廓
     * pathMeasure.getLength() 是测量当前轮廓的长度
     */
    public float getPathLength(Path measurePath) {
        PathMeasure pathMeasure = new PathMeasure(measurePath, false);//false是路径不封闭
        float length = pathMeasure.getLength();
        while (pathMeasure.nextContour()) {
            length += pathMeasure.getLength();
        }
        return length;
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

    /**
     * 将时间戳转为时间字符串
     */
    public String convertTimeStamp2String() {
        return DEFAULT_SDF.format(new Date(System.currentTimeMillis()));
    }

    public interface OnFinishedListener {
        void finished();
    }

    public NumberClockView setOnFinishedListner(OnFinishedListener onFinishedListner) {
        this.onFinishedListener = onFinishedListner;
        return this;
    }

    public NumberClockView setmColorBg(int mColorBg) {
        this.mColorBg = mColorBg;
        return this;
    }
}
