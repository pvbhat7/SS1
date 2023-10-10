package com.example.ss1;

import android.app.Activity;

import com.example.ss1.modal.ContactViewedModal;
import com.example.ss1.modal.Customer;
import com.example.ss1.modal.Stat;
import com.example.ss1.modal.Level_1_cardModal;
import com.example.ss1.modal.MembershipModal;
import com.example.ss1.modal.OrderModal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocalCache {


    public static String setLoggedInCustomer(Customer customer, Activity activity){
        String jsonString = new Gson().toJson(customer);
        AppPreference.setStringPref(activity, AppPreference.CUSTOMER_JSON, AppPreference.CUSTOMER_JSON,jsonString);
        return jsonString;
    }

    public static Customer getLoggedInCustomer(Activity activity) {
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

    public static String setActiveOrder(OrderModal orderModal, Activity activity){
        String jsonString = new Gson().toJson(orderModal);
        AppPreference.setStringPref(activity, AppPreference.ACTIVE_ORDER_JSON, AppPreference.ACTIVE_ORDER_JSON,jsonString);
        return jsonString;
    }

    public static OrderModal getActiveOrder(Activity activity) {
        OrderModal orderModal;
        String jsonString = AppPreference.getStringPref(activity, AppPreference.ACTIVE_ORDER_JSON, AppPreference.ACTIVE_ORDER_JSON);
        if (!jsonString.isEmpty())
            orderModal = new Gson().fromJson(jsonString, OrderModal.class);
        else
            orderModal = new OrderModal();
        return orderModal;
    }

    public static String setLevel1List(List<Level_1_cardModal> list, Activity activity){
        String jsonString = new Gson().toJson(list);
        AppPreference.setStringPref(activity, AppPreference.LEVEL_1_LIST_JSON, AppPreference.LEVEL_1_LIST_JSON,jsonString);
        return jsonString;
    }

    public static List<Level_1_cardModal> getLevel1List(Activity activity) {
        List<Level_1_cardModal> list = new ArrayList<>();
        String jsonString = (String) AppPreference.getStringPref(activity, AppPreference.LEVEL_1_LIST_JSON, AppPreference.LEVEL_1_LIST_JSON);
        if (!jsonString.isEmpty()){
            Type type = new TypeToken<ArrayList<Level_1_cardModal>>() {}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, type);
        }
        return list;
    }

    public static String setContactViewedList(List<ContactViewedModal> list, Activity activity){
        String jsonString = new Gson().toJson(list);
        AppPreference.setStringPref(activity, AppPreference.CONTACT_VIEWED_LIST_JSON, AppPreference.CONTACT_VIEWED_LIST_JSON,jsonString);
        return jsonString;
    }

    public static List<ContactViewedModal> getContactViewedList(Activity activity) {
        List<ContactViewedModal> list = new ArrayList<>();
        String jsonString = AppPreference.getStringPref(activity, AppPreference.CONTACT_VIEWED_LIST_JSON, AppPreference.CONTACT_VIEWED_LIST_JSON);
        if (!jsonString.isEmpty()){
            Type type = new TypeToken<ArrayList<ContactViewedModal>>() {}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, type);
        }
        return list;
    }

    public static String setGenderStat(List<Stat> list, Activity activity){
        String jsonString = new Gson().toJson(list);
        AppPreference.setStringPref(activity, AppPreference.GENDER_STAT_JSON, AppPreference.GENDER_STAT_JSON,jsonString);
        return jsonString;
    }

    public static List<Stat> getGenderStat(Activity activity) {
        List<Stat> list = new ArrayList<>();
        String jsonString = (String) AppPreference.getStringPref(activity, AppPreference.GENDER_STAT_JSON, AppPreference.GENDER_STAT_JSON);
        if (!jsonString.isEmpty()){
            Type type = new TypeToken<ArrayList<Stat>>() {}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, type);
        }
        return list;
    }


    public static String setMembershipList(List<MembershipModal> list, Activity activity){
        String jsonString = new Gson().toJson(list);
        AppPreference.setStringPref(activity, AppPreference.MEMBERSHIP_LIST_JSON, AppPreference.MEMBERSHIP_LIST_JSON,jsonString);
        return jsonString;
    }

    public static List<MembershipModal> getMembershipList(Activity activity) {
        List<MembershipModal> list = new ArrayList<>();
        String jsonString = AppPreference.getStringPref(activity, AppPreference.MEMBERSHIP_LIST_JSON, AppPreference.MEMBERSHIP_LIST_JSON);
        if (!jsonString.isEmpty()){
            Type type = new TypeToken<ArrayList<MembershipModal>>() {}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, type);
        }
        return list;
    }

    public static String setAdminPhone(String adminPhone, Activity activity){
        String jsonString = new Gson().toJson(adminPhone);
        AppPreference.setStringPref(activity, AppPreference.ADMINPHONE_JSON, AppPreference.ADMINPHONE_JSON,jsonString);
        return jsonString;
    }

    public static String getAdminPhone(Activity activity) {
        String adminPhone;
        String jsonString = AppPreference.getStringPref(activity, AppPreference.ADMINPHONE_JSON, AppPreference.ADMINPHONE_JSON);
        if (!jsonString.isEmpty())
            adminPhone = new Gson().fromJson(jsonString, String.class);
        else
            adminPhone = new String();
        return adminPhone;
    }



}
