package com.plugin.weight.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.plugin.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2016/12/30 15:09
 */

public class TimeClockView extends View {
    private int mViewHeight, mViewWidth;
    private List<Path> mDialPath;//刻度的路径
    private Path mDialOutsidePath, mDialHourPath, mDialMinutePath, mDialHourTextPath;//最外层的圆的路径,代表小时的刻度圆,代表分的刻度圆,时刻的圆
    private Paint mPaintHourHand, mPaintMinuteHand;//时针，分针
    private Paint mPaintText;
    private float mDialLongLength, mDialShortLength;
    private float mDialCenterX, mDialCenterY;//中心点
    private float mDialCircleRadius, mDialCircleHourRadius, mDialCircleMinuteRadius, mDialCircleHourTextRaidus;//刻度盘半径,时针，分针的半径,时刻的半径
    private float mDialSideWith = 10;//刻度盘的外边长度
    private int mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;//View的左，上,右,下padding
    private int mColorGold = 0xffffd700;//金色
    private int mColorBlue = 0xff4169e1;
    private List<String> mDialHourData;
    private List<PointF> mDialHoutTextPoints;
    private float mDialTextX, mDialTextY;

    public TimeClockView(Context context) {
        this(context, null, 0);
    }

    public TimeClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
        mPaddingBottom = getPaddingBottom();
        mPaddingRight = getPaddingRight();
        int width = DisplayUtils.getInstance().getScreenWidth() - mPaddingLeft - mPaddingRight;
        int height = DisplayUtils.getInstance().getScreenHeight() - mPaddingTop - mPaddingBottom;
        mViewHeight = mViewWidth = width > height ? height : width;//取出屏幕宽高，计算出view所需要的宽高
        mDialCenterX = mDialCenterY = mViewWidth / 2;//中心点
        mDialCircleRadius = mViewWidth / 2 - mDialSideWith;
        mDialLongLength = mDialCircleRadius / 4;
        mDialShortLength = mDialCircleRadius / 10;
        mDialCircleMinuteRadius = mDialCircleRadius - mDialShortLength - mDialSideWith;
        mDialCircleHourRadius = mDialCircleRadius - mDialLongLength - mDialSideWith;
        mDialCircleHourTextRaidus = mDialCircleHourRadius - 10;

        mPaintHourHand = new Paint();
        mPaintHourHand.setAntiAlias(true);
        mPaintHourHand.setDither(true);
        mPaintHourHand.setStyle(Paint.Style.STROKE);
        mPaintHourHand.setColor(mColorGold);
        mPaintHourHand.setStrokeWidth(5);

        mPaintMinuteHand = new Paint();
        mPaintMinuteHand.setAntiAlias(true);
        mPaintMinuteHand.setDither(true);
        mPaintMinuteHand.setStyle(Paint.Style.STROKE);
        mPaintMinuteHand.setColor(mColorBlue);
        mPaintMinuteHand.setStrokeWidth(2);

        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setDither(true);
        mPaintText.setStyle(Paint.Style.STROKE);
        mPaintText.setColor(mColorBlue);
        mPaintText.setStrokeWidth(2);
        mPaintText.setTextSize(18);

        mDialPath = new ArrayList<>();
        mDialHourData = new ArrayList<>();
        mDialHoutTextPoints = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            mDialHourData.add((i + 1) + " ");
        }
        measureDial();
    }

    public void measureDial() {
        //绘制最外层的路径
        mDialOutsidePath = new Path();
        mDialOutsidePath.addCircle(mDialCenterX, mDialCenterY, mDialCircleRadius, Path.Direction.CW);//CW  顺时针   CCW 逆时针
        PathMeasure pathOutSideMeasure = new PathMeasure(mDialOutsidePath, true);

        mDialHourPath = new Path();
        mDialHourPath.addCircle(mDialCenterX, mDialCenterY, mDialCircleHourRadius, Path.Direction.CW);//长刻度,时针刻度
        PathMeasure pathHourMeasure = new PathMeasure(mDialHourPath, true);

        mDialMinutePath = new Path();
        mDialMinutePath.addCircle(mDialCenterX, mDialCenterY, mDialCircleMinuteRadius, Path.Direction.CW);//短刻度,分针刻度
        PathMeasure pathMinuteMeasure = new PathMeasure(mDialMinutePath, true);

        mDialHourTextPath = new Path();
        mDialHourTextPath.addCircle(mDialCenterX, mDialCenterY, mDialCircleHourTextRaidus, Path.Direction.CW);//短刻度,分针刻度
        PathMeasure pathHourTextMeasure = new PathMeasure(mDialHourTextPath, true);

        float lengthOutSide = pathOutSideMeasure.getLength();
        float lengthShortSide = pathMinuteMeasure.getLength();
        float lengthLongSide = pathHourMeasure.getLength();
        float lengthHourTextSide=pathHourTextMeasure.getLength();

        float posOutSide[] = new float[2];
        float posShortSide[] = new float[2];
        float posLongSide[] = new float[2];
        float posHourTextSide[]=new float[2];

        mDialPath.clear();
        Paint.FontMetricsInt fontMetrics = mPaintText.getFontMetricsInt();
        Rect rectF = new Rect();
        for (int i = 0; i < 60; i++) {
            pathOutSideMeasure.getPosTan(lengthOutSide * i / 60, posOutSide, null);
            Path path = new Path();
            path.moveTo(posOutSide[0], posOutSide[1]);
            if (i % 5 == 0) {
                //时针
                pathHourMeasure.getPosTan(lengthLongSide * i / 60, posLongSide, null);
                path.lineTo(posLongSide[0], posLongSide[1]);
            } else {
                pathMinuteMeasure.getPosTan(lengthShortSide * i / 60, posShortSide, null);
                path.lineTo(posShortSide[0], posShortSide[1]);
            }
            mDialPath.add(path);
        }

        mDialHoutTextPoints.clear();
        for (int i = 0; i < 12; i++) {
            pathHourTextMeasure.getPosTan(lengthHourTextSide * i / 12, posHourTextSide, null);
            rectF.bottom = (int) (posHourTextSide[1] + 10);
            rectF.top = (int) (posHourTextSide[1] - 10);
            rectF.left = (int) (posHourTextSide[0] - 20);
            rectF.right = (int) (posHourTextSide[0] + 10);
            mDialHoutTextPoints.add(new PointF(posHourTextSide[0], (rectF.bottom + rectF.top - fontMetrics.bottom - fontMetrics.top) / 2));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //设置View宽高
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int index = 0;
        for (Path path : mDialPath) {
            if (index % 5 == 0) {
                canvas.drawPath(path, mPaintHourHand);
            } else {
                canvas.drawPath(path, mPaintMinuteHand);
            }
            index++;
        }
        for (int i = 0; i < 12; i++) {
            index = (i + 2) % 12;
            canvas.drawText(mDialHourData.get(index), mDialHoutTextPoints.get(i).x, mDialHoutTextPoints.get(i).y, mPaintText);
        }
    }
}
