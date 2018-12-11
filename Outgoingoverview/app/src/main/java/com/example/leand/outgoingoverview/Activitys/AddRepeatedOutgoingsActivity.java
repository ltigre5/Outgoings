package com.example.leand.outgoingoverview.Activitys;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.leand.outgoingoverview.Classes.SelectedDate;
import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.ListviewHelper.ListViewAdapter;
import com.example.leand.outgoingoverview.R;

import java.util.Calendar;
import java.util.Objects;

import static com.example.leand.outgoingoverview.Activitys.OverviewListActivity.EXTRA_INTEGER_ID;

public class AddRepeatedOutgoingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editText_AddReapeatedValueActivity_Start, editText_AddReapeatedValueActivity_End, editText_AddReapeatedValueActivity_Value;
    private EditText editText_AddReapeatedValueActivity_Titel, editText_AddReapeatedValueActivity_Description;
    private TextView textView_AddReapeatedValueActivity_Currency;
    private Spinner spinner_AddReapeatedValueActivity_RepeatBy;
    private ListView listView_AddReapeatedValueActivity;

    private ListViewAdapter listViewAdapter;

    private SelectedDate selectedDate, selectedDate_Start, selectedDate_End;

    private DatePickerDialog.OnDateSetListener mDateSetListenerStart, mDateSetListenerEnd;

    private String string_Every;
    private String SPINNER_YEAR, SPINNER_MONTH, SPINNER_WEEK, SPINNER_DAY;
    private Calendar calendarStart, calendarEnd;

    private double double_Value = 0.0;
    private String string_Description = "";
    private String string_TitleRepeated = "";
    private String string_Currency = "";

    private int year, month, day;


    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_repeated_outgoings);

        //definition of Items in Activity
        editText_AddReapeatedValueActivity_Start = findViewById(R.id.editText_AddReapeatedValueActivity_Start);
        editText_AddReapeatedValueActivity_End = findViewById(R.id.editText_AddReapeatedValueActivity_End);
        editText_AddReapeatedValueActivity_Value = findViewById(R.id.editText_AddReapeatedValueActivity_Value);
        textView_AddReapeatedValueActivity_Currency = findViewById(R.id.textView_AddReapeatedValueActivity_Currency);
        spinner_AddReapeatedValueActivity_RepeatBy = findViewById(R.id.spinner_AddReapeatedValueActivity_RepeatBy);
        editText_AddReapeatedValueActivity_Description = findViewById(R.id.editText_AddReapeatedValueActivity_Description);
        editText_AddReapeatedValueActivity_Titel = findViewById(R.id.editText_AddReapeatedValueActivity_Titel);
        listView_AddReapeatedValueActivity = findViewById(R.id.listView_AddReapeatedValueActivity);


        //Initialize Start and End Date for DatePicker
        selectedDate = new SelectedDate();
        selectedDate_Start = new SelectedDate();
        selectedDate_End = new SelectedDate();

        //Initialize Start and End calendar to iterate Date
        calendarStart = Calendar.getInstance();
        calendarEnd = Calendar.getInstance();

        SPINNER_YEAR = getResources().getString(R.string.repeated_year);
        SPINNER_MONTH = getResources().getString(R.string.repeated_month);
        SPINNER_WEEK = getResources().getString(R.string.repeated_week);
        SPINNER_DAY = getResources().getString(R.string.repeated_Day);
        string_Every = SPINNER_YEAR;

        //get the Currency
        getCurrency();

        //create new listViewAdapter
        listViewAdapter = new ListViewAdapter(this, string_Currency);
        listViewAdapter.setCursorAllWithEndDateNotDuplicate();

        //create the spinner
        createSpinnerEvery();

        //create the arrayList
        createArrayList();


        //set OnclickListener with Datepicker for Start Date
        editText_AddReapeatedValueActivity_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_AddReapeatedValueActivity_Start.getText().toString().equals("")) {
                    Calendar cal = Calendar.getInstance();
                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH);
                    day = cal.get(Calendar.DAY_OF_MONTH);

                } else {
                    year = selectedDate_Start.getInteger_Year();
                    month = selectedDate_Start.getInteger_Month() - 1;
                    day = selectedDate_Start.getInteger_day();
                }

                DatePickerDialog dialog = new DatePickerDialog(
                        AddRepeatedOutgoingsActivity.this,
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
                calendarStart.set(year, month, day);
                editText_AddReapeatedValueActivity_Start.setText(selectedDate_Start.getString_Date());
            }
        };

        //set onClickListener with Datepicker for End date
        editText_AddReapeatedValueActivity_End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_AddReapeatedValueActivity_End.getText().toString().equals("")) {
                    Calendar cal = Calendar.getInstance();
                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH);
                    day = cal.get(Calendar.DAY_OF_MONTH);

                } else {
                    year = selectedDate_End.getInteger_Year();
                    month = selectedDate_End.getInteger_Month() - 1;
                    day = selectedDate_End.getInteger_day();
                }

                DatePickerDialog dialog = new DatePickerDialog(
                        AddRepeatedOutgoingsActivity.this,
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
                calendarEnd.set(year, month, day);
                editText_AddReapeatedValueActivity_End.setText(selectedDate_End.getString_Date());
            }
        };

    }

    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // onClick Methods

    public void onClick_SaveReapeatedValues(View view) {
        double_Value = Double.parseDouble(editText_AddReapeatedValueActivity_Value.getText().toString());
        string_Description = editText_AddReapeatedValueActivity_Description.getText().toString();
        string_TitleRepeated = editText_AddReapeatedValueActivity_Titel.getText().toString();

        if (string_Every.equals(SPINNER_YEAR)) {
            string_Every = SPINNER_YEAR;

            //loop over day by day
            for (; calendarStart.compareTo(calendarEnd) <= 0;
                 calendarStart.add(Calendar.YEAR, 1)) {
                selectedDate.setLong_Date(calendarStart.getTimeInMillis());
                addNewItemRepeatedFirst();

            }

        } else if (string_Every.equals(SPINNER_MONTH)) {
            string_Every = SPINNER_MONTH;

            //loop over day by day
            for (; calendarStart.compareTo(calendarEnd) <= 0;
                 calendarStart.add(Calendar.MONTH, 1)) {
                selectedDate.setLong_Date(calendarStart.getTimeInMillis());
                addNewItemRepeatedFirst();
            }

        } else if (string_Every.equals(SPINNER_WEEK)) {
            string_Every = SPINNER_WEEK;

            //loop over day by day
            for (; calendarStart.compareTo(calendarEnd) <= 0;
                 calendarStart.add(Calendar.DATE, 7)) {
                selectedDate.setLong_Date(calendarStart.getTimeInMillis());
                addNewItemRepeatedFirst();
            }

        } else if (string_Every.equals(SPINNER_DAY)) {
            string_Every = SPINNER_DAY;
            //loop over day by day
            for (; calendarStart.compareTo(calendarEnd) <= 0;
                 calendarStart.add(Calendar.DATE, 1)) {
                selectedDate.setLong_Date(calendarStart.getTimeInMillis());
                addNewItemRepeatedFirst();

            }
        }
        displayItemsOnActivity();
    }

    // onClick Methods
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // Database Methods


    //Adds Date and value to Database
    private void addNewItemRepeatedFirst() {
        MainActivity.myDbMain.insertRowRepeatedItem(selectedDate.getLong_Date(), double_Value, string_Description, string_TitleRepeated, string_Currency, selectedDate_End.getLong_Date(), string_Every, selectedDate_Start.getLong_Date());
    }


    //gets Currency out of Database
    private void getCurrency() {
        Cursor cursor = MainActivity.myDbMain.getAllRows();
        if (cursor.moveToFirst()) {
            string_Currency = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_CURRENCY));
        } else {
            string_Currency = "";
        }
        cursor.close();
    }

    // Database Methods
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // Spinner Methods

    //create Spinner to Order Items by values or Date
    private void createSpinnerEvery() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.repeated_arrays, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_AddReapeatedValueActivity_RepeatBy.setAdapter(adapter);
        spinner_AddReapeatedValueActivity_RepeatBy.setOnItemSelectedListener(this);
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

    // Communicate with other Activitys

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //set new Cursor

                displayItemsOnActivity();
            }
        }
    }

    // Communicate with other Activitys

    //----------------------------------------------------------------------------------------------------------------------------------------------
    // List Methods

    //Creates Arraylist of all values for ListView and adds an onClick Method which opens EditValueActivity and tranfers Databse-ID of selected Value
    private void createArrayList() {
        listView_AddReapeatedValueActivity.setAdapter(listViewAdapter.getListViewAdapter());

        //by clicking of Item get Database-ID of Position and open EditValueActivity and send ID
        listView_AddReapeatedValueActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //get ID of selected Item from Databse
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                Integer integer_iD = cursor.getInt(cursor.getColumnIndexOrThrow(DBAdapter.KEY_ROWID));

                //Open EditValueActivity and send ID
                Intent intent = new Intent(AddRepeatedOutgoingsActivity.this, EditRepeatedOutgoingsActivity.class);
                intent.putExtra(EXTRA_INTEGER_ID, integer_iD);
                startActivityForResult(intent, RESULT_FIRST_USER);
            }
        });
    }

    // List Methods
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // Displaying Values

    //show Values on Activity
    public void displayItemsOnActivity() {
        textView_AddReapeatedValueActivity_Currency.setText(string_Currency);
        listViewAdapter.setCursorAllWithEndDateNotDuplicate();
        listView_AddReapeatedValueActivity.setAdapter(listViewAdapter.getListViewAdapter());
    }

    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
