package com.google.bct.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
    public static Settings _instance;
    public float Accuracy = 0.0f;
    public double Altitude = 0.0d;
    public float Bearing = 0.0f;
    public int Duration = 700;
    public int History = 10;
    public boolean IsJoySitckMode = false;
    public boolean IsJoySitckPlay = false;
    public boolean IsPokemon = false;
    public int JoyDistacne = 10;
    public double Latitude = 0.0d;
    public double Latitude_Old = 0.0d;
    public double Longitude = 0.0d;
    public double Longitude_Old = 0.0d;
    public int MockUpdateTime = 500;
    public String Snippet = "";
    public float Tilt = 0.0f;
    public String Title = "";
    public float Zoom = 15.0f;
    private Context mContext;

    public Settings(Context context) {
        this.mContext = context;
        init();
    }

    public static Settings getInstance(Context context) {
        if (_instance == null) {
            _instance = new Settings(context);
        }
        return _instance;
    }

    private void init() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);

    }

    public void commit() {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();

        edit.commit();
    }
}