package com.example.leand.outgoingoverview.Activitys;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.leand.outgoingoverview.ListviewHelper.ListViewAdapter;
import com.example.leand.outgoingoverview.R;
import com.example.leand.outgoingoverview.Classes.SelectedDate;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Objects;

public class OverviewListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button button_OverviewListActivity_OK;
    private EditText editText_OverviewListActivity_Year, editText_OverviewListActivity_Month;
    private EditText editText_OverviewListActivity_From, editText_OverviewListActivity_To;
    private Spinner spinner_OverviewListActivity_OrderBy, spinner_OverviewListActivity_Show, spinner_OverviewListActivity_AscendingDescending;
    private ListView listView_OverviewListActivity;
    private TextView textView_OverviewListActivity_totalValue, textView_OverviewListActivity_MonthToStatment, textView_OverviewListActivity_YearFromStatment;

    private SelectedDate selectedDate, selectedDate_Start, selectedDate_End;
    private String string_Currency = "", string_OrderBy_DateValue = "", string_OrderBy_AscendingDescending = "";
    private String string_FilterFor;
    private String SPINNER_YEAR, SPINNER_MONTH, SPINNER_FROMTO, SPINNER_ASCENDING, SPINNER_DESCENDING, SPINNER_VALUE, SPINNER_DATE;
    private Integer integer_OrderBy_Year, integer_OrderBy_Month;

    public static final String EXTRA_INTEGER_ID = "Integer ID";
    ListViewAdapter listViewAdapter;

    DecimalFormat df = new DecimalFormat("0.00");
    private DatePickerDialog.OnDateSetListener mDateSetListenerStart, mDateSetListenerEnd;

    private int year, month, day;

    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_list_);

        //save Currency String
        getCurrency();

        //definition of Items in MainActivity
        listView_OverviewListActivity = findViewById(R.id.listView_OverviewListActivity);
        textView_OverviewListActivity_totalValue = findViewById(R.id.textView_OverviewListActivity_TotalValue);
        spinner_OverviewListActivity_OrderBy = findViewById(R.id.spinner_OverviewListActivity_OrderBy);
        spinner_OverviewListActivity_Show = findViewById(R.id.spinner_OverviewListActivity_Show);
        spinner_OverviewListActivity_AscendingDescending = findViewById(R.id.spinner_OverviewListActivity_AscendingDescending);
        editText_OverviewListActivity_Year = findViewById(R.id.editText_OverviewListActivity_Year);
        editText_OverviewListActivity_Month = findViewById(R.id.editText_OverviewListActivity_Month);
        textView_OverviewListActivity_MonthToStatment = findViewById(R.id.textView_OverviewListActivity_MonthToStatment);
        textView_OverviewListActivity_YearFromStatment = findViewById(R.id.textView_OverviewListActivity_YearFromStatment);
        editText_OverviewListActivity_To = findViewById(R.id.editText_OverviewListActivity_To);
        editText_OverviewListActivity_From = findViewById(R.id.editText_OverviewListActivity_From);
        button_OverviewListActivity_OK = findViewById(R.id.button_OverviewListActivity_OK);

        //get Intent from MainActivity
        Intent intent = getIntent();
        selectedDate = new SelectedDate(intent.getLongExtra(MainActivity.EXTRA_LONG_DATE, -1));
        integer_OrderBy_Month = selectedDate.getInteger_Month();
        integer_OrderBy_Year = selectedDate.getInteger_Year();

        //create new listViewAdapter
        listViewAdapter = new ListViewAdapter(selectedDate.getInteger_Month(), selectedDate.getInteger_Year(), DBAdapter.KEY_DATE, string_Currency, this, ListViewAdapter.ASCENDING);

        //Initialize Start and End Date for DatePicker
        selectedDate_Start = new SelectedDate();
        selectedDate_End = new SelectedDate();

        //get selected Month and Year to Filter the list
        integer_OrderBy_Month = selectedDate.getInteger_Month();
        integer_OrderBy_Year = selectedDate.getInteger_Year();
        editText_OverviewListActivity_Month.setText(integer_OrderBy_Month.toString());
        editText_OverviewListActivity_Year.setText(integer_OrderBy_Year.toString());

        //define Spinners values
        SPINNER_YEAR = getResources().getString(R.string.show_year);
        SPINNER_MONTH = getResources().getString(R.string.show_month);
        SPINNER_FROMTO = getResources().getString(R.string.show_from_to);
        SPINNER_ASCENDING = getResources().getString(R.string.order_by_ascending);
        SPINNER_DESCENDING = getResources().getString(R.string.order_by_descending);
        SPINNER_VALUE = getResources().getString(R.string.order_by_value);
        SPINNER_DATE = getResources().getString(R.string.order_by_date);
        string_FilterFor = getResources().getString(R.string.show_month);

        //create Spinners and arrayList
        createArrayList();
        createSpinnerOrderBy();
        createSpinnerAscendingDescending();
        createSpinnerShow();

        //shows all Items On Activity
        displayItemsOnActivity();


        //set OnclickListener with Datepicker for Start Date
        editText_OverviewListActivity_From.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_OverviewListActivity_From.getText().toString().equals("")) {
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
                        OverviewListActivity.this,
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
                editText_OverviewListActivity_From.setText(selectedDate_Start.getString_Date());
                displayItemsOnActivity();
            }
        };

        //set onClickListener with Datepicker for End date
        editText_OverviewListActivity_To.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_OverviewListActivity_To.getText().toString().equals("")) {
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
                        OverviewListActivity.this,
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
                editText_OverviewListActivity_To.setText(selectedDate_End.getString_Date());
                displayItemsOnActivity();
            }
        };
    }


    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // onClick Methods

    public void onClick_FilterList(View view) {
        if (string_FilterFor.equals(SPINNER_YEAR)) {
            if (!editText_OverviewListActivity_Year.getText().toString().equals("")) {
                integer_OrderBy_Year = Integer.parseInt(editText_OverviewListActivity_Year.getText().toString());
            }
        } else if (string_FilterFor.equals(SPINNER_MONTH)) {
            if (!editText_OverviewListActivity_Year.getText().toString().equals("") && !editText_OverviewListActivity_Month.getText().toString().equals("") && Integer.parseInt(editText_OverviewListActivity_Month.getText().toString()) > 0 && Integer.parseInt(editText_OverviewListActivity_Month.getText().toString()) < 13) {
                integer_OrderBy_Year = Integer.parseInt(editText_OverviewListActivity_Year.getText().toString());
                integer_OrderBy_Month = Integer.parseInt(editText_OverviewListActivity_Month.getText().toString());
            }
        }
        displayItemsOnActivity();
    }

    // onClick Methods
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // Database methods


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

    //sums all values of the selected Month
    private Double sumAllValues() {
        Cursor cursor = listViewAdapter.getCursor();
        Double doubleTotalValue = 0.0;

        if (cursor.moveToFirst()) {
            do {
                doubleTotalValue += cursor.getDouble(DBAdapter.COL_VALUE);
            } while (cursor.moveToNext());
        } else doubleTotalValue = 0.0;

        return doubleTotalValue;
    }

    // Database methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Communicate with other Activitys

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
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
        spinner_OverviewListActivity_OrderBy.setAdapter(adapter);
        spinner_OverviewListActivity_OrderBy.setOnItemSelectedListener(this);
    }

    //create Spinner to order Items in Ascending or Descending order
    private void createSpinnerAscendingDescending() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ascending_descending, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_OverviewListActivity_AscendingDescending.setAdapter(adapter);
        spinner_OverviewListActivity_AscendingDescending.setOnItemSelectedListener(this);
    }

    //create Spinner to select if Items should be Selected by Year, Month or Start and End Date
    private void createSpinnerShow() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.show_arrays, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_OverviewListActivity_Show.setAdapter(adapter);
        spinner_OverviewListActivity_Show.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //see if it should be ordered by Value or date
        if (parent.getId() == R.id.spinner_OverviewListActivity_OrderBy) {
            if (parent.getItemAtPosition(position).toString().equals(SPINNER_DATE)) {
                string_OrderBy_DateValue = DBAdapter.KEY_DATE;

            } else if (parent.getItemAtPosition(position).toString().equals(SPINNER_VALUE)) {
                string_OrderBy_DateValue = DBAdapter.KEY_VALUE;
            }
        }

        //see if it should be ordered ascending or descending
        else if (parent.getId() == R.id.spinner_OverviewListActivity_AscendingDescending) {
            if (parent.getItemAtPosition(position).toString().equals(SPINNER_ASCENDING)) {
                string_OrderBy_AscendingDescending = ListViewAdapter.ASCENDING;

            } else if (parent.getItemAtPosition(position).toString().equals(SPINNER_DESCENDING)) {
                string_OrderBy_AscendingDescending = ListViewAdapter.DESCENDING;
            }
        }
        // set the layout if it should be filtered by year, by month or from start and end Day
        else if (parent.getId() == R.id.spinner_OverviewListActivity_Show)
            if (parent.getItemAtPosition(position).toString().equals(SPINNER_YEAR)) {
                editText_OverviewListActivity_From.setLayoutParams(new ConstraintLayout.LayoutParams(0, 0));
                editText_OverviewListActivity_To.setLayoutParams(new ConstraintLayout.LayoutParams(0, 0));
                editText_OverviewListActivity_Year.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                editText_OverviewListActivity_Month.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                button_OverviewListActivity_OK.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                editText_OverviewListActivity_Month.setVisibility(View.INVISIBLE);
                textView_OverviewListActivity_MonthToStatment.setVisibility(View.INVISIBLE);
                textView_OverviewListActivity_YearFromStatment.setText(getString(R.string.statment_year));
                editText_OverviewListActivity_Year.setText(integer_OrderBy_Year.toString());
                string_FilterFor = SPINNER_YEAR;

            } else if (parent.getItemAtPosition(position).toString().equals(SPINNER_MONTH)) {
                editText_OverviewListActivity_From.setLayoutParams(new ConstraintLayout.LayoutParams(0, 0));
                editText_OverviewListActivity_To.setLayoutParams(new ConstraintLayout.LayoutParams(0, 0));
                editText_OverviewListActivity_Year.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                editText_OverviewListActivity_Month.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                button_OverviewListActivity_OK.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                editText_OverviewListActivity_Month.setVisibility(View.VISIBLE);
                textView_OverviewListActivity_MonthToStatment.setVisibility(View.VISIBLE);
                textView_OverviewListActivity_MonthToStatment.setText(getString(R.string.statment_month));
                textView_OverviewListActivity_YearFromStatment.setText(getString(R.string.statment_year));
                if (string_FilterFor.equals(SPINNER_YEAR)) {
                    editText_OverviewListActivity_Year.setText(integer_OrderBy_Year.toString());
                    editText_OverviewListActivity_Month.setText("");
                } else if (string_FilterFor.equals(SPINNER_FROMTO)) {
                    editText_OverviewListActivity_Year.setText("");
                    editText_OverviewListActivity_Month.setText("");
                } else {
                    editText_OverviewListActivity_Year.setText(integer_OrderBy_Year.toString());
                    editText_OverviewListActivity_Month.setText(integer_OrderBy_Month.toString());
                }
                string_FilterFor = SPINNER_MONTH;

            } else if (parent.getItemAtPosition(position).toString().equals(SPINNER_FROMTO)) {
                editText_OverviewListActivity_From.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                editText_OverviewListActivity_To.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                editText_OverviewListActivity_Year.setLayoutParams(new ConstraintLayout.LayoutParams(0, 0));
                editText_OverviewListActivity_Month.setLayoutParams(new ConstraintLayout.LayoutParams(0, 0));
                button_OverviewListActivity_OK.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                textView_OverviewListActivity_MonthToStatment.setText(getString(R.string.statment_to));
                textView_OverviewListActivity_YearFromStatment.setText(getString(R.string.statment_from));
                editText_OverviewListActivity_From.setText(selectedDate_Start.getString_Date());
                editText_OverviewListActivity_To.setText(selectedDate_End.getString_Date());
                string_FilterFor = SPINNER_FROMTO;

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

    //Creates Arraylist of all values for ListView and adds an onClick Method which opens EditValueActivity and tranfers Databse-ID of selected Value
    private void createArrayList() {
        listView_OverviewListActivity.setAdapter(listViewAdapter.getListViewAdapter());

        //by clicking of Item get Database-ID of Position and open EditValueActivity and send ID
        listView_OverviewListActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //get ID of selected Item from Databse
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                Integer integer_iD = cursor.getInt(cursor.getColumnIndexOrThrow(DBAdapter.KEY_ROWID));

                if (cursor.isNull(DBAdapter.COL_END_DATE)) {
                    //Open EditValueActivity and send ID
                    Intent intent = new Intent(OverviewListActivity.this, EditValueActivity.class);
                    intent.putExtra(EXTRA_INTEGER_ID, integer_iD);
                    startActivityForResult(intent, RESULT_FIRST_USER);
                } else {
                    //Open EditRepeatedOutgoingsActivity and send ID
                    Intent intent = new Intent(OverviewListActivity.this, EditRepeatedOutgoingsActivity.class);
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
        //Actualize Cursor
        if (string_FilterFor.equals(SPINNER_YEAR)) {
            listViewAdapter.setCursorYear(integer_OrderBy_Year, string_OrderBy_DateValue, string_OrderBy_AscendingDescending);
            if (editText_OverviewListActivity_Year.getText().toString().equals("")) {
                Toast.makeText(OverviewListActivity.this,  getString(R.string.toast_noYear),
                        Toast.LENGTH_LONG).show();
            }

        } else if (string_FilterFor.equals(SPINNER_MONTH)) {
            listViewAdapter.setCursorMonthYear(integer_OrderBy_Month, integer_OrderBy_Year, string_OrderBy_DateValue, string_OrderBy_AscendingDescending);

            if (editText_OverviewListActivity_Year.getText().toString().equals("") && editText_OverviewListActivity_Month.getText().toString().equals("")) {
                Toast.makeText(OverviewListActivity.this,  getString(R.string.toast_noYearMonth),
                        Toast.LENGTH_LONG).show();
            } else if (editText_OverviewListActivity_Month.getText().toString().equals("")) {
                Toast.makeText(OverviewListActivity.this,  getString(R.string.toast_noMonth),
                        Toast.LENGTH_LONG).show();
            } else if (editText_OverviewListActivity_Year.getText().toString().equals("")) {
                Toast.makeText(OverviewListActivity.this,  getString(R.string.toast_noYear),
                        Toast.LENGTH_LONG).show();
            } else if (Integer.parseInt(editText_OverviewListActivity_Month.getText().toString()) < 1 || Integer.parseInt(editText_OverviewListActivity_Month.getText().toString()) > 12) {
                Toast.makeText(OverviewListActivity.this,  getString(R.string.toast_invalidMonth),
                        Toast.LENGTH_LONG).show();
            }

        } else if (string_FilterFor.equals(SPINNER_FROMTO)) {
            listViewAdapter.setCursorStartEndDate(selectedDate_Start.getInteger_DateWithoutTime(), selectedDate_End.getInteger_DateWithoutTime(), string_OrderBy_DateValue, string_OrderBy_AscendingDescending);

        }
        //Actualize listView
        listView_OverviewListActivity.setAdapter(listViewAdapter.getListViewAdapter());

        textView_OverviewListActivity_totalValue.setText(df.format(sumAllValues()) + string_Currency);
    }

    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
