package com.hellbilling.bambulkacka;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BambulkackaDbHelper extends SQLiteOpenHelper {

    public BambulkackaDbHelper(Context context) {
        super(context, BambulkackaContract.DATABASE_NAME, null, BambulkackaContract.DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BambulkackaContract.CalcExercises.CREATE_TABLE);
        db.execSQL(BambulkackaContract.CalcExamples.CREATE_TABLE);
        db.execSQL(BambulkackaContract.CalcAttempts.CREATE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(BambulkackaContract.CalcExercises.DELETE_TABLE);
        db.execSQL(BambulkackaContract.CalcExamples.DELETE_TABLE);
        db.execSQL(BambulkackaContract.CalcAttempts.DELETE_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

