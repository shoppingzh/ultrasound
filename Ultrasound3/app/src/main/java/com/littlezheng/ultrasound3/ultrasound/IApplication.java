package com.littlezheng.ultrasound3.ultrasound;

import android.view.ViewGroup;

import com.littlezheng.ultrasound3.ultrasound.base.Colors;
import com.littlezheng.ultrasound3.ultrasound.base.Mode;

import java.io.File;

/**
 * Created by Administrator on 2017/9/5/005.
 */

public interface IApplication {

    /**
     * 冻结
     * 冻结状态主要完成以下工作：
     * 1. 发送冻结指令与更新屏幕显示运行状态（通过操作状态转换器完成）
     * 2. 停止进行任何数据接收工作
     * 3. 停止进行任何数据处理工作（防止缓冲区内尚有数据而导致处理线程不能及时阻塞）
     */
    void freeze();

    /**
     * 解冻
     * 解冻状态主要完成以下工作：
     * 1. 发送解冻指令及更新屏幕运行状态显示
     * 2. 开启数据接收
     * 3. 开启数据处理
     */
    void unfreeze();

    /**
     * 是否处于冻结状态
     *
     * @return
     */
    boolean isFrozen();

    /**
     * 参数调节：增加
     * 该操作在成功的情况下会同时发送指令及更新屏幕参数列表
     *
     * @param param
     */
    void increase(int param);

    /**
     * 参数调节：减少
     * 该操作在成功的情况下会同时发送指令及更新屏幕参数列表
     *
     * @param param
     */
    void decrease(int param);

    /**
     * 参数调节：重置
     * 该操作在成功的情况下会同时发送指令及更新屏幕参数列表
     *
     * @param param
     */
    void reset(int param);

    /**
     * 初始化显示窗口
     * 请务必调用此方法，否则将无法显示任何内容在屏幕上！
     *
     * @param wrapper 显示窗口的显示视图
     */
    void initWindow(ViewGroup wrapper);

    /**
     * 设置工作模式：B Model B/B B/Model
     * 模式设置涉及到前后端的操作有：
     * 1. 发送模式指令及更新屏幕显示
     * 2. 开启不同的数据处理形式，如M模式只对数据进行M处理而不进行B处理
     * 3. 更改屏幕显示及数据更新
     *
     * @param mode
     */
    void setMode(Mode mode);

    /**
     * 改变伪彩色
     * 1. RED 红伪彩
     * 2. YELLOW 黄伪彩
     * 3. MIX 红黄混合伪彩
     *
     * @param color
     */
    void changePseudoColor(Colors.PseudoColor color);

    /**
     * 拍摄屏幕快照
     * 将捕获当前屏幕并将快照保存到：
     * {@link com.littlezheng.ultrasound3.ultrasound.UContext#IMAGE_STORAGE_PATH}文件夹
     */
    void takeSnapshot();

    /**
     * 显示快照
     *
     * @param snapshot
     */
    void showSnapshot(File snapshot);

    /**
     * 保存视频
     * 将最近前N帧（取决于用户设置）B图像（只能处理B图像）打包为视频存储在：
     * {@link com.littlezheng.ultrasound3.ultrasound.UContext#VIDEO_STORAGE_PATH}文件夹
     */
    void saveVideo();

    /**
     * 回放
     * 回放最近N帧（取决于用户设置）的B图像视频
     */
    void replay();

    /**
     * 播放指定视频
     *
     * @param video
     */
    void play(File video);

    /**
     * 计算并显示背膘
     */
    void calculateAndDisplayBackFat();

}
