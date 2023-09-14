package com.example.ss1;

import android.app.Activity;

import com.example.ss1.modal.Customer;
import com.example.ss1.modal.Level_1_cardModal;
import com.example.ss1.modal.OrderModal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocalCache {


    public static String saveLoggedInCustomer(Customer customer, Activity activity){
        String jsonString = new Gson().toJson(customer);
        AppPreference.setStringPref(activity, AppPreference.CUSTOMER_JSON, AppPreference.CUSTOMER_JSON,jsonString);
        return jsonString;
    }

    public static Customer retrieveLoggedInCustomer(Activity activity) {
        Customer customer;
        String jsonString = AppPreference.getStringPref(activity, AppPreference.CUSTOMER_JSON, AppPreference.CUSTOMER_JSON);
        if (!jsonString.isEmpty())
            customer = new Gson().fromJson(jsonString, Customer.class);
        else
            customer = new Customer();
        return customer;
    }

    public static Customer convertJsonToObjectCustomer(String json) {
        return new Gson().fromJson(json, Customer.class);
    }

    public static String saveActiveOrder(OrderModal orderModal, Activity activity){
        String jsonString = new Gson().toJson(orderModal);
        AppPreference.setStringPref(activity, AppPreference.ACTIVE_ORDER_JSON, AppPreference.ACTIVE_ORDER_JSON,jsonString);
        return jsonString;
    }

    public static OrderModal retrieveActiveOrder(Activity activity) {
        OrderModal orderModal;
        String jsonString = AppPreference.getStringPref(activity, AppPreference.ACTIVE_ORDER_JSON, AppPreference.ACTIVE_ORDER_JSON);
        if (!jsonString.isEmpty())
            orderModal = new Gson().fromJson(jsonString, OrderModal.class);
        else
            orderModal = new OrderModal();
        return orderModal;
    }

    public static String saveLevel1List(List<Level_1_cardModal> list, Activity activity){
        String jsonString = new Gson().toJson(list);
        AppPreference.setStringPref(activity, AppPreference.LEVEL_1_LIST_JSON, AppPreference.LEVEL_1_LIST_JSON,jsonString);
        return jsonString;
    }

    public static List<Level_1_cardModal> retrieveLevel1List(Activity activity) {
        List<Level_1_cardModal> list = new ArrayList<>();
        String jsonString = (String) AppPreference.getStringPref(activity, AppPreference.LEVEL_1_LIST_JSON, AppPreference.LEVEL_1_LIST_JSON);
        if (!jsonString.isEmpty()){
            Type type = new TypeToken<ArrayList<Level_1_cardModal>>() {}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, type);
        }
        return list;
    }



}
