package com.littlezheng.ultrasound.ultrasound.view.strategy.decorator;

import android.content.Context;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound.ultrasound.view.strategy.DisplayStrategy;

/**
 * Created by Administrator on 2017/8/25/025.
 */

public abstract class AbstractDisplayStrategyDecorator implements DisplayStrategy{

    protected Context mContext;
    protected DisplayStrategy displayStrategy;

    public AbstractDisplayStrategyDecorator(Context context, DisplayStrategy displayStrategy){
        mContext = context;
        this.displayStrategy = displayStrategy;
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        displayStrategy.onGLDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return displayStrategy.onTouchEvent(event);
    }

}
