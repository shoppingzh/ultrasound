package com.littlezheng.ultrasound3.ultrasound;

import android.graphics.Bitmap;
import android.view.ViewGroup;

import com.littlezheng.ultrasound3.ultrasound.base.Colors;
import com.littlezheng.ultrasound3.ultrasound.base.Mode;

import java.io.File;

/**
 * Created by Administrator on 2017/9/5/005.
 */

public interface IApplication {

    /**
     * 初始化显示窗口
     * @param wrapper 显示窗口的显示视图
     */
    void initWindow(ViewGroup wrapper);

    /**
     * 参数调节：增加
     * @param param
     */
    void increase(int param);

    /**
     * 参数调节：减少
     * @param param
     */
    void decrease(int param);

    /**
     * 参数调节：重置
     * @param param
     */
    void reset(int param);

    /**
     * 冻结
     */
    void freeze();

    /**
     * 解冻
     */
    void unfreeze();

    /**
     * 是否处于冻结状态
     * @return
     */
    boolean isFrozen();

    /**
     * 改变工作模式
     * @param mode
     */
    void changeMode(Mode mode);

    /**
     * 改变伪彩色
     * @param color
     */
    void changePseudoColor(Colors.PseudoColor color);

    /**
     * 拍摄屏幕快照
     */
    void takeSnapshot();

    /**
     * 显示屏幕快照
     * @param snapshot
     */
    void showSnapshot(Bitmap snapshot);

    /**
     * 回放
     */
    void replay();

    /**
     * 播放指定视频
     * @param video
     */
    void play(File video);

    /**
     * 保存视频
     */
    void saveVideo();

}
