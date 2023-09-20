package com.example.ss1.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ss1.LocalCache;
import com.example.ss1.R;
import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.api.HelperUtils;
import com.example.ss1.modal.MembershipModal;
import com.example.ss1.modal.OrderModal;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminZoneActivity extends AppCompatActivity {

    LinearLayout link1, link2;

    CardView card1, card2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminzone);

        initUIElements();
        onclickListeners();
        ApiCallUtil.syncStats(this);
    }

    private void onclickListeners() {
        link1.setOnClickListener(view -> {
            HelperUtils.vibrateFunction(AdminZoneActivity.this);
            Intent intent = new Intent(AdminZoneActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
        link2.setOnClickListener(view -> {
            Dialog d = new Dialog(this);
            Activity activity = this;
            d.setContentView(R.layout.assign_membership_dialog);


            handleAssignMembership(d, activity);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            d.show();
        });
        card1.setOnClickListener(view -> {
            HelperUtils.vibrateFunction(AdminZoneActivity.this);
            Intent intent = new Intent(AdminZoneActivity.this, AllMemberActivity.class);
            intent.putExtra("gender", "male");
            startActivity(intent);
        });
        card2.setOnClickListener(view -> {
            HelperUtils.vibrateFunction(AdminZoneActivity.this);
            Intent intent = new Intent(AdminZoneActivity.this, AllMemberActivity.class);
            intent.putExtra("gender", "female");
            startActivity(intent);
        });

    }


    private void initUIElements() {
        link1 = findViewById(R.id.link1);
        link2 = findViewById(R.id.link2);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApiCallUtil.syncStats(this);
    }

    private void handleAssignMembership(Dialog d, Activity activity) {
        d.findViewById(R.id.searchBtn).setOnClickListener(view14 -> {
            d.findViewById(R.id.searchBtn).setEnabled(false);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(d.getCurrentFocus().getWindowToken(), 0);
            ((TextView) d.findViewById(R.id.selectedProfileName)).setText("");
            ((TextView) d.findViewById(R.id.selectedProfileId)).setText("");
            String searchBy = ((AutoCompleteTextView) d.findViewById(R.id.searchFilter)).getText().toString().trim();
            String value = ((TextInputEditText) d.findViewById(R.id.searchValue)).getText().toString().trim();
            RecyclerView recyclerView = d.findViewById(R.id.searchResultListRecyclerView);
            LinearLayout downLayout = d.findViewById(R.id.downLayout);
            ApiCallUtil.searchProfilesBy(d, activity, searchBy, value, recyclerView, downLayout);
        });

        String[] searchByArray = {"name", "mobile", "profile id",};
        ((AutoCompleteTextView) d.findViewById(R.id.searchFilter)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, searchByArray));

        String[] paymentModeArray = {"Cash", "Google Pay", "Phone Pe", "Paytm"};
        ((AutoCompleteTextView) d.findViewById(R.id.paymentmode)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, paymentModeArray));

        List<MembershipModal> membershiplist = LocalCache.getMembershipList(this);
        ((AutoCompleteTextView) d.findViewById(R.id.selectplan)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, membershiplist.toArray()));

        d.findViewById(R.id.txnDate).setOnClickListener(view1 -> {
            //Date Picker
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            MaterialDatePicker<Long> picker = builder.build();
            picker.show(getSupportFragmentManager(), picker.toString());
            picker.addOnPositiveButtonClickListener(selectedDate -> {
                Date date = new Date(selectedDate);
                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                ((TextInputEditText) d.findViewById(R.id.txnDate)).setText(simpleFormat.format(date));
            });

        });

        ((TextInputEditText) d.findViewById(R.id.txnDate)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                validateAssignMembershipForm(d);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        d.findViewById(R.id.searchFilter).setOnClickListener(view12 -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(d.getCurrentFocus().getWindowToken(), 0);
            ((TextInputEditText) d.findViewById(R.id.searchValue)).setText("");
            d.findViewById(R.id.downLayout).setVisibility(View.GONE);
            d.findViewById(R.id.errorTxt).setVisibility(View.GONE);
        });

        ((AutoCompleteTextView) d.findViewById(R.id.searchFilter)).setOnItemClickListener((parent, arg1, pos, id) -> {
            String selection = String.valueOf(parent.getAdapter().getItem(pos));
            if (selection.equalsIgnoreCase("name")) {
                ((TextInputEditText) d.findViewById(R.id.searchValue)).setInputType(InputType.TYPE_CLASS_TEXT);
            } else {

                ((TextInputEditText) d.findViewById(R.id.searchValue)).setInputType(InputType.TYPE_CLASS_NUMBER);
            }

        });
        ((AutoCompleteTextView) d.findViewById(R.id.selectplan)).setOnItemClickListener((parent, arg1, pos, id) -> {
            MembershipModal obj = (MembershipModal) parent.getAdapter().getItem(pos);
            ((TextView) d.findViewById(R.id.fees)).setText("Rs. " + obj.getFees());
            ((TextView) d.findViewById(R.id.count)).setText("Contacts : " + obj.getCount());
            ((TextView) d.findViewById(R.id.days)).setText("Days " + obj.getDays());
            ((TextView) d.findViewById(R.id.membershipId)).setText(obj.getId());
            validateAssignMembershipForm(d);

        });
        ((AutoCompleteTextView) d.findViewById(R.id.paymentmode)).setOnItemClickListener((parent, arg1, pos, id) -> {
            validateAssignMembershipForm(d);
        });

        d.findViewById(R.id.cancelBtn).setOnClickListener(view13 -> d.dismiss());
        d.findViewById(R.id.cancelBtn1).setOnClickListener(view13 -> d.dismiss());

        d.findViewById(R.id.submitBtn).setOnClickListener(view14 -> {
            d.findViewById(R.id.submitBtn).setEnabled(false);

            String days = ((TextView) d.findViewById(R.id.days)).getText().toString().trim();

            // create order object
            String cpid = ((TextView) d.findViewById(R.id.selectedProfileId)).getText().toString().trim();
            String membershipId = ((TextView) d.findViewById(R.id.membershipId)).getText().toString().trim();
            String paymentmode = ((AutoCompleteTextView)d.findViewById(R.id.paymentmode)).getText().toString().trim();
            String txnDate = ((TextInputEditText)d.findViewById(R.id.txnDate)).getText().toString().trim();
            String startDate = ((TextInputEditText)d.findViewById(R.id.txnDate)).getText().toString().trim();
            String endDate = HelperUtils.addDaysToDate(startDate,days.replace("Days ",""));

            OrderModal o = new OrderModal(cpid,membershipId,paymentmode,txnDate,startDate,endDate);
            d.dismiss();
            ApiCallUtil.assignMembership(o,activity);
        });

    }

    private void validateAssignMembershipForm(Dialog d){

        if( !((AutoCompleteTextView)d.findViewById(R.id.selectplan)).getText().toString().trim().isEmpty()
            && !((AutoCompleteTextView)d.findViewById(R.id.paymentmode)).getText().toString().trim().isEmpty()
                && !((TextInputEditText)d.findViewById(R.id.txnDate)).getText().toString().trim().isEmpty()
                && !((TextView)d.findViewById(R.id.selectedProfileName)).getText().toString().trim().isEmpty()
                && !((TextView)d.findViewById(R.id.selectedProfileId)).getText().toString().trim().isEmpty())
            d.findViewById(R.id.submitBtn).setEnabled(true);
        else
            d.findViewById(R.id.submitBtn).setEnabled(false);

    }
}
