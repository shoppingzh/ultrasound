package com.littlezheng.ultrasound4.ultrasound.display;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound4.framework.view.GLCaptureScreenView;
import com.littlezheng.ultrasound4.ultrasound.UContext;
import com.littlezheng.ultrasound4.ultrasound.display.draw.DrawCommand;
import com.littlezheng.ultrasound4.ultrasound.util.BitmapUtils;
import com.littlezheng.ultrasound4.ultrasound.util.DateUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/9/22/022.
 */

public class DisplayView extends GLCaptureScreenView {

    private static final String TAG = "DisplayView";

    private Context mContext;
    private List<DrawCommand> drawCommands = new ArrayList<>();

    public DisplayView(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        setInterval(60);
    }

    @Override
    protected void onGLDraw(ICanvasGL canvas) {
//        long start = System.currentTimeMillis();
        super.onGLDraw(canvas);

        for (DrawCommand drawCommand : drawCommands) {
            drawCommand.execute(canvas);
        }

//        Log.d(TAG, "绘图用时：" + (System.currentTimeMillis()-start) + "ms");
    }

    public void draw(DrawCommand command) {
        if (drawCommands.contains(command)) return;
        drawCommands.add(command);
    }

    public void cancel(DrawCommand command) {
        drawCommands.remove(command);
    }

    @Override
    protected void onCaptureScreen(Bitmap screenBitmap) {
        BitmapUtils.saveBitmap(screenBitmap, new File(Environment.getExternalStorageDirectory(),
                UContext.IMAGE_STORAGE_PATH + DateUtils.getDateStr()), System.currentTimeMillis() + ".jpg");
    }

    public int getWindowWidth() {
        return width;
    }

    public int getWindowHeight() {
        return height;
    }

}
