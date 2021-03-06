package com.hellbilling.bambulkacka;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class BambulkackaDB {

    private final SQLiteOpenHelper openHelper;

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

    public Cursor getExercisesResults() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db.rawQuery("SELECT exercises._id, date_start, date_end, ok.count AS ok_count, nok.count  AS nok_count FROM exercises LEFT JOIN (SELECT count(*) AS count, examples.exercise_id AS exercise_id FROM attempts LEFT JOIN examples ON attempts.example_id=examples._id WHERE attempts.ok = 1 GROUP BY  examples.exercise_id) AS ok ON exercises._id = ok.exercise_id LEFT JOIN (SELECT count(*) AS count, examples.exercise_id AS exercise_id FROM attempts LEFT JOIN examples ON attempts.example_id=examples._id WHERE attempts.ok = 0 GROUP BY  examples.exercise_id) AS nok ON  exercises._id = nok.exercise_id", null);
    }

    public Cursor getResultExamples(String exercise_id) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor examples = db.rawQuery("SELECT examples._id, a,b,sign,result, ok.count AS ok_count, nok.count  AS nok_count  FROM examples LEFT JOIN (SELECT count(*) AS count, examples._id AS example_id FROM attempts LEFT JOIN examples ON attempts.example_id=examples._id WHERE attempts.ok = 1 GROUP BY  examples._id) AS ok ON examples._id = ok.example_id LEFT JOIN (SELECT count(*) AS count, examples._id AS example_id FROM attempts LEFT JOIN examples ON attempts.example_id=examples._id WHERE attempts.ok = 0 GROUP BY  examples._id) AS nok ON  examples._id = nok.example_id WHERE examples.exercise_id = " + exercise_id, null);
        return examples;
    }

    long insertExercise(String sign, String date_start, String date_end) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BambulkackaContract.TbExercises.COLUMN_NAME_SIGN, sign);
        values.put(BambulkackaContract.TbExercises.COLUMN_NAME_DATE_START, date_start);
        values.put(BambulkackaContract.TbExercises.COLUMN_NAME_DATE_END, date_end);
        long id = db.insert(BambulkackaContract.TbExercises.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public long insertExerciseStart(String sign, String date_start) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BambulkackaContract.TbExercises.COLUMN_NAME_SIGN, sign);
        values.put(BambulkackaContract.TbExercises.COLUMN_NAME_DATE_START, date_start);
        long id = db.insert(BambulkackaContract.TbExercises.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public long updateExerciseEnd(long exercise_id, String date_end) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BambulkackaContract.TbExercises.COLUMN_NAME_DATE_END, date_end);
        String whereClause = "_id=?";
        String[] whereArgs = new String[]{String.valueOf(exercise_id)};
        long id = db.update (BambulkackaContract.TbExercises.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
        return id;
    }

    public long insertExample(long exercise_id, int a, int b, String sign, int result) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BambulkackaContract.TbExamples.COLUMN_NAME_EXERCISE_ID, exercise_id);
        values.put(BambulkackaContract.TbExamples.COLUMN_NAME_A, a);
        values.put(BambulkackaContract.TbExamples.COLUMN_NAME_B, b);
        values.put(BambulkackaContract.TbExamples.COLUMN_NAME_SIGN, sign);
        values.put(BambulkackaContract.TbExamples.COLUMN_NAME_RESULT, result);
        long id = db.insert(BambulkackaContract.TbExamples.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public long insertAttempt(long example_id, int attempt_result, String date, int ok) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BambulkackaContract.TbAttempts.COLUMN_NAME_EXAMPLE_ID, example_id);
        values.put(BambulkackaContract.TbAttempts.COLUMN_NAME_ATTEMPT_RESULT, attempt_result);
        values.put(BambulkackaContract.TbAttempts.COLUMN_NAME_DATE, date);
        values.put(BambulkackaContract.TbAttempts.COLUMN_NAME_OK, ok);

        long id = db.insert(BambulkackaContract.TbAttempts.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public int deleteAllTables() {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.delete(BambulkackaContract.TbAttempts.TABLE_NAME, null, null);
        db.delete(BambulkackaContract.TbExamples.TABLE_NAME,null,null);
        db.delete(BambulkackaContract.TbExercises.TABLE_NAME,null,null);
        int deletedCount = db.delete(BambulkackaContract.TbExercises.TABLE_NAME,null,null);
        db.close();
        return deletedCount;
    }

    public void insertTestData() {
        insertExercise("+", Utils.getNow(), Utils.getNowPlus(5));
        insertExercise("-", Utils.getNowPlus(6), Utils.getNowPlus(10));
        insertExercise("*", Utils.getNowPlus(11), Utils.getNowPlus(15));
        insertExample(1, 2, 2, "+", 4);
        insertExample(1, 2, 3, "+", 5);
        insertExample(1, 3, 3, "+", 6);
        insertExample(2, 4, 2, "-", 2);
        insertExample(2, 8, 2, "-", 6);
        insertExample(2, 10, 2, "-", 8);
        insertAttempt(1, 4, Utils.getNow(), 1);
        insertAttempt(2, 2, Utils.getNow(), 0);
        insertAttempt(2, 5, Utils.getNow(), 1);
        insertAttempt(3, 6, Utils.getNow(), 1);
        insertAttempt(4, 2, Utils.getNow(), 1);
        insertAttempt(5, 2, Utils.getNow(), 0);
        insertAttempt(5, 6, Utils.getNow(), 1);
        insertAttempt(6, 8, Utils.getNow(), 1);
    }
    /*
    public Cursor getRawExercises() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db.rawQuery("SELECT _id,sign,date_start,date_end FROM exercises", null);
    }


    public Cursor getRawExamples() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db.rawQuery("SELECT _id,exercise_id,a,b,result,sign FROM examples", null);
    }

    public Cursor getRawAttempts() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db.rawQuery("SELECT _id,example_id,attempt_result,date,ok FROM attempts", null);
    }
    */
    public void close() {
        openHelper.close();
    }

}
