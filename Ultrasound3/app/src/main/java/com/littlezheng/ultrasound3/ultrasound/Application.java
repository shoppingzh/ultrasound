package com.littlezheng.ultrasound3.ultrasound;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Toast;

import com.littlezheng.ultrasound3.ultrasound.base.Colors;
import com.littlezheng.ultrasound3.ultrasound.base.Mode;
import com.littlezheng.ultrasound3.ultrasound.mvc.Controller;
import com.littlezheng.ultrasound3.ultrasound.mvc.Model;
import com.littlezheng.ultrasound3.ultrasound.mvc.View;
import com.littlezheng.ultrasound3.ultrasound.process.PlayProcessor;

import java.io.File;

/**
 * Created by Administrator on 2017/9/5/005.
 */

public class Application implements IApplication {

    private final UContext uContext;

    //mvc
    private final Controller controller;
    private final Model model;
    private final View view;

    public Application(UContext uContext) {
        this.uContext = uContext;
        controller = new Controller(uContext);
        model = new Model(uContext);
        view = new View(uContext);

        //默认B模式
        setMode(Mode.MODE_B);
    }

    @Override
    public void freeze() {
        controller.freeze();
        model.disableProcess();
    }

    @Override
    public void unfreeze() {
        controller.unfreeze();
        model.enableProcess();

        setMode(uContext.getModeSwitcher().getMode());
    }

    @Override
    public boolean isFrozen() {
        return controller.isFrozen();
    }

    @Override
    public void increase(int param) {
        controller.increase(param);
    }

    @Override
    public void decrease(int param) {
        controller.decrease(param);
    }

    @Override
    public void reset(int param) {
        controller.reset(param);
    }

    @Override
    public void initWindow(ViewGroup wrapper) {
        view.show(wrapper);
    }

    @Override
    public void setMode(Mode mode) {
        controller.setMode(mode);
//        switch (mode){
//            case MODE_B:
//                model.enableProcess(true, false);
//                break;
//            case MODE_M:
//                model.enableProcess(false, true);
//                break;
//            case MODE_BB:
//                model.enableProcess(true, false);
//                break;
//            case MODE_BM:
//                model.enableProcess(false, true);
//                break;
//        }
        view.setMode(mode, model.getMainProcessor());
    }

    @Override
    public void changePseudoColor(Colors.PseudoColor color) {
        model.setPseudoColor(color);
    }

    @Override
    public void takeSnapshot() {
        view.saveViewBitmap();
    }

    @Override
    public void showSnapshot(File snapshot) {
        freeze();
        view.displayImage(snapshot);
    }

    @Override
    public void saveVideo() {
        if (uContext.getImageHolder().isEmpty()) {
            Toast.makeText(uContext.getAndroidContext(), "当前没有任何回放！", Toast.LENGTH_LONG).show();
            return;
        }
        boolean rst = model.saveVideo();
        Toast t = Toast.makeText(uContext.getAndroidContext(), "保存" + (rst ? "成功" : "失败！"), Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        t.show();
    }

    @Override
    public void replay() {
        //在回放前，先确定已经进入冻结状态
        if (isFrozen()) {
            PlayProcessor playProcessor = model.getPlayProcessor();
            view.replayMode(playProcessor);
            playProcessor.replay();
        }
    }

    @Override
    public void play(File video) {
        if (isFrozen()) {
            PlayProcessor playProcessor = model.getPlayProcessor();
            view.replayMode(playProcessor);
            playProcessor.play(video);
        }
    }

    @Override
    public void calculateAndDisplayBackFat() {
        if (!isFrozen()) {
            Toast.makeText(uContext.getAndroidContext(), "请先冻结！", Toast.LENGTH_SHORT).show();
            return;
        }
        model.calculateBackFat();
    }

}
