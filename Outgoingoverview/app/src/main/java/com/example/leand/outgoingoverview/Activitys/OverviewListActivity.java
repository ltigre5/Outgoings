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
import android.widget.DatePicker;
import android.widget.EditText;
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
    private DBAdapter myDb;
    private Calendar calendar;

    private EditText editText_OverviewListActivity_Year;
    private EditText editText_OverviewListActivity_Month;
    private Spinner spinner_OverviewListActivity_OrderBy;
    private Spinner spinner_OverviewListActivity_Show;
    private Spinner spinner_OverviewListActivity_AscendingDescending;
    private ListView listView_OverviewListActivity;
    private TextView textView_OverviewListActivity_totalValue;
    private TextView textView_OverviewListActivity_MonthToStatment;
    private TextView textView_OverviewListActivity_YearFromStatment;

    private SelectedDate selectedDate;
    private SelectedDate selectedDate_Start;
    private SelectedDate selectedDate_End;
    private String string_Currency = "";
    private String string_OrderBy_DateValue = "";
    private Integer integer_OrderBy_Year;
    private Integer integer_OrderBy_Month;
    private String string_OrderBy_AscendingDescending = "";
    private String string_FilterFor = "";
    private static final String SPINNER_YEAR = "Year";
    private static final String SPINNER_MONTH = "Month";
    private static final String SPINNER_FROMTO = "from-to";
    private static final String SPINNER_ASCENDING = "Ascending";
    private static final String SPINNER_DESCENDING = "Descending";
    private static final String SPINNER_VALUE = "Value";
    private static final String SPINNER_DATE = "Date";


    public static final String EXTRA_INTEGER_ID = "Integer ID";
    ListViewAdapter listViewAdapter;

    DecimalFormat df = new DecimalFormat("0.00");
    private DatePickerDialog.OnDateSetListener mDateSetListenerStart, mDateSetListenerEnd;
    private EditText editText_OverviewListActivity_From;
    private EditText editText_OverviewListActivity_To;

    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_list_);

        //open the Database
        openDB();

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

        //get Intent from MainActivity
        Intent intent = getIntent();
        selectedDate = new SelectedDate(intent.getLongExtra("LongSelectedDate", -1));
        integer_OrderBy_Month = selectedDate.getInteger_Month();
        integer_OrderBy_Year = selectedDate.getInteger_Year();

        //create new listViewAdapter
        listViewAdapter = new ListViewAdapter(selectedDate.getInteger_Month(), selectedDate.getInteger_Year(), DBAdapter.KEY_DATE, string_Currency, this, ListViewAdapter.ASCENDING);

        calendar = Calendar.getInstance();
        selectedDate_Start = new SelectedDate(calendar.getTimeInMillis());
        selectedDate_End = new SelectedDate(calendar.getTimeInMillis());

        //get selected Month and Year to Filter the list
        integer_OrderBy_Month = selectedDate.getInteger_Month();
        integer_OrderBy_Year = selectedDate.getInteger_Year();
        editText_OverviewListActivity_Month.setText(integer_OrderBy_Month.toString());
        editText_OverviewListActivity_Year.setText(integer_OrderBy_Year.toString());

        //create Spinners and arrayList
        createArrayList();
        createSpinnerOrderBy();
        createSpinnerAscendingDescending();
        createSpinnerShow();

        //shows all Items On Activity
        displayItemsOnActivity();


        editText_OverviewListActivity_From.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year;
                int month;
                int day;
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
                calendar.set(year, month, day);
                selectedDate_Start = new SelectedDate(calendar.getTimeInMillis());
                editText_OverviewListActivity_From.setText(selectedDate_Start.getString_Date());
            }
        };

        editText_OverviewListActivity_To.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year;
                int month;
                int day;
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
                calendar.set(year, month, day);
                selectedDate_End = new SelectedDate(calendar.getTimeInMillis());
                editText_OverviewListActivity_To.setText(selectedDate_End.getString_Date());
            }
        };
    }


    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // onClick Methods

    public void onClick_FilterList(View view) {
        switch (string_FilterFor) {
            case SPINNER_YEAR:
                integer_OrderBy_Year = Integer.parseInt(editText_OverviewListActivity_Year.getText().toString());
                listViewAdapter.setCursorYear(integer_OrderBy_Year, string_OrderBy_DateValue, string_OrderBy_AscendingDescending);
                listView_OverviewListActivity.setAdapter(listViewAdapter.getListViewAdapter());
                break;
            case SPINNER_MONTH:
                if (Integer.parseInt(editText_OverviewListActivity_Month.getText().toString()) > 0 && Integer.parseInt(editText_OverviewListActivity_Month.getText().toString()) < 13) {
                    integer_OrderBy_Year = Integer.parseInt(editText_OverviewListActivity_Year.getText().toString());
                    integer_OrderBy_Month = Integer.parseInt(editText_OverviewListActivity_Month.getText().toString());
                    listViewAdapter.setCursorMonthYear(integer_OrderBy_Month, integer_OrderBy_Year, string_OrderBy_DateValue, string_OrderBy_AscendingDescending);
                    listView_OverviewListActivity.setAdapter(listViewAdapter.getListViewAdapter());
                } else if (editText_OverviewListActivity_Year.getText().toString().equals("") || editText_OverviewListActivity_Month.getText().toString().equals("")) {
                    Toast.makeText(OverviewListActivity.this, "Enter A Year and Month",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(OverviewListActivity.this, "Enter A valid Month: 1-12",
                            Toast.LENGTH_LONG).show();
                }
                break;
            case SPINNER_FROMTO:
                listViewAdapter.setCursorStartEndDate(selectedDate_Start.getLong_Date(), selectedDate_End.getLong_Date(), string_OrderBy_DateValue, string_OrderBy_AscendingDescending);
                listView_OverviewListActivity.setAdapter(listViewAdapter.getListViewAdapter());
                break;
        }
        displayItemsOnActivity();
    }

    // onClick Methods
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // Database methods

    //Close Database when Activity is closed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
        listViewAdapter.closeDB();
    }

    //open Database
    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }

    //close Database
    private void closeDB() {
        myDb.close();
    }

    //gets Currency out of Database
    private void getCurrency() {
        Cursor cursor = myDb.getAllRows();
        if (cursor.moveToFirst()) {
            string_Currency = cursor.getString(cursor.getColumnIndexOrThrow("currency"));
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
                //set new Cursor
                if (string_FilterFor.equals(SPINNER_YEAR)) {
                    listViewAdapter.setCursorYear(integer_OrderBy_Year, string_OrderBy_DateValue, string_OrderBy_AscendingDescending);
                } else if (string_FilterFor.equals(SPINNER_MONTH)) {
                    listViewAdapter.setCursorMonthYear(integer_OrderBy_Month, integer_OrderBy_Year, string_OrderBy_DateValue, string_OrderBy_AscendingDescending);
                } else if (string_FilterFor.equals(SPINNER_FROMTO)) {
                    listViewAdapter.setCursorStartEndDate(selectedDate_Start.getLong_Date(), selectedDate_End.getLong_Date(), string_OrderBy_DateValue, string_OrderBy_AscendingDescending);
                }
                displayItemsOnActivity();
                listView_OverviewListActivity.setAdapter(listViewAdapter.getListViewAdapter());
            }
        }
    }

    // Communicate with other Activitys
    //----------------------------------------------------------------------------------------------------------------------------------------------
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

                //Open EditValueActivity and send ID
                Intent intent = new Intent(OverviewListActivity.this, EditValueActivity.class);
                intent.putExtra(EXTRA_INTEGER_ID, integer_iD);
                startActivityForResult(intent, RESULT_FIRST_USER);
            }
        });
    }

    // List Methods
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
        if (parent.getId() == R.id.spinner_OverviewListActivity_OrderBy) {
            switch (parent.getItemAtPosition(position).toString()) {
                case SPINNER_DATE:
                    string_OrderBy_DateValue = DBAdapter.KEY_DATE;
                    break;
                case SPINNER_VALUE:
                    string_OrderBy_DateValue = DBAdapter.KEY_VALUE;
                    break;
                default:
                    string_OrderBy_DateValue = DBAdapter.KEY_DATE;
                    break;
            }

        } else if (parent.getId() == R.id.spinner_OverviewListActivity_Show) {
            switch (parent.getItemAtPosition(position).toString()) {
                case SPINNER_YEAR:
                    editText_OverviewListActivity_From.setLayoutParams(new ConstraintLayout.LayoutParams(0, 0));
                    editText_OverviewListActivity_To.setLayoutParams(new ConstraintLayout.LayoutParams(0, 0));
                    editText_OverviewListActivity_Year.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    editText_OverviewListActivity_Month.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    editText_OverviewListActivity_Month.setVisibility(View.INVISIBLE);
                    textView_OverviewListActivity_MonthToStatment.setVisibility(View.INVISIBLE);
                    textView_OverviewListActivity_YearFromStatment.setText("Year:");
                    editText_OverviewListActivity_Year.setText(integer_OrderBy_Year.toString());
                    string_FilterFor = SPINNER_YEAR;
                    break;
                case SPINNER_MONTH:
                    editText_OverviewListActivity_From.setLayoutParams(new ConstraintLayout.LayoutParams(0, 0));
                    editText_OverviewListActivity_To.setLayoutParams(new ConstraintLayout.LayoutParams(0, 0));
                    editText_OverviewListActivity_Year.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    editText_OverviewListActivity_Month.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    editText_OverviewListActivity_Month.setVisibility(View.VISIBLE);
                    textView_OverviewListActivity_MonthToStatment.setVisibility(View.VISIBLE);
                    textView_OverviewListActivity_MonthToStatment.setText("Month:");
                    textView_OverviewListActivity_YearFromStatment.setText("Year:");
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
                    break;
                case SPINNER_FROMTO:
                    editText_OverviewListActivity_From.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    editText_OverviewListActivity_To.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    editText_OverviewListActivity_Year.setLayoutParams(new ConstraintLayout.LayoutParams(0, 0));
                    editText_OverviewListActivity_Month.setLayoutParams(new ConstraintLayout.LayoutParams(0, 0));
                    textView_OverviewListActivity_MonthToStatment.setText("To:");
                    textView_OverviewListActivity_YearFromStatment.setText("From:");
                    editText_OverviewListActivity_From.setText(selectedDate_Start.getString_Date());
                    editText_OverviewListActivity_To.setText(selectedDate_End.getString_Date());
                    string_FilterFor = SPINNER_FROMTO;
                    break;
                default:

                    break;
            }
        } else if (parent.getId() == R.id.spinner_OverviewListActivity_AscendingDescending) {
            switch (parent.getItemAtPosition(position).toString()) {
                case SPINNER_ASCENDING:
                    string_OrderBy_AscendingDescending = ListViewAdapter.ASCENDING;
                    break;
                case SPINNER_DESCENDING:
                    string_OrderBy_AscendingDescending = ListViewAdapter.DESCENDING;
                    break;
                default:
                    string_OrderBy_AscendingDescending = ListViewAdapter.ASCENDING;
                    break;
            }
        }

        //set new Cursor
        if (string_FilterFor.equals(SPINNER_YEAR)) {
            listViewAdapter.setCursorYear(integer_OrderBy_Year, string_OrderBy_DateValue, string_OrderBy_AscendingDescending);
        } else if (string_FilterFor.equals(SPINNER_MONTH)) {
            listViewAdapter.setCursorMonthYear(integer_OrderBy_Month, integer_OrderBy_Year, string_OrderBy_DateValue, string_OrderBy_AscendingDescending);
        } else if (string_FilterFor.equals(SPINNER_FROMTO)) {
            listViewAdapter.setCursorStartEndDate(selectedDate_Start.getLong_Date(), selectedDate_End.getLong_Date(), string_OrderBy_DateValue, string_OrderBy_AscendingDescending);
        }

        //Actualize listView
        listView_OverviewListActivity.setAdapter(listViewAdapter.getListViewAdapter());
        displayItemsOnActivity();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }

    // Spinner Methods
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // Displaying Values

    //show Values on Activity
    public void displayItemsOnActivity() {
        textView_OverviewListActivity_totalValue.setText(df.format(sumAllValues()) + string_Currency);
    }

    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
