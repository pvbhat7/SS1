package com.example.ss1.ui.settings;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ss1.LocalCache;
import com.example.ss1.R;
import com.example.ss1.activity.ContactViewedActivity;
import com.example.ss1.activity.SendOtpActivity;
import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.api.ApiUtils;
import com.example.ss1.modal.Customer;
import com.example.ss1.modal.OrderModal;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyAccountFragment extends Fragment {

    private View view;
    Customer customer;

    OrderModal activeOrder;

    CardView cb_card;
    LinearLayout registration_link, logoutId,cb_link;
    TextView profileHeadingName, profileHeadingmobile, profileHeadingEmail,profileCardId,cb_text;

    ImageView sprofilephoto;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myaccountnew, container, false);

        try{
            initUIElements();
            initOnClickListeners();
            syncLoggedInCustomer();
            customer = LocalCache.retrieveLoggedInCustomer(this.getActivity());
            activeOrder = LocalCache.retrieveActiveOrder(this.getActivity());



            profileHeadingName.setText(customer.getFirstName()+" "+customer.getLastName());
            profileHeadingmobile.setText("+91 "+customer.getMobile1());
            profileHeadingEmail.setText(customer.getEmail());
            profileCardId.setText("Profile id : A"+customer.getProfileId());
            Glide.with(this.getActivity())
                    .load(customer.getProfilePhotoAddress() != null ? customer.getProfilePhotoAddress() : R.drawable.prashant)
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

            ApiCallUtil.syncAccountBalance(customer.getProfileId(), this.getActivity());
        }
        catch (Exception e){
            Log.i("ss_nw_call","ErrorAla : myaccountfragment"+e.getMessage());
        }


        return view;
    }

    private void initOnClickListeners() {
        registration_link.setOnClickListener(view -> {
            Dialog d = new Dialog(this.getContext());
            d.setContentView(R.layout.registration_dialog);

            String[] bloodGroupArray = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
            ((AutoCompleteTextView) d.findViewById(R.id.bloodGroupDropdown)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, bloodGroupArray));

            String[] educationArray = {"B.E.", "B.Sc", "B.Ed", "10th Pass"};
            ((AutoCompleteTextView) d.findViewById(R.id.educationDropdown)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, educationArray));

            String[] occupationArray = {"Business", "Job"};
            ((AutoCompleteTextView) d.findViewById(R.id.occupationDropdown)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, occupationArray));

            String[] incomeArray = {"1-3 lakh", "3-5 lakh", "5-8 lakh", "8-12 lakh", "12+ lakh"};
            ((AutoCompleteTextView) d.findViewById(R.id.incomeDropdown)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, incomeArray));

            String[] birthplaceArray = {"Kolhapur", "Pune", "Mumbai"};
            ((AutoCompleteTextView) d.findViewById(R.id.birthplaceDropdown)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, birthplaceArray));

            String[] zodiacArray = {"virgo", "taurus", "Mumbai"};
            ((AutoCompleteTextView) d.findViewById(R.id.zodiacDropdown)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, zodiacArray));

            String[] casteArray = {"Hindu", "Brahmin"};
            ((AutoCompleteTextView) d.findViewById(R.id.casteDropdown)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, casteArray));

            d.findViewById(R.id.cancel_btn).setOnClickListener(view12 -> {
                d.dismiss();
            });
            d.findViewById(R.id.dob_field).setOnClickListener(view1 -> {
                //Date Picker
                MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
                MaterialDatePicker<Long> picker = builder.build();
                picker.show(getFragmentManager(), picker.toString());
                picker.addOnPositiveButtonClickListener(selectedDate -> {
                    // Do something...
                    //Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    //calendar.setTimeInMillis(selection);
                    Date date = new Date(selectedDate);
                    SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    ((TextView) d.findViewById(R.id.dob_field)).setText(simpleFormat.format(date));
                });
            });
            d.findViewById(R.id.time_field).setOnClickListener(view1 -> {
                //Time Picker
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int min = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(), (timePicker, hour1, min1) -> {
                    String time = hour1 + " : " + min1;
                    ((TextView) d.findViewById(R.id.time_field)).setText(time);

                }, hour, min, true);
                timePickerDialog.show();
            });

            d.setCancelable(false);
            d.setCanceledOnTouchOutside(false);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            d.show();
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