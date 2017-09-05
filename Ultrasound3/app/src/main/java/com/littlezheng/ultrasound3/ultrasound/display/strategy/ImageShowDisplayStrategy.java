package com.littlezheng.ultrasound3.ultrasound.display.strategy;

import android.content.Context;
import android.graphics.Bitmap;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound3.ultrasound.util.BitmapUtils;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public class ImageShowDisplayStrategy extends BaseDisplayStrategy {

    //创建一个默认图片
    private Bitmap image = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8);

    public ImageShowDisplayStrategy(Context context) {
        super(context);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        canvas.invalidateTextureContent(image);
        canvas.drawBitmap(image, 0, 0);
    }

    /**
     * 改变当前显示图片
     * @param newImage
     */
    public void changeImage(Bitmap newImage){
        BitmapUtils.recycle(image);
        image = newImage;
    }



}
