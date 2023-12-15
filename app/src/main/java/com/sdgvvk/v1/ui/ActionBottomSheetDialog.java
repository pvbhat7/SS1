package com.sdgvvk.v1.ui;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.activity.AllMemberActivity;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.modal.Level_1_cardModal;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActionBottomSheetDialog extends BottomSheetDialogFragment {

    private static String mobile = "" , message = "श्री दत्त गुरु वधू-वर केंद्र : \n अँप डाउनलोड लिंक \n: http://bit.ly/3SsXq7M";
    private static final int REQUEST_SMS = 1;

    private String youtubelink = "https://www.youtube.com/watch?v=Nzurh8PXEuM&t=2s";
    Activity activity;
    TextInputEditText followupdate;

    LinearLayout layout_1 , layout_2 , layout_3 , layout_4;
    TextInputEditText comment;
    Button savebtn;
    ImageView call1 , call2 , call3 , call4 , whatsapp1 , whatsapp2 , whatsapp3 , whatsapp4;

    TextView mobile1txt , mobile2txt , mobile3txt , mobile4txt;

    Level_1_cardModal obj;

    String profileLink,name ;
    CardView card;
    RecyclerView recyclerview;


    public ActionBottomSheetDialog(Activity activity, Level_1_cardModal obj, CardView card) {
        this.activity = activity;
        this.obj = obj;
        this.card = card;
    }

    public ActionBottomSheetDialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.action_bottom_sheet, container, false);

        init(v);
        handleOnClickListeners(v);

        if(obj != null){
            profileLink = "https://tavrostechinfo.com/profile?id="+obj.getProfileId()+" ";
            name = obj.getName();

            ApiCallUtil.getFollowupByCpid(obj.getProfileId(),recyclerview,activity);
        }




        return v;
    }

    private void handleOnClickListeners(View v) {

        if(obj != null){
            if(obj.getMobile1() != null && !obj.getMobile1().isEmpty()){
                layout_1.setVisibility(View.VISIBLE);
                mobile1txt.setText(obj.getMobile1());

                call1.setOnClickListener(view -> makecall(obj.getMobile1().toString().trim()));

                whatsapp1.setOnClickListener(view -> {
                    mobile = obj.getMobile1();
                    smstrigger();
                    sendWhatsappMsg("+91" + obj.getMobile1().toString().trim());
                });
            }

            if(obj.getMobile2() != null && !obj.getMobile2().isEmpty()){
                layout_2.setVisibility(View.VISIBLE);
                mobile2txt.setText(obj.getMobile2());

                call2.setOnClickListener(view -> makecall(obj.getMobile2().toString().trim()));

                whatsapp2.setOnClickListener(view -> {
                    mobile = obj.getMobile2();
                    smstrigger();
                    sendWhatsappMsg("+91" + obj.getMobile2().toString().trim());
                });
            }

            if(obj.getMobile3() != null && !obj.getMobile3().isEmpty()){
                layout_3.setVisibility(View.VISIBLE);
                mobile3txt.setText(obj.getMobile3());

                call3.setOnClickListener(view -> makecall(obj.getMobile3().toString().trim()));

                whatsapp3.setOnClickListener(view -> {
                    mobile = obj.getMobile3();
                    smstrigger();
                    sendWhatsappMsg("+91" + obj.getMobile3().toString().trim());
                });
            }

            if(obj.getMobile4() != null && !obj.getMobile4().isEmpty()){
                layout_4.setVisibility(View.VISIBLE);
                mobile4txt.setText(obj.getMobile4());

                call4.setOnClickListener(view -> makecall(obj.getMobile4().toString().trim()));

                whatsapp4.setOnClickListener(view -> {
                    mobile = obj.getMobile4();
                    smstrigger();
                    sendWhatsappMsg("+91" + obj.getMobile4().toString().trim());
                });
            }

            // date
            followupdate.setOnClickListener(view1 -> {
                //Date Picker
                MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
                MaterialDatePicker<Long> picker = builder.build();
                picker.show(((AllMemberActivity)activity).getSupportFragmentManager(), picker.toString());
                picker.addOnPositiveButtonClickListener(selectedDate -> {
                    // Do something...
                    //Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    //calendar.setTimeInMillis(selection);
                    Date date = new Date(selectedDate);
                    SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    followupdate.setText(simpleFormat.format(date));
                });
            });

            savebtn.setOnClickListener(view -> {
                savebtn.setEnabled(false);
                card.setCardBackgroundColor(Color.YELLOW);
                Toast.makeText(activity, "Saved", Toast.LENGTH_SHORT).show();
                String followupdate_= "" , comment_ = "";
                followupdate_ = followupdate.getText().toString().trim();
                comment_ = comment.getText().toString().trim();
                ApiCallUtil.SaveFollowUp(obj.getProfileId(),followupdate_,comment_);
            });
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

    private void smstrigger() {
        Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.SEND_SMS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            sendSMS();
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
                "\n" +
                "    \"*श्री दत्त गुरु वधू-वर केंद्र*\" मध्ये आपले हार्दिक स्वागत. \uD83C\uDF89 \uD83C\uDF89 \uD83C\uDF89\n" +
                "    \nआम्हाला हे सांगताना खूप आनंद होत आहे की आम्ही स्वतःचे वधू-वर सूचक केंद्र सुरु केलं आहे तरी सर्वांनी \n" +
                "     खालील लिंक वापरून स्थळें website आणि  app वर पाहू शकता.\n" +
                "\n" +
                "    अँप डाउनलोड लिंक :\uD83D\uDC47\uD83C\uDFFB \uD83D\uDC47\uD83C\uDFFB \uD83D\uDC47\uD83C\uDFFB \n https://play.google.com/store/apps/details?id=com.sdgvvk.v1\n\n"
                +"    अधिक माहिती साठी आमचा youtube विडिओ पाहावा  :\uD83D\uDC47\uD83C\uDFFB \uD83D\uDC47\uD83C\uDFFB \uD83D\uDC47\uD83C\uDFFB \n"+youtubelink+"\n\n\n" +
                "    तुमची प्रोफाइल  लिंक :\uD83D\uDC47\uD83C\uDFFB \uD83D\uDC47\uD83C\uDFFB \uD83D\uDC47\uD83C\uDFFB \n"+profileLink+"\n\n" +
                "\n" +
                "    आमचे अँप डाउनलोड करून तुम्ही भरपूर स्थळे पाहू शकता अगदी माफक दारात.\n" +
                "    अधिक माहिती साठी आम्हाला संपर्क करा : 7972864487";
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
        recyclerview = v.findViewById(R.id.recyclerView);

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

        followupdate = v.findViewById(R.id.contactdate);
        comment = v.findViewById(R.id.comments);
        savebtn = v.findViewById(R.id.savebtn);

        mobile1txt = v.findViewById(R.id.mobile1);
        mobile2txt = v.findViewById(R.id.mobile2);
        mobile3txt = v.findViewById(R.id.mobile3);
        mobile4txt = v.findViewById(R.id.mobile4);
    }

    private void sendSMS() {

        try {
            SmsManager sms = SmsManager.getDefault();
            PendingIntent sentPI;
            String SENT = "SMS_SENT";
            sentPI = PendingIntent.getBroadcast(activity.getApplicationContext(), 0,new Intent(SENT), PendingIntent.FLAG_IMMUTABLE);
            sms.sendTextMessage(mobile, null, message, sentPI, null);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "SMS sending failed", Toast.LENGTH_SHORT).show();
        }
    }


}
