package com.google.bct.hook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SC implements IXposedHookLoadPackage {
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("com.scottyab.rootbeer.RootBeer", loadPackageParam.classLoader, "detectRootManagementApps", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        }});
        XposedHelpers.findAndHookMethod("com.scottyab.rootbeer.RootBeer", loadPackageParam.classLoader, "detectPotentiallyDangerousApps", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        }});
        XposedHelpers.findAndHookMethod("com.scottyab.rootbeer.RootBeer", loadPackageParam.classLoader, "detectTestKeys", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        }});
        XposedHelpers.findAndHookMethod("com.scottyab.rootbeer.RootBeer", loadPackageParam.classLoader, "checkForBusyBoxBinary", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        }});
        XposedHelpers.findAndHookMethod("com.scottyab.rootbeer.RootBeer", loadPackageParam.classLoader, "checkForSuBinary", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        }});
        XposedHelpers.findAndHookMethod("com.scottyab.rootbeer.RootBeer", loadPackageParam.classLoader, "checkSuExists", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        }});
        XposedHelpers.findAndHookMethod("com.scottyab.rootbeer.RootBeer", loadPackageParam.classLoader, "checkForRWPaths", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        }});
        XposedHelpers.findAndHookMethod("com.scottyab.rootbeer.RootBeer", loadPackageParam.classLoader, "checkForDangerousProps", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        }});
        XposedHelpers.findAndHookMethod("com.scottyab.rootbeer.RootBeer", loadPackageParam.classLoader, "checkForRootNative", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        }});
        XposedHelpers.findAndHookMethod("com.scottyab.rootbeer.RootBeer", loadPackageParam.classLoader, "detectRootCloakingApps", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        }});
        XposedHelpers.findAndHookMethod("com.scottyab.rootbeer.RootBeer", loadPackageParam.classLoader, "isSelinuxFlagInEnabled", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        }});
        XposedHelpers.findAndHookMethod("com.scottyab.rootbeer.RootBeer", loadPackageParam.classLoader, "isRooted", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        }});
        XposedHelpers.findAndHookMethod("com.scottyab.rootbeer.RootBeer", loadPackageParam.classLoader, "isRootedWithoutBusyBoxCheck", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        }});
        XposedHelpers.findAndHookMethod("com.scottyab.rootbeer.RootBeer", loadPackageParam.classLoader, "canLoadNativeLibrary", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        }});
        XposedHelpers.findAndHookMethod("com.scottyab.rootbeer.RootBeerNative", loadPackageParam.classLoader, "wasNativeLibraryLoaded", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult(false);
            }
        }});
    }
}