package com.littlezheng.ultrasound4.ultrasound.display;

import com.littlezheng.ultrasound4.framework.view.AbstractDisplayStrategy;
import com.littlezheng.ultrasound4.ultrasound.UContext;

/**
 * Created by Administrator on 2017/9/23/023.
 */

public abstract class UAbstractDisplayStrategy extends AbstractDisplayStrategy {

    protected UContext uContext;

    public UAbstractDisplayStrategy(UContext uContext) {
        super(uContext.getAndroidContext());
        this.uContext = uContext;
    }

}
