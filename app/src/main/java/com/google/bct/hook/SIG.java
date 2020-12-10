package com.google.bct.hook;


import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SIG implements IXposedHookLoadPackage {

    /* renamed from: a  reason: collision with root package name */
    public static boolean f5572a;

    /* renamed from: b  reason: collision with root package name */
    public static XC_MethodHook f5573b;

    public static void a() {
        XposedHelpers.findAndHookMethod("java.security.Signature", null, "verify", byte[].class, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(Boolean.TRUE);
            }
        });
        XposedHelpers.findAndHookMethod("java.security.Signature", null, "verify", byte[].class, Integer.TYPE, Integer.TYPE, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(Boolean.TRUE);
            }
        });
        XposedHelpers.findAndHookMethod("java.security.MessageDigest", null, "isEqual", byte[].class, byte[].class, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(Boolean.TRUE);
            }
        });
    }

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if ("android".equals(loadPackageParam.packageName) && "android".equals(loadPackageParam.processName)) {
            Class findClass = XposedHelpers.findClass("com.android.server.pm.PackageManagerService", loadPackageParam.classLoader);
            XposedBridge.hookAllMethods(findClass, "installPackageAsUser", f5573b);
            XposedBridge.hookAllMethods(findClass, "installStage", f5573b);
        }
    }
}