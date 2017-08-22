package com.littlezheng.displaymodule.display.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.CanvasGL;
import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.displaymodule.display.strategy.AbsDisplayStrategy;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/8/21/021.
 */

public class BitmapLoadStrategy extends AbsDisplayStrategy {

    private static final String TAG = "BitmapLoadStrategy";

    private int width;
    private int height;

    private Bitmap bmp;
    private CanvasGL.BitmapMatrix m = new CanvasGL.BitmapMatrix();
    private boolean large = false;
    private GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d(TAG,"双击");
            final float x = e.getX();
            final float y = e.getY();
            if(!large){
                m.postScale(3f, 3f, x, y);
                large = true;
            }else{
                m.reset();
                m.postScale((float)width/bmp.getWidth(),(float)height/bmp.getHeight());
                large = false;
            }

            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

    });

    public BitmapLoadStrategy(Context context) {
        super(context);
    }

    public BitmapLoadStrategy(Context context,Bitmap bmp) {
        super(context);
        this.bmp = bmp;
    }

    public void init (int width, int height){
        m.reset();
        m.postScale((float)width/bmp.getWidth(),(float)height/bmp.getHeight());
        this.width = width;
        this.height = height;
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        if(bmp != null){
            canvas.drawBitmap(bmp,m);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    public void loadBitmap(Bitmap newBmp){
        if(bmp != null && !bmp.isRecycled()){
            bmp.recycle();
            bmp = null;
        }
        bmp = newBmp;
    }


}
