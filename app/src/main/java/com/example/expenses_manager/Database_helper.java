package com.example.expenses_manager;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

public class Database_helper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "expenses_manager_DB.db";

    public final static String RECEIPT_TABLE = "receipt_table";
    public final static String COLUMN_ID = "receipt_id";
    public final static String COLUMN_DATE = "date";
    public final static String COLUMN_NOTE = "note";

    public final static String PRODUCT_TABLE = "product_table";
    public final static String COLUMN_ID_PRODUCT = "product_id";
    public final static String COLUMN_ID_RECEIPT = "receipt_id";
    public final static String ITEM_NAME = "item_name";
    public final static String ITEM_PRICE = "item_price";
    public final static String ITEM_CATEGORY = "item_category";


    public Database_helper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TABLE_PRODUCT = "CREATE TABLE " + PRODUCT_TABLE + " (" +
                COLUMN_ID_PRODUCT + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_ID_RECEIPT + " INTEGER NOT NULL," +
                ITEM_NAME + " TEXT, " +
                ITEM_PRICE + " TEXT, " +
                ITEM_CATEGORY + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_ID_RECEIPT + ") REFERENCES " +
                RECEIPT_TABLE + " (" + COLUMN_ID + ") ON DELETE CASCADE )";
        db.execSQL(CREATE_TABLE_PRODUCT);

        String CREATE_TABLE = "CREATE TABLE " + RECEIPT_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_DATE + " TEXT," +
                COLUMN_NOTE + " TEXT )";
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RECEIPT_TABLE);
        onCreate(db);
    }
    //receipt handlers ============================================================================
    public int get_auto_increment()
    {
        String result = null;
        String query = "SELECT * FROM SQLITE_SEQUENCE WHERE name = 'receipt_table' ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst())
        {
            do{
                result = cursor.getString(cursor.getColumnIndex("seq"));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        assert result != null;
        return Integer.parseInt(result);
    }
    public long add_handler_receipt(Receipt receipt)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, receipt.get_date());
        values.put(COLUMN_NOTE, receipt.get_optional_name());
        SQLiteDatabase db = this.getWritableDatabase();
        long success = db.insertOrThrow(RECEIPT_TABLE, null, values);
        db.close();
        return success;
    }

    public String load_handler_receipt()
    {
        String result = "";
        String query = "SELECT* FROM " + RECEIPT_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while(cursor.moveToNext())
        {
            int result_0 = cursor.getInt(0);
            String result_1 = cursor.getString(1);
            String result_2 = cursor.getString(2);
            result += String.valueOf(result_0) + ", " + result_1 + ", " +
                    result_2 + System.getProperty("line.separator");
        }
        cursor.close();
        db.close();
        return result;
    }
    public boolean delete_handler_receipt(int id)
    {
        boolean result = false;
        String query = "SELECT* FROM " + RECEIPT_TABLE +
                " WHERE " + COLUMN_ID + " = '" +
                String.valueOf(id) + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Receipt receipt = new Receipt();
        if(cursor.moveToFirst())
        {
            receipt.set_id(Integer.parseInt(cursor.getString(0)));
            db.delete(RECEIPT_TABLE, COLUMN_ID + "=?",
                    new String[] {String.valueOf(receipt.get_receipt_id())
                    });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
    //product handlers =============================================================================
    public long add_handler_product(Product product, int foreign_key)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_RECEIPT, foreign_key);
        values.put(ITEM_NAME, product.getName());
        values.put(ITEM_PRICE, product.getPrice());
        values.put(ITEM_CATEGORY, product.getCategory());
        SQLiteDatabase db = this.getWritableDatabase();
        long success = db.insertOrThrow(PRODUCT_TABLE, null, values);
        db.close();
        return success;
    }
    public String load_handler_product(int foreign_key)
    {
        String result = "";
        String query = "SELECT* FROM " + PRODUCT_TABLE +" WHERE " + COLUMN_ID_RECEIPT + " = '" +
                String.valueOf(foreign_key) + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while(cursor.moveToNext())
        {
            int result_0 = cursor.getInt(0);
            String result_1 = cursor.getString(1);
            String result_2 = cursor.getString(2);
            String result_3 = cursor.getString(3);
            String result_4 = cursor.getString(4);
            result += result_0 + ", " + result_1 + ", " + result_2 + ", " + result_3 + ", " + result_4 + ", " + System.getProperty("line.separator");
        }
        cursor.close();
        db.close();
        return result;
    }

    public boolean delete_handler_product(int id)
    {
        boolean result = false;
        String query = "SELECT* FROM " + PRODUCT_TABLE +
                " WHERE " + COLUMN_ID_PRODUCT+ " = '" +
                String.valueOf(id) + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Product product = new Product();
        if(cursor.moveToFirst())
        {
            product.setProduct_id(Integer.parseInt(cursor.getString(0)));
            db.delete(PRODUCT_TABLE, COLUMN_ID_PRODUCT + "=?",
                    new String[] {String.valueOf(product.getProduct_id())
                    });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
    //analyze tools ================================================================================
    public String[] get_categories()
    {
        return new String[]{"other", "food", "health", "comfort purchases", "utilities", "going-outs", "clothes", "regular payments"};
    }
    public int[] get_products_summary(int year, int month)
    {
        int[] result = new int[8];
        String[] category = get_categories();
        SQLiteDatabase db = this.getWritableDatabase();
        String date = "%" + month + "/" + year;

        for(int i = 0; i<= category.length-1; i++ )
        {
            String query = "SELECT SUM(item_price) FROM product_table INNER JOIN receipt_table ON receipt_table.receipt_id = product_table.receipt_id " +
                    "WHERE receipt_table.date LIKE " +"'" + date +"'" + " AND product_table.item_category = " +"'" +category[i] +"'";
            //String query = "SELECT receipt_id FROM receipt_table WHERE date " + "'" +date +"'"
            //String query = "SELECT SUM(item_price) FROM product_table WHERE item_category=" +"'" +category[i] +"'"
            //        +" AND receipt_id ";
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst())
                result[i] = cursor.getInt(0);
            else
                result[i] = 0;
            cursor.close();
        }
        db.close();
        return result;
    }
    public String get_products_list(int year, int month)
    {
        String result = "receipt_date receipt_name item_name item_category item_price"+ System.getProperty("line.separator");
        String date = "%" + month + "/" + year;
        String query = "SELECT date, note, item_name, item_category, item_price FROM " + PRODUCT_TABLE +" " +
                "INNER JOIN receipt_table ON receipt_table.receipt_id = product_table.receipt_id  WHERE receipt_table.date LIKE " +"'" + date +"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while(cursor.moveToNext())
        {
            String result_0 = cursor.getString(0);
            String result_1 = cursor.getString(1);
            String result_2 = cursor.getString(2);
            String result_3 = cursor.getString(3);
            String result_4 = cursor.getString(4);
            result += result_0 + ", " + result_1 + ", " + result_2 + ", " + result_3 + ", " + result_4 + ", " + System.getProperty("line.separator");
        }
        cursor.close();
        db.close();
        return result;
    }

    //debug and utilities ==========================================================================
    public void fill_db()
    {
        String[] colummn_date = {"29/12/2020", "29/12/2020", "29/12/2020", "29/12/2020", "29/12/2020", "29/12/2020", "29/12/2020", "29/12/2020", "29/12/2020", "29/12/2020", "29/12/2020", "29/12/2020", "29/12/2020", "29/12/2020", "29/12/2020", "29/12/2020", "30/12/2020", "30/12/2020", "30/12/2020", "30/12/2020", "30/12/2020", "30/12/2020", "30/12/2020", "30/12/2020", "30/12/2020", "30/12/2020", "30/12/2020", "30/12/2020", "30/12/2020", "30/12/2020", "30/12/2020", "30/12/2020", "30/12/2020", "30/12/2020", "30/12/2020", "30/12/2020", "31/12/2020", "31/12/2020", "31/12/2020", "31/12/2020", "31/12/2020", "31/12/2020", "31/12/2020", "31/12/2020", "31/12/2020", "31/12/2020"};
        String[] columnn_note = {"zakupy biedronka", "zakupi Ali", "jedzenie na miescie", "zakupy emil", "zakupy matylda", "ikea"};
        String[] item_name = {"zel pod prysznic", "koszulka jojo", "nasiona trawy", "sianko dla Wandzi", "zestaw szklanek", "pizza", "pampuchy", "swieczki", "yerba mate", "papier toaletowy"};
        String[] price = {"19.99", "59.99", "16.99", "7.99", "89.99", "12.99", "115.99"};
        String[] category = {"other", "food", "health", "comfort purchases", "utilities", "going-outs", "clothes", "regular payments"};
        SQLiteDatabase db = this.getWritableDatabase();
        for(int i = 0; i <= 40; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DATE, colummn_date[i%(colummn_date.length)]);
            values.put(COLUMN_NOTE, columnn_note[i%columnn_note.length]);
            long success = db.insertOrThrow(RECEIPT_TABLE, null, values);
            for(int j = 0; j <= 15; j++) {
                ContentValues valuess = new ContentValues();
                valuess.put(COLUMN_ID_RECEIPT, (int) success);
                valuess.put(ITEM_NAME, item_name[j%item_name.length]);
                valuess.put(ITEM_PRICE, price[j%price.length]);
                valuess.put(ITEM_CATEGORY, category[j%category.length]);
                long successs = db.insertOrThrow(PRODUCT_TABLE, null, valuess);
            }
        }
        db.close();
    }
}

