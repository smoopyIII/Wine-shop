package com.example.progettodbfx;

public class Report  {
    private static final String DBURL = "jdbc:mysql://127.0.0.1:3306/Wineshop?";
    private static final String ARGS = "createDatabaseIfNotExist=true&serverTimezone=UTC";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "";

    private String contwine;
    private String name;

    public Report(String contwine, String name) {
        this.contwine = contwine;
        this.name = name;
    }

    public String getContwine() {
        return contwine;
    }

    public void setContwine(String contwine) {
        this.contwine = contwine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
