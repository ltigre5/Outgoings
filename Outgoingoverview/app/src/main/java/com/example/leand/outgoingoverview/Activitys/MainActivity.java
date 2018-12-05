package com.example.leand.outgoingoverview.Activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.R;
import com.example.leand.outgoingoverview.Classes.SelectedDate;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    DBAdapter myDb;
    TextView textView_MainActivity_SelectedDate;
    TextView textView_MainActivity_SelectedDateValue;
    TextView textView_MainActivity_totalValue;
    CalendarView calendarView_MainActivity_calendar;
    public static final String EXTRA_LONG_DATE = "long Date";
    SelectedDate selectedDateNew;
    SelectedDate selectedDateOld;
    int counterCalendar;
    String string_Currency = "";

    SimpleDateFormat sdf_Month = new SimpleDateFormat("MM");
    SimpleDateFormat sdf_Year = new SimpleDateFormat("yyyy");
    DecimalFormat df = new DecimalFormat("0.00");


    // Deklaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //open the Database
        openDB();

        //save Currency String
        getCurrency();

        //definition of Items in MainActivity
        textView_MainActivity_totalValue = findViewById(R.id.textView_MainActivity_totalValue);
        textView_MainActivity_SelectedDate = findViewById(R.id.textView_MainActivity_SelectedDate);
        textView_MainActivity_SelectedDateValue = findViewById(R.id.textView_MainActivity_SelectedDateValue);
        calendarView_MainActivity_calendar = findViewById(R.id.calendarView_MainActivity_calendar);

        counterCalendar = 0;

        //get values Of selected Date
        selectedDateNew = new SelectedDate(calendarView_MainActivity_calendar.getDate());

        //initialize old date vale
        selectedDateOld = new SelectedDate(0);

        //show new selected values on mainactivity
        displayItemsOnActivity();

        //When clicked on a date, open AddNewItemActivity and send Date to this activity
        calendarView_MainActivity_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                //set selected values new
                selectedDateNew.setDate_Date(new Date(year - 1900, month, dayOfMonth));

                if (counterCalendar < 2) {

                    //show new selected values on mainactivity
                    displayItemsOnActivity();

                    if (!selectedDateOld.getString_Date().equals(selectedDateNew.getString_Date())) {
                        counterCalendar = 0;
                    }

                    counterCalendar++;
                    selectedDateOld.setLong_Date(selectedDateNew.getLong_Date());
                }
                if (counterCalendar >= 2) {
                    //open AddNewItemActivity and send Data
                    Intent intent = new Intent(MainActivity.this, AddNewItemActivity.class);
                    intent.putExtra(EXTRA_LONG_DATE, selectedDateNew.getLong_Date());
                    startActivityForResult(intent, 1);

                    counterCalendar = 0;
                }
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

    private void getCurrency() {
        Cursor cursor = myDb.getFirstRow();
        if (!cursor.moveToFirst()) {
            string_Currency = "CHF";
        } else {
            string_Currency = cursor.getString(cursor.getColumnIndexOrThrow("currency"));
        }
        cursor.close();
    }

    //Delet all data in Database
    public void onClick_ClearAll(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        myDb.deleteAll();
                        displayItemsOnActivity();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    //sums the value of the selected date
    private double sumAllValuesOfSelectedDay() {
        //show saved Value of selected Date
        Cursor cursor = myDb.getRowWithDate(selectedDateNew.getLong_Date());

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
        Cursor cursor = myDb.getRowWithMonthYear(selectedDateNew.getInteger_Month(), selectedDateNew.getInteger_Year());

        Double doubleTotalValue = 0.0;

        if (cursor.moveToFirst()) {
            do {
                doubleTotalValue += cursor.getDouble(DBAdapter.COL_VALUE);
            } while (cursor.moveToNext());
        } else doubleTotalValue = 0.0;

        cursor.close();
        return doubleTotalValue;
    }

    // Database Methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Convert Values


    // Convert Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Communicate with other Activitys

    //Save Date and Value on Database if Value is saved from AddNewItemActivity
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
        Intent intent = new Intent(this, OverviewListActivity.class);
        intent.putExtra("LongSelectedDate", selectedDateNew.getLong_Date());
        startActivity(intent);
    }

    public void onClick_OpenProperties(View view) {
        Intent intent = new Intent(this, PropertiesActivity.class);
        startActivityForResult(intent, 1);
    }

    // Communicate with other Activitys
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Displaying Values

    public void displayItemsOnActivity() {

        //save Currency String
        getCurrency();

        //set selected Date
        textView_MainActivity_SelectedDate.setText(selectedDateNew.getString_Date());

        //show sum of all values of this date
        textView_MainActivity_SelectedDateValue.setText(df.format(sumAllValuesOfSelectedDay()) + string_Currency);

        //show total value of selected Month
        textView_MainActivity_totalValue.setText(df.format(sumAllValuesOfSelectedMonth()) + string_Currency);
    }


    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
