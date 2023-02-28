package com.example.progettodbfx;

public class Purchase {
    private int id;
    private int cid;
    private String product;
    private String date;
    private double value;
    private double price;
    private int quantity;

    public Purchase(int id, int cid, String product,String date,int quantity,  float price) {
        this.id = id;
        this.cid = cid;
        this.product = product;
        this.date = date;
        this.price= price;
        this.quantity=quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

