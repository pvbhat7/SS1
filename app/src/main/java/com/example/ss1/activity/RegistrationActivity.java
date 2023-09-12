package com.example.ss1.activity;

import static android.view.View.GONE;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.ss1.R;
import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.api.ApiUtils;
import com.example.ss1.modal.Customer;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity {

    public TextInputEditText cmmobile,email, name, birthname, fathername, birthdate,mothername, mobile1, mobile2, mobile3, mobile4, education, caste, birthtime, property, address, kuldaivat, devak, nakshatra, nadi, gan, yoni, charan, gotra, varn, mangal, expectations, relationname1, relationname2,relatives,family;
    public AutoCompleteTextView gender, bloodgroup, marriagestatus, height, religion, occupation, zodiac, city, birthday, birthplace, income;

    static String clickedImagename , profilePhotoAddressBase64 , biodataAddressBase64;

    public static final int REQUEST_IMAGE = 100,PICK_IMAGE_REQUEST = 1;
    Button save_btn, cancen_btn,cmbtn;
    CardView addcard;
    LinearLayout formLayout,cmlayout;
    CircularImageView profilePhotoAddress, biodataAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
        initFormData();
        handleOnclickListeners();
        handleFormOnClickListeners();

        cmlayout.setVisibility(GONE);
        addcard.setVisibility(View.VISIBLE);
        formLayout.setVisibility(GONE);

    }

    private void handleFormOnClickListeners() {
        marriagestatus.setOnClickListener(view -> {
            hideKeyboard(view);
        });
        height.setOnClickListener(view -> {
            hideKeyboard(view);
        });
        gender.setOnClickListener(view -> {
            hideKeyboard(view);
        });
        bloodgroup.setOnClickListener(view -> {
            hideKeyboard(view);
        });
        birthday.setOnClickListener(view -> {
            hideKeyboard(view);
        });
        birthplace.setOnClickListener(view -> {
            hideKeyboard(view);
        });
        education.setOnClickListener(view -> {
            hideKeyboard(view);
        });
        occupation.setOnClickListener(view -> {
            hideKeyboard(view);
        });
        income.setOnClickListener(view -> {
            hideKeyboard(view);
        });
        city.setOnClickListener(view -> {
            hideKeyboard(view);
        });
        religion.setOnClickListener(view -> {
            hideKeyboard(view);
        });
        zodiac.setOnClickListener(view -> {
            hideKeyboard(view);
        });

    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void handleOnclickListeners() {

        addcard.setOnClickListener(view -> {
            profilePhotoAddressBase64 = "";
            biodataAddressBase64 = "";
            cmlayout.setVisibility(View.VISIBLE);
            addcard.setVisibility(GONE);
        });

        cmbtn.setOnClickListener(view -> {
            ViewUtils.hideKeyboard(view);
            cmlayout.setVisibility(GONE);
            String mobile = ((TextInputEditText) findViewById(R.id.cmmobile)).getText().toString().trim();
            ApiCallUtil.validateLoginMobile(this,mobile,formLayout,addcard,mobile1,save_btn);
        });

        save_btn.setOnClickListener(view -> {
            save_btn.setEnabled(false);
            createProfile();
        });
        cancen_btn.setOnClickListener(view -> {
            nullifyformdata();
            formLayout.setVisibility(GONE);
            addcard.setVisibility(View.VISIBLE);
        });
        profilePhotoAddress.setOnClickListener(view -> {
            clickedImagename = "profilePhotoAddress";
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
        biodataAddress.setOnClickListener(view -> {
            clickedImagename = "biodataAddress";
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        cmmobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.length() == 10)
                    cmbtn.setEnabled(true);
                else
                    cmbtn.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void init() {
        save_btn = findViewById(R.id.save_btn);
        cancen_btn = findViewById(R.id.cancel_btn);
        addcard = findViewById(R.id.add);
        formLayout = findViewById(R.id.formLayout);
        profilePhotoAddress = findViewById(R.id.profilePhotoAddress);
        biodataAddress = findViewById(R.id.biodataAddress);
        cmlayout = findViewById(R.id.cmlayout);
        cmbtn = findViewById(R.id.cmbtn);
        cmmobile = findViewById(R.id.cmmobile);
        email = findViewById(R.id.email);
        mobile1 = findViewById(R.id.mobile1);
        mobile2 = findViewById(R.id.mobile2);
        mobile3 = findViewById(R.id.mobile3);
        mobile4 = findViewById(R.id.mobile4);
        gender = findViewById(R.id.gender);
        height = findViewById(R.id.height);
        birthtime = findViewById(R.id.birthtime);
        caste = findViewById(R.id.caste);
        religion = findViewById(R.id.religion);
        education = findViewById(R.id.education);
        occupation = findViewById(R.id.occupation);
        zodiac = findViewById(R.id.zodiac);
        birthname = findViewById(R.id.birthname);
        bloodgroup = findViewById(R.id.bloodgroup);
        property = findViewById(R.id.property);
        fathername = findViewById(R.id.fathername);
        mothername = findViewById(R.id.mothername);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        marriagestatus = findViewById(R.id.marriagestatus);
        birthdate = findViewById(R.id.birthdate);
        birthday = findViewById(R.id.birthday);
        birthplace = findViewById(R.id.birthplace);
        income = findViewById(R.id.income);
        kuldaivat = findViewById(R.id.kuldaivat);
        devak = findViewById(R.id.devak);
        nakshatra = findViewById(R.id.nakshatra);
        nadi = findViewById(R.id.nadi);
        gan = findViewById(R.id.gan);
        yoni = findViewById(R.id.yoni);
        charan = findViewById(R.id.charan);
        gotra = findViewById(R.id.gotra);
        varn = findViewById(R.id.varn);
        mangal = findViewById(R.id.mangal);
        expectations = findViewById(R.id.expectations);
        name = findViewById(R.id.name);
        relationname1 = findViewById(R.id.relationname1);
        relationname2 = findViewById(R.id.relationname2);
        relatives = findViewById(R.id.relatives);
        family = findViewById(R.id.family);
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

        String[] heightArray = {"4 feet | 121 cm","4 feet 1 inch | 124 cm","4 feet 2 inches | 127 cm","4 feet 3 inches | 130 cm","4 feet 4 inches | 132 cm","4 feet 5 inches | 135 cm","4 feet 6 inches | 138 cm","4 feet 7 inches | 140 cm","4 feet 8 inches | 143 cm","4 feet 9 inches | 145 cm","4 feet 10 inches | 148 cm","4 feet 11 inches | 151 cm","5 feet | 152 cm","5 feet 1 inch | 155 cm","5 feet 2 inches | 157 cm","5 feet 3 inches | 160 cm","5 feet 4 inches | 163 cm","5 feet 5 inches | 165 cm","5 feet 6 inches | 168 cm","5 feet 7 inches | 170 cm","5 feet 8 inches | 173 cm","5 feet 9 inches | 175 cm","5 feet 10 inches | 178 cm","5 feet 11 inches | 180 cm","6 feet | 183 cm","6 feet 1 inch | 185 cm","6 feet 2 inches | 188 cm","6 feet 3 inches | 191 cm","6 feet 4 inches | 193 cm","6 feet 5 inches | 196 cm","6 feet 6 inches | 198 cm","6 feet 7 inches | 201 cm","6 feet 8 inches | 203 cm","6 feet 9 inches | 206 cm","6 feet 10 inches | 208 cm","6 feet 11 inches | 211 cm","7 feet | 213 cm","7 feet 1 inch | 216 cm","7 feet 2 inches | 218 cm","7 feet 3 inches | 221 cm","7 feet 4 inches | 224 cm","7 feet 5 inches | 226 cm","7 feet 6 inches | 229 cm","7 feet 7 inches | 231 cm","7 feet 8 inches | 234 cm","7 feet 9 inches | 237 cm","7 feet 10 inches | 239 cm","7 feet 11 inches | 242 cm"};
        ((AutoCompleteTextView) findViewById(R.id.height)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, heightArray));

        /*String[] educationArray = {"B.E.", "B.Sc", "B.Ed", "10th Pass"};
        ((AutoCompleteTextView) findViewById(R.id.education)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, educationArray));*/

        String[] occupationArray = {"Business", "Job"};
        ((AutoCompleteTextView) findViewById(R.id.occupation)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, occupationArray));

        String[] genderArray = {"male", "female"};
        ((AutoCompleteTextView) findViewById(R.id.gender)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, genderArray));

        String[] dayArray = {"रविवार", "सोमवार", "मंगळवार", "बुधवार", "गुरुवार", "शुक्रवार", "शनिवार"};
        ((AutoCompleteTextView) findViewById(R.id.birthday)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, dayArray));

        String[] marriagestatusArray = {"single","married", "divorsed", "widowed"};
        ((AutoCompleteTextView) findViewById(R.id.marriagestatus)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, marriagestatusArray));

        String[] incomeArray = {"1-3 lakh", "3-5 lakh", "5-8 lakh", "8-12 lakh", "12+ lakh"};
        ((AutoCompleteTextView) findViewById(R.id.income)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, incomeArray));

        String[] birthplaceArray = {"Kolhapur", "Pune", "Mumbai","satara","sangli"};
        ((AutoCompleteTextView) findViewById(R.id.birthplace)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, birthplaceArray));
        ((AutoCompleteTextView) findViewById(R.id.city)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, birthplaceArray));

        String[] zodiacArray = {"मेष | Aries", "वृषभ | Taurus", "मिथुन | Gemini", "कर्क | Cancer", "सिंह | Leo", "कन्या | Virgo", "तुला | Libra",
                "वृश्चिक | Scorpio", "धनु | Sagittarius", "मकर | Capricorn", "कुंभ | Aquarius", "मीन | Pisces"};
        ((AutoCompleteTextView) findViewById(R.id.zodiac)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, zodiacArray));


         String[] religionArray = {"Hindu", "Brahmin","Muslim","Cristian","sikh","jain"};
        ((AutoCompleteTextView) findViewById(R.id.religion)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, religionArray));

    }

    private void createProfile() {
        String creationsource = "mobile app";
        String profilephotoaddress = "";
        String biodataaddress = "";

        String name = ((TextInputEditText) findViewById(R.id.name)).getText().toString().trim();
        String firstname = "",middlename = "",lastname = "";
        String[] nameArr = name.split(" ");
        if(nameArr.length == 1) {
            firstname = nameArr[0];
        }
        else if(nameArr.length == 2){
            firstname = nameArr[0];
            lastname = nameArr[1];
        }
        else if(nameArr.length == 3) {
            firstname = nameArr[0];
            middlename = nameArr[1];
            lastname = nameArr[2];
        }
        else
            firstname =  name;

        String relation1 = "Brother";
        String relation2 = "Sister";

        Customer customer = new Customer(creationsource, profilePhotoAddressBase64, biodataAddressBase64,
                firstname, middlename, lastname, email.getText().toString().trim(), mobile1.getText().toString().trim(), mobile2.getText().toString().trim(),mobile3.getText().toString().trim(),mobile4.getText().toString().trim(), gender.getText().toString().trim(), height.getText().toString().trim(),
                birthtime.getText().toString().trim(), caste.getText().toString().trim(), religion.getText().toString().trim(),education.getText().toString().trim(), occupation.getText().toString().trim(), zodiac.getText().toString().trim(), birthname.getText().toString().trim(), bloodgroup.getText().toString().trim(),
                property.getText().toString().trim(), fathername.getText().toString().trim(), mothername.getText().toString().trim(), address.getText().toString().trim(), city.getText().toString().trim(), marriagestatus.getText().toString().trim(), birthdate.getText().toString().trim(),
                birthday.getText().toString().trim(), birthplace.getText().toString().trim(), income.getText().toString().trim(), kuldaivat.getText().toString().trim(), devak.getText().toString().trim(), nakshatra.getText().toString().trim(), nadi.getText().toString().trim(), gan.getText().toString().trim(), yoni.getText().toString().trim(),
                charan.getText().toString().trim(), gotra.getText().toString().trim(), varn.getText().toString().trim(), mangal.getText().toString().trim(), expectations.getText().toString().trim(),relation1,relation2,relationname1.getText().toString().trim(),relationname2.getText().toString().trim(),relatives.getText().toString().trim(),family.getText().toString().trim());

        ApiCallUtil.registerProfile(customer, this);
        nullifyformdata();
        cmmobile.setText("");
        formLayout.setVisibility(GONE);
        addcard.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                if(data != null){
                    //Uri uri = data.getParcelableExtra("path");
                    Uri uri = data.getData();
                    try {
                        // You can update this bitmap to your server
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        String imgB64 = ApiUtils.convertBitmapToString(bitmap, 500);


                        if (clickedImagename != null) {
                            if(clickedImagename.equalsIgnoreCase("profilePhotoAddress")){
                                profilePhotoAddressBase64 = imgB64;
                                Glide.with(this).load(uri.toString()).placeholder(R.drawable.progym_icon).into(profilePhotoAddress);
                            }
                            if(clickedImagename.equalsIgnoreCase("biodataAddress")){
                                biodataAddressBase64 = imgB64;
                                Glide.with(this).load(uri.toString()).placeholder(R.drawable.progym_icon).into(biodataAddress);
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void nullifyformdata(){
        name.setText("");
        email.setText("");
        mobile1.setText("");
        mobile2.setText("");
        mobile3.setText("");
        mobile4.setText("");
        gender.setText("");
        height.setText("");
        birthtime.setText("");
        caste.setText("");
        religion.setText("");
        education.setText("");
        occupation.setText("");
        zodiac.setText("");
        birthname.setText("");
        bloodgroup.setText("");
        property.setText("");
        fathername.setText("");
        mothername.setText("");
        address.setText("");
        city.setText("");
        marriagestatus.setText("");
        birthdate.setText("");
        birthday.setText("");
        birthplace.setText("");
        income.setText("");
        kuldaivat.setText("");
        devak.setText("");
        nakshatra.setText("");
        nadi.setText("");
        gan.setText("");
        yoni.setText("");
        charan.setText("");
        gotra.setText("");
        varn.setText("");
        mangal.setText("");
        expectations.setText("");
        relationname1.setText("");
        relationname2.setText("");
        family.setText("");
        relatives.setText("");



    }
}
