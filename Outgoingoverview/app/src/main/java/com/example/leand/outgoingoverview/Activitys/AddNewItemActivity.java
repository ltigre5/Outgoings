package com.example.leand.outgoingoverview.Activitys;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.EditTextFilter.InputFilterDecimal;
import com.example.leand.outgoingoverview.ListviewHelper.ListViewAdapter;
import com.example.leand.outgoingoverview.R;
import com.example.leand.outgoingoverview.Classes.SelectedDate;

import static com.example.leand.outgoingoverview.Activitys.OverviewListActivity.EXTRA_INTEGER_ID;

public class AddNewItemActivity extends AppCompatActivity {
    private DBAdapter myDb;

    private TextView textView_AddNewItemActivity_SelectedDate;
    private TextView textView_AddNewItemActivity_Currency;
    private TextView textView_AddNewItemActivity_TotalValue;
    private EditText editText_AddNewItemActivity_Value;
    private EditText editText_AddNewItemActivity_Description;
    private EditText editText_AddNewItemActivity_Titel;

    private SelectedDate selectedDate;
    private String string_Currency = "";
    private ListViewAdapter listViewAdapter;
    private ListView listView_AddNewItemActivity;


    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        //open Database
        openDB();

        //get Currency
        getCurrency();

        //definition of Items in Activity
        textView_AddNewItemActivity_SelectedDate = findViewById(R.id.textView_AddNewItemActivity_SelectedDate);
        editText_AddNewItemActivity_Value = findViewById(R.id.editText_AddNewItemActivity_Value);
        editText_AddNewItemActivity_Description = findViewById(R.id.editText_AddNewItemActivity_description);
        editText_AddNewItemActivity_Titel = findViewById(R.id.editText_AddNewItemActivity_Titel);
        textView_AddNewItemActivity_Currency = findViewById(R.id.textView_AddNewItemActivity_Currency);
        listView_AddNewItemActivity = findViewById(R.id.listView_AddNewItemActivity);
        textView_AddNewItemActivity_TotalValue = findViewById(R.id.textView_AddNewItemActivity_TotalValue);

        //set Filter for Value Input, only allow 2 digits after point and 14 before point
        editText_AddNewItemActivity_Value.setFilters(new InputFilter[]{new InputFilterDecimal(14, 2)});

        //get Date from MainActivity
        Intent caller = getIntent();
        selectedDate = new SelectedDate(caller.getLongExtra(MainActivity.EXTRA_LONG_DATE, -1));

        //create new listViewAdapter
        listViewAdapter = new ListViewAdapter(selectedDate.getInteger_Month(), selectedDate.getInteger_Year(), DBAdapter.KEY_DATE, string_Currency, this, ListViewAdapter.ASCENDING);
        listViewAdapter.setCursorDate(selectedDate.getInteger_DateWithoutTime());

        //create the arrayList
        createArrayList();

        //show Items on Activity
        displayItemsOnActivity();
    }

    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // onClick Methods

    //save Item on Databse by Button click
    public void onClick_SaveItem(View view) {
        String string_Titel;
        Double double_Value;
        String string_Description;

        //if nothing entered dont save value
        if (editText_AddNewItemActivity_Value.getText().toString().equals("") && editText_AddNewItemActivity_Description.getText().toString().equals("") && editText_AddNewItemActivity_Titel.getText().toString().equals("")) {
            finish();
        }
        //else save values on Database
        else {
            string_Titel = editText_AddNewItemActivity_Titel.getText().toString();
            string_Description = editText_AddNewItemActivity_Description.getText().toString();
            double_Value = Double.parseDouble(editText_AddNewItemActivity_Value.getText().toString());

            addNewItemInDatabase(selectedDate.getLong_Date(), double_Value, selectedDate.getInteger_day(), selectedDate.getInteger_Month(), selectedDate.getInteger_Year(), string_Description, string_Titel, string_Currency, selectedDate.getInteger_DateWithoutTime());
        }

        //return to MainActivity
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }

    // onClick Methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Database methods

    //Adds Date and value to Database
    private void addNewItemInDatabase(long longDate, double value, int int_Day, int int_Month, int int_Year, String description, String titel, String currency, int dateWithoutTime) {
        myDb.insertRow(longDate, value, int_Day, int_Month, int_Year, description, titel, currency, dateWithoutTime);
    }

    //Close Database when Activity is closed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
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

    private void getCurrency() {
        Cursor cursor = myDb.getAllRows();

        if (cursor.moveToFirst()) {
            string_Currency = cursor.getString(cursor.getColumnIndexOrThrow("currency"));
        } else {
            string_Currency = "CHF";
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
                listViewAdapter.setCursorDate(selectedDate.getInteger_DateWithoutTime());
                displayItemsOnActivity();
                listView_AddNewItemActivity.setAdapter(listViewAdapter.getListViewAdapter());
            }
        }
    }

    // Communicate with other Activitys
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // List Methods

    //Creates Arraylist of all values for ListView and adds an onClick Method which opens EditValueActivity and tranfers Databse-ID of selected Value
    private void createArrayList() {
        listView_AddNewItemActivity.setAdapter(listViewAdapter.getListViewAdapter());

        //by clicking of Item get Database-ID of Position and open EditValueActivity and send ID
        listView_AddNewItemActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //get ID of selected Item from Databse
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                Integer integer_iD = cursor.getInt(cursor.getColumnIndexOrThrow(DBAdapter.KEY_ROWID));

                //Open EditValueActivity and send ID
                Intent intent = new Intent(AddNewItemActivity.this, EditValueActivity.class);
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
        textView_AddNewItemActivity_SelectedDate.setText(selectedDate.getString_Date());
        textView_AddNewItemActivity_Currency.setText(string_Currency);
        textView_AddNewItemActivity_TotalValue.setText(sumAllValues().toString());
    }

    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End

}
