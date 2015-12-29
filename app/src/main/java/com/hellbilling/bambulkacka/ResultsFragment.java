package com.hellbilling.bambulkacka;

import android.app.Activity;
import android.content.Context;
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

        String[] from = { BambulkackaContract.TbExercises.COLUMN_NAME_DATE_START,BambulkackaContract.TbExercises.COLUMN_NAME_DATE_END,"ok_count","nok_count" };
        int[] to = { R.id.date_start,R.id.date_end,R.id.ok_count,R.id.nok_count};

        // Definition of the row, SELECT here has to have _id !!!!
        ListAdapter adapter = new SimpleCursorAdapter(ctx, R.layout.results_row, dbh.getExercisesResults(), from, to, 0);

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