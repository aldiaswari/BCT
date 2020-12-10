package com.google.bct.hook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AndroidAppHelper;
import android.app.Application;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.crossbowffs.remotepreferences.RemotePreferences;
import com.google.bct.BuildConfig;
import com.google.bct.common.Constants;

import com.google.bct.utils.ConfigUtils;
import com.google.bct.utils.FilterXpInputStream;
import com.google.bct.utils.XSharedPref;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.core.content.pm.PackageInfoCompat;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class bacotLu   implements IXposedHookLoadPackage, IXposedHookZygoteInit {
    private static final String ANDROID_APP_APPLICATION_PACKAGE_MANAGER_CLASS_NAME = "android.app.ApplicationPackageManager" ;
    String[] act, cmd1, key1, lib1, pkg1;
    private HashSet<String> commandSet;
    private Activity currentActivity;
    private Double jarakParam;
    final File aaa = new File("/data/user_de/0/com.google.bct/shared_prefs/bct.xml");
    final File bbb = new File("/data/user/0/com.google.bct/shared_prefs/bct.xml");
    final File ccc = new File("/data/data/com.google.bct/shared_prefs/bct.xml");
    private static String hookValue, getPayment;
    private static XSharedPreferences prefs;
    String[] jobArray = { "0", "GO-RIDE", "2", "GO-SHOP", "4", "GO-FOOD", "GO-MART", "7", "8", "9", "10", "GO-MED", "GO-MED", "GO-CAR", "GO-SEND", "15", "16", "17", "18", "GO-BLUE", "20", "21", "21", "22", "GO KILAT" };
    private Set<String> keywordSet;
    public XC_MethodHook opHook, finishOpHook;
    private Integer paymenttype,jobParam, food, ride ,jarakMin, jarakMax;
    private HashSet appSet, activity, libnameSet;
    private Context systemContext;
    private SharedPreferences pref;
    private Double point;
    private static boolean debug = false;
    private static final String X_BCT_TAG = "XBct";
    private static boolean flagKeepScreenOn, systemwideScreenOn, isTouch;
    public static final int TYPE_SYSTEM = 0;
    public static final int TYPE_APP = 1;
    private XSharedPref PREFS;
    private int i2;
    private String var1,
            var2, var3, var4, var5,
            var6, var7, var8, var9,
            var10, var11, var12, var13,
            var14, var15, var16, var17,
            var18, var19, var20, var21, var22, var23, var24, var25;
    private static String applicationLabel;
    private static String packageName;
    private String mSdcard;
    public Boolean ggg, fff;
    public Boolean hhh;
    public Boolean iii;
    public Boolean jjj;
    public Boolean kkk;
    public Boolean lll;

    private static List<String> alwaysOnPackages;
    private static long lastUpdate = 0L;

    private static HashMap<String, View.OnKeyListener> listeners;
    private static boolean isInitialized = false;
    private Context context;
    private Activity mActivity;
    public XC_ProcessNameMethodHook hideAllowMockSettingHook;
    public XC_ProcessNameMethodHook hideMockProviderHook;
    public XC_ProcessNameMethodHook hideMockGooglePlayServicesHook;
    class XC_ProcessNameMethodHook extends XC_MethodHook {

        String processName;
        String packageName;

        private XC_MethodHook init(String processName, String packageName){
            this.processName = processName;
            this.packageName = packageName;
            return this;
        }

        boolean isHidingEnabled() {
            return false;
        }
    }

    private static void logDebug(String msg) {
        if (BuildConfig.DEBUG) Log.d(X_BCT_TAG, msg);
    }

    public void initZygote(IXposedHookZygoteInit.StartupParam paramStartupParam)
            throws Throwable {
        File file;
        int sdk = Build.VERSION.SDK_INT;
        if (sdk == 23) {
            file = aaa;
        } else if (sdk < 23) {
            file = bbb;
        } else {
            file = ccc;
        }
        prefs = new XSharedPreferences(file);
        isInitialized = false;
        findAndHookMethod(Instrumentation.class, "newActivity", ClassLoader.class, String.class, Intent.class, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable {
                currentActivity = (Activity) paramAnonymousMethodHookParam.getResult();
                readPrefs();
            }
        });
        findAndHookMethod(Activity.class, "onResume", new XC_MethodHook() {
            protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable {
                if ((currentActivity != null) &&  (currentActivity.getPackageName().equals("com.gojek.partner"))) {
                    readPrefs();
          //          setApplicationLabel();
                    String message = "BCT " +"Connected"+"\n"+ applicationLabel + " Sync";
                    log(message);

                    currentActivity.getWindow().addFlags(FLAG_KEEP_SCREEN_ON);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= 23) {
            opHook = new XC_MethodHook() {
                @SuppressLint({"InlinedApi"})
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    Object localObject = paramAnonymousMethodHookParam.args[0];
                    if ((localObject.equals(58)) || (localObject.equals("android:mock_location"))) {
                        paramAnonymousMethodHookParam.setResult(0);
                    }
                }
            };
            finishOpHook = new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    Object localObject = paramAnonymousMethodHookParam.args[0];
                    if ((localObject.equals(58)) || (localObject.equals("android:mock_location"))) {
                        paramAnonymousMethodHookParam.setResult(null);
                    }
                }
            };

        }
       Common.b();

    }

    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam)
            throws Throwable {
        logDebug("Called handleLoadPackage");
        moremock(loadPackageParam);
        bb.b();
        if ("android".equals(loadPackageParam.packageName)) {
            findAndHookMethod(File.class, "exists", new XC_MethodHook() {
                /* access modifiers changed from: protected */
                public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                    File file = (File) methodHookParam.thisObject;
                    if (new File("/sys/fs/selinux/enforce").equals(file)) {
                        methodHookParam.setResult(true);
                    } else if (new File("/system/bin/su").equals(file) || new File("/system/xbin/su").equals(file)) {
                        methodHookParam.setResult(false);
                    }
                }
            });
        }
        findAndHookMethod(JSONObject.class, "getBoolean", String.class, new XC_MethodHook() {
            /* access modifiers changed from: protected */
            public void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                String str = (String) methodHookParam.args[0];
                if ("ctsProfileMatch".equals(str) || "basicIntegrity".equals(str) || "isValidSignature".equals(str)) {
                    methodHookParam.setResult(true);
                }
            }
        });
        Common.b();
        log("BCT - ===Safetynet Ok===");
        log("BCT is Connecting...");

            fak();
            aaa(loadPackageParam);
            //next(loadPackageParam);
            c(loadPackageParam);
            if (loadPackageParam.packageName.equals("com.kozhevin.rootchecks")) {
                new KO().handleLoadPackage(loadPackageParam);
            }
            if (loadPackageParam.packageName.equals("com.scottyab.rootbeer.sample")) {
                new SC().handleLoadPackage(loadPackageParam);
            }
            beer(loadPackageParam);
            new SIG().handleLoadPackage(loadPackageParam);
            gopartner(loadPackageParam);



    }

    private void beer(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod(XposedHelpers.findClass("com.kimchangyoun.rootbeerFresh.RootBeerNative", loadPackageParam.classLoader), "INotificationSideChannel$Stub", new Object[]{new p(this)});
    }


    private void gopartner(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (loadPackageParam.packageName.equals("com.gojek.driver.bike")){
            selinux(loadPackageParam);
            bridge(loadPackageParam);
            act = new String[]{"com.google.bct", "com.diwa.detector.mock", "com.sgc2.sby", "com.gojek.driver.car", "com.gojek.goboxdriver", "com.grabtaxi.driver2"};
            pkg1 = new String[]{"com.google.bct", "com.diwa.detector.mock", "com.sgc2.sby", "id.co.cimbniaga.mobile.android", "com.deuxvelva.satpolapp", "com.telkom.mwallet"};
            key1 = new String[]{"magisksu", "supersu", "magisk", "superuser", "Superuser", "noshufou", "xposed", "rootcloak", "chainfire", "titanium", "Titanium", "substrate", "greenify", "daemonsu", "root", "busybox", "titanium", ".tmpsu", "su", "rootcloak2"};
            cmd1 = new String[]{"su", "which", "busybox", "pm", "am", "sh", "ps", "magisk"};
            lib1 = new String[]{"tool-checker"};
            appSet = new HashSet(Arrays.asList(this.pkg1));
            keywordSet = new HashSet(Arrays.asList(this.key1));
            commandSet = new HashSet<>(Arrays.asList(this.cmd1));
            libnameSet = new HashSet<>(Arrays.asList(this.lib1));
            activity = new HashSet<>(Arrays.asList(this.act));
            prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "bct");
            prefs.makeWorldReadable();
            prefs.reload();
            XposedBridge.log("===BCT powered SIT ===");
            XposedBridge.log("GoPartner 104 Is Running.... !");
            XposedBridge.log("Bypass  GoPartner 104 success...");
            XposedBridge.log("Bypass ALL FRAUD Detector is success ....");
            XposedBridge.log("Location ping....");
            moremock(loadPackageParam);
            initFile(loadPackageParam);
            hideXposed(loadPackageParam);
            c(loadPackageParam);
            mocka(loadPackageParam);
            mockloc(loadPackageParam);
            initRuntime(loadPackageParam);
            hideXposed(loadPackageParam);
            bypassFa(loadPackageParam);
            writeModule(var2);
            write();
            mainGpart(loadPackageParam);
            fraudGo(loadPackageParam);
            responGopart(loadPackageParam);
            if (loadPackageParam.packageName.toLowerCase().equals("com.kozhevin.rootchecks")) {
                new KO().handleLoadPackage(loadPackageParam);
            }
            bidPart(loadPackageParam);
            pingGoogle(loadPackageParam);
            XposedHelpers.findAndHookMethod("android.location.Location", loadPackageParam.classLoader,
                    "isFromMockProvider", hideMockProviderHook.init(loadPackageParam.processName, loadPackageParam.packageName));
        }
    }

    private void mockloc(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("android.provider.Settings.Secure", lpparam.classLoader, "getString",
                ContentResolver.class, String.class,
                hideAllowMockSettingHook.init(lpparam.processName, lpparam.packageName));

        XposedHelpers.findAndHookMethod("android.provider.Settings.Secure", lpparam.classLoader, "getInt",
                ContentResolver.class, String.class,
                hideAllowMockSettingHook.init(lpparam.processName, lpparam.packageName));

        XposedHelpers.findAndHookMethod("android.provider.Settings.Secure", lpparam.classLoader, "getInt",
                ContentResolver.class, String.class, int.class,
                hideAllowMockSettingHook.init(lpparam.processName, lpparam.packageName));

        XposedHelpers.findAndHookMethod("android.provider.Settings.Secure", lpparam.classLoader, "getFloat",
                ContentResolver.class, String.class,
                hideAllowMockSettingHook.init(lpparam.processName, lpparam.packageName));

        XposedHelpers.findAndHookMethod("android.provider.Settings.Secure", lpparam.classLoader, "getFloat",
                ContentResolver.class, String.class, float.class,
                hideAllowMockSettingHook.init(lpparam.processName, lpparam.packageName));

        XposedHelpers.findAndHookMethod("android.provider.Settings.Secure", lpparam.classLoader, "getLong",
                ContentResolver.class, String.class,
                hideAllowMockSettingHook.init(lpparam.processName, lpparam.packageName));

        XposedHelpers.findAndHookMethod("android.provider.Settings.Secure", lpparam.classLoader, "getLong",
                ContentResolver.class, String.class, long.class,
                hideAllowMockSettingHook.init(lpparam.processName, lpparam.packageName));
    }

    private void fraudGo(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        Class findClass2 = XposedHelpers.findClass("dark.TrackerModule$INotificationSideChannel$Stub", loadPackageParam.classLoader);
        XposedHelpers.findAndHookMethod(findClass2, "cancelAll", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (param!= null) {
                    param.setResult(Boolean.FALSE);
                }

            }
        });

    }

    private void mainGpart(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        Class<?> asek = XposedHelpers.findClass("com.gojek.driver.home.MainActivity", loadPackageParam.classLoader);

        XposedHelpers.findAndHookMethod(asek, "Ɉ", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (param != null) {
                    param.setResult((Object) null);
                }
            }
        });
        XposedHelpers.findAndHookMethod(XposedHelpers.findClass("dark.TrackerModule$cancel", loadPackageParam.classLoader), "cancel", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (param != null) {
                    param.setResult((Object) null);
                }
            }
        });
        XposedHelpers.findAndHookMethod(XposedHelpers.findClass("dark.access$5400", loadPackageParam.classLoader), "cancel", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (param != null) {
                    param.setResult((Object) null);
                }
            }
        });

    }

    private void pingGoogle(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod(XposedHelpers.findClass("dark.OcrModule", loadPackageParam.classLoader), "cancel", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if (param != null) {
                    param.setResult(2L);
                }

            }
        });

    }

    private void bidPart(XC_LoadPackage.LoadPackageParam loadPackageParam) {

            final Class<?> clazz1 = XposedHelpers.findClass("com.gojek.driver.ulysses.booking.BookingFragment", loadPackageParam.classLoader);


            XposedHelpers.findAndHookMethod(clazz1, "ͻ", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    log("New Booking");
                    BctToast("Ada Orderan - [BCT] \n"+jarakParam+"\n"+getPayment+"\n"+jobArray[jobParam]+"\n POINT "+point);
                    log("Ada Orderan - BCT by Sit "+jarakParam+" - "+getPayment+" - "+jobArray[jobParam]+" POINT "+point);

                    prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "bct");
                    prefs.makeWorldReadable();
                    prefs.reload();

                    Method a = clazz1.getDeclaredMethod("IconCompatParcelizer");
                    a.setAccessible(true);
                    if(normal()) {
                        log("==Orderan==");
                        XposedHelpers.callMethod(a.invoke(param.thisObject), "callOnClick");

                    }

                }

            });



    }


    private void write() {
        prefs.reload();
        pref = new XSharedPreferences( "com.google.bct", "bct");
        jarakMin = pref.getInt("jarakawal", 0);
        jarakMax = pref.getInt("jarakakhir", 0);
        LogGet logGet =new LogGet();
        logGet.a("JARAK MIN : " + jarakMin + " KM");
        logGet.a("JARAK MAX : " + jarakMax + " KM");
        boolean aaaaa = pref.getBoolean("gosit", false);
        boolean A = pref.getBoolean("goride", false);
        boolean B = pref.getBoolean("gofood", false);
        logGet.a("Autobid : " + aaaaa);
        logGet.a("Autobid GR : " + A);
        logGet.a("Autobid GF : " + B);

    }

    private void responGopart(XC_LoadPackage.LoadPackageParam lpparam) {
        prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "bct");
            prefs.makeWorldReadable();
            prefs.reload();
            XposedBridge.hookAllConstructors(XposedHelpers.findClass("dark.OrderHistoryActivity_ViewBinding", lpparam.classLoader), new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    if (methodHookParam != null) {

                        //point
                        point = (Double) XposedHelpers.getObjectField(methodHookParam.thisObject, "INotificationSideChannel");
                        //distance
                        jarakParam = (Double) XposedHelpers.getObjectField(methodHookParam.thisObject, "notify");
                        //jobType
                        jobParam = (Integer) XposedHelpers.getObjectField(methodHookParam.thisObject, "asBinder");
                        //payment Type
                        paymenttype = (Integer) XposedHelpers.getObjectField(methodHookParam.thisObject, "getInterfaceDescriptor");
                        if (paymenttype == 4) {
                            getPayment = "GO-PAY";
                        } else if (paymenttype == 3) {
                            getPayment = "Corporate Pin";
                        } else {
                            getPayment = "Cash";
                        }
                    }

                }
            });
    }

    private void bridge(XC_LoadPackage.LoadPackageParam loadPackageParam) {
         }


    private void selinux(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("android.os.SystemProperties", loadPackageParam.classLoader, "get", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (((String) param.args[0]).equals("ro.build.selinux")) {
                    param.setResult("1");
                }

            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if (((String) param.args[0]).equals("ro.build.selinux")) {
                    param.setResult("1");
                }
            }
        });
    }


    private void godriv(XC_LoadPackage.LoadPackageParam loadPackageParam){
        if (loadPackageParam.packageName.equals("com.gojek.driver.bike") || loadPackageParam.packageName.equals("com.gojek.driver")) {
            bb.b();
            act = new String[]{"com.google.bct", "com.diwa.detector.mock", "com.sgc2.sby", "com.gojek.driver.car", "com.gojek.goboxdriver", "com.grabtaxi.driver2"};
            pkg1 = new String[]{"com.google.bct", "com.diwa.detector.mock", "com.sgc2.sby", "id.co.cimbniaga.mobile.android", "com.deuxvelva.satpolapp", "com.telkom.mwallet"};
            key1 = new String[]{"magisksu", "supersu", "magisk", "superuser", "Superuser", "noshufou", "xposed", "rootcloak", "chainfire", "titanium", "Titanium", "substrate", "greenify", "daemonsu", "root", "busybox", "titanium", ".tmpsu", "su", "rootcloak2"};
            cmd1 = new String[]{"su", "which", "busybox", "pm", "am", "sh", "ps", "magisk"};
            lib1 = new String[]{"tool-checker"};
            appSet = new HashSet(Arrays.asList(this.pkg1));
            keywordSet = new HashSet(Arrays.asList(this.key1));
            commandSet = new HashSet<>(Arrays.asList(this.cmd1));
            libnameSet = new HashSet<>(Arrays.asList(this.lib1));
            activity = new HashSet<>(Arrays.asList(this.act));

            prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "bct");
            prefs.makeWorldReadable();
            prefs.reload();
            moremock(loadPackageParam);
            initFile(loadPackageParam);
            initRuntime(loadPackageParam);
            hideXposed(loadPackageParam);
            bypassFa(loadPackageParam);
            prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "bct");
            prefs.makeWorldReadable();
            prefs.reload();
            this.systemContext = (Context) XposedHelpers.callMethod(XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", loadPackageParam.classLoader), "currentActivityThread"), "getSystemContext", new Object[0]);
            moremock(loadPackageParam);
            //        Common.b();
            prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "bct");
            prefs.makeWorldReadable();
            prefs.reload();
            mocka(loadPackageParam);
            MockLocation(loadPackageParam);


//                bb.b();
            respon(loadPackageParam);
            bid(loadPackageParam);
            prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "bct");
            prefs.makeWorldReadable();
            prefs.reload();

            LogGet logGet = new LogGet();
            pref = new RemotePreferences(systemContext, "com.google.bct", "bct");
            jarakMin = pref.getInt("jarakawal", 0);
            jarakMax = pref.getInt("jarakakhir", 0);

            logGet.a("JARAK MIN : " + jarakMin + " KM");
            logGet.a("JARAK MAX : " + jarakMax + " KM");
            boolean aaaaa = pref.getBoolean("gosit", false);
            boolean A = pref.getBoolean("goride", false);
            boolean B = pref.getBoolean("gofood", false);
            logGet.a("Autobid : " + aaaaa);
            logGet.a("Autobid GR : " + A);
            logGet.a("Autobid GF : " + B);
            writeModule(var2);
            XposedBridge.log("BCT Success Hook " + loadPackageParam.packageName);
            XposedBridge.log(loadPackageParam.packageName + "Ping Connected");
            XposedBridge.log("Ping GPS" + " connected...");

            Class<?> asek = XposedHelpers.findClass("com.gojek.driver.home.MainActivity", loadPackageParam.classLoader);

            XposedHelpers.findAndHookMethod(asek, "Ɉ", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    if (param != null) {
                        param.setResult((Object) null);
                    }
                }
            });

            Class<?> findClassa = XposedHelpers.findClass("dark.Im$if", loadPackageParam.classLoader);
            //          Common.b();
            XposedHelpers.findAndHookMethod(findClassa, "Ι", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    if (param != null) {
                        param.setResult(Boolean.FALSE);
                    }
                }
            });

            Class<?> findClassb = XposedHelpers.findClass("dark.Im$ɩ", loadPackageParam.classLoader);

            XposedHelpers.findAndHookMethod(findClassb, "ı", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    super.beforeHookedMethod(methodHookParam);
                    if (methodHookParam != null) {
                        methodHookParam.setResult(Boolean.FALSE);
                    }
                }
            });
            Class<?> findClassc = XposedHelpers.findClass("dark.aIK", loadPackageParam.classLoader);

            XposedHelpers.findAndHookMethod(findClassc, "ɩ", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    if (param != null) {
                        param.setResult(0L);

                    } else {
                        throw null;
                    }

                }
            });

            final Class<?> cl2 = XposedHelpers.findClass("com.gojek.driver.ulysses.booking.BookingFragment", loadPackageParam.classLoader);

            XposedHelpers.findAndHookMethod(cl2, "ͻ", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "bct");
                    prefs.makeWorldReadable();
                    prefs.reload();
                    BctToast("Ada Orderan - BCT version 423\n" + jarakParam + "\n" + getPayment + "\n" + jobArray[jobParam] + "\n POINT " + point);
                    XposedBridge.log("Orderan - BCT " + jarakParam + " - " + getPayment + " - " + jobArray[jobParam] + " POINT " + point);
                    bb.b();
                    Method a = cl2.getDeclaredMethod("ɨ");
                    a.setAccessible(true);
                    //             if (normal()) {
                    if (aaabi()) {
                        XposedBridge.log("===Orderan===");
                        XposedHelpers.callMethod(a.invoke(param.thisObject), "callOnClick");

                    }


                }

            });

        }
        }

    private void gojgrb(XC_LoadPackage.LoadPackageParam loadPackageParam){
        boolean equals2 = loadPackageParam.packageName.equals("com.gojek.driver.car");
        boolean equals3 = loadPackageParam.packageName.equals("com.gojek.driver.kilat");
        //       boolean equals4 = loadPackageParam.packageName.equals("com.goviet.driver.bike");
        boolean equals5 = loadPackageParam.packageName.equals("com.grabtaxi.driver2");
        if (equals2 || equals3 || equals5) {
            act = new String[]{"com.google.bct", "com.diwa.detector.mock", "com.sgc2.sby", "com.gojek.driver.car", "com.gojek.goboxdriver", "com.grabtaxi.driver2", "de.robv.android.xposed.installer", "org.meowcat.edxposed.manager"};
            pkg1 = new String[]{"com.google.bct", "com.diwa.detector.mock", "com.sgc2.sby", "id.co.cimbniaga.mobile.android", "com.deuxvelva.satpolapp", "com.telkom.mwallet"};
            key1 = new String[]{"magisksu", "supersu", "magisk", "superuser", "Superuser", "noshufou", "xposed", "rootcloak", "chainfire", "titanium", "Titanium", "substrate", "greenify", "daemonsu", "root", "busybox", "titanium", ".tmpsu", "su", "rootcloak2", "edxposed", "EdXposed Manager","Xposed Installer"};
            cmd1 = new String[]{"su", "which", "busybox", "pm", "am", "sh", "ps", "magisk"};
            lib1 = new String[]{"tool-checker"};
            appSet = new HashSet(Arrays.asList(this.pkg1));
            keywordSet = new HashSet(Arrays.asList(this.key1));
            commandSet = new HashSet<>(Arrays.asList(this.cmd1));
            libnameSet = new HashSet<>(Arrays.asList(this.lib1));
            activity = new HashSet<>(Arrays.asList(this.act));
            prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "bct");
            prefs.makeWorldReadable();
            prefs.reload();
            moremock(loadPackageParam);
            initFile(loadPackageParam);
            initRuntime(loadPackageParam);
            hideXposed(loadPackageParam);
            bypassFa(loadPackageParam);
            prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "bct");
            prefs.makeWorldReadable();
            prefs.reload();
            systemContext = (Context) XposedHelpers.callMethod(XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", loadPackageParam.classLoader), "currentActivityThread"), "getSystemContext", new Object[0]);
            moremock(loadPackageParam);
            Common.b();
            prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "bct");
            prefs.makeWorldReadable();
            prefs.reload();
            mocka(loadPackageParam);
            MockLocation(loadPackageParam);
        }
    }

    private void aaa(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!isInitialized) {
            logDebug("handleLoadPackage: Hooking methods");
            isInitialized = true;
            Class<?> clsPMS = XposedHelpers.findClass(ANDROID_APP_APPLICATION_PACKAGE_MANAGER_CLASS_NAME, lpparam.classLoader);

            XposedBridge.hookAllMethods(clsPMS, "getApplicationInfo", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    logDebug("getApplicationInfo");
                    modifyHookedMethodArguments(param);
                }
            });


            XposedBridge.hookAllMethods(clsPMS, "getPackageInfo", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    logDebug("getPackageInfo");
                    modifyHookedMethodArguments(param);
                }
            });

            XposedBridge.hookAllMethods(clsPMS, "getInstalledApplications", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    logDebug("getInstalledApplications");
                    modifyHookedMethodResult(param, new ApplicationInfoData());
                }
            });

            XposedBridge.hookAllMethods(clsPMS, "getInstalledPackages", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    logDebug("getInstalledPackages");
                    modifyHookedMethodResult(param, new PackageInfoData());
                }
            });

            XposedBridge.hookAllMethods(clsPMS, "getPackagesForUid", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    logDebug("getPackagesForUid");
                    modifyHookedMethodResult(param, new PackageNameStringData());
                }
            });

            XposedBridge.hookAllMethods(clsPMS, "queryIntentActivities", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    logDebug("queryIntentActivities");
                    modifyHookedMethodResult(param, new ResolveInfoData());
                }
            });

            XposedBridge.hookAllMethods(clsPMS, "queryIntentActivityOptions", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    logDebug("queryIntentActivityOptions");
                    modifyHookedMethodResult(param, new ResolveInfoData());

                }
            });

        }

        if (lpparam.packageName.equals(BuildConfig.APPLICATION_ID)) {
            XposedHelpers.findAndHookMethod(BuildConfig.APPLICATION_ID + ".MainActivity", lpparam.classLoader, "isXposedActive", XC_MethodReplacement.returnConstant(true));
        }
    }

    private Boolean normal()
    {
        pref = new RemotePreferences(systemContext, "com.google.bct", "bct");
        jarakMin = pref.getInt("jarakawal", 0);
        jarakMax =  pref.getInt("jarakakhir", 0);
        if ((jarakParam <= jarakMin)  && (pref.getBoolean("goride", true)) && (jobParam == 1)
                ||  (jarakParam <= jarakMax) && (pref.getBoolean("gofood", false)) && (this.jobParam == 5)) {

            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private boolean aO(){

        Common.b();
            pref = new RemotePreferences(systemContext, "com.google.bct", "bct");
            jarakMin = prefs.getInt("jarakawal", 0);
            jarakMax = prefs.getInt("jarakakhir", 25);
            fff = prefs.getBoolean("gosit", false);
            ggg= prefs.getBoolean("goride", false);
            hhh =prefs.getBoolean("gofood", false);
            iii =prefs.getBoolean("gosend", false);
            jjj =prefs.getBoolean("gomart", false);
            kkk =prefs.getBoolean("goshop", false);
            lll = prefs.getBoolean("gokilat", false);
        if ((jarakParam > jarakMax) || (jarakParam < jarakMin) || ((!prefs.getBoolean("gosit", false) || !ggg) || jobParam != 1) && ((!prefs.getBoolean("gosit", false) || !hhh) || jobParam != 5) && ((!prefs.getBoolean("gosit", false) || !iii) || jobParam != 14) && ((!prefs.getBoolean("gosit", false) || !jjj) || jobParam != 6) && ((!prefs.getBoolean("gosit", false) || !kkk) || jobParam != 3) && ((!prefs.getBoolean("gosit", false) || !lll) || jobParam != 23)) {
            return false;
        }

        return true;

    }

    private void respon(final XC_LoadPackage.LoadPackageParam lpparam) {
        prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "bct");
        prefs.makeWorldReadable();
        prefs.reload();

    //    Common.b();
        //getOrderDetailFrom
        // dark.zh
        XposedBridge.hookAllConstructors(XposedHelpers.findClass("dark.KW", lpparam.classLoader), new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                if (methodHookParam != null) {

                    //point
                    point = (Double) XposedHelpers.getObjectField(methodHookParam.thisObject, "Ӏ");
                    //distance
                    jarakParam = (Double) XposedHelpers.getObjectField(methodHookParam.thisObject, "ɩ");
                    //jobType
                    jobParam = (Integer) XposedHelpers.getObjectField(methodHookParam.thisObject, "Ɩ");
                    //payment Type
                    paymenttype = (Integer) XposedHelpers.getObjectField(methodHookParam.thisObject, "ɹ");
                    if (paymenttype == 4) {
                        getPayment = "GO-PAY";
                    } else if (paymenttype == 3) {
                        getPayment = "Corporate Pin";
                    } else {
                        getPayment = "Cash";
                    }
                }

            }
        });

        XposedBridge.hookAllConstructors(XposedHelpers.findClass("dark.xq", lpparam.classLoader), new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                if (methodHookParam != null) {

                    //distance
                    jarakParam = (Double) XposedHelpers.getObjectField(methodHookParam.thisObject, "ɪ");
                    //jobType
                    jobParam = (Integer) XposedHelpers.getObjectField(methodHookParam.thisObject, "ł");
                    //payment Type
                    paymenttype = (Integer) XposedHelpers.getObjectField(methodHookParam.thisObject, "ɿ");
                    point = (Double) XposedHelpers.getObjectField(methodHookParam.thisObject, "ƚ");

                    if (paymenttype == 4) {
                        getPayment = "GO-PAY";
                    } else if (paymenttype == 3) {
                        getPayment = "Corporate Pin";
                    } else {
                        getPayment = "Cash";
                    }
        //            Common.b();
                }

            }
        });

        XposedHelpers.findAndHookMethod(XposedHelpers.findClass("okhttp3.Response", lpparam.classLoader), "body", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {

                if (methodHookParam != null) {
                    Object objectField = XposedHelpers.getObjectField(methodHookParam.thisObject, "body");
                    if (objectField != null) {
                        String obj = objectField.toString();
                        if (obj.length() > 9 && obj.contains("orderId")) {
                            JSONObject jSONObject = new JSONObject(obj);
                            String optString = jSONObject.optString("bookingId", "");
                            if (optString.length() >= 0) {
                                JSONObject jSONObject2 = jSONObject.getJSONObject("details");
                                paymenttype = jSONObject2.getInt("paymentType");
                                jobParam = jSONObject2.getInt("jobType");
                                jarakParam = jSONObject2.getDouble("totalDistance");
                                prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "bct");
                                prefs.makeWorldReadable();
                                prefs.reload();



                            }
                        }
                    }
                }
            }

        });
    }

    public final Context gas() {
        Application currentApplication = AndroidAppHelper.currentApplication();
        if (currentApplication != null) {
            return currentApplication.getApplicationContext();
        }
        return null;
    }


    public void mocka(XC_LoadPackage.LoadPackageParam lpparam) {

        Class findClass = XposedHelpers.findClass("com.gojek.driver.common.MyLocation", lpparam.classLoader);
        XposedHelpers.findAndHookConstructor(findClass, Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Long.TYPE, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (param != null) {
                    param.args[7] = "false";
                }
            }
        });
        XposedHelpers.findAndHookConstructor(findClass, Location.class, Long.TYPE, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {

                if (methodHookParam != null) {
                    methodHookParam.args[2] = "false";
                }
            }
        });
        XposedHelpers.findAndHookMethod(findClass, "getMockLocationStatus", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {

                if (methodHookParam != null) {
                    methodHookParam.args[2] = "false";
                }
            }

        });
        Class findClass2 = XposedHelpers.findClass("android.provider.Settings.Global", lpparam.classLoader);

        XposedHelpers.findAndHookMethod(findClass2, "getInt", ContentResolver.class, String.class, Integer.TYPE, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                if (methodHookParam != null) {
                    if (methodHookParam.args[1].equals("install_non_market_apps")) {
                        methodHookParam.setResult(0);
                    }
                }

            }
        });




        if (Build.VERSION.SDK_INT >= 23)
        {
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "checkOp", String.class, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "checkOp", Integer.TYPE, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "checkOpNoThrow", String.class, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "noteOp", String.class, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "noteOp", Integer.TYPE, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "noteOpNoThrow", String.class, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "noteOpNoThrow", Integer.TYPE, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "noteProxyOp", String.class, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "noteProxyOp", Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "noteProxyOpNoThrow", String.class, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "noteProxyOpNoThrow", Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "startOp", String.class, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "startOp", Integer.TYPE, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "startOpNoThrow", String.class, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "startOpNoThrow", Integer.TYPE, Integer.TYPE, String.class, opHook);

        }
    }


    private void bypassFa(XC_LoadPackage.LoadPackageParam lpparam)
    {
        XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", lpparam.classLoader, "getInstalledPackages", Integer.TYPE, new a());
        findAndHookMethod("android.os.SystemProperties", lpparam.classLoader, "get", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                if (param.args[0].equals("ro.build.selinux")) {
                    param.setResult("0");

                }

            }

            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                if (param.args[0].equals("ro.build.selinux")) {
                    param.setResult("0");


                }
            }

        });

    }


    private void hideXposed(XC_LoadPackage.LoadPackageParam lpparam) {
        findAndHookMethod("java.lang.Class", lpparam.classLoader, "forName", String.class, Boolean.TYPE, ClassLoader.class, new XC_MethodHook() {
            protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable {
                String str = (String) paramAnonymousMethodHookParam.args[0];
                if ((str != null) && ((str.equals("de.robv.android.xposed.XposedBridge")) || (str.equals("de.robv.android.xposed.XC_MethodReplacement")))) {
                    paramAnonymousMethodHookParam.setThrowable(new ClassNotFoundException());

                }
            }
        });
        Class clazz2 = XposedHelpers.findClass("java.io.File", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(clazz2, "exists", new kon());
        String str = Build.TAGS;
        if (!str.equals("release-keys")) {
            XposedHelpers.setStaticObjectField(android.os.Build.class, "TAGS", "release-keys");

        }

        XposedHelpers.findAndHookMethod("android.os.Debug", lpparam.classLoader, "isDebuggerConnected", new Object[]{XC_MethodReplacement.returnConstant(Boolean.FALSE)});
        XposedHelpers.findAndHookMethod("android.os.SystemProperties", lpparam.classLoader, "get", String.class, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                if (methodHookParam.args[0].equals("ro.build.selinux")) {
                    methodHookParam.setResult("1");
                }
            }
        });

    }

    public static final class kon extends XC_MethodHook {
        public void afterHookedMethod(XC_MethodHook.MethodHookParam param1MethodHookParam) {
            List<String> list = Arrays.asList("/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su", "/system/app/Superuser.apk");
            if (param1MethodHookParam != null) {
                String str = XposedHelpers.getObjectField(param1MethodHookParam.thisObject, "path").toString();
                if (list.contains(str)) {
                    param1MethodHookParam.setResult(Boolean.FALSE);
                }
                if (str.equals("/etc/security/otacerts.zip")) {
                    param1MethodHookParam.setResult(Boolean.TRUE);
                }
            }
        }
    }

    
        private void moremock(XC_LoadPackage.LoadPackageParam lpparam)
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "checkOp", String.class, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "checkOp", Integer.TYPE, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "checkOpNoThrow", String.class, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "noteOp", String.class, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "noteOp", Integer.TYPE, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "noteOpNoThrow", String.class, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "noteOpNoThrow", Integer.TYPE, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "noteProxyOp", String.class, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "noteProxyOp", Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "noteProxyOpNoThrow", String.class, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "noteProxyOpNoThrow", Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "startOp", String.class, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "startOp", Integer.TYPE, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "startOpNoThrow", String.class, Integer.TYPE, String.class, opHook);
            findAndHookMethod("android.app.AppOpsManager", lpparam.classLoader, "startOpNoThrow", Integer.TYPE, Integer.TYPE, String.class, opHook);

        }
    }

    private void initRuntime(final XC_LoadPackage.LoadPackageParam lpparam) {
        /**
         * Hooks exec() within java.lang.Runtime.
         * is the only version that needs to be hooked, since all of the others are "convenience" variations.
         * takes the form: exec(String[] cmdarray, String[] envp, File dir).
         * There are a lot of different ways that exec can be used to check for a rooted device. See the comments within section for more details.
         */
        findAndHookMethod("java.lang.Runtime", lpparam.classLoader, "exec", String[].class, String[].class, File.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {


                String[] execArray = (String[]) param.args[0]; // Grab the tokenized array of commands
                if ((execArray != null) && (execArray.length >= 1)) { // Do some checking so we don't break anything
                    String firstParam = execArray[0]; // firstParam is going to be the main command/program being run

                    String tempString = "Exec Command:";
                    for (String temp : execArray) {
                        tempString = tempString + " " + temp;
                    }


                    if (stringEndsWithFromSet(firstParam, commandSet)) { // Check if the firstParam is one of the keywords we want to filter


                        // A bunch of logic follows since the solution depends on which command is being called
                        // TODO: ***Clean up logic***s
                        if (firstParam.equals("su") || firstParam.endsWith("/su")) { // If its su or ends with su (/bin/su, /xbin/su, etc)
                            param.setThrowable(new IOException()); // Throw an exception to imply the command was not found
                        } else if (commandSet.contains("pm") && (firstParam.equals("pm") || firstParam.endsWith("/pm"))) {
                            // Trying to run the pm (package manager) using exec. Now let's deal with the subcases
                            if (execArray.length >= 3 && execArray[1].equalsIgnoreCase("list") && execArray[2].equalsIgnoreCase("packages")) {
                                // Trying to list out all of the packages, so we will filter out anything that matches the keywords
                                //param.args[0] = new String[] {"pm", "list", "packages", "-v", "grep", "-v", "\"su\""};
                                param.args[0] = buildGrepArraySingle(execArray, true);
                            } else if (execArray.length >= 3 && (execArray[1].equalsIgnoreCase("dump") || execArray[1].equalsIgnoreCase("path"))) {
                                // Trying to either dump package info or list the path to the APK (both will tell the app that the package exists)
                                // If it matches anything in the keywordSet, stop it from working by using a fake package name
                                if (stringContainsFromSet(execArray[2], keywordSet)) {
                                    param.args[0] = new String[]{execArray[0], execArray[1], ""};
                                }
                            }
                        } else if (commandSet.contains("ps") && (firstParam.equals("ps") || firstParam.endsWith("/ps"))) { // is a process list command
                            // Trying to run the ps command to see running processes (e.g. looking for things running as su or daemonsu). Filter out.
                            param.args[0] = buildGrepArraySingle(execArray, true);
                        } else if (commandSet.contains("which") && (firstParam.equals("which") || firstParam.endsWith("/which"))) {
                            // Busybox "which" command. Thrown an excepton
                            param.setThrowable(new IOException());
                        } else if (commandSet.contains("busybox") && anyWordEndingWithKeyword("busybox", execArray)) {
                            param.setThrowable(new IOException());
                        } else if (commandSet.contains("sh") && (firstParam.equals("sh") || firstParam.endsWith("/sh"))) {
                            param.setThrowable(new IOException());
                        } else {
                            param.setThrowable(new IOException());
                        }

                        if (param.getThrowable() == null) { // Print out the new command if debugging is on
                            tempString = "New Exec Command:";
                            for (String temp : (String[]) param.args[0]) {
                                tempString = tempString + " " + temp;
                            }
                        }
                    }


                }
            }
        });
        findAndHookMethod("java.lang.Runtime", lpparam.classLoader, "loadLibrary", String.class, ClassLoader.class, new XC_MethodHook() {
            protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable {
                String str = (String) paramAnonymousMethodHookParam.args[0];
                if ((str != null) && (stringContainsFromSet(str, libnameSet))) {
                    paramAnonymousMethodHookParam.setResult(null);
                }
            }
        });
    }

    public boolean stringContainsFromSet(String base, Set<String> values) {
        if (base != null && values != null) {
            for (String tempString : values) {
                if (base.matches(".*(\\W|^)" + tempString + "(\\W|$).*")) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean stringEndsWithFromSet(String base, Set<String> values) {
        if (base != null && values != null) {
            for (String tempString : values) {
                if (base.endsWith(tempString)) {
                    return true;
                }
            }
        }

        return false;
    }
    private Boolean anyWordEndingWithKeyword(String paramString, String[] paramArrayOfString)
    {
        int j = paramArrayOfString.length;
        int i = 0;
        while (i < j)
        {
            if (paramArrayOfString[i].endsWith(paramString)) {
                return Boolean.TRUE;
            }
            i += 1;
        }
        return Boolean.FALSE;
    }
    private String[] buildGrepArraySingle(String[] original, boolean addSH) {
        StringBuilder builder = new StringBuilder();
        ArrayList<String> originalList = new ArrayList<String>();
        if (addSH) {
            originalList.add("sh");
            originalList.add("-c");
        }
        for (String temp : original) {
            builder.append(" ");
            builder.append(temp);
        }
        //originalList.addAll(Arrays.asList(original));
        // ***TODO: Switch to using -e with alternation***
        for (String temp : keywordSet) {
            builder.append(" | grep -v ");
            builder.append(temp);
        }
        //originalList.addAll(Common.DEFAULT_GREP_ENTRIES);
        originalList.add(builder.toString());
        return originalList.toArray(new String[0]);
    }

    private void MockLocation(XC_LoadPackage.LoadPackageParam lpparam)
    {
        Class clazz1 = XposedHelpers.findClass("android.provider.Settings.Secure", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(clazz1, "getInt", ContentResolver.class, String.class, int.class, new b());


        findAndHookMethod("android.provider.Settings.Secure", lpparam.classLoader, "getString", ContentResolver.class, String.class, new XC_MethodHook()
        {
            protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable
            {
                if (paramAnonymousMethodHookParam.args[1].equals("mock_location")) {
                    paramAnonymousMethodHookParam.setResult("0");
                }
            }
        });
        findAndHookMethod("android.provider.Settings.Secure", lpparam.classLoader, "getInt", ContentResolver.class, String.class, new XC_MethodHook()
        {
            protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable
            {
                if (paramAnonymousMethodHookParam.args[1].equals("mock_location")) {
                    paramAnonymousMethodHookParam.setResult("0");
                }
            }
        });
        findAndHookMethod("android.provider.Settings.Secure", lpparam.classLoader, "getInt", ContentResolver.class, String.class, Integer.TYPE, new XC_MethodHook()
        {
            protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable
            {
                if (paramAnonymousMethodHookParam.args[1].equals("mock_location")) {
                    paramAnonymousMethodHookParam.setResult("0");
                }
            }
        });
        findAndHookMethod("android.provider.Settings.Secure", lpparam.classLoader, "getFloat", ContentResolver.class, String.class, new XC_MethodHook()
        {
            protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable
            {
                if (paramAnonymousMethodHookParam.args[1].equals("mock_location")) {
                    paramAnonymousMethodHookParam.setResult("0.0f");
                }
            }
        });
        findAndHookMethod("android.provider.Settings.Secure", lpparam.classLoader, "getFloat", ContentResolver.class, String.class, Float.TYPE, new XC_MethodHook()
        {
            protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable
            {
                if (paramAnonymousMethodHookParam.args[1].equals("mock_location")) {
                    paramAnonymousMethodHookParam.setResult("0.0f");
                }
            }
        });
        findAndHookMethod("android.provider.Settings.Secure", lpparam.classLoader, "getLong", ContentResolver.class, String.class, new XC_MethodHook()
        {
            protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable
            {
                if (paramAnonymousMethodHookParam.args[1].equals("mock_location")) {
                    paramAnonymousMethodHookParam.setResult("0.0f");
                }
            }
        });
        findAndHookMethod("android.provider.Settings.Secure", lpparam.classLoader, "getLong", ContentResolver.class, String.class, Long.TYPE, new XC_MethodHook()
        {
            protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable
            {
                if (paramAnonymousMethodHookParam.args[1].equals("mock_location")) {
                    paramAnonymousMethodHookParam.setResult("0L");
                }
            }
        });
        findAndHookMethod("android.location.Location", lpparam.classLoader, "getExtras", new XC_MethodHook()
        {
            protected void afterHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
            {
                Bundle localBundle = (Bundle)paramAnonymousMethodHookParam.getResult();
                if ((localBundle != null) && (localBundle.getBoolean("mockLocation"))) {
                    localBundle.putBoolean("mockLocation", false);
                }
                paramAnonymousMethodHookParam.setResult(Boolean.FALSE);
            }
        });

        findAndHookMethod("android.location.Location", lpparam.classLoader, "isFromMockProvider", new XC_MethodHook()
        {
            protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
            {
                paramAnonymousMethodHookParam.setResult(Boolean.FALSE);
            }
        });

    }

    private void initFile(XC_LoadPackage.LoadPackageParam lpparam)
    {
        XposedBridge.hookMethod(XposedHelpers.findConstructorExact(File.class, String.class), new XC_MethodHook(10000)
        {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable
            {
                if (((String)paramAnonymousMethodHookParam.args[0]).endsWith("su"))
                {
                    paramAnonymousMethodHookParam.args[0] = "";
                    return;
                }
                if (((String)paramAnonymousMethodHookParam.args[0]).endsWith("busybox"))
                {
                    paramAnonymousMethodHookParam.args[0] = "";
                    return;
                }
                if (stringContainsFromSet((String)paramAnonymousMethodHookParam.args[0], keywordSet)) {
                    paramAnonymousMethodHookParam.args[0] = "";
                }
            }
        });
    }
    private void readPrefs() {
        prefs = new XSharedPreferences("com.google.bct", "bct");
        prefs.makeWorldReadable();
        prefs.reload();
        List<String> packages = new LinkedList<String>();
        packages.add("com.google.bct");
        packages.add("com.gojek.driver.bike");
        packages.add("com.gojek.partner");

    }


    public static String ready(String message) {
        byte[] data = Base64.decode(message, Base64.DEFAULT);
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    void bid(XC_LoadPackage.LoadPackageParam lpparam){
        final Class<?> clazz1 = XposedHelpers.findClass("com.gojek.driver.ulysses.booking.BookingFragment", lpparam.classLoader);


        XposedHelpers.findAndHookMethod(clazz1, "ͻ", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                log("Order OM");
                BctToast("Ada Orderan - [BCT] \n"+jarakParam+"\n"+getPayment+"\n"+jobArray[jobParam]+"\n POINT "+point);
                log("Ada Orderan - BCT by Sit "+jarakParam+" - "+getPayment+" - "+jobArray[jobParam]+" POINT "+point);

                prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "bct");
                 prefs.makeWorldReadable();
                prefs.reload();

                Method a = clazz1.getDeclaredMethod("ɨ");
                a.setAccessible(true);
              //  if(normal()) {
                if (aaabi()) {
                    log("Orderan OM");
                    XposedHelpers.callMethod(a.invoke(param.thisObject), "callOnClick");

                }

            }

        });
    }

    private void BctToast(String s) {
        if (currentActivity != null) {
            Toast toast = Toast.makeText(currentActivity, s, Toast.LENGTH_SHORT);
            TextView localTextView = toast.getView().findViewById(android.R.id.message);
            if (localTextView != null) {
                localTextView.setGravity(17);
            }
            toast.show();
        }
    }


    public boolean isFlagKeepScreenOn() {
        int flags, flag;
        flags = currentActivity.getWindow().getAttributes().flags;
        flag = flags & WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        if (flag == 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean setFlagKeepScreenOn(boolean keepScreenOn, int type) {
        if (type == TYPE_SYSTEM) {
            systemwideScreenOn = flagKeepScreenOn = keepScreenOn;
            if (keepScreenOn) {
                currentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                currentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        } else if (type == TYPE_APP) {
            flagKeepScreenOn = keepScreenOn;
            if (keepScreenOn) {
                currentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                currentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
            if (flagKeepScreenOn) {
                BctToast("[BACOT!]\n" + applicationLabel + " will stay awake");
            } else {
                BctToast("[BACOT!]\n" + applicationLabel + " will use default screen timeout");
            }
        } else {
            // should not happen
        }
        return isFlagKeepScreenOn();
    }

    private void setApplicationLabel() {
        PackageManager pm;
        ApplicationInfo ai;
        packageName = currentActivity.getPackageName();
        try {
            pm = currentActivity.getPackageManager();
            ai = pm.getApplicationInfo(currentActivity.getPackageName(), 0);
            applicationLabel = (String) (ai != null ? pm.getApplicationLabel(ai) : packageName);
        } catch (PackageManager.NameNotFoundException | NullPointerException e) {
            log(e);
            applicationLabel = packageName;
        }
        if (applicationLabel == null) {
            applicationLabel = "App";
        }
    }

    private void setSystemwide(boolean systemwide) {
        //not used for now
        //Intent toggle_system = new Intent("stayawake.intent.action.TOGGLE_SYSTEM");
        //toggle_system.putExtra("systemwide", systemwide);
        //currentActivity.sendBroadcast(toggle_system);
    }

    /**
     * Tell if screen is curerntly on
     *
     * @return true if screen is on, false if off
     */
    private boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) currentActivity.getSystemService(Activity.POWER_SERVICE);
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH && powerManager.isInteractive() || Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH && powerManager.isScreenOn();
    }

    /**
     * Logs given message if debug flag is set
     *
     * @param message message to log
     * @see #debug
     * @see XposedBridge#log(String)
     */
    private void log(String message) {
        if (debug) {
            XposedBridge.log(message);
        }
    }

    /**
     * Logs throwable
     *
     * @param t throwable to log
     * @see XposedBridge#log(Throwable)
     */
    private void log(Throwable t) {
        XposedBridge.log(t);
    }

    private String getCallingName(Object thiz) {
        int uid = Binder.getCallingUid();

        return (String) XposedHelpers.callMethod(thiz, "getNameForUid", uid);
    }

    private boolean shouldBlock(Object thiz, String callingName, String queryName) {
        prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "bct");
        prefs.makeWorldReadable();
        prefs.reload();
        String key = callingName + ":" + queryName;
        String key_hide_from_system = queryName + Constants.KEY_HIDE_FROM_SYSTEM;
        logDebug("can Read: "+prefs.getFile().canRead());
        if (prefs.getBoolean(key, false)) {

            logDebug(key + " true");
            return true;
        }
        if (prefs.getBoolean(key_hide_from_system, false)) {

            // block system processes like android.uid.systemui:10015
            if (callingName.contains(":")) {
                logDebug(key + " true");
                return true;
            }

            // public ApplicationInfo getApplicationInfo(String packageName, int flags, int userId)
            // need to bypass enforceCrossUserPermission
            ApplicationInfo info = (ApplicationInfo) XposedHelpers.callMethod(thiz, "ApplicationInfo", callingName,
                    0, Binder.getCallingUid());
            if ((info.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                logDebug(key + " true");
                return true;
            }
        }
        logDebug(key + " false");
        return false;
    }


    private void modifyHookedMethodArguments(XC_MethodHook.MethodHookParam param) {
        prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "bct");
        prefs.makeWorldReadable();
        prefs.reload();
        if (shouldBlock(param.thisObject, getCallingName(param.thisObject), (String) param.args[0])) {
            param.args[0] = "";
        }
    }


    private <T> void modifyHookedMethodResult(XC_MethodHook.MethodHookParam param, InfoData<T> infoData) throws Throwable {

        List<T> mList = infoData.resultToList(param.getResultOrThrowable());
        if (mList == null) {
            return;
        }
        List<T> result = new ArrayList<>();
        prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "bct");
        prefs.makeWorldReadable();
        prefs.reload();
        for (T info : mList) {

            if (shouldBlock(param.thisObject, getCallingName(param.thisObject), infoData.getPackageName(info))) {
                continue;
            }
            result.add(info);
        }

        param.setResult(infoData.getResultObject(result));

    }

    private abstract class InfoData<Type> {
        abstract String getPackageName(Type info);

        public Object getResultObject(List<Type> modifiedResult) {
            return modifiedResult;
        }

        public List<Type> resultToList(Object result) {
            return (List<Type>) result;
        }
    }

    private class ResolveInfoData extends InfoData<ResolveInfo> {
        public ResolveInfoData() {
        }

        @Override
        public String getPackageName(ResolveInfo info) {
            return info.activityInfo.packageName;
        }

    }

    private class PackageInfoData extends InfoData<PackageInfo> {
        public PackageInfoData() {
        }

        @Override
        public String getPackageName(PackageInfo info) {
            return info.packageName;
        }
    }

    private class ApplicationInfoData extends InfoData<ApplicationInfo> {
        public ApplicationInfoData() {
        }

        @Override
        public String getPackageName(ApplicationInfo info) {
            return info.packageName;
        }
    }

    private class PackageNameStringData extends InfoData<String> {
        public PackageNameStringData() {
        }

        @Override
        public String getPackageName(String info) {
            return info;
        }

        @Override
        public String[] getResultObject(List<String> result) {
            return result.toArray(new String[result.size()]);
        }

        @Override
        public List<String> resultToList(Object result) {
            return result == null ? null : Arrays.asList((String[]) result);
        }

    }

    private void fak() {
        XposedHelpers.findAndHookMethod("java.security.MessageDigest", null, "isEqual", byte[].class, byte[].class, new XC_MethodHook() {
            @Override
            public void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(Boolean.TRUE);
            }
        });
        XposedHelpers.findAndHookMethod("java.security.Signature", null, "verify", byte[].class, new XC_MethodHook() {
            @Override
            public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult(Boolean.TRUE);
            }
        });
        Class cls = Integer.TYPE;
        XposedHelpers.findAndHookMethod("java.security.Signature", null, "verify", byte[].class, cls, cls, new XC_MethodHook() {
            @Override
            public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult(Boolean.TRUE);
            }
        });
        XposedBridge.log("BCT -- Signature off");
    }


    private void next(XC_LoadPackage.LoadPackageParam lpparam) {
        if ((lpparam.classLoader == null) || !ConfigUtils.get().contains(lpparam.packageName)) {
            return;
        }

        XposedBridge.log("[I/XposedHider] Handle package " + lpparam.packageName);

        XC_MethodHook hookClass = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                String packageName = (String) param.args[0];
                if (packageName.matches("de\\.robv\\.android\\.xposed\\.Xposed+.+")) {
                    param.setThrowable(new ClassNotFoundException(packageName));
                }
            }
        };
        // FIXME: 18-6-23 w568w: It's very dangerous to hook these methods, thinking to replace them.
        XposedHelpers.findAndHookMethod(
                ClassLoader.class,
                "loadClass",
                String.class,
                boolean.class,
                hookClass
        );
        XposedHelpers.findAndHookMethod(
                Class.class,
                "forName",
                String.class,
                boolean.class,
                ClassLoader.class,
                hookClass
        );

        XposedHelpers.findAndHookConstructor(
                File.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        String path = (String) param.args[0];
                        boolean shouldDo = path.matches("/proc/[0-9]+/maps") ||
                                (path.toLowerCase().contains(C.KW_XPOSED) &&
                                        !path.startsWith(mSdcard) && !path.contains("fkzhang"));
                        if (shouldDo) {
                            param.args[0] = "/system/build.prop";
                        }
                    }
                }
        );

        XC_MethodHook hookStack = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                StackTraceElement[] elements = (StackTraceElement[]) param.getResult();
                List<StackTraceElement> clone = new ArrayList<>();
                for (StackTraceElement element : elements) {
                    if (!element.getClassName().toLowerCase().contains(C.KW_XPOSED)) {
                        clone.add(element);
                    }
                }
                param.setResult(clone.toArray(new StackTraceElement[0]));
            }
        };
        XposedHelpers.findAndHookMethod(
                Throwable.class,
                "getStackTrace",
                hookStack
        );
        XposedHelpers.findAndHookMethod(
                Thread.class,
                "getStackTrace",
                hookStack
        );

        XposedHelpers.findAndHookMethod(
                "android.app.ApplicationPackageManager",
                lpparam.classLoader,
                "getInstalledPackages",
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        List<PackageInfo> apps = (List<PackageInfo>) param.getResult();
                        List<PackageInfo> clone = new ArrayList<>();
                        // foreach is very slow.
                        final int len = apps.size();
                        for (int i = 0; i < len; i++) {
                            PackageInfo app = apps.get(i);
                            if (!app.packageName.toLowerCase().contains(C.KW_XPOSED)) {
                                clone.add(app);
                            }
                        }
                        param.setResult(clone);
                    }
                }
        );
        XposedHelpers.findAndHookMethod(
                "android.app.ApplicationPackageManager",
                lpparam.classLoader,
                "getInstalledApplications",
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        List<ApplicationInfo> apps = (List<ApplicationInfo>) param.getResult();
                        List<ApplicationInfo> clone = new ArrayList<>();
                        final int len = apps.size();
                        for (int i = 0; i < len; i++) {
                            ApplicationInfo app = apps.get(i);
                            boolean shouldRemove = app.metaData != null && app.metaData.getBoolean("xposedmodule") ||
                                    app.packageName != null && app.packageName.toLowerCase().contains(C.KW_XPOSED) ||
                                    app.className != null && app.className.toLowerCase().contains(C.KW_XPOSED) ||
                                    app.processName != null && app.processName.toLowerCase().contains(C.KW_XPOSED);
                            if (!shouldRemove) {
                                clone.add(app);
                            }
                        }
                        param.setResult(clone);
                    }
                }
        );

        XposedHelpers.findAndHookMethod(
                Modifier.class,
                "isNative",
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        param.setResult(false);
                    }
                }
        );

        XposedHelpers.findAndHookMethod(
                System.class,
                "getProperty",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        if ("vxp".equals(param.args[0])) {
                            param.setResult(null);
                        }
                    }
                }
        );

        XposedHelpers.findAndHookMethod(
                File.class,
                "list",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String[] fs = (String[]) param.getResult();
                        if (fs == null) {
                            return;
                        }
                        List<String> list = new ArrayList<>();
                        for (String f : fs) {
                            if (!f.toLowerCase().contains(C.KW_XPOSED) && !f.equals("su")) {
                                list.add(f);
                            }
                        }
                        param.setResult(list.toArray(new String[0]));
                    }
                }
        );

        Class<?> clazz = null;
        try {
            clazz = Runtime.getRuntime().exec("echo").getClass();
        } catch (IOException ignore) {
            XposedBridge.log("[W/XposedHider] Cannot hook Process#getInputStream");
        }
        if (clazz != null) {
            XposedHelpers.findAndHookMethod(
                    clazz,
                    "getInputStream",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            InputStream is = (InputStream) param.getResult();
                            if (is instanceof FilterXpInputStream) {
                                param.setResult(is);
                            } else {
                                param.setResult(new FilterXpInputStream(is));
                            }
                        }
                    }
            );
        }

        XposedBridge.hookAllMethods(System.class, "getenv",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        if (param.args.length == 0) {
                            Map<String, String> res = (Map<String, String>) param.getResult();
                            String classpath = res.get("CLASSPATH");
                            param.setResult(filter(classpath));
                        } else if ("CLASSPATH".equals(param.args[0])) {
                            String classpath = (String) param.getResult();
                            param.setResult(filter(classpath));
                        }
                    }

                    private String filter(String s) {
                        List<String> list = Arrays.asList(s.split(":"));
                        List<String> clone = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            if (!list.get(i).toLowerCase().contains(C.KW_XPOSED)) {
                                clone.add(list.get(i));
                            }
                        }
                        StringBuilder res = new StringBuilder();
                        for (int i = 0; i < clone.size(); i++) {
                            res.append(clone);
                            if (i != clone.size() - 1) {
                                res.append(":");
                            }
                        }
                        return res.toString();
                    }
                }
        );
    }


    private void c(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod(("android.location.Location"), loadPackageParam.classLoader, "getAccuracy", new XC_MethodHook() {
            protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                super.beforeHookedMethod(methodHookParam);
                methodHookParam.setResult(Float.valueOf(0.1f));
            }
        });
        if (loadPackageParam.packageName.equals("com.gojek.driver.bike") || loadPackageParam.packageName.equals("com.gojek.driver.kilat") || loadPackageParam.packageName.equals("com.gojek.driver.car") || loadPackageParam.packageName.equals("com.grabtaxi.driver2") || loadPackageParam.packageName.equals("com.gojek.kampret.bike") || loadPackageParam.packageName.equals("com.gojek.kampret.car") || loadPackageParam.packageName.equals("com.google.android.apps.maps") || loadPackageParam.packageName.equals("com.gojek.goboxdriver") || loadPackageParam.packageName.equals("com.google.bct")) {
            XposedHelpers.findAndHookMethod(Constants.h("YW5kcm9pZC5sb2NhdGlvbi5Mb2NhdGlvbg=="), loadPackageParam.classLoader, Constants.h("Z2V0TGF0aXR1ZGU="), new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    super.beforeHookedMethod(methodHookParam);
                    if (bb.b().f5496c.booleanValue()) {
                        methodHookParam.setResult(Double.valueOf(bb.b().f5494a));
                    }
                }
            });
            XposedHelpers.findAndHookMethod(Constants.h("YW5kcm9pZC5sb2NhdGlvbi5Mb2NhdGlvbg=="), loadPackageParam.classLoader, Constants.h("Z2V0TG9uZ2l0dWRl"), new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    super.beforeHookedMethod(methodHookParam);
                    if (bb.b().f5496c.booleanValue()) {
                        methodHookParam.setResult(Double.valueOf(bb.b().f5495b));
                    }
                }
            });
            XposedHelpers.findAndHookMethod(Constants.h("YW5kcm9pZC5sb2NhdGlvbi5Mb2NhdGlvbk1hbmFnZXI="), loadPackageParam.classLoader, Constants.h("Z2V0TGFzdEtub3duTG9jYXRpb24="), String.class, new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam methodHookParam) {
                    if (bb.b().f5496c.booleanValue() && methodHookParam.getResult() != null) {
                        Location location = new Location("network");
                        location.setLatitude(Double.valueOf(bb.b().f5494a).doubleValue());
                        location.setLongitude(Double.valueOf(bb.b().f5495b).doubleValue());
                        location.setAccuracy(0.1f);
                    }
                }
            });
            XposedHelpers.findAndHookMethod(Constants.h("YW5kcm9pZC5sb2NhdGlvbi5Mb2NhdGlvbk1hbmFnZXIuTGlzdGVuZXJUcmFuc3BvcnQ="), loadPackageParam.classLoader, Constants.h("b25Mb2NhdGlvbkNoYW5nZWQ="), Location.class, new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam methodHookParam) {
                    if (bb.b().f5496c.booleanValue()) {
                        Location location = new Location("network");
                        location.setLatitude(Double.valueOf(bb.b().f5494a).doubleValue());
                        location.setLongitude(Double.valueOf(bb.b().f5495b).doubleValue());
                        location.setAccuracy(0.1f);
                    }
                }
            });
            XposedHelpers.findAndHookMethod(LocationManager.class, Constants.h("Z2V0TGFzdExvY2F0aW9u"), new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam methodHookParam) {
                    if (bb.b().f5496c.booleanValue()) {
                        Location location = new Location("network");
                        location.setLatitude(Double.valueOf(bb.b().f5494a).doubleValue());
                        location.setLongitude(Double.valueOf(bb.b().f5495b).doubleValue());
                        location.setAccuracy(0.1f);
                        location.setTime(0);
                        methodHookParam.setResult(location);
                    }
                }
            });
            XposedHelpers.findAndHookMethod(LocationManager.class, Constants.h("Z2V0TGFzdEtub3duTG9jYXRpb24="), String.class, new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam methodHookParam) {
                    if (bb.b().f5496c.booleanValue()) {
                        Location location = new Location("network");
                        location.setLatitude(Double.valueOf(bb.b().f5494a).doubleValue());
                        location.setLongitude(Double.valueOf(bb.b().f5495b).doubleValue());
                        location.setAccuracy(0.1f);
                        location.setTime(System.currentTimeMillis());
                        if (Build.VERSION.SDK_INT >= 17) {
                            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                        }
                        methodHookParam.setResult(location);
                    }
                }
            });
            XposedHelpers.findAndHookMethod(Constants.h("YW5kcm9pZC5sb2NhdGlvbi5Mb2NhdGlvbk1hbmFnZXI="), loadPackageParam.classLoader, "getGpsStatus", GpsStatus.class, new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam methodHookParam) {
                    if (((GpsStatus) methodHookParam.getResult()) != null) {
                        Method method = null;
                        Method[] declaredMethods = GpsStatus.class.getDeclaredMethods();
                        int length = declaredMethods.length;
                        int i = 0;
                        while (true) {
                            if (i >= length) {
                                break;
                            }
                            Method method2 = declaredMethods[i];
                            if (method2.getName().equals("setStatus") && method2.getParameterTypes().length > 1) {
                                method = method2;
                                break;
                            }
                            i++;
                        }
                        if (method != null) {
                            method.setAccessible(true);
                        }
                    }
                }
            });
            for (Method method : LocationManager.class.getDeclaredMethods()) {
                if (method.getName().equals("requestLocationUpdates") && !Modifier.isAbstract(method.getModifiers()) && Modifier.isPublic(method.getModifiers())) {
                    XposedBridge.hookMethod(method, new XC_MethodHook() {
                        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                            if (methodHookParam.args.length >= 4 && (methodHookParam.args[3] instanceof LocationListener)) {
                                LocationListener locationListener = (LocationListener) methodHookParam.args[3];
                                Method method = null;
                                Method[] declaredMethods = LocationListener.class.getDeclaredMethods();
                                int length = declaredMethods.length;
                                int i = 0;
                                while (true) {
                                    if (i >= length) {
                                        break;
                                    }
                                    Method method2 = declaredMethods[i];
                                    if (method2.getName().equals(Constants.h("b25Mb2NhdGlvbkNoYW5nZWQ=")) && !Modifier.isAbstract(method2.getModifiers())) {
                                        method = method2;
                                        break;
                                    }
                                    i++;
                                }
                                if (bb.b().f5496c.booleanValue()) {
                                    Location location = new Location("network");
                                    location.setLatitude(Double.valueOf(bb.b().f5494a).doubleValue());
                                    location.setLongitude(Double.valueOf(bb.b().f5495b).doubleValue());
                                    location.setAccuracy(0.1f);
                                    location.setTime(System.currentTimeMillis());
                                    if (Build.VERSION.SDK_INT >= 17) {
                                        location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                                    }
                                    XposedHelpers.callMethod(locationListener, Constants.h("b25Mb2NhdGlvbkNoYW5nZWQ="), new Object[]{location});
                                    if (method != null) {
                                        try {
                                            method.invoke(locationListener, new Object[]{location});
                                        } catch (Exception e2) {
                                            XposedBridge.log(e2);
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
                if (method.getName().equals("requestSingleUpdate ") && !Modifier.isAbstract(method.getModifiers()) && Modifier.isPublic(method.getModifiers())) {
                    XposedBridge.hookMethod(method, new XC_MethodHook() {
                        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                            if (methodHookParam.args.length >= 3 && (methodHookParam.args[1] instanceof LocationListener)) {
                                LocationListener locationListener = (LocationListener) methodHookParam.args[3];
                                Method method = null;
                                Method[] declaredMethods = LocationListener.class.getDeclaredMethods();
                                int length = declaredMethods.length;
                                int i = 0;
                                while (true) {
                                    if (i >= length) {
                                        break;
                                    }
                                    Method method2 = declaredMethods[i];
                                    if (method2.getName().equals(Constants.h("b25Mb2NhdGlvbkNoYW5nZWQ=")) && !Modifier.isAbstract(method2.getModifiers())) {
                                        method = method2;
                                        break;
                                    }
                                    i++;
                                }
                                if (bb.b().f5496c.booleanValue() && method != null) {
                                    try {
                                        Location location = new Location("network");
                                        location.setLatitude(Double.valueOf(bb.b().f5494a).doubleValue());
                                        location.setLongitude(Double.valueOf(bb.b().f5495b).doubleValue());
                                        location.setAccuracy(0.1f);
                                        location.setTime(System.currentTimeMillis());
                                        if (Build.VERSION.SDK_INT >= 17) {
                                            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                                        }
                                        method.invoke(locationListener, new Object[]{location});
                                    } catch (Exception e2) {
                                        XposedBridge.log(e2);
                                    }
                                }
                            }
                        }
                    });


                }

            }
        }
    }


    private String a(int i2) {
        return i2 == 0 ? "CASH" : "GOPAY";
    }

    private String b(int i2) {
        switch (i2) {
            case 1:
                return "GO-RIDE";
            case 3:
                return "GO-SHOP";
            case 5:
                return "GO-FOOD";
            case 6:
                return "GO-MART";
            case 11:
                return "APOTIK ANTAR";
            case 12:
                return "GO-MED";
            case 14:
                return "GO-SEND";
            case 19:
                return "GO-BLUE";
            case 23:
                return "GO-KILAT";
            case 24:
                return "GO-CAR";
            case 35:
                return "GO-BOX";
            default:
                return null;
        }
    }


    private void writeModule(String version) {
        Common.b();
        XposedBridge.log("Module Filter Autobid ");
        XposedBridge.log("Autobid : " + bb.b().d);
        XposedBridge.log("- Jarak min : " + bb.b().e);
        XposedBridge.log("- Jarak max : " + bb.b().f);
        XposedBridge.log("Filter jenis order");
        XposedBridge.log("- GoRide : " + bb.b().g);
        XposedBridge.log("- GoFood : " + bb.b().h);
        XposedBridge.log("- GoKilat : " + bb.b().i);
        XposedBridge.log("- GoSend : " + bb.b().j);
        XposedBridge.log("- GoShop : " + bb.b().k);
        XposedBridge.log("- GoMart : " + bb.b().l);
    }

    public Integer jag;
    public Double jag2;
    public String jarak;
    public Double jarak1;
    public Integer jarak2 = 0;
    public Integer jarak3;
    public Integer jarak4;
    public Double jarak5;
    public Double jarak6;
    public String jarak8;
    public String job;
    public Integer jobtipe = 0;


    public Boolean aaabi() {
        bb.a();
        Common.b();
        pref = new RemotePreferences(systemContext, "com.google.bct", Constants.h("YmN0"));
       jarakMin = prefs.getInt("jarakawal", 0);
       jarakMax = prefs.getInt("jarakakhir", 25);
       ggg= prefs.getBoolean("goride", false);
        prefs.getBoolean("gofood", false);
        prefs.getBoolean("gosend", false);
        prefs.getBoolean("gomart", false);
        prefs.getBoolean("goshop", false);
        prefs.getBoolean("gokilat", false);
        if ((!bb.a().d || jarak2 >= bb.a().e || this.jarak2 <= bb.b().f || !bb.a().hh || this.jobtipe != 5) && ((!bb.a().gg || this.jobtipe != 1) && ((!bb.a().ii || jobtipe != 14) && ((!bb.a().jj || jobtipe != 6) && ((!bb.a().kk || jobtipe != 3) && (!bb.a().ll || jobtipe != 23)))))) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean ssc() {
        bb.a();
        prefs.getInt("jarakawal", 0);
        prefs.getInt("jarakakhir", 25);
        if (!bb.a().d || this.jarak5.intValue() > bb.a().e || this.jarak5.intValue() < bb.a().f) {
            return false;
        }
        return true;
    }

    public void aaaa(){


    }

    private class p extends XC_MethodHook{
        public p(bacotLu bacotLu) {
        }

        public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
            afterHookedMethod(methodHookParam);
            if (methodHookParam != null) {
                XposedHelpers.getObjectField(methodHookParam.thisObject, "notify");
                methodHookParam.setResult(Boolean.FALSE);
            }
        }

    }
}