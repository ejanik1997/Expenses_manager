package com.example.expenses_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class analyze_expenses extends AppCompatActivity {

    private static final String TAG = "analyze_expenses";
    private DatePickerDialog.OnDateSetListener date_set_listener;
    TextView text_view_date;
    TextView tv_firstrow_value;
    TextView tv_secondrow_value;
    TextView tv_thirdrow_value;
    TextView tv_fourthrow_value;
    TextView tv_fifthrow_value;
    TextView tv_sixthrow_value;
    TextView tv_seventhrow_value;
    TextView tv_eighthrow_value;
    TextView tv_lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_expenses);
        text_view_date = findViewById(R.id.tv_analyze_expenses_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
        tv_firstrow_value = findViewById(R.id.tv_analyze_expenses_value_firstrow);
        tv_secondrow_value = findViewById(R.id.tv_analyze_expenses_value_secondrow);
        tv_thirdrow_value = findViewById(R.id.tv_analyze_expenses_value_thirdrow);
        tv_fourthrow_value = findViewById(R.id.tv_analyze_expenses_value_fourthrow);
        tv_fifthrow_value = findViewById(R.id.tv_analyze_expenses_value_fifthrow);
        tv_sixthrow_value = findViewById(R.id.tv_analyze_expenses_value_sixthrow);
        tv_seventhrow_value = findViewById(R.id.tv_analyze_expenses_value_seventhrow);
        tv_eighthrow_value = findViewById(R.id.tv_analyze_expenses_value_eighthrow);
        tv_lst = findViewById(R.id.tv_analyze_expenses_list);
        tv_lst.setMovementMethod(new ScrollingMovementMethod());
        tv_lst.setHorizontallyScrolling(true);
        text_view_date.setText(dateFormat.format(new Date()));
        text_view_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        analyze_expenses.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        date_set_listener,
                        year, month, 0);
                dialog.getDatePicker().findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
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
                Log.d(TAG, "onDateSet: mm/yyy: " + month + "/" + year);

                String date = month + "/" + year;
                text_view_date.setText(date);

            }
        };
    }

    public void analyze(View view)
    {
        int[] results;
        Database_helper db_h = new Database_helper(this,
                null,
                null,
                Database_helper.DATABASE_VERSION);
        String date = text_view_date.getText().toString();
        String[] sep = date.split("/");
        int month = Integer.parseInt(sep[0]);
        int year = Integer.parseInt(sep[1]);
        results = db_h.get_products_summary(year, month);
        //Toast.makeText(this, String.valueOf(results[0]), Toast.LENGTH_LONG).show();
        tv_firstrow_value.setText(String.valueOf(results[0]));
        tv_secondrow_value.setText(String.valueOf(results[1]));
        tv_thirdrow_value.setText(String.valueOf(results[2]));
        tv_fourthrow_value.setText(String.valueOf(results[3]));
        tv_fifthrow_value.setText(String.valueOf(results[4]));
        tv_sixthrow_value.setText(String.valueOf(results[5]));
        tv_seventhrow_value.setText(String.valueOf(results[6]));
        tv_eighthrow_value.setText(String.valueOf(results[7]));

        tv_lst.setText(db_h.get_products_list(year, month));


    }
}