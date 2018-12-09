package com.example.leand.outgoingoverview.ListviewHelper;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class CustomCursorAdapter extends SimpleCursorAdapter {
    private String string_Currency;

    //create Data Formatter
    SimpleDateFormat sdf_DateInNumbers = new SimpleDateFormat("dd/MM/yyyy");
    DecimalFormat df = new DecimalFormat("0.00");

    //constructor
    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, String currency) {
        super(context, layout, c, from, to, flags);
        string_Currency = currency;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        TextView textView_adapter_view_list_titel = view.findViewById(R.id.textView_adapter_view_list_titel);
        TextView textView_adapter_view_list_date = view.findViewById(R.id.textView_adapter_view_list_DATE);
        TextView textView_adapter_view_list_value = view.findViewById(R.id.textView_adapter_view_list_VALUE);

        //search for values in Database
        long long_date_index = cursor.getLong(cursor.getColumnIndexOrThrow(DBAdapter.KEY_DATE));
        double double_value_index = cursor.getDouble(cursor.getColumnIndexOrThrow(DBAdapter.KEY_VALUE));
        String string_titel_index = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_TITEL));

        //Convert Values in needed format
        String string_date_index = sdf_DateInNumbers.format(long_date_index);
        String string_value_index = df.format(double_value_index) + string_Currency;

        //set values in TextViews
        textView_adapter_view_list_titel.setText(string_titel_index);
        textView_adapter_view_list_date.setText(string_date_index);
        textView_adapter_view_list_value.setText(string_value_index);

    }


}
