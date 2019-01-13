package com.example.leand.outgoingoverview.GeneralHelperClasses;

import android.database.Cursor;

import com.example.leand.outgoingoverview.Activitys.MainActivity;
import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;

/**
 * This class help's to get the right value out of the Database, with the ID of the row or with a cursor
 */

public class EditHelper {
    private Cursor cursor;
    private GeneralHelper generalHelper;

    /**
     * Set the Data's to get with a ID
     *
     * @param id ID of row which you want to get Data
     */

    public EditHelper(Integer id) {
        cursor = MainActivity.myDbMain.getRowWithID(id);
        generalHelper = new GeneralHelper();
    }

    /**
     * Set the Data's to get with a cursor
     *
     * @param cursor cursor of row which you want to get Data
     */

    public EditHelper(Cursor cursor) {
        this.cursor = cursor;
    }

    /**
     * Set the cursor of the row you want to get Data
     *
     * @param cursor cursor of row which you want to get Data
     */

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    /**
     * get the Date in long (milliseconds since epoch)
     *
     * @return Date in milliseconds
     */

    public long getLongDate() {
        return cursor.getLong(cursor.getColumnIndexOrThrow(DBAdapter.KEY_DATE));
    }

    /**
     * get the start date in long (milliseconds since epoch)
     *
     * @return start date in milliseconds
     */

    public long getLongStartDate() {
        return cursor.getLong(cursor.getColumnIndexOrThrow(DBAdapter.KEY_START_DATE));
    }

    /**
     * get the end date in long (milliseconds since epoch)
     *
     * @return end date in milliseconds
     */

    public long getLongEndDate() {
        return cursor.getLong(cursor.getColumnIndexOrThrow(DBAdapter.KEY_END_DATE));
    }

    /**
     * get the value in double
     *
     * @return value in double
     */

    public double getDoubleValue() {
        return cursor.getDouble(cursor.getColumnIndexOrThrow(DBAdapter.KEY_VALUE));
    }

    /**
     * get the value in string
     *
     * @return value in string
     */

    public String getStringValue() {
        return generalHelper.currencyFormat.format(cursor.getDouble(cursor.getColumnIndexOrThrow(DBAdapter.KEY_VALUE)));
    }

    /**
     * get the title in string
     *
     * @return title in string
     */

    public String getStringTitle() {
        return cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_TITLE));
    }

    /**
     * get the description in string
     *
     * @return description in string
     */

    public String getStringDescription() {
        return cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_DESCRIPTION));
    }

    /**
     * get the color of the title in int
     *
     * @return color of title in int
     */

    public int getIntTitleColor() {
        return cursor.getInt(cursor.getColumnIndexOrThrow(DBAdapter.KEY_TITLE_COLOR));
    }

    /**
     * get the int of Repeated by how many times
     *
     * @return int of Repeated by how many times
     */

    public int getIntRepeatedByTimes() {
        return cursor.getInt(cursor.getColumnIndexOrThrow(DBAdapter.KEY_REPEATED_BY_TIMES));
    }

    /**
     * get the int of every, 0=year, 1=month, 2=week, 3=day
     *
     * @return int of every
     */

    public int getIntEvery() {
        return cursor.getInt(cursor.getColumnIndexOrThrow(DBAdapter.KEY_EVERY));
    }

    /**
     * get the string if it is a repeated or single expense
     *
     * @return string if it is a repeated or single expense
     */

    public String getStringSingleRepeated() {
        return cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_SINGLE_OR_REPEATED));
    }

}
