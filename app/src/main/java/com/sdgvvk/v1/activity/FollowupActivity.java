package com.sdgvvk.v1.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sdgvvk.v1.R;
import com.sdgvvk.v1.api.ApiCallUtil;

public class FollowupActivity extends AppCompatActivity {

    private RecyclerView followupRecyclerView;

    TextView cv_count,cv_zero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup);
        initUiElements();

        ApiCallUtil.getFollowupByCpid(null,followupRecyclerView,this);
    }

    private void initUiElements() {
        followupRecyclerView = findViewById(R.id.followupRecyclerView);
    }


}