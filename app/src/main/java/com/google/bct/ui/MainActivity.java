package com.google.bct.ui;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import com.crashlytics.android.Crashlytics;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.bct.R;

import com.google.bct.common.Constants;
import com.google.bct.db.AddLocationHistoryDialog;
import com.google.bct.db.DAO_History;
import com.google.bct.init.AppBase;
import com.google.bct.init.AppUpdateDialog;
import com.google.bct.init.Brcast;
import com.google.bct.init.CheckInternetConnection;
import com.google.bct.init.ForceUpdateChecker;
import com.google.bct.model.User;
import com.google.bct.service.FloatingViewService;
import com.google.bct.service.HService;
import com.google.bct.service.LocationService;
import com.google.bct.service.Ping;
import com.google.bct.utils.Download;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements  ForceUpdateChecker.OnUpdateNeededListener, OnMapReadyCallback, LocationListener, NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {



    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2004;
    private LatLng loc_center;
    private static Bundle bundle = new Bundle();
    Marker marker = null;
    ArrayList<HashMap<String, String>> gofo;
    ArrayList<HashMap<String, String>> goso;
    private Context context;
    private MenuAdapter mMenuAdapter;
    private RecyclerView.ViewHolder mViewHolder;
    private ArrayList<String> mTitles = new ArrayList<>();
    private GoogleMap mMap;
    private View rootView;
    MainActivity activity;
    ImageView imageBekerja, gjkk;
    Switch acti;
    private UiSettings mUiSettings;
    CircularImageView fotoDriver;
    TextView namaDriver, percent;
    TextView driverWallet;
    TextView driveRating;
    TextView phoneDriver;
    TextView emailDriver;
    Button driverTopup;
    ArrayList<HashMap<String, String>> gofo5;
    ArrayList<HashMap<String, String>> gofo6;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private KProgressHUD hud;
    LocationManager locationManager;
    Location location;
    ImageView gjkDr;
    public boolean statusFragment, ordering = false;
    private static final int REQUEST_PERMISSION_LOCATION = 991;
    RoundedImageView imageDriver;
    FrameLayout switchWrapper;
    GeoJsonLayer layer;
    AppBase app;
    Location myLocation;
    Marker myMarker;
    boolean isOn = false;
    SharedPreferences pref, prefs;
    int maxRetry = 4;
    int maxRetry1 = 4;
    int maxRetry2 = 4;
    private ClipboardManager myClipboard;
    private ClipData myClip;
    public AlertDialog alertDialog;
    private ToggleButton fake;
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private TextView tvLat;
    private TextView tvLng, tvAcc;
    private ImageView imageZoom, imageZoomOut;
    private BroadcastReceiver myBroadcastReceiver;
    private DatabaseReference databaseReference;
    private int CountTask = 0;
    Brcast brcast;
    private AppUpdateDialog appUpdateDialog;
    public static final int download_progress = 0;
    private ProgressDialog prgDialog;
    private String uniquename;
    private ProgressDialog mProgressDialog;
    private LinearLayout layoutGojek, layoutGrab;
    private ImageView icbct;
    private ImageView icbct2;
    private ImageView grbfood, grbbike;
    private String updateUrl;
    public ArrayList<HashMap<String, String>> grab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = AppBase.obtainApp(this);
        pref = this.getSharedPreferences(Constants.h("YmN0"), 0);
        brcast = new Brcast();
        registerReceiver(brcast, new IntentFilter(Constants.h("Y29tLmdvb2dsZS5iY3QuTWFpbkFjdGl2aXR5")));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new CheckInternetConnection(this).checkConnection();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        auth = FirebaseAuth.getInstance();
        authListener = new authGo();
        Integer data[] = {++CountTask, null, null};
        new myAsy().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
        Log.d(TAG, Constants.h("VGFzayA9IA==") + (int) CountTask + Constants.h("VGFzayBRdWV1ZWQ="));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        buildGoogleApiClient();
        createLocationRequest();
        myBroadcastReceiver = new MyBroadcastReceiver();
        gofo5 = new ArrayList<>();
        gofo6 = new ArrayList<>();
        inBottom();
        fake = (ToggleButton) findViewById(R.id.toggleButton4);
        if (Build.VERSION.SDK_INT >= 23) {
            checkLocationPermission();
        }
        expired();
        getText();
        expi();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, Constants.h("UmVwbGFjZSB3aXRoIHlvdXIgb3duIGFjdGlvbg=="), Snackbar.LENGTH_LONG)
                        .setAction(Constants.h("QWN0aW9u"), null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        tvLat = (TextView) findViewById(R.id.tv_lat);
        tvLng = (TextView) findViewById(R.id.tv_lng);
        tvAcc = (TextView) findViewById(R.id.tv_acc);
        pref.edit().putString(Constants.h("c3RhZHJpdg=="), Constants.h("QXZhaWxhYmxl")).apply();
        tvName = headerView.findViewById(R.id.nameBctteam);
        tvEmail = headerView.findViewById(R.id.emailBctteam);
        tvExp = headerView.findViewById(R.id.expBct);
        imageZoom = findViewById(R.id.ic_zoom);
        imageZoomOut = findViewById(R.id.ic_zoomout);
        tvName.setText(pref.getString(Constants.h("bmFtZQ=="), ""));
        tvEmail.setText(pref.getString(Constants.h("ZW1haWxz"), ""));
        tvExp.setText(pref.getString(Constants.h("ZXhw"), ""));
        isCancelable = true;
        layoutGojek = findViewById(R.id.layout_gojek);
        layoutGrab = findViewById(R.id.layout_grab);
        //laygograb();
        initbct();
        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();
        agcm();
        getImei();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
            System.out.println("settings");
        } else {
            initializeView();
        }


    }

    private String imeiDev;
    private void getImei(){
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= 28) {
            imeiDev = telephonyManager.getImei();
        } else {
            imeiDev = telephonyManager.getDeviceId();
        }

        if(!imeiDev.equals(pref.getString("imei", ""))){
           signOut();
        }

    }

    private void initbct() {
        icbct = (ImageView) findViewById(R.id.ic_bct);
        icbct2 = (ImageView) findViewById(R.id.ic_bct2);
        icbct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogmode();
            }
        });
        icbct2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogmode();
            }
        });
    }
    Button   gojekmode, grabmode, okk;
    private void dialogmode() {
        dialog = new android.app.AlertDialog.Builder(MainActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_gojek_grab, null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        final android.app.AlertDialog d = dialog.show();
        gojekmode = dialogView.findViewById(R.id.btn_set_gojek);
        grabmode = dialogView.findViewById(R.id.btn_set_grab);
        okk = dialogView.findViewById(R.id.btn_okk);
        gojekmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.edit().putBoolean(Constants.h("Z29qZWtfbW9kZQ=="), true).apply();
   //             pref.edit().putBoolean("grab_mode", false).apply();


            }
        });
        okk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        grabmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.edit().putBoolean(Constants.h("Z3JhYl9tb2Rl"), true).apply();
            }
        });

        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Log.i("back key pressed", "Back key pressed");
                        d.dismiss();
                }
                return true;
            }
        });

        if(pref.getBoolean(Constants.h("Z29qZWtfbW9kZQ=="),false)){
            gojekmode.setBackgroundColor(Color.parseColor("#FF0000"));
            grabmode.setBackgroundColor(Color.parseColor("#313131"));
            pref.edit().putBoolean("grab_mode", false).apply();
            return;
        }
        pref.edit().putBoolean("grab_mode", true).apply();
        grabmode.setBackgroundColor(Color.parseColor("#FF0000"));
        gojekmode.setBackgroundColor(Color.parseColor("#313131"));

        if(pref.getBoolean("grab_mode", false)){
            grabmode.setBackgroundColor(Color.parseColor("#FF0000"));
            gojekmode.setBackgroundColor(Color.parseColor("#313131"));
            pref.edit().putBoolean(Constants.h("Z29qZWtfbW9kZQ=="), false).apply();
            return;
        }
        pref.edit().putBoolean(Constants.h("Z29qZWtfbW9kZQ=="), true).apply();
        gojekmode.setBackgroundColor(Color.parseColor("#FF0000"));
        grabmode.setBackgroundColor(Color.parseColor("#313131"));

    }

    private void laygograb() {
        laygojek();
        laygrab();
    }

    private void laygojek() {
        if (pref.getBoolean(Constants.h("Z29qZWtfbW9kZQ=="), false)) {
            layoutGojek.setVisibility(View.VISIBLE);
            layoutGrab.setVisibility(View.GONE);

            pref.edit().putBoolean("grab_mode", false).apply();
            return;
        }
        layoutGrab.setVisibility(View.VISIBLE);
        layoutGojek.setVisibility(View.GONE);

        pref.edit().putBoolean(Constants.h("Z29qZWtfbW9kZQ=="), false).apply();
    }

    private void laygrab() {
        if (pref.getBoolean("grab_mode", false)) {
            layoutGrab.setVisibility(View.VISIBLE);
            layoutGojek.setVisibility(View.GONE);

            pref.edit().putBoolean(Constants.h("Z29qZWtfbW9kZQ=="), false).apply();
            return;
        }
        layoutGojek.setVisibility(View.VISIBLE);
        layoutGrab.setVisibility(View.GONE);

        pref.edit().putBoolean("grab_mode", false).apply();
    }

    private void getmod() {

        if(pref.getBoolean(Constants.h("Z29qZWtfbW9kZQ=="), false)) {
            inputManual();
            return;
        }
        inputManualGrab();
    }
    private void getmoda() {

        if(pref.getBoolean("grab_mode", false)) {

            inputManualGrab();
            return;
        }
        inputManual();
    }

    public void inputManualGrab(){
        dialog = new android.app.AlertDialog.Builder(MainActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_token_grab, null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        etTok = (EditText)dialogView.findViewById(R.id.edtTokengrb);
        tokSer = dialogView.findViewById(R.id.btn_token_servergrb);
        but1 = (Button)dialogView.findViewById(R.id.btn_ok_tokengrb);
        but2 = (Button)dialogView.findViewById(R.id.btn_cancel_tokengrb);
        text1 = dialogView.findViewById(R.id.tv_ambilgrb);
        final android.app.AlertDialog d = dialog.show();
        tokSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hud = KProgressHUD.create(MainActivity.this)
                        .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                        .setLabel("Please wait")
                        .setMaxProgress(100)
                        .show();
                hud.setProgress(90);
                simulateProgressUpdate();
                String aa = pref.getString("tog", "");
                pref.edit().putString("tokengrab", aa).apply();
                MDToast.makeText(getApplicationContext(), "Token Berhasil di dapat", Toast.LENGTH_SHORT, MDToast.TYPE_INFO).show();
                d.dismiss();
            }
        });
        but1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String token = (etTok.getText().toString());
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("tokenfood", token);
                /** Saving the values stored in the shared preferences */
                editor.apply();
                MDToast.makeText(getApplicationContext(), "Input Token baru berhasil", Toast.LENGTH_SHORT, MDToast.TYPE_INFO).show();
                d.dismiss();
            }
        });
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // tokgjk();
            }
        });
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

    }

    boolean isCancelable;
    Button update;
    TextView tvupd;
    ImageView imgClose;
    @Override
    public void onUpdateNeeded(final String updateUrl) {
        dialog = new AlertDialog.Builder(MainActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.view_app_update, null);
        dialog.setCancelable(false);
        dialog.setView(dialogView);
        final android.app.AlertDialog d = dialog.show();
        tvupd = dialogView.findViewById(R.id.tvUpdateNow);
        imgClose = dialogView.findViewById(R.id.ivClose);
        tvupd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               redirectStore(updateUrl);

            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void dialdown(String urldown) {
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Update...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        // execute this when the downloader must be fired
        final Download downloadTask = new Download(MainActivity.this);
        downloadTask.execute(urldown);

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true); //cancel the task
            }
        });
    }




    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goNameUser()
    {
        String name = pref.getString(Constants.h("bmFtZQ=="), "");
        MDToast.makeText(this, Constants.h("SGksIA==") + name + "\n" + Constants.h("U2VsYW1hdCBCZWtlcmph"), Toast.LENGTH_LONG, MDToast.TYPE_SUCCESS).show();

    }

    public void bbb(){
        if(pref.getBoolean("data_driver", false)){
            aaa();

        }
    }


    public void aaa(){
        if (pref.getBoolean("acti", false)){

            Intent service5 = new Intent(MainActivity.this, Ping.class);
            {
                service5.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);

            }
            startService(service5);

        }

    }
    public void agcm(){
        if (pref.getBoolean("gcm", false)){
            Intent service5 = new Intent(MainActivity.this, HService.class);
            startService(service5);

        }

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    TextView tvName, tvEmail, tvExp;

    private void getText() {
        Log.e(Constants.h("YmN0"), "kepo lu...");
        getRetri();
    }

    private DatabaseReference ref;
    FirebaseDatabase mDatabase;

    private void getRetri() {
        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference(Constants.h("Z29qZWs="));
        ref.addChildEventListener(new goj());
        //     ref.child(getString(R.string.gojek)).addChildEventListener(new goj());
    }

    ImageView gof;

    private void inBottom() {
        rightFab();
        leftFab();
        expired();
        gjkDr = findViewById(R.id.ic_goride);
        gof = findViewById(R.id.ic_gofood);
        grbbike = findViewById(R.id.ic_grbbike);
        grbfood = findViewById(R.id.ic_grbfood);
    }

    com.github.clans.fab.FloatingActionButton fabSea, fabSho, fabLog, fabTra, fabHea, fabTok ,fabData;

    private void leftFab() {
        fabSea =(com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab1);
        fabSho =(com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab2);
        fabLog =(com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab3);
        fabHea = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.fab4);
        fabTra = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.fab5);
        fabTok =(com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabToken);
        fabData =(com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabDataDriver);
        expired();
        fabData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDataDriver();
            }
        });
        fabTok.setOnClickListener(new goToken());
        fabSea.setOnClickListener(new search());
        fabSho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (marker == null) {
                    MDToast.makeText(MainActivity.this, "Silahkan Pilih Lokasi", Toast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                    return;
                }

                hud = KProgressHUD.create(MainActivity.this)
                        .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                        .setLabel("Please wait")
                        .setMaxProgress(100)
                        .show();
                hud.setProgress(90);
                simulateProgressUpdate();
                loc_center = mMap.getCameraPosition().target;
                SharedPreferences.Editor e = pref.edit();
                e.putString("lat", Double.toString(loc_center.latitude));
                e.putString("lng", Double.toString(loc_center.longitude));
                e.apply();
                goshop();

            }
        });
        fabLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tokengjk();
            }
        });
        fabHea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isClickable()) {
                    heat();
                } else {
                    removeMarker();
                }
            }
        });
        fabTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trafficMap();
            }
        });
    }
    EditText fcm, access,iddriv;
    Button set2, cancel;
    private void goDataDriver() {

        dialog = new AlertDialog.Builder(MainActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_driver_data, null);
        dialog.setCancelable(false);
        dialog.setView(dialogView);
        //        dialog.setIcon(R.mipmap.ic_launcher);
        fcm = dialogView.findViewById(R.id.input_fcm);
        access= dialogView.findViewById(R.id.input_access);
        iddriv= dialogView.findViewById(R.id.input_id_driver);
        set2 = dialogView.findViewById(R.id.btn_ok_data);
        cancel = dialogView.findViewById(R.id.btn_cancel_data);
        final android.app.AlertDialog d = dialog.show();
        set2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etFcm = fcm.getText().toString();
                String etAcc = access.getText().toString();
                String etId = iddriv.getText().toString();
                if (etFcm.isEmpty() || etAcc.isEmpty() || etId.isEmpty()) {
                    MDToast.makeText(MainActivity.this, Constants.h("VGlkYWsgQm9sZWggS29zb25n"), Toast.LENGTH_SHORT, MDToast.TYPE_INFO).show();
                    return;
                }
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("fcmx", etFcm);
                edit.putString("tkbvx", etAcc);
                edit.putString("iddriverx", etId);
                edit.putBoolean("data_driver", true);
                edit.apply();
                aping();
       //         MDToast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                d.dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });


    }

    public void trafficMap() {
        if (mMap.isTrafficEnabled()) {
            mMap.setTrafficEnabled(false);
            pref.edit().putBoolean("trafic_maps", false).apply();
            Toast.makeText(MainActivity.this.getApplicationContext(), "Set Traffic Disabled", Toast.LENGTH_SHORT).show();
            return;
        }
        mMap.setTrafficEnabled(true);
        pref.edit().putBoolean("trafic_maps", true).apply();
        Toast.makeText(MainActivity.this.getApplicationContext(), "Set Traffic Enabled", Toast.LENGTH_SHORT).show();

    }


    private void goshop() {
        String lat;
        String lng;
        lat = pref.getString("lat", "0");
        String token = pref.getString("tokenfood", "c44afe01-8437-4f7f-8f43-e069a0dc6ea1");
        // Getting the longitude of the i-th location
        lng = pref.getString("lng", "0");
        OkHttpClient client = new OkHttpClient();
        Bundle extras = getIntent().getExtras();
        Request.Builder builder = new Request.Builder();


        Request request = builder
                .url(Constants.h("aHR0cHM6Ly9hcGkuZ29qZWthcGkuY29tL3N0b3Jla2VlcGVyL3YxL21lcmNoYW50cy9uZWFyYnk/cGFnZXNpemU9MjAmcGFnZW51bT0xJmxhdD0kbGF0Jmxvbmc9JGxuZyZjMmM9ZmFsc2U="))
                .header("Authorization", "Bearer " + token)
                .header("X-Location", app.latitude + "," + app.longitude)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string().toString();
                goso = new ArrayList<>();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObj = new JSONObject(myResponse);
                            //  JSONObject as = jsonObj.getJSONObject();
                            JSONArray aab = new JSONArray();
                            aab = jsonObj.getJSONArray("merchants");

                            for (int i = 0; i < aab.length(); i++) {
                                JSONObject gs = aab.getJSONObject(i);
                                String name = gs.getString("name");
                                String alamat = gs.getString("fullAddress");
                                String category = gs.getJSONArray("categories").toString();
                                String lat = gs.getString("latitude");
                                String lng = gs.getString("longitude");
                                String jarak = gs.getString("distance");
                                HashMap<String, String> gs1 = new HashMap<>();
                                gs1.put("shop_name", name);
                                gs1.put("shop_address", alamat);
                                gs1.put("shop_jarak", jarak + " KM");
                                gs1.put("shop_latLong", lat+","+ lng);
                                gs1.put("shop_categories", category);
                                gofo.add(gs1);

                            }
                            listgoshop();
                            MDToast.makeText(MainActivity.this, "GO-SHOP", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();


                        } catch (JSONException e) {
                            Log.e("dash", "Error processing JSON", e);
                            //       Toast.makeText(MainActivity.this, "gf error" + myResponse, Toast.LENGTH_SHORT).show();
                            MDToast.makeText(MainActivity.this, "GO-SHOP error" + myResponse, MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();

                            tokengjk();
                        }
                    }
                });

            }
        });


    }

    public void listgoshop() {
        MDToast.makeText(MainActivity.this, "GO-SHOP", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();

        hud.dismiss();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.gslist, null);
        builder.setView(convertView);
        builder.setTitle("GO-SHOP ");
        ListView lv = (ListView) convertView.findViewById(R.id.listView);
        ListAdapter adapter = new SimpleAdapter(
                this, goso,
                R.layout.list_shop, new String[]{"shop_name", "shop_address",
                "shop_latLong", "shop_jarak", "shop_categories",}, new int[]{R.id.namags,
                R.id.alamatgs, R.id.jarakgs, R.id.lokasigs, R.id.categorygs});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long duration) {
                TextView textNama = (TextView) view.findViewById(R.id.namags);
                TextView textViewIata = (TextView) view.findViewById(R.id.lokasigs);
                String loc = (String) textViewIata.getText();
                String nam = (String) textNama.getText();
                SharedPreferences.Editor editor = pref.edit();
                // Storing the latitude for the i-th location
                String[] latLng1 = loc.split(",");
                double latitude1 = Double.parseDouble(latLng1[0]);
                double longitude1 = Double.parseDouble(latLng1[1]);
                editor.putString("lat", Double.toString(latitude1));
                editor.putString("lng", Double.toString(longitude1));
                editor.apply();
                LatLng ltg = new LatLng(latitude1, longitude1);
                placeMarker(ltg);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ltg, 17));
                MDToast.makeText(MainActivity.this, "GO-Shop" + "\n" + nam + "\n" + loc, MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();

                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }


    public void heat() {

        String lat;
        String lng;
        String token;

        String loc = pref.getString("loka", "0,0");
        String[] latLng1 = loc.split(",");
        lat = (latLng1[0]);
        lng = (latLng1[1]);
        token = pref.getString("tokenfood", "c44afe01-8437-4f7f-8f43-e069a0dc6ea1");
        String link = app.b;
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();


        Request request = builder
                .url(link + lat + "&longitude=" + lng + "&radius=50")
                .header("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string().toString();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObj = new JSONObject(myResponse);
                            System.out.println("test " + myResponse);
                            layer = new GeoJsonLayer(mMap, new JSONObject(myResponse));

                            JSONArray a = jsonObj.getJSONArray("features");
                            System.out.println("test1  " + a);

                            for (int i = 0; i < a.length(); i++) {
                                JSONObject gf = a.getJSONObject(i);
                                JSONObject b = gf.getJSONObject("geometry");
                                JSONObject c = gf.getJSONObject("properties");
                                String s = c.getString("color");
                                String d = gf.getString("geometry");


                                layer = new GeoJsonLayer(mMap, new JSONObject(d));

                                GeoJsonPolygonStyle style = layer.getDefaultPolygonStyle();
                                style.setFillColor(Color.parseColor(s));
                                style.setStrokeColor(Color.parseColor(s));
                                layer.addLayerToMap();
                                layer.setOnFeatureClickListener(new GeoJsonLayer.OnFeatureClickListener() {

                                    @Override
                                    public void onFeatureClick(Feature feature) {
                                        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {


                                            @Override
                                            public void onPolylineClick(Polyline polyline) {
                                                //TODO show LineString properties
                                                Object b = polyline.getPoints();
                                                String ss = String.valueOf(b);

                                            }
                                        });

                                        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {

                                            @Override
                                            public void onPolygonClick(Polygon polygon) {
                                                //TODO show Polygon properties
                                                Object a = polygon.getPoints();
                                                String ss = String.valueOf(a);

                                            }
                                        });
                                    }
                                });
                            }


                        } catch (JSONException e) {

                            Log.e("pickup", "Error processing JSON", e);
                            //           Toast.makeText(MainActivity.this, " heat  :" + e, Toast.LENGTH_SHORT).show();
                            MDToast.makeText(MainActivity.this, " heat  :" + e, MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();

                        }
                    }
                });

            }
        });

    }


    android.app.AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText et1, et2;
    TextView text1;
    Button but1, but2, exit;

    public void tokengjk() {
        dialog = new android.app.AlertDialog.Builder(MainActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.login_cust, null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        dialog.setIcon(R.mipmap.ic_launcher);
        et1 = (EditText) dialogView.findViewById(R.id.et1);
        et2 = (EditText) dialogView.findViewById(R.id.et2);
        but1 = (Button) dialogView.findViewById(R.id.but1);
        but2 = (Button) dialogView.findViewById(R.id.but2);
        text1 = (TextView) dialogView.findViewById(R.id.text1);
        exit = (Button) dialogView.findViewById(R.id.exit);
        text1.setText(pref.getString("token", "5cada888-4a80-442a-8bd2-a37f693edf8f"));
        final android.app.AlertDialog d = dialog.show();
        but1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //     showProgressDialog("Waiting",true,true);
                hud = KProgressHUD.create(MainActivity.this)
                        .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                        .setLabel("Please wait")
                        .setMaxProgress(100)
                        .show();
                hud.setProgress(90);
                simulateProgressUpdate();
                String token = (et1.getText().toString());
                String token1 = "+62" + token;
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("nomer", token1);
                editor.apply();
                a();
            }
        });
        but2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //            showProgressDialog("Waiting",true,true);
                hud = KProgressHUD.create(MainActivity.this)
                        .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                        .setLabel("Please wait")
                        .setMaxProgress(100)
                        .show();
                hud.setProgress(90);
                simulateProgressUpdate();
                String token = (et2.getText().toString());
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("otp", token);
                // Storing the longitude for the i-th location
                // Storing the count of locations or marker count


                /** Saving the values stored in the shared preferences */
                editor.apply();
                gjkotp();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                d.dismiss();
            }
        });

    }

    private void a() {

        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("client_id", "gojek:consumer:app");
            jsonObject.put("client_secret", "pGwQ7oi8bKqqwvid09UrjqpkMEHklb");
            jsonObject.put("country_code","+62");
            jsonObject.put("phone_number", pref.getString("nomer", ""));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        // put your json here
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = builder
                .url("https://goid.gojekapi.com/goid/login/request")
                .header("User-Agent", "okhttp/3.12.1")
                .header("D1", "43:A3:BB:63:EB:57:A6:45:AE:08:35:E3:D2:FB:F2:E5:AF:6C:19:50:41:17:73:59:01:13:CC:81:A8:03:5C:DA")
                .header("X-AppVersion", "3.51.4")
                .header("X-User-Type", "customer")
                .header("X-AppId", "com.gojek.app")
                .header("Authorization", "Bearer")
                .header("X-Platform", "Android")
                .header("X-PhoneMake", Build.MANUFACTURER)
         //       .header("X-UniqueId", aa)
         //       .header("X-PhoneModel", aa)
                .header("Accept", "application/json")
                .header("X-DeviceOs", "Android,9")
                .header("accept", "application/json")
                .header("Accept-Language", "id")
                .header("X-User-Locale", "id_ID")
         //       .header("X-DeviceToken", prefs.getString("fcm", ""))
         //       .header("X-PushTokenType", "FCM")
                //       .header("X-Location", "0,0")
                //       .header("X-Location-Accuracy", "3")
               .header("X-PhoneModel", Build.MODEL)
             .header("X-UniqueId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID))

                .post(body)
                .build();

        System.out.println("Test 3 : " + request);


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string().toString();
                hud.dismiss();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject jsonObj = new JSONObject(myResponse);
                            System.out.println("Test 4 : " + myResponse);
                            String a = jsonObj.getJSONObject("data").getString("otp_token");
                            pref.edit().putString("otptk", a).apply();
                            //         Toast.makeText(MainActivity.this, "Kode Otp Berhasil dikirim", Toast.LENGTH_SHORT).show();
                            MDToast.makeText(MainActivity.this, "Kode Otp Berhasil dikirim", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();

                        } catch (JSONException e) {
                            Log.e("tq", "Error processing JSON", e);
                            System.out.println("error login" + e);
                            //          Toast.makeText(MainActivity.this, myResponse, Toast.LENGTH_SHORT).show();
                            MDToast.makeText(MainActivity.this, myResponse, MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();

                        }
                    }
                });

            }
        });


    }


    private void gjkotp() {
        JSONObject object = new JSONObject();
        try {
            object.put("client_id", "gojek:consumer:app");
            object.put("client_secret", "pGwQ7oi8bKqqwvid09UrjqpkMEHklb");
            object.put("grant_type", "otp");
            JSONArray array = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("otp_token", pref.getString("otptk", ""));
            obj.put("otp", pref.getString("otp", ""));
            object.put("scopes",array);
            object.put("data", obj);
            //   array.put(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        // put your json here
        RequestBody Body = RequestBody.create(JSON, object.toString());
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url("https://goid.gojekapi.com/goid/token")
                .header("User-Agent", "okhttp/3.12.1")
                .header("D1", "43:A3:BB:63:EB:57:A6:45:AE:08:35:E3:D2:FB:F2:E5:AF:6C:19:50:41:17:73:59:01:13:CC:81:A8:03:5C:DA")
                .header("X-AppVersion", "3.51.4")
                .header("X-User-Type", "customer")
                .header("X-AppId", "com.gojek.app")
                .header("Authorization", "Bearer")
                .header("X-Platform", "Android")
                .header("X-PhoneMake", Build.MANUFACTURER)

                .header("X-PhoneModel", Build.MODEL)
                .header("X-UniqueId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID))

                .header("Accept", "application/json")
                .header("X-DeviceOs", "Android,9")
                .header("accept", "application/json")
                .header("Accept-Language", "id")
                .header("X-User-Locale", "id_ID")
   //             .header("X-Location", "0,0")
   //             .header("X-Location-Accuracy", "3")
                .post(Body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string().toString();
                hud.dismiss();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObj = new JSONObject(myResponse);
                            System.out.println("Response : " + myResponse);
                            String a = jsonObj.getString("access_token");
                            pref.edit().putString("tokenfood", a).apply();
                            pref.edit().putBoolean("lgngjk", true).apply();
                            MDToast.makeText(MainActivity.this, "Berhasil Masuk !", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                        } catch (JSONException e) {
                            Log.e("log", "Error processing JSON", e);
                            System.out.println("ResponError : " + myResponse);
                            MDToast.makeText(MainActivity.this, myResponse, MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();

                        }
                    }
                });

            }
        });

    }

    com.github.clans.fab.FloatingActionButton fabHisto;
    com.github.clans.fab.FloatingActionButton fabDelLoc;
    com.github.clans.fab.FloatingActionButton fabPickPoi;
    com.github.clans.fab.FloatingActionButton fabSett;
    com.github.clans.fab.FloatingActionButton fabGoto;
    private void rightFab() {
        fabHisto =(com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabHisto);
        fabDelLoc = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.fabDelLoc);
        fabGoto = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.fabGoto);
        fabPickPoi = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.fabPickup);
        fabSett = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.fabSetting);
        fabSett.setOnClickListener(new settings());
        fabHisto.setOnClickListener(new cekHisto());
        fabDelLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeMarker();
            }
        });
        fabPickPoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hud = KProgressHUD.create(MainActivity.this)
                        .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                        .setLabel("Please wait")
                        .setMaxProgress(100)
                        .show();
                hud.setProgress(90);
                simulateProgressUpdate();

                loc_center = mMap.getCameraPosition().target;
                SharedPreferences.Editor e = pref.edit();
                e.putString("lat", Double.toString(loc_center.latitude));
                e.putString("lng", Double.toString(loc_center.longitude));
                e.apply();
                new gjkpox().execute();
            }
        });
        fabGoto.setOnClickListener(new goCoor());
    }




    private class gjkpox extends AsyncTask<Void, Void, String> {
        final HashMap<String, Integer> driver = new HashMap<>();
        String lat = "";
        String lng = "";
        String token = "";
        // Invoked by execute() method of this object

        @Override
        protected String doInBackground(Void... args) {
            lat = pref.getString("lat", "0");
            token = pref.getString("tokenfood", "5cada888-4a80-442a-8bd2-a37f693edf8f");
            // Getting the longitude of the i-th location
            lng = pref.getString("lng", "0");
            OkHttpClient client = new OkHttpClient();
            Bundle extras = getIntent().getExtras();
            Request.Builder builder = new Request.Builder();


            Request request = builder
                    .url("https://api.gojekapi.com/pegasus/v1/pickup_area?latitude=" + lat + "&longitude=" + lng + "&service_type=1")
                    .header("Authorization", "Bearer " + token)
                    .build();
            System.out.println("Test 3 : " + request);

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String myResponse = response.body().string().toString();
                    hud.dismiss();
                    gofo = new ArrayList<>();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObj = new JSONObject(myResponse);
                                JSONArray a = jsonObj.getJSONArray("pickup_points");
                                for (int i = 0; i < a.length(); i++) {
                                    JSONObject gfx = a.getJSONObject(i);
                                    String lat = String.valueOf(gfx.getJSONObject("coordinates").getDouble("latitude"));
                                    String lon = String.valueOf(gfx.getJSONObject("coordinates").getDouble("longitude"));
                                    String latlon = lat + "," + lon;
                                    String name = gfx.getString("name");
                                    String alamat = gfx.getString("address");
                                    HashMap<String, String> gf = new HashMap<>();
                                    gf.put("address", alamat);
                                    gf.put("name", name);
                                    gf.put("latLong", latlon);
                                    gofo.add(gf);
                                }
                                poilist();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                hud.dismiss();
                                //                      Toast.makeText(MainActivity.this,  "maaf tidak ditemukan titik pickup point", Toast.LENGTH_SHORT).show();
                                MDToast.makeText(MainActivity.this, "maaf tidak ditemukan titik pickup point", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();

                            }

                        }


                    });

                }

            });
            return builder.toString();

        }

    }

    public void poilist() {

        hud.dismiss();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.list2, null);
        builder.setView(convertView);
        builder.setTitle("Pickup Point");
        ListView lv = (ListView) convertView.findViewById(R.id.lv);
        ListAdapter adapter = new SimpleAdapter(
                MainActivity.this, gofo,
                R.layout.list_poi, new String[]{"address", "name", "latLong"
        }, new int[]{R.id.name,
                R.id.alamat, R.id.lokasi});

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long duration) {
                TextView textViewIata = (TextView) view.findViewById(R.id.lokasi);
                String loc = (String) textViewIata.getText();
                TextView textName = (TextView) view.findViewById(R.id.name);
                String aa = (String) textName.getText();
                SharedPreferences.Editor editor = pref.edit();

                // Storing the latitude for the i-th location

                String[] latLng1 = loc.split(",");
                double latitude1 = Double.parseDouble(latLng1[0]);
                double longitude1 = Double.parseDouble(latLng1[1]);


                // Storing the latitude for the i-th location
                editor.putString("lat", Double.toString(latitude1));

                // Storing the longitude for the i-th location
                editor.putString("lng", Double.toString(longitude1));

                editor.apply();

                LatLng ltg = new LatLng(latitude1, longitude1);
                placeMarker(ltg);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ltg, 15));
                MDToast.makeText(MainActivity.this, "PICKUP POINT" + "\n" + aa + "\n" + loc, MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();

                alertDialog.dismiss();


            }
        });
        alertDialog = builder.create();
        alertDialog.show();

    }

    private void removeMarker() {
        mMap.clear();
    }

    public void grbDrv(View v) {
        if (marker == null) {
            MDToast.makeText(MainActivity.this, "Silahkan Pilih Lokasi", Toast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
            return;
        }

        hud = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(100)
                .show();
        hud.setProgress(90);
        simulateProgressUpdate();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
            }
        }, 200);

        loc_center = mMap.getCameraPosition().target;
        SharedPreferences.Editor e = pref.edit();
        e.putString("lat", Double.toString(loc_center.latitude));
        e.putString("lng", Double.toString(loc_center.longitude));
        e.apply();
        new grb1().execute();

    }


    public void gjkDriv(View v) {
        if (marker == null) {
            MDToast.makeText(MainActivity.this, "Silahkan Pilih Lokasi", Toast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
            return;
        }

        hud = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(100)
                .show();
        hud.setProgress(90);
        simulateProgressUpdate();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
            }
        }, 200);

        loc_center = mMap.getCameraPosition().target;
        SharedPreferences.Editor e = pref.edit();
        e.putString("lat", Double.toString(loc_center.latitude));
        e.putString("lng", Double.toString(loc_center.longitude));
        e.apply();
        new gjk1().execute();
     //   new grbbik().execute();
    }

    private class grb1 extends AsyncTask<Void, Void, String> {
        String token = "";
        final HashMap<String, Integer> driver = new HashMap<>();
        String lat = "";
        String lng = "";
        // Invoked by execute() method of this object

        @Override
        protected String doInBackground(Void... args) {
            lat = pref.getString("lat", "-6.121435");
            token = pref.getString("tokengrab", "c44afe01-8437-4f7f-8f43-e069a0dc6ea1");
            // Getting the longitude of the i-th location
            lng = pref.getString("lng", "106.774124");
            OkHttpClient client = new OkHttpClient();
            Bundle extras = getIntent().getExtras();
            Request.Builder builder = new Request.Builder();

            Request request = builder
                    .url("https://p.grabtaxi.com/api/passenger/v2/poi/predict?reference=" + lat + "," + lng)
                    .header("x-mts-ssid", token)
                    .header("X-Location", lat + "," + lng)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String myResponse = response.body().string().toString();
                    final String b;

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jSONObject = new JSONObject(myResponse);
                                if (myResponse.contains("extra")) {
                                    JSONArray jSONArray = jSONObject.getJSONArray("extra");
                                    int i = 0;
                                    while (i < jSONArray.length()) {
                                        JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                                        JSONObject jSONObject3 = jSONObject2.getJSONObject("latlng");
                                        JSONObject jSONObject4 = jSONObject2.getJSONObject("address");
                                        String str = lat + "," + lng;
                                        JSONArray jSONArray2 = jSONArray;
                                        new LatLng(Double.parseDouble(jSONObject3.getString("latitude")), Double.parseDouble(jSONObject3.getString("longitude")));
                                        String string = jSONObject4.getString("name");
                                        String string2 = jSONObject4.getString("combined_address");
                                        try {
                                            String string3 = jSONObject2.getString("pick_up_count");
                                            HashMap hashMap = new HashMap();
                                            hashMap.put("address", string2);
                                            hashMap.put("name", string);
                                            hashMap.put("latLong", str);
                                            hashMap.put("pickup", string3);
                                            grab.add(hashMap);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            HashMap hashMap2 = new HashMap();
                                            hashMap2.put("address", string2);
                                            hashMap2.put("name", string);
                                            hashMap2.put("latLong", str);
                                            hashMap2.put("pickup", "not Found");
                                            grab.add(hashMap2);
                                        }
                                        i++;
                                        jSONArray = jSONArray2;
                                    }
                                    listgrab();
                                }
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                                //         Toast.makeText(MapsActivity.this, "Lokasi Preferensi order tidak tersedia", 0).show();
                                hud.dismiss();
                            }
                        }


                    });


                }
            });

            return builder.toString();
        }
    }

    private void listgrab() {


            hud.dismiss();
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.list2, null);
            builder.setView(convertView);
            builder.setTitle("Pickup Point");
            ListView lv = (ListView) convertView.findViewById(R.id.lv);
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, gofo,
                    R.layout.list_poi, new String[]{"address", "name", "latLong"
            }, new int[]{R.id.name,
                    R.id.alamat, R.id.lokasi});

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long duration) {
                    TextView textViewIata = (TextView) view.findViewById(R.id.lokasi);
                    String loc = (String) textViewIata.getText();
                    TextView textName = (TextView) view.findViewById(R.id.name);
                    String aa = (String) textName.getText();
                    SharedPreferences.Editor editor = pref.edit();

                    // Storing the latitude for the i-th location

                    String[] latLng1 = loc.split(",");
                    double latitude1 = Double.parseDouble(latLng1[0]);
                    double longitude1 = Double.parseDouble(latLng1[1]);


                    // Storing the latitude for the i-th location
                    editor.putString("lat", Double.toString(latitude1));

                    // Storing the longitude for the i-th location
                    editor.putString("lng", Double.toString(longitude1));

                    editor.apply();

                    LatLng ltg = new LatLng(latitude1, longitude1);
                    placeMarker(ltg);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ltg, 15));
                    MDToast.makeText(MainActivity.this, "PICKUP POINT" + "\n" + aa + "\n" + loc, MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();

                    alertDialog.dismiss();


                }
            });
            alertDialog = builder.create();
            alertDialog.show();


    }

    private class grbbik extends AsyncTask<Void, Void, String> {
        String token = "";
        final HashMap<String, Integer> driver = new HashMap<>();
        String lat = "";
        String lng = "";
        String string4 = pref.getString("gbr", "71");

        // Invoked by execute() method of this object

        @Override
        protected String doInBackground(Void... args) {
            lat = pref.getString("lat", "-6.121435");
            token = pref.getString("tokengrab", "c44afe01-8437-4f7f-8f43-e069a0dc6ea1");
            // Getting the longitude of the i-th location
            lng = pref.getString("lng", "106.774124");
            OkHttpClient client = new OkHttpClient();
            Bundle extras = getIntent().getExtras();
            Request.Builder builder = new Request.Builder();

            Request request = builder
                    .url("https://p.grabtaxi.com/api/passenger/v3/services/nearby?latitude=" +   lat + "&longitude=" + lng + "&serviceIDs=" + string4)
                    .header("x-mts-ssid", token)
                    .header("X-Location", lat + "," + lng)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String myResponse = response.body().string().toString();
                    final String b;

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                JSONArray jsonArray = new JSONArray(myResponse);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                                    String string = jsonObj.getString("latitude");
                                    String string2 = jsonObj.getString("longitude");
                                    String latlong = string + "," + string2;

                                    Integer jml = (Integer) (driver).get(latlong);
                                    if (jml == null) {
                                        driver.put(latlong, Integer.valueOf(1));
                                    } else {
                                        driver.put(latlong, Integer.valueOf(jml).intValue() + 1);
                                    }
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                            Iterator<Map.Entry<String, Integer>> it = driver.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry<String, Integer> pair = it.next();
                                String[] latLng = pair.getKey().split(",");
                                double latitude = Double.parseDouble(latLng[0]);
                                double longitude = Double.parseDouble(latLng[1]);
                                LatLng point = new LatLng(latitude, longitude);
                                Integer b = pair.getValue();
                                String a = pair.getKey();
                                System.out.println("test6" + a + ",  " + b);
                                if (b.intValue() > 1) {
                                    mMap.addMarker(new MarkerOptions()
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_avtar))
                                            .title(String.valueOf(b) + "  Drivers"+ " Mencurigakan")
                                            .snippet(a)
                                            .position(point));
                                    return;
                                }
                                mMap.addMarker(new MarkerOptions()
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.m_grabbike))
                                        .title(String.valueOf(b) + "  Drivers")
                                        .snippet(a)
                                        .position(point));

                            }

                        }

                    });

                }

            });

            return builder.toString();

        }
    }


    public void grbfood(View v) {
        if (marker == null) {
            MDToast.makeText(MainActivity.this, "Silahkan Pilih Lokasi", Toast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
            return;
        }

        hud = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(100)
                .show();
        hud.setProgress(90);
        simulateProgressUpdate();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
            }
        }, 200);

        loc_center = mMap.getCameraPosition().target;
        SharedPreferences.Editor e = pref.edit();
        e.putString("lat", Double.toString(loc_center.latitude));
        e.putString("lng", Double.toString(loc_center.longitude));
        e.apply();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
                grbfo();

            }
        }, 200);


    }

    private void grbfo() {
        String lat;
        String lng;
        lat = pref.getString("lat", "0");
        String token = pref.getString("tokenfood", "c44afe01-8437-4f7f-8f43-e069a0dc6ea1");
        // Getting the longitude of the i-th location
        lng = pref.getString("lng", "0");
        OkHttpClient client = new OkHttpClient();
        Bundle extras = getIntent().getExtras();
        Request.Builder builder = new Request.Builder();


        Request request = builder
                .url("https://api.gojekapi.com/gofood/consumer/v3/restaurants")
                .header("Authorization", "Bearer " + token)
                .header("X-Location", lat + "," + lng)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string().toString();
                gofo = new ArrayList<>();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObj = new JSONObject(myResponse);
                            JSONObject as = jsonObj.getJSONObject("data");
                            JSONArray a = as.getJSONArray("cards");

                            for (int i = 0; i < a.length(); i++) {
                                JSONObject gf = a.getJSONObject(i);
                                String latlong = gf.getJSONObject("content").getString("location"); //

                                String name = gf.getJSONObject("content").getJSONObject("brand").getString("name");
                                String alamat = gf.getJSONObject("content").getString("address");
                                HashMap<String, String> gf1 = new HashMap<>();
                                gf1.put("address", alamat);
                                gf1.put("name", name);
                                gf1.put("latLong", latlong);
                                gofo.add(gf1);

                            }
                            listgrbfood();
                            //      Toast.makeText(MainActivity.this, "gf1", Toast.LENGTH_SHORT).show();
                            MDToast.makeText(MainActivity.this, "GRB-FOOD", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();


                        } catch (JSONException e) {
                            Log.e("dash", "Error processing JSON", e);
                            //       Toast.makeText(MainActivity.this, "gf error" + myResponse, Toast.LENGTH_SHORT).show();
                            MDToast.makeText(MainActivity.this, "GO-FOOD error" + myResponse, MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();

                            tokengjk();
                        }
                    }
                });

            }
        });


    }

    public void listgrbfood() {
        //       Toast.makeText(MainActivity.this, "gf2", Toast.LENGTH_SHORT).show();
        hud.dismiss();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.list2, null);
        builder.setView(convertView);
        builder.setTitle("GRB-FOOD");
        builder.setIcon(R.drawable.ic_restaurant_menu_red_24dp);
        ListView lv = (ListView) convertView.findViewById(R.id.lv);
        ListAdapter adapter = new SimpleAdapter(
                this, gofo,
                R.layout.gflist, new String[]{"name", "address",
                "latLong"}, new int[]{R.id.name,
                R.id.alamat, R.id.lokasi});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long duration) {
                TextView textViewIata = (TextView) view.findViewById(R.id.lokasi);
                String loc = (String) textViewIata.getText();
                TextView textName = (TextView) view.findViewById(R.id.name);
                String nam = (String) textName.getText();
                SharedPreferences.Editor editor = pref.edit();
                // Storing the latitude for the i-th location
                String[] latLng1 = loc.split(",");
                double latitude1 = Double.parseDouble(latLng1[0]);
                double longitude1 = Double.parseDouble(latLng1[1]);
                editor.putString("lat", Double.toString(latitude1));
                editor.putString("lng", Double.toString(longitude1));
                editor.apply();
                LatLng ltg = new LatLng(latitude1, longitude1);
                placeMarker(ltg);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ltg, 17));
                MDToast.makeText(MainActivity.this, "GO-FOOD" + "\n" + nam + "\n" + loc, MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();

                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }




    private class gjk1 extends AsyncTask<Void, Void, String> {
        String token = "";
        final HashMap<String, Integer> driver = new HashMap<>();
        String lat = "";
        String lng = "";
        // Invoked by execute() method of this object

        @Override
        protected String doInBackground(Void... args) {
            lat = pref.getString("lat", "-6.121435");
            token = pref.getString("tokenfood", "c44afe01-8437-4f7f-8f43-e069a0dc6ea1");
            // Getting the longitude of the i-th location
            lng = pref.getString("lng", "106.774124");
            OkHttpClient client = new OkHttpClient();
            Bundle extras = getIntent().getExtras();
            Request.Builder builder = new Request.Builder();

            Request request = builder
                    .url("https://api.gojekapi.com/gojek/service_type/1/drivers/nearby?location=" + lat + "," + lng)
                    .header("Authorization", "Bearer " + token)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String myResponse = response.body().string().toString();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                JSONArray jsonArray = new JSONArray(myResponse);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                                    String latlong = jsonObj.getString("driverLatLong");
                                    Integer jml = (Integer) (driver).get(latlong);
                                    if (jml == null) {
                                        driver.put(latlong, Integer.valueOf(1));
                                    } else {
                                        driver.put(latlong, Integer.valueOf(jml).intValue() + 1);
                                    }
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                            Iterator<Map.Entry<String, Integer>> it = driver.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry<String, Integer> pair = it.next();
                                String[] latLng = pair.getKey().split(",");
                                double latitude = Double.parseDouble(latLng[0]);
                                double longitude = Double.parseDouble(latLng[1]);
                                LatLng point = new LatLng(latitude, longitude);
                                Integer b = pair.getValue();
                                String a = pair.getKey();
                                System.out.println("test6" + a + ",  " + b);
                                if (b.intValue() > 1) {
                                    mMap.addMarker(new MarkerOptions()
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_avtar))
                                            .title(String.valueOf(b) + "  Drivers"+ " Mencurigakan")
                                            .snippet(a)
                                            .position(point));
                                    return;
                                }
                                mMap.addMarker(new MarkerOptions()
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.m_gojek))
                                        .title(String.valueOf(b) + "  Drivers")
                                        .snippet(a)
                                        .position(point));

                            }

                        }

                    });

                }

            });

            return builder.toString();

        }

    }

    public void gfood(View v) {
        if (marker == null) {
            Toast.makeText(this, "Marker Not Place", Toast.LENGTH_LONG).show();
            return;
        }

        hud = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(100)
                .show();
        hud.setProgress(90);
        simulateProgressUpdate();
        loc_center = mMap.getCameraPosition().target;
        SharedPreferences.Editor e = pref.edit();
        e.putString("lat", Double.toString(loc_center.latitude));
        e.putString("lng", Double.toString(loc_center.longitude));
        e.apply();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
                gofood();

            }
        }, 200);


    }


    private void gofood() {
        String lat;
        String lng;
        lat = pref.getString("lat", "0");
        String token = pref.getString("tokenfood", "c44afe01-8437-4f7f-8f43-e069a0dc6ea1");
        // Getting the longitude of the i-th location
        lng = pref.getString("lng", "0");
        OkHttpClient client = new OkHttpClient();
        Bundle extras = getIntent().getExtras();
        Request.Builder builder = new Request.Builder();


        Request request = builder
                .url("https://api.gojekapi.com/gofood/consumer/v3/restaurants")
                .header("Authorization", "Bearer " + token)
                .header("X-Location", lat + "," + lng)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string().toString();
                gofo = new ArrayList<>();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObj = new JSONObject(myResponse);
                            JSONObject as = jsonObj.getJSONObject("data");
                            JSONArray a = as.getJSONArray("cards");

                            for (int i = 0; i < a.length(); i++) {
                                JSONObject gf = a.getJSONObject(i);
                                String latlong = gf.getJSONObject("content").getString("location"); //

                                String name = gf.getJSONObject("content").getJSONObject("brand").getString("name");
                                String alamat = gf.getJSONObject("content").getString("address");
                                HashMap<String, String> gf1 = new HashMap<>();
                                gf1.put("address", alamat);
                                gf1.put("name", name);
                                gf1.put("latLong", latlong);
                                gofo.add(gf1);


                            }
                            listgofood();
                            //      Toast.makeText(MainActivity.this, "gf1", Toast.LENGTH_SHORT).show();
                            MDToast.makeText(MainActivity.this, "GO-FOOD", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();


                        } catch (JSONException e) {
                            Log.e("dash", "Error processing JSON", e);
                            //       Toast.makeText(MainActivity.this, "gf error" + myResponse, Toast.LENGTH_SHORT).show();
                            MDToast.makeText(MainActivity.this, "GO-FOOD error" + myResponse, MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();

                            tokengjk();
                        }
                    }
                });

            }
        });


    }

    public void listgofood() {
        //       Toast.makeText(MainActivity.this, "gf2", Toast.LENGTH_SHORT).show();
        hud.dismiss();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.list2, null);
        builder.setView(convertView);
        builder.setTitle("GO-FOOD");
        builder.setIcon(R.drawable.ic_restaurant_menu_red_24dp);
        ListView lv = (ListView) convertView.findViewById(R.id.lv);
        ListAdapter adapter = new SimpleAdapter(
                this, gofo,
                R.layout.gflist, new String[]{"name", "address",
                "latLong"}, new int[]{R.id.name,
                R.id.alamat, R.id.lokasi});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long duration) {
                TextView textViewIata = (TextView) view.findViewById(R.id.lokasi);
                String loc = (String) textViewIata.getText();
                TextView textName = (TextView) view.findViewById(R.id.name);
                String nam = (String) textName.getText();
                SharedPreferences.Editor editor = pref.edit();
                // Storing the latitude for the i-th location
                String[] latLng1 = loc.split(",");
                double latitude1 = Double.parseDouble(latLng1[0]);
                double longitude1 = Double.parseDouble(latLng1[1]);
                editor.putString("lat", Double.toString(latitude1));
                editor.putString("lng", Double.toString(longitude1));
                editor.apply();
                LatLng ltg = new LatLng(latitude1, longitude1);
                placeMarker(ltg);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ltg, 17));
                MDToast.makeText(MainActivity.this, "GO-FOOD" + "\n" + nam + "\n" + loc, MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();

                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            dialdown(updateUrl);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_fake) {
            // Handle the camera action
            Intent fake = new Intent(MainActivity.this, MainActivity.class);
            startActivity(fake);
            expired();
            finish();
        } else if (id == R.id.nav_bid) {
            Intent auto = new Intent(MainActivity.this, Auto.class);
            startActivity(auto);
            expired();
            finish();
        }   else if (id == R.id.nav_hide) {
            Intent hide = new Intent(MainActivity.this, HideActivity.class);
            startActivity(hide);
            expired();
            finish();
        } else if (id == R.id.nav_signout) {
            signOut();
        } else if (id == R.id.nav_versi) {
            MDToast.makeText(this, "BCT version 1.0", Toast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
            expired();
            Log.d("v", "version1.0");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void simulateProgressUpdate() {
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

    public void clear(View v) {
        mMap.clear();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        //   mMap.getUiSettings().setMyLocationButtonEnabled(true);
        setMyLocationLayerEnabled();
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_maps));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
//        if(mMap != null){
//            LatLng myLoc = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 16));
//        }else{
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                placeMarker(point);
                SharedPreferences.Editor e = pref.edit();
                e.putString("lat", Double.toString(point.latitude));
                e.putString("lng", Double.toString(point.longitude));
                e.apply();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(point));

                // zoomToSpecificLocation(point);
            }

        });
        fake.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean checked) {

                if (checked) {

                    initializeView();
                    loc_center = mMap.getCameraPosition().target;
                    double latitude2 = Double.parseDouble(pref.getString("lat", ""));
                    double longitude2 = Double.parseDouble(pref.getString("lng", ""));
                    app.longitude = loc_center.longitude;
                    app.latitude = loc_center.latitude;
                    SharedPreferences.Editor e = pref.edit();
                    e.putString("lat4", Double.toString(loc_center.latitude));
                    e.putString("lng4", Double.toString(loc_center.longitude));
                    e.putString("loka", Double.toString(loc_center.latitude)+","+Double.toString(loc_center.longitude));
                    e.putBoolean("mock", true);
                    e.apply();
                    pref.edit().putString("loka",  Double.toString(loc_center.latitude)+","+Double.toString(loc_center.longitude)).apply();
                    expired();
                    startService(new Intent(MainActivity.this, FloatingViewService.class));
                    app.loka = new LatLng(app.latitude, app.longitude);
                    String remark = "Lokasi";
                    goNameUser();
                    @SuppressLint("SimpleDateFormat")
                    String format = new SimpleDateFormat("dd-MM-yy HH:mm").format(Calendar.getInstance().getTime());

                    Location location = new Location("gps");
                    location.setLatitude(latitude2);
                    location.setLongitude(longitude2);
                    DAO_History.addLocationHistory(getApplicationContext(), location, "History"+"\n"+format);



//                    location.getLongitude();
 //                   DAO_History.addLocationHistory(context, location , remark);
//                    DAO_History.addLocationHistory(context,location, remark);
                    if (pref.getBoolean("lgjk", false)) {
                        Intent service3 = new Intent(MainActivity.this, Ping.class);
                        {
                            service3.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);

                        }
                        placeMarker2(app.loka);
                        startService(service3);
                        return;
                    }

                        Intent service2 = new Intent(MainActivity.this, LocationService.class);
                        {
                            service2.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);

                        }
                        //               placeMarker2(app.loka);
                        startService(service2);


                    Log.d("ru", "bo dong!");

                        try {
                            locationManager.addTestProvider(LocationManager.GPS_PROVIDER,
                                    "requiresNetwork" == "",
                                    "requiresSatellite" == "",
                                    "requiresCell" == "",
                                    "hasMonetaryCost" == "",
                                    "supportsAltitude" == "",
                                    "supportsSpeed" == "",
                                    "supportsBearing" == "",
                                    Criteria.POWER_HIGH,
                                    Criteria.ACCURACY_FINE);
                        } catch (Exception x) {
                            Crashlytics.logException(x);
                      //      showAlert("Failed", "Ooooppdd!!! Mock location tidak active");

                        }



                } else {
                    if (pref.getBoolean("mock", false)) {
                        try {

                            SharedPreferences.Editor e = pref.edit();
                            e.putBoolean("mock", false);
                            e.apply();
                            expired();
                            if (pref.getBoolean("lgjk", false)) {
                                Intent service2 = new Intent(MainActivity.this, Ping.class);
                                {
                                    service2.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);

                                }
                                stopService(service2);

                                return;

                            }

                                Intent service1 = new Intent(MainActivity.this, LocationService.class);
                                {
                                    service1.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);

                                }
                                stopService(service1);


                            Log.d("ru", "bo tutup!");
                        }catch (Exception e){
                            Crashlytics.logException(e);
                            showAlert("Failed", "Ooooppss!!! Mock location tidak active");
                        }
                    }

                }
            }


        });

 /*       fabTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isClickable()){
                    mMap.setTrafficEnabled(true);

                }else {
                    mMap.setTrafficEnabled(false);
                }
            }
        });


        fabHea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isClickable()) {
                    heat();

                }else{
                  }
            }
        });
*/


    }

    private void showAlert(String title, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void removemarker() {
        mMap.clear();
    }

    private void getTraffic() {
        pref = getSharedPreferences(Constants.h("YmN0"), 0);
        if (pref.getBoolean("trafic_maps", true)) {
            mMap.setTrafficEnabled(true);
            return;
        }
        mMap.setTrafficEnabled(false);


    }

    @Override
    protected void onResume() {
        super.onResume();
        receive();
   //     laygograb();
        fake.setChecked(bundle.getBoolean("fake", false));

        //     traffic.setChecked(bundle.getBoolean("ToggleButtonState1", false));
        ////      heat.setChecked(bundle.getBoolean("ToggleButtonState3", false));
    }

    @Override
    protected void onPause() {
        super.onPause();
        bundle.putBoolean("fake", fake.isChecked());
        //     trafficMap();
        //     bundle.putBoolean("ToggleButtonState1", traffic.isChecked());
        //     bundle.putBoolean("ToggleButtonState3", heat.isChecked());


    }

    private void initializeView() {

        //  startService(new Intent(Main.this, FloatingViewService.class));

    }


    private Marker placeMarker2(LatLng latLng) {


        marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_location_chat_marker)));

        return marker;
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private Marker placeMarker(LatLng latLng) {

        marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(pref.getString("lat", "") + "," + pref.getString("lng", ""))
                .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_pin_red_48dp)));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.getSnippet();
                Double a = marker.getPosition().latitude;
                Double b = marker.getPosition().longitude;
                LatLng ltg = new LatLng(a, b);
                placeMarker(ltg);
                SharedPreferences.Editor e = pref.edit();
                e.putString("lat", Double.toString(a));
                e.putString("lng", Double.toString(b));
                e.apply();

                myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String text;
                text = (marker.getSnippet());

                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                MDToast.makeText(MainActivity.this, "Copy this Location", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();

                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.getSnippet();
                Double a = marker.getPosition().latitude;
                Double b = marker.getPosition().longitude;
                LatLng ltg = new LatLng(a, b);
                placeMarker(ltg);
                SharedPreferences.Editor e = pref.edit();
                e.putString("lat", Double.toString(a));
                e.putString("lng", Double.toString(b));
                e.apply();
                myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String text;
                text = (marker.getSnippet());

                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                //     Toast.makeText(MainActivity.this, "Copy this Location", Toast.LENGTH_SHORT).show();
                MDToast.makeText(MainActivity.this, "Copy this Location", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();

            }
        });


        return marker;
    }

    private void setMyLocationLayerEnabled() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setAllGesturesEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        String a = pref.getString("lat", "0");
        String b = pref.getString("lng", "0");
        double latitude1 = Double.parseDouble(a);
        double longitude1 = Double.parseDouble(b);
        final LatLng ltg = new LatLng(latitude1, longitude1);
        placeMarker(ltg);
