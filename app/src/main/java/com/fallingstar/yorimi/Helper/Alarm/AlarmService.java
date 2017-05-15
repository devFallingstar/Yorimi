package com.fallingstar.yorimi.Helper.Alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.fallingstar.yorimi.MainActivity;
import com.fallingstar.yorimi.R;

/*
purpose : Class for Alarm System Service that receive message from AlarmReceiver and start the command.
 */
public class AlarmService extends Service {
    /*
   Define variables.
    */
    private String ruleTitle;
    private int ruleID;
    private NotificationManager notiManager;
    private Notification noti;
    private ServiceThread thread;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
    purpose : Get NotificationManager instance from system service and notify the notification
            with handler and thread.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ruleID = Integer.parseInt(intent.getStringExtra("IDX"));
        ruleTitle = intent.getStringExtra("TITLE");

        notiManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        MyNotiHandler handler = new MyNotiHandler();
        thread = new ServiceThread(handler);
        thread.start();

        return START_NOT_STICKY;
    }

    /*
    purpose : Put null value to clear the variable as fast as garbage collector can.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        thread = null;
    }

    /*
    purpose : Make intent with Notification instance and notify it with ID value
     */
    class MyNotiHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(AlarmService.this, MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(AlarmService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            noti = new Notification.Builder(getApplicationContext())
                    .setContentTitle(ruleTitle)
                    .setContentText("내용!")
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setTicker("Yorimi")
                    .setContentIntent(pIntent)
                    .build();
            noti.defaults = Notification.DEFAULT_SOUND;
            noti.flags = Notification.FLAG_ONLY_ALERT_ONCE;
            noti.flags = Notification.FLAG_AUTO_CANCEL;
            notiManager.notify(ruleID, noti);

            Toast.makeText(AlarmService.this, "Working? "+ruleID, Toast.LENGTH_SHORT).show();
        }
    }
}
