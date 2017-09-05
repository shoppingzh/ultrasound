package com.littlezheng.ultrasound2.ultrasound.transmission.validate;

import android.util.Log;

/**
 * Created by zxp on 2017/5/25.
 */

public class UdpPacket406Validator extends UdpPacketValidator {

    private static final String TAG = "UdpPacket406Validator";

    private final int packetSize;

    /*----------------------校验位----------------------*/

    private final int headOne;

    private final int headTwo;

    private final int lastIdIdx;

    private final int idIdx;

    private final int tailOne;

    private final int tailTwo;

    public UdpPacket406Validator(){
        this.packetSize = 406;
        this.headOne = 0;
        this.headTwo = 1;
        this.lastIdIdx = packetSize - 4;
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

        int lastId = pd[lastIdIdx] & 0xff;
        int id = pd[idIdx] & 0xff;
        if(lastId+1 != id || id < 1 || id > 127){
            Log.d(TAG,"校/验错误，上一条线id："+lastId+"，当前线id："+id);
            return false;
        }

        return true;
    }




}
