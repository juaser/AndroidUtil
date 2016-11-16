package com.zxl.androidtools.ui.animations;

import android.graphics.drawable.AnimationDrawable;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.utils.log.LogUtils;
import com.zxl.androidtools.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description:
 * @Author: zxl
 * @Date: 9/9/16 PM12:15.
 */
public class BaseAnimationActivity extends BaseAppCompatActivity {
    @Bind(R.id.tv_view_alpha)
    TextView tvViewAlpha;
    @Bind(R.id.tv_view_rotate)
    TextView tvViewRotate;
    @Bind(R.id.tv_view_scale)
    TextView tvViewScale;
    @Bind(R.id.tv_view_translate)
    TextView tvViewTranslate;
    @Bind(R.id.check_alpha)
    CheckBox checkAlpha;
    @Bind(R.id.check_rotate)
    CheckBox checkRotate;
    @Bind(R.id.check_scale)
    CheckBox checkScale;
    @Bind(R.id.check_translate)
    CheckBox checkTranslate;
    @Bind(R.id.tv_drawablenimation)
    ImageView tvDrawablenimation;
    @Bind(R.id.tv_drawablenimation_start)
    TextView tvDrawablenimationStart;
    @Bind(R.id.tv_drawablenimation_stop)
    TextView tvDrawablenimationStop;

    private AnimationDrawable frameAnim;

    @Override
    public int getLayoutId() {
        return R.layout.activity_baseanimation;
    }

    /**
     * AnimationDrawable 就是用来控制这个帧动画，这个类中提供了很多方法。
     * animationDrawable.start(); 开始这个动画
     * animationDrawable.stop(); 结束这个动画
     * animationDrawable.setAlpha(100);设置动画的透明度, 取值范围(0 - 255)
     * animationDrawable.setOneShot(true); 设置单次播放
     * animationDrawable.setOneShot(false); 设置循环播放
     * animationDrawable.isRunning(); 判断动画是否正在播放
     * animationDrawable.getNumberOfFrames(); 得到动画的帧数。
     */
    @Override
    public void initView() {
//        //如果当前的I妈个View没有设置src
//        frameAnim = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.drawable_animation_sapi);
//        tvDrawablenimation.setBackgroundDrawable(frameAnim);
        frameAnim = (AnimationDrawable) tvDrawablenimation.getDrawable();
        frameAnim.start();
    }

    @OnClick(R.id.tv_view_alpha)
    void alpha() {
        LogUtils.e(getString(R.string.str_viewanimation_alpha));
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillBefore(true);//动画执行完后是否停留在执行完的状态
        alphaAnimation.setRepeatMode(checkAlpha.isChecked() ? Animation.REVERSE : Animation.RESTART);
        alphaAnimation.setRepeatCount(3);
        tvViewAlpha.startAnimation(alphaAnimation);
    }

    /**
     * RotateAnimation (float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
     * float fromDegrees：旋转的开始角度。
     * float toDegrees：旋转的结束角度。
     * int pivotXType：X轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
     * float pivotXValue：X坐标的伸缩值。
     * int pivotYType：Y轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
     * float pivotYValue：Y坐标的伸缩值。
     * <p/>
     * Animation.ABSOLUTE：具体的坐标值，指绝对的屏幕像素单位
     * Animation.RELATIVE_TO_SELF：相对自己的坐标值，0.1f是指自己的坐标值乘以0.1
     * Animation.RELATIVE_TO_PARENT：相对父容器的坐标值，0.1f是指父容器的坐标值乘以0.1
     */
    @OnClick(R.id.tv_view_rotate)
    void rotate() {
        LogUtils.e(getString(R.string.str_viewanimation_rotate));
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, Animation.ABSOLUTE, 0.5f, Animation.ABSOLUTE, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        rotateAnimation.setRepeatMode(checkRotate.isChecked() ? Animation.REVERSE : Animation.RESTART);
        rotateAnimation.setRepeatCount(3);
        tvViewRotate.startAnimation(rotateAnimation);
    }

    /**
     * ScaleAnimation(float fromX, float toX, float fromY, float toY,int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
     * float fromX 动画起始时 X坐标上的伸缩尺寸
     * float toX 动画结束时 X坐标上的伸缩尺寸
     * float fromY 动画起始时Y坐标上的伸缩尺寸
     * float toY 动画结束时Y坐标上的伸缩尺寸
     * int pivotXType 动画在X轴相对于物件位置类型
     * float pivotXValue 动画相对于物件的X坐标的开始位置
     * int pivotYType 动画在Y轴相对于物件位置类型
     * float pivotYValue 动画相对于物件的Y坐标的开始位置
     */
    @OnClick(R.id.tv_view_scale)
    void scale() {
        LogUtils.e(getString(R.string.str_viewanimation_scale));
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1.4f, 0f, 1.4f, Animation.ABSOLUTE, 0.5f, Animation.ABSOLUTE, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillBefore(true);
        scaleAnimation.setRepeatCount(3);
        scaleAnimation.setRepeatMode(checkScale.isChecked() ? Animation.REVERSE : Animation.RESTART);//选中设置反方向执行 设置动作重复的模式
        tvViewScale.startAnimation(scaleAnimation);
    }

    /**
     * float fromXDelta 动画开始的点离当前View X坐标上的差值
     * float toXDelta 动画结束的点离当前View X坐标上的差值
     * float fromYDelta 动画开始的点离当前View Y坐标上的差值
     * float toYDelta 动画开始的点离当前View Y坐标上的差值
     */
    @OnClick(R.id.tv_view_translate)
    void translate() {
        LogUtils.e(getString(R.string.str_viewanimation_translate));
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 150f, 0f, 0f);
        translateAnimation.setDuration(1000);
        translateAnimation.setFillBefore(true);
        translateAnimation.setRepeatCount(3);
        translateAnimation.setRepeatMode(checkTranslate.isChecked() ? Animation.REVERSE : Animation.RESTART);//选中设置反方向执行 设置动作重复的模式
        tvViewTranslate.startAnimation(translateAnimation);
    }

    public void setViewWithXml() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_sample);//从res/anim中取出动画文件
    }

    /**
     * AnimationDrawable 就是用来控制这个帧动画，这个类中提供了很多方法。
     * animationDrawable.start(); 开始这个动画
     * animationDrawable.stop(); 结束这个动画
     * animationDrawable.setAlpha(100);设置动画的透明度, 取值范围(0 - 255)
     * animationDrawable.setOneShot(true); 设置单次播放
     * animationDrawable.setOneShot(false); 设置循环播放
     * animationDrawable.isRunning(); 判断动画是否正在播放
     * animationDrawable.getNumberOfFrames(); 得到动画的帧数。
     */
    @OnClick(R.id.tv_drawablenimation_start)
    void startDrawableAnimation() {
        if (frameAnim != null && !frameAnim.isRunning()) {
            frameAnim.start();
        }
    }

    @OnClick(R.id.tv_drawablenimation_stop)
    void stopDrawableAnimation() {
        if (frameAnim != null && frameAnim.isRunning()) {
            frameAnim.stop();
        }
    }
}
