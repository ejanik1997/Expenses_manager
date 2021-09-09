package com.example.expenses_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class change_date extends AppCompatActivity {

    SimpleDateFormat date_format;
    //DatePicker date_picker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //LayoutInflater
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_date);
        //Calendar c = Calendar.getInstance();
        //int year = c.get(Calendar.YEAR);
        //int month = c.get(Calendar.MONTH);
        //int day = c.get(Calendar.DAY_OF_MONTH);
        //date_picker.init(year, month, day, null);
        date_format = new SimpleDateFormat("yyyy/MM/dd");

    }
    public void select_data(View view)
    {
        DatePicker date_picker = (DatePicker) view.findViewById(R.id.date_picker);
        String date = date_format.format(new Date(date_picker.getYear(), date_picker.getMonth(), date_picker.getDayOfMonth()));
        Toast.makeText(this, date, Toast.LENGTH_LONG).show();
        //Intent returnIntent = new Intent();
        //returnIntent.putExtra("result",date);
        //setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}