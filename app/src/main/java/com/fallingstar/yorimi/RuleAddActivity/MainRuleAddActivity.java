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
    /*
    Define variables.
     */
    private Button SubmitBtn;
    private EditText timeTxt;
    private EditText costTxt;
    private String timeStr, costStr;
    private boolean isOnlyAdditional = false;

    /*
    purpose : start MainRuleAddActivity and init.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rule_add);

        timeTxt = (EditText) findViewById(R.id.time);
        costTxt = (EditText) findViewById(R.id.cost);
        SubmitBtn = (Button) findViewById(R.id.SubmitBtn);

        initWidgetsListener();
    }

    /*
    purpose : Initiate all widgets that should contain listener.
    */
    private void initWidgetsListener() {
        /*
        When the submit button is touched, send result to MarketAddActivity.
         */
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                timeStr = timeTxt.getText().toString();
                costStr = costTxt.getText().toString();

                intent.putExtra("RuleType", "main");
                intent.putExtra("Time", timeStr);
                intent.putExtra("Cost", costStr);
                intent.putExtra("isOnlyMain", isOnlyAdditional);
                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }
}
