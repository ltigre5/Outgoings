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
    TextView textView_MainActivity_totalValue;
    CalendarView calendarView_MainActivity_calendar;
    public static final String EXTRA_LONG_DATE = "long Date";
    SelectedDate selectedDate;

    SimpleDateFormat sdf_Month = new SimpleDateFormat("MM");
    SimpleDateFormat sdf_Year = new SimpleDateFormat("yyyy");

    // Deklaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //open the Database
        openDB();

        //definition of Items in MainActivity
        textView_MainActivity_totalValue = findViewById(R.id.textView_MainActivity_totalValue);
        textView_MainActivity_SelectedDate = findViewById(R.id.textView_MainActivity_SelectedDate);
        textView_MainActivity_SelectedDateValue = findViewById(R.id.textView_MainActivity_SelectedDateValue);
        calendarView_MainActivity_calendar = findViewById(R.id.calendarView_MainActivity_calendar);

        //get values Of selected Date
        selectedDate=new SelectedDate(calendarView_MainActivity_calendar.getDate());

        //show new selected values on mainactivity
        displayItemsOnActivity();

        //When clicked on a date, open AddValueToDateActivity and send Date to this activity
        calendarView_MainActivity_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //set selected values new
                selectedDate.setDate_Date( new Date(year - 1900, month, dayOfMonth));

                //show new selected values on mainactivity
                displayItemsOnActivity();

                //open AddValueToDateActivity and send Data
                Intent intent = new Intent(MainActivity.this, AddValueToDateActivity.class);
                intent.putExtra(EXTRA_LONG_DATE, selectedDate.getLong_Date());
                startActivityForResult(intent, 1);
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

    //sums the value of the selected date
    private double sumAllValuesOfSelectedDay() {
        //show saved Value of selected Date
        Cursor cursor = myDb.getRowWithDate(selectedDate.getLong_Date());

        //sum all values of this date
        double doubleAllValuesOfSelectedDate = 0;
        if (cursor.moveToFirst()) {
            do {
                doubleAllValuesOfSelectedDate += cursor.getDouble(DBAdapter.COL_VALUE);
            } while (cursor.moveToNext());
        }

        return doubleAllValuesOfSelectedDate;
    }

    //sums all values of the selected Month
    private Double sumAllValuesOfSelectedMonth() {
        Cursor cursor = myDb.getAllRows();

        Double doubleTotalValue = 0.0;

        if (cursor.moveToFirst()) {
            Integer integerMonth = cursor.getInt(DBAdapter.COL_MONTH);
            Integer integerYear = cursor.getInt(DBAdapter.COL_YEAR);
            do {
                if (integerMonth.equals(selectedDate.getInteger_Month())&&integerYear.equals(selectedDate.getInteger_Year())) {
                    doubleTotalValue += cursor.getDouble(DBAdapter.COL_VALUE);
                }
            } while (cursor.moveToNext());
        }
        else doubleTotalValue=0.0;
        return doubleTotalValue;
    }

    // Database Methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Convert Values


    // Convert Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Communicate with other Activitys

    //Save Date and Value on Database if Value is saved from AddValueToDateActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                //show new Values on Activity
                displayItemsOnActivity();
            }
        }
    }

    //Opens new Activity which shows all values
    public void onClick_ShowAllvalues(View view) {
        Intent intent = new Intent(this, OutgoingsOfSelectedMonthActivity.class);
        intent.putExtra("IntSelectedMonth", selectedDate.getInteger_Month());
        startActivity(intent);
    }

    // Communicate with other Activitys
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Displaying Values

    public void displayItemsOnActivity() {
        //set selected Date
        textView_MainActivity_SelectedDate.setText(selectedDate.getString_Date());

        //show sum of all values of this date
        textView_MainActivity_SelectedDateValue.setText(Double.toString(sumAllValuesOfSelectedDay()));

        //show total value of selected Month
        textView_MainActivity_totalValue.setText(sumAllValuesOfSelectedMonth().toString());
    }

    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
