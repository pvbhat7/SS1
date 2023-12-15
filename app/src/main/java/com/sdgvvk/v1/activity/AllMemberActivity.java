package com.sdgvvk.v1.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sdgvvk.v1.R;
import com.sdgvvk.v1.adapters.AllMembersAdapter;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.modal.Level_1_cardModal;

import java.util.ArrayList;
import java.util.List;

public class AllMemberActivity extends AppCompatActivity {

    String key;
    public static AllMembersAdapter adapter;
    RecyclerView recyclerView;

    public static List<Level_1_cardModal> filteredList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allmembersactivity);

        recyclerView = findViewById(R.id.allmembersrecyclerview);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            key = extras.getString("key");
        }

        if(key.equalsIgnoreCase("distinctuserswithnotifications"))
            ApiCallUtil.getDistinctUsers(this,recyclerView,adapter);
        else
            ApiCallUtil.fetchAllMembersList(this,key,recyclerView,adapter);




        ((EditText)findViewById(R.id.searchMemberEditText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(filteredList != null && !filteredList.isEmpty())
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
            String mobile1 = obj.getMobile1() != null && !obj.getMobile1().isEmpty() ? obj.getMobile1() : "";
            String mobile2 = obj.getMobile2() != null && !obj.getMobile2().isEmpty() ? obj.getMobile2() : "";
            String mobile3 = obj.getMobile3() != null && !obj.getMobile3().isEmpty() ? obj.getMobile2() : "";
            String mobile4 = obj.getMobile4() != null && !obj.getMobile4().isEmpty() ? obj.getMobile4() : "";
            if(obj.getName().toLowerCase().contains(text.toLowerCase()) || obj.getProfileId().toLowerCase().contains(text.toLowerCase())
            || mobile1.contains(text.toLowerCase())
                    || (!mobile2.isEmpty() && mobile2.contains(text.toLowerCase()))
                    || (!mobile3.isEmpty() && mobile3.contains(text.toLowerCase()))
                    || (!mobile4.isEmpty() && mobile4.contains(text.toLowerCase()))){
                tempList.add(obj);
            }
        }
        if(adapter != null)
        adapter.updateList(tempList);
    }


}
