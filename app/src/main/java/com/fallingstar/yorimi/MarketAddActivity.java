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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_add);

        lblMainRule = (TextView)findViewById(R.id.lblMainRule);
        lblOptionalRule = (TextView)findViewById(R.id.lblOptionalRule);
        SubmitBtn=(Button)findViewById(R.id.SubmitBtn);

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
                Intent intent = new Intent();
                intent.putExtra("ruleType", ruleType);
                intent.putExtra("ruleTime", ruleTime);
                intent.putExtra("ruleCost", ruleCost);

                setResult(0, intent);

                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                Log.e("WOW", data.getStringExtra("RuleType"));
                ruleType = data.getStringExtra("RuleType");
                ruleTime = data.getStringExtra("Time");
                ruleCost = data.getStringExtra("Cost");

                lblMainRule.setText("RuleType: "+ruleType+", Time: "+ruleTime+", Cost:"+ruleCost);
                Log.e("OMFG", "ASDASDASDASD");

                break;
            case 2:
                String option = data.getStringExtra("RuleType");
                String time2 = data.getStringExtra("Time");
                String cost2 = data.getStringExtra("Cost");
                lblOptionalRule.setText("RuleType: "+option+", Time: "+time2+", Cost:"+cost2);
                break;
            default:
                break;

        }
    }

}
