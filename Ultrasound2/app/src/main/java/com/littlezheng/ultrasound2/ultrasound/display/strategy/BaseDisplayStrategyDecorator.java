package com.littlezheng.ultrasound2.ultrasound.display.strategy;

import android.content.Context;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;

/**
 * Created by Administrator on 2017/8/29/029.
 */

public abstract class BaseDisplayStrategyDecorator extends BaseDisplayStrategy {

    protected DisplayStrategy strategy;

    public BaseDisplayStrategyDecorator(Context context, DisplayStrategy strategy) {
        super(context);
        this.strategy = strategy;
    }

    @Override
    public void init(int width, int height) {
        strategy.init(width, height);
        super.init(width, height);
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
