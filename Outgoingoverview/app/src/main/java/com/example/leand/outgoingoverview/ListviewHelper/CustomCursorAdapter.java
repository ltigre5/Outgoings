package com.example.leand.outgoingoverview.ListviewHelper;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.leand.outgoingoverview.DatabaseHelper.DBAdapter;
import com.example.leand.outgoingoverview.GeneralHelperClasses.EditHelper;
import com.example.leand.outgoingoverview.GeneralHelperClasses.GeneralHelper;
import com.example.leand.outgoingoverview.R;

public class CustomCursorAdapter extends SimpleCursorAdapter {
    private Cursor cursor;
    private String string_Currency;
    private Context context;
    private GeneralHelper generalHelper;
    private EditHelper editHelper;
    private String string_RepeatedStatement;

    // Declaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor

    /**
     * Creates a new Cursor Adapter with the Data's from the cursor,
     *
     * @param context Context
     * @param layout don't need to enter a layout, enter 0
     * @param c enter the cursor from which data's should a list be created
     * @param from don't need to enter something, enter a empty array of string
     * @param to don't need to enter something, enter a empty array of int
     * @param flags don't need to enter something, enter 0
     */

    //constructor
    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        generalHelper = new GeneralHelper();
        this.cursor = c;
        this.context = context;
        string_Currency = generalHelper.getCurrency();
        editHelper = new EditHelper(c);
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

    //chose which Layout to us
    @Override
    public int getItemViewType(int position) {
        cursor.moveToPosition(position);
        if (DBAdapter.SINGLE.equals(editHelper.getStringSingleRepeated())) {
            return 0;
        } else {
            return 1;
        }
    }

    //Gets the raw Layout and enters the data from Database
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        String string_EndDate_index;
        String string_StartDate_index;
        int type = getItemViewType(position);


        //search for values in Database
        long long_Date = editHelper.getLongDate();
        double double_Value = editHelper.getDoubleValue();
        String string_Title = editHelper.getStringTitle();
        int int_titleColor = editHelper.getIntTitleColor();

        long long_EndDate_index = editHelper.getLongEndDate();
        long long_StartDate_index = editHelper.getLongStartDate();

        //Convert Values in needed format
        String string_WeekDay = generalHelper.longDateToWeekday.format(long_Date);
        String string_Date = generalHelper.longDateToDDMMYYYY.format(long_Date);
        String string_Value = generalHelper.currencyFormat.format(double_Value) + string_Currency;

        string_EndDate_index = generalHelper.longDateToDDMMYYYY.format(long_EndDate_index);
        string_StartDate_index = generalHelper.longDateToDDMMYYYY.format(long_StartDate_index);

        string_RepeatedStatement = context.getResources().getString(R.string.statement_every) + " " + editHelper.getIntRepeatedByTimes() + " " + generalHelper.getRepeatedEveryWithNumber(editHelper.getIntEvery(), context);

        //new ViewHolder is old view is null
        if (convertView == null) {
            viewHolder = new ViewHolder();

            // Inflate the layout according to the view type
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (type == 0) {
                // Inflate the layout with image
                convertView = inflater.inflate(R.layout.adapter_view_list, parent, false);

            } else if (type == 1) {
                convertView = inflater.inflate(R.layout.adapter_view_list_repeated, parent, false);
                viewHolder.textView_AdapterViewList_RepeatedDate = convertView.findViewById(R.id.textView_AdapterViewList_RepeatedDate);
                viewHolder.textView_AdapterViewList_RepeatedStatement = convertView.findViewById(R.id.textView_AdapterViewList_RepeatedStatment);
            }

            viewHolder.textView_AdapterViewList_title = convertView.findViewById(R.id.textView_AdapterViewList_title);
            viewHolder.textView_AdapterViewList_date = convertView.findViewById(R.id.textView_AdapterViewList_DATE);
            viewHolder.textView_AdapterViewList_value = convertView.findViewById(R.id.textView_AdapterViewList_VALUE);
            viewHolder.textView_AdapterViewList_WeekDay = convertView.findViewById(R.id.textView_AdapterViewList_WeekDay);

            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();

        //set values in TextViews
        if (type == 1) {
            viewHolder.textView_AdapterViewList_RepeatedDate.setText(string_StartDate_index + " - " + string_EndDate_index);
            viewHolder.textView_AdapterViewList_RepeatedStatement.setText(string_RepeatedStatement);
        }

        viewHolder.textView_AdapterViewList_title.setText(string_Title);
        viewHolder.textView_AdapterViewList_date.setText(string_Date);
        viewHolder.textView_AdapterViewList_value.setText(string_Value);
        viewHolder.textView_AdapterViewList_WeekDay.setText(string_WeekDay);
        viewHolder.textView_AdapterViewList_title.setTextColor(int_titleColor);

        return convertView;
    }

    //Class ViewHolder
    static class ViewHolder {
        TextView textView_AdapterViewList_RepeatedDate;
        TextView textView_AdapterViewList_RepeatedStatement;

        TextView textView_AdapterViewList_title;
        TextView textView_AdapterViewList_date;
        TextView textView_AdapterViewList_value;
        TextView textView_AdapterViewList_WeekDay;
    }

    // Methods to get Data from Database and enter into raw Layout
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End

}
