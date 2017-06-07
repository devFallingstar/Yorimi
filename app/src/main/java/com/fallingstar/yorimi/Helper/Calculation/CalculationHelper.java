package com.fallingstar.yorimi.Helper.Calculation;

import com.fallingstar.yorimi.Helper.Database.DatabaseHelper;
import com.fallingstar.yorimi.MainActivity;

/**
 * Created by stard on 2017-05-16.
 */

public class CalculationHelper {
    /*
    Define variables.
     */
    private static long[] startMillis;
    private static int[] mainTime;
    private static int[] mainCost;
    private static int[] optTime;
    private static int[] optCost;
    private static boolean isMade = false;
    private static DatabaseHelper db;
    private static int size;
    /*
    purpose : Constructor of CalculationHelper.
    If the db instance was already made, initialization is blocked, So the every class can use only one static instance with a memory saving
     */
    public CalculationHelper(){
        if (!isMade){
            db = MainActivity.getYoribi();

            size = db.getCount();

            mainTime = new int[size];
            mainCost = new int[size];
            optTime = new int[size];
            optCost = new int[size];
            startMillis = new long[size];
            isMade = true;
        }
    }
    public void setTimeAndCost(int idx){
        mainTime[idx] = Integer.parseInt(db.getMainRuleTime(idx+1));
        mainCost[idx] = Integer.parseInt(db.getMainRulePrice(idx+1));
        optTime[idx] = Integer.parseInt(db.getoptRuleTime(idx+1));
        optCost[idx] = Integer.parseInt(db.getoptRulePrice(idx+1));
    }
    /*
    purpose : Class that help to set milliseconds of each alarms
     */
    public void setStartMillis(long _millis, int idx){
        /* save previous data */
        size = db.getCount();
        int i;
        long[] tempMillis = new long[size];
        int[] tempMainTime = new int[size];
        int[] tempMainCost = new int[size];
        int[] tempOptTime = new int[size];
        int[] tempOptCost = new int[size];

        for (i = 0; i < size-1; i++){
            tempMainTime[i] = mainTime[i];
            tempMainCost[i] = mainCost[i];
            tempOptTime[i] = optTime[i];
            tempOptCost[i] = optCost[i];
            tempMillis[i] = startMillis[i];
        }

        /* load and save current data */
        mainTime = new int[size];
        mainCost = new int[size];
        optTime = new int[size];
        optCost = new int[size];
        startMillis = new long[size];
        tempMillis[idx] = _millis;

        for (i = 0; i < size; i++){
            mainTime[i] = tempMainTime[i];
            mainCost[i] = tempMainCost[i];
            optTime[i] = tempOptTime[i];
            optCost[i] = tempOptCost[i];
            startMillis[i] = tempMillis[i];
        }
    }
    /*
   purpose : Class that help to calculate cost per minutes.
    */
    public double getCostPerMinute(int time, int cost){
        return (double) cost/time;
    }
    /*
  purpose : Class that set current time and help to calculate difference between two times
   */
    public long getDiffFromPrevTimeWithMilliSec(long prevTime, int idx){
            long currentTime = startMillis[idx];
        return getDiffOfTwoMilliSec(prevTime, currentTime);
    }
    /*
    purpose : Class that help to change milliseconds to seconds,minutes,hours.
     */
    public int getMinuteFromMilliSec(long milliSec){
        int sec,min,hour;
        float totalMinD;
        int totalMinI;
        milliSec=milliSec/1000;
        sec=(int)milliSec%60;
        min=(int)milliSec/60%60;
        hour=(int)milliSec/3600;

        totalMinD = (sec/60) + (min) + (hour*60);
        totalMinI = Math.round(totalMinD);

        return totalMinI;
    }
    /*
    purpose : Class that help to calculate difference between current time and previous time.
     */
    private long getDiffOfTwoMilliSec(long prevTime, long currentTime){
        return prevTime-currentTime;
    }

    public int getMainRuleTime(int RuleID){
        return mainTime[RuleID];
    }
    public int getMainRuleCost(int RuleID){
        return mainCost[RuleID];
    }
    public int getOptRuleTime(int RuleID){
        return optTime[RuleID];
    }
    public int getOptRuleCost(int RuleID){
        return optCost[RuleID];
    }

}
