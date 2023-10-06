package com.example.ss1.activity;

import static android.view.View.GONE;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
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
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.ss1.LocalCache;
import com.example.ss1.R;
import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.api.HelperUtils;
import com.example.ss1.modal.Customer;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity {

    private boolean lockAspectRatio = false, setBitmapMaxWidthHeight = false;
    private int ASPECT_RATIO_X = 16, ASPECT_RATIO_Y = 9, bitmapMaxWidth = 1000, bitmapMaxHeight = 1000;
    private int IMAGE_COMPRESSION = 80;
    public static String fileName;

    public TextInputEditText cmmobile, email, name, birthname, fathername, birthdate,occupation, mothername, mobile1, mobile2, mobile3, mobile4, education, caste, property, address, kuldaivat, devak, nakshatra, nadi, gan, yoni, charan, gotra, varn, mangal, expectations, relationname1, relationname2, relatives, family;
    public AutoCompleteTextView gender, bloodgroup, marriagestatus, height, religion, zodiac, city, birthplace, income, hour, minute, ampm;

    static String clickedImagename, profilePhotoAddressBase64, biodataAddressBase64;

    public static final int PICK_IMAGE_REQUEST = 1;
    Button save_btn, cancel_btn, cmbtn;
    LinearLayout formLayout, cmlayout;
    CircularImageView profilePhotoAddress, biodataAddress;

    Customer customer;

    Boolean editprofile = false;
    String profile ;
    Boolean updateCache = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();

        customer = LocalCache.getLoggedInCustomer(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            editprofile = extras.getBoolean("editprofile");
            profile = extras.getString("profile");
            if(profile != null && !profile.isEmpty()){
                customer = LocalCache.convertJsonToObjectCustomer(profile);
                updateCache = false;
            }
        }


        if (editprofile) {
            formLayout.setVisibility(View.VISIBLE);
            findViewById(R.id.save_btn).setEnabled(true);
            cancel_btn.setVisibility(GONE);
            preFillFormData();
        } else {
            profilePhotoAddressBase64 = "";
            biodataAddressBase64 = "";
            cmlayout.setVisibility(View.VISIBLE);
            formLayout.setVisibility(GONE);
        }

        initFormData();
        handleOnclickListeners();
        handleFormOnClickListeners();
    }

    private void preFillFormData() {
        Glide.with(this).load(customer.getProfilephotoaddress()).placeholder(R.drawable.oops).into(profilePhotoAddress);
        Glide.with(this).load(customer.getBiodataaddress()).placeholder(R.drawable.oops).into(biodataAddress);
        email.setText(customer.getEmail());
        mobile1.setText(customer.getMobile1());
        mobile2.setText(customer.getMobile2());
        mobile3.setText(customer.getMobile3());
        mobile4.setText(customer.getMobile4());
        gender.setText(customer.getGender());
        height.setText(customer.getHeight());
        if(customer.getBirthtime() != null && !customer.getBirthtime().isEmpty()){
            String birthtime = customer.getBirthtime();// 01:01 am
            String[] arr1 = birthtime.split(":");

            if(arr1.length == 2){
                hour.setText(arr1[0]);
                String[] arr2 = arr1[1].split(" ");
                if(arr2.length == 2){
                    minute.setText(arr2[0]);
                    ampm.setText(arr2[1]);
                }
            }

        }

        caste.setText(customer.getCaste());
        religion.setText(customer.getReligion());
        education.setText(customer.getEducation());
        occupation.setText(customer.getOccupation());
        zodiac.setText(customer.getZodiac());
        birthname.setText(customer.getBirthname());
        bloodgroup.setText(customer.getBloodgroup());
        property.setText(customer.getProperty());
        fathername.setText(customer.getFathername());
        mothername.setText(customer.getMothername());
        address.setText(customer.getAddress());
        city.setText(customer.getCity());
        marriagestatus.setText(customer.getMarriagestatus());
        birthdate.setText(customer.getBirthdate());
        birthplace.setText(customer.getBirthplace());
        income.setText(customer.getIncome());
        kuldaivat.setText(customer.getKuldaivat());
        devak.setText(customer.getDevak());
        nakshatra.setText(customer.getNakshatra());
        nadi.setText(customer.getNadi());
        gan.setText(customer.getGan());
        yoni.setText(customer.getYoni());
        charan.setText(customer.getCharan());
        gotra.setText(customer.getGotra());
        varn.setText(customer.getVarn());
        mangal.setText(customer.getMangal());
        expectations.setText(customer.getExpectations());
        name.setText((customer.getFirstname()) + " " + (customer.getMiddlename() != null ? customer.getMiddlename() : "") + " " + (customer.getLastname() != null ? customer.getLastname() : ""));
        relationname1.setText(customer.getRelationname1());
        relationname2.setText(customer.getRelationname2());
        relatives.setText(customer.getRelatives());
        family.setText(customer.getFamily());
    }

    private void handleFormOnClickListeners() {
        hour.setOnClickListener(view -> {
            hideKeyboard(view);
        });
        minute.setOnClickListener(view -> {
            hideKeyboard(view);
        });
        ampm.setOnClickListener(view -> {
            hideKeyboard(view);
        });
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

        birthplace.setOnClickListener(view -> {
            hideKeyboard(view);
        });
        education.setOnClickListener(view -> {
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
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    private void handleOnclickListeners() {



        cmbtn.setOnClickListener(view -> {
            ViewUtils.hideKeyboard(view);
            cmlayout.setVisibility(GONE);
            String mobile = ((TextInputEditText) findViewById(R.id.cmmobile)).getText().toString().trim();
            ApiCallUtil.validateLoginMobile(this, mobile, formLayout, cmlayout, mobile1, save_btn);
        });

        save_btn.setOnClickListener(view -> {
            save_btn.setEnabled(false);
            createProfile();
        });
        cancel_btn.setOnClickListener(view -> {
            nullifyformdata();
            formLayout.setVisibility(GONE);
            cmlayout.setVisibility(View.VISIBLE);
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

        ((AutoCompleteTextView) findViewById(R.id.height)).setOnItemClickListener((parent, arg1, pos, id) -> {
            validateForm();
        });
        ((AutoCompleteTextView) findViewById(R.id.gender)).setOnItemClickListener((parent, arg1, pos, id) -> {
            validateForm();
        });
        ((TextInputEditText) findViewById(R.id.name)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                validateForm();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        ((TextInputEditText) findViewById(R.id.birthdate)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                validateForm();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void init() {
        save_btn = findViewById(R.id.save_btn);
        cancel_btn = findViewById(R.id.cancel_btn);
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
        hour = findViewById(R.id.hour);
        minute = findViewById(R.id.minute);
        ampm = findViewById(R.id.ampm);
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
        /*findViewById(R.id.birthtime).setOnClickListener(view1 -> {
            //Time Picker
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR);
            int min = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hour1, min1) -> {
                String time = hour1 + " : " + min1;
                ((TextView) findViewById(R.id.birthtime)).setText(time);

            }, hour, min, true);
            timePickerDialog.show();
        });*/


        String[] bloodGroupArray = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ((AutoCompleteTextView) findViewById(R.id.bloodgroup)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, bloodGroupArray));

        String[] heightArray = {"4 feet | 121 cm", "4 feet 1 inch | 124 cm", "4 feet 2 inches | 127 cm", "4 feet 3 inches | 130 cm", "4 feet 4 inches | 132 cm", "4 feet 5 inches | 135 cm", "4 feet 6 inches | 138 cm", "4 feet 7 inches | 140 cm", "4 feet 8 inches | 143 cm", "4 feet 9 inches | 145 cm", "4 feet 10 inches | 148 cm", "4 feet 11 inches | 151 cm", "5 feet | 152 cm", "5 feet 1 inch | 155 cm", "5 feet 2 inches | 157 cm", "5 feet 3 inches | 160 cm", "5 feet 4 inches | 163 cm", "5 feet 5 inches | 165 cm", "5 feet 6 inches | 168 cm", "5 feet 7 inches | 170 cm", "5 feet 8 inches | 173 cm", "5 feet 9 inches | 175 cm", "5 feet 10 inches | 178 cm", "5 feet 11 inches | 180 cm", "6 feet | 183 cm", "6 feet 1 inch | 185 cm", "6 feet 2 inches | 188 cm", "6 feet 3 inches | 191 cm", "6 feet 4 inches | 193 cm", "6 feet 5 inches | 196 cm", "6 feet 6 inches | 198 cm", "6 feet 7 inches | 201 cm", "6 feet 8 inches | 203 cm", "6 feet 9 inches | 206 cm", "6 feet 10 inches | 208 cm", "6 feet 11 inches | 211 cm", "7 feet | 213 cm", "7 feet 1 inch | 216 cm", "7 feet 2 inches | 218 cm", "7 feet 3 inches | 221 cm", "7 feet 4 inches | 224 cm", "7 feet 5 inches | 226 cm", "7 feet 6 inches | 229 cm", "7 feet 7 inches | 231 cm", "7 feet 8 inches | 234 cm", "7 feet 9 inches | 237 cm", "7 feet 10 inches | 239 cm", "7 feet 11 inches | 242 cm"};
        ((AutoCompleteTextView) findViewById(R.id.height)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, heightArray));

        /*String[] educationArray = {"B.E.", "B.Sc", "B.Ed", "10th Pass"};
        ((AutoCompleteTextView) findViewById(R.id.education)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, educationArray));*/


        String[] hourArray = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        ((AutoCompleteTextView) findViewById(R.id.hour)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, hourArray));

        String[] minuteArray = {"00","01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60"};
        ((AutoCompleteTextView) findViewById(R.id.minute)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, minuteArray));

        String[] ampmArray = {"am", "pm"};
        ((AutoCompleteTextView) findViewById(R.id.ampm)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, ampmArray));

        String[] genderArray = {"female", "male"};
        ((AutoCompleteTextView) findViewById(R.id.gender)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, genderArray));

        String[] marriagestatusArray = {"single", "married", "divorsed", "widowed"};
        ((AutoCompleteTextView) findViewById(R.id.marriagestatus)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, marriagestatusArray));

        String[] incomeArray = {"1-3 lakh", "3-5 lakh", "5-8 lakh", "8-12 lakh", "12+ lakh"};
        ((AutoCompleteTextView) findViewById(R.id.income)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, incomeArray));

        String[] birthplaceArray = {"Kolhapur", "Pune", "Mumbai", "satara", "sangli","solapur","belgav","thane"};
        ((AutoCompleteTextView) findViewById(R.id.birthplace)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, birthplaceArray));
        ((AutoCompleteTextView) findViewById(R.id.city)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, birthplaceArray));

        String[] zodiacArray = {"मेष | Aries", "वृषभ | Taurus", "मिथुन | Gemini", "कर्क | Cancer", "सिंह | Leo", "कन्या | Virgo", "तुला | Libra",
                "वृश्चिक | Scorpio", "धनु | Sagittarius", "मकर | Capricorn", "कुंभ | Aquarius", "मीन | Pisces"};
        ((AutoCompleteTextView) findViewById(R.id.zodiac)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, zodiacArray));


        String[] religionArray = {"Hindu", "Brahmin", "Muslim", "Cristian", "sikh", "jain"};
        ((AutoCompleteTextView) findViewById(R.id.religion)).setAdapter(new ArrayAdapter(this, R.layout.package_list_item, religionArray));
        ((TextInputEditText)findViewById(R.id.caste)).setText("मराठा  96 कोळी");


    }

    private void createProfile() {
        String creationsource = "mobile app";
        String profilephotoaddress = "";
        String biodataaddress = "";

        String name = ((TextInputEditText) findViewById(R.id.name)).getText().toString().trim();
        String firstname = "", middlename = "", lastname = "";
        String[] nameArr = name.split(" ");
        if (nameArr.length == 1) {
            firstname = nameArr[0];
        } else if (nameArr.length == 2) {
            firstname = nameArr[0];
            lastname = nameArr[1];
        } else if (nameArr.length == 3) {
            firstname = nameArr[0];
            middlename = nameArr[1];
            lastname = nameArr[2];
        } else
            firstname = name;

        String relation1 = "Brother";
        String relation2 = "Sister";

        String birthtime = hour.getText().toString().trim() + ":" + minute.getText().toString().trim() + " " + ampm.getText().toString().trim();
        if(birthtime.equalsIgnoreCase(":"))
            birthtime = "";


        Customer c = new Customer(creationsource, profilePhotoAddressBase64, biodataAddressBase64,
                firstname, middlename, lastname, email.getText().toString().trim(), mobile1.getText().toString().trim(), mobile2.getText().toString().trim(), mobile3.getText().toString().trim(), mobile4.getText().toString().trim(), gender.getText().toString().trim(), height.getText().toString().trim(),
                birthtime, caste.getText().toString().trim(), religion.getText().toString().trim(), education.getText().toString().trim(), occupation.getText().toString().trim(), zodiac.getText().toString().trim(), birthname.getText().toString().trim(), bloodgroup.getText().toString().trim(),
                property.getText().toString().trim(), fathername.getText().toString().trim(), mothername.getText().toString().trim(), address.getText().toString().trim(), city.getText().toString().trim(), marriagestatus.getText().toString().trim(), birthdate.getText().toString().trim(),
                birthplace.getText().toString().trim(), income.getText().toString().trim(), kuldaivat.getText().toString().trim(), devak.getText().toString().trim(), nakshatra.getText().toString().trim(), nadi.getText().toString().trim(), gan.getText().toString().trim(), yoni.getText().toString().trim(),
                charan.getText().toString().trim(), gotra.getText().toString().trim(), varn.getText().toString().trim(), mangal.getText().toString().trim(), expectations.getText().toString().trim(), relation1, relation2, relationname1.getText().toString().trim(), relationname2.getText().toString().trim(), relatives.getText().toString().trim(), family.getText().toString().trim());

        if (editprofile) {
            c.setProfileId(customer.getProfileId());
            ApiCallUtil.updateProfile(c, this, updateCache);
        } else
            ApiCallUtil.registerProfile(c, this, false, null);
        nullifyformdata();
        cmmobile.setText("");
        formLayout.setVisibility(GONE);
        cmlayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {

                try {
                    Uri uri = data.getData();
                    cropImage(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri uri = UCrop.getOutput(data);

            if (uri != null) {
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    String imgB64 = HelperUtils.convertBitmapToBase64(bitmap, 500);


                    if (clickedImagename != null) {
                        if (clickedImagename.equalsIgnoreCase("profilePhotoAddress")) {
                            profilePhotoAddressBase64 = imgB64;
                            Glide.with(this).load(uri.toString()).placeholder(R.drawable.progym_icon).into(profilePhotoAddress);
                        }
                        if (clickedImagename.equalsIgnoreCase("biodataAddress")) {
                            biodataAddressBase64 = imgB64;
                            Glide.with(this).load(uri.toString()).placeholder(R.drawable.progym_icon).into(biodataAddress);
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Handle the case where the cropped image URI is null
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            // Handle the cropping error
            Throwable cropError = UCrop.getError(data);
        }
    }

    public void nullifyformdata() {
        name.setText("");
        email.setText("");
        mobile1.setText("");
        mobile2.setText("");
        mobile3.setText("");
        mobile4.setText("");
        gender.setText("");
        height.setText("");
        hour.setText("");
        minute.setText("");
        ampm.setText("");
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
        //family.setText("");
        relatives.setText("");
        profilePhotoAddressBase64 = "";
        biodataAddressBase64 = "";

    }

    private void cropImage(Uri sourceUri) {

        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), queryName(getContentResolver(), sourceUri)));
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(IMAGE_COMPRESSION);

        // applying UI theme
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));

        if (lockAspectRatio)
            options.withAspectRatio(ASPECT_RATIO_X, ASPECT_RATIO_Y);

        if (setBitmapMaxWidthHeight)
            options.withMaxResultSize(bitmapMaxWidth, bitmapMaxHeight);

        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(this);
    }

    private static String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    private void validateForm(){

        if(!editprofile){
            if( !((TextInputEditText)findViewById(R.id.name)).getText().toString().trim().isEmpty()
                    && !((AutoCompleteTextView)findViewById(R.id.height)).getText().toString().trim().isEmpty()
                    && !((AutoCompleteTextView)findViewById(R.id.gender)).getText().toString().trim().isEmpty()
                    && !((TextInputEditText)findViewById(R.id.birthdate)).getText().toString().trim().isEmpty())
                findViewById(R.id.save_btn).setEnabled(true);
            else
                findViewById(R.id.save_btn).setEnabled(false);
        }
        else
            findViewById(R.id.save_btn).setEnabled(true);


    }

}
