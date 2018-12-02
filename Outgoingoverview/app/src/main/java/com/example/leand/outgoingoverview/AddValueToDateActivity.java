package com.example.leand.outgoingoverview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddValueToDateActivity extends AppCompatActivity {
    DBAdapter myDb;
    TextView textView_AddValueToDateActivity_SelectedDate;
    Button button_AddValueToDateActivity_SaveValue;
    EditText editText_AddValueToDateActivity_Value;
    SelectedDate selectedDate;

    // Deklaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_value_to_date);

        openDB();

        //definition of Items in MainActivity
        textView_AddValueToDateActivity_SelectedDate = findViewById(R.id.textView_putOutgoing_SelectedDate);
        button_AddValueToDateActivity_SaveValue = findViewById(R.id.button_putOutgoing_SaveValue);
        editText_AddValueToDateActivity_Value = findViewById(R.id.editText_putOutgoing_Value);

        //get Date from MainActivity
        Intent caller = getIntent();
        selectedDate = new SelectedDate(caller.getLongExtra(MainActivity.EXTRA_LONG_DATE, -1));

        //show Items on Activity
        displayItemsOnActivity();
    }

    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Communication with other Activity

    //save Value and Date by Button click
    public void onClick_SaveValue(View view) {
        Double double_Value;
        if (editText_AddValueToDateActivity_Value.getText().toString().equals("")) {
            finish();
        } else {
            double_Value = Double.parseDouble(editText_AddValueToDateActivity_Value.getText().toString());
            addDateAndValue(selectedDate.getLong_Date(), double_Value, selectedDate.getInteger_day(), selectedDate.getInteger_Month(), selectedDate.getInteger_Year());
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
        textView_AddValueToDateActivity_SelectedDate.setText(selectedDate.getString_Date());
    }

    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Database methods

    //Adds Date and value to Database
    private void addDateAndValue(long longDate, double value, int int_Day, int int_Month, int int_Year) {
        myDb.insertRow(longDate, value, int_Day, int_Month, int_Year);
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
