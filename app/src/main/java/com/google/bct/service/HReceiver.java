package com.google.bct.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

class HReceiver extends BroadcastReceiver {
    private static final Intent GTALK_HEART_BEAT_INTENT = new Intent("com.google.android.intent.action.GTALK_HEARTBEAT");
    private static final Intent MCS_MCS_HEARTBEAT_INTENT = new Intent("com.google.android.intent.action.MCS_HEARTBEAT");

    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(GTALK_HEART_BEAT_INTENT);
        context.sendBroadcast(MCS_MCS_HEARTBEAT_INTENT);
        FixerUtils.scheduleHeartbeatRequest(context, false);
    }
}
