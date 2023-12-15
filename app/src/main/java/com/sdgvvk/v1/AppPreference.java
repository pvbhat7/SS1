package com.sdgvvk.v1;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {

    public static final String CUSTOMER_JSON = "customerJson";
    public static final String CUSTOMER_ACTIVITY_JSON = "customerActivityJson";

    public static final String ACTIVE_ORDER_JSON = "ActiveOrderJson";
    public static final String LEVEL_1_LIST_JSON = "Level1ListJson";
    public static final String CONTACT_VIEWED_LIST_JSON = "ContactViewedListJson";
    public static final String MEMBERSHIP_LIST_JSON = "membershipListJson";
    public static final String GENDER_STAT_JSON = "genderStatJson";
    public static final String ADMINPHONE_JSON = "adminphoneJson";

    public static final String IS_LIVE_JSON = "isliveJson"; // no

    public static final String EDUCATION_JSON = "educationJson";// no

    public static final String CITY_JSON = "cityJson";// no

    public static final String CASTE_JSON = "casteJson";// no

    public static final String OCCUPATION_JSON = "occupationJson";// no
    public static final String LASTNAMES_JSON = "lastnamesJson";// no


    public static final String MATCHES_CONTACTVIEWED_JSON = "matchesContactviewedJson";
    public static final String MATCHES_LIKED_JSON = "matchesLikedJson";
    public static final String MATCHES_SHORTLISTED_JSON = "matchesShortlistedJson";
    public static final String MATCHES_SENTINTEREST_JSON = "matchesSentinterestJson";



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
        return sp == null ? "" : sp.getString(key, "");
    }


}
