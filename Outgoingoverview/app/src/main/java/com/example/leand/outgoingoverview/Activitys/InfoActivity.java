package com.example.leand.outgoingoverview.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.leand.outgoingoverview.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Toolbar toolbar=findViewById(R.id.toolbar_MainActivity);
        setSupportActionBar(toolbar);
    }
}
