package com.hellbilling.bambulkacka;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class ResultsActivity extends ActionBarActivity implements ResultsFragment.OnResultClickedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
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

    public void onResultClicked(long id) {
        showNote(id);
    }

    private void showNote(long id){
        Intent i = new Intent(this, ExamplesActivity.class);
        i.putExtra("exercise_id", id);
        startActivity(i);
    }

}
