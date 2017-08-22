package com.littlezheng.ultrasound.ultrasound.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLException;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.glview.GLContinuousView;
import com.littlezheng.ultrasound.ultrasound.Configuration;
import com.littlezheng.ultrasound.R;
import com.littlezheng.ultrasound.ultrasound.util.BitmapUtils;
import com.littlezheng.ultrasound.ultrasound.util.DateUtils;
import com.littlezheng.ultrasound.ultrasound.view.strategy.DisplayStrategy;

import java.io.File;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/8/21/021.
 */

public class DisplayView extends GLContinuousView {

    private static final String TAG = "DisplayView";
    private Context mContext;

    //绘制策略
    private DisplayStrategy displayStrategy;
    //窗口大小改变监听器
    private OnWindowSizeChangedListener onWindowSizeChangedListener;

    //是否截取当前画面
    private boolean isCaptureScreen;

    //窗口属性
    private int windowWidth;
    private int windowHeight;

    public DisplayView(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * 如果需要对该视图进行状态监听如：窗口创建、窗口大小改变等，请使用该构造方法
     * 该监听器可对需要使用到窗口宽度和高度的绘图策略提供窗口的宽高，以便绘制时确定元素绘制的位置等，如：
     * DisplayView view = new DisplayView(MainActivity.this, new OnWindowSizeChangedListener(){
     *      @Override
     *      public void onWindowSizeChanged(int width, int height) {
     *           //此处获取到宽高，可对绘制策略进行初始化工作
     *      }
     * });
     * @param context
     * @param onWindowSizeChangedListener
     */
    public DisplayView(Context context, OnWindowSizeChangedListener onWindowSizeChangedListener) {
        super(context);
        mContext = context;
        this.onWindowSizeChangedListener = onWindowSizeChangedListener;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        if(onWindowSizeChangedListener != null){
            onWindowSizeChangedListener.onWindowSizeChanged(width, height);
        }
        windowWidth = width;
        windowHeight = height;
    }

    @Override
    protected void onGLDraw(ICanvasGL canvas) {
//        long start = System.currentTimeMillis();
        displayStrategy.onGLDraw(canvas);

        //如果截图的话
        if (isCaptureScreen) {
            final Bitmap bmp = createBitmapFromGLSurface(0, 0, windowWidth,
                    windowHeight, gl);
            isCaptureScreen = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File path = new File(Environment.getExternalStorageDirectory(),
                            Configuration.SNAPSHOT_SAVE_FOLDER+"/"+ DateUtils.getDateStr());
                    boolean rst = BitmapUtils.saveBitmap(bmp, path, System.currentTimeMillis()+".jpg");
                    if(rst){
                        ((Activity)mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext,"保存成功，点击对应日期查看！",Toast.LENGTH_SHORT).show();
                                Button preview = (Button) ((Activity) mContext).findViewById(R.id.btn_preview);
                                preview.callOnClick();
                            }
                        });
                    }
                }
            }).start();
        }


        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        Log.d(TAG,"绘图用时："+(System.currentTimeMillis()-start)+"ms");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return displayStrategy.onTouchEvent(event);
    }

    public void setDisplayStrategy(DisplayStrategy displayStrategy) {
        this.displayStrategy = displayStrategy;
    }

    public DisplayStrategy getDisplayStrategy() {
        return displayStrategy;
    }

    /**
     * 截取屏幕
     */
    public void captureScreen(){
        isCaptureScreen = true;
    }

    public void setOnWindowSizeChangedListener(OnWindowSizeChangedListener onWindowSizeChangedListener) {
        this.onWindowSizeChangedListener = onWindowSizeChangedListener;
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

    /**
     * 窗口尺寸改变监听器
     */
    public interface OnWindowSizeChangedListener{

        void onWindowSizeChanged(int width, int height);

    }

}
