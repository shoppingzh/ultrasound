package com.littlezheng.ultrasound2.ultrasound.display.test;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.glcanvas.GLPaint;
import com.littlezheng.ultrasound2.ultrasound.display.strategy.BaseDisplayStrategy;

/**
 * Created by Administrator on 2017/8/29/029.
 */

public class OneStrategy extends BaseDisplayStrategy {

    private static final String TAG = "OneStrategy";
    GLPaint p = new GLPaint();

    public OneStrategy(Context context) {
        super(context);
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);
        p.setColor(Color.CYAN);
        p.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        canvas.drawRect(0, 0, 100, 100, p);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        Log.d(TAG,"one清理");
    }
}
