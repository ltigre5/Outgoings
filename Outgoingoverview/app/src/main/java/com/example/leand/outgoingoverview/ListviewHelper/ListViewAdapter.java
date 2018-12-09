package com.example.leand.outgoingoverview.ListviewHelper;

import android.content.Context;
import android.database.Cursor;

import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.R;

public class ListViewAdapter {
    private DBAdapter myDb;
    private Context context;
    private Cursor cursor;
    private String string_Currency;
    public static final String ASCENDING = " ASC";
    public static final String DESCENDING = " DESC";

    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor


    public ListViewAdapter(Integer integer_month, Integer integer_Year, String string_OrderBy, String string_Currency, Context context, String string_AscendDescend) {
        this.context = context;
        openDB();
        this.string_Currency = string_Currency;
        this.cursor = myDb.getRowWithMonthYear(integer_month, integer_Year, string_OrderBy, string_AscendDescend);
    }

    // Constructor
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Database methods

    private void openDB() {
        myDb = new DBAdapter(context);
        myDb.open();
    }

    public void closeDB() {
        myDb.close();
    }

    // Database methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Set Cursor

    public void setCursorMonthYear(Integer integer_month, Integer integer_Year, String string_OrderBy, String string_AscendDescend) {
        this.cursor = myDb.getRowWithMonthYear(integer_month, integer_Year, string_OrderBy, string_AscendDescend);
    }

    public void setCursorYear(Integer integer_Year, String string_OrderBy, String string_AscendDescend) {
        this.cursor = myDb.getRowWithYear(integer_Year, string_OrderBy, string_AscendDescend);
    }

    public void setCursorStartEndDate(long longRowStartDate, long longRowEndDate, String string_OrderBy, String string_AscendDescend) {
        this.cursor = myDb.getRowWithStartEndDay(longRowStartDate, longRowEndDate, string_OrderBy, string_AscendDescend);
    }

    public void setCursorDate(int dateWithoutTime) {
        this.cursor = myDb.getRowWithDate(dateWithoutTime);
    }

    // Set Cursor
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // get Cursor

    public Cursor getCursor() {
        return cursor;
    }

    // get Cursor
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // GetAdapters

    public CustomCursorAdapter getListViewAdapter() {
        String[] fromFieldsName = new String[]{DBAdapter.KEY_ROWID, DBAdapter.KEY_DATE, DBAdapter.KEY_VALUE};
        int[] toViewsID = new int[]{};
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(context, R.layout.adapter_view_list, cursor, fromFieldsName, toViewsID, 0, string_Currency);
        return customCursorAdapter;
    }
    // GetAdapters
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
