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

import com.example.leand.outgoingoverview.Classes.GeneralHelper;
import com.example.leand.outgoingoverview.Classes.SelectedDate;
import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.EditTextFilter.InputFilterDecimal;
import com.example.leand.outgoingoverview.R;

public class EditValueActivity extends AppCompatActivity {

    private EditText editText_EditValueActivity_Value, editText_EditValueActivity_Description, editText_EditValueActivity_Titel;
    private TextView textView_EditValueActivity_Date, textView_EditValueActivity_Currency;
    private SelectedDate selectedDate;

    private Integer interger_ID;
    private String string_oldValue, string_oldTitel, string_oldDescription, string_Currency;

    private GeneralHelper generalHelper;

    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_value);


        //definition of Items in Activity
        textView_EditValueActivity_Date = findViewById(R.id.textView_EditValueActivity_Date);
        editText_EditValueActivity_Value = findViewById(R.id.editText_EditValueActivity_Value);
        editText_EditValueActivity_Titel = findViewById(R.id.editText_EditValueActivity_Titel);
        editText_EditValueActivity_Description = findViewById(R.id.editText_EditValueActivity_Description);
        textView_EditValueActivity_Currency = findViewById(R.id.textView_EditValueActivity_Currency);

        //Initilaize selected Date
        selectedDate=new SelectedDate();

        //get Database-ID from OverviewListActivity
        Intent caller = getIntent();
        interger_ID = caller.getIntExtra(OverviewListActivity.EXTRA_INTEGER_ID, -1);

        //get Row to edit with ID
        Cursor cursor = MainActivity.myDbMain.getRowWithID(interger_ID);

        //Initialize General Helper
        generalHelper= new GeneralHelper();

        //get Currency
        string_Currency=generalHelper.getCurrency();

        //get old values to Edit
        selectedDate.setLong_Date(cursor.getLong(DBAdapter.COL_DATE));
        string_oldValue = generalHelper.currencyFormat.format(cursor.getDouble(DBAdapter.COL_VALUE));
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
        MainActivity.myDbMain.updateRow(interger_ID, newValue, newDescription, newTitel);

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
                        MainActivity.myDbMain.deleteRow(interger_ID);
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
        builder.setMessage(getString(R.string.dialog_question)).setPositiveButton(getString(R.string.dialog_yes), dialogClickListener)
                .setNegativeButton(getString(R.string.dialog_no), dialogClickListener).show();
    }

    //onClick Methods
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // Displaying Values

    //show Values on Activity
    public void displayItemsOnActivity() {
        editText_EditValueActivity_Value.setText(string_oldValue);
        editText_EditValueActivity_Value.setSelection(editText_EditValueActivity_Value.getText().length());
        textView_EditValueActivity_Date.setText(selectedDate.getString_WeekDayOfDate()+" "+selectedDate.getString_Date());
        editText_EditValueActivity_Titel.setText(string_oldTitel);
        editText_EditValueActivity_Description.setText(string_oldDescription);
        textView_EditValueActivity_Currency.setText(string_Currency);
    }

    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
