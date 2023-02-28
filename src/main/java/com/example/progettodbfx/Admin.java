package com.example.progettodbfx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;


public class Admin {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static final String DBURL = "jdbc:mysql://127.0.0.1:3306/Wineshop?";
    private static final String ARGS = "createDatabaseIfNotExist=true&serverTimezone=UTC";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "";

    private int id;
    private String name;
    private String surname;
    private String fiscalCode;
    private String email;
    private String phoneNumber;
    private String Address;
    private String username;
    private String password;
    private Socket socket;

    public Admin(int id, String name, String surname, String fiscalCode, String email, String phoneNumber, String Address, String username, String password){

        this.id=id;
        this.name = name;
        this.surname = surname;
        this.fiscalCode = fiscalCode;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.Address = Address;
        this.username = username;
        this.password = password;
        this.socket=null;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String deliveryAddress) {
        this.Address = deliveryAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
