package com.littlezheng.drawingboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.glview.GLContinuousView;
import com.chillingvan.canvasgl.glview.texture.GLContinuousTextureView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/8/17/017.
 */

public class DisplayWindow extends GLContinuousView {

    private static final String TAG = "DisplayWindow";

    private Context mContext;

    private int windowWidth;
    private int windowHeight;
    private Bitmap windowBmp;

    public DisplayWindow(Context context) {
        super(context);
        mContext = context;
        setKeepScreenOn(true);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
    }

    @Override
    protected void onGLDraw(ICanvasGL canvas) {


    }

}
