package com.example.ss1.api;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ss1.LocalCache;
import com.example.ss1.R;
import com.example.ss1.modal.Customer;
import com.github.ybq.android.spinkit.SpinKitView;

import java.io.ByteArrayOutputStream;

public class ApiUtils {

    private static Matrix matrix = new Matrix();
    private static float scale = 1f;
    private static ScaleGestureDetector scaleGestureDetector;

    private static ImageView img_pp;


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

    public static void showSearchingDialog(Fragment fragment , SpinKitView progressBar, Activity activity, int img, String title, String btnTitle) {
        Customer customer = LocalCache.retrieveLoggedInCustomer(activity);
        Dialog d = new Dialog(activity);
        d.setContentView(R.layout.searching_dialog);

        Glide.with(activity).asGif().load(img).into((ImageView) d.findViewById(R.id.d_img));

        d.findViewById(R.id.d_img);
        ((TextView)d.findViewById(R.id.d_title)).setText(title);
        ((Button)d.findViewById(R.id.d_btn)).setText(btnTitle);
        ((Button)d.findViewById(R.id.d_btn)).setOnClickListener(view -> {
            d.dismiss();
            ApiCallUtil.getAllProfiles(customer.getProfileId(),fragment, progressBar,activity,true);
        });


        /*d.setCanceledOnTouchOutside(false);
        d.setCancelable(false);*/
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.show();
    }

    public static void showDialog(Activity activity, int img, String title, String subtitle) {
        Dialog d = new Dialog(activity);
        d.setContentView(R.layout.generic_dialog);

        Glide.with(activity)
                .load(img)
                .into((ImageView) d.findViewById(R.id.d_img));
        d.findViewById(R.id.d_img);
        ((TextView)d.findViewById(R.id.d_title)).setText(title);
        ((TextView)d.findViewById(R.id.d_subtitle)).setText(subtitle);


        /*d.setCanceledOnTouchOutside(false);
        d.setCancelable(false);*/
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.show();
    }

    public static void showImageDialog(Activity activity, String img) {
        Dialog d = new Dialog(activity);
        d.setContentView(R.layout.image_dialog);
        img_pp = d.findViewById(R.id.img);
        Glide.with(activity)
                .load(img)
                .placeholder(R.drawable.oops)
                .into(img_pp);

        scaleGestureDetector = new ScaleGestureDetector(activity, new ScaleListener());

       img_pp.setOnTouchListener((View.OnTouchListener) (v, event) -> {
            scaleGestureDetector.onTouchEvent(event);
            return true;
        });


        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.show();
    }


    public static String convertBitmapToString(Bitmap bitmap , int maxSize){
        bitmap = getResizedBitmap(bitmap , maxSize);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] array = bao.toByteArray();
        String imgB64 = Base64.encodeToString(array,Base64.DEFAULT);
        return imgB64;
    }
    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private static class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            scale *= scaleFactor;

            // Limit the scale range (optional)
            float minScale = 1f;
            float maxScale = 5f;
            scale = Math.max(minScale, Math.min(scale, maxScale));

            matrix.setScale(scale, scale);
            img_pp.setImageMatrix(matrix);

            return true;
        }
    }



}
