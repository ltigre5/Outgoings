package com.example.leand.outgoingoverview.GeneralHelperClasses;

import android.database.Cursor;

import com.example.leand.outgoingoverview.Activitys.MainActivity;
import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;

public class EditHelper {
    Cursor cursor;
    GeneralHelper generalHelper;

    public EditHelper(Integer id) {
        cursor = MainActivity.myDbMain.getRowWithID(id);
        generalHelper = new GeneralHelper();
    }

    public EditHelper(Cursor cursor) {
        this.cursor = cursor;
    }

    public void setCursor(Cursor cursor){
        this.cursor=cursor;
    }


    public long getLongDate() {
        return cursor.getLong(cursor.getColumnIndexOrThrow(DBAdapter.KEY_DATE));
    }

    public long getLongStartDate() {
        return cursor.getLong(cursor.getColumnIndexOrThrow(DBAdapter.KEY_START_DATE));
    }

    public long getLongEndDate() {
        return cursor.getLong(cursor.getColumnIndexOrThrow(DBAdapter.KEY_END_DATE));
    }

    public double getDoubleValue() {
        return cursor.getDouble(cursor.getColumnIndexOrThrow(DBAdapter.KEY_VALUE));
    }

    public String getStringValue() {
        return generalHelper.currencyFormat.format(cursor.getDouble(cursor.getColumnIndexOrThrow(DBAdapter.KEY_VALUE)));
    }

    public String getStringTitle() {
        return cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_TITLE));
    }

    public String getStringDescription() {
        return cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_DESCRIPTION));
    }

    public int getIntTitleColor() {
        return cursor.getInt(cursor.getColumnIndexOrThrow(DBAdapter.KEY_TITLE_COLOR));
    }

    public int getIntRepeatedByTimes() {
        return cursor.getInt(cursor.getColumnIndexOrThrow(DBAdapter.KEY_REPEATED_BY_TIMES));
    }

    public int getIntEvery() {
        return cursor.getInt(cursor.getColumnIndexOrThrow(DBAdapter.KEY_EVERY));
    }

    public String getStringSingleRepeated() {
        return cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_SINGLE_OR_REPEATED));
    }

}
