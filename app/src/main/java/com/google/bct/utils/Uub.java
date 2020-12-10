package com.google.bct.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Uub extends BroadcastReceiver {
    XPref PREFS;

    @Override
    public void onReceive(Context context, Intent intent) {
        PREFS = new XPref(context);
        String action = intent.getAction();
        if (action.equals("bct.intent.action.UPDATE_APP_VERSION")) {
            Bundle extras = intent.getExtras();
            boolean hasExtras = extras != null;

            if (hasExtras) {
                PREFS.setGDVersion(extras.getInt("gobis2Version"));
            }
        } else if (action.equals("bct.intent.action.RECEIVE_ORDER")) {
            Bundle extras = intent.getExtras();
            boolean hasExtras = extras != null;
            if (hasExtras) {
                PREFS.setGDVersion(extras.getInt("gobis2Version"));
            }
        }
    }
}