//        placeMarkeruser(app.loka);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ltg, 15));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                setMyLocationLayerEnabled();
            } else {
                // TODO: 10/15/2016 Tell user to use GPS
            }
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.ACCESS_FINE_LOCATION")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 99);
            return false;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 99);
        return false;
    }

    public void expi() {
        String date = pref.getString(Constants.h("ZXhw"), "");
        try {

            if (isPackageExpired(date)) {
                //         Toast.makeText(MainActivity.this, "SHVidW5naW4gQWRtaW4gVW50dWsgUGVycGFuamFuZw==", Toast.LENGTH_LONG).show();
                MDToast.makeText(MainActivity.this, Constants.h("SHVidW5naW4gQWRtaW4gVW50dWsgUGVycGFuamFuZw=="), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();

                signOut();
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    public void expired() {
        String date = pref.getString(Constants.h("ZXhw"), "");
        try {

            if (isPackageExpired(date)) {
                //         Toast.makeText(MainActivity.this, "SHVidW5naW4gQWRtaW4gVW50dWsgUGVycGFuamFuZw==", Toast.LENGTH_LONG).show();
                MDToast.makeText(MainActivity.this, Constants.h("SHVidW5naW4gQWRtaW4gVW50dWsgUGVycGFuamFuZw=="), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();

                signOut();
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    private boolean isPackageExpired(String date) {
        boolean isExpired = false;
        try {
            Date expiredDate = stringToDate(date, "yyyy-MM-dd");
            if (new Date().after(expiredDate)) isExpired = true;
            System.out.println("Test 3 : " + expiredDate + "   " + date);
            System.out.println("Test 5 : " + isExpired);
            return isExpired;
        } catch (Exception e) {
            Crashlytics.logException(e);
        }

        return isExpired;
    }

    private Date stringToDate(String aDate, String aFormat) {

        if (aDate == null) return null;
        try {
            ParsePosition pos = new ParsePosition(0);
            SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
            Date stringDate = simpledateformat.parse(aDate, pos);
            return stringDate;
        } catch (Exception e) {
        }
        return null;
    }

    public void signOut() {

         //
        auth.signOut();
        pref.edit().clear().apply();
        startActivity(new Intent(MainActivity.this, main.class));
        finish();
    }


    private class authGo implements FirebaseAuth.AuthStateListener {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                startActivity(new Intent(MainActivity.this, main.class));
                finish();
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("login", false);
                editor.apply();
            }
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("login", true);
            editor.apply();
        }
    }


    private class goj implements ChildEventListener {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            getDa(dataSnapshot);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            getDa(dataSnapshot);
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            getDa(dataSnapshot);
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            getDa(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d(TAG, databaseError.getMessage());
        }
    }

    @SuppressLint("MissingPermission")
    private void getDa(DataSnapshot dataSnapshot) {
        HashMap<String, String> value = (HashMap<String, String>)dataSnapshot.getValue();
        String str2 = (String) value.get("av");
        String str3 = (String) value.get("ac");
        String str4 = (String) value.get("si");
        String str5 = (String) value.get("di");
        String str6 = (String) value.get("tok");
        String str7 = (String) value.get("li");
        String str8 = (String) value.get("me");
        String str;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 28) {
            str = telephonyManager.getImei();
        } else {
            str = telephonyManager.getDeviceId();
        }
        pref.edit().putString("imei", str).apply();
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("apv", str2);
        edit.putString("apc", str3);
        edit.putString("sig", str4);
        edit.putString("dii", str5);
        edit.putString("tog", str6);
        edit.putString("lii", str7);
        edit.putString("mee", str8);
        edit.putString("ia1", str2);
        edit.putString("ia2", str3);
        edit.putString("ia4", str4);
        edit.putString("ia3", str5);
        edit.apply();

    }

    private class search implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            final String POPUP_LOGIN_TITLE = "Isi dengan lokasi";
            final String POPUP_LOGIN_TEXT = "ISI Dengan LOKASI";


            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

            alert.setTitle(POPUP_LOGIN_TITLE);
            alert.setMessage(POPUP_LOGIN_TEXT);
            // Set an EditText view to get user input
            final EditText email = new EditText(MainActivity.this);
            LinearLayout layout = new LinearLayout(MainActivity.this.getApplicationContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(email);
            alert.setView(layout);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String lokasi = (email.getText().toString());
                    pref.edit().putString("lokasi", lokasi).apply();
                    gjkpo();
                    // Do something with value!
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                    dialog.cancel();

                }
            });


            alert.show();

        }
    }

    private void gjkpo() {

        String lat;
        String lng;


        lat = pref.getString("lat", "0");
        String token = pref.getString("tokenfood", "c44afe01-8437-4f7f-8f43-e069a0dc6ea1");
        // Getting the longitude of the i-th location
        lng = pref.getString("lng", "0");
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();

        String lokasi = pref.getString("lokasi", "senayan");
        Request request = builder
                .url("https://api.gojekapi.com/poi/v4/search?name=" + lokasi + "&service_type=1&country_code=id&location=-6.203956%2C106.82869")
                .header("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string().toString();
                gofo = new ArrayList<>();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObj = new JSONObject(myResponse);
                            JSONArray a = jsonObj.getJSONArray("data");

                            for (int i = 0; i < a.length(); i++) {
                                JSONObject gf = a.getJSONObject(i);
                                Double lat = gf.getDouble("latitude");
                                Double lon = gf.getDouble("longitude");
                                String name = gf.getString("name");
                                String alamat = gf.getString("address");
                                String latlong = String.valueOf(lat) + "," + String.valueOf(lon);

                                HashMap<String, String> gf1 = new HashMap<>();
                                gf1.put("address", alamat);
                                gf1.put("name", name);
                                gf1.put("latLong", latlong);


                                gofo.add(gf1);


                            }
                            listgofood();
                            //        Toast.makeText(MainActivity.this, "gf1", Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            Log.e("dash", "Error processing JSON", e);
                            Toast.makeText(MainActivity.this, "spot error" + myResponse, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        tvLat.setText(String.valueOf(location.getLatitude()));
        tvLat.setTextColor(ContextCompat.getColor(this, R.color.biru_bct));
        tvLng.setText(String.valueOf(location.getLongitude()));
        tvLng.setTextColor(ContextCompat.getColor(this, R.color.biru_bct));
        tvAcc.setText(String.valueOf(location.getAccuracy()));
        tvAcc.setTextColor(ContextCompat.getColor(this, R.color.biru_bct));
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        getlatlng();

        //move map camera
//       mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        Log.d("IsMyLocationChange", "Yes : " + location.getLatitude() + " " + location.getLongitude());
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        createLocationRequest();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    protected void onStart() {

        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
            stopLocationUpdates();
            mGoogleApiClient.disconnect();

        super.onStop();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    LocationRequest mLocationRequest;

    public final boolean lok() {
        if (checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
        return false;
    }


    protected void createLocationRequest() {
        if (lok()) {
            mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(10000);
//        mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(500);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
    }

    protected void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }


    private class cekHisto implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, DisplayHistoryActivity.class));
            finish();
        }
    }
    private void getDeviceLocation() {
        try {
            Task<Location> locationResult = mFusedLocationClient.getLastLocation();
            locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        try {
                            Location location = task.getResult();

                            LatLng currentLatLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            CameraUpdate update = CameraUpdateFactory.newLatLng(currentLatLng);
                            mMap.moveCamera(update);
                        } catch (NullPointerException e){
                            Crashlytics.logException(e);

                        }
                    }
                }
            });
        } catch (NullPointerException e){
            Crashlytics.logException(e);

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }



    EditText etTok;
    Button tokSer;

    public void inputManual(){
        dialog = new android.app.AlertDialog.Builder(MainActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_token, null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        //     dialog.setIcon(R.drawable.ic_gojek;
        etTok = (EditText)dialogView.findViewById(R.id.edtToken);
        tokSer = dialogView.findViewById(R.id.btn_token_server);
        but1 = (Button)dialogView.findViewById(R.id.btn_ok_token);
        but2 = (Button)dialogView.findViewById(R.id.btn_cancel_token);
        text1 = dialogView.findViewById(R.id.tv_ambil);
        final android.app.AlertDialog d = dialog.show();
        tokSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hud = KProgressHUD.create(MainActivity.this)
                        .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                        .setLabel("Please wait")
                        .setMaxProgress(100)
                        .show();
                hud.setProgress(90);
                simulateProgressUpdate();
                String aa = pref.getString("tog", "");
                pref.edit().putString("tokenfood", aa).apply();
                MDToast.makeText(getApplicationContext(), "Token Berhasil di dapat", Toast.LENGTH_SHORT, MDToast.TYPE_INFO).show();
                d.dismiss();
            }
        });
        but1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String token = (etTok.getText().toString());
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("tokenfood", token);
                /** Saving the values stored in the shared preferences */
                editor.apply();
                MDToast.makeText(getApplicationContext(), "Input Token baru berhasil", Toast.LENGTH_SHORT, MDToast.TYPE_INFO).show();
               d.dismiss();
            }
        });
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tokgjk();
            }
        });
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

    }


    TextView tkgjk;

    public void tokgjk(){
        @SuppressLint("SdCardPath") String path = "/data/data/com.gojek.driver.bike/shared_prefs";
        Process proc;
        File sdcard = Environment.getExternalStorageDirectory();
        File targetFile = new File(Environment.getDataDirectory(), "data/com.gojek.app/shared_prefs/AuthPreferences.xml");

        context = getApplicationContext();
        try {
            proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "chmod 777 " + targetFile});
            proc.waitFor();

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Object data = targetFile;
            String response = data.toString();

            Document doc = documentBuilder.parse(targetFile);
            Element rootElement = doc.getDocumentElement();

            NodeList nodes = rootElement.getChildNodes();
            System.out.println("Node Length :" + nodes.getLength());
            for(int i=0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);

                if(node instanceof Element)
                {
                    Element child = (Element) node;

                    if(node.getNodeName().equals("string")){
                        if(child.getAttribute("name").equals("accessToken")){
                            String a = child.getTextContent();
                            tkgjk.setText(child.getTextContent());
                            pref.edit().putString("cektoken",a).apply();
                            System.out.println("test1" + child.getTextContent());
                            inputx();

                        }


                    }

                }
            }



            //Log.d("EDITOR_ACTIVITY", "X  :  " + nodeList.getLength());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Terjadi kesalahan  atau pref tidak ditemukan",Toast.LENGTH_SHORT).show();

            System.out.println("test1" + e);


        }
    }
    public void inputx(){

        final String a = pref.getString("cektoken", "token");
        String b;


        final String POPUP_LOGIN_TITLE="Token dari pref";
        final String POPUP_LOGIN_TEXT="tekan ya untuk input token  ";
        final String EMAIL_HINT= a;
        final String PASSWORD_HINT="longitude";

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(POPUP_LOGIN_TITLE);
        alert.setMessage(POPUP_LOGIN_TEXT);
        b = pref.getString("lng", "0");

        final TextView email = new TextView(this);
        email.setText(a);
        email.setTextIsSelectable(true);
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(email);
        alert.setView(layout);



        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                SharedPreferences.Editor editor = pref.edit();
                editor.putString("tokenfood", a);
                /** Saving the values stored in the shared preferences */
                editor.apply();
                Toast.makeText(getApplicationContext(),"Input Token baru berhasil",Toast.LENGTH_SHORT).show();
                //           dialog.dismiss();
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                dialog.cancel();

            }
        });



        alert.show();
    }

    private class settings implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            settingan();
        }
    }

    Switch swMag ,swPin,swGho ,swRan, swGcm ,swAutoran;
    Button ok;
    TextView tvMin, tvMax;
    CrystalRangeSeekbar rangeSeekbar;
    private void settingan() {
        pref = getSharedPreferences(Constants.h("YmN0"), 0);
        dialog = new android.app.AlertDialog.Builder(MainActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_settings, null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        //     dialog.setIcon(R.drawable.ic_gojek;
        swMag = dialogView.findViewById(R.id.sw_magiskmode);
        swPin = dialogView.findViewById(R.id.sw_ping);
        swGho = dialogView.findViewById(R.id.sw_ghostmode);
        swRan = dialogView.findViewById(R.id.sw_random_goy);
        swGcm = dialogView.findViewById(R.id.sw_ping_gscm);
        swAutoran = dialogView.findViewById(R.id.sw_auto_random);
        tvMin = dialogView.findViewById(R.id.textMin);
        tvMax = (TextView)dialogView.findViewById(R.id.textMax);
        rangeSeekbar = (CrystalRangeSeekbar)dialogView.findViewById(R.id.rangeSeekbarInterval);
        ok = (Button)dialogView.findViewById(R.id.btn_ok_sett);

        final android.app.AlertDialog d = dialog.show();

        rangeSeekbar
                .setMinStartValue(pref.getInt("min", 3))
                .setMaxStartValue(pref.getInt("max", 30))
                .setDataType(CrystalRangeSeekbar.DataType.INTEGER)
                .apply();
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue));
                tvMax.setText(String.valueOf(maxValue));
            }
        });
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("RNG=>Bct Inter", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
                int jawal = Integer.parseInt(String.valueOf(minValue));
                int jakir = Integer.parseInt(String.valueOf(maxValue));
                pref = getSharedPreferences(Constants.h("YmN0"), 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("min", jawal);
                editor.putInt("max", jakir);
                editor.apply();
            }
        });
        boolean ran = this.pref.getBoolean("auto_random", false);
        swAutoran.setChecked(ran);
        swAutoran.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    pref.edit().putBoolean("auto_random", true).apply();
                    return;
                }
                pref.edit().putBoolean("auto_random", false).apply();
            }
        });
        boolean rom = this.pref.getBoolean("random", false);
        swRan.setChecked(rom);
        swRan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pref.edit().putBoolean("random", true).apply();
                    return;
                }
                pref.edit().putBoolean("random", false).apply();
            }
        });
        boolean gc = this.pref.getBoolean("gcm", false);
        swGcm.setChecked(gc);
        swGcm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    //                startPing();
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("gcm", true);
                    //           editor.putString("stadriv", "Available");
                    editor.apply();
                    MDToast.makeText(getApplicationContext(), "Ping Server Gojek is Running...",Toast.LENGTH_SHORT,
                            MDToast.TYPE_SUCCESS).show();
                    return;
                }
