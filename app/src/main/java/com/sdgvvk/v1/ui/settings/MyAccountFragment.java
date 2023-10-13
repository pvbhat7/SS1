package com.sdgvvk.v1.ui.settings;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.sdgvvk.v1.LocalCache;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.activity.ContactViewedActivity;
import com.sdgvvk.v1.activity.MyMembershipActivity;
import com.sdgvvk.v1.activity.RegistrationActivity;
import com.sdgvvk.v1.activity.SendOtpActivity;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.api.HelperUtils;
import com.sdgvvk.v1.modal.Customer;
import com.sdgvvk.v1.modal.OrderModal;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MyAccountFragment extends Fragment {

    private View view;
    Customer customer;

    OrderModal activeOrder;

    static CoordinatorLayout coordinatorLayout;
    CardView cb_card,mymembership_card, adminzone_card, myaccount_profile_card;
    LinearLayout adminzone_link, logoutId, cb_link, mymembership_link, editprofile_link;
    TextView profileHeadingName, profileHeadingmobile, profileHeadingEmail, profileCardId, cb_text;

    ImageView sprofilephoto;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myaccountnew, container, false);
        HelperUtils.checkNetworkStatus(this.getActivity());
        try {
            initUIElements();
            initOnClickListeners();
            syncLoggedInCustomer();
            customer = LocalCache.getLoggedInCustomer(this.getActivity());
            activeOrder = LocalCache.getActiveOrder(this.getActivity());


            if (customer != null) {
                if (customer.getIsAdmin() != null && customer.getIsAdmin().equalsIgnoreCase("1"))
                    adminzone_card.setVisibility(View.VISIBLE);
                else
                    adminzone_card.setVisibility(View.GONE);
            } else
                adminzone_card.setVisibility(View.GONE);


            profileHeadingName.setText(customer.getFirstname() + " " + customer.getLastname());
            profileHeadingmobile.setText("+91 " + customer.getMobile1());
            profileHeadingEmail.setText(customer.getEmail());
            profileCardId.setText("Profile id : A" + customer.getProfileId());
            Glide.with(this.getActivity())
                    .load(customer.getProfilephotoaddress() != null ? customer.getProfilephotoaddress() : R.drawable.oops)
                    .placeholder(R.drawable.oops)
                    .into(sprofilephoto);

            // handle contact balance
            if (activeOrder != null && activeOrder.getId() != null) {
                mymembership_card.setVisibility(View.VISIBLE);
                int balance = Integer.parseInt(activeOrder.getMaxCount()) - Integer.parseInt(activeOrder.getUsedCount());
                cb_card.setVisibility(View.VISIBLE);
                cb_text.setText("Contact Balance : " + balance);
            } else {
                cb_card.setVisibility(View.GONE);
                mymembership_card.setVisibility(View.GONE);
            }

            ApiCallUtil.syncAccountBalance(customer.getProfileId(), this.getActivity(), cb_card,mymembership_card, cb_text, true);
        } catch (Exception e) {
            Log.i("ss_nw_call", "ErrorAla : myaccountfragment" + e.getMessage());
        }


        return view;
    }

    private void initOnClickListeners() {
        myaccount_profile_card.setOnClickListener(view -> {
            ApiCallUtil.getLevel2Data(customer.getProfileId(), this.getActivity());
        });
        editprofile_link.setOnClickListener(view -> {
            HelperUtils.vibrateFunction(this.getActivity());
            Intent intent = new Intent(this.getActivity(), RegistrationActivity.class);
            intent.putExtra("editprofile", true);
            startActivity(intent);
        });
        mymembership_link.setOnClickListener(view -> {
            HelperUtils.vibrateFunction(this.getActivity());
            Intent intent = new Intent(this.getActivity(), MyMembershipActivity.class);
            startActivity(intent);
        });
        adminzone_link.setOnClickListener(view -> {
            Dialog d = new Dialog(this.getActivity());
            d.setContentView(R.layout.admin_zone_access_dialog);
            d.findViewById(R.id.submitAdminCodeBtn).setOnClickListener(view1 -> {
                d.findViewById(R.id.submitAdminCodeBtn).setEnabled(false);
                String inputCode = ((TextInputEditText) d.findViewById(R.id.adminCodeField)).getText().toString().trim();
                if (inputCode.isEmpty()) {
                    d.findViewById(R.id.errorcode).setVisibility(View.VISIBLE);
                    ((TextView) d.findViewById(R.id.errorcode)).setText("Please entry pin");
                } else {
                    ApiCallUtil.validateAdminCode(d, inputCode, this.getActivity());
                }
            });

            ((TextInputEditText) d.findViewById(R.id.adminCodeField)).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    if (s.length() > 0)
                    {
                        d.findViewById(R.id.submitAdminCodeBtn).setEnabled(true);
                        d.findViewById(R.id.errorcode).setVisibility(View.GONE);
                    }
                    else
                    {
                        d.findViewById(R.id.submitAdminCodeBtn).setEnabled(false);
                        d.findViewById(R.id.errorcode).setVisibility(View.VISIBLE);
                        ((TextView) d.findViewById(R.id.errorcode)).setText("Please entry pin");
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });


            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            d.show();
        });

        cb_link.setOnClickListener(view -> {
            HelperUtils.vibrateFunction(this.getActivity());
            Intent intent = new Intent(this.getActivity(), ContactViewedActivity.class);
            startActivity(intent);
        });

        logoutId.setOnClickListener(view -> {
            logoutId.setEnabled(false);
            handleLogout();
        });


    }


    private void initUIElements() {
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        profileHeadingName = view.findViewById(R.id.profileHeadingName);
        profileHeadingmobile = view.findViewById(R.id.profileHeadingmobile);
        profileHeadingEmail = view.findViewById(R.id.profileHeadingEmail);
        profileCardId = view.findViewById(R.id.profileCardId);
        adminzone_card = view.findViewById(R.id.adminzone_card);
        myaccount_profile_card = view.findViewById(R.id.myaccount_profile_card);

        sprofilephoto = view.findViewById(R.id.sprofilephoto);
        adminzone_link = view.findViewById(R.id.adminzone_link);
        mymembership_link = view.findViewById(R.id.mymembership_link);
        editprofile_link = view.findViewById(R.id.editprofile_link);

        cb_link = view.findViewById(R.id.cb_link);
        cb_card = view.findViewById(R.id.cb_card);
        mymembership_card = view.findViewById(R.id.mymembership_card);

        cb_text = view.findViewById(R.id.cb_text);


        logoutId = view.findViewById(R.id.logoutId);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void handleLogout() {
        if (HelperUtils.isConnected()) {
            LocalCache.setLoggedInCustomer(new Customer(), this.getActivity());
            LocalCache.setActiveOrder(new OrderModal(), this.getActivity());
            LocalCache.setLevel1List(new ArrayList<>(), this.getActivity());
            LocalCache.setContactViewedList(new ArrayList<>(), this.getActivity());
            LocalCache.setMembershipList(new ArrayList<>( ), this.getActivity());
            LocalCache.setGenderStat(new ArrayList<>( ), this.getActivity());
            LocalCache.setAdminPhone("", this.getActivity());
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
            ApiCallUtil.updateLoggedInCustomerDetails(this.getActivity(), user.getPhoneNumber().replace("+91", ""));
        }
    }

    public static void showSnackBar(String content) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, content, Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
        snackbar.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ApiCallUtil.syncAccountBalance(customer.getProfileId(), this.getActivity(), cb_card,mymembership_card, cb_text, true);
    }
}