package com.zxl.androidtools.ui.customviews;

import android.support.design.widget.Snackbar;
import android.widget.Button;

import com.plugin.weight.draw.DrawingBoardView;
import com.zxl.androidtools.R;
import com.zxl.androidtools.base.BaseMvpActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description
 * @Created by zxl on 30/9/16.
 */

public class DrawBoardActivity extends BaseMvpActivity {
    @Bind(R.id.btn_save)
    Button btnSave;
    @Bind(R.id.btn_clear)
    Button btnClear;
    @Bind(R.id.drawingBoard)
    DrawingBoardView drawingBoard;

    @Override
    public int getLayoutId() {
        return R.layout.activity_drawboard;
    }

    @Override
    public void initView() {

    }

    @OnClick(R.id.btn_save)
    void save() {
        if (!drawingBoard.getTouched()) {
            Snackbar.make(drawingBoard, "还没有签名", Snackbar.LENGTH_SHORT).show();
            return;
        }
        String path = drawingBoard.save(true, false, 0);
        Snackbar.make(drawingBoard, "保存在\n" + path, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_clear)
    void clear() {
        drawingBoard.clear();
    }
}
