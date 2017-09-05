package com.littlezheng.ultrasound3.ultrasound.display.strategy;

import com.littlezheng.ultrasound3.ultrasound.UContext;

import java.util.Observable;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public class BMModeDisplayStrategyDecorator extends ModeDisplayStrategyDecorator {

    public BMModeDisplayStrategyDecorator(UContext uContext, DisplayStrategy strategy) {
        super(uContext, strategy);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

}
