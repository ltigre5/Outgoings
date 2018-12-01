package com.example.leand.outgoingoverview;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    DBAdapter myDb;
    TextView textView_MainActivity_SelectedDate;
    TextView textView_MainActivity_SelectedDateValue;
    CalendarView calendarView_MainActivity_calendar;
    public static final String EXTRA_DATE = "Outgoingoverview.DATE";
    String string_selectedDate;
    Date date_selectedDate;
    Integer integer_selectedMonth;
    public static final int requestedValue = 1;
    Double double_AddValueToDateActivity_DateValue;
    TextView textView_MainActivity_savedValues;

    SimpleDateFormat sdf_DateInNumbers = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdf_Month = new SimpleDateFormat("MM");

    // Deklaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openDB();


        //TextViews of showed Date and value of the Selected Date
        textView_MainActivity_SelectedDate = (TextView) findViewById(R.id.textView_MainActivity_SelectedDate);
        textView_MainActivity_SelectedDateValue = (TextView) findViewById(R.id.textView_MainActivity_SelectedDateValue);

        //CalendarView of the calendar
        calendarView_MainActivity_calendar = (CalendarView) findViewById(R.id.calendarView_MainActivity_calendar);

        //get Date
        calendarView_MainActivity_calendar.getDate();
        string_selectedDate = sdf_DateInNumbers.format(new Date(calendarView_MainActivity_calendar.getDate()));

        //set Month
        textView_MainActivity_SelectedDate.setText(string_selectedDate);

        // get selected Date
        date_selectedDate = new Date(calendarView_MainActivity_calendar.getDate());

        //show sum of all values of this date
        textView_MainActivity_SelectedDateValue.setText(Double.toString(sumAllValuesOfSelectedDate()));

        //When clicked on a date, open AddValueToDateActivity and send Date to this activity
        calendarView_MainActivity_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                date_selectedDate = new Date(year - 1900, month, dayOfMonth);
                Intent putOutgoingIntent = new Intent(MainActivity.this, AddValueToDateActivity.class);
                putOutgoingIntent.putExtra(EXTRA_DATE, date_selectedDate.getTime());

                startActivityForResult(putOutgoingIntent, requestedValue);
            }
        });
    }

    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Database Methods

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }

    private void closeDB() {
        myDb.close();
    }

    //Delet all data in Database
    public void onClick_ClearAll(View view) {

        myDb.deleteAll();
    }

    //Adds Date and value to Database
    private void addDateAndValue(Date date, double value) {
        long intDate = date.getTime();

        long newId = myDb.insertRow(intDate, value);
    }

    //sums the value of the selected date
    private double sumAllValuesOfSelectedDate() {
        //show saved Value of selected Date
        Cursor cursor = myDb.getRowWithDate(date_selectedDate.getTime());

        //sum all values of this date
        double doubleAllValuesOfSelectedDate = 0;
        if (cursor.moveToFirst()) {
            do {
                doubleAllValuesOfSelectedDate += cursor.getDouble(DBAdapter.COL_VALUE);
            } while (cursor.moveToNext());
        }

        return doubleAllValuesOfSelectedDate;
    }

    // Database Methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Convert Values

    //Makes String Date out of long
    private String longToStringDate(long longDate) {
        String date = sdf_DateInNumbers.format(longDate);
        return date;
    }

    //Make String Date out of date
    private String dateToStringDate(Date date) {
        String stringDate = sdf_DateInNumbers.format(date.getTime());
        return stringDate;
    }

    // Convert Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Communicate with other Activitys

    //Save Date and Value on Database if Value is saved from AddValueToDateActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestedValue) {
            if (resultCode == RESULT_OK) {
                date_selectedDate.setTime(data.getLongExtra("OutgoingDate", 0));
                double_AddValueToDateActivity_DateValue = data.getDoubleExtra("OutgoingValue", 0);

                if (double_AddValueToDateActivity_DateValue != 0) {
                    //add Value to Database
                    addDateAndValue(date_selectedDate, double_AddValueToDateActivity_DateValue);
                }

                //show selected Date in TextView
                textView_MainActivity_SelectedDate.setText(longToStringDate(date_selectedDate.getTime()));
                //textView_MainActivity_SelectedDateValue.setText(double_AddValueToDateActivity_DateValue.toString());

                //show sum of all values of this date
                textView_MainActivity_SelectedDateValue.setText(Double.toString(sumAllValuesOfSelectedDate()));

            }
        }
    }

    //Opens new Activity which shows all values
    public void onClick_ShowAllvalues(View view) {
        Intent intent = new Intent(this, OutgoingsOfSelectedMonthActivity.class);
        integer_selectedMonth = Integer.valueOf(sdf_Month.format(date_selectedDate.getTime()));
        intent.putExtra("IntSelectedMonth", integer_selectedMonth);
        startActivity(intent);
    }

    // Communicate with other Activitys
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Displaying Values

    //Displays Values on MainActivity


    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
