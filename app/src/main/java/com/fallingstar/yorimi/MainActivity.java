package com.fallingstar.yorimi;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /*
    Define variables.
     */
    private ListView listview;
    private ListViewAdapter adapter;
    private FloatingActionButton fBtn;
    private BottomNavigationView bNavView;

    /*
    purpose : start main application activity and init.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);
        bNavView = (BottomNavigationView) findViewById(R.id.bottomNav);
        fBtn = (FloatingActionButton) findViewById(R.id.addMarketBtn);

        adapter = new ListViewAdapter();

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
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
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
                    Log.d("Value", mainBundle.getString("ruleTime") + " | " + mainBundle.getString("ruleCost"));
                    setValues(data.getStringExtra("ruleName"), Integer.parseInt(mainBundle.getString("ruleTime")), Integer.parseInt(mainBundle.getString("ruleCost")));
                } else {
                    setValues(data.getStringExtra("ruleName"), Integer.parseInt(mainBundle.getString("ruleTime")), Integer.parseInt(mainBundle.getString("ruleCost")), Integer.parseInt(mainBundle.getString("optRuleTime")), Integer.parseInt(mainBundle.getString("optRuleCost")));
                }
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
