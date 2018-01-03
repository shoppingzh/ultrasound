package com.littlezheng.newultrasound_displaymodule;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

import com.littlezheng.newultrasound_displaymodule.graph.Label;

/**
 * Created by Administrator on 2017/11/9/009.
 */

public class DisplayView extends ContinuousSurfaceView {

    Label control = new Label();

    public DisplayView(Context context) {
        super(context);
        control.setText("你好啊");
        control.setTextColor(Color.WHITE);
    }

    @Override
    protected void onSurfaceDraw(Canvas canvas) {
        control.draw(canvas);
    }

}
