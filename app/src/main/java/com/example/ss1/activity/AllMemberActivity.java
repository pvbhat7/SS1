package com.example.ss1.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ss1.LocalCache;
import com.example.ss1.R;
import com.example.ss1.adapters.AllMembersAdapter;
import com.example.ss1.adapters.Level_1_profilecardAdapter;
import com.example.ss1.modal.Level_1_cardModal;

import java.util.List;

public class AllMemberActivity extends AppCompatActivity {

    String gender ;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allmembersactivity);
        recyclerView = findViewById(R.id.recyclerview);

        List<Level_1_cardModal> list = LocalCache.retrieveLevel1List(this);
        AllMembersAdapter adapter = new AllMembersAdapter(list, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        /*Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gender = extras.getString("gender");
            if(gender != null && !gender.isEmpty()){

            }
        }*/

    }
}
