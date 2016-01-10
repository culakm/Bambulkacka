package com.hellbilling.bambulkacka;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import javax.xml.transform.Result;

public class ResultsActivity extends ActionBarActivity implements ResultsFragment.OnResultClickedListener {

    // ResultFragment that will be updated after delete and insert
    // this way can be used only when the fragment is defided as static in activity_results.xml
    private ResultsFragment resultsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        // Find ResultActivity fragment
        resultsFragment = (ResultsFragment)getSupportFragmentManager().findFragmentById(R.id.results_list);
        // Back key
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // All menu cliks
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return(true);
            default:
                return(true);
        }
    }

    public void onDeleteResultsClicked(View view) {
        Context ctx = getApplicationContext();
        BambulkackaDB dbh = new BambulkackaDB(ctx);
        int deletedResults = dbh.deleteAllTables();
        Toast.makeText(ctx, "All " + deletedResults + " results deleted", Toast.LENGTH_SHORT).show();
        resultsFragment.updateList();
    }

    public void onInsertResultsClicked(View view) {
        Context ctx = getApplicationContext();
        BambulkackaDB dbh = new BambulkackaDB(ctx);
        dbh.insertTestData();
        Toast.makeText(ctx, "Data loaded", Toast.LENGTH_SHORT).show();
        resultsFragment.updateList();
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
