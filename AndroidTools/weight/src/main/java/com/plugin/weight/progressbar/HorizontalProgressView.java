package com.plugin.weight.progressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.plugin.utils.log.LogUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Description:
 * @Author: zxl
 * @Date: 8/9/16 AM10:47.
 */
public class HorizontalProgressView extends View {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 最大进度
     */
    private int progress_max = 100;
    /**
     * 当前进度
     */
    private int progress_current = 0;


    /**
     * 进度的颜色
     */
    private int progress_reached_color = 0xff4291F1;
    /**
     * 进度的背景颜色
     */
    private int progress_unreached_color = 0xffcccccc;
    /**
     * 进度的高
     */
    private final float progress_height = dp2px(2f);
    /**
     * 进度的宽
     */
    private int progress_width;

    /**
     * 当期进度的区域
     */
    private RectF mReachedRectF = new RectF(0, 0, 0, 0);
    /**
     * 剩余进度的区域
     */
    private RectF mUnreachedRectF = new RectF(0, 0, 0, 0);
    /**
     * 是否绘制进度区域
     */
    private boolean mDrawReachedBar = true;
    /**
     * 是否绘制剩余区域
     */
    private boolean mDrawUnreachedBar = true;
    /**
     * 绘制进度的画笔
     */
    private Paint mReachedBarPaint;
    /**
     * 绘制剩余区域的画笔
     */
    private Paint mUnreachedBarPaint;

    private MyTimerTask mTask;
    private Timer timer;


    private boolean isStartTimer = false;

    public HorizontalProgressView(Context context) {
        this(context, null, 0);
    }

    public HorizontalProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        progress_width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(progress_width, (int) progress_height);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus && isStartTimer) {
            start();
        } else {
            stop();
        }
    }

    /**
     * 开启进度条计时器
     */
    public void start() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 100);
    }

    /**
     * 关闭计时器
     */
    public void stop() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
    }

    /**
     * 初始化一些数据
     */
    public void init() {
        timer = new Timer();
        mReachedBarPaint = new Paint();
        mUnreachedBarPaint = new Paint();
        initPaint(mReachedBarPaint, progress_reached_color, Paint.Style.FILL);
        initPaint(mUnreachedBarPaint, progress_unreached_color, Paint.Style.FILL);
    }

    /**
     * 初始化画笔
     */
    public void initPaint(Paint paint, int color, Paint.Style style) {
        paint.setColor(color);
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(style);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        calculateDrawRectF();
        if (mDrawReachedBar)
            canvas.drawRect(mReachedRectF, mReachedBarPaint);
        if (mDrawUnreachedBar)
            canvas.drawRect(mUnreachedRectF, mUnreachedBarPaint);
    }

    /**
     * 测量进度需要的区域
     */
    public void calculateDrawRectF() {
        LogUtils.d("progress_current==" + progress_current + "\nprogress_width==" + progress_width + "\nprogress_height==" + progress_height);
        if (progress_current == 0) {
            mDrawReachedBar = false;
            mDrawUnreachedBar = true;
            mUnreachedRectF.left = 0;
            mUnreachedRectF.top = 0;
            mUnreachedRectF.right = progress_width;
            mUnreachedRectF.bottom = progress_height;
        } else if (progress_current == progress_max) {
            mDrawReachedBar = true;
            mDrawUnreachedBar = false;
            mReachedRectF.left = 0;
            mReachedRectF.top = 0;
            mReachedRectF.right = progress_width;
            mReachedRectF.bottom = progress_height;
        } else {
            mDrawReachedBar = true;
            mDrawUnreachedBar = true;

            mReachedRectF.left = 0;
            mReachedRectF.top = 0;
            mReachedRectF.right = progress_width * progress_current / progress_max;
            mReachedRectF.bottom = progress_height;

            mUnreachedRectF.left = progress_width * progress_current / progress_max;
            mUnreachedRectF.top = 0;
            mUnreachedRectF.right = progress_width;
            mUnreachedRectF.bottom = progress_height;
        }
    }

    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public float sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public void setProgress_reached_color(int progress_reached_color) {
        this.progress_reached_color = progress_reached_color;
    }

    public void setProgress_unreached_color(int progress_unreached_color) {
        this.progress_unreached_color = progress_unreached_color;
    }

    public int getProgress_max() {
        return progress_max;
    }

    public void setProgress_current(int progress_current) {
        if (progress_current <= progress_max && progress_current >= 0 && this.progress_current != progress_current) {
            this.progress_current = progress_current;
            invalidate();
        }
    }

    private class MyTimerTask extends TimerTask {
        private Handler handler;

        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }
    }

    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress_current++;
            if (progress_current > 100) {
                progress_current = 0;
            }
            invalidate();
        }
    };

    public void setStartTimer(boolean startTimer) {
        isStartTimer = startTimer;
        if (isStartTimer) {
            start();
        } else {
            stop();
        }
    }
}
