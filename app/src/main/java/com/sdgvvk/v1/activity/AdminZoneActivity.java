package com.sdgvvk.v1.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sdgvvk.v1.LocalCache;
import com.sdgvvk.v1.ProjectConstants;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.api.HelperUtils;
import com.sdgvvk.v1.modal.FcmNotificationModal;
import com.sdgvvk.v1.modal.MembershipModal;
import com.sdgvvk.v1.modal.OrderModal;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminZoneActivity extends AppCompatActivity {

    private boolean lockAspectRatio = true, setBitmapMaxWidthHeight = false;
    private int ASPECT_RATIO_X = 3, ASPECT_RATIO_Y = 4, bitmapMaxWidth = 1000, bitmapMaxHeight = 1000;
    private int IMAGE_COMPRESSION = 80;
    public static Dialog pushnotificationDialog = null;
    public static String pushnotificationImage = null;
    public static final int PICK_IMAGE_REQUEST = 1;

    LinearLayout link1, link2, link3, link4, link5, link7, inputLayout;

    FloatingActionButton link6;

    CardView card1, card2, card7, card8, card9;

    static String searchBy = "";
    Activity activity;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminzone);
        activity = this;
        initUIElements();
        onclickListeners();
        ApiCallUtil.syncStats(this);
    }

    private void onclickListeners() {
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    ApiCallUtil.syncStats(this);
                    swipeRefreshLayout.setRefreshing(false);
                }
        );
        link1.setOnClickListener(view -> {
            HelperUtils.vibrateFunction(AdminZoneActivity.this);
            Intent intent = new Intent(AdminZoneActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        link2.setOnClickListener(view -> {
            Dialog d = new Dialog(activity);
            d.setContentView(R.layout.assign_membership_dialog);
            inputLayout = d.findViewById(R.id.inputLayout);

            handleAssignMembership(d, activity);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            d.show();

            /*Dexter.withActivity(activity)
                    .withPermissions(Manifest.permission.SEND_SMS)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                Dialog d = new Dialog(activity);
                                d.setContentView(R.layout.assign_membership_dialog);
                                inputLayout = d.findViewById(R.id.inputLayout);

                                handleAssignMembership(d, activity);
                                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                d.show();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();*/

        });

        link3.setOnClickListener(view -> {

            Intent intent = new Intent(AdminZoneActivity.this, ProfileExportActivity.class);
            startActivity(intent);
        });

        link4.setOnClickListener(view -> {
            HelperUtils.vibrateFunction(AdminZoneActivity.this);
            Intent intent = new Intent(AdminZoneActivity.this, FollowupActivity.class);
            startActivity(intent);
        });

        link5.setOnClickListener(view -> {
            callAllmembersActivity("distinctuserswithnotifications");
        });

        link6.setOnClickListener(view -> {
            Toast.makeText(AdminZoneActivity.this, "link copied", Toast.LENGTH_SHORT).show();
            String text = "अँप डाउनलोड लिंक : \nhttps://play.google.com/store/apps/details?id=com.sdgvvk.v1\n\nyoutube link : \nhttps://www.youtube.com/watch?v=Nzurh8PXEuM&t=2s";

            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Text Label", text);
            clipboard.setPrimaryClip(clip);

        });

        link7.setOnClickListener(view -> {
            handleAddPushNotificationFlow();
        });

        card1.setOnClickListener(view -> {
            callAllmembersActivity("allmale");
        });


        card2.setOnClickListener(view -> {
            callAllmembersActivity("allfemale");
        });


        card7.setOnClickListener(view -> {
            callAllmembersActivity("allpaidmembers");
        });

        card8.setOnClickListener(view -> {
            callAllmembersActivity("allnonpaidmembers");
        });


        card9.setOnClickListener(view -> {
            Dialog d = new Dialog(this);
            Activity activity = this;
            d.setContentView(R.layout.transactions_dialog);
            ((TextView) d.findViewById(R.id.title)).setText("Total Collection");
            ApiCallUtil.getAllTransactions(d, activity);
            //d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            d.show();
        });

    }

    private void handleAddPushNotificationFlow() {
        Dialog d = new Dialog(this);
        d.setContentView(R.layout.add_push_notification_dialog);
        AutoCompleteTextView notificationtype = d.findViewById(R.id.notificationtype);
        AutoCompleteTextView activitytype = d.findViewById(R.id.activity);
        AutoCompleteTextView filter = d.findViewById(R.id.filter);
        AutoCompleteTextView category = d.findViewById(R.id.category);
        TextInputEditText title = d.findViewById(R.id.title);
        TextInputEditText subtitle = d.findViewById(R.id.subtitle);
        TextInputEditText longtext = d.findViewById(R.id.longtext);
        Button saveBtn = d.findViewById(R.id.save);
        Button uploadimage = d.findViewById(R.id.uploadimage);

        notificationtype.setAdapter(new ArrayAdapter(this, R.layout.day_dropdown_list_item, new String[]{ProjectConstants.SIMPLE, ProjectConstants.EXPANDED, ProjectConstants.COLLAPSED}));
        activitytype.setAdapter(new ArrayAdapter(this, R.layout.day_dropdown_list_item, new String[]{ProjectConstants.MAIN_ACTIVITY, ProjectConstants.NOTIFICATION_ACTIVITY, ProjectConstants.LEVEL_2_PROFILE_ACTIVITY}));
        filter.setAdapter(new ArrayAdapter(this, R.layout.day_dropdown_list_item, new String[]{"all", "allmale", "allfemale", "allpaid", "allnonpaid", "allactive"}));
        category.setAdapter(new ArrayAdapter(this, R.layout.day_dropdown_list_item, new String[]{ProjectConstants.CATEGORY_SMALL_TEXT_DIALOG, ProjectConstants.CATEGORY_IMAGE_DIALOG, ProjectConstants.CATEGORY_LONG_TEXT_MESSAGE}));

        saveBtn.setOnClickListener(view1 -> {
            if (!(activitytype.getText().toString().isEmpty()
                    || title.getText().toString().isEmpty()
                    || subtitle.getText().toString().isEmpty()
                    || notificationtype.getText().toString().isEmpty()
                    || filter.getText().toString().isEmpty()
                    || category.getText().toString().isEmpty())) {
                d.dismiss();

                FcmNotificationModal fcmNotificationModal = new
                        FcmNotificationModal(title.getText().toString().trim(), subtitle.getText().toString().trim(), pushnotificationImage,
                        category.getText().toString().trim(), longtext.getText().toString().trim(), "NotificationActivity", notificationtype.getText().toString().trim(), "false", filter.getText().toString().trim());
                ApiCallUtil.addPushNotificationToServer(activity, fcmNotificationModal);
                Toast.makeText(AdminZoneActivity.this, "submitted...", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(AdminZoneActivity.this, "please fill all details", Toast.LENGTH_SHORT).show();


        });

        uploadimage.setOnClickListener(view12 -> {
            pushnotificationDialog = d;
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });


        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.show();
    }

    private void callAllmembersActivity(String key) {
        Log.i("ss_nw_call", "selected key : " + key);
        HelperUtils.vibrateFunction(AdminZoneActivity.this);
        Intent intent = new Intent(AdminZoneActivity.this, AllMemberActivity.class);
        intent.putExtra("key", key);
        intent.putExtra("key", key);
        startActivity(intent);
    }


    private void initUIElements() {
        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        link1 = findViewById(R.id.link1);
        link2 = findViewById(R.id.link2);
        link3 = findViewById(R.id.link3);
        link4 = findViewById(R.id.link4);
        link5 = findViewById(R.id.link5);
        link6 = findViewById(R.id.link6);
        link7 = findViewById(R.id.link7);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card7 = findViewById(R.id.card7);
        card8 = findViewById(R.id.card8);
        card9 = findViewById(R.id.card9);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApiCallUtil.syncStats(this);
    }

    private void handleAssignMembership(Dialog d, Activity activity) {
        CardView byName = d.findViewById(R.id.c1);
        CardView byMobile = d.findViewById(R.id.c2);
        CardView byId = d.findViewById(R.id.c3);
        EditText searchValue = d.findViewById(R.id.searchValue);
        Button searchBtn = d.findViewById(R.id.searchBtn);

        ((TextInputEditText) d.findViewById(R.id.txnDate)).setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        byName.setOnClickListener(view1 -> {
            inputLayout.setVisibility(View.VISIBLE);
            byName.setCardBackgroundColor(Color.YELLOW);
            byMobile.setCardBackgroundColor(Color.WHITE);
            byId.setCardBackgroundColor(Color.WHITE);
            searchValue.setInputType(InputType.TYPE_CLASS_TEXT);
            searchValue.setEnabled(true);
            searchValue.setText("");
            searchValue.setHint("Enter name");
            searchBy = "name";
            d.findViewById(R.id.downLayout).setVisibility(View.GONE);
            d.findViewById(R.id.errorTxt).setVisibility(View.GONE);
        });
        byMobile.setOnClickListener(view1 -> {
            inputLayout.setVisibility(View.VISIBLE);
            byName.setCardBackgroundColor(Color.WHITE);
            byMobile.setCardBackgroundColor(Color.YELLOW);
            byId.setCardBackgroundColor(Color.WHITE);
            searchValue.setInputType(InputType.TYPE_CLASS_NUMBER);
            searchValue.setEnabled(true);
            searchValue.setText("");
            searchValue.setHint("Enter mobile");
            searchBy = "mobile";
            d.findViewById(R.id.downLayout).setVisibility(View.GONE);
            d.findViewById(R.id.errorTxt).setVisibility(View.GONE);
        });
        byId.setOnClickListener(view1 -> {
            inputLayout.setVisibility(View.VISIBLE);
            byName.setCardBackgroundColor(Color.WHITE);
            byMobile.setCardBackgroundColor(Color.WHITE);
            byId.setCardBackgroundColor(Color.YELLOW);
            searchValue.setInputType(InputType.TYPE_CLASS_NUMBER);
            searchValue.setEnabled(true);
            searchValue.setText("");
            searchValue.setHint("Enter profile id");
            searchBy = "profile id";
            d.findViewById(R.id.downLayout).setVisibility(View.GONE);
            d.findViewById(R.id.errorTxt).setVisibility(View.GONE);
        });

        d.findViewById(R.id.searchBtn).setOnClickListener(view14 -> {
            String value = ((TextInputEditText) d.findViewById(R.id.searchValue)).getText().toString().trim();
            d.findViewById(R.id.searchBtn).setEnabled(false);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(d.getCurrentFocus().getWindowToken(), 0);
            ((TextView) d.findViewById(R.id.selectedProfileName)).setText("");
            ((TextView) d.findViewById(R.id.selectedProfileId)).setText(";");
            RecyclerView recyclerView = d.findViewById(R.id.searchResultListRecyclerView);
            LinearLayout downLayout = d.findViewById(R.id.downLayout);
            ApiCallUtil.searchProfilesBy(d, activity, searchBy, value, recyclerView, downLayout);
        });


        String[] paymentModeArray = {"Cash", "Google Pay", "Phone Pe", "Paytm"};
        ((AutoCompleteTextView) d.findViewById(R.id.paymentmode)).setAdapter(new ArrayAdapter(this, R.layout.day_dropdown_list_item, paymentModeArray));

        List<MembershipModal> membershiplist = LocalCache.getMembershipList(this);
        List<String> mlist = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        for (MembershipModal m : membershiplist) {
            mlist.add(m.getDescription() + "  -  Rs." + m.getFees() + "  -> (" + m.getCount() + " contacts)");
            map.put(m.getDescription() + "  -  Rs." + m.getFees() + "  -> (" + m.getCount() + " contacts)", m.getId());
        }
        ((AutoCompleteTextView) d.findViewById(R.id.selectplan)).setAdapter(new ArrayAdapter(this, R.layout.day_dropdown_list_item, mlist.toArray()));

        d.findViewById(R.id.txnDate).setOnClickListener(view1 -> {
            //Date Picker
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            MaterialDatePicker<Long> picker = builder.build();
            picker.show(getSupportFragmentManager(), picker.toString());
            picker.addOnPositiveButtonClickListener(selectedDate -> {
                Date date = new Date(selectedDate);
                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                ((TextInputEditText) d.findViewById(R.id.txnDate)).setText(simpleFormat.format(date));
            });

        });

        ((TextInputEditText) d.findViewById(R.id.txnDate)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                validateAssignMembershipForm(d);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        ((AutoCompleteTextView) d.findViewById(R.id.selectplan)).setOnItemClickListener((parent, arg1, pos, id) -> {
            String obj = (String) parent.getAdapter().getItem(pos);
            ((TextView) d.findViewById(R.id.membershipId)).setText(map.get(obj));
            validateAssignMembershipForm(d);

        });
        ((AutoCompleteTextView) d.findViewById(R.id.paymentmode)).setOnItemClickListener((parent, arg1, pos, id) -> {
            validateAssignMembershipForm(d);
        });

        d.findViewById(R.id.cancelBtn).setOnClickListener(view13 -> d.dismiss());

        d.findViewById(R.id.submitBtn).setOnClickListener(view14 -> {
            d.findViewById(R.id.submitBtn).setEnabled(false);


            // create order object
            String cpid = ((TextView) d.findViewById(R.id.selectedProfileId)).getText().toString().trim();
            String membershipId = ((TextView) d.findViewById(R.id.membershipId)).getText().toString().trim();
            String paymentmode = ((AutoCompleteTextView) d.findViewById(R.id.paymentmode)).getText().toString().trim();
            String txnDate = ((TextInputEditText) d.findViewById(R.id.txnDate)).getText().toString().trim();

            OrderModal o = new OrderModal(cpid, membershipId, paymentmode, txnDate);
            d.dismiss();
            ApiCallUtil.assignMembership(o, activity);
        });

        searchValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if (s.length() > 0)
                    searchBtn.setEnabled(true);
                else
                    searchBtn.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void validateAssignMembershipForm(Dialog d) {

        if (!((AutoCompleteTextView) d.findViewById(R.id.selectplan)).getText().toString().trim().isEmpty()
                && !((AutoCompleteTextView) d.findViewById(R.id.paymentmode)).getText().toString().trim().isEmpty()
                && !((TextInputEditText) d.findViewById(R.id.txnDate)).getText().toString().trim().isEmpty()
                && !((TextView) d.findViewById(R.id.selectedProfileName)).getText().toString().trim().isEmpty()
                && !((TextView) d.findViewById(R.id.selectedProfileId)).getText().toString().trim().isEmpty())
            d.findViewById(R.id.submitBtn).setEnabled(true);
        else
            d.findViewById(R.id.submitBtn).setEnabled(false);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {

                try {
                    Uri uri = data.getData();
                    cropImage(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri uri = UCrop.getOutput(data);

            if (uri != null) {
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    String imgB64 = HelperUtils.convertBitmapToBase64(bitmap, 500);


                    if (pushnotificationDialog != null) {
                        pushnotificationImage = imgB64;
                        Glide.with(this).load(uri.toString()).placeholder(R.drawable.oops).into((ImageView) pushnotificationDialog.findViewById(R.id.image));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Handle the case where the cropped image URI is null
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            // Handle the cropping error
            Throwable cropError = UCrop.getError(data);
        }
    }

    private void cropImage(Uri sourceUri) {

        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), queryName(getContentResolver(), sourceUri)));
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(IMAGE_COMPRESSION);

        // applying UI theme
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));

        if (lockAspectRatio)
            options.withAspectRatio(ASPECT_RATIO_X, ASPECT_RATIO_Y);

        if (setBitmapMaxWidthHeight)
            options.withMaxResultSize(bitmapMaxWidth, bitmapMaxHeight);

        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(this);
    }

    private static String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }


}
