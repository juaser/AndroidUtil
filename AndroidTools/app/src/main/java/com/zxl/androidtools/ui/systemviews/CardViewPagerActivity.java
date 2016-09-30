package com.zxl.androidtools.ui.systemviews;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.zxl.androidtools.R;
import com.zxl.androidtools.adapter.CardViewpagerAdapter;

import butterknife.Bind;

/**
 * @Description:
 * @Author: zxl
 * @Date: 18/9/16 PM4:23.
 */
public class CardViewPagerActivity extends BaseAppCompatActivity implements ViewPager.OnPageChangeListener{
    @Bind(R.id.card_viewpager)
    ViewPager cardViewpager;

    private CardViewpagerAdapter adapter;

    private float mLastOffset;

    @Override
    public int getLayoutId() {
        return R.layout.activity_card_viewpager;
    }

    @Override
    public void initView() {
        adapter = new CardViewpagerAdapter(this);
        cardViewpager.setAdapter(adapter);
        cardViewpager.addOnPageChangeListener(this);
    }

    /**
     * @param position             向左滑的话，position就是当前的页面，如果不是的话 position就是要滑到的界面
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int realCurrentPosition;
        int nextPosition;
        float baseElevation = adapter.getBaseElevation();
        float realOffset;
        boolean goingLeft = mLastOffset > positionOffset;

        // If we're going backwards, onPageScrolled receives the last position
        // instead of the current one
        if (goingLeft) {
            realCurrentPosition = position + 1;
            nextPosition = position;
            realOffset = 1 - positionOffset;
        } else {
            nextPosition = position + 1;
            realCurrentPosition = position;
            realOffset = positionOffset;
        }

        // Avoid crash on overscroll
        if (nextPosition > adapter.getCount() - 1
                || realCurrentPosition > adapter.getCount() - 1) {
            return;
        }

        CardView currentCard = adapter.getCardViewAt(realCurrentPosition);

        // This might be null if a fragment is being used
        // and the views weren't created yet
        if (currentCard != null) {
            if (true) {
                currentCard.setScaleX((float) (1 + 0.1 * (1 - realOffset)));
                currentCard.setScaleY((float) (1 + 0.1 * (1 - realOffset)));
            }
            currentCard.setCardElevation( (baseElevation + baseElevation
                    * (adapter.MAX_ELEVATION_FACTOR - 1) * (1 - realOffset)));
        }

        CardView nextCard = adapter.getCardViewAt(nextPosition);

        // We might be scrolling fast enough so that the next (or previous) card
        // was already destroyed or a fragment might not have been created yet
        if (nextCard != null) {
            if (true) {
                nextCard.setScaleX((float) (1 + 0.1 * (realOffset)));
                nextCard.setScaleY((float) (1 + 0.1 * (realOffset)));
            }
            nextCard.setCardElevation((baseElevation + baseElevation
                    * (adapter.MAX_ELEVATION_FACTOR - 1) * (realOffset)));
        }

        mLastOffset = positionOffset;
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
