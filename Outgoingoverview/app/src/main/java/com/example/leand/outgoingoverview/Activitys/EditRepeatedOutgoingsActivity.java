package com.example.leand.outgoingoverview.Activitys;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.leand.outgoingoverview.Classes.GeneralHelper;
import com.example.leand.outgoingoverview.Classes.SelectedDate;
import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.EditTextFilter.InputFilterDecimal;
import com.example.leand.outgoingoverview.R;

import java.util.Calendar;
import java.util.Objects;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EditRepeatedOutgoingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editText_EditRepeatedOutgoingsActivity_Start, editText_EditRepeatedOutgoingsActivity_End, editText_EditRepeatedOutgoingsActivity_Value;
    private EditText editText_EditRepeatedOutgoingsActivity_Titel, editText_EditRepeatedOutgoingsActivity_Description;
    private TextView textView_EditRepeatedOutgoingsActivity_Currency;
    private Button button_EditRepeatedOutgoingsActivity_ChooseColor;
    private Spinner spinner_EditRepeatedOutgoingsActivity_RepeatBy;

    private SelectedDate selectedDate, selectedDateNew_Start, selectedDateNew_End, selectedDateOld_Start, selectedDateOld_End;

    private DatePickerDialog.OnDateSetListener mDateSetListenerStart, mDateSetListenerEnd;

    private String string_Every;
    private String SPINNER_YEAR, SPINNER_MONTH, SPINNER_WEEK, SPINNER_DAY;
    private Calendar calendarStart, calendarEnd;

    private double double_Value = 0.0;
    private String string_Description = "", string_TitleRepeated = "", string_Currency = "";
    private String string_EveryForList;

    private GeneralHelper generalHelper;

    private double double_oldValue;
    private String string_oldValue;
    private String string_oldTitel;
    private String string_oldDescription;
    private int int_titleColor = 0xff000000; //for black

    int year, month, day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_repeated_outgoings);

        //Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_MainActivity);
        setSupportActionBar(toolbar);

        //definition of Items in Activity
        editText_EditRepeatedOutgoingsActivity_Start = findViewById(R.id.editText_EditRepeatedOutgoingsActivity_Start);
        editText_EditRepeatedOutgoingsActivity_End = findViewById(R.id.editText_EditRepeatedOutgoingsActivity_End);
        editText_EditRepeatedOutgoingsActivity_Value = findViewById(R.id.editText_EditRepeatedOutgoingsActivity_Value);
        textView_EditRepeatedOutgoingsActivity_Currency = findViewById(R.id.textView_EditRepeatedOutgoingsActivity_Currency);
        spinner_EditRepeatedOutgoingsActivity_RepeatBy = findViewById(R.id.spinner_EditRepeatedOutgoingsActivity_RepeatBy);
        editText_EditRepeatedOutgoingsActivity_Description = findViewById(R.id.editText_EditRepeatedOutgoingsActivity_Description);
        editText_EditRepeatedOutgoingsActivity_Titel = findViewById(R.id.editText_EditRepeatedOutgoingsActivity_Titel);
        button_EditRepeatedOutgoingsActivity_ChooseColor = findViewById(R.id.button_EditRepeatedOutgoingsActivity_ChooseColor);

        //Initialize Start and End Date for DatePicker
        selectedDate = new SelectedDate();
        selectedDateOld_Start = new SelectedDate();
        selectedDateOld_End = new SelectedDate();
        selectedDateNew_Start = new SelectedDate();
        selectedDateNew_End = new SelectedDate();

        //get Database-ID from OverviewListActivity
        Intent caller = getIntent();
        Integer interger_ID = caller.getIntExtra(OverviewListActivity.EXTRA_INTEGER_ID, -1);

        //get Row to edit with ID
        Cursor cursor = MainActivity.myDbMain.getRowWithID(interger_ID);

        //Initialize General Helper
        generalHelper = new GeneralHelper();

        //get old values to Edit
        selectedDateNew_Start.setLong_Date((cursor.getLong(DBAdapter.COL_START_DATE)));
        selectedDateNew_End.setLong_Date(cursor.getLong(DBAdapter.COL_END_DATE));
        selectedDateOld_Start.setLong_Date((cursor.getLong(DBAdapter.COL_START_DATE)));
        selectedDateOld_End.setLong_Date(cursor.getLong(DBAdapter.COL_END_DATE));
        double_oldValue = cursor.getDouble(DBAdapter.COL_VALUE);
        string_oldValue = generalHelper.currencyFormat.format(cursor.getDouble(DBAdapter.COL_VALUE));
        string_oldTitel = cursor.getString(DBAdapter.COL_TITEL);
        string_oldDescription = cursor.getString(DBAdapter.COL_DESCRIPTION);


        //Initialize Start and End calendar to iterate Date
        calendarStart = Calendar.getInstance();
        calendarStart.setTimeInMillis(selectedDateOld_Start.getLong_Date());
        calendarEnd = Calendar.getInstance();
        calendarEnd.setTimeInMillis(selectedDateNew_End.getLong_Date());

        SPINNER_YEAR = getResources().getString(R.string.repeated_year);
        SPINNER_MONTH = getResources().getString(R.string.repeated_month);
        SPINNER_WEEK = getResources().getString(R.string.repeated_week);
        SPINNER_DAY = getResources().getString(R.string.repeated_Day);
        string_Every = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_EVERY));

        //get the Currency
        string_Currency = generalHelper.getCurrency();


        //set Filter for Value Input, only allow 2 digits after point and 14 befor point
        editText_EditRepeatedOutgoingsActivity_Value.setFilters(new InputFilter[]{new InputFilterDecimal(14, 2)});

        //create the spinner
        createSpinnerEvery();

        displayItemsOnActivity();

        //set ColorPicker for Button to Edit title color
        button_EditRepeatedOutgoingsActivity_ChooseColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        //set OnclickListener with Datepicker for Start Date
        editText_EditRepeatedOutgoingsActivity_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = selectedDateOld_Start.getInteger_Year();
                month = selectedDateOld_Start.getInteger_Month() - 1;
                day = selectedDateOld_Start.getInteger_day();


                DatePickerDialog dialog = new DatePickerDialog(
                        EditRepeatedOutgoingsActivity.this,
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
                selectedDateNew_Start.setLong_Date(year, month, day);
                calendarStart.set(year, month, day);
                editText_EditRepeatedOutgoingsActivity_Start.setText(selectedDateNew_Start.getString_Date());
            }
        };

        //set onClickListener with Datepicker for End date
        editText_EditRepeatedOutgoingsActivity_End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = selectedDateOld_End.getInteger_Year();
                month = selectedDateOld_End.getInteger_Month() - 1;
                day = selectedDateOld_End.getInteger_day();


                DatePickerDialog dialog = new DatePickerDialog(
                        EditRepeatedOutgoingsActivity.this,
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
                selectedDateNew_End.setLong_Date(year, month, day);
                calendarEnd.set(year, month, day);
                editText_EditRepeatedOutgoingsActivity_End.setText(selectedDateNew_End.getString_Date());
            }
        };
    }

    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // onClick Methods

    //Delet this Item
    public void onClick_DeletReapeatedValuees(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        //Yes button clicked, delet row in Database
                        MainActivity.myDbMain.deleteRowWithRepeatedItem(string_oldTitel, selectedDateOld_Start.getInteger_DateWithoutTime(), selectedDateOld_End.getInteger_DateWithoutTime(), string_oldDescription, double_oldValue);
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        //No button clicked, make nothing
                        break;
                }
            }
        };

        //set the message to show in the DialogWindow
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_question)).setPositiveButton(getString(R.string.dialog_yes), dialogClickListener)
                .setNegativeButton(getString(R.string.dialog_no), dialogClickListener).show();
    }

    //ColorPicker to Edit title color
    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, int_titleColor, true, new AmbilWarnaDialog.OnAmbilWarnaListener() {

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                button_EditRepeatedOutgoingsActivity_ChooseColor.setBackgroundColor(color);
                int_titleColor = color;
                editText_EditRepeatedOutgoingsActivity_Titel.setTextColor(color);
            }
        });
        colorPicker.show();

    }

    public void onClick_SaveReapeatedValues(View view) {

        MainActivity.myDbMain.deleteRowWithRepeatedItem(string_oldTitel, selectedDateOld_Start.getInteger_DateWithoutTime(), selectedDateOld_End.getInteger_DateWithoutTime(), string_oldDescription, double_oldValue);

        double_Value = Double.parseDouble(editText_EditRepeatedOutgoingsActivity_Value.getText().toString());
        string_Description = editText_EditRepeatedOutgoingsActivity_Description.getText().toString();
        string_TitleRepeated = editText_EditRepeatedOutgoingsActivity_Titel.getText().toString();

        if (string_Every.equals(SPINNER_YEAR)) {
            string_EveryForList = SPINNER_YEAR;

            //loop over day by day
            for (; calendarStart.compareTo(calendarEnd) <= 0;
                 calendarStart.add(Calendar.YEAR, 1)) {
                selectedDate.setLong_Date(calendarStart.getTimeInMillis());
                addNewItemRepeatedFirst();

            }

        } else if (string_Every.equals(SPINNER_MONTH)) {
            string_EveryForList = SPINNER_MONTH;

            //loop over day by day
            for (; calendarStart.compareTo(calendarEnd) <= 0;
                 calendarStart.add(Calendar.MONTH, 1)) {
                selectedDate.setLong_Date(calendarStart.getTimeInMillis());
                addNewItemRepeatedFirst();
            }

        } else if (string_Every.equals(SPINNER_WEEK)) {
            string_EveryForList = SPINNER_WEEK;

            //loop over day by day
            for (; calendarStart.compareTo(calendarEnd) <= 0;
                 calendarStart.add(Calendar.DATE, 7)) {
                selectedDate.setLong_Date(calendarStart.getTimeInMillis());
                addNewItemRepeatedFirst();
            }

        } else if (string_Every.equals(SPINNER_DAY)) {
            string_EveryForList = SPINNER_DAY;
            //loop over day by day
            for (; calendarStart.compareTo(calendarEnd) <= 0;
                 calendarStart.add(Calendar.DATE, 1)) {
                selectedDate.setLong_Date(calendarStart.getTimeInMillis());
                addNewItemRepeatedFirst();

            }
        }
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();

    }

    // onClick Methods
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // Database Methods


    //Adds Date and value to Database
    private void addNewItemRepeatedFirst() {
        MainActivity.myDbMain.insertRowRepeatedItem(selectedDate.getLong_Date(), double_Value, string_Description, string_TitleRepeated, string_Currency, selectedDateNew_End.getLong_Date(), string_EveryForList, selectedDateNew_Start.getLong_Date(), int_titleColor);
    }


    // Database Methods
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // Spinner Methods

    //create Spinner to Order Items by values or Date
    private void createSpinnerEvery() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.repeated_arrays, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_EditRepeatedOutgoingsActivity_RepeatBy.setAdapter(adapter);
        spinner_EditRepeatedOutgoingsActivity_RepeatBy.setOnItemSelectedListener(this);
        spinner_EditRepeatedOutgoingsActivity_RepeatBy.setSelection(adapter.getPosition(string_Every));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position).toString().equals(SPINNER_YEAR)) {
            string_Every = SPINNER_YEAR;

        } else if (parent.getItemAtPosition(position).toString().equals(SPINNER_MONTH)) {
            string_Every = SPINNER_MONTH;

        } else if (parent.getItemAtPosition(position).toString().equals(SPINNER_WEEK)) {
            string_Every = SPINNER_WEEK;

        } else if (parent.getItemAtPosition(position).toString().equals(SPINNER_DAY)) {
            string_Every = SPINNER_DAY;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Spinner Methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Displaying Values

    //show Values on Activity
    public void displayItemsOnActivity() {
        editText_EditRepeatedOutgoingsActivity_Value.setText(string_oldValue);
        editText_EditRepeatedOutgoingsActivity_Value.setSelection(editText_EditRepeatedOutgoingsActivity_Value.getText().length());
        editText_EditRepeatedOutgoingsActivity_Titel.setText(string_oldTitel);
        editText_EditRepeatedOutgoingsActivity_Description.setText(string_oldDescription);
        textView_EditRepeatedOutgoingsActivity_Currency.setText(string_Currency);
        editText_EditRepeatedOutgoingsActivity_Start.setText(selectedDateOld_Start.getString_Date());
        editText_EditRepeatedOutgoingsActivity_End.setText(selectedDateOld_End.getString_Date());
    }


// Displaying Values
//----------------------------------------------------------------------------------------------------------------------------------------------
// End}
}