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

import com.fallingstar.yorimi.Helper.Calculation.CalculationHelper;
import com.fallingstar.yorimi.R;
import com.fallingstar.yorimi.MainActivity;

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

        return START_REDELIVER_INTENT;
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
            Intent intent = new Intent(AlarmService.this, MainActivity.class);
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
        }

        private int calculateElapsedMinMinutes(){
            long timeDiff;
            int min;

            timeDiff = myCalcHelper.getDiffFromPrevTimeWithMilliSec(System.currentTimeMillis(), ruleID);
            min = myCalcHelper.getMinuteFromMilliSec(timeDiff);

            return min;
        }

        private int calculateCost(int min){
            int originCost = myCalcHelper.getMainRuleCost(ruleID);
            int originMainTime = myCalcHelper.getMainRuleTime(ruleID);

            if (isOnlyMain){
                if (min < originMainTime){
                    return originCost;
                }else{
                    return originCost + calculateMainCost(ruleID, min-originMainTime);
                }
            }else {
                if (min <  originMainTime){
                    return originCost;
                }else{
                    return originCost + calculateOptCost(ruleID, min-originMainTime);
                }
            }
        }
        private int calculateMainCost(int idx, int min){
            int costPerMin, totalCost;
            costPerMin = Math.round((float)myCalcHelper.getCostPerMinute(myCalcHelper.getMainRuleTime(idx), myCalcHelper.getMainRuleCost(idx)));

            totalCost = costPerMin*min;
            return totalCost;
        }
        private int calculateOptCost(int idx, int min){
            int costPerMin, totalCost;
            costPerMin = Math.round((float)myCalcHelper.getCostPerMinute(myCalcHelper.getOptRuleTime(idx) , myCalcHelper.getOptRuleCost(idx)));

            totalCost = costPerMin*min;
            return totalCost;
        }
    }
}
