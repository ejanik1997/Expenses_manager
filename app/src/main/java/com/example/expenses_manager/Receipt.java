package com.example.expenses_manager;

public class Receipt {
    private int receipt_id;
    private String date;
    private String optional_name;

    //constructor
    public Receipt() {}
    public Receipt(int id) {
        this.receipt_id = id;
    }
    public Receipt(String date, String optional_name)
    {
        this.date = date;
        this.optional_name = optional_name;
    }
    //setters
    public void set_id(int id)
    {
        this.receipt_id = id;
    }
    public void set_date(String date)
    {
        this.date = date;
    }
    public void set_name(String name)
    {
        this.optional_name = name;
    }
    //getters
    public int get_receipt_id()
    {
        return this.receipt_id;
    }
    public String get_date()
    {
        return this.date;
    }
    public String get_optional_name()
    {
        return this.optional_name;
    }
}