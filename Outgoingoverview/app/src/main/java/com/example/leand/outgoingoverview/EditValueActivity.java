package com.example.leand.outgoingoverview;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditValueActivity extends AppCompatActivity {
    DBAdapter myDb;
    EditText editText_edit_value_value;
    Integer iD;
    TextView textView_edit_value_date;
    SimpleDateFormat sdf_DateInNumbers = new SimpleDateFormat("dd/MM/yyyy");
    double newValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_value);

        myDb = new DBAdapter(this);
        myDb.open();

        Intent caller= getIntent();

        iD= Integer.parseInt(caller.getStringExtra("values"));

        Cursor cursor = myDb.getRowWithID(iD);


        textView_edit_value_date = findViewById(R.id.textView_edit_value_date);
        editText_edit_value_value = findViewById(R.id.editText_edit_value_value);

        editText_edit_value_value.setText(String.valueOf(cursor.getDouble(DBAdapter.COL_VALUE)));
        textView_edit_value_date.setText(longToStringDate(cursor.getLong(DBAdapter.COL_DATE)));
    }

    // onCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Convert Values

    //Makes String Date out of long
    private String longToStringDate(long longDate) {
        String date = sdf_DateInNumbers.format(longDate);
        return date;
    }

    //Make String Date out of date
    private String dateToStringDate(Date date) {
        String stringDate = sdf_DateInNumbers.format(date.getTime());
        return stringDate;
    }

    //update value and close Activity
    public void onClick_SaveNewValue(View view) {
        newValue=Double.parseDouble(editText_edit_value_value.getText().toString());
        myDb.updateValue(newValue,iD);

        Intent intent=new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    // Convert Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    //
}
