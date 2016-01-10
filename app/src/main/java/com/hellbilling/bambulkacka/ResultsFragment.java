package com.hellbilling.bambulkacka;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ResultsFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_results, container, false);
        return view;
    }


    OnResultClickedListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnResultClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnResultClickedListener");
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
        updateList();
    }

    public void updateList() {
        Context ctx = getActivity();
        BambulkackaDB dbh = new BambulkackaDB(ctx);

        // Get database data in Cursor
        Cursor exercisesCursor = dbh.getExercisesResults();

        // Define columns for MatrixCursor
        String[] columns = new String[] { "_id", "date_start", "ok_count", "nok_count", "minutes" };

        // This is updated cursor that will be used in SimpleCursorAdapter instead of simple exercisesCursor
        MatrixCursor matrixCursor= new MatrixCursor(columns);

        // if Cursor is contains results
        if (exercisesCursor != null) {
            // move cursor to first row
            if (exercisesCursor.moveToFirst()) {
                do {
                    // Original Cursor data
                    String _id = exercisesCursor.getString(exercisesCursor.getColumnIndex("_id"));
                    String date_start = exercisesCursor.getString(exercisesCursor.getColumnIndex("date_start"));
                    String date_end = exercisesCursor.getString(exercisesCursor.getColumnIndex("date_end"));
                    String ok_count = exercisesCursor.getString(exercisesCursor.getColumnIndex("ok_count"));
                    String nok_count = exercisesCursor.getString(exercisesCursor.getColumnIndex("nok_count"));
                    // New data for the Cursor
                    String minutes = Utils.timeDiff(date_start,date_end);
                    // Add data
                    matrixCursor.addRow(new Object[] { _id, date_start, ok_count, nok_count, minutes });
                    // move to next row
                } while (exercisesCursor.moveToNext());
            }
        }

        // Prepare input
        // Definition of the row, SELECT here has to have _id !!!!
        String[] from = { BambulkackaContract.TbExercises.COLUMN_NAME_DATE_START,"ok_count","nok_count","minutes" };
        // Prepare output
        int[] to = { R.id.date_start,R.id.ok_count,R.id.nok_count,R.id.minutes};

        // Create adapter for List
        ListAdapter adapter = new SimpleCursorAdapter(ctx, R.layout.results_row, matrixCursor, from, to, 0);

        setListAdapter(adapter);

        dbh.close();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        listener.onResultClicked(id);
    }

    public static interface OnResultClickedListener {
        public void onResultClicked(long id);
    }



/*
    @Override
    public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, MENU_DELETE_ID, 0, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case MENU_DELETE_ID:
                deleteNote(info.id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteNote(long id){
        Context ctx = getActivity();
        Notes notes = new Notes(ctx);

        if(notes.deleteNote(id)){
            Toast.makeText(ctx, R.string.note_deleted, Toast.LENGTH_SHORT).show();
            updateList();
        } else{
            Toast.makeText(ctx, R.string.note_not_deleted, Toast.LENGTH_SHORT).show();
        }
    }

    public static interface OnResultClickedListener {
        public void onNoteClicked(long id);
    }

*/
}