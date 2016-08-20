package com.plugin.weight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @description：圆形的水波纹
 * @author：zxl
 * @CreateTime 2016/8/20.
 */
public class WaterWaveView extends View {
    private int view_width, view_height;//整个控件的宽高
    private int view_padding_left, view_padding_top, view_padding_right, view_padding_bottom;
    private float waveview_width, waveview_height;//整个水波纹的宽高
    private float stroke_circle_width = 10;//画个外圆 遮住水波纹 外圆宽度
    private float stroke_wave_width = 2;//水波纹的线宽;
    private float wave_length;//水波纹的波长
    private float wave_length_part;//四分之一的长度
    private float wave_height = 10; //水波纹的波高 波高要和移动速度一致
    private float wave_moveSpeed = 10;//移动速度
    private float wave_line;//水波纹水平线
    private float wave_leftx;//波纹的左边缘
    private int wave_num = 1;//水波纹界面的波形数目
    private int point_num;//需要测绘的点的数目
    private int current_progress = 0;//当前的进度
    private int wave_progress = 0;
    private int wave_max = 100;
    private int padding = 10;//外圆和水波纹的间距
    private int color_wave = 0xff29c475;//水波纹的颜色
    private float cicle_radius;//外圆的半径
    private Paint paint_wave, paint_cicle;
    private List<WavePoint> points;//波浪的绘制点
    private Path wavePath;//波动路径
    private boolean isMeasured = false;//是否测量了
    private MyTimerTask mTask;
    private Timer timer;
    private boolean isWave = false;//是否是波纹

    public WaterWaveView(Context context) {
        this(context, null, 0);
    }

