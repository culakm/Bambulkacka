package com.hellbilling.bambulkacka;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BambulkackaDB {

    private SQLiteOpenHelper openHelper;

    static class BambulkackaDbHelper extends SQLiteOpenHelper {

        public BambulkackaDbHelper(Context context) {
            super(context, BambulkackaContract.DATABASE_NAME, null, BambulkackaContract.DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(BambulkackaContract.TbExercises.CREATE_TABLE);
            db.execSQL(BambulkackaContract.TbExamples.CREATE_TABLE);
            db.execSQL(BambulkackaContract.TbAttempts.CREATE_TABLE);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(BambulkackaContract.TbExercises.DELETE_TABLE);
            db.execSQL(BambulkackaContract.TbExamples.DELETE_TABLE);
            db.execSQL(BambulkackaContract.TbAttempts.DELETE_TABLE);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    public BambulkackaDB(Context ctx) {
        openHelper = new BambulkackaDbHelper(ctx);
    }

    public Cursor getExercises() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db.query(BambulkackaContract.TbExercises.TABLE_NAME, BambulkackaContract.TbExercises.COLUMNS, null, null, null, null, null);
    }

    public Cursor getExercisesResults() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db.rawQuery("SELECT exercises._id, date_start, ok.count AS ok_count, nok.count  AS nok_count FROM exercises, (SELECT count(*) AS count, examples.exercise_id AS exercise_id FROM attempts INNER JOIN examples ON attempts.example_id=examples._id WHERE attempts.ok = 1 GROUP BY  examples.exercise_id) AS ok, (SELECT count(*) AS count, examples.exercise_id AS exercise_id FROM attempts INNER JOIN examples ON attempts.example_id=examples._id WHERE attempts.ok = 0 GROUP BY  examples.exercise_id) AS nok WHERE exercises._id = ok.exercise_id AND exercises._id = nok.exercise_id", null);

    //select exercises._id, date_start, ok.count, nok.count from exercises, (select count(*) as count, examples.exercise_id as exercise_id from attempts INNER JOIN examples ON attempts.example_id=examples._id WHERE attempts.ok = 1 GROUP BY  examples.exercise_id) as ok, (select count(*) as count, examples.exercise_id as exercise_id from attempts INNER JOIN examples ON attempts.example_id=examples._id WHERE attempts.ok = 0 GROUP BY  examples.exercise_id) as nok WHERE exercises._id = ok.exercise_id AND exercises._id = nok.exercise_id
    }

    public void close() {
        openHelper.close();
    }
}
