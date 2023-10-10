package com.example.ss1.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.example.ss1.LocalCache;
import com.example.ss1.BuyMembershipBottomSheetDialog;
import com.example.ss1.R;
import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.api.HelperUtils;
import com.example.ss1.api.DateApi;
import com.example.ss1.modal.Customer;
import com.example.ss1.modal.Level_2_Modal;
import com.example.ss1.modal.OrderModal;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
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
import java.util.List;

public class Level2ProfileActivity extends AppCompatActivity {

    LinearLayout call1_link , call2_link , whatsapp_link , insta_link , email_link;

    public static CoordinatorLayout coordinatorLayout;
    Level_2_Modal viewedProfile;
    SliderView sliderView;
    static Context ctx;
    Boolean enableDisableContactViewButton = false;
    Customer loggedinCustomer;

    TextView profileid, name, birthdate, birthtime, height, education, occupation, religion, caste, income, bloodgroup, marriagestatus, birthname, birthplace, fathername, mothername, relatives, family, city, address, expectations, kuldaivat, zodiac, varn, nakshatra, nadi, gan, yoni, charan, gotra, mangal, email, mobile1, mobile2, mobile3;

    Button viewContactDetailsBtn;
    ImageView profilephotoaddresss,shareprofileicon,editprofile_link;

