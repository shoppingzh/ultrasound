package com.littlezheng.ultrasound4.ultrasound.process;

import com.littlezheng.ultrasound4.framework.udp.UdpPacketValidator;
import com.littlezheng.ultrasound4.ultrasound.SampledData;
import com.littlezheng.ultrasound4.ultrasound.UContext;
import com.littlezheng.ultrasound4.ultrasound.component.UImage;
import com.littlezheng.ultrasound4.ultrasound.transfer.UdpPacket406Validator;

import java.util.Observable;

/**
 * Created by Administrator on 2017/9/24/024.
 */

public class BFrameCreator extends FrameCreator {

    private UContext uContext;
    private UdpPacketValidator validator;

    //图像处理相关
    private int[] colors; //颜色数组

    //以下参数会随着深度的变化而变化
    private int depth; //深度
    private Frame bFrame; //B图像帧
    private int secSamWid; //二次采样宽度
    private int secSamHei; //二次采样高度
    private byte[] zeros; //插零参数
    private int[][] positions; //三次采样位置参数
    private int[][] intervals; //三次采样间隔参数

    private int pixelsLength;
    private int[] fillPixels;   //像素填充数组
    private int[] displayPixels;    //像素显示数组，该数组的将提交给视图用于显示
    private int[] clearPixels;  //用以清除填充像素的数组

    private byte[] backFatSourceData;//背膘源数据

    public BFrameCreator(UContext uContext) {
        super(uContext.getUdpTransceiver());
        this.uContext = uContext;
        validator = new UdpPacket406Validator();
        colors = uContext.getColors().get();

        //深度、伪彩、模式变化感知
        uContext.getParam(UContext.PARAM_DEPTH).addObserver(this);
        uContext.getColors().addObserver(this);
    }

    @Override
    protected void init() {
        pixelsLength = SampledData.THIRD_SAMPLE_MAX_WIDTH *
                SampledData.THIRD_SAMPLE_MAX_HEIGHT;
        fillPixels = new int[pixelsLength];   //像素填充数组
        displayPixels = fillPixels.clone();   //像素显示数组，该数组的将提交给视图用于显示
        clearPixels = fillPixels.clone();    //用以清除填充像素的数组
        depthChanged();
    }

    @Override
    protected void onReceiveData(byte[] data) {
        int id = data[403] & 0xff;
        if (id == 64) {
            backFatSourceData = data;
        }
        int offset = zeros[id - 1];
        if (bFrame.full()) {
            System.arraycopy(clearPixels, 0, fillPixels, 0, pixelsLength);
            byte[][] frameData = bFrame.getData();
            CalculateSupport.thirdSample(fillPixels, frameData, secSamWid,
                    secSamHei, positions, intervals, colors);
            //处理完成后将填充后的像素复制到显示数组，再提交给视图
            System.arraycopy(fillPixels, 0, displayPixels, 0, pixelsLength);
            bFrame.clear();

            setChanged();
            notifyObservers(new UImage(displayPixels, depth));
        } else {
            bFrame.put(id - 1, data, 2, offset, SampledData.ORIGINAL_FRAME_HEIGHT);
        }
    }

    @Override
    protected boolean validate(byte[] data) {
        return validator.validate(data);
    }

    /**
     * 获取当前背膘
     *
     * @return
     */
    public float getCurrentBackFat() {
        if (backFatSourceData == null) return -1;
        return CalculateSupport.calculateBackFat(backFatSourceData);
    }

    @Override
    public void update(Observable o, Object arg) {
        depthChanged();
        colors = uContext.getColors().get();
    }

    private void depthChanged() {
        depth = uContext.getParam(UContext.PARAM_DEPTH).getCurrValue();
        bFrame = new BFrame(SampledData.SECOND_SAMPLE_WIDTH_BASE,
                SampledData.SECOND_SAMPLE_MAX_HEIGHT, false);
        secSamWid = SampledData.getSecondSampleWidth(depth);
        secSamHei = SampledData.getSecondSampleHeight(depth);
        zeros = SampledData.getZeroInsertions(depth);
        positions = uContext.getSampledData().getPositions(depth);
        intervals = uContext.getSampledData().getIntervals(depth);
    }

}
