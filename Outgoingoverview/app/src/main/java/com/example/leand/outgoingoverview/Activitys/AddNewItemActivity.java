package com.example.leand.outgoingoverview.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.EditTextDezimalFilter.InputFilterDecimal;
import com.example.leand.outgoingoverview.R;
import com.example.leand.outgoingoverview.Classes.SelectedDate;

public class AddNewItemActivity extends AppCompatActivity {
    DBAdapter myDb;
    TextView textView_AddNewItemActivity_SelectedDate;
    Button button_AddNewItemActivity_SaveValue;
    EditText editText_AddNewItemActivity_Value;
    EditText editText_AddNewItemActivity_description;
    EditText editText_AddNewItemActivity_Titel;
    SelectedDate selectedDate;

    // Deklaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        openDB();

        //definition of Items in Activity
        textView_AddNewItemActivity_SelectedDate = findViewById(R.id.textView_AddNewItemActivity_SelectedDate);
        button_AddNewItemActivity_SaveValue = findViewById(R.id.button_AddNewItemActivity_SaveValue);
        editText_AddNewItemActivity_Value = findViewById(R.id.editText_AddNewItemActivity_Value);
        editText_AddNewItemActivity_description = findViewById(R.id.editText_AddNewItemActivity_description);
        editText_AddNewItemActivity_Titel = findViewById(R.id.editText_AddNewItemActivity_Titel);

        editText_AddNewItemActivity_Value.setFilters(new InputFilter[]{new InputFilterDecimal(1000000000,2)});

        //get Date from MainActivity
        Intent caller = getIntent();
        selectedDate = new SelectedDate(caller.getLongExtra(MainActivity.EXTRA_LONG_DATE, -1));

        //show Items on Activity
        displayItemsOnActivity();
    }

    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // onClick Methods

    //save Item by Button click
    public void onClick_SaveItem(View view) {
        String string_Titel;
        Double double_Value;
        String string_Description;

        if (editText_AddNewItemActivity_Value.getText().toString().equals("")) {
            finish();
        } else {
            string_Titel = editText_AddNewItemActivity_Titel.getText().toString();
            string_Description = editText_AddNewItemActivity_description.getText().toString();
            double_Value = Double.parseDouble(editText_AddNewItemActivity_Value.getText().toString());

            addNewItemInDatabase(selectedDate.getLong_Date(), double_Value, selectedDate.getInteger_day(), selectedDate.getInteger_Month(), selectedDate.getInteger_Year(), string_Description, string_Titel);
        }

        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }

    // Communication with other Activity
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Displaying Values

    public void displayItemsOnActivity() {
        //show Date in layout
        textView_AddNewItemActivity_SelectedDate.setText(selectedDate.getString_Date());
    }

    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Database methods

    //Adds Date and value to Database
    private void addNewItemInDatabase(long longDate, double value, int int_Day, int int_Month, int int_Year, String description, String titel) {
        myDb.insertRow(longDate, value, int_Day, int_Month, int_Year, description, titel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }

    private void closeDB() {
        myDb.close();
    }

    // Database methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End

}