    public WaterWaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            le("波纹开启");
            setWaveLine();
            if (wave_progress != 0) {
                isWave = true;
                startWave();
            } else {
                isWave = false;
                current_progress = 0;
                startLine();
            }
        } else {
            le("波纹关闭");
            stop();
        }
    }

    public void init() {
        timer = new Timer();
        points = new ArrayList<>();
        wavePath = new Path();
        paint_wave = new Paint();
        paint_wave.setAntiAlias(true);
        paint_wave.setStrokeWidth(stroke_wave_width);
        paint_wave.setStyle(Paint.Style.FILL);
        paint_wave.setColor(color_wave);

        paint_cicle = new Paint();
        paint_cicle.setAntiAlias(true);
        paint_cicle.setStyle(Paint.Style.STROKE);
        paint_cicle.setColor(color_wave);
        paint_cicle.setStrokeWidth(stroke_circle_width);
    }

    /**
     * 开启波浪计时器
     */
    public void startWave() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
    }

    /**
     * 开启进度条计时器
     */
    public void startLine() {
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initMeasured();
        canvas.drawCircle(view_width / 2, view_height / 2, cicle_radius, paint_cicle);
        drawWave(canvas);
    }

    /**
     * 测量水波纹的各个点
     */
    public void initMeasured() {
        if (isMeasured) {
            return;
        }
        isMeasured = !isMeasured;
        view_width = getMeasuredWidth();
        view_height = getMeasuredHeight();
        view_padding_left = getPaddingLeft();
        view_padding_top = getPaddingTop();
        view_padding_right = getPaddingRight();
        view_padding_bottom = getPaddingBottom();
        waveview_width = waveview_height = view_width - view_padding_left - view_padding_right - 2 * stroke_circle_width - padding * 2;//水波纹的宽高
        setWaveLine();
        wave_length = waveview_width;//只显示一个完整的波形
        wave_length_part = wave_length / 4;//四分之一波长
        wave_leftx = -wave_length;
        cicle_radius = view_width / 2 - view_padding_left - stroke_circle_width / 2;
        points.clear();
        //测量水波纹的点，整个水波纹一共有wave_num 波纹，
        //这个控件只能显示wave个波形，但是需要在左右两边各放置一个波形，与中间的波形连接
        //波浪是放在一个新的画布上的，所以点的坐标不需要考虑到控件的位置
        point_num = 4 * (wave_num + 2) + 1;
        for (int i = 0; i < point_num; i++) {
            float x = i * wave_length_part - wave_length;
            float y = 0;
            switch (i % 4) {
                case 1:// 上波峰
                    y = wave_line - wave_height;
                    break;
                case 3:// 下波峰
                    y = wave_line + wave_height;
                    break;
                default:
                    y = wave_line;
                    break;
            }
            points.add(new WavePoint(x, y));
        }
        le("测量" + "view_width==" + view_width + "\nview_height==" + view_height + "\nview_padding_left==" + view_padding_left
                + "\nview_padding_top==" + view_padding_top + "\nview_padding_right==" + view_padding_right + "\nview_padding_bottom==" + view_padding_bottom
        );
    }

    /**
     * 绘制波浪布
     */
    public void drawWave(Canvas canvas) {
        Bitmap waveBitmap = Bitmap.createBitmap((int) waveview_width, (int) waveview_width, Bitmap.Config.ARGB_8888);
        Canvas waveCanvas = new Canvas(waveBitmap);
        wavePath.reset();
        if (isWave) {
            //绘制波浪
            wavePath.moveTo(points.get(0).point_x, points.get(0).point_y);//波动路径的起始点
            for (int i = 0; i < point_num - 2; i++) {
                wavePath.quadTo(points.get(i + 1).point_x, points.get(i + 1).point_y,
                        points.get(i + 2).point_x, points.get(i + 2).point_y);
            }
            wavePath.lineTo(points.get(point_num - 1).point_x, waveview_height);//连接右边缘
            wavePath.lineTo(wave_leftx, waveview_height);//连接左边缘
        } else {
            //绘制水平面
            wavePath.moveTo(0, waveview_width);
            wavePath.lineTo(0, wave_line);
            wavePath.lineTo(waveview_width, wave_line);
            wavePath.lineTo(waveview_width, waveview_width);
        }
        wavePath.close();
        waveCanvas.drawPath(wavePath, paint_wave);
        canvas.drawBitmap(createCircleImage(waveBitmap),
                stroke_circle_width + padding + view_padding_left,
                stroke_circle_width + padding + view_padding_top + (view_height - view_width) / 2, paint_wave);
    }

    public Bitmap createCircleImage(Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        int width = source.getWidth();
        Bitmap target = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制圆形
         */
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);
        /**
         * 使用SRC_IN
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 绘制图片
         */
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
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
            if (wave_progress == 0) {
                isWave = true;
                startWave();
            } else if (current_progress < wave_progress) {
                current_progress++;
                //水平线上升
                setWaveLine();
                if (current_progress == wave_progress) {
                    startWave();
                }
            } else {
                isWave = true;
                wave_leftx += wave_moveSpeed;
                // 波形平移
                for (int i = 0; i < point_num; i++) {
                    points.get(i).point_x = points.get(i).point_x + wave_moveSpeed;
                    switch (i % 4) {
                        case 1:// 下波峰
                            points.get(i).point_y = wave_line + wave_height;
                            break;
                        case 3:// 上波峰
                            points.get(i).point_y = wave_line - wave_height;
                            break;
                        default:
                            points.get(i).point_y = wave_line;
                            break;
                    }
                }
                if (wave_leftx >= 0) {
                    // 波形平移超过一个完整波形后复位
                    wave_leftx = -wave_length;
                    for (int i = 0; i < point_num; i++) {
                        points.get(i).point_x = i * wave_length_part - wave_length;
                    }
                }
            }
            invalidate();
        }
    };

    public void le(String msg) {
        Log.e(getClass().getName(), "msg==" + msg);
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        wave_progress = progress;
        current_progress = 0;
        isWave = false;
        setWaveLine();
        startLine();
    }

    /**
     * 设置水平线
     */
    public void setWaveLine() {
        wave_line = waveview_height - waveview_height * current_progress / wave_max;//水波纹的水平线
    }

    private class WavePoint {
        public float point_x;
        public float point_y;

        public WavePoint(float point_x, float point_y) {
            this.point_x = point_x;
            this.point_y = point_y;
        }

        @Override
        public String toString() {
            return "WavePoint{" +
                    "point_x=" + point_x +
                    ", point_y=" + point_y +
                    '}';
        }
    }

}
