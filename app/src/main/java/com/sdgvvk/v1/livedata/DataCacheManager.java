package com.sdgvvk.v1.livedata;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class DataCacheManager {

    private static final String PREF_NAME = "DataCachePrefs";
    private static final String KEY_CACHED_DATA = "cachedData";

    // Load cached data from SharedPreferences
    public static List<YourDataModel> loadCachedData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String cachedDataJson = sharedPreferences.getString(KEY_CACHED_DATA, null);

        if (cachedDataJson != null) {
            Type listType = new TypeToken<List<YourDataModel>>(){}.getType();
            return new Gson().fromJson(cachedDataJson, listType);
        } else {
            return null; // No cached data available
        }
    }

    // Cache new data to SharedPreferences
    public static void cacheData(Context context, List<YourDataModel> newData) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String newDataJson = new Gson().toJson(newData);
        editor.putString(KEY_CACHED_DATA, newDataJson);
        editor.apply();
    }
    // Update cached data for a specific item
    public static void updateCachedItem(Context context, YourDataModel updatedItem) {
        List<YourDataModel> cachedData = loadCachedData(context);

        if (cachedData != null) {
            for (int i = 0; i < cachedData.size(); i++) {
                if (cachedData.get(i).getId().equals(updatedItem.getId())) {
                    // Update the specific item in the cached data
                    cachedData.set(i, updatedItem);
                    cacheData(context, cachedData); // Update the cached data
                    break;
                }
            }
        }
    }
}
