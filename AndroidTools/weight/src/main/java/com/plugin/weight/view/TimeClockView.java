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
import com.plugin.utils.TimeUtils;

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
    private Path mDialHourHandPath, mDialMinuteHandPath, mDialSecondHandPath;//时针,分针,秒针长度头所指向的位置集合，
    private Paint mPaintHourHand, mPaintMinuteHand, mPaintSecondHand;//时针，分针,秒针
    private Paint mPaintText;
    private float mDialLongLength, mDialShortLength;
    private float mDialCenterX, mDialCenterY;//中心点
    private float mDialCircleRadius, mDialCircleHourRadius, mDialCircleMinuteRadius, mDialCircleHourTextRaidus;//刻度盘半径,时针，分针的半径,时刻的半径
    private float mDialSideWith = 10;//刻度盘的外边长度
    private int mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;//View的左，上,右,下padding
    private int mColorGold = 0xffffd700;//金色
    private int mColorBlue = 0xff4169e1;//蓝色
    private List<String> mDialHourData;
    private List<PointF> mDialHoutTextPoints;
    private PathMeasure pathHourHandMeasure, pathMinuteHandMeasure, pathSecondHandMeasure;

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
        initMeasureDial();
    }

    public void initMeasureDial() {
        initDialPathList();
        initDialTextPathList();
        //时针头对应的路径
        mDialHourHandPath = new Path();
        mDialHourHandPath.addCircle(mDialCenterX, mDialCenterY, mDialCircleHourRadius - 10, Path.Direction.CW);//CW  顺时针
        pathHourHandMeasure = new PathMeasure(mDialHourHandPath, true);
        //分针头对应的路径
        mDialMinuteHandPath = new Path();
        mDialMinuteHandPath.addCircle(mDialCenterX, mDialCenterY, mDialCircleMinuteRadius - 10, Path.Direction.CW);//CW  顺时针
        pathMinuteHandMeasure = new PathMeasure(mDialHourHandPath, true);//分针头对应的路径
        //秒针头对应的路径
        mDialSecondHandPath = new Path();
        mDialSecondHandPath.addCircle(mDialCenterX, mDialCenterY, mDialCircleRadius - 10, Path.Direction.CW);//CW  顺时针
        pathSecondHandMeasure = new PathMeasure(mDialSecondHandPath, true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //设置View宽高
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaintMinuteHand.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mDialCenterX, mDialCenterY, 10, mPaintMinuteHand);
        mPaintMinuteHand.setStyle(Paint.Style.STROKE);
        //绘制表盘
        int index = 0;
        for (Path path : mDialPath) {
            if (index % 5 == 0) {
                canvas.drawPath(path, mPaintHourHand);
            } else {
                canvas.drawPath(path, mPaintMinuteHand);
            }
            index++;
        }
        //绘制表盘上的数字
        for (int i = 0; i < 12; i++) {
            //为啥要加2呢，因为这个是顺时针的计算的点数，起始点是从圆的最右端也就是钟表的3的位置计算的，i+2取出来的字符就是3的位置
            index = (i + 2) % 12;
            canvas.drawText(mDialHourData.get(index), mDialHoutTextPoints.get(i).x, mDialHoutTextPoints.get(i).y, mPaintText);
        }
        long currenttime = TimeUtils.getInstance().getCurrentTimeStamp();
        long time = (currenttime - TimeUtils.getInstance().getTimeStampZeroTime(currenttime)) / 1000 % 43200;//当前表盘的总秒数 12*60*60=43200
        PointF hourPoint = getCurrentHourHandPoint(time);
        PointF minutePoint = getCurrentMinuteHandPoint(time);
        PointF secondPoint = getCurrentSecondHandPoint(time);
        //绘制时针,分针,秒针
        canvas.drawLine(mDialCenterX, mDialCenterY, hourPoint.x, hourPoint.y, mPaintHourHand);
        canvas.drawLine(mDialCenterX, mDialCenterY, minutePoint.x, minutePoint.y, mPaintMinuteHand);
        canvas.drawLine(mDialCenterX, mDialCenterY, secondPoint.x, secondPoint.y, mPaintMinuteHand);
        invalidate();
    }

    /**
     * @param temp 距离当前时间零点的时间戳 12x60x60=43200  因为路径是按照顺时针，开始位置在三点钟位置，所以最终的时间应该有3/4的跨度
     */
    public PointF getCurrentHourHandPoint(long temp) {
        if (pathHourHandMeasure == null) {
            return new PointF(0, 0);
        }
        temp = (temp + 32400) % 43200;
        float lengthHourHandSide = pathHourHandMeasure.getLength();
        float posHourHandSide[] = new float[2];
        pathMinuteHandMeasure.getPosTan(lengthHourHandSide * temp / 43200, posHourHandSide, null);
        return new PointF(posHourHandSide[0], posHourHandSide[1]);
    }

    /**
     * @param temp 距离当前时间零点的时间戳 60x60  因为路径是按照顺时针，开始位置在三点钟位置，所以最终的时间应该有3/4的跨度
     */
    public PointF getCurrentMinuteHandPoint(long temp) {
        if (pathMinuteHandMeasure == null) {
            return new PointF(0, 0);
        }
        temp = (temp + 2700) % 3600;
        float lengthMinuteHandSide = pathMinuteHandMeasure.getLength();
        float posMinuteHandSide[] = new float[2];
        pathMinuteHandMeasure.getPosTan(lengthMinuteHandSide * temp / 3600, posMinuteHandSide, null);
        return new PointF(posMinuteHandSide[0], posMinuteHandSide[1]);
    }

    /**
     * @param temp 距离当前时间零点的时间戳 60  因为路径是按照顺时针，开始位置在三点钟位置，所以最终的时间应该有3/4的跨度
     */
    public PointF getCurrentSecondHandPoint(long temp) {
        if (pathSecondHandMeasure == null) {
            return new PointF(0, 0);
        }
        temp = (temp + 45) % 60;
        float lengthSecondHandSide = pathSecondHandMeasure.getLength();
        float posSecondHandSide[] = new float[2];
        pathSecondHandMeasure.getPosTan(lengthSecondHandSide * temp / 60, posSecondHandSide, null);
        return new PointF(posSecondHandSide[0], posSecondHandSide[1]);
    }

    /**
     * 表盘的刻度路径
     */
    public void initDialPathList() {
        //绘制最外层的路径
        Path mDialOutsidePath = new Path();
        mDialOutsidePath.addCircle(mDialCenterX, mDialCenterY, mDialCircleRadius, Path.Direction.CW);//CW  顺时针   CCW 逆时针
        PathMeasure pathOutSideMeasure = new PathMeasure(mDialOutsidePath, true);

        //时针刻度围成圆的路径
        Path mDialHourPath = new Path();
        mDialHourPath.addCircle(mDialCenterX, mDialCenterY, mDialCircleHourRadius, Path.Direction.CW);//长刻度,时针刻度
        PathMeasure pathHourMeasure = new PathMeasure(mDialHourPath, true);

        //分针刻度围成圆的路径
        Path mDialMinutePath = new Path();
        mDialMinutePath.addCircle(mDialCenterX, mDialCenterY, mDialCircleMinuteRadius, Path.Direction.CW);//短刻度,分针刻度
        PathMeasure pathMinuteMeasure = new PathMeasure(mDialMinutePath, true);
        float lengthOutSide = pathOutSideMeasure.getLength();
        float lengthShortSide = pathMinuteMeasure.getLength();
        float lengthLongSide = pathHourMeasure.getLength();
        float posOutSide[] = new float[2];
        float posShortSide[] = new float[2];
        float posLongSide[] = new float[2];
        //测量出刻度盘各个刻度的位置
        mDialPath.clear();

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
    }

    /**
     * 时针对应的标注
     */
    public void initDialTextPathList() {
        mDialHourData.clear();
        for (int i = 0; i < 12; i++) {
            mDialHourData.add((i + 1) + " ");
        }
        //刻度对应数字的围成圆的路径
        Path mDialHourTextPath = new Path();
        mDialHourTextPath.addCircle(mDialCenterX, mDialCenterY, mDialCircleHourTextRaidus, Path.Direction.CW);//短刻度,分针刻度
        PathMeasure pathHourTextMeasure = new PathMeasure(mDialHourTextPath, true);
        float lengthHourTextSide = pathHourTextMeasure.getLength();
        float posHourTextSide[] = new float[2];
        Paint.FontMetricsInt fontMetrics = mPaintText.getFontMetricsInt();
        Rect rectF = new Rect();
        //测量出刻度对应数字的位置
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

}
