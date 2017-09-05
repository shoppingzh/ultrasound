package com.littlezheng.ultrasound.ultrasound.view.strategy;

import android.content.Context;

import com.chillingvan.canvasgl.ICanvasGL;

/**
 * Created by Administrator on 2017/8/28/028.
 */

public class NonDisplayStrategy extends AbstractDisplayStrategy {

    public NonDisplayStrategy(Context context) {
        super(context);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {

    }

}
