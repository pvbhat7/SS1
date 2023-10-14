package com.sdgvvk.v1.api;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String TAVROS_SERVER = "http://tavrostechinfo.com/PROGYM/progym_online/api/";
    public static final String GGS_SERVER = "http://tavrostechinfo.com/PROGYM/ggs/api/";

    public static final String MATRIMONY_SERVER = "http://tavrostechinfo.com/matrimony/api/";
    public static final String TEST_SERVER = "http://tavrostechinfo.com/PROGYM/test_api/api/";
    public static final String LOCALHOST_SERVER = "http://localhost/progym_online/api/";

    private static String BASE_URL = "http://192.168.1.11/progym_online/api/";
    private static RetrofitClient retrofitClient;
    private static Retrofit retrofit;

    private RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(MATRIMONY_SERVER)
                .build();
    }

    public static synchronized RetrofitClient getInstance(){
        if(retrofitClient == null)
            retrofitClient = new RetrofitClient();
        return retrofitClient;
    }

    public Api getApi(){
        return retrofit.create(Api.class);
    }
}
