package com.example.ss1.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ss1.R;
import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.modal.Level_1_cardModal;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

public class Level_1_shortprofilecardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ProgressBar progressBar;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private List<Level_1_cardModal> mItemList;

    Activity activity;


    public Level_1_shortprofilecardAdapter(List<Level_1_cardModal> itemList,Activity activity) {
        mItemList = itemList;
        this.activity = activity;
    }

    // Based on the View type we are instantiating the
    // ViewHolder in the onCreateViewHolder() method
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_1_short_profile_card_new, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingviewHolder(view);
        }
    }

    // Inside the onBindViewHolder() method we
    // are checking the type of ViewHolder
    // instance and populating the row accordingly
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) holder, position);
        } else if (holder instanceof LoadingviewHolder) {
            showLoadingView((LoadingviewHolder) holder, position);
        }
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

    private void showLoadingView(LoadingviewHolder viewHolder, int position) {
        // Progressbar would be displayed
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }

    private void populateItemRows(ItemViewHolder holder, int position) {
        final Level_1_cardModal obj = mItemList.get(position);
        if (obj != null) {

            holder.name.setText(obj.getFirstname() + " " + obj.getLastname());


            Glide.with(activity)
                    .load(obj.getProfilephotoaddress())
                    .placeholder(R.drawable.oops)
                    .into(holder.profilephoto);

            holder.level1short_cardview.setOnClickListener(view -> {
                ApiCallUtil.getLevel2Data(obj.getProfileId(), activity);
            });
        }
    }

    private class LoadingviewHolder extends RecyclerView.ViewHolder {
        //ProgressBar progressBar;
        SpinKitView spinKitView;

        public LoadingviewHolder(@NonNull View itemView) {
            super(itemView);
            spinKitView = itemView.findViewById(R.id.spinKitView);
        }

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView profilephoto;

        public CardView level1short_cardview;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.profilephoto = itemView.findViewById(R.id.profilephoto);
            this.level1short_cardview = itemView.findViewById(R.id.level1short_cardview);
        }
    }


}
