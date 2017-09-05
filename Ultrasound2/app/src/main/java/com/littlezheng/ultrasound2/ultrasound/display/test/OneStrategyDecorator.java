package com.littlezheng.ultrasound2.ultrasound.display.test;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.glcanvas.GLPaint;
import com.littlezheng.ultrasound2.ultrasound.display.strategy.DisplayStrategy;
import com.littlezheng.ultrasound2.ultrasound.display.strategy.BaseDisplayStrategyDecorator;

/**
 * Created by Administrator on 2017/8/29/029.
 */

public class OneStrategyDecorator extends BaseDisplayStrategyDecorator {

    private static final String TAG = "OneStrategyDecorator";
    GLPaint p = new GLPaint();
    Rect rect = new Rect();

    public OneStrategyDecorator(Context context, DisplayStrategy strategy) {
        super(context, strategy);
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL);

        rect.set(width-100, height-100, width, height);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);

        canvas.drawRect(rect, p);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        Log.d(TAG,"oneDec清理");
    }
}
