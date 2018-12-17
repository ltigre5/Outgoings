package com.example.leand.outgoingoverview.Activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.GeneralHelperClasses.GeneralHelper;
import com.example.leand.outgoingoverview.EditTextFilter.InputFilterDecimal;
import com.example.leand.outgoingoverview.ListviewHelper.ListViewAdapter;
import com.example.leand.outgoingoverview.R;
import com.example.leand.outgoingoverview.GeneralHelperClasses.SelectedDate;
import com.example.leand.outgoingoverview.Widget.AddNewOutgoingWidget;

import yuku.ambilwarna.AmbilWarnaDialog;

import static com.example.leand.outgoingoverview.Activitys.OverviewActivity.EXTRA_INTEGER_ID;

public class AddNewItemActivity extends AppCompatActivity {
    private TextView textView_AddNewItemActivity_SelectedDate, textView_AddNewItemActivity_Currency, textView_AddNewItemActivity_TotalValue;
    private EditText editText_AddNewItemActivity_Value, editText_AddNewItemActivity_Description, editText_AddNewItemActivity_Title;
    private ListView listView_AddNewItemActivity;
    private ListViewAdapter listViewAdapter;
    private Button button_AddNewItemActivity_ChooseColor;

    private GeneralHelper generalHelper;
    private SelectedDate selectedDate;

    private String string_Currency;
    private String string_Description, string_Title;
    private double double_Value;
    private int int_titleColor = 0xff000000; //for black

    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Log.e("AddNewItem", "Start");

        //Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_MainActivity);
        setSupportActionBar(toolbar);

        //definition of Items in Activity
        textView_AddNewItemActivity_SelectedDate = findViewById(R.id.textView_AddNewItemActivity_SelectedDate);
        editText_AddNewItemActivity_Value = findViewById(R.id.editText_AddNewItemActivity_Value);
        editText_AddNewItemActivity_Description = findViewById(R.id.editText_AddNewItemActivity_description);
        editText_AddNewItemActivity_Title = findViewById(R.id.editText_AddNewItemActivity_Title);
        textView_AddNewItemActivity_Currency = findViewById(R.id.textView_AddNewItemActivity_Currency);
        listView_AddNewItemActivity = findViewById(R.id.listView_AddNewItemActivity);
        textView_AddNewItemActivity_TotalValue = findViewById(R.id.textView_AddNewItemActivity_TotalValue);
        button_AddNewItemActivity_ChooseColor = findViewById(R.id.button_AddNewItemActivity_ChooseColor);

        //set Filter for Value Input, only allow 2 digits after point and 14 before point
        editText_AddNewItemActivity_Value.setFilters(new InputFilter[]{new InputFilterDecimal(14, 2)});

        //get Date from MainActivity
        Intent caller = getIntent();
        selectedDate = new SelectedDate(caller.getLongExtra(MainActivity.EXTRA_LONG_DATE, -1));

        //Initialize General Helper
        generalHelper = new GeneralHelper();

        //check if Main is Active
        if (MainActivity.myDbMain==null) {
            Log.e("Widget", "StartNew");

            Intent intent = new Intent(AddNewItemActivity.this, MainActivity.class);
            intent.putExtra(AddNewOutgoingWidget.EXTRA_INT_DIRECT_OPEN_ACTIVITY, 1);
            startActivity(intent);
            finish();

        } else {

            //create new listViewAdapter
            listViewAdapter = new ListViewAdapter(selectedDate.getInteger_Month(), selectedDate.getInteger_Year(), DBAdapter.KEY_DATE, this, DBAdapter.ASCENDING);
            listViewAdapter.setCursorDate(selectedDate.getInteger_DateWithoutTime());

            //get Currency
            string_Currency = generalHelper.getCurrency();

            //create the arrayList
            createArrayList();

            //show Items on Activity
            displayItemsOnActivity();
        }

