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
import android.util.Log;
import android.widget.Toast;

import com.fallingstar.yorimi.Helper.Calculation.CalculationHelper;
import com.fallingstar.yorimi.Helper.Database.DatabaseHelper;
import com.fallingstar.yorimi.R;
import com.fallingstar.yorimi.ViewActivity;

/*
purpose : Class for Alarm System Service that receive message from AlarmReceiver and start the command.
 */
public class AlarmService extends Service {
    /*
   Define variables.
    */
    private String ruleTitle;
    private int ruleID;
    private boolean isOnlyMain;
    private NotificationManager notiManager;
    private Notification noti;
    private ServiceThread thread;
    private DatabaseHelper db = ViewActivity.getYoribi();
    private CalculationHelper myCalcHelper = new CalculationHelper();

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
        ruleID = Integer.parseInt(intent.getStringExtra("IDX"))-1; //minus 1 to match with array index.
        ruleTitle = intent.getStringExtra("TITLE");
        isOnlyMain = intent.getBooleanExtra("ISONLYMAIN", false);

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

            int cost, elapsedMin;
            Intent intent = new Intent(AlarmService.this, ViewActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(AlarmService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            elapsedMin = calculateElapsedMinMinutes();
            cost = calculateCost(elapsedMin);

            noti = new Notification.Builder(getApplicationContext())
                    .setContentTitle(ruleTitle)
                    .setContentText("경과 시간 : "+elapsedMin + "분, 약 "+cost+"원")
                    .setSmallIcon(R.mipmap.ic_yorimi)
                    .setTicker("약 "+cost+"원")
                    .setContentIntent(pIntent)
                    .build();
            noti.defaults = Notification.DEFAULT_SOUND;
            noti.flags = Notification.FLAG_ONLY_ALERT_ONCE;
            noti.flags = Notification.FLAG_AUTO_CANCEL;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                noti.category = Notification.CATEGORY_MESSAGE;
                noti.priority = Notification.PRIORITY_HIGH;
                noti.visibility = Notification.VISIBILITY_PUBLIC;
            }
            notiManager.notify(ruleID, noti);

            Toast.makeText(AlarmService.this, "경과 시간 : "+elapsedMin + "\n" +
                    "요금 : 약 "+cost+"원", Toast.LENGTH_SHORT).show();
            Log.d("CHECK", "경과 시간 : "+elapsedMin + "\n" +
                    "요금 : 약 "+cost+"원 "+ruleID);
        }

        private int calculateElapsedMinMinutes(){
            long timeDiff;
            int min;

            timeDiff = myCalcHelper.getDiffFromPrevTimeWithMilliSec(System.currentTimeMillis(), ruleID);
            min = myCalcHelper.getMinuteFromMilliSec(timeDiff);

            return min;
        }

        private int calculateCost(int min){
            int originCost = Integer.parseInt(db.getMainRulePrice(ruleID+1));
            int originMainTime = Integer.parseInt(db.getMainRuleTime(ruleID+1));
            int originOptTime = Integer.parseInt(db.getoptRuleTime(ruleID+1));

            if (isOnlyMain){
                if (min < Integer.parseInt(db.getMainRuleTime(ruleID+1))){
                    return originCost;
                }else{
                    return originCost + calculateMainCost(min-originMainTime);
                }
            }else {
                if (min <  Integer.parseInt(db.getMainRuleTime(ruleID+1))){
                    return originCost;
                }else{
                    return originCost + calculateOptCost(min-originMainTime);
                }
            }

        }
        private int calculateMainCost(int min){
            int costPerMin, totalCost;
            costPerMin = Math.round((float)myCalcHelper.getCostPerMinute(Integer.parseInt(db.getMainRuleTime(ruleID+1)) , Integer.parseInt(db.getMainRulePrice(ruleID+1))));

            totalCost = costPerMin*min;
            return totalCost;
        }
        private int calculateOptCost(int min){
            int costPerMin, totalCost;
            costPerMin = Math.round((float)myCalcHelper.getCostPerMinute(Integer.parseInt(db.getoptRuleTime(ruleID+1)) , Integer.parseInt(db.getoptRulePrice(ruleID+1))));

            totalCost = costPerMin*min;
            return totalCost;
        }
    }
}
