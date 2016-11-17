package com.plugin.weight.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.plugin.weight.R;

import java.util.Random;

/**
 * @Description: 制作一个可以颜色变化的心http://www.jianshu.com/p/9423ca99c303
 * @Author: zxl
 * @Date: 2016/11/16 17:34
 */

public class HeartImageView extends ImageView {
    Bitmap bitmap_heart;
    private Random random = new Random();

    public HeartImageView(Context context) {
        this(context, null);
    }

    public HeartImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bitmap_heart = BitmapFactory.decodeResource(getResources(), R.drawable.heart0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(bitmap_heart.getWidth() / 2, bitmap_heart.getHeight() / 2);
    }

    public void setColor(int color) {
        setImageBitmap(createColor(color));
    }

    public void setRandomColor() {
        setImageBitmap(createColor(randomColor(1)));
    }

    private Bitmap createColor(int color) {
        int heartWidth = bitmap_heart.getWidth();
        int heartHeight = bitmap_heart.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(heartWidth, heartHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(bitmap_heart, 0, 0, paint);
        canvas.drawColor(color, PorterDuff.Mode.SRC_ATOP);
        canvas.setBitmap(null);
        return newBitmap;
    }

    /**
     * 获得一个随机的颜色
     *
     * @param rate
     * @return
     */
    public int randomColor(int rate) {
        int red = random.nextInt(256) / rate, green = random.nextInt(256) / rate, blue = random.nextInt(256) / rate;
        return Color.rgb(red, green, blue);
    }
}
