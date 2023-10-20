package com.sdgvvk.v1.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sdgvvk.v1.LocalCache;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.modal.ContactViewedModal;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sdgvvk.v1.modal.Customer;

import java.net.URLEncoder;
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
            holder.cv_card.setOnClickListener(view -> ApiCallUtil.getLevel2Data(obj.getVcpid(), activity));

            holder.cv_whatstappicon.setOnClickListener(view -> {
                Customer loggedinCustomer = LocalCache.getLoggedInCustomer(activity);
                String phoneNumber = "+91" + obj.getMobile().toString().trim();
                String message = "Hi "+obj.getName()+", "+
                        "\n\nMy name is "+loggedinCustomer.getFirstname()+" , I viewed your profile on sri datt guru vadhu var kendra app.\n" +
                        "\n\nI am interested in your profile, can we connect to discuss further." +
                        "\n\nApp link: \nhttps://play.google.com/store/apps/details?id=com.sdgvvk.v1\n" +
                        "\n "+obj.getName()+"'s profile: \nhttps://tavrostechinfo.com/profile?id="+obj.getVcpid()+"\n" +
                        "\n "+loggedinCustomer.getFirstname()+"'s profile: \nhttps://tavrostechinfo.com/profile?id="+loggedinCustomer.getProfileId()+"\n";
                // create an Intent to send data to the whatsapp
                Intent intent = new Intent(Intent.ACTION_VIEW);    // setting action

                // setting data url, if we not catch the exception then it shows an error
                try {
                    String url = "https://api.whatsapp.com/send?phone="+phoneNumber+"" + "&text=" + URLEncoder.encode(message, "UTF-8");
                    intent.setData(Uri.parse(url));
                    activity.startActivity(intent);
                }
                catch(Exception e){
                    Log.d("notSupport", "thrown by encoder");
                }
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
