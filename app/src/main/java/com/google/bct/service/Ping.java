package com.google.bct.service;


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
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.crashlytics.android.Crashlytics;
import com.google.bct.R;
import com.google.bct.common.Constants;
import com.google.bct.init.AppBase;
import com.google.bct.ui.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.NotificationManager.IMPORTANCE_MAX;

public class Ping extends Service {
    private static final String LOG_TAG = "ForegroundService";
    public static boolean IS_SERVICE_RUNNING = false;
    private NotificationManager mNotificationManager;
    AppBase app;
    LocationManager locationManager;
    boolean startMock;
    BroadcastReceiver br;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public final OkHttpClient clients = new OkHttpClient();
    private boolean isStop=true;
    Handler handler;
    private HandlerThread handlerThread;
    SharedPreferences prefs;
    private static final String TAG = LocationService.class.getSimpleName();
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void permissionCheck() {
        //GPS 권한
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else {
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        permissionCheck();
        prefs = getSharedPreferences("bct", 0);
        app = AppBase.obtainApp(this);

        handler = new Handler(Looper.getMainLooper());
        handlerThread=new HandlerThread(getUUID(),-2);
        handlerThread.start();

        handler=new Handler(handlerThread.getLooper()){
            public void handleMessage(Message msg){
                try {
                    Thread.sleep(100);
                    if (!isStop){
                        sendEmptyMessage(0);
                        //broadcast to MainActivity
                        Intent intent=new Intent();
                        intent.setAction("com.google.bct.service.Ping");
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
            new LocationUpdateTask().start();
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    private String NOTIFICATION_CHANNEL_ID = "BCT bike";
    private String NOTIFICATION_CHANNEL_NAME = "BCT Bike";
    private String CHANNEL_DESCRIPTION = "description";
    @SuppressLint("NewApi")
    private void startMyOwnForeground()
    {
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
            PendingIntent pStopSelf = PendingIntent.getService(this, 0, stopSelf,PendingIntent.FLAG_CANCEL_CURRENT);
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
            PendingIntent pStopSelf = PendingIntent.getService(this, 0, stopSelf,0);

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)  {

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
    @Override
    public void onDestroy() {
        startMock = false;
        stopSelf();
        super.onDestroy();
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
    Double alt,latitude,longitude;
    Float acc;

    public static double randDoub(double min, double max) {
        Random rand = new Random();
        double result = rand.nextFloat() * (max - min) + min;
        return result;

    }
    private void a() {
        JSONObject object=new JSONObject();
        try {
            object.put("accuracy",randFloat(Float.valueOf(prefs.getInt("min",1)),Float.valueOf(prefs.getInt("maz",50))));
            object.put("altitude",alt);
            object.put("appVersion","96");
            object.put("driverStatus","Available");
            object.put("pingTimestamp", System.currentTimeMillis());
            object.put("locationTimestamp",System.currentTimeMillis());
            object.put("speed",randDoub(Double.valueOf(prefs.getInt("min",1)),Double.valueOf(prefs.getInt("max",30))));
            object.put("gcmKey",prefs.getString("fcmx",""));
            object.put("userId",prefs.getString("iddriverx",""));
            object.put("time",System.currentTimeMillis());
            double latitude2 = app.latitude;
            object.put("latitude",Double.valueOf((((app.latitude + 3.0E-5d) - latitude2) * new Random().nextDouble()) + latitude2).doubleValue());
            double longitude2 = app.longitude;
            object.put("longitude",Double.valueOf((((app.longitude + 3.0E-5d) - longitude2) * new Random().nextDouble()) + app.longitude));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String body = object.toString();
        RequestBody Body = RequestBody.create(JSON, body);
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(prefs.getString("mee", ""))
                .header("User-Agent", "okhttp/3.12.0")
                .header("X-AppVersion", prefs.getString("ia1","4.21.1"))
                .header("X-AppCode", prefs.getString("ia2","92"))
                .header("X-AppId", "com.gojek.driver.bike")
                .header("X-IMEI", prefs.getString("imei",""))
                .header("X-DeviceOS", "Android")
                .header("appSignature", prefs.getString("ia4",""))
                .header("D1", prefs.getString("ia3",""))
                .header("X-Location", app.latitude +","+ app.longitude)
                .header("X-Location-Accuracy", String.valueOf(randFloat(0.01f,50f)) )
                .header("X-HTTP-DRIVER-ID", prefs.getString("iddriverx",""))
                .header("authorization","Bearer "+ prefs.getString("tkbvx",""))
                .header("X-PhoneModel", Build.BRAND)
                .header("X-PhoneMake", Build.DEVICE )
                .header("X-UniqueId", Build.SERIAL )
                .header("X-DeviceOS", "Android")
                .put(Body)
                .build();
        clients.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string().toString();
                System.out.println("Ping   =" + myResponse);
            }
        });
    }
    public  void login(){
        // String email = "081213444";
        //  String email = "08121133444";
        String text;
        String email = prefs.getString("emails","");
        String password = prefs.getString("password","");
        // Getting the longitude of the i-th location
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("email", email)
                    .add("password", password)
                    .build();
            OkHttpClient client = new OkHttpClient();
            String d = "https://bacot-team.com/";
            String e = prefs.getString("link","use");
            String f = "/v1/login";
            Request.Builder builder = new Request.Builder();
            Request request = builder
                    .url(d+e+f)
                    .post(formBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String myResponse = response.body().string().toString();

                    try {
                        JSONObject jsonObj = new JSONObject(myResponse);
                        String a = jsonObj.getString("name");
                        String b = jsonObj.getString("apiKey");
                        String c = jsonObj.getString("expired");
                        System.out.println("SUKSES" +  a + b + c);
                        prefs.edit().putBoolean("login",true).putString("nama",a).putString("apikey",b).putString("expired",c).apply();
                    }
                    catch (JSONException e) {
                        System.out.println("test gagal" + myResponse);
                        // System.exit(0);
                    }
                }});
        } catch (Exception e) {
            e.printStackTrace();
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
                    a();
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }




}
