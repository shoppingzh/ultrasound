package com.littlezheng.newultrasound.displayer;

import android.graphics.Bitmap;

import com.littlezheng.newultrasound.core.FrameGenerator;
import com.littlezheng.newultrasound.transmission.Sender;

/**
 * Created by Administrator on 2017/11/17/017.
 */

public class DisplayerFactory {

    public static WorkModeDisplayer createBModeDisplayer(FrameGenerator frameGenerator){
        return new BModeDisplayer(frameGenerator);
    }

    public static WorkModeDisplayer createBBModeDisplayer(FrameGenerator frameGenerator, boolean left){
        return new BBModeDisplayer(frameGenerator, left);
    }

    public static WorkModeDisplayer createMModeDisplayer(FrameGenerator frameGenerator){
        return new MModeDisplayer(frameGenerator);
    }

    public static WorkModeDisplayer createBMModeDisplayer(FrameGenerator frameGenerator, boolean left){
        return new BMModeDisplayer(frameGenerator, left);
    }

    public static Displayer createImageShowDisplayer(Bitmap image){
        return new ImageShowDisplayer(image);
    }

    public static MeasureDisplayer createMeasureDisplayer(Displayer wrapper){
        return new MeasureDisplayer(wrapper);
    }

}
