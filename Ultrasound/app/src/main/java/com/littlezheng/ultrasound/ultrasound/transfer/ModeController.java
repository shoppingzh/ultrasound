package com.littlezheng.ultrasound.ultrasound.transfer;

/**
 * Created by Administrator on 2017/8/22/022.
 */

public class ModeController extends DataReturner {

    private Mode mode;

    public ModeController(int controlCode) {
        super(controlCode);
    }

    public void setMode(Mode mode){
        setData(mode.getProtocol());
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public enum Mode{
        MODE_B(0x00), MODE_BB(0x50), MODE_BM(0xD0), MODE_M(0xE0);

        private int protocol;
        private Mode(int protocol){
            this.protocol = protocol;
        }

        public int getProtocol() {
            return protocol;
        }
    }

}
