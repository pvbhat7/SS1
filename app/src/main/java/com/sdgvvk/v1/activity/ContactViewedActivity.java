package com.sdgvvk.v1.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sdgvvk.v1.LocalCache;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.adapters.ContactViewedAdapter;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.modal.Customer;
import com.sdgvvk.v1.modal.OrderModal;

public class ContactViewedActivity extends AppCompatActivity {

    private RecyclerView contactviewedRecyclerView;

    TextView cv_count,cv_zero;

    Customer customer;
    ContactViewedAdapter adapter;
    String count;
    OrderModal activeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_viewed);
        //activeOrder = LocalCache.getActiveOrder(this);
        initUiElements();




       /* if(activeOrder.getUsedCount().equalsIgnoreCase("0")) {
            contactviewedRecyclerView.setVisibility(View.GONE);
            cv_zero.setVisibility(View.VISIBLE);
        }
*/
        customer = LocalCache.getLoggedInCustomer(this);

        ApiCallUtil.getContactViewedProfiles(customer.getProfileId(),adapter,contactviewedRecyclerView,cv_count,cv_zero,this);
    }

    private void initUiElements() {
        contactviewedRecyclerView = findViewById(R.id.contactviewedRecyclerView);
        cv_count = findViewById(R.id.cv_count);
        cv_zero = findViewById(R.id.cv_zero);
    }


}