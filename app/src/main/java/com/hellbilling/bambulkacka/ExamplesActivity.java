package com.hellbilling.bambulkacka;

import android.content.Context;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;


public class ExamplesActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examples);
        // Back key
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_examples, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // All menu cliks
        switch (item.getItemId()) {
            case R.id.action_settings:
//                intent = new Intent(this, ExamplesActivity.class);
//                startActivity(intent);
                return(true);
            case android.R.id.home:
                onBackPressed();
                return(true);
            default:
                return(true);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends ListFragment {

        public PlaceholderFragment() {
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            registerForContextMenu(getListView());
            updateList();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_examples, container, false);
            return rootView;
        }

        public void updateList() {
            Context ctx = getActivity();
            BambulkackaDB dbh = new BambulkackaDB(ctx);

//        String[] from = { BambulkackaContract.TbExercises.COLUMN_NAME_DATE_START,"nok_count" };
//        int[] to = { android.R.id.text1, android.R.id.text2 };

            String[] from = { BambulkackaContract.TbExercises.COLUMN_NAME_DATE_START,"ok_count","nok_count" };
            int[] to = { R.id.date_start,R.id.ok_count,R.id.nok_count};


            // toto tu simple_list_item_1 treba zmenit na custom row.xml kde budu definovane vsetky veci ohladom riadku zoznamu
            ListAdapter adapter = new SimpleCursorAdapter(ctx, R.layout.results_row, dbh.getExercisesResults(), from, to, 0);

            setListAdapter(adapter);

            dbh.close();
        }
    }
}
