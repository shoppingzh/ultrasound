package com.littlezheng.ultrasound4.ultrasound.display;

import com.littlezheng.ultrasound4.framework.view.DisplayStrategy;
import com.littlezheng.ultrasound4.framework.view.MultipleDisplayStrategy;
import com.littlezheng.ultrasound4.ultrasound.UContext;

/**
 * Created by Administrator on 2017/9/23/023.
 */

public class UMultipleDisplayStrategy extends MultipleDisplayStrategy {

    protected UContext uContext;

    public UMultipleDisplayStrategy(UContext uContext, DisplayStrategy strategy) {
        super(uContext.getAndroidContext(), strategy);
        this.uContext = uContext;
    }


}
