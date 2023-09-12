package com.example.ss1.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ss1.LocalCache;
import com.example.ss1.R;
import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.modal.Customer;

public class MyMembershipActivity extends AppCompatActivity {

    Customer customer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymembership);

        customer = LocalCache.retrieveLoggedInCustomer(this);

        RecyclerView mymembership_recyclerview = findViewById(R.id.mymembership_recyclerview);
        ApiCallUtil.getMyMemberships(customer.getProfileId(),mymembership_recyclerview,this);

    }
}
