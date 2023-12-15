package com.sdgvvk.v1.ui.home;


import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sdgvvk.v1.LocalCache;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.activity.ContactViewedActivity;
import com.sdgvvk.v1.activity.Level2ProfileActivity;
import com.sdgvvk.v1.activity.RegistrationActivity;
import com.sdgvvk.v1.adapters.Level_1_profilecardAdapter;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.api.HelperUtils;
import com.sdgvvk.v1.modal.Customer;
import com.sdgvvk.v1.modal.FcmTokenModal;
import com.sdgvvk.v1.modal.FilterModal;
import com.sdgvvk.v1.modal.Level_1_cardModal;
import com.sdgvvk.v1.modal.OrderModal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

public class HomeFragment extends Fragment {


    FloatingActionButton refreshBtn;
    ExtendedFloatingActionButton balanceBtn;
    Fragment fragment;
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

    public void init() {
        initUIElements();
        initOnclickListener();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i("ss_nw_call", new Date() + "lifecycle : HomeFragment onCreateView");
        activity = this.getActivity();
        fragment = this;

        view = inflater.inflate(R.layout.fragment_home, container, false);
        Log.i("ss_nw_call", "singleuser : 8");
        searchPanelAnimator();
        checkforappupdates();
        init();
        HelperUtils.checkNetworkStatus(this.getActivity());
        askfornotificationpermission();


        loggedinCustomer = LocalCache.getLoggedInCustomer(this.getActivity());

        // NON-REGISTERED USER FLOW
        if (loggedinCustomer.getProfileId() == null)
            onboardNewUser();
        else {
            captureFCMToken();
            Boolean isAdmin = loggedinCustomer.getIsAdmin() != null && loggedinCustomer.getIsAdmin().equalsIgnoreCase("1") ? true : false;

            // add blocker for existing user for filling complete profile
            if (!isAdmin && (loggedinCustomer.getHeight().isEmpty() || loggedinCustomer.getProfilephotoaddress() == null)) {
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

                displayBalance();
                //ApiCallUtil.syncUserActivity(loggedinCustomer.getProfileId(),this.getActivity());

            }

            if (loggedinCustomer.getIsNoticeAvailable() != null && !loggedinCustomer.getIsNoticeAvailable().equalsIgnoreCase("no")) {
                ApiCallUtil.getAdminNotice(activity, loggedinCustomer);
            }

            ShowSingleUserActivityNotification();

        }


        return view;
    }

    private void askfornotificationpermission() {
        Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.POST_NOTIFICATIONS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
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
        balanceBtn = view.findViewById(R.id.balanceBtn);
        refreshBtn = view.findViewById(R.id.refreshBtn);
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

        // TODO: 08-Dec-23 changing searching flow
        //searchProfiles.setOnClickListener(view -> searchProfiles());


        searchProfiles.setOnClickListener(view -> {
            TextView blocker1, blocker2, blocker3, blocker4,blocker5, searchMode;
            Button searchBtn;
            Boolean isPaidCustomer = loggedinCustomer.getActivepackageid() != null ? true : false;
            Boolean isAdmin = loggedinCustomer.getIsAdmin() != null && loggedinCustomer.getIsAdmin().equalsIgnoreCase("1") ? true : false;
            AutoCompleteTextView minHeight, maxHeight, minAge, maxAge, education, occupation, city, surname,caste;
            TextInputLayout education_p, city_p, occupation_p, surname_p,caste_p;
            TextInputEditText profileId;
            SpinKitView progressBar;

            Dialog d = new Dialog(this.getActivity());
            d.setContentView(R.layout.search_profile_new);
            progressBar = d.findViewById(R.id.progressBar);
            profileId = d.findViewById(R.id.profileId);
            CardView card1 = d.findViewById(R.id.card1);
            CardView card2 = d.findViewById(R.id.card2);
            LinearLayout layout1 = d.findViewById(R.id.layout1);
            LinearLayout layout2 = d.findViewById(R.id.layout2);
            minHeight = d.findViewById(R.id.minHeight);
            maxHeight = d.findViewById(R.id.maxHeight);
            minAge = d.findViewById(R.id.minAge);
            maxAge = d.findViewById(R.id.maxAge);
            city = d.findViewById(R.id.city);
            city_p = d.findViewById(R.id.city_p);
            education = d.findViewById(R.id.education);
            education_p = d.findViewById(R.id.education_p);
            occupation = d.findViewById(R.id.occupation);
            occupation_p = d.findViewById(R.id.occupation_p);
            surname = d.findViewById(R.id.surname);
            surname_p = d.findViewById(R.id.surname_p);
            caste = d.findViewById(R.id.caste);
            caste_p = d.findViewById(R.id.caste_p);

            blocker1 = d.findViewById(R.id.blocker1);
            blocker2 = d.findViewById(R.id.blocker2);
            blocker3 = d.findViewById(R.id.blocker3);
            blocker4 = d.findViewById(R.id.blocker4);
            blocker5 = d.findViewById(R.id.blocker5);
            searchMode = d.findViewById(R.id.searchMode);
            searchMode.setText("1");

            searchBtn = d.findViewById(R.id.searchBtn);


            minHeight.setText("4' 1\" | 124 cm");
            maxHeight.setText("7' 11\" | 242 cm");

            minHeight.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.day_night_dropdown_list_item, new String[]{"4' 1\"  |  124 cm", "4' 2\"  |  127 cm", "4' 3\"  |  130 cm", "4' 4\"  |  132 cm", "4' 5\"  |  135 cm", "4' 6\"  |  138 cm", "4' 7\"  |  140 cm", "4' 8\"  |  143 cm", "4' 9\"  |  145 cm", "4' 10\"  |  148 cm", "4' 11\"  |  151 cm", "5'  |  152 cm", "5' 1\"  |  155 cm", "5' 2\"  |  157 cm", "5' 3\"  |  160 cm", "5' 4\"  |  163 cm", "5' 5\"  |  165 cm", "5' 6\"  |  168 cm", "5' 7\"  |  170 cm", "5' 8\"  |  173 cm", "5' 9\"  |  175 cm", "5' 10\"  |  178 cm", "5' 11\"  |  180 cm", "6'  |  183 cm", "6' 1\"  |  185 cm", "6' 2\"  |  188 cm", "6' 3\"  |  191 cm", "6' 4\"  |  193 cm", "6' 5\"  |  196 cm", "6' 6\"  |  198 cm", "6' 7\"  |  201 cm", "6' 8\"  |  203 cm", "6' 9\"  |  206 cm", "6' 10\"  |  208 cm", "6' 11\"  |  211 cm", "7'  |  213 cm", "7' 1\"  |  216 cm", "7' 2\"  |  218 cm", "7' 3\"  |  221 cm", "7' 4\"  |  224 cm", "7' 5\"  |  226 cm", "7' 6\"  |  229 cm", "7' 7\"  |  231 cm", "7' 8\"  |  234 cm", "7' 9\"  |  237 cm", "7' 10\"  |  239 cm", "7' 11\"  |  242 cm"}));
            maxHeight.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.day_night_dropdown_list_item, new String[]{"4' 1\"  |  124 cm", "4' 2\"  |  127 cm", "4' 3\"  |  130 cm", "4' 4\"  |  132 cm", "4' 5\"  |  135 cm", "4' 6\"  |  138 cm", "4' 7\"  |  140 cm", "4' 8\"  |  143 cm", "4' 9\"  |  145 cm", "4' 10\"  |  148 cm", "4' 11\"  |  151 cm", "5'  |  152 cm", "5' 1\"  |  155 cm", "5' 2\"  |  157 cm", "5' 3\"  |  160 cm", "5' 4\"  |  163 cm", "5' 5\"  |  165 cm", "5' 6\"  |  168 cm", "5' 7\"  |  170 cm", "5' 8\"  |  173 cm", "5' 9\"  |  175 cm", "5' 10\"  |  178 cm", "5' 11\"  |  180 cm", "6'  |  183 cm", "6' 1\"  |  185 cm", "6' 2\"  |  188 cm", "6' 3\"  |  191 cm", "6' 4\"  |  193 cm", "6' 5\"  |  196 cm", "6' 6\"  |  198 cm", "6' 7\"  |  201 cm", "6' 8\"  |  203 cm", "6' 9\"  |  206 cm", "6' 10\"  |  208 cm", "6' 11\"  |  211 cm", "7'  |  213 cm", "7' 1\"  |  216 cm", "7' 2\"  |  218 cm", "7' 3\"  |  221 cm", "7' 4\"  |  224 cm", "7' 5\"  |  226 cm", "7' 6\"  |  229 cm", "7' 7\"  |  231 cm", "7' 8\"  |  234 cm", "7' 9\"  |  237 cm", "7' 10\"  |  239 cm", "7' 11\"  |  242 cm"}));
            String[] ageArray = new String[38]; // Since you want to fill it till 55 (55 - 18 + 1)

            for (int i = 18; i <= 55; i++) {
                ageArray[i - 18] = Integer.toString(i);
            }
            minAge.setText("18");
            maxAge.setText("70");
            minAge.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.day_night_dropdown_list_item, ageArray));
            maxAge.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.day_night_dropdown_list_item, ageArray));

            String[] cityArray = {"Kolhapur", "Pune", "Mumbai", "Satara", "Sangli", "Karad","Solapur", "Belgav", "Thane"};
            city.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.day_night_dropdown_list_item, cityArray));

            List<String> eduList = LocalCache.getEducationList(activity);
            if(eduList != null && !eduList.isEmpty())
                education.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.day_night_dropdown_list_item, eduList.toArray()));

            List<String> occupationList = LocalCache.getOccupationList(activity);
            if(occupationList != null && !occupationList.isEmpty())
                occupation.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.day_night_dropdown_list_item, occupationList.toArray()));

            List<String> lastnamesList = LocalCache.getLastnamesList(activity);
            if(lastnamesList != null && !lastnamesList.isEmpty())
                surname.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.day_night_dropdown_list_item, lastnamesList.toArray()));

            List<String> cityList = LocalCache.getCityList(activity);
            if(cityList != null && !cityList.isEmpty())
                city.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.day_night_dropdown_list_item, cityList.toArray()));

            List<String> casteList = LocalCache.getCasteList(activity);
            if(casteList != null && !casteList.isEmpty())
                caste.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.day_night_dropdown_list_item, casteList.toArray()));




            card1.setBackgroundColor(getResources().getColor(R.color.yellow));
            card1.setRadius(20.0f);
            card1.setOnClickListener(view1 -> {
                searchBtn.setEnabled(true);
                searchMode.setText("1");
                card1.setBackgroundColor(getResources().getColor(R.color.yellow));
                card2.setBackgroundColor(getResources().getColor(R.color.white));
                card1.setRadius(20.0f);
                card2.setRadius(20.0f);
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
                ViewUtils.hideKeyboard(d.getWindow().getDecorView());
            });

            card2.setOnClickListener(view1 -> {
                profileId.requestFocus();
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(profileId, InputMethodManager.SHOW_IMPLICIT);

                searchBtn.setEnabled(false);
                searchMode.setText("2");
                card1.setBackgroundColor(getResources().getColor(R.color.white));
                card2.setBackgroundColor(getResources().getColor(R.color.yellow));
                card1.setRadius(20.0f);
                card2.setRadius(20.0f);
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
            });


            if (isPaidCustomer || isAdmin) {
                city_p.setEnabled(true);
                blocker1.setVisibility(View.GONE);

                education_p.setEnabled(true);
                blocker2.setVisibility(View.GONE);

                occupation_p.setEnabled(true);
                blocker3.setVisibility(View.GONE);

                surname_p.setEnabled(true);
                blocker4.setVisibility(View.GONE);

                caste_p.setEnabled(true);
                blocker5.setVisibility(View.GONE);

            } else {
                city_p.setEnabled(false);
                blocker1.setVisibility(View.VISIBLE);

                education_p.setEnabled(false);
                blocker2.setVisibility(View.VISIBLE);

                occupation_p.setEnabled(false);
                blocker3.setVisibility(View.VISIBLE);

                surname_p.setEnabled(false);
                blocker4.setVisibility(View.VISIBLE);

                caste_p.setEnabled(false);
                blocker5.setVisibility(View.VISIBLE);
            }

            profileId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                    if (s.length() == 4)
                        searchBtn.setEnabled(true);
                    else
                        searchBtn.setEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            searchBtn.setOnClickListener(view12 -> {
                FilterModal modal = null;
                Customer c = LocalCache.getLoggedInCustomer(activity);

                searchBtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                String mode = searchMode.getText().toString().trim();
                if (mode.equalsIgnoreCase("2")) {
                    String profileId_ = profileId.getText().toString();
                    String _gender = c.getGender() != null && c.getGender().equalsIgnoreCase("male") ? "female" : "male";

                    FilterModal modal1 = new FilterModal(c.getProfileId(), profileId_, _gender);
                    ApiCallUtil.searchSingleProfile(activity, fragment, modal1 , d);
                } else {
                    String minHeight_ = "", maxHeight_ = "", minAge_ = "", maxAge_ = "", _caste = "", _city = "", _lastname = "", _gender = "", education_ = "", occupation_ = "";
                    minAge_ = minAge.getText().toString().trim();
                    maxAge_ = maxAge.getText().toString().trim();
                    _caste = caste.getText().toString().trim();
                    _gender = c.getGender() != null && c.getGender().equalsIgnoreCase("male") ? "female" : "male";
                    education_ = education.getText().toString().trim();
                    occupation_ = occupation.getText().toString().trim();
                    _city = city.getText().toString().trim();
                    _lastname = surname.getText().toString().trim();


                    String[] parts1 = minHeight.getText().toString().trim().split("\\|");
                    for (String part : parts1) {
                        if (part.contains("cm")) {
                            String[] cmSplit = part.trim().split("\\s+");
                            minHeight_ = cmSplit[0];
                        }
                    }

                    String[] parts2 = maxHeight.getText().toString().trim().split("\\|");
                    for (String part : parts2) {
                        if (part.contains("cm")) {
                            String[] cmSplit = part.trim().split("\\s+");
                            maxHeight_ = cmSplit[0];
                        }
                    }
                    modal = new FilterModal(c.getProfileId(), minAge_, maxAge_, minHeight_, maxHeight_, _gender, education_, occupation_, _city, _lastname, _caste);
                    ApiCallUtil.searchLevel1Profiles(activity, fragment, modal , d);
                }
            });


            d.setCanceledOnTouchOutside(true);
            d.setCancelable(true);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            d.show();
        });


        // Refresh  the layout
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    ApiCallUtil.getAllProfiles(loggedinCustomer.getProfileId(), this, progressBar, this.getActivity(), true);
                    swipeRefreshLayout.setRefreshing(false);
                }
        );

        refreshBtn.setOnClickListener(view -> {
            Toast.makeText(activity, "refreshing...", Toast.LENGTH_SHORT).show();
            ApiCallUtil.getAllProfiles(loggedinCustomer.getProfileId(), fragment, progressBar, activity, true);
        });

        balanceBtn.setOnClickListener(view -> {
            HelperUtils.vibrateFunction(this.getActivity());
            Intent intent = new Intent(this.getActivity(), ContactViewedActivity.class);
            startActivity(intent);
        });


    }


    private void showUserProfile() {
        HelperUtils.vibrateFunction(this.getActivity());
        //ApiCallUtil.getLevel2Data(loggedinCustomer.getProfileId(), activity);
        activity.startActivity(new Intent(activity, Level2ProfileActivity.class)
                .putExtra("level2data", loggedinCustomer.getProfileId()));
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

                        if (obj.getIsAdmin() != null && obj.getIsAdmin().equalsIgnoreCase("999")) {
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
        ApiCallUtil.getUserNotifications(this.getActivity(), d, null, false);
        d.setCanceledOnTouchOutside(true);
        d.setCancelable(true);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.show();
    }

    private void ShowSingleUserActivityNotification() {
        HelperUtils.vibrateFunction(this.getActivity());
        Dialog d = new Dialog(this.getActivity());
        d.setContentView(R.layout.single_notification_dialog);
        ApiCallUtil.getUserNotifications(this.getActivity(), d, null, true);
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
        askfornotificationpermission();
        if (ApiCallUtil.clicked_level2activity)
            ApiCallUtil.clicked_level2activity = false;
        else {
            if (!forceupdate)
                ApiCallUtil.getAllProfiles(loggedinCustomer.getProfileId(), this, progressBar, this.getActivity(), true);
        }

        displayBalance();

    }

    public void captureFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }

                    String token = task.getResult();
                    // Get the Android ID
                    String deviceid = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);

                    // Handle the case where the Android ID is null
                    if (deviceid == null) {
                        deviceid = "UNKNOWN";
                    }

                    String version = "0";
                    try {
                        version = this.getActivity().getPackageManager().getPackageInfo(this.getActivity().getPackageName(), 0).versionName;
                    } catch (PackageManager.NameNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    ApiCallUtil.sendTokenToServer(new FcmTokenModal(loggedinCustomer.getProfileId(), token, deviceid, version));

                });
    }

    public void searchPanelAnimator() {
        String color = null;
        ObjectAnimator anim = null;
        TextView textView = view.findViewById(R.id.searchprofiles);
        anim = ObjectAnimator.ofInt(textView, "textColor", Color.BLACK, Color.RED, Color.BLUE, Color.GREEN);

        //linearLayout.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        anim.setDuration(1500);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();
    }

    public void displayBalance() {

        if (loggedinCustomer != null) {
            ApiCallUtil.syncAccountBalanceForHomeScreen(loggedinCustomer.getProfileId(), activity, balanceBtn);
            if (loggedinCustomer.getActivepackageid() != null) {
                OrderModal activeOrder = LocalCache.getActiveOrder(activity);
                if (activeOrder != null && activeOrder.getId() != null) {
                    balanceBtn.setVisibility(View.VISIBLE);
                    balanceBtn.setText("Balance : " + activeOrder.getCountRemaining());
                } else
                    balanceBtn.setVisibility(View.GONE);
            } else
                balanceBtn.setVisibility(View.GONE);

        }
    }


}