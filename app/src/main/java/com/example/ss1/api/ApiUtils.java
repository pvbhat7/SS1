package com.example.ss1.api;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;

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


}
