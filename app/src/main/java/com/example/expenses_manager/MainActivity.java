package com.example.expenses_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DatePickerDialog.OnDateSetListener date_set_listener;
    TextView lst;
    TextView text_view_date;
    EditText edit_text_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lst = findViewById(R.id.lst);
        lst.setMovementMethod(new ScrollingMovementMethod());
        text_view_date = findViewById(R.id.tv_receipt_date);
        edit_text_name = findViewById(R.id.edittext_receipt_name);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        text_view_date.setText(dateFormat.format(new Date()));
        Toast.makeText(this, "Tap on the date to change it", Toast.LENGTH_LONG).show();
        //the next bit is from
        //https://www.youtube.com/watch?v=hwe1abDO2Ag
        text_view_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        date_set_listener,
                        year, month, day);
                dialog.getDatePicker().setCalendarViewShown(false);
                dialog.getDatePicker().getSpinnersShown();
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

        });
        date_set_listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                text_view_date.setText(date);

            }
        };
    }
    //end of bit

    public void add_receipt(View view)
    {

        Database_helper db_h = new Database_helper(this,
                null,
                null,
                Database_helper.DATABASE_VERSION);
        String date = text_view_date.getText().toString();
        String name = edit_text_name.getText().toString();
        int temp = db_h.get_auto_increment() + 1;
        //Toast.makeText(this, "temp: " + temp, Toast.LENGTH_LONG).show();
        if(name.trim().isEmpty())
            name = "receipt_" + temp;
        Receipt receipt = new Receipt(date, name);
        long succ = db_h.add_handler_receipt(receipt);
        int id = (int) succ;
        edit_text_name.setText("");
        //Toast.makeText(this, "Last ID: " + id, Toast.LENGTH_LONG).show();
        String _id = "" + succ;
        Intent intent = new Intent(this, adding_products.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", _id);
        bundle.putString("date", date);
        bundle.putString("name", name);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void load_receipts(View view)
    {
        Database_helper db_h = new Database_helper(this,
                null,
                null,
                Database_helper.DATABASE_VERSION);
        lst.setText(db_h.load_handler_receipt());
    }

    public void inspect_receipts(View view)
    {
        Intent intent = new Intent(this, inspect_receipts.class);
        startActivity(intent);
    }

    public void fill_db(View view)
    {
        Database_helper db_h = new Database_helper(this,
                null,
                null,
                Database_helper.DATABASE_VERSION);
        db_h.fill_db();
        Toast.makeText(this, "Database filled", Toast.LENGTH_LONG).show();
    }

    public void analyze_expenses(View view)
    {
        Intent intent = new Intent(this, analyze_expenses.class);
        startActivity(intent);
    }

    /*public void change_date(View view)
    {
        Intent intent = new Intent(this, change_date.class);
        startActivityForResult(intent, 1);
    }*/
    /*public void remove_receipt(View view)
    {

        Database_helper db_h = new Database_helper(this,
                null,
                null,
                1);
        boolean res = db_h.delete_handler(Integer.parseInt(edit_text_id.getText().toString()));
        if(res)
        {
            edit_text_id.setText("");
            edit_text_date.setText("");
            edit_text_name.setText("");
            Toast.makeText(this, "Record deleted", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "No match found", Toast.LENGTH_LONG).show();
        }
    }*/
}