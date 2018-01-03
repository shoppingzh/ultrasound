package com.littlezheng.newultrasound_displaymodule.graph;

import android.graphics.Paint;

/**
 * Created by Administrator on 2017/11/9/009.
 */

public abstract class Control implements Drawable{

    private static final Paint EMPTY_PAINT = new Paint();

    protected float x;
    protected float y;
    protected Paint paint = EMPTY_PAINT;



}
