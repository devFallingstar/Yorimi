package com.fallingstar.yorimi.RuleAddActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(OptionalRuleAddActivity.this)
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
                        intent.putExtra("isOnlyAdditional", isOnlyAdditional);
                        intent.putExtra("RuleType", "additional");
                        intent.putExtra("Time", timeStr);
                        intent.putExtra("Cost", costStr);
                        setResult(RESULT_OK + 55, intent);
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
