package com.littlezheng.ultrasound.ultrasound.transfer.validate;

/**
 * Created by zxp on 2017/5/25.
 */

public class UdpPacket405Validator extends UdpPacketValidator {

    private static final String TAG = "UdpPacket406Validator";

    private final int packetSize;

    /*----------------------校验位----------------------*/

    private final int headOne;

    private final int headTwo;

    private final int idIdx;

    private final int tailOne;

    private final int tailTwo;

    public UdpPacket405Validator(int packetSize){
        this.packetSize = packetSize;
        this.headOne = 0;
        this.headTwo = 1;
        this.idIdx = packetSize - 3;
        this.tailOne = packetSize - 2;
        this.tailTwo = packetSize - 1;
    }

    @Override
    public boolean validate(byte[] pd) {
        //校验头尾
        if(pd[headOne] != 0x55 || (pd[headTwo]&0xff) != 0xaa
                || (pd[tailOne]&0xff) != 0xaa || pd[tailTwo] != 0x55){
            return false;
        }

        int id = pd[idIdx] & 0xff;
        if(id < 1 || id > 159){
            return false;
        }

        return true;
    }




}
