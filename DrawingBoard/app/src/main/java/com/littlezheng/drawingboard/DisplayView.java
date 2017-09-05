package com.littlezheng.drawingboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.glcanvas.GLPaint;
import com.chillingvan.canvasgl.glview.GLContinuousView;
import com.littlezheng.drawingboard.api.LineMaker;
import com.littlezheng.drawingboard.api.ShapeMaker;
import com.littlezheng.drawingboard.strategy.DisplayStrategy;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/8/17/017.
 */

public class DisplayView extends GLContinuousView {

    private static final String TAG = "DisplayWindow";

    private Context mContext;

    private DisplayStrategy strategy;
    private OnWindowSizeChangedListener onWindowSizeChangedListener;

    public DisplayView(Context context,DisplayStrategy strategy) {
        super(context);
        mContext = context;
        setKeepScreenOn(true);

        this.strategy = strategy;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        onWindowSizeChangedListener.onWindowSizeChanged(width, height);
    }


    @Override
    protected void onGLDraw(ICanvasGL canvas) {
//        long start = System.currentTimeMillis();
        strategy.onGLDraw(canvas);

        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        Log.d(TAG,"绘图用时："+(System.currentTimeMillis()-start)+"ms");
    }

    public void setStrategy(DisplayStrategy strategy) {
        this.strategy = strategy;
    }

    public void setOnWindowSizeChangedListener(OnWindowSizeChangedListener onWindowSizeChangedListener) {
        this.onWindowSizeChangedListener = onWindowSizeChangedListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return strategy.onTouchEvent(event);
    }

    public interface OnWindowSizeChangedListener{
        void onWindowSizeChanged(int width,int height);
    }

}
