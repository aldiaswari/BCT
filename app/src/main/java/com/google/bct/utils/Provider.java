package com.google.bct.utils;

import com.crossbowffs.remotepreferences.RemotePreferenceProvider;

public class Provider extends RemotePreferenceProvider {
    public Provider() {
        super("com.google.bct", new String[] {"bct"});
    }
}