        //set ColorPicker for Button to Edit title color
        button_AddNewItemActivity_ChooseColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

    }

    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // onClick Methods




    //save Item on Database by Button click
    public void onClick_SaveItem(View view) {

        //if nothing entered dont save value
        if (editText_AddNewItemActivity_Title.getText().toString().equals("") || editText_AddNewItemActivity_Value.getText().toString().equals("")) {
            Toast.makeText(AddNewItemActivity.this, getString(R.string.toast_enterATitleAndValue),
                    Toast.LENGTH_LONG).show();
        }

        //else save values on Database
        else {
            if (!editText_AddNewItemActivity_Title.getText().toString().equals("")) {
                string_Title = editText_AddNewItemActivity_Title.getText().toString();
            }

            if (!editText_AddNewItemActivity_Description.getText().toString().equals("")) {
                string_Description = editText_AddNewItemActivity_Description.getText().toString();
            }

            if (!editText_AddNewItemActivity_Value.getText().toString().equals("")) {
                double_Value = Double.parseDouble(editText_AddNewItemActivity_Value.getText().toString());
            }

            addNewItemRepeated();
            makeDefaultActivity();
        }


        displayItemsOnActivity();

    }

    //ColorPicker to Edit title color
    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, int_titleColor, true, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                button_AddNewItemActivity_ChooseColor.setBackgroundColor(color);
                int_titleColor = color;
                editText_AddNewItemActivity_Title.setTextColor(color);
            }
        });
        colorPicker.show();

    }


    @Override
    public void onBackPressed() {
        Log.e("AddNewItem", "backpressed");

        //return to MainActivity
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }

    // onClick Methods
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // Database Methods


    //Adds Date and value to Database
    private void addNewItemRepeated() {
        if (MainActivity.myDbMain.checkRowItem(string_Title,double_Value,selectedDate.getInteger_DateWithoutTime())) {
            Toast.makeText(AddNewItemActivity.this, getString(R.string.toast_itemsAlreadyExists),
                    Toast.LENGTH_LONG).show();

        } else {
            MainActivity.myDbMain.insertRow(string_Title,int_titleColor,double_Value,string_Description,selectedDate.getLong_Date());
        }

    }

    // Database Methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Communicate with other Activity's

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                displayItemsOnActivity();
            }
        }
    }

    // Communicate with other Activitys
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // List Methods

    //Creates Arraylist of all values for ListView and adds an onClick Method which opens EditItemActivity and tranfers Databse-ID of selected Value
    private void createArrayList() {
        listView_AddNewItemActivity.setAdapter(listViewAdapter.getListViewAdapter());

        //by clicking of Item get Database-ID of Position and open EditItemActivity and send ID
        listView_AddNewItemActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //get ID of selected Item from Database
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                Integer integer_iD = cursor.getInt(cursor.getColumnIndexOrThrow(DBAdapter.KEY_ROWID));

                //Open Activity to edit item and send ID
                if (cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_SINGLE_OR_REPEATED)).equals(DBAdapter.SINGLE)) {
                    //Open EditItemActivity and send ID
                    Intent intent = new Intent(AddNewItemActivity.this, EditItemActivity.class);
                    intent.putExtra(EXTRA_INTEGER_ID, integer_iD);
                    startActivityForResult(intent, RESULT_FIRST_USER);
                } else {
                    //Open EditRepeatedItemsActivity and send ID
                    Intent intent = new Intent(AddNewItemActivity.this, EditRepeatedItemsActivity.class);
                    intent.putExtra(EXTRA_INTEGER_ID, integer_iD);
                    startActivityForResult(intent, RESULT_FIRST_USER);
                }
            }
        });
    }

    // List Methods
    // ----------------------------------------------------------------------------------------------------------------------------------------------
    // Displaying Values

    //show Values on Activity
    public void displayItemsOnActivity() {
        editText_AddNewItemActivity_Title.requestFocus();

        textView_AddNewItemActivity_SelectedDate.setText(selectedDate.getString_WeekDayOfDate() + " " + selectedDate.getString_Date());
        textView_AddNewItemActivity_Currency.setText(string_Currency);

        listViewAdapter.setCursorDate(selectedDate.getInteger_DateWithoutTime());
        listView_AddNewItemActivity.setAdapter(listViewAdapter.getListViewAdapter());

        textView_AddNewItemActivity_TotalValue.setText(generalHelper.currencyFormat.format(generalHelper.sumAllValues(listViewAdapter)) + string_Currency);
    }

    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // default values

    private void makeDefaultActivity() {
        editText_AddNewItemActivity_Description.setText("");
        editText_AddNewItemActivity_Title.setText("");
        editText_AddNewItemActivity_Value.setText("");

        int_titleColor = 0xff000000;
        editText_AddNewItemActivity_Title.setTextColor(int_titleColor);
        button_AddNewItemActivity_ChooseColor.setBackgroundColor(int_titleColor);
    }

    // default values
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

            case R.id.menu_AddCurrency:
                intent = new Intent(this, CurrencyActivity.class);
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
                                displayItemsOnActivity();
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
