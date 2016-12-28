package com.plugin.weight.flowchar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
    private int mColorCharFill = 0xffff00ff;//线的填充颜色
    private int mLineWidth = 4;//线条的宽度
    private Paint mPaintDefault, mPaintFill;//默认，填充画笔
    private ArrayList<float[]> mPathList;//存储线条的集合
    private boolean isLooper = true;//线条的选择是否可以循环
    private int mFillSize = 2;//每次填充的线条数目
    private int mStartIndex = 0;//线条填充时的下标
    private float mScale = 1;//缩放比例
    private float mGapBetweenLetter = 30;//两个字符的间距
    private Timer mTimer;//计时器
    private MTimeTask mTask;
    private int mPeriod = 500;//500毫秒发送一个消息

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

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            stop();
        } else {
            if (mPathList != null) {
                start();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(mColorBg);
        canvas.translate(mPaddingLeft,mPaddingTop);
        canvas.drawPath(mPathDefault, mPaintDefault);
        canvas.drawPath(mPathFill, mPaintFill);
    }

    private class MTimeTask extends TimerTask {

        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mPathList == null)
                return;
            mPathFill = FlowCharPathManager.getInstance().getFillPath(mPathList, mFillSize, mStartIndex, isLooper);
            mStartIndex++;
            if (mStartIndex >= mPathList.size()) {
                mStartIndex = 0;
            }
            invalidate();
        }
    };

    /**
     * 开启计时器 重置数据
     */
    public void start() {
        stop();
        mTimer = new Timer();
        mTask = new MTimeTask();
        mTimer.schedule(mTask, 0, mPeriod);
    }

    /**
     * 关闭计时器
     */
    public void stop() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
            mTimer = null;
        }
    }

    public void setResourseString(String srcString) {
        stop();
        mPathDefault.reset();
        mPathFill.reset();
        mStartIndex = 0;
        mPathList = FlowCharPathManager.getInstance().getPathList(srcString, mScale, mGapBetweenLetter);
        mPathDefault = FlowCharPathManager.getInstance().getSrcPath(mPathList);
        mPathFill = FlowCharPathManager.getInstance().getFillPath(mPathList, mFillSize, mStartIndex, isLooper);
        start();
    }

}
