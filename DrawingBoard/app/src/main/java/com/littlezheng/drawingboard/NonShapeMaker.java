package com.littlezheng.drawingboard;

import android.graphics.Canvas;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.drawingboard.api.ShapeMaker;

/**
 * Created by Administrator on 2017/8/23/023.
 */

public class NonShapeMaker extends ShapeMaker {

    public NonShapeMaker() {
        super(0);
    }

    @Override
    public void make(Canvas canvas) {
        // no op
    }

    @Override
    public void reset(float x, float y) {
        // no op
    }

}
