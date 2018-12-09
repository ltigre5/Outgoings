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
import com.example.leand.outgoingoverview.EditTextFilter.InputFilterDecimal;
import com.example.leand.outgoingoverview.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class EditValueActivity extends AppCompatActivity {
    private DBAdapter myDb;

    private EditText editText_EditValueActivity_Value;
    private EditText editText_EditValueActivity_Description;
    private EditText editText_EditValueActivity_Titel;
    private TextView textView_EditValueActivity_Date;
    private TextView textView_EditValueActivity_Currency;

    private Integer interger_ID;
    private String string_Date;
    private String string_oldValue;
    private String string_oldTitel;
    private String string_oldDescription;
    private String string_Currency;

    private SimpleDateFormat sdf_DateInNumbers = new SimpleDateFormat("dd/MM/yyyy");
    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_value);

        //open the Database
        openDB();

        //definition of Items in Activity
        textView_EditValueActivity_Date = findViewById(R.id.textView_EditValueActivity_Date);
        editText_EditValueActivity_Value = findViewById(R.id.editText_EditValueActivity_Value);
        editText_EditValueActivity_Titel = findViewById(R.id.editText_EditValueActivity_Titel);
        editText_EditValueActivity_Description = findViewById(R.id.editText_EditValueActivity_Description);
        textView_EditValueActivity_Currency = findViewById(R.id.textView_EditValueActivity_Currency);


        //get Database-ID from OverviewListActivity
        Intent caller = getIntent();
        interger_ID = caller.getIntExtra(OverviewListActivity.EXTRA_INTEGER_ID, -1);

        //get Row to edit with ID
        Cursor cursor = myDb.getRowWithID(interger_ID);

        //get Currency
        getCurrency();

        //get old values to Edit
        string_Date = longToStringDate(cursor.getLong(DBAdapter.COL_DATE));
        string_oldValue = decimalFormat.format(cursor.getDouble(DBAdapter.COL_VALUE));
        string_oldTitel = cursor.getString(DBAdapter.COL_TITEL);
        string_oldDescription = cursor.getString(DBAdapter.COL_DESCRIPTION);

        //show Items on Activity
        displayItemsOnActivity();

        //set Filter for Value Input, only allow 2 digits after point and 14 befor point
        editText_EditValueActivity_Value.setFilters(new InputFilter[]{new InputFilterDecimal(14, 2)});
    }

    // onCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // onClick Methods

    //update value and close Activity
    public void onClick_SaveNewValue(View view) {
        //get the new values
        double newValue = Double.parseDouble(editText_EditValueActivity_Value.getText().toString());
        String newDescription = editText_EditValueActivity_Description.getText().toString();
        String newTitel = editText_EditValueActivity_Titel.getText().toString();

        //update the Database
        myDb.updateRow(interger_ID, newValue, newDescription, newTitel);

        //return to OverviewListActivity
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    //Delet this Item
    public void onClick_DeletItem(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        //Yes button clicked, delet row in Database
                        myDb.deleteRow(interger_ID);
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        //No button clicked, make nothing
                        break;
                }
            }
        };

        //set the message to show in the DialogWindow
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    //onClick Methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Database Methods

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

    // Database Methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Convert Values

    //Makes String Date out of long
    private String longToStringDate(long longDate) {
        String date = sdf_DateInNumbers.format(longDate);
        return date;
    }

    // Convert Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Displaying Values

    //show Values on Activity
    public void displayItemsOnActivity() {
        editText_EditValueActivity_Value.setText(string_oldValue);
        editText_EditValueActivity_Value.setSelection(editText_EditValueActivity_Value.getText().length());
        textView_EditValueActivity_Date.setText(string_Date);
        editText_EditValueActivity_Titel.setText(string_oldTitel);
        editText_EditValueActivity_Description.setText(string_oldDescription);
        textView_EditValueActivity_Currency.setText(string_Currency);
    }

    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
