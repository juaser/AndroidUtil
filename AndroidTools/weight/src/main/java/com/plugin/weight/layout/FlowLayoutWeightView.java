package com.plugin.weight.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/2/8 17:58
 */

public class FlowLayoutWeightView extends ViewGroup {
    private Context mContext;
    private int mWeight = 3;
    private int mWeightViewNum = 6;
    private int mWeightViewHeight = 80;
    private int mWeightViewWidth = 80;
    private int mLayoutPaddingTop = 0;
    private int mLayoutPaddingBottom = 0;
    private int mLineSpace = 10;
    private float mScale;
    private OnItemClickListerner onItemClickListerner;

    public FlowLayoutWeightView(Context context) {
        this(context, null, 0);
    }

    public FlowLayoutWeightView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayoutWeightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    public void init() {
        mScale = mContext.getResources().getDisplayMetrics().density;
        mWeightViewHeight = dp2Px(80);
        mWeightViewWidth = dp2Px(80);
        mLineSpace = dp2Px(10);
    }

    public int dp2Px(float dp) {
        return (int) (dp * mScale + 0.5f);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        checkEffective();
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int lineSpaceNum = mWeightViewNum / mWeight;
        int height = lineSpaceNum * mWeightViewHeight + (lineSpaceNum - 1) * mLineSpace + mLayoutPaddingTop + mLayoutPaddingBottom;
        eee("onMeasure(" + sizeWidth + "," + height + ")");
        setMeasuredDimension(sizeWidth, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        checkEffective();
        int width = getWidth() / mWeight;//每一行子View平均分配的宽度
        int lineHeight = 0;
        int cCount = getChildCount();
        int left = 0;
        int top = mLayoutPaddingTop;
        int lc = 0, tc = 0, rc = 0, bc = 0;//left,top,right,bottom
        for (int i = 0; i < cCount; i++) {
            final View child = getChildAt(i);
            setClickView(child, i);
            measureChild(child, 0, 0);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            vvv("child(" + childWidth + "," + childHeight + ")");
            if (i % mWeight == 0) {
                //每行的第一个
                left = 0;
                top += lineHeight;
                if (i != 0) {
                    top += mLineSpace;
                }
            }
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);

            lc = left + (width - childWidth) / 2;
            tc = top + lp.topMargin;
            rc = lc + childWidth;
            bc = tc + childHeight;

            child.layout(lc, tc, rc, bc);
            left += width;
        }
    }

    public void checkEffective() {
        if (mWeight > 0 && mWeightViewNum > 0) {
            return;
        }
        mWeight = 1;
        mWeightViewNum = 1;
    }

    public void setmWeight(int mWeight, int mNum) {
        this.mWeight = mWeight;
        this.mWeightViewNum = mNum;
        checkEffective();
        requestLayout();
    }

    public void addLayoutViews(List<View> views) {
        if (views == null || views.size() == 0) {
            return;
        }
        checkEffective();
        for (int i = 0; i < views.size(); i++) {
            if (i < mWeightViewNum) {
                addView(views.get(i), mWeightViewHeight, mWeightViewWidth);
            }
        }
    }

    public void setClickView(View view, final int position) {
        if (view == null) {
            return;
        }
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListerner != null) {
                    onItemClickListerner.click(position);
                }
            }
        });
    }

    public interface OnItemClickListerner {
        void click(int position);
    }

    public void setOnItemClickListerner(OnItemClickListerner onItemClickListerner) {
        this.onItemClickListerner = onItemClickListerner;
    }

    public void eee(String msg) {
        Log.e("TAG", msg);
    }

    public void vvv(String msg) {
        Log.v("TAG", msg);
    }
}
