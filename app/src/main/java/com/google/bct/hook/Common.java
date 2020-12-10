package com.google.bct.hook;

import android.annotation.SuppressLint;
import android.os.Build;

import java.io.File;

import androidx.annotation.RequiresApi;

public class Common
{


    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint({"SdCardPath", "SetWorldReadable"})
    public static void b()
    {
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
