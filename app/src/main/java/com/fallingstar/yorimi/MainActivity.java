package com.fallingstar.yorimi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.fallingstar.yorimi.Helper.DatabaseHelper;

import javax.annotation.Nonnull;

public class MainActivity extends AppCompatActivity {

    /*
    Define variables.
     */
    private ListView listview;
    private ListViewAdapter adapter;
    private FloatingActionButton fBtn;
    private BottomNavigationView bNavView;
    private DatabaseHelper yoribi;

    /*
    purpose : start main application activity and init.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context mainAppContext = getApplicationContext();
        yoribi = new DatabaseHelper(mainAppContext);

        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);
        bNavView = (BottomNavigationView) findViewById(R.id.bottomNav);
        fBtn = (FloatingActionButton) findViewById(R.id.addMarketBtn);

        adapter = new ListViewAdapter();

        int count = yoribi.getCount();
        for(int i = 1; i <= count; i++)
        {
            if(yoribi.getoptRuleBool(i) == 1)
                setValues(yoribi.getTitle(i), Integer.parseInt(yoribi.getMainRuleTime(i)), Integer.parseInt(yoribi.getMainRulePrice(i)), Integer.parseInt(yoribi.getoptRuleTime(i)), Integer.parseInt(yoribi.getoptRulePrice(i)));
            else
                setValues(yoribi.getTitle(i), Integer.parseInt(yoribi.getMainRuleTime(i)), Integer.parseInt(yoribi.getMainRulePrice(i)));
        }

        initWidgets();
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
            public boolean onNavigationItemSelected(@Nonnull MenuItem menuItem) {
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
                then call setValues(String, int, int),
                and if the main rule is type of 'every time n',
                then call setValues(String, int, int, int, int).
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                Bundle mainBundle = data.getExtras();
                if (mainBundle.getBoolean("isMainRuleAdditional")) {
                    Log.d("TEST", data.getStringExtra("ruleName")+"  "+ Integer.parseInt(mainBundle.getString("ruleTime"))+ " " + Integer.parseInt(mainBundle.getString("ruleCost")));

                    setValues(data.getStringExtra("ruleName"), Integer.parseInt(mainBundle.getString("ruleTime")), Integer.parseInt(mainBundle.getString("ruleCost")));
                    yoribi.insert(data.getStringExtra("ruleName"), Integer.parseInt(mainBundle.getString("ruleTime")), Integer.parseInt(mainBundle.getString("ruleCost")), 0, 0, 0, mainBundle.getInt("notiDelay"));
                } else {
                    setValues(data.getStringExtra("ruleName"), Integer.parseInt(mainBundle.getString("ruleTime")), Integer.parseInt(mainBundle.getString("ruleCost")), Integer.parseInt(mainBundle.getString("optRuleTime")), Integer.parseInt(mainBundle.getString("optRuleCost")));
                    yoribi.insert(data.getStringExtra("ruleName"), Integer.parseInt(mainBundle.getString("ruleTime")), Integer.parseInt(mainBundle.getString("ruleCost")), 1, Integer.parseInt(mainBundle.getString("optRuleTime")), Integer.parseInt(mainBundle.getString("optRuleCost")), mainBundle.getInt("notiDelay"));
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
    private void setValues(String name, int time, int cost) {
        adapter.addItem(name, "매 " + time + "분 마다 " + cost + "원");
        listview.setAdapter(adapter);
    }

    /*
    purpose : Add the rule with main rule and optional rule,
                to custom listView that represent all of markets user added.
     */
    private void setValues(String name, int mainTime, int maincost, int optTime, int optCost) {
        adapter.addItem(name, "첫 " + mainTime + "분 까지 " + maincost + "원, 매 " + optTime + "분 마다 " + optCost + "원");
        listview.setAdapter(adapter);
    }
}
