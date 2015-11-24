package com.hellbilling.bambulkacka;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

public class SettingsActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Back key
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    // Manage back key
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}