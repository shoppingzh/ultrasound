package com.littlezheng.newultrasound.displayer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;

import com.littlezheng.newultrasound.graphics.ImageBox;
import com.littlezheng.newultrasound.graphics.Label;
import com.littlezheng.newultrasound.graphics.TransformImageBox;

/**
 * Created by Administrator on 2017/11/17/017.
 */

class ImageShowDisplayer extends Displayer {

    private static final Label tips = new Label();
    static{
        tips.setText("图片预览模式");
        tips.setTextSize(20);
        tips.setTextColor(Color.RED);
    }

    private ImageBox pb;

    public ImageShowDisplayer(Bitmap image){
        pb = new TransformImageBox(image);
        add(pb);
        add(tips);
    }

    @Override
    public void init(int width, int height) {
        tips.setLocation(width-tips.getBounds().width()-10, height-10);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return pb.onTouchEvent(event);
    }

}
