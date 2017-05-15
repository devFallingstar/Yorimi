package com.fallingstar.yorimi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fallingstar.yorimi.RuleAddActivity.MainRuleAddActivity;
import com.fallingstar.yorimi.RuleAddActivity.OptionalRuleAddActivity;

public class MarketAddActivity extends AppCompatActivity {
    /*
    Define variables
     */
    private EditText titleTxt;
    private TextView lblMainRule, lblOptionalRule;
    private Button SubmitBtn;
    private RadioGroup notiRadGroup;
    private String ruleName, ruleTime, ruleCost;
    private int notiDelay = 0;
    private Bundle mainBundle = new Bundle();
    private boolean isMainRuleAdditional = true;

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
        notiRadGroup = (RadioGroup) findViewById(R.id.notiRadGroup);
        SubmitBtn = (Button) findViewById(R.id.SubmitBtn);

        initWidgets();
    }

    /*
    purpose : Initiate all widgets that should contain listener.
    */
    private void initWidgets() {
        /*
        Default alarm radio button is 5 minute.
         */
        notiRadGroup.check(R.id.notiRad1);

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
                setIntentExrasAndSend();
            }
        });
    }

    /*
    purpose : Set and init Intent with values,
    that represent each time and cost of rule,
    by checking if the value is null or not.
     */
    private void setIntentExrasAndSend() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MarketAddActivity.this)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        ruleName = titleTxt.getText().toString();
        /*
        Get checked notification radio button, and set notification delay value.
        */
        switch (notiRadGroup.getCheckedRadioButtonId()) {
            case R.id.notiRad0:
                notiDelay = 0;
                break;
            case R.id.notiRad1:
                notiDelay = 5;
                break;
            case R.id.notiRad2:
                notiDelay = 10;
                break;
            case R.id.notiRad3:
                notiDelay = 30;
                break;
            default:
                break;
        }
        switch (checkNullValue()){
            case -1:
                dialog.setTitle("가게 이름을 입력해주세요.");
                dialog.show();
                break;
            case -2:
                dialog.setTitle("필수 요금제를 입력해주세요.");
                dialog.show();
                break;
            case -3:
                dialog.setTitle("추가 요금제를 입력해주세요.");
                dialog.show();
                break;
            default:
                Intent resultIntent = new Intent();
                resultIntent.putExtra("ruleName", ruleName);
                resultIntent.putExtra("isMainRuleAdditional", isMainRuleAdditional);
                resultIntent.putExtra("notiDelay", notiDelay);
                resultIntent.putExtras(mainBundle);
                setResult(RESULT_OK, resultIntent);
                finish();
                break;
        }
    }

    /*
    purpose : Check if there's null value or not from every text view
     */
    private int checkNullValue() {
        if (ruleName.equals("")) {
            return -1;
        } else if (lblMainRule.getText().equals("")) {
            return -2;
        } else if (lblOptionalRule.getText().equals("") && !isMainRuleAdditional) {
            return -3;
        } else {
            return 0;
        }
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
                /*
                If resultCode is RESULT_OK,
                it means the result come from MainRuleAddActivity,
                so enable optional rule label,
                because we have to add option rule for market :)
                 */
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
                because we don't have to add duplicated rule for market :)
                 */
                if (resultCode == (RESULT_OK + 55)) {
                    isMainRuleAdditional = true;
                    ruleTime = data.getStringExtra("Time");
                    ruleCost = data.getStringExtra("Cost");
                    mainBundle.putString("ruleTime", ruleTime);
                    mainBundle.putString("ruleCost", ruleCost);

                    lblMainRule.setText(ruleTime + "분마다 " + ruleCost + "원");

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
                    lblOptionalRule.setText("매 " + ruleTime + "분마다 " + ruleCost + "원");
                }
                break;
            default:
                break;
        }
    }
}
