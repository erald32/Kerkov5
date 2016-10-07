package com.jeddigital.kerkotaxi.IOTools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Theodhori on 8/9/2016.
 */
public class StorageConfigurations {
    public static final String USER_LOGGED_IN_SHARED_PREFS_KEY = "LOGED_IN_USER_DATA";

    public static final String LAST_BOOKING_STATUS_ID_KNOWN = "LAST_BOOKING_STATUS_ID_KNOWN";


    public static final String SHARED_PREF_CLIENT = "client_prefs";

    public static final String BOOKING_ID_PREF = "BookingID";

    public static final String ClIENT_ID_PREF = "client_id";
    public static final String CLIENT_STATUS_ID_PREF = "1";
    public static final String CLIENT_LATITUDE_PREF = "latitude klientit qe vjen nga serveri";
    public static final String CLIENT_LONGITUDE_PREF = "longitude klientit qe vjen nga serveri";


    public static void setInPrefsClientId(Context c, String klientId){
        SharedPreferences userLoggedInPreferences = c.getSharedPreferences(StorageConfigurations.USER_LOGGED_IN_SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor userLoggedInPreferencesEditor  = userLoggedInPreferences.edit();


        userLoggedInPreferencesEditor.putString(ClIENT_ID_PREF, klientId);
        userLoggedInPreferencesEditor.commit();
    }

    public static String getInPrefsClientId(Context c){
        SharedPreferences userLoggedInPreferences = c.getSharedPreferences(StorageConfigurations.USER_LOGGED_IN_SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        return userLoggedInPreferences.getString(StorageConfigurations.ClIENT_ID_PREF, "-1");
    }
}
