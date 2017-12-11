package com.avengers.sleepylog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

/**
 * Database storing.
 */

public final class DBAdapter extends AppCompatActivity {

    private static DBAdapter sInstance;

    private static final String DATABASE_NAME = "MYDB";
    private static final String DATABASE_TABLE = "MYDBTABLE";
    private static final int DATABASE_VERSION = 3;

    private static final String KEY_DATE = "date";
    private static final String KEY_TIME_TO_BED = "time_to_bed";
    private static final String KEY_TIME_TO_SLEEP = "time_to_sleep";
    private static final String KEY_TIME_TO_WAKE_UP = "time_to_wake_up";
    private static final String KEY_TIME_OUT_BED = "time_out_bed";
    private static final String KEY_ASLEEP = "asleep";
    private static final String KEY_AWAKE = "awake";
    private static final String KEY_DURATION_NAP = "nap_duration";
    private static final String KEY_NAP = "naps";
    private static final String KEY_QUALITY = "quality";
    private static final String KEY_TOTAL_TIME_ASLEEP = "total_time_asleep";
    private static final String KEY_TOTAL_TIME_IN_BED = "total_time_in_bed";
    private static final String KEY_SLEEP_EFFICIENCY = "sleep_efficiency";


    protected static final int COL_DATE = 0;
    protected static final int COL_TIME_TO_BED = 1;
    protected static final int COL_TIME_TO_SLEEP = 2;
    protected static final int COL_TIME_TO_WAKE_UP = 3;
    protected static final int COL_TIME_OUT_BED = 4;
    protected static final int COL_ASLEEP = 5;
    protected static final int COL_AWAKE = 6;
    protected static final int COL_DURATION_NAP = 7;
    protected static final int COL_NAP = 8;
    protected static final int COL_QUALITY = 9;
    protected static final int COL_TOTAL_TIME_ASLEEP = 10;
    protected static final int COL_TOTAL_TIME_IN_BED = 11;
    protected static final int COL_SLEEP_EFFICIENCY = 12;

    private static final String[] ALL_KEYS = {KEY_DATE, KEY_TIME_TO_BED, KEY_TIME_TO_SLEEP, KEY_TIME_TO_WAKE_UP, KEY_TIME_OUT_BED,
            KEY_ASLEEP, KEY_AWAKE, KEY_DURATION_NAP, KEY_NAP, KEY_QUALITY, KEY_TOTAL_TIME_ASLEEP, KEY_TOTAL_TIME_IN_BED, KEY_SLEEP_EFFICIENCY};

    private Context context;
    private SQLiteDatabase db;
    private DatabaseHelper myDBHelper;

    public static synchronized DBAdapter getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBAdapter(context.getApplicationContext());
        }
        return sInstance;
    }

    private DBAdapter(Context context){
        this.context = context;
        myDBHelper = DatabaseHelper.getInstance(context);
    }

    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        Log.i("DBAdapter", dbFile.getAbsolutePath());
        return this;
    }

    public void close() {
        myDBHelper.close();
    }

    public long insertRow(String date, long time_to_bed, long  time_to_sleep, long time_to_wake_up,
                          long time_out_bed, long asleep, long awake, long nap_duration,
                          boolean naps, int quality, long total_time_asleep,
                          long total_time_in_bed, float sleep_efficiency) {
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date);
        values.put(KEY_TIME_TO_BED, time_to_bed);
        values.put(KEY_TIME_TO_SLEEP, time_to_sleep);
        values.put(KEY_TIME_TO_WAKE_UP, time_to_wake_up);
        values.put(KEY_TIME_OUT_BED, time_out_bed);
        values.put(KEY_ASLEEP, asleep);
        values.put(KEY_AWAKE, awake);
        values.put(KEY_DURATION_NAP, nap_duration);
        values.put(KEY_NAP, naps);
        values.put(KEY_QUALITY, quality);
        values.put(KEY_TOTAL_TIME_ASLEEP, total_time_asleep);
        values.put(KEY_TOTAL_TIME_IN_BED, total_time_in_bed);
        values.put(KEY_SLEEP_EFFICIENCY, sleep_efficiency);


        long rowid = db.insert(DATABASE_TABLE, null, values);
        return rowid;

    }

    public Cursor getAll() {
        Cursor cursor = db.query(DATABASE_TABLE, ALL_KEYS, null, null, null, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();

        }
        return cursor;
    }

    public Cursor getRowByPrimaryKey(String date){
        String whereStr = " date = "+ date;
        ///SELECT * FROM Person WHERE Name = 'B';
        Cursor cursor = db.query(DATABASE_TABLE, ALL_KEYS, whereStr, null, null, null, null, null);
        return cursor;
    }

    public boolean checkIfRowExists(String date){
        String whereStr = " date = "+ date;
        Cursor cursor = db.query(DATABASE_TABLE, ALL_KEYS, whereStr, null, null, null, null, null);
        return (cursor.getCount() > 0);
    }

    public boolean deleteRow(long rowid) {
        String whereStr = KEY_DATE + " = "+ rowid;
        return db.delete(DATABASE_TABLE, whereStr, null) > 0;
    }

    public void deleteAll() {
        Cursor cursor = getAll();
        long rowid = cursor.getColumnIndexOrThrow(KEY_DATE);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                deleteRow(cursor.getLong((int) rowid));
            } while (cursor.moveToNext());
        }
    }

    final static class DatabaseHelper extends SQLiteOpenHelper {

        private static DatabaseHelper databaseHelper;

        public static synchronized DatabaseHelper getInstance(Context context) {
            if (databaseHelper == null) {
                databaseHelper = new DatabaseHelper(context);
            }
            return databaseHelper;
        }

        private DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //CREATE TABLE ARTISTS(ID INTEGER PRIMARY KEY, NAME TEXT);
            String CREATE_DB_SQL = "CREATE TABLE " + DATABASE_TABLE
                    + " ( " + KEY_DATE + " STRING PRIMARY KEY, "
                    + KEY_TIME_TO_BED + " LONG, "
                    + KEY_TIME_TO_SLEEP + " LONG, "
                    + KEY_TIME_TO_WAKE_UP + " LONG, "
                    + KEY_TIME_OUT_BED + " LONG, "
                    + KEY_ASLEEP + " LONG, "
                    + KEY_AWAKE + " LONG, "
                    + KEY_DURATION_NAP + " LONG, "
                    + KEY_NAP + " BOOLEAN, "
                    + KEY_QUALITY + " INTEGER, "
                    + KEY_TOTAL_TIME_ASLEEP + " LONG, "
                    + KEY_TOTAL_TIME_IN_BED + " LONG, "
                    + KEY_SLEEP_EFFICIENCY + " FLOAT);";
            db.execSQL(CREATE_DB_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);

        }
    }
}
