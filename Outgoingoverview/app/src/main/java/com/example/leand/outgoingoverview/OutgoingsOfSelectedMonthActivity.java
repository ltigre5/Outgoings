package com.example.leand.outgoingoverview;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OutgoingsOfSelectedMonthActivity extends AppCompatActivity {
    SimpleDateFormat sdf_DateInNumbers = new SimpleDateFormat("dd/MM/yyyy");
    DBAdapter myDb;
    ListView listView;

    // Deklaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoings_of_selected_month);

        listView= (ListView) findViewById(R.id.listView_outgoingsOfSelectedMonth);

        myDb= new DBAdapter(this);
        myDb.open();

        enterStringDateAndValueToArraylist();

    }

    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Database methods

    //Enters all Data from Database into a Array list
    private void enterStringDateAndValueToArraylist() {
        Cursor cursor = myDb.getAllRows();
        ArrayList<String> arrayListDatabaseAll= new ArrayList<String>();
        // populate the message from the cursor

        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                String message = "";

                // Process the data:
                long longDate = cursor.getLong(DBAdapter.COL_DATE);
                double value = cursor.getDouble(DBAdapter.COL_VALUE);
                String date = longToStringDate(longDate);

                // Append data to the message:
                message += "date=" + date
                        +", value=" + value;

                // add Data to array list
                arrayListDatabaseAll.add(message);

            } while(cursor.moveToNext());

            ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayListDatabaseAll);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name= parent.getItemAtPosition(position).toString();
                }
            });
        }

        // Close the cursor to avoid a resource leak.
        cursor.close();

    }

    // Database methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Displaying Values



    // Displaying Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Convert Values

    private String longToStringDate(long longDate){
        String date=sdf_DateInNumbers.format(longDate);
        return date;
    }

    // Convert Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
