package com.example.leand.outgoingoverview.Activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leand.outgoingoverview.GeneralHelperClasses.GeneralHelper;
import com.example.leand.outgoingoverview.R;

public class CurrencyActivity extends AppCompatActivity {
    private EditText editText_CurrencyActivity_Currency;
    private TextView textView_CurrencyActivity_oldCurrency;
    private String string_Currency;

    private GeneralHelper generalHelper;

    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        //Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_MainActivity);
        setSupportActionBar(toolbar);

        generalHelper= new GeneralHelper();

        editText_CurrencyActivity_Currency = findViewById(R.id.editText_CurrencyActivity_Currency);
        textView_CurrencyActivity_oldCurrency=findViewById(R.id.textView_CurrencyActivity_oldCurrency);

        if (MainActivity.myDbMain.checkCurrencyExists()){
            textView_CurrencyActivity_oldCurrency.setText(generalHelper.getCurrency());
        }
    }

    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // onClick Methods

    //save new value of Currency
    public void onClick_SaveItem(View view) {
        if (editText_CurrencyActivity_Currency.length() < 3) {
            Toast.makeText(CurrencyActivity.this, "Enter A 3-Letter Currency",
                    Toast.LENGTH_LONG).show();

        } else {

            string_Currency = " " + editText_CurrencyActivity_Currency.getText().toString().toUpperCase();
            if (MainActivity.myDbMain.checkCurrencyExists()) {

                MainActivity.myDbMain.updateCurrency(string_Currency);
            } else {

                MainActivity.myDbMain.insertCurrency(string_Currency);
            }
            textView_CurrencyActivity_oldCurrency.setText(generalHelper.getCurrency());
        }
    }

    // onClick Methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Menu

    //Create the Menubar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Menus
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_Overview:
                intent = new Intent(this, OverviewActivity.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.menu_AddRepeatedIssues:
                intent = new Intent(this, AddRepeatedItemsActivity.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.menu_DeleteAll:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                //Yes button clicked, delete all Data in Database
                                MainActivity.myDbMain.deleteAll();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked, do nothing
                                break;
                        }
                    }
                };

                //set the message to show in the DialogWindow
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.dialog_question)).setPositiveButton(getString(R.string.dialog_yes), dialogClickListener)
                        .setNegativeButton(getString(R.string.dialog_no), dialogClickListener).show();
                return true;

            case R.id.menu_info:
                intent = new Intent(this, InfoActivity.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.menu_MainMenu:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Menu
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
