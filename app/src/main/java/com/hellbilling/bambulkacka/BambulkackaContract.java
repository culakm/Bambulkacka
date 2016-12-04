package com.hellbilling.bambulkacka;

import android.provider.BaseColumns;

final class BambulkackaContract {

    // If you change the database schema, you must increment the database version.
    public static final  int    DATABASE_VERSION   = 6;
    public static final String DATABASE_NAME = "Bambulkacka.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private BambulkackaContract() {}

    /* Inner class that defines the table contents */
    public static abstract class TbExercises implements BaseColumns {
        public static final String TABLE_NAME = "exercises";
        // _ID is default
        public static final String COLUMN_NAME_SIGN = "sign";
        public static final String COLUMN_NAME_DATE_START = "date_start";
        public static final String COLUMN_NAME_DATE_END = "date_end";

        public static final String[] COLUMNS = { "_id", COLUMN_NAME_SIGN, COLUMN_NAME_DATE_START, COLUMN_NAME_DATE_END };

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TbExercises.TABLE_NAME + " (" +
                        TbExercises._ID + " INTEGER PRIMARY KEY," +
                        TbExercises.COLUMN_NAME_SIGN + TEXT_TYPE + COMMA_SEP +
                        TbExercises.COLUMN_NAME_DATE_START + TEXT_TYPE + COMMA_SEP +
                        TbExercises.COLUMN_NAME_DATE_END + TEXT_TYPE +
                        " )";

        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TbExercises.TABLE_NAME;
    }

    public static abstract class TbExamples implements BaseColumns {
        public static final String TABLE_NAME = "examples";
        public static final String COLUMN_NAME_EXERCISE_ID = "exercise_id";
        public static final String COLUMN_NAME_A = "a";
        public static final String COLUMN_NAME_B = "b";
        public static final String COLUMN_NAME_RESULT = "result";
        public static final String COLUMN_NAME_SIGN = "sign";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TbExamples.TABLE_NAME + " (" +
                        TbExamples._ID + " INTEGER PRIMARY KEY," +
                        TbExamples.COLUMN_NAME_EXERCISE_ID + INTEGER_TYPE + COMMA_SEP +
                        TbExamples.COLUMN_NAME_A + TEXT_TYPE + COMMA_SEP +
                        TbExamples.COLUMN_NAME_B + TEXT_TYPE + COMMA_SEP +
                        TbExamples.COLUMN_NAME_RESULT + TEXT_TYPE + COMMA_SEP +
                        TbExamples.COLUMN_NAME_SIGN + TEXT_TYPE +
                        " )";

        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TbExamples.TABLE_NAME;
    }

    public static abstract class TbAttempts implements BaseColumns {
        public static final String TABLE_NAME = "attempts";
        public static final String COLUMN_NAME_EXAMPLE_ID = "example_id";
        public static final String COLUMN_NAME_ATTEMPT_RESULT = "attempt_result";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_OK = "ok"; //true - false

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TbAttempts.TABLE_NAME + " (" +
                        TbAttempts._ID + " INTEGER PRIMARY KEY," +
                        TbAttempts.COLUMN_NAME_EXAMPLE_ID + TEXT_TYPE + COMMA_SEP +
                        TbAttempts.COLUMN_NAME_ATTEMPT_RESULT + TEXT_TYPE + COMMA_SEP +
                        TbAttempts.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                        TbAttempts.COLUMN_NAME_OK + TEXT_TYPE +
                        " )";

        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TbAttempts.TABLE_NAME;
    }

