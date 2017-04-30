package com.fallingstar.yorimi.RuleAddActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fallingstar.yorimi.R;


public class MainRuleAddActivity extends AppCompatActivity {
    Button SubmitBtn;
    EditText timeTxt1, costTxt1;
    EditText timeTxt2, costTxt2;
    RadioGroup ruleRadGroup;
    RadioButton mainRad, optionalRad;
    boolean isOnlyMain = true;

    String timeStr1, costStr1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rule_add);

        timeTxt1 = (EditText)findViewById(R.id.time1);
        costTxt1 = (EditText)findViewById(R.id.cost1);
        timeTxt2 = (EditText)findViewById(R.id.time2);
        costTxt2 = (EditText)findViewById(R.id.cost2);
        SubmitBtn = (Button) findViewById(R.id.SubmitBtn);
        mainRad = (RadioButton)findViewById(R.id.radMainRule);
        optionalRad = (RadioButton)findViewById(R.id.radOptionalRule);

        ruleRadGroup.addView(mainRad);
        ruleRadGroup.addView(optionalRad);
        initWidgetsListener();
    }
    private void initWidgetsListener() {
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (isOnlyMain){
                    timeStr1 = timeTxt1.getText().toString();
                    costStr1 = costTxt1.getText().toString();
                    intent.putExtra("RuleType", "main");
                }else{
                    timeStr1 = timeTxt2.getText().toString();
                    costStr1 = costTxt2.getText().toString();
                    intent.putExtra("RuleType", "optional");
                }
                intent.putExtra("Time", timeStr1);
                intent.putExtra("Cost", costStr1);
                intent.putExtra("isOnlyMain", isOnlyMain);
                setResult(RESULT_OK, intent);

                finish();
            }
        });
        mainRad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RAD", "MAINRAD");
                ruleRadGroup.check(mainRad);
                ruleRad
                optionalRad.setSelected(false);
                isOnlyMain = true;
            }
        });
        optionalRad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainRad.setSelected(false);
                optionalRad.setSelected(true);
                isOnlyMain = false;
            }
        });
    }
}
