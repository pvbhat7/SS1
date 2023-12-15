package com.sdgvvk.v1.livedata;

import static com.sdgvvk.v1.livedata.DataCacheManager.cacheData;
import static com.sdgvvk.v1.livedata.DataCacheManager.loadCachedData;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdgvvk.v1.R;

import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private YourViewModel viewModel;
    private YourAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        viewModel = new ViewModelProvider(this).get(YourViewModel.class);

        showUserRecyclerView();
    }

    public void showUserRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.usersRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new YourAdapter();
        recyclerView.setAdapter(adapter);

        // Load cached data synchronously
        List<YourDataModel> cachedData = loadCachedData(this);
        adapter.setData(cachedData);
        adapter.notifyDataSetChanged();

        // Observe changes in LiveData
        viewModel.getData().observe(this, newData -> {
            // Update RecyclerView with the new data
            adapter.setData(newData);
            adapter.notifyDataSetChanged();

            // Cache the new data for future use
            cacheData(this,newData);
        });
    }
}
