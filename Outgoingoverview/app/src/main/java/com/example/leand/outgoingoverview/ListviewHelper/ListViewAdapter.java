package com.example.leand.outgoingoverview.ListviewHelper;

import android.content.Context;
import android.database.Cursor;

import com.example.leand.outgoingoverview.Activitys.MainActivity;
import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.R;

public class ListViewAdapter {
    private Context context;
    private Cursor cursor;

    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor

    public ListViewAdapter(Integer integer_month, Integer integer_Year, String string_OrderBy, Context context, String string_AscendDescend) {
        this.context = context;
        this.cursor = MainActivity.myDbMain.getRowWithMonthYear(integer_month, integer_Year, string_OrderBy, string_AscendDescend);
    }

    public ListViewAdapter(Context context) {
        this.context = context;
        this.cursor = MainActivity.myDbMain.getAllRows();
    }

    // Constructor
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Set Cursor

    public void setCursorMonthYear(Integer integer_month, Integer integer_Year, String string_OrderBy, String string_AscendDescend) {
        this.cursor = MainActivity.myDbMain.getRowWithMonthYear(integer_month, integer_Year, string_OrderBy, string_AscendDescend);
    }

    public void setCursorYear(Integer integer_Year, String string_OrderBy, String string_AscendDescend) {
        this.cursor = MainActivity.myDbMain.getRowWithYear(integer_Year, string_OrderBy, string_AscendDescend);
    }

    public void setCursorStartEndDate(int startDateWithoutTime, int endDateWithoutTime, String string_OrderBy, String string_AscendDescend) {
        this.cursor = MainActivity.myDbMain.getRowWithStartEndDay(startDateWithoutTime, endDateWithoutTime, string_OrderBy, string_AscendDescend);
    }

    public void setCursorDate(int dateWithoutTime) {
        this.cursor = MainActivity.myDbMain.getRowWithDate(dateWithoutTime);
    }

    public void setCursorAllWithEndDateNotDuplicate() {
        this.cursor = MainActivity.myDbMain.getAllRowsRepeatedItemNotDuplicated();
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
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(context, R.layout.adapter_view_list, cursor, fromFieldsName, toViewsID, 0);
        return customCursorAdapter;
    }

    // GetAdapters
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
