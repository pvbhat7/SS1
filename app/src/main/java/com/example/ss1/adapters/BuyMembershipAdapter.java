package com.example.ss1.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ss1.LocalCache;
import com.example.ss1.R;
import com.example.ss1.modal.MembershipModal;
import com.example.ss1.modal.MyMembershipModal;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class BuyMembershipAdapter extends RecyclerView.Adapter<BuyMembershipAdapter.ViewHolder> {


    private List<MembershipModal> mItemList;

    Activity activity;


    public BuyMembershipAdapter(List<MembershipModal> itemList, Activity activity) {
        mItemList = itemList;
        this.activity = activity;
    }

    // Based on the View type we are instantiating the
    // ViewHolder in the onCreateViewHolder() method
    @Override
    public BuyMembershipAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.buymembership_list, parent, false);
        BuyMembershipAdapter.ViewHolder viewHolder = new BuyMembershipAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    // Inside the onBindViewHolder() method we
    // are checking the type of ViewHolder
    // instance and populating the row accordingly
    @Override
    public void onBindViewHolder(BuyMembershipAdapter.ViewHolder holder, int position) {
        final MembershipModal obj = mItemList.get(position);
        if (obj != null) {

            holder.plan_description.setText(obj.getDescription());
            holder.plan_fees.setText("Rs."+obj.getFees()+"/-");
            holder.plan_count.setText(obj.getCount()+" contacts");
            holder.plan_buybtn.setOnClickListener(view -> Dexter.withActivity(activity)
                    .withPermissions(Manifest.permission.CALL_PHONE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                String adminPhone = LocalCache.getAdminPhone(activity);
                                if(adminPhone.isEmpty() || adminPhone == "")
                                    callIntent.setData(Uri.parse("tel:+917972864487" ));
                                else
                                    callIntent.setData(Uri.parse("tel:+91"+adminPhone ));
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

        public TextView plan_description, plan_fees, plan_count;
        Button plan_buybtn;

        public ViewHolder(View itemView) {
            super(itemView);
            this.plan_description = itemView.findViewById(R.id.plan_description);
            this.plan_fees = itemView.findViewById(R.id.plan_fees);
            this.plan_count = itemView.findViewById(R.id.plan_count);
            this.plan_buybtn = itemView.findViewById(R.id.plan_buybtn);
        }
    }


}
