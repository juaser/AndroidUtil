package com.plugin.weight.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @description： 做一个只展示7天的数据折线图 有红绿两条线
 * @author：zxl
 * @CreateTime 2016/8/4.
 */
public class ChartView extends View {
    private int color_text_normal = Color.parseColor("#a5a5a5");//绘制文字的默认颜色
    private int color_text_select = Color.parseColor("#52cf90");//绘制文字的选中颜色
    private int color_normal_green = Color.parseColor("#beedd5");//绿色线的默认颜色
    private int color_select_green = Color.parseColor("#29c475");//绿色线的默认颜色
    private int color_normal_red = Color.parseColor("#ffb2b2");//红色线的默认颜色
    private int color_select_red = Color.parseColor("#ff0000");//红色线的默认颜色
    private int color_pic_bg = Color.parseColor("#ffffff");//选中部分的默认背景
    private int color_pic_border = Color.parseColor("#29c475");//选中部分的边框颜色
    private int color_bottom_bg = Color.parseColor("#24B069");//最底部的边框颜色
    private int view_width, view_height, view_step;//整个布局的宽高，把分成point_num*2个的部分宽度
    private int view_toptext_height = 100;//顶部用来放日期的高度
    private int view_bottomtext_height = 100;//底部用来放星期的高度
    private int view_bottom_green = 40;//最底部的边框高度
    private int point_num = 7;//总共有几个点
    private float circle_radius = 5;//点的中心画圆形的半径
    private int textsize = 32;//字体的大小
    private int strokewidth = 5;//字体的宽度
    private Paint paint_line, paint_pic;//划线的画笔
    private List<String> chart_top, chart_bottom;//存储日期和星期的集合
    private List<CharPoint> chart_point_green, chart_point_red, text_point_top, text_point_bottom;//存储红绿线条的点的集合，日期和星期点的集合
    private List<Integer> data_greeen, data_red;//存储红绿点的数值
    private List<RectF> part_rectf;//选中边框的集合
    private boolean isMeasured = false;//是否已经测量
    private int select_part = 6;//当前选中的部分
    private int green_max = 100;//绿色线条的最大值
    private int red_max = 100;//红色线条的最大值
    private int green_min = 0;//绿色线条的最小值
    private int red_min = 0;//红色线条的最小值
    private int select_border_padding = 10;//选中边框的外边距
    private Context context;
    private RectF rectf_bottom;//底部边框

    public ChartView(Context context) {
        this(context, null, 0);
    }

    public ChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void init() {
        data_greeen = new ArrayList<>();
        data_red = new ArrayList<>();
        part_rectf = new ArrayList<>();
        chart_top = new ArrayList<>();
        chart_bottom = new ArrayList<>();
        chart_point_green = new ArrayList<>();
        chart_point_red = new ArrayList<>();
        text_point_bottom = new ArrayList<>();
        text_point_top = new ArrayList<>();
        paint_line = new Paint();
        paint_pic = new Paint();
        for (int i = 0; i < point_num; i++) {
            int green = (int) (Math.random() * 70) + 20;
            int red = (int) (Math.random() * 70) + 20;
            data_greeen.add(green);
            data_red.add(red);
            chart_top.add(i + 1 + "");
            chart_bottom.add(getWeedDay(i));
        }
        initPaint(paint_line, color_normal_green, Paint.Style.FILL);
        initPaint(paint_pic, color_pic_bg, Paint.Style.FILL);
        rectf_bottom = new RectF();
    }

    /**
     * 初始化画笔
     */
    public void initPaint(Paint paint, int color, Paint.Style style) {
        paint.setColor(color);
        paint.setStrokeWidth(strokewidth);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTextSize(textsize);
        paint.setStyle(style);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        MeasurePoint();//测量point
        drawRect(canvas);//绘制底部绿色部分，以及选中模块的边框，白色
        drawText(canvas);//绘制日期，星期
        drawLine(canvas, chart_point_green, paint_line, color_select_green, color_normal_green);// 绘制绿色折叠线
        drawLine(canvas, chart_point_red, paint_line, color_select_red, color_normal_red); //绘制红色折叠线
        drawBorderRect(canvas);//绘制选中部分的边框颜色
    }

