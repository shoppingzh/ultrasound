package com.littlezheng.ultrasound.ultrasound.api;

/**
 * Created by Administrator on 2017/8/25/025.
 */

public class UContext {

    private UContext(){}

    //可调参数
    public static final int PARAM_CONTRAST = 0x00000101;
    public static final int PARAM_LIGHTNESS = 0x00000102;
    public static final int PARAM_GAIN = 0x00000103;
    public static final int PARAM_NEAR_GAIN = 0x00000104;
    public static final int PARAM_FAR_GAIN = 0x00000105;
    public static final int PARAM_DEPTH = 0x00000106;
    public static final int PARAM_SPEED = 0x00000107;

    //可选工作模式
    public static final int MODE_B = 0x00001001;
    public static final int MODE_M = 0x00001002;
    public static final int MODE_BB = 0x00001003;
    public static final int MODE_BM = 0x00001004;

    //测量方案
    public static final int MEASURE_FUCK_OFF = 0x00010001;
    public static final int MEASURE_LENGTH = 0x00010002;
    public static final int MEASURE_AREA_PERIMETER = 0x00010003;




}
