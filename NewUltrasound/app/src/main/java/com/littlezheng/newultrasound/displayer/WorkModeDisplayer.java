package com.littlezheng.newultrasound.displayer;

import android.graphics.Bitmap;

import com.littlezheng.newultrasound.core.FrameGenerator;

/**
 * Created by Administrator on 2017/11/17/017.
 */

public abstract class WorkModeDisplayer extends CommonDisplayer {

    static final Bitmap EMPTY_ASIDE_FRAME = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8);

    protected FrameGenerator frameGenerator;
    private boolean multiple = false;
    private boolean left = true;

    protected WorkModeDisplayer(FrameGenerator frameGenerator, boolean multiple, boolean left){
        this.frameGenerator = frameGenerator;
        this.multiple = multiple;
        this.left = left;
        modeLabel.setText("模式：" + desc());
    }

    public boolean isMultiple() {
        return multiple;
    }

    public boolean isLeft() {
        return left;
    }

    public void toggle(){
        if(isMultiple()){
            left = !left;
            modeLabel.setText("模式：" + desc());
        }
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);
    }

    public abstract String desc();

}
