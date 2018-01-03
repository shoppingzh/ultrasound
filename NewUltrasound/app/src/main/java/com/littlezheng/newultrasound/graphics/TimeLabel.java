package com.littlezheng.newultrasound.graphics;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/11/3/003.
 */

public class TimeLabel extends Label {

//    private static final Timer timer = new Timer(true);
//    private static List<TimerListener> listeners = new CopyOnWriteArrayList<>();
//    static {
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                for(TimerListener listener : listeners){
//                    listener.onTimeChanged();
//                }
//            }
//        }, new Date(), 1000);
//    }

    private DateFormat df;

    public TimeLabel() {
        this("yyyy-MM-dd HH:mm:ss");
        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                setText(getTime());
            }
        }, new Date(), 1000);
    }

    public TimeLabel(String timePattern) {
        df = new SimpleDateFormat(timePattern);
        setText(getTime());
    }

    public void setTimePattern(String timePattern) {
        df = new SimpleDateFormat(timePattern);
    }

    private String getTime() {
        return df.format(new Date());
    }

}