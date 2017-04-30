package com.fallingstar.yorimi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fallingstar.yorimi.RuleAddActivity.MainRuleAddActivity;
import com.fallingstar.yorimi.RuleAddActivity.OptionalRuleAddActivity;

import static com.fallingstar.yorimi.R.id.SubmitBtn;

public class MarketAddActivity extends AppCompatActivity {

    /*
    Define variables
     */
    private EditText titleTxt;
    private TextView lblMainRule, lblOptionalRule;
    private Button SubmitBtn;
    private String ruleName, ruleTime, ruleCost;
    private Bundle mainBundle = new Bundle();
    boolean isMainRuleAdditional = true;

    /*
    purpose : start MarketAddActivity and init.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_add);

        titleTxt = (EditText) findViewById(R.id.txtName);
        lblMainRule = (TextView) findViewById(R.id.lblMainRule);
        lblOptionalRule = (TextView) findViewById(R.id.lblOptionalRule);
        SubmitBtn = (Button) findViewById(R.id.SubmitBtn);

        initWidgets();
    }

    /*
    purpose : Initiate all widgets that should contain listener.
    */
    private void initWidgets() {
        /*
        When main rule label is touched,
        show alert that contains a button list for rule type selecting.
        When user select 'first-time-type', it shows MainRuleAddActivity,
        and when select 'repeat-time-type', it shows OptionalRuleAddActivity.
         */
        lblMainRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = new CharSequence[]{"(1) 초기 시간 요금제", "(2) 추가 시간 요금제"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MarketAddActivity.this);
                dialog.setTitle("요금제를 선택해주세요.");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    // 리스트 선택 시 이벤트
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intent = new Intent(MarketAddActivity.this, MainRuleAddActivity.class);
                            startActivityForResult(intent, 1);
                        } else if (which == 1) {
                            Intent intent = new Intent(MarketAddActivity.this, OptionalRuleAddActivity.class);
                            startActivityForResult(intent, 1);
                        }
                    }
                });
                dialog.show();
            }
        });
        /*
        When optional rule label is touched,
        make intent and start for activity 'OptionalRuleAddActivity'.
         */
        lblOptionalRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MarketAddActivity.this, OptionalRuleAddActivity.class);
                startActivityForResult(intent, 2);
            }
        });
        /*
        When submit button is touched,
        validate each text fields, and alert if something is wrong.
        If there's no problem, dismiss and return a result intent with bundle,
        with result code RESULT_OK.
         */
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ruleName = titleTxt.getText().toString();
                AlertDialog.Builder dialog = new AlertDialog.Builder(MarketAddActivity.this);
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                if (ruleName.equals("")) {
                    dialog.setTitle("가게 이름을 입력해주세요.");
                    dialog.show();
                } else if (lblMainRule.getText().equals("")) {
                    dialog.setTitle("초기 요금제를 입력해주세요.");
                    dialog.show();
                } else if (lblOptionalRule.getText().equals("") && isMainRuleAdditional == false) {
                    dialog.setTitle("추가 요금제를 입력해주세요.");
                    dialog.show();
                } else {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("ruleName", ruleName);
                    resultIntent.putExtras(mainBundle);
                    resultIntent.putExtra("isMainRuleAdditional", isMainRuleAdditional);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }

    /*
       When RuleAddActivity sends a result with result code RESULT_OK,
       check the type and value of each return intent, and put it in to the mainBundle.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            /*
            If the result is come from MainRuleAddActivity,
            with a request code 2, which means rule for main rule,
            add appropriate data to mainBundle.
             */
            case 1:
                if (resultCode == RESULT_OK) {
                    isMainRuleAdditional = false;
                    ruleTime = data.getStringExtra("Time");
                    ruleCost = data.getStringExtra("Cost");

                    mainBundle.putString("ruleTime", ruleTime);
                    mainBundle.putString("ruleCost", ruleCost);
                    lblMainRule.setText("첫 " + ruleTime + "분까지 " + ruleCost + "원");

                    lblOptionalRule.setHint("추가 요금제 등록");
                    lblOptionalRule.setEnabled(true);
                }
                /*
                If resultCode is RESULT_OK+55,
                it means the result come from OptionalRuleAddActivity,
                so disable optional rule label,
                because we don't have to add duplicated rule for one market :)
                 */
                if (resultCode == (RESULT_OK + 55)) {
                    isMainRuleAdditional = true;
                    ruleTime = data.getStringExtra("Time");
                    ruleCost = data.getStringExtra("Cost");

                    mainBundle.putString("ruleTime", ruleTime);
                    mainBundle.putString("ruleCost", ruleCost);
                    lblMainRule.setText("첫 " + ruleTime + "분까지 " + ruleCost + "원");

                    lblOptionalRule.setText("");
                    lblOptionalRule.setHint("필수 요금제가 이미 추가 요금제입니다");
                    lblOptionalRule.setEnabled(false);
                }
                break;
            /*
            If the result is come from OptionalRuleAddActivity,
            with a request code 2, which means rule for optional rule,
            add appropriate data to mainBundle.
             */
            case 2:
                if (resultCode == RESULT_OK + 55) {
                    ruleTime = data.getStringExtra("Time");
                    ruleCost = data.getStringExtra("Cost");

                    mainBundle.putString("optRuleTime", ruleTime);
                    mainBundle.putString("optRuleCost", ruleCost);
                    lblOptionalRule.setText("첫 " + ruleTime + "분까지 " + ruleCost + "원");
                }
                break;
            default:
                break;
        }
    }
}
