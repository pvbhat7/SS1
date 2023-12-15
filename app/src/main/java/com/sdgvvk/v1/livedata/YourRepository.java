package com.sdgvvk.v1.livedata;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdgvvk.v1.adapters.FollowUpAdapter;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.api.RetrofitClient;
import com.sdgvvk.v1.modal.FollowUpModal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Step 2: Define your Repository
public class YourRepository {
    public interface DataCallback {
        void onDataLoaded(List<YourDataModel> data);
    }

    public void getDataFromApi(DataCallback callback) {
        new GetAllUsersTask(callback).execute();
    }

    static class GetAllUsersTask extends AsyncTask<Void, Void, Void> {


        DataCallback callback;
        List<YourDataModel> list = new ArrayList<>();

        public GetAllUsersTask(DataCallback cb) {
            callback =  cb;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.i("ss_nw_call", new Date() + " api call : GetAllUsersTask");
                list = RetrofitClient.getInstance().getApi().getAllUsers().execute().body();
            } catch (Exception e) {
                Log.i("ss_nw_call", "error"+e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (list != null && !list.isEmpty()) {
                Log.i("ss_nw_call", "GetAllUsersTask list size"+list.size());
                callback.onDataLoaded(list);
            }
            else
                Log.i("ss_nw_call", "GetAllUsersTask list is empty");
        }
    }}