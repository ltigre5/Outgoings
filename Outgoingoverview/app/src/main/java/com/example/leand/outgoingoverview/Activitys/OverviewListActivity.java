package com.example.leand.outgoingoverview.Activitys;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.leand.outgoingoverview.ListviewHelper.CustomCursorAdapter;
import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.R;
import com.example.leand.outgoingoverview.Classes.SelectedDate;

import java.text.DecimalFormat;

public class OverviewListActivity extends AppCompatActivity {
    TextView textView_OverviewListActivity_Month;
    TextView textView_OverviewListActivity_totalValue;
    DBAdapter myDb;
    ListView listView_OverviewListActivity;
    SelectedDate selectedDate;
    String string_Currency = "CHF";

    DecimalFormat df = new DecimalFormat("0.00");

    // Deklaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_list_);

        //open the Database
        openDB();

        getCurrency();

        //definition of Items in MainActivity
        listView_OverviewListActivity = findViewById(R.id.listView_OverviewListActivity);
        textView_OverviewListActivity_Month = findViewById(R.id.textView_OverviewListActivity_Month);
        textView_OverviewListActivity_totalValue = findViewById(R.id.textView_OverviewListActivity_TotalValue);

        //get Intent from MainActivity
        Intent intent = getIntent();
        selectedDate = new SelectedDate(intent.getLongExtra("LongSelectedDate", -1));

        //shows all Items On Activity
        displayItemsOnActivity();
    }


    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Database methods

    //Creates Arraylist of all values and adds an onClick Method which opens EditValueActivity and tranfers Databse-ID of selected Value
    private void createArrayListOfAllValues() {
        Cursor cursor = myDb.getRowWithMonthYear(selectedDate.getInteger_Month(), selectedDate.getInteger_Year());
        String[] fromFieldsName = new String[]{DBAdapter.KEY_ROWID, DBAdapter.KEY_DATE, DBAdapter.KEY_VALUE};
        int[] toViewsID = new int[]{};
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this, R.layout.adapter_view_list, cursor, fromFieldsName, toViewsID, 0, string_Currency);
        listView_OverviewListActivity.setAdapter(customCursorAdapter);

        //by clicking of Item get Database-ID of Position and open EditValueActivity and send ID
        listView_OverviewListActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get ID of selected Item from Databse
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                Integer iD = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));

                //Open EditValueActivity and send ID
                Intent intent = new Intent(OverviewListActivity.this, EditValueActivity.class);
                intent.putExtra("id", iD);
                startActivityForResult(intent, RESULT_FIRST_USER);
            }
        });
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

    private void getCurrency() {
        Cursor cursor = myDb.getFirstRow();
        if (!cursor.moveToFirst()) {
            string_Currency = "CHF";
        } else {
            string_Currency = cursor.getString(cursor.getColumnIndexOrThrow("currency"));
        }
        cursor.close();
    }

    //sums all values of the selected Month
    private Double sumAllValuesOfSelectedMonth() {
        Cursor cursor = myDb.getRowWithMonthYear(selectedDate.getInteger_Month(), selectedDate.getInteger_Year());

        Double doubleTotalValue = 0.0;

        if (cursor.moveToFirst()) {
            do {
                doubleTotalValue += cursor.getDouble(DBAdapter.COL_VALUE);
            } while (cursor.moveToNext());
        } else doubleTotalValue = 0.0;

        cursor.close();
        return doubleTotalValue;
    }

    // Database methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Come back from EditValueActivity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                displayItemsOnActivity();
            }
        }
    }

    // Come back from EditValueActivity
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Displaying Values

    public void displayItemsOnActivity() {
        textView_OverviewListActivity_Month.setText(selectedDate.getString_Month());
        textView_OverviewListActivity_totalValue.setText(df.format(sumAllValuesOfSelectedMonth()) + string_Currency);
        createArrayListOfAllValues();
    }

    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
