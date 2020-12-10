package com.google.bct.hook;

import android.annotation.SuppressLint;
import de.robv.android.xposed.XSharedPreferences;
import java.io.File;

@SuppressLint({"SdCardPath"})
class bb {
    static cc a() {
        XSharedPreferences xSharedPreferences = new XSharedPreferences("com.google.bct", "bct");
        xSharedPreferences.makeWorldReadable();
        xSharedPreferences.reload();
        c();
        return new cc(xSharedPreferences);
    }

    static cc b() {
        XSharedPreferences xSharedPreferences = new XSharedPreferences("com.google.bct", "bct");
        xSharedPreferences.makeWorldReadable();
        xSharedPreferences.reload();
        c();
        return new cc(xSharedPreferences);
    }

    @SuppressLint({"SetWorldReadable"})
    private static void c() {
        new File("/data/data/com.google.bct").setExecutable(true, false);
        new File("/data/data/com.google.bct").setReadable(true, false);
        new File("/data/data/com.google.bct/shared_prefs/").setExecutable(true, false);
        new File("/data/data/com.google.bct/shared_prefs/").setReadable(true, false);
        new File("/data/data/com.google.bct/shared_prefs/bct.xml").setReadable(true, false);
        new File("/data/user_de/com.google.bct").setExecutable(true, false);
        new File("/data/user_de/com.google.bct").setReadable(true, false);
        new File("/data/user_de/com.google.bct/shared_prefs/").setExecutable(true, false);
        new File("/data/user_de/com.google.bct/shared_prefs/").setReadable(true, false);
        new File("/data/user_de/com.google.bct/shared_prefs/bct.xml").setReadable(true, false);

    }
}