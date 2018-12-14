package com.example.leand.outgoingoverview.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leand.outgoingoverview.R;

public class ChangeCurrencyActivity extends AppCompatActivity {
    private EditText editText_ChangeCurrency_Currency;
    private String string_Currency;

    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_currency);

        //Set Toolbar
        Toolbar toolbar=findViewById(R.id.toolbar_MainActivity);
        setSupportActionBar(toolbar);

        editText_ChangeCurrency_Currency = findViewById(R.id.editText_Properties_Currency);
    }

    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // onClick Methods

    //save new value of Currency
    public void onClick_SaveItem(View view) {
        if (editText_ChangeCurrency_Currency.length() < 3) {
            Toast.makeText(ChangeCurrencyActivity.this, "Enter A 3-Letter Currency",
                    Toast.LENGTH_LONG).show();
        } else {
            string_Currency=" "+editText_ChangeCurrency_Currency.getText().toString().toUpperCase();
            MainActivity.myDbMain.updateCurrency(string_Currency);
            Intent intent = new Intent();
            setResult(1, intent);
            finish();
        }
    }

    // onClick Methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Database methods


    // Database methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
