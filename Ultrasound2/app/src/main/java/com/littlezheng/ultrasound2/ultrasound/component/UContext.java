package com.littlezheng.ultrasound2.ultrasound.component;

import android.content.Context;

import com.littlezheng.ultrasound2.task.SampledDataLoadTask;
import com.littlezheng.ultrasound2.ultrasound.enums.Mode;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public class UContext {

    public static final String IMAGE_STORAGE_PATH = "aaa/img";
    public static final String VIDEO_STORAGE_PATH = "aaa/video";

    private Context mContext;

    //采样数据
    private final SampledData sampledData;

    //参数控制器
    private ParamController contrast;
    private ParamController lightness;
    private ParamController gain;
    private ParamController nearGain;
    private ParamController farGain;
    private ParamController depth;
    private ParamController speed;

    //状态、模式控制器
    private StateController stateController;
    private ModeController modeController;

    //图像信息
    //b图像像素
    private int[] bImagePixels = new int[SampledData.THIRD_SAMPLE_MAX_WIDTH *
            SampledData.THIRD_SAMPLE_MAX_HEIGHT];
    //m图像像素
    private int[] mImagePixels = new int[500 * SampledData.ORIGINAL_FRAME_HEIGHT];

    //颜色控制器（保存了默认颜色和伪彩颜色信息）
    private ColorController colorController;

    //帧保持器（保存了若干帧的图像信息）
    private ImageHolder imageHolder;

    public UContext(Context context){
        mContext = context;
        //加载采样数据
        sampledData = SampledData.getInstance(context);
        new SampledDataLoadTask(context).execute(sampledData);
        initControllers();
        imageHolder = new ImageHolder(100);
    }

    /**
     * 加载控制器（参数、模式、状态）
     */
    private void initControllers() {
        contrast = new ParamController("对比度", 32, 48, 16, 1);
        lightness = new ParamController("亮度", 32, 46, 20, 1);
        gain = new ParamController("总增", 16, 32, 1, 1);
        nearGain = new ParamController("近增", 16, 32, 1, 1);
        farGain = new ParamController("远增", 16, 32, 1, 1);
        depth = new ParamController("深度", 13, 19, 0, 1){
            @Override
            public String desc() {
                StringBuilder sb = new StringBuilder(getName());
                sb.append(": ");
                sb.append(getCurrValue()*10 + 30);
                sb.append("mm");
                return sb.toString();
            }
        };
        speed = new ParamController("速度", 3, 3, 0, 1);
        stateController = new StateController();
        modeController = new ModeController(Mode.MODE_B);

        colorController = new ColorController();
    }


    /********************************GETTERS***********************************/

    public Context getContext() {
        return mContext;
    }

    public SampledData getSampledData() {
        return sampledData;
    }

    public ParamController getContrast() {
        return contrast;
    }

    public ParamController getLightness() {
        return lightness;
    }

    public ParamController getGain() {
        return gain;
    }

    public ParamController getNearGain() {
        return nearGain;
    }

    public ParamController getFarGain() {
        return farGain;
    }

    public ParamController getDepth() {
        return depth;
    }

    public ParamController getSpeed() {
        return speed;
    }

    public StateController getStateController() {
        return stateController;
    }

    public ModeController getModeController() {
        return modeController;
    }

    public int[] getBImagePixels() {
        return bImagePixels;
    }

    public int[] getMImagePixels() {
        return mImagePixels;
    }

    public ColorController getColorController() {
        return colorController;
    }

    public ImageHolder getImageHolder() {
        return imageHolder;
    }
}
