package com.example.leand.outgoingoverview.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.R;

public class PropertiesActivity extends AppCompatActivity {
    DBAdapter myDb;
    EditText editText_Properties_Currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties);

        //open the Database
        openDB();

        editText_Properties_Currency = findViewById(R.id.editText_Properties_Currency);
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

    public void onClick_SaveItem(View view) {
        myDb.updateCurrency(editText_Properties_Currency.getText().toString());
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }
}
