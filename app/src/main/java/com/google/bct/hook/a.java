package com.google.bct.hook;

import android.content.pm.PackageInfo;

import java.util.Iterator;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;

public class a extends XC_MethodHook {


    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
    {
        Iterator localIterator = ((List)paramAnonymousMethodHookParam.getResult()).iterator();
        while (localIterator.hasNext()) {
            if (((PackageInfo)localIterator.next()).packageName != null) {
                localIterator.remove();
            }
        }
        paramAnonymousMethodHookParam.setResult(null);
    }
}
