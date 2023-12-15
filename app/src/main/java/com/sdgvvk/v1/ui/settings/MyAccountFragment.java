package com.sdgvvk.v1.ui.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdgvvk.v1.LocalCache;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.activity.ContactViewedActivity;
import com.sdgvvk.v1.activity.Level2ProfileActivity;
import com.sdgvvk.v1.activity.MyMembershipActivity;
import com.sdgvvk.v1.activity.RegistrationActivity;
import com.sdgvvk.v1.activity.SendOtpActivity;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.api.HelperUtils;
import com.sdgvvk.v1.livedata.UsersActivity;
import com.sdgvvk.v1.modal.Customer;
import com.sdgvvk.v1.modal.CustomerActivityModal;
import com.sdgvvk.v1.modal.OrderModal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class MyAccountFragment extends Fragment {

    private View view;
    Customer loggedincustomer;

    OrderModal activeOrder;

    static CoordinatorLayout coordinatorLayout;
    CardView cb_card, mymembership_card, adminzone_card, myaccount_profile_card, deactivate_card;
    LinearLayout adminzone_link, logoutId, cb_link, mymembership_link, editprofile_link, deactivate_link, users_testing;
    TextView profileHeadingName, profileHeadingmobile, profileHeadingEmail, profileCardId, cb_text;

    ImageView sprofilephoto;
    Activity activity;

    Button sendsms;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myaccountnew, container, false);
        activity = this.getActivity();
        HelperUtils.checkNetworkStatus(this.getActivity());

        try {
            String version = this.getActivity().getPackageManager().getPackageInfo(this.getActivity().getPackageName(), 0).versionName;
            ((TextView) view.findViewById(R.id.version)).setText("app version : v" + version);


            initUIElements();
            initOnClickListeners();
            syncLoggedInCustomer();

            loggedincustomer = LocalCache.getLoggedInCustomer(this.getActivity());
            activeOrder = LocalCache.getActiveOrder(this.getActivity());


            if (loggedincustomer != null) {
                adminzone_card.setVisibility(loggedincustomer.getIsAdmin() != null && loggedincustomer.getIsAdmin().equalsIgnoreCase("1") ? View.VISIBLE : View.GONE);
                initPageFields();
            }

            if (activeOrder != null && activeOrder.getId() != null) {
                mymembership_card.setVisibility(View.VISIBLE);
                cb_card.setVisibility(View.VISIBLE);
                cb_text.setText("Contact Balance : " + activeOrder.getCountRemaining());
            }

            ApiCallUtil.syncAccountBalance(loggedincustomer.getProfileId(), this.getActivity(), cb_card, mymembership_card, cb_text, true);

        } catch (Exception e) {
            Log.i("ss_nw_call", "ErrorAla : myaccountfragment" + e.getMessage());
        }


        return view;
    }

    private void initPageFields() {
        profileHeadingName.setText(loggedincustomer.getFirstname() + " " + loggedincustomer.getLastname());
        profileHeadingmobile.setText("+91 " + loggedincustomer.getMobile1());
        profileHeadingEmail.setText(loggedincustomer.getEmail());
        profileCardId.setText("Profile id : A" + loggedincustomer.getProfileId());
        Glide.with(this.getActivity())
                .load(loggedincustomer.getProfilephotoaddress() != null && loggedincustomer.getProfilephotoaddress() != "" ? loggedincustomer.getProfilephotoaddress() : R.drawable.oops)
                .placeholder(R.drawable.oops)
                .into(sprofilephoto);
    }

    private void initOnClickListeners() {
        sendsms.setOnClickListener(view -> {
            //ApiCallUtil.sendSmsFunction(activity,null);
            String imageUrl = "https://tavrostechinfo.com/assets/img/team/team-1.jpg";

// Download the image and save it locally (you can use a library like Picasso or Glide for this)
// For simplicity, I'm using a synchronous method here, which is not recommended on the main thread
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(new URL(imageUrl).openConnection().getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            File pictureFile = new File(activity.getExternalCacheDir(), "shared_image.jpg");
            try (FileOutputStream out = new FileOutputStream(pictureFile)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (IOException e) {
                e.printStackTrace();
            }

// Now create the intent and share the locally saved image
            Uri imgUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", pictureFile);
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("image/jpeg");

            whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
            whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
            whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            whatsappIntent.setPackage("com.whatsapp");

            try {
                activity.startActivity(whatsappIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                //ToastHelper.MakeShortText("WhatsApp is not installed.");
            }



        });
        myaccount_profile_card.setOnClickListener(view -> {
            //ApiCallUtil.getLevel2Data(loggedincustomer.getProfileId(), this.getActivity());
            activity.startActivity(new Intent(activity, Level2ProfileActivity.class)
                    .putExtra("level2data", loggedincustomer.getProfileId()));
        });
        editprofile_link.setOnClickListener(view -> {
            HelperUtils.vibrateFunction(this.getActivity());
            Intent intent = new Intent(this.getActivity(), RegistrationActivity.class);
            intent.putExtra("editprofile", true);
            startActivity(intent);
        });
        mymembership_link.setOnClickListener(view -> {
            HelperUtils.vibrateFunction(this.getActivity());
            Intent intent = new Intent(this.getActivity(), MyMembershipActivity.class);
            startActivity(intent);
        });
        adminzone_link.setOnClickListener(view -> {
            Dialog d = new Dialog(this.getActivity());
            d.setContentView(R.layout.admin_zone_access_dialog);
            d.findViewById(R.id.submitAdminCodeBtn).setOnClickListener(view1 -> {
                d.findViewById(R.id.submitAdminCodeBtn).setEnabled(false);
                String inputCode = ((TextInputEditText) d.findViewById(R.id.adminCodeField)).getText().toString().trim();
                if (inputCode.isEmpty()) {
                    d.findViewById(R.id.errorcode).setVisibility(View.VISIBLE);
                    ((TextView) d.findViewById(R.id.errorcode)).setText("Please entry pin");
                } else {
                    ApiCallUtil.validateAdminCode(d, inputCode, this.getActivity());
                }
            });

            ((TextInputEditText) d.findViewById(R.id.adminCodeField)).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    if (s.length() > 0) {
                        d.findViewById(R.id.submitAdminCodeBtn).setEnabled(true);
                        d.findViewById(R.id.errorcode).setVisibility(View.GONE);
                    } else {
                        d.findViewById(R.id.submitAdminCodeBtn).setEnabled(false);
                        d.findViewById(R.id.errorcode).setVisibility(View.VISIBLE);
                        ((TextView) d.findViewById(R.id.errorcode)).setText("Please entry pin");
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });


            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            d.show();
        });

        cb_link.setOnClickListener(view -> {
            HelperUtils.vibrateFunction(this.getActivity());
            Intent intent = new Intent(this.getActivity(), ContactViewedActivity.class);
            startActivity(intent);
        });

        logoutId.setOnClickListener(view -> {
            logoutId.setEnabled(false);Log.i("ss_nw_call", new Date() + " api call : logout started");
            FirebaseAuth.getInstance().signOut();
            handleLogout();
        });

        users_testing.setOnClickListener(view -> {
            Intent intent = new Intent(this.getActivity(), UsersActivity.class);
            startActivity(intent);
        });


        deactivate_link.setOnClickListener(view -> {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Toast.makeText(activity, "Account deletion in progress...", Toast.LENGTH_SHORT).show();
                        ApiCallUtil.deleteProfile(activity, loggedincustomer.getProfileId());
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder
                    .setMessage("Are you sure want to deactivate account ?")
                    .setPositiveButton("yes", dialogClickListener)
                    .setNegativeButton("no", dialogClickListener).show();
        });


    }


    private void initUIElements() {
        sendsms = view.findViewById(R.id.sendsms);
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        profileHeadingName = view.findViewById(R.id.profileHeadingName);
        profileHeadingmobile = view.findViewById(R.id.profileHeadingmobile);
        profileHeadingEmail = view.findViewById(R.id.profileHeadingEmail);
        profileCardId = view.findViewById(R.id.profileCardId);
        adminzone_card = view.findViewById(R.id.adminzone_card);
        deactivate_card = view.findViewById(R.id.deactivate_card);
        myaccount_profile_card = view.findViewById(R.id.myaccount_profile_card);

        sprofilephoto = view.findViewById(R.id.sprofilephoto);
        adminzone_link = view.findViewById(R.id.adminzone_link);
        mymembership_link = view.findViewById(R.id.mymembership_link);
        editprofile_link = view.findViewById(R.id.editprofile_link);
        deactivate_link = view.findViewById(R.id.deactivate_link);


        cb_link = view.findViewById(R.id.cb_link);
        cb_card = view.findViewById(R.id.cb_card);
        mymembership_card = view.findViewById(R.id.mymembership_card);

        cb_text = view.findViewById(R.id.cb_text);


        logoutId = view.findViewById(R.id.logoutId);
        users_testing = view.findViewById(R.id.users_testing);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void handleLogout() {
        if (HelperUtils.isConnected(this.getActivity())) {

            LocalCache.setLoggedInCustomer(new Customer(), this.getActivity());
            LocalCache.setCustomerActivity(new CustomerActivityModal(), activity);
            LocalCache.setActiveOrder(new OrderModal(), this.getActivity());
            LocalCache.setLevel1List(new ArrayList<>(), this.getActivity());
            LocalCache.setContactViewedList(new ArrayList<>(), this.getActivity());
            LocalCache.setMembershipList(new ArrayList<>(), this.getActivity());
            LocalCache.setGenderStat(new ArrayList<>(), this.getActivity());
            LocalCache.setAdminPhone("", this.getActivity());
            LocalCache.setContactviewedMatchesList(true, activity);
            LocalCache.setLikedMatchesList(true, activity);
            LocalCache.setShortlistedMatchesList(true, activity);
            LocalCache.setSentinterestMatchesList(true, activity);

            Intent intent = new Intent(this.getActivity(), SendOtpActivity.class);
            intent.putExtra("logout", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void syncLoggedInCustomer() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            ApiCallUtil.updateLoggedInCustomerDetails(this.getActivity(), user.getPhoneNumber().replace("+91", ""));
        }
    }

    public static void showSnackBar(String content) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, content, Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
        snackbar.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ApiCallUtil.syncAccountBalance(loggedincustomer.getProfileId(), this.getActivity(), cb_card, mymembership_card, cb_text, true);

        if (ApiCallUtil.editedCustomerProfile != null && loggedincustomer != null) {
            if (ApiCallUtil.editedCustomerProfile.getMobile1().equalsIgnoreCase(loggedincustomer.getMobile1())) {
                profileHeadingName.setText(ApiCallUtil.editedCustomerProfile.getFirstname()+" "+ApiCallUtil.editedCustomerProfile.getLastname());
                profileHeadingEmail.setText(ApiCallUtil.editedCustomerProfile.getEmail());
                profileHeadingmobile.setText(ApiCallUtil.editedCustomerProfile.getMobile1());
                LocalCache.setLoggedInCustomer(ApiCallUtil.editedCustomerProfile,this.getActivity());
                ApiCallUtil.editedCustomerProfile = null;
            }
        }
    }


}