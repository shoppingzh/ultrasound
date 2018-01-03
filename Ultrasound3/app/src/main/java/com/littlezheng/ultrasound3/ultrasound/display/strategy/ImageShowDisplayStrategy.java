package com.littlezheng.ultrasound3.ultrasound.display.strategy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound3.ultrasound.util.BitmapUtils;

import java.io.File;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public class ImageShowDisplayStrategy extends RefreshableDisplayStrategy {

    //创建一个默认图片
    private File imageFile;
    private Bitmap image = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8);
    private GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Uri photoURI = FileProvider.getUriForFile(mContext, "com.littlezheng.app.fileprovider", imageFile);

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setAction(android.content.Intent.ACTION_VIEW);
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
        super.onGLDraw(canvas);
        canvas.drawBitmap(image, 0, 0);
    }

    @Override
    protected void onRefresh(ICanvasGL canvas) {
        canvas.invalidateTextureContent(image);
    }

//    /**
//     * 改变当前显示图片
//     * @param newImage
//     */
//    public void changeImage(Bitmap newImage){
//        BitmapUtils.recycle(image);
//        image = newImage;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    public void changeImage(File imageFile) {
        this.imageFile = imageFile;
        BitmapUtils.recycle(image);
        image = BitmapFactory.decodeFile(imageFile.toString());
        setRefresh();
    }

}
