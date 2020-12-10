package com.google.bct.ui;

import android.Manifest;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.tasks.Task;
import com.google.bct.R;
import com.google.bct.common.Constants;
import com.google.bct.init.CheckInternetConnection;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.List;

import androidx.annotation.NonNull;

public class Reg extends AppCompatActivity {

    TextView tvdaft;
    public FirebaseAuth mAuth;
    EditText edtnama;
    EditText edtpass;
    ProgressBar progressBar;
    private Button btndaft;
    private SharedPreferences prefs;
    private ProgressDialog progressDialog;
    private Activity activity = null;
    private static final int PERMISSIONS_REQUEST_ALLERT = 101;
    private static final int PERMISSIONS_REQUEST_LOCATION = 102;
    private KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        prefs = getSharedPreferences(Constants.h("YmN0"), 0);

        mAuth = FirebaseAuth.getInstance();
        edtnama = (EditText) findViewById(R.id.register_email_editText);
        edtpass = (EditText) findViewById(R.id.register_password_editText);
        btndaft = (Button) findViewById(R.id.register_button);
        tvdaft = (TextView) findViewById(R.id.register_login_link_textView);
        progressBar = (ProgressBar) findViewById(R.id.profile_progressBar);
        btndaft.setOnClickListener(new goregister());
        tvdaft.setOnClickListener(new tedaft());

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
        /*

        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 92);
        }
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission("android.permission.READ_PHONE_STATE") != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"android.permission.READ_PHONE_STATE"}, 92);
        }
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 92);
        }
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 102);
        }
  */
        if (isPackageInstalled()){
            finish();
            System.exit(0);
        }
    }

    private boolean isPackageInstalled() {
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.equals(Constants.h("Y29tLmtlcmFtaWRhcy5UaXRhbml1bUJhY2t1cA==")))
                return true;
        }
        return false;
    }


    private void goema(){
        aa();
    }

    private void aa() {
        email();
    }

    private static final int REQUEST_CODE_EMAIL = 1;

    public void email() {
        try {
            Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                    new String[] { GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE }, false, null, null, null, null);
            startActivityForResult(intent, REQUEST_CODE_EMAIL);
        } catch (ActivityNotFoundException e) {
            // TODO
        }
    }

    private class goregister implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            goema();
        }
    }

    private class tedaft implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EMAIL && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString(Constants.h("ZW1haWxz"), accountName);

            editor.apply();
            //          showProgressDialog("please wait",true,true);

            prog();
         //   simulateProgressUpdate();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_LOCATION);
            } else {


            }

            final String email =prefs.getString(Constants.h("ZW1haWxz"),"");
            String password = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

            String str;
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            if (Build.VERSION.SDK_INT >= 28) {
                str = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                str = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            SharedPreferences.Editor editorX = prefs.edit();
            editorX.putString(Constants.h("aW1laQ=="), password);
            editorX.putString(Constants.h("aW1laQ=="), str);
            editorX.apply();
  /*          if (Build.VERSION.SDK_INT >= 28) {
                str = telephonyManager.getImei();
            } else {
                str = telephonyManager.getDeviceId();
            }
            */
            if (TextUtils.isEmpty(email)) {
                MDToast.makeText(getApplicationContext(), Constants.h("RW50ZXIgZW1haWwgYWRkcmVzcyE="), Toast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
            }
            goo();
                //        regis(email,password);

        }
    }

    private void goo() {
        String emails = prefs.getString(Constants.h("ZW1haWxz"),"");
        String password = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if (TextUtils.isEmpty(emails)) {
            MDToast.makeText(getApplicationContext(), Constants.h("RW50ZXIgZW1haWwgYWRkcmVzcyE="), Toast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();

        } else if (TextUtils.isEmpty(password)) {
            MDToast.makeText(getApplicationContext(), Constants.h("RW50ZXIgcGFzc3dvcmQh"), Toast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();

        } else if (password.length() < 6) {
            MDToast.makeText(getApplicationContext(), Constants.h("UGFzc3dvcmQgdG9vIHNob3J0LCBlbnRlciBtaW5pbXVtIDYgY2hhcmFjdGVycyE="), Toast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();

  //          Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", 0).show();
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emails, password).addOnCompleteListener(this, new goReg());

    }

    public void prog(){
        hud = KProgressHUD.create(Reg.this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel(Constants.h("UGxlYXNlIHdhaXQ="))
                .setMaxProgress(100)
                .show();
        hud.setProgress(90);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hud.dismiss();
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
                    hud.setLabel(Constants.h("U2ViZW50YXIgTGFnaQ=="));
                }
                if (currentProgress < 100) {
                    handler.postDelayed(this, 50);
                }
            }
        }, 100);
    }


    private class goReg implements com.google.android.gms.tasks.OnCompleteListener<com.google.firebase.auth.AuthResult> {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            MDToast.makeText(Reg.this, Constants.h("Y3JlYXRlVXNlcldpdGhFbWFpbDpvbkNvbXBsZXRlOg=="), Toast.LENGTH_SHORT, MDToast.TYPE_INFO).show();
            if(!task.isSuccessful()){
                MDToast.makeText(Reg.this, Constants.h("QXV0aGVudGljYXRpb24gZmFpbGVkLg=="), Toast.LENGTH_SHORT, MDToast.TYPE_INFO).show();
                return;
            }
            startActivity(new Intent(Reg.this, LoginAct.class));
            finish();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, main.class));
        finish();
    }

}
