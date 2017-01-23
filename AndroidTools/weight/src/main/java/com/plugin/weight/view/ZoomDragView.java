package com.plugin.weight.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2016/12/26 16:24
 */

public class ZoomDragView extends View {
    public static final int MODE_SCALE = 0x002;//放缩
    public static final int MODE_TRANSLATION = 0x001;//移动
    public static final int MODE_NULL = 0x00;
    private int MODE_TYPE = MODE_TRANSLATION;
    private float mStartX, mStartY, mStartScaleDaistace;//按下时，第一个点的坐标，以及第二个触摸点按下时的距离
    private int action;//触摸事件
    private float drawCircle_X, drawCircle_Y;
    private float drawCircle_radius = 5;
    private float circle_x, circle_y;
    private float circle_radius = 5;
    private Paint paint, paint_text;
    private float mViewWidth, mViewHeight;
    private boolean isMeasured = false;

    public ZoomDragView(Context context) {
        this(context, null, 0);
    }

    public ZoomDragView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomDragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);

        paint_text = new Paint();
        paint_text.setAntiAlias(true);
        paint_text.setColor(Color.GREEN);
        paint_text.setStrokeWidth(1);
        paint_text.setTextSize(60);
        paint_text.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isMeasured) {
            isMeasured = true;
            mViewWidth = getMeasuredWidth();
            mViewHeight = getMeasuredHeight();
        }
        canvas.drawCircle(drawCircle_X, drawCircle_Y, drawCircle_radius, paint);
        canvas.drawText("x::::" + drawCircle_X, 0, mViewHeight / 2 - 60, paint_text);
        canvas.drawText("y::::" + drawCircle_Y, 0, mViewHeight / 2, paint_text);
        canvas.drawText("radius::::" + drawCircle_radius, 0, mViewHeight / 2 + 60, paint_text);
    }

    /***
     * 1、第一个手指按下，记录x,y坐标，模式为拖动模式
     *    第二个手指按下，记录第一个手指和第二个手指之间的距离，记录为缩放模式
     * 2、手指抬起 第二个手机抬起，记录绘制的圆的半径，当前模式为无
     *             第一个手指抬起，记录圆的中心坐标，当前模式为无
     * 3、手指处于移动中时，判断模式，若是缩放模式或手指大于1，则计算圆的半径，中心点坐标不变
     *                                若是拖动模式，则计算圆滑动后的中心点坐标，圆半径不变
     *                                若是为无模式，则记录为滑动模式，记录按下的坐标，以及圆的半径，中心点坐标
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        action = event.getAction() & MotionEvent.ACTION_MASK;
        int nCnt = event.getPointerCount();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //设置成滑动模式
                zzz("MotionEvent.ACTION_DOWN");
                MODE_TYPE = MODE_TRANSLATION;
                mStartX = event.getX();
                mStartY = event.getY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                zzz("MotionEvent.ACTION_POINTER_DOWN");
                //有两个以上的触摸点时，会调用此方法
                MODE_TYPE = MODE_SCALE;
                if (nCnt == 2) {
                    mStartScaleDaistace = distanceScale(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                zzz("MotionEvent.ACTION_UP");
                //手指抬起，设置成无模式，保存当前的圆的中心坐标
                MODE_TYPE = MODE_NULL;
                circle_x = drawCircle_X;
                circle_y = drawCircle_Y;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //当多个触摸点，其中一个离开时，会调用此方法 执行此事件后可能会立即执行 ACTION_MOVE
                if (nCnt == 2) {
                    zzz("MotionEvent.ACTION_POINTER_UP" + nCnt);
                    //c触摸点为2时执行此方法，说明之后就只有一个触摸点，所以在此时设置成无模式，保存缩放的半径
                    MODE_TYPE = MODE_NULL;
                    circle_radius = drawCircle_radius;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (MODE_TYPE == MODE_SCALE || nCnt > 1) {
                    float distance = distanceScale(event);
                    drawCircle_radius = circle_radius * distance / mStartScaleDaistace;//计算放缩后圆的半径
                    zzz("0000--------" + "  r==" + drawCircle_radius + "  r_x==" + drawCircle_X + "   r_y==" + drawCircle_Y + "   X==" + mStartX + "   Y==" + mStartY + "  " + "  x==" + event.getX() + "   y==" + event.getY() + "  D==" + mStartScaleDaistace + "   d==" + distance);
                } else if (MODE_TYPE == MODE_TRANSLATION) {
                    //计算滑动后圆的坐标
                    drawCircle_X = circle_x + (event.getX() - mStartX);
                    drawCircle_Y = circle_y + (event.getY() - mStartY);
                    zzz("1111--------" + "  r==" + drawCircle_radius + "  r_x==" + drawCircle_X + "   r_y==" + drawCircle_Y + "   X==" + mStartX + "   Y==" + mStartY + "  " + "  x==" + event.getX() + "   y==" + event.getY());
                } else {
                    MODE_TYPE = MODE_TRANSLATION;
                    mStartX = event.getX();
                    mStartY = event.getY();
                    circle_radius = drawCircle_radius;
                    circle_x = drawCircle_X;
                    circle_y = drawCircle_Y;
                    zzz("2222--------" + "  r==" + drawCircle_radius + "  r_x==" + drawCircle_X + "   r_y==" + drawCircle_Y + "   X==" + mStartX + "   Y==" + mStartY);
                }
                invalidate();
                break;
            default:
                zzz("----------------------------------------------------");
                break;

        }
        return super.onTouchEvent(event);
    }

    /**
     * 两个手指之间的距离
     */
    public float distanceScale(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance;
    }

    private void zzz(String msg) {
//        LogUtils.e("ZoomDragView", msg);
    }
}
