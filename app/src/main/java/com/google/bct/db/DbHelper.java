package com.google.bct.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.bct.init.AppBase;

import java.util.Map;
import java.util.Set;

public class DbHelper {

    private static final String DB_NAME ="history.db" ;
    private SQLiteDatabase db;


    /**
     * 请不要实例化此对象，直接使用Application中的缓存对象
     *
     */
    public DbHelper(AppBase app) {
        if (app.dbHelper != null) {
            throw new RuntimeException("Duplicated db helper");
        }
        db = app.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        app.dbHelper = this;
        createTableIfNotExist();
        db = app.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        app.dbHelper = this;
        createTableIfNotExist();
    }

    private void createTableIfNotExist() {
        String sql = "CREATE TABLE if NOT EXISTS \"history\" (\n" +
                "\"id\"  INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\"latitude\"  TEXT NOT NULL,\n" +
                "\"longitude\"  TEXT NOT NULL,\n" +
                "\"remark\"  TEXT\n" +
                ");\n";
        db.execSQL(sql);
    }


    /**
     * 执行数据库可写的操作
     * {@link SQLiteDatabase#execSQL(String, Object[]) }
     *
     * @throws java.sql.SQLException if the SQL string is invalid
     */
    public void execSQL(String sql, @androidx.annotation.Nullable Object[] bindArgs) {
        synchronized (db) {
            db.beginTransaction();
            try {
                if (bindArgs == null) {
                    db.execSQL(sql);
                } else {
                    db.execSQL(sql, bindArgs);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }

    /**
     * 一次执行多条SQL, 做一次事务，使用这个方法。
     *
     * @param sql_bind_map Map,  value是sql语句，key 是 绑定数据的 Object[]
     * @throws java.sql.SQLException if the SQL string is invalid
     */
    public void execBulkSQL(Map<Object[], String> sql_bind_map) {
        Set<Map.Entry<Object[], String>> entrySet = sql_bind_map.entrySet();
        synchronized (db) {
            db.beginTransaction();
            try {
                for (Map.Entry<Object[], String> sqlEntry : entrySet) {
                    if (sqlEntry.getKey() == null) {
                        db.execSQL(sqlEntry.getValue());
                    } else {
                        db.execSQL(sqlEntry.getValue(), sqlEntry.getKey());
                    }
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }


    /**
     * proxy to: {@link SQLiteDatabase#rawQuery(String, String[])}
     */
    public Cursor rawQuery(String sql, String[] selectionArgs) {
        synchronized (db) {
            return db.rawQuery(sql, selectionArgs);
        }
    }

}
