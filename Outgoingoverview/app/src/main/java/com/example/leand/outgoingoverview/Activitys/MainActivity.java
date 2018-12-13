package com.example.leand.outgoingoverview.Activitys;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.leand.outgoingoverview.Classes.GeneralHelper;
import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.R;
import com.example.leand.outgoingoverview.Classes.SelectedDate;

public class MainActivity extends AppCompatActivity {
    public static DBAdapter myDbMain;

    private TextView textView_MainActivity_SelectedDateValue, textView_MainActivity_totalValue;
    private Button button_MainActivity_changeValue;

    private GeneralHelper generalHelper;
    private SelectedDate selectedDateNew, selectedDateOld;

    private int counterCalendar;
    private String string_Currency = "";

    public static final String EXTRA_LONG_DATE = "long Date";
    public static final String EXTRA_INT_DIRECT_OPEN_ACTIVITY = "direct Open Activity";

    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AddRepeatedItemsActivity.MainActivRepeated = true;
        AddNewItemActivity.MainActiv = true;

        //get Date from MainActivity
        Intent caller = getIntent();
        int int_directOpenActvity = caller.getIntExtra(MainActivity.EXTRA_INT_DIRECT_OPEN_ACTIVITY, -1);

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

        //Initialize General Helper
        generalHelper = new GeneralHelper();

        //show values on mainActivity
        displayItemsOnActivity();

        //Open AddRepeatedActivity or AddNewActivity if started from Widget
        if (int_directOpenActvity == 1) {
            Intent intent = new Intent(MainActivity.this, AddNewItemActivity.class);
            intent.putExtra(EXTRA_LONG_DATE, selectedDateNew.getLong_Date());
            startActivityForResult(intent, 1);

        } else if (int_directOpenActvity == 2) {
            Intent intent = new Intent(MainActivity.this, AddRepeatedItemsActivity.class);
            startActivityForResult(intent, 1);
        }

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
        Intent intent = new Intent(this, AddRepeatedItemsActivity.class);
        startActivityForResult(intent, 1);
    }

    //Opens OverviewListActivity, which shows a list of all Dates with values
    public void onClick_ShowAllvalues(View view) {
        Intent intent = new Intent(this, OverviewListActivity.class);
        intent.putExtra(EXTRA_LONG_DATE, selectedDateNew.getLong_Date());
        startActivityForResult(intent, 1);
    }

    //Opens ChangeCurrencyActivity, where you can change the Currency
    public void onClick_OpenChangeCurrency(View view) {
        Intent intent = new Intent(this, ChangeCurrencyActivity.class);
        startActivityForResult(intent, 1);
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
            } else {
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
        textView_MainActivity_SelectedDateValue.setText(generalHelper.currencyFormat.format(generalHelper.sumAllValuesOfSelectedDay(selectedDateNew)) + string_Currency);

        //show total value of selected Month
        textView_MainActivity_totalValue.setText(generalHelper.currencyFormat.format(generalHelper.sumAllValuesOfSelectedMonth(selectedDateNew)) + string_Currency);
    }

    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
