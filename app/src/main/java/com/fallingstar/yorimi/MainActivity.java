package com.fallingstar.yorimi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private FloatingActionButton fBtn;
    private BottomNavigationView bNavView;

    String typeStr;
    int timeInt, costInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listview;
        ListViewAdapter adapter;

        adapter = new ListViewAdapter();

        listview = (ListView)findViewById(R.id.listview);
        listview.setAdapter(adapter);

        bNavView = (BottomNavigationView) findViewById(R.id.bottomNav);
        fBtn = (FloatingActionButton) findViewById(R.id.addMarketBtn);

        initWidgets();
    }

    private void initWidgets() {
        bNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        mTextMessage.setText(R.string.title_home);
                        return true;
                    case R.id.navigation_dashboard:
                        mTextMessage.setText(R.string.title_dashboard);
                        return true;
                }
                return false;
            }
        });

        fBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : Show 'activity market add'
                Intent marketAddIntent = new Intent(MainActivity.this, MarketAddActivity.class);
                startActivityForResult(marketAddIntent, 3);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case RESULT_OK:
                setValues(data.getStringExtra("ruleType"), Integer.parseInt(data.getStringExtra("ruleTime")), Integer.parseInt(data.getStringExtra("ruleCost")));
                break;
            default:
                break;
        }
    }

    public void setValues(String type, int time, int cost){
        typeStr = type;
        timeInt = time;
        costInt = cost;
        LolSoGoodFunction();
    }
    private void LolSoGoodFunction(){
        Toast.makeText(MainActivity.this, typeStr + "    " + timeInt + "    " + costInt + "    ",Toast.LENGTH_SHORT).show();
    }

}
