package com.hellbilling.bambulkacka;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * instance of ExampleSetting is messenger (prepravka) with all atributes neccessary to create an Example
 * static methods are used to handle general data of the Examples setting (DB access etc.)
 * Created by mculak on 11/15/16.
 */
public class ExampleSetting {
    public final int resultStart;
    public final int resultStop;
    public final int numberStart;
    public final int numberStop;
    /**
     * string of signs delimited by comma
     */
    public final String sign;
    public final String extra;

    /**
     * Retrurns HashMap (_id, descr) containing CharSequences of all records from examples_setting
     * for MultiSelectListPreference in settings
     *
     * @param ctx
     * @return HashMap, map.get(menu_order),map(descr)
     */
    static public HashMap getExamplesSettingAllKeyText(Context ctx){

        BambulkackaDB dbh = new BambulkackaDB(ctx);

        // Get database data in Cursor
        Cursor cursor = dbh.getExamplesSettingAllKeyText();
        // List of strings for preferenceExamplesSetting
        List<String> listItemsReadable = new ArrayList<String>();
        List<String> listItemsValue = new ArrayList<String>();

        if (cursor.moveToFirst()) {
            do {
                //labels.add(cursor.getString(1));
                String _id = cursor.getString(cursor.getColumnIndex("_id"));
                String descr = cursor.getString(cursor.getColumnIndex("descr"));
//                Toast.makeText(ctx, "HAHA " + _id + " = " + descr, Toast.LENGTH_SHORT).show();
//                Log.d("myTag", "HAHA " + _id + " = " + descr);
                listItemsReadable.add(descr);
                listItemsValue.add(_id);
            } while (cursor.moveToNext());
        }
        cursor.close();
        dbh.close();
//        Toast.makeText(ctx, "List toString " + listItemsKey.toString(), Toast.LENGTH_SHORT).show();
//        Log.d("myTag", "List toString " + listItemsKey.toString());
        final CharSequence[] charSequenceItemsReadable = listItemsReadable.toArray(new CharSequence[listItemsReadable.size()]);
        final CharSequence[] charSequenceItemsValue = listItemsValue.toArray(new CharSequence[listItemsValue.size()]);


        //CharSequence[] outArray [] = {charSequenceItemsReadable,charSequenceItemsValue};
        HashMap<String, CharSequence[]> map = new HashMap<String, CharSequence[]>();
        map.put("_id", charSequenceItemsValue);
        map.put("descr", charSequenceItemsReadable);
        return map;
        //return charSequenceItemsReadable;
    }

    public ExampleSetting(int resultStart, int resultStop, int numberStart, int numberStop, String sign, String extra){
        this.resultStart = resultStart;
        this.resultStop = resultStop;
        this.numberStart = resultStop;
        this.numberStop = resultStop;
        this.sign = sign;
        this.extra = extra;
    }
}
