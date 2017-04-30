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
    Button SubmitBtn;
    EditText timeTxt;
    EditText costTxt;
    String timeStr,costStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optional_rule_add);

        timeTxt = (EditText)findViewById(R.id.time);
        costTxt = (EditText)findViewById(R.id.cost);

        SubmitBtn = (Button) findViewById(R.id.SubmitBtn);
        initWidgets();
    }
    private void initWidgets() {
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    timeStr = timeTxt.getText().toString();
                    costStr = costTxt.getText().toString();

                    intent.putExtra("RuleType", "optional");
                    intent.putExtra("Time", timeStr);
                    intent.putExtra("Cost", costStr);
                    setResult(RESULT_OK, intent);
                    finish();

//                Intent intent = new Intent(OptionalRuleAddActivity.this, MarketAddActivity.class);
//                startActivity(intent);
            }
        });
    }

}
