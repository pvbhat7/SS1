package com.sdgvvk.v1.api;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.sdgvvk.v1.LocalCache;
import com.sdgvvk.v1.MainActivity;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.activity.AdminZoneActivity;
import com.sdgvvk.v1.activity.AllMemberActivity;
import com.sdgvvk.v1.activity.Level2ProfileActivity;
import com.sdgvvk.v1.activity.ProfileExportActivity;
import com.sdgvvk.v1.activity.SendOtpActivity;
import com.sdgvvk.v1.activity.VerifyOtpActivity;
import com.sdgvvk.v1.adapters.AllMembersAdapter;
import com.sdgvvk.v1.adapters.BuyMembershipAdapter;
import com.sdgvvk.v1.adapters.ContactViewedAdapter;
import com.sdgvvk.v1.adapters.FollowUpAdapter;
import com.sdgvvk.v1.adapters.MyMembershipAdapter;
import com.sdgvvk.v1.adapters.NotificationAdapter;
import com.sdgvvk.v1.adapters.SearchedMembersAdapter;
import com.sdgvvk.v1.adapters.TransactionAdapter;
import com.sdgvvk.v1.modal.BitmapDataModal;
import com.sdgvvk.v1.modal.ContactViewedModal;
import com.sdgvvk.v1.modal.Customer;
import com.sdgvvk.v1.modal.CustomerActivityModal;
import com.sdgvvk.v1.modal.FcmNotificationModal;
import com.sdgvvk.v1.modal.FcmTokenModal;
import com.sdgvvk.v1.modal.FilterModal;
import com.sdgvvk.v1.modal.FollowUpModal;
import com.sdgvvk.v1.modal.Level_1_cardModal;
import com.sdgvvk.v1.modal.Level_2_Modal;
import com.sdgvvk.v1.modal.MembershipModal;
import com.sdgvvk.v1.modal.MyMembershipModal;
import com.sdgvvk.v1.modal.NotificationModal;
import com.sdgvvk.v1.modal.OrderModal;
import com.sdgvvk.v1.modal.SingleResponse;
import com.sdgvvk.v1.modal.SmsModal;
import com.sdgvvk.v1.modal.Stat;
import com.sdgvvk.v1.modal.TransactionModal;
import com.sdgvvk.v1.ui.ViewedContactBottomSheetDialog;
import com.sdgvvk.v1.ui.home.HomeFragment;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ApiCallUtil {

    public static Boolean redirected_via_home_screen_noti = false;
    public static Boolean redirected_via_deep_link = false;
    public static Boolean redirected_via_notification = false;
    public static String noti_target_class = null;
    public static String noti_target_cpid = null;

    public static String noti_category= null;
    public static String noti_longtext= null;

    public static String noti_title = null;
    public static String noti_message = null;
    public static String noti_image = null;
    public static boolean logout_initiated = false;


    static Boolean educationApiCalled = false, occupationApiCalled = false , lastnamesApiCalled = false , cityApiCalled = false , casteApiCalled = false;

    public static int counter = 0;
    public static Boolean clicked_level2activity = false;

    public static List<BitmapDataModal> blist = new ArrayList<>();
    public static Dialog onboardDialog = null;
    public static String b64 = null;
    public static String cpid = null;
    public static Customer editedCustomerProfile = null;


    // get level 1 data
    public static void getAllProfiles(String loggedInCpid, Fragment fragment, SpinKitView progressBar, Activity activity, Boolean override) {
        new GetAllCustomerProfilesTask(loggedInCpid, fragment, progressBar, activity, override).execute();
    }

    public static void setLoggedInCustomer(Activity activity, String mobile, SpinKitView progressBar) {
        new SetLoggedInCustomerTask(activity, mobile, progressBar).execute();
    }

    public static void updateLoggedInCustomerDetails(Activity activity, String mobile) {
        new UpdateLoggedInCustomerDetailsTask(activity, mobile).execute();
    }

    public static void getUserNotifications(Activity activity, Dialog dialog, String cpid, Boolean showSingleNotification) {
        new GetUserNotificationsListTask(activity, dialog, cpid , showSingleNotification).execute();
    }

    public static void getDistinctUsers(Activity activity, RecyclerView recyclerView, AllMembersAdapter adapter) {
        new GetDistinctUserNotificationsTask(activity, recyclerView, adapter).execute();
    }

    public static void updateViewedNotificationState(String noti_id) {
        new UpdateViewedNotificationStateTask(noti_id).execute();
    }

    public static void addLeads(String cpid , String vcpid , String type) {
        new AddLeadsTask(cpid , vcpid , type).execute();
    }

    public static void getContactViewedProfiles(String cpid, ContactViewedAdapter adapter, RecyclerView contactviewedRecyclerView, TextView cv_count,TextView cv_zero, Activity activity) {
        new GetContactViewedProfilesTask(cpid, adapter, contactviewedRecyclerView, cv_count,cv_zero, activity).execute();
    }

    public static void getFollowupByCpid(String cpid, RecyclerView recyclerView, Activity activity) {
        new GetFollowupsTask(cpid, recyclerView, activity).execute();
    }

    public static void registerProfile(Customer customer, Activity activity, Boolean onboardNewUser, Fragment fragment, Boolean isAdmin) {
        new RegisterNewCustomerTask(customer, activity, onboardNewUser, fragment , isAdmin).execute();
    }

    public static void updateProfile(Customer customer, Activity activity, Boolean updateCache, Boolean forceupdate) {
        new UpdateProfileTask(customer, activity, updateCache, forceupdate).execute();
    }

    public static void validateLoginMobile(Activity activity, String mobile, LinearLayout formLayout, LinearLayout cmLayout, TextInputEditText mobile1, ExtendedFloatingActionButton savebtn) {
        new ValidateLoginMobileTask(activity, mobile, formLayout, cmLayout, mobile1, savebtn).execute();
    }

    public static void validateAdminCode(Dialog d, String inputCode, Activity activity) {
        new ValidateAdminCodeTask(d, inputCode, activity).execute();
    }

    public static void isLive(Activity activity) {
        new CheckIsLiveTask(activity).execute();
    }

    public static void syncStats(Activity activity) {
        new SyncStatsTask(activity).execute();
    }



    public static void searchLevel1Profiles(Activity activity, Fragment fragment, FilterModal modal , Dialog d) {
        new SearchLevel1DataTask(activity, fragment, modal, d ).execute();
    }

    public static void searchSingleProfile(Activity activity, Fragment fragment,FilterModal modal , Dialog d) {
        new SearchSingleProfileTask(activity, fragment, modal, d ).execute();
    }

    public static void getFilteredLevel2Profiles(Activity activity, FilterModal modal) {
        new GetFilteredLevel2DataTask(activity, modal).execute();
    }

    public static void getMyMemberships(String cpid, RecyclerView recyclerView, Activity activity) {
        new GetMyMembershipsTask(cpid, activity, recyclerView).execute();
    }

    public static void getAdminPhone(Activity activity) {
        new GetAdminPhoneTask(activity).execute();
    }

    public static void getMembershipPlans(RecyclerView recyclerView, Activity activity) {
        new GetMembershipPlansTask(activity, recyclerView ).execute();
    }

    public static void searchProfilesBy(Dialog d, Activity activity, String searchBy, String value, RecyclerView recyclerView, LinearLayout downLayout) {
        new SearchProfileTask(d, activity, searchBy, value, recyclerView, downLayout).execute();
    }

    public static void assignMembership(OrderModal o, Activity activity) {
        new AssignMembershipTask(o, activity).execute();
    }

    public static void dynamicLayoutCreation(Activity activity, List<Customer> list, View v) {
        ApiCallUtil.counter = 0;
        ApiCallUtil.blist = new ArrayList<>();
        new DynamicLayoutCreationTask(activity, list, v).execute();
    }

    public static void persistBitmap(Activity activity) {
        new PersistBitmapTask(activity).execute();
    }


    public static void disableProfile(Activity activity, String vcpid) {
        new DisableProfileTask(activity, vcpid).execute();
    }

    public static void CheckAccountStatus(SendOtpActivity activity, String mobile) {
        new CheckAccountStatusTask(activity, mobile).execute();
    }

    public static void getAllTransactions(Dialog d, Activity activity) {
        new GetAllTransactionsTask(d, activity).execute();
    }

    public static void SaveFollowUp(String profileId, String followupdate, String comment) {
        new SaveFollowUpTask(profileId, followupdate, comment).execute();
    }

    public static void updateFollowUp(String noti_id) {
        new UpdateFollowupTask(noti_id).execute();
    }

    public static void sendTokenToServer(FcmTokenModal tokenModal) {
        new CreateNewFcmTokenTask(tokenModal).execute();
    }

    public static void addPushNotificationToServer(Activity activity, FcmNotificationModal fcmNotificationModal) {
        new AddPushNotificationToServerTask(activity,fcmNotificationModal).execute();
    }

    public static void getAdminNotice(Activity activity, Customer loggedinCustomer) {
        new FetchAdminNoticesTask(activity,loggedinCustomer).execute();
    }

    public static void syncAccountBalanceForHomeScreen(String profileId, Activity activity, ExtendedFloatingActionButton balanceBtn) {
        new SyncAccountBalanceForHomeScreenTask(profileId,activity,balanceBtn).execute();
    }

    public static void getAllViewedContacts(String cpid, Dialog d, Activity activity) {
        new GetAdminAllViewedContactsTask(cpid,d,activity).execute();
    }

    static class CreateNewFcmTokenTask extends AsyncTask<Void, Void, Void> {

        FcmTokenModal tokenModal;

        public CreateNewFcmTokenTask(FcmTokenModal tokenModal) {
            this.tokenModal = tokenModal;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                RetrofitClient.getInstance().getApi().createFcmTokenToServer(tokenModal).execute();
            } catch (IOException e) {
                System.out.println("");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    static class GetAdminAllViewedContactsTask extends AsyncTask<Void, Void, Void> {

        String cpid;
        Dialog d;
        Activity activity;
        List<ContactViewedModal> list ;

        public GetAdminAllViewedContactsTask(String cpid, Dialog d, Activity activity) {
            this.cpid = cpid;
            this.d = d;
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
               list = RetrofitClient.getInstance().getApi().getContactViewedProfiles(cpid).execute().body();
            } catch (IOException e) {
                System.out.println("");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(list != null && !list.isEmpty()){
                ContactViewedAdapter adapter = new ContactViewedAdapter(list, activity);
                RecyclerView recyclerView = d.findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                recyclerView.setAdapter(adapter);
            }
        }
    }

    static class AddPushNotificationToServerTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        FcmNotificationModal fcmNotificationModal ;
        public AddPushNotificationToServerTask(Activity activity, FcmNotificationModal fcmNotificationModal) {
            this.activity = activity ;
            this.fcmNotificationModal = fcmNotificationModal;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                RetrofitClient.getInstance().getApi().createFcmNotificationToServer(fcmNotificationModal).execute();
            } catch (IOException e) {
                System.out.println("");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    static class FetchAdminNoticesTask extends AsyncTask<Void, Void, Void> {

        String noticeType ;
        Activity activity;
        Customer customer;

        SingleResponse response;
        public FetchAdminNoticesTask(Activity activity, Customer customer) {
            this.activity = activity ;
            this.customer = customer;
            this.noticeType = customer.getIsNoticeAvailable();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                response = RetrofitClient.getInstance().getApi().getAdminNotice(noticeType, customer.getProfileId()).execute().body();
            } catch (IOException e) {
                System.out.println("");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (response != null && !response.getResult().equalsIgnoreCase("0")) {
                Dialog d = new Dialog(activity);
                if(noticeType.equalsIgnoreCase("custom") || noticeType.equalsIgnoreCase("longtext")){
                    d.setContentView(R.layout.notification_text_dialog);
                    ((TextView)d.findViewById(R.id.longtext)).setText(response.getResult());
                }
                else if(noticeType.equalsIgnoreCase("image")){
                    d.setContentView(R.layout.notification_image_dialog);
                    Glide.with(activity)
                            .load(response.getResult())
                            .placeholder(R.drawable.oops)
                            .into((ImageView) d.findViewById(R.id.img));
                }
                d.setCanceledOnTouchOutside(true);
                d.setCancelable(true);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
            }


        }
    }

    static class SyncAccountBalanceForHomeScreenTask extends AsyncTask<Void, Void, Void> {

        String profileId ;
        ExtendedFloatingActionButton button;
        Activity activity;
        List<OrderModal> activeOrder;

        public SyncAccountBalanceForHomeScreenTask(String profileId , Activity activity, ExtendedFloatingActionButton button) {
            this.activity = activity ;
            this.profileId = profileId;
            this.button = button;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                activeOrder = RetrofitClient.getInstance().getApi().getActiveOrderByCpid(profileId).execute().body();
            } catch (IOException e) {
                System.out.println("");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (activeOrder != null && !activeOrder.isEmpty()) {
                LocalCache.setActiveOrder(activeOrder.get(0), activity);
                OrderModal activeOrder = LocalCache.getActiveOrder(activity);
                if (activeOrder != null && activeOrder.getId() != null) {
                    button.setVisibility(View.VISIBLE);
                    button.setText("Balance : "+activeOrder.getCountRemaining());
                }
                else
                    button.setVisibility(View.GONE);
            }
            else
                button.setVisibility(View.GONE);


        }
    }


    static class SaveFollowUpTask extends AsyncTask<Void, Void, Void> {

        String profileId;
        String followupdate;
        String comment;

        public SaveFollowUpTask(String profileId, String followupdate, String comment) {
            this.profileId = profileId;
            this.followupdate = followupdate;
            this.comment = comment;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : SaveFollowUpTask");
            try {
                RetrofitClient.getInstance().getApi().saveFollowUp(profileId, followupdate, comment).execute().body();
            } catch (Exception e) {
                Log.i("ss_nw_call", "nw error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "onPostExecute ");
            super.onPostExecute(aVoid);
        }
    }

    static class CheckAccountStatusTask extends AsyncTask<Void, Void, Void> {

        SendOtpActivity activity;
        SingleResponse obj;
        Boolean account_deactivated;
        String mobile;

        public CheckAccountStatusTask(SendOtpActivity activity, String mobile) {
            this.activity = activity;
            this.mobile = mobile;
            this.account_deactivated = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : CheckAccountStatusTask");
            try {
                obj = RetrofitClient.getInstance().getApi().checkAccountStatus(mobile).execute().body();
            } catch (Exception e) {
                Log.i("ss_nw_call", "nw error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "onPostExecute ");
            super.onPostExecute(aVoid);
            if (obj != null && obj.getResult().equalsIgnoreCase("true"))
                account_deactivated = true;

            if (!account_deactivated) {

                HelperUtils.vibrateFunction(activity);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + mobile,
                        10,
                        TimeUnit.SECONDS,
                        activity,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                //progressBar.setVisibility(View.GONE);
                                //hideProgressBar();
                                //buttonGetOtp.setVisibility(View.VISIBLE);
                                activity.signInWIthPhoneAuthCredentials(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                //progressBar.setVisibility(View.GONE);
                                //hideProgressBar();
                                //buttonGetOtp.setVisibility(View.VISIBLE);
                                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                //progressBar.setVisibility(View.GONE);
                                activity.hideProgressBar();
                                //buttonGetOtp.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(activity, VerifyOtpActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("mobile", mobile);
                                intent.putExtra("verificationId", verificationId);
                                //buttonGetOtp.setVisibility(View.VISIBLE);
                                activity.startActivity(intent);
                            }
                        }
                );
            } else {
                activity.findViewById(R.id.loginbox).setVisibility(View.GONE);
                activity.findViewById(R.id.errorbox).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.contactsupportbtn).setVisibility(View.VISIBLE);
                ((TextView) activity.findViewById(R.id.errorTxt)).setText("Account deactivated");
            }
        }
    }

    static class GetAllTransactionsTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        Dialog d;
        List<TransactionModal> txnList;

        public GetAllTransactionsTask(Dialog d, Activity activity) {
            this.activity = activity;
            this.d = d;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : GetAllTransactionsTask");
            try {
                txnList = RetrofitClient.getInstance().getApi().getAllTransactions().execute().body();
            } catch (Exception e) {
                Log.i("ss_nw_call", "nw error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "onPostExecute ");
            super.onPostExecute(aVoid);
            if (txnList != null && !txnList.isEmpty()) {
                // update in recyclerview
                TransactionAdapter adapter = new TransactionAdapter(txnList, activity, d);
                RecyclerView recyclerView = d.findViewById(R.id.recyclerView);
                if(recyclerView != null ){
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    recyclerView.setAdapter(adapter);
                }
            }
        }
    }

    static class DisableProfileTask extends AsyncTask<Void, Void, Void> {

        String vcpid;
        Activity activity;

        public DisableProfileTask(Activity activity, String vcpid) {
            this.vcpid = vcpid;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : DisableProfileTask");
            try {
                RetrofitClient.getInstance().getApi().disableProfile(vcpid).execute();
            } catch (Exception e) {
                Log.i("ss_nw_call", "nw error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "onPostExecute ");
            super.onPostExecute(aVoid);
        }
    }

    public static void deleteProfile(Activity activity, String vcpid) {
        new DeleteProfileTask(activity, vcpid).execute();
    }

    static class DeleteProfileTask extends AsyncTask<Void, Void, Void> {

        String vcpid;
        Activity activity;

        public DeleteProfileTask(Activity activity, String vcpid) {
            this.vcpid = vcpid;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : DeleteProfileTask");
            try {
                RetrofitClient.getInstance().getApi().deleteProfile(vcpid).execute();
            } catch (Exception e) {
                Log.i("ss_nw_call", "nw error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "onPostExecute ");
            super.onPostExecute(aVoid);
            if (HelperUtils.isConnected(activity)) {
                LocalCache.setLoggedInCustomer(new Customer(), activity);
                LocalCache.setCustomerActivity(new CustomerActivityModal(), activity);
                LocalCache.setActiveOrder(new OrderModal(), activity);
                LocalCache.setLevel1List(new ArrayList<>(), activity);
                LocalCache.setContactViewedList(new ArrayList<>(), activity);
                LocalCache.setMembershipList(new ArrayList<>(), activity);
                LocalCache.setGenderStat(new ArrayList<>(), activity);
                LocalCache.setIsLive("Account deactivated", activity);
                LocalCache.setContactviewedMatchesList(true, activity);
                LocalCache.setLikedMatchesList(true, activity);
                LocalCache.setShortlistedMatchesList(true, activity);
                LocalCache.setSentinterestMatchesList(true, activity);
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(activity, SendOtpActivity.class);
                intent.putExtra("logout", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
            }
        }
    }

    static class GetAllCustomerProfilesTask extends AsyncTask<Void, Void, Void> {

        Fragment fragment;
        SpinKitView progressBar;

        String loggedInCpid;
        Circle d = new Circle();
        List<Level_1_cardModal> list = new ArrayList<>();
        Boolean override;
        Activity activity;

        public GetAllCustomerProfilesTask(String loggedInCpid, Fragment fragment, SpinKitView progressBar, Activity activity, Boolean override) {
            this.fragment = fragment;
            this.progressBar = progressBar;
            this.loggedInCpid = loggedInCpid;
            this.activity = activity;
            this.override = override;
        }

        @Override
        protected void onPreExecute() {

            progressBar.setIndeterminateDrawable(d);
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : GetAllCustomerProfilesTask");
            try {
                list = RetrofitClient.getInstance().getApi().getAllCustomerProfiles(loggedInCpid).execute().body();

            } catch (Exception e) {
                Log.i("ss_nw_call", "nw error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "onPostExecute ");
            super.onPostExecute(aVoid);
            if (list != null && !list.isEmpty()) {
                List<Level_1_cardModal> newList = new ArrayList<>();
                newList = list.stream()
                        .distinct()
                        .collect(Collectors.toList());
                //Collections.shuffle(list);

                if(LocalCache.getLevel1List(activity) != null && !LocalCache.getLevel1List(activity).isEmpty())
                    newList = checkAndUpdateLikes(newList,activity);


                if (LocalCache.getLevel1List(activity).isEmpty())
                    ((HomeFragment) fragment).initLevel_1_CardProfilesRecyclerView(newList);


                if (override)
                    ((HomeFragment) fragment).initLevel_1_CardProfilesRecyclerView(newList);


                LocalCache.setLevel1List(newList, activity);

                d.stop();
                progressBar.setVisibility(View.GONE);
            } else {
                Log.i("onPostExecute", "list is null");
                LocalCache.setLevel1List(new ArrayList<>(), activity);
                if (override)
                    ((HomeFragment) fragment).initLevel_1_CardProfilesRecyclerView(list);
            }

            d.stop();
            progressBar.setVisibility(View.GONE);
        }

        private List<Level_1_cardModal> checkAndUpdateLikes(List<Level_1_cardModal> newList, Activity activity) {
            List<Level_1_cardModal> updatedList  = new ArrayList<>() ;
            List<Level_1_cardModal> cachedList = LocalCache.getLevel1List(activity);

            if (cachedList != null && !cachedList.isEmpty()) {
                updatedList = newList.stream()
                        .map(newItem -> {
                            Level_1_cardModal cachedItem = findItemById(cachedList, newItem.getProfileId());

                            if (cachedItem != null) {
                                // Compare and update properties if needed
                                if (shouldUpdate(newItem.getIsLiked(), cachedItem.getIsLiked())) {
                                    newItem.setIsLiked(cachedItem.getIsLiked());
                                }
                                if (shouldUpdate(newItem.getIsShortlisted(), cachedItem.getIsShortlisted())) {
                                    newItem.setIsShortlisted(cachedItem.getIsShortlisted());
                                }
                                if (shouldUpdate(newItem.getIsInterestsent(), cachedItem.getIsInterestsent())) {
                                    newItem.setIsInterestsent(cachedItem.getIsInterestsent());
                                }

                                // Add more comparisons for other properties if needed

                                return newItem;
                            } else {
                                return newItem;
                            }
                        })
                        .collect(Collectors.toList());

                // TODO: Use the updatedList as needed
            }
            return updatedList;
        }

        // Utility method to find an item by profile ID
        private Level_1_cardModal findItemById(List<Level_1_cardModal> list, String profileId) {
            return list.stream()
                    .filter(item -> item.getProfileId().equals(profileId))
                    .findFirst()
                    .orElse(null);
        }

        // Utility method to check if the property should be updated
        private boolean shouldUpdate(String newValue, String cachedValue) {
            // Implement your logic here, e.g., if the values are not equal
            return !newValue.equals(cachedValue);
        }
    }



    public static void syncUserActivity(String cpid, Activity activity) {
        new SyncUserActivityTask(cpid, activity).execute();
    }



    static class SyncUserActivityTask extends AsyncTask<Void, Void, Void> {

        String cpid;

        Activity activity;
        List<CustomerActivityModal> activityModal = new ArrayList<>();


        public SyncUserActivityTask(String cpid, Activity activity) {
            this.cpid = cpid;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : SyncUserActivityTask");
            try {
                activityModal = RetrofitClient.getInstance().getApi().getActivityData(cpid).execute().body();

            } catch (Exception e) {
                Log.i("ss_nw_call", "nw error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "onPostExecute ");
            super.onPostExecute(aVoid);
            //HelperUtility.addLastTenDaysCount(list, activity);
            if(activityModal != null && !activityModal.isEmpty()){
                CustomerActivityModal modal = activityModal.get(0);
                LocalCache.setCustomerActivity(modal,activity);
            }
        }
    }

    // get level 2 data

    public static void getLevel2Data(String cpid, Activity activity) {
        HelperUtils.vibrateFunction(activity);
        new GetLevel2DataTask(cpid, activity).execute();
    }

    static class GetLevel2DataTask extends AsyncTask<Void, Void, Void> {

        String cpid;
        Activity activity;

        List<Level_2_Modal> list = new ArrayList<>();
        List<ContactViewedModal> contactviewedlist = new ArrayList<>();


        public GetLevel2DataTask(String cpid, Activity activity) {
            this.cpid = cpid;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            Log.i("ss_nw_call", "performance : GetLevel2DataTask preexecute ");
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : GetLevel2DataTask");
            try {
                Log.i("ss_nw_call", "performance : GetLevel2DataTask doinbackground api calling start ");
                list = RetrofitClient.getInstance().getApi().getLevel2DataByCPID(cpid).execute().body();
                Log.i("ss_nw_call", "performance : GetLevel2DataTask doinbackground api calling end ");
                // TODO: 14-Sep-23 prepare single rest api
                //Customer c = LocalCache.getLoggedInCustomer(activity);
                //contactviewedlist = RetrofitClient.getInstance().getApi().getContactViewedProfiles(c.getProfileId()).execute().body();


            } catch (Exception e) {
                Log.i("ss_nw_call", "GetLevel2DataTask error" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "GetLevel2DataTask onPostExecute calling... ");
            Log.i("ss_nw_call", "performance : GetLevel2DataTask onPostExecute ");
            super.onPostExecute(aVoid);
            if (list != null && !list.isEmpty()) {


                // set iscontactViewed
                list.get(0).setContactViewed(false);
                contactviewedlist = LocalCache.getContactViewedList(activity);
                if (contactviewedlist != null && contactviewedlist.size() > 0) {
                    for (ContactViewedModal modal : contactviewedlist) {
                        if (modal.getVcpid().equalsIgnoreCase(cpid))
                            list.get(0).setContactViewed(true);
                    }
                }

                ((Level2ProfileActivity)activity).initByLevel2Object(list.get(0));

                Log.i("ss_nw_call", "GetLevel2DataTask onPostExecute list size  " + list.size() + " time is " );
                //activity.startActivity(new Intent(activity, Level2ProfileActivity.class).putExtra("level2data", new Gson().toJson(list.get(0))));


            } else
                Log.i("ss_nw_call", "GetLevel2DataTask onPostExecute list is null  time is " );
        }
    }

    public static void viewContactData(String cpid, Level_2_Modal vcpid, Activity activity) {
        new ViewContactDataTask(cpid, vcpid, activity).execute();
    }

    static class ViewContactDataTask extends AsyncTask<Void, Void, Void> {

        String cpid;

        Level_2_Modal vcpid;

        SingleResponse response;
        Activity activity;
        String mobile;

        public ViewContactDataTask(String cpid, Level_2_Modal vcpid, Activity activity) {
            this.cpid = cpid;
            this.vcpid = vcpid;
            this.activity = activity;
            this.mobile = LocalCache.getLoggedInCustomer(activity).getMobile1();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : ViewContactDataTask");
            try {
                // check if package exists or not
                response = RetrofitClient.getInstance().getApi().viewContactData(cpid, vcpid.getProfileId()).execute().body();
                if (response != null && response.getResult() != null && response.getResult().equalsIgnoreCase("true")) {
                    syncAccountBalance(cpid, activity, null, null, null, false);
                    updateLoggedInCustomerDetails(activity, mobile);

                    // update view contact data list here
                    List<Level_1_cardModal> list = LocalCache.getLevel1List(activity);
                    for(Level_1_cardModal o : list){
                        if(o.getProfileId().equalsIgnoreCase(vcpid.getProfileId())){
                            o.setIsViewed("1");
                            LocalCache.updateLevel1List(o,activity,false);
                            break;
                        }
                    }
                    LocalCache.setContactviewedMatchesList(false,activity);
                }

            } catch (Exception e) {
                Log.i("ss_nw_call", "UpdateViewCountTask error" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "UpdateViewCountTask onPostExecute calling... ");
            super.onPostExecute(aVoid);
            if (response != null && response.getResult() != null && response.getResult().equalsIgnoreCase("true")) {
                activity.findViewById(R.id.viewContactDetailsBtn).setEnabled(false);
                activity.findViewById(R.id.viewContactDetailsBtn).setVisibility(View.GONE);

                if(vcpid.getIsDummy() != null && vcpid.getIsDummy().equalsIgnoreCase("yes")){
                    activity.findViewById(R.id.marriage_card).setVisibility(View.VISIBLE);
                }
                else{
                    new ViewedContactBottomSheetDialog(activity,vcpid).show(((Level2ProfileActivity)activity).getSupportFragmentManager(), "ViewedContactBottomSheetDialog");

                    //activity.findViewById(R.id.contact_card).setVisibility(View.VISIBLE);
                    ((TextView) activity.findViewById(R.id.address)).setText(vcpid.getAddress());
                    ((TextView) activity.findViewById(R.id.mobile1)).setText(vcpid.getMobile1());
                    ((TextView) activity.findViewById(R.id.mobile2)).setText(vcpid.getMobile2());
                    ((TextView) activity.findViewById(R.id.mobile3)).setText(vcpid.getMobile3());
                    ((TextView) activity.findViewById(R.id.email)).setText(vcpid.getEmail());
                    MediaPlayer.create(activity, R.raw.done_sound).start();
                }

            } else {
                ApiCallUtil.addLeads(cpid,vcpid.getProfileId(),"clicked_view_contact_button");
                Level2ProfileActivity.showBuyMembershipBottomSheet();
            }

        }
    }

    public static void syncAccountBalance(String cpid, Activity activity, CardView cb_card, CardView membership_card, TextView cb_text, Boolean flag) {
        new SyncAccountBalanceTask(cpid, activity, cb_card, membership_card, cb_text, flag).execute();
    }

    static class SyncAccountBalanceTask extends AsyncTask<Void, Void, Void> {

        String cpid;
        List<OrderModal> activeOrder;

        Activity activity;
        CardView cb_card;
        CardView membership_card;
        TextView cb_text;
        Boolean flag;

        public SyncAccountBalanceTask(String cpid, Activity activity, CardView cb_card, CardView membership_card, TextView cb_text, Boolean flag) {
            this.cpid = cpid;
            this.activity = activity;
            this.cb_card = cb_card;
            this.membership_card = membership_card;
            this.cb_text = cb_text;
            this.flag = flag;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : SyncAccountBalanceTask");
            try {
                activeOrder = RetrofitClient.getInstance().getApi().getActiveOrderByCpid(cpid).execute().body();
                LocalCache.setContactViewedList(RetrofitClient.getInstance().getApi().getContactViewedProfiles(cpid).execute().body(), activity);
            } catch (Exception e) {
                Log.i("ss_nw_call", "GetCountLeftTask error" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "GetCountLeftTask onPostExecute calling... ");
            super.onPostExecute(aVoid);
            if (activeOrder != null && !activeOrder.isEmpty()) {
                LocalCache.setActiveOrder(activeOrder.get(0), activity);
                OrderModal activeOrder = LocalCache.getActiveOrder(activity);
                if (flag && activeOrder != null && activeOrder.getId() != null) {
                    cb_card.setVisibility(View.VISIBLE);
                    membership_card.setVisibility(View.VISIBLE);
                    cb_text.setText("Contact Balance : " + activeOrder.getCountRemaining());
                }
            }
        }
    }

    public static void getEducationList(Activity activity, String _gender) {
        new GetEducationListTask(activity,_gender).execute();
    }

    public static void getCityList(Activity activity, String _gender) {
        new GetCityListTask(activity,_gender).execute();
    }

    public static void getCasteList(Activity activity, String _gender) {
        new GetCasteListTask(activity,_gender).execute();
    }

    static class GetCityListTask extends AsyncTask<Void, Void, Void> {

        List<SingleResponse> response_list;

        Activity activity;
        String _gender;

        public GetCityListTask(Activity activity,String _gender) {
            this.activity = activity;
            this._gender = _gender;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : GetCityListTask");
            try {
                cityApiCalled = true;
                response_list = RetrofitClient.getInstance().getApi().getAllCityList(_gender).execute().body();
            } catch (Exception e) {
                Log.i("ss_nw_call", "GetEducationListTask error" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LocalCache.setCityList(response_list, activity);
        }
    }

    static class GetCasteListTask extends AsyncTask<Void, Void, Void> {

        List<SingleResponse> response_list;

        Activity activity;
        String _gender;

        public GetCasteListTask(Activity activity,String _gender) {
            this.activity = activity;
            this._gender = _gender;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : GetEducationListTask");
            try {
                casteApiCalled = true;
                response_list = RetrofitClient.getInstance().getApi().getAllCasteList(_gender).execute().body();
            } catch (Exception e) {
                Log.i("ss_nw_call", "GetEducationListTask error" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LocalCache.setCasteList(response_list, activity);
        }
    }

    static class GetEducationListTask extends AsyncTask<Void, Void, Void> {

        List<SingleResponse> response_list;

        Activity activity;
        String _gender;

        public GetEducationListTask(Activity activity,String _gender) {
            this.activity = activity;
            this._gender = _gender;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : GetEducationListTask");
            try {
                educationApiCalled = true;
                response_list = RetrofitClient.getInstance().getApi().getAllEducationList(_gender).execute().body();
            } catch (Exception e) {
                Log.i("ss_nw_call", "GetEducationListTask error" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LocalCache.setEducationList(response_list, activity);
        }
    }

    public static void getOccupationList(Activity activity, String _gender) {
        new GetOccupationListTask(activity,_gender).execute();
    }

    public static void getLastnamesList(Activity activity, String _gender) {
        new GetLastnamesTask(activity,_gender).execute();
    }

    static class GetOccupationListTask extends AsyncTask<Void, Void, Void> {

        List<SingleResponse> response_list;

        Activity activity;
         String _gender;

        public GetOccupationListTask(Activity activity, String _gender) {
            this.activity = activity;
            this._gender = _gender;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : GetOccupationListTask");
            try {
                occupationApiCalled = true;
                response_list = RetrofitClient.getInstance().getApi().getAllOccupationList(_gender).execute().body();
            } catch (Exception e) {
                Log.i("ss_nw_call", "GetOccupationListTask error" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LocalCache.setOccupationList(response_list, activity);
        }
    }

    static class GetLastnamesTask extends AsyncTask<Void, Void, Void> {

        List<SingleResponse> response_list;

        Activity activity;
        String _gender;

        public GetLastnamesTask(Activity activity,String _gender) {
            this.activity = activity;
            this._gender = _gender;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : GetOccupationListTask");
            try {
                lastnamesApiCalled = true;
                response_list = RetrofitClient.getInstance().getApi().getAllLastnamesList(_gender).execute().body();
            } catch (Exception e) {
                Log.i("ss_nw_call", "GetOccupationListTask error" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LocalCache.setLastnamesList(response_list, activity);
        }
    }


    public static void addToShortListedProfiles(String cpid, String vcpid) {
        new AddToShortListedProfilesTask(cpid, vcpid).execute();
    }

    public static void addToNotInterestedProfiles(String cpid, String vcpid) {
        new AddToNotInterestedProfilesTask(cpid, vcpid).execute();
    }

    public static void addToInterestedProfiles(String cpid, String vcpid) {
        new AddToInterestedProfilesTask(cpid, vcpid).execute();
    }

    public static void addToLikedProfiles(String cpid, String vcpid) {
        new AddToLikedProfilesTask(cpid, vcpid).execute();
    }

    public static void addNotification(NotificationModal modal) {
        new AddNotificationTask(modal).execute();
    }


    static class AddToShortListedProfilesTask extends AsyncTask<Void, Void, Void> {

        String cpid;

        String vcpid;

        SingleResponse response;

        public AddToShortListedProfilesTask(String cpid, String vcpid) {
            this.cpid = cpid;
            this.vcpid = vcpid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : AddToShortListedProfilesTask");
            try {
                RetrofitClient.getInstance().getApi().addToShortListedProfiles(cpid, vcpid).execute().body();

            } catch (Exception e) {
                Log.i("ss_nw_call", "AddToShortListedProfilesTask error" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "AddToShortListedProfilesTask onPostExecute calling... ");
            super.onPostExecute(aVoid);

        }
    }

    static class AddToNotInterestedProfilesTask extends AsyncTask<Void, Void, Void> {

        String cpid;

        String vcpid;

        SingleResponse response;

        public AddToNotInterestedProfilesTask(String cpid, String vcpid) {
            this.cpid = cpid;
            this.vcpid = vcpid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : AddToNotInterestedProfilesTask");
            try {
                RetrofitClient.getInstance().getApi().addToNotInterestedProfiles(cpid, vcpid).execute().body();

            } catch (Exception e) {
                Log.i("ss_nw_call", "GetCountLeftTask error" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "AddToNotInterestedProfilesTask onPostExecute calling... ");
            super.onPostExecute(aVoid);

        }
    }

    static class AddToInterestedProfilesTask extends AsyncTask<Void, Void, Void> {

        String cpid;

        String vcpid;

        SingleResponse response;

        public AddToInterestedProfilesTask(String cpid, String vcpid) {
            this.cpid = cpid;
            this.vcpid = vcpid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : AddToInterestedProfilesTask");
            try {
                RetrofitClient.getInstance().getApi().addToInterestedProfiles(cpid, vcpid).execute().body();

            } catch (Exception e) {
                Log.i("ss_nw_call", "AddToInterestedProfilesTask error" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "AddToInterestedProfilesTask onPostExecute calling... ");
            super.onPostExecute(aVoid);

        }
    }

    static class AddToLikedProfilesTask extends AsyncTask<Void, Void, Void> {

        String cpid;

        String vcpid;

        SingleResponse response;

        public AddToLikedProfilesTask(String cpid, String vcpid) {
            this.cpid = cpid;
            this.vcpid = vcpid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : AddToLikedProfilesTask");
            try {
                RetrofitClient.getInstance().getApi().addToLikedProfiles(cpid, vcpid).execute().body();

            } catch (Exception e) {
                Log.i("ss_nw_call", "AddToLikedProfilesTask error" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "AddToInterestedProfilesTask onPostExecute calling... ");
            super.onPostExecute(aVoid);

        }
    }

    static class AddNotificationTask extends AsyncTask<Void, Void, Void> {

        NotificationModal modal;

        public AddNotificationTask(NotificationModal modal) {
            this.modal = modal;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : AddNotificationTask");
            try {
                RetrofitClient.getInstance().getApi().addNotification(modal).execute().body();

            } catch (Exception e) {
                Log.i("ss_nw_call", "AddNotificationTask error" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "AddNotificationTask onPostExecute calling... ");
            super.onPostExecute(aVoid);

        }
    }

    static class SetLoggedInCustomerTask extends AsyncTask<Void, Void, Void> {


        List<Customer> customer;

        String mobile;
        Activity activity;
        Circle d = new Circle();

        SpinKitView progressBar;

        public SetLoggedInCustomerTask(Activity activity, String mobile, SpinKitView progressBar) {
            this.mobile = mobile;
            this.activity = activity;
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminateDrawable(d);
            }
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : SetLoggedInCustomerTask");
            try {
                customer = RetrofitClient.getInstance().getApi().getCustomerByMobile(mobile).execute().body();

                LocalCache.setMembershipList(RetrofitClient.getInstance().getApi().getAllMembershipPlans().execute().body(), activity);






                if (customer != null && !customer.isEmpty()) {
                    Log.i("ss_nw_call", "pkg :id " + customer.get(0).getActivepackageid());
                    Log.i("local_logs", "SendOtpActivity - saving customer" + new Date());
                    LocalCache.setLoggedInCustomer(customer.get(0), activity);

                    syncAccountBalance(customer.get(0).getProfileId(), activity, null, null, null, false);
                    LocalCache.setContactViewedList(RetrofitClient.getInstance().getApi().getContactViewedProfiles(customer.get(0).getProfileId()).execute().body(), activity);

                    // todo call this only for paid user
                    String _gender = customer.get(0).getGender() != null && customer.get(0).getGender().equalsIgnoreCase("male") ? "female" : "male";
                    if(customer.get(0).getActivepackageid() != null){
                        if (!educationApiCalled)
                            getEducationList(activity,_gender);
                        if (!occupationApiCalled)
                            getOccupationList(activity,_gender);
                        if (!lastnamesApiCalled)
                            getLastnamesList(activity,_gender);
                        if (!cityApiCalled)
                            getCityList(activity,_gender);
                        if (!casteApiCalled)
                            getCasteList(activity,_gender);
                    }



                }
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
            }

        }
    }

    static class UpdateLoggedInCustomerDetailsTask extends AsyncTask<Void, Void, Void> {


        List<Customer> customer;

        String mobile;
        Activity activity;

        public UpdateLoggedInCustomerDetailsTask(Activity activity, String mobile) {
            this.mobile = mobile;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : UpdateLoggedInCustomerDetailsTask");
            try {
                customer = RetrofitClient.getInstance().getApi().getCustomerByMobile(mobile).execute().body();
                if (customer != null && !customer.isEmpty()) {
                    Log.i("local_logs", "SendOtpActivity - saving customer" + new Date());
                    if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                        LocalCache.setLoggedInCustomer(customer.get(0), activity);
                    }
                }
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    static class GetUserNotificationsListTask extends AsyncTask<Void, Void, Void> {


        List<NotificationModal> notificationsList;


        Dialog dialog;
        Activity activity;
        TextView no_noti_text;
        RecyclerView recyclerView;
        String cpid;

        Boolean flag = false;
        Boolean showSingleNotification;

        public GetUserNotificationsListTask(Activity activity, Dialog dialog, String cpid, Boolean showSingleNotification) {
            this.dialog = dialog;
            this.activity = activity;
            this.cpid = cpid;
            this.showSingleNotification = showSingleNotification;
            if(!showSingleNotification){
                no_noti_text = dialog.findViewById(R.id.no_noti_text);
                recyclerView = dialog.findViewById(R.id.notificationlistRecyclerview);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : GetUserNotificationsListTask");
            try {
                if (cpid == null) {
                    // user flow
                    Customer c = LocalCache.getLoggedInCustomer(activity);
                    cpid = c.getProfileId();
                    notificationsList = RetrofitClient.getInstance().getApi().getUserNotifications(cpid).execute().body();
                } else{
                    //admin flow
                    flag = true;
                    notificationsList = RetrofitClient.getInstance().getApi().adminByCpid(cpid).execute().body();
                }



            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(showSingleNotification){
                if (notificationsList != null && !notificationsList.isEmpty()) {
                    NotificationModal modal = null ;
                    for(NotificationModal obj : notificationsList){
                        if(obj.getIs_viewed().equalsIgnoreCase("0")){
                            modal = obj;
                            break;
                        }
                    }
                    if(modal != null){
                        LinearLayout layout = ((LinearLayout)dialog.findViewById(R.id.notilayout));
                        Button callbutton = ((Button)dialog.findViewById(R.id.callbutton));
                        CardView notificationcard = dialog.findViewById(R.id.card);
                        ((TextView)dialog.findViewById(R.id.notiid)).setText(String.valueOf(modal.getId()));
                        ((TextView)dialog.findViewById(R.id.vcpid)).setText(String.valueOf(modal.getVcpid()));
                        ((TextView)dialog.findViewById(R.id.name)).setText(modal.getTitle());
                        ((TextView)dialog.findViewById(R.id.city)).setText(modal.getCity());
                        ((TextView)dialog.findViewById(R.id.age)).setText(modal.getAge()+" yrs");
                        ((Button)dialog.findViewById(R.id.callbutton)).setText("Call "+modal.getClientname());
                        Glide.with(activity)
                                .load(modal.getPhoto())
                                .placeholder(R.drawable.oops)
                                .into(((ImageView)dialog.findViewById(R.id.photo)));

                        notificationcard.setOnClickListener(view -> {
                            dialog.dismiss();
                            ApiCallUtil.addLeads(cpid, ((TextView)dialog.findViewById(R.id.vcpid)).getText().toString().trim(),"clicked_notification_popup_card");
                            ApiCallUtil.updateViewedNotificationState(((TextView)dialog.findViewById(R.id.notiid)).getText().toString().trim());
                            ApiCallUtil.redirected_via_home_screen_noti = true;
                            //ApiCallUtil.getLevel2Data(((TextView)dialog.findViewById(R.id.vcpid)).getText().toString().trim(), activity);
                            activity.startActivity(new Intent(activity, Level2ProfileActivity.class)
                                    .putExtra("level2data", ((TextView)dialog.findViewById(R.id.vcpid)).getText().toString().trim()));
                        });
                        callbutton.setOnClickListener(view -> {
                            dialog.dismiss();
                            ApiCallUtil.addLeads(cpid, ((TextView)dialog.findViewById(R.id.vcpid)).getText().toString().trim(),"clicked_notification_popup_card");
                            ApiCallUtil.updateViewedNotificationState(((TextView)dialog.findViewById(R.id.notiid)).getText().toString().trim());
                            ApiCallUtil.redirected_via_home_screen_noti = true;
                            //ApiCallUtil.getLevel2Data(((TextView)dialog.findViewById(R.id.vcpid)).getText().toString().trim(), activity);
                            activity.startActivity(new Intent(activity, Level2ProfileActivity.class)
                                    .putExtra("level2data", ((TextView)dialog.findViewById(R.id.vcpid)).getText().toString().trim()));

                        });

                        String color = null;
                        ObjectAnimator anim = null;
                        anim = ObjectAnimator.ofInt(callbutton, "backgroundColor", Color.GREEN, Color.YELLOW,Color.WHITE);

                        //linearLayout.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        anim.setDuration(1500);
                        anim.setEvaluator(new ArgbEvaluator());
                        anim.setRepeatMode(ValueAnimator.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        anim.start();

                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                }
            }
            else {
                if (notificationsList != null && !notificationsList.isEmpty()) {

                    NotificationAdapter adapter = new NotificationAdapter(notificationsList, activity, dialog, flag);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    recyclerView.setAdapter(adapter);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    no_noti_text.setVisibility(View.VISIBLE);
                }
            }

        }
    }

    static class GetDistinctUserNotificationsTask extends AsyncTask<Void, Void, Void> {


        List<Level_1_cardModal> list;
        Activity activity;
        RecyclerView recyclerView;
        AllMembersAdapter adapter;

        public GetDistinctUserNotificationsTask(Activity activity, RecyclerView recyclerView, AllMembersAdapter adapter) {
            this.activity = activity;
            this.recyclerView = recyclerView;
            this.adapter = adapter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date() + " api call : GetUserNotificationsListTask");
            try {
                list = RetrofitClient.getInstance().getApi().getDistinctUserNotifications().execute().body();
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (list != null && !list.isEmpty()) {
                AllMemberActivity.filteredList = list;
                adapter = new AllMembersAdapter(list, activity, true);
                AllMemberActivity.adapter = adapter;
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                recyclerView.setAdapter(adapter);
            }
        }
    }

    static class UpdateViewedNotificationStateTask extends AsyncTask<Void, Void, Void> {


        String noti_id;

        public UpdateViewedNotificationStateTask(String noti_id) {
            this.noti_id = noti_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : UpdateViewedNotificationStateTask");
                RetrofitClient.getInstance().getApi().updateViewedNotificationState(noti_id).execute().body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


    static class AddLeadsTask extends AsyncTask<Void, Void, Void> {


        String cpid;
        String vcpid;
        String type;

        public AddLeadsTask(String cpid, String vcpid, String type) {
            this.cpid = cpid;
            this.vcpid = vcpid;
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : AddLeadsTask");
                RetrofitClient.getInstance().getApi().addLeads(cpid,vcpid,type).execute().body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    static class UpdateFollowupTask extends AsyncTask<Void, Void, Void> {


        String id;

        public UpdateFollowupTask(String noti_id) {
            this.id = noti_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : UpdateFollowupTask");
                RetrofitClient.getInstance().getApi().updateFollowup(id).execute().body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    static class GetFollowupsTask extends AsyncTask<Void, Void, Void> {


        String cpid;

        Activity activity;
        List<FollowUpModal> list = null;

        RecyclerView recyclerView;

        public GetFollowupsTask(String cpid, RecyclerView recyclerView, Activity activity) {
            this.cpid = cpid;
            this.activity = activity;
            this.recyclerView = recyclerView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : GetFollowupsTask");
                if (cpid == null) {
                    // all followups
                    list = RetrofitClient.getInstance().getApi().getAllFollowUps().execute().body();
                } else
                    list = RetrofitClient.getInstance().getApi().getFollowUpByCpid(cpid).execute().body();
            } catch (Exception e) {
                Log.i("ss_nw_call", "GetFollowupsTask error : " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (list != null && !list.isEmpty()) {

                FollowUpAdapter adapter = new FollowUpAdapter(list, activity);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                recyclerView.setAdapter(adapter);

            }
        }
    }

    static class GetContactViewedProfilesTask extends AsyncTask<Void, Void, Void> {


        String cpid;
        List<ContactViewedModal> list = new ArrayList<>();
        ContactViewedAdapter adapter;
        RecyclerView contactviewedRecyclerView;
        Activity activity;
        TextView cv_count;
        TextView cv_zero;

        public GetContactViewedProfilesTask(String cpid, ContactViewedAdapter adapter, RecyclerView contactviewedRecyclerView, TextView cv_count,TextView cv_zero, Activity activity) {
            this.cpid = cpid;
            this.adapter = adapter;
            this.contactviewedRecyclerView = contactviewedRecyclerView;
            this.activity = activity;
            this.cv_count = cv_count;
            this.cv_zero = cv_zero;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : GetContactViewedProfilesTask");
                list = RetrofitClient.getInstance().getApi().getContactViewedProfiles(cpid).execute().body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (list != null && !list.isEmpty()) {
                cv_count.setText("Total contacts viewed : " + list.size());
                adapter = new ContactViewedAdapter(list, activity);
                contactviewedRecyclerView.setHasFixedSize(true);
                contactviewedRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                contactviewedRecyclerView.setAdapter(adapter);
                contactviewedRecyclerView.setVisibility(View.VISIBLE);
            }
            else
                cv_zero.setVisibility(View.VISIBLE);
        }
    }

    static class RegisterNewCustomerTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        Customer c;
        List<Customer> persistedCustomerObj = null;

        String error;
        SpinKitView progressBar;
        Circle d = new Circle();
        List<Level_1_cardModal> list = new ArrayList<>();

        Boolean onboardNewUser = false;

        Fragment fragment;
        Boolean isAdmin;

        public RegisterNewCustomerTask(Customer c, Activity activity, Boolean onboardNewUser, Fragment fragment, Boolean isAdmin) {
            this.c = c;
            this.activity = activity;
            progressBar = activity.findViewById(R.id.progressBar1);
            this.onboardNewUser = onboardNewUser;
            this.fragment = fragment;
            this.isAdmin = isAdmin;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminateDrawable(d);
            }
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : RegisterNewCustomerTask");
                persistedCustomerObj = RetrofitClient.getInstance().getApi().registerAndGetCustomer(c).execute().body();
                if (persistedCustomerObj != null) {
                    if(!isAdmin)
                    LocalCache.setLoggedInCustomer(persistedCustomerObj.get(0), activity);
                    if (onboardNewUser)
                        ApiCallUtil.cpid = persistedCustomerObj.get(0).getProfileId();
                }

            } catch (Exception e) {
                error = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressBar != null)
                progressBar.setVisibility(View.GONE);

            if (persistedCustomerObj != null) {

                if (onboardNewUser) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activity.startActivity(intent);
                } else {
                    MediaPlayer.create(activity, R.raw.done_sound).start();
                    HelperUtils.showDialog(activity, R.drawable.success_icon, "Profile created !!!", "id : " + persistedCustomerObj.get(0).getProfileId());
                }
            } else
                HelperUtils.showDialog(activity, R.drawable.failed_icon, "Error occured !!!", error);


        }
    }

    static class UpdateProfileTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        Customer c;
        List<Customer> loggedInCustomer;

        SingleResponse response;
        String error;
        SpinKitView progressBar;
        Circle d = new Circle();
        List<Level_1_cardModal> list = new ArrayList<>();

        Boolean updateCache;
        Boolean forceupdate;

        public UpdateProfileTask(Customer c, Activity activity, Boolean updateCache, Boolean forceupdate) {
            this.c = c;
            this.activity = activity;
            progressBar = activity.findViewById(R.id.progressBar);
            this.updateCache = updateCache;
            this.forceupdate = forceupdate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminateDrawable(d);
            }
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : UpdateProfileTask");
                loggedInCustomer = RetrofitClient.getInstance().getApi().updateProfile(c).execute().body();
                if (updateCache && loggedInCustomer != null && !loggedInCustomer.isEmpty()) {
                    LocalCache.setLoggedInCustomer(loggedInCustomer.get(0), activity);
                }

            } catch (Exception e) {
                error = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressBar != null)
                progressBar.setVisibility(View.GONE);
            MediaPlayer.create(activity, R.raw.done_sound).start();
            // setting level2 obj , just to update views
            ApiCallUtil.editedCustomerProfile = c ;
            activity.onBackPressed();
            if (forceupdate) {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
            }


        }
    }

    static class ValidateLoginMobileTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        String mobile;
        LinearLayout formLayout;
        LinearLayout cmLayout;
        SingleResponse response;
        String error;
        TextInputEditText mobile1;

        ExtendedFloatingActionButton savebtn;

        public ValidateLoginMobileTask(Activity activity, String mobile, LinearLayout formLayout, LinearLayout cmLayout, TextInputEditText mobile1, ExtendedFloatingActionButton savebtn) {
            this.activity = activity;
            this.mobile = mobile;
            this.formLayout = formLayout;
            this.cmLayout = cmLayout;
            this.mobile1 = mobile1;
            this.savebtn = savebtn;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : ValidateLoginMobileTask");
                //response = RetrofitClient.getInstance().getApi().isMobileExists(mobile).execute().body();
                response = RetrofitClient.getInstance().getApi().isMobileExistsAll(mobile).execute().body();
            } catch (Exception e) {
                error = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (response != null) {
                if (!response.getResult().equalsIgnoreCase("0")) {
                    cmLayout.setVisibility(View.VISIBLE);
                    HelperUtils.showDialog(activity, R.drawable.failed_icon, "Account exists id:" + response.getResult(), "change mobile number to continue");
                } else {
                    // allow registration flow
                    formLayout.setVisibility(View.VISIBLE);
                    savebtn.setVisibility(View.VISIBLE);
                    mobile1.setText(mobile);
                    mobile1.setEnabled(false);
                    //savebtn.setEnabled(true);
                    cmLayout.setVisibility(View.GONE);
                }
            } else {
                cmLayout.setVisibility(View.VISIBLE);
                HelperUtils.showDialog(activity, R.drawable.failed_icon, "Error occured !!!", error);
            }
        }
    }

    static class GetAdminPhoneTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        SingleResponse response;


        public GetAdminPhoneTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : GetAdminPhoneTask");
                response = RetrofitClient.getInstance().getApi().getAdminPhone().execute().body();
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (response != null && !response.getResult().equalsIgnoreCase("0"))
                LocalCache.setAdminPhone(response.getResult(), activity);
        }
    }

    static class GetMyMembershipsTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        RecyclerView recyclerView;
        String cpid;
        MyMembershipAdapter adapter;
        List<MyMembershipModal> list = new ArrayList<>();


        public GetMyMembershipsTask(String cpid, Activity activity, RecyclerView recyclerView) {
            this.activity = activity;
            this.cpid = cpid;
            this.recyclerView = recyclerView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : GetMyMembershipsTask");
                list = RetrofitClient.getInstance().getApi().getMyMemberships(cpid).execute().body();
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (list != null && !list.isEmpty()) {
                adapter = new MyMembershipAdapter(list, activity);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                recyclerView.setAdapter(adapter);
            }
        }
    }

    static class GetMembershipPlansTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        RecyclerView recyclerView;
        BuyMembershipAdapter adapter;
        List<MembershipModal> list = new ArrayList<>();

        public GetMembershipPlansTask(Activity activity, RecyclerView recyclerView) {
            this.activity = activity;
            this.recyclerView = recyclerView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : GetMembershipPlansTask");
                list = RetrofitClient.getInstance().getApi().getAllMembershipPlans().execute().body();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (list != null && !list.isEmpty()) {
                LocalCache.setMembershipList(new ArrayList<>(), activity);
                LocalCache.setMembershipList(list, activity);
                adapter = new BuyMembershipAdapter(list, activity);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                recyclerView.setAdapter(adapter);


            }
        }
    }

    static class ValidateAdminCodeTask extends AsyncTask<Void, Void, Void> {

        String inputCode;
        SingleResponse obj;
        Dialog d;

        Activity activity;

        public ValidateAdminCodeTask(Dialog d, String inputCode, Activity activity) {
            this.inputCode = inputCode;
            this.d = d;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : ValidateAdminCodeTask");
                obj = RetrofitClient.getInstance().getApi().getAdminCode().execute().body();

            } catch (Exception e) {
                Log.i("local_logs", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (obj != null) {
                if (inputCode.equalsIgnoreCase(obj.getResult())) {
                    MediaPlayer.create(activity, R.raw.done_sound).start();
                    d.dismiss();
                    activity.startActivity(new Intent(activity, AdminZoneActivity.class));
                } else {
                    d.findViewById(R.id.errorcode).setVisibility(View.VISIBLE);
                    ((TextView) d.findViewById(R.id.errorcode)).setText("wrong pin entered , try again");
                }
            } else {
                ((TextView) d.findViewById(R.id.errorcode)).setText("wrong pin entered , try again");
            }
        }
    }

    static class CheckIsLiveTask extends AsyncTask<Void, Void, Void> {


        SingleResponse obj;
        Activity activity;

        public CheckIsLiveTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : CheckIsLiveTask");
                obj = RetrofitClient.getInstance().getApi().isLive().execute().body();
            } catch (Exception e) {
                Log.i("local_logs", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (obj != null) {
                LocalCache.setIsLive(obj.getResult(), activity);
            } else
                LocalCache.setIsLive("server error", activity);

        }
    }


    static class SyncStatsTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        List<Stat> list;

        public SyncStatsTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : SyncStatsTask");
                list = RetrofitClient.getInstance().getApi().getStats().execute().body();

            } catch (Exception e) {
                Log.i("local_logs", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (list != null) {
                LocalCache.setGenderStat(list, activity);

                Stat stat = list.get(0);
                ((TextView) activity.findViewById(R.id.totalmalecount)).setText(String.valueOf(stat.getMale()));
                ((TextView) activity.findViewById(R.id.totalfemalecount)).setText(String.valueOf(stat.getFemale()));
                ((TextView) activity.findViewById(R.id.malewithphotos)).setText(String.valueOf(stat.getMale_profiles_with_photos()));
                ((TextView) activity.findViewById(R.id.malewithoutphotos)).setText(String.valueOf(stat.getMale_profiles_without_photos()));
                ((TextView) activity.findViewById(R.id.femalewithphotos)).setText(String.valueOf(stat.getFemale_profiles_with_photos()));
                ((TextView) activity.findViewById(R.id.femalewithoutphotos)).setText(String.valueOf(stat.getFemale_profiles_without_photos()));
                ((TextView) activity.findViewById(R.id.collection)).setText("Rs." + stat.getCollection());
                ((TextView) activity.findViewById(R.id.paidmembers)).setText(String.valueOf(stat.getPaid_customer()));
                ((TextView) activity.findViewById(R.id.nonpaidmembers)).setText(String.valueOf(stat.getNon_paid_customer()));
            }
        }
    }


    static class SearchLevel1DataTask extends AsyncTask<Void, Void, Void> {

        Activity activity;

        List<Level_1_cardModal> list;

        FilterModal filter;

        Fragment fragment;
        Dialog d;


        public SearchLevel1DataTask(Activity activity, Fragment fragment, FilterModal filter,Dialog d) {
            this.filter = filter;
            this.activity = activity;
            this.fragment = fragment;
            this.d = d;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : SearchLevel1DataTask");
                list = RetrofitClient.getInstance().getApi().searchLevel1Profiles(filter).execute().body();

            } catch (Exception e) {
                Log.i("local_logs", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            d.dismiss();
            ((HomeFragment) fragment).initLevel_1_CardProfilesRecyclerView(list);
           /* if (list != null && !list.isEmpty())
                LocalCache.setLevel1List(list, activity);*/

        }
    }

    static class SearchSingleProfileTask extends AsyncTask<Void, Void, Void> {

        Activity activity;

        List<Level_1_cardModal> list;

        Fragment fragment;

        FilterModal modal;
        Dialog d;


        public SearchSingleProfileTask(Activity activity, Fragment fragment, FilterModal modal , Dialog d) {
            this.modal = modal;
            this.activity = activity;
            this.fragment = fragment;
            this.d = d;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : SearchSingleProfileTask");
                list = RetrofitClient.getInstance().getApi().searchLevel1ProfileByCpid(modal).execute().body();

            } catch (Exception e) {
                Log.i("local_logs", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            d.dismiss();
            ((HomeFragment) fragment).initLevel_1_CardProfilesRecyclerView(list);
           /* if (list != null && !list.isEmpty())
                LocalCache.setLevel1List(list, activity);*/

        }
    }


    static class GetFilteredLevel2DataTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        List<Customer> list;

        SpinKitView progressBar;

        Circle d = new Circle();

        FilterModal filter;


        public GetFilteredLevel2DataTask(Activity activity, FilterModal filter) {
            this.filter = filter;
            this.activity = activity;
            progressBar = activity.findViewById(R.id.progressBar);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

            progressBar.setIndeterminateDrawable(d);
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : GetFilteredLevel2DataTask");
                ProfileExportActivity.temp_level2list = new ArrayList<>();
                Log.i("local_logs", filter.toString());
                list = RetrofitClient.getInstance().getApi().getFilteredLevel2Profiles(filter).execute().body();

            } catch (Exception e) {
                Log.i("local_logs", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            Log.i("local_logs", "GetFilteredLevel2DataTask postexecute");
            if (list != null && list.size() > 0) {
                Log.i("local_logs", list.size() + "");
                ProfileExportActivity.temp_level2list = list;
                activity.findViewById(R.id.filterlayout).setVisibility(View.GONE);
                activity.findViewById(R.id.resultlayout).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.exportBtn).setEnabled(true);

                activity.findViewById(R.id.profilefoundtext).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.exportBtn).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.exportToPhoneLayout).setVisibility(View.GONE);
                ((TextView) activity.findViewById(R.id.profilefoundtext)).setText(list.size() + " profiles found");
            }

        }
    }

    static class SearchProfileTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        List<Customer> list;
        Dialog d;
        String searchBy, value;
        RecyclerView recyclerView;

        LinearLayout downLayout;


        public SearchProfileTask(Dialog d, Activity activity, String searchBy, String value, RecyclerView recyclerView, LinearLayout downLayout) {
            this.d = d;
            this.activity = activity;
            this.searchBy = searchBy;
            this.value = value;
            this.recyclerView = recyclerView;
            this.downLayout = downLayout;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : SearchProfileTask");
                if (searchBy.equalsIgnoreCase("profile id"))
                    list = RetrofitClient.getInstance().getApi().getCustomerByCpid(value).execute().body();
                else if (searchBy.equalsIgnoreCase("mobile"))
                    list = RetrofitClient.getInstance().getApi().getCustomerByMobile(value).execute().body();
                else if (searchBy.equalsIgnoreCase("name"))
                    list = RetrofitClient.getInstance().getApi().getCustomerByName(value).execute().body();

            } catch (Exception e) {
                Log.i("local_logs", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            d.findViewById(R.id.searchBtn).setEnabled(true);
            if (list != null && !list.isEmpty()) {
                downLayout.setVisibility(View.VISIBLE);
                d.findViewById(R.id.errorTxt).setVisibility(View.GONE);
                SearchedMembersAdapter adapter = new SearchedMembersAdapter(d, list, activity);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                recyclerView.setAdapter(adapter);
            } else {
                downLayout.setVisibility(View.GONE);
                d.findViewById(R.id.errorTxt).setVisibility(View.VISIBLE);
            }


        }
    }

    static class AssignMembershipTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        OrderModal orderModal;

        SingleResponse response;
        List<Customer> customer = new ArrayList<>();


        public AssignMembershipTask(OrderModal orderModal, Activity activity) {
            this.orderModal = orderModal;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : AssignMembershipTask");
                response = RetrofitClient.getInstance().getApi().assignMembership(orderModal).execute().body();

            } catch (Exception e) {
                Log.i("local_logs", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (response != null && !response.getResult().equalsIgnoreCase("0")) {
                MediaPlayer.create(activity, R.raw.done_sound).start();
                HelperUtils.showDialog(activity, R.drawable.success_icon, "Membership assigned !!!", "id : " + response.getResult());
            } else {
                HelperUtils.showDialog(activity, R.drawable.failed_icon, "Error occured !!!", "id : " + response.getResult());
            }
            //{"cpid":"26","endDate":"09/10/2024","membershipId":"","paymentmode":"Phone Pe","startDate":"21/09/2023","txnDate":"21/09/2023"}

        }
    }

    static class DynamicLayoutCreationTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        List<Customer> list;
        View v;

        public DynamicLayoutCreationTask(Activity activity, List<Customer> list, View v) {
            this.activity = activity;
            this.list = list;
            this.v = v;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : DynamicLayoutCreationTask");
                //activity.runOnUiThread(() -> dynamicLayout(activity,list, v));
                dynamicLayout(activity, list, v);

            } catch (Exception e) {
                Log.i("local_logs", "DynamicLayoutCreationTask " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    static class PersistBitmapTask extends AsyncTask<Void, Void, Void> {

        Activity activity;

        public PersistBitmapTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : PersistBitmapTask");
                persistBitmapProcess(activity);

            } catch (Exception e) {
                Log.i("local_logs", "DynamicLayoutCreationTask " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ((TextView) activity.findViewById(R.id.bitmapCount)).setText("Done");

        }
    }

    public static void dynamicLayout(Activity activity, List<Customer> list, View v) {

        try {
            for (Customer obj : list) {
                createBitmapForViewAsync(v, activity, obj);

            }
        } catch (Exception e) {
            Log.i("local_logs", "DynamicLayoutCreationTask " + e.toString());
        }


    }

    // Function to create the bitmap asynchronously for each view
    private static void createBitmapForViewAsync(View view, Activity activity, Customer obj) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                try {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    // Check if the view's dimensions are valid
                    if (view.getWidth() > 0 && view.getHeight() > 0) {
                        //Customer obj = ProfileExportActivity.temp_level2list.get(counter);
                        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        updateExportViewData(view, canvas, bitmap, obj, activity);
                    }
                } catch (Exception e) {
                    Log.i("local_logs", "DynamicLayoutCreationTask " + e.toString());
                }
            }
        });
    }

    private static void updateExportViewData(View view, Canvas canvas, Bitmap bitmap, Customer obj, Activity activity) {
        Log.i("local_logs", "processing export data for profile " + obj.getProfileId());
        if (!obj.getB64().isEmpty()) {
            Glide.with(activity)
                    .load(HelperUtils.convertBitmapToDrawable(activity, HelperUtils.convertBase64ToBitmap(obj.getB64())))
                    .placeholder(HelperUtils.convertBitmapToDrawable(activity, HelperUtils.convertBase64ToBitmap(obj.getB64())))
                    .into((ImageView) view.findViewById(R.id.profilephotoaddresss));
        }

        ((TextView) view.findViewById(R.id.profileid)).setText("Profile id : A" + obj.getProfileId());
        ((TextView) view.findViewById(R.id.name)).setText(obj.getFirstname() + " " + obj.getLastname());
        ((TextView) view.findViewById(R.id.birthdate)).setText(obj.getBirthdate() + "  ( " + obj.getAge() + " yrs )");
        ((TextView) view.findViewById(R.id.birthtime)).setText(obj.getBirthtime());
        ((TextView) view.findViewById(R.id.birthplace)).setText(obj.getBirthplace());
        ((TextView) view.findViewById(R.id.height)).setText(obj.getHeight());
        ((TextView) view.findViewById(R.id.bloodgroup)).setText(obj.getBloodgroup());
        ((TextView) view.findViewById(R.id.zodiac)).setText(obj.getZodiac());
        ((TextView) view.findViewById(R.id.education)).setText(obj.getEducation());
        ((TextView) view.findViewById(R.id.occupation)).setText(obj.getOccupation());
        ((TextView) view.findViewById(R.id.religion)).setText(obj.getReligion());
        ((TextView) view.findViewById(R.id.caste)).setText(obj.getCaste());
        ((TextView) view.findViewById(R.id.marriagestatus)).setText(obj.getMarriagestatus());
        ((TextView) view.findViewById(R.id.fathername)).setText(obj.getFathername());
        ((TextView) view.findViewById(R.id.mothername)).setText(obj.getMothername());
        ((TextView) view.findViewById(R.id.relatives)).setText(obj.getRelatives());
        ((TextView) view.findViewById(R.id.family)).setText(obj.getFamily());
        ((TextView) view.findViewById(R.id.expectations)).setText(obj.getExpectations());
        view.draw(canvas);
        blist.add(new BitmapDataModal(bitmap, obj.getProfileId()));
        if (blist.size() == ProfileExportActivity.temp_level2list.size()) {
            LinearLayout parentLayout = activity.findViewById(R.id.exportview_view);
            parentLayout.removeView(view);
            activity.findViewById(R.id.export_parentlayout).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.profilefoundtext).setVisibility(View.GONE);
            activity.findViewById(R.id.exportBtn).setVisibility(View.GONE);
            activity.findViewById(R.id.exportToPhoneLayout).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.savetophoneBtn).setEnabled(true);
            ((TextView) activity.findViewById(R.id.bitmapCount)).setText(blist.size() + " profiles scanned");

        }
        counter++;
    }

    private static void persistBitmapProcess(Activity activity) {
        int count = 1;
        for (BitmapDataModal obj : blist) {
            Bitmap bitmap = obj.getBitmap();
            try {
                //((TextView) activity.findViewById(R.id.bitmapCount)).setText("Processing : " + count);

                int percent = (count * 100) / blist.size();
                ((TextView) activity.findViewById(R.id.bitmapCount)).setText(percent + " % completed");
                // Assuming you have a Bitmap object named 'bitmap' containing the generated image
// and 'obj' is your profile object

// Define the filename
                String filename = "profile_" + obj.getProfileId() + ".png";

// Get the content resolver
                ContentResolver resolver = activity.getContentResolver();

// Define the image details
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

// Define the selection criteria to find an existing image with the same filename
                String selection = MediaStore.Images.Media.DISPLAY_NAME + "=?";
                String[] selectionArgs = new String[]{filename};

// Check if an image with the same filename already exists
                Cursor cursor = resolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null,
                        selection,
                        selectionArgs,
                        null
                );

                if (cursor != null && cursor.moveToFirst()) {
                    // An image with the same filename exists, so update it

                    // Get the index of the _ID column
                    int idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);

                    if (idColumnIndex >= 0) {
                        // Get the ID of the existing image
                        long existingImageId = cursor.getLong(idColumnIndex);
                        Uri existingImageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, existingImageId);

                        // Update the image in the MediaStore
                        resolver.update(existingImageUri, values, null, null);
                    } else {
                        // Handle the case where the _ID column index is invalid or not found
                        // You can choose to log an error or handle this situation as needed
                    }

                    // Close the cursor
                    cursor.close();
                } else {
                    // No image with the same filename found, so insert the new image

                    // Insert the image into the MediaStore
                    Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    if (imageUri != null) {
                        // Open an output stream to write the Bitmap to the MediaStore
                        OutputStream outputStream = resolver.openOutputStream(imageUri);

                        // Compress the Bitmap as a PNG image and write it to the output stream
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                        // Close the output stream
                        outputStream.close();
                    }
                }

            } catch (Exception e) {
                Log.i("local_logs", "DynamicLayoutCreationTask " + e.toString());
            }
            count++;
        }
    }

    public static void fetchAllMembersList(Activity activity, String key, RecyclerView recyclerView, AllMembersAdapter adapter) {
        AllMemberActivity.filteredList = null;
        new FetchAllmembersTask(activity, key, recyclerView, adapter).execute();

    }

    static class FetchAllmembersTask extends AsyncTask<Void, Void, Void> {

        List<Level_1_cardModal> list = null;
        Activity activity;
        String key;
        RecyclerView recyclerView;
        AllMembersAdapter adapter;

        SpinKitView progressBar;

        Circle d = new Circle();

        public FetchAllmembersTask(Activity activity, String key, RecyclerView recyclerView, AllMembersAdapter adapter) {
            this.activity = activity;
            this.key = key;
            this.recyclerView = recyclerView;
            this.adapter = adapter;
            progressBar = activity.findViewById(R.id.progressBar1);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminateDrawable(d);
        }

        protected Void doInBackground(Void... params) {
            try {

                Log.i("ss_nw_call", new Date() + " api call : FetchAllmembersTask");
                list = RetrofitClient.getInstance().getApi().getAllCustomerProfilesByFilter(key).execute().body();

            } catch (Exception e) {
                Log.i("local_logs", "FetchAllmembersTask " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            if (list != null) {

                AllMemberActivity.filteredList = list;
                recyclerView = activity.findViewById(R.id.allmembersrecyclerview);
                Boolean flag = key.equalsIgnoreCase("allpaidmembers") || key.equalsIgnoreCase("allnonpaidmembers") ? true : false;
                adapter = new AllMembersAdapter(list, activity, flag);
                AllMemberActivity.adapter = adapter;
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                recyclerView.setAdapter(adapter);
            }
        }
    }

    public static void sendSmsFunction(Activity activity, List<SmsModal> list) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, 1);
            return;
        } else {
            /*sendSmsButton.setVisibility(View.GONE);
            smsCountLayout.setVisibility(View.GONE);*/
            new SendSmsFunctionTask(activity, list).execute();
        }
    }

    private static class SendSmsFunctionTask extends AsyncTask<String, String, String> {
        Activity activity;
        List<SmsModal> list;
        PendingIntent sentPI;
        SmsManager sms = SmsManager.getDefault();

        public SendSmsFunctionTask(Activity activity, List<SmsModal> list) {
            this.activity = activity;
            this.list = list;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Log.i("local_logs", "SendSmsFunctionTask sending sms started");

                for (SmsModal obj : list) {
                    sentPI = PendingIntent.getBroadcast(activity.getApplicationContext(), 0, new Intent("SMS_SENT").setAction(Long.toString(System.currentTimeMillis())), PendingIntent.FLAG_IMMUTABLE);
                    sms.sendTextMessage(obj.getMobile(), null, obj.getMessage(), sentPI, null);
                    Log.i("local_logs", "SendSmsFunctionTask sending sms : \nmobile : " + obj.getMobile() + "\nmessage : " + obj.getMessage());
                }

            } catch (Exception e) {
                Log.i("local_logs", "SendSmsFunctionTask sending eror : " + e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            /*Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    hidepDialog();
                }
            }, 4000);*/
        }
    }

    private static void initiateViewContactFlow(Activity activity, String vcpid) {
        Customer loggedinCustomer = LocalCache.getLoggedInCustomer(activity);

        // check if package exist or not
        if (loggedinCustomer.getActivepackageid() == null ) {
            Log.i("ss_nw_call", "View contact : pkg id is null");
            Level2ProfileActivity.showBuyMembershipBottomSheet();
        }
        else if(LocalCache.getActiveOrder(activity) != null && LocalCache.getActiveOrder(activity).getCountRemaining() <= 0 ){
            Log.i("ss_nw_call", "Balance left : "+LocalCache.getActiveOrder(activity).getCountRemaining());
            Level2ProfileActivity.showBuyMembershipBottomSheet();
        }else {
            //ApiCallUtil.getLevel2Data(vcpid, activity);
            activity.startActivity(new Intent(activity, Level2ProfileActivity.class)
                    .putExtra("level2data", vcpid));
        }
    }

    public static void sendSMS(Activity activity, String mobile, String message) {

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