    /**
     * 测量各个部分的点
     */
    public void MeasurePoint() {
        if (isMeasured) {
            return;
        }
        isMeasured = true;
        view_width = getMeasuredWidth();
        view_height = getMeasuredHeight();
        view_step = view_width / (point_num * 2);
        Log.e("TAG", "view_width==" + view_width + "----view_height==" + view_height + "----view_step==" + view_step);
        initTextPoint();//日期和时间的点的集合
        initPointCenter(data_greeen, 1);//绿色的点的集合
        initPointCenter(data_red, 2);//红色的点的集合
        initRectf();// 绘制底部绿色边框大小 点击白色边框的大小
    }

    /**
     * 绘制折线图的点
     *
     * @param data
     * @param type
     */
    public void initPointCenter(List<Integer> data, int type) {
        Log.e("TAG", "" + data.toString());
        if (data.size() == point_num) {
            List<CharPoint> charPoints = new ArrayList<>();
            for (int i = 0; i < point_num; i++) {
                CharPoint point = new CharPoint();
                point.point_left_x = view_step * (2 * i);
                point.point_left_y = view_height - (view_bottom_green + view_bottomtext_height);
                point.point_center_x = view_step * (2 * i + 1);
                float view_total = view_height - (view_bottom_green + view_bottomtext_height + view_toptext_height);
                if (type == 1) {
                    point.point_center_y = view_height - (view_bottom_green + view_bottomtext_height) - view_total * (data.get(i) - green_min) / (green_max - green_min);
                } else {
                    point.point_center_y = view_height - (view_bottom_green + view_bottomtext_height) - view_total * (data.get(i) - red_min) / (red_max - red_min);
                }

                if (i == select_part) {
                    point.clicked = true;
                }
                charPoints.add(point);
            }
            for (int i = 1; i < point_num; i++) {
                charPoints.get(i).point_left_y = (charPoints.get(i - 1).point_center_y + charPoints.get(i).point_center_y) / 2;
                Log.e("TAG", "第" + i + "个点" + charPoints.get(i).toString());
            }
            if (type == 1) {
                chart_point_green.clear();
                chart_point_green.addAll(charPoints);
            } else {
                chart_point_red.clear();
                chart_point_red.addAll(charPoints);
            }
        }
    }

    /**
     * 绘制折线图上方和下方文字的坐标
     */
    public void initTextPoint() {
        Paint.FontMetricsInt fontMetrics = paint_line.getFontMetricsInt();
        RectF text_top = new RectF(0, 0, view_width, view_toptext_height);
        RectF text_bottom = new RectF(0, view_height - view_bottom_green - view_toptext_height, view_width, view_height - view_bottom_green);
        float baseline_top = (text_top.bottom + text_top.top - fontMetrics.bottom - fontMetrics.top) / 2;
        float baseline_bottom = (text_bottom.bottom + text_bottom.top - fontMetrics.bottom - fontMetrics.top) / 2;
        text_point_bottom.clear();
        text_point_top.clear();
        for (int i = 0; i < point_num; i++) {
            text_point_top.add(new CharPoint(view_step * (2 * i + 1), baseline_top));
            text_point_bottom.add(new CharPoint(view_step * (2 * i + 1), baseline_bottom));
        }
    }

    /**
     * 绘制底部绿色边框大小
     * 点击白色边框的大小
     */
    public void initRectf() {
        rectf_bottom = new RectF();
        rectf_bottom.left = 0;
        rectf_bottom.top = view_height - view_bottom_green;
        rectf_bottom.right = view_width;
        rectf_bottom.bottom = view_height;
        part_rectf.clear();
        for (int i = 0; i < point_num; i++) {
            RectF rectF = new RectF();
            rectF.left = 2 * i * view_step;
            rectF.top = select_border_padding;
            rectF.right = 2 * (i + 1) * view_step;
            rectF.bottom = view_height - select_border_padding;
            part_rectf.add(rectF);
        }
    }

