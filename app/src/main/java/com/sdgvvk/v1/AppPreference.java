package com.sdgvvk.v1;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {

    public static final String CUSTOMER_JSON = "customerJson";
    public static final String ACTIVE_ORDER_JSON = "ActiveOrderJson";
    public static final String LEVEL_1_LIST_JSON = "Level1ListJson";
    public static final String CONTACT_VIEWED_LIST_JSON = "ContactViewedListJson";
    public static final String MEMBERSHIP_LIST_JSON = "membershipListJson";
    public static final String GENDER_STAT_JSON = "genderStatJson";
    public static final String ADMINPHONE_JSON = "adminphoneJson";

    public static final String IS_LIVE_JSON = "isliveJson";

    public static final void setStringPref(Context context, String prefKey, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(prefKey, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static final String getStringPref(Context context, String prefName, String key) {
        SharedPreferences sp = null;
        if(context != null)
        sp = context.getSharedPreferences(prefName, 0);
        return sp.getString(key, "");
    }


}