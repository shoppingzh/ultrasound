package com.littlezheng.ultrasound.ultrasound;

/**
 * Created by zxp on 2017/8/3.
 */

public enum Depth {

    DEPTH_30(30,0),DEPTH_40(40,1),DEPTH_50(50,2),DEPTH_60(60,3),DEPTH_70(70,4),
    DEPTH_80(80,5), DEPTH_90(90,6),DEPTH_100(100,7),DEPTH_110(110,8),DEPTH_120(120,9),
    DEPTH_130(130,10), DEPTH_140(140,11),DEPTH_150(150,12),DEPTH_160(160,13),DEPTH_170(170,14),
    DEPTH_180(180,15), DEPTH_190(190,16),DEPTH_200(200,17),DEPTH_210(210,18),DEPTH_220(220,19),
    DEPTH_DEFAULT(DEPTH_160.value,DEPTH_160.index);

    //深度值
    private int value;
    //深度索引
    private int index;

    private Depth(int value, int index){
        this.value = value;
        this.index = index;
    }

    public int getIndex(){
        return index;
    }

    public int getValue(){
        return value;
    }

    public static Depth getDepth(int value){
        Depth depth;
        switch (value){
            case 30:
                depth = DEPTH_30;
                break;
            case 40:
                depth = DEPTH_40;
                break;
            case 50:
                depth = DEPTH_50;
                break;
            case 60:
                depth = DEPTH_60;
                break;
            case 70:
                depth = DEPTH_70;
                break;
            case 80:
                depth = DEPTH_80;
                break;
            case 90:
                depth = DEPTH_90;
                break;
            case 100:
                depth = DEPTH_100;
                break;
            case 110:
                depth = DEPTH_110;
                break;
            case 120:
                depth = DEPTH_120;
                break;
            case 130:
                depth = DEPTH_130;
                break;
            case 140:
                depth = DEPTH_140;
                break;
            case 150:
                depth = DEPTH_150;
                break;
            case 160:
                depth = DEPTH_160;
                break;
            case 170:
                depth = DEPTH_170;
                break;
            case 180:
                depth = DEPTH_180;
                break;
            case 190:
                depth = DEPTH_190;
                break;
            case 200:
                depth = DEPTH_200;
                break;
            case 210:
                depth = DEPTH_210;
                break;
            case 220:
                depth = DEPTH_220;
                break;
            default:
                depth = DEPTH_160;
                break;
        }
        return depth;
    }

    @Override
    public String toString() {
        return "Depth{" +
                "value=" + value +
                ", index=" + index +
                '}';
    }
}
