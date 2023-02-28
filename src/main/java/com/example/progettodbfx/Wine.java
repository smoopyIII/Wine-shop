package com.example.progettodbfx;

public class Wine {

    private static final String DBURL = "jdbc:mysql://127.0.0.1:3306/Wineshop?";
    private static final String ARGS = "createDatabaseIfNotExist=true&serverTimezone=UTC";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "";
    private int id,contwine;
    private String name;
    private String producer;
    private String origin;
    private int year;
    private String technicalNotes;
    private String grapes;
    private float price;
    private int availability;

    public Wine(int id, String name, String producer, String origin, int year, String technicalNotes, String grapes, float price, int availability) {
        this.id=id;
        this.name = name;
        this.producer = producer;
        this.origin = origin;
        this.year = year;
        this.technicalNotes = technicalNotes;
        this.grapes = grapes;
        this.price=price;
        this.availability = availability;
    }
    public int getId() {
        return this.id;
    }

    public void setId( int i) {
        this.id = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTechnicalNotes() {
        return technicalNotes;
    }

    public void setTechnicalNotes(String technicalNotes) {
        this.technicalNotes = technicalNotes;
    }

    public String getGrapes() {
        return grapes;
    }

    public void setGrapes(String grapes) {
        this.grapes = grapes;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAvailability() { return availability; }

    public void setAvailability(int availability) { this.availability = availability; }

    public void printaddWine() {
        System.out.println("Wine{" + "id=" + this.id + " ,Name=" + this.name + ", Producer: " + this.producer + ", Origin: " + this.origin + ", Year: " + this.year + ", Technical Notes: " + this.technicalNotes + ", Grapes: " + this.grapes + ", Price: " + this.price + ", Availability: " + this.availability + "} successfully added");
    }

}






