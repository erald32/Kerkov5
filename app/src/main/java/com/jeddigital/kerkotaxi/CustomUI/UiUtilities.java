package com.jeddigital.kerkotaxi.CustomUI;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.jeddigital.kerkotaxi.R;

/**
 * Created by Theodhori on 8/30/2016.
 */
public class UiUtilities {

    public static void showAndroidToolbarNotification(Context context, String notificationTittle, String notificationMessage, int notifikationID, Intent notificationIntent){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.x_icon)
                .setContentTitle(notificationTittle)
                .setContentText(notificationMessage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        mBuilder.setContentIntent(intent);
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(notifikationID, mBuilder.build());
    }
}
