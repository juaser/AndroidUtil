package com.plugin.weight.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.plugin.utils.image.ImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Description 画板
 * @Created by zxl on 30/9/16.
 */

public class DrawingBoardView extends View {
    private Context mContext;
    private Paint mPaint;//画笔
    private float mX, mY;//笔画X坐标起点,笔画Y坐标起点
    private int mColor_board = Color.WHITE;//画板的颜色
    private int mPaint_color = Color.GREEN;//画笔颜色
    private int mPaint_width = 10;//画笔的宽度
    private Path mPath;//画笔的路径
    private Canvas mCacheCanvas;//背景画布
    private Bitmap mCachebBitmap;//背景Bitmap缓存
    private boolean isTouched = false;//是否画上

    private int view_width, view_height;//画板的宽高

    public DrawingBoardView(Context context) {
        this(context, null, 0);
    }

    public DrawingBoardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawingBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    /**
     * 初始化数据 画笔
     */
    public void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setStyle(Paint.Style.STROKE);//画笔空心
        mPaint.setStrokeWidth(mPaint_width);//设置画笔款宽度
        mPaint.setStrokeCap(Paint.Cap.ROUND);//画笔笔刷类型
        mPaint.setColor(mPaint_color);//画笔颜色
    }

    /**
     * View 大小发生变化时 调用
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //这里只是创造一个画板 ，但是还没有放到View上 需在onDraw上进行
        view_width = getWidth();
        view_height = getHeight();
        mCachebBitmap = Bitmap.createBitmap(view_width, view_height, Bitmap.Config.ARGB_8888);
        mCacheCanvas = new Canvas(mCachebBitmap);
        mCacheCanvas.drawColor(mColor_board);
        isTouched = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mCachebBitmap, 0, 0, mPaint);
        canvas.drawPath(mPath, mPaint); // 通过画布绘制多点形成的图形
    }

    /**
     * 监听画板上的动作
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
                touchDown(event);
                break;
            case MotionEvent.ACTION_MOVE://滑动
                isTouched = true;
                touchMove(event);
                break;
            case MotionEvent.ACTION_UP://提起
                mCacheCanvas.drawPath(mPath, mPaint);
                mPath.reset();
                break;
        }
        // 更新绘制
        invalidate();
        return true;
    }

    /**
     * 手指点下屏幕时调用
     *
     * @param event
     */
    private void touchDown(MotionEvent event) {
        // 重置绘制路线，即隐藏之前绘制的轨迹
        mPath.reset();
        //取出x,y坐标
        mX = event.getX();
        mY = event.getY();
        // mPath绘制的绘制起点
        mPath.moveTo(mX, mY);
    }

    /**
     * 手指在屏幕上滑动时调用
     *
     * @param event
     */
    private void touchMove(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        final float previousX = mX;
        final float previousY = mY;

        final float dx = Math.abs(x - previousX);
        final float dy = Math.abs(y - previousY);

        // 两点之间的距离大于等于3时，生成贝塞尔绘制曲线
        if (dx >= 3 || dy >= 3) {
            // 设置贝塞尔曲线的操作点为起点和终点的一半
            float cX = (x + previousX) / 2;
            float cY = (y + previousY) / 2;
            // 二次贝塞尔，实现平滑曲线；previousX, previousY为操作点，cX, cY为终点
            mPath.quadTo(previousX, previousY, cX, cY);
            // 第二次执行时，第一次结束调用的坐标值将作为第二次调用的初始坐标值
            mX = x;
            mY = y;
        }
    }

    /**
     * 清除画板
     */
    public void clear() {
        if (mCacheCanvas != null) {
            isTouched = false;
            mPaint.setColor(mPaint_color);
            mCacheCanvas.drawColor(mColor_board, PorterDuff.Mode.SRC_OVER);// 所绘制不会提交到画布上。
            mPaint.setColor(mPaint_color);
            invalidate();
        }
    }

    /**
     * 保存画板
     *
     * @param isSign     是否签名
     * @param clearBlank 是否清楚空白区域
     * @param blank      边缘空白区域
     */
    public String save(boolean isSign, boolean clearBlank, int blank) {
        Bitmap bitmap = getBitmap(clearBlank, blank);
        if (isSign) {
            //签名
            bitmap = ImageUtils.getInstance().createWaterMaskBitmap(bitmap, ImageUtils.getInstance().createSignBitmap(view_width, 150), 0, 0);
        }

        String path = getPath();
        if (saveBitmap(bitmap, path)) {
            return path;
        } else {
            return "";
        }
    }

    /**
     * 保存bitmap 到手机
     *
     * @param bitmap
     * @param path
     */
    public boolean saveBitmap(Bitmap bitmap, String path) {
        File file = isFileExits(path);
        if (file == null) {
            return false;
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            Log.e("TAG", "saveBitmap Exception----" + path);
            return false;
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 获取画板的bitmap
     */
    public Bitmap getBitmap(boolean clearBlank, int blank) {
        if (clearBlank) {
            return clearBlank(mCachebBitmap, blank);
        } else {
            return mCachebBitmap;
        }
    }


    /**
     * 逐行扫描 清楚边界空白。
     *
     * @param bp
     * @param blank 边距留多少个像素
     * @return
     */
    private Bitmap clearBlank(Bitmap bp, int blank) {
        int HEIGHT = bp.getHeight();
        int WIDTH = bp.getWidth();
        int top = 0, left = 0, right = 0, bottom = 0;
        int[] pixs = new int[WIDTH];
        boolean isStop;
        for (int y = 0; y < HEIGHT; y++) {
            bp.getPixels(pixs, 0, WIDTH, 0, y, WIDTH, 1);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mColor_board) {
                    top = y;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        for (int y = HEIGHT - 1; y >= 0; y--) {
            bp.getPixels(pixs, 0, WIDTH, 0, y, WIDTH, 1);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mColor_board) {
                    bottom = y;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        pixs = new int[HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            bp.getPixels(pixs, 0, 1, x, 0, 1, HEIGHT);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mColor_board) {
                    left = x;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        for (int x = WIDTH - 1; x > 0; x--) {
            bp.getPixels(pixs, 0, 1, x, 0, 1, HEIGHT);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mColor_board) {
                    right = x;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        if (blank < 0) {
            blank = 0;
        }
        left = left - blank > 0 ? left - blank : 0;
        top = top - blank > 0 ? top - blank : 0;
        right = right + blank > WIDTH - 1 ? WIDTH - 1 : right + blank;
        bottom = bottom + blank > HEIGHT - 1 ? HEIGHT - 1 : bottom + blank;
        return Bitmap.createBitmap(bp, left, top, right - left, bottom - top);
    }

    /**
     * 获取保存路径
     * 文件夹下 sign/
     *
     * @return
     */
    public String getPath() {
        String dir = "test";
        String rootpath = "";
        if (isSDCardAvailable()) {
            rootpath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + dir;
        } else {
            rootpath = mContext.getCacheDir().getAbsolutePath() + File.separator + dir;
        }
        File file = new File(rootpath);
        if (!file.exists()) {
            //如果文件夹不存在则创建
            file.mkdirs();
        }
        return new File(rootpath, System.currentTimeMillis() + ".png").getAbsolutePath();
    }

    /**
     * 判断SD卡是否挂载
     */
    public boolean isSDCardAvailable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 文件是否存在
     */
    public File isFileExits(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e("TAG", "saveBitmap can not create new file----" + path);
            }
        }
        return file.exists() ? file : null;
    }

    /**
     * 是否有签名
     *
     * @return
     */
    public boolean getTouched() {
        return isTouched;
    }

}
