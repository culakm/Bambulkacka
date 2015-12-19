package com.hellbilling.bambulkacka;

import android.provider.BaseColumns;

public final class BambulkackaContract {

    // If you change the database schema, you must increment the database version.
    public static final  int    DATABASE_VERSION   = 1;
    public static final String DATABASE_NAME = "Bambulkacka.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private BambulkackaContract() {}

    /* Inner class that defines the table contents */
    public static abstract class CalcExercises implements BaseColumns {
        public static final String TABLE_NAME = "exercises";
        public static final String COLUMN_NAME_EXERCISE_ID = "exercise_id";
        public static final String COLUMN_NAME_DATE_START = "date_start";
        public static final String COLUMN_NAME_DATE_END = "date_end";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + CalcExercises.TABLE_NAME + " (" +
                        CalcExercises._ID + " INTEGER PRIMARY KEY," +
                        CalcExercises.COLUMN_NAME_EXERCISE_ID + TEXT_TYPE + COMMA_SEP +
                        CalcExercises.COLUMN_NAME_DATE_START + TEXT_TYPE + COMMA_SEP +
                        CalcExercises.COLUMN_NAME_DATE_END + TEXT_TYPE +
                        " )";

        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + CalcExercises.TABLE_NAME;
    }

    public static abstract class CalcExamples implements BaseColumns {
        public static final String TABLE_NAME = "examples";
        public static final String COLUMN_NAME_EXAMPLE_ID = "example_id";
        public static final String COLUMN_NAME_EXERCISE_ID = "exercise_id";
        public static final String COLUMN_NAME_A = "a";
        public static final String COLUMN_NAME_B = "b";
        public static final String COLUMN_NAME_RESULT = "result";
        public static final String COLUMN_NAME_SIGN = "sign";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + CalcExamples.TABLE_NAME + " (" +
                        CalcExamples._ID + " INTEGER PRIMARY KEY," +
                        CalcExamples.COLUMN_NAME_EXAMPLE_ID + TEXT_TYPE + COMMA_SEP +
                        CalcExamples.COLUMN_NAME_EXERCISE_ID + TEXT_TYPE + COMMA_SEP +
                        CalcExamples.COLUMN_NAME_A + TEXT_TYPE + COMMA_SEP +
                        CalcExamples.COLUMN_NAME_B + TEXT_TYPE + COMMA_SEP +
                        CalcExamples.COLUMN_NAME_RESULT + TEXT_TYPE + COMMA_SEP +
                        CalcExamples.COLUMN_NAME_SIGN + TEXT_TYPE +
                        " )";

        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + CalcExamples.TABLE_NAME;
    }

    public static abstract class CalcAttempts implements BaseColumns {
        public static final String TABLE_NAME = "attempts";
        public static final String COLUMN_NAME_ATTEMPT_ID = "attempt_id";
        public static final String COLUMN_NAME_EXAMPLE_ID = "example_id";
        public static final String COLUMN_NAME_ATTEMPT_RESULT = "attempt_result";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_OK = "ok"; //true - false

        public static final String CREATE_TABLE =
                "CREATE TABLE " + CalcAttempts.TABLE_NAME + " (" +
                        CalcAttempts._ID + " INTEGER PRIMARY KEY," +
                        CalcAttempts.COLUMN_NAME_ATTEMPT_ID + TEXT_TYPE + COMMA_SEP +
                        CalcAttempts.COLUMN_NAME_EXAMPLE_ID + TEXT_TYPE + COMMA_SEP +
                        CalcAttempts.COLUMN_NAME_ATTEMPT_RESULT + TEXT_TYPE + COMMA_SEP +
                        CalcAttempts.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                        CalcAttempts.COLUMN_NAME_OK + TEXT_TYPE +
                        " )";

        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + CalcAttempts.TABLE_NAME;
    }

    // CalcExercises


    // CalcExamples


    // CalcAttempts


}