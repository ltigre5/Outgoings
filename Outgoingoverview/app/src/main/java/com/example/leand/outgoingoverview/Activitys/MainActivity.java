package com.example.leand.outgoingoverview.Activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.GeneralHelperClasses.GeneralHelper;
import com.example.leand.outgoingoverview.R;
import com.example.leand.outgoingoverview.GeneralHelperClasses.SelectedDate;
import com.example.leand.outgoingoverview.Widget.AddNewOutgoingWidget;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static DBAdapter myDbMain;

    private TextView textView_MainActivity_SelectedDateValue, textView_MainActivity_totalValue, textView_MainActivity_totalValueYear;

    private GeneralHelper generalHelper;
    private SelectedDate selectedDateNew, selectedDateOld;

    private int counterCalendar;
    public String string_Currency = "";

    public static final String EXTRA_LONG_DATE = "long Date";


    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("MainActivity", "Start");

        //Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_MainActivity);
        setSupportActionBar(toolbar);

        //get int to Choose which Activity to open Direct
        Intent caller = getIntent();
        int int_directOpenActivity = caller.getIntExtra(AddNewOutgoingWidget.EXTRA_INT_DIRECT_OPEN_ACTIVITY, -1);

        //open the Database
        openDB();

        //definition of Items in Activity
        textView_MainActivity_totalValue = findViewById(R.id.textView_MainActivity_totalValueMonth);
        textView_MainActivity_SelectedDateValue = findViewById(R.id.textView_MainActivity_SelectedDateValue);
        textView_MainActivity_totalValueYear = findViewById(R.id.textView_MainActivity_totalValueYear);
        CalendarView calendarView_MainActivity_calendar = findViewById(R.id.calendarView_MainActivity_calendar);

        //counter for double tab on calendar
        counterCalendar = 0;

        //get values Of selected Date
        selectedDateNew = new SelectedDate(calendarView_MainActivity_calendar.getDate());

        //initialize old date value
        selectedDateOld = new SelectedDate();

        //Initialize General Helper
        generalHelper = new GeneralHelper();

        //show values on mainActivity
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


        //Open AddRepeatedActivity or AddNewActivity if started from Widget
        if (int_directOpenActivity == 1) {
            Intent intent = new Intent(MainActivity.this, AddNewItemActivity.class);
            intent.putExtra(EXTRA_LONG_DATE, selectedDateNew.getLong_Date());
            startActivity(intent);

        } else if (int_directOpenActivity == 2) {
            Intent intent = new Intent(MainActivity.this, AddRepeatedItemsActivity.class);
            startActivity(intent);
        }
    }


    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // onClick Methods

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
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
        Cursor cursor = myDbMain.getCurrency();

        if (cursor.moveToFirst()) {
            string_Currency = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_CURRENCY));

        } else {
            string_Currency = "";
        }
        cursor.close();
    }

    // Database Methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Communicate with other Activitys

    //Show new Values if new Values are added from AddNewItemActivity or new Currency is added from ChangeCurrencyActivity
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

        textView_MainActivity_totalValueYear.setText(generalHelper.currencyFormat.format(generalHelper.sumAllValuesOfSelectedYear(selectedDateNew)) + string_Currency);
    }

    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Menu

    //Create the Menubar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Menus
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_Overview:
                intent = new Intent(this, OverviewActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_AddCurrency:
                intent = new Intent(this, CurrencyActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_AddRepeatedIssues:
                intent = new Intent(this, AddRepeatedItemsActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_DeleteAll:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                //Yes button clicked, delete all Data in Database
                                MainActivity.myDbMain.deleteAll();
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
                builder.setMessage(getString(R.string.dialog_question)).setPositiveButton(getString(R.string.dialog_yes), dialogClickListener)
                        .setNegativeButton(getString(R.string.dialog_no), dialogClickListener).show();
                return true;

            case R.id.menu_info:
                intent = new Intent(this, InfoActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_MainMenu:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Menu
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
