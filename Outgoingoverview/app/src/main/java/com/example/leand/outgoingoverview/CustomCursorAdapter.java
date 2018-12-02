package com.example.leand.outgoingoverview;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class CustomCursorAdapter extends SimpleCursorAdapter {

    //create Data Formatter
    SimpleDateFormat sdf_DateInNumbers = new SimpleDateFormat("dd/MM/yyyy");

    //constructor
    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        //find TextView to enter values, if defined here it isnt necessary to define in constructor
        TextView textView_adapter_view_list_id = view.findViewById(R.id.textView_adapter_view_list_ID);
        TextView textView_adapter_view_list_date = view.findViewById(R.id.textView_adapter_view_list_DATE);
        TextView textView_adapter_view_list_value = view.findViewById(R.id.textView_adapter_view_list_VALUE);
        TextView textView_adapter_view_list_day = view.findViewById(R.id.textView_adapter_view_list_DAY);
        TextView textView_adapter_view_list_month = view.findViewById(R.id.textView_adapter_view_list_MONTH);
        TextView textView_adapter_view_list_year = view.findViewById(R.id.textView_adapter_view_list_YEAR);

        //search for values in Database
        int int_id_index = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        long long_date_index = cursor.getLong(cursor.getColumnIndexOrThrow("dateInInt"));
        double double_value_index = cursor.getDouble(cursor.getColumnIndexOrThrow("value"));
        int int_day_index = cursor.getInt(cursor.getColumnIndexOrThrow("dayInInt"));
        int int_month_index = cursor.getInt(cursor.getColumnIndexOrThrow("monthInInt"));
        int int_year_index = cursor.getInt(cursor.getColumnIndexOrThrow("yearInInt"));

        //Convert Values in needed format
        String string_id_index = String.valueOf(int_id_index);
        String string_date_index = sdf_DateInNumbers.format(long_date_index);
        String string_value_index = String.valueOf(double_value_index);
        String string_day_index = String.valueOf(int_day_index);
        String string_month_index = String.valueOf(int_month_index);
        String string_year_index = String.valueOf(int_year_index);

        //set values in TextViews
        textView_adapter_view_list_id.setText(string_id_index);
        textView_adapter_view_list_date.setText(string_date_index);
        textView_adapter_view_list_value.setText(string_value_index);
        textView_adapter_view_list_day.setText(string_day_index);
        textView_adapter_view_list_month.setText(string_month_index);
        textView_adapter_view_list_year.setText(string_year_index);
    }
}
