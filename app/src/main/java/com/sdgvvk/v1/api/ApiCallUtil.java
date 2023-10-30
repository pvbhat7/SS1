package com.sdgvvk.v1.api;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.sdgvvk.v1.BuyMembershipBottomSheetDialog;
import com.sdgvvk.v1.LocalCache;
import com.sdgvvk.v1.MainActivity;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.activity.AdminZoneActivity;
import com.sdgvvk.v1.activity.Level2ProfileActivity;
import com.sdgvvk.v1.activity.ProfileExportActivity;
import com.sdgvvk.v1.activity.SendOtpActivity;
import com.sdgvvk.v1.activity.VerifyOtpActivity;
import com.sdgvvk.v1.adapters.BuyMembershipAdapter;
import com.sdgvvk.v1.adapters.ContactViewedAdapter;
import com.sdgvvk.v1.adapters.MyMembershipAdapter;
import com.sdgvvk.v1.adapters.NotificationAdapter;
import com.sdgvvk.v1.adapters.SearchedMembersAdapter;
import com.sdgvvk.v1.adapters.TransactionAdapter;
import com.sdgvvk.v1.modal.BitmapDataModal;
import com.sdgvvk.v1.modal.ContactViewedModal;
import com.sdgvvk.v1.modal.Customer;
import com.sdgvvk.v1.modal.FilterModal;
import com.sdgvvk.v1.modal.Level_1_cardModal;
import com.sdgvvk.v1.modal.Level_2_Modal;
import com.sdgvvk.v1.modal.MembershipModal;
import com.sdgvvk.v1.modal.MyMembershipModal;
import com.sdgvvk.v1.modal.NotificationModal;
import com.sdgvvk.v1.modal.OrderModal;
import com.sdgvvk.v1.modal.SingleResponse;
import com.sdgvvk.v1.modal.Stat;
import com.sdgvvk.v1.modal.TransactionModal;
import com.sdgvvk.v1.ui.dashboard.MatchesFragment;
import com.sdgvvk.v1.ui.home.HomeFragment;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ApiCallUtil {

    static Boolean educationApiCalled = false , occupationApiCalled = false;

    public static int counter = 0;
    public static Boolean clicked_level2activity = false;

    public static List<BitmapDataModal> blist = new ArrayList<>();
    public static Dialog onboardDialog = null;
    public static String b64 = null;
    public static String cpid = null;


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

    public static void getUserNotifications(Activity activity, Dialog dialog) {
        new GetUserNotificationsListTask(activity, dialog).execute();
    }

    public static void updateViewedNotificationState(String noti_id) {
        new UpdateViewedNotificationStateTask(noti_id).execute();
    }

    public static void getContactViewedProfiles(String cpid, ContactViewedAdapter adapter, RecyclerView contactviewedRecyclerView,TextView cv_count, Activity activity) {
        new GetContactViewedProfilesTask(cpid, adapter, contactviewedRecyclerView,cv_count, activity).execute();
    }

    public static void registerProfile(Customer customer, Activity activity, Boolean onboardNewUser, Fragment fragment) {
        new RegisterNewCustomerTask(customer, activity, onboardNewUser, fragment).execute();
    }

    public static void updateProfile(Customer customer, Activity activity, Boolean updateCache) {
        new UpdateProfileTask(customer, activity, updateCache).execute();
    }

    public static void validateLoginMobile(Activity activity, String mobile, LinearLayout formLayout, LinearLayout cmLayout, TextInputEditText mobile1, Button savebtn) {
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

    public static void getFilteredLevel1Profiles(Activity activity, Fragment fragment, FilterModal modal) {
        new GetFilteredLevel1DataTask(activity, fragment, modal).execute();
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
        new GetMembershipPlansTask(activity, recyclerView).execute();
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
        new DisableProfileTask(activity,vcpid).execute();
    }

    public static void CheckAccountStatus(SendOtpActivity activity , String mobile) {
        new CheckAccountStatusTask(activity , mobile).execute();
    }

    public static void getAllTransactions(Dialog d, Activity activity) {
        new GetAllTransactionsTask(d,activity).execute();
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
            Log.i("ss_nw_call", new Date()+" api call : CheckAccountStatusTask");
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
            if(obj != null && obj.getResult().equalsIgnoreCase("true"))
                account_deactivated = true;

            if(!account_deactivated){

                HelperUtils.vibrateFunction(activity);

                activity.findViewById(R.id.buttonGetOtp).setVisibility(View.GONE);
                activity.showProgressBar();


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
            }
            else{
                activity.findViewById(R.id.loginbox).setVisibility(View.GONE);
                activity.findViewById(R.id.errorbox).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.contactsupportbtn).setVisibility(View.VISIBLE);
                ((TextView)activity.findViewById(R.id.errorTxt)).setText("Account deactivated");
            }
        }
    }

    static class GetAllTransactionsTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        Dialog d;
        List<TransactionModal> txnList;

        public GetAllTransactionsTask(Dialog d , Activity activity) {
            this.activity = activity;
            this.d = d;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date()+" api call : GetAllTransactionsTask");
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
            if(txnList != null && !txnList.isEmpty()){
                // update in recyclerview
                TransactionAdapter adapter = new TransactionAdapter(txnList, activity, d);
                RecyclerView recyclerView = d.findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                recyclerView.setAdapter(adapter);
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
            Log.i("ss_nw_call", new Date()+" api call : DisableProfileTask");
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
        new DeleteProfileTask(activity,vcpid).execute();
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
            Log.i("ss_nw_call", new Date()+" api call : DeleteProfileTask");
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
                LocalCache.setActiveOrder(new OrderModal(), activity);
                LocalCache.setLevel1List(new ArrayList<>(), activity);
                LocalCache.setContactViewedList(new ArrayList<>(), activity);
                LocalCache.setMembershipList(new ArrayList<>( ), activity);
                LocalCache.setGenderStat(new ArrayList<>( ), activity);
                LocalCache.setIsLive("Account deactivated", activity);
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
            Log.i("ss_nw_call", new Date()+" api call : GetAllCustomerProfilesTask");
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
                //Collections.shuffle(list);

                if (LocalCache.getLevel1List(activity).isEmpty())
                    ((HomeFragment) fragment).initLevel_1_CardProfilesRecyclerView(list);

                if (override)
                    ((HomeFragment) fragment).initLevel_1_CardProfilesRecyclerView(list);


                LocalCache.setLevel1List(list, activity);

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
    }

    public static void getProfilesByTag(String cpid, Fragment fragment, String tag, SpinKitView progressBar) {
        new GetProfilesByTagTask(cpid, fragment, tag, progressBar).execute();
    }

    static class GetProfilesByTagTask extends AsyncTask<Void, Void, Void> {

        String cpid, tag;

        Fragment fragment;
        List<Level_1_cardModal> list = new ArrayList<>();

        SpinKitView progressBar;

        Circle d;

        public GetProfilesByTagTask(String cpid, Fragment fragment, String tag, SpinKitView progressBar) {
            this.cpid = cpid;
            this.tag = tag;
            this.fragment = fragment;
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            d = new Circle();
            progressBar.setIndeterminateDrawable(d);
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date()+" api call : GetProfilesByTagTask");
            try {
                list = RetrofitClient.getInstance().getApi().getProfilesByTag(cpid, tag).execute().body();

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
            if (list != null && !list.isEmpty()) {
                Log.i("onPostExecute", "list size  " + list.size());
                ((MatchesFragment) fragment).initRecyclerView(list);
                d.stop();
                progressBar.setVisibility(View.GONE);
            } else
                Log.i("onPostExecute", "list is null");
            d.stop();
            progressBar.setVisibility(View.GONE);
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
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date()+" api call : GetLevel2DataTask");
            try {
                list = RetrofitClient.getInstance().getApi().getLevel2DataByCPID(cpid).execute().body();
                // TODO: 14-Sep-23 prepare single rest api
                Customer c = LocalCache.getLoggedInCustomer(activity);
                //contactviewedlist = RetrofitClient.getInstance().getApi().getContactViewedProfiles(c.getProfileId()).execute().body();


            } catch (Exception e) {
                Log.i("ss_nw_call", "GetLevel2DataTask error" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "GetLevel2DataTask onPostExecute calling... ");
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

                Log.i("ss_nw_call", "GetLevel2DataTask onPostExecute list size  " + list.size() + " time is " + new Date());
                activity.startActivity(new Intent(activity, Level2ProfileActivity.class).putExtra("level2data", new Gson().toJson(list.get(0))));
            } else
                Log.i("ss_nw_call", "GetLevel2DataTask onPostExecute list is null  time is " + new Date());
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
            Log.i("ss_nw_call", new Date()+" api call : ViewContactDataTask");
            try {
                // check if package exists or not
                response = RetrofitClient.getInstance().getApi().viewContactData(cpid, vcpid.getProfileId()).execute().body();
                if(response !=  null && response.getResult().equalsIgnoreCase("true")){
                    syncAccountBalance(cpid, activity, null,null, null, false);
                    updateLoggedInCustomerDetails(activity,mobile);
                }

            } catch (Exception e) {
                Log.i("ss_nw_call", "UpdateViewCountTask error" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "UpdateViewCountTask onPostExecute calling... ");
            super.onPostExecute(aVoid);
            if(response !=  null && response.getResult().equalsIgnoreCase("true")){
                ((TextView)activity.findViewById(R.id.address)).setText(vcpid.getAddress());
                ((TextView)activity.findViewById(R.id.mobile1)).setText(vcpid.getMobile1());
                ((TextView)activity.findViewById(R.id.mobile2)).setText(vcpid.getMobile2());
                ((TextView)activity.findViewById(R.id.mobile3)).setText(vcpid.getMobile3());
                ((TextView)activity.findViewById(R.id.email)).setText(vcpid.getEmail());
                activity.findViewById(R.id.contactDetailsLayout).setVisibility(View.GONE);
                activity.findViewById(R.id.viewContactDetailsBtn).setEnabled(false);
                activity.findViewById(R.id.viewContactDetailsBtn).setVisibility(View.GONE);
                activity.findViewById(R.id.contact_card).setVisibility(View.VISIBLE);

                if(vcpid.getMobile2().equalsIgnoreCase(""))
                    activity.findViewById(R.id.call2_link).setVisibility(View.GONE);
            }
            else{
                new BuyMembershipBottomSheetDialog(activity).show(((Level2ProfileActivity)activity).getSupportFragmentManager(), "ModalBottomSheet");
            }

        }
    }

    public static void syncAccountBalance(String cpid, Activity activity, CardView cb_card,CardView membership_card, TextView cb_text, Boolean flag) {
        new SyncAccountBalanceTask(cpid, activity, cb_card, membership_card, cb_text, flag).execute();
    }

    static class SyncAccountBalanceTask extends AsyncTask<Void, Void, Void> {

        String cpid;
        List<OrderModal> response_list;

        Activity activity;
        CardView cb_card;
        CardView membership_card;
        TextView cb_text;
        Boolean flag;

        public SyncAccountBalanceTask(String cpid, Activity activity, CardView cb_card, CardView membership_card,TextView cb_text, Boolean flag) {
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
            Log.i("ss_nw_call", new Date()+" api call : SyncAccountBalanceTask");
            try {
                response_list = RetrofitClient.getInstance().getApi().getActiveOrderByCpid(cpid).execute().body();
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
            if (response_list != null && !response_list.isEmpty()) {
                LocalCache.setActiveOrder(response_list.get(0), activity);
                OrderModal activeOrder = LocalCache.getActiveOrder(activity);
                if (flag && activeOrder != null && activeOrder.getId() != null) {
                    int balance = Integer.parseInt(activeOrder.getMaxCount()) - Integer.parseInt(activeOrder.getUsedCount());
                    cb_card.setVisibility(View.VISIBLE);
                    membership_card.setVisibility(View.VISIBLE);
                    cb_text.setText("Contact Balance : " + balance);
                }
            }
        }
    }

    public static void getEducationList(Activity activity) {
        new GetEducationListTask(activity).execute();
    }
    static class GetEducationListTask extends AsyncTask<Void, Void, Void> {

        List<SingleResponse> response_list;

        Activity activity;

        public GetEducationListTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date()+" api call : GetEducationListTask");
            try {
                educationApiCalled = true;
                response_list = RetrofitClient.getInstance().getApi().getAllEducationList().execute().body();
            } catch (Exception e) {
                Log.i("ss_nw_call", "GetEducationListTask error" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LocalCache.setEducationList(response_list,activity);
        }
    }

    public static void getOccupationList(Activity activity) {
        new GetOccupationListTask(activity).execute();
    }
    static class GetOccupationListTask extends AsyncTask<Void, Void, Void> {

        List<SingleResponse> response_list;

        Activity activity;

        public GetOccupationListTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date()+" api call : GetOccupationListTask");
            try {
                occupationApiCalled = true;
                response_list = RetrofitClient.getInstance().getApi().getAllOccupationList().execute().body();
            } catch (Exception e) {
                Log.i("ss_nw_call", "GetOccupationListTask error" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LocalCache.setOccupationList(response_list , activity);
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
            Log.i("ss_nw_call", new Date()+" api call : AddToShortListedProfilesTask");
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
            Log.i("ss_nw_call", new Date()+" api call : AddToNotInterestedProfilesTask");
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
            Log.i("ss_nw_call", new Date()+" api call : AddToInterestedProfilesTask");
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
            Log.i("ss_nw_call", new Date()+" api call : AddToLikedProfilesTask");
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
            Log.i("ss_nw_call", new Date()+" api call : AddNotificationTask");
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
            Log.i("ss_nw_call", new Date()+" api call : SetLoggedInCustomerTask");
            try {
                customer = RetrofitClient.getInstance().getApi().getCustomerByMobile(mobile).execute().body();
                LocalCache.setMembershipList(RetrofitClient.getInstance().getApi().getAllMembershipPlans().execute().body(), activity);

                if(!educationApiCalled)
                getEducationList(activity);
                if(!occupationApiCalled)
                getOccupationList(activity);

                if (customer != null && !customer.isEmpty()) {
                    Log.i("local_logs", "SendOtpActivity - saving customer" + new Date());
                    LocalCache.setLoggedInCustomer(customer.get(0), activity);


                    LocalCache.setContactViewedList(RetrofitClient.getInstance().getApi().getContactViewedProfiles(customer.get(0).getProfileId()).execute().body(), activity);

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
            Log.i("ss_nw_call", new Date()+" api call : UpdateLoggedInCustomerDetailsTask");
            try {
                customer = RetrofitClient.getInstance().getApi().getCustomerByMobile(mobile).execute().body();
                if (customer != null && !customer.isEmpty()) {
                    Log.i("local_logs", "SendOtpActivity - saving customer" + new Date());
                    LocalCache.setLoggedInCustomer(customer.get(0), activity);
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

        public GetUserNotificationsListTask(Activity activity, Dialog dialog) {
            this.dialog = dialog;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            no_noti_text = dialog.findViewById(R.id.no_noti_text);
            recyclerView = dialog.findViewById(R.id.notificationlistRecyclerview);
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", new Date()+" api call : GetUserNotificationsListTask");
            try {
                Customer c = LocalCache.getLoggedInCustomer(activity);
                notificationsList = RetrofitClient.getInstance().getApi().getUserNotifications(c.getProfileId()).execute().body();
            } catch (Exception e) {
                
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (notificationsList != null && !notificationsList.isEmpty()) {
                NotificationAdapter adapter = new NotificationAdapter(notificationsList, activity, dialog);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                recyclerView.setAdapter(adapter);
            } else {
                recyclerView.setVisibility(View.GONE);
                no_noti_text.setVisibility(View.VISIBLE);
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
                Log.i("ss_nw_call", new Date()+" api call : UpdateViewedNotificationStateTask");
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

    static class GetContactViewedProfilesTask extends AsyncTask<Void, Void, Void> {


        String cpid;
        List<ContactViewedModal> list = new ArrayList<>();
        ContactViewedAdapter adapter;
        RecyclerView contactviewedRecyclerView;
        Activity activity;
        TextView cv_count;

        public GetContactViewedProfilesTask(String cpid, ContactViewedAdapter adapter, RecyclerView contactviewedRecyclerView,TextView cv_count, Activity activity) {
            this.cpid = cpid;
            this.adapter = adapter;
            this.contactviewedRecyclerView = contactviewedRecyclerView;
            this.activity = activity;
            this.cv_count = cv_count;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date()+" api call : GetContactViewedProfilesTask");
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
                cv_count.setText("Total contacts viewed : "+ list.size());
                adapter = new ContactViewedAdapter(list, activity);
                contactviewedRecyclerView.setHasFixedSize(true);
                contactviewedRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                contactviewedRecyclerView.setAdapter(adapter);
            }
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

        public RegisterNewCustomerTask(Customer c, Activity activity, Boolean onboardNewUser, Fragment fragment) {
            this.c = c;
            this.activity = activity;
            progressBar = activity.findViewById(R.id.progressBar1);
            this.onboardNewUser = onboardNewUser;
            this.fragment = fragment;
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
                Log.i("ss_nw_call", new Date()+" api call : RegisterNewCustomerTask");
                persistedCustomerObj = RetrofitClient.getInstance().getApi().registerNewCustomernew(c).execute().body();
                if(persistedCustomerObj != null){
                    LocalCache.setLoggedInCustomer(persistedCustomerObj.get(0),activity);
                    if(onboardNewUser)
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
                }
                else{
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

        public UpdateProfileTask(Customer c, Activity activity, Boolean updateCache) {
            this.c = c;
            this.activity = activity;
            progressBar = activity.findViewById(R.id.progressBar);
            this.updateCache = updateCache;
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
                Log.i("ss_nw_call", new Date()+" api call : UpdateProfileTask");
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
            activity.onBackPressed();
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

        Button savebtn;

        public ValidateLoginMobileTask(Activity activity, String mobile, LinearLayout formLayout, LinearLayout cmLayout, TextInputEditText mobile1, Button savebtn) {
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
                Log.i("ss_nw_call", new Date()+" api call : ValidateLoginMobileTask");
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
                Log.i("ss_nw_call", new Date()+" api call : GetAdminPhoneTask");
                response = RetrofitClient.getInstance().getApi().getAdminPhone().execute().body();
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (response != null && !response.getResult().equalsIgnoreCase("0"))
                LocalCache.setAdminPhone(response.getResult(),activity);
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
                Log.i("ss_nw_call", new Date()+" api call : GetMyMembershipsTask");
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
                Log.i("ss_nw_call", new Date()+" api call : GetMembershipPlansTask");
                list = LocalCache.getMembershipList(activity);
                if (list == null && list.isEmpty())
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
                Log.i("ss_nw_call", new Date()+" api call : ValidateAdminCodeTask");
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
                Log.i("ss_nw_call", new Date()+" api call : CheckIsLiveTask");
                obj = RetrofitClient.getInstance().getApi().isLive().execute().body();
            } catch (Exception e) {
                Log.i("local_logs", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(obj != null){
                LocalCache.setIsLive(obj.getResult(),activity);
            }
            else
                LocalCache.setIsLive("server error",activity);

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
                Log.i("ss_nw_call", new Date()+" api call : SyncStatsTask");
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
                String malecount = "0", femalecount = "0";
                for (Stat g : list) {
                    if (g.getGender().equalsIgnoreCase("male"))
                        malecount = g.getCount();
                    else if (g.getGender().equalsIgnoreCase("female"))
                        femalecount = g.getCount();
                }
                ((TextView) activity.findViewById(R.id.totalmalecount)).setText(malecount);
                ((TextView) activity.findViewById(R.id.totalfemalecount)).setText(femalecount);
            }
        }
    }

    static class GetFilteredLevel1DataTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        List<Level_1_cardModal> list;

        FilterModal filter;

        Fragment fragment;


        public GetFilteredLevel1DataTask(Activity activity, Fragment fragment, FilterModal filter) {
            this.filter = filter;
            this.activity = activity;
            this.fragment = fragment;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date()+" api call : GetFilteredLevel1DataTask");
                list = RetrofitClient.getInstance().getApi().getFilteredLevel1Profiles(filter).execute().body();

            } catch (Exception e) {
                Log.i("local_logs", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            HelperUtils.searchProfileBottomSheetDialog.dismiss();
            ((HomeFragment) fragment).initLevel_1_CardProfilesRecyclerView(list);
            if (list != null && !list.isEmpty())
                LocalCache.setLevel1List(list, activity);

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
                Log.i("ss_nw_call", new Date()+" api call : GetFilteredLevel2DataTask");
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
                Log.i("ss_nw_call", new Date()+" api call : SearchProfileTask");
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
                Log.i("ss_nw_call", new Date()+" api call : AssignMembershipTask");
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
                Log.i("ss_nw_call", new Date()+" api call : DynamicLayoutCreationTask");
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
                Log.i("ss_nw_call", new Date()+" api call : PersistBitmapTask");
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
                createBitmapForViewAsync(v, activity,obj);

            }
        } catch (Exception e) {
            Log.i("local_logs", "DynamicLayoutCreationTask " + e.toString());
        }


    }

    // Function to create the bitmap asynchronously for each view
    private static void createBitmapForViewAsync(View view, Activity activity,Customer obj) {
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
        if(!obj.getB64().isEmpty()){
            Glide.with(activity)
                    .load(HelperUtils.convertBitmapToDrawable(activity,HelperUtils.convertBase64ToBitmap(obj.getB64())))
                    .placeholder(HelperUtils.convertBitmapToDrawable(activity,HelperUtils.convertBase64ToBitmap(obj.getB64())))
                    .into((ImageView) view.findViewById(R.id.profilephotoaddresss));
        }

        ((TextView) view.findViewById(R.id.profileid)).setText("Profile id : A" + obj.getProfileId());
        ((TextView) view.findViewById(R.id.name)).setText(obj.getFirstname() + " " + obj.getLastname());
        ((TextView) view.findViewById(R.id.birthdate)).setText(obj.getBirthdate()+"  ( "+obj.getAge()+" yrs )");
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

                int percent = (count*100)/blist.size();
                ((TextView) activity.findViewById(R.id.bitmapCount)).setText(percent+" % completed");
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




}
