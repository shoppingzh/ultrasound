package com.littlezheng.ultrasound4.ultrasound.display;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound4.framework.view.AbstractDisplayStrategy;
import com.littlezheng.ultrasound4.ultrasound.util.BitmapUtils;

import java.io.File;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public class ImageShowDisplayStrategy extends AbstractDisplayStrategy {

    //创建一个默认图片
    private File imageFile;
    private Bitmap image = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8);
    private GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Uri photoURI = FileProvider.getUriForFile(mContext, "com.littlezheng.app.fileprovider", imageFile);

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(photoURI, "image/*");
            mContext.startActivity(intent);
            return super.onDoubleTap(e);
        }
    });

    public ImageShowDisplayStrategy(Context context) {
        super(context);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        if (image == null || image.isRecycled()) return;
        canvas.drawBitmap(image, 0, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    public void changeImage(File imageFile) {
        this.imageFile = imageFile;
        BitmapUtils.recycle(image);
        image = BitmapFactory.decodeFile(imageFile.toString());
    }

}
