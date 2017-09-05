package com.littlezheng.transmissionmodule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.littlezheng.transmissionmodule.component.Param;
import com.littlezheng.transmissionmodule.transmission.Fronter;
import com.littlezheng.transmissionmodule.transmission.UdpTransmitter;
import com.littlezheng.transmissionmodule.component.StateController;
import com.littlezheng.transmissionmodule.component.Mode;
import com.littlezheng.transmissionmodule.component.ModeController;
import com.littlezheng.transmissionmodule.component.ParamController;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
//            UdpSender sender = new SimpleUdpSender(InetAddress.getByName("192.168.1.103"), 5001);
//            for(int i=0;i<100;i++){
//                boolean r = sender.send(new byte[]{(byte) i});
//                Log.d(TAG,"结果：" + r );
//            }
//
//            final UdpReceiver receiver = new BufferedUdpReceiver(5001, 400);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while(true){
//                        byte[] data = receiver.receive();
////                        Log.d(TAG,"接收到数据：" + Arrays.toString(data));
////
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        ((BufferedUdpReceiver)receiver).disable(true);
//
//                        data = receiver.receive();
//                        Log.d(TAG,"接收到数据：" + Arrays.toString(data));
//
//
//                    }
//                }
//            }).start();

            UdpTransmitter.Config config = new UdpTransmitter.Config();
//            config.receivePort = 5001;
//            config.receivePackSize = 400;
//            config.receiveBufSize = 100;
//            config.sendPort = 5001;
//            config.sendAddress  = InetAddress.getByName("192.168.1.103");
            config.loadFromStream(getAssets().open("transmission.properties"));
            UdpTransmitter transmitter = new UdpTransmitter(config);
            Fronter fronter = new Fronter(transmitter);
            fronter.increase(Param.CONTRAST);
            fronter.setMode(Mode.MODE_BB);


//            ParamController contrast = new ParamController("对比度",32,48,16,1);
////            ParamProtocol paramPotocol = new ParamProtocol(0x01, contrast);
//
//            StateController stateController = new StateController();
////            FreezeProtocol freezeProtocol = new FreezeProtocol(0xF0, stateController);
//
//            ModeController modeController = new ModeController(Mode.MODE_B);
////            ModeProtocol modeProtocol = new ModeProtocol(0x03, modeController);
//
////            transmitter.addProtocol(paramProtocol);
////            transmitter.addProtocol(freezeProtocol);
////            transmitter.addProtocol(modeProtocol);
//
//            contrast.increase();
//            stateController.freeze();
//            modeController.setMode(Mode.MODE_BM);
//            modeController.setMode(Mode.MODE_M);
//
////            transmitter.deleteProtocol(modeProtocol);
//            modeController.setMode(Mode.MODE_BM);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
