package com.plugin.weight.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.plugin.weight.R;

/**
 * @Description: 星星图片
 * @Author: zxl
 * @Date: 2016/11/21 16:02
 */

public class StarImageView extends ImageView {
    private Bitmap bitmap_source;
    private Context mContext;

    public StarImageView(Context context) {
        this(context, null, 0);
    }

    public StarImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    public void init() {
        bitmap_source = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_transparent_star);
    }


    public void setColor(int color) {
        setImageBitmap(createStarBitmap(color));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public Bitmap createStarBitmap(int color) {
        //创建一个装载star的bitmap
        Bitmap target = Bitmap.createBitmap(bitmap_source.getWidth(), bitmap_source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(bitmap_source, 0, 0, paint);
        canvas.drawColor(color, PorterDuff.Mode.SRC_ATOP);//源像素和目标像素相混合。取下层非交集部分与上层交集部分
        canvas.setBitmap(null);
        return target;
    }
}
