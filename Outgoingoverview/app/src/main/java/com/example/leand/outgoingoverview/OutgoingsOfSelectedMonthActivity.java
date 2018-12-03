package com.example.leand.outgoingoverview;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class OutgoingsOfSelectedMonthActivity extends AppCompatActivity {
    TextView textView_out_goings_of_selected_month_Month;
    TextView textView_out_goings_of_selected_month_totalValue;
    DBAdapter myDb;
    ListView listView_outgoingsOfSelectedMonth;
    SelectedDate selectedDate;
    String string_Currency = "CHF";

    DecimalFormat df = new DecimalFormat("0.00");

    // Deklaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoings_of_selected_month);

        //open the Database
        openDB();

        //definition of Items in MainActivity
        listView_outgoingsOfSelectedMonth = findViewById(R.id.listView_outgoingsOfSelectedMonth);
        textView_out_goings_of_selected_month_Month = findViewById(R.id.textView_out_goings_of_selected_month_Month);
        textView_out_goings_of_selected_month_totalValue = findViewById(R.id.textView_out_goings_of_selected_month_TotalValue);

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
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this, R.layout.adapter_view_list, cursor, fromFieldsName, toViewsID, 0);
        listView_outgoingsOfSelectedMonth.setAdapter(customCursorAdapter);

        //by clicking of Item get Database-ID of Position and open EditValueActivity and send ID
        listView_outgoingsOfSelectedMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get ID of selected Item from Databse
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                Integer iD = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));

                //Open EditValueActivity and send ID
                Intent intent = new Intent(OutgoingsOfSelectedMonthActivity.this, EditValueActivity.class);
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
    // Set selected Values


    // Set selected Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Displaying Values

    public void displayItemsOnActivity() {
        textView_out_goings_of_selected_month_Month.setText(selectedDate.getString_Month());
        textView_out_goings_of_selected_month_totalValue.setText("Total of the Month: " + df.format(sumAllValuesOfSelectedMonth()) + string_Currency);
        createArrayListOfAllValues();
    }

    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
