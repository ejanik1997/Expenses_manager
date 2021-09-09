package com.example.expenses_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class inspect_receipts extends AppCompatActivity {

    TextView receipts_list;
    TextView products_list;
    EditText receipt_id;
    EditText product_id;
    Button back;
    Button view_products;
    Button delete_receipt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_receipts);
        receipts_list = findViewById(R.id.tv_inspect_receipts_receipts_lst);
        products_list = findViewById(R.id.tv_inspect_receipts_products_lst);
        receipt_id = findViewById(R.id.et_inspect_receipts_receipt_id);
        back = findViewById(R.id.btn_isnpect_receipt_delete_product);
        view_products = findViewById(R.id.btn_isnpect_receipt_view_products);
        delete_receipt = findViewById(R.id.btn_inspect_receipts_delete_receipt);
        product_id = findViewById(R.id.et_inspect_receipts_product_id);
        receipts_list.setMovementMethod(new ScrollingMovementMethod());
        products_list.setMovementMethod(new ScrollingMovementMethod());
        Database_helper db_h = new Database_helper(this,
                null,
                null,
                Database_helper.DATABASE_VERSION);
        receipts_list.setText(db_h.load_handler_receipt());
    }


    public void view_products(View view)
    {
        if(!receipt_id.getText().toString().equals("")) {
            products_list.setText("");
            int id = Integer.parseInt(receipt_id.getText().toString());
            Database_helper db_h = new Database_helper(this,
                    null,
                    null,
                    Database_helper.DATABASE_VERSION);
            products_list.setText(db_h.load_handler_product(id));
        }
        else
            Toast.makeText(this, "Please, enter receipt id", Toast.LENGTH_LONG).show();
    }

    public void remove_receipt(View view)
    {
        if(!receipt_id.getText().toString().equals("")) {
            Database_helper db_h = new Database_helper(this,
                    null,
                    null,
                    Database_helper.DATABASE_VERSION);
            boolean res = db_h.delete_handler_receipt(Integer.parseInt(receipt_id.getText().toString()));
            if (res) {
                receipt_id.setText("");
                product_id.setText("");
                products_list.setText("");
                receipts_list.setText("");
                Toast.makeText(this, "Record deleted", Toast.LENGTH_LONG).show();
                receipts_list.setText(db_h.load_handler_receipt());
            } else {
                Toast.makeText(this, "No match found", Toast.LENGTH_LONG).show();
            }
        }
        else
            Toast.makeText(this, "Please, enter receipt id", Toast.LENGTH_LONG).show();
    }

    public void remove_product(View view)
    {
        if(!product_id.getText().toString().equals("") && !receipt_id.getText().toString().equals("")) {
            Database_helper db_h = new Database_helper(this,
                    null,
                    null,
                    1);
            boolean res = db_h.delete_handler_product(Integer.parseInt(product_id.getText().toString()));
            if (res) {
                int temp = Integer.parseInt(receipt_id.getText().toString());
                receipt_id.setText("");
                product_id.setText("");
                products_list.setText("");
                receipts_list.setText("");
                Toast.makeText(this, "Record deleted", Toast.LENGTH_LONG).show();
                receipts_list.setText(db_h.load_handler_receipt());
                products_list.setText(db_h.load_handler_product(temp));
            } else {
                Toast.makeText(this, "No match found", Toast.LENGTH_LONG).show();
            }
        }
        else
            Toast.makeText(this, "Please, enter both receipt and product id", Toast.LENGTH_LONG).show();
    }

}