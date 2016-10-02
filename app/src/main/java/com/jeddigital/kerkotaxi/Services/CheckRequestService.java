package com.jeddigital.kerkotaxi.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLngBounds;
import com.jeddigital.kerkotaxi.AndroidRestClientApi.AndroidRestClientApiMethods;
import com.jeddigital.kerkotaxi.AndroidRestClientApi.Configurations;
import com.jeddigital.kerkotaxi.AnroidRestModels.CheckRequestResponse;
import com.jeddigital.kerkotaxi.CustomUI.UiUtilities;
import com.jeddigital.kerkotaxi.IOTools.StorageConfigurations;
import com.jeddigital.kerkotaxi.MenuHyreseActivity;

import java.util.Calendar;

/**
 * Created by Theodhori on 8/26/2016.
 */
public class CheckRequestService extends Service{

    public static final String clientId = "1";
    private static final String TAG = "OnStartUp Service Status";
    Handler handler;
    AndroidRestClientApiMethods restClientApiMethods;

    private Runnable checkRequestInterval = new Runnable(){
        public void run(){
            if(!MenuHyreseActivity.activeActivity){
                restClientApiMethods.checkRequestStatus(clientId);

                handler.postDelayed(checkRequestInterval, Configurations.CHECK_REQUEST_STATUS_BACKGROUND_SERVICE_INTERVAL);
            }else{
                handler.removeCallbacks(this);
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "Service started");
        handler = new Handler();
        restClientApiMethods = new AndroidRestClientApiMethods(this);
        handler.post(checkRequestInterval);

        //this.stopSelf();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
    }

    public void handleCheckRequestAction(CheckRequestResponse requestResponse){
        int booking_status_id = requestResponse.getStatus_id();
        LatLngBounds.Builder routeBoundsBuilder = new LatLngBounds.Builder();
        Log.e("dev", "handleCheckRequestAction 'backgroundService' bookingStatusId = "+booking_status_id);

        Toast.makeText(CheckRequestService.this, "status "+booking_status_id, Toast.LENGTH_SHORT).show();

        if(booking_status_id == 1){//pending

        }else if(booking_status_id == 2){//duke mare klientin

        }else if(booking_status_id == 3){//duke pritur klientin
            Intent notificationIntent = new Intent(this, MenuHyreseActivity.class);
        //  notificationIntent.putExtra(NotificationIntentsForSfidaZone.i_ftuar_per_sfide, true);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            UiUtilities.showAndroidToolbarNotification(this, "Kerko Taxi", "TAKSIJA juaj ka mbëritur", 1, notificationIntent);
        }else if(booking_status_id == 4){//me klient

        }else if(booking_status_id == 5){//kompletuar

        }else if(booking_status_id == 6){//Refuzuar Nga Shoferi Ne Pending

        }else if(booking_status_id == 7){//Refuzuar Nga Klienti Ne Pending

        }else if(booking_status_id == 8){//Refuzuar Nga Shoferi Duke Marre Klientin

        }else if(booking_status_id == 9){//Refuzuar Nga Klienti Duke Marre Klientin

        }else if(booking_status_id == 10){//Refuzuar Nga Shoferi Duke Pritur Klientin

        }else if(booking_status_id == 11){//Refuzuar Nga Klienti Duke Pritur Klientin

        }else if(booking_status_id == 12){//Refuzuar Nga Shoferi Me Klient

        }else if(booking_status_id == 13){//Anulluar nga sistemi per mospranim nga shoferi

        }else{//no booking status id -> there is no booking for this client

            return;
        }

    }

}
