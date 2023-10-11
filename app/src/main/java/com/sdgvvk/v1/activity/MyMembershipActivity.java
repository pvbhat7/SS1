package com.sdgvvk.v1.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sdgvvk.v1.LocalCache;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.modal.Customer;

public class MyMembershipActivity extends AppCompatActivity {

    Customer customer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymembership);

        customer = LocalCache.getLoggedInCustomer(this);

        RecyclerView mymembership_recyclerview = findViewById(R.id.mymembership_recyclerview);
        ApiCallUtil.getMyMemberships(customer.getProfileId(),mymembership_recyclerview,this);

    }
}
