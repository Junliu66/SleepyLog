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

    private static final String KEY_ID = "_id";
    private static final String KEY_DATE = "_date";
    private static final String KEY_TIME_TO_BED = "time to bed";

    protected static final int COL_ID = 0;
    protected static final int COL_DATE = 1;
    protected static final int COL_TIME_TO_BED = 2;

    private static final String[] ALL_KEYS = {KEY_ID, KEY_DATE, KEY_TIME_TO_BED};

    private Context context;
    private SQLiteDatabase db;
    private DatabaseHelper myDBHelper;

    public DBAdapter(Context context) {
        this.context = context;
        myDBHelper = new DatabaseHelper(context);
    }

    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        myDBHelper.close();
    }

    public long insertRow(String date, String time_to_bed) {
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date);
        values.put(KEY_TIME_TO_BED, time_to_bed);
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
        String whereStr = KEY_ID + " = "+ rowid;
        return db.delete(DATABASE_TABLE, whereStr, null) > 0;
    }

    public void deleteAll() {
        Cursor cursor = getAll();
        long rowid = cursor.getColumnIndexOrThrow(KEY_ID);
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

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);

        }
    }
}
