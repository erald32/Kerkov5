package com.jeddigital.kerkotaxi.Receivers;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jeddigital.kerkotaxi.Services.OnStartUpService;

/**
 * Created by Theodhori on 8/26/2016.
 */


public class OnStartUpBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            Intent serviceIntent = new Intent(context, OnStartUpService.class);
            context.startService(serviceIntent);
        }
    }

}
