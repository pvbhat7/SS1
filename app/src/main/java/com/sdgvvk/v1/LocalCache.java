package com.sdgvvk.v1;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sdgvvk.v1.modal.ContactViewedModal;
import com.sdgvvk.v1.modal.Customer;
import com.sdgvvk.v1.modal.CustomerActivityModal;
import com.sdgvvk.v1.modal.Level_1_cardModal;
import com.sdgvvk.v1.modal.MembershipModal;
import com.sdgvvk.v1.modal.OrderModal;
import com.sdgvvk.v1.modal.SingleResponse;
import com.sdgvvk.v1.modal.Stat;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class LocalCache {


    public static void setLoggedInCustomer(Customer customer, Activity activity){
        if(customer != null){
            String jsonString = new Gson().toJson(customer);
            AppPreference.setStringPref(activity, AppPreference.CUSTOMER_JSON, AppPreference.CUSTOMER_JSON,jsonString);
        }
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

    public static void setActiveOrder(OrderModal orderModal, Activity activity){
        if(orderModal != null){
            String jsonString = new Gson().toJson(orderModal);
            AppPreference.setStringPref(activity, AppPreference.ACTIVE_ORDER_JSON, AppPreference.ACTIVE_ORDER_JSON,jsonString);
        }
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

    public static void setLevel1List(List<Level_1_cardModal> list, Activity activity){
        String jsonString = new Gson().toJson(list);
        AppPreference.setStringPref(activity, AppPreference.LEVEL_1_LIST_JSON, AppPreference.LEVEL_1_LIST_JSON,jsonString);
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

    public static void setContactViewedList(List<ContactViewedModal> list, Activity activity){
        String jsonString = new Gson().toJson(list);
        AppPreference.setStringPref(activity, AppPreference.CONTACT_VIEWED_LIST_JSON, AppPreference.CONTACT_VIEWED_LIST_JSON,jsonString);
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

    public static void setGenderStat(List<Stat> list, Activity activity){
        String jsonString = new Gson().toJson(list);
        AppPreference.setStringPref(activity, AppPreference.GENDER_STAT_JSON, AppPreference.GENDER_STAT_JSON,jsonString);
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


    public static void setMembershipList(List<MembershipModal> list, Activity activity){
        String jsonString = new Gson().toJson(list);
        AppPreference.setStringPref(activity, AppPreference.MEMBERSHIP_LIST_JSON, AppPreference.MEMBERSHIP_LIST_JSON,jsonString);
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

    public static void setAdminPhone(String adminPhone, Activity activity){
        String jsonString = new Gson().toJson(adminPhone);
        AppPreference.setStringPref(activity, AppPreference.ADMINPHONE_JSON, AppPreference.ADMINPHONE_JSON,jsonString);
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


    public static void setIsLive(String flag, Activity activity) {
        String jsonString = new Gson().toJson(flag);
        AppPreference.setStringPref(activity, AppPreference.IS_LIVE_JSON, AppPreference.IS_LIVE_JSON,jsonString);
    }

    public static String getIsLive(Activity activity) {
        String flag;
        String jsonString = AppPreference.getStringPref(activity, AppPreference.IS_LIVE_JSON, AppPreference.IS_LIVE_JSON);
        if (!jsonString.isEmpty())
            flag = new Gson().fromJson(jsonString, String.class);
        else
            flag = new String();
        return flag;
    }

    public static void setEducationList(List<SingleResponse> list, Activity activity){
        List<String> list_ = new ArrayList<>();
        if(list != null && !list.isEmpty()){
            for(SingleResponse obj : list){
                Log.i("ss_nw_call", "looper adding "+obj.getResult());
                list_.add(obj.getResult());
            }
        }
        String jsonString = new Gson().toJson(list_);
        AppPreference.setStringPref(activity, AppPreference.EDUCATION_JSON, AppPreference.EDUCATION_JSON,jsonString);
    }

    public static List<String> getEducationList(Activity activity) {
        List<String> list = new ArrayList<>();
        String jsonString = AppPreference.getStringPref(activity, AppPreference.EDUCATION_JSON, AppPreference.EDUCATION_JSON);
        if (!jsonString.isEmpty()){
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, type);
        }
        return list;
    }

    public static void setCasteList(List<SingleResponse> list, Activity activity){
        List<String> list_ = new ArrayList<>();
        if(list != null && !list.isEmpty()){
            for(SingleResponse obj : list){
                Log.i("ss_nw_call", "looper adding "+obj.getResult());
                list_.add(obj.getResult());
            }
        }
        String jsonString = new Gson().toJson(list_);
        AppPreference.setStringPref(activity, AppPreference.CASTE_JSON, AppPreference.CASTE_JSON,jsonString);
    }

    public static List<String> getCasteList(Activity activity) {
        List<String> list = new ArrayList<>();
        String jsonString = AppPreference.getStringPref(activity, AppPreference.CASTE_JSON, AppPreference.CASTE_JSON);
        if (!jsonString.isEmpty()){
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, type);
        }
        return list;
    }

    public static void setCityList(List<SingleResponse> list, Activity activity){
        List<String> list_ = new ArrayList<>();
        if(list != null && !list.isEmpty()){
            for(SingleResponse obj : list){
                Log.i("ss_nw_call", "looper adding "+obj.getResult());
                list_.add(obj.getResult());
            }
        }
        String jsonString = new Gson().toJson(list_);
        AppPreference.setStringPref(activity, AppPreference.CITY_JSON, AppPreference.CITY_JSON,jsonString);
    }

    public static List<String> getCityList(Activity activity) {
        List<String> list = new ArrayList<>();
        String jsonString = AppPreference.getStringPref(activity, AppPreference.CITY_JSON, AppPreference.CITY_JSON);
        if (!jsonString.isEmpty()){
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, type);
        }
        return list;
    }

    public static void setOccupationList(List<SingleResponse> list, Activity activity){
        List<String> list_ = new ArrayList<>();
        if(list != null && !list.isEmpty()){
            for(SingleResponse obj : list){
                list_.add(obj.getResult());
            }
        }
        String jsonString = new Gson().toJson(list_);
        AppPreference.setStringPref(activity, AppPreference.OCCUPATION_JSON, AppPreference.OCCUPATION_JSON,jsonString);
    }

    public static List<String> getOccupationList(Activity activity) {
        List<String> list = new ArrayList<>();
        String jsonString = AppPreference.getStringPref(activity, AppPreference.OCCUPATION_JSON, AppPreference.OCCUPATION_JSON);
        if (!jsonString.isEmpty()){
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, type);
        }
        return list;
    }


    public static void setLastnamesList(List<SingleResponse> list, Activity activity){
        List<String> list_ = new ArrayList<>();
        if(list != null && !list.isEmpty()){
            for(SingleResponse obj : list){
                list_.add(obj.getResult());
            }
        }
        String jsonString = new Gson().toJson(list_);
        AppPreference.setStringPref(activity, AppPreference.LASTNAMES_JSON, AppPreference.LASTNAMES_JSON,jsonString);
    }

    public static List<String> getLastnamesList(Activity activity) {
        List<String> list = new ArrayList<>();
        String jsonString = AppPreference.getStringPref(activity, AppPreference.LASTNAMES_JSON, AppPreference.LASTNAMES_JSON);
        if (!jsonString.isEmpty()){
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, type);
        }
        return list;
    }


    public static void setCustomerActivity(CustomerActivityModal modal, Activity activity){
        if(modal != null){
            String jsonString = new Gson().toJson(modal);
            AppPreference.setStringPref(activity, AppPreference.CUSTOMER_ACTIVITY_JSON, AppPreference.CUSTOMER_ACTIVITY_JSON,jsonString);
        }
    }

    public static CustomerActivityModal getCustomerActivity(Activity activity) {
        CustomerActivityModal modal;
        String jsonString = AppPreference.getStringPref(activity, AppPreference.CUSTOMER_ACTIVITY_JSON, AppPreference.CUSTOMER_ACTIVITY_JSON);
        if (!jsonString.isEmpty())
            modal = new Gson().fromJson(jsonString, CustomerActivityModal.class);
        else
            modal = new CustomerActivityModal();
        return modal;
    }


    public static void setContactviewedMatchesList(Boolean clearList , Activity activity){
        List<Level_1_cardModal> finalList = new ArrayList<>();

        if(!clearList){
            List<Level_1_cardModal> templist = getLevel1List(activity);

            for(Level_1_cardModal obj : templist){
                if(obj.getIsViewed().equalsIgnoreCase("1"))
                    finalList.add(obj);
            }
        }

        String jsonString = new Gson().toJson(finalList);
        AppPreference.setStringPref(activity, AppPreference.MATCHES_CONTACTVIEWED_JSON, AppPreference.MATCHES_CONTACTVIEWED_JSON,jsonString);
    }

    public static List<Level_1_cardModal> getContactviewedMatchesList(Activity activity) {
        List<Level_1_cardModal> list = new ArrayList<>();
        String jsonString = AppPreference.getStringPref(activity, AppPreference.MATCHES_CONTACTVIEWED_JSON, AppPreference.MATCHES_CONTACTVIEWED_JSON);
        if (!jsonString.isEmpty()){
            Type type = new TypeToken<ArrayList<Level_1_cardModal>>() {}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, type);
        }
        return list;
    }

    public static void setLikedMatchesList(Boolean clearList , Activity activity){
        List<Level_1_cardModal> finalList = new ArrayList<>();
        if(!clearList){
            List<Level_1_cardModal> templist = getLevel1List(activity);
            for(Level_1_cardModal obj : templist){
                if(obj.getIsLiked().equalsIgnoreCase("1"))
                    finalList.add(obj);
            }
        }

        String jsonString = new Gson().toJson(finalList);
        AppPreference.setStringPref(activity, AppPreference.MATCHES_LIKED_JSON, AppPreference.MATCHES_LIKED_JSON,jsonString);
    }

    public static List<Level_1_cardModal> getLikedMatchesList(Activity activity) {
        List<Level_1_cardModal> list = new ArrayList<>();
        String jsonString = AppPreference.getStringPref(activity, AppPreference.MATCHES_LIKED_JSON, AppPreference.MATCHES_LIKED_JSON);
        if (!jsonString.isEmpty()){
            Type type = new TypeToken<ArrayList<Level_1_cardModal>>() {}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, type);
        }
        return list;
    }

    public static void setShortlistedMatchesList(Boolean clearList , Activity activity){
        List<Level_1_cardModal> finalList = new ArrayList<>();
        if(!clearList){
            List<Level_1_cardModal> templist = getLevel1List(activity);
            for(Level_1_cardModal obj : templist){
                if(obj.getIsShortlisted().equalsIgnoreCase("1"))
                    finalList.add(obj);
            }
        }

        String jsonString = new Gson().toJson(finalList);
        AppPreference.setStringPref(activity, AppPreference.MATCHES_SHORTLISTED_JSON, AppPreference.MATCHES_SHORTLISTED_JSON,jsonString);
    }

    public static List<Level_1_cardModal> getShortlistedMatchesList(Activity activity) {
        List<Level_1_cardModal> list = new ArrayList<>();
        String jsonString = AppPreference.getStringPref(activity, AppPreference.MATCHES_SHORTLISTED_JSON, AppPreference.MATCHES_SHORTLISTED_JSON);
        if (!jsonString.isEmpty()){
            Type type = new TypeToken<ArrayList<Level_1_cardModal>>() {}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, type);
        }
        return list;
    }

    public static void setSentinterestMatchesList(Boolean clearList ,Activity activity){
        List<Level_1_cardModal> finalList = new ArrayList<>();
        if(!clearList){
            List<Level_1_cardModal> templist = getLevel1List(activity);
            for(Level_1_cardModal obj : templist){
                if(obj.getIsInterestsent().equalsIgnoreCase("1"))
                    finalList.add(obj);
            }
        }

        String jsonString = new Gson().toJson(finalList);
        AppPreference.setStringPref(activity, AppPreference.MATCHES_SENTINTEREST_JSON, AppPreference.MATCHES_SENTINTEREST_JSON,jsonString);
    }

    public static List<Level_1_cardModal> getSentinterestMatchesList(Activity activity) {
        List<Level_1_cardModal> list = new ArrayList<>();
        String jsonString = AppPreference.getStringPref(activity, AppPreference.MATCHES_SENTINTEREST_JSON, AppPreference.MATCHES_SENTINTEREST_JSON);
        if (!jsonString.isEmpty()){
            Type type = new TypeToken<ArrayList<Level_1_cardModal>>() {}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, type);
        }
        return list;
    }

    public static void updateLevel1List(Level_1_cardModal updatedItem, Activity activity, boolean toBeRemoved) {
        List<Level_1_cardModal> cachedData = getLevel1List(activity);

        if (cachedData != null) {
            for (Level_1_cardModal item : cachedData) {
                if (item.getProfileId().equals(updatedItem.getProfileId())) {
                    if (toBeRemoved) {
                        cachedData.remove(item);
                    } else {
                        // Update the specific item in the cached data with the same object reference
                        cachedData.set(cachedData.indexOf(item), updatedItem);
                    }

                    setLevel1List(cachedData, activity); // Update the cached data
                    return; // Exit the loop early
                }
            }
        }
    }


}
