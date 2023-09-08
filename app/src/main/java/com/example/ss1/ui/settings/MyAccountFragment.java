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
import com.example.ss1.modal.Level_1_cardModal;
import com.example.ss1.modal.OrderModal;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
        ApiUtils.checkNetworkStatus(this.getActivity());
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

            ApiCallUtil.syncAccountBalance(customer.getProfileId(), this.getActivity(),cb_card,cb_text,true);
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

            initArrays(d);
            d.findViewById(R.id.cancel_btn).setOnClickListener(view12 -> {
                d.dismiss();
            });
            d.findViewById(R.id.save_btn).setOnClickListener(view12 -> {
                createProfile(d);
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

    private void createProfile(Dialog d) {
        String creationSource = "mobile app";
        String profilePhotoAddress;
        String biodataAddress;

        String firstName = ((TextInputEditText) d.findViewById(R.id.firstName)).getText().toString().trim();
        String middleName = ((TextInputEditText) d.findViewById(R.id.middleName)).getText().toString().trim();
        String lastName  = ((TextInputEditText) d.findViewById(R.id.lastName)).getText().toString().trim();
        String email  = ((TextInputEditText) d.findViewById(R.id.email)).getText().toString().trim();
        String mobile1  = ((TextInputEditText) d.findViewById(R.id.mobile1)).getText().toString().trim();
        String mobile2  = ((TextInputEditText) d.findViewById(R.id.mobile2)).getText().toString().trim();
        String gender  = ((AutoCompleteTextView) d.findViewById(R.id.gender)).getText().toString().trim();
        String height = ((TextInputEditText) d.findViewById(R.id.height)).getText().toString().trim();
        String birthtime = ((TextInputEditText) d.findViewById(R.id.birthtime)).getText().toString().trim();
        String caste = ((AutoCompleteTextView) d.findViewById(R.id.caste)).getText().toString().trim();
        String education = ((AutoCompleteTextView) d.findViewById(R.id.education)).getText().toString().trim();
        String occupation = ((AutoCompleteTextView) d.findViewById(R.id.occupation)).getText().toString().trim();
        String zodiac = ((AutoCompleteTextView) d.findViewById(R.id.zodiac)).getText().toString().trim();
        String birthName = ((TextInputEditText) d.findViewById(R.id.birthname)).getText().toString().trim();
        String bloodGroup = ((AutoCompleteTextView) d.findViewById(R.id.bloodgroup)).getText().toString().trim();
        String property = ((TextInputEditText) d.findViewById(R.id.property)).getText().toString().trim();
        String fatherName = ((TextInputEditText) d.findViewById(R.id.fathername)).getText().toString().trim();
        String motherName = ((TextInputEditText) d.findViewById(R.id.mothername)).getText().toString().trim();
        String address = ((TextInputEditText) d.findViewById(R.id.address)).getText().toString().trim();
        String city = ((AutoCompleteTextView) d.findViewById(R.id.city)).getText().toString().trim();
        String marriageStatus = ((AutoCompleteTextView) d.findViewById(R.id.marriagestatus)).getText().toString().trim();
        String birthdate = ((TextInputEditText) d.findViewById(R.id.birthdate)).getText().toString().trim();
        String birthday = ((AutoCompleteTextView) d.findViewById(R.id.birthday)).getText().toString().trim();
        String birthplace = ((AutoCompleteTextView) d.findViewById(R.id.birthplace)).getText().toString().trim();
        String income = ((AutoCompleteTextView) d.findViewById(R.id.income)).getText().toString().trim();
        String kuldaivat = ((TextInputEditText) d.findViewById(R.id.kuldaivat)).getText().toString().trim();
        String devak = ((TextInputEditText) d.findViewById(R.id.devak)).getText().toString().trim();
        String nakshatra = ((TextInputEditText) d.findViewById(R.id.nakshatra)).getText().toString().trim();
        String nadi = ((TextInputEditText) d.findViewById(R.id.nadi)).getText().toString().trim();
        String gan = ((TextInputEditText) d.findViewById(R.id.gan)).getText().toString().trim();
        String yoni = ((TextInputEditText) d.findViewById(R.id.yoni)).getText().toString().trim();
        String charan = ((TextInputEditText) d.findViewById(R.id.charan)).getText().toString().trim();
        String gotra = ((TextInputEditText) d.findViewById(R.id.gotra)).getText().toString().trim();
        String varn = ((TextInputEditText) d.findViewById(R.id.varn)).getText().toString().trim();
        String mangal = ((TextInputEditText) d.findViewById(R.id.mangal)).getText().toString().trim();
        String expectations = ((TextInputEditText) d.findViewById(R.id.expectations)).getText().toString().trim();

        System.out.println("hello");

    }

    private void initArrays(Dialog d) {

        d.findViewById(R.id.birthdate).setOnClickListener(view1 -> {
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
                ((TextView) d.findViewById(R.id.birthdate)).setText(simpleFormat.format(date));
            });
        });
        d.findViewById(R.id.birthtime).setOnClickListener(view1 -> {
            //Time Picker
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR);
            int min = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(), (timePicker, hour1, min1) -> {
                String time = hour1 + " : " + min1;
                ((TextView) d.findViewById(R.id.birthtime)).setText(time);

            }, hour, min, true);
            timePickerDialog.show();
        });


        String[] bloodGroupArray = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ((AutoCompleteTextView) d.findViewById(R.id.bloodgroup)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, bloodGroupArray));

        String[] educationArray = {"B.E.", "B.Sc", "B.Ed", "10th Pass"};
        ((AutoCompleteTextView) d.findViewById(R.id.education)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, educationArray));

        String[] occupationArray = {"Business", "Job"};
        ((AutoCompleteTextView) d.findViewById(R.id.occupation)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, occupationArray));

        String[] genderArray = {"male", "female"};
        ((AutoCompleteTextView) d.findViewById(R.id.gender)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, genderArray));

        String[] dayArray = {"रविवार","सोमवार","मंगळवार","बुधवार","गुरुवार","शुक्रवार","शनिवार"};
        ((AutoCompleteTextView) d.findViewById(R.id.birthday)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, dayArray));

        String[] marriagestatusArray = {"unmarried", "divorsed","widowed"};
        ((AutoCompleteTextView) d.findViewById(R.id.marriagestatus)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, marriagestatusArray));

        String[] incomeArray = {"1-3 lakh", "3-5 lakh", "5-8 lakh", "8-12 lakh", "12+ lakh"};
        ((AutoCompleteTextView) d.findViewById(R.id.income)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, incomeArray));

        String[] birthplaceArray = {"Kolhapur", "Pune", "Mumbai"};
        ((AutoCompleteTextView) d.findViewById(R.id.birthplace)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, birthplaceArray));
        ((AutoCompleteTextView) d.findViewById(R.id.city)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, birthplaceArray));

        String[] zodiacArray = {"मेष | Aries","वृषभ | Taurus","मिथुन | Gemini","कर्क | Cancer","सिंह | Leo","कन्या | Virgo","तुला | Libra",
                "वृश्चिक | Scorpio","धनु | Sagittarius","मकर | Capricorn","कुंभ | Aquarius","मीन | Pisces"};
        ((AutoCompleteTextView) d.findViewById(R.id.zodiac)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, zodiacArray));


        String[] relationArray = {"बहीण", "भाऊ", "चुलते", "मामा", "आत्या", "मावशी", "आजोबा", "आजी"};
        ((AutoCompleteTextView) d.findViewById(R.id.relation1)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, relationArray));
        ((AutoCompleteTextView) d.findViewById(R.id.relation2)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, relationArray));
        ((AutoCompleteTextView) d.findViewById(R.id.relation3)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, relationArray));
        ((AutoCompleteTextView) d.findViewById(R.id.relation4)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, relationArray));
        ((AutoCompleteTextView) d.findViewById(R.id.relation5)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, relationArray));

        String[] casteArray = {"Hindu", "Brahmin"};
        ((AutoCompleteTextView) d.findViewById(R.id.caste)).setAdapter(new ArrayAdapter(d.getContext(), R.layout.package_list_item, casteArray));

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