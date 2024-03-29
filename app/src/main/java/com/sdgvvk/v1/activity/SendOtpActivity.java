package com.sdgvvk.v1.activity;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sdgvvk.v1.LocalCache;
import com.sdgvvk.v1.MainActivity;
import com.sdgvvk.v1.ProjectConstants;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.api.HelperUtils;

import java.util.List;

public class SendOtpActivity extends AppCompatActivity {

    private static final int REQ_CODE = 100 ;
    Activity activity;
    SpinKitView progressBar,progressBarBtnView;
    CardView boxCard;

    LinearLayout loginbox,errorbox;
    TextView errorTxt;

    Button contactsupportbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);
        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().get(ProjectConstants.TARGET_CLASS) != null){
                Log.i("ss_nw_call", "targetclass : "+(String)getIntent().getExtras().get(ProjectConstants.TARGET_CLASS));
            }
        }
        activity = this;
        handleOnClickListeners();
        //test_flow();
        launch_flow();



    }

    private void checkforappupdates() {
        Log.e("sendotpactivity", "mainactivity calling checkforappupdates");
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

    /*private void test_flow() {
        findViewById(R.id.loginbox).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.inputMobile)).setText("1111111112");
        findViewById(R.id.buttonGetOtp).setEnabled(true);

            Boolean isLogoutFlow = false;

            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().get("logout") != null) {
                    if ((Boolean) getIntent().getExtras().get("logout"))
                        isLogoutFlow = true;
                }
            }

            if (!isLogoutFlow) {
                boxCard.setVisibility(View.GONE);
                Log.i("local_logs", "SendOtpActivity - onCreate called");
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Log.i("local_logs", "SendOtpActivity - checking firebase logged in codition");
                    // checking if user is registered in firebase
                    ApiCallUtil.setLoggedInCustomer(this,user.getPhoneNumber().replace("+91",""), progressBar);
                }
                else
                    boxCard.setVisibility(View.VISIBLE);
            }
            else
                boxCard.setVisibility(View.VISIBLE);


    }*/

    private void launch_flow() {

        String isLive = LocalCache.getIsLive(this);
        if(isLive != null && isLive.equalsIgnoreCase("true")){
            loginbox.setVisibility(View.VISIBLE);
            errorbox.setVisibility(View.GONE);
            Boolean isLogoutFlow = false;

            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().get("logout") != null) {
                    if ((Boolean) getIntent().getExtras().get("logout"))
                        isLogoutFlow = true;
                }
            }

            if (!isLogoutFlow) {
                boxCard.setVisibility(View.GONE);
                Log.i("local_logs", "SendOtpActivity - onCreate called");
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                    // handle notification flow
                    if(ApiCallUtil.noti_target_class != null){
                        if(ApiCallUtil.noti_target_class.equalsIgnoreCase("Level2ProfileActivity")){
                            ApiCallUtil.redirected_via_notification = true;
                            String cpid = ApiCallUtil.noti_target_cpid;
                            ApiCallUtil.noti_target_cpid = null;
                            ApiCallUtil.noti_target_class = null;
                            //ApiCallUtil.getLevel2Data(cpid, activity);
                            activity.startActivity(new Intent(activity, Level2ProfileActivity.class)
                                    .putExtra("level2data", cpid));
                        }
                        else if(ApiCallUtil.noti_target_class.equalsIgnoreCase("NotificationActivity")){
                            startActivity(new Intent(activity, NotificationActivity.class));
                        }
                    }else{
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        Log.i("local_logs", "SendOtpActivity - checking firebase logged in codition");
                        // checking if user is registered in firebase
                        ApiCallUtil.setLoggedInCustomer(this,user.getPhoneNumber().replace("+91",""), progressBar);
                    }


                }
                else{
                    boxCard.setVisibility(View.VISIBLE);

                }

            }
            else{
                // logout flow
                checkforappupdates();
                boxCard.setVisibility(View.VISIBLE);
                errorbox.setVisibility(View.GONE);
                loginbox.setVisibility(View.VISIBLE);
            }

        }
        else{
            loginbox.setVisibility(View.GONE);
            errorbox.setVisibility(View.VISIBLE);
            contactsupportbtn.setVisibility(View.VISIBLE);
            errorTxt.setText(isLive != null ? isLive : "server error");
            TextInputEditText in = findViewById(R.id.inputMobile);
            if(in != null)
                in.setEnabled(false);

            contactsupportbtn.setOnClickListener(view -> Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.CALL_PHONE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                String adminPhone = LocalCache.getAdminPhone(SendOtpActivity.this);
                                if(adminPhone.isEmpty() || adminPhone == "")
                                    callIntent.setData(Uri.parse("tel:+917972864487" ));
                                else
                                    callIntent.setData(Uri.parse("tel:+91"+adminPhone ));
                                startActivity(callIntent);
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check());
        }
    }

    private void handleOnClickListeners() {
        contactsupportbtn = findViewById(R.id.contactsupportbtn);
        loginbox = findViewById(R.id.loginbox);
        errorbox = findViewById(R.id.errorbox);
        errorTxt = findViewById(R.id.errorTxt);
        progressBar = findViewById(R.id.progressBar);
        progressBarBtnView = findViewById(R.id.progressBarBtnView);
        boxCard = findViewById(R.id.boxCard);
        final EditText inputMobile = findViewById(R.id.inputMobile);
        Button buttonGetOtp = findViewById(R.id.buttonGetOtp);
        findViewById(R.id.termsOfService).setOnClickListener(view -> {
            String uri = "https://tavrostechinfo.com/PROGYM/progym_app_privacy_policy.html";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        });
        findViewById(R.id.tavrosLink2).setOnClickListener(view -> {
            String uri = "https://tavrostechinfo.com/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        });

        buttonGetOtp.setOnClickListener(view -> {
            hideKeyboard(view);
            if (!HelperUtils.isConnected(this)) {
                Toast.makeText(SendOtpActivity.this, "NO INTERNET", Toast.LENGTH_SHORT).show();
            } else {
                activity.findViewById(R.id.buttonGetOtp).setVisibility(View.GONE);
                showProgressBar();
                ApiCallUtil.CheckAccountStatus(this,inputMobile.getText().toString());
            }
        });

        inputMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if (s.length() == 10)
                    buttonGetOtp.setEnabled(true);
                else
                    buttonGetOtp.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void signInWIthPhoneAuthCredentials(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(task -> {
                    /*progressBar.setVisibility(View.GONE);
                    buttonVerify.setVisibility(View.INVISIBLE);*/
                    if (task.isSuccessful()) {
                        // register user to api server
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        //CustomProgressDialog pb = new CustomProgressDialog(SendOtpActivity.this, "");
                        showProgressBar();

                        //ApiUtils.initClientAppData(getApplicationContext(), user, SendOtpActivity.this);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideProgressBar();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }, 2000);
                    } else {
                        Toast.makeText(SendOtpActivity.this, "Invalid otp entered", Toast.LENGTH_SHORT).show();
                        //buttonVerify.setVisibility(View.VISIBLE);
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("local_logs", "SendOtpActivity - onDestroy called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("local_logs", "SendOtpActivity - onResume called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("local_logs", "SendOtpActivity - onStart called");
    }

    public void showProgressBar(){
        progressBar = findViewById(R.id.progressBarBtnView);
        //Circle d = new Circle();
        progressBar.setVisibility(View.VISIBLE);
        //progressBar.setIndeterminateDrawable(/d);
        //boxCard.setVisibility(View.GONE);
    }

    public void hideProgressBar(){
        progressBarBtnView.setVisibility(View.GONE);
        //boxCard.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("sendotpactivity", "sendotpactivity onActivityResult req_code = "+requestCode+" & res_code = "+resultCode);
        // handle callback
        if(requestCode == REQ_CODE){
            if (resultCode != RESULT_OK) {
                checkforappupdates();
            }
        }

    }




}