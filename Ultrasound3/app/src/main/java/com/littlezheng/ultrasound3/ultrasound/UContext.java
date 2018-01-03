package com.littlezheng.ultrasound3.ultrasound;

import android.content.Context;

import com.littlezheng.ultrasound3.task.SampledDataLoadTask;
import com.littlezheng.ultrasound3.ultrasound.base.Colors;
import com.littlezheng.ultrasound3.ultrasound.base.Mode;
import com.littlezheng.ultrasound3.ultrasound.base.ModeSwitcher;
import com.littlezheng.ultrasound3.ultrasound.base.Param;
import com.littlezheng.ultrasound3.ultrasound.base.SampledData;
import com.littlezheng.ultrasound3.ultrasound.base.StateSwitcher;
import com.littlezheng.ultrasound3.ultrasound.process.ImageHolder;
import com.littlezheng.ultrasound3.ultrasound.transmission.UdpTransmitter;

import java.net.InetAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/5/005.
 */

public class UContext {

    public static final String IMAGE_STORAGE_PATH = "aaa/img";
    public static final String VIDEO_STORAGE_PATH = "aaa/video";

    public static final int STATE_NO_CONNECT = 0x00000000;
    public static final int STATE_FREEZE = 0x00000001;
    public static final int STATE_UNFREEZE = 0x00000002;

    public static final int PARAM_CONTRAST = 0x00000010;
    public static final int PARAM_LIGHTNESS = 0x00000011;
    public static final int PARAM_GAIN = 0x00000012;
    public static final int PARAM_NEAR_GAIN = 0x00000013;
    public static final int PARAM_FAR_GAIN = 0x00000014;
    public static final int PARAM_DEPTH = 0x00000015;
    public static final int PARAM_SPEED = 0x00000016;

    public static final int MODE_B = 0x00000100;
    public static final int MODE_M = 0x00000101;
    public static final int MODE_BB = 0x00000102;
    public static final int MODE_BM = 0x00000103;

    public static final int PSEUDO_COLOR_NORMAL = 0x00000110;
    public static final int PSEUDO_COLOR_RED = 0x00000111;
    public static final int PSEUDO_COLOR_YELLOW = 0x00000112;
    public static final int PSEUDO_COLOR_MIX = 0x00000113;

    private Configuration configuration;

    //当前Activity
    private final Context androidContext;

    //项目中的必需唯一组件
    private UdpTransmitter udpTransmitter;
    private Map<Integer, Param> params = new HashMap<>();
    private StateSwitcher stateSwitcher;
    private ModeSwitcher modeSwitcher;
    private Colors colors;
    private SampledData sampledData;
    private int[] bImagePixels = new int[SampledData.THIRD_SAMPLE_MAX_WIDTH *
            SampledData.THIRD_SAMPLE_MAX_HEIGHT];
    private int[] mImagePixels = new int[500 * SampledData.ORIGINAL_FRAME_HEIGHT];
    private ImageHolder imageHolder;

    public UContext(Configuration configuration) {
        this.configuration = configuration;
        androidContext = configuration.getContext();
        imageHolder = new ImageHolder(configuration.getVideoFrames());
        initComponents();
    }

    private void initComponents() {
        initBaseConponents();
        initSampleData();
        initWorkers();
    }

    /**
     * 初始化基础组件：
     * 1. 参数列表
     * 2. 状态控制器
     * 3. 模式控制器
     * 4. 伪彩色控制器
     */
    private void initBaseConponents() {
        params.put(PARAM_CONTRAST, new Param("对比度", 32, 48, 16, 1));
        params.put(PARAM_LIGHTNESS, new Param("亮度", 32, 46, 20, 1));
        params.put(PARAM_GAIN, new Param("总增", 16, 32, 1, 1));
        params.put(PARAM_NEAR_GAIN, new Param("近增", 16, 32, 1, 1));
        params.put(PARAM_FAR_GAIN, new Param("远增", 16, 32, 1, 1));
        params.put(PARAM_DEPTH, new Param("深度", 13, 19, 0, 1) {
            @Override
            public String desc() {
                StringBuilder sb = new StringBuilder(getName());
                sb.append(": ");
                sb.append(getCurrValue() * 10 + 30);
                sb.append("mm");
                return sb.toString();
            }
        });
        params.put(PARAM_SPEED, new Param("速度", 3, 3, 0, 1));
        stateSwitcher = new StateSwitcher();
        modeSwitcher = new ModeSwitcher(Mode.MODE_B);
        colors = new Colors();
    }

    /**
     * 初始化工作线程
     */
    private void initWorkers() {
        //数据收发器
        try {
            UdpTransmitter.Config conf = new UdpTransmitter.Config();
            conf.receivePort = configuration.getReceivePort();
            conf.receivePackSize = configuration.getReceivePacketSize();
            conf.receiveBufSize = configuration.getReceiveBufferSize();
            conf.sendAddress = InetAddress.getByName(configuration.getSendAddress());
            conf.sendPort = configuration.getSendPort();
            udpTransmitter = new UdpTransmitter(conf);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("数据收发器初始化失败！" + e);
        }
    }

    /**
     * 异步加载采样数据
     */
    private void initSampleData() {
        sampledData = SampledData.getInstance(androidContext, configuration.getSampleDataFile());
        new SampledDataLoadTask(androidContext).execute(sampledData);
    }

    public UdpTransmitter getUdpTransmitter() {
        return udpTransmitter;
    }

    /**
     * 获取所有参数集合
     *
     * @return
     */
    public Collection<Param> getParams() {
        return params.values();
    }

    /**
     * 获取指定参数
     *
     * @param param UContext.PARAM_CONTRAST
     *              //TODO
     * @return
     */
    public Param getParam(int param) {
        return params.get(param);
    }

    public Context getAndroidContext() {
        return androidContext;
    }

    public StateSwitcher getStateSwitcher() {
        return stateSwitcher;
    }

    public ModeSwitcher getModeSwitcher() {
        return modeSwitcher;
    }

    public Colors getColors() {
        return colors;
    }

    public SampledData getSampledData() {
        return sampledData;
    }

    public int[] getBImagePixels() {
        return bImagePixels;
    }

    public int[] getMImagePixels() {
        return mImagePixels;
    }

    public ImageHolder getImageHolder() {
        return imageHolder;
    }
}
