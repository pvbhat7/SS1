package com.example.ss1.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ss1.R;
import com.example.ss1.modal.MembershipModal;
import com.example.ss1.modal.MyMembershipModal;

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
            holder.plan_days.setText(obj.getDays()+" days");
            holder.plan_buybtn.setOnClickListener(view -> {
                // TODO: 09-Sep-23
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView plan_description, plan_fees, plan_days, plan_count;
        Button plan_buybtn;

        public ViewHolder(View itemView) {
            super(itemView);
            this.plan_description = itemView.findViewById(R.id.plan_description);
            this.plan_fees = itemView.findViewById(R.id.plan_fees);
            this.plan_days = itemView.findViewById(R.id.plan_days);
            this.plan_count = itemView.findViewById(R.id.plan_count);
            this.plan_buybtn = itemView.findViewById(R.id.plan_buybtn);
        }
    }


}
