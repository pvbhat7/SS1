package com.sdgvvk.v1;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sdgvvk.v1.activity.Level2ProfileActivity;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.sdgvvk.v1.api.HelperUtils;
import com.sdgvvk.v1.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE = 100 ;

    private ActivityMainBinding binding;
    private boolean lockAspectRatio = false, setBitmapMaxWidthHeight = false;
    private int ASPECT_RATIO_X = 16, ASPECT_RATIO_Y = 9, bitmapMaxWidth = 1000, bitmapMaxHeight = 1000;
    private int IMAGE_COMPRESSION = 80;
    public static final int PICK_IMAGE_REQUEST = 1;

    static Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ctx == null || ((MainActivity) ctx).isDestroyed())
            ctx = this;

        checkforappupdates();
        ApiCallUtil.getAdminPhone(this);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            // checking if user is registered in firebase
            ApiCallUtil.setLoggedInCustomer(this,user.getPhoneNumber().replace("+91",""), null);
        }

        try{
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            BottomNavigationView navView = findViewById(R.id.nav_view);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_matches,R.id.navigation_myaccount)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);

            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
        }
        catch (Exception e){
            Log.i("local","");
        }
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


                    if (ApiCallUtil.onboardDialog != null) {
                        Dialog d = ApiCallUtil.onboardDialog;
                        CircularImageView profilePhotoAddress = ApiCallUtil.onboardDialog.findViewById(R.id.profilePhotoAddress);
                        ApiCallUtil.b64 = imgB64;
                        Glide.with(this).load(uri.toString()).placeholder(R.drawable.oops).into(profilePhotoAddress);


                        if (!((TextInputEditText) d.findViewById(R.id.name)).getText().toString().trim().isEmpty()
                                && !((TextInputEditText) d.findViewById(R.id.email)).getText().toString().trim().isEmpty()
                                && !((AutoCompleteTextView) d.findViewById(R.id.gender)).getText().toString().trim().isEmpty()
                                && !((TextInputEditText) d.findViewById(R.id.birthdate)).getText().toString().trim().isEmpty()
                                && ApiCallUtil.b64 != null)
                            d.findViewById(R.id.create_profile_btn).setEnabled(true);
                        else
                            d.findViewById(R.id.create_profile_btn).setEnabled(false);


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Handle the case where the cropped image URI is null
            }
        }
        else if (resultCode == UCrop.RESULT_ERROR) {
            // Handle the cropping error
            Throwable cropError = UCrop.getError(data);
        }

        if(requestCode == REQ_CODE){
            if (resultCode != RESULT_OK) {
                checkforappupdates();
            }
        }

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

    public static Context getContextObject() {
        return ctx;
    }

    private void checkforappupdates() {
        Log.e("ImageUtils", "calling checkforappupdates");
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,AppUpdateType.IMMEDIATE,this,REQ_CODE);
                } catch (IntentSender.SendIntentException e) {
                    Log.e("ImageUtils", e.toString());
                    throw new RuntimeException(e);
                }
            }
        });
    }


}