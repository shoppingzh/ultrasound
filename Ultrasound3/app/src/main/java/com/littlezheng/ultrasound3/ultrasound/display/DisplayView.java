package com.littlezheng.ultrasound3.ultrasound.display;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLException;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.glview.GLContinuousView;
import com.littlezheng.ultrasound3.ultrasound.UContext;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.DisplayStrategy;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.NonDisplayStrategy;
import com.littlezheng.ultrasound3.ultrasound.util.BitmapUtils;
import com.littlezheng.ultrasound3.ultrasound.util.DateUtils;

import java.io.File;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/8/29/029.
 */

public class DisplayView extends GLContinuousView {

    private static final String TAG = "DisplayView";

    //视图的宽和高
    private int width, height;
    private boolean isCaptureScreen;

    private DisplayStrategy strategy;

    public DisplayView(Context context) {
        super(context);
        setKeepScreenOn(true);
        strategy = new NonDisplayStrategy(context);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        this.width = width;
        this.height = height;
        Log.d(TAG,"视窗大小：["+width+","+height+"]");
        strategy.init(width, height);
    }

    @Override
    protected void onGLDraw(ICanvasGL canvas) {
//        long start = System.currentTimeMillis();
        strategy.onGLDraw(canvas);

        //如果截图的话
        if (isCaptureScreen) {
            final Bitmap bmp = createBitmapFromGLSurface(0, 0, width,
                    height, gl);
            isCaptureScreen = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File path = new File(Environment.getExternalStorageDirectory(),
                            UContext.IMAGE_STORAGE_PATH + "/" + DateUtils.getDateStr());
                    BitmapUtils.saveBitmap(bmp, path, System.currentTimeMillis()+".jpg");
                }
            }).start();
        }

        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        Log.d(TAG, "绘图用时："+(System.currentTimeMillis()-start)+"ms");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return strategy.onTouchEvent(event);
    }

    /**
     * 转换显示策略，该方法在调用前请先保证视图初始化完成，否则新的策略将无法使用到视图的宽高属性
     * @param newStrategy 新的策略
     */
    public void changeStrategy(DisplayStrategy newStrategy){
        //转换策略之前，将原有策略进行清理工作
        strategy.cleanup();
        newStrategy.init(width, height);
        strategy = newStrategy;
    }

    /**
     * 保存当前视窗内容为位图
     */
    public void saveCurrentBitmap(){
        isCaptureScreen = true;
    }

    public DisplayStrategy getStrategy() {
        return strategy;
    }

    /**
     * 从当前GLSurface中创建位图，即截取当前视图的图片
     * @param x 截取x坐标
     * @param y 截取y坐标
     * @param w 截取高度
     * @param h 截取宽度
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

}
