package com.example.leand.outgoingoverview.Classes;

import android.database.Cursor;

import com.example.leand.outgoingoverview.Activitys.MainActivity;
import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.ListviewHelper.ListViewAdapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import static com.example.leand.outgoingoverview.Activitys.MainActivity.myDbMain;

public class GeneralHelper {
    //Date formatter
    public SimpleDateFormat longDateToDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
    public SimpleDateFormat longDateToDateWithoutTime = new SimpleDateFormat("yyyyMMdd");
    public SimpleDateFormat longDateToDayNumber = new SimpleDateFormat("dd");
    public SimpleDateFormat longDateToMonthNumber = new SimpleDateFormat("MM");
    public SimpleDateFormat longDateToMonthWriteOut = new SimpleDateFormat("MMMM");
    public SimpleDateFormat longDateToYearNumber = new SimpleDateFormat("yyyy");
    public SimpleDateFormat longDateToWeekday = new SimpleDateFormat("EEEE");
    public DecimalFormat currencyFormat = new DecimalFormat("#0.00");

    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor

    public GeneralHelper() {
    }

    // Constructor
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // General Methods

    //gets Currency out of Database
    public String getCurrency() {
        String string_Currency;
        Cursor cursor = myDbMain.getAllRows();
        if (cursor.moveToFirst() && !cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_CURRENCY)).equals(null)) {
            string_Currency = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_CURRENCY));
        } else {
            string_Currency = "";
        }
        cursor.close();
        return string_Currency;
    }

    //sums all values of the selected Month
    public Double sumAllValues(ListViewAdapter listViewAdapter) {
        Cursor cursor = listViewAdapter.getCursor();
        Double doubleTotalValue = 0.0;

        if (cursor.moveToFirst()) {
            do {
                doubleTotalValue += cursor.getDouble(DBAdapter.COL_VALUE);
            } while (cursor.moveToNext());
        } else doubleTotalValue = 0.0;


        return doubleTotalValue;
    }

    //sums the value of the selected date
    public double sumAllValuesOfSelectedDay(SelectedDate selectedDate) {
        Cursor cursor = myDbMain.getRowWithDate(selectedDate.getInteger_DateWithoutTime());
        double doubleAllValuesOfSelectedDate = 0.0;

        if (cursor.moveToFirst()) {
            do {
                doubleAllValuesOfSelectedDate += cursor.getDouble(DBAdapter.COL_VALUE);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return doubleAllValuesOfSelectedDate;
    }

    //sums all values of the selected Month
    public Double sumAllValuesOfSelectedMonth(SelectedDate selectedDate) {
        Cursor cursor = myDbMain.getRowWithMonthYear(selectedDate.getInteger_Month(), selectedDate.getInteger_Year());
        Double doubleTotalValue = 0.0;

        if (cursor.moveToFirst()) {
            do {
                doubleTotalValue += cursor.getDouble(DBAdapter.COL_VALUE);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return doubleTotalValue;
    }

    // General Methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End

}
