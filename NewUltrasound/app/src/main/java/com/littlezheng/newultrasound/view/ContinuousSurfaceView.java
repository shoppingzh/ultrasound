package com.littlezheng.newultrasound.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Administrator on 2017/11/3/003.
 */

public abstract class ContinuousSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final Paint CLEAR_PAINT = new Paint();

    static {
        CLEAR_PAINT.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    protected SurfaceHolder mHolder;
    private Thread drawThread;
    private boolean flag;

    public ContinuousSurfaceView(Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    /**
     * 该方法每16ms会回调一次，使用canvas对象进行的绘制操作都将反映到SurfaceView上
     *
     * @param canvas 画布，用于在SurfaceView上绘制
     */
    protected abstract void onSurfaceDraw(Canvas canvas);

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startDraw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopDraw();
    }

    //开启绘制线程，开始绘图
    private void startDraw() {
        drawThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    Canvas canvas = mHolder.lockCanvas();
                    if (canvas != null) {
                        clearCanvas(canvas);
                        onSurfaceDraw(canvas);
                        mHolder.unlockCanvasAndPost(canvas);
                    }
                    try {
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        drawThread.setDaemon(true);
        flag = true;
        drawThread.start();
    }

    //清除画布内容
    private void clearCanvas(Canvas canvas) {
        canvas.drawPaint(CLEAR_PAINT);
    }

    //停止绘图
    private void stopDraw() {
        flag = false;
        if (drawThread != null) {
            try {
                drawThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
