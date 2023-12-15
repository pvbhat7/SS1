package com.sdgvvk.v1.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.sdgvvk.v1.MainActivity;
import com.sdgvvk.v1.ProjectConstants;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.api.HelperUtils;

public class NotificationActivity extends AppCompatActivity {

    SpinKitView progressBar;
    static Activity activity;

    static String targetClass,title,message,image,cpid,category,longtext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_handler);
        progressBar = findViewById(R.id.progressBar1);
        activity = this;

        handleIntent(getIntent());

        if (ApiCallUtil.redirected_via_deep_link || ApiCallUtil.redirected_via_notification)
            handlebackpressed();

    }

    private void launchIntentByTargetClass() {
        Intent intent = null;
        if (targetClass.equalsIgnoreCase(ProjectConstants.LEVEL_2_PROFILE_ACTIVITY)) {
            ApiCallUtil.redirected_via_notification = true;
            //ApiCallUtil.getLevel2Data(cpid, this);
            activity.startActivity(new Intent(activity, Level2ProfileActivity.class)
                    .putExtra("level2data", cpid));
        } else if (targetClass.equalsIgnoreCase(ProjectConstants.NOTIFICATION_ACTIVITY)){
            if(category.equalsIgnoreCase(ProjectConstants.CATEGORY_SMALL_TEXT_DIALOG)){
                showMiniDialog(title,message);
            }
            else if(category.equalsIgnoreCase(ProjectConstants.CATEGORY_IMAGE_DIALOG)){
                Dialog d = new Dialog(this);
                Activity activity = this;
                d.setContentView(R.layout.notification_image_dialog);
                Glide.with(activity)
                        .load(image)
                        .placeholder(R.drawable.oops)
                        .into((ImageView) d.findViewById(R.id.img));
                d.findViewById(R.id.resultCard).setOnClickListener(view -> {
                    d.dismiss();
                    openMainactivity();
                });
                d.setCanceledOnTouchOutside(false);
                d.setCancelable(false);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
            }else if(category.equalsIgnoreCase(ProjectConstants.CATEGORY_LONG_TEXT_MESSAGE)){
                Dialog d = new Dialog(this);
                Activity activity = this;
                d.setContentView(R.layout.notification_text_dialog);
                ((TextView)d.findViewById(R.id.longtext)).setText(longtext);

                d.findViewById(R.id.resultCard).setOnClickListener(view -> {
                    d.dismiss();
                    openMainactivity();
                });


                d.setCanceledOnTouchOutside(false);
                d.setCancelable(false);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
            }
        }
        else
            openMainactivity();

    }

    private void handleIntent(Intent intent) {
        // Extract data from the intent and perform necessary actions

        if (ApiCallUtil.noti_target_class != null){
            targetClass = ApiCallUtil.noti_target_class;
            title = ApiCallUtil.noti_title;
            message = ApiCallUtil.noti_message;
            image = ApiCallUtil.noti_image;
            cpid = ApiCallUtil.noti_target_cpid;
            category = ApiCallUtil.noti_category;
            longtext = ApiCallUtil.noti_longtext;
        }
        else if (intent.getExtras() != null) {
            if (intent.getExtras().get(ProjectConstants.TARGET_CLASS) != null) {
                targetClass = (String) intent.getExtras().get(ProjectConstants.TARGET_CLASS);
                title = (String) intent.getExtras().get(ProjectConstants.TITLE);
                message = (String) intent.getExtras().get(ProjectConstants.MESSAGE);
                image = (String) intent.getExtras().get(ProjectConstants.IMAGE);
                cpid = (String) intent.getExtras().get(ProjectConstants.CPID);
                category = (String) intent.getExtras().get(ProjectConstants.CATEGORY);
                longtext = (String) intent.getExtras().get(ProjectConstants.LONG_TEXT);
            }
        }

        if (targetClass != null && !targetClass.isEmpty()) {
            Log.i("ss_nw_call", "targetclass : " + targetClass);
            launchIntentByTargetClass();
        }
        else{
            openMainactivity();
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
                openMainactivity();
            }
        });
    }

    private static void openMainactivity() {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public static void showMiniDialog(String title, String message){
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    openMainactivity();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("ok", dialogClickListener)
                .show();
    }



}
