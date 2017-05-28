package com.fallingstar.yorimi.Helper.Calculation;

import com.fallingstar.yorimi.Helper.Database.DatabaseHelper;
import com.fallingstar.yorimi.MainActivity;

import java.util.ArrayList;

/**
 * Created by stard on 2017-05-16.
 */

public class CalculationHelper {

    private static long[] startMillis;
    private static boolean isMade = false;
    private static DatabaseHelper db;
    private static int size;

    public CalculationHelper(){
        if (!isMade){
            db = MainActivity.getYoribi();
            size = db.getCount();
            startMillis = new long[size];
            isMade = true;
        }
    }

    public void setStartMillis(long _millis, int idx){
        /* save previous data */
        int i;
        long[] tempMillis = new long[db.getCount()];
        for (i = 0; i < size; i++){
            tempMillis[i] = startMillis[i];
        }

        /* load and save current data */
        size = db.getCount();
        startMillis = new long[size];
        tempMillis[idx] = _millis;
        for (i = 0; i < size; i++){
            startMillis[i] = tempMillis[i];
        }
    }
    public long getStartMillis(int idx){
        return startMillis[idx];
    }
    public double getCostPerMinute(int time, int cost){
        return (double) cost/time;
    }
    public long getDiffFromPrevTimeWithMilliSec(long prevTime, int idx){
        long currentTime = startMillis[idx];
        return getDiffOfTwoMilliSec(prevTime, currentTime);
    }
    public int getMinuteFromMilliSec(long milliSec){
        int sec,min,hour;
        float totalMinD = 0;
        int totalMinI = 0;
        milliSec=milliSec/1000;
        sec=(int)milliSec%60;
        min=(int)milliSec/60%60;
        hour=(int)milliSec/3600;

        totalMinD = (sec/60) + (min) + (hour*60);
        totalMinI = Math.round(totalMinD);

        return totalMinI;
    }
    private long getDiffOfTwoMilliSec(long prevTime, long currentTime){
        return prevTime-currentTime;
    }

}
