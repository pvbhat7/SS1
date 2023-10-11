package com.sdgvvk.v1.activity;


import static com.google.android.material.internal.ViewUtils.hideKeyboard;
import static com.google.android.material.internal.ViewUtils.showKeyboard;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.sdgvvk.v1.R;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOtpActivity extends AppCompatActivity {

    SpinKitView progressBar;

    CardView boxCard;

    String verificationId;
    EditText otptext;
    Button buttonVerifyOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("local_logs","SendOtpActivity - onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        initFields();
        onclickListeners();
        handleVerifyOtpFunctionality();
    }

    private void onclickListeners() {
        otptext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if (s.length() == 6)
                    buttonVerifyOtp.setEnabled(true);
                else
                    buttonVerifyOtp.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void handleVerifyOtpFunctionality() {
        final Button buttonVerify = findViewById(R.id.buttonVerifyOtp);
        verificationId = getIntent().getStringExtra("verificationId");
        buttonVerify.setOnClickListener(view -> {
            if(otptext.getText().toString().trim().isEmpty()){
                Toast.makeText(VerifyOtpActivity.this,"Please enter valid otp",Toast.LENGTH_SHORT).show();
                return;
            }
            String code = otptext.getText().toString().trim();
            if(verificationId != null){
                buttonVerify.setVisibility(View.GONE);
                showProgressBar();
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId,code);
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                buttonVerify.setVisibility(View.GONE);
                                hideProgressBar();

                                if(task.isSuccessful()){
                                    hideKeyboard(view);
                                    boxCard.setVisibility(View.GONE);
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    ApiCallUtil.setLoggedInCustomer(VerifyOtpActivity.this,user.getPhoneNumber().replace("+91",""),(SpinKitView)findViewById(R.id.progressBar));
                                    //showProgressBar();
                                    // TODO: 03-Sep-23
                                    //ApiUtils.initClientAppData(getApplicationContext(),user,VerifyOtpActivity.this);
                                    /*new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideProgressBar();
                                            Intent intent = new Intent(VerifyOtpActivity.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            VerifyOtpActivity.this.startActivity(intent);
                                        }
                                    }, 3000);*/

                                }
                                else
                                {
                                    Toast.makeText(VerifyOtpActivity.this,"Invalid otp entered",Toast.LENGTH_SHORT).show();
                                    buttonVerify.setVisibility(View.VISIBLE);
                                }
                            }
                        });

            }

        });

        findViewById(R.id.txtResendOtp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91"+getIntent().getStringExtra("mobile"),
                        60,
                        TimeUnit.SECONDS,
                        VerifyOtpActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(VerifyOtpActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verificationId = newVerificationId;
                                Toast.makeText(VerifyOtpActivity.this,"otp sent",Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
    }

    private void initFields() {
        buttonVerifyOtp = findViewById(R.id.buttonVerifyOtp);
        boxCard = findViewById(R.id.boxCard);
        progressBar = findViewById(R.id.progressBar);
        otptext = findViewById(R.id.otptext);
        TextView textMobile = findViewById(R.id.textMobile);
        String mobile = String.format("+91 - %s",getIntent().getStringExtra("mobile"));
        if(mobile.equalsIgnoreCase("+91 - 1111111111") || mobile.equalsIgnoreCase("+91 - 1111111112") || mobile.equalsIgnoreCase("+91 - 1111111113")){
            otptext.setText("111111");
            buttonVerifyOtp.setEnabled(true);
        }
        textMobile.setText(mobile);
    }


    public void showProgressBar(){
        progressBar = findViewById(R.id.progressBarBtnView);
        progressBar.setVisibility(View.VISIBLE);

    }

    public void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
    }


}