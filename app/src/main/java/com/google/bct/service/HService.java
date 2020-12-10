package com.google.bct.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

public class HService extends Service {

    SharedPreferences pref,prefss;
    @Override
    public void onCreate() {
        pref = getSharedPreferences("bct", 0);
    }
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new UpdateTask().start();
        return super.onStartCommand(intent, flags, startId);
    }
    private class UpdateTask extends Thread {
        @Override
        public void run() {
            super.run();
            try {

                a();

                Thread.sleep(pref.getInt("max",5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void a () {
        getApplicationContext().sendBroadcast(new Intent("com.google.android.intent.action.GTALK_HEARTBEAT"));
        getApplicationContext().sendBroadcast(new Intent("com.google.android.intent.action.MCS_HEARTBEAT"));


    }

}
