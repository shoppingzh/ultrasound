package com.littlezheng.newultrasound.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by Administrator on 2017/11/18/018.
 */

public class ImageBox extends Control{

    private Bitmap image;

    public ImageBox(Bitmap image){
        if(image == null){
            throw new IllegalArgumentException("显示图片不能为空！");
        }
        this.image = image;
    }

    @Override
    public void draw(Canvas c) {
        c.drawBitmap(image, x, y, paint);
    }

    /**
     * 设置图片盒子中显示的图片
     *
     * @param image 位图，不能为空
     */
    public void setImage(Bitmap image){
        if(image == null){
            throw new IllegalArgumentException("显示图片不能为空！");
        }
        this.image = image;
    }

    /**
     * 获取图片盒子中正在显示的图片
     *
     * @return
     */
    public Bitmap getImage(){
        return image;
    }

    @Override
    public RectF getBounds() {
        return new RectF(x, y, x + image.getWidth(), y + image.getHeight());
    }

}
