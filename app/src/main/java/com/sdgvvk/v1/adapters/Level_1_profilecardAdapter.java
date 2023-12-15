package com.sdgvvk.v1.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sdgvvk.v1.LocalCache;
import com.sdgvvk.v1.ProjectConstants;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.activity.Level2ProfileActivity;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.api.DateApi;
import com.sdgvvk.v1.api.HelperUtils;
import com.sdgvvk.v1.modal.Customer;
import com.sdgvvk.v1.modal.Level_1_cardModal;
import com.sdgvvk.v1.modal.NotificationModal;
import com.sdgvvk.v1.ui.home.HomeFragment;
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
    View view;
    SpinKitView sprogressBar;

    Customer customer;


    public Level_1_profilecardAdapter(SpinKitView sprogressBar , View view, List<Level_1_cardModal> itemList, Fragment fragment, Activity activity) {
        mItemList = itemList;
        this.activity = activity;
        this.fragment = fragment;
        this.view = view;
        this.sprogressBar = sprogressBar;
        this.customer = LocalCache.getLoggedInCustomer(activity);
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

                // notification notice if present then isadmin = 999
                if(obj.getIsAdmin() != null && obj.getIsAdmin().equalsIgnoreCase("999")){
                    holder.level1_cardview.setVisibility(View.GONE);
                    holder.notification_cardview.setVisibility(View.VISIBLE);

                    // photo
                    Glide.with(activity)
                            .load(obj.getProfilephotoaddress())
                            .placeholder(R.drawable.oops)
                            .into(holder.notiphoto);
                }
                else{
                    holder.level1_cardview.setVisibility(View.VISIBLE);
                    holder.notification_cardview.setVisibility(View.GONE);

                    if(customer.getIsAdmin() != null && customer.getIsAdmin().equalsIgnoreCase("1")) {
                        holder.admin_view.setVisibility(View.VISIBLE);
                        holder.deleteprofile.setOnClickListener(view -> {
                            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        HelperUtils.vibrateFunction(activity);
                                        mItemList.remove(position);
                                        notifyDataSetChanged();
                                        ApiCallUtil.disableProfile(activity,obj.getProfileId());
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder
                                    .setMessage("Delete profile ?")
                                    .setPositiveButton("yes", dialogClickListener)
                                    .setNegativeButton("no", dialogClickListener).show();
                        });
                        holder.callprofile.setOnClickListener(view -> Dexter.withActivity(activity)
                                .withPermissions(Manifest.permission.CALL_PHONE)
                                .withListener(new MultiplePermissionsListener() {
                                    @Override
                                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                                        if (report.areAllPermissionsGranted()) {
                                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                                            callIntent.setData(Uri.parse("tel:+91" + obj.getMobile().toString().trim()));
                                            activity.startActivity(callIntent);
                                        }
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                        token.continuePermissionRequest();
                                    }
                                }).check());
                    }
                    else
                        holder.non_admin_view.setVisibility(View.VISIBLE);

                    // profileCardId
                    holder.profileCardId.setText("Profile id : A"+obj.getProfileId());

                    // name
                    holder.name.setText(obj.getFirstname().trim() + " " + obj.getLastname().trim());

                    // dob
                    Date dob ;
                    dob = new SimpleDateFormat("dd/MM/yyyy").parse(obj.getDob());
                    int daysleft = DateApi.daysDiff(new Date(), dob);
                    //holder.age.setText(Math.abs(daysleft) / 365 + " yrs , Kolhapur");
                    String age = Math.abs(daysleft) / 365 + " yrs";
                    String city = obj.getCity() != null ? " , "+obj.getCity() : "";
                    holder.age.setText(age+city);


                    // photo
                    Glide.with(activity)
                            .load(obj.getProfilephotoaddress())
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
                        Log.i("ss_nw_call", "performance : level 1 card clicked ");
                        ApiCallUtil.clicked_level2activity = true;
                        ApiCallUtil.addLeads(customer.getProfileId(), obj.getProfileId(),"clicked_level1_profile_card");


                        activity.startActivity(new Intent(activity, Level2ProfileActivity.class)
                                .putExtra("level2data", obj.getProfileId()));
                    /*if(customer.getIs_verified() != null && customer.getIs_verified().equalsIgnoreCase("2"))
                    ApiCallUtil.getLevel2Data(obj.getProfileId(), activity);
                    else
                        ((HomeFragment)fragment).showSnackBar("Complete your profile to view "+obj.getFirstname()+"'s profile");*/

                    });

                    holder.like.setOnClickListener(view -> {
                        holder.like.setEnabled(false);
                        handleActionClick(activity, holder, fragment, obj, position, ProjectConstants.LIKE);
                    });

                    holder.shortlist.setOnClickListener(view -> {
                        holder.shortlist.setEnabled(false);
                        handleActionClick(activity, holder, fragment, obj, position, ProjectConstants.SHORTLIST);
                    });

                    holder.sendinterest.setOnClickListener(view -> {
                        holder.sendinterest.setEnabled(false);
                        handleActionClick(activity, holder, fragment, obj, position, ProjectConstants.SEND_INTEREST);
                    });

                    holder.ignore.setOnClickListener(view -> {
                        holder.ignore.setEnabled(false);
                        handleActionClick(activity, holder, fragment, obj, position, ProjectConstants.IGNORE);
                    });
                }




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
        public CardView level1_cardview,notification_cardview;
        public LinearLayout like, shortlist, sendinterest, ignore , non_admin_view , admin_view ,deleteprofile,callprofile;
        public ImageView like_img, shortlist_img, sendinterest_img, ignore_img, profilephoto,notiphoto;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.notiphoto = itemView.findViewById(R.id.notiphoto);

            this.profileCardId = itemView.findViewById(R.id.profileCardId);
            this.age = itemView.findViewById(R.id.age);
            this.interestSentText = itemView.findViewById(R.id.interestSentText);
            this.shortlistprofiletext = itemView.findViewById(R.id.shortlistprofiletext);
            this.likeprofiletext = itemView.findViewById(R.id.likeprofiletext);
            this.level1_cardview = itemView.findViewById(R.id.level1_cardview);
            this.notification_cardview = itemView.findViewById(R.id.notification_cardview);
            this.like = itemView.findViewById(R.id.like);
            this.shortlist = itemView.findViewById(R.id.shortlist);
            this.sendinterest = itemView.findViewById(R.id.sendinterest);
            this.ignore = itemView.findViewById(R.id.ignore);
            this.like_img = itemView.findViewById(R.id.like_img);
            this.shortlist_img = itemView.findViewById(R.id.shortlist_img);
            this.sendinterest_img = itemView.findViewById(R.id.sendinterest_img);
            this.ignore_img = itemView.findViewById(R.id.ignore_img);
            this.profilephoto = itemView.findViewById(R.id.profilephoto);
            this.deleteprofile = itemView.findViewById(R.id.deleteprofile);
            this.callprofile = itemView.findViewById(R.id.callprofile);
            this.non_admin_view = itemView.findViewById(R.id.non_admin_view);
            this.admin_view = itemView.findViewById(R.id.admin_view);

        }
    }

    private void handleActionClick(Activity activity, ItemViewHolder holder, Fragment fragment, Level_1_cardModal obj, int position, String action) {
        Customer customer = LocalCache.getLoggedInCustomer(activity);
        HelperUtils.vibrateFunction(activity);
        if (action.equalsIgnoreCase(ProjectConstants.LIKE)) {
            obj.setIsLiked("1");
            LocalCache.updateLevel1List(obj,activity, false);
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
            obj.setIsShortlisted("1");
            LocalCache.updateLevel1List(obj,activity, false);
            HelperUtils.vibrateFunction(activity);
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
            obj.setIsInterestsent("1");
            LocalCache.updateLevel1List(obj,activity, false);
            HelperUtils.vibrateFunction(activity);
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
            //((HomeFragment) fragment).showSnackBar("Profile removed");
            LocalCache.updateLevel1List(obj,activity,true);
            HelperUtils.vibrateFunction(activity);
            mItemList.remove(position);
            notifyDataSetChanged();
            ApiCallUtil.addToNotInterestedProfiles(customer.getProfileId(), obj.getProfileId());
            ApiCallUtil.addNotification(new NotificationModal(customer.getProfileId(),obj.getProfileId(),ProjectConstants.ACTION_IGNORE_PROFILE));
            ApiCallUtil.getAllProfiles(customer.getProfileId(),fragment, sprogressBar,activity,true);
        }

        ApiCallUtil.syncUserActivity(customer.getProfileId(),activity);
    }

    public void updateList(List<Level_1_cardModal> list){
        mItemList = list;
        notifyDataSetChanged();
    }


}
