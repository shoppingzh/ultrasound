package com.littlezheng.ultrasound4.ultrasound;

import android.content.Context;

import com.littlezheng.ultrasound4.ultrasound.component.Colors;
import com.littlezheng.ultrasound4.ultrasound.component.Mode;
import com.littlezheng.ultrasound4.ultrasound.component.ModeSwitcher;
import com.littlezheng.ultrasound4.ultrasound.component.Param;
import com.littlezheng.ultrasound4.ultrasound.component.StateSwitcher;
import com.littlezheng.ultrasound4.ultrasound.transfer.UdpTransceiver;

import java.net.InetAddress;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2017/9/5/005.
 */

public class UContext {

    public static final String IMAGE_STORAGE_PATH = "aaa/img/";
    public static final String VIDEO_STORAGE_PATH = "aaa/video/";

    public static final int PARAM_CONTRAST = 0;
    public static final int PARAM_LIGHTNESS = 1;
    public static final int PARAM_GAIN = 2;
    public static final int PARAM_NEAR_GAIN = 3;
    public static final int PARAM_FAR_GAIN = 4;
    public static final int PARAM_DEPTH = 5;
    public static final int PARAM_SPEED = 6;
    //当前Activity
    private final Context androidContext;
    private Configuration configuration;
    private UdpTransceiver udpTransceiver;
    private Map<Integer, Param> params = new TreeMap<>();
    private StateSwitcher stateSwitcher;
    private ModeSwitcher modeSwitcher;
    private Colors colors;

    private SampledData sampledData;

    public UContext(Configuration configuration) {
        this.configuration = configuration;
        androidContext = configuration.getContext();
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
            UdpTransceiver.Config conf = new UdpTransceiver.Config();
            conf.receivePort = configuration.getReceivePort();
            conf.receivePackSize = configuration.getReceivePacketSize();
            conf.receiveBufSize = configuration.getReceiveBufferSize();
            conf.sendAddress = InetAddress.getByName(configuration.getSendAddress());
            conf.sendPort = configuration.getSendPort();
            udpTransceiver = new UdpTransceiver(conf);
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

    public UdpTransceiver getUdpTransceiver() {
        return udpTransceiver;
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

}
