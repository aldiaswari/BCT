package com.google.bct.init;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class Brcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), 0);
        String stringExtra = intent.getStringExtra("data");
        if (stringExtra.contains("bike")) {
            String[] split = (stringExtra + "google.bct").split("google.bct");
            sharedPreferences.edit().putString("driverToken", split[1]).putString("driverProfile", split[2]).putString("gcmDriver", split[3]).apply();
            //sharedPreferences.edit().putString("driverToken", split[1]).putString("driverProfile", split[2]).putString("gcmDriver", split[3]).apply();
        }

    }
}