    CardView contact_card;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2_profile_new);

        initUiElements();
        handleOnClickListeners();

        // TODO: 12-Sep-23 <a href="https://www.freepik.com/free-photo/abstract-yellow-sunshine-theme-summer-watercolor-background-illustration-high-resolution-free-photo_26887846.htm#query=texture%20background&position=32&from_view=keyword&track=ais">Image by Sketchepedia</a> on Freepik
        loggedinCustomer = LocalCache.getLoggedInCustomer(this);

        //*set context*//*
        if (ctx == null || ((Level2ProfileActivity) ctx).isDestroyed())
            ctx = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            viewedProfile = new Gson().fromJson(extras.getString("level2data"), new TypeToken<Level_2_Modal>() {
            }.getType());
            enableDisableContactViewButton = extras.getBoolean("enableDisableContactViewButton");
        }


        if (viewedProfile != null) {
            if (viewedProfile.getContactViewed()) {
                findViewById(R.id.contactDetailsLayout).setVisibility(View.GONE);
                viewContactDetailsBtn.setVisibility(View.GONE);
                contact_card.setVisibility(View.VISIBLE);

            } else {

                viewContactDetailsBtn.setVisibility(View.VISIBLE);
                contact_card.setVisibility(View.GONE);
            }
            //handleCarosol(profile.getProfilephotoaddress() , profile.getBiodataaddress());
            Glide.with(this)
                    .load(viewedProfile.getProfilephotoaddress())
                    .placeholder(R.drawable.oops)
                    .into((ImageView) findViewById(R.id.profilephotoaddresss));
            name.setText(viewedProfile.getFirstname() + " " + viewedProfile.getLastname());
            String age = getAgeByDOB(viewedProfile.getBirthdate());
            profileid.setText("Profile id : A" + viewedProfile.getProfileId());
            birthdate.setText(viewedProfile.getBirthdate() + getAgeByDOB(viewedProfile.getBirthdate()));
            birthtime.setText(viewedProfile.getBirthtime());
            height.setText(viewedProfile.getHeight());
            education.setText(viewedProfile.getEducation());
            occupation.setText(viewedProfile.getOccupation());
            religion.setText(viewedProfile.getReligion());
            caste.setText(viewedProfile.getCaste());
            income.setText(viewedProfile.getIncome());
            expectations.setText(viewedProfile.getExpectations());
            bloodgroup.setText(viewedProfile.getBloodgroup());
            marriagestatus.setText(viewedProfile.getMarriagestatus());
            birthname.setText(viewedProfile.getBirthname());
            birthplace.setText(viewedProfile.getBirthplace());
            fathername.setText(viewedProfile.getFathername());
            mothername.setText(viewedProfile.getMothername());
            relatives.setText(viewedProfile.getRelatives());
            family.setText(viewedProfile.getFamily());
            city.setText(viewedProfile.getCity());
            address.setText(viewedProfile.getContactViewed() ? viewedProfile.getAddress() : "********");
            kuldaivat.setText(viewedProfile.getKuldaivat());
            zodiac.setText(viewedProfile.getZodiac());
            varn.setText(viewedProfile.getVarn());
            nakshatra.setText(viewedProfile.getNakshatra());
            nadi.setText(viewedProfile.getNadi());
            gan.setText(viewedProfile.getGan());
            yoni.setText(viewedProfile.getYoni());
            charan.setText(viewedProfile.getCharan());
            gotra.setText(viewedProfile.getGotra());
            mangal.setText(viewedProfile.getMangal());

            email.setText(viewedProfile.getContactViewed() ? viewedProfile.getEmail() : "********");
            mobile1.setText(viewedProfile.getContactViewed() ? viewedProfile.getMobile1() : "********");
            mobile2.setText(viewedProfile.getContactViewed() ? viewedProfile.getMobile2() : "********");
            mobile3.setText(viewedProfile.getContactViewed() ? viewedProfile.getMobile3() : "********");


        }

        //ApiCallUtil.dynamicLayoutCreation(this);
        //runinthread();

    }

    private String getAgeByDOB(String birthdate) {
        String age = "";
        try {
            age = "    ( " + Math.abs(DateApi.daysDiff(new Date(), new SimpleDateFormat("dd/MM/yyyy").parse(birthdate))) / 365 + " yrs )";
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return age;
    }

    private void handleOnClickListeners() {



        viewContactDetailsBtn.setOnClickListener(view -> {

            OrderModal order = LocalCache.getActiveOrder(this);

            // check if package exist or not
            if (loggedinCustomer.getActivepackageid() == null ) {
                Log.i("ss_nw_call", "View contact : pkg id is null");
                new BuyMembershipBottomSheetDialog(this).show(getSupportFragmentManager(), "ModalBottomSheet");
            } else {
                Log.i("ss_nw_call", "View contact : pkg id is "+ loggedinCustomer.getActivepackageid()+ " and balance is "+order.getCountRemaining());

                ApiCallUtil.viewContactData(loggedinCustomer.getProfileId(), viewedProfile, this);

                //showSnackBar("Credit left : 25");
            }

        });

        profilephotoaddresss.setOnClickListener(view -> HelperUtils.showImageDialog(Level2ProfileActivity.this, viewedProfile.getProfilephotoaddress()));

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
            HelperUtils.vibrateFunction(Level2ProfileActivity.this);
            Intent intent = new Intent(Level2ProfileActivity.this, RegistrationActivity.class);
            intent.putExtra("editprofile", true);
            intent.putExtra("profile", new Gson().toJson(viewedProfile));

            startActivity(intent);
        });

        shareprofileicon.setOnClickListener(view -> ApiCallUtil.shareProfile(this, viewedProfile));

        call1_link.setOnClickListener(view -> Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:+91" + viewedProfile.getMobile1().toString().trim()));
                            startActivity(callIntent);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check());
        call2_link.setOnClickListener(view -> Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:+91" + viewedProfile.getMobile2().toString().trim()));
                            startActivity(callIntent);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check());
        whatsapp_link.setOnClickListener(view -> {
            String uri = "https://wa.me/+91" + viewedProfile.getMobile1().toString().trim();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        });
        insta_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        email_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    private void initUiElements() {
        shareprofileicon = findViewById(R.id.shareprofileicon);
        contact_card = findViewById(R.id.contact_card);
        editprofile_link = findViewById(R.id.editprofile_link);
        profilephotoaddresss = findViewById(R.id.profilephotoaddresss);
        profileid = findViewById(R.id.profileid);
        name = findViewById(R.id.name);
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

        expectations = findViewById(R.id.expectations);
        kuldaivat = findViewById(R.id.kuldaivat);
        zodiac = findViewById(R.id.zodiac);
        nakshatra = findViewById(R.id.nakshatra);
        varn = findViewById(R.id.varn);
        nadi = findViewById(R.id.nadi);
        gan = findViewById(R.id.gan);
        yoni = findViewById(R.id.yoni);
        charan = findViewById(R.id.charan);
        gotra = findViewById(R.id.gotra);
        mangal = findViewById(R.id.mangal);

        email = findViewById(R.id.email);
        mobile1 = findViewById(R.id.mobile1);
        mobile2 = findViewById(R.id.mobile2);
        mobile3 = findViewById(R.id.mobile3);


        call1_link  = findViewById(R.id.call1_link);
        call2_link   = findViewById(R.id.call2_link);
        whatsapp_link   = findViewById(R.id.whatsapp_link);
        insta_link   = findViewById(R.id.insta_link);
        email_link  = findViewById(R.id.email_link);

    }

    public static Context getContextObject() {
        return ctx;
    }

    private void handleCarosol(String img1, String img2) {
        try {
            String[] arr = new String[2];
            arr[0] = img1;
            arr[1] = img2;

            SliderAdapter sliderAdapter = new SliderAdapter(arr);

            sliderView.setSliderAdapter(sliderAdapter);
            sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
            sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
            sliderView.startAutoCycle();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showSnackBar(String content) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, content, Snackbar.LENGTH_LONG)
                .setAction("OK", view -> {
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
