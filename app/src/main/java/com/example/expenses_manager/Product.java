package com.example.expenses_manager;

public class Product {
    private int product_id;
    private String name;
    private String price;
    private String category;

    public Product() {}

    public Product(String name, String price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
