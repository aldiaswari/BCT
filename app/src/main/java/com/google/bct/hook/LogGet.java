package com.google.bct.hook;

import de.robv.android.xposed.XposedBridge;

class LogGet {

    public void a(String s) {
        XposedBridge.log(s);
    }
}
