package com.google.bct.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Debug;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.bct.R;
import com.google.bct.common.Constants;
import com.google.bct.init.CheckInternetConnection;
import com.jrummyapps.android.shell.Shell;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class Auto  extends AppCompatActivity {

    private Switch gosit;
    private Switch gofood;
    private Switch gokil;
    private Switch gomart;
    private Switch gomed;
    private Switch gopay;
    private Switch goride;
    private Switch gosend;
    private Switch goshop;
    private Intent intent;
    private CrystalRangeSeekbar jarak;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_auto);
        prefs = getSharedPreferences(Constants.h("YmN0"), 0);
   //     new CheckInternetConnection(this).checkConnection();
        initView();
        checkRoot();
    }

    private static boolean isRootAccessGiven = false;
    private void checkRoot() {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Boolean> checking = new AsyncTask<Void, Void, Boolean>() {

        @Override
        protected Boolean doInBackground(Void... params) {
            return Shell.SU.available();
        }

        @Override
        protected void onPostExecute(Boolean hasRoot) {
            super.onPostExecute(hasRoot);
            isRootAccessGiven = hasRoot;
            if (!hasRoot) {

            }
        }
    };
        checking.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
    }
    private void initView() {
        gosit = findViewById(R.id.sw_autobid);
        gofood = findViewById(R.id.bid_food);
        goride = findViewById(R.id.bid_ride);
        gokil = findViewById(R.id.bid_kil);
        gomart = findViewById(R.id.bid_mart);
        goshop = findViewById(R.id.bid_shop);
        gosend = findViewById(R.id.bid_send);
        gomed = findViewById(R.id.bid_med);
        jarak = findViewById(R.id.Seek_jarak);
        gopay = findViewById(R.id.bid_pay);
        initButton();
    }

    private void initButton() {
        prefs = getSharedPreferences(Constants.h("YmN0"), 0);
        boolean a = prefs.getBoolean("gosit", false);
        boolean b = prefs.getBoolean("gofood", false);
        boolean c = prefs.getBoolean("goride", false);
        boolean d = prefs.getBoolean("goshop", false);
        boolean e = prefs.getBoolean("gomart", false);
        boolean f = prefs.getBoolean("gosend", false);
        boolean g = prefs.getBoolean("gokil", false);
        boolean h = prefs.getBoolean("gomed", false);
        boolean i = prefs.getBoolean("gopay", false);
        setJarak();
        gosit.setChecked(a);
        gosit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    prefs.edit().putBoolean("gosit", true).apply();
                    return;
                }
                prefs.edit().putBoolean("gosit", false).apply();
            }
        });
        gofood.setChecked(b);
        gofood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    prefs.edit().putBoolean("gofood", true).apply();
                    return;
                }
                prefs.edit().putBoolean("gofood", false).apply();
            }
        });
        goride.setChecked(c);
        goride.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    prefs.edit().putBoolean("goride", true).apply();
                    return;
                }
                prefs.edit().putBoolean("goride", false).apply();
            }
        });
        goshop.setChecked(d);
        goshop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    prefs.edit().putBoolean("goshop", true).apply();
                    return;
                }
                prefs.edit().putBoolean("goshop", false).apply();
            }
        });
        gomart.setChecked(e);
        gomart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    prefs.edit().putBoolean("gomart", true).apply();
                    return;
                }
                prefs.edit().putBoolean("gomart", false).apply();
            }
        });
        gosend.setChecked(f);
        gosend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    prefs.edit().putBoolean("gosend", true).apply();
                    return;
                }
                prefs.edit().putBoolean("gosend", false).apply();
            }
        });
        gokil.setChecked(g);
        gokil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    prefs.edit().putBoolean("gokill", true).apply();
                    return;
                }
                prefs.edit().putBoolean("gokill", false).apply();
            }
        });
        gomed.setChecked(h);
        gomed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    prefs.edit().putBoolean("gomed", true).apply();
                    return;
                }
                prefs.edit().putBoolean("gomed", false).apply();
            }
        });
        gopay.setChecked(i);
        gopay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    prefs.edit().putBoolean("gopay", true).apply();
                    return;
                }
                prefs.edit().putBoolean("gopay", false).apply();
            }
        });
    }

    private void setJarak() {
        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar)findViewById(R.id.rangeSeekbar1);

// get min and max text view
        final TextView tvMin = (TextView)findViewById(R.id.textMin1);
        final TextView tvMax = (TextView)findViewById(R.id.textMax1);
        rangeSeekbar


                .setMinStartValue(prefs.getInt("jarakawal", 0))
                .setMaxStartValue(prefs.getInt("jarakakhir", 30))


                .setDataType(CrystalRangeSeekbar.DataType.INTEGER)
                .apply();;

// set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue));
                tvMax.setText(String.valueOf(maxValue));
            }
        });

// set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("BCT range=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
                int jawal = Integer.parseInt(String.valueOf(minValue));
                int jakir = Integer.parseInt(String.valueOf(maxValue));
                prefs = getSharedPreferences("bct", 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("jarakawal", jawal);
                editor.putInt("jarakakhir", jakir);
                editor.apply();
            }
        });
                //getAccess();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent aa = new Intent(Auto.this, MainActivity.class);
        aa.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(aa);
        finish();
    }

    void getAccess() {

        java.lang.Process proc;
        Toast.makeText(this, "Mencoba mendapatkan akses root", Toast.LENGTH_SHORT).show();

        try {
            File prefsDir = new File(getApplication().getApplicationInfo().dataDir, "shared_prefs");
            File prefsFile = new File(prefsDir, "bct.xml");

            proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "chmod 777 " + prefsFile.getAbsolutePath()});
            proc.waitFor();
            proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "chmod 777 " + prefsDir.getAbsolutePath()});
            proc.waitFor();
            proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "chmod 777 " + getApplication().getApplicationInfo().dataDir});
            proc.waitFor();
            Toast.makeText(this, "Permintaan root selesai, silahkan restart HP anda", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {

            Toast.makeText(this, "Permintaan root gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {

            Toast.makeText(this, "Permintaan root gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean haveRootAccess(){
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
            DataInputStream inputStream = new DataInputStream(process.getInputStream());

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            process.waitFor();
        } catch (IOException e) {

            return false;
        } catch (InterruptedException e) {

            return false;
        }
        return true;
    }
}