/*
                Intent service2 = new Intent(MainActivity.this, Ping.class);
                {
                    service2.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                }
                stopService(service2);
          */
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("gcm", false);
                //          editor.putString("stadriv", "UnAvailable");
                //            editor.putString("stadriv", "Available");
                editor.apply();
                MDToast.makeText(getApplicationContext(), "Ping is Stopped...",Toast.LENGTH_SHORT,
                        MDToast.TYPE_INFO).show();


            }
        });


        boolean bol = this.pref.getBoolean("fuse", false);
        swMag.setChecked(bol);
        swMag.setOnCheckedChangeListener(new magiskMode());
        boolean bol2 = this.pref.getBoolean("acti", false);
        swPin.setChecked(bol2);
        swPin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    //            startPing();
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("acti", true);

                    editor.apply();
                    MDToast.makeText(getApplicationContext(), "Ping Server Gojek is Running...",Toast.LENGTH_SHORT,
                            MDToast.TYPE_SUCCESS).show();
                    return;
                }

                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("acti", false);

                editor.apply();
                MDToast.makeText(getApplicationContext(), "Ping is Stopped...",Toast.LENGTH_SHORT,
                        MDToast.TYPE_INFO).show();


            }
        });
        boolean bol3 = this.pref.getBoolean("lgjk", false);
        swGho.setChecked(bol3);
        swGho.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pref = getSharedPreferences(Constants.h("YmN0"), 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("lgjk", true);
                    editor.apply();
                    return;

                }
                pref = getSharedPreferences(Constants.h("YmN0"), 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("lgjk", false);
                editor.apply();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }

    private class magiskMode implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                pref = getSharedPreferences(Constants.h("YmN0"), 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("fuse", true);
                editor.apply();
                return;

            }
            pref = getSharedPreferences(Constants.h("YmN0"), 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("fuse", false);
            editor.apply();
            //           editor.putString("stadriv", "UnAvailable");
        }
    }



    public void zoomMin(View v){
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
    }
    public void zoomOut(View v){
        mMap.animateCamera(CameraUpdateFactory.zoomOut());

    }

    private class goToken implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            inputManual();
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            pref.edit().putBoolean("mock", false).apply();
            context.stopService(new Intent(context, LocationService.class));

        }
    }

    public class myAsy  extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            // result holds what you return from doInBackground
            Boolean bool = (Boolean) result;
            super.onPostExecute(bool);
            bool.booleanValue();

        }
    }

    private void active()
    {
        boolean bool = this.pref.getBoolean("acti", false);
        this.acti.setChecked(bool);
        this.acti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
            {
                if (paramAnonymousBoolean)
                {

                    startPing();
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("acti", true);
                    //           editor.putString("stadriv", "Available");
                    editor.apply();

                    return;
                }

                Intent service2 = new Intent(MainActivity.this, Ping.class);
                {
                    service2.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);

                }
                stopService(service2);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("acti", false);
                //       editor.putString("stadriv", "UnAvailable");
                editor.apply();


            }
        });

    }

    private void startPing() {
        Intent service1 = new Intent(MainActivity.this, Ping.class);
        {
            service1.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);

        }
        startService(service1);
    }

    AddLocationHistoryDialog addLocationHistoryDialog;
    private class goCoor implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            goTocoor();

        }
    }

    private void goTocoor() {

        dialog = new AlertDialog.Builder(MainActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_goto, null);
        dialog.setCancelable(false);
        dialog.setView(dialogView);
        dialog.setIcon(R.mipmap.ic_launcher);
        final EditText latiTude = dialogView.findViewById(R.id.gotoLat);
        final EditText longTitude = dialogView.findViewById(R.id.gotoLng);
        final Button set = dialogView.findViewById(R.id.btn_ok_lok);
        final Button can = dialogView.findViewById(R.id.btn_cancel_lok);

        final android.app.AlertDialog d = dialog.show();
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String la = (latiTude.getText().toString());
                String lo = (longTitude.getText().toString());

                pref.edit().putString("lat", la).putString("lng", lo).apply();
                if (la.isEmpty() || lo.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                    return;

                }

                SharedPreferences.Editor editor = pref.edit();
                double latitude1 = Double.parseDouble(la);
                double longitude1 = Double.parseDouble(lo);
                editor.putString("lat", Double.toString(latitude1));
                editor.putString("lng", Double.toString(longitude1));
                editor.apply();
                LatLng ltg = new LatLng(latitude1, longitude1);
                placeMarker(ltg);
                MDToast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                d.dismiss();
            }
        });
        can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }


    private void addhisto(){
        mDatabase = FirebaseDatabase.getInstance();
        databaseReference = mDatabase.getReference("data");
        databaseReference.child("history").addValueEventListener(new histoadd());
    }

    private class histoadd implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //Map addValue = new HashMap();
            HashMap<String, Object> histo = new HashMap<>();
            //    histo.put();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    private void getlatlng(){
        mDatabase = FirebaseDatabase.getInstance();
        databaseReference = mDatabase.getReference("location");
        FirebaseUser mUser = auth.getCurrentUser();
        User user = new User();
        user.setmLat(pref.getString("lat", ""));
        user.setmLon(pref.getString("lng", ""));
        user.setmTitle(pref.getString("name", ""));
        databaseReference.child(mUser.getUid()).setValue(user);
    }

    private void setlatlng() {
        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference(Constants.h("Z29qZWs="));
        ref.addChildEventListener(new goj());
        //     ref.child(getString(R.string.gojek)).addChildEventListener(new goj());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(brcast);
    }

    private void receive(){
            Intent intent = new Intent();
            intent.putExtra("data", "raw");
            intent.setAction("com.gojek.driver");
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            getApplicationContext().sendBroadcast(intent);

    }
    Double alt;
    public final OkHttpClient clients = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    int statusCodeapi;
    private void aping() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("accuracy", 0);
            jSONObject.put("altitude", 0);
            jSONObject.put("appVersion", "96");
            jSONObject.put("driverStatus", "Available");
            jSONObject.put("locationTimestamp", System.currentTimeMillis());
            jSONObject.put("speed", 0);
            jSONObject.put("gcmKey", pref.getString("fcmx", ""));
            jSONObject.put("userId", pref.getString("iddriverx", ""));
            jSONObject.put("time", System.currentTimeMillis());
            jSONObject.put("latitude", 0);
            jSONObject.put("longitude", 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String body = jSONObject.toString();

        RequestBody Body = RequestBody.create( JSON, body);
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(prefs.getString("mee", ""))
                .header("User-Agent", "okhttp/3.12.0")
                .header("X-AppVersion", pref.getString("ia1","4.21.1"))
                .header("X-AppCode", pref.getString("ia2","92"))
                .header("X-AppId", "com.gojek.driver.bike")
                .header("X-IMEI", pref.getString("imei",""))
                .header("X-DeviceOS", "Android")
                .header("appSignature", pref.getString("ia4",""))
                .header("D1", pref.getString("ia3",""))
                .header("X-Location", app.latitude +","+ app.longitude)
                .header("X-Location-Accuracy", String.valueOf(randFloat(0.01f,50f)) )
                .header("X-HTTP-DRIVER-ID", pref.getString("iddriverx",""))
                .header("authorization","Bearer "+ pref.getString("tkbvx",""))
                .header("X-PhoneModel", Build.BRAND)
                .header("X-PhoneMake", Build.DEVICE )
                .header("X-UniqueId", Build.SERIAL )
                .header("X-DeviceOS", "Android")
                .put(Body)
                .build();

        clients.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string().toString();
                System.out.println("Ping   =" + myResponse);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (statusCodeapi == 200 ) {
                                pref.edit().putBoolean("ste", true).apply();
                                MDToast.makeText(getApplicationContext(), "Sukses !" + "\n" + "terkoneksi dengan gojek ", Toast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                            }else{
                                MDToast.makeText(getApplicationContext(), "gagal !"+"\n"+ "tidak terkoneksi dengan gojek " ,Toast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                            }
                        } catch (Exception e) {
                            Log.e("hasil", "Error processing JSON", e);
                            MDToast.makeText(getApplicationContext(), "gagal !"+String.valueOf(e) ,Toast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                            PrintStream printStream = System.out;
                            printStream.println("ping  : " + e);
                        }

                    }
                });
            }
        });
    }

    public static float randFloat(float min, float max) {
        Random rand = new Random();
        float result = rand.nextFloat() * (max - min) + min;
        return result;
    }

    private void getloc() {
        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference("location");
        ref.addChildEventListener(new loc());

    }

    private class loc implements ChildEventListener {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            setloc(dataSnapshot);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            setloc(dataSnapshot);
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            setloc(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d(TAG, databaseError.getMessage());
        }
    }

    private void setloc(DataSnapshot dataSnapshot) {
        HashMap<String, String> value = (HashMap<String, String>)dataSnapshot.getValue();
        String str2 = (String) value.get("lat");
        String str3 = (String) value.get("lng");
        String str4 = (String) value.get("tittle");
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("lati", str2);
        edit.putString("lngi", str3);
        edit.putString("titt", str4);
        edit.putString("loka", str2+","+str3);
        edit.apply();
    }

    private Marker placeMarkeruser(LatLng latLng) {

        String a = pref.getString("lati", "");
        String b = pref.getString("lngi", "");
        double latitude1 = Double.parseDouble(a);
        double longitude1 = Double.parseDouble(b);
        final LatLng ltg = new LatLng(latitude1, longitude1);
        marker = mMap.addMarker(new MarkerOptions()
                .position(ltg)
                .title(pref.getString("titt", ""))
                .snippet("BCT Team")
                .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_person_pin)));

        return marker;
    }


    private class grbfo {
    }
}
