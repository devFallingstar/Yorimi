package com.fallingstar.yorimi.Helper.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/*
purpose : Class that receive system broadcast that send from AlarmHelper class.
 */
public class AlarmReceiver extends BroadcastReceiver {

    /*
    purpose : Get ID and send it to the service class with intent extra
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String idxStr = intent.getStringExtra("IDX");
        String titleStr = intent.getStringExtra("TITLE");
        Intent serviceIntent = new Intent(context, AlarmService.class);
        serviceIntent.putExtra("IDX", idxStr);
        serviceIntent.putExtra("TITLE", titleStr);

        context.startService(serviceIntent);
    }
}
