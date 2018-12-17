package com.example.leand.outgoingoverview.GeneralHelperClasses;

import android.content.Context;
import android.database.Cursor;

import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.ListviewHelper.ListViewAdapter;
import com.example.leand.outgoingoverview.R;

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
        Cursor cursor = myDbMain.getCurrency();
        if (cursor.moveToFirst()) {
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
                doubleTotalValue += cursor.getDouble(cursor.getColumnIndexOrThrow(DBAdapter.KEY_VALUE));
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
                doubleAllValuesOfSelectedDate += cursor.getDouble(cursor.getColumnIndexOrThrow(DBAdapter.KEY_VALUE));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return doubleAllValuesOfSelectedDate;
    }

    //sums all values of the selected Month
    public Double sumAllValuesOfSelectedMonth(SelectedDate selectedDate) {
        Cursor cursor = myDbMain.getRowWithMonthYear(selectedDate.getInteger_Month(), selectedDate.getInteger_Year(), null);
        Double doubleTotalValue = 0.0;

        if (cursor.moveToFirst()) {
            do {
                doubleTotalValue += cursor.getDouble(cursor.getColumnIndexOrThrow(DBAdapter.KEY_VALUE));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return doubleTotalValue;
    }

    //sums all values of the selected Month
    public Double sumAllValuesOfSelectedYear(SelectedDate selectedDate) {
        Cursor cursor = myDbMain.getRowWithYear(selectedDate.getInteger_Year(), null);
        Double doubleTotalValue = 0.0;

        if (cursor.moveToFirst()) {
            do {
                doubleTotalValue += cursor.getDouble(cursor.getColumnIndexOrThrow(DBAdapter.KEY_VALUE));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return doubleTotalValue;
    }

    //gets Spinner String RepeatedBy with Number
    public String getRepeatedByWithNumber(int every, Context context) {
        switch (every) {
            case 0:
                return context.getResources().getString(R.string.repeated_year);
            case 1:
                return context.getResources().getString(R.string.repeated_month);
            case 2:
                return context.getResources().getString(R.string.repeated_week);
            case 3:
                return context.getResources().getString(R.string.repeated_Day);
            default:
                return context.getResources().getString(R.string.repeated_year);
        }
    }

    // get Number to enter in Database by RepeatedBy value
    public int setRepeatedByWithNumber(String every, Context context) {
        if (context.getResources().getString(R.string.repeated_year).equals(every)) {
            return 0;

        } else if (context.getResources().getString(R.string.repeated_month).equals(every)) {
            return 1;

        } else if (context.getResources().getString(R.string.repeated_week).equals(every)) {
            return 2;

        } else if (context.getResources().getString(R.string.repeated_Day).equals(every)) {
            return 3;

        } else {
            return 0;
        }
    }

    // General Methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End

}
