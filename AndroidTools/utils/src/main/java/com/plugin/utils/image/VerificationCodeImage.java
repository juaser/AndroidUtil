package com.plugin.utils.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * @Description: 生成一个四位数的验证码图片
 * @Author: zxl
 * @Date: 2016/11/16 14:19
 */

public class VerificationCodeImage {
    private int width = 140, height = 40, codeLen = 4;
    private String checkCode = "";
    private Random random = new Random();
    private static volatile VerificationCodeImage mInstance = null;

    private VerificationCodeImage() {
    }

    public static VerificationCodeImage getInstance() {
        VerificationCodeImage instance = mInstance;
        if (instance == null) {
            synchronized (VerificationCodeImage.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new VerificationCodeImage();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    /**
     * 产生一个4位随机数字的图片验证码
     *
     * @return Bitmap
     */
    public Bitmap createCode() {
        checkCode = "";
        String[] chars = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        for (int i = 0; i < codeLen; i++) {
            checkCode += chars[random.nextInt(chars.length)];
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setTextSize(30);
        paint.setColor(Color.BLUE);
        for (int i = 0; i < checkCode.length(); i++) {
            paint.setColor(randomColor(1));
            paint.setFakeBoldText(random.nextBoolean());
            float skewX = random.nextInt(11) / 10;
            paint.setTextSkewX(random.nextBoolean() ? skewX : -skewX);
            int x = width / codeLen * i + random.nextInt(10);
            canvas.drawText(String.valueOf(checkCode.charAt(i)), x, 28, paint);
        }
        for (int i = 0; i < 3; i++) {
            drawLine(canvas, paint);
        }
        for (int i = 0; i < 255; i++) {
            drawPoints(canvas, paint);
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bitmap;
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

    /**
     * 画随机线条
     *
     * @param canvas
     * @param paint
     */
    public void drawLine(Canvas canvas, Paint paint) {
        int startX = random.nextInt(width), startY = random.nextInt(height);
        int stopX = random.nextInt(width), stopY = random.nextInt(height);
        paint.setStrokeWidth(1);
        paint.setColor(randomColor(1));
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    /**
     * 画随机干扰点
     *
     * @param canvas
     * @param paint
     */
    public void drawPoints(Canvas canvas, Paint paint) {
        int stopX = random.nextInt(width), stopY = random.nextInt(height);
        paint.setStrokeWidth(1);
        paint.setColor(randomColor(1));
        canvas.drawPoint(stopX, stopY, paint);
    }

    /**
     * 返回真实验证码字符串
     *
     * @return String
     */
    public String getCheckCode() {
        return checkCode;
    }


}