    /**
     * 绘制折叠线
     */
    public void drawLine(Canvas canvas, List<CharPoint> charPoints, Paint paint, int color_select, int color_normal) {
        for (int i = 0; i < point_num; i++) {
            CharPoint start = charPoints.get(i);
            if (start.clicked) {
                paint.setColor(color_select);
            } else {
                paint.setColor(color_normal);
            }
            if (i + 1 < point_num) {
                CharPoint end = charPoints.get(i + 1);
                if (i == 0) {
                    canvas.drawCircle(start.point_center_x, start.point_center_y, circle_radius, paint);
                    canvas.drawLine(start.point_center_x, start.point_center_y, end.point_left_x, end.point_left_y, paint);
                } else {
                    canvas.drawLine(start.point_left_x, start.point_left_y, start.point_center_x, start.point_center_y, paint);
                    canvas.drawLine(start.point_center_x, start.point_center_y, end.point_left_x, end.point_left_y, paint);
                }
            } else {
                canvas.drawLine(start.point_left_x, start.point_left_y, start.point_center_x, start.point_center_y, paint);
                canvas.drawCircle(start.point_center_x, start.point_center_y, circle_radius, paint);
            }
        }
    }

    /**
     * 绘制日期，星期
     */
    public void drawText(Canvas canvas) {
        for (int i = 0; i < point_num; i++) {
            if (i == select_part) {
                paint_line.setColor(color_text_select);
            } else {
                paint_line.setColor(color_text_normal);
            }
            canvas.drawText(chart_top.get(i), text_point_top.get(i).point_center_x, text_point_top.get(i).point_center_y, paint_line);
            canvas.drawText(chart_bottom.get(i), text_point_bottom.get(i).point_center_x, text_point_bottom.get(i).point_center_y, paint_line);
        }
    }

    /**
     * 绘制底部绿色部分，以及选中模块的边框，白色
     */
    public void drawRect(Canvas canvas) {
        paint_line.setColor(color_bottom_bg);
        canvas.drawRect(rectf_bottom, paint_line);
        paint_pic.setColor(color_pic_bg);
        paint_pic.setStyle(Paint.Style.FILL);
        canvas.drawRect(part_rectf.get(select_part), paint_pic);
    }

    /**
     * 绘制选中部分的边框颜色
     */
    public void drawBorderRect(Canvas canvas) {
        paint_pic.setColor(color_pic_border);
        paint_pic.setStyle(Paint.Style.STROKE);
        paint_pic.setStrokeWidth(1);
        canvas.drawRect(part_rectf.get(select_part), paint_pic);
    }

    /**
     * view 监听事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            int part = (int) (x / (view_width / point_num));
            Log.e("TAG", "part==" + part);
            if (part != select_part) {
                if (onClickPartListerner != null) {
                    onClickPartListerner.click(part);
                }
                select_part = part;
                for (int i = 0; i < point_num; i++) {
                    if (i == select_part) {
                        chart_point_green.get(i).clicked = true;
                        chart_point_red.get(i).clicked = true;
                    } else {
                        chart_point_green.get(i).clicked = false;
                        chart_point_red.get(i).clicked = false;
                    }
                }
                invalidate();
            }
        }
        return super.onTouchEvent(event);
    }

    public class CharPoint {
        public float point_left_x;
        public float point_left_y;
        public float point_center_x;
        public float point_center_y;
        public boolean clicked = false;

        public CharPoint(float point_center_x, float point_center_y) {
            this.point_center_x = point_center_x;
            this.point_center_y = point_center_y;
        }

        public CharPoint() {

        }

        @Override
        public String toString() {
            return "CharPoint{" +
                    "point_left_x=" + point_left_x +
                    ", point_left_y=" + point_left_y +
                    ", point_center_x=" + point_center_x +
                    ", point_center_y=" + point_center_y +
                    ", clicked=" + clicked +
                    '}';
        }
    }

    public interface OnClickPartListerner {
        void click(int position);
    }

    private OnClickPartListerner onClickPartListerner = null;

    public void setOnClickPartListerner(OnClickPartListerner onClickPartListerner) {
        this.onClickPartListerner = onClickPartListerner;
    }

    public String getWeedDay(int day) {
        String weekday = "";
        switch (day) {
            case 0:
                weekday = "日";
                break;
            case 1:
                weekday = "一";
                break;
            case 2:
                weekday = "二";
                break;
            case 3:
                weekday = "三";
                break;
            case 4:
                weekday = "四";
                break;
            case 5:
                weekday = "五";
                break;
            case 6:
                weekday = "六";
                break;
        }
        return weekday;
    }
}
