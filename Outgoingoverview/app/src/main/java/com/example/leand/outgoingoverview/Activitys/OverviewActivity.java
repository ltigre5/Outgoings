package com.example.leand.outgoingoverview.Activitys;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.GeneralHelperClasses.GeneralHelper;
import com.example.leand.outgoingoverview.ListviewHelper.ListViewAdapter;
import com.example.leand.outgoingoverview.R;
import com.example.leand.outgoingoverview.GeneralHelperClasses.SelectedDate;

import java.util.Objects;

public class OverviewActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button button_OverviewActivity_OK;
    private TextView textView_OverviewActivity_totalValue, textView_OverviewActivity_MonthToStatment, textView_OverviewActivity_YearFromStatment;
    private EditText editText_OverviewActivity_Year, editText_OverviewActivity_Month, editText_OverviewActivity_From, editText_OverviewActivity_To;
    private Spinner spinner_OverviewActivity_OrderBy, spinner_OverviewActivity_Show, spinner_OverviewActivity_AscendingDescending, spinner_OverviewActivity_Hide;
    private ListView listView_OverviewActivity;

    private ListViewAdapter listViewAdapter;
    private DatePickerDialog.OnDateSetListener mDateSetListenerStart, mDateSetListenerEnd;

    private SelectedDate selectedDate_Start;
    private SelectedDate selectedDate_End;
    private GeneralHelper generalHelper;

    private String string_Currency, string_OrderBy_DateValue = DBAdapter.KEY_DATE, string_OrderBy_AscendingDescending = DBAdapter.DESCENDING;
    private String string_FilterFor;
    private String string_hide;
    private String SPINNER_YEAR, SPINNER_MONTH, SPINNER_FROM_TO, SPINNER_ASCENDING, SPINNER_DESCENDING, SPINNER_VALUE, SPINNER_DATE, SPINNER_HIDE_SINGLE, SPINNER_HIDE_REPEATED, SPINNER_SHOW_ALL;
    private Integer integer_OrderBy_Year, integer_OrderBy_Month;
    private int year, month, day;

    public static final String EXTRA_INTEGER_ID = "Integer ID";

    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_);

        //Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_MainActivity);
        setSupportActionBar(toolbar);

        //definition of Items in MainActivity
        listView_OverviewActivity = findViewById(R.id.listView_OverviewActivity);
        textView_OverviewActivity_totalValue = findViewById(R.id.textView_OverviewActivity_TotalValue);
        spinner_OverviewActivity_OrderBy = findViewById(R.id.spinner_OverviewActivity_OrderBy);
        spinner_OverviewActivity_Show = findViewById(R.id.spinner_OverviewActivity_Show);
        spinner_OverviewActivity_AscendingDescending = findViewById(R.id.spinner_OverviewActivity_AscendingDescending);
        editText_OverviewActivity_Year = findViewById(R.id.editText_OverviewActivity_Year);
        editText_OverviewActivity_Month = findViewById(R.id.editText_OverviewActivity_Month);
        textView_OverviewActivity_MonthToStatment = findViewById(R.id.textView_OverviewActivity_MonthToStatment);
        textView_OverviewActivity_YearFromStatment = findViewById(R.id.textView_OverviewActivity_YearFromStatment);
        editText_OverviewActivity_To = findViewById(R.id.editText_OverviewActivity_To);
        editText_OverviewActivity_From = findViewById(R.id.editText_OverviewActivity_From);
        button_OverviewActivity_OK = findViewById(R.id.button_OverviewActivity_OK);
        spinner_OverviewActivity_Hide = findViewById(R.id.spinner_OverviewActivity_Hide);

        //get Spinner string Ressource
        SPINNER_YEAR = getResources().getString(R.string.show_year);
        SPINNER_MONTH = getResources().getString(R.string.show_month);
        SPINNER_FROM_TO = getResources().getString(R.string.show_from_to);
        SPINNER_ASCENDING = getResources().getString(R.string.order_by_ascending);
        SPINNER_DESCENDING = getResources().getString(R.string.order_by_descending);
        SPINNER_VALUE = getResources().getString(R.string.order_by_value);
        SPINNER_DATE = getResources().getString(R.string.order_by_date);
        SPINNER_HIDE_SINGLE = getResources().getString(R.string.hide_hideSingle);
        SPINNER_HIDE_REPEATED = getResources().getString(R.string.hide_hideRepeated);
        SPINNER_SHOW_ALL = getResources().getString(R.string.hide_showAll);


        string_FilterFor = getResources().getString(R.string.show_month);

        //get Today's Date
        SelectedDate selectedDate = new SelectedDate();
        integer_OrderBy_Month = selectedDate.getInteger_Month();
        integer_OrderBy_Year = selectedDate.getInteger_Year();

        //create new listViewAdapter
        listViewAdapter = new ListViewAdapter(selectedDate.getInteger_Month(), selectedDate.getInteger_Year(), DBAdapter.KEY_DATE, this, DBAdapter.ASCENDING);

        //Initialize Start and End Date for DatePicker
        selectedDate_Start = new SelectedDate();
        selectedDate_End = new SelectedDate();

        //Initialize General Helper
        generalHelper = new GeneralHelper();

        //get selected Month and Year to Filter the list
        editText_OverviewActivity_Month.setText(integer_OrderBy_Month.toString());
        editText_OverviewActivity_Year.setText(integer_OrderBy_Year.toString());

        //save Currency String
        string_Currency = generalHelper.getCurrency();

        //create List and Spinner
        createArrayList();
        createSpinnerOrderBy();
        createSpinnerAscendingDescending();
        createSpinnerShow();
        createSpinnerHide();

        //shows all Items On Activity
        displayItemsOnActivity();

        //set OnclickListener with Datepicker for Start Date
        editText_OverviewActivity_From.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = selectedDate_Start.getInteger_Year();
                month = selectedDate_Start.getInteger_Month() - 1;
                day = selectedDate_Start.getInteger_day();

                DatePickerDialog dialog = new DatePickerDialog(
                        OverviewActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenerStart,
                        year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListenerStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                selectedDate_Start.setLong_Date(year, month, day);
                editText_OverviewActivity_From.setText(selectedDate_Start.getString_Date());
                displayItemsOnActivity();
            }
        };

        //set onClickListener with Datepicker for End date
        editText_OverviewActivity_To.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = selectedDate_End.getInteger_Year();
                month = selectedDate_End.getInteger_Month() - 1;
                day = selectedDate_End.getInteger_day();

                DatePickerDialog dialog = new DatePickerDialog(
                        OverviewActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenerEnd,
                        year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListenerEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                selectedDate_End.setLong_Date(year, month, day);
                editText_OverviewActivity_To.setText(selectedDate_End.getString_Date());
                displayItemsOnActivity();
            }
        };
    }

    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // onClick Methods

    //get Values to Filter for
    public void onClick_FilterList(View view) {
        if (string_FilterFor.equals(SPINNER_YEAR)) {
            if (!editText_OverviewActivity_Year.getText().toString().equals("")) {
                integer_OrderBy_Year = Integer.parseInt(editText_OverviewActivity_Year.getText().toString());
            }
        } else if (string_FilterFor.equals(SPINNER_MONTH)) {
            if (!editText_OverviewActivity_Year.getText().toString().equals("") && !editText_OverviewActivity_Month.getText().toString().equals("") && Integer.parseInt(editText_OverviewActivity_Month.getText().toString()) > 0 && Integer.parseInt(editText_OverviewActivity_Month.getText().toString()) < 13) {
                integer_OrderBy_Year = Integer.parseInt(editText_OverviewActivity_Year.getText().toString());
                integer_OrderBy_Month = Integer.parseInt(editText_OverviewActivity_Month.getText().toString());
            }
        }
        displayItemsOnActivity();
    }


    // onClick Methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Communicate with other Activitys

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                displayItemsOnActivity();
            } else {
                displayItemsOnActivity();
            }
        }
    }

    // Communicate with other Activitys
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // Spinner Methods

    //create Spinner to Order Items by values or Date
    private void createSpinnerOrderBy() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.order_by_arrays, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_OverviewActivity_OrderBy.setAdapter(adapter);
        spinner_OverviewActivity_OrderBy.setOnItemSelectedListener(this);
    }

    //create Spinner to order Items in Ascending or Descending order
    private void createSpinnerAscendingDescending() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ascending_descending, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_OverviewActivity_AscendingDescending.setAdapter(adapter);
        spinner_OverviewActivity_AscendingDescending.setOnItemSelectedListener(this);
    }

    //create Spinner to select if Items should be Selected by Year, Month or Start and End Date
    private void createSpinnerShow() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.show_arrays, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_OverviewActivity_Show.setAdapter(adapter);
        spinner_OverviewActivity_Show.setOnItemSelectedListener(this);
    }

    //create Spinner to select if Items should be Selected by Year, Month or Start and End Date
    private void createSpinnerHide() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.hide_arrays, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_OverviewActivity_Hide.setAdapter(adapter);
        spinner_OverviewActivity_Hide.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //see if Repeated or Single Items should be Hidden
        if (parent.getId() == R.id.spinner_OverviewActivity_Hide) {
            if (parent.getItemAtPosition(position).toString().equals(SPINNER_HIDE_SINGLE)) {
                string_hide = DBAdapter.SEARCH_REPEATED;

            } else if (parent.getItemAtPosition(position).toString().equals(SPINNER_HIDE_REPEATED)) {
                string_hide = DBAdapter.SEARCH_SINGLE;
            } else if (parent.getItemAtPosition(position).toString().equals(SPINNER_SHOW_ALL)) {
                string_hide = null;
            }
        }

        //see if it should be ordered by Value or date
        if (parent.getId() == R.id.spinner_OverviewActivity_OrderBy) {
            if (parent.getItemAtPosition(position).toString().equals(SPINNER_DATE)) {
                string_OrderBy_DateValue = DBAdapter.KEY_DATE;

            } else if (parent.getItemAtPosition(position).toString().equals(SPINNER_VALUE)) {
                string_OrderBy_DateValue = DBAdapter.KEY_VALUE;
            }
        }

        //see if it should be ordered ascending or descending
        else if (parent.getId() == R.id.spinner_OverviewActivity_AscendingDescending) {
            if (parent.getItemAtPosition(position).toString().equals(SPINNER_ASCENDING)) {
                string_OrderBy_AscendingDescending = DBAdapter.DESCENDING;

            } else if (parent.getItemAtPosition(position).toString().equals(SPINNER_DESCENDING)) {
                string_OrderBy_AscendingDescending = DBAdapter.ASCENDING;
            }
        }

        // set the layout if it should be filtered by year, by month or from start and end Day
        else if (parent.getId() == R.id.spinner_OverviewActivity_Show)
            if (parent.getItemAtPosition(position).toString().equals(SPINNER_YEAR)) {
                button_OverviewActivity_OK.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                editText_OverviewActivity_Year.setVisibility(View.VISIBLE);
                editText_OverviewActivity_Month.setVisibility(View.INVISIBLE);
                editText_OverviewActivity_To.setVisibility(View.INVISIBLE);
                editText_OverviewActivity_From.setVisibility(View.INVISIBLE);

                textView_OverviewActivity_MonthToStatment.setVisibility(View.INVISIBLE);
                textView_OverviewActivity_YearFromStatment.setText(getString(R.string.statement_year));
                editText_OverviewActivity_Year.setText(integer_OrderBy_Year.toString());
                string_FilterFor = SPINNER_YEAR;

            } else if (parent.getItemAtPosition(position).toString().equals(SPINNER_MONTH)) {
                button_OverviewActivity_OK.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                editText_OverviewActivity_Year.setVisibility(View.VISIBLE);
                editText_OverviewActivity_Month.setVisibility(View.VISIBLE);
                editText_OverviewActivity_To.setVisibility(View.INVISIBLE);
                editText_OverviewActivity_From.setVisibility(View.INVISIBLE);

                textView_OverviewActivity_MonthToStatment.setVisibility(View.VISIBLE);
                textView_OverviewActivity_MonthToStatment.setText(getString(R.string.statement_month));
                textView_OverviewActivity_YearFromStatment.setText(getString(R.string.statement_year));
                editText_OverviewActivity_Year.setText(integer_OrderBy_Year.toString());
                editText_OverviewActivity_Month.setText(integer_OrderBy_Month.toString());
                string_FilterFor = SPINNER_MONTH;

            } else if (parent.getItemAtPosition(position).toString().equals(SPINNER_FROM_TO)) {
                button_OverviewActivity_OK.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

                editText_OverviewActivity_Year.setVisibility(View.INVISIBLE);
                editText_OverviewActivity_Month.setVisibility(View.INVISIBLE);
                editText_OverviewActivity_To.setVisibility(View.VISIBLE);
                editText_OverviewActivity_From.setVisibility(View.VISIBLE);

                textView_OverviewActivity_MonthToStatment.setText(getString(R.string.statement_to));
                textView_OverviewActivity_YearFromStatment.setText(getString(R.string.statement_from));
                editText_OverviewActivity_From.setText(selectedDate_Start.getString_Date());
                editText_OverviewActivity_To.setText(selectedDate_End.getString_Date());
                string_FilterFor = SPINNER_FROM_TO;
            }

        //show new Total on display
        displayItemsOnActivity();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }

    // Spinner Methods
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // List Methods

    //Creates Arraylist of all values for ListView and adds an onClick Method which opens EditItemActivity and tranfers Databse-ID of selected Value
    public void createArrayList() {
        listView_OverviewActivity.setAdapter(listViewAdapter.getListViewAdapter());

        //by clicking of Item get Database-ID of Position and open EditItemActivity and send ID
        listView_OverviewActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //get ID of selected Item from Database
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                Integer integer_iD = cursor.getInt(cursor.getColumnIndexOrThrow(DBAdapter.KEY_ROWID));

                if (cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_SINGLE_OR_REPEATED)).equals(DBAdapter.SINGLE)) {
                    //Open EditItemActivity and send ID
                    Intent intent = new Intent(OverviewActivity.this, EditItemActivity.class);
                    intent.putExtra(EXTRA_INTEGER_ID, integer_iD);
                    startActivityForResult(intent, RESULT_FIRST_USER);
                } else {
                    //Open EditRepeatedItemsActivity and send ID
                    Intent intent = new Intent(OverviewActivity.this, EditRepeatedItemsActivity.class);
                    intent.putExtra(EXTRA_INTEGER_ID, integer_iD);
                    startActivityForResult(intent, RESULT_FIRST_USER);
                }
            }
        });
    }

    // List Methods
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // Displaying Values

    //show Values on Activity
    public void displayItemsOnActivity() {
        //Actualize Cursor and shows Toast message
        if (string_FilterFor.equals(SPINNER_YEAR)) {
            listViewAdapter.setCursorYear(integer_OrderBy_Year, string_OrderBy_DateValue, string_OrderBy_AscendingDescending, string_hide);
            if (editText_OverviewActivity_Year.getText().toString().equals("")) {
                Toast.makeText(OverviewActivity.this, getString(R.string.toast_noYear),
                        Toast.LENGTH_LONG).show();
            }

        } else if (string_FilterFor.equals(SPINNER_MONTH)) {
            listViewAdapter.setCursorMonthYear(integer_OrderBy_Month, integer_OrderBy_Year, string_OrderBy_DateValue, string_OrderBy_AscendingDescending, string_hide);

            if (editText_OverviewActivity_Year.getText().toString().equals("") && editText_OverviewActivity_Month.getText().toString().equals("")) {
                Toast.makeText(OverviewActivity.this, getString(R.string.toast_noYearMonth),
                        Toast.LENGTH_LONG).show();
            } else if (editText_OverviewActivity_Month.getText().toString().equals("")) {
                Toast.makeText(OverviewActivity.this, getString(R.string.toast_noMonth),
                        Toast.LENGTH_LONG).show();
            } else if (editText_OverviewActivity_Year.getText().toString().equals("")) {
                Toast.makeText(OverviewActivity.this, getString(R.string.toast_noYear),
                        Toast.LENGTH_LONG).show();
            } else if (Integer.parseInt(editText_OverviewActivity_Month.getText().toString()) < 1 || Integer.parseInt(editText_OverviewActivity_Month.getText().toString()) > 12) {
                Toast.makeText(OverviewActivity.this, getString(R.string.toast_invalidMonth),
                        Toast.LENGTH_LONG).show();
            }

        } else if (string_FilterFor.equals(SPINNER_FROM_TO)) {
            listViewAdapter.setCursorStartEndDate(selectedDate_Start.getInteger_DateWithoutTime(), selectedDate_End.getInteger_DateWithoutTime(), string_OrderBy_DateValue, string_OrderBy_AscendingDescending, string_hide);
        }

        //Actualize listView
        listView_OverviewActivity.setAdapter(listViewAdapter.getListViewAdapter());

        //Actualize Total Value
        textView_OverviewActivity_totalValue.setText(generalHelper.currencyFormat.format(generalHelper.sumAllValues(listViewAdapter)) + string_Currency);
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

            case R.id.menu_AddCurrency:
                intent = new Intent(this, CurrencyActivity.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.menu_AddRepeatedIssues:
                intent = new Intent(this, AddRepeatedItemsActivity.class);
                startActivity(intent);
                finish();
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
                finish();
                return true;

            case R.id.menu_MainMenu:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Menu
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
