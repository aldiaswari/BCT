package com.google.bct.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Switch;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Arie on 10/13/2017.
 */

public class XPref {
    private  SharedPreferences prefs;
    private Context mContext;
    private boolean mStatus;
    private SharedPreferences mConfig;

    private SharedPreferences user_stat;
    SharedPreferences.Editor editor;

    public XPref(Context context){
        mContext = context;

      //  WorldReadablePrefs prefs = SettingsManager.getInstance(mContext).getMainPrefs();
        this.mConfig = prefs;
        this.editor = prefs.edit();

//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            SharedPreferences prefs = context.getSharedPreferences("com.deuxvelva.satpolapp", Context.MODE_WORLD_READABLE);
//            this.mConfig = prefs;
//            this.editor = prefs.edit();
//        } else {
//
//        }
//        wPrefs.edit().putBoolean(PrefKey.KEY_AUTOBID, value).commit();
//        Debug.log("Autobid " + value + " " + String.valueOf(wPrefs.getBoolean(PrefKey.KEY_AUTOBID, false)));


//        SharedPreferences prefs = context.getSharedPreferences("xpref", 0);
//        File prefsFile = new File(context.getFilesDir() + "/../shared_prefs/" + "xpref" + ".xml");
//        prefsFile.setReadable(true, false);

        mStatus = true;
    }

    void refresh(){

    }

    String getFA(){
        return getString(PrefsKey.KEY_FA);
    }

    void setFA(String value){
        putData(PrefsKey.KEY_FA, value);
    }

    boolean autoBid(){
        return getBoolean(PrefsKey.KEY_AUTOBID);
    }

    void setAutoBid(boolean value){

        putData(PrefsKey.KEY_AUTOBID, value);
    }

    boolean cashFilter(){
        return getBoolean(PrefsKey.KEY_FILTERCASH);
    }

    void setCashFilter(boolean value){
        putData(PrefsKey.KEY_FILTERCASH, value);
    }

    boolean gopayFilter(){
        return getBoolean(PrefsKey.KEY_FILTERGOPAY);
    }

    void setGopayFilter(boolean value){
        putData(PrefsKey.KEY_FILTERGOPAY, value);
    }

    boolean jarakFilter(){
        return getBoolean(PrefsKey.KEY_FILTERJARAK);
    }

    void setJarakFilter(boolean value){
        putData(PrefsKey.KEY_FILTERJARAK, value);
    }

    boolean tarifFilter(){
        return getBoolean(PrefsKey.KEY_FILTERTARIF);
    }

    void setTarifFilter(boolean value){
        putData(PrefsKey.KEY_FILTERTARIF, value);
    }

    boolean gorideFilter(){
        return getBoolean(PrefsKey.KEY_FILTERGORIDE);
    }

    void setGorideFilter(boolean value){
        putData(PrefsKey.KEY_FILTERGORIDE, value);
    }

    boolean gofoodFilter(){
        return getBoolean(PrefsKey.KEY_FILTERGOFOOD);
    }

    void setGoFoodFilter(boolean value){
        putData(PrefsKey.KEY_FILTERGOFOOD, value);
    }

    boolean gokilatFilter(){
        return getBoolean(PrefsKey.KEY_FILTERGOKILAT);
    }

    void setGoKilatFilter(boolean value){
        putData(PrefsKey.KEY_FILTERGOKILAT, value);
    }

    boolean gosendFilter(){
        return getBoolean(PrefsKey.KEY_FILTERGOSEND);
    }

    void setGoSendFilter(boolean value){
        putData(PrefsKey.KEY_FILTERGOSEND, value);
    }

    boolean goshopFilter(){
        return getBoolean(PrefsKey.KEY_FILTERGOSHOP);
    }

    void setGoShopFilter(boolean value){
        putData(PrefsKey.KEY_FILTERGOSHOP, value);
    }

    boolean gomartFilter(){
        return getBoolean(PrefsKey.KEY_FILTERGOMART);
    }

    void setGoMartFilter(boolean value){
        putData(PrefsKey.KEY_FILTERGOMART, value);
    }

    int GDVersion(){
        return getInt(PrefsKey.KEY_GDVERSION);
    }

    void setGDVersion(int value){
        putData(PrefsKey.KEY_GDVERSION, value);
    }

    int jarakMin(){
        return getInt(PrefsKey.KEY_JARAKMIN);
    }

    void setJarakMin(int value){
        putData(PrefsKey.KEY_JARAKMIN, value);
    }

    int jarakMax(){
        return getInt(PrefsKey.KEY_JARAKMAX, 60);
    }

    void setJarakMax(int value){
        putData(PrefsKey.KEY_JARAKMAX, value);
    }

    int tarifMin(){
        return getInt(PrefsKey.KEY_TARIFMIN);
    }

    void setTarifMin(int value){
        putData(PrefsKey.KEY_TARIFMIN, value);
    }

    int tarifMax(){
        return getInt(PrefsKey.KEY_TARIFMAX, 500000);
    }

    void setTarifMax(int value){
        putData(PrefsKey.KEY_TARIFMAX, value);
    }

    String email(){
        return getString(PrefsKey.KEY_EMAIL);
    }

    void setEmail(String value){
        putData(PrefsKey.KEY_EMAIL, value);
    }

    String IMEI(){  //untuk unlock di tools
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

    void setAON(boolean value){
        putData(PrefsKey.KEY_AON, value);
    }

    boolean showCue(){
        return getBoolean(PrefsKey.KEY_BCT, true);
    }

    void setShowCue(boolean value){
        putData(PrefsKey.KEY_BCT, value);
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
        editor.commit();
    }

    private void putData(String key, Object value){
        String valueType = value.getClass().getSimpleName();
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
        editor.commit();
        //SettingsManager.getInstance(mContext).fixFolderPermissionsAsync();
    }

    SharedPreferences config(){
        return mConfig;
    }

    boolean status(){
        return mStatus;
    }

}
