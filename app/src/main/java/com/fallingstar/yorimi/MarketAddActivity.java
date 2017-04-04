package com.fallingstar.yorimi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fallingstar.yorimi.RuleAddActivity.MainRuleAddActivity;
import com.fallingstar.yorimi.RuleAddActivity.OptionalRuleAddActivity;

public class MarketAddActivity extends AppCompatActivity {

    TextView lblMainRule, lblOptionalRule;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_add);

        lblMainRule = (TextView)findViewById(R.id.lblMainRule);
        lblOptionalRule = (TextView)findViewById(R.id.lblOptionalRule);

        initWidgets();
    }

    private void initWidgets() {
        lblMainRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MarketAddActivity.this, MainRuleAddActivity.class);
                startActivity(intent);
            }
        });

        lblOptionalRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MarketAddActivity.this, OptionalRuleAddActivity.class);
                startActivity(intent);
            }
        });
    }
}
