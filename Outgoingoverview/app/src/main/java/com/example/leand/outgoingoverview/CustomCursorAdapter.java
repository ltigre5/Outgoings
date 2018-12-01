package com.example.leand.outgoingoverview;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
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
        TextView textView_adapter_view_list_date = view.findViewById(R.id.textView_adapter_view_list_DATE);
        TextView textView_adapter_view_list_value = view.findViewById(R.id.textView_adapter_view_list_VALUE);

        //search for values in Database
        long long_date_index = cursor.getLong(cursor.getColumnIndexOrThrow("dateInInt"));
        double double_value_index = cursor.getDouble(cursor.getColumnIndexOrThrow("value"));

        //Convert Values in needed format
        String string_date_index = sdf_DateInNumbers.format(long_date_index);
        String string_value_index = String.valueOf(double_value_index);

        //set values in TextViews
        textView_adapter_view_list_date.setText(string_date_index);
        textView_adapter_view_list_value.setText(string_value_index);
    }
}
