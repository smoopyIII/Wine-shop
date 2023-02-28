package com.example.progettodbfx;

public class Supplier {

    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String fiscalCode;
    private String companyAddress;
    private String username;
    private String password;

    public Supplier(String name, String surname, String email, String phoneNumber, String fiscalCode, String companyAddress, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.fiscalCode = fiscalCode;
        this.companyAddress = companyAddress;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
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
