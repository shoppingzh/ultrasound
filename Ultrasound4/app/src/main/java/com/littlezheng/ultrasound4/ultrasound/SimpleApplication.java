package com.littlezheng.ultrasound4.ultrasound;

import android.view.ViewGroup;

import com.littlezheng.ultrasound4.framework.graphics.measuremaker.ShapeMaker;
import com.littlezheng.ultrasound4.ultrasound.component.Colors;
import com.littlezheng.ultrasound4.ultrasound.component.Mode;
import com.littlezheng.ultrasound4.ultrasound.display.WindowController;
import com.littlezheng.ultrasound4.ultrasound.process.FrameCreatorFacade;
import com.littlezheng.ultrasound4.ultrasound.transfer.TransferFacade;

import java.io.File;

/**
 * Created by Administrator on 2017/9/24/024.
 */

public class SimpleApplication implements IApplication {

    private UContext uContext;

    private TransferFacade transferFacade;
    private FrameCreatorFacade frameCreatorFacade;
    private WindowController windowController;

    public SimpleApplication(UContext uContext, ViewGroup wrapper) {
        this.uContext = uContext;
        transferFacade = new TransferFacade(uContext);
        frameCreatorFacade = new FrameCreatorFacade(uContext);
        windowController = new WindowController(uContext, wrapper);
    }

    @Override
    public void freeze() {
        //发送冻结指令并关闭处理线程
        transferFacade.freeze();
        frameCreatorFacade.disable();
    }

    @Override
    public void unfreeze() {
        transferFacade.unfreeze();
        Mode mode = uContext.getModeSwitcher().getMode();
        frameCreatorFacade.enable(mode);
        setMode(mode);
    }

    @Override
    public boolean isFrozen() {
        return transferFacade.isFrozen();
    }

    @Override
    public void increase(int param) {
        transferFacade.increase(param);
    }

    @Override
    public void decrease(int param) {
        transferFacade.decrease(param);
    }

    @Override
    public void reset(int param) {
        transferFacade.reset(param);
    }

    @Override
    public void setMode(Mode mode) {
        transferFacade.setMode(mode);
//        frameCreatorFacade.enable(mode);
        windowController.changeMode(mode, frameCreatorFacade.getCreator(mode));
    }

    @Override
    public void changePseudoColor(Colors.PseudoColor color) {
        uContext.getColors().change(color);
    }

    @Override
    public void takeSnapshot() {
        windowController.startCaptureScreen();
    }

    @Override
    public void showSnapshot(File snapshot) {
        windowController.showImage(snapshot);
    }

    @Override
    public void saveVideo() {

    }

    @Override
    public void replay() {

    }

    @Override
    public void play(File video) {

    }

    @Override
    public void calculateAndDisplayBackFat() {
        windowController.showBackFat(frameCreatorFacade.getCurrentBackFat());
    }

    @Override
    public void startMeasure(ShapeMaker shapeMaker) {
        windowController.startMeasure(shapeMaker);
    }

    @Override
    public void clearMeasures() {
        windowController.clearMeasures();
    }

}
