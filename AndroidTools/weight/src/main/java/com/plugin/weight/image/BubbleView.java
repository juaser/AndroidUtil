package com.plugin.weight.image;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.plugin.utils.log.LogUtils;

/**
 * @Description: 制作一个可以不断冒出图片的布局
 * @Author: zxl
 * @Date: 2016/11/17 11:46
 */

public class BubbleView extends RelativeLayout {
    private Context mContext;
    private int width;//控件的宽
    private int height;//控件的高
    private int duration = 1000;//冒泡动画时间

    public BubbleView(Context context) {
        this(context, null, 0);
    }

    public BubbleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        LogUtils.e("控件宽度：" + width + "    高度：" + height);
    }

    public void init() {

    }

    public void addView() {
        //添加一个view 在布局的最底部
        final HeartImageView heartImageView = new HeartImageView(mContext);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        heartImageView.setRandomColor();
        addView(heartImageView, params);
        heartImageView.post(new Runnable() {
            @Override
            public void run() {
                ValueAnimator valueAnimator = ValueAnimator.ofObject(new BubbleEvaluator(), new PointF(0, 0), new PointF(100, 100));
                valueAnimator.setDuration(duration);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        //动画过程中的值
                        PointF pointF = (PointF) valueAnimator.getAnimatedValue();
                        heartImageView.setX(pointF.x);
                        heartImageView.setY(pointF.y);
                    }
                });
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    //监听动画结束的时候
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        removeView(heartImageView);
                    }
                });
                valueAnimator.start();
            }
        });
    }

    public void addView(View view) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        addView(view, params);
    }

    private class BubbleEvaluator implements TypeEvaluator<PointF> {
        /**
         * fraction是动画的进度，从0.0到1.0，根据进度，初始值和最终值，给出动画过程的中间值。
         *
         * @param v
         * @param start
         * @param end
         * @return
         */
        @Override
        public PointF evaluate(float v, PointF start, PointF end) {
            LogUtils.e("v==" + v + "   start.x==" + start.x + "   start.y==" + start.y + "   end.x==" + end.x + "  end.y==" + end.y);
            return new PointF(0, 0);
        }
    }

}
