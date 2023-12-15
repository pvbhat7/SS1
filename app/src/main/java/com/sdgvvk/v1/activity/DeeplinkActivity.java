package com.sdgvvk.v1.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.sdgvvk.v1.R;
import com.sdgvvk.v1.api.ApiCallUtil;

public class DeeplinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deeplink_handler);
        Log.i("local_logs", "DeeplinkActivity - onCreate called");
        // Handle the deep link
        handleDeepLink(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Handle the deep link for cases where the activity is already running
        handleDeepLink(intent);
    }

    private void handleDeepLink(Intent intent) {
        Uri data = intent.getData();
        if (data != null
                && "https".equals(data.getScheme())
                && "tavrostechinfo.com".equals(data.getHost())) {
            // Extract data from the deep link
            String cpid = data.getQueryParameter("id");
            // Now, `profileId` contains the information from the deep link
            // Implement logic to open the user profile in your app
            openUserProfile(cpid);
        }
    }

    private void openUserProfile(String cpid) {
        // Implement logic to open the user profile in your app
        // For example, navigate to the user profile activity
        // You might want to use an Intent to achieve this
        ApiCallUtil.redirected_via_deep_link = true;
        //ApiCallUtil.getLevel2Data(cpid, this);
        this.startActivity(new Intent(this, Level2ProfileActivity.class)
                .putExtra("level2data", cpid));
    }
}