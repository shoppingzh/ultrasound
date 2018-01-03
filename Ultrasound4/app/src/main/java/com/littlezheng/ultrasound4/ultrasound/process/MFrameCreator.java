package com.littlezheng.ultrasound4.ultrasound.process;

import com.littlezheng.ultrasound4.framework.udp.UdpPacketValidator;
import com.littlezheng.ultrasound4.ultrasound.SampledData;
import com.littlezheng.ultrasound4.ultrasound.UContext;
import com.littlezheng.ultrasound4.ultrasound.component.UImage;

import java.util.Observable;

/**
 * Created by Administrator on 2017/9/24/024.
 */

public class MFrameCreator extends FrameCreator {

    private UContext uContext;
    private UdpPacketValidator validator;
    private int[] colors;

    private int[] pixels; //M图像像素
    private MFrame mFrame; //M图像帧
    private int[] clearPixels; //用于清除M图像的空白帧

    public MFrameCreator(UContext uContext) {
        super(uContext.getUdpTransceiver());
        this.uContext = uContext;
        colors = uContext.getColors().get();
        pixels = new int[500 * SampledData.ORIGINAL_FRAME_HEIGHT];
        clearPixels = pixels.clone();

        uContext.getColors().addObserver(this);
    }

    @Override
    protected void init() {
        mFrame = new MFrame(500, SampledData.ORIGINAL_FRAME_HEIGHT, false);
    }

    @Override
    protected void onReceiveData(byte[] data) {
        mFrame.put(data, 2, 0, SampledData.ORIGINAL_FRAME_HEIGHT);
        CalculateSupport.generateMImage(pixels, mFrame, colors);

        setChanged();
        notifyObservers(new UImage(pixels));
    }

    @Override
    public void update(Observable o, Object arg) {
        colors = uContext.getColors().get();
    }


}
