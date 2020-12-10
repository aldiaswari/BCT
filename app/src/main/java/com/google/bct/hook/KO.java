package com.google.bct.hook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class KO implements IXposedHookLoadPackage {
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("com.kozhevin.rootchecks.util.MeatGrinder", loadPackageParam.classLoader, "isDetectedDevKeys", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        });
        XposedHelpers.findAndHookMethod("com.kozhevin.rootchecks.util.MeatGrinder", loadPackageParam.classLoader, "isDetectedTestKeys", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        });
        XposedHelpers.findAndHookMethod("com.kozhevin.rootchecks.util.MeatGrinder", loadPackageParam.classLoader, "isNotFoundReleaseKeys", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        });
        XposedHelpers.findAndHookMethod("com.kozhevin.rootchecks.util.MeatGrinder", loadPackageParam.classLoader, "isFoundDangerousProps", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        });
        XposedHelpers.findAndHookMethod("com.kozhevin.rootchecks.util.MeatGrinder", loadPackageParam.classLoader, "isPermissiveSelinux", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        });
        XposedHelpers.findAndHookMethod("com.kozhevin.rootchecks.util.MeatGrinder", loadPackageParam.classLoader, "isSuExists", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        });
        XposedHelpers.findAndHookMethod("com.kozhevin.rootchecks.util.MeatGrinder", loadPackageParam.classLoader, "isAccessedSuperuserApk", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        });
        XposedHelpers.findAndHookMethod("com.kozhevin.rootchecks.util.MeatGrinder", loadPackageParam.classLoader, "isFoundSuBinary", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        });
        XposedHelpers.findAndHookMethod("com.kozhevin.rootchecks.util.MeatGrinder", loadPackageParam.classLoader, "isFoundBusyboxBinary", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        });
        XposedHelpers.findAndHookMethod("com.kozhevin.rootchecks.util.MeatGrinder", loadPackageParam.classLoader, "isFoundXposed", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        });
        XposedHelpers.findAndHookMethod("com.kozhevin.rootchecks.util.MeatGrinder", loadPackageParam.classLoader, "isFoundResetprop", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        });
        XposedHelpers.findAndHookMethod("com.kozhevin.rootchecks.util.MeatGrinder", loadPackageParam.classLoader, "isFoundWrongPathPermission", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        });
        XposedHelpers.findAndHookMethod("com.kozhevin.rootchecks.util.MeatGrinder", loadPackageParam.classLoader, "isFoundHooks", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        });
    }
}
