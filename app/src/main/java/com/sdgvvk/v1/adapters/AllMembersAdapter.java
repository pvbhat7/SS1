package com.sdgvvk.v1.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.activity.AllMemberActivity;
import com.sdgvvk.v1.activity.Level2ProfileActivity;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.api.HelperUtils;
import com.sdgvvk.v1.modal.Level_1_cardModal;
import com.sdgvvk.v1.ui.ActionBottomSheetDialog;

import java.util.List;

public class AllMembersAdapter extends RecyclerView.Adapter<AllMembersAdapter.ViewHolder> {


    private List<Level_1_cardModal> mItemList;

    Activity activity;

    Boolean flag;

    public AllMembersAdapter(List<Level_1_cardModal> itemList, Activity activity, Boolean flag) {
        mItemList = itemList;
        this.activity = activity;
        this.flag = flag;
    }

    // Based on the View type we are instantiating the
    // ViewHolder in the onCreateViewHolder() method
    @Override
    public AllMembersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.allmembers_list, parent, false);
        AllMembersAdapter.ViewHolder viewHolder = new AllMembersAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    // Inside the onBindViewHolder() method we
    // are checking the type of ViewHolder
    // instance and populating the row accordingly
    @Override
    public void onBindViewHolder(AllMembersAdapter.ViewHolder holder, int position) {
        final Level_1_cardModal obj = mItemList.get(position);
        if (obj != null) {

            if(obj.getNoticount() != null && !obj.getNoticount().isEmpty()){
                holder.actionLink.setVisibility(View.GONE);
                holder.noticard.setVisibility(View.VISIBLE);
                holder.notitext.setText(String.valueOf(obj.getNoticount()));

                holder.call.setVisibility(View.VISIBLE);
                holder.deleteprofile.setVisibility(View.GONE);
            }
            else{
                holder.actionLink.setVisibility(View.VISIBLE);
                holder.noticard.setVisibility(View.GONE);

                holder.call.setVisibility(View.GONE);
                holder.deleteprofile.setVisibility(View.VISIBLE);
            }

            if(obj.getFollowup_flag() != null && obj.getFollowup_flag().equalsIgnoreCase("1")){
                holder.card.setCardBackgroundColor(Color.YELLOW);
            }
            else
                holder.card.setCardBackgroundColor(Color.WHITE);

            holder.creationDate.setText("created on : "+obj.getCreationdate());
            holder.name.setText(obj.getName());
            holder.cpid.setText(obj.getProfileId());
            if(flag || (obj.getProfilephotoaddress() != null && !obj.getProfilephotoaddress().isEmpty())){
                Glide.with(activity)
                        .load(obj.getProfilephotoaddress())
                        .placeholder(R.drawable.oops)
                        .into(holder.photo);
            }
            else{
                if(!flag)
                holder.photo.setVisibility(View.GONE);
            }


            holder.card.setOnClickListener(view -> activity.startActivity(new Intent(activity, Level2ProfileActivity.class)
                    .putExtra("level2data", obj.getProfileId())));

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

            holder.actionLink.setOnClickListener(view ->
                    new ActionBottomSheetDialog(activity,obj,holder.card).show(((AllMemberActivity)activity).getSupportFragmentManager(), "ActionBottomSheetDialog")
            );
            holder.noticard.setOnClickListener(view -> {
                Dialog d = new Dialog(activity);
                d.setContentView(R.layout.notificationlist_dialog);

                d.setContentView(R.layout.notificationlist_dialog);
                ApiCallUtil.getUserNotifications(activity, d,obj.getProfileId(), false);

                d.setCanceledOnTouchOutside(true);
                d.setCancelable(true);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
            });

            holder.call.setOnClickListener(view -> Dexter.withActivity(activity)
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
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name,cpid,creationDate,notitext;
        CircularImageView photo;
        ImageView deleteprofile,actionLink,call;

        CardView card,noticard;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.cpid = itemView.findViewById(R.id.cpid);
            this.photo = itemView.findViewById(R.id.photo);
            this.card= itemView.findViewById(R.id.card);
            this.deleteprofile= itemView.findViewById(R.id.deleteprofile);
            this.actionLink= itemView.findViewById(R.id.actionLink);
            this.creationDate= itemView.findViewById(R.id.creationDate);
            this.noticard= itemView.findViewById(R.id.noticard);
            this.notitext= itemView.findViewById(R.id.notitext);
            this.call= itemView.findViewById(R.id.call);

        }
    }


    public void updateList(List<Level_1_cardModal> list){
        mItemList = list;
        notifyDataSetChanged();
    }


}
