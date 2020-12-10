package com.google.bct.ui;

import android.Manifest;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.tasks.Task;
import com.google.bct.R;
import com.google.bct.common.Constants;
import com.google.bct.init.AppSignatureHashHelper;
import com.google.bct.init.BaseActivity;
import com.google.bct.init.CheckInternetConnection;
import com.google.bct.model.Member;
import com.google.bct.utils.Prefs;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.io.PrintStream;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

public class main extends BaseActivity {
    private static final String TAG = "bct";
    ImageView imageView;
    TextView textView;
    int count = 0;
    DatabaseReference databaseReference;
    FirebaseDatabase db;
    SharedPreferences pref;
    private Button btnLogin, btnReset, btnDaftar;
    private static final int PERMISSIONS_REQUEST_ALLERT = 101;
    private static final int PERMISSIONS_REQUEST_LOCATION = 102;
    AppSignatureHashHelper signHelp;
    private FirebaseAuth auth;
    Prefs prefs;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2004;
    private TextView clikDaftar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        pref = getSharedPreferences(Constants.h("YmN0"), 0);
        btnLogin = (Button) findViewById(R.id.masuk);
        btnDaftar = findViewById(R.id.daftar);
        clikDaftar =findViewById(R.id.clickdaftar);
        atb();
        signHelp = new AppSignatureHashHelper(this);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Constants.h("bWVtYmVy"));
   //     goooo();
        Log.d(TAG, Constants.h("QkNUIEhhc2ggS2V5Og==") + signHelp.getAppSignatures().get(0));
        clickbtn();
        new CheckInternetConnection(this).checkConnection();
