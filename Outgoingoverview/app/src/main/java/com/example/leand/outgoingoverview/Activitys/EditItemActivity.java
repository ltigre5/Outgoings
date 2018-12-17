package com.example.leand.outgoingoverview.Activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.GeneralHelperClasses.EditHelper;
import com.example.leand.outgoingoverview.GeneralHelperClasses.GeneralHelper;
import com.example.leand.outgoingoverview.GeneralHelperClasses.SelectedDate;
import com.example.leand.outgoingoverview.EditTextFilter.InputFilterDecimal;
import com.example.leand.outgoingoverview.R;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EditItemActivity extends AppCompatActivity {

    private EditText editText_EditItemActivity_Value, editText_EditItemActivity_Description, editText_EditItemActivity_Title;
    private TextView textView_EditItemActivity_Date, textView_EditItemActivity_Currency;
    private Button button_EditItemActivity_ChooseColor;
    private SelectedDate selectedDate;

    private Integer integer_ID;
    private String string_oldValue, string_oldTitle, string_oldDescription, string_Currency;
    private int int_titleColor = 0xff000000; //for black


    private GeneralHelper generalHelper;
    private EditHelper editHelper;

    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Toolbar toolbar = findViewById(R.id.toolbar_MainActivity);
        setSupportActionBar(toolbar);


        //definition of Items in Activity
        textView_EditItemActivity_Date = findViewById(R.id.textView_EditItemActivity_Date);
        editText_EditItemActivity_Value = findViewById(R.id.editText_EditItemActivity_Value);
        editText_EditItemActivity_Title = findViewById(R.id.editText_EditItemActivity_Titel);
        editText_EditItemActivity_Description = findViewById(R.id.editText_EditItemActivity_Description);
        textView_EditItemActivity_Currency = findViewById(R.id.textView_EditItemActivity_Currency);
        button_EditItemActivity_ChooseColor = findViewById(R.id.button_EditItemActivity_ChooseColor);

        //Initilaize selected Date
        selectedDate = new SelectedDate();

        //get Database-ID from OverviewActivity
        Intent caller = getIntent();
        integer_ID = caller.getIntExtra(OverviewActivity.EXTRA_INTEGER_ID, -1);

        //Initialize General Helper
        generalHelper = new GeneralHelper();
        editHelper = new EditHelper(integer_ID);

        //get Currency
        string_Currency = generalHelper.getCurrency();

        //get old values to Edit
        selectedDate.setLong_Date(editHelper.getLongDate());
        string_oldValue = editHelper.getStringValue();
        string_oldTitle = editHelper.getStringTitle();
        string_oldDescription = editHelper.getStringDescription();
        int_titleColor = editHelper.getIntTitleColor();

        //set color of button color chooser and title
        button_EditItemActivity_ChooseColor.setBackgroundColor(int_titleColor);
        editText_EditItemActivity_Title.setTextColor(int_titleColor);

        //show Items on Activity
        displayItemsOnActivity();

        //set Filter for Value Input, only allow 2 digits after point and 14 before point
        editText_EditItemActivity_Value.setFilters(new InputFilter[]{new InputFilterDecimal(14, 2)});

        //set ColorPicker for Button to Edit title color
        button_EditItemActivity_ChooseColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });
    }

    // onCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // onClick Methods

    //update value and close Activity
    public void onClick_SaveNewValue(View view) {
        //get the new values
        double double_NewValue = Double.parseDouble(editText_EditItemActivity_Value.getText().toString());
        String string_NewDescription = editText_EditItemActivity_Description.getText().toString();
        String string_NewTitel = editText_EditItemActivity_Title.getText().toString();

        //update the Database
        MainActivity.myDbMain.updateRow(integer_ID, string_NewTitel, int_titleColor, double_NewValue, string_NewDescription);

        //return to OverviewActivity
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
                        MainActivity.myDbMain.deleteRow(integer_ID);
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

    //ColorPicker to Edit title color
    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, int_titleColor, true, new AmbilWarnaDialog.OnAmbilWarnaListener() {

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                button_EditItemActivity_ChooseColor.setBackgroundColor(color);
                int_titleColor = color;
                editText_EditItemActivity_Title.setTextColor(color);
            }
        });

        colorPicker.show();
    }

    //onClick Methods
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // Displaying Values

    //show Values on Activity
    public void displayItemsOnActivity() {
        editText_EditItemActivity_Title.setTextColor(int_titleColor);
        editText_EditItemActivity_Value.setText(string_oldValue);
        editText_EditItemActivity_Value.setSelection(editText_EditItemActivity_Value.getText().length());
        textView_EditItemActivity_Date.setText(selectedDate.getString_WeekDayOfDate() + " " + selectedDate.getString_Date());
        editText_EditItemActivity_Title.setText(string_oldTitle);
        editText_EditItemActivity_Description.setText(string_oldDescription);
        textView_EditItemActivity_Currency.setText(string_Currency);
    }

    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
