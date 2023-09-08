package com.example.ss1.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ss1.LocalCache;
import com.example.ss1.ProjectConstants;
import com.example.ss1.R;
import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.api.DateApi;
import com.example.ss1.api.ApiUtils;
import com.example.ss1.modal.Customer;
import com.example.ss1.modal.Level_1_cardModal;
import com.example.ss1.modal.NotificationModal;
import com.example.ss1.ui.home.HomeFragment;
import com.github.ybq.android.spinkit.SpinKitView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Level_1_profilecardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ProgressBar progressBar;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private List<Level_1_cardModal> mItemList;

    Activity activity;
    Fragment fragment;
    Boolean flag;
    View view;
    SpinKitView sprogressBar;


    public Level_1_profilecardAdapter(SpinKitView sprogressBar , View view, List<Level_1_cardModal> itemList, Fragment fragment, Activity activity, boolean flag) {
        mItemList = itemList;
        this.activity = activity;
        this.fragment = fragment;
        this.flag = flag;
        this.view = view;
        this.sprogressBar = sprogressBar;
    }

    // Based on the View type we are instantiating the
    // ViewHolder in the onCreateViewHolder() method
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_1_profile_card_new, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingviewHolder(view);
        }
    }

    // Inside the onBindViewHolder() method we
    // are checking the type of ViewHolder
    // instance and populating the row accordingly
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) holder, position);
        } else if (holder instanceof LoadingviewHolder) {
            showLoadingView((LoadingviewHolder) holder, position);
        }
    }

    // getItemCount() method returns the size of the list
    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    // getItemViewType() method is the method where we check each element
    // of the list. If the element is NULL we set the view type as 1 else 0
    public int getItemViewType(int position) {
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void showLoadingView(LoadingviewHolder viewHolder, int position) {
        // Progressbar would be displayed
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }

    private void populateItemRows(ItemViewHolder holder, int position) {
        final Level_1_cardModal obj = mItemList.get(position);
        if (obj != null) {
            try {

                // profileCardId
                holder.profileCardId.setText("Profile id : A"+obj.getProfileId());

                // name
                holder.name.setText(obj.getFirstName() + " " + obj.getLastName());

                // dob
                Date dob = new SimpleDateFormat("dd/MM/yyyy").parse(obj.getDob());
                int daysleft = DateApi.daysDiff(new Date(), dob);
                holder.age.setText(Math.abs(daysleft) / 365 + " yrs");

                // photo
                Glide.with(activity)
                        .load(obj.getProfilePhotoAddress())
                        .placeholder(R.drawable.oops)
                        .into(holder.profilephoto);

                // like
                Glide.with(activity)
                        .load(obj.getIsLiked().equalsIgnoreCase("1") ? R.drawable.redheart : R.drawable.whiteheart)
                        .placeholder(R.drawable.oops)
                        .into(holder.like_img);
                holder.likeprofiletext.setText(obj.getIsLiked().equalsIgnoreCase("1") ? "Liked" : "Like");
                holder.like.setEnabled(obj.getIsLiked().equalsIgnoreCase("1") ? false : true);


                // shortlist
                Glide.with(activity)
                        .load(obj.getIsShortlisted().equalsIgnoreCase("1") ? R.drawable.star_golden : R.drawable.star_white)
                        .placeholder(R.drawable.oops)
                        .into(holder.shortlist_img);
                holder.shortlistprofiletext.setText(obj.getIsShortlisted().equalsIgnoreCase("1") ? "Shortlisted" : "Shortlist");
                holder.shortlist.setEnabled(obj.getIsShortlisted().equalsIgnoreCase("1") ? false : true);

                // interested profiles
                Glide.with(activity)
                        .load(obj.getIsInterestsent().equalsIgnoreCase("1") ? R.drawable.interestsent : R.drawable.sendinterest)
                        .placeholder(R.drawable.oops)
                        .into(holder.sendinterest_img);
                holder.interestSentText.setText(obj.getIsInterestsent().equalsIgnoreCase("1") ? "Interest Sent" : "Send Interest");
                holder.sendinterest.setEnabled(obj.getIsInterestsent().equalsIgnoreCase("1") ? false : true);


                holder.profilephoto.setOnClickListener(view -> {

                    ApiCallUtil.getLevel2Data(obj.getProfileId(), activity, flag);

                });

                holder.like.setOnClickListener(view -> {
                    handleActionClick(activity, holder, fragment, obj, position, ProjectConstants.LIKE);
                });

                holder.shortlist.setOnClickListener(view -> {
                    handleActionClick(activity, holder, fragment, obj, position, ProjectConstants.SHORTLIST);
                });

                holder.sendinterest.setOnClickListener(view -> {
                    handleActionClick(activity, holder, fragment, obj, position, ProjectConstants.SEND_INTEREST);
                });

                holder.ignore.setOnClickListener(view -> {
                    handleActionClick(activity, holder, fragment, obj, position, ProjectConstants.IGNORE);
                });

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private class LoadingviewHolder extends RecyclerView.ViewHolder {
        //ProgressBar progressBar;
        SpinKitView spinKitView;

        public LoadingviewHolder(@NonNull View itemView) {
            super(itemView);
            spinKitView = itemView.findViewById(R.id.spinKitView);
        }

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView name, interestSentText, age, shortlistprofiletext, likeprofiletext,profileCardId;
        public CardView level1_cardview;
        public LinearLayout like, shortlist, sendinterest, ignore;
        public ImageView like_img, shortlist_img, sendinterest_img, ignore_img, profilephoto;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.profileCardId = itemView.findViewById(R.id.profileCardId);
            this.age = itemView.findViewById(R.id.age);
            this.interestSentText = itemView.findViewById(R.id.interestSentText);
            this.shortlistprofiletext = itemView.findViewById(R.id.shortlistprofiletext);
            this.likeprofiletext = itemView.findViewById(R.id.likeprofiletext);
            this.level1_cardview = itemView.findViewById(R.id.level1_cardview);
            this.like = itemView.findViewById(R.id.like);
            this.shortlist = itemView.findViewById(R.id.shortlist);
            this.sendinterest = itemView.findViewById(R.id.sendinterest);
            this.ignore = itemView.findViewById(R.id.ignore);
            this.like_img = itemView.findViewById(R.id.like_img);
            this.shortlist_img = itemView.findViewById(R.id.shortlist_img);
            this.sendinterest_img = itemView.findViewById(R.id.sendinterest_img);
            this.ignore_img = itemView.findViewById(R.id.ignore_img);
            this.profilephoto = itemView.findViewById(R.id.profilephoto);

        }
    }

    private void handleActionClick(Activity activity, ItemViewHolder holder, Fragment fragment, Level_1_cardModal obj, int position, String action) {
        Customer customer = LocalCache.retrieveLoggedInCustomer(activity);
        ApiUtils.vibrateFunction(activity);

        if (action.equalsIgnoreCase(ProjectConstants.LIKE)) {
            Glide.with(activity)
                    .load(R.drawable.redheart)
                    .placeholder(R.drawable.whiteheart)
                    .into(holder.like_img);
            holder.like.setEnabled(false);
            holder.likeprofiletext.setText("Liked");
            ((HomeFragment) fragment).showSnackBar("Profile Liked");
            ApiCallUtil.addToLikedProfiles(customer.getProfileId(), obj.getProfileId());
            ApiCallUtil.addNotification(new NotificationModal(customer.getProfileId(),obj.getProfileId(),ProjectConstants.ACTION_LIKE_PROFILE));
        } else if (action.equalsIgnoreCase(ProjectConstants.SHORTLIST)) {
            ApiUtils.vibrateFunction(activity);
            Glide.with(activity)
                    .load(R.drawable.star_golden)
                    .placeholder(R.drawable.star_white)
                    .into(holder.shortlist_img);
            holder.shortlist.setEnabled(false);
            holder.shortlistprofiletext.setText("Shortlisted");
            ((HomeFragment) fragment).showSnackBar("Profile Shortlisted");
            ApiCallUtil.addToShortListedProfiles(customer.getProfileId(), obj.getProfileId());
            ApiCallUtil.addNotification(new NotificationModal(customer.getProfileId(),obj.getProfileId(),ProjectConstants.ACTION_SHORTLIST_PROFILE));
        } else if (action.equalsIgnoreCase(ProjectConstants.SEND_INTEREST)) {
            ApiUtils.vibrateFunction(activity);
            Glide.with(activity)
                    .load(R.drawable.interestsent)
                    .placeholder(R.drawable.star_white)
                    .into(holder.sendinterest_img);
            holder.sendinterest.setEnabled(false);
            holder.interestSentText.setText("Interest sent");
            ((HomeFragment) fragment).showSnackBar("Interest Sent");
            ApiCallUtil.addToInterestedProfiles(customer.getProfileId(), obj.getProfileId());
            ApiCallUtil.addNotification(new NotificationModal(customer.getProfileId(),obj.getProfileId(),ProjectConstants.ACTION_SEND_INTEREST));
        } else if (action.equalsIgnoreCase(ProjectConstants.IGNORE)) {
            ((HomeFragment) fragment).showSnackBar("Profile removed");
            ApiUtils.vibrateFunction(activity);
            mItemList.remove(position);
            notifyDataSetChanged();
            ApiCallUtil.addToNotInterestedProfiles(customer.getProfileId(), obj.getProfileId());
            ApiCallUtil.addNotification(new NotificationModal(customer.getProfileId(),obj.getProfileId(),ProjectConstants.ACTION_IGNORE_PROFILE));
            ApiCallUtil.getAllProfiles(customer.getProfileId(),fragment, sprogressBar,activity,true);
        }
    }


}
