package com.sdgvvk.v1.livedata;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

// Step 1: Define your ViewModel
public class YourViewModel extends ViewModel {
    private MutableLiveData<List<YourDataModel>> data = new MutableLiveData<>();
    private YourRepository repository;

    public YourViewModel() {
        repository = new YourRepository();
        fetchDataFromApi();
    }

    public LiveData<List<YourDataModel>> getData() {
        return data;
    }

    private void fetchDataFromApi() {
        // Use your repository to fetch data from the API
        repository.getDataFromApi(newData -> data.setValue(newData));
    }
}

