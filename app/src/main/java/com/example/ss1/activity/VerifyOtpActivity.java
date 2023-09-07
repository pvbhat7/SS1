package com.example.ss1.activity;


import static com.google.android.material.internal.ViewUtils.hideKeyboard;
import static com.google.android.material.internal.ViewUtils.requestFocusAndShowKeyboard;
import static com.google.android.material.internal.ViewUtils.showKeyboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.ss1.MainActivity;
import com.example.ss1.R;
import com.example.ss1.api.ApiCallUtil;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Circle;
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

    private EditText inputCode1 , inputCode2 , inputCode3 , inputCode4 , inputCode5 , inputCode6 ;
    String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("local_logs","SendOtpActivity - onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        boxCard = findViewById(R.id.boxCard);
        progressBar = findViewById(R.id.progressBar);
        initFields();
        setupOtpInputs();
        handleVerifyOtpFunctionality();
    }

    private void handleVerifyOtpFunctionality() {
        final Button buttonVerify = findViewById(R.id.buttonVerifyOtp);
        verificationId = getIntent().getStringExtra("verificationId");
        buttonVerify.setOnClickListener(view -> {
            if(inputCode1.getText().toString().trim().isEmpty() ||
                    inputCode2.getText().toString().trim().isEmpty() ||
                    inputCode3.getText().toString().trim().isEmpty() ||
                    inputCode4.getText().toString().trim().isEmpty() ||
                    inputCode5.getText().toString().trim().isEmpty() ||
                    inputCode6.getText().toString().trim().isEmpty()){
                Toast.makeText(VerifyOtpActivity.this,"Please enter valid otp",Toast.LENGTH_SHORT).show();
                return;
            }
            String code = inputCode1.getText().toString().trim() +
                    inputCode2.getText().toString().trim() +
                    inputCode3.getText().toString().trim() +
                    inputCode4.getText().toString().trim() +
                    inputCode5.getText().toString().trim() +
                    inputCode6.getText().toString().trim();
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
        TextView textMobile = findViewById(R.id.textMobile);
        textMobile.setText(String.format("+91 - %s",getIntent().getStringExtra("mobile")));

        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);
    }

    public void setupOtpInputs(){
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().trim().isEmpty())
                    inputCode2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().trim().isEmpty())
                    inputCode3.requestFocus();
                else
                    inputCode1.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().trim().isEmpty())
                    inputCode4.requestFocus();
                else
                    inputCode2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().trim().isEmpty())
                    inputCode5.requestFocus();
                else
                    inputCode3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().trim().isEmpty())
                    inputCode6.requestFocus();
                else
                    inputCode4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.toString().trim().isEmpty())
                    inputCode5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void showProgressBar(){
        progressBar = findViewById(R.id.progressBarBtnView);
        progressBar.setVisibility(View.VISIBLE);

    }

    public void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
    }


}