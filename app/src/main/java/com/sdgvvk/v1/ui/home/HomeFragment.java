package com.sdgvvk.v1.ui.home;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sdgvvk.v1.LocalCache;
import com.sdgvvk.v1.MainActivity;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.SearchProfileBottomSheetDialog;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.adapters.Level_1_profilecardAdapter;
import com.sdgvvk.v1.api.HelperUtils;
import com.sdgvvk.v1.modal.Customer;
import com.sdgvvk.v1.modal.Level_1_cardModal;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private static final int REQ_CODE = 100 ;

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
    Customer customer;
    List<Level_1_cardModal> level1list;
    SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_home, container, false);
        activity = this.getActivity();
        checkforappupdates();
        initUIElements();
        initOnclickListener();
        
        HelperUtils.checkNetworkStatus(this.getActivity());

        syncLoggedInCustomer();
        customer = LocalCache.getLoggedInCustomer(this.getActivity());
        level1list = LocalCache.getLevel1List(this.getActivity());

        if (customer.getProfileId() == null)
            onboardNewUser();


        setProfileIcon();

        if (!level1list.isEmpty()) {
            initLevel_1_CardProfilesRecyclerView(level1list);
        }

        if (customer.getProfileId() != null)
            ApiCallUtil.getAllProfiles(customer.getProfileId(), this, progressBar, this.getActivity(), false);


        return view;
    }

    private void onboardNewUser() {
        HelperUtils.vibrateFunction(this.getActivity());
        Dialog d = new Dialog(this.getActivity());
        d.setContentView(R.layout.onboarding_dialog);
        LinearLayout errBox = d.findViewById(R.id.errorBox);
        TextView errText = d.findViewById(R.id.errText);

        CircularImageView profilePhotoAddress = d.findViewById(R.id.profilePhotoAddress);
        profilePhotoAddress.setOnClickListener(view -> {
            // TODO: 13-Oct-23
            ApiCallUtil.onboardDialog = d;
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            ((MainActivity)MainActivity.getContextObject()).startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });




        String[] genderArray = {"male", "female"};
        ((AutoCompleteTextView) d.findViewById(R.id.gender)).setAdapter(new ArrayAdapter(this.getActivity(), R.layout.package_list_item, genderArray));

        d.findViewById(R.id.birthdate).setOnClickListener(view1 -> {
            //Date Picker
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            MaterialDatePicker<Long> picker = builder.build();
            picker.show(this.getActivity().getSupportFragmentManager(), picker.toString());
            picker.addOnPositiveButtonClickListener(selectedDate -> {

                Date date = new Date(selectedDate);
                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                ((TextView) d.findViewById(R.id.birthdate)).setText(simpleFormat.format(date));
            });
        });

        d.findViewById(R.id.create_profile_btn).setOnClickListener(view -> {
            String result = validateOnBoardingForm(d);
            if(result.equalsIgnoreCase("")){
                d.dismiss();
                String mobile = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().replace("+91", "");
                String name = ((TextInputEditText) d.findViewById(R.id.name)).getText().toString().trim();
                String firstname = "", middlename = "", lastname = "";
                String[] nameArr = name.split(" ");
                if (nameArr.length == 1) {
                    firstname = nameArr[0];
                } else if (nameArr.length == 2) {
                    firstname = nameArr[0];
                    lastname = nameArr[1];
                } else if (nameArr.length == 3) {
                    firstname = nameArr[0];
                    middlename = nameArr[1];
                    lastname = nameArr[2];
                } else
                    firstname = name;

                String email = ((TextInputEditText) d.findViewById(R.id.email)).getText().toString().trim();
                String gender = ((AutoCompleteTextView) d.findViewById(R.id.gender)).getText().toString().trim();
                String birthdate = ((TextInputEditText) d.findViewById(R.id.birthdate)).getText().toString().trim();

                Customer c = new Customer(firstname, middlename, lastname, mobile, email, gender, birthdate, "0");
                if(ApiCallUtil.b64 != null){
                    c.setProfilephotoaddress(ApiCallUtil.b64);
                }
                ApiCallUtil.onboardDialog = null;
                ApiCallUtil.b64 = null;
                ApiCallUtil.registerProfile(c, getFragmentActivity(), true, this);
            }
            else{
                errBox.setVisibility(View.VISIBLE);
                errText.setText(result);
            }

        });


        ((TextInputEditText) d.findViewById(R.id.name)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.length() > 0){
                    errText.setText("");
                    errBox.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        ((TextInputEditText) d.findViewById(R.id.birthdate)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.length() > 0){
                    errText.setText("");
                    errBox.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ((AutoCompleteTextView) d.findViewById(R.id.gender)).setOnItemClickListener((parent, arg1, pos, id) -> {
            String gender = ((AutoCompleteTextView) d.findViewById(R.id.gender)).getText().toString().trim();
            if(gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female")){
                errText.setText("");
                errBox.setVisibility(View.GONE);
            }
        });



        d.setCanceledOnTouchOutside(false);
        d.setCancelable(false);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.show();
    }


    private void setProfileIcon() {
        if (customer != null && customer.getProfileId() != null) {
            Glide.with(this.getActivity())
                    .load(customer.getProfilephotoaddress() != null ? customer.getProfilephotoaddress() : R.drawable.oops)
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
                    ApiCallUtil.getAllProfiles(customer.getProfileId(), this, progressBar, this.getActivity(), true);
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
        ApiCallUtil.getLevel2Data(customer.getProfileId(), activity);
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
                rowsArrayList.add(new Level_1_cardModal(String.valueOf(currentSize), "fname", "lname", "06/09/1994", "age", "address", "0", "0", "0"));

                currentSize++;
            }

            level1CardAdapter.notifyDataSetChanged();
            isLoading = false;
        }, 100);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ApiCallUtil.clicked_level2activity)
            ApiCallUtil.clicked_level2activity = false;
        else
            ApiCallUtil.getAllProfiles(customer.getProfileId(), this, progressBar, this.getActivity(), true);
    }

    public void initLevel_1_CardProfilesRecyclerView(List<Level_1_cardModal> list) {
        if (view != null) {
            recyclerView = view.findViewById(R.id.level1cardsRecyclerView);
            if (recyclerView != null) {
                if (list != null) {
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
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
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
        String gender = ((AutoCompleteTextView) d.findViewById(R.id.gender)).getText().toString().trim();
        String birthdate = ((TextInputEditText) d.findViewById(R.id.birthdate)).getText().toString().trim();


        if(name.isEmpty())
            list.add("name");
        if(gender.isEmpty())
            list.add("gender");
        if(birthdate.isEmpty())
            list.add("birthdate");

        if(!list.isEmpty()){
            for(int i=0;i<list.size();i++){
                errorTxt = errorTxt +" "+list.get(i);
                if(!(i == list.size() -1))
                    errorTxt = errorTxt+",";
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
                            appUpdateInfo,AppUpdateType.IMMEDIATE,this.getActivity(),REQ_CODE);
                } catch (IntentSender.SendIntentException e) {
                    Log.e("ImageUtils", e.toString());
                    throw new RuntimeException(e);
                }
            }
        });
    }


}