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
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leand.outgoingoverview.GeneralHelperClasses.GeneralHelper;
import com.example.leand.outgoingoverview.GeneralHelperClasses.SelectedDate;
import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.EditTextFilter.InputFilterDecimal;
import com.example.leand.outgoingoverview.ListviewHelper.ListViewAdapter;
import com.example.leand.outgoingoverview.R;
import com.example.leand.outgoingoverview.Widget.AddNewOutgoingWidget;

import java.util.Calendar;
import java.util.Objects;

import yuku.ambilwarna.AmbilWarnaDialog;

import static com.example.leand.outgoingoverview.Activitys.OverviewActivity.EXTRA_INTEGER_ID;

public class AddRepeatedItemsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editText_AddRepeatedItemsActivity_Start, editText_AddRepeatedItemsActivity_End, editText_AddRepeatedItemsActivity_Value;
    private EditText editText_AddRepeatedItemsActivity_Title, editText_AddRepeatedItemsActivity_Description,editText_AddRepeatedItemsActivity_RepeatedByTimes;
    private TextView textView_AddRepeatedItemsActivity_Currency;
    private Button button_AddRepeatedItemsActivity_ChooseColor;
    private Spinner spinner_AddRepeatedItemsActivity_RepeatBy;
    private ListView listView_AddRepeatedItemsActivity;

    private ListViewAdapter listViewAdapter;
    private GeneralHelper generalHelper;
    private SelectedDate selectedDate_ForIteration, selectedDate_Start, selectedDate_End;
    private Calendar calendarStart, calendarEnd, calendarForIteration;

    private DatePickerDialog.OnDateSetListener mDateSetListenerStart, mDateSetListenerEnd;

    private String string_Every;
    private String SPINNER_YEAR, SPINNER_MONTH, SPINNER_WEEK, SPINNER_DAY;
    private int int_RepeatedByTimes;

    private String string_Description;
    private String string_Title;
    private String string_Currency;
    private double double_Value;
    private int int_titleColor = 0xff000000; //for black



    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_repeated_items);

        Log.e("AddRepeatedNewItem", "Start");

        //Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_MainActivity);
        setSupportActionBar(toolbar);

        //definition of Items in Activity
        editText_AddRepeatedItemsActivity_Start = findViewById(R.id.editText_AddRepeatedItemsActivity_Start);
        editText_AddRepeatedItemsActivity_End = findViewById(R.id.editText_AddRepeatedItemsActivity_End);
        editText_AddRepeatedItemsActivity_Value = findViewById(R.id.editText_AddRepeatedItemsActivity_Value);
        textView_AddRepeatedItemsActivity_Currency = findViewById(R.id.textView_AddRepeatedItemsActivity_Currency);
        spinner_AddRepeatedItemsActivity_RepeatBy = findViewById(R.id.spinner_AddRepeatedItemsActivity_RepeatBy);
        editText_AddRepeatedItemsActivity_Description = findViewById(R.id.editText_AddRepeatedItemsActivity_Description);
        editText_AddRepeatedItemsActivity_Title = findViewById(R.id.editText_AddRepeatedItemsActivity_Title);
        listView_AddRepeatedItemsActivity = findViewById(R.id.listView_AddRepeatedItemsActivity);
        button_AddRepeatedItemsActivity_ChooseColor = findViewById(R.id.button_AddRepeatedItemsActivity_ChooseColor);
        editText_AddRepeatedItemsActivity_RepeatedByTimes=findViewById(R.id.editText_AddRepeatedItemsActivity_RepeatedByTimes);


        //Initialize Start and End Date
        selectedDate_ForIteration = new SelectedDate();
        selectedDate_Start = new SelectedDate();
        selectedDate_End = new SelectedDate();

        //Initialize Start and End calendar to iterate Date
        calendarStart = Calendar.getInstance();
        calendarEnd = Calendar.getInstance();
        calendarForIteration=Calendar.getInstance();

        SPINNER_YEAR = getResources().getString(R.string.repeated_year);
        SPINNER_MONTH = getResources().getString(R.string.repeated_month);
        SPINNER_WEEK = getResources().getString(R.string.repeated_week);
        SPINNER_DAY = getResources().getString(R.string.repeated_Day);


        //show start and end Date from today
        editText_AddRepeatedItemsActivity_Start.setText(selectedDate_Start.getString_Date());
        editText_AddRepeatedItemsActivity_End.setText(selectedDate_End.getString_Date());

        //Initialize General Helper
        generalHelper = new GeneralHelper();

        //set Filter for Value Input, only allow 2 digits after point and 14 befor point
        editText_AddRepeatedItemsActivity_Value.setFilters(new InputFilter[]{new InputFilterDecimal(14, 2)});

        //check if Main is Active for Database, if not open MainActivity first and Re-Enter AddRepeatedItemsActivity
        if (MainActivity.myDbMain==null) {
            Log.e("Widget", "StartRepeated");
            Intent intent = new Intent(AddRepeatedItemsActivity.this, MainActivity.class);
            intent.putExtra(AddNewOutgoingWidget.EXTRA_INT_DIRECT_OPEN_ACTIVITY, 2);
            startActivity(intent);
            finish();

            //else get things from Database
        } else {
            listViewAdapter = new ListViewAdapter(this);
            listViewAdapter.setCursorAllRepeatedNotDuplicate();

            string_Currency = generalHelper.getCurrency();

            createSpinnerEvery();
            createArrayList();
            displayItemsOnActivity();
        }

        //set ColorPicker for Button to Edit title color
        button_AddRepeatedItemsActivity_ChooseColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        //set OnclickListener with Datepicker for Start Date
        editText_AddRepeatedItemsActivity_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(
                        AddRepeatedItemsActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenerStart,
                        selectedDate_Start.getInteger_Year(), selectedDate_Start.getInteger_Month() - 1, selectedDate_Start.getInteger_day());
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListenerStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                selectedDate_Start.setLong_Date(year, month, day);
                editText_AddRepeatedItemsActivity_Start.setText(selectedDate_Start.getString_Date());
            }
        };

        //set onClickListener with Datepicker for End date
        editText_AddRepeatedItemsActivity_End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(
                        AddRepeatedItemsActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenerEnd,
                        selectedDate_Start.getInteger_Year(), selectedDate_End.getInteger_Month() - 1, selectedDate_End.getInteger_day());
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListenerEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                selectedDate_End.setLong_Date(year, month, day);
                editText_AddRepeatedItemsActivity_End.setText(selectedDate_End.getString_Date());
            }
        };
    }

    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // onClick Methods

    public void onClick_SaveRepeatedValues(View view) {
        if (!editText_AddRepeatedItemsActivity_Value.getText().toString().equals("")) {
            double_Value = Double.parseDouble(editText_AddRepeatedItemsActivity_Value.getText().toString());
        }

        if (!editText_AddRepeatedItemsActivity_Description.getText().toString().equals("")) {
            string_Description = editText_AddRepeatedItemsActivity_Description.getText().toString();
        }

        if (!editText_AddRepeatedItemsActivity_Title.getText().toString().equals("")) {
            string_Title = editText_AddRepeatedItemsActivity_Title.getText().toString();
        }

        if (!editText_AddRepeatedItemsActivity_Value.getText().toString().equals("") && !editText_AddRepeatedItemsActivity_Title.getText().toString().equals("")) {

            //get start and end date
            calendarStart.setTimeInMillis(selectedDate_Start.getLong_Date());
            selectedDate_ForIteration.setLong_Date(selectedDate_Start.getLong_Date());
            calendarEnd.setTimeInMillis(selectedDate_End.getLong_Date());
            calendarForIteration.setTimeInMillis(calendarStart.getTimeInMillis());
            int_RepeatedByTimes=Integer.parseInt(editText_AddRepeatedItemsActivity_RepeatedByTimes.getText().toString());

            if (MainActivity.myDbMain.checkRowRepeatedItem(string_Title, double_Value,selectedDate_Start.getInteger_DateWithoutTime(),selectedDate_End.getInteger_DateWithoutTime(),int_RepeatedByTimes, generalHelper.setRepeatedByWithNumber(string_Every,this))) {
                Toast.makeText(AddRepeatedItemsActivity.this, getString(R.string.toast_itemsAlreadyExists),
                        Toast.LENGTH_LONG).show();

            } else {

                if (string_Every.equals(SPINNER_YEAR)) {

                    //loop over start to end date
                    for (; calendarForIteration.compareTo(calendarEnd) <= 0; calendarForIteration.add(Calendar.YEAR, int_RepeatedByTimes)) {
                        selectedDate_ForIteration.setLong_Date(calendarForIteration.getTimeInMillis());
                        addNewItemRepeated();
                    }

                } else if (string_Every.equals(SPINNER_MONTH)) {

                    //loop over start to end date
                    for (; calendarForIteration.compareTo(calendarEnd) <= 0; calendarForIteration.add(Calendar.MONTH, int_RepeatedByTimes)) {
                        selectedDate_ForIteration.setLong_Date(calendarForIteration.getTimeInMillis());
                        addNewItemRepeated();
                    }

                } else if (string_Every.equals(SPINNER_WEEK)) {

                    //loop over start to end date
                    for (; calendarForIteration.compareTo(calendarEnd) <= 0; calendarForIteration.add(Calendar.DATE, 7*int_RepeatedByTimes)) {
                        selectedDate_ForIteration.setLong_Date(calendarForIteration.getTimeInMillis());
                        addNewItemRepeated();
                    }

                } else if (string_Every.equals(SPINNER_DAY)) {

                    //loop over start to end date
                    for (; calendarForIteration.compareTo(calendarEnd) <= 0; calendarForIteration.add(Calendar.DATE, int_RepeatedByTimes)) {
                        selectedDate_ForIteration.setLong_Date(calendarForIteration.getTimeInMillis());
                        addNewItemRepeated();
                    }
                }
            }

            makeDefaultActivity();
            displayItemsOnActivity();


        } else {
            Toast.makeText(AddRepeatedItemsActivity.this, getString(R.string.toast_enterATitleAndValue),
                    Toast.LENGTH_LONG).show();
        }
    }

    //ColorPicker to Edit title color
    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, int_titleColor, true, new AmbilWarnaDialog.OnAmbilWarnaListener() {

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                int_titleColor = color;
                button_AddRepeatedItemsActivity_ChooseColor.setBackgroundColor(color);
                editText_AddRepeatedItemsActivity_Title.setTextColor(color);
            }
        });
        colorPicker.show();
    }

    @Override
    public void onBackPressed() {
        //return to MainActivity
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }

    // onClick Methods
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // Database Methods

    //Adds Date and value to Database
    private void addNewItemRepeated() {
        MainActivity.myDbMain.insertRowRepeatedItem(string_Title,int_titleColor,double_Value,string_Description,selectedDate_ForIteration.getLong_Date(),selectedDate_Start.getLong_Date(),selectedDate_End.getLong_Date(), int_RepeatedByTimes, generalHelper.setRepeatedByWithNumber(string_Every,this));
    }

    // Database Methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Communicate with other Activity's

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

    // Communicate with other Activity's
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // Spinner Methods

    //create Spinner to Order Items by values or Date
    private void createSpinnerEvery() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.repeated_arrays, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_AddRepeatedItemsActivity_RepeatBy.setAdapter(adapter);
        spinner_AddRepeatedItemsActivity_RepeatBy.setOnItemSelectedListener(this);

        string_Every = spinner_AddRepeatedItemsActivity_RepeatBy.getSelectedItem().toString();
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
    // List Methods

    //Creates Arraylist of all values for ListView and adds an onClick Method which opens EditItemActivity and tranfers Databse-ID of selected Value
    private void createArrayList() {
        listView_AddRepeatedItemsActivity.setAdapter(listViewAdapter.getListViewAdapter());

        //by clicking of Item get Database-ID of Position and open EditItemActivity and send ID
        listView_AddRepeatedItemsActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //get ID of selected Item from Database
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                Integer integer_iD = cursor.getInt(cursor.getColumnIndexOrThrow(DBAdapter.KEY_ROWID));

                //Open EditItemActivity and send ID
                Intent intent = new Intent(AddRepeatedItemsActivity.this, EditRepeatedItemsActivity.class);
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
        editText_AddRepeatedItemsActivity_Start.setText(selectedDate_Start.getString_Date());
        editText_AddRepeatedItemsActivity_End.setText(selectedDate_End.getString_Date());
        editText_AddRepeatedItemsActivity_Title.requestFocus();

        textView_AddRepeatedItemsActivity_Currency.setText(string_Currency);

        listViewAdapter.setCursorAllRepeatedNotDuplicate();
        listView_AddRepeatedItemsActivity.setAdapter(listViewAdapter.getListViewAdapter());
    }

    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // default values

    private void makeDefaultActivity() {
        editText_AddRepeatedItemsActivity_Description.setText("");
        editText_AddRepeatedItemsActivity_Title.setText("");
        editText_AddRepeatedItemsActivity_Value.setText("");

        int_titleColor = 0xff000000;
        editText_AddRepeatedItemsActivity_Title.setTextColor(int_titleColor);
        button_AddRepeatedItemsActivity_ChooseColor.setBackgroundColor(int_titleColor);

        selectedDate_Start.setLong_DateToday();
        selectedDate_End.setLong_DateToday();
    }

    // default values
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
                finish();
                return true;

            case R.id.menu_AddCurrency:
                intent = new Intent(this, CurrencyActivity.class);
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
