package com.sdgvvk.v1.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.modal.Customer;
import com.google.android.material.textfield.TextInputEditText;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class SearchedMembersAdapter extends RecyclerView.Adapter<SearchedMembersAdapter.ViewHolder> {


    private List<Customer> mItemList;

    Activity activity;

    Dialog d;


    public SearchedMembersAdapter(Dialog d, List<Customer> itemList, Activity activity) {
        mItemList = itemList;
        this.activity = activity;
        this.d = d;
    }

    // Based on the View type we are instantiating the
    // ViewHolder in the onCreateViewHolder() method
    @Override
    public SearchedMembersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.allmembers_list, parent, false);
        SearchedMembersAdapter.ViewHolder viewHolder = new SearchedMembersAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    // Inside the onBindViewHolder() method we
    // are checking the type of ViewHolder
    // instance and populating the row accordingly
    @Override
    public void onBindViewHolder(SearchedMembersAdapter.ViewHolder holder, int position) {
        final Customer obj = mItemList.get(position);
        if (obj != null) {

            holder.name.setText(obj.getFirstname()+" "+obj.getLastname());
            holder.cpid.setText(obj.getProfileId());
            Glide.with(activity)
                    .load(obj.getProfilephotoaddress())
                    .placeholder(R.drawable.oops)
                    .into(holder.photo);

            holder.card.setOnClickListener(view -> {
                ((TextView)d.findViewById(R.id.selectedProfileName)).setText(obj.getFirstname()+" "+obj.getLastname());
                ((TextView)d.findViewById(R.id.selectedProfileId)).setText(obj.getProfileId());

                if( !((AutoCompleteTextView)d.findViewById(R.id.selectplan)).getText().toString().trim().isEmpty()
                        && !((AutoCompleteTextView)d.findViewById(R.id.paymentmode)).getText().toString().trim().isEmpty()
                        && !((TextInputEditText)d.findViewById(R.id.txnDate)).getText().toString().trim().isEmpty()
                        && !((TextView)d.findViewById(R.id.selectedProfileName)).getText().toString().trim().isEmpty()
                        && !((TextView)d.findViewById(R.id.selectedProfileId)).getText().toString().trim().isEmpty())
                    d.findViewById(R.id.submitBtn).setEnabled(true);
                else
                    d.findViewById(R.id.submitBtn).setEnabled(false);

            });



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
