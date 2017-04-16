package com.fallingstar.yorimi.RuleAddActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fallingstar.yorimi.MarketAddActivity;
import com.fallingstar.yorimi.R;


public class MainRuleAddActivity extends AppCompatActivity {
    Button SubmitBtn;
    EditText timeTxt1;
    EditText costTxt1;
    EditText timeTxt2;
    EditText costTxt2;

    String timeStr1, timeStr2, costStr1, costStr2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rule_add);

        timeTxt1 = (EditText)findViewById(R.id.time1);
        costTxt1=(EditText)findViewById(R.id.cost1);
        SubmitBtn = (Button) findViewById(R.id.SubmitBtn);

        initWidgets();
    }
    private void initWidgets() {
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                timeStr1 = timeTxt1.getText().toString();
                costStr1 = costTxt1.getText().toString();
//                timeStr2 = timeTxt2.getText().toString();
//                costStr2 = costTxt2.getText().toString();

                intent.putExtra("RuleType", "origin");
                intent.putExtra("Time", timeStr1);
                intent.putExtra("Cost", costStr1);
                setResult(0, intent);
//                intent.putExtra("RuleType", "add");
//                intent.putExtra("Time", timeStr2);
//                intent.putExtra("Cost", costStr2);
                finish();
//                Intent intent = new Intent(MainRuleAddActivity.this, MarketAddActivity.class);
//                startActivity(intent);
            }
        });

    }
}
