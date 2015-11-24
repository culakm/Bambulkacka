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

        // Pass preferencesType to SettingsFragment
        // Get preferencesType
        String preferencesType = getIntent().getExtras().getString("preferencesType");
        // Add agrument for SettingsFragment
        Bundle bundle = new Bundle();
        bundle.putString("preferencesType", preferencesType );
        SettingsFragment mySettingsFragment = new SettingsFragment();
        mySettingsFragment.setArguments(bundle);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, mySettingsFragment)
                .commit();
    }

    // Manage back key
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}