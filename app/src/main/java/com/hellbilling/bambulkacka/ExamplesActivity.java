package com.hellbilling.bambulkacka;

import android.content.Context;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;

// depricated http://stackoverflow.com/questions/29890530/actionbaractivity-is-deprecated-android-studio
public class ExamplesActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        long exercise_id = getIntent().getLongExtra("exercise_id", -1);
        PlaceholderFragment fragment = PlaceholderFragment.newInstance( String.valueOf(exercise_id));
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
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

        private static final String EXERCISE_ID = "exercise_id";

        // Toto nie je uplne jasne, ide tu o uchovanie argumentov pokial je aktivita znicena pri otoceni
        // vlastne je to prepisanie konstruktoru ktory prebera aj parametre, ten sa nema pouzivat
        public static PlaceholderFragment newInstance(String exercise_id) {
            Bundle bundle = new Bundle();
            bundle.putString(EXERCISE_ID, exercise_id);

            PlaceholderFragment f = new PlaceholderFragment();
            f.setArguments(bundle);

            return f;
        }

        // Load a view of this fragment
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            registerForContextMenu(getListView());
            String exercise_id = this.getArguments().getString(EXERCISE_ID);
            updateList(exercise_id);
        }

        public void updateList(String exercise_id) {
            Context ctx = getActivity();
            BambulkackaDB dbh = new BambulkackaDB(ctx);

            //getResultExamples
            String[] from = { "a","b","sign","result", "ok_count", "nok_count" };
            int[] to = { R.id.a,R.id.b,R.id.sign,R.id.result,R.id.ok_count,R.id.nok_count};
            ListAdapter adapter = new SimpleCursorAdapter(ctx, R.layout.examples_row, dbh.getResultExamples(exercise_id), from, to, 0);

            setListAdapter(adapter);

            dbh.close();
        }
    }
}
