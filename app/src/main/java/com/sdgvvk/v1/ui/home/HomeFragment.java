package com.sdgvvk.v1.ui.home;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdgvvk.v1.LocalCache;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.SearchProfileBottomSheetDialog;
import com.sdgvvk.v1.activity.RegistrationActivity;
import com.sdgvvk.v1.adapters.Level_1_profilecardAdapter;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.api.HelperUtils;
import com.sdgvvk.v1.modal.Customer;
import com.sdgvvk.v1.modal.Level_1_cardModal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

public class HomeFragment extends Fragment {

    Boolean forceupdate = false;
    private static final int REQ_CODE = 100;

    static Activity activity;

    public static final int PICK_IMAGE_REQUEST = 1;
    public CoordinatorLayout coordinatorLayout;

    SpinKitView progressBar;


    ImageView notification, profilePhoto;

    CardView searchProfiles;

    private Level_1_profilecardAdapter level1CardAdapter;
    private List<Level_1_cardModal> rowsArrayList = new ArrayList<>();
    private RecyclerView recyclerView, level1cardsRecyclerView;
    private View view;
    boolean isLoading = false;
    Customer loggedinCustomer;
    List<Level_1_cardModal> level1list;
    SwipeRefreshLayout swipeRefreshLayout;

    public void init(){
        initUIElements();
        initOnclickListener();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i("ss_nw_call", new Date() + "lifecycle : HomeFragment onCreateView");
        view = inflater.inflate(R.layout.fragment_home, container, false);
        activity = this.getActivity();
        checkforappupdates();
        init();
        HelperUtils.checkNetworkStatus(this.getActivity());


        loggedinCustomer = LocalCache.getLoggedInCustomer(this.getActivity());

        // NON-REGISTERED USER FLOW
        if (loggedinCustomer.getProfileId() == null)
            onboardNewUser();
        else {

            Boolean isAdmin = loggedinCustomer.getIsAdmin() != null && loggedinCustomer.getIsAdmin().equalsIgnoreCase("1") ? true : false;

            // add blocker for existing user for filling complete profile
            if (!isAdmin && ( loggedinCustomer.getHeight().isEmpty() || loggedinCustomer.getProfilephotoaddress() == null ) ) {
                forceupdate = true;
                Intent intent = new Intent(this.getActivity(), RegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("forceupdate", true);
                startActivity(intent);
            } else {
                // REGISTERED USER FLOW
                if (ApiCallUtil.cpid != null) {
                    // onboarding complete : show successful registration msg
                    MediaPlayer.create(activity, R.raw.done_sound).start();
                    HelperUtils.showDialog(activity, R.drawable.success_icon, "Profile created !!!", "profild id : " + ApiCallUtil.cpid);
                    ApiCallUtil.cpid = null;
                }

                syncLoggedInCustomer();

                level1list = LocalCache.getLevel1List(this.getActivity());

                setProfileIcon();

                if (!level1list.isEmpty()) {
                    initLevel_1_CardProfilesRecyclerView(level1list);
                }

                if (loggedinCustomer.getProfileId() != null)
                    ApiCallUtil.getAllProfiles(loggedinCustomer.getProfileId(), this, progressBar, this.getActivity(), false);

            }

        }


        return view;
    }


    private void onboardNewUser() {
        Log.i("ss_nw_call", new Date() + "lifecycle : cpid " + ApiCallUtil.cpid);
        Log.i("ss_nw_call", new Date() + "lifecycle : onboarding new user");
        Intent intent = new Intent(this.getActivity(), RegistrationActivity.class);
        intent.putExtra("onboarding", true);
        startActivity(intent);
    }


    private void setProfileIcon() {
        if (loggedinCustomer != null && loggedinCustomer.getProfileId() != null) {
            Glide.with(this.getActivity())
                    .load(loggedinCustomer.getProfilephotoaddress() != null ? loggedinCustomer.getProfilephotoaddress() : R.drawable.oops)
                    .placeholder(R.drawable.oops)
                    .into(profilePhoto);
        }
    }

    private void syncLoggedInCustomer() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            ApiCallUtil.updateLoggedInCustomerDetails(this.getActivity(), user.getPhoneNumber().replace("+91", ""));
        }
    }

    private void initUIElements() {
        profilePhoto = view.findViewById(R.id.profilePhoto);
        coordinatorLayout = view.findViewById(R.id.level1CoordinatorLayout);
        progressBar = view.findViewById(R.id.progressBar);
        level1cardsRecyclerView = view.findViewById(R.id.level1cardsRecyclerView);
        notification = view.findViewById(R.id.notification);
        searchProfiles = view.findViewById(R.id.searchProfiles);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
    }

    private void initOnclickListener() {
        /*level1cardsRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
                        // bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });*/

        notification.setOnClickListener(view -> {
            showNotificationListDialog();
        });

        profilePhoto.setOnClickListener(view -> showUserProfile());

        searchProfiles.setOnClickListener(view -> searchProfiles());

        // Refresh  the layout
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    ApiCallUtil.getAllProfiles(loggedinCustomer.getProfileId(), this, progressBar, this.getActivity(), true);
                    swipeRefreshLayout.setRefreshing(false);
                }
        );


    }

    private void searchProfiles() {
        HelperUtils.vibrateFunction(this.getActivity());
        SearchProfileBottomSheetDialog searchProfileBottomSheetDialog = new SearchProfileBottomSheetDialog(this.getActivity(), this);
        HelperUtils.searchProfileBottomSheetDialog = searchProfileBottomSheetDialog;
        searchProfileBottomSheetDialog.show(this.getActivity().getSupportFragmentManager(), "ModalBottomSheet");
    }

    private void showUserProfile() {
        HelperUtils.vibrateFunction(this.getActivity());
        ApiCallUtil.getLevel2Data(loggedinCustomer.getProfileId(), activity);
    }

    private void loadMore() {
        rowsArrayList.add(null);
        level1CardAdapter.notifyItemInserted(rowsArrayList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            rowsArrayList.remove(rowsArrayList.size() - 1);
            int scrollPosition = rowsArrayList.size();
            level1CardAdapter.notifyItemRemoved(scrollPosition);
            int currentSize = scrollPosition;

            // Next load more option is to be shown after every 10 items.
            int nextLimit = currentSize + 20;

            while (currentSize - 1 < nextLimit) {
                //rowsArrayList.add(new Level_1_cardModal(String.valueOf(currentSize), "fname", "lname", "06/09/1994", "age", "address", "0", "0", "0"));

                currentSize++;
            }

            level1CardAdapter.notifyDataSetChanged();
            isLoading = false;
        }, 100);
    }


    public void initLevel_1_CardProfilesRecyclerView(List<Level_1_cardModal> list) {
        if (view != null) {
            recyclerView = view.findViewById(R.id.level1cardsRecyclerView);
            if (recyclerView != null) {
                if (list != null) {
                    Level_1_cardModal tempObj = null;
                    ListIterator<Level_1_cardModal> iterator = list.listIterator();

                    while (iterator.hasNext()) {
                        Level_1_cardModal obj = iterator.next();

                        if (obj.getIsAdmin().equalsIgnoreCase("999")) {
                            tempObj = obj;
                            iterator.remove();
                        }
                    }

                    if (tempObj != null) {
                        list.add(0, tempObj);
                    }


                    rowsArrayList = list;
                    level1CardAdapter = new Level_1_profilecardAdapter(progressBar, view, list, this, this.getActivity());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
                    recyclerView.setAdapter(level1CardAdapter);
                } else {
                    list = new ArrayList<>();
                    rowsArrayList = list;
                    level1CardAdapter = new Level_1_profilecardAdapter(progressBar, view, list, this, this.getActivity());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
                    recyclerView.setAdapter(level1CardAdapter);
                    HelperUtils.showSearchingDialog(this, progressBar, this.getActivity(), R.drawable.searching_gif, "0 profiles found", "try again");
                }
            }
        }

    }

    public void showSnackBar(String content) {
        Snackbar snackbar = Snackbar
                .make(view.findViewById(R.id.level1CoordinatorLayout), content, Snackbar.LENGTH_LONG)
                .setAction("OK", view -> {
                });
        snackbar.show();
    }

    public void showNotificationListDialog() {
        HelperUtils.vibrateFunction(this.getActivity());
        Dialog d = new Dialog(this.getActivity());
        d.setContentView(R.layout.notificationlist_dialog);

        d.setContentView(R.layout.notificationlist_dialog);
        ApiCallUtil.getUserNotifications(this.getActivity(), d);

        d.setCanceledOnTouchOutside(true);
        d.setCancelable(true);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.show();
    }

    public static Activity getFragmentActivity() {
        return activity;
    }

    private String validateOnBoardingForm(Dialog d) {
        String errorTxt = "";
        List<String> list = new ArrayList<>();

        String name = ((TextInputEditText) d.findViewById(R.id.name)).getText().toString().trim();
        String birthdate = ((TextInputEditText) d.findViewById(R.id.birthdate)).getText().toString().trim();
        String education = ((TextInputEditText) d.findViewById(R.id.education)).getText().toString().trim();
        String occupation = ((TextInputEditText) d.findViewById(R.id.occupation)).getText().toString().trim();
        String income = ((TextInputEditText) d.findViewById(R.id.income)).getText().toString().trim();
        String caste = ((TextInputEditText) d.findViewById(R.id.caste)).getText().toString().trim();
        String address = ((TextInputEditText) d.findViewById(R.id.address)).getText().toString().trim();

        String gender = ((AutoCompleteTextView) d.findViewById(R.id.gender)).getText().toString().trim();
        String status = ((AutoCompleteTextView) d.findViewById(R.id.marriagestatus)).getText().toString().trim();
        String height = ((AutoCompleteTextView) d.findViewById(R.id.height)).getText().toString().trim();
        String zodiac = ((AutoCompleteTextView) d.findViewById(R.id.zodiac)).getText().toString().trim();
        String religion = ((AutoCompleteTextView) d.findViewById(R.id.religion)).getText().toString().trim();
        String bloodgroup = ((AutoCompleteTextView) d.findViewById(R.id.bloodgroup)).getText().toString().trim();
        String city = ((AutoCompleteTextView) d.findViewById(R.id.city)).getText().toString().trim();

        if (name.isEmpty())
            list.add("संपूर्ण नाव");
        if (gender.isEmpty())
            list.add("gender");
        if (birthdate.isEmpty())
            list.add("जन्म तारीख");
        if (education.isEmpty())
            list.add("शिक्षण");
        if (occupation.isEmpty())
            list.add("नोकरी / व्यवसाय");
        if (income.isEmpty())
            list.add("महिना उत्पन्न");
        if (status.isEmpty())
            list.add("status");
        if (height.isEmpty())
            list.add("उंची");
        if (zodiac.isEmpty())
            list.add("रास");
        if (religion.isEmpty())
            list.add("धर्म");
        if (caste.isEmpty())
            list.add("जात");
        if (bloodgroup.isEmpty())
            list.add("bloodgroup");
        if (city.isEmpty())
            list.add("city");
        if (address.isEmpty())
            list.add("address");

        if (ApiCallUtil.b64 == null)
            list.add("photo");


        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                errorTxt = errorTxt + " " + list.get(i);
                if (!(i == list.size() - 1))
                    errorTxt = errorTxt + ",";
            }
        }

        return errorTxt;

    }

    private void checkforappupdates() {
        Log.e("homefragment", "mainactivity calling checkforappupdates");
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this.getActivity());

// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.IMMEDIATE, this.getActivity(), REQ_CODE);
                } catch (IntentSender.SendIntentException e) {
                    Log.e("ImageUtils", e.toString());
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.i("ss_nw_call", new Date() + "lifecycle : HomeFragment onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i("ss_nw_call", new Date() + "lifecycle : HomeFragment onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i("ss_nw_call", new Date() + "lifecycle : HomeFragment onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStop() {
        Log.i("ss_nw_call", new Date() + "lifecycle : HomeFragment onStop");
        super.onStop();
    }

    @Override
    public void onStart() {
        Log.i("ss_nw_call", new Date() + "lifecycle : HomeFragment onStart");
        super.onStart();
    }

    @Override
    public void onDestroy() {
        Log.i("ss_nw_call", new Date() + "lifecycle : HomeFragment onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.i("ss_nw_call", new Date() + "lifecycle : HomeFragment onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        Log.i("ss_nw_call", new Date() + "lifecycle : HomeFragment onDetach");
        super.onDetach();
    }

    @Override
    public void onPause() {
        Log.i("ss_nw_call", new Date() + "lifecycle : HomeFragment onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.i("ss_nw_call", new Date() + "lifecycle : HomeFragment onResume");
        super.onResume();
        if (ApiCallUtil.clicked_level2activity)
            ApiCallUtil.clicked_level2activity = false;
        else{
            if(!forceupdate)
                ApiCallUtil.getAllProfiles(loggedinCustomer.getProfileId(), this, progressBar, this.getActivity(), true);
        }

    }

}