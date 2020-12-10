package com.google.bct.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.bct.BuildConfig;
import com.google.bct.R;
import com.google.bct.adapter.AppListGetter;
import com.google.bct.adapter.MainAdapter;
import com.google.bct.adapter.ShowConfigurationAdapter;
import com.google.bct.common.Constants;

import java.io.File;

public class HideActivity extends   AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static  SharedPreferences pref;
    private static  final String preferencesFileName = BuildConfig.APPLICATION_ID +"/"+ Constants.h("YmN0");
    private static final String preferencesRelativeDir = "/shared_prefs/";
    private final Handler handler = new Handler();
    private ListView listView;
    private MainAdapter mAdapter;
    private File sharedPrefsFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide);
        pref = getSharedPreferences(Constants.h("YmN0"), 0);
        mAdapter = new MainAdapter(this, pref.getBoolean(Constants.KEY_SHOW_SYSTEM_APP, false));

        pref.registerOnSharedPreferenceChangeListener(this);
        fixDataFilesPermissions();
        AppCompatEditText inputSearch = (AppCompatEditText) findViewById(R.id.searchInput);

        assert inputSearch != null;
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
               HideActivity.this.mAdapter.getFilter().filter(cs);
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
            @Override
            public void afterTextChanged(Editable arg0) {}
        });


        listView = (ListView) findViewById(R.id.mListView);
        assert listView != null;
        listView.setAdapter(mAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HideActivity.this, AppHideConfigurationActivity.class);
                intent.putExtra(Constants.KEY_PACKAGE_NAME, mAdapter.getKey(position));
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String pkgName = mAdapter.getKey(position);

                View settingsDisplay = getLayoutInflater().inflate(R.layout.settings_display, null, false);
                ListView sub_listView = (ListView) settingsDisplay.findViewById(R.id.settingsDisplayListViewID);
                ShowConfigurationAdapter subListAdapter = new ShowConfigurationAdapter(HideActivity.this, pkgName);
                sub_listView.setAdapter(subListAdapter);

                new AlertDialog.Builder(HideActivity.this)
                        .setTitle(R.string.app_name)
                        .setView(settingsDisplay)
                        .show();

                return true;
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mAdapter.notifyDataSetChanged();
        fixFilePermissions(sharedPrefsFile);
    }

    private void fixDataFilesPermissions() {
        final File packageDataDirectory = this.getApplicationContext().getFilesDir().getParentFile();
        final File sharedPrefDirectory = new File(packageDataDirectory, preferencesRelativeDir);
        sharedPrefsFile = new File(sharedPrefDirectory,preferencesFileName+".xml");

        fixFilePermissions(packageDataDirectory);
        fixFilePermissions(sharedPrefDirectory);
        fixFilePermissions(sharedPrefsFile);
    }

    private void fixFilePermissions(File file) {
        file.setReadable(true,false);
        file.setExecutable(true,false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
        pref.registerOnSharedPreferenceChangeListener(this);

    }
    @Override
    protected void onPause()
    {
        super.onPause();
        fixDataFilesPermissions();
        pref.unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hide, menu);

        MenuItem showSystemApps = menu.findItem(R.id.action_show_system_app);
        MenuItem showPackageName = menu.findItem(R.id.action_show_package_name);

        showSystemApps.setChecked(pref.getBoolean(Constants.KEY_SHOW_SYSTEM_APP, false));
        showPackageName.setChecked(pref.getBoolean(Constants.KEY_SHOW_PACKAGE_NAME, false));
        refresh(showSystemApps.isChecked());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement

        switch (item.getItemId()) {
            case R.id.action_restart_launcher: {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        restartLauncher();
                    }
                });
                return true;
            }
            case R.id.action_show_system_app: {

                boolean showSystemApps = !item.isChecked();
                item.setChecked(showSystemApps);
                pref.edit()
                        .putBoolean(Constants.KEY_SHOW_SYSTEM_APP, showSystemApps)
                        .apply();
                refresh(showSystemApps);
                return true;
            }
            case R.id.action_show_package_name: {
                boolean showPackageName = !item.isChecked();
                item.setChecked(showPackageName);
                pref.edit()
                        .putBoolean(Constants.KEY_SHOW_PACKAGE_NAME, showPackageName)
                        .apply();
                mAdapter.notifyDataSetChanged();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void restartLauncher() {

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PackageManager packageManager = getPackageManager();
        ResolveInfo resolveInfo = packageManager.resolveActivity(startMain, PackageManager.MATCH_DEFAULT_ONLY);
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.killBackgroundProcesses(resolveInfo.activityInfo.packageName);
        try {
            packageManager.getApplicationLabel(packageManager.getApplicationInfo(resolveInfo.activityInfo.packageName,0));
            Toast.makeText(this,"Killed " + resolveInfo.activityInfo.packageName, Toast.LENGTH_SHORT).show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


    private void refresh(boolean showSystemApp) {
        mAdapter = new MainAdapter(this, showSystemApp);
        listView.setAdapter(mAdapter);

    }

    //Method is hooked by framework.
    @SuppressWarnings("SameReturnValue")
    private boolean isXposedActive() {
        return false;
    }

    private void checkIfXposedIsActive() {
        if (!isXposedActive()) {
            AlertDialog alertDialog = new AlertDialog.Builder(HideActivity.this).create();
            alertDialog.setTitle(R.string.app_name);
            alertDialog.setMessage(getString(R.string.module_not_active));
            alertDialog.setCancelable(false);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, Constants.h("RXhpdCBBcHA="), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AppListGetter.getInstance(HideActivity.this).cancel(true);
                    finish();
                }
            });
            alertDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(HideActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
