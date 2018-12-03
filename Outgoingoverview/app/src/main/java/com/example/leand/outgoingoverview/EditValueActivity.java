package com.example.leand.outgoingoverview;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    EditText editText_edit_value_description;
    EditText editText_edit_value_Titel;

    TextView textView_edit_value_date;
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
        textView_edit_value_date = findViewById(R.id.textView_edit_value_date);
        editText_edit_value_value = findViewById(R.id.editText_edit_value_value);
        editText_edit_value_Titel = findViewById(R.id.editText_edit_value_Titel);
        editText_edit_value_description = findViewById(R.id.editText_edit_value_description);

        //get Databse-ID from OutgoingsOfSelectedMonthActivity
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
        newValue = Double.parseDouble(editText_edit_value_value.getText().toString());
        newDescription = editText_edit_value_description.getText().toString();
        newTitel = editText_edit_value_Titel.getText().toString();
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
        editText_edit_value_value.setText(string_toEditValue);
        editText_edit_value_value.setSelection(editText_edit_value_value.getText().length());
        textView_edit_value_date.setText(string_toEditDate);
        editText_edit_value_Titel.setText(string_toEditTitel);
        editText_edit_value_description.setText(string_toEditDescription);
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
