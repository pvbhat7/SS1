package com.example.ss1.api;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ss1.LocalCache;
import com.example.ss1.R;
import com.example.ss1.modal.Customer;
import com.example.ss1.ui.home.HomeFragment;
import com.github.ybq.android.spinkit.SpinKitView;

public class ApiUtils {

    public static void vibrateFunction(Activity activity) {
        Vibrator vibe = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(50);
    }



    public static boolean isConnected() {
        Boolean result = false;
        String command = "ping -c 1 google.com";
        try {
            result = Runtime.getRuntime().exec(command).waitFor() == 0;
        } catch (Exception e) {
        }

        //Toast.makeText(activity,"NO INTERNET",Toast.LENGTH_SHORT).show();
        return result;
    }

    public static void checkNetworkStatus(Activity activity) {
        if(!ApiUtils.isConnected())
            showNoInternetDialog(activity);
    }

    public static void showNoInternetDialog(Activity activity) {
        Dialog d = new Dialog(activity);
        d.setContentView(R.layout.nointernet_dialog);
        Button tryagaininternetconnectionbtn = d.findViewById(R.id.tryagaininternetconnectionbtn);
        tryagaininternetconnectionbtn.setOnClickListener(view -> {
            d.dismiss();
            checkNetworkStatus(activity);
        });

        //Glide.with(this).asGif().load(img).into((ImageView) d.findViewById(R.id.result_img));

        d.setCanceledOnTouchOutside(false);
        d.setCancelable(false);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.show();
    }

    public static void showDialog(Fragment fragment , SpinKitView progressBar, Activity activity, int img, String title, String btnTitle) {
        Customer customer = LocalCache.retrieveLoggedInCustomer(activity);
        Dialog d = new Dialog(activity);
        d.setContentView(R.layout.generic_dialog);

        Glide.with(activity).asGif().load(img).into((ImageView) d.findViewById(R.id.d_img));

        d.findViewById(R.id.d_img);
        ((TextView)d.findViewById(R.id.d_title)).setText(title);
        ((Button)d.findViewById(R.id.d_btn)).setText(btnTitle);
        ((Button)d.findViewById(R.id.d_btn)).setOnClickListener(view -> {
            d.dismiss();
            ApiCallUtil.getAllProfiles(customer.getProfileId(),fragment, progressBar,activity,true);
        });


        d.setCanceledOnTouchOutside(false);
        d.setCancelable(false);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.show();
    }



}
