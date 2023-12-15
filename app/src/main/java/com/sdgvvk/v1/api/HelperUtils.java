package com.sdgvvk.v1.api;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.sdgvvk.v1.LocalCache;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.modal.Customer;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.ybq.android.spinkit.SpinKitView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HelperUtils {



    public static void vibrateFunction(Activity activity) {
        Vibrator vibe = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(50);
    }


    public static boolean isConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            return true;
        }

        return false;
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
        if(!HelperUtils.isConnected(activity))
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
        Customer customer = LocalCache.getLoggedInCustomer(activity);
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
        //img_pp = d.findViewById(R.id.img);
        Glide.with(activity)
                .load(img)
                .placeholder(R.drawable.oops)
                .into((PhotoView)d.findViewById(R.id.imageView));

        /*scaleGestureDetector = new ScaleGestureDetector(activity, new ScaleListener());

       img_pp.setOnTouchListener((View.OnTouchListener) (v, event) -> {
            scaleGestureDetector.onTouchEvent(event);
            return true;
        });
*/

        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.show();
    }


    public static String convertBitmapToBase64(Bitmap bitmap , int maxSize){
        bitmap = getResizedBitmap(bitmap , maxSize);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] array = bao.toByteArray();
        return Base64.encodeToString(array,Base64.DEFAULT);
    }

    public static Bitmap convertBase64ToBitmap(String base64) {
        try {
            byte[] decodedBytes = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Drawable convertBitmapToDrawable(Context context, Bitmap bitmap) {
        // Create a Drawable from the Bitmap
        return new BitmapDrawable(context.getResources(), bitmap);
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

    public static String addDaysToDate(String inputDate,String days){
        Date outputDate = Date.from(new Date(inputDate).toInstant().plus(Integer.parseInt(days), ChronoUnit.DAYS));
        return new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(outputDate);
    }


}
