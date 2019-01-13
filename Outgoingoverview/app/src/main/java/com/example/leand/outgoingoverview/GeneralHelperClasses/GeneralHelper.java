package com.example.leand.outgoingoverview.GeneralHelperClasses;

import android.content.Context;
import android.database.Cursor;

import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.ListviewHelper.ListViewAdapter;
import com.example.leand.outgoingoverview.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import static com.example.leand.outgoingoverview.Activitys.MainActivity.myDbMain;

/**
 * This class helps in various forms. You can use the various Date formatter's to use for formatting the long value of the date's.
 * Or you can get the Currency out of the Database.
 * You can get the sum of all values in the Database, the sum of a specific year, month or day.
 * And you can convert the int number of repeatedBy into the string or the string into the number.
 *
 */

public class GeneralHelper {

    /**
     * formats the long into a format like 01/12/2018
     */
    public SimpleDateFormat longDateToDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
    /**
     * formats the long into a format like 20181201, this helps to order or search the date's in the Database
     */
    public SimpleDateFormat longDateToDateWithoutTime = new SimpleDateFormat("yyyyMMdd");
    /**
     * formats the long into a format like 01
     */
    public SimpleDateFormat longDateToDayNumber = new SimpleDateFormat("dd");
    /**
     * formats the long into a format like 12
     */
    public SimpleDateFormat longDateToMonthNumber = new SimpleDateFormat("MM");
    /**
     * formats the long into a format like december
     */
    public SimpleDateFormat longDateToMonthWriteOut = new SimpleDateFormat("MMMM");
    /**
     * formats the long into a format like 2018
     */
    public SimpleDateFormat longDateToYearNumber = new SimpleDateFormat("yyyy");
    /**
     * formats the long into a format like monday
     */
    public SimpleDateFormat longDateToWeekday = new SimpleDateFormat("EEEE");
    /**
     * formats the currency into a format like 1,324,342.00 or 0.00
     */
    public DecimalFormat currencyFormat = new DecimalFormat("#,###,###,###,###,###,##0.00");


    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor

    public GeneralHelper() {
    }

    // Constructor
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // General Methods

    /**
     * Get the currency from the database, if no currency in the database it returns ""
     *
     * @return currency
     */
    public String getCurrency() {
        String currency;
        Cursor cursor = myDbMain.getCurrency();
        if (cursor.moveToFirst()) {
            currency = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_CURRENCY));
        } else {
            currency = "";
        }
        cursor.close();
        return currency;
    }

    /**
     * Sums all values of the ListViewAdapter
     *
     * @param listViewAdapter ListViewAdapter to sum all values
     * @return sum of all values in double
     */

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

    /**
     * Sums all values of the day from the SelectedDate
     *
     * @param selectedDate selectedDate to sum all values
     * @return sum of all values of selected day in double
     */

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

    /**
     * Sums all values of the month of the SelectedDate
     *
     * @param selectedDate selectedDate to sum all values
     * @return sum of all values of selected month in double
     */

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

    /**
     * Sums all values of the year of the SelectedDate
     *
     * @param selectedDate selectedDate to sum all values
     * @return sum of all values of selected year in double
     */

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

    /**
     * Get the string of every with the int number
     *
     * @param every int of every
     * @param context context
     * @return returns the string of every
     */

    public String getRepeatedEveryWithNumber(int every, Context context) {
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

    /**
     * Get the int of every with the string
     *
     * @param every string of every
     * @param context context
     * @return returns the int of every
     */

    public int setRepeatedEveryWithNumber(String every, Context context) {
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
