package com.sdgvvk.v1.livedata;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sdgvvk.v1.R;
import com.sdgvvk.v1.api.RetrofitClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class YourAdapter extends RecyclerView.Adapter<YourAdapter.ViewHolder> {


    private List<YourDataModel> data;

    Activity activity;


    public YourAdapter(List<YourDataModel> itemList, Activity activity) {
        data = itemList;
        this.activity = activity;
    }

    public YourAdapter() {

    }
    // Based on the View type we are instantiating the
    // ViewHolder in the onCreateViewHolder() method
    @Override
    public YourAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.users_list, parent, false);
        YourAdapter.ViewHolder viewHolder = new YourAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    // Inside the onBindViewHolder() method we
    // are checking the type of ViewHolder
    // instance and populating the row accordingly
    @Override
    public void onBindViewHolder(YourAdapter.ViewHolder holder, int position) {
        Log.i("ss_nw_call", "GetAllUsersTask onBindViewHolder called");

        if (data != null && position < data.size()) {
            YourDataModel obj = data.get(position);
            if (obj != null) {
                holder.name.setText(obj.getName());
                holder.city.setText(obj.getCity());
                holder.age.setText(obj.getAge());
                if(obj.getIsApproved() != null){
                    if(obj.getIsApproved().equalsIgnoreCase("1"))
                        holder.button.setText("approve");
                    else if(obj.getIsApproved().equalsIgnoreCase("0"))
                        holder.button.setText("reject");
                }

                holder.button.setOnClickListener(view -> {
                    if(obj.getIsApproved().equalsIgnoreCase("1")){
                        obj.setIsApproved("0");
                        DataCacheManager.updateCachedItem(view.getContext(), obj);
                        holder.button.setText("reject");
                        new updateActionTask("0",obj.getId()).execute();
                    }
                    else if(obj.getIsApproved().equalsIgnoreCase("0")){
                        holder.button.setText("approve");
                        obj.setIsApproved("1");
                        DataCacheManager.updateCachedItem(view.getContext(), obj);
                        new updateActionTask("1",obj.getId()).execute();
                    }
                });

            }
        }
    }

    // getItemCount() method returns the size of the list
    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setData(List<YourDataModel> newData) {
        data = newData;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name,city,age;
        public Button button;
        public ViewHolder(View itemView) {
            super(itemView);
            this.button = itemView.findViewById(R.id.button);
            this.name = itemView.findViewById(R.id.name);
            this.city = itemView.findViewById(R.id.city);
            this.age = itemView.findViewById(R.id.age);
        }
    }


    static class updateActionTask extends AsyncTask<Void, Void, Void> {


        String isApproved;
        String id;

        public updateActionTask(String isApproved , String id) {
           this.isApproved = isApproved;
           this.id = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : GetAllUsersTask");

                RetrofitClient.getInstance().getApi().updateUserStatus(isApproved,id).execute();
            } catch (Exception e) {
                Log.i("ss_nw_call", "error"+e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

}
