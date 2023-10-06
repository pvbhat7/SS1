package com.example.ss1.adapters;

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
import com.example.ss1.R;
import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.modal.ContactViewedModal;
import com.example.ss1.modal.MyMembershipModal;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MyMembershipAdapter extends RecyclerView.Adapter<MyMembershipAdapter.ViewHolder> {


    private List<MyMembershipModal> mItemList;

    Activity activity;


    public MyMembershipAdapter(List<MyMembershipModal> itemList, Activity activity) {
        mItemList = itemList;
        this.activity = activity;
    }

    // Based on the View type we are instantiating the
    // ViewHolder in the onCreateViewHolder() method
    @Override
    public MyMembershipAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.membership_list_item, parent, false);
        MyMembershipAdapter.ViewHolder viewHolder = new MyMembershipAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    // Inside the onBindViewHolder() method we
    // are checking the type of ViewHolder
    // instance and populating the row accordingly
    @Override
    public void onBindViewHolder(MyMembershipAdapter.ViewHolder holder, int position) {
        final MyMembershipModal obj = mItemList.get(position);
        if (obj != null) {

            holder.m_name.setText(obj.getName());
            holder.m_fees.setText(obj.getFees());
            holder.m_totalbalance.setText(obj.getTotalbalance());
            holder.m_balanceleft.setText(obj.getBalanceleft());
            holder.m_mode.setText(obj.getMode());
            holder.m_txn.setText(obj.getTxn());


        }
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView m_name  , m_totalbalance , m_balanceleft , m_fees , m_mode , m_txn;
        public ViewHolder(View itemView) {
            super(itemView);
            this.m_name = itemView.findViewById(R.id.m_name);
            this.m_totalbalance = itemView.findViewById(R.id.m_totalbalance);
            this.m_balanceleft = itemView.findViewById(R.id.m_balanceleft);
            this.m_fees = itemView.findViewById(R.id.m_fees);
            this.m_mode = itemView.findViewById(R.id.m_mode);
            this.m_txn = itemView.findViewById(R.id.m_txn);
        }
    }


}
