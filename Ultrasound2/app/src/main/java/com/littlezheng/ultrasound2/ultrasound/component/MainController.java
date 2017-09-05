package com.littlezheng.ultrasound2.ultrasound.component;

import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Toast;

import com.littlezheng.ultrasound2.activity.MainActivity;
import com.littlezheng.ultrasound2.ultrasound.enums.Mode;
import com.littlezheng.ultrasound2.ultrasound.enums.Param;

import java.io.File;
import java.util.Observable;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public class MainController {

    private final UContext uContext;
    private final FrontController frontController;
    private final DataController dataController;
    private final ViewController viewController;
    private final ReplayController replayController;

    public MainController(UContext uContext){
        this.uContext = uContext;
        frontController = new FrontController(uContext);
        dataController = new DataController(uContext, frontController.getUdpTransmitter());
        viewController = new ViewController(uContext);
        replayController = new ReplayController(uContext);
    }

    /**
     * 在一个视图组中显示当前视图
     * @param viewGroup
     */
    public void showInViewGroup(ViewGroup viewGroup){
        viewController.show(viewGroup);
    }

    /**
     * 是否处于冻结状态
     * @return
     */
    public boolean isFrozen(){
        return frontController.isFrozen();
    }

    /**
     * 冻结：
     *      1. 发送冻结指令
     *      2. 关闭接收
     *      3. 关闭处理
     *
     */
    public void freeze() {
        frontController.freeze();
        dataController.disable();
    }

    /**
     * 解冻：与冻结相反处理
     */
    public void unfreeze(){
        frontController.unfreeze();
        dataController.enable();

        setMode(uContext.getModeController().getMode());
    }

    /**
     * 参数值增加
     * @param p
     */
    public void increase(Param p){
        frontController.increase(p);
    }

    /**
     * 参数值减少
     * @param p
     */
    public void decrease(Param p){
        frontController.decrease(p);
    }

    /**
     * 参数值恢复默认
     * @param p
     */
    public void reset(Param p){
        frontController.reset(p);
    }

    /**
     * 改变工作模式
     * @param mode
     */
    public void setMode(Mode mode){
        frontController.setMode(mode);
        dataController.setMode(mode);
        viewController.setMode(mode, dataController);
    }

    /**
     * 设置伪彩
     * @param color
     */
    public void setPseudoColor(ColorController.PseudoColor color){
        dataController.setPseudoColor(color);
    }

    /**
     * 保存当前显示图片
     */
    public void saveDisplayImage() {
        viewController.saveViewBitmap();
    }

    /**
     * 显示一张图片
     * @param image
     */
    public void displayImage(Bitmap image) {
        freeze();
        viewController.displayImage(image);
    }

    /**
     * 视频回放
     */
    public void replay(){
        //在回放前，先确定已经进入冻结状态
        if(isFrozen()){
            viewController.replayMode(replayController);
            replayController.replay();
        }

    }

    /**
     * 保存视频
     */
    public void saveVideo() {
        if(uContext.getImageHolder().isEmpty()){
            Toast.makeText(uContext.getContext(), "当前没有任何回放！", Toast.LENGTH_LONG).show();
            return;
        }
        dataController.saveVideo();
        Toast t = Toast.makeText(uContext.getContext(), "保存成功", Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        t.show();
    }

    /**
     * 播放视频
     * @param video
     */
    public void playVideo(File video) {
        if(isFrozen()){
            viewController.replayMode(replayController);
            replayController.replay(video);
        }
    }
}
