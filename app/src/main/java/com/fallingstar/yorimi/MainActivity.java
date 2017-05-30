package com.fallingstar.yorimi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fallingstar.yorimi.Helper.Database.DatabaseHelper;
import com.fallingstar.yorimi.ListView.ListViewAdapter;

public class MainActivity extends AppCompatActivity {
    /*
    Define variables.
     */
    private ListView listview;
    private ListViewAdapter adapter;
    private FloatingActionButton fBtn;
    private BottomNavigationView bNavView;
    private static DatabaseHelper yoribi;

    /*
    purpose : start main application activity and init.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListViewClickLIstener listViewClickLIstener = new ListViewClickLIstener();

        final Context mainAppContext = getApplicationContext();

        yoribi = new DatabaseHelper(mainAppContext);
        listview = (ListView) findViewById(R.id.listview);
        bNavView = (BottomNavigationView) findViewById(R.id.bottomNav);
        fBtn = (FloatingActionButton) findViewById(R.id.addMarketBtn);

        listview.setOnItemLongClickListener(listViewClickLIstener);

        initListView();
        initListWithSQLData();

        initWidgets();
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
            if(yoribi.getoptRuleBool(i) == 1)
                setValues(yoribi.getTitle(i), Integer.parseInt(yoribi.getMainRuleTime(i)), Integer.parseInt(yoribi.getMainRulePrice(i)), Integer.parseInt(yoribi.getoptRuleTime(i)), Integer.parseInt(yoribi.getoptRulePrice(i)), yoribi.getAlarmSet(i));
            else
                setValues(yoribi.getTitle(i), Integer.parseInt(yoribi.getMainRuleTime(i)), Integer.parseInt(yoribi.getMainRulePrice(i)), yoribi.getAlarmSet(i));
        }
    }

    /*
    purpose : Initiate all widgets that should contain listener.
    */
    private void initWidgets() {

        /*
        Segments changing(Tabbar method).
         */
        bNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        return true;
                    case R.id.navigation_dashboard:
                        return true;
                }
                return false;
            }
        });

        /*
        Floating button with MarketAddActivity intent.
         */
        fBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent marketAddIntent = new Intent(MainActivity.this, MarketAddActivity.class);
                startActivityForResult(marketAddIntent, 3);
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
        switch (resultCode) {
            case RESULT_OK:
                Bundle mainBundle = data.getExtras();
                if (mainBundle.getBoolean("isMainRuleAdditional")) {
                    Log.d("TEST", data.getStringExtra("ruleName")+"  "+ Integer.parseInt(mainBundle.getString("ruleTime"))+ " " + Integer.parseInt(mainBundle.getString("ruleCost")));

                    setValues(data.getStringExtra("ruleName"), Integer.parseInt(mainBundle.getString("ruleTime")), Integer.parseInt(mainBundle.getString("ruleCost")), mainBundle.getBoolean("alarmSet"));
                    yoribi.insert(data.getStringExtra("ruleName"), Integer.parseInt(mainBundle.getString("ruleTime")), Integer.parseInt(mainBundle.getString("ruleCost")), 0, 0, 0, mainBundle.getInt("notiDelay"), mainBundle.getBoolean("alarmSet"));
                } else {
                    setValues(data.getStringExtra("ruleName"), Integer.parseInt(mainBundle.getString("ruleTime")), Integer.parseInt(mainBundle.getString("ruleCost")), Integer.parseInt(mainBundle.getString("optRuleTime")), Integer.parseInt(mainBundle.getString("optRuleCost")), mainBundle.getBoolean("alarmSet"));
                    yoribi.insert(data.getStringExtra("ruleName"), Integer.parseInt(mainBundle.getString("ruleTime")), Integer.parseInt(mainBundle.getString("ruleCost")), 1, Integer.parseInt(mainBundle.getString("optRuleTime")), Integer.parseInt(mainBundle.getString("optRuleCost")), mainBundle.getInt("notiDelay"), mainBundle.getBoolean("alarmSet"));
                }
                Log.d("Value", mainBundle.getString("ruleTime") + " | " + mainBundle.getString("ruleCost"));
                break;
            default:
                break;
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

    /*
    purpose : Add the rule with main rule and optional rule,
                to custom listView that represent all of markets user added.
     */
    private void setValues(String name, int mainTime, int maincost, int optTime, int optCost, Boolean state) {
        adapter.addItem(name, "첫 " + mainTime + "분 까지 " + maincost + "원, 매 " + optTime + "분 마다 " + optCost + "원", state);
        listview.setAdapter(adapter);
    }

    public static DatabaseHelper getYoribi(){
        return yoribi;
    }

    private class ListViewClickLIstener extends Activity implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
            alert_confirm.setMessage("해당 규칙을 삭제 할까요?").setCancelable(false).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            yoribi.delete(position+1);
                            initListWithSQLData();
                        }
                    }).setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 'No'
                        }
                    });
            AlertDialog alert = alert_confirm.create();
            alert.show();

            return true;
        }
    }

}
