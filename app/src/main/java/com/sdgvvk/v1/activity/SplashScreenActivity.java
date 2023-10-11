package com.sdgvvk.v1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.sdgvvk.v1.R;
import com.sdgvvk.v1.api.ApiCallUtil;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        ApiCallUtil.isLive(this);

        // Add any necessary initialization code here, if needed

        // Create a handler to delay the transition to the main activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity
                Intent intent = new Intent(SplashScreenActivity.this, SendOtpActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2100); // 2000 milliseconds (2 seconds) delay
    }
}
