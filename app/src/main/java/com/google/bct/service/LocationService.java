package com.google.bct.service;

import java.util.UUID;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Random;

import static android.app.NotificationManager.IMPORTANCE_MAX;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.crashlytics.android.Crashlytics;
import com.google.bct.R;
import com.google.bct.common.Constants;
import com.google.bct.init.AppBase;
import com.google.bct.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LocationService extends Service {

    private static final String LOG_TAG = "ForegroundService";
    public static boolean IS_SERVICE_RUNNING = false;
    private NotificationManager mNotificationManager;

    AppBase app;
    LocationManager locationManager;
    boolean startMock;


    BroadcastReceiver br;


    private boolean isStop = true;

    Handler handler;
    private HandlerThread handlerThread;


    SharedPreferences sharedPreferences;


    private static final String TAG = LocationService.class.getSimpleName();

    private FusedLocationProviderClient fusedLocationClient;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void permissionCheck() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else {
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        permissionCheck();
        sharedPreferences = getSharedPreferences("bct", 0);
        app = AppBase.obtainApp(this);
        FirebaseAuth.getInstance();

        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


            locationManager.addTestProvider(LocationManager.GPS_PROVIDER,
                    false, false, false,
                    false, true, false,
                    false, 0, 5);
            locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
            locationManager.setTestProviderStatus(LocationManager.GPS_PROVIDER, 2, null, System.currentTimeMillis());
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "IllegalArgumentException thrown in _register");
        }

        handler = new Handler(Looper.getMainLooper());
        handlerThread = new HandlerThread(getUUID(), -2);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper()) {
            public void handleMessage(Message msg) {
                try {
                    Thread.sleep(1000);
                    if (!isStop) {
                        sendEmptyMessage(0);
                        //broadcast to MainActivity
                        Intent intent = new Intent();
                        intent.setAction("com.google.bct.LocationService");
                        sendBroadcast(intent);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        };
        handler.sendEmptyMessage(0);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startMock = true;

        new LocationUpdateTask().start();

        try {
            if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
                Log.i(LOG_TAG, "Received Start Foreground Intent ");
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
                    startMyOwnForeground();
                else
                    showNotification();


            } else if (intent.getAction().equals(
                    Constants.ACTION.STOPFOREGROUND_ACTION)) {
                Log.i(LOG_TAG, "Received Stop Foreground Intent");
                stopForeground(true);
                stopSelf();
                Intent aaa = new Intent();
                aaa.putExtra("data", "putOff");
                aaa.setAction("com.gojek.driver");
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                getApplicationContext().sendBroadcast(intent);

            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
        return START_STICKY;


    }

    private String NOTIFICATION_CHANNEL_ID = "BCT bike";
    private String NOTIFICATION_CHANNEL_NAME = "BCT Bike";
    private String CHANNEL_DESCRIPTION = "description";

    @SuppressLint("NewApi")
    private void startMyOwnForeground() {
        try {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);
            String NOTIFICATION_CHANNEL_ID = "com.bct.bgt";
            String channelName = "BCT Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
            Intent stopSelf = new Intent(this, LocationService.class);
            stopSelf.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            PendingIntent pStopSelf = PendingIntent.getService(this, 0, stopSelf, PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.ic_pin_drop_black_24dp)
                    .setContentText(Double.toString(app.latitude) + "." + Double.toString(app.longitude))
                    .setPriority(NotificationManager.IMPORTANCE_MAX)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .addAction(R.mipmap.ic_launcher, "Stop", pStopSelf)

                    .build();
            startForeground(43242, notification);
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    private void showNotification() {
        try {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);


            Intent stopSelf = new Intent(this, LocationService.class);
            stopSelf.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            PendingIntent pStopSelf = PendingIntent.getService(this, 0, stopSelf, 0);

            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_pin_drop_black_24dp);

            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("BCT FAKE")
                    .setPriority(NotificationManager.IMPORTANCE_MAX)
                    .setTicker(Double.toString(app.latitude) + "." + Double.toString(app.longitude))
                    .setContentText(Double.toString(app.latitude) + "." + Double.toString(app.longitude))
                    .setSmallIcon(R.drawable.ic_pin_drop_black_24dp)
                    .setPriority(1)
                    .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .addAction(R.mipmap.ic_launcher, "Stop", pStopSelf)
                    .setOngoing(true)

                    .build();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, IMPORTANCE_MAX);
                channel.setDescription(Double.toString(app.latitude) + "." + Double.toString(app.longitude));
                mNotificationManager.createNotificationChannel(channel);
            }
            startForeground(432,
                    notification);
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    public static double randDoub(double min, double max) {

        Random rand = new Random();

        double result = rand.nextFloat() * (max - min) + min;

        return result;

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onDestroy() {
        super.onDestroy();
        startMock = false;
        try {
            fusedLocationClient.setMockMode(false);
            this.locationManager = ((LocationManager) getSystemService(LOCATION_SERVICE));
            this.locationManager.setTestProviderEnabled(LocationManager.NETWORK_PROVIDER, false);
            this.locationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
            this.locationManager.addTestProvider(LocationManager.NETWORK_PROVIDER, true, true, true, false, true, false, false, 1, 5);
            LocationRequest localLocationRequest = LocationRequest.create();
            localLocationRequest.setPriority(105);
            localLocationRequest.setInterval(59L);
            localLocationRequest.setFastestInterval(10L);
            localLocationRequest.setSmallestDisplacement(0.0F);
            handler = new Handler(Looper.getMainLooper());
            unregisterReceiver(br);
            if (sharedPreferences.getBoolean("fuse", false)) {
                fusedLocationClient.setMockMode(false);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    public static float randFloat(float min, float max) {

        Random rand = new Random();

        float result = rand.nextFloat() * (max - min) + min;

        return result;

    }

    public static Double randdouble(double min, double max) {

        Random rand = new Random();

        double result = rand.nextDouble() * (max - min) + min;

        return result;

    }

    @SuppressLint("MissingPermission")
    private void updateMockLocation() {

        double latitude2 = Double.parseDouble(sharedPreferences.getString("lat4", ""));
        double longitude2 = Double.parseDouble(sharedPreferences.getString("lng4", ""));
        final Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(randdouble((latitude2), (latitude2 + 0.0000033243)));
        location.setLongitude(randdouble((longitude2), (longitude2 + 0.0000034342)));
        location.setAccuracy(randFloat(Float.valueOf(sharedPreferences.getInt("min", 4)), Float.valueOf(sharedPreferences.getInt("maz", 50))));
        location.setTime(System.currentTimeMillis());
        location.setAltitude(randFloat(0.01f, 4f));
        location.setProvider(LocationManager.GPS_PROVIDER);
        location.setSpeed(randFloat(4, 10));

        if (Build.VERSION.SDK_INT >= 17) {
            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, location);

             if (sharedPreferences.getBoolean("fuse", false)) {
        fusedLocationClient.setMockMode(true);
        fusedLocationClient.setMockLocation(location);
        Log.d("fused", "fused Mock location ON");
             }

    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    private class LocationUpdateTask extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                while (startMock) {
                    if (sharedPreferences.getBoolean("random", false)) {
                        updateMockLocation();
                        Thread.sleep(100);
                    } else {
                        noGoyang();
                        Thread.sleep(100);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @SuppressLint("MissingPermission")
    private void noGoyang() {
        if (sharedPreferences.getBoolean("auto_random", false)) {

            double latitude2 = Double.parseDouble(sharedPreferences.getString("lat4", ""));
            double longitude2 = Double.parseDouble(sharedPreferences.getString("lng4", ""));
            final Location location = new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(Double.valueOf((((latitude2 + 3.0E-8d) - latitude2) * new Random().nextDouble()) + latitude2).doubleValue());
            location.setLongitude(Double.valueOf((((longitude2 + 3.0E-8d) - longitude2) * new Random().nextDouble()) + longitude2).doubleValue());
            location.setAccuracy((float) (1.0d + (new Random().nextDouble() * 35.0d)));
            location.setTime(System.currentTimeMillis());
            location.setAltitude(randFloat(0.01f, 4f));
            location.setProvider(LocationManager.GPS_PROVIDER);
            location.setSpeed((float) (0.0d + (new Random().nextDouble() * 1.0d)));

            if (Build.VERSION.SDK_INT >= 17) {
                location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
            }
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, location);
            if (sharedPreferences.getBoolean("fuse", false)) {
                fusedLocationClient.setMockMode(true);
                fusedLocationClient.setMockLocation(location);
                Log.d("fused", "fused Mock location ON");
            }
        }
        norandom();


    }

    @SuppressLint("MissingPermission")
    private void norandom() {

            double latitude2 = Double.parseDouble(sharedPreferences.getString("lat4", ""));
            double longitude2 = Double.parseDouble(sharedPreferences.getString("lng4", ""));
            final Location location = new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(latitude2);
            location.setLongitude(longitude2);
            location.setAccuracy((float) (1.0d + (new Random().nextDouble() * 35.0d)));
            location.setTime(System.currentTimeMillis());
            location.setAltitude(randFloat(0.01f, 4f));
            location.setProvider(LocationManager.GPS_PROVIDER);
            location.setSpeed((float) (0.0d + (new Random().nextDouble() * 1.0d)));
            if (Build.VERSION.SDK_INT >= 17) {
                location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
            }
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            fusedLocationClient.setMockMode(true);
            fusedLocationClient.setMockLocation(location);
            Log.d("fused", "fused Mock location ON");


    }
}
