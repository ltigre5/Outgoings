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

    /**
     * Makes a ListViewAdapter with all Data from Year and Month
     *
     * @param month Month you want to show
     * @param Year Year you want to show
     * @param OrderBy Order in Which you want to sort Data
     * @param context Context
     * @param AscendDescend If in Ascending or Descending Order
     */

    public ListViewAdapter(Integer month, Integer Year, String OrderBy, Context context, String AscendDescend) {
        this.context = context;
        this.cursor = MainActivity.myDbMain.getRowWithMonthYear(month, Year, OrderBy, AscendDescend, null);

    }

    /**
     * Makes a ListViewAdapter with all Data in Database
     *
     * @param context Context
     */

    public ListViewAdapter(Context context) {
        this.context = context;
        this.cursor = MainActivity.myDbMain.getAllRows();

    }

    // Constructor
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Set Cursor

    /**
     * Set ListViewAdapter for Data from Year and Month
     *
     * @param month Month you want to show
     * @param Year Year you want to show
     * @param OrderBy Order in Which you want to sort Data
     * @param AscendDescend If in Ascending or Descending Order
     * @param hide If you want to hide Repeated or single, enter null if you want to show all
     */

    public void setCursorMonthYear(Integer month, Integer Year, String OrderBy, String AscendDescend, String hide) {
        this.cursor = MainActivity.myDbMain.getRowWithMonthYear(month, Year, OrderBy, AscendDescend, hide);
    }

    /**
     * Set ListViewAdapter for Data from Year
     *
     * @param Year Year you want to show
     * @param OrderBy Order in Which you want to sort Data
     * @param AscendDescend If in Ascending or Descending Order
     * @param hide If you want to hide Repeated or single, enter null if you want to show all
     */

    public void setCursorYear(Integer Year, String OrderBy, String AscendDescend, String hide) {
        this.cursor = MainActivity.myDbMain.getRowWithYear(Year, OrderBy, AscendDescend, hide);
    }

    /**
     * Set ListViewAdapter for Data from a beginning Date until End Date
     *
     * @param startDateWithoutTime start date to show Data
     * @param endDateWithoutTime end date to show data
     * @param OrderBy Order in Which you want to sort Data
     * @param AscendDescend If in Ascending or Descending Order
     * @param hide If you want to hide Repeated or single, enter null if you want to show all
     */

    public void setCursorStartEndDate(int startDateWithoutTime, int endDateWithoutTime, String OrderBy, String AscendDescend, String hide) {
        this.cursor = MainActivity.myDbMain.getRowWithStartEndDay(startDateWithoutTime, endDateWithoutTime, OrderBy, AscendDescend, hide);
    }

    /**
     * Set ListViewAdapter for Datas from a specific Date
     *
     * @param dateWithoutTime the date without time to show data
     */

    public void setCursorDate(int dateWithoutTime) {
        this.cursor = MainActivity.myDbMain.getRowWithDate(dateWithoutTime);
    }

    /**
     * Set ListViewAdapter for Data from all Repeated Dates, but only get one Instance of the repeated Dates
     *
     */

    public void setCursorAllRepeatedNotDuplicate() {
        this.cursor = MainActivity.myDbMain.getAllRowsRepeatedNotDuplicated();
    }

    // Set Cursor
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // get Cursor

    /**
     * Get the Cursor for the selected Data's
     *
     * @return Cursor
     */

    public Cursor getCursor() {
        return cursor;
    }

    // get Cursor
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // GetAdapters

    /**
     * Get the ListViewAdapter for the selected Data's
     *
     * @return ListViewAdapter
     */

    public CustomCursorAdapter getListViewAdapter() {
        String[] fromFieldsName = new String[]{};
        int[] toViewsID = new int[]{};
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(context, 0, cursor, fromFieldsName, toViewsID, 0);
        return customCursorAdapter;
    }

    // GetAdapters
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