    public static abstract class TbExampleSettings implements BaseColumns {
        public static final String TABLE_NAME = "example_settings";
        public static final String COLUMN_NAME_ORDER = "menu_order";
        public static final String COLUMN_NAME_RESULT_START = "result_start";
        public static final String COLUMN_NAME_RESULT_STOP = "result_stop";
        public static final String COLUMN_NAME_NUMBER_START = "number_start";
        public static final String COLUMN_NAME_NUMBER_STOP = "number_stop";
        public static final String COLUMN_NAME_SIGNS_STRING = "signs_string";
        public static final String COLUMN_NAME_EXTRA = "extra";
        public static final String COLUMN_NAME_LONG_NAME = "long_name";
        public static final String COLUMN_NAME_DESCR = "descr";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TbExampleSettings.TABLE_NAME + " (" +
                        TbExampleSettings._ID + " INTEGER PRIMARY KEY," +
                        TbExampleSettings.COLUMN_NAME_ORDER + INTEGER_TYPE + COMMA_SEP +
                        TbExampleSettings.COLUMN_NAME_RESULT_START + INTEGER_TYPE + COMMA_SEP +
                        TbExampleSettings.COLUMN_NAME_RESULT_STOP + INTEGER_TYPE + COMMA_SEP +
                        TbExampleSettings.COLUMN_NAME_NUMBER_START + INTEGER_TYPE + COMMA_SEP +
                        TbExampleSettings.COLUMN_NAME_NUMBER_STOP + INTEGER_TYPE + COMMA_SEP +
                        TbExampleSettings.COLUMN_NAME_SIGNS_STRING + TEXT_TYPE + COMMA_SEP +
                        TbExampleSettings.COLUMN_NAME_EXTRA + TEXT_TYPE + COMMA_SEP +
                        TbExampleSettings.COLUMN_NAME_LONG_NAME + TEXT_TYPE + COMMA_SEP +
                        TbExampleSettings.COLUMN_NAME_DESCR + TEXT_TYPE +
                        " )";

        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TbExampleSettings.TABLE_NAME;


        //INSERT INTO table_name (column1,column2,column3,...) VALUES (value1,value2,value3,...);
        private static final String INSERT_COLUMNS_STRING = " (" +
                TbExampleSettings.COLUMN_NAME_ORDER + COMMA_SEP +
                TbExampleSettings.COLUMN_NAME_RESULT_START + COMMA_SEP +
                TbExampleSettings.COLUMN_NAME_RESULT_STOP + COMMA_SEP +
                TbExampleSettings.COLUMN_NAME_NUMBER_START + COMMA_SEP +
                TbExampleSettings.COLUMN_NAME_NUMBER_STOP + COMMA_SEP +
                TbExampleSettings.COLUMN_NAME_SIGNS_STRING + COMMA_SEP +
                TbExampleSettings.COLUMN_NAME_EXTRA + COMMA_SEP +
                TbExampleSettings.COLUMN_NAME_LONG_NAME + COMMA_SEP +
                TbExampleSettings.COLUMN_NAME_DESCR +
                " ) ";
        // Insert types of examples
        public static final String INSERT_EXAMPLES_SETTING =
        "INSERT INTO " + TbExampleSettings.TABLE_NAME + INSERT_COLUMNS_STRING +
                " SELECT 1 AS " + TbExampleSettings.COLUMN_NAME_ORDER + COMMA_SEP +
                " 0 AS " + TbExampleSettings.COLUMN_NAME_RESULT_START + COMMA_SEP +
                " 10 AS " + TbExampleSettings.COLUMN_NAME_RESULT_STOP + COMMA_SEP +
                " 0 AS " + TbExampleSettings.COLUMN_NAME_NUMBER_START + COMMA_SEP +
                " 10 AS " + TbExampleSettings.COLUMN_NAME_NUMBER_STOP + COMMA_SEP +
                " '+,-' AS " + TbExampleSettings.COLUMN_NAME_SIGNS_STRING + COMMA_SEP +
                " '10+-' AS " + TbExampleSettings.COLUMN_NAME_EXTRA + COMMA_SEP +
                " '+,- do 10' " + TbExampleSettings.COLUMN_NAME_LONG_NAME + COMMA_SEP +
                " '10+-' AS " + TbExampleSettings.COLUMN_NAME_DESCR +
                " UNION ALL SELECT 2,0,20,0,20,'+,-','20notOver10','+,- do 20 bez prechodu cez 10','20notOver10'"+
                " UNION ALL SELECT 3,0,20,0,20,'+,-','20Over10','+,- do 20 s prechodom cez 10','20Over10'"+
                " UNION ALL SELECT 4,10,100,0,100,'+,-','100notOver10','+,- do 100 bez prechodu cez 10','100notOver10'"+
                " UNION ALL SELECT 5,10,100,0,100,'+,-','100with1Over10','+,- do 100 s jednotkami a prechodom cez 10','100with1Over10'"+
                " UNION ALL SELECT 6,10,100,0,100,'+,-','100with10oneWhole10','+,- do 100 obe desiatky, jedna cela desiatka','100with10oneWhole10'"+
                " UNION ALL SELECT 7,10,100,0,100,'+,-','100with10notOver10','+,- do 100 obe desiatky bez prechodu cez 10','100with10notOver10'"
                ;
    }
}