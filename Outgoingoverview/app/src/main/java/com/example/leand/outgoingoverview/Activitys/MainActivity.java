package com.example.leand.outgoingoverview.Activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.R;
import com.example.leand.outgoingoverview.Classes.SelectedDate;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    public static DBAdapter myDbMain;
    private TextView textView_MainActivity_SelectedDateValue, textView_MainActivity_totalValue;
    private Button button_MainActivity_changeValue;

    public static final String EXTRA_LONG_DATE = "long Date";

    private SelectedDate selectedDateNew, selectedDateOld;

    private int counterCalendar;
    private String string_Currency = "";

    DecimalFormat df = new DecimalFormat("0.00");

    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //open the Database
        openDB();

        //definition of Items in Activity
        textView_MainActivity_totalValue = findViewById(R.id.textView_MainActivity_totalValue);
        textView_MainActivity_SelectedDateValue = findViewById(R.id.textView_MainActivity_SelectedDateValue);
        CalendarView calendarView_MainActivity_calendar = findViewById(R.id.calendarView_MainActivity_calendar);
        button_MainActivity_changeValue = findViewById(R.id.button_MainActivity_ChangeCurrency);

        //counter for double tab on calendar
        counterCalendar = 0;

        //get values Of selected Date
        selectedDateNew = new SelectedDate(calendarView_MainActivity_calendar.getDate());

        //initialize old date value
        selectedDateOld = new SelectedDate(0);

        //show values on mainactivity
        displayItemsOnActivity();

        //When clicked on a date twice, open AddNewItemActivity and send Date to this activity
        calendarView_MainActivity_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                //set selected values new
                selectedDateNew.setLong_Date(year, month, dayOfMonth);

                //if new Date selected, show values of this date
                if (counterCalendar < 2) {

                    //if clicked on another date display new values
                    if (!selectedDateOld.getString_Date().equals(selectedDateNew.getString_Date())) {
                        displayItemsOnActivity();
                        counterCalendar = 0;
                    }

                    counterCalendar++;
                    selectedDateOld.setLong_Date(selectedDateNew.getLong_Date());
                }

                //if same Date clicked twice, open AddNewItemActivity and send selected Date
                if (counterCalendar >= 2) {
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
    // onClick Methods

    public void onClick_OpenRepaetedOutgoing(View view) {
        Intent intent = new Intent(this, AddRepeatedOutgoingsActivity.class);
        startActivityForResult(intent, 1);
    }

    //Opens OverviewListActivity, which shows a list of all Dates with values
    public void onClick_ShowAllvalues(View view) {
        Intent intent = new Intent(this, OverviewListActivity.class);
        intent.putExtra(EXTRA_LONG_DATE, selectedDateNew.getLong_Date());
        startActivity(intent);
    }

    //Opens ChangeCurrencyActivity, where you can change the Currency
    public void onClick_OpenChangeCurrency(View view) {
        Intent intent = new Intent(this, ChangeCurrencyActivity.class);
        startActivityForResult(intent, 1);
    }

    //Delet all data in Database
    public void onClick_ClearAll(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked, delet all Data in Database
                        myDbMain.deleteAll();
                        displayItemsOnActivity();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked, do nothing
                        break;
                }
            }
        };

        //set the message to show in the DialogWindow
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    // onClick Methods
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // Database Methods

    //Close Database when Activity is closed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    //open Database
    private void openDB() {
        myDbMain = new DBAdapter(this);
        myDbMain.open();
    }

    //close Database
    private void closeDB() {
        myDbMain.close();
    }

    //get the Currency out of Database
    private void getCurrency() {
        Cursor cursor = myDbMain.getAllRows();

        if (cursor.moveToFirst()) {
            string_Currency = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_CURRENCY));
            button_MainActivity_changeValue.setVisibility(View.VISIBLE);
            if (string_Currency.equals("")) {
                button_MainActivity_changeValue.setText(getString(R.string.button_addCurrency));
            } else {
                button_MainActivity_changeValue.setText(getString(R.string.button_changeCurrency));
            }
        } else {
            button_MainActivity_changeValue.setVisibility(View.INVISIBLE);
            string_Currency = "";
        }
        cursor.close();
    }

    //sums the value of the selected date
    private double sumAllValuesOfSelectedDay() {
        Cursor cursor = myDbMain.getRowWithDate(selectedDateNew.getInteger_DateWithoutTime());
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
    private Double sumAllValuesOfSelectedMonth() {
        Cursor cursor = myDbMain.getRowWithMonthYear(selectedDateNew.getInteger_Month(), selectedDateNew.getInteger_Year());
        Double doubleTotalValue = 0.0;

        if (cursor.moveToFirst()) {
            do {
                doubleTotalValue += cursor.getDouble(DBAdapter.COL_VALUE);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return doubleTotalValue;
    }

    // Database Methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Communicate with other Activitys

    //Show new Values if new Values are added from AddNewItemActivity or new Currency is added from ChangeCurrenyActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                //show new Values on Activity
                displayItemsOnActivity();
            } else if (resultCode == RESULT_CANCELED) {
                displayItemsOnActivity();
            }
        }
    }

    // Communicate with other Activitys
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Displaying Values

    public void displayItemsOnActivity() {

        //save Currency String
        getCurrency();

        //show sum of all values of this date
        textView_MainActivity_SelectedDateValue.setText(df.format(sumAllValuesOfSelectedDay()) + string_Currency);

        //show total value of selected Month
        textView_MainActivity_totalValue.setText(df.format(sumAllValuesOfSelectedMonth()) + string_Currency);
    }


    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
