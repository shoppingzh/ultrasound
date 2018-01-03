package com.littlezheng.ultrasound4.framework.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLException;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.glview.GLContinuousView;

import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/9/20/020.
 */

public abstract class GLCaptureScreenView extends GLContinuousView {

    protected int width;    //窗口宽度
    protected int height;   //窗口高度
    protected DisplayStrategy strategy;     //显示策略
    private boolean captureScreen;  //是否捕捉屏幕
    private int interval;   //绘图时间间隔

    public GLCaptureScreenView(final Context context) {
        super(context);
        setKeepScreenOn(true);
        strategy = new NullDisplayStrategy();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        this.width = width;
        this.height = height;
        strategy.onSurfaceChanged(width, height);
    }

    @Override
    protected void onGLDraw(ICanvasGL canvas) {
        strategy.onGLDraw(canvas);

        //如果截图的话
        if (captureScreen) {
            final Bitmap screen = createBitmapFromGLSurface(0, 0, width,
                    height, gl);
            captureScreen = false;
            onCaptureScreen(screen);
        }

        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 捕捉到屏幕图像的回调方法，实现该方法决定捕捉到的屏幕图像的去处
     *
     * @param screenBitmap
     */
    protected abstract void onCaptureScreen(Bitmap screenBitmap);

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return strategy.onTouchEvent(event);
    }

    /**
     * 开始捕捉屏幕图像，捕捉过程不会马上进行，而是在下一帧图像被绘制时进行
     */
    public void startCaptureScreen() {
        captureScreen = true;
    }

    /**
     * 设置绘图间隔
     *
     * @param interval
     */
    public void setInterval(int interval) {
        if (interval < 0) {
            return;
        }
        this.interval = interval;
    }

    public DisplayStrategy getDisplayStrategy() {
        return strategy;
    }

    public void setDisplayStrategy(DisplayStrategy newStrategy) {
        //由于新的显示策略可能要使用到窗口的尺寸属性
        //在切换新的显示策略前要先将窗口的尺寸告知给新显示策略
        newStrategy.onSurfaceChanged(width, height);
        strategy = newStrategy;
    }

    /**
     * 从当前GLSurface中创建位图，即截取当前视图的图片
     *
     * @param x  截取x坐标
     * @param y  截取y坐标
     * @param w  截取高度
     * @param h  截取宽度
     * @param gl
     * @return
     */
    private Bitmap createBitmapFromGLSurface(int x, int y, int w, int h, GL10 gl) {
        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);
        try {
            gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE,
                    intBuffer);
            int offset1, offset2;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            return null;
        }
        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }

    /**
     * 屏幕图像捕捉监听器
     */
    @Deprecated
    public interface OnCaptureScreenListener {
        /**
         * 当捕捉到屏幕时回调该方法
         * 该方法运行在绘图线程中，非主线程！
         * 如果处理该图像时间过长，请考虑开启新的处理线程（建议一律开启新线程处理），以免影响绘图
         *
         * @param screenBitmap
         */
        void onCaptureScreen(Bitmap screenBitmap);
    }

}
