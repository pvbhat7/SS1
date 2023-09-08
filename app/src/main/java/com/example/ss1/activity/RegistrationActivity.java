package com.example.ss1.activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ss1.R;
import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.modal.Customer;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity {

    Button save_btn,cancen_btn ;
    CardView addcard;
    LinearLayout formLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


            init();

        addcard.setOnClickListener(view -> {
                formLayout.setVisibility(View.VISIBLE);
                addcard.setVisibility(View.GONE);
                initFormData();
            });

        save_btn.setOnClickListener(view -> {
            createProfile();
        });
        cancen_btn.setOnClickListener(view -> {
            formLayout.setVisibility(View.GONE);
            addcard.setVisibility(View.VISIBLE);
        });

    }

    private void init(){
        save_btn = findViewById(R.id.save_btn);
        cancen_btn = findViewById(R.id.cancel_btn);
        addcard = findViewById(R.id.add);
        formLayout = findViewById(R.id.formLayout);
    }

    private void initFormData() {

        findViewById(R.id.birthdate).setOnClickListener(view1 -> {
            //Date Picker
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            MaterialDatePicker<Long> picker = builder.build();
            picker.show(getSupportFragmentManager(), picker.toString());
            picker.addOnPositiveButtonClickListener(selectedDate -> {
                // Do something...
                //Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                //calendar.setTimeInMillis(selection);
                Date date = new Date(selectedDate);
                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                ((TextView) findViewById(R.id.birthdate)).setText(simpleFormat.format(date));
            });
        });
        findViewById(R.id.birthtime).setOnClickListener(view1 -> {
            //Time Picker
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR);
            int min = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hour1, min1) -> {
                String time = hour1 + " : " + min1;
                ((TextView) findViewById(R.id.birthtime)).setText(time);

            }, hour, min, true);
            timePickerDialog.show();
        });


        String[] bloodGroupArray = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ((AutoCompleteTextView) findViewById(R.id.bloodgroup)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, bloodGroupArray));

        String[] educationArray = {"B.E.", "B.Sc", "B.Ed", "10th Pass"};
        ((AutoCompleteTextView) findViewById(R.id.education)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, educationArray));

        String[] occupationArray = {"Business", "Job"};
        ((AutoCompleteTextView) findViewById(R.id.occupation)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, occupationArray));

        String[] genderArray = {"male", "female"};
        ((AutoCompleteTextView) findViewById(R.id.gender)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, genderArray));

        String[] dayArray = {"रविवार","सोमवार","मंगळवार","बुधवार","गुरुवार","शुक्रवार","शनिवार"};
        ((AutoCompleteTextView) findViewById(R.id.birthday)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, dayArray));

        String[] marriagestatusArray = {"unmarried", "divorsed","widowed"};
        ((AutoCompleteTextView) findViewById(R.id.marriagestatus)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, marriagestatusArray));

        String[] incomeArray = {"1-3 lakh", "3-5 lakh", "5-8 lakh", "8-12 lakh", "12+ lakh"};
        ((AutoCompleteTextView) findViewById(R.id.income)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, incomeArray));

        String[] birthplaceArray = {"Kolhapur", "Pune", "Mumbai"};
        ((AutoCompleteTextView) findViewById(R.id.birthplace)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, birthplaceArray));
        ((AutoCompleteTextView) findViewById(R.id.city)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, birthplaceArray));

        String[] zodiacArray = {"मेष | Aries","वृषभ | Taurus","मिथुन | Gemini","कर्क | Cancer","सिंह | Leo","कन्या | Virgo","तुला | Libra",
                "वृश्चिक | Scorpio","धनु | Sagittarius","मकर | Capricorn","कुंभ | Aquarius","मीन | Pisces"};
        ((AutoCompleteTextView) findViewById(R.id.zodiac)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, zodiacArray));


        String[] relationArray = {"बहीण", "भाऊ", "चुलते", "मामा", "आत्या", "मावशी", "आजोबा", "आजी"};
        ((AutoCompleteTextView) findViewById(R.id.relation1)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, relationArray));
        ((AutoCompleteTextView) findViewById(R.id.relation2)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, relationArray));
        ((AutoCompleteTextView) findViewById(R.id.relation3)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, relationArray));
        ((AutoCompleteTextView) findViewById(R.id.relation4)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, relationArray));
        ((AutoCompleteTextView) findViewById(R.id.relation5)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, relationArray));

        String[] casteArray = {"Hindu", "Brahmin"};
        ((AutoCompleteTextView) findViewById(R.id.caste)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, casteArray));

    }

    private void createProfile() {
        String creationsource = "mobile app";
        String profilephotoaddress="";
        String biodataaddress="";

        String firstname = ((TextInputEditText) findViewById(R.id.firstname)).getText().toString().trim();
        String middlename = ((TextInputEditText) findViewById(R.id.middlename)).getText().toString().trim();
        String lastname  = ((TextInputEditText) findViewById(R.id.lastname)).getText().toString().trim();
        String email  = ((TextInputEditText) findViewById(R.id.email)).getText().toString().trim();
        String mobile1  = ((TextInputEditText) findViewById(R.id.mobile1)).getText().toString().trim();
        String mobile2  = ((TextInputEditText) findViewById(R.id.mobile2)).getText().toString().trim();
        String gender  = ((AutoCompleteTextView) findViewById(R.id.gender)).getText().toString().trim();
        String height = ((TextInputEditText) findViewById(R.id.height)).getText().toString().trim();
        String birthtime = ((TextInputEditText) findViewById(R.id.birthtime)).getText().toString().trim();
        String caste = ((AutoCompleteTextView) findViewById(R.id.caste)).getText().toString().trim();
        String education = ((AutoCompleteTextView) findViewById(R.id.education)).getText().toString().trim();
        String occupation = ((AutoCompleteTextView) findViewById(R.id.occupation)).getText().toString().trim();
        String zodiac = ((AutoCompleteTextView) findViewById(R.id.zodiac)).getText().toString().trim();
        String birthName = ((TextInputEditText) findViewById(R.id.birthname)).getText().toString().trim();
        String bloodGroup = ((AutoCompleteTextView) findViewById(R.id.bloodgroup)).getText().toString().trim();
        String property = ((TextInputEditText) findViewById(R.id.property)).getText().toString().trim();
        String fatherName = ((TextInputEditText) findViewById(R.id.fathername)).getText().toString().trim();
        String motherName = ((TextInputEditText) findViewById(R.id.mothername)).getText().toString().trim();
        String address = ((TextInputEditText) findViewById(R.id.address)).getText().toString().trim();
        String city = ((AutoCompleteTextView) findViewById(R.id.city)).getText().toString().trim();
        String marriageStatus = ((AutoCompleteTextView) findViewById(R.id.marriagestatus)).getText().toString().trim();
        String birthdate = ((TextInputEditText) findViewById(R.id.birthdate)).getText().toString().trim();
        String birthday = ((AutoCompleteTextView) findViewById(R.id.birthday)).getText().toString().trim();
        String birthplace = ((AutoCompleteTextView) findViewById(R.id.birthplace)).getText().toString().trim();
        String income = ((AutoCompleteTextView) findViewById(R.id.income)).getText().toString().trim();
        String kuldaivat = ((TextInputEditText) findViewById(R.id.kuldaivat)).getText().toString().trim();
        String devak = ((TextInputEditText) findViewById(R.id.devak)).getText().toString().trim();
        String nakshatra = ((TextInputEditText) findViewById(R.id.nakshatra)).getText().toString().trim();
        String nadi = ((TextInputEditText) findViewById(R.id.nadi)).getText().toString().trim();
        String gan = ((TextInputEditText) findViewById(R.id.gan)).getText().toString().trim();
        String yoni = ((TextInputEditText) findViewById(R.id.yoni)).getText().toString().trim();
        String charan = ((TextInputEditText) findViewById(R.id.charan)).getText().toString().trim();
        String gotra = ((TextInputEditText) findViewById(R.id.gotra)).getText().toString().trim();
        String varn = ((TextInputEditText) findViewById(R.id.varn)).getText().toString().trim();
        String mangal = ((TextInputEditText) findViewById(R.id.mangal)).getText().toString().trim();
        String expectations = ((TextInputEditText) findViewById(R.id.expectations)).getText().toString().trim();

        Customer customer = new Customer(creationsource, profilephotoaddress, biodataaddress,
                firstname, middlename, lastname, email, mobile1, mobile2, gender, height,
                birthtime, caste, education, occupation, zodiac, birthName, bloodGroup,
                property, fatherName, motherName, address, city, marriageStatus, birthdate,
                birthday, birthplace, income, kuldaivat, devak, nakshatra, nadi, gan, yoni,
                charan, gotra, varn, mangal, expectations);

        ApiCallUtil.registerProfile(customer,this);
        formLayout.setVisibility(View.GONE);
        addcard.setVisibility(View.VISIBLE);

    }
    
}
