package com.example.ss1.api;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ss1.LocalCache;
import com.example.ss1.MainActivity;
import com.example.ss1.R;
import com.example.ss1.activity.Level2ProfileActivity;
import com.example.ss1.adapters.ContactViewedAdapter;
import com.example.ss1.adapters.NotificationAdapter;
import com.example.ss1.modal.ContactViewedModal;
import com.example.ss1.modal.Customer;
import com.example.ss1.modal.Level_1_cardModal;
import com.example.ss1.modal.Level_2_Modal;
import com.example.ss1.modal.NotificationModal;
import com.example.ss1.modal.OrderModal;
import com.example.ss1.modal.SingleResponse;
import com.example.ss1.ui.dashboard.MatchesFragment;
import com.example.ss1.ui.home.HomeFragment;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
            //HelperUtility.addLastTenDaysCount(list, activity);
            if (list != null && !list.isEmpty()) {
                Collections.shuffle(list);

                if (LocalCache.retrieveLevel1List(activity).isEmpty())
                    ((HomeFragment) fragment).initLevel_1_CardProfilesRecyclerView(list);

                if (override)
                    ((HomeFragment) fragment).initLevel_1_CardProfilesRecyclerView(list);


                LocalCache.saveLevel1List(list, activity);

                d.stop();
                progressBar.setVisibility(View.GONE);
            } else{
                Log.i("onPostExecute", "list is null");
                LocalCache.saveLevel1List(new ArrayList<>(), activity);
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

    public static void getLevel2Data(String cpid, Activity activity, Boolean flag) {
        ApiUtils.vibrateFunction(activity);
        new GetLevel2DataTask(cpid, activity, flag).execute();
    }

    static class GetLevel2DataTask extends AsyncTask<Void, Void, Void> {

        String cpid;
        Activity activity;

        Boolean flag;
        List<Level_2_Modal> list = new ArrayList<>();

        public GetLevel2DataTask(String cpid, Activity activity, Boolean flag) {
            this.cpid = cpid;
            this.activity = activity;
            this.flag = flag;
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
                Log.i("ss_nw_call", "GetLevel2DataTask onPostExecute list size  " + list.size() + " time is " + new Date());
                activity.startActivity(new Intent(activity, Level2ProfileActivity.class).putExtra("level2data", new Gson().toJson(list.get(0))).putExtra("enableDisableContactViewButton", flag));
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

            } catch (Exception e) {
                Log.i("ss_nw_call", "GetCountLeftTask doInBackground error" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ss_nw_call", "GetCountLeftTask onPostExecute calling... ");
            super.onPostExecute(aVoid);
            if (flag && !response_list.isEmpty()) {
                LocalCache.saveActiveOrder(response_list.get(0), activity);
                OrderModal activeOrder = LocalCache.retrieveActiveOrder(activity);
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
                    LocalCache.saveLoggedInCustomer(customer.get(0), activity);
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
                    LocalCache.saveLoggedInCustomer(customer.get(0), activity);
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
                Customer c = LocalCache.retrieveLoggedInCustomer(activity);
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


}
