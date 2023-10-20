package com.sdgvvk.v1.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.modal.NotificationModal;
import com.sdgvvk.v1.modal.TransactionModal;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    ProgressBar progressBar;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private List<TransactionModal> mItemList;

    Activity activity;
    Dialog dialog;


    public TransactionAdapter(List<TransactionModal> itemList, Activity activity, Dialog dialog) {
        mItemList = itemList;
        this.activity = activity;
        this.dialog = dialog;
    }

    // Based on the View type we are instantiating the
    // ViewHolder in the onCreateViewHolder() method
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.transaction_list_item, parent, false);
        TransactionAdapter.ViewHolder viewHolder = new TransactionAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    // Inside the onBindViewHolder() method we
    // are checking the type of ViewHolder
    // instance and populating the row accordingly
    @Override
    public void onBindViewHolder(TransactionAdapter.ViewHolder holder, int position) {
        final TransactionModal obj = mItemList.get(position);
        holder.name.setText(obj.getName());
        holder.mobile.setText(obj.getMobile());
        holder.date.setText(obj.getDate());
        holder.amount.setText("Rs. "+obj.getAmount());
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

       public TextView name , mobile , date , amount;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.mobile = itemView.findViewById(R.id.mobile);
            this.date = itemView.findViewById(R.id.date);
            this.amount = itemView.findViewById(R.id.amount);
        }
    }


}
