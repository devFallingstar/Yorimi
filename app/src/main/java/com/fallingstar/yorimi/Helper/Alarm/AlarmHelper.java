package com.fallingstar.yorimi.Helper.Alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;

import com.fallingstar.yorimi.MainActivity;

import static android.content.ContentValues.TAG;

/*
purpose : Class that help to set and notify the alarm user set in application.
 */
public class AlarmHelper extends Activity {
    String title;
    boolean isOnlyMain;
    int mainCost, mainTime;
    int optCost, optTime;

    public AlarmHelper (String _title, String _mainTime, String _mainCost){
        this.title = _title;
        this.mainTime = Integer.parseInt(_mainTime);
        this.mainCost = Integer.parseInt(_mainCost);
        this.isOnlyMain = true;
    }
    public AlarmHelper (String _title, String _mainTime, String _mainCost, String _optCost, String _optTime) {
        this.title = _title;
        this.mainTime = Integer.parseInt(_mainTime);
        this.mainCost = Integer.parseInt(_mainCost);
        this.optTime = Integer.parseInt(_optTime);
        this.optCost = Integer.parseInt(_optCost);
        this.isOnlyMain = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void registerAlarm(Context context, int timeMin, int idx)
    {
        AlarmManager manager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("IDX", String.valueOf(idx));
        intent.putExtra("TITLE", this.title);
        intent.putExtra("ISONLYMAIN", isOnlyMain);
        PendingIntent pIntent = PendingIntent.getBroadcast(
                context,
                idx,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), timeMin*(1000*60), pIntent);
    }

    public void unRegisterAlarm(Context context, int idx)
    {
        AlarmManager manager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("IDX", String.valueOf(idx));
        intent.putExtra("TITLE", this.title);
        PendingIntent pIntent = PendingIntent.getBroadcast(
                context,
                idx,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        manager.cancel(pIntent);
    }
}


