package com.hellbilling.bambulkacka;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ResultsActivity extends FragmentActivity implements ResultsFragment.OnResultClickedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

    }

    public void onResultClicked(long id) {
        showNote(id);
    }

    private void showNote(long id){
        Intent i = new Intent(this, ExamplesActivity.class);
        i.putExtra("exercise_id", id);
        startActivity(i);
    }

}
