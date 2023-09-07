package com.example.ss1.ui.home;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.ss1.LocalCache;
import com.example.ss1.R;
import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.adapters.Level_1_profilecardAdapter;
import com.example.ss1.api.ApiUtils;
import com.example.ss1.modal.Customer;
import com.example.ss1.modal.Level_1_cardModal;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public CoordinatorLayout coordinatorLayout;

    SpinKitView progressBar;

    ImageView notification,profilePhoto;

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

        initUIElements();
        initOnclickListener();

        syncLoggedInCustomer();
        customer = LocalCache.retrieveLoggedInCustomer(this.getActivity());
        level1list = LocalCache.retrieveLevel1List(this.getActivity());


        setProfileIcon();

        if(!level1list.isEmpty()){
            initLevel_1_CardProfilesRecyclerView(level1list);
        }

        ApiCallUtil.getAllProfiles(customer.getProfileId(),this, progressBar,this.getActivity(),false);




        return view;
    }

    private void setProfileIcon() {
        if(customer.getProfileId() != null){
            Glide.with(this.getActivity())
                    .load(customer.getProfilePhotoAddress() != null ? customer.getProfilePhotoAddress() : R.drawable.prashant)
                    .placeholder(R.drawable.oops)
                    .into(profilePhoto);
        }
    }

    private void syncLoggedInCustomer() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            ApiCallUtil.updateLoggedInCustomerDetails(this.getActivity(),user.getPhoneNumber().replace("+91",""));
        }
    }

    private void initUIElements() {
        profilePhoto =  view.findViewById(R.id.profilePhoto);
        coordinatorLayout = view.findViewById(R.id.level1CoordinatorLayout);
        progressBar = view.findViewById(R.id.progressBar);
        level1cardsRecyclerView = view.findViewById(R.id.level1cardsRecyclerView);
        notification = view.findViewById(R.id.notification);
        searchProfiles = view.findViewById(R.id.searchProfiles);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refreshLayout);
    }

    private void initOnclickListener() {
        level1cardsRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
        });

        notification.setOnClickListener(view -> {
            showNotificationListDialog();
        });

        profilePhoto.setOnClickListener(view -> showUserProfile());

        searchProfiles.setOnClickListener(view -> searchProfiles());

        // Refresh  the layout
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    ApiCallUtil.getAllProfiles(customer.getProfileId(),this, progressBar,this.getActivity(),true);
                    swipeRefreshLayout.setRefreshing(false);
                    showSnackBar("refrshing...");
                }
        );


    }

    private void searchProfiles() {
        ApiUtils.vibrateFunction(this.getActivity());
    }

    private void showUserProfile() {
        ApiUtils.vibrateFunction(this.getActivity());
    }

    private void loadMore() {
        rowsArrayList.add(null);
        level1CardAdapter.notifyItemInserted(rowsArrayList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
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
            }
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
    }

    public void initLevel_1_CardProfilesRecyclerView(List<Level_1_cardModal> list) {
        if (view != null) {
            recyclerView = view.findViewById(R.id.level1cardsRecyclerView);
            if (recyclerView != null) {
                if (list != null) {
                    rowsArrayList = list;
                    level1CardAdapter = new Level_1_profilecardAdapter(view, list, this, this.getActivity(), false);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
                    recyclerView.setAdapter(level1CardAdapter);
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

    public void showNotificationListDialog(){
        ApiUtils.vibrateFunction(this.getActivity());
        Dialog d = new Dialog(this.getActivity());
        d.setContentView(R.layout.notificationlist_dialog);

        ApiCallUtil.getUserNotifications(this.getActivity() , d);

        d.setCanceledOnTouchOutside(true);
        d.setCancelable(true);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.show();
    }

}