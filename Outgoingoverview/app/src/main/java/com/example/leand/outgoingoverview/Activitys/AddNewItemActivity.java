package com.example.leand.outgoingoverview.Activitys;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leand.outgoingoverview.Classes.GeneralHelper;
import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.EditTextFilter.InputFilterDecimal;
import com.example.leand.outgoingoverview.ListviewHelper.ListViewAdapter;
import com.example.leand.outgoingoverview.R;
import com.example.leand.outgoingoverview.Classes.SelectedDate;

import static com.example.leand.outgoingoverview.Activitys.OverviewListActivity.EXTRA_INTEGER_ID;

public class AddNewItemActivity extends AppCompatActivity {
    private TextView textView_AddNewItemActivity_SelectedDate, textView_AddNewItemActivity_Currency, textView_AddNewItemActivity_TotalValue;
    private EditText editText_AddNewItemActivity_Value, editText_AddNewItemActivity_Description, editText_AddNewItemActivity_Titel;
    private ListView listView_AddNewItemActivity;
    private ListViewAdapter listViewAdapter;

    private GeneralHelper generalHelper;
    private SelectedDate selectedDate;
    private String string_Currency = "";

    private double double_Value;
    private String string_Description, string_Title;


    public static boolean MainActiv = false;

    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

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

        //Initialize General Helper
        generalHelper = new GeneralHelper();

        //check if Main is Active
        if (!MainActiv) {
            Intent intent = new Intent(AddNewItemActivity.this, MainActivity.class);
            intent.putExtra(MainActivity.EXTRA_INT_DIRECT_OPEN_ACTIVITY, 1);
            startActivity(intent);
            finish();
        } else {

            //create new listViewAdapter
            listViewAdapter = new ListViewAdapter(selectedDate.getInteger_Month(), selectedDate.getInteger_Year(), DBAdapter.KEY_DATE, this, DBAdapter.ASCENDING);
            listViewAdapter.setCursorDate(selectedDate.getInteger_DateWithoutTime());

            //get Currency
            string_Currency = generalHelper.getCurrency();

            //create the arrayList
            createArrayList();

            //show Items on Activity
            displayItemsOnActivity();
        }

    }

    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // onClick Methods

    //save Item on Databse by Button click
    public void onClick_SaveItem(View view) {

        //if nothing entered dont save value
        if (editText_AddNewItemActivity_Value.getText().toString().equals("") && editText_AddNewItemActivity_Description.getText().toString().equals("") && editText_AddNewItemActivity_Titel.getText().toString().equals("")) {
            finish();
        } else if (editText_AddNewItemActivity_Description.getText().toString().equals("") && editText_AddNewItemActivity_Titel.getText().toString().equals("")) {
            Toast.makeText(AddNewItemActivity.this, getString(R.string.toast_enterATitleAndValue),
                    Toast.LENGTH_LONG).show();
        }
        //else save values on Database
        else {
            if (!editText_AddNewItemActivity_Titel.getText().toString().equals("")) {
                string_Title = editText_AddNewItemActivity_Titel.getText().toString();
            }

            if (!editText_AddNewItemActivity_Description.getText().toString().equals("")) {
                string_Description = editText_AddNewItemActivity_Description.getText().toString();
            }

            if (!editText_AddNewItemActivity_Value.getText().toString().equals("")) {
                double_Value = Double.parseDouble(editText_AddNewItemActivity_Value.getText().toString());
            }

            addNewItemRepeated();

        }
        displayItemsOnActivity();
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
        if (MainActivity.myDbMain.checkRowItem(double_Value, string_Title, selectedDate.getInteger_DateWithoutTime())) {
            Toast.makeText(AddNewItemActivity.this, getString(R.string.toast_itemsAlreadyExists),
                    Toast.LENGTH_LONG).show();

        } else {
            MainActivity.myDbMain.insertRow(selectedDate.getLong_Date(), double_Value, string_Description, string_Title, string_Currency);
        }

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

                //Open Activity to edit item and send ID
                if (cursor.isNull(DBAdapter.COL_END_DATE)) {
                    //Open EditValueActivity and send ID
                    Intent intent = new Intent(AddNewItemActivity.this, EditValueActivity.class);
                    intent.putExtra(EXTRA_INTEGER_ID, integer_iD);
                    startActivityForResult(intent, RESULT_FIRST_USER);
                } else {
                    //Open EditRepeatedOutgoingsActivity and send ID
                    Intent intent = new Intent(AddNewItemActivity.this, EditRepeatedOutgoingsActivity.class);
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
        editText_AddNewItemActivity_Description.setText("");
        editText_AddNewItemActivity_Titel.setText("");
        editText_AddNewItemActivity_Value.setText("");

        textView_AddNewItemActivity_SelectedDate.setText(selectedDate.getString_WeekDayOfDate() + " " + selectedDate.getString_Date());
        textView_AddNewItemActivity_Currency.setText(string_Currency);

        listViewAdapter.setCursorDate(selectedDate.getInteger_DateWithoutTime());
        listView_AddNewItemActivity.setAdapter(listViewAdapter.getListViewAdapter());

        textView_AddNewItemActivity_TotalValue.setText(generalHelper.currencyFormat.format(generalHelper.sumAllValues(listViewAdapter)) + string_Currency);
    }

    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End

}
