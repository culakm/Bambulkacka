package com.hellbilling.bambulkacka;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class ResultsActivity extends ActionBarActivity {

    TextView textmojkonr;
    BambulkackaDbHelper dbHelper;
    SQLiteDatabase db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        textmojkonr =(TextView)findViewById(R.id.tttt);
        textmojkonr.setText("pako");

        // Create new helper
        dbHelper = new BambulkackaDbHelper(this);
        // Get the database. If it does not exist, this is where it will
        // also be created.
        db = dbHelper.getWritableDatabase();//getReadableDatabase()

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buttonSendsMessageInsert(View view) {


        // Create insert entries
        ContentValues values = new ContentValues();
        values.put(BambulkackaContract.CalcExercises.COLUMN_NAME_EXERCISE_ID, "1");
        values.put(BambulkackaContract.CalcExercises.COLUMN_NAME_DATE_START, "startttt");
        values.put(BambulkackaContract.CalcExercises.COLUMN_NAME_DATE_END, "endddd");

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                BambulkackaContract.CalcExercises.TABLE_NAME,
                null,
                values);

        textmojkonr.setText("Inserted" + newRowId);
    }

    public void buttonSendsMessageSelect(View view) {


        db = dbHelper.getWritableDatabase();

        String[] projection = {
                BambulkackaContract.CalcExercises._ID,
                BambulkackaContract.CalcExercises.COLUMN_NAME_EXERCISE_ID,
                BambulkackaContract.CalcExercises.COLUMN_NAME_DATE_START,
                BambulkackaContract.CalcExercises.COLUMN_NAME_DATE_END,
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                BambulkackaContract.CalcExercises.COLUMN_NAME_EXERCISE_ID + " DESC";

        Cursor c = db.query(
                BambulkackaContract.CalcExercises.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // selection - The columns for the WHERE clause
                null,                            // selectionArgs - The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        c.moveToLast();
        long itemId = c.getLong(
                c.getColumnIndexOrThrow(BambulkackaContract.CalcExercises._ID)
        );

        textmojkonr.setText("select" + itemId);
    }
}
