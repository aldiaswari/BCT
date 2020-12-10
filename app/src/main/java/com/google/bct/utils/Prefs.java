package com.google.bct.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Boolean.FALSE;

/**
 * Created by Arie on 24/11/2015.
 */
public class Prefs {
    private Context context;
    private SharedPreferences user_stat;
    private boolean sip = true;

    public Prefs(Context context) {
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences("pref", 0);
        File prefsFile = new File(context.getFilesDir() + "/../shared_prefs/" + "pref" + ".xml");
        prefsFile.setReadable(true, false);
        this.user_stat = prefs;
        String key = Protection.md5(Protection.getCertificateSHA1Fingerprint(context));
//        this.sip = BuildConfig.DEBUG ? key.equals("8de7a11da526739759a36941f1617e3a") : key.equals("cf7735c7f87880e3367b6122e24cb206");
    }







    public int getGobisVersion(){
        return user_stat.getInt("gobisVersion", 0);
    }

    public void setGobisVersion(int version){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putInt("gobisVersion", version);
        editor.commit();
    }

    boolean getHideRoot(){
        return user_stat.getBoolean("hideRoot", false);
    }

    void setHideRoot(boolean hideMock){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("hideRoot", hideMock);
        editor.commit();
    }

    public boolean getHideMock(){
        return user_stat.getBoolean("hideMock", false);
    }

    public void setHideMock(boolean hideMock){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("hideMock", hideMock);
        editor.commit();
    }

    boolean getHideFA(){
        return user_stat.getBoolean("hideFA", false);
    }

    void setHideFA(boolean hideFA){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("hideFA", hideFA);
        editor.commit();
    }

    public boolean getFakeDevice(){

        return user_stat.getBoolean("fakeDevice", false);
    }

    public void setFakeDevice(boolean hideFA){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("fakeDevice", hideFA);
        editor.commit();
    }

    public String getIMEIHistory(){
        return user_stat.getString("imeiHistory", "");
    }

    public void setIMEIHistory(String json){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putString("imeiHistory", json);
        editor.commit();
    }

    public String getFA(){
        return user_stat.getString("fa", "");
    }

    public void setFA(String json){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putString("fa", json);
        editor.commit();
    }

    public String getFakeManufacture(){
        return user_stat.getString("fakeManufacture", "");
    }

    public void setFakeManufacture(String json){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putString("fakeManufacture", json);
        editor.commit();
    }

    public String getFakeModel(){
        return user_stat.getString("fakeModel", "");
    }

    public void setFakeModel(String json){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putString("fakeModel", json);
        editor.commit();
    }

    public boolean getHideVibrateG1(){
        return user_stat.getBoolean("hideVibrateG1", false);
    }

    public void setHideVibrateG1(boolean json){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("hideVibrateG1", json);
        editor.commit();
    }

    public boolean getHideSoundG1(){
        return user_stat.getBoolean("hideSoundG1", false);
    }

    public void setHideSoundG1(boolean json){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("hideSoundG1", json);
        editor.commit();
    }

    public String getˊ(){
        return user_stat.getString("ˊ", "");
    }

    public void setˊ(String json){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putString("ˊ", json);
        editor.commit();
    }

    public String getFakeIMEI(){
        return user_stat.getString("fakeIMEI", "");
    }

    public void setFakeIMEI(String json){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putString("fakeIMEI", json);
        editor.commit();
    }

    public String getIMEI(){
        return user_stat.getString("imei", "");
    }

    public void setIMEI(String json){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putString("imei", json);
        editor.commit();
    }

    public String getEmail(){
        return user_stat.getString("email", "");
    }

    public void setEmail(String json){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putString("email", json);
        editor.commit();
    }

    public String getAccessCode(){
        return user_stat.getString("accessCode", "");
    }

    public void setAccessCode(String json){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putString("accessCode", json);
        editor.commit();
    }


    public int getInterCount(){
        return user_stat.getInt("interCount", 0);
    }

    public void setInterCount(int size){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putInt("interCount", size);
        editor.commit();
    }


    public String getLog(){
        return user_stat.getString("log", "Log:");
    }

    public void setLog(String msg){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putString("log", msg);
        editor.commit();
    }

    public boolean getShowLog(){
        return user_stat.getBoolean("showLog", true);
    }

    public void setShowLog(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("showLog", status);
        editor.commit();
    }

    public boolean getGopayG1(){
        return user_stat.getBoolean("gopayG1", false);
    }

    public void setGopayG1(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("gopayG1", status);
        editor.commit();
    }

    public boolean getCashG1(){
        return user_stat.getBoolean("cashG1", false);
    }

    public void setCashG1(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("cashG1", status);
        editor.commit();
    }

    public boolean getAutoBidG1(){
        if (! this.sip) return false;
        return user_stat.getBoolean("autobidG1", false);
    }

    public void setAutoBidG1(boolean status){
        if (! this.sip) status=false;
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autobidG1", status);
        editor.commit();
    }

    public boolean getAutoJarakG1(){
        return user_stat.getBoolean("autojarakG1", false);
    }

    public void setAutoJarakG1(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autojarakG1", status);
        editor.commit();
    }

    public boolean getAutoTarifG1(){
        return user_stat.getBoolean("autotarifG1", false);
    }

    public void setAutoTarifG1(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autotarifG1", status);
        editor.commit();
    }

    public boolean getAutoGorideG1(){
        return user_stat.getBoolean("autogorideG1", false);
    }

    public void setAutoGorideG1(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autogorideG1", status);
        editor.commit();
    }

    public boolean getAutoGofoodG1(){
        return user_stat.getBoolean("autogofoodG1", false);
    }

