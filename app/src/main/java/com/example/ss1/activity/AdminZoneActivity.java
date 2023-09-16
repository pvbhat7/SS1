package com.example.ss1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ss1.R;
import com.example.ss1.adapters.AllMembersAdapter;
import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.api.ApiUtils;

public class AdminZoneActivity extends AppCompatActivity {

    LinearLayout link1 , link2;

    CardView card1 , card2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminzone);

        initUIElements();
        onclickListeners();
        ApiCallUtil.syncGenderStats(this);
    }

    private void onclickListeners() {
        link1.setOnClickListener(view -> {
            ApiUtils.vibrateFunction(AdminZoneActivity.this);
            Intent intent = new Intent(AdminZoneActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
        link2.setOnClickListener(view -> {
           /* ApiUtils.vibrateFunction(AdminZoneActivity.this);
            Intent intent = new Intent(AdminZoneActivity.this, RegistrationActivity.class);
            startActivity(intent);*/
        });
        card1.setOnClickListener(view -> {
            ApiUtils.vibrateFunction(AdminZoneActivity.this);
            Intent intent = new Intent(AdminZoneActivity.this, AllMemberActivity.class);
            startActivity(intent);
        });
        card2.setOnClickListener(view -> {
            ApiUtils.vibrateFunction(AdminZoneActivity.this);
            Intent intent = new Intent(AdminZoneActivity.this, AllMemberActivity.class);
            startActivity(intent);
        });

    }

    private void initUIElements() {
        link1 = findViewById(R.id.link1);
        link2 = findViewById(R.id.link2);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApiCallUtil.syncGenderStats(this);
    }
}
