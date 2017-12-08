package com.avengers.sleepylog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by zhangJunliu on 12/5/17.
 */

public class DBAdapter extends AppCompatActivity {

    private static final String DATABASE_NAME = "MYDB";
    private static final String DATABASE_TABLE = "MYDBTABLE";
    private static final int DATABASE_VERSION = 1;

    private static final String KEY_DATE = "_date";
    private static final String KEY_TIME_TO_BED = "time_to_bed";
    private static final String KEY_TIME_TO_SLEEP = "time_to_sleep";
    private static final String KEY_TIME_TO_WAKE_UP = "time_to_wake_up";
    private static final String KEY_TIME_OUT_BED = "time_out_bed";
    private static final String KEY_ASLEEP = "asleep";
    private static final String KEY_AWAKE = "awake";
    private static final String KEY_DURATION_NAP = "nap_duration";
    private static final String KEY_NAP = "naps";
    private static final String KEY_QUALITY = "quality";

    protected static final int COL_DATE = 0;
    protected static final int COL_TIME_TO_BED = 1;
    protected static final int COL_TIME_TO_SLEEP = 2;
    protected static final int COL_TIME_TO_WAKE_UP = 3;
    protected static final int COL_TIME_OUT_BED = 4;
    protected static final int COL_ASLEEP = 4;
    protected static final int COL_AWAKE = 4;
    protected static final int COL_DURATION_NAP = 4;
    protected static final int COL_NAP = 4;
    protected static final int COL_QUALITY = 4;

    private static final String[] ALL_KEYS = {KEY_DATE, KEY_TIME_TO_BED, KEY_TIME_TO_SLEEP,
            KEY_TIME_TO_WAKE_UP, KEY_TIME_OUT_BED, KEY_ASLEEP, KEY_AWAKE, KEY_DURATION_NAP,
            KEY_NAP, KEY_QUALITY};

    private Context context;
    private SQLiteDatabase db;
    private DatabaseHelper myDBHelper;

    public DBAdapter(Context context) {
        this.context = context;
        myDBHelper = new DatabaseHelper(context);
    }

    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        //File dbFile = context.getDatabasePath(DATABASE_NAME);
        //Log.i("DBAdapter", dbFile.getAbsolutePath());
        return this;
    }

    public void close() {
        myDBHelper.close();
    }

    public long insertRow(long date, long time_to_bed, long time_to_sleep, long time_to_wake_up,
                          long time_out_bed, long asleep, long awake, long nap_duration,
                          boolean nap, int quality) {
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date);
        values.put(KEY_TIME_TO_BED, time_to_bed);
        values.put(KEY_TIME_TO_SLEEP, time_to_sleep);
        values.put(KEY_TIME_TO_WAKE_UP, time_to_wake_up);
        values.put(KEY_TIME_OUT_BED, time_out_bed);
        values.put(KEY_ASLEEP, asleep);
        values.put(KEY_AWAKE, awake);
        values.put(KEY_DURATION_NAP, nap_duration);
        values.put(KEY_NAP, nap);
        values.put(KEY_QUALITY, quality);

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

    class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_DB_SQL = "CREATE TABLE " + DATABASE_TABLE
                    + " ( " + KEY_DATE + " long primary key, "
                    + KEY_TIME_TO_BED + " long, "
                    + KEY_TIME_TO_SLEEP + " long, "
                    + KEY_TIME_TO_WAKE_UP + " long, "
                    + KEY_TIME_OUT_BED + " long, "
                    + KEY_ASLEEP + " long, "
                    + KEY_AWAKE + "long, "
                    + KEY_DURATION_NAP + "long, "
                    + KEY_NAP + "boolean, "
                    + KEY_QUALITY + "integer, ";
            db.execSQL(CREATE_DB_SQL);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);

        }
    }
}