    public void setAutoGofoodG1(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autogofoodG1", status);
        editor.commit();
    }

    public boolean getAutoGokilatG1(){
        return user_stat.getBoolean("autogokilatG1", false);
    }

    public void setAutoGokilatG1(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autogokilatG1", status);
        editor.commit();
    }

    public boolean getAutoGosendG1(){
        return user_stat.getBoolean("autogosendG1", false);
    }

    public void setAutoGosendG1(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autogosendG1", status);
        editor.commit();
    }

    public boolean getAutoGomartG1(){
        return user_stat.getBoolean("autogomartG1", false);
    }

    public void setAutoGomartG1(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autogomartG1", status);
        editor.commit();
    }

    public boolean getAutoGoshopG1(){
        return user_stat.getBoolean("autogoshopG1", false);
    }

    public void setAutoGoshopG1(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autogoshopG1", status);
        editor.commit();
    }

    public int getJarakMinG1(){
        return user_stat.getInt("jarakMinG1", 0);
    }

    public void setJarakMinG1(int n){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putInt("jarakMinG1", n);
        editor.commit();
    }

    public int getJarakMaxG1(){
        return user_stat.getInt("jarakMaxG1", 60);
    }

    public void setJarakMaxG1(int n){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putInt("jarakMaxG1", n);
        editor.commit();
    }

    public int getTarifMinG1(){
        return user_stat.getInt("tarifMinG1", 0);
    }

    public void setTarifMinG1(int n){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putInt("tarifMinG1", n);
        editor.commit();
    }

    public int getTarifMaxG1(){
        return user_stat.getInt("tarifMaxG1", 500000);
    }

    public void setTarifMaxG1(int n){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putInt("tarifMaxG1", n);
        editor.commit();
    }


    //gobis 2x

    public boolean getAutoBidG28(){
        if (! this.sip) return false;
        return user_stat.getBoolean("autobidG28", false);
    }

    public void setAutoBidG28(boolean status){
        if (! this.sip) status=false;
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autobidG28", status);
        editor.commit();
    }

    public boolean getAutoBidG210(){
        if (! this.sip) return false;
        return user_stat.getBoolean("autobidG210", false);
    }

    public void setAutoBidG210(boolean status){
        if (! this.sip) status=false;
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autobidG210", status);
        editor.commit();
    }

    public boolean getAutoBidG2(){
        if (! this.sip) return false;
        return user_stat.getBoolean("autobidG2", false);
    }

    public void setAutoBidG2(boolean status){
        if (! this.sip) status=false;
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autobidG2", status);
        editor.commit();
    }

    public boolean getAon(){
        if (! this.sip) return false;
        return user_stat.getBoolean("aon", false);
    }

    public void setAon(boolean status){
        if (! this.sip) status=false;
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("aon", status);
        editor.commit();
    }

    public boolean getCashG2(){
        return user_stat.getBoolean("cashG2", false);
    }

    public void setCashG2(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("cashG2", status);
        editor.commit();
    }

    public boolean getGopayG2(){
        return user_stat.getBoolean("gopayG2", false);
    }

    public void setGopayG2(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("gopayG2", status);
        editor.commit();
    }

    public boolean getAutoJarakG2(){
        return user_stat.getBoolean("autojarakG2", false);
    }

    public void setAutoJarakG2(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autojarakG2", status);
        editor.commit();
    }

    public boolean getAutoTarifG2(){
        return user_stat.getBoolean("autotarifG2", false);
    }

    public void setAutoTarifG2(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autotarifG2", status);
        editor.commit();
    }

    public boolean getAutoGorideG2(){
        return user_stat.getBoolean("autogorideG2", false);
    }

    public void setAutoGorideG2(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autogorideG2", status);
        editor.commit();
    }

    public boolean getAutoGofoodG2(){
        return user_stat.getBoolean("autogofoodG2", false);
    }

    public void setAutoGofoodG2(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autogofoodG2", status);
        editor.commit();
    }

    public boolean getAutoGokilatG2(){
        return user_stat.getBoolean("autogokilatG2", false);
    }

    public void setAutoGokilatG2(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autogokilatG2", status);
        editor.commit();
    }

    public boolean getAutoGosendG2(){
        return user_stat.getBoolean("autogosendG2", false);
    }

    public void setAutoGosendG2(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autogosendG2", status);
        editor.commit();
    }

    public boolean getAutoGomartG2(){
        return user_stat.getBoolean("autogomartG2", false);
    }

    public void setAutoGomartG2(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autogomartG2", status);
        editor.commit();
    }

    public boolean getAutoGoshopG2(){
        return user_stat.getBoolean("autogoshopG2", false);
    }

    public void setAutoGoshopG2(boolean status){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putBoolean("autogoshopG2", status);
        editor.commit();
    }

    public int getJarakMinG2(){
        return user_stat.getInt("jarakMinG2", 0);
    }

    public void setJarakMinG2(int n){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putInt("jarakMinG2", n);
        editor.commit();
    }

    public int getJarakMaxG2(){
        return user_stat.getInt("jarakMaxG2", 60);
    }

    public void setJarakMaxG2(int n){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putInt("jarakMaxG2", n);
        editor.commit();
    }

    public int getTarifMinG2(){
        return user_stat.getInt("tarifMinG2", 0);
    }

    public void setTarifMinG2(int n){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putInt("tarifMinG2", n);
        editor.commit();
    }

    public int getTarifMaxG2(){
        return user_stat.getInt("tarifMaxG2", 500000);
    }

    public void setTarifMaxG2(int n){
        SharedPreferences.Editor editor = user_stat.edit();
        editor.putInt("tarifMaxG2", n);
        editor.commit();
    }
}
