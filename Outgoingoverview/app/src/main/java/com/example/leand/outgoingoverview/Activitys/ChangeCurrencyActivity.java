package com.example.leand.outgoingoverview.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.R;

public class ChangeCurrencyActivity extends AppCompatActivity {
    DBAdapter myDb;
    EditText editText_ChangeCurrency_Currency;

    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_currency);

        //open the Database
        openDB();

        editText_ChangeCurrency_Currency = findViewById(R.id.editText_Properties_Currency);
    }

    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // onClick Methods

    //save new value of Currency
    public void onClick_SaveItem(View view) {
        myDb.updateCurrency(editText_ChangeCurrency_Currency.getText().toString());
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }

    // onClick Methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Database methods

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

    // Database methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
