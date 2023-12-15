package com.sdgvvk.v1.activity;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sdgvvk.v1.BuyMembershipBottomSheetDialog;
import com.sdgvvk.v1.LocalCache;
import com.sdgvvk.v1.MainActivity;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.api.DateApi;
import com.sdgvvk.v1.api.HelperUtils;
import com.sdgvvk.v1.modal.Customer;
import com.sdgvvk.v1.modal.Level_2_Modal;
import com.sdgvvk.v1.modal.OrderModal;
import com.sdgvvk.v1.ui.ViewedContactBottomSheetDialog;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Level2ProfileActivity extends AppCompatActivity {

    static BuyMembershipBottomSheetDialog buyMembershipBottomSheetDialog = null;
    static Activity activity;
    static Boolean isAdmin = false;
    LinearLayout call1_link, call2_link, whatsapp_link, insta_link, email_link;

    public static CoordinatorLayout coordinatorLayout;
    static Level_2_Modal viewedProfile;
    SliderView sliderView;
    static Context ctx;
    Boolean enableDisableContactViewButton = false;
    static Customer loggedinCustomer;

    static TextView profileid, name, birthdate, birthtime, height, education, occupation, religion, caste, income, bloodgroup, marriagestatus, birthname, birthplace, fathername, mothername, relatives, family, city, address, expectations, kuldaivat, zodiac, varn, nakshatra, nadi, gan, yoni, charan, gotra, mangal, email, mobile1, mobile2, mobile3;

    static ExtendedFloatingActionButton viewContactDetailsBtn;
    static ImageView profilephotoaddresss,biodataImg;

    static CardView contact_card;
    static CardView marriage_card;
    CardView bg_card;

    static FloatingActionButton shareprofile;
    static FloatingActionButton editprofile_link;
    static Boolean ownAccount = false;

    private static ShimmerFrameLayout[] shimmerFrameLayouts = new ShimmerFrameLayout[33];
    private static ShimmerFrameLayout t1, t2, t3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2_profile_newv2_shimmer);

        Log.i("ss_nw_call", "performance : level 1 activity oncreate ");
        Log.i("ss_nw_call", new Date() + "lifecycle : Level2ProfileActivity onCreate");

        loggedinCustomer = LocalCache.getLoggedInCustomer(this);
        init();
        handleBundleExtraction();
        forceBuyMembership();

        //  12-Sep-23 <a href="https://www.freepik.com/free-photo/abstract-yellow-sunshine-theme-summer-watercolor-background-illustration-high-resolution-free-photo_26887846.htm#query=texture%20background&position=32&from_view=keyword&track=ais">Image by Sketchepedia</a> on Freepik

    }

    private void handleBundleExtraction() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String profileId = extras.getString("level2data");
            if (profileId != null)
                ApiCallUtil.getLevel2Data(profileId, this);
            enableDisableContactViewButton = extras.getBoolean("enableDisableContactViewButton");
        }
    }

    public static void initByLevel2Object(Level_2_Modal viewedProfile_) {
        if (viewedProfile_ != null) {
            viewedProfile = viewedProfile_;
            shareprofile.setVisibility(View.VISIBLE);


            t1.stopShimmer();
            t1.setVisibility(View.GONE);
            t2.stopShimmer();
            t2.setVisibility(View.GONE);
            t3.stopShimmer();
            t3.setVisibility(View.GONE);
            name.setVisibility(View.VISIBLE);
            profilephotoaddresss.setVisibility(View.VISIBLE);
            profileid.setVisibility(View.VISIBLE);

            fillProfileByWS(viewedProfile_);


            if (viewedProfile_.getMobile1().equalsIgnoreCase(loggedinCustomer.getMobile1()))
                ownAccount = true;

            if (ownAccount || isAdmin) {
                editprofile_link.setVisibility(View.VISIBLE);
            }


            if (viewedProfile_.getContactViewed()) {
                if (viewedProfile_.getIsDummy() != null && viewedProfile_.getIsDummy().equalsIgnoreCase("yes")) {
                    marriage_card.setVisibility(View.VISIBLE);
                } else {
                    new ViewedContactBottomSheetDialog(activity, viewedProfile).show(((Level2ProfileActivity) activity).getSupportFragmentManager(), "ViewedContactBottomSheetDialog");
                    //contact_card.setVisibility(View.VISIBLE);
                }

                if (viewedProfile_.getMobile2() != null && viewedProfile_.getMobile2().equalsIgnoreCase(""))
                    activity.findViewById(R.id.call2_link).setVisibility(View.GONE);

            } else {
                viewContactDetailsBtn.setVisibility(View.VISIBLE);
            }
            //handleCarosol(profile.getProfilephotoaddress() , profile.getBiodataaddress());

            Log.i("ss_nw_call", "performance : level 1 setters calling ");


        }
        stopShimmerAnimations();
    }

    private static void fillProfileByWS(Level_2_Modal viewedProfile) {
        name.setText(viewedProfile.getFirstname() + " " + viewedProfile.getLastname());
        profileid.setText("Profile id : A" + viewedProfile.getProfileId());
        birthdate.setText(viewedProfile.getBirthdate() + getAgeByDOB(viewedProfile.getBirthdate()));
        birthtime.setText(viewedProfile.getBirthtime());
        height.setText(viewedProfile.getHeight());
        education.setText(viewedProfile.getEducation());
        occupation.setText(viewedProfile.getOccupation());
        religion.setText(viewedProfile.getReligion());
        caste.setText(viewedProfile.getCaste());
        income.setText(viewedProfile.getIncome());
        expectations.setText((viewedProfile.getContactViewed() && viewedProfile.getIsDummy().equalsIgnoreCase("no")) || isAdmin ? viewedProfile.getExpectations() : "********");

        bloodgroup.setText(viewedProfile.getBloodgroup());
        marriagestatus.setText(viewedProfile.getMarriagestatus());
        birthname.setText(viewedProfile.getBirthname());
        birthplace.setText(viewedProfile.getBirthplace());
        fathername.setText(viewedProfile.getFathername());
        mothername.setText(viewedProfile.getMothername());
        relatives.setText(viewedProfile.getRelatives());
        family.setText(viewedProfile.getFamily());
        city.setText(viewedProfile.getCity());
        address.setText((viewedProfile.getContactViewed() && viewedProfile.getIsDummy().equalsIgnoreCase("no")) || isAdmin ? viewedProfile.getAddress() : "********");
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

        email.setText((viewedProfile.getContactViewed() && viewedProfile.getIsDummy().equalsIgnoreCase("no")) || isAdmin ? viewedProfile.getEmail() : "********");
        mobile1.setText((viewedProfile.getContactViewed() && viewedProfile.getIsDummy().equalsIgnoreCase("no")) || isAdmin ? viewedProfile.getMobile1() : "********");
        mobile2.setText((viewedProfile.getContactViewed() && viewedProfile.getIsDummy().equalsIgnoreCase("no")) || isAdmin ? viewedProfile.getMobile2() : "********");
        mobile3.setText((viewedProfile.getContactViewed() && viewedProfile.getIsDummy().equalsIgnoreCase("no")) || isAdmin ? viewedProfile.getMobile3() : "********");

        Glide.with(getContextObject())
                .load(viewedProfile.getProfilephotoaddress())
                .placeholder(R.drawable.oops)
                .into(profilephotoaddresss);

        if((viewedProfile.getContactViewed() && viewedProfile.getIsDummy().equalsIgnoreCase("no")) || isAdmin){
            biodataImg.setVisibility(View.VISIBLE);
            Glide.with(getContextObject())
                    .load(viewedProfile.getBiodataaddress())
                    .placeholder(R.drawable.oops)
                    .into(biodataImg);
        }

    }

    private void handlebackpressed() {
        // Set up the callback for handling the back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button press
                // You can put your custom logic here

                // If you want to allow the default behavior as well,
                // call the super.handleOnBackPressed() method.
                ApiCallUtil.redirected_via_deep_link = false;
                Intent intent = new Intent(Level2ProfileActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private static String getAgeByDOB(String birthdate) {
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

            initiateViewContactFlow(false);

        });

        profilephotoaddresss.setOnClickListener(view -> HelperUtils.showImageDialog(Level2ProfileActivity.this, viewedProfile.getProfilephotoaddress()));

        biodataImg.setOnClickListener(view -> HelperUtils.showImageDialog(Level2ProfileActivity.this, viewedProfile.getBiodataaddress()));

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
            Log.i("ss_nw_call", "performance : edit profile clicked ");
            HelperUtils.vibrateFunction(Level2ProfileActivity.this);
            Intent intent = new Intent(Level2ProfileActivity.this, RegistrationActivity.class);
            intent.putExtra("editprofile", true);
            intent.putExtra("profile", new Gson().toJson(viewedProfile));

            startActivity(intent);
        });


        shareprofile.setOnClickListener(view -> {
            String profileUrl = "https://tavrostechinfo.com/profile?id=" + viewedProfile.getProfileId() + "";
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, profileUrl);
            shareIntent.setType("text/plain");

            startActivity(Intent.createChooser(shareIntent, "Share URL using"));
        });

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
            String phoneNumber = "+91" + viewedProfile.getMobile1().toString().trim();
            String message = "नमस्कार " + viewedProfile.getFirstname() + ", " +
                    "\n\nमाझे नाव " + loggedinCustomer.getFirstname() + " , मी तुमची प्रोफाइल श्री दत्त गुरु वधू-वर अँप मध्ये पाहिली आहे आणि आम्हाला तुमची प्रोफाइल आवडली आहे. " +
                    "\n\nअँप डाउनलोड लिंक: \nhttps://play.google.com/store/apps/details?id=com.sdgvvk.v1\n" +
                    "\n " + viewedProfile.getFirstname() + "'s प्रोफाइल  लिंक : \nhttps://tavrostechinfo.com/profile?id=" + viewedProfile.getProfileId() + "\n" +
                    "\n " + loggedinCustomer.getFirstname() + "'s प्रोफाइल  लिंक : \nhttps://tavrostechinfo.com/profile?id=" + loggedinCustomer.getProfileId() + "\n";
            // create an Intent to send data to the whatsapp
            Intent intent = new Intent(Intent.ACTION_VIEW);    // setting action

            // setting data url, if we not catch the exception then it shows an error
            try {
                String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "" + "&text=" + URLEncoder.encode(message, "UTF-8");
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } catch (Exception e) {
                Log.d("notSupport", "thrown by encoder");
            }
        });
        /*
         *
         * Hi Prashant ,
         *
         * My name is Pranali , I viewed your profile on sri datt guru vadhu var kendra app.
         * I am interested in your profile , can we connect to discuss further.
         *
         * app link : https://play.google.com/store/apps/details?id=com.sdgvvk.v1
         * prashant's profile
         * https://tavrostechinfo.com/profile?id=4405
         *
         * pranali's profile
         * https://tavrostechinfo.com/profile?id=4405
         *
         * */
        insta_link.setOnClickListener(view -> {
            Uri uri = Uri.parse("http://instagram.com");


            Intent i = new Intent(Intent.ACTION_VIEW, uri);

            i.setPackage("com.instagram.android");

            try {
                startActivity(i);
            } catch (Exception e) {

                /*startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/xxx")));*/
            }
        });
        email_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private static void initiateViewContactFlow(Boolean noLeads) {
        OrderModal order = LocalCache.getActiveOrder(activity);

        // check if package exist or not
        if (loggedinCustomer.getActivepackageid() == null) {
            Log.i("ss_nw_call", "View contact : pkg id is null");

            if (!noLeads)
                ApiCallUtil.addLeads(loggedinCustomer.getProfileId(), viewedProfile.getProfileId(), "clicked_view_contact_button");

            showBuyMembershipBottomSheet();



        } else if (LocalCache.getActiveOrder(activity) != null && LocalCache.getActiveOrder(activity).getCountRemaining() <= 0) {
            Log.i("ss_nw_call", "Balance left : " + LocalCache.getActiveOrder(activity).getCountRemaining());

            if (!noLeads)
                ApiCallUtil.addLeads(loggedinCustomer.getProfileId(), viewedProfile.getProfileId(), "clicked_view_contact_button");

            showBuyMembershipBottomSheet();

        } else {
            Log.i("ss_nw_call", "View contact : pkg id is " + loggedinCustomer.getActivepackageid() + " and balance is " + order.getCountRemaining());

            if (!noLeads)
                ApiCallUtil.viewContactData(loggedinCustomer.getProfileId(), viewedProfile, activity);

            //showSnackBar("Credit left : 25");
        }
    }

    public static void showBuyMembershipBottomSheet() {
        FragmentManager fragmentManager = ((Level2ProfileActivity) activity).getSupportFragmentManager();

        BuyMembershipBottomSheetDialog existingFragment = (BuyMembershipBottomSheetDialog) fragmentManager.findFragmentByTag("BuyMembershipBottomSheetDialog");

        if (existingFragment != null) {
            // Remove the existing fragment
            fragmentManager.beginTransaction().remove(existingFragment).commit();
        }

        if (!fragmentManager.isStateSaved()) {
            // Add the new fragment
            BuyMembershipBottomSheetDialog buyMembershipBottomSheetDialog = new BuyMembershipBottomSheetDialog(activity);
            buyMembershipBottomSheetDialog.show(fragmentManager, "BuyMembershipBottomSheetDialog");
        }

    }



    private void initUiElements() {
        biodataImg = findViewById(R.id.biodataImg);
        bg_card = findViewById(R.id.bg_card);
        shareprofile = findViewById(R.id.shareprofile);
        contact_card = findViewById(R.id.contact_card);
        marriage_card = findViewById(R.id.marriage_card);
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


        call1_link = findViewById(R.id.call1_link);
        call2_link = findViewById(R.id.call2_link);
        whatsapp_link = findViewById(R.id.whatsapp_link);
        insta_link = findViewById(R.id.insta_link);
        email_link = findViewById(R.id.email_link);

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

    @Override
    protected void onStart() {
        Log.i("ss_nw_call", new Date() + "lifecycle : Level2ProfileActivity onStart");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.i("ss_nw_call", new Date() + "lifecycle : Level2ProfileActivity onRestart");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i("ss_nw_call", new Date() + "lifecycle : Level2ProfileActivity onResume");
        super.onResume();
        if (ApiCallUtil.editedCustomerProfile != null && loggedinCustomer != null) {
            if (ApiCallUtil.editedCustomerProfile.getMobile1().equalsIgnoreCase(loggedinCustomer.getMobile1()) || isAdmin) {
                fillProfileForEditFlow(ApiCallUtil.editedCustomerProfile);
                ApiCallUtil.editedCustomerProfile = null;
            }
        }
    }

    private void fillProfileForEditFlow(Customer customer) {

        viewedProfile.copyFromCustomer(customer);

        name.setText(customer.getFirstname() + " " + customer.getLastname());
        profileid.setText("Profile id : A" + customer.getProfileId());
        birthdate.setText(customer.getBirthdate() + getAgeByDOB(customer.getBirthdate()));
        birthtime.setText(customer.getBirthtime());
        height.setText(customer.getHeight());
        education.setText(customer.getEducation());
        occupation.setText(customer.getOccupation());
        religion.setText(customer.getReligion());
        caste.setText(customer.getCaste());
        income.setText(customer.getIncome());
        expectations.setText(customer.getExpectations());
        bloodgroup.setText(customer.getBloodgroup());
        marriagestatus.setText(customer.getMarriagestatus());
        birthname.setText(customer.getBirthname());
        birthplace.setText(customer.getBirthplace());
        fathername.setText(customer.getFathername());
        mothername.setText(customer.getMothername());
        relatives.setText(customer.getRelatives());
        family.setText(customer.getFamily());
        city.setText(customer.getCity());
        address.setText(customer.getAddress());
        kuldaivat.setText(customer.getKuldaivat());
        zodiac.setText(customer.getZodiac());
        varn.setText(customer.getVarn());
        nakshatra.setText(customer.getNakshatra());
        nadi.setText(customer.getNadi());
        gan.setText(customer.getGan());
        yoni.setText(customer.getYoni());
        charan.setText(customer.getCharan());
        gotra.setText(customer.getGotra());
        mangal.setText(customer.getMangal());

        email.setText(customer.getEmail());
        mobile1.setText(customer.getMobile1());
        mobile2.setText(customer.getMobile2());
        mobile3.setText(customer.getMobile3());

        Glide.with(activity)
                .load(customer.getProfilephotoaddress())
                .placeholder(R.drawable.oops)
                .into((ImageView) activity.findViewById(R.id.profilephotoaddresss));
    }

    @Override
    protected void onPause() {
        Log.i("ss_nw_call", new Date() + "lifecycle : Level2ProfileActivity onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("ss_nw_call", new Date() + "lifecycle : Level2ProfileActivity onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("ss_nw_call", new Date() + "lifecycle : Level2ProfileActivity onDestroy");
        super.onDestroy();
    }

    public void animateViewContactButton() {


        ObjectAnimator anim = null;
        anim = ObjectAnimator.ofInt(viewContactDetailsBtn, "textColor", Color.BLACK, Color.RED, Color.BLUE);

        //linearLayout.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        anim.setDuration(4000);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();

        ObjectAnimator anim1 = null;
        anim1 = ObjectAnimator.ofInt(bg_card, "cardBackgroundColor",
                Color.YELLOW, Color.RED, Color.BLUE, Color.GREEN,
                Color.CYAN, Color.MAGENTA,
                Color.BLACK, Color.WHITE);

        /*bg_card.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TransitionDrawable transitionDrawable = new TransitionDrawable(drawables);
        bg_card.setBackground(transitionDrawable);
        */
        anim1.setDuration(6000);
        anim1.setEvaluator(new ArgbEvaluator());
        anim1.setRepeatMode(ValueAnimator.REVERSE);
        anim1.setRepeatCount(Animation.INFINITE);
        anim1.start();

    }


    public void startShimmerAnimations() {
        // Initialize your ShimmerFrameLayout instances
        for (int i = 0; i < shimmerFrameLayouts.length; i++) {
            String resourceName = "s" + (i + 1);  // Assuming your IDs are like s1, s2, s3, ..., s33
            int resourceId = getResources().getIdentifier(resourceName, "id", getPackageName());
            shimmerFrameLayouts[i] = findViewById(resourceId);
        }
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);

        t1.startShimmer();
        t2.startShimmer();
        t3.startShimmer();

        for (ShimmerFrameLayout shimmerFrameLayout : shimmerFrameLayouts) {
            if (shimmerFrameLayout != null) {
                shimmerFrameLayout.startShimmer();
            }
        }

        /*// Simulate a delay (replace this with your actual data loading code)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopShimmerAnimations();
            }
        }, 5000); // 5000 milliseconds (5 seconds) is just an example delay*/
    }

    public static void stopShimmerAnimations() {
        for (ShimmerFrameLayout shimmerFrameLayout : shimmerFrameLayouts) {
            if (shimmerFrameLayout != null) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        }
        birthdate.setVisibility(View.VISIBLE);
        birthtime.setVisibility(View.VISIBLE);
        height.setVisibility(View.VISIBLE);
        education.setVisibility(View.VISIBLE);
        occupation.setVisibility(View.VISIBLE);
        income.setVisibility(View.VISIBLE);
        religion.setVisibility(View.VISIBLE);
        caste.setVisibility(View.VISIBLE);
        bloodgroup.setVisibility(View.VISIBLE);
        marriagestatus.setVisibility(View.VISIBLE);
        birthname.setVisibility(View.VISIBLE);
        birthplace.setVisibility(View.VISIBLE);
        fathername.setVisibility(View.VISIBLE);
        mothername.setVisibility(View.VISIBLE);
        relatives.setVisibility(View.VISIBLE);
        family.setVisibility(View.VISIBLE);
        expectations.setVisibility(View.VISIBLE);
        city.setVisibility(View.VISIBLE);
        address.setVisibility(View.VISIBLE);
        kuldaivat.setVisibility(View.VISIBLE);
        zodiac.setVisibility(View.VISIBLE);
        varn.setVisibility(View.VISIBLE);
        nakshatra.setVisibility(View.VISIBLE);
        nadi.setVisibility(View.VISIBLE);
        gan.setVisibility(View.VISIBLE);
        yoni.setVisibility(View.VISIBLE);
        charan.setVisibility(View.VISIBLE);
        gotra.setVisibility(View.VISIBLE);
        mangal.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        mobile1.setVisibility(View.VISIBLE);
        mobile2.setVisibility(View.VISIBLE);
        mobile3.setVisibility(View.VISIBLE);
    }

    public void forceBuyMembership(){


            if(ApiCallUtil.redirected_via_home_screen_noti || ApiCallUtil.redirected_via_deep_link){
                if(ApiCallUtil.redirected_via_home_screen_noti){
                    new Handler().postDelayed(() -> {
                        if (!isFinishing() && !isDestroyed()) {
                            initiateViewContactFlow(ApiCallUtil.redirected_via_home_screen_noti);
                            ApiCallUtil.redirected_via_home_screen_noti = false;
                        }

                    }, 2500);
                }
                else if(ApiCallUtil.redirected_via_deep_link){
                    new Handler().postDelayed(() -> {
                        if (!isFinishing() && !isDestroyed()) {
                            initiateViewContactFlow(ApiCallUtil.redirected_via_deep_link);
                            ApiCallUtil.redirected_via_deep_link = false;
                        }
                    }, 2500);
                }
            }
            else{

                new Handler().postDelayed(() -> {
                    if (!isFinishing() && !isDestroyed()) {
                        initiateViewContactFlow(true);
                    }
                }, 20000);
            }
    }

    private void init(){
        ctx = getApplicationContext();

        activity = this;
        startShimmerAnimations();
        initUiElements();
        handleOnClickListeners();
        animateViewContactButton();

        isAdmin = loggedinCustomer.getIsAdmin() != null && loggedinCustomer.getIsAdmin().equalsIgnoreCase("1");
        if (ApiCallUtil.redirected_via_deep_link || ApiCallUtil.redirected_via_notification)
            handlebackpressed();

    }

    private static Boolean isPkgBottomSheetShowing(BuyMembershipBottomSheetDialog bottomSheetDialog) {
        View bottomSheetView = bottomSheetDialog.getView();

        if (bottomSheetView != null) {
            LinearLayout bottomSheetLayout = bottomSheetView.findViewById(R.id.bottomSheetLayout);

            if (bottomSheetLayout != null) {
                // Delay the check by a short duration
                new Handler().postDelayed(() -> {
                    BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

                    if (bottomSheetBehavior != null &&
                            (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED ||
                                    bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)) {
                        // Handle the state
                    }
                }, 100);
            }
        }

        return false;
    }





}
