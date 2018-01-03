package com.littlezheng.ultrasound4.ultrasound.process;

import android.util.Log;

import com.littlezheng.ultrasound4.ultrasound.SampledData;

/**
 * Created by Administrator on 2017/9/24/024.
 */

public class CalculateSupport {

    private static final String TAG = "CalculateSupport";

    private static final int LENGTH = 400;
    private static final int THRES = 5;
    private static final int START = 25;
    private static final float THREEBORD = 0.5f;

    /**
     * 三次采样处理
     *
     * @param bImagePixels 存放处理结果的像素数组
     * @param origin       源数据
     * @param width        源数据帧宽度
     * @param height       源数据帧高度
     * @param positions    插值采样位置信息
     * @param intervals    插值间隔信息
     * @param colors       处理像素的颜色
     */
    public static void thirdSample(int[] bImagePixels,
                                   byte[][] origin,
                                   int width,
                                   int height,
                                   int[][] positions,
                                   int[][] intervals,
                                   int[] colors) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width - 1; j++) {
                int curr = origin[j][i] & 0xff;
                int next = origin[j + 1][i] & 0xff;

                int interval = intervals[i][j];
                int pos = positions[i][j];

                int cIdx = i * SampledData.THIRD_SAMPLE_MAX_WIDTH + pos;
//                //如果取样间隔小于等于1，则直接放置数据即可
                if (interval <= 1) {
                    bImagePixels[cIdx] = colors[curr];
                    continue;
                }

                bImagePixels[cIdx++] = colors[curr];
//                //如果相邻两点值相同，则插值也相同，故不做运算
                if (curr == next) {
                    for (int k = interval - 1; k > 0; k--) {
                        bImagePixels[cIdx++] = colors[curr];
                    }
                } else {
                    for (int k = interval - 1; k > 0; k--) {
                        //float fInterval =  interval;
                        //float pow1 = k / fInterval, pow2 = (interval - k) / fInterval;
                        int pow1 = k * 100 / interval, pow2 = (interval - k) * 100 / interval;
//                        int value = (int) (curr * pow1 + next * pow2);
                        int value =  (curr * pow1 + next * pow2) / 100;
                        bImagePixels[cIdx++] = colors[value];
                    }
                }

            }
        }
        Log.d(TAG,"一帧数据处理时间："+(System.currentTimeMillis()-start)+"ms");
//        Log.d(TAG, "处理B数据");
    }


    /**
     * M图像生成算法
     *
     * @param mImagePixels 存放处理后的M图像像素数组
     * @param mFrame       M图像源数据帧
     * @param colors       处理像素的颜色
     */
    public static void generateMImage(int[] mImagePixels,
                                      MFrame mFrame,
                                      int[] colors) {
//        long s = System.currentTimeMillis();
        byte[][] data = mFrame.getData();
        int wid = mFrame.getWidth(), hei = mFrame.getHeight();
        int start = mFrame.getPos();
        int idx = 0;
        for (int i = 0; i < hei; i++) {
            for (int j = start; j < wid; j++) {
                mImagePixels[idx++] = colors[data[j][i] & 0xff];
            }
            for (int j = 0; j < start; j++) {
                mImagePixels[idx++] = colors[data[j][i] & 0xff];
            }
        }
//        Log.d(TAG,"M模式一帧数据处理时间："+(System.currentTimeMillis()-s)+"ms");
//        Log.d(TAG, "处理M数据");
    }

    /**
     * 计算背膘
     *
     * @param data
     * @return
     */
    public static float calculateBackFat(byte[] data) {
        int len = 0;
        char[] cdata = new char[len = data.length];
        for (int i = 0; i < len; i++) {
            cdata[i] = (char) (data[i] & 0xff);
        }
        return calculateBackFat(cdata);
    }

    /**
     * 计算背膘
     *
     * @param data
     * @return
     */
    private static float calculateBackFat(char[] data)       //输入变量为背膘数据
    {
        short i, j, k, m;                             //定义无意义循环变量
        char[] newdata = new char[LENGTH];         ////暂存背膘数据数组

        int aver = 0;                                //数据平均值
        int aver200 = 0;                             //第三层膘所在的200-280的数据平均值
        char max = 0, min = 255;                        //数据最大值与最小值
        short maxcode = 0;                           //数据最大值在数组中所对应的位
        char high = 0;                               //最大值的高度，即最大值
        short tmp = 0;                               //无意义中间变量
        short[] topwave = new short[30];                         //存储波峰在data数组中对应的位
        short data_top3 = 0;                         //最高的三个峰的数据总值
//        memset(topwave, 2, sizeof(topwave));                //初始化数据为极大值514
        short topnum = 0;                            //波峰数量
        short botwave = 0;                           //波谷在data数组中对应的位
        short[] record = new short[9];                          //记录最终的有效波形
        short left = 0;                              //背膘的左侧波峰
        short right = 0;                             //背膘的右侧波峰

        short width = 0;                             //判断是否为空气的几组变量
        short aver1 = 0;
        short aver2 = 0;
        short aver3 = 0;
        short max2 = 0;
        short max3 = 0;

        short top = 0;                               //背膘的上界
        short bot = 0;                               //背膘的下界
        short flag = 1;

        int start, length;

        start = 0;
        length = 0;
        float fat = 0;                                        //背膘有效厚度（单位mm）

        for (i = START; i < (LENGTH * 23 / 40); i++) aver += data[i];          //求出有效数据的平均值
        aver = aver / (LENGTH * 23 / 40 - START);

        for (i = LENGTH / 2; i < (LENGTH * 25 / 40); i++)
            aver200 += data[i];         //求出200-280第三层背膘所在位置的平均值
        aver200 = aver200 / (LENGTH * 25 / 40 - LENGTH / 2);

        for (i = 0; i < START; i++) newdata[i] = 1;              //前START个数据舍去，赋值为1
        for (i = START; i < (LENGTH * 28 / 40); i++) newdata[i] = data[i];          //转存数据
        for (i = (LENGTH * 23 / 40); i < LENGTH; i++)
            newdata[i] = 1;              //后175个数据舍去，赋值为1

        j = 0;                                                //波峰数量初始化

        for (k = 0; k < (LENGTH / 2); k++) {
            max = 0;                                          //最大值初始化
            for (i = START; i < (LENGTH * 3 / 4); i++) {
                if ((newdata[i] >= max) && (newdata[i] > (high / 2)))      //寻找波峰，且波峰必须满足大于最高峰的一半（最高峰不管）
                {
                    max = newdata[i];                      //暂存波峰数据
                    maxcode = i;                            //波峰所在的位置
                }
            }

            if ((newdata[maxcode - THRES] != 0) && (newdata[maxcode + THRES] != 0)) //判断波峰是否为孤立，孤立则有效
            {
                if (j == 0)
                    high = max;                        //第一组波峰。   即最高波峰，存储其高度
                topwave[j] = maxcode;                        //存储位置
                j++;
                if (j >= 30) break;
            }

            for (i = 0; i < THRES * 2; i++)                        // 存储之后，铲除该波峰
            {
                newdata[maxcode - THRES + i] = 0;
            }
        }
        topnum = j;


        for (i = 0; i < topnum; i++)                              //冒泡法升序排列
        {
            for (j = 0; j < topnum; j++) {
                if (topwave[j] >= topwave[j + 1]) {
                    tmp = topwave[j];
                    topwave[j] = topwave[j + 1];
                    topwave[j + 1] = tmp;
                }
            }
        }

        k = 0;
        m = 1;
        i = 0;
        while (true) {
            if ((i + m) >= topnum) break;                  //大于波峰数量则终止
            min = 255;                                        //最小值初始化为数据的最大值
            left = topwave[i];
            right = topwave[i + m];
            if (right >= (LENGTH * 28 / 40)) break;                      //终止的位置，280为三层表最大位置3.5cm

            for (j = left; j < right; j++)                       //寻找两两波峰之间的波谷
            {
                if (data[j] <= min) {
                    min = data[j];
                    botwave = j;
                }
            }

            if ((data[right] - min) >= ((high / 2 + aver) / 2))      //判断波峰波谷高度差是否满足要求
            {
                record[k++] = left;
                record[k++] = botwave;
                record[k++] = right;

                if (k >= 9) break;                            //存储3组“峰--谷--峰”即足够

                i = (short) (i + m);
                m = 1;
            } else                                           //不满足要求，则right取下一个波峰
            {
                m++;
            }
        }

        if (record[5] <= 0) flag = 0;


        if (flag == 1)                                     //是否打空气的判断，根据走势
        {
            aver1 = 0;
            aver2 = 0;
            max2 = 0;
            aver3 = 0;
            max3 = 0;
            width = (short) ((record[5] - START) / 3);                    //从开始的START到2层膘的末端
            for (i = START; i < width + START; i++) {
                aver1 += data[i];
            }
            aver1 = (short) (aver1 / width);                            //逐级取最大值和平均值

            for (i = (short) (width + START); i < 2 * width + START; i++) {
                aver2 += data[i];
                if (data[i] > max2) max2 = (short) data[i];
            }
            aver2 = (short) (aver2 / width);
            for (i = (short) (2 * width + START); i < 3 * width + START; i++) {
                aver3 += data[i];
                if (data[i] > max3) max3 = (short) data[i];
            }
            aver3 = (short) (aver3 / width);
            if ((aver1 >= max2) && (aver2 >= max3)) flag = 0;      //如果逐级降低，则为空气

            top = 0;                                      //上界(/10*9表示乘以十分之9 )
            data_top3 = (short) ((data[topwave[0]] + data[topwave[1]] + data[topwave[2]]) / 3);  //前三个波峰的均值
            if ((aver200 >= aver) && (data[record[8]] > data_top3 * THREEBORD))
            //第三层膘区域的平均值大于整体平均值，且第三层膘的峰值大于前三峰均值，则认为有第三层膘
            {
                bot = (short) (record[7] + (record[8] - record[7]) * 9 / 10);
                for (i = record[8]; i >= record[7]; i--)
                    if ((data[i] >= data[i - 1]) && (i > bot)) {
                        bot = i;
                        break;
                    }
            } else                                                //否则，认为只有两层膘
            {
                bot = (short) (record[4] + (record[5] - record[4]) * 9 / 10);       //下界
                for (i = record[5]; i >= record[4]; i--)
                    if ((data[i] >= data[i - 1]) && (i > bot)) {
                        bot = i;
                        break;
                    }
            }
        }
        start = top;
        length = bot - top;

        fat = (float) ((bot - top) * 50.0 / 341);                      //差值，并转换为毫米

        if (fat > 40)
            return 40.0f;
        else
            return fat;
    }

}
