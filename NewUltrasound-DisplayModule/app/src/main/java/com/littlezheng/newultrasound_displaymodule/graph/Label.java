package com.littlezheng.newultrasound_displaymodule.graph;

import android.graphics.Canvas;

/**
 * Created by Administrator on 2017/11/9/009.
 */

public class Label extends Control {

    private static final String EMPTY_TEXT = "";

    private String text = EMPTY_TEXT;

    public Label(){
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setTextColor(int textColor){
        paint.setColor(textColor);
    }

    @Override
    public void draw(Canvas c) {
        c.drawText(text, x, y, paint);
    }

}
