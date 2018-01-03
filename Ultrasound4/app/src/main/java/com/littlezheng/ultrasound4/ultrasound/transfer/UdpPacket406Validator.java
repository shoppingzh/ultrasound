package com.littlezheng.ultrasound4.ultrasound.transfer;

import android.util.Log;

import com.littlezheng.ultrasound4.framework.udp.UdpPacketValidator;

/**
 * Created by zxp on 2017/5/25.
 */

public class UdpPacket406Validator extends UdpPacketValidator {

    private static final String TAG = "UdpPacket406Validator";

    private final int packetSize = 406;

    /*----------------------校验位----------------------*/

    private final int headOne = 0;

    private final int headTwo = 1;

    private final int lastIdIdx = packetSize - 4;

    private final int idIdx = packetSize - 3;

    private final int tailOne = packetSize - 2;

    private final int tailTwo = packetSize - 1;

    @Override
    public boolean validate(byte[] pd) {
        //校验头尾
        if (pd[headOne] != 0x55 || (pd[headTwo] & 0xff) != 0xaa
                || (pd[tailOne] & 0xff) != 0xaa || pd[tailTwo] != 0x55) {
            return false;
        }

        int lastId = pd[lastIdIdx] & 0xff;
        int id = pd[idIdx] & 0xff;
        if (lastId + 1 != id || id < 1 || id > 127) {
            Log.d(TAG, "校验错误，上一条线id：" + lastId + "，当前线id：" + id);
            return false;
        }

        return true;
    }


}
