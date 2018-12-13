package com.example.leand.outgoingoverview.Activitys;

import android.app.DatePickerDialog;
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

import com.example.leand.outgoingoverview.Classes.GeneralHelper;
import com.example.leand.outgoingoverview.Classes.SelectedDate;
import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.EditTextFilter.InputFilterDecimal;
import com.example.leand.outgoingoverview.ListviewHelper.ListViewAdapter;
import com.example.leand.outgoingoverview.R;

import java.util.Calendar;
import java.util.Objects;

import yuku.ambilwarna.AmbilWarnaDialog;

import static com.example.leand.outgoingoverview.Activitys.OverviewListActivity.EXTRA_INTEGER_ID;

public class AddRepeatedItemsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editText_AddReapeatedValueActivity_Start, editText_AddReapeatedValueActivity_End, editText_AddReapeatedValueActivity_Value;
    private EditText editText_AddReapeatedValueActivity_Title, editText_AddReapeatedValueActivity_Description;
    private TextView textView_AddReapeatedValueActivity_Currency;
    private Button button_AddReapeatedValueActivity_ChooseColor;
    private Spinner spinner_AddReapeatedValueActivity_RepeatBy;
    private ListView listView_AddReapeatedValueActivity;

    private ListViewAdapter listViewAdapter;
    private GeneralHelper generalHelper;
    private SelectedDate selectedDate, selectedDate_Start, selectedDate_End;

    private DatePickerDialog.OnDateSetListener mDateSetListenerStart, mDateSetListenerEnd;

    private String string_Every;
    private String SPINNER_YEAR, SPINNER_MONTH, SPINNER_WEEK, SPINNER_DAY;
    private Calendar calendarStart, calendarEnd;

    private double double_Value = 0.0;
    private String string_Description = "";
    private String string_TitleRepeated = "";
    private String string_Currency = "";
    private int end_Year, end_Month, end_Day;
    private int start_Year, start_Month, start_Day;
    private int int_titleColor = 0xff000000; //for black
    public static boolean MainActivRepeated = false;


    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_repeated_items);

        //Set Toolbar
        Toolbar toolbar=findViewById(R.id.toolbar_MainActivity);
        setSupportActionBar(toolbar);

        //definition of Items in Activity
        editText_AddReapeatedValueActivity_Start = findViewById(R.id.editText_AddReapeatedValueActivity_Start);
        editText_AddReapeatedValueActivity_End = findViewById(R.id.editText_AddReapeatedValueActivity_End);
        editText_AddReapeatedValueActivity_Value = findViewById(R.id.editText_AddReapeatedValueActivity_Value);
        textView_AddReapeatedValueActivity_Currency = findViewById(R.id.textView_AddReapeatedValueActivity_Currency);
        spinner_AddReapeatedValueActivity_RepeatBy = findViewById(R.id.spinner_AddReapeatedValueActivity_RepeatBy);
        editText_AddReapeatedValueActivity_Description = findViewById(R.id.editText_AddReapeatedValueActivity_Description);
        editText_AddReapeatedValueActivity_Title = findViewById(R.id.editText_AddReapeatedValueActivity_Title);
        listView_AddReapeatedValueActivity = findViewById(R.id.listView_AddReapeatedValueActivity);
        button_AddReapeatedValueActivity_ChooseColor = findViewById(R.id.button_AddReapeatedValueActivity_ChooseColor);


        //Initialize Start and End Date
        selectedDate = new SelectedDate();
        selectedDate_Start = new SelectedDate();
        selectedDate_End = new SelectedDate();

        end_Day = start_Day = selectedDate_Start.getInteger_day();
        end_Month = start_Month = selectedDate_Start.getInteger_Month() - 1;
        end_Year = start_Year = selectedDate_Start.getInteger_Year();

        //Initialize Start and End calendar to iterate Date
        calendarStart = Calendar.getInstance();
        calendarEnd = Calendar.getInstance();

        SPINNER_YEAR = getResources().getString(R.string.repeated_year);
        SPINNER_MONTH = getResources().getString(R.string.repeated_month);
        SPINNER_WEEK = getResources().getString(R.string.repeated_week);
        SPINNER_DAY = getResources().getString(R.string.repeated_Day);
        string_Every = SPINNER_YEAR;

        //show start and end Date from today
        editText_AddReapeatedValueActivity_Start.setText(selectedDate_Start.getString_Date());
        editText_AddReapeatedValueActivity_End.setText(selectedDate_End.getString_Date());

        //Initialize General Helper
        generalHelper = new GeneralHelper();

        //set Filter for Value Input, only allow 2 digits after point and 14 befor point
        editText_AddReapeatedValueActivity_Value.setFilters(new InputFilter[]{new InputFilterDecimal(14, 2)});

        //check if Main is Active
        if (!MainActivRepeated) {
            Intent intent = new Intent(AddRepeatedItemsActivity.this, MainActivity.class);
            intent.putExtra(MainActivity.EXTRA_INT_DIRECT_OPEN_ACTIVITY, 2);
            startActivity(intent);
            finish();
        } else {
            //create new listViewAdapter
            listViewAdapter = new ListViewAdapter(this);
            listViewAdapter.setCursorAllWithEndDateNotDuplicate();

            //get Currency
            string_Currency = generalHelper.getCurrency();

            //create the spinner
            createSpinnerEvery();

            //create the arrayList
            createArrayList();
        }

        //set ColorPicker for Button to Edit title color
        button_AddReapeatedValueActivity_ChooseColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        //set OnclickListener with Datepicker for Start Date
        editText_AddReapeatedValueActivity_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(
                        AddRepeatedItemsActivity.this,
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
                selectedDate_Start.setLong_Date(year, month, day);
                calendarStart.setTimeInMillis(selectedDate_Start.getLong_Date());
                editText_AddReapeatedValueActivity_Start.setText(selectedDate_Start.getString_Date());
            }
        };

        //set onClickListener with Datepicker for End date
        editText_AddReapeatedValueActivity_End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(
                        AddRepeatedItemsActivity.this,
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
                selectedDate_End.setLong_Date(year, month, day);
                calendarEnd.setTimeInMillis(selectedDate_End.getLong_Date());
                editText_AddReapeatedValueActivity_End.setText(selectedDate_End.getString_Date());
            }
        };
    }

    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // onClick Methods

    public void onClick_SaveReapeatedValues(View view) {
        if (!editText_AddReapeatedValueActivity_Value.getText().toString().equals("")) {
            double_Value = Double.parseDouble(editText_AddReapeatedValueActivity_Value.getText().toString());
        }

        if (!editText_AddReapeatedValueActivity_Description.getText().toString().equals("")) {
            string_Description = editText_AddReapeatedValueActivity_Description.getText().toString();
        }

        if (!editText_AddReapeatedValueActivity_Title.getText().toString().equals("")) {
            string_TitleRepeated = editText_AddReapeatedValueActivity_Title.getText().toString();
        }

        if (!editText_AddReapeatedValueActivity_Value.getText().toString().equals("") && !editText_AddReapeatedValueActivity_Title.getText().toString().equals("")) {

            calendarStart.setTimeInMillis(selectedDate_Start.getLong_Date());

            if (MainActivity.myDbMain.checkRowRepeatedItem(double_Value, string_TitleRepeated, selectedDate_End.getInteger_DateWithoutTime(), selectedDate_Start.getInteger_DateWithoutTime())) {
                Toast.makeText(AddRepeatedItemsActivity.this, getString(R.string.toast_itemsAlreadyExists),
                        Toast.LENGTH_LONG).show();

            } else {

                if (string_Every.equals(SPINNER_YEAR)) {
                    string_Every = SPINNER_YEAR;

                    //loop over end_Day by end_Day
                    for (; calendarStart.compareTo(calendarEnd) <= 0;
                         calendarStart.add(Calendar.YEAR, 1)) {
                        selectedDate.setLong_Date(calendarStart.getTimeInMillis());
                        addNewItemRepeated();
                    }

                } else if (string_Every.equals(SPINNER_MONTH)) {
                    string_Every = SPINNER_MONTH;

                    //loop over end_Day by end_Day
                    for (; calendarStart.compareTo(calendarEnd) <= 0;
                         calendarStart.add(Calendar.MONTH, 1)) {
                        selectedDate.setLong_Date(calendarStart.getTimeInMillis());
                        addNewItemRepeated();
                    }

                } else if (string_Every.equals(SPINNER_WEEK)) {
                    string_Every = SPINNER_WEEK;

                    //loop over end_Day by end_Day
                    for (; calendarStart.compareTo(calendarEnd) <= 0;
                         calendarStart.add(Calendar.DATE, 7)) {
                        selectedDate.setLong_Date(calendarStart.getTimeInMillis());
                        addNewItemRepeated();
                    }

                } else if (string_Every.equals(SPINNER_DAY)) {
                    string_Every = SPINNER_DAY;
                    //loop over end_Day by end_Day
                    for (; calendarStart.compareTo(calendarEnd) <= 0;
                         calendarStart.add(Calendar.DATE, 1)) {
                        selectedDate.setLong_Date(calendarStart.getTimeInMillis());
                        addNewItemRepeated();

                    }
                }
            }
            displayItemsOnActivity();
            int_titleColor = 0xff000000;
            button_AddReapeatedValueActivity_ChooseColor.setBackgroundColor(int_titleColor);

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
                button_AddReapeatedValueActivity_ChooseColor.setBackgroundColor(color);
                editText_AddReapeatedValueActivity_Title.setTextColor(color);
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
        MainActivity.myDbMain.insertRowRepeatedItem(selectedDate.getLong_Date(), double_Value, string_Description, string_TitleRepeated, string_Currency, selectedDate_End.getLong_Date(), string_Every, selectedDate_Start.getLong_Date(), int_titleColor);
    }

    // Database Methods
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
                Intent intent = new Intent(AddRepeatedItemsActivity.this, EditRepeatedOutgoingsActivity.class);
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
        editText_AddReapeatedValueActivity_Description.setText("");
        editText_AddReapeatedValueActivity_Title.setText("");
        editText_AddReapeatedValueActivity_Value.setText("");

        textView_AddReapeatedValueActivity_Currency.setText(string_Currency);
        listViewAdapter.setCursorAllWithEndDateNotDuplicate();
        listView_AddReapeatedValueActivity.setAdapter(listViewAdapter.getListViewAdapter());
    }

    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
