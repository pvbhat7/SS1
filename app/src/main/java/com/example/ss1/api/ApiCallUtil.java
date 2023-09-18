package com.example.ss1.api;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ss1.LocalCache;
import com.example.ss1.MainActivity;
import com.example.ss1.R;
import com.example.ss1.activity.AdminZoneActivity;
import com.example.ss1.activity.Level2ProfileActivity;
import com.example.ss1.adapters.BuyMembershipAdapter;
import com.example.ss1.adapters.ContactViewedAdapter;
import com.example.ss1.adapters.MyMembershipAdapter;
import com.example.ss1.adapters.NotificationAdapter;
import com.example.ss1.adapters.SearchedMembersAdapter;
import com.example.ss1.modal.ContactViewedModal;
import com.example.ss1.modal.Customer;
import com.example.ss1.modal.FilterModal;
import com.example.ss1.modal.Stat;
import com.example.ss1.modal.Level_1_cardModal;
import com.example.ss1.modal.Level_2_Modal;
import com.example.ss1.modal.MembershipModal;
import com.example.ss1.modal.MyMembershipModal;
import com.example.ss1.modal.NotificationModal;
import com.example.ss1.modal.OrderModal;
import com.example.ss1.modal.SingleResponse;
import com.example.ss1.ui.dashboard.MatchesFragment;
import com.example.ss1.ui.home.HomeFragment;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ApiCallUtil {


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

    public static void getContactViewedProfiles(String cpid, ContactViewedAdapter adapter, RecyclerView contactviewedRecyclerView, Activity activity) {
        new GetContactViewedProfilesTask(cpid, adapter, contactviewedRecyclerView, activity).execute();
    }

    public static void registerProfile(Customer customer, Activity activity, Boolean onboardNewUser, Fragment fragment) {
        new RegisterNewCustomerTask(customer, activity, onboardNewUser, fragment).execute();
    }

    public static void updateProfile(Customer customer, Activity activity,Boolean updateCache) {
        new UpdateProfileTask(customer, activity , updateCache).execute();
    }

    public static void validateLoginMobile(Activity activity, String mobile, LinearLayout formLayout, LinearLayout cmLayout, TextInputEditText mobile1, Button savebtn) {
        new ValidateLoginMobileTask(activity, mobile, formLayout, cmLayout, mobile1, savebtn).execute();
    }

    public static void validateAdminCode(Dialog d , String inputCode,Activity activity) {
        new ValidateAdminCodeTask(d,inputCode,activity).execute();
    }

    public static void syncStats(Activity activity) {
        new SyncStatsTask(activity).execute();
    }

    public static void filterProfiles(Activity activity,Fragment fragment , FilterModal modal) {
        new GetFilteredLevel1DataTask(activity,fragment,modal).execute();
    }

    public static void getMyMemberships(String cpid, RecyclerView recyclerView, Activity activity) {
        new GetMyMembershipsTask(cpid, activity, recyclerView).execute();
    }

    public static void getMembershipPlans(RecyclerView recyclerView, Activity activity) {
        new GetMembershipPlansTask(activity, recyclerView).execute();
    }

    public static void searchProfilesBy(Dialog d, Activity activity, String searchBy, String value, RecyclerView recyclerView,LinearLayout downLayout) {
        new SearchProfileTask(d,activity, searchBy, value , recyclerView,downLayout).execute();
    }

    public static void assignMembership(OrderModal o, Activity activity) {
        new AssignMembershipTask(o,activity).execute();
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
            Log.i("ss_nw_call", "onPreExecute");

            progressBar.setIndeterminateDrawable(d);
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", "doInBackground");
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
            Log.i("ss_nw_call", "onPreExecute");
            d = new Circle();
            progressBar.setIndeterminateDrawable(d);
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", "doInBackground");
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
            Log.i("ss_nw_call", "GetLevel2DataTask onPreExecute");
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", "GetLevel2DataTask doInBackground calling...");
            try {
                list = RetrofitClient.getInstance().getApi().getLevel2DataByCPID(cpid).execute().body();
                // TODO: 14-Sep-23 prepare single rest api
                Customer c = LocalCache.getLoggedInCustomer(activity);
                //contactviewedlist = RetrofitClient.getInstance().getApi().getContactViewedProfiles(c.getProfileId()).execute().body();


            } catch (Exception e) {
                Log.i("ss_nw_call", "GetLevel2DataTask doInBackground error" + e);
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
                if(contactviewedlist != null && contactviewedlist.size() > 0){
                    for(ContactViewedModal modal : contactviewedlist){
                        if(modal.getVcpid().equalsIgnoreCase(cpid))
                            list.get(0).setContactViewed(true);
                    }
                }

                Log.i("ss_nw_call", "GetLevel2DataTask onPostExecute list size  " + list.size() + " time is " + new Date());
                activity.startActivity(new Intent(activity, Level2ProfileActivity.class).putExtra("level2data", new Gson().toJson(list.get(0))));
            } else
                Log.i("ss_nw_call", "GetLevel2DataTask onPostExecute list is null  time is " + new Date());
        }
    }

    public static void viewContactData(String cpid, String vcpid, Activity activity) {
        new ViewContactDataTask(cpid, vcpid, activity).execute();
    }

    static class ViewContactDataTask extends AsyncTask<Void, Void, Void> {

        String cpid;

        String vcpid;

        SingleResponse response;
        Activity activity;

        public ViewContactDataTask(String cpid, String vcpid, Activity activity) {
            this.cpid = cpid;
            this.vcpid = vcpid;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            Log.i("ss_nw_call", "UpdateViewCountTask onPreExecute");
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", "UpdateViewCountTask doInBackground calling...");
            try {
                // check if package exists or not
                response = RetrofitClient.getInstance().getApi().viewContactData(cpid, vcpid).execute().body();
                syncAccountBalance(cpid, activity, null, null, false);
            } catch (Exception e) {
                Log.i("ss_nw_call", "UpdateViewCountTask doInBackground error" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "UpdateViewCountTask onPostExecute calling... ");
            super.onPostExecute(aVoid);

        }
    }

    public static void syncAccountBalance(String cpid, Activity activity, CardView cb_card, TextView cb_text, Boolean flag) {
        new SyncAccountBalanceTask(cpid, activity, cb_card, cb_text, flag).execute();
    }

    static class SyncAccountBalanceTask extends AsyncTask<Void, Void, Void> {

        String cpid;
        List<OrderModal> response_list;

        Activity activity;
        CardView cb_card;
        TextView cb_text;
        Boolean flag;

        public SyncAccountBalanceTask(String cpid, Activity activity, CardView cb_card, TextView cb_text, Boolean flag) {
            this.cpid = cpid;
            this.activity = activity;
            this.cb_card = cb_card;
            this.cb_text = cb_text;
            this.flag = flag;
        }

        @Override
        protected void onPreExecute() {
            Log.i("ss_nw_call", "GetCountLeftTask onPreExecute");
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", "GetCountLeftTask doInBackground calling...");
            try {
                response_list = RetrofitClient.getInstance().getApi().getActiveOrderByCpid(cpid).execute().body();
                LocalCache.setContactViewedList(RetrofitClient.getInstance().getApi().getContactViewedProfiles(cpid).execute().body() , activity);
            } catch (Exception e) {
                Log.i("ss_nw_call", "GetCountLeftTask doInBackground error" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "GetCountLeftTask onPostExecute calling... ");
            super.onPostExecute(aVoid);
            if (flag && response_list != null && !response_list.isEmpty()) {
                LocalCache.setActiveOrder(response_list.get(0), activity);
                OrderModal activeOrder = LocalCache.getActiveOrder(activity);
                if (activeOrder != null && activeOrder.getId() != null) {
                    int balance = Integer.parseInt(activeOrder.getMaxCount()) - Integer.parseInt(activeOrder.getUsedCount());
                    cb_card.setVisibility(View.VISIBLE);
                    cb_text.setText("Contact Balance : " + balance);
                }
            }
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
            Log.i("ss_nw_call", "AddToShortListedProfilesTask onPreExecute");
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", "AddToShortListedProfilesTask doInBackground calling...");
            try {
                RetrofitClient.getInstance().getApi().addToShortListedProfiles(cpid, vcpid).execute().body();

            } catch (Exception e) {
                Log.i("ss_nw_call", "AddToShortListedProfilesTask doInBackground error" + e.toString());
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
            Log.i("ss_nw_call", "AddToNotInterestedProfilesTask onPreExecute");
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", "AddToNotInterestedProfilesTask doInBackground calling...");
            try {
                RetrofitClient.getInstance().getApi().addToNotInterestedProfiles(cpid, vcpid).execute().body();

            } catch (Exception e) {
                Log.i("ss_nw_call", "GetCountLeftTask doInBackground error" + e.toString());
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
            Log.i("ss_nw_call", "AddToInterestedProfilesTask onPreExecute");
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", "AddToInterestedProfilesTask doInBackground calling...");
            try {
                RetrofitClient.getInstance().getApi().addToInterestedProfiles(cpid, vcpid).execute().body();

            } catch (Exception e) {
                Log.i("ss_nw_call", "AddToInterestedProfilesTask doInBackground error" + e.toString());
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
            Log.i("ss_nw_call", "AddToLikedProfilesTask onPreExecute");
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", "AddToLikedProfilesTask doInBackground calling...");
            try {
                RetrofitClient.getInstance().getApi().addToLikedProfiles(cpid, vcpid).execute().body();

            } catch (Exception e) {
                Log.i("ss_nw_call", "AddToLikedProfilesTask doInBackground error" + e.toString());
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
            Log.i("ss_nw_call", "AddNotificationTask onPreExecute");
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", "AddNotificationTask doInBackground calling...");
            try {
                RetrofitClient.getInstance().getApi().addNotification(modal).execute().body();

            } catch (Exception e) {
                Log.i("ss_nw_call", "AddNotificationTask doInBackground error" + e.toString());
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
            Log.i("ss_nw_call", "SetLoggedInCustomerTask onPreExecute");
            super.onPreExecute();
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminateDrawable(d);
            }
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", "SetLoggedInCustomerTask doInBackground calling...");
            try {
                customer = RetrofitClient.getInstance().getApi().getCustomerByMobile(mobile).execute().body();
                if (customer != null && !customer.isEmpty()) {
                    Log.i("local_logs", "SendOtpActivity - saving customer" + new Date());
                    LocalCache.setLoggedInCustomer(customer.get(0), activity);

                    if(LocalCache.getContactViewedList(activity).isEmpty()) {
                        List<ContactViewedModal> contactViewedModals = RetrofitClient.getInstance().getApi().getContactViewedProfiles(customer.get(0).getProfileId()).execute().body();
                        LocalCache.setContactViewedList(contactViewedModals, activity);
                    }
                    if(LocalCache.getMembershipList(activity).isEmpty()) {
                        List<MembershipModal> membershipModals = RetrofitClient.getInstance().getApi().getAllMembershipPlans().execute().body();
                        LocalCache.setMembershipList(membershipModals, activity);
                    }
                }
            } catch (Exception e) {
                Log.i("ss_nw_call", "SetLoggedInCustomerTask doInBackground error" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "SetLoggedInCustomerTask onPostExecute calling... ");
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
            Log.i("ss_nw_call", "SetLoggedInCustomerTask onPreExecute");
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", "SetLoggedInCustomerTask doInBackground calling...");
            try {
                customer = RetrofitClient.getInstance().getApi().getCustomerByMobile(mobile).execute().body();
                if (customer != null && !customer.isEmpty()) {
                    Log.i("local_logs", "SendOtpActivity - saving customer" + new Date());
                    LocalCache.setLoggedInCustomer(customer.get(0), activity);
                }
            } catch (Exception e) {
                Log.i("ss_nw_call", "SetLoggedInCustomerTask doInBackground error" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "SetLoggedInCustomerTask onPostExecute calling... ");
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
            Log.i("ss_nw_call", "SetLoggedInCustomerTask onPreExecute");
            super.onPreExecute();
            no_noti_text = dialog.findViewById(R.id.no_noti_text);
            recyclerView = dialog.findViewById(R.id.notificationlistRecyclerview);
        }

        protected Void doInBackground(Void... params) {
            Log.i("ss_nw_call", "SetLoggedInCustomerTask doInBackground calling...");
            try {
                Customer c = LocalCache.getLoggedInCustomer(activity);
                notificationsList = RetrofitClient.getInstance().getApi().getUserNotifications(c.getProfileId()).execute().body();
            } catch (Exception e) {
                Log.i("ss_nw_call", "SetLoggedInCustomerTask doInBackground error" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "SetLoggedInCustomerTask onPostExecute calling... ");
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

        public GetContactViewedProfilesTask(String cpid, ContactViewedAdapter adapter, RecyclerView contactviewedRecyclerView, Activity activity) {
            this.cpid = cpid;
            this.adapter = adapter;
            this.contactviewedRecyclerView = contactviewedRecyclerView;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
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
        List<Customer> loggedInCustomer;

        SingleResponse response;
        String error;
        SpinKitView progressBar;
        Circle d = new Circle();
        List<Level_1_cardModal> list = new ArrayList<>();

        Boolean onboardNewUser = false;

        Fragment fragment;

        public RegisterNewCustomerTask(Customer c, Activity activity, Boolean onboardNewUser, Fragment fragment) {
            this.c = c;
            this.activity = activity;
            progressBar = activity.findViewById(R.id.progressBar);
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
                response = RetrofitClient.getInstance().getApi().registerNewCustomer(c).execute().body();
                if (onboardNewUser) {
                    if (response != null && !response.getResult().equalsIgnoreCase("0")) {
                        loggedInCustomer = RetrofitClient.getInstance().getApi().getCustomerByMobile(c.getMobile1()).execute().body();
                        if (loggedInCustomer != null && !loggedInCustomer.isEmpty()) {
                            LocalCache.setLoggedInCustomer(loggedInCustomer.get(0), activity);
                            list = RetrofitClient.getInstance().getApi().getAllCustomerProfiles(loggedInCustomer.get(0).getProfileId()).execute().body();
                        }
                    }
                }

            } catch (Exception e) {
                error = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressBar != null && !onboardNewUser)
                progressBar.setVisibility(View.GONE);

            if (response != null && !response.getResult().equalsIgnoreCase("0")) {
                MediaPlayer.create(activity, R.raw.done_sound).start();
                HelperUtils.showDialog(activity, R.drawable.success_icon, "Profile created !!!", "id : " + response.getResult());

                if (onboardNewUser) {
                    if (list != null && !list.isEmpty()) {
                        //Collections.shuffle(list);
                        LocalCache.setLevel1List(list, activity);
                        ((HomeFragment) fragment).initLevel_1_CardProfilesRecyclerView(list);
                        if (progressBar != null)
                            progressBar.setVisibility(View.GONE);
                    }
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
                    savebtn.setEnabled(true);
                    cmLayout.setVisibility(View.GONE);
                }
            } else {
                cmLayout.setVisibility(View.VISIBLE);
                HelperUtils.showDialog(activity, R.drawable.failed_icon, "Error occured !!!", error);
            }
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

        String inputCode ;
        SingleResponse obj;
        Dialog d;

        Activity activity;

        public ValidateAdminCodeTask(Dialog d , String inputCode, Activity activity) {
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
                obj = RetrofitClient.getInstance().getApi().getAdminCode().execute().body();

            } catch (Exception e) {
                Log.i("local_logs", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(obj != null){
                if(inputCode.equalsIgnoreCase(obj.getResult())){
                    d.dismiss();
                   activity.startActivity(new Intent(activity, AdminZoneActivity.class));
                }
                else{
                    d.findViewById(R.id.errorcode).setVisibility(View.VISIBLE);
                    ((TextView)d.findViewById(R.id.errorcode)).setText("wrong pin entered , try again");
                }
            }
            else{
                ((TextView)d.findViewById(R.id.errorcode)).setText("wrong pin entered , try again");
            }
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
                list = RetrofitClient.getInstance().getApi().getStats().execute().body();

            } catch (Exception e) {
                Log.i("local_logs", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(list != null){
               LocalCache.setGenderStat(list,activity);
               String malecount  = "0", femalecount = "0";
               for(Stat g : list){
                   if(g.getGender().equalsIgnoreCase("male"))
                       malecount = g.getCount();
                   else if(g.getGender().equalsIgnoreCase("female"))
                       femalecount = g.getCount();
               }
                ((TextView)activity.findViewById(R.id.totalmalecount)).setText(malecount);
                ((TextView)activity.findViewById(R.id.totalfemalecount)).setText(femalecount);
            }
        }
    }

    static class GetFilteredLevel1DataTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        List<Level_1_cardModal> list;

        FilterModal filter;

        Fragment fragment;


        public GetFilteredLevel1DataTask(Activity activity , Fragment fragment , FilterModal filter) {
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
            if(list != null && !list.isEmpty())
            LocalCache.setLevel1List(list, activity);

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
            this.value = value ;
            this.recyclerView = recyclerView;
            this.downLayout = downLayout;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
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
            if(list != null && !list.isEmpty()){
                downLayout.setVisibility(View.VISIBLE);
                d.findViewById(R.id.errorTxt).setVisibility(View.GONE);
                SearchedMembersAdapter adapter = new SearchedMembersAdapter(d,list,activity);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                recyclerView.setAdapter(adapter);
            }
            else{
                downLayout.setVisibility(View.GONE);
                d.findViewById(R.id.errorTxt).setVisibility(View.VISIBLE);
            }


        }
    }

    static class AssignMembershipTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        OrderModal orderModal;

        SingleResponse response ;


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
                response = RetrofitClient.getInstance().getApi().assignMembership(orderModal).execute().body();

            } catch (Exception e) {
                Log.i("local_logs", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(response != null && !response.getResult().equalsIgnoreCase("0")){
                MediaPlayer.create(activity, R.raw.done_sound).start();
                HelperUtils.showDialog(activity, R.drawable.success_icon, "Membership assigned !!!", "id : " + response.getResult());
            }
            else{
                HelperUtils.showDialog(activity, R.drawable.failed_icon, "Error occured !!!", "id : " + response.getResult());
            }
            //{"cpid":"26","endDate":"09/10/2024","membershipId":"","paymentmode":"Phone Pe","startDate":"21/09/2023","txnDate":"21/09/2023"}

        }
    }


}
