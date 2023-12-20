package com.sdgvvk.v1.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sdgvvk.v1.LocalCache;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.modal.Customer;
import com.sdgvvk.v1.modal.Level_2_Modal;

import java.net.URLEncoder;
import java.util.List;

public class ViewedContactBottomSheetDialog extends BottomSheetDialogFragment {

    String loggedincustomerprofilelink,profileLink,name ;

    private static String mobile = "" , message = "श्री दत्त गुरु वधू-वर केंद्र : \n अँप डाउनलोड लिंक \n: http://bit.ly/3SsXq7M";

    private String youtubelink = "https://www.youtube.com/watch?v=Nzurh8PXEuM&t=2s";
    Activity activity;

    LinearLayout layout_1 , layout_2 , layout_3 , layout_4;
    ImageView call1 , call2 , call3 , call4 , whatsapp1 , whatsapp2 , whatsapp3 , whatsapp4;

    TextView mobile1txt , mobile2txt , mobile3txt , mobile4txt;

    Level_2_Modal obj;

    TextInputEditText address;



    public ViewedContactBottomSheetDialog(Activity activity, Level_2_Modal obj) {
        this.activity = activity;
        this.obj = obj;
    }

    public ViewedContactBottomSheetDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.viewed_contact_bottom_sheet, container, false);

        init(v);
        handleOnClickListeners();


        return v;
    }

    private void handleOnClickListeners() {

        if(obj != null){
            Customer loggedincustomer = LocalCache.getLoggedInCustomer(this.getActivity());

            loggedincustomerprofilelink = "https://tavrostechinfo.com/profile?id="+loggedincustomer.getProfileId()+" ";
            profileLink = "https://tavrostechinfo.com/profile?id="+obj.getProfileId()+" ";
            name = obj.getFirstname()+" "+obj.getLastname();


            if(obj.getMobile1() != null && !obj.getMobile1().isEmpty()){
                layout_1.setVisibility(View.VISIBLE);
                mobile1txt.setText(obj.getMobile1());

                call1.setOnClickListener(view -> makecall(obj.getMobile1().toString().trim()));

                whatsapp1.setOnClickListener(view -> {
                    mobile = obj.getMobile1();
                    
                    sendWhatsappMsg("+91" + obj.getMobile1().toString().trim());
                });
            }

            if(obj.getMobile2() != null && !obj.getMobile2().isEmpty()){
                layout_2.setVisibility(View.VISIBLE);
                mobile2txt.setText(obj.getMobile2());

                call2.setOnClickListener(view -> makecall(obj.getMobile2().toString().trim()));

                whatsapp2.setOnClickListener(view -> {
                    mobile = obj.getMobile2();
                    
                    sendWhatsappMsg("+91" + obj.getMobile2().toString().trim());
                });
            }

            if(obj.getMobile3() != null && !obj.getMobile3().isEmpty()){
                layout_3.setVisibility(View.VISIBLE);
                mobile3txt.setText(obj.getMobile3());

                call3.setOnClickListener(view -> makecall(obj.getMobile3().toString().trim()));

                whatsapp3.setOnClickListener(view -> {
                    mobile = obj.getMobile3();
                    
                    sendWhatsappMsg("+91" + obj.getMobile3().toString().trim());
                });
            }

            if(obj.getMobile4() != null && !obj.getMobile4().isEmpty()){
                layout_4.setVisibility(View.VISIBLE);
                mobile4txt.setText(obj.getMobile4());

                call4.setOnClickListener(view -> makecall(obj.getMobile4().toString().trim()));

                whatsapp4.setOnClickListener(view -> {
                    mobile = obj.getMobile4();
                    
                    sendWhatsappMsg("+91" + obj.getMobile4().toString().trim());
                });
            }

            address.setText(obj.getAddress());
        }


    }

    private void makecall(String mobile) {
        Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:+91" + mobile));
                            activity.startActivity(callIntent);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    private void sendWhatsappMsg(String phoneNumber) {
        String message = "नमस्कार "+name+",\n" +
                "\n मी तुमची profile \"श्री दत्त गुरु वधू-वर केंद्र अँप\" वर पहिली आहे आणि मला profile आवडली आहे.\nतरी आपण सुद्धा एकदा माझी profile पाहावी खालील लिंक ओपन करून  " +
                "\n\n" + " माझी प्रोफाइल  लिंक :\uD83D\uDC47\uD83C\uDFFB \uD83D\uDC47\uD83C\uDFFB \uD83D\uDC47\uD83C\uDFFB \n"+loggedincustomerprofilelink+"\n\n"
                +"   तुमची प्रोफाइल  लिंक :\uD83D\uDC47\uD83C\uDFFB \uD83D\uDC47\uD83C\uDFFB \uD83D\uDC47\uD83C\uDFFB \n"+profileLink+"\n\n" +
                "\n अँप डाउनलोड लिंक :\uD83D\uDC47\uD83C\uDFFB \uD83D\uDC47\uD83C\uDFFB \uD83D\uDC47\uD83C\uDFFB \n https://play.google.com/store/apps/details?id=com.sdgvvk.v1";

        // create an Intent to send data to the whatsapp
        Intent intent = new Intent(Intent.ACTION_VIEW);    // setting action

        // setting data url, if we not catch the exception then it shows an error
        try {
            String url = "https://api.whatsapp.com/send?phone="+phoneNumber+"" + "&text=" + URLEncoder.encode(message, "UTF-8");
            intent.setData(Uri.parse(url));
            activity.startActivity(intent);
        }
        catch(Exception e){
            Log.d("notSupport", "thrown by encoder");
        }
    }

    public void init(View v){

        address = v.findViewById(R.id.address);
        call1 = v.findViewById(R.id.call1);
        call2 = v.findViewById(R.id.call2);
        call3 = v.findViewById(R.id.call3);
        call4 = v.findViewById(R.id.call4);

        whatsapp1 = v.findViewById(R.id.whatsapp1);
        whatsapp2 = v.findViewById(R.id.whatsapp2);
        whatsapp3 = v.findViewById(R.id.whatsapp3);
        whatsapp4 = v.findViewById(R.id.whatsapp4);

        layout_1 = v.findViewById(R.id.layout_1);
        layout_2 = v.findViewById(R.id.layout_2);
        layout_3 = v.findViewById(R.id.layout_3);
        layout_4 = v.findViewById(R.id.layout_4);

        mobile1txt = v.findViewById(R.id.mobile1);
        mobile2txt = v.findViewById(R.id.mobile2);
        mobile3txt = v.findViewById(R.id.mobile3);
        mobile4txt = v.findViewById(R.id.mobile4);
    }



}
