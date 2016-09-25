package com.jeddigital.kerkotaxi.AndroidRestClientApi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Theodhori on 8/26/2016.
 */
public class RestCommunicationHelperMethods {
    public static String ApiBaseURL = "http://www.filmaturk.com/auto/androidRestApi/";
    static String LOG_TAG = "Internet Conection Info";

    public static boolean isNetworkAvailable(Context cntx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) cntx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean hasInternetAccess(Context context) {
        if (isNetworkAvailable(context)) {
            Runtime runtime = Runtime.getRuntime();
            try {

                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                int     exitValue = ipProcess.waitFor();
                return (exitValue == 0);

            } catch (IOException e)          { e.printStackTrace(); }
            catch (InterruptedException e) { e.printStackTrace(); }

            return false;
        } else {
            Log.d(LOG_TAG, "No network available!");
        }
        return false;
    }
}