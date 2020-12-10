package com.google.bct.db;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;

import com.google.bct.init.AppBase;

import java.util.ArrayList;

public class DAO_History {

    public static void clearHistory(Context context) {
        DbHelper dbHelper = AppBase.obtainApp(context).dbHelper;
        dbHelper.execSQL("delete from history", null);
    }

    public static void addLocationHistory(Context context, Location location, String remark) {
        DbHelper dbHelper = AppBase.obtainApp(context).dbHelper;
        dbHelper.execSQL("INSERT into history (latitude, longitude, remark) VALUES( ?,?,?);", new Object[]{
                location.getLatitude(), location.getLongitude(), remark

        });
    }
    public static void deletefav(Context paramContext, String paramString1, String paramString2)
    {
        AppBase.obtainApp(paramContext).dbHelper.execSQL("delete from history where latitude = '" + paramString1 + "' and longitude = '" + paramString2 + "'", null);
    }
    public static ArrayList<HistoryBean> getLocationHistories(Context context) {
        DbHelper dbHelper = AppBase.obtainApp(context).dbHelper;
        Cursor cursor = dbHelper.rawQuery("select * from history", null);
        ArrayList<HistoryBean> historyBeans = new ArrayList<HistoryBean>();
        while (cursor.moveToNext()) {
            HistoryBean history = new HistoryBean();
            history.id = cursor.getInt(0);
            history.latitude = cursor.getString(1);
            history.longitude = cursor.getString(2);
            history.remark = cursor.getString(3);
            historyBeans.add(history);
        }
        cursor.close();
        return historyBeans;

    }
}
