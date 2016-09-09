package com.zxl.androidtools.ui.animations;

import android.widget.TextView;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.utils.log.LogUtils;
import com.plugin.weight.explosion_particle.ExplosionField;
import com.zxl.androidtools.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 粒子爆炸效果
 * Created by zxl on 16-1-16.
 */
public class ExplosionParticleActivity extends BaseAppCompatActivity {
    @Bind(R.id.view_explosion_particle)
    TextView viewExplosionParticle;

    private ExplosionField mExplosionField;

    @Override
    public int getLayoutId() {
        return R.layout.activity_explosionparticle;
    }

    @Override
    public void initView() {
        mExplosionField = ExplosionField.attach2Window(this);
    }

    @OnClick(R.id.view_explosion_particle)
    void explosion() {
        LogUtils.e(getString(R.string.str_explosion));
        mExplosionField.explode(viewExplosionParticle);
    }


    @OnClick(R.id.view_explosion_recovery)
    void recover() {
        LogUtils.e(getString(R.string.str_recover));
        mExplosionField.clear();
        viewExplosionParticle.setScaleX(1);
        viewExplosionParticle.setScaleY(1);
        viewExplosionParticle.setAlpha(1);
    }
}
