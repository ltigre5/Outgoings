package com.example.leand.outgoingoverview.Activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.EditTextDezimalFilter.InputFilterDecimal;
import com.example.leand.outgoingoverview.R;

import java.text.SimpleDateFormat;

public class EditValueActivity extends AppCompatActivity {
    DBAdapter myDb;
    EditText editText_EditValueActivity_value;
    EditText editText_EditValueActivity_description;
    EditText editText_EditValueActivity_Titel;

    TextView textView_EditValueActivity_date;
    Integer iD;
    String string_toEditValue;
    String string_toEditDate;
    String string_toEditTitel;
    String string_toEditDescription;

    SimpleDateFormat sdf_DateInNumbers = new SimpleDateFormat("dd/MM/yyyy");
    double newValue;
    String newTitel;
    String newDescription;

    // Deklaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_value);

        openDB();

        //definition of Items in EditValue
        textView_EditValueActivity_date = findViewById(R.id.textView_EditValueActivity_date);
        editText_EditValueActivity_value = findViewById(R.id.editText_EditValueActivity_value);
        editText_EditValueActivity_Titel = findViewById(R.id.editText_EditValueActivity_Titel);
        editText_EditValueActivity_description = findViewById(R.id.editText_EditValueActivity_description);

        editText_EditValueActivity_value.setFilters(new InputFilter[]{new InputFilterDecimal(1000000000,2)});

        //get Databse-ID from OverviewListActivity
        Intent caller = getIntent();
        iD = caller.getIntExtra("id", -1);
        Cursor cursor = myDb.getRowWithID(iD);

        //get values to Edit
        string_toEditDate = longToStringDate(cursor.getLong(DBAdapter.COL_DATE));
        string_toEditValue = String.valueOf(cursor.getDouble(DBAdapter.COL_VALUE));
        string_toEditTitel = String.valueOf(cursor.getString(DBAdapter.COL_TITEL));
        string_toEditDescription = String.valueOf(cursor.getString(DBAdapter.COL_DESCRIPTION));

        //show Items on Activity
        displayItemsOnActivity();

    }

    // onCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Convert Values

    //Makes String Date out of long
    private String longToStringDate(long longDate) {
        String date = sdf_DateInNumbers.format(longDate);
        return date;
    }

    // Convert Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Communicate with other Activitys

    //update value and close Activity
    public void onClick_SaveNewValue(View view) {
        newValue = Double.parseDouble(editText_EditValueActivity_value.getText().toString());
        newDescription = editText_EditValueActivity_description.getText().toString();
        newTitel = editText_EditValueActivity_Titel.getText().toString();
        myDb.updateRow(iD, newValue, newDescription, newTitel);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    // Communicate with other Activitys
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Database Methods

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

    // Database Methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Displaying Values

    public void displayItemsOnActivity() {
        editText_EditValueActivity_value.setText(string_toEditValue);
        editText_EditValueActivity_value.setSelection(editText_EditValueActivity_value.getText().length());
        textView_EditValueActivity_date.setText(string_toEditDate);
        editText_EditValueActivity_Titel.setText(string_toEditTitel);
        editText_EditValueActivity_description.setText(string_toEditDescription);
    }

    public void onClick_DeletItem(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        myDb.deleteRow(iD);
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
