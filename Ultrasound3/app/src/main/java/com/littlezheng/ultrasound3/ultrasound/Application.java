package com.littlezheng.ultrasound3.ultrasound;

import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Toast;

import com.littlezheng.ultrasound3.ultrasound.base.Colors;
import com.littlezheng.ultrasound3.ultrasound.base.Mode;
import com.littlezheng.ultrasound3.ultrasound.controller.DataController;
import com.littlezheng.ultrasound3.ultrasound.controller.FrontController;
import com.littlezheng.ultrasound3.ultrasound.controller.ViewController;
import com.littlezheng.ultrasound3.ultrasound.process.PlayProcessor;

import java.io.File;

/**
 * Created by Administrator on 2017/9/5/005.
 */

public class Application implements IApplication {

    private final UContext uContext;

    private final FrontController frontController;
    private final DataController dataController;
    private final ViewController viewController;

    public Application(UContext uContext){
        this.uContext = uContext;
        frontController = new FrontController(uContext);
        dataController = new DataController(uContext);
        viewController = new ViewController(uContext);

        changeMode(Mode.MODE_B);
    }

    @Override
    public void initWindow(ViewGroup wrapper) {
        viewController.show(wrapper);
    }

    @Override
    public void increase(int param) {
        frontController.increase(param);
    }

    @Override
    public void decrease(int param) {
        frontController.decrease(param);
    }

    @Override
    public void reset(int param) {
        frontController.reset(param);
    }

    @Override
    public void freeze() {
        frontController.freeze();
        dataController.disableProcess();
    }

    @Override
    public void unfreeze() {
        frontController.unfreeze();
        dataController.enableProcess();

        changeMode(uContext.getModeSwitcher().getMode());
    }

    @Override
    public boolean isFrozen() {
        return frontController.isFrozen();
    }

    @Override
    public void changeMode(Mode mode) {
        frontController.setMode(mode);
        switch (mode){
            case MODE_B:
                dataController.enableProcess(true, false);
                break;
            case MODE_M:
                dataController.enableProcess(false, true);
                break;
            case MODE_BB:
                dataController.enableProcess(true, false);
                break;
            case MODE_BM:
                dataController.enableProcess(true, false);
                break;
        }
        viewController.setMode(mode, dataController.getMainProcessor());
    }

    @Override
    public void changePseudoColor(Colors.PseudoColor color) {
        dataController.setPseudoColor(color);
    }

    @Override
    public void takeSnapshot() {
        viewController.saveViewBitmap();
    }

    @Override
    public void showSnapshot(Bitmap snapshot) {
        freeze();
        viewController.displayImage(snapshot);
    }

    @Override
    public void replay() {
        //在回放前，先确定已经进入冻结状态
        if(isFrozen()){
            PlayProcessor playProcessor = dataController.getPlayProcessor();
            viewController.replayMode(playProcessor);
            playProcessor.replay();
        }
    }

    @Override
    public void play(File video) {
        if(isFrozen()){
            PlayProcessor playProcessor = dataController.getPlayProcessor();
            viewController.replayMode(playProcessor);
            playProcessor.play(video);
        }
    }

    @Override
    public void saveVideo() {
        if(uContext.getImageHolder().isEmpty()){
            Toast.makeText(uContext.getAndroidContext(), "当前没有任何回放！", Toast.LENGTH_LONG).show();
            return;
        }
        boolean rst = dataController.saveVideo();
        Toast t = Toast.makeText(uContext.getAndroidContext(), "保存" + (rst ? "成功" : "失败！"), Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        t.show();
    }
}
