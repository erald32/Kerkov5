package com.jeddigital.kerkotaxi.Services;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class OnStartUpService extends Service{

    private static final String TAG = "OnStartUp Service Status";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(TAG, "Service started");
/*
            AlarmManager alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            Intent notifycationIntent = new Intent(getApplicationContext(), NotificateUserService.class);
            notifycationIntent.setAction("com.motionapps.autoshkollashqiptare.TEST_ZYRTAR_ALARM_ACTION");
            PendingIntent alarmIntent = PendingIntent.getService(getApplicationContext(), 0, notifycationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(),  AlarmManager.INTERVAL_DAY, alarmIntent);

*/
        this.stopSelf();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
    }


}
