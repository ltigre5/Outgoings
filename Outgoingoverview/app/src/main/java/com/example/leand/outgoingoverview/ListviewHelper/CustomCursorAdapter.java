package com.example.leand.outgoingoverview.ListviewHelper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.leand.outgoingoverview.Activitys.EditRepeatedOutgoingsActivity;
import com.example.leand.outgoingoverview.Activitys.EditValueActivity;
import com.example.leand.outgoingoverview.Activitys.OverviewListActivity;
import com.example.leand.outgoingoverview.Classes.GeneralHelper;
import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.R;

import java.text.DecimalFormat;

import static android.app.Activity.RESULT_FIRST_USER;
import static com.example.leand.outgoingoverview.Activitys.OverviewListActivity.EXTRA_INTEGER_ID;

public class CustomCursorAdapter extends SimpleCursorAdapter {
    private Cursor cursor;
    private String string_Currency;
    private Context context;
    private GeneralHelper generalHelper;

    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor

    //constructor
    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        generalHelper = new GeneralHelper();
        this.cursor = c;
        this.context = context;
        string_Currency = generalHelper.getCurrency();
    }

    // Constructor
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Methods to get Data from Database and enter into raw Layout

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
    }

    //the number of different Layouts to chose
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    //choses which Layout to us
    @Override
    public int getItemViewType(int position) {
        cursor.moveToPosition(position);
        if (cursor.isNull(DBAdapter.COL_END_DATE)) {
            return 0;
        } else {
            return 1;
        }
    }

    //Gets the raw Layout and enters the data from Database
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        int type = getItemViewType(position);
        if (v == null) {
            // Inflate the layout according to the view type
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //.inflate(R.layout.adapter_view_list_repeated, parent,false);
            if (type == 0) {
                // Inflate the layout with image
                v = inflater.inflate(R.layout.adapter_view_list, parent, false);
            } else {
                v = inflater.inflate(R.layout.adapter_view_list_repeated, parent, false);
                TextView textView_AdapterViewList_RepeatedDate = v.findViewById(R.id.textView_AdapterViewList_RepeatedDate);
                TextView textView_AdapterViewList_RepeatedStatment = v.findViewById(R.id.textView_AdapterViewList_RepeatedStatment);

                String string_Every_index = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_EVERY));
                long long_EndDate_index = cursor.getLong(cursor.getColumnIndexOrThrow(DBAdapter.KEY_END_DATE));
                long long_StartDate_index = cursor.getLong(cursor.getColumnIndexOrThrow(DBAdapter.KEY_START_DATE));

                String string_EndDate_index = generalHelper.longDateToDDMMYYYY.format(long_EndDate_index);
                String string_StartDate_index = generalHelper.longDateToDDMMYYYY.format(long_StartDate_index);

                textView_AdapterViewList_RepeatedDate.setText(string_StartDate_index + " - " + string_EndDate_index);
                textView_AdapterViewList_RepeatedStatment.setText(string_Every_index);
            }
        }

        TextView textView_AdapterViewList_title = v.findViewById(R.id.textView_AdapterViewList_title);
        TextView textView_AdapterViewList_date = v.findViewById(R.id.textView_AdapterViewList_DATE);
        TextView textView_AdapterViewList_value = v.findViewById(R.id.textView_AdapterViewList_VALUE);
        TextView textView_AdapterViewList_WeekDay = v.findViewById(R.id.textView_AdapterViewList_WeekDay);

        //search for values in Database
        long long_Date = cursor.getLong(cursor.getColumnIndexOrThrow(DBAdapter.KEY_DATE));
        double double_Value = cursor.getDouble(cursor.getColumnIndexOrThrow(DBAdapter.KEY_VALUE));
        String string_Title = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_TITEL));
        int int_titleColor= cursor.getInt(cursor.getColumnIndexOrThrow(DBAdapter.KEY_TITLE_COLOR));

        //Convert Values in needed format
        String string_WeekDay = generalHelper.longDateToWeekday.format(long_Date);
        String string_Date = generalHelper.longDateToDDMMYYYY.format(long_Date);
        String string_Value = generalHelper.currencyFormat.format(double_Value) + string_Currency;

        //set values in TextViews
        textView_AdapterViewList_title.setText(string_Title);
        textView_AdapterViewList_date.setText(string_Date);
        textView_AdapterViewList_value.setText(string_Value);
        textView_AdapterViewList_WeekDay.setText(string_WeekDay);

        textView_AdapterViewList_title.setTextColor(int_titleColor);

        return v;
    }

    // Methods to get Data from Database and enter into raw Layout
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End

}
