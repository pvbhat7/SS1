package com.sdgvvk.v1.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.modal.Level_1_cardModal;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class AllMembersAdapter extends RecyclerView.Adapter<AllMembersAdapter.ViewHolder> {


    private List<Level_1_cardModal> mItemList;

    Activity activity;


    public AllMembersAdapter(List<Level_1_cardModal> itemList, Activity activity) {
        mItemList = itemList;
        this.activity = activity;
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

            holder.name.setText(obj.getFirstname()+" "+obj.getLastname());
            holder.cpid.setText(obj.getProfileId());
            Glide.with(activity)
                    .load(obj.getProfilephotoaddress())
                    .placeholder(R.drawable.oops)
                    .into(holder.photo);

            holder.card.setOnClickListener(view -> ApiCallUtil.getLevel2Data(obj.getProfileId(), activity));
        }
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name,cpid;
        CircularImageView photo;

        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.cpid = itemView.findViewById(R.id.cpid);
            this.photo = itemView.findViewById(R.id.photo);
            this.card= itemView.findViewById(R.id.card);
        }
    }


}
