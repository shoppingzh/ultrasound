package com.littlezheng.doubleclickzoom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.chillingvan.canvasgl.CanvasGL;
import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.glview.GLContinuousView;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/8/18/018.
 */

public class DisplayWindow extends GLContinuousView {

    public static final String TAG = "DisplayWindow";

    private Context mContext;

    private int windowWid,windowHei;
    private Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.bg);
    private CanvasGL.BitmapMatrix m2 = new CanvasGL.BitmapMatrix();
    private boolean large = false;

    public DisplayWindow(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            final float x = e.getX();
            final float y = e.getY();
            if(!large){
                m2.postScale(3f, 3f, x, y);
                large = true;
            }else{
                m2.reset();
                m2.postScale((float)windowWid/bmp.getWidth(),(float)windowHei/bmp.getHeight());
                large = false;
            }

            return super.onDoubleTap(e);
        }


        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

    });

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        windowWid = width;
        windowHei = height;
        m2.postScale((float)width/bmp.getWidth(),(float)height/bmp.getHeight());
    }

    @Override
    protected void onGLDraw(ICanvasGL canvas) {
        canvas.drawBitmap(bmp,m2);

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
