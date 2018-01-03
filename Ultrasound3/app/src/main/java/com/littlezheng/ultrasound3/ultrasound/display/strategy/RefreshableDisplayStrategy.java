package com.littlezheng.ultrasound3.ultrasound.display.strategy;

import android.content.Context;

import com.chillingvan.canvasgl.ICanvasGL;

/**
 * Created by Administrator on 2017/9/10/010.
 */

public abstract class RefreshableDisplayStrategy extends BaseDisplayStrategy {

    protected boolean refreshed;

    public RefreshableDisplayStrategy(Context context) {
        super(context);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        if (refreshed) {
            onRefresh(canvas);
            refreshed = false;
        }
    }

    /**
     * 设置刷新
     */
    public final void setRefresh() {
        refreshed = true;
    }

    protected abstract void onRefresh(ICanvasGL canvas);

}
