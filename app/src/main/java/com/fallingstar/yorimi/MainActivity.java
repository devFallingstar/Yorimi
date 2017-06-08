package com.fallingstar.yorimi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fallingstar.yorimi.Helper.Alarm.AlarmHelper;
import com.fallingstar.yorimi.Helper.Database.DatabaseHelper;
import com.fallingstar.yorimi.ListView.ListViewAdapter;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class MainActivity extends AppCompatActivity {

    /*
    Define variables.
     */
    final private int MODIFY_REQ = 1000;
    final private int NEW_REQ = 9999;
    private ListView listview;
    private ListViewAdapter adapter;
    private FloatingActionButton fBtn;
    private static DatabaseHelper yoribi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        yoribi = new DatabaseHelper(getApplicationContext());

        ViewPager viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this));
        SmartTabLayout smartTabLayout = (SmartTabLayout)findViewById(R.id.main_viewpagertab);
        smartTabLayout.setViewPager(viewPager);
    }

    public enum CustomPagerEnum {

        HOME(R.string.title_home, R.layout.activity_main),
        INFO(R.string.title_info, R.layout.info);

        private int mTitleResId;
        private int mLayoutResId;

        CustomPagerEnum(int titleResId, int layoutResId) {
            mTitleResId = titleResId;
            mLayoutResId = layoutResId;
        }

        public int getTitleResId() {
            return mTitleResId;
        }

        public int getLayoutResId() {
            return mLayoutResId;
        }

    }

    public class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;

        public CustomPagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewGroup layout = (ViewGroup) inflater.inflate(customPagerEnum.getLayoutResId(), collection, false);

            switch (position) {
                case 0:
                    listview = (ListView) layout.findViewById(R.id.listview);
                    fBtn = (FloatingActionButton) layout.findViewById(R.id.addMarketBtn);
                    listview.setOnItemLongClickListener(new ListViewClickLIstener());

                    initListView();
                    initListWithSQLData();
                    initWidgets();
                    break;
                default:
                    break;
            }

            collection.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return CustomPagerEnum.values().length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
            if(position==0)
                return ViewUtil.iconText(ViewUtil.drawable(mContext, R.drawable.ic_list), "  "+mContext.getString(customPagerEnum.getTitleResId()));
            else
                return ViewUtil.iconText(ViewUtil.drawable(mContext, R.drawable.ic_cube), "  "+mContext.getString(customPagerEnum.getTitleResId()));
        }
    }

    /*
   purpose : Initialize list view.
    */
    private void initListView() {
        adapter = new ListViewAdapter(MainActivity.this, yoribi);
        listview.setAdapter(adapter);
    }

    private void initListWithSQLData() {
        int count = yoribi.getCount();

        initListView();
        for(int i = 1; i <= count; i++)
        {
            if(yoribi.getAlarmSet(i))
                setValuesUpdate(yoribi.getTitle(i), adapter.getCostResult(i-1), yoribi.getAlarmSet(i));
            else
            {
                if(yoribi.getoptRuleBool(i) == 1)
                    setValues(yoribi.getTitle(i), Integer.parseInt(yoribi.getMainRuleTime(i)), Integer.parseInt(yoribi.getMainRulePrice(i)), Integer.parseInt(yoribi.getoptRuleTime(i)), Integer.parseInt(yoribi.getoptRulePrice(i)), yoribi.getAlarmSet(i));
                else
                    setValues(yoribi.getTitle(i), Integer.parseInt(yoribi.getMainRuleTime(i)), Integer.parseInt(yoribi.getMainRulePrice(i)), yoribi.getAlarmSet(i));
            }
        }
    }

    /*
    purpose : Initiate all widgets that should contain listener.
    */
    private void initWidgets() {
        /*
        Floating button with MarketAddActivity intent.
         */
        fBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent marketAddIntent = new Intent(MainActivity.this, MarketAddActivity.class);
                marketAddIntent.putExtra("REQ_id", NEW_REQ);
                startActivityForResult(marketAddIntent, NEW_REQ);
            }
        });
    }

    /*
    purpose : when the marketAddActivity return the intent with result code,
                check its boolean value that represent a type of main rule.
                If the main rule is type of 'until first time n',
                then call setValues(String, int, int, int),
                and if the main rule is type of 'every time n',
                then call setValues(String, int, int, int, int, int).
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_REQ){
            switch (resultCode) {
                case RESULT_OK:
                    Bundle mainBundle = data.getExtras();
                    String RName = data.getStringExtra("ruleName");
                    int mainTime = Integer.parseInt(mainBundle.getString("ruleTime"));
                    int mainCost = Integer.parseInt(mainBundle.getString("ruleCost"));
                    int delay = mainBundle.getInt("notiDelay");
                    boolean alarmSet = mainBundle.getBoolean("alarmSet");

                    if (mainBundle.getBoolean("isMainRuleAdditional")) {
                        setValues(RName, mainTime, mainCost, alarmSet);
                        yoribi.insert(RName, mainTime, mainCost, 0, 0, 0, delay, alarmSet);
                    } else {
                        int optTime = Integer.parseInt(mainBundle.getString("optRuleTime"));
                        int optCost = Integer.parseInt(mainBundle.getString("optRuleCost"));
                        setValues(RName, mainTime, mainCost, optTime, optCost, alarmSet);
                        yoribi.insert(RName, mainTime, mainCost, 1, optTime, optCost, delay, alarmSet);
                    }
                    break;
                default:
                    break;
            }
        }else if (requestCode >= MODIFY_REQ){
            switch (resultCode) {
                case RESULT_OK:
                    Bundle mainBundle = data.getExtras();
                    String RName = data.getStringExtra("ruleName");
                    int mainTime = Integer.parseInt(mainBundle.getString("ruleTime"));
                    int mainCost = Integer.parseInt(mainBundle.getString("ruleCost"));
                    int delay = mainBundle.getInt("notiDelay");
                    boolean alarmSet = mainBundle.getBoolean("alarmSet");

                    if (mainBundle.getBoolean("isMainRuleAdditional")) {
                        modifyValues(requestCode, RName, mainTime, mainCost, alarmSet);
                        yoribi.update(RName, mainTime, mainCost, 0, 0, 0, delay, alarmSet, requestCode-MODIFY_REQ+1);
                    } else {
                        int optTime = Integer.parseInt(mainBundle.getString("optRuleTime"));
                        int optCost = Integer.parseInt(mainBundle.getString("optRuleCost"));
                        modifyValues(requestCode, RName, mainTime, mainCost, optTime, optCost, alarmSet);
                        yoribi.update(RName, mainTime, mainCost, 1, optTime, optCost, delay, alarmSet, requestCode-MODIFY_REQ+1);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /*
    purpose : Add the rule with only main rule,
                to custom listView that represent all of markets user added.
     */
    private void setValues(String name, int time, int cost, Boolean state) {
        adapter.addItem(name, "매 " + time + "분 마다 " + cost + "원", state);
        listview.setAdapter(adapter);
    }

    private void setValuesUpdate(String name, String result, Boolean state)
    {
        adapter.addItem(name, result, state);
        listview.setAdapter(adapter);
    }

    /*
    purpose : Add the rule with main rule and optional rule,
                to custom listView that represent all of markets user added.
     */
    private void setValues(String name, int mainTime, int maincost, int optTime, int optCost, Boolean state) {
        adapter.addItem(name, "첫 " + mainTime + "분 까지 " + maincost + "원, 매 " + optTime + "분 마다 " + optCost + "원", state);
        listview.setAdapter(adapter);
    }

    private void modifyValues(int requestCode, String name, int time, int cost, Boolean state) {
        adapter.modifyItem(requestCode-MODIFY_REQ, name, "매 " + time + "분 마다 " + cost + "원", state);
        listview.setAdapter(adapter);
    }
    private void modifyValues(int requestCode, String name, int mainTime, int maincost, int optTime, int optCost, Boolean state) {
        adapter.modifyItem(requestCode-MODIFY_REQ, name, "첫 " + mainTime + "분 까지 " + maincost + "원, 매 " + optTime + "분 마다 " + optCost + "원", state);
        listview.setAdapter(adapter);
    }

    private void modifyItem(int position){
        int DBIdx = position+1;
        Intent marketAddIntent = new Intent(MainActivity.this, MarketAddActivity.class);
        marketAddIntent.putExtra("REQ_id", MODIFY_REQ);
        Bundle dataBundle = new Bundle();
        dataBundle.putString("TITLE_data", yoribi.getTitle(DBIdx));
        dataBundle.putString("MAINTIME_data", yoribi.getMainRuleTime(DBIdx));
        dataBundle.putString("MAINCOST_data", yoribi.getMainRulePrice(DBIdx));
        if (yoribi.getoptRuleBool(DBIdx)==1){
            dataBundle.putInt("OPTBOOL", 1);
            dataBundle.putString("OPTTIME_data", yoribi.getoptRuleTime(DBIdx));
            dataBundle.putString("OPTCOST_data", yoribi.getoptRulePrice(DBIdx));
        }else{
            dataBundle.putInt("OPTBOOL", 0);
        }
        dataBundle.putString("ALARMDELAY", yoribi.getPushAlarm(DBIdx));
        marketAddIntent.putExtras(dataBundle);

        startActivityForResult(marketAddIntent, MODIFY_REQ+position);
    }

    private void deleteItem(int position){
        int DBIdx = position+1;
        AlarmHelper helper = new AlarmHelper( //If there's no option rule,
                yoribi.getTitle(DBIdx)
                ,yoribi.getMainRuleTime(DBIdx)
                ,yoribi.getMainRulePrice(DBIdx));
        helper.unRegisterAlarm(MainActivity.this, DBIdx);

        yoribi.delete(position+1);
        initListWithSQLData();
    }

    public static DatabaseHelper getYoribi(){
        return yoribi;
    }

    private class ListViewClickLIstener extends Activity implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            final ArrayAdapter<String> alertAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_item);
            alertAdapter.add("편집");
            alertAdapter.add("삭제");

            final AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
            alert_confirm.setTitle("해당 규칙을 어떻게 할까요?");
            alert_confirm.setAdapter(alertAdapter, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which==0){
                        //Modify
                        modifyItem(position);
                    }else if (which==1){
                        //Delete
                        deleteItem(position);
                    }
                }
            });
            alert_confirm.setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = alert_confirm.create();
            alert.show();

            return true;
        }
    }
}
