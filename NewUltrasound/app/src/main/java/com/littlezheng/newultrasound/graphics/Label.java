package com.littlezheng.newultrasound.graphics;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Administrator on 2017/11/2/002.
 */

public class Label extends Control {

    private static final String EMPTY_LABEL_TEXT = "";

    private String text = EMPTY_LABEL_TEXT;

    public Label() {
    }

    @Override
    public void draw(Canvas c) {
        c.drawText(text, x, y, paint);
    }


    /**
     * 设置显示文本大小
     *
     * @param textSize 文本大小
     */
    public void setTextSize(float textSize) {
        paint.setTextSize(textSize);
    }

    /**
     * 获取文本大小
     *
     * @return
     */
    public float getTextSize() {
        return paint.getTextSize();
    }

    /**
     * 设置显示文本颜色
     *
     * @param textColor 颜色
     */
    public void setTextColor(int textColor) {
        paint.setColor(textColor);
    }

    /**
     * 获取文本颜色
     *
     * @return
     */
    public int getTextColor() {
        return paint.getColor();
    }

    public void setText(String text) {
        if(text == null) text = EMPTY_LABEL_TEXT;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public RectF getBounds() {
        if (text == null) return new RectF();
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return new RectF(x, y - bounds.height(), x + bounds.width(), y);
    }

}
