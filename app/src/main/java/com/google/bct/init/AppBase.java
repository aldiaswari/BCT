package com.google.bct.init;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.bct.BuildConfig;
import com.google.bct.R;
import com.google.bct.db.DbHelper;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.onesignal.OneSignal;
import android.os.Process;
import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import androidx.annotation.NonNull;
import io.fabric.sdk.android.Fabric;

public class AppBase extends Application {

    public DbHelper dbHelper;

    public double longitude;
    public double latitude;
    public LatLng loka;
    public String token;
    public static final String TAG = AppBase.class.getSimpleName();
    private HashMap<String, Object> firebaseDefaultMap;
    public static final String VERSION_CODE_KEY = "latest_app_version";
    private static AppBase mInstance;
    private float scale;
    public String a,b,c,d,e;
    public static String deviceImieNumber = "";
    private static final String APP_ID = "728DC395-ED26-4344-A580-0BBADFC0665C"; // US-1 Demo
    Context context;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    @Override
    public void onCreate() {
        super.onCreate();
  //      SendBird.init(APP_ID, this);

        Fabric.with(this, new Crashlytics());
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new NotificationOpenedHandler())
                .autoPromptLocation(true)
                .init();
        mInstance = this;
        this.scale = getResources().getDisplayMetrics().density;
        b = getString(R.string.link1);
        dbHelper = new DbHelper(this);
        initFirebaseRemoteConfig();
    }

    private void initFirebaseRemoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        // [END get_remote_config_instance]

        // Create a Remote Config Setting to enable developer mode, which you can use to increase
        // the number of fetches available per hour during development. See Best Practices in the
        // README for more information.
        // [START enable_dev_mode]
        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // set in-app defaults
        Map<String, Object> remoteConfigDefaults = new HashMap();
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false);
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, "1.0");
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL,
                "https://");

        firebaseRemoteConfig.setDefaults(remoteConfigDefaults);
        firebaseRemoteConfig.fetch(60) // fetch every minutes
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "remote config is fetched.");
                            firebaseRemoteConfig.activateFetched();
                        }
                    }
                });
    }

    public static synchronized AppBase getInstance() {
        return mInstance;
    }





    public int getPixelValue(int dps) {
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }

    private LinkedList<Activity> activities = new LinkedList<Activity>();

    /**
     * 当Activity 被create的时候放进来
     *
     * @param activity 被创建的Activity
     */
    public void addActivity(Activity activity) {
        activities.add(activity);

        echoAllActivity();
    }

    /**
     * 当某个Activity destroy的时候
     *
     * @param activity 被销毁的Activity
     */
    public void removeActivity(Activity activity) {
        activities.remove(activity);

        echoAllActivity();
    }


    /**
     * Test an activity is  whether in the activities stack.
     *
     * @param cls Activity class
     * @return true if contains this activity
     */
    public boolean testActivityInStack(Class cls) {
        for (Activity activity : activities) {
            if (activity != null && activity.getClass() == cls) {
                return true;
            }
        }
        return false;
    }

    public Activity getActivity(Class clz) {
        for (Activity activity : activities) {
            if (activity != null && activity.getClass() == clz) {
                return activity;
            }
        }
        return null;
    }




    /**
     * @deprecated 发布时一定要关闭
     */
    @Deprecated
    private void echoAllActivity() {
        for (Activity activity : activities) {

        }
    }


    /**
     * 退出应用
     */
    public void exit() {
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        Process.killProcess(Process.myPid());
    }

    /**
     * 获取到Application类
     * http://stackoverflow.com/questions/3826905/singletons-vs-application-context-in-android
     *
     * @param context 本应用的一个context
     * @return CurrentApp对象
     */
    public static AppBase obtainApp(Context context) {
        return (AppBase) context.getApplicationContext();
    }

}
