package com.example.ss1.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ss1.R;
import com.example.ss1.activity.Level2ProfileActivity;
import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.modal.ContactViewedModal;
import com.example.ss1.modal.NotificationModal;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class ContactViewedAdapter extends RecyclerView.Adapter<ContactViewedAdapter.ViewHolder> {


    private List<ContactViewedModal> mItemList;

    Activity activity;


    public ContactViewedAdapter(List<ContactViewedModal> itemList, Activity activity) {
        mItemList = itemList;
        this.activity = activity;
    }

    // Based on the View type we are instantiating the
    // ViewHolder in the onCreateViewHolder() method
    @Override
    public ContactViewedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.contactviewed_list_item, parent, false);
        ContactViewedAdapter.ViewHolder viewHolder = new ContactViewedAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    // Inside the onBindViewHolder() method we
    // are checking the type of ViewHolder
    // instance and populating the row accordingly
    @Override
    public void onBindViewHolder(ContactViewedAdapter.ViewHolder holder, int position) {
        final ContactViewedModal obj = mItemList.get(position);
        if (obj != null) {
            holder.cv_name.setText(obj.getName());
            holder.cv_city.setText(obj.getCity());
            holder.cv_age.setText(obj.getDob());
            holder.cv_viewedOn.setText("viewed on :"+obj.getViewedOn());
            Glide.with(activity)
                    .load(obj.getPhoto())
                    .into(holder.cv_photo);
            holder.cv_card.setOnClickListener(view -> ApiCallUtil.getLevel2Data(obj.getVcpid(), activity, false));

            holder.cv_whatstappicon.setOnClickListener(view -> {
                String uri = "https://wa.me/+91" + obj.getMobile().toString().trim();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                activity.startActivity(intent);
            });
            holder.cv_callicon.setOnClickListener(view -> Dexter.withActivity(activity)
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

            holder.cv_instaicon.setOnClickListener(view -> {

            });

        }
    }

    // getItemCount() method returns the size of the list
    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView cv_name,cv_city,cv_age,cv_viewedOn;

        public ImageView cv_photo,cv_callicon,cv_whatstappicon,cv_instaicon;
        CardView cv_card;
        public ViewHolder(View itemView) {
            super(itemView);
            this.cv_name = itemView.findViewById(R.id.cv_name);
            this.cv_city = itemView.findViewById(R.id.cv_city);
            this.cv_age = itemView.findViewById(R.id.cv_age);
            this.cv_viewedOn = itemView.findViewById(R.id.cv_viewedOn);
            this.cv_photo = itemView.findViewById(R.id.cv_photo);
            this.cv_card = itemView.findViewById(R.id.cv_card);
            this.cv_callicon = itemView.findViewById(R.id.cv_callicon);
            this.cv_whatstappicon = itemView.findViewById(R.id.cv_whatstappicon);
            this.cv_instaicon = itemView.findViewById(R.id.cv_instaicon);
        }
    }


}
