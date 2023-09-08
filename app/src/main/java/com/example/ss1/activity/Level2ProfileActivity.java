package com.example.ss1.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.ss1.LocalCache;
import com.example.ss1.R;
import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.api.DateApi;
import com.example.ss1.modal.Customer;
import com.example.ss1.modal.Level_2_Modal;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Level2ProfileActivity extends AppCompatActivity {

    public static CoordinatorLayout coordinatorLayout;
    CardView profileIdCard;
    Level_2_Modal vcpid;
    SliderView sliderView;
    static Context ctx;
    Boolean enableDisableContactViewButton = false;
    Button viewContactDetailsBtn, l2_whatsappsendmsgbtn, l2_callbtn;
    TextView l2_name, l2_mobile, l2_email, l2_age, profileCardId;
    LinearLayout contactSecurityLayout, celebrationLayout, basicdetails_layout, contactdetails_layout, astrodetails_layout, familydetails_layout, partnerdetails_layout, basicdetails_layout_title, contactdetails_layout_title, astrodetails_layout_title, familydetails_layout_title, partnerdetails_layout_title;
    ImageView basicdetails_img, contactdetails_img, astrodetails_img, familydetails_img, partnerdetails_img;

    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2_profile);


        customer = LocalCache.retrieveLoggedInCustomer(this);

        /*set context*/
        if (ctx == null || ((Level2ProfileActivity) ctx).isDestroyed())
            ctx = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            vcpid = new Gson().fromJson(extras.getString("level2data"), new TypeToken<Level_2_Modal>() {
            }.getType());
            enableDisableContactViewButton = extras.getBoolean("enableDisableContactViewButton");
        }

        initUiElements();
        handleOnClickListeners();

        try {


            if (vcpid != null) {
                handleCarosol(vcpid);
                l2_name.setText(vcpid.getFirstname() + " " + vcpid.getLastname());
                l2_age.setText(Math.abs(DateApi.daysDiff(new Date(), new SimpleDateFormat("dd/MM/yyyy").parse(vcpid.getBirthdate()))) / 365 + " yrs , " + vcpid.getCity());
                profileCardId.setText("Profile id : A" + vcpid.getProfileId());
            }
        } catch (Exception e) {
            System.out.println(e);
        }


    }

    private void handleOnClickListeners() {

        basicdetails_layout_title.setOnClickListener(view -> {
            showHideLayout(basicdetails_img, "1");
        });
        contactdetails_layout_title.setOnClickListener(view -> {
            showHideLayout(contactdetails_img, "2");
        });
        astrodetails_layout_title.setOnClickListener(view -> {
            showHideLayout(astrodetails_img, "3");
        });
        familydetails_layout_title.setOnClickListener(view -> {
            showHideLayout(familydetails_img, "4");
        });
        partnerdetails_layout_title.setOnClickListener(view -> {
            showHideLayout(partnerdetails_img, "5");
        });
    }

    private void showHideLayout(ImageView imgView, String layoutSeq) {
        String tag = String.valueOf(imgView.getTag());
        if (tag.equalsIgnoreCase("up")) {
            imgView.setTag("down");
            imgView.setImageResource(R.drawable.down_icon);

            if (layoutSeq.equalsIgnoreCase("1"))
                basicdetails_layout.setVisibility(View.GONE);
            if (layoutSeq.equalsIgnoreCase("2"))
                contactdetails_layout.setVisibility(View.GONE);
            if (layoutSeq.equalsIgnoreCase("3"))
                astrodetails_layout.setVisibility(View.GONE);
            if (layoutSeq.equalsIgnoreCase("4"))
                familydetails_layout.setVisibility(View.GONE);
            if (layoutSeq.equalsIgnoreCase("5"))
                partnerdetails_layout.setVisibility(View.GONE);
        } else {
            imgView.setTag("up");
            imgView.setImageResource(R.drawable.up_icon);

            if (layoutSeq.equalsIgnoreCase("1"))
                basicdetails_layout.setVisibility(View.VISIBLE);
            if (layoutSeq.equalsIgnoreCase("2"))
                contactdetails_layout.setVisibility(View.VISIBLE);
            if (layoutSeq.equalsIgnoreCase("3"))
                astrodetails_layout.setVisibility(View.VISIBLE);
            if (layoutSeq.equalsIgnoreCase("4"))
                familydetails_layout.setVisibility(View.VISIBLE);
            if (layoutSeq.equalsIgnoreCase("5"))
                partnerdetails_layout.setVisibility(View.VISIBLE);
        }


    }

    private void initUiElements() {

        basicdetails_layout = findViewById(R.id.basicdetails_layout);
        contactdetails_layout = findViewById(R.id.contactdetails_layout);
        astrodetails_layout = findViewById(R.id.astrodetails_layout);
        familydetails_layout = findViewById(R.id.familydetails_layout);
        partnerdetails_layout = findViewById(R.id.partnerdetails_layout);
        basicdetails_layout_title = findViewById(R.id.basicdetails_layout_title);
        contactdetails_layout_title = findViewById(R.id.contactdetails_layout_title);
        astrodetails_layout_title = findViewById(R.id.astrodetails_layout_title);
        familydetails_layout_title = findViewById(R.id.familydetails_layout_title);
        partnerdetails_layout_title = findViewById(R.id.partnerdetails_layout_title);

        basicdetails_img = findViewById(R.id.basicdetails_img);
        contactdetails_img = findViewById(R.id.contactdetails_img);
        astrodetails_img = findViewById(R.id.astrodetails_img);
        familydetails_img = findViewById(R.id.familydetails_img);
        partnerdetails_img = findViewById(R.id.partnerdetails_img);

        basicdetails_img.setTag("up");
        contactdetails_img.setTag("up");
        astrodetails_img.setTag("up");
        familydetails_img.setTag("up");
        partnerdetails_img.setTag("up");


        profileIdCard = findViewById(R.id.profileIdCard);
        profileCardId = findViewById(R.id.profileCardId);
        contactSecurityLayout = findViewById(R.id.contactSecurityLayout);
        celebrationLayout = findViewById(R.id.celebrationLayout);
        coordinatorLayout = findViewById(R.id.level2CoordinatorLayout);
        viewContactDetailsBtn = findViewById(R.id.viewContactDetails);
        l2_whatsappsendmsgbtn = findViewById(R.id.l2_whatsappsendmsgbtn);
        l2_callbtn = findViewById(R.id.l2_callbtn);
        l2_mobile = findViewById(R.id.l2_mobile);
        l2_email = findViewById(R.id.l2_email);
        l2_name = findViewById(R.id.l2_name);
        l2_age = findViewById(R.id.l2_age);
        sliderView = findViewById(R.id.image_slider);

        viewContactDetailsBtn.setOnClickListener(view -> {
            // check if package exist or not
            if (customer.getActivepackageId() == null)
                //showSnackBar("No active membership found :"+customer.getActivePackageId());
                showDialog("No Active membership", "Please purchase memership to view contact details", null);
            else {

                ApiCallUtil.viewContactData(customer.getProfileId(), vcpid.getProfileId(), this);

                l2_mobile.setText("+91 " + vcpid.getMobile1().toString().trim());
                l2_email.setText(vcpid.getEmail().trim());
                viewContactDetailsBtn.setVisibility(View.GONE);

                contactSecurityLayout.setVisibility(View.GONE);
                celebrationLayout.setVisibility(View.VISIBLE);

                l2_callbtn.setEnabled(true);
                l2_whatsappsendmsgbtn.setEnabled(true);

                //showSnackBar("Credit left : 25");
            }

        });
        if (enableDisableContactViewButton) {
            viewContactDetailsBtn.setVisibility(View.GONE);
            if (vcpid != null) {
                l2_mobile.setText("+91 " + vcpid.getMobile1().toString().trim());
                l2_email.setText(vcpid.getEmail().toString().trim());
            }
        } else
            viewContactDetailsBtn.setVisibility(View.VISIBLE);

        l2_whatsappsendmsgbtn.setOnClickListener(view -> {
            String uri = "https://wa.me/+91" + vcpid.getMobile1().toString().trim();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        });

        l2_callbtn.setOnClickListener(view -> Dexter.withActivity(Level2ProfileActivity.this)
                .withPermissions(Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:+91" + vcpid.getMobile1().toString().trim()));
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

                /*try {
                    layoutToImage(view);
                    imageToPDF();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }*/
            }
        });
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

    public void showDialog(String title, String subTitle, Integer img) {
        Dialog d = new Dialog(this);
        d.setContentView(R.layout.nomembership_dialog);
        //Glide.with(this).asGif().load(img).into((ImageView) d.findViewById(R.id.result_img));
        ((TextView) d.findViewById(R.id.result_title)).setText(title);
        if (subTitle.isEmpty()) {
            ((TextView) d.findViewById(R.id.result_subtitle)).setVisibility(View.GONE);
        } else
            ((TextView) d.findViewById(R.id.result_subtitle)).setText(subTitle);
        d.setCanceledOnTouchOutside(true);
        d.setCancelable(true);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.show();
    }


}