package com.sdgvvk.v1.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdgvvk.v1.LocalCache;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.adapters.AllMembersAdapter;
import com.sdgvvk.v1.modal.Level_1_cardModal;

import java.util.ArrayList;
import java.util.List;

public class AllMemberActivity extends AppCompatActivity {

    String gender ;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allmembersactivity);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gender = extras.getString("gender");
        }
        List<Level_1_cardModal> list = LocalCache.getLevel1List(this);
        List<Level_1_cardModal> filteredList = new ArrayList<>();
        for(Level_1_cardModal obj : list){
            if(obj.getGender().equalsIgnoreCase(gender))
                filteredList.add(obj);
        }

        recyclerView = findViewById(R.id.recyclerview);
        AllMembersAdapter adapter = new AllMembersAdapter(filteredList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}
