package com.plugin.weight.arc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Description
 * @Created by zxl on 15/10/16.
 */

public class ArcRing4View extends TextView {
    private Timer timer = null;
    private int countMax = 30;//总计数
    private int current_num = 0;
    private int period = 100;//每个100毫秒计时器刷新一下
    private MTimeTask task;
    private View bindView;
    private int view_width, view_height;//整个View的宽高
    private String str = "计时(30)";
    private int text_size = 30;//字体大小
    private Paint paint_circle_in, paint_circle_out;//画笔,内外圆
    private Paint paint_arc;//画笔,绘制弧线
    private Paint paint_text;//画笔,绘制字体

    private int width_arc = 10;//弧线的宽度
    private int padding_arc2circle = 10;//弧形和内圆之间的距离
    private int padding_circle2text = 20;//内圆和字体的距离
    private float radius_circle_out, radius_circle_in;//内外圆的半径 此半径需要在onMeasure 里面计算和设置

    private int color_circle_out = Color.parseColor("#4169E1");//外圆颜色
    private int color_text = Color.WHITE;//字体颜色
    private int color_arc = Color.parseColor("#00FFFF");//弧线颜色
    private int color_circle_in = Color.parseColor("#3CB371");//内圆颜色
    private RectF rectFArc;//此为弧形区域
    private RectF rectFText;//字体的绘制区域
    private float baseTextY;//绘制字体的Y轴基准线

    public ArcRing4View(Context context) {
        this(context, null, 0);
    }

    public ArcRing4View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcRing4View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化画笔
     */
    public void init() {
        paint_circle_in = new Paint();
        paint_circle_in.setDither(true);
        paint_circle_in.setAntiAlias(true);
        paint_circle_in.setStrokeWidth(1);
        paint_circle_in.setStyle(Paint.Style.FILL);//实心圆
        paint_circle_in.setColor(color_circle_in);

        paint_circle_out = new Paint();
        paint_circle_out.setDither(true);
        paint_circle_out.setAntiAlias(true);
        paint_circle_out.setStrokeWidth(1);
        paint_circle_out.setStyle(Paint.Style.FILL);//实心圆
        paint_circle_out.setColor(color_circle_out);

        paint_arc = new Paint();
        paint_arc.setDither(true);
        paint_arc.setAntiAlias(true);
        paint_arc.setStrokeWidth(width_arc);
        paint_arc.setStyle(Paint.Style.STROKE);//空心,只是线
        paint_arc.setColor(color_arc);
        paint_arc.setStrokeCap(Paint.Cap.ROUND);//线的类型是圆头的

        paint_text = new Paint();
        paint_text.setDither(true);
        paint_text.setAntiAlias(true);
        paint_text.setStrokeWidth(width_arc);
        paint_text.setStyle(Paint.Style.FILL);//实心圆
        paint_text.setColor(color_text);
        paint_text.setTextSize(text_size);
        paint_text.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * 给View设置宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float length = paint_text.measureText(str);//获取要绘制字符串的最大长度,此长度加上内圆和字体的距离就是内圆的直径
        view_height = view_width = (int) (length + padding_arc2circle * 2 + width_arc * 2 + padding_circle2text * 2);//内圆直径+弧线的宽度+弧线和内圆的间距+内圆和字体的距离
        radius_circle_in = padding_circle2text + length / 2;
        radius_circle_out = view_width / 2;
        rectFArc = new RectF(width_arc / 2, width_arc / 2, view_width - width_arc / 2, view_height - width_arc / 2);//圆弧的区域要留一定的边距,此距离是弧线宽度的一半
        rectFText = new RectF(0, 0, view_width, view_height);
        //测量每行的基准线，这样才能使字符串的位置垂直居中
        Paint.FontMetricsInt fontMetrics = paint_text.getFontMetricsInt();
        baseTextY = (rectFText.bottom + rectFText.top - fontMetrics.bottom - fontMetrics.top) / 2;
        setMeasuredDimension(view_width, view_height);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        float degree = (countMax - current_num) * 360 / countMax;//弧线要滑过的度数
        Log.e("TAG", "view_height==" + view_height + "  degree==" + degree);
        canvas.drawCircle(view_width / 2, view_height / 2, radius_circle_out, paint_circle_out);//绘制外圆
        canvas.drawArc(rectFArc, 180, degree, false, paint_arc);//绘制弧线
        canvas.drawCircle(view_width / 2, view_height / 2, radius_circle_in, paint_circle_in);//绘制内圆
        canvas.drawText(getCountString(current_num), view_width / 2, baseTextY, paint_text);//绘制字体
    }

    /**
     * 界面消失销毁
     *
     * @param hasWindowFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            stop();
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (current_num == 0) {
                setVisibility(GONE);
                if (bindView != null) {
                    bindView.setVisibility(VISIBLE);
                }
                stop();
            } else {
                if (current_num == countMax) {
                    setVisibility(VISIBLE);
                    if (bindView != null) {
                        bindView.setVisibility(GONE);
                    }
                }
                current_num = current_num - 1;
            }
            invalidate();
        }
    };

    public void bindBtn(View view) {
        this.bindView = view;
    }

    private class MTimeTask extends TimerTask {

        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }


    public String getCountString(int index) {
        return "计时(" + index + ")";
    }

    /**
     * 开启计时器 重置数据
     */
    public void start() {
        stop();
        current_num = countMax;
        timer = new Timer();
        task = new MTimeTask();
        timer.schedule(task, 0, period);
    }

    /**
     * 关闭计时器
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            if (task != null) {
                task.cancel();
            }
        }
    }

}
