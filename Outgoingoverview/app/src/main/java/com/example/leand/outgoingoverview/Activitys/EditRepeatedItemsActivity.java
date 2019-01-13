package com.example.leand.outgoingoverview.Activitys;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.leand.outgoingoverview.GeneralHelperClasses.EditHelper;
import com.example.leand.outgoingoverview.GeneralHelperClasses.GeneralHelper;
import com.example.leand.outgoingoverview.GeneralHelperClasses.SelectedDate;
import com.example.leand.outgoingoverview.EditTextFilter.InputFilterDecimal;
import com.example.leand.outgoingoverview.R;

import java.util.Calendar;
import java.util.Objects;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EditRepeatedItemsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editText_EditRepeatedItemsActivity_Start, editText_EditRepeatedItemsActivity_End, editText_EditRepeatedItemsActivity_Value;
    private EditText editText_EditRepeatedItemsActivity_Title, editText_EditRepeatedItemsActivity_Description,editText_EditRepeatedItemsActivity_RepeatedByTimes;
    private TextView textView_EditRepeatedItemsActivity_Currency;
    private Button button_EditRepeatedItemsActivity_ChooseColor;
    private Spinner spinner_EditRepeatedItemsActivity_RepeatBy;

    private SelectedDate selectedDate_ForIteration, selectedDateNew_Start, selectedDateNew_End, selectedDateOld_Start, selectedDateOld_End;

    private DatePickerDialog.OnDateSetListener mDateSetListenerStart, mDateSetListenerEnd;

    private String string_NewEvery;
    private String SPINNER_YEAR, SPINNER_MONTH, SPINNER_WEEK, SPINNER_DAY;
    private Integer integer_RepeatedByTimes;
    private Calendar calendarStart, calendarEnd, calendarForIteration;

    private double double_NewValue;
    private String string_NewDescription, string_NewTitle, string_Currency;

    private GeneralHelper generalHelper;
    private EditHelper editHelper;

    private double double_oldValue;
    private String string_oldValue;
    private String string_oldTitle;
    private String string_oldDescription;
    private int int_titleColor = 0xff000000; //for black
    private int int_OldEvery;

    private int end_Year, end_Month, end_Day;
    private int start_Year, start_Month, start_Day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_repeated_items);

        //Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_MainActivity);
        setSupportActionBar(toolbar);

        //definition of Items in Activity
        editText_EditRepeatedItemsActivity_Start = findViewById(R.id.editText_EditRepeatedItemsActivity_Start);
        editText_EditRepeatedItemsActivity_End = findViewById(R.id.editText_EditRepeatedItemsActivity_End);
        editText_EditRepeatedItemsActivity_Value = findViewById(R.id.editText_EditRepeatedItemsActivity_Value);
        textView_EditRepeatedItemsActivity_Currency = findViewById(R.id.textView_EditRepeatedItemsActivity_Currency);
        spinner_EditRepeatedItemsActivity_RepeatBy = findViewById(R.id.spinner_EditRepeatedItemsActivity_RepeatBy);
        editText_EditRepeatedItemsActivity_Description = findViewById(R.id.editText_EditRepeatedItemsActivity_Description);
        editText_EditRepeatedItemsActivity_Title = findViewById(R.id.editText_EditRepeatedItemsActivity_Title);
        button_EditRepeatedItemsActivity_ChooseColor = findViewById(R.id.button_EditRepeatedItemsActivity_ChooseColor);
        editText_EditRepeatedItemsActivity_RepeatedByTimes=findViewById(R.id.editText_EditRepeatedItemsActivity_RepeatedByTimes);

        //Initialize Start and End Date for DatePicker
        selectedDate_ForIteration = new SelectedDate();
        selectedDateOld_Start = new SelectedDate();
        selectedDateOld_End = new SelectedDate();
        selectedDateNew_Start = new SelectedDate();
        selectedDateNew_End = new SelectedDate();

        //get Database-ID from OverviewActivity
        Intent caller = getIntent();
        Integer interger_ID = caller.getIntExtra(OverviewActivity.EXTRA_INTEGER_ID, -1);

        //Initialize General Helper
        generalHelper = new GeneralHelper();
        editHelper= new EditHelper(interger_ID);

        //get old values to Edit
        selectedDateNew_Start.setLong_Date(editHelper.getLongStartDate());
        selectedDateNew_End.setLong_Date(editHelper.getLongEndDate());
        selectedDateOld_Start.setLong_Date(editHelper.getLongStartDate());
        selectedDateOld_End.setLong_Date(editHelper.getLongEndDate());
        double_oldValue = editHelper.getDoubleValue();
        string_oldValue = editHelper.getStringValue();
        string_oldTitle = editHelper.getStringTitle();
        string_oldDescription = editHelper.getStringDescription();
        int_titleColor=editHelper.getIntTitleColor();
        integer_RepeatedByTimes =editHelper.getIntRepeatedByTimes();
        int_OldEvery=generalHelper.setRepeatedEveryWithNumber(string_NewEvery,EditRepeatedItemsActivity.this);


        //set color of button color chooser and title
        button_EditRepeatedItemsActivity_ChooseColor.setBackgroundColor(int_titleColor);
        editText_EditRepeatedItemsActivity_Title.setTextColor(int_titleColor);

        //set date chooser values
        end_Day = selectedDateOld_End.getInteger_day();
        start_Day = selectedDateOld_Start.getInteger_day();
        end_Month = selectedDateOld_End.getInteger_Month() - 1;
        start_Month = selectedDateOld_Start.getInteger_Month() - 1;
        end_Year = selectedDateOld_End.getInteger_Year();
        start_Year = selectedDateOld_Start.getInteger_Year();

        //Initialize Start and End calendar to iterate Date
        calendarStart = Calendar.getInstance();
        calendarStart.setTimeInMillis(selectedDateOld_Start.getLong_Date());
        calendarEnd = Calendar.getInstance();
        calendarEnd.setTimeInMillis(selectedDateNew_End.getLong_Date());
        calendarForIteration=Calendar.getInstance();
        calendarForIteration.setTimeInMillis(calendarStart.getTimeInMillis());

        SPINNER_YEAR = getResources().getString(R.string.repeated_year);
        SPINNER_MONTH = getResources().getString(R.string.repeated_month);
        SPINNER_WEEK = getResources().getString(R.string.repeated_week);
        SPINNER_DAY = getResources().getString(R.string.repeated_Day);

        //get the Currency
        string_Currency = generalHelper.getCurrency();


        //set Filter for Value Input, only allow 2 digits after point and 14 befor point
        editText_EditRepeatedItemsActivity_Value.setFilters(new InputFilter[]{new InputFilterDecimal(14, 2)});

        //create the spinner
        createSpinnerEvery();

        displayItemsOnActivity();

        //set ColorPicker for Button to Edit title color
        button_EditRepeatedItemsActivity_ChooseColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        //set OnclickListener with Datepicker for Start Date
        editText_EditRepeatedItemsActivity_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(
                        EditRepeatedItemsActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenerStart,
                        start_Year, start_Month, start_Day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListenerStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                start_Year = year;
                start_Month = month;
                start_Day = day;
                selectedDateNew_Start.setLong_Date(year, month, day);
                calendarStart.set(year, month, day);
                editText_EditRepeatedItemsActivity_Start.setText(selectedDateNew_Start.getString_Date());
            }
        };

        //set onClickListener with Datepicker for End date
        editText_EditRepeatedItemsActivity_End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(
                        EditRepeatedItemsActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenerEnd,
                        end_Year, end_Month, end_Day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListenerEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                end_Year = year;
                end_Month = month;
                end_Day = day;
                selectedDateNew_End.setLong_Date(year, month, day);
                calendarEnd.set(year, month, day);
                editText_EditRepeatedItemsActivity_End.setText(selectedDateNew_End.getString_Date());
            }
        };
    }

    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // onClick Methods

    //Delet this Item
    public void onClick_DeletReapeatedValues(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        //Yes button clicked, delete row in Database
                        MainActivity.myDbMain.deleteRowWithRepeatedItem(string_oldTitle,double_oldValue,selectedDateOld_Start.getInteger_DateWithoutTime(),selectedDateOld_End.getInteger_DateWithoutTime(), integer_RepeatedByTimes, int_OldEvery);
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
                button_EditRepeatedItemsActivity_ChooseColor.setBackgroundColor(color);
                int_titleColor = color;
                editText_EditRepeatedItemsActivity_Title.setTextColor(color);
            }
        });
        colorPicker.show();

    }

    public void onClick_SaveRepeatedValues(View view) {

        MainActivity.myDbMain.deleteRowWithRepeatedItem(string_oldTitle,double_oldValue,selectedDateOld_Start.getInteger_DateWithoutTime(),selectedDateOld_End.getInteger_DateWithoutTime(), integer_RepeatedByTimes,int_OldEvery);

        if (!editText_EditRepeatedItemsActivity_Value.getText().toString().equals("")) {
            double_NewValue = Double.parseDouble(editText_EditRepeatedItemsActivity_Value.getText().toString());
        }

        if (!editText_EditRepeatedItemsActivity_Description.getText().toString().equals("")) {
            string_NewDescription = editText_EditRepeatedItemsActivity_Description.getText().toString();
        }

        if (!editText_EditRepeatedItemsActivity_Title.getText().toString().equals("")) {
            string_NewTitle = editText_EditRepeatedItemsActivity_Title.getText().toString();
        }

        if (!editText_EditRepeatedItemsActivity_Value.getText().toString().equals("") && !editText_EditRepeatedItemsActivity_Title.getText().toString().equals("")) {

            calendarStart.setTimeInMillis(selectedDateNew_Start.getLong_Date());
            selectedDate_ForIteration.setLong_Date(selectedDateNew_Start.getLong_Date());
            calendarEnd.setTimeInMillis(selectedDateNew_End.getLong_Date());
            calendarForIteration.setTimeInMillis(calendarStart.getTimeInMillis());
            integer_RepeatedByTimes =Integer.parseInt(editText_EditRepeatedItemsActivity_RepeatedByTimes.getText().toString());

            if (MainActivity.myDbMain.checkRowRepeatedItem(string_NewTitle, double_NewValue,selectedDateNew_Start.getInteger_DateWithoutTime(),selectedDateNew_End.getInteger_DateWithoutTime(), integer_RepeatedByTimes,generalHelper.setRepeatedEveryWithNumber(string_NewEvery,this))) {
                Toast.makeText(EditRepeatedItemsActivity.this, getString(R.string.toast_itemsAlreadyExists),
                        Toast.LENGTH_LONG).show();

            } else {

                if (string_NewEvery.equals(SPINNER_YEAR)) {

                    //loop over end_Day by end_Day
                    for (; calendarForIteration.compareTo(calendarEnd) <= 0; calendarForIteration.add(Calendar.YEAR, integer_RepeatedByTimes)) {
                        selectedDate_ForIteration.setLong_Date(calendarForIteration.getTimeInMillis());
                        addNewItemRepeated();
                    }

                } else if (string_NewEvery.equals(SPINNER_MONTH)) {

                    //loop over end_Day by end_Day
                    for (; calendarForIteration.compareTo(calendarEnd) <= 0; calendarForIteration.add(Calendar.MONTH, integer_RepeatedByTimes)) {
                        selectedDate_ForIteration.setLong_Date(calendarForIteration.getTimeInMillis());
                        addNewItemRepeated();
                    }

                } else if (string_NewEvery.equals(SPINNER_WEEK)) {

                    //loop over end_Day by end_Day
                    for (; calendarForIteration.compareTo(calendarEnd) <= 0; calendarForIteration.add(Calendar.DATE, 7* integer_RepeatedByTimes)) {
                        selectedDate_ForIteration.setLong_Date(calendarForIteration.getTimeInMillis());
                        addNewItemRepeated();
                    }

                } else if (string_NewEvery.equals(SPINNER_DAY)) {

                    //loop over end_Day by end_Day
                    for (; calendarForIteration.compareTo(calendarEnd) <= 0; calendarForIteration.add(Calendar.DATE, integer_RepeatedByTimes)) {
                        selectedDate_ForIteration.setLong_Date(calendarForIteration.getTimeInMillis());
                        addNewItemRepeated();

                    }
                }
            }


        } else {

            Toast.makeText(EditRepeatedItemsActivity.this, getString(R.string.toast_enterATitleAndValue),
                    Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();

    }

    // onClick Methods
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // Database Methods


    //Adds Date and value to Database
    private void addNewItemRepeated() {
        MainActivity.myDbMain.insertRowRepeatedItem(string_NewTitle,int_titleColor,double_NewValue,string_NewDescription, selectedDate_ForIteration.getLong_Date(),selectedDateNew_Start.getLong_Date(),selectedDateNew_End.getLong_Date(), integer_RepeatedByTimes, generalHelper.setRepeatedEveryWithNumber(string_NewEvery,this));
    }


    // Database Methods
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // Spinner Methods

    //create Spinner to Order Items by values or Date
    private void createSpinnerEvery() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.repeated_arrays, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_EditRepeatedItemsActivity_RepeatBy.setAdapter(adapter);
        spinner_EditRepeatedItemsActivity_RepeatBy.setOnItemSelectedListener(this);

        spinner_EditRepeatedItemsActivity_RepeatBy.setSelection(adapter.getPosition(generalHelper.getRepeatedEveryWithNumber(editHelper.getIntEvery(),this)));
        string_NewEvery = spinner_EditRepeatedItemsActivity_RepeatBy.getSelectedItem().toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position).toString().equals(SPINNER_YEAR)) {
            string_NewEvery = SPINNER_YEAR;

        } else if (parent.getItemAtPosition(position).toString().equals(SPINNER_MONTH)) {
            string_NewEvery = SPINNER_MONTH;

        } else if (parent.getItemAtPosition(position).toString().equals(SPINNER_WEEK)) {
            string_NewEvery = SPINNER_WEEK;

        } else if (parent.getItemAtPosition(position).toString().equals(SPINNER_DAY)) {
            string_NewEvery = SPINNER_DAY;
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
        editText_EditRepeatedItemsActivity_Title.setTextColor(int_titleColor);
        editText_EditRepeatedItemsActivity_Value.setText(string_oldValue);
        editText_EditRepeatedItemsActivity_Value.setSelection(editText_EditRepeatedItemsActivity_Value.getText().length());
        editText_EditRepeatedItemsActivity_Title.setText(string_oldTitle);
        editText_EditRepeatedItemsActivity_Description.setText(string_oldDescription);
        textView_EditRepeatedItemsActivity_Currency.setText(string_Currency);
        editText_EditRepeatedItemsActivity_Start.setText(selectedDateOld_Start.getString_Date());
        editText_EditRepeatedItemsActivity_End.setText(selectedDateOld_End.getString_Date());
        editText_EditRepeatedItemsActivity_RepeatedByTimes.setText(integer_RepeatedByTimes.toString());
    }


// Displaying Values
//----------------------------------------------------------------------------------------------------------------------------------------------
// End}
}