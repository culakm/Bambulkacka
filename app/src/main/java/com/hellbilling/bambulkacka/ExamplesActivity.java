package com.hellbilling.bambulkacka;

import android.content.Context;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;


public class ExamplesActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examples);
        // Back key
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Load id of the result
        long exercise_id = getIntent().getLongExtra("exercise_id", -1);

        if (savedInstanceState == null) {
            Fragment fragment = new PlaceholderFragment(exercise_id);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
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

        private long exercise_id;

        public PlaceholderFragment() {

        }

        public PlaceholderFragment(long exercise_id) {
            this.exercise_id = exercise_id;
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


//            String[] from = { BambulkackaContract.TbExamples.COLUMN_NAME_A,BambulkackaContract.TbExamples.COLUMN_NAME_B,BambulkackaContract.TbExamples.COLUMN_NAME_SIGN,BambulkackaContract.TbExamples.COLUMN_NAME_RESULT,"ok_count","nok_count" };
//            int[] to = { R.id.a,R.id.b,R.id.sign,R.id.result,R.id.ok_count,R.id.nok_count};
//            ListAdapter adapter = new SimpleCursorAdapter(ctx, R.layout.examples_row, dbh.getResultExamples(), from, to, 0);


            //getExercisesResults
//            String[] from = {"_id,sign,date_start,date_end" };
//            int[] to = { R.id.p1v,R.id.p2v,R.id.p3v,R.id.p4v};

            //getRawExercises
//            String[] from = {"_id","sign","date_start","date_end" };
//            int[] to = { R.id.p1v,R.id.p2v,R.id.p3v,R.id.p4v};


            //getRawExamples
//            String[] from = {"_id","exercise_id","a","b","result","sign" };
//            int[] to = { R.id.p1v,R.id.p2v,R.id.p3v,R.id.p4v,R.id.p5v,R.id.p6v};


            //getRawAttempts
//            String[] from = { "_id","example_id","attempt_result","date","ok" };
//            int[] to = { R.id.p1v,R.id.p2v,R.id.p3v,R.id.p4v,R.id.p5v};
//
            //getResultExamples
            String[] from = { "a","b","sign","result", "ok_count", "nok_count" };
            int[] to = { R.id.a,R.id.b,R.id.sign,R.id.result,R.id.ok_count,R.id.nok_count};
            ListAdapter adapter = new SimpleCursorAdapter(ctx, R.layout.examples_row, dbh.getResultExamples(exercise_id), from, to, 0);


/*/OK
            String[] from = { BambulkackaContract.TbExercises.COLUMN_NAME_DATE_START,"ok_count","nok_count" };
            int[] to = { R.id.date_start,R.id.ok_count,R.id.nok_count};
            ListAdapter adapter = new SimpleCursorAdapter(ctx, R.layout.results_row, dbh.getExercisesResults(), from, to, 0);
*/
            setListAdapter(adapter);

            dbh.close();
        }
    }
}
