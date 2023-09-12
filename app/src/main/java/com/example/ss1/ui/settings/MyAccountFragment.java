package com.example.ss1.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ss1.LocalCache;
import com.example.ss1.R;
import com.example.ss1.activity.ContactViewedActivity;
import com.example.ss1.activity.MyMembershipActivity;
import com.example.ss1.activity.RegistrationActivity;
import com.example.ss1.activity.SendOtpActivity;
import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.api.ApiUtils;
import com.example.ss1.modal.Customer;
import com.example.ss1.modal.OrderModal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class MyAccountFragment extends Fragment {

    private View view;
    Customer customer;

    OrderModal activeOrder;

    CardView cb_card;
    LinearLayout registration_link, logoutId,cb_link,mymembership_link;
    TextView profileHeadingName, profileHeadingmobile, profileHeadingEmail,profileCardId,cb_text;

    ImageView sprofilephoto;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myaccountnew, container, false);
        ApiUtils.checkNetworkStatus(this.getActivity());
        try{
            initUIElements();
            initOnClickListeners();
            syncLoggedInCustomer();
            customer = LocalCache.retrieveLoggedInCustomer(this.getActivity());
            activeOrder = LocalCache.retrieveActiveOrder(this.getActivity());



            profileHeadingName.setText(customer.getFirstname()+" "+customer.getLastname());
            profileHeadingmobile.setText("+91 "+customer.getMobile1());
            profileHeadingEmail.setText(customer.getEmail());
            profileCardId.setText("Profile id : A"+customer.getProfileId());
            Glide.with(this.getActivity())
                    .load(customer.getProfilephotoaddress() != null ? customer.getProfilephotoaddress() : R.drawable.prashant)
                    .placeholder(R.drawable.oops)
                    .into(sprofilephoto);

            // handle contact balance
            if(activeOrder != null && activeOrder.getId() != null){
                int balance = Integer.parseInt(activeOrder.getMaxCount()) - Integer.parseInt(activeOrder.getUsedCount());
                cb_card.setVisibility(View.VISIBLE);
                cb_text.setText("Contact Balance : "+balance);
            }
            else
                cb_card.setVisibility(View.GONE);

            ApiCallUtil.syncAccountBalance(customer.getProfileId(), this.getActivity(),cb_card,cb_text,true);
        }
        catch (Exception e){
            Log.i("ss_nw_call","ErrorAla : myaccountfragment"+e.getMessage());
        }


        return view;
    }

    private void initOnClickListeners() {
        mymembership_link.setOnClickListener(view -> {
            ApiUtils.vibrateFunction(this.getActivity());
            Intent intent = new Intent(this.getActivity(), MyMembershipActivity.class);
            startActivity(intent);
        });
        registration_link.setOnClickListener(view -> {
            ApiUtils.vibrateFunction(this.getActivity());
            Intent intent = new Intent(this.getActivity(), RegistrationActivity.class);
            startActivity(intent);
        });

        cb_link.setOnClickListener(view -> {
            ApiUtils.vibrateFunction(this.getActivity());
            Intent intent = new Intent(this.getActivity(), ContactViewedActivity.class);
            startActivity(intent);
        });

        logoutId.setOnClickListener(view -> {
            logoutId.setEnabled(false);
            handleLogout();
        });


    }





    private void initUIElements() {
        profileHeadingName = view.findViewById(R.id.profileHeadingName);
        profileHeadingmobile = view.findViewById(R.id.profileHeadingmobile);
        profileHeadingEmail = view.findViewById(R.id.profileHeadingEmail);
        profileCardId = view.findViewById(R.id.profileCardId);

        sprofilephoto = view.findViewById(R.id.sprofilephoto);
        registration_link = view.findViewById(R.id.registration_link);
        mymembership_link = view.findViewById(R.id.mymembership_link);
        cb_link = view.findViewById(R.id.cb_link);
        cb_card = view.findViewById(R.id.cb_card);
        cb_text = view.findViewById(R.id.cb_text);



        logoutId = view.findViewById(R.id.logoutId);
        CircularImageView cView = view.findViewById(R.id.sprofilephoto);

        if (cView != null) {
            Glide.with(this.getActivity())
                    .load(R.drawable.prashant)
                    .placeholder(R.drawable.oops)
                    .into(cView);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void handleLogout() {
        if (ApiUtils.isConnected()) {
            LocalCache.saveLoggedInCustomer(new Customer(), this.getActivity());
            LocalCache.saveActiveOrder(new OrderModal(), this.getActivity());
            LocalCache.saveLevel1List(new ArrayList<>(), this.getActivity());
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this.getActivity(), SendOtpActivity.class);
            intent.putExtra("logout", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void syncLoggedInCustomer() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            ApiCallUtil.updateLoggedInCustomerDetails(this.getActivity(),user.getPhoneNumber().replace("+91",""));
        }
    }

}