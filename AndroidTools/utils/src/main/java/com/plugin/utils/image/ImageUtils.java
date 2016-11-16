package com.plugin.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.util.Log;

import com.plugin.utils.PathUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @Description: 图片处理的工具类
 * @Author: zxl
 * @Date: 2016/11/16 11:11
 */

public class ImageUtils {
    private static volatile ImageUtils mInstance = null;

    private ImageUtils() {
    }

    public static ImageUtils getInstance() {
        ImageUtils instance = mInstance;
        if (instance == null) {
            synchronized (ImageUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new ImageUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }


    /**
     * 根据路径获得图片并压缩，返回bitmap用于显示
     *
     * @param filePath
     * @return
     */
    public Bitmap getSmallBitmapFromFile(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 根据路径获得图片并压缩，返回bitmap用于显示
     *
     * @param resourseId
     * @return
     */
    public Bitmap getSmallBitmapFromResourse(Context context, int resourseId) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resourseId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 100, 100);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(context.getResources(), resourseId, options);
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /***
     * 读取图片拍摄角度
     *
     * @param path
     * @return
     */
    public int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片角度
     *
     * @param bitmap
     * @param degress
     * @return
     */
    public Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    /**
     * 放大或缩小图片
     *
     * @param bitmap 源图片
     * @param ratio  放大或缩小的倍数，大于1表示放大，小于1表示缩小
     * @return Bitmap
     */
    public Bitmap zoomBitmap(Bitmap bitmap, float ratio) {
        if (ratio < 0f) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(ratio, ratio);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 创建一个带文字的bitmap
     *
     * @param width
     * @param height
     * @return
     */
    public Bitmap createSignBitmap(int width, int height) {
        String lin1 = "第一行";
        String lin2 = "第二行";
        String lin3 = "第三行";
        RectF Rect_line1, Rect_line2, Rect_line3;//三行文字的布局
        float baseline1, baseline2, baseline3;//每行的基准线
        Log.e("createSignBitmap--TAG", "width==" + width + "    height==" + height);
        int color_background = Color.WHITE;
        int text_height = height / 3;
        Bitmap waterBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(waterBitmap);
        canvas.drawColor(color_background);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(28);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);//实心
        paint.setTextAlign(Paint.Align.CENTER);

        //测量每一行的矩形区域
        Rect_line1 = new RectF();
        Rect_line1.left = 0;
        Rect_line1.top = 0;
        Rect_line1.right = width;
        Rect_line1.bottom = text_height;

        Rect_line2 = new RectF();
        Rect_line2.left = 0;
        Rect_line2.top = text_height;
        Rect_line2.right = width;
        Rect_line2.bottom = 2 * text_height;

        Rect_line3 = new RectF();
        Rect_line3.left = 0;
        Rect_line3.top = 2 * text_height;
        Rect_line3.right = width;
        Rect_line3.bottom = 3 * text_height;


        //测量每行的基准线，这样才能使字符串的位置垂直居中
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        baseline1 = (Rect_line1.bottom + Rect_line1.top - fontMetrics.bottom - fontMetrics.top) / 2;
        baseline2 = (Rect_line2.bottom + Rect_line2.top - fontMetrics.bottom - fontMetrics.top) / 2;
        baseline3 = (Rect_line3.bottom + Rect_line3.top - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText(lin1, width / 2, baseline1, paint);//字符串的中心(view_width / 2, baseline1)
        canvas.drawText(lin2, width / 2, baseline2, paint);
        canvas.drawText(lin3, width / 2, baseline3, paint);
        // 保存
        canvas.save(Canvas.ALL_SAVE_FLAG);
        // 存储
        canvas.restore();
        Log.e("createSignBitmap--TAG", "width==" + waterBitmap.getWidth() + "    height==" + waterBitmap.getHeight());
        return waterBitmap;
    }

    /**
     * 合成两张bitmap
     *
     * @param src         源文件Bitmap
     * @param watermark   水印Bitmap
     * @param paddingLeft 距离左边
     * @param paddingTop  距离上边
     * @return
     */
    public Bitmap createWaterMaskBitmap(Bitmap src, Bitmap watermark, int paddingLeft, int paddingTop) {
        if (src == null) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight() + watermark.getHeight();
        Log.e("TAG1", "src_width==" + width + "    src_height==" + height
                + "\nwater_width==" + watermark.getWidth() + "   water_height==" + watermark.getHeight());
        //创建一个bitmap
        Bitmap newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        //将该图片作为画布
        Canvas canvas = new Canvas(newb);
        //在画布 0，150坐标上开始绘制原始图片
        canvas.drawBitmap(src, 0, 150, null);
        //在画布上绘制水印图片
        canvas.drawBitmap(watermark, paddingLeft, paddingTop, null);
        // 保存
        canvas.save(Canvas.ALL_SAVE_FLAG);
        // 存储
        canvas.restore();
        return newb;
    }

    /**
     * 不做任何操作，直接存储到手机上
     *
     * @param bitmap
     * @param path
     */
    public boolean saveSimpleBitmap(Bitmap bitmap, String path) {
        if (!PathUtils.getInstance().isFileExits(path)) {
            return false;
        }
        File file = new File(path);
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
     * 根据bitmap压缩图片质量
     *
     * @param bitmap 未压缩的bitmap
     * @return 压缩后的bitmap
     */
    public Bitmap cQuality(Bitmap bitmap) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        int beginRate = 100;
        //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
        while (bOut.size() / 1024 / 1024 > 100) {  //如果压缩后大于100Kb，则提高压缩率，重新压缩
            beginRate -= 10;
            bOut.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, beginRate, bOut);
        }
        ByteArrayInputStream bInt = new ByteArrayInputStream(bOut.toByteArray());
        Bitmap newBitmap = BitmapFactory.decodeStream(bInt);
        if (newBitmap != null) {
            return newBitmap;
        } else {
            return bitmap;
        }
    }

    /**
     * 压缩图片
     *
     * @param bitmap   源图片
     * @param width    想要的宽度
     * @param height   想要的高度
     * @param isAdjust 是否自动调整尺寸, true图片就不会拉伸，false严格按照你的尺寸压缩
     * @return Bitmap
     */
    public Bitmap reduce(Bitmap bitmap, int width, int height, boolean isAdjust) {
        // 如果想要的宽度和高度都比源图片小，就不压缩了，直接返回原图
        if (bitmap.getWidth() < width && bitmap.getHeight() < height) {
            return bitmap;
        }
        // 根据想要的尺寸精确计算压缩比例, 方法详解：public BigDecimal divide(BigDecimal divisor, int scale, int roundingMode);
        // scale表示要保留的小数位, roundingMode表示如何处理多余的小数位，BigDecimal.ROUND_DOWN表示自动舍弃
        float sx = new BigDecimal(width).divide(new BigDecimal(bitmap.getWidth()), 4, BigDecimal.ROUND_DOWN).floatValue();
        float sy = new BigDecimal(height).divide(new BigDecimal(bitmap.getHeight()), 4, BigDecimal.ROUND_DOWN).floatValue();
        if (isAdjust) {// 如果想自动调整比例，不至于图片会拉伸
            sx = (sx < sy ? sx : sy);
            sy = sx;// 哪个比例小一点，就用哪个比例
        }
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy);// 调用api中的方法进行压缩，就大功告成了
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
