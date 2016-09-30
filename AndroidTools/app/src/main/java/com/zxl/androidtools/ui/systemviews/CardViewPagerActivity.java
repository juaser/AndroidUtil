package com.zxl.androidtools.ui.systemviews;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.View;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.utils.log.LogUtils;
import com.zxl.androidtools.R;
import com.zxl.androidtools.adapter.CardViewpagerAdapter;

import butterknife.Bind;

/**
 * @Description:
 * @Author: zxl
 * @Date: 18/9/16 PM4:23.
 */
public class CardViewPagerActivity extends BaseAppCompatActivity implements ViewPager.OnPageChangeListener, ViewPager.PageTransformer {
    @Bind(R.id.card_viewpager)
    ViewPager cardViewpager;

    private CardViewpagerAdapter adapter;

    private float mLastOffset;
    private boolean goingLeft = false;//向左滑动
    private int nowPagePosition;//当前页面
    private int targetPagePosition;//目标页面
    private float realOffset;

    @Override
    public int getLayoutId() {
        return R.layout.activity_card_viewpager;
    }

    @Override
    public void initView() {
        adapter = new CardViewpagerAdapter(this);
        cardViewpager.setAdapter(adapter);
        cardViewpager.setPageTransformer(false, this);
        cardViewpager.addOnPageChangeListener(this);
    }

    private float view_now_scale;
    private float view_target_scale;

    /**
     * @param position             向左滑的话，position就是当前的页面，如果不是的话 position就是要滑到的界面
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        boolean goingLeft = mLastOffset < positionOffset;//是否向左滑动，是指手指向左 页面的话是从0-->1
        float baseElevation = adapter.getBaseElevation();
        if (goingLeft) {
            nowPagePosition = position;
            targetPagePosition = position + 1;
            realOffset = positionOffset;
        } else {
            nowPagePosition = position + 1;
            targetPagePosition = position;
            realOffset = 1 - positionOffset;
        }
        view_now_scale = (float) (1 + 0.1 * (1 - realOffset));
        view_target_scale = (float) (1 + 0.1 * realOffset);

        if (targetPagePosition > adapter.getCount() - 1 || nowPagePosition > adapter.getCount() - 1) {
            return;
        }
        LogUtils.i((goingLeft ? "   向左滑动" : "   向右滑动")
                + "页面" + nowPagePosition + "---->页面" + targetPagePosition + "    滑动的距离==" + realOffset
                + "   当前页面缩放比例==" + view_now_scale + "  目标页面缩放比例==" + view_target_scale);
        CardView view_now = adapter.getCardViewAt(position);
        CardView view_target = adapter.getCardViewAt(targetPagePosition);

        if (view_now != null) {
            view_now.setScaleX(view_now_scale);
            view_now.setScaleY(view_now_scale);
            view_now.setCardElevation((baseElevation + baseElevation * (adapter.MAX_ELEVATION_FACTOR - 1) * (1 - realOffset)));
        }
        if (view_target != null) {
            view_target.setScaleX(view_target_scale);
            view_target.setScaleY(view_target_scale);
            view_target.setCardElevation((baseElevation + baseElevation * (adapter.MAX_ELEVATION_FACTOR - 1) * realOffset));
        }
        mLastOffset = positionOffset;
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void transformPage(View page, float position) {

    }
}
