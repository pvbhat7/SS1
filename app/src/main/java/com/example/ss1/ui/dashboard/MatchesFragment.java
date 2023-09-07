package com.example.ss1.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ss1.LocalCache;
import com.example.ss1.ProjectConstants;
import com.example.ss1.R;
import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.databinding.FragmentMatchesBinding;
import com.example.ss1.adapters.Level_1_shortprofilecardAdapter;
import com.example.ss1.modal.Customer;
import com.example.ss1.modal.Level_1_cardModal;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MatchesFragment extends Fragment {

    public CoordinatorLayout coordinatorLayout;

    public CardView menu_contactsViewed, menu_shortlistedByYou, menu_likedByYou, menu_ignoredByYou, menu_interestSentByYou;

    private FragmentMatchesBinding binding;
    private View view;

    SpinKitView progressBar;

    RecyclerView contactViewedRecyclerView;
    
    Customer customer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_matches, container, false);

        customer = LocalCache.retrieveLoggedInCustomer(this.getActivity());
        
        initUIElements();
        handleOnclick();
        syncLoggedInCustomer();
        ApiCallUtil.getProfilesByTag(customer.getProfileId(), this, "1",progressBar);
        return view;
    }


    private void initUIElements() {
        progressBar = view.findViewById(R.id.progressBar);
        contactViewedRecyclerView = view.findViewById(R.id.contactViewedRecyclerView);
        coordinatorLayout = view.findViewById(R.id.level1CoordinatorLayout);
        menu_contactsViewed = view.findViewById(R.id.menu_contactsViewed);
        menu_shortlistedByYou = view.findViewById(R.id.menu_shortlistedByYou);
        menu_likedByYou = view.findViewById(R.id.menu_likedByYou);
        menu_ignoredByYou = view.findViewById(R.id.menu_ignoredByYou);
        menu_interestSentByYou = view.findViewById(R.id.menu_interestSentByYou);
    }

    private void handleOnclick() {
        menu_contactsViewed.setOnClickListener(view -> {
            contactViewedRecyclerView.setVisibility(View.GONE);
            ApiCallUtil.getProfilesByTag(customer.getProfileId(), this, "1", progressBar);
            resetCardSelection(ProjectConstants.ACTION_VIEW_CONTACT_DETAILS);
        });

        menu_shortlistedByYou.setOnClickListener(view -> {
            contactViewedRecyclerView.setVisibility(View.GONE);
            ApiCallUtil.getProfilesByTag(customer.getProfileId(), this, "2", progressBar);
            resetCardSelection(ProjectConstants.ACTION_SHORTLIST_PROFILE);
        });

        menu_likedByYou.setOnClickListener(view -> {
            contactViewedRecyclerView.setVisibility(View.GONE);
            ApiCallUtil.getProfilesByTag(customer.getProfileId(), this, "3", progressBar);
            resetCardSelection(ProjectConstants.ACTION_LIKE_PROFILE);
        });

        menu_ignoredByYou.setOnClickListener(view -> {
            contactViewedRecyclerView.setVisibility(View.GONE);
            ApiCallUtil.getProfilesByTag(customer.getProfileId(), this, "4", progressBar);
            resetCardSelection(ProjectConstants.ACTION_IGNORE_PROFILE);
        });
        menu_interestSentByYou.setOnClickListener(view -> {
            contactViewedRecyclerView.setVisibility(View.GONE);
            ApiCallUtil.getProfilesByTag(customer.getProfileId(), this, "5", progressBar);
            resetCardSelection(ProjectConstants.ACTION_SEND_INTEREST);
        });

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void initRecyclerView(List<Level_1_cardModal> list) {
        if (view != null) {
            if (contactViewedRecyclerView != null) {
                contactViewedRecyclerView.setVisibility(View.VISIBLE);
                Level_1_shortprofilecardAdapter level1CardAdapter = new Level_1_shortprofilecardAdapter(list, this.getActivity());
                GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 3);
                contactViewedRecyclerView.setLayoutManager(layoutManager);
                contactViewedRecyclerView.setAdapter(level1CardAdapter);
            }
        }

    }

    public void showSnackBar(String content) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, content, Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
        snackbar.show();
    }

    private void resetCardSelection(String selection) {
        menu_contactsViewed.setBackgroundColor(getResources().getColor(R.color.white));
        menu_shortlistedByYou.setBackgroundColor(getResources().getColor(R.color.white));
        menu_likedByYou.setBackgroundColor(getResources().getColor(R.color.white));
        menu_ignoredByYou.setBackgroundColor(getResources().getColor(R.color.white));
        menu_interestSentByYou.setBackgroundColor(getResources().getColor(R.color.white));

        if(selection.equalsIgnoreCase(ProjectConstants.ACTION_VIEW_CONTACT_DETAILS))
            menu_contactsViewed.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if(selection.equalsIgnoreCase(ProjectConstants.ACTION_LIKE_PROFILE))
            menu_likedByYou.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if(selection.equalsIgnoreCase(ProjectConstants.ACTION_SHORTLIST_PROFILE))
            menu_shortlistedByYou.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if(selection.equalsIgnoreCase(ProjectConstants.ACTION_SEND_INTEREST))
            menu_interestSentByYou.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if(selection.equalsIgnoreCase(ProjectConstants.ACTION_IGNORE_PROFILE))
            menu_ignoredByYou.setBackgroundColor(getResources().getColor(R.color.yellow));

    }

    private void syncLoggedInCustomer() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            ApiCallUtil.updateLoggedInCustomerDetails(this.getActivity(),user.getPhoneNumber().replace("+91",""));
        }
    }

}