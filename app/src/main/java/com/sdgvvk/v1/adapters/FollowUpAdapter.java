package com.sdgvvk.v1.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.activity.Level2ProfileActivity;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.modal.FollowUpModal;

import java.util.List;

public class FollowUpAdapter extends RecyclerView.Adapter<FollowUpAdapter.ViewHolder> {


    private List<FollowUpModal> mItemList;

    Activity activity;


    public FollowUpAdapter(List<FollowUpModal> itemList, Activity activity) {
        mItemList = itemList;
        this.activity = activity;
    }

    // Based on the View type we are instantiating the
    // ViewHolder in the onCreateViewHolder() method
    @Override
    public FollowUpAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.followup_list, parent, false);
        FollowUpAdapter.ViewHolder viewHolder = new FollowUpAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    // Inside the onBindViewHolder() method we
    // are checking the type of ViewHolder
    // instance and populating the row accordingly
    @Override
    public void onBindViewHolder(FollowUpAdapter.ViewHolder holder, int position) {
        final FollowUpModal obj = mItemList.get(position);
        if (obj != null) {
            holder.name.setText(obj.getName());
            holder.comment.setText(obj.getComment());
            holder.followupdate.setText(obj.getFollowupdate());
            holder.updatedate.setText("added on :"+obj.getUpdatedate());
            Glide.with(activity)
                    .load(obj.getPhoto())
                    .into(holder.photo);
            holder.remove.setOnClickListener(view -> {
                mItemList.remove(position);
                notifyDataSetChanged();
                ApiCallUtil.updateFollowUp(obj.getId());
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

            holder.card.setOnClickListener(view -> activity.startActivity(new Intent(activity, Level2ProfileActivity.class)
                    .putExtra("level2data", obj.getCpid())));
        }
    }

    // getItemCount() method returns the size of the list
    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name,followupdate,updatedate,comment;

        public ImageView photo,remove,call;

        public CardView card;
        public ViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.followupdate = itemView.findViewById(R.id.followupdate);
            this.updatedate = itemView.findViewById(R.id.updatedate);
            this.comment = itemView.findViewById(R.id.comment);
            this.photo = itemView.findViewById(R.id.photo);
            this.remove = itemView.findViewById(R.id.remove);
            this.call = itemView.findViewById(R.id.call);
            this.card = itemView.findViewById(R.id.card);
        }
    }


}
