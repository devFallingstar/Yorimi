package com.fallingstar.yorimi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Jiran on 2017-05-31.
 */

public class SplashActivity extends AppCompatActivity {

    private static int SPALSH_TIME_OUT = 1500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, ViewActivity.class);
                startActivity(intent);

                finish();
            }
        }, SPALSH_TIME_OUT);
    }
}
