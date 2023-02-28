package com.example.progettodbfx;

public class PurchaseNew {
    private int id;
    private String cid;
    private String date;
    private float val;
    private int quantity;
    private String product;

    public PurchaseNew(int id, String cid, String date, float price, int quantity , String product) {
        this.id = id; //id purchase
        this.cid = cid; //id client
        this.date = date; //data
        this.val = price;
        this.quantity=quantity;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getVal() {
        return val;
    }

    public void setVal(float val) {
        this.val = val;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }


}

