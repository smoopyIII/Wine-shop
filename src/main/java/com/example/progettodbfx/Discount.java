package com.example.progettodbfx;


public class Discount {
    private String discount;
    private String wine;
    private String oldvalue;

    public Discount(String wine,String discount, String oldvalue) {
        this.wine = wine;
        this.discount = discount;
        this.oldvalue = oldvalue;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
    public String getDiscount() {
        return discount;
    }
    public void setWine(String wine) {
        this.wine = wine;
    }
    public String getWine() {
        return wine;
    }
    public void setoldvalue(String wine) {
        this.oldvalue = oldvalue;
    }
    public String getoldvalue() {
        return oldvalue;
    }

}
