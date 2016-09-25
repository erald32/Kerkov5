package com.jeddigital.kerkotaxi.Receivers;

import com.google.gson.Gson;
import com.jeddigital.kerkotaxi.AndroidRestClientApi.RestCommunicationHelperMethods;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;

/**
 * Created by Theodhori on 8/26/2016.
 */


public class ConectivityChangedReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable()) {
            if(RestCommunicationHelperMethods.hasInternetAccess(context)){
                //TODO duhen derguar sfidatEPerfunduarPorTePaDerGuara ne server


            }
        }
    }
}