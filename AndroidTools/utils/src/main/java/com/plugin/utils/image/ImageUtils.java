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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
}
