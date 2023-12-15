package com.sdgvvk.v1.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import com.sdgvvk.v1.activity.Level2ProfileActivity;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.modal.NotificationModal;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    ProgressBar progressBar;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private List<NotificationModal> mItemList;

    Activity activity;
    Dialog dialog;
    Boolean flag;

    public NotificationAdapter(List<NotificationModal> itemList, Activity activity, Dialog dialog, Boolean flag) {
        mItemList = itemList;
        this.activity = activity;
        this.dialog = dialog;
        this.flag = flag;
    }

    // Based on the View type we are instantiating the
    // ViewHolder in the onCreateViewHolder() method
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.notifications_list_item, parent, false);
        NotificationAdapter.ViewHolder viewHolder = new NotificationAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    // Inside the onBindViewHolder() method we
    // are checking the type of ViewHolder
    // instance and populating the row accordingly
    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, int position) {
        final NotificationModal obj = mItemList.get(position);
        if (obj != null) {
            if (flag) {
                String tit = "";
                if (obj.getTitle().contains("liked your profile"))
                    tit = obj.getTitle().replace("your", "");
                else if (obj.getTitle().contains("you"))
                    tit = obj.getTitle().replace("you", "");

                String[] arr = tit.split(" ");
                String title_ = arr[1] + " " + arr[0] + " " + arr[3];

                holder.notilist_title.setText(title_);
            } else
                holder.notilist_title.setText(obj.getTitle());
            holder.notilist_time.setText(obj.getTime());
            Glide.with(activity)
                    .load(obj.getPhoto())
                    .into(holder.notilist_photo);

            Glide.with(activity)
                    .load(obj.getIs_viewed().equalsIgnoreCase("1") ? R.drawable.bluetick : R.drawable.graytick)
                    .into(holder.tickimg);


            holder.noti_card.setOnClickListener(view -> {
                if (!flag) {
                    if (obj.getIs_viewed().equalsIgnoreCase("0"))
                        ApiCallUtil.updateViewedNotificationState(String.valueOf(obj.getId()));
                    dialog.dismiss();
                }

                activity.startActivity(new Intent(activity, Level2ProfileActivity.class)
                        .putExtra("level2data", obj.getVcpid()));

            });


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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CircularImageView notilist_photo;
        public TextView notilist_title, notilist_time;

        public ImageView tickimg;

        public CardView noti_card;

        public ViewHolder(View itemView) {
            super(itemView);
            this.notilist_photo = itemView.findViewById(R.id.notilist_photo);
            this.notilist_title = itemView.findViewById(R.id.notilist_title);
            this.notilist_time = itemView.findViewById(R.id.notilist_time);
            this.tickimg = itemView.findViewById(R.id.tickimg);
            this.noti_card = itemView.findViewById(R.id.noti_card);
        }
    }


}
