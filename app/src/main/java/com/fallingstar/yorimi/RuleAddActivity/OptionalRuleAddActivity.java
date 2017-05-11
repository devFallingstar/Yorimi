package com.fallingstar.yorimi.RuleAddActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fallingstar.yorimi.MarketAddActivity;
import com.fallingstar.yorimi.R;

public class OptionalRuleAddActivity extends AppCompatActivity {
    /*
    Define variables.
     */
    private Button SubmitBtn;
    private EditText timeTxt;
    private EditText costTxt;
    private String timeStr, costStr;
    private boolean isOnlyAdditional = true;

    /*
    purpose : start MainRuleAddActivity and init.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optional_rule_add);

        timeTxt = (EditText) findViewById(R.id.time);
        costTxt = (EditText) findViewById(R.id.cost);

        SubmitBtn = (Button) findViewById(R.id.SubmitBtn);
        initWidgets();
    }

    /*
    purpose : Initiate all widgets that should contain listener.
    */
    private void initWidgets() {
        /*
        When the submit button is touched, send result to MarketAddActivity.
         */
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                timeStr = timeTxt.getText().toString();
                costStr = costTxt.getText().toString();

                intent.putExtra("isOnlyAdditional", isOnlyAdditional);
                intent.putExtra("RuleType", "additional");
                intent.putExtra("Time", timeStr);
                intent.putExtra("Cost", costStr);
                setResult(RESULT_OK + 55, intent);

                finish();
            }
        });
    }

}
