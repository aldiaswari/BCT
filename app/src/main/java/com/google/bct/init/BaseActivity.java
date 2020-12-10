package com.google.bct.init;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.bct.common.Constants;
import com.google.bct.ui.LoginAct;
import com.google.bct.ui.MainActivity;
import com.google.bct.ui.Reg;
import com.google.bct.ui.main;
import com.kaopiz.kprogresshud.KProgressHUD;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "bct";
    SharedPreferences prefs;
    Intent a;
    AppSignatureHashHelper signHelp;
    private KProgressHUD hud;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        prefs = getSharedPreferences("bct", 0);
        signHelp = new AppSignatureHashHelper(this);
        Log.d(TAG, "BCT Hash Key: " + signHelp.getAppSignatures().get(0));
        pref();
    }

    public void prog(){
    hud = KProgressHUD.create(BaseActivity.this)
            .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(100)
                .show();
        hud.setProgress(90);
    }

    public void huddim(){
        hud.dismiss();
    }

    public void simulateProgressUpdate() {
        hud.setMaxProgress(100);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            int currentProgress;

            @Override
            public void run() {
                currentProgress += 1;
                hud.setProgress(currentProgress);
                if (currentProgress == 80) {
                    hud.setLabel("Sebentar Lagi...");
                }
                if (currentProgress < 100) {
                    handler.postDelayed(this, 50);
                }
            }
        }, 100);
    }

    public void aaaa(){
        hud.dismiss();
    }

    public void goreg(){
        startActivity(new Intent(BaseActivity.this, Reg.class));
        finish();
    }

    public void goback() {
        startActivity(new Intent(BaseActivity.this, LoginAct.class));
    }

    public void pref(){

    }

    public void mai(){
       startActivity(new Intent(BaseActivity.this, main.class));

        Log.e("log", Constants.h("YmVsdW0gbG9naW4="));

    }

    public void gomainact(){

    }

    public void fin(){
        finish();
    }

    public void preflo(){
       SharedPreferences.Editor editor = prefs.edit();
       editor.putBoolean("login", true);
       editor.apply();
    }

    public void goo(){
       if(prefs.getBoolean("login", true)){
           gomainact();
       }
    }
}
