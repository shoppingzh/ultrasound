package com.littlezheng.ultrasound4.framework.view;

import android.content.Context;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;

/**
 * Created by Administrator on 2017/9/20/020.
 */

public abstract class MultipleDisplayStrategy extends AbstractDisplayStrategy {

    protected DisplayStrategy strategy;

    public MultipleDisplayStrategy(Context context, DisplayStrategy strategy) {
        super(context);
        this.strategy = strategy;
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        strategy.onSurfaceChanged(width, height);
        super.onSurfaceChanged(width, height);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        strategy.onGLDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return strategy.onTouchEvent(event);
    }
}
