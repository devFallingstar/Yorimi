package com.fallingstar.yorimi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fallingstar.yorimi.RuleAddActivity.MainRuleAddActivity;
import com.fallingstar.yorimi.RuleAddActivity.OptionalRuleAddActivity;

import static com.fallingstar.yorimi.R.id.SubmitBtn;

public class MarketAddActivity extends AppCompatActivity {

    TextView lblMainRule, lblOptionalRule;
    Button SubmitBtn;
    String ruleType, ruleTime, ruleCost;
    Bundle mainBundle = new Bundle();
    Bundle optionalBundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_add);

        lblMainRule = (TextView)findViewById(R.id.lblMainRule);
        lblOptionalRule = (TextView)findViewById(R.id.lblOptionalRule);
        SubmitBtn = (Button)findViewById(R.id.SubmitBtn);

        initWidgets();
    }

    private void initWidgets() {
        lblMainRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MarketAddActivity.this, MainRuleAddActivity.class);
                startActivityForResult(intent, 1);

            }
        });

        lblOptionalRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MarketAddActivity.this, OptionalRuleAddActivity.class);
                startActivityForResult(intent,2);
            }
        });
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();

                resultIntent.putExtras(mainBundle);
                resultIntent.putExtras(optionalBundle);

                setResult(RESULT_OK, resultIntent);

                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    boolean isMainOnly = data.getBooleanExtra("isOnlyMain", true);
                    ruleType = data.getStringExtra("RuleType");
                    ruleTime = data.getStringExtra("Time");
                    ruleCost = data.getStringExtra("Cost");

                    mainBundle.putString("ruleType", ruleType);
                    mainBundle.putString("ruleTime", ruleTime);
                    mainBundle.putString("ruleCost", ruleCost);
                    lblMainRule.setText("Main RuleType: "+ruleType+", Time: "+ruleTime+", Cost:"+ruleCost);
                    if (!isMainOnly){
                        lblOptionalRule.setEnabled(false);
                    }else{
                        lblOptionalRule.setEnabled(true);
                    }
                }

                break;
            case 2:
                if(resultCode == RESULT_OK){
                    ruleType = data.getStringExtra("RuleType");
                    ruleTime = data.getStringExtra("Time");
                    ruleCost = data.getStringExtra("Cost");

                    optionalBundle.putString("ruleType", ruleType);
                    optionalBundle.putString("ruleTime", ruleTime);
                    optionalBundle.putString("ruleCost", ruleCost);
                    lblOptionalRule.setText("Optional RuleType: "+ruleType+", Time: "+ruleTime+", Cost:"+ruleCost);
                }

                break;
            default:
                break;

        }
    }

}
