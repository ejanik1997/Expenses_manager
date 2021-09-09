package com.example.expenses_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

public class adding_products extends AppCompatActivity {

    EditText product_name;
    EditText product_price;
    Spinner product_category;
    int id;
    int last_added_product_id = 0;
    TextView lst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_products);
        Bundle extras = getIntent().getExtras();
        String id = extras != null ? extras.getString("id") : null;
        String date = extras != null ? extras.getString("date") : null;
        String name = extras != null ? extras.getString("name") : null;
        TextView receipt_id = findViewById(R.id.textview_receipt_id_adding_products);
        TextView receipt_date = findViewById(R.id.textview_receipt_date_adding_products);
        TextView receipt_name = findViewById(R.id.textview_receipt_note_adding_products);
        receipt_id.setText(id);
        receipt_date.setText(date);
        receipt_name.setText(name);

        product_name = findViewById(R.id.et_adding_products_product_name);
        product_price = findViewById(R.id.et_adding_products_product_price);
        product_category = findViewById(R.id.spn_adding_products_product_category);
        String[] product_categories_array = new String[]{"other", "food", "health", "comfort purchases", "utilities", "going-outs", "clothes", "regular payments"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, product_categories_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        product_category.setAdapter(adapter);
        lst = findViewById(R.id.textview_lst_adding_products);
        Toast.makeText(this, "You can multiply two numbers in price field by putting space between them", Toast.LENGTH_LONG).show();

    }
    public void add_product(View view)
    {
        Bundle extras = getIntent().getExtras();
        String id = extras != null ? extras.getString("id") : null;
        assert id != null;
        int _id = Integer.parseInt(id);
        if(!product_name.getText().toString().equals("") &&
                !product_price.getText().toString().equals("") &&
                !product_category.getSelectedItem().toString().equals("")) {
            Database_helper db_h = new Database_helper(this,
                    null,
                    null,
                    Database_helper.DATABASE_VERSION);
            String name = product_name.getText().toString();
            String price = product_price.getText().toString();
            price = price.trim();
            if(price.contains(" "))
            {

                String[] sep = price.split(" ");
                price = String.valueOf(Integer.parseInt(sep[0])*Integer.parseInt(sep[1]));
            }
            String category = product_category.getSelectedItem().toString();
            Product product = new Product(name, price, category);
            long succ = db_h.add_handler_product(product, _id);
            int _succ = (int) succ;
            Toast.makeText(this, "Last ID: " + _succ, Toast.LENGTH_LONG).show();
            last_added_product_id = _succ;
            product_name.setText("");
            product_price.setText("");
            product_category.setSelection(0);
            load_products(view);
        }
        else
            Toast.makeText(this, "Please, enter name, price and category", Toast.LENGTH_LONG).show();
    }

    public void load_products(View view)
    {
        Bundle extras = getIntent().getExtras();
        String id = extras != null ? extras.getString("id") : null;
        assert id != null;
        int _id = Integer.parseInt(id);
        lst.setText("");
        Database_helper db_h = new Database_helper(this,
                null,
                null,
                Database_helper.DATABASE_VERSION);
        lst.setText(db_h.load_handler_product(_id));
    }

    public void cancel_receipt(View view)
    {
        Bundle extras = getIntent().getExtras();
        String id = extras != null ? extras.getString("id") : null;
        assert id != null;
        int _id = Integer.parseInt(id);
        Database_helper db_h = new Database_helper(this,
                null,
                null,
                Database_helper.DATABASE_VERSION);
        db_h.delete_handler_receipt(_id);
        finish();
    }

    public void delete_last_product(View view)
    {
        if(last_added_product_id == 0)
            Toast.makeText(this, "No product has yet been added", Toast.LENGTH_LONG).show();
        else
        {
            Bundle extras = getIntent().getExtras();
            String id = extras != null ? extras.getString("id") : null;
            assert id != null;
            int _id = Integer.parseInt(id);
            Database_helper db_h = new Database_helper(this,
                    null,
                    null,
                    Database_helper.DATABASE_VERSION);
            db_h.delete_handler_product(last_added_product_id);
            Toast.makeText(this, "Record has been deleted", Toast.LENGTH_LONG).show();
            lst.setText("");
            lst.setText(db_h.load_handler_product(_id));
        }
    }

    public void finish(View view)
    {
        Toast.makeText(this, "Products succesfully added", Toast.LENGTH_LONG).show();
        finish();
    }
}