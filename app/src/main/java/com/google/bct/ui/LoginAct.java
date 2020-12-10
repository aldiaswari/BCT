package com.google.bct.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.bct.R;
import com.google.bct.common.Constants;
import com.google.bct.init.CheckInternetConnection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginAct extends AppCompatActivity {

    private SharedPreferences prefs;
    private FirebaseAuth.AuthStateListener authListener;

    public HashMap<String, Object> hashMap;
    private static final int PERMISSIONS_REQUEST_ALLERT = 101;
    private static final int PERMISSIONS_REQUEST_LOCATION = 102;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mai);
        prefs = getSharedPreferences(Constants.h("YmN0"), 0);
        FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        hashMap = new HashMap<>();
        hashMap.put(Constants.h("dmVyc2lvbl9hcHA="), Integer.valueOf(b()));
        hashMap.put(Constants.h("dmVyaW9uX2NvZGU="), prefs.getInt(Constants.h("dmVyaW9uX2NvZGU="),0));
        db.getReference(Constants.h("YXBw")).setValue(hashMap);
        getVersion();
        authListener = new goda();
        new CheckInternetConnection(this).checkConnection();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container ,new Prof()).commit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
        } else {
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_LOCATION);
        } else {

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_LOCATION);
        } else {

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_LOCATION);
        } else {

        }
    }

    private int b() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void getVersion(){
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            String version = info.versionName;
            int versionCode = info.versionCode;
            SharedPreferences.Editor pre = getSharedPreferences("bct",0).edit();
            pre.putString(Constants.h("dmVyc2lvbl9hcHA="), version);
            pre.putInt(Constants.h("dmVyaW9uX2NvZGU="), versionCode);
            pre.apply();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    private class goda implements FirebaseAuth.AuthStateListener {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
           //     Toast.makeText(MainActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginAct.this, main.class));
                finish();
                return;
            }
            SharedPreferences.Editor edit2 =prefs.edit();
            edit2.putBoolean(Constants.h("bG9naW4="), true);
            edit2.apply();
            new StringBuilder(":").append(user.getEmail());

        }
    }
}
