package com.example.progettodbfx;

public class Proposal {
    private int id;
    private String wineId;

    private String clientId;
    private String producer;
    private int quantity;
    private int code;


    public Proposal(int id, String winename,String clientid, String p,int q, int c ) {
        this.id=id;
        this.wineId = winename;
        this.clientId = clientid;
        this.producer = p;
        this.quantity = q;
        this.code = c;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getWineId() {
        return wineId;
    }

    public void setWineId(String wineId) {
        this.wineId = wineId;
    }
}






