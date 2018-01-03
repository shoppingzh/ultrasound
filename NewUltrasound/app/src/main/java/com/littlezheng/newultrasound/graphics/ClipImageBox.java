package com.littlezheng.newultrasound.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Administrator on 2017/11/9/009.
 */

public class ClipImageBox extends ImageBox {

    protected Rect srcRect = new Rect();
    protected RectF dstRect = new RectF();

    public ClipImageBox(Bitmap image) {
        super(image);
        srcRect.set(0, 0, image.getWidth(),image.getHeight());
    }

    @Override
    public void draw(Canvas c) {
        c.drawBitmap(getImage(), srcRect, dstRect, paint);
    }

    @Override
    public void setImage(Bitmap image) {
        super.setImage(image);
        srcRect.set(0, 0, image.getWidth(),image.getHeight());
    }

    /**
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setImageBounds(int left, int top, int right, int bottom) {
        srcRect.set(left, top, right, bottom);
    }

    /**
     * @param imageBounds
     */
    public void setImageBounds(Rect imageBounds) {
        srcRect.set(imageBounds);
    }

    public Rect getImageBounds(){
        return new Rect(srcRect);
    }

    /**
     * 设置图片绘制的目标位置
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setDrawBounds(float left, float top, float right, float bottom) {
        dstRect.set(left, top, right, bottom);
    }

    /**
     * @param drawBounds
     */
    public void setDrawBounds(RectF drawBounds) {
        dstRect.set(drawBounds);
    }

    public RectF getDrawBounds(){
        return new RectF(dstRect);
    }


    @Override
    public void setLocation(float x, float y) {
        float offsetX = x - this.x, offsetY = y - this.y;
        super.setLocation(x, y);
        dstRect.set(x, y, dstRect.right + (offsetX > 0 ? offsetX : (-offsetX)),
                dstRect.bottom + (offsetY > 0 ? offsetY : (-offsetY)));
    }

    @Override
    public RectF getBounds() {
        return new RectF(dstRect);
    }

}
