package com.google.bct.hook;

import android.content.SharedPreferences;
import java.io.Serializable;

public class ab implements Serializable {
    public String a;
    public String b;
    public Boolean c;
    public Boolean d;
    public Integer e;
    public Integer f;
    public Boolean g;
    public Boolean h;
    public Boolean i;
    public Boolean j;
    public Boolean k;
    public Boolean l;

    public ab(SharedPreferences sharedPreferences) {
        a(sharedPreferences);
    }

    public void a(SharedPreferences sharedPreferences) {
        this.a = sharedPreferences.getString("lat", "");
        this.b = sharedPreferences.getString("lng", "");
        this.c = sharedPreferences.getBoolean("mock", false);
        this.d = sharedPreferences.getBoolean("gosit", false);
        this.e = sharedPreferences.getInt("jarakawal", 0);
        this.f = sharedPreferences.getInt("jarakakhir", 25);
        this.g = sharedPreferences.getBoolean("goride", false);
        this.h = sharedPreferences.getBoolean("gofood", false);
        this.i = sharedPreferences.getBoolean("gosend", false);
        this.j = sharedPreferences.getBoolean("gomart", false);
        this.k = sharedPreferences.getBoolean("goshop", false);
        this.l = sharedPreferences.getBoolean("gokilat", false);
    }
}