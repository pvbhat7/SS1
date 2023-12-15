package com.sdgvvk.v1.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdgvvk.v1.LocalCache;
import com.sdgvvk.v1.ProjectConstants;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.api.HelperUtils;
import com.sdgvvk.v1.adapters.Level_1_shortprofilecardAdapter;
import com.sdgvvk.v1.modal.Customer;
import com.sdgvvk.v1.modal.CustomerActivityModal;
import com.sdgvvk.v1.modal.Level_1_cardModal;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MatchesFragment extends Fragment {

    public CoordinatorLayout coordinatorLayout;

    public CardView menu_contactsViewed, menu_shortlistedByYou, menu_likedByYou, menu_ignoredByYou, menu_interestSentByYou;

    public TextView txt1, txt2, txt3, txt4;

    private View view;


    RecyclerView contactViewedRecyclerView;

    Customer customer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_matches, container, false);
        HelperUtils.checkNetworkStatus(this.getActivity());

        customer = LocalCache.getLoggedInCustomer(this.getActivity());

        initUIElements();
        handleOnclick();
        LocalCache.setContactviewedMatchesList(false,getActivity());
        LocalCache.setLikedMatchesList(false,getActivity());
        LocalCache.setShortlistedMatchesList(false,getActivity());
        LocalCache.setSentinterestMatchesList(false,getActivity());

        txt1.setText(LocalCache.getContactviewedMatchesList(this.getActivity()).size()+" Profiles");
        txt2.setText(LocalCache.getLikedMatchesList(this.getActivity()).size()+" Profiles");
        txt3.setText(LocalCache.getShortlistedMatchesList(this.getActivity()).size()+" Profiles");
        txt4.setText(LocalCache.getSentinterestMatchesList(this.getActivity()).size()+" Profiles");

        startRecyclerview(LocalCache.getContactviewedMatchesList(this.getActivity()));



        //syncLoggedInCustomer();
        //ApiCallUtil.syncUserActivity(customer.getProfileId(), this.getActivity());


        return view;
    }

    private void startRecyclerview(List<Level_1_cardModal> list) {

            initRecyclerView(list);
    }


    private void initUIElements() {
        contactViewedRecyclerView = view.findViewById(R.id.contactViewedRecyclerView);
        coordinatorLayout = view.findViewById(R.id.level1CoordinatorLayout);
        menu_contactsViewed = view.findViewById(R.id.menu_contactsViewed);
        menu_shortlistedByYou = view.findViewById(R.id.menu_shortlistedByYou);
        menu_likedByYou = view.findViewById(R.id.menu_likedByYou);
        //menu_ignoredByYou = view.findViewById(R.id.menu_ignoredByYou);
        menu_interestSentByYou = view.findViewById(R.id.menu_interestSentByYou);

        txt1 = view.findViewById(R.id.txt1);
        txt2 = view.findViewById(R.id.txt2);
        txt3 = view.findViewById(R.id.txt3);
        txt4 = view.findViewById(R.id.txt4);
    }

    private void handleOnclick() {
        menu_contactsViewed.setOnClickListener(view -> {
            resetCardSelection(ProjectConstants.ACTION_VIEW_CONTACT_DETAILS);
            startRecyclerview(LocalCache.getContactviewedMatchesList(this.getActivity()));
            //ApiCallUtil.getProfilesByTag(customer.getProfileId(), this, this.getActivity(), ProjectConstants.MATCHES_CONTACT_VIEWED);
        });

        menu_shortlistedByYou.setOnClickListener(view -> {
            resetCardSelection(ProjectConstants.ACTION_SHORTLIST_PROFILE);
            startRecyclerview(LocalCache.getShortlistedMatchesList(this.getActivity()));
            //ApiCallUtil.getProfilesByTag(customer.getProfileId(), this, this.getActivity(), ProjectConstants.MATCHES_SHORTLIST);
        });

        menu_likedByYou.setOnClickListener(view -> {
            resetCardSelection(ProjectConstants.ACTION_LIKE_PROFILE);
            startRecyclerview(LocalCache.getLikedMatchesList(this.getActivity()));
            //ApiCallUtil.getProfilesByTag(customer.getProfileId(), this, this.getActivity(), ProjectConstants.MATCHES_LIKED);

        });

       /* menu_ignoredByYou.setOnClickListener(view -> {
            contactViewedRecyclerView.setVisibility(View.GONE);
            ApiCallUtil.getProfilesByTag(customer.getProfileId(), this, "4", progressBar);
            resetCardSelection(ProjectConstants.ACTION_IGNORE_PROFILE);
        });*/
        menu_interestSentByYou.setOnClickListener(view -> {
            resetCardSelection(ProjectConstants.ACTION_SEND_INTEREST);
            startRecyclerview(LocalCache.getSentinterestMatchesList(this.getActivity()));
            //ApiCallUtil.getProfilesByTag(customer.getProfileId(), this, this.getActivity(), ProjectConstants.MATCHES_SENT_INTEREST);

        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        float cornerRadius = 15f; // Replace this with your desired corner radius

        menu_contactsViewed.setCardBackgroundColor(getResources().getColor(R.color.white));
        menu_shortlistedByYou.setCardBackgroundColor(getResources().getColor(R.color.white));
        menu_likedByYou.setCardBackgroundColor(getResources().getColor(R.color.white));
        menu_interestSentByYou.setCardBackgroundColor(getResources().getColor(R.color.white));

        if (selection.equalsIgnoreCase(ProjectConstants.ACTION_VIEW_CONTACT_DETAILS)) {
            menu_contactsViewed.setCardBackgroundColor(getResources().getColor(R.color.yellow));
            menu_contactsViewed.setRadius(cornerRadius);
        } else if (selection.equalsIgnoreCase(ProjectConstants.ACTION_LIKE_PROFILE)) {
            menu_likedByYou.setCardBackgroundColor(getResources().getColor(R.color.yellow));
            menu_likedByYou.setRadius(cornerRadius);
        } else if (selection.equalsIgnoreCase(ProjectConstants.ACTION_SHORTLIST_PROFILE)) {
            menu_shortlistedByYou.setCardBackgroundColor(getResources().getColor(R.color.yellow));
            menu_shortlistedByYou.setRadius(cornerRadius);
        } else if (selection.equalsIgnoreCase(ProjectConstants.ACTION_SEND_INTEREST)) {
            menu_interestSentByYou.setCardBackgroundColor(getResources().getColor(R.color.yellow));
            menu_interestSentByYou.setRadius(cornerRadius);
        }
        // Add more conditions if needed for other cards
    }

   /* private void syncLoggedInCustomer() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            ApiCallUtil.updateLoggedInCustomerDetails(this.getActivity(), user.getPhoneNumber().replace("+91", ""));
        }
    }*/

}