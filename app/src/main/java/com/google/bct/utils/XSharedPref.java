package com.google.bct.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.lang.reflect.Constructor;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;

/**
 * Created by Arie on 10/13/2017.
 */

public class XSharedPref {
    private boolean mStatus;
    private XSharedPreferences mConfig;

    private SharedPreferences user_stat;
    SharedPreferences.Editor editor;

    public XSharedPref(XSharedPreferences xSharedPreferences){
        mConfig = xSharedPreferences;
        mStatus = true;
        refresh();
    }

    void refresh(){
        mConfig.reload();
        mConfig.makeWorldReadable();
    }

    public int GDVersion(){
        return getInt(PrefsKey.KEY_GDVERSION);
    }

    String getFA(){
        return mConfig.getString(PrefsKey.KEY_FA, "");
    }

    void setFA(String value){
        editor.putString(PrefsKey.KEY_FA, value);
        editor.apply();
    }

    boolean autoBid(){
        return getBoolean(PrefsKey.KEY_AUTOBID);
    }

    boolean cashFilter(){
        return getBoolean(PrefsKey.KEY_FILTERCASH);
    }

    boolean gopayFilter(){
        return getBoolean(PrefsKey.KEY_FILTERGOPAY);
    }

    boolean jarakFilter(){
        return getBoolean(PrefsKey.KEY_FILTERJARAK);
    }

    boolean tarifFilter(){
        return getBoolean(PrefsKey.KEY_FILTERTARIF);
    }

    boolean gorideFilter(){
        return getBoolean(PrefsKey.KEY_FILTERGORIDE);
    }

    boolean gofoodFilter(){
        return getBoolean(PrefsKey.KEY_FILTERGOFOOD);
    }

    boolean gokilatFilter(){
        return getBoolean(PrefsKey.KEY_FILTERGOKILAT);
    }

    boolean gosendFilter(){
        return getBoolean(PrefsKey.KEY_FILTERGOSEND);
    }

    boolean goshopFilter(){
        return getBoolean(PrefsKey.KEY_FILTERGOSHOP);
    }

    boolean gomartFilter(){
        return getBoolean(PrefsKey.KEY_FILTERGOMART);
    }

    int jarakMin(){
        return getInt(PrefsKey.KEY_JARAKMIN);
    }

    int jarakMax(){
        return getInt(PrefsKey.KEY_JARAKMAX, 60);
    }

    int tarifMin(){
        return getInt(PrefsKey.KEY_TARIFMIN);
    }

    int tarifMax(){
        return getInt(PrefsKey.KEY_TARIFMAX, 500000);
    }

    String email(){
        return getString(PrefsKey.KEY_EMAIL);
    }

    void setEmail(String value){
        putData(PrefsKey.KEY_EMAIL, value);
    }

    String IMEI(){
        return getString(PrefsKey.KEY_IMEI);
    }

    void setIMEI(String value){
        putStringData(PrefsKey.KEY_IMEI, value);
    }

    String key(){
        return getString(PrefsKey.KEY_KEY);
    }

    void setKey(String value){
        putData(PrefsKey.KEY_KEY, value);
    }

    String ads(){
        return getString(PrefsKey.KEY_ADS);
    }

    void setAds(String value){
        putData(PrefsKey.KEY_ADS, value);
    }

    int interCount(){
        return getInt(PrefsKey.KEY_INTERCOUNT);
    }

    void setInterCount(int value){
        putData(PrefsKey.KEY_INTERCOUNT, value);
    }

    String manufacture(){
        return getString(PrefsKey.KEY_MANUFACTURE);
    }

    void setManufacture(String value){
        putData(PrefsKey.KEY_MANUFACTURE, value);
    }

    String model(){
        return getString(PrefsKey.KEY_MODEL);
    }

    void setModel(String value){
        putData(PrefsKey.KEY_MODEL, value);
    }

    String fakeIMEI(String defaultValue){
        return getString(PrefsKey.KEY_FAKEIMEI, defaultValue);
    }

    String fakeIMEI(){
        return getString(PrefsKey.KEY_FAKEIMEI);
    }

    void setFakeIMEI(String value){
        putData(PrefsKey.KEY_FAKEIMEI, value);
    }

    String IMEIHistory(){
        return getString(PrefsKey.KEY_IMEIHISTORY);
    }

    void setIMEIHistory(String value){
        putData(PrefsKey.KEY_IMEIHISTORY, value);
    }

    boolean showLog(){
        return getBoolean(PrefsKey.KEY_SHOWLOG, true);
    }

    void setShowLog(boolean value){
        putData(PrefsKey.KEY_SHOWLOG, value);
    }

    boolean fakeDevice(){
        return getBoolean(PrefsKey.KEY_FAKEDEVICE, true);
    }

    void setFakeDevice(boolean value){
        putData(PrefsKey.KEY_FAKEDEVICE, value);
    }

    boolean AON(){
        return getBoolean(PrefsKey.KEY_AON);
    }
    boolean showCue(){
        return getBoolean(PrefsKey.KEY_BCT, true);
    }

    void setAON(boolean value){
        putData(PrefsKey.KEY_AON, value);
    }

    private String getString(String key, String defaultValue){
        return mConfig.getString(key, defaultValue);
    }

    private String getString(String key){
        return getString(key, "");
    }

    private boolean getBoolean(String key, boolean defaultValue){
        return mConfig.getBoolean(key, defaultValue);
    }

    private boolean getBoolean(String key){
        return getBoolean(key, false);
    }

    private int getInt(String key, int defaultValue){
        return mConfig.getInt(key, defaultValue);
    }

    private int getInt(String key){
        return getInt(key, 0);
    }

    private void putStringData(String key, String value){
        editor.putString(key, String.valueOf(value));
        editor.apply();
    }

    private void putData(String key, Object value){
        String valueType = value.getClass().getSimpleName();
//        Debug.log("Value type " + key + " " + value + " " + valueType);
        switch (valueType){
            case "Boolean":
                editor.putBoolean(key, (boolean) value);
                break;
            case "Integer":
                editor.putInt(key, (int) value);
                break;
            case "String":
                editor.putString(key, (String) value);
                break;
        }
        editor.apply();
    }

    XSharedPreferences config(){
        return mConfig;
    }

    boolean status(){
        return mStatus;
    }

    private void getAccess() {
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "chmod 777 /data/data/com.nostratech.gojek.driver/shared_prefs/com.nostratech.gojek.driver.xml"});
            proc.waitFor();
            proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "chmod 777 /data/data/com.nostratech.gojek.driver/shared_prefs"});
            proc.waitFor();
            proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "chmod 777 /data/data/com.nostratech.gojek.driver"});
            proc.waitFor();

//            File preffile = new File("/data/data/com.nostratech.gojek.driver/shared_prefs/com.nostratech.gojek.driver.xml");
//
//            Class prefimplclass = Class.forName("android.app.SharedPreferencesImpl");
//
//            Constructor prefimplconstructor = prefimplclass.getDeclaredConstructor(File.class, int.class);
//            prefimplconstructor.setAccessible(true);
//
//            Object prefimpl = prefimplconstructor.newInstance(preffile, Context.CONTEXT_IGNORE_SECURITY);
//
//            SharedPreferences.Editor editor = (SharedPreferences.Editor) prefimplclass.getMethod("edit").invoke(prefimpl);
////put your settings here
//            editor.commit();
//            Debug.log("pref done");
//            String code3 = encodeString(getCode3());
//            autologinRoot(email, code1, code2, code3);

        } catch (Exception err){

        }
    }

}