/*
        try {
            for (Signature byteArray : getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES).signatures) {
                MessageDigest instance = MessageDigest.getInstance("SHA");
                instance.update(byteArray.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(instance.digest(), 0));
            }
        } catch (PackageManager.NameNotFoundException unused) {
        } catch (NoSuchAlgorithmException unused2) {
        }
    */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_LOCATION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_LOCATION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_LOCATION);
        }

 //       String token_id = FirebaseInstanceId.getInstance().getToken();
 //       pref.edit().putString("fcm", token_id).apply();
        if (isPackageInstalled()){
            finish();
            System.exit(0);
        }
    //    getGojek();
    }

    private void permission() {
        if (checkCallingOrSelfPermission(Constants.h("YW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU=")) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Constants.h("YW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU=")}, 1);
        }
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Constants.h("YW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19GSU5FX0xPQ0FUSU9O")) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Constants.h("YW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19GSU5FX0xPQ0FUSU9O")}, 92);
        }
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Constants.h("YW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU=")) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Constants.h("YW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU=")}, 92);
        }
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Constants.h("YW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfRVhURVJOQUxfU1RPUkFHRQ==")) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Constants.h("YW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfRVhURVJOQUxfU1RPUkFHRQ==")}, 92);
        }
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Constants.h("YW5kcm9pZC5wZXJtaXNzaW9uLldSSVRFX0VYVEVSTkFMX1NUT1JBR0U=")) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Constants.h("YW5kcm9pZC5wZXJtaXNzaW9uLldSSVRFX0VYVEVSTkFMX1NUT1JBR0U=")}, 102);
        }
    }

    private void clickbtn() {
        dfrr();
        login();
        daftarNew();
    }

    private void daftarNew() {
        clikDaftar.setOnClickListener(new daftarNih());
    }

    private void dfrr(){
        btnDaftar.setOnClickListener(new goregist());
    }

    private void login(){
        btnLogin.setOnClickListener(new golog());
    }



    private class goregist implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            goreg();
        }
    }

    private void goema(){
        aa();
    }

    private void aa() {
        email();
    }

    private class golog implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            goema();
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
    private boolean isPackageGojek() {
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.equals(Constants.h("Y29tLmdvamVrLmRyaXZlci5iaWtl")))
                return true;
        }
        return false;
    }

    private void getGojek(){
        int gobis2ver = 0;

        if(isPackageGojek()){
            try {
                PackageInfo gobis2PackageInfo = getPackageManager().getPackageInfo(Constants.h("Y29tLmdvamVrLmRyaXZlci5iaWtl"), 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    gobis2ver = (int) gobis2PackageInfo.getLongVersionCode();
                }else{
                    gobis2ver = gobis2PackageInfo.versionCode;
                }
                prefs.setGobisVersion(gobis2ver);
            } catch (Exception err){

            }
        }
    }


    public void ganti(){
        ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String text = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        ClipData myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        MDToast.makeText(main.this, Constants.h("SHViIGFkbWlu"), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();

  //      Toast.makeText(this, "Hub admin", Toast.LENGTH_SHORT).show();
    }

    private void showAlert(String title, String msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

private void atb(){
    if (isPackageInstalled()){
        finish();
        System.exit(0);
    }
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



    private String imeiDev;
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EMAIL && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            SharedPreferences.Editor editor = pref.edit();

            editor.putString(Constants.h("ZW1haWxz"), accountName);

            editor.apply();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_LOCATION);
            } else {


            }
            final String email =pref.getString(Constants.h("ZW1haWxz"),"");
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= 28) {
                imeiDev = telephonyManager.getImei();
            } else {
                imeiDev = telephonyManager.getDeviceId();
            }

            SharedPreferences.Editor editorX = pref.edit();
            editorX.putString(Constants.h("aW1laQ=="), imeiDev);
            editorX.apply();


            if (TextUtils.isEmpty(email)) {
                MDToast.makeText(getApplicationContext(), Constants.h("RW50ZXIgZW1haWwgYWRkcmVzcyE="), Toast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
            }
    //        regis(email,password);
            auth.signInWithEmailAndPassword(email, imeiDev).addOnCompleteListener(this, new gologi());
        }
        if (requestCode == PERMISSIONS_REQUEST_ALLERT && Build.VERSION.SDK_INT >= 23) {
            Settings.canDrawOverlays(this);

        }
    }


    private class gologi implements com.google.android.gms.tasks.OnCompleteListener<com.google.firebase.auth.AuthResult> {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {

                Log.d(TAG, Constants.h("c2lnbkluV2l0aEVtYWlsOnN1Y2Nlc3M="));
                FirebaseUser user = auth.getCurrentUser();
                updateui(user);
            } else {
                Log.w(TAG, Constants.h("c2lnbkluV2l0aEVtYWlsOmZhaWx1cmU="), task.getException());
                updateui(null);

            }

            if (!task.isSuccessful()) {

                Log.w(TAG, Constants.h("c2lnbkluV2l0aEVtYWlsOmZhaWx1cmU="), task.getException());
                MDToast.makeText(main.this, Constants.h("TG9naW4gR2FnYWw="), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();

            }

        }
    }



    private void updateui(FirebaseUser user) {

   //         hideProgressBar();
            if (user != null) {
               databaseReference.child(user.getUid()).addValueEventListener(new gomember());

                return;
            }
            mai();

        }

    private class gomember implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Member member = dataSnapshot.getValue(Member.class);
            if(member == null){
                Log.e(Constants.h("bG9naW4="), Constants.h("VXNlciBkYXRhIGlzIG51bGwh"));
                goback();
                fin();
                MDToast.makeText(getApplicationContext(), Constants.h("VXNlciB0aWRhayBBZGEgICEgSHViIEFkbWlu") ,Toast.LENGTH_SHORT, MDToast.TYPE_INFO).show();
                return;
            }
            int activated = member.getActivated();
            String exp = member.getExp();
            String password = member.getPassword();
            member.getName();

            String name = member.getName();
            System.out.println(member.getName());
            pref.edit().putString(Constants.h("ZXhw"), exp).putString(Constants.h("YWRtaW5z"), password).putString("name", name).apply();
            String a2 = Constants.h("bG9naW4=");
            Log.e(a2, Constants.h("ZXhwICAgPSA=") + exp + Constants.h("ICBha3RpZiAgID0=") + activated);
            PrintStream printStream = System.out;
            printStream.println(Constants.h("ZXhwICAgPSA=") + exp + Constants.h("ICBha3RpZiAgID0=") + activated);
            pref.getString(Constants.h("ZXhwaXJlZA=="), exp);
            if (member.getActivated() != 0) {
                int activated2 = member.getActivated();
                Log.e("bct", "VXNlciBkYXRhIHRpZGFrIGFkYSE=djdjejrjf");
                Log.e("bct", "VXNlciBkdsgstGDtsjdasdsdsdtHdsgdsdjsdgska");
                Log.e("bct", "XVDGDNSJkdsdshSYd7ejddnsdejwew93e37js");
                Log.e("bct", "%&##(#^#*#)#&#(#&#(#)#*##)#_");
                startActivity(new Intent(main.this, MainActivity.class));
                fin();
                PrintStream printStream2 = System.out;
                printStream2.println(Constants.h("ZXhwICAgPSA=") + exp + Constants.h("ICBha3RpZiAgID0=") + activated2);
                Log.e("bct", "XVDGDNSJkdsdshSYd7ejddnsdejwew93e37js");
                Log.e("bct", "%&##(#^#*#)#&#(#&#(#)#*##)#_");
                Log.e("bct", "XVDGDNSJkdsdshSYd7eDGDdjsdhsdywedg");
                Log.e("bct", "$$$%W$%W$W%^W$WW$W$W$W%W$W%WRS%SS%");
                printStream2.println(Constants.h("ZXhwICAgPSA=") + exp + Constants.h("ICBha3RpZiAgID0=") + activated2);
       //         preflo();
                return;
            }
            int activated3 = member.getActivated();
            MDToast.makeText(
                    getApplicationContext(), Constants.h("IEh1YiBBZG1pbiAhIFVudHVrIG1lbmdha3RpZmthbg=="), Toast.LENGTH_SHORT, MDToast.TYPE_INFO).show();
            PrintStream printStream3 = System.out;
            printStream3.println(Constants.h("ZXhwICAgPSA=") + exp + Constants.h("ICBha3RpZiAgID0=") + activated3);
            StringBuilder sb = new StringBuilder();
            sb.append(Constants.h("VXNlciBkYXRhIHRpZGFrIGFkYSE="));
            sb.append(activated3);
            Log.e(Constants.h("bG9naW4="), sb.toString());

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(Constants.h("bG9naW4="), Constants.h("ZmFpbGVkIHRlc3QgcmVhZCB1c2Vy"), databaseError.toException());

        }
    }

    private void goooo(){
        goo();
    }

    private class daftarNih implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            goreg();
        }
    }
}
