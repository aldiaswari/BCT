package com.google.bct.hook;

import android.content.SharedPreferences;
import java.io.Serializable;

class cc implements Serializable {

    /* renamed from: a  reason: collision with root package name */
    String f5494a;

    /* renamed from: b  reason: collision with root package name */
    String f5495b;

    /* renamed from: c  reason: collision with root package name */
    Boolean f5496c;

    /* renamed from: d  reason: collision with root package name */
    Boolean f5497d;

    /* renamed from: e  reason: collision with root package name */
    Boolean f5498e;

    /* renamed from: f  reason: collision with root package name */
    Integer f5499f;
    Integer g;
    Integer h;
    Integer i;
    Integer j;
    Integer k;
    Integer l;
    Integer m;
    Boolean n;
    Boolean o;
    Boolean p;
    Boolean q;
    String r;
    public String a;
    public String b;
    public Boolean c;
    public Boolean d;
    public Integer e;
    public Integer f;
    public Boolean gg;
    public Boolean hh;
    public Boolean ii;
    public Boolean jj;
    public Boolean kk;
    public Boolean ll;

    cc(SharedPreferences sharedPreferences) {
        a(sharedPreferences);
    }

    private void a(SharedPreferences sharedPreferences) {
        this.f5494a = sharedPreferences.getString("latitude", "");
        this.f5495b = sharedPreferences.getString("longitude", "");
        this.f5496c = sharedPreferences.getBoolean("state", false);
        this.f5498e = sharedPreferences.getBoolean("pf", false);
        this.f5497d = sharedPreferences.getBoolean("bctlu", false);
        this.f5499f = sharedPreferences.getInt("dn1", 0);
        this.g = sharedPreferences.getInt("dx1", 25);
        this.h = sharedPreferences.getInt("dn2", 0);
        this.i = sharedPreferences.getInt("dx2", 25);
        this.j = sharedPreferences.getInt("fn1", 5000);
        this.k = sharedPreferences.getInt("fx1", 200000);
        this.l = sharedPreferences.getInt("fn2", 5000);
        this.m = sharedPreferences.getInt("fx2", 200000);
        this.n = sharedPreferences.getBoolean("goride", false);
        this.o = sharedPreferences.getBoolean("gofood", false);
        this.p = sharedPreferences.getBoolean("gomart", false);
        this.q = sharedPreferences.getBoolean("goshop", false);
        this.a = sharedPreferences.getString("lat", "");
        this.b = sharedPreferences.getString("lng", "");
        this.c = sharedPreferences.getBoolean("mock", false);
        this.d = sharedPreferences.getBoolean("gosit", false);
        this.e = sharedPreferences.getInt("jarakawal", 0);
        this.f = sharedPreferences.getInt("jarakakhir", 25);
        this.gg = sharedPreferences.getBoolean("goride", false);
        this.hh = sharedPreferences.getBoolean("gofood", false);
        this.ii = sharedPreferences.getBoolean("gosend", false);
        this.jj = sharedPreferences.getBoolean("gomart", false);
        this.kk = sharedPreferences.getBoolean("goshop", false);
        this.ll = sharedPreferences.getBoolean("gokilat", false);
        this.r = sharedPreferences.getString("login", "");
    }

}
