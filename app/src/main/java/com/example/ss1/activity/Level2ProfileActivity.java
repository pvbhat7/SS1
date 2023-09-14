package com.example.ss1.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.example.ss1.LocalCache;
import com.example.ss1.ProCoinBottomSheetDialog;
import com.example.ss1.R;
import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.api.ApiUtils;
import com.example.ss1.api.DateApi;
import com.example.ss1.modal.Customer;
import com.example.ss1.modal.Level_2_Modal;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Level2ProfileActivity extends AppCompatActivity {

    public static CoordinatorLayout coordinatorLayout;
    Level_2_Modal profile;
    SliderView sliderView;
    static Context ctx;
    Boolean enableDisableContactViewButton = false;
    Customer customer;

    TextView profileid,name,birthdate,birthtime,height,education,occupation,religion,caste,income,bloodgroup,marriagestatus,birthname,birthplace,fathername,mothername,relatives,family,city,address;
    Button viewContactDetailsBtn;
    ImageView profilephotoaddress;

    CardView editprofile_link;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2_profile_new);

        initUiElements();
        handleOnClickListeners();

        // TODO: 12-Sep-23 <a href="https://www.freepik.com/free-photo/abstract-yellow-sunshine-theme-summer-watercolor-background-illustration-high-resolution-free-photo_26887846.htm#query=texture%20background&position=32&from_view=keyword&track=ais">Image by Sketchepedia</a> on Freepik
        customer = LocalCache.retrieveLoggedInCustomer(this);

        //*set context*//*
        if (ctx == null || ((Level2ProfileActivity) ctx).isDestroyed())
            ctx = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            profile = new Gson().fromJson(extras.getString("level2data"), new TypeToken<Level_2_Modal>() {}.getType());
            enableDisableContactViewButton = extras.getBoolean("enableDisableContactViewButton");
        }



        if (profile != null) {
            //handleCarosol(profile);
            Glide.with(this)
                    .load(profile.getProfilephotoaddress())
                    .placeholder(R.drawable.oops)
                    .into(profilephotoaddress);
            name.setText(profile.getFirstname() + " " + profile.getLastname());
            String age = getAgeByDOB(profile.getBirthdate());
            profileid.setText("Profile id : A" + profile.getProfileId());
            birthdate.setText(profile.getBirthdate()+getAgeByDOB(profile.getBirthdate()));
            birthtime.setText(profile.getBirthtime());
            height.setText(profile.getHeight());
            education.setText(profile.getEducation());
            occupation.setText(profile.getOccupation());
            religion.setText(profile.getReligion());
            caste.setText(profile.getCaste());
            income.setText(profile.getIncome());

            bloodgroup.setText(profile.getBloodgroup());
            marriagestatus.setText(profile.getMarriagestatus());
            birthname.setText(profile.getBirthname());
            birthplace.setText(profile.getBirthplace());
            fathername.setText(profile.getFathername());
            mothername.setText(profile.getMothername());
            relatives.setText(profile.getRelatives());
            family.setText(profile.getFamily());
            city.setText(profile.getCity());
            address.setText(profile.getAddress());


        }

    }

    private String getAgeByDOB(String birthdate) {
        String age = "";
        try {
            age = "    ( "+Math.abs(DateApi.daysDiff(new Date(), new SimpleDateFormat("dd/MM/yyyy").parse(birthdate))) / 365 + " yrs )";
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return age;
    }

    private void handleOnClickListeners() {

        viewContactDetailsBtn.setOnClickListener(view -> {
            // check if package exist or not
            if (customer.getActivepackageId() == null) {
                new ProCoinBottomSheetDialog(this).show(getSupportFragmentManager(), "ModalBottomSheet");
            } else {

                ApiCallUtil.viewContactData(customer.getProfileId(), profile.getProfileId(), this);

                //showSnackBar("Credit left : 25");
            }

        });

        profilephotoaddress.setOnClickListener(view -> ApiUtils.showImageDialog(Level2ProfileActivity.this,profile.getProfilephotoaddress()));

        /*whatsapp.setOnClickListener(view -> {
            String uri = "https://wa.me/+91" + profile.getMobile1().toString().trim();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        });

        call.setOnClickListener(view -> Dexter.withActivity(Level2ProfileActivity.this)
                .withPermissions(Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:+91" + profile.getMobile1().toString().trim()));
                            startActivity(callIntent);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check());

        profileIdCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                *//*try {
                    layoutToImage(view);
                    imageToPDF();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }*//*
            }
        });*/

        editprofile_link.setOnClickListener(view -> {
            ApiUtils.vibrateFunction(Level2ProfileActivity.this);
            Intent intent = new Intent(Level2ProfileActivity.this, RegistrationActivity.class);
            intent.putExtra("editprofile",true);
            intent.putExtra("profile",new Gson().toJson(profile));

            startActivity(intent);
        });
    }


    private void initUiElements() {
        editprofile_link = findViewById(R.id.editprofile_link);
        profilephotoaddress = findViewById(R.id.profilephotoaddress);
        profileid= findViewById(R.id.profileid);
        name= findViewById(R.id.name);
        coordinatorLayout = findViewById(R.id.level2CoordinatorLayout);
        //sliderView = findViewById(R.id.image_slider);
        viewContactDetailsBtn = findViewById(R.id.viewContactDetailsBtn);
        birthdate = findViewById(R.id.birthdate);
        birthtime = findViewById(R.id.birthtime);
        height = findViewById(R.id.height);
        education = findViewById(R.id.education);
        occupation = findViewById(R.id.occupation);
        religion = findViewById(R.id.religion);
        caste = findViewById(R.id.caste);
        income = findViewById(R.id.income);
        bloodgroup = findViewById(R.id.bloodgroup);
        marriagestatus = findViewById(R.id.marriagestatus);
        birthname = findViewById(R.id.birthname);
        birthplace = findViewById(R.id.birthplace);
        fathername = findViewById(R.id.fathername);
        mothername = findViewById(R.id.mothername);
        relatives = findViewById(R.id.relatives);
        family = findViewById(R.id.family);
        city = findViewById(R.id.city);
        address = findViewById(R.id.address);
    }

    public static Context getContextObject() {
        return ctx;
    }

    private void handleCarosol(Level_2_Modal level2Modal) {
        String[] arr;
        if (level2Modal.getPhotos() != null) {
            arr = new String[level2Modal.getPhotos().size()];
            for (int i = 0; i < level2Modal.getPhotos().size(); i++) {
                arr[i] = level2Modal.getPhotos().get(i).getImageAddress();
            }
        } else {
            arr = new String[1];
            arr[0] = level2Modal.getProfilephotoaddress();
        }

        SliderAdapter sliderAdapter = new SliderAdapter(arr);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showSnackBar(String content) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, content, Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
        snackbar.show();
    }

    String dirpath;

    public void layoutToImage(View view) {
        // get view group using reference
        //relativeLayout = (RelativeLayout) view.findViewById(R.id.print);
        // convert view group to bitmap
        coordinatorLayout.setDrawingCacheEnabled(true);
        coordinatorLayout.buildDrawingCache();
        Bitmap bm = coordinatorLayout.getDrawingCache();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void imageToPDF() throws FileNotFoundException {
        try {
            Document document = new Document();
            dirpath = android.os.Environment.getExternalStorageDirectory().toString();
            PdfWriter.getInstance(document, new FileOutputStream(dirpath + "/NewPDF.pdf")); //  Change pdf's name.
            document.open();
            Image img = Image.getInstance(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / img.getWidth()) * 100;
            img.scalePercent(scaler);
            img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
            document.add(img);
            document.close();
            Toast.makeText(this, "PDF Generated successfully!..", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
    }


}