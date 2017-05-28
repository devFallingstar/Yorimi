package com.fallingstar.yorimi.Helper.Alarm;

import android.os.Handler;

/*
purpose : This class is needed for creating thread with handler in AlarmService class.
 */
public class ServiceThread extends Thread {
    /*
   Define variables.
    */
    Handler handler;

    public ServiceThread(Handler _handler){
        this.handler = _handler;
    }

    public void run() {
        handler.sendEmptyMessage(0);
    }
}
