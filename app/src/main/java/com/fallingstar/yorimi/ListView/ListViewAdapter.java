package com.fallingstar.yorimi.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.fallingstar.yorimi.Helper.Alarm.AlarmHelper;
import com.fallingstar.yorimi.Helper.Calculation.CalculationHelper;
import com.fallingstar.yorimi.Helper.Database.DatabaseHelper;
import com.fallingstar.yorimi.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jiran on 2017-04-27.
 */

public class ListViewAdapter extends BaseAdapter {
    /*
    Define variables.
     */
    private AlarmHelper helper;
    private DatabaseHelper DBHelper;
    private Context mainContext;
    public ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
    private CalculationHelper myCalcHelper = new CalculationHelper();

    /*
    purpose : Initiator for ListViewAdapter
     */
    public ListViewAdapter(Context _context, DatabaseHelper _DBHelper) {
        mainContext = _context;
        DBHelper = _DBHelper;
    }

    /*
     purpose : Return the number of data of Adapter
      */
    public int getCount() {
        return listViewItemList.size();
    }

    /*
    purpose : Return the View instance that will use to print a data,
                that has specific postion value 'position'.
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        /*
         Get convertView's reference by inflate the layout.listview_time.
          */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        /*
         Get reference from View(that inflate from layout.listview_item).
          */
        final TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1);
        final TextView descTextView = (TextView) convertView.findViewById(R.id.textView2);
        Button button = (Button) convertView.findViewById(R.id.button2);

        /*
         Get data reference from data set(listViewitemList) that placed at 'position'.
          */
        final ListViewItem listViewItem = listViewItemList.get(position);
        /*
         Apply the data to each widget that in listViewItem.
          */
        titleTextView.setText(listViewItem.getTitle());
        if (DBHelper.getAlarmSet(position+1))
        {
            descTextView.setText(getCostResult(position));
        }
        else
        {
            if(DBHelper.getoptRuleBool(position+1)==0)
                descTextView.setText("매 " + DBHelper.getMainRuleTime(position+1) + "분 마다 " + DBHelper.getMainRulePrice(position+1) + "원");
            else
                descTextView.setText("첫 " + DBHelper.getMainRuleTime(position+1) + "분 까지 " + DBHelper.getMainRulePrice(position+1) + "원, 매 " + DBHelper.getoptRuleTime(position+1) + "분 마다 " + DBHelper.getoptRulePrice(position+1) + "원");
        }

        descTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DBHelper.getAlarmSet(position+1))
                {
                    descTextView.setText(getCostResult(position));
                }
                else
                {
                    if(DBHelper.getoptRuleBool(position+1)==0)
                        descTextView.setText("매 " + DBHelper.getMainRuleTime(position+1) + "분 마다 " + DBHelper.getMainRulePrice(position+1) + "원");
                    else
                        descTextView.setText("첫 " + DBHelper.getMainRuleTime(position+1) + "분 까지 " + DBHelper.getMainRulePrice(position+1) + "원, 매 " + DBHelper.getoptRuleTime(position+1) + "분 마다 " + DBHelper.getoptRulePrice(position+1) + "원");
                }
            }
        });
        if (DBHelper.getAlarmSet(position+1)){
            button.setText("End");
        }
        else{
            button.setText("Start");
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = ((Button) v).getText().toString();
                int delay = 0;
                int period = 60*1000;
                final Timer timerUpdate = new Timer();

                if (str.equalsIgnoreCase("Start")) {
                    ((Button) v).setText("End");

                    timerUpdate.scheduleAtFixedRate(new TimerTask() {
                        public void run() {
                            final Runnable timerAction = new Runnable() {
                                @Override
                                public void run() {
                                    descTextView.setText(getCostResult(position));
                                }
                            };
                            descTextView.post(timerAction);
                        }
                    }, delay, period);

                    DBHelper.updateState(position+1, true);
                    setAlarm(position+1);
                } else if(str.equalsIgnoreCase("End")) {
                    ((Button) v).setText("Start");
                    descTextView.setText(getCostResult(position));

                    timerUpdate.cancel();
                    final Timer reset = new Timer();
                    reset.schedule(new TimerTask() {
                    public void run() {
                        final Runnable timerAction = new Runnable() {
                            @Override
                            public void run() {
                                if(DBHelper.getoptRuleBool(position+1)==0)
                                    descTextView.setText("매 " + DBHelper.getMainRuleTime(position+1) + "분 마다 " + DBHelper.getMainRulePrice(position+1) + "원");
                                else
                                    descTextView.setText("첫 " + DBHelper.getMainRuleTime(position+1) + "분 까지 " + DBHelper.getMainRulePrice(position+1) + "원, 매 " + DBHelper.getoptRuleTime(position+1) + "분 마다 " + DBHelper.getoptRulePrice(position+1) + "원");
                            }
                        };
                        descTextView.post(timerAction);
                    }
                }, 1000);

                    DBHelper.updateState(position+1, false);
                    removeAlarm(position+1);
                }
            }
        });

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    /*
    purpose : add item to list with title and description.
     */
    public void addItem(String title, String desc, Boolean state) {
        ListViewItem item = new ListViewItem();

        item.setTitle(title);
        item.setDesc(desc);

        if(state)
            item.setButtonStr("End");
        else
            item.setButtonStr("Start");

        listViewItemList.add(item);
    }

    public void modifyItem(int pos, String title, String desc, Boolean state){
        ListViewItem item = listViewItemList.get(pos);

        item.setTitle(title);
        item.setDesc(desc);

        if(state)
            item.setButtonStr("End");
        else
            item.setButtonStr("Start");
    }

    /*
    purpose : Set alarm by using DB Index (index of collection view postion + 1) to ID value.
     */
    private void setAlarm(int _DBIdx) {
        CalculationHelper myCalcHelper = new CalculationHelper();
        int DBIdx = _DBIdx;
        int delay = Integer.parseInt(DBHelper.getPushAlarm(DBIdx));

        if (DBHelper.getoptRuleBool(DBIdx)==1){ //If there's option rule,
            helper = new AlarmHelper(
                    DBHelper.getTitle(DBIdx)
                    ,DBHelper.getMainRuleTime(DBIdx)
                    ,DBHelper.getMainRulePrice(DBIdx)
                    ,DBHelper.getoptRuleTime(DBIdx)
                    ,DBHelper.getoptRulePrice(DBIdx));
            helper.registerAlarm(mainContext, delay, DBIdx);
        }else{
            helper = new AlarmHelper( //If there's no option rule,
                    DBHelper.getTitle(DBIdx)
                    ,DBHelper.getMainRuleTime(DBIdx)
                    ,DBHelper.getMainRulePrice(DBIdx));
            helper.registerAlarm(mainContext, delay, DBIdx);
        }
        //Save current time for notice the alarm
        myCalcHelper.setStartMillis(System.currentTimeMillis(), DBIdx-1);
        myCalcHelper.setTimeAndCost(DBIdx-1);
    }

    /*
    purpose : Cancel and remove alarm by using DB Index (index of collection view postion + 1) to ID value.
     */
    private void removeAlarm(int _DBIdx) {
        int DBIdx = _DBIdx;

        if (DBHelper.getoptRuleBool(DBIdx)==1){
            helper = new AlarmHelper(
                    DBHelper.getTitle(DBIdx)
                    ,DBHelper.getMainRuleTime(DBIdx)
                    ,DBHelper.getMainRulePrice(DBIdx)
                    ,DBHelper.getoptRuleTime(DBIdx)
                    ,DBHelper.getoptRulePrice(DBIdx));
            helper.unRegisterAlarm(mainContext, DBIdx);
        }else{
            helper = new AlarmHelper(
                    DBHelper.getTitle(DBIdx)
                    ,DBHelper.getMainRuleTime(DBIdx)
                    ,DBHelper.getMainRulePrice(DBIdx));
            helper.unRegisterAlarm(mainContext, DBIdx);
        }
    }

    private int calculateElapsedMinMinutes(int idx){
        long timeDiff;
        int min;

        timeDiff = myCalcHelper.getDiffFromPrevTimeWithMilliSec(System.currentTimeMillis(), idx);
        min = myCalcHelper.getMinuteFromMilliSec(timeDiff);

        return min;
    }

    private int calculateCost(int idx, int min){
        int originCost = myCalcHelper.getMainRuleCost(idx);
        int originMainTime = myCalcHelper.getMainRuleTime(idx);

        if (DBHelper.getoptRuleBool(idx+1)==0){
            if (min < originMainTime){
                return originCost;
            }else{
                return originCost + calculateMainCost(idx, min-originMainTime);
            }
        }else {
            if (min <  originMainTime){
                return originCost;
            }else{
                return originCost + calculateOptCost(idx, min-originMainTime);
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

    public String getCostResult(int id)
    {
        int time = calculateElapsedMinMinutes(id);
        int cost = calculateCost(id, time);
        String result = "경과 시간 : "+ time + "분, 약 "+ cost +"원";

        return result;
    }
}
