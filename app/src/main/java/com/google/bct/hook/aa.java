package com.google.bct.hook;

import java.io.File;

import de.robv.android.xposed.XSharedPreferences;

public class aa {
    public static ab a() {
    XSharedPreferences xSharedPreferences = new XSharedPreferences("com.diwa.bikes", "diwa");
    b();
    xSharedPreferences.makeWorldReadable();
    xSharedPreferences.reload();
    return new ab(xSharedPreferences);
}

        public static void b() {
            new File("/data/com.google.bct").setExecutable(true, false);
            new File("/data/data/com.google.bct").setReadable(true, false);
            new File("/data/data/com.google.bct/shared_prefs/").setExecutable(true, false);
            new File("/data/data/com.google.bct/shared_prefs/").setReadable(true, false);
            new File("/data/data/com.google.bct/shared_prefs/bct.xml").setReadable(true, false);
            new File("/data/user_de/0/com.google.bct").setExecutable(true, false);
            new File("/data/user_de/0/com.google.bct").setReadable(true, false);
            new File("/data/user_de/0/com.google.bct/shared_prefs/").setExecutable(true, false);
            new File("/data/user_de/0/com.google.bct/shared_prefs/").setReadable(true, false);
            new File("/data/user_de/0/com.google.bct/shared_prefs/bct.xml").setReadable(true, false);
            new File("/data/user/0/com.google.bct").setExecutable(true, false);
            new File("/data/user/0/com.google.bct").setReadable(true, false);
            new File("/data/user/0/com.google.bct/shared_prefs/").setExecutable(true, false);
            new File("/data/user/0/com.google.bct/shared_prefs/").setReadable(true, false);
            new File("/data/user/0/com.google.bct/shared_prefs/bct.xml").setReadable(true, false);
        }
}