package com.sdgvvk.v1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import com.sdgvvk.v1.ProjectConstants;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.api.ApiCallUtil;

public class SplashScreenActivity extends AppCompatActivity {

    Intent sendotpactivity_intent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendotpactivity_intent = new Intent(SplashScreenActivity.this, SendOtpActivity.class);
        Intent intent = getIntent();
        if(intent != null){
            if(intent.getExtras() != null){
                if(intent.getExtras().get(ProjectConstants.TARGET_CLASS) != null){
                    Log.i("ss_nw_call", "targetclass : "+ getIntent().getExtras().get(ProjectConstants.TARGET_CLASS));
                    ApiCallUtil.noti_target_class = (String) getIntent().getExtras().get(ProjectConstants.TARGET_CLASS);
                    ApiCallUtil.noti_target_cpid = (String) getIntent().getExtras().get(ProjectConstants.CPID);
                    ApiCallUtil.noti_title = (String) getIntent().getExtras().get(ProjectConstants.TITLE);
                    ApiCallUtil.noti_message = (String) getIntent().getExtras().get(ProjectConstants.MESSAGE);
                    ApiCallUtil.noti_image = (String) getIntent().getExtras().get(ProjectConstants.IMAGE);
                }
            }
        }

        setContentView(R.layout.splash_screen);
        ApiCallUtil.isLive(this);

        // Add any necessary initialization code here, if needed

        // Create a handler to delay the transition to the main activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start activity
                startActivity(sendotpactivity_intent);
                finish();
            }
        }, 2100); // 2000 milliseconds (2 seconds) delay
    }
}
