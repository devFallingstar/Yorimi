package com.fallingstar.yorimi.RuleAddActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fallingstar.yorimi.MarketAddActivity;
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainRuleAddActivity.this)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                timeStr = timeTxt.getText().toString();
                costStr = costTxt.getText().toString();
                switch (checkNullValue()) {
                    case -1:
                        dialog.setTitle("시간을 입력해주세요.");
                        dialog.show();
                        break;
                    case -2:
                        dialog.setTitle("금액을 입력해주세요.");
                        dialog.show();
                        break;
                    default:
                        Intent intent = new Intent();
                        intent.putExtra("RuleType", "main");
                        intent.putExtra("Time", timeStr);
                        intent.putExtra("Cost", costStr);
                        intent.putExtra("isOnlyMain", isOnlyAdditional);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                }
            }
        });
    }
    private int checkNullValue() {
        if (timeStr.equals("")) {
            return -1;
        }else if (costStr.equals("")) {
            return -2;
        }else {
            return 0;
        }
    }
}
