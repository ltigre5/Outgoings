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

import java.text.SimpleDateFormat;

public class OutgoingsOfSelectedMonthActivity extends AppCompatActivity {
    SimpleDateFormat sdf_DateInNumbers = new SimpleDateFormat("dd/MM/yyyy");
    DBAdapter myDb;
    ListView listView;
    Integer integer_selectedMonth;
    String string_SelectedMonth;

    // Deklaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoings_of_selected_month);

        Intent intent= getIntent();
        integer_selectedMonth=intent.getIntExtra("IntSelectedMonth",-1);
        setMonth();

        listView = findViewById(R.id.listView_outgoingsOfSelectedMonth);

        myDb = new DBAdapter(this);
        myDb.open();

        createArrayListOfAllValues();

    }

    //sets textView for Month to selected Month
    private void setMonth() {
        TextView textView_out_goings_of_selected_month_Month= findViewById(R.id.textView_out_goings_of_selected_month_Month);
        switch (integer_selectedMonth) {
            case 1:  string_SelectedMonth = "January";
                break;
            case 2:  string_SelectedMonth = "February";
                break;
            case 3:  string_SelectedMonth = "March";
                break;
            case 4:  string_SelectedMonth = "April";
                break;
            case 5:  string_SelectedMonth = "May";
                break;
            case 6:  string_SelectedMonth = "June";
                break;
            case 7:  string_SelectedMonth = "July";
                break;
            case 8:  string_SelectedMonth = "August";
                break;
            case 9:  string_SelectedMonth = "September";
                break;
            case 10: string_SelectedMonth = "October";
                break;
            case 11: string_SelectedMonth = "November";
                break;
            case 12: string_SelectedMonth = "December";
                break;
            default: string_SelectedMonth = "Invalid month";
                break;
        }
        textView_out_goings_of_selected_month_Month.setText(string_SelectedMonth);
    }

    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Database methods

    //Creates Arraylist of all values and adds an onClick Method which opens EditValueActivity and tranfers Databse-ID of selected Value
    private void createArrayListOfAllValues() {
        //Get cursor for all Rows
        Cursor cursor = myDb.getAllRows();

        //Select from which Rows in Databse to get values
        String[] fromFieldsName = new String[]{DBAdapter.KEY_ROWID, DBAdapter.KEY_DATE, DBAdapter.KEY_VALUE};

        //Select where to show values, textViews defined in Class CustomCursorAdapter dont need to enter here
        int[] toViewsID = new int[]{R.id.textView_adapter_view_list_ID};

        //create Adapter
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this, R.layout.adapter_view_list, cursor, fromFieldsName, toViewsID, 0);

        //set Adapter to listView
        listView.setAdapter(customCursorAdapter);

        //by clicking of Item get Database-ID of Position and open EditValueActivity and send ID
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get cursor of Position
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                //get ID of position
                Integer iD = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));

                //Open EditValueActivity and send ID
                Intent intent = new Intent(OutgoingsOfSelectedMonthActivity.this, EditValueActivity.class);
                intent.putExtra("values", iD.toString());
                startActivityForResult(intent, RESULT_FIRST_USER);
            }
        });


    }

    // Database methods
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Come back from EditValueActivity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                createArrayListOfAllValues();
            }
        }
    }


    // Come back from EditValueActivity
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Convert Values


    // Convert Values
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
