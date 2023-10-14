package com.sdgvvk.v1.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

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
    AllMembersAdapter adapter;
    RecyclerView recyclerView;

    List<Level_1_cardModal> filteredList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allmembersactivity);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gender = extras.getString("gender");
        }
        List<Level_1_cardModal> list = LocalCache.getLevel1List(this);
        for(Level_1_cardModal obj : list){
            if(obj.getGender().equalsIgnoreCase(gender))
                filteredList.add(obj);
        }

        recyclerView = findViewById(R.id.recyclerview);
        adapter = new AllMembersAdapter(filteredList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ((EditText)findViewById(R.id.searchMemberEditText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void filter(String text) {
        List<Level_1_cardModal> tempList = new ArrayList();

        for(Level_1_cardModal obj : filteredList){
            if(obj.getFirstname().toLowerCase().contains(text.toLowerCase()) || obj.getLastname().toLowerCase().contains(text.toLowerCase()) || obj.getProfileId().toLowerCase().contains(text.toLowerCase())){
                tempList.add(obj);
            }
        }
        adapter.updateList(tempList);
    }


}
