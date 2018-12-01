package com.example.leand.outgoingoverview;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddValueToDateActivity extends AppCompatActivity {
    Date date_selectedDate = new Date();
    TextView textView_AddValueToDateActivity_SelectedDate;
    String date;
    private String string_selectedDate;
    Button button_AddValueToDateActivity_SaveValue;
    EditText editText_AddValueToDateActivity_Value;
    Double double_returnValue;

    // Deklaration
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_value_to_date);

        SimpleDateFormat sdf_DateInNumbers = new SimpleDateFormat("dd/MM/yyyy");

        //get Date from MainActivity
        Intent caller = getIntent();
        date_selectedDate.setTime(caller.getLongExtra(MainActivity.EXTRA_DATE, -1));
        string_selectedDate = sdf_DateInNumbers.format(date_selectedDate);

        //show Date in layout
        textView_AddValueToDateActivity_SelectedDate = (TextView) findViewById(R.id.textView_putOutgoing_SelectedDate);
        textView_AddValueToDateActivity_SelectedDate.setText(string_selectedDate);

        //Get Value
        editText_AddValueToDateActivity_Value = (EditText) findViewById(R.id.editText_putOutgoing_Value);

        //save Value and Date by Button click
        button_AddValueToDateActivity_SaveValue = (Button) findViewById(R.id.button_putOutgoing_SaveValue);
        button_AddValueToDateActivity_SaveValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_AddValueToDateActivity_Value.getText().toString() != null) {
                    Intent putOutgoingValueIntent = new Intent();
                    if (editText_AddValueToDateActivity_Value.getText().toString().equals("")) {
                        double_returnValue = 0.0;
                    } else {
                        double_returnValue = Double.parseDouble(editText_AddValueToDateActivity_Value.getText().toString());
                    }
                    putOutgoingValueIntent.putExtra("OutgoingValue", double_returnValue);
                    putOutgoingValueIntent.putExtra("OutgoingDate", date_selectedDate.getTime());

                    setResult(RESULT_OK, putOutgoingValueIntent);
                    finish();
                } else {
                    finish();
                }
            }
        });

    }

    // OnCreate
    //----------------------------------------------------------------------------------------------------------------------------------------------
    // End
}
