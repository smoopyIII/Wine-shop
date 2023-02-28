package com.example.progettodbfx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;


public class Client {

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
    private String deliveryAddress;
    private String username;
    private String password;
    private Socket socket;

    public Client(int id, String name, String surname, String fiscalCode, String email, String phoneNumber, String deliveryAddress, String username, String password){

        this.id=id;
        this.name = name;
        this.surname = surname;
        this.fiscalCode = fiscalCode;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
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

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
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

    public void menu(Socket socket) throws IOException, SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n1. Logout");
        System.out.println("2. Show wines");
        System.out.println("3. Search wine");
        System.out.println("4. Buy wine");
        System.out.println("5. Print purchases");

        System.out.print("Enter the number of your choice: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                System.out.println("You have logged out");
                System.out.println("\n>");

                //Gestione_Client.login();
                Gestione_Client.login(this.username,this.password);
                break;
            case 2:
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("SHOW_WINES");

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line;
                while (!(line = in.readLine()).equals("null")) {
                    System.out.println(line);
                }

                menu(socket);
                break;
            case 3:
                System.out.print("Insert the name of the wine you want to search: ");
                String name = scanner.nextLine();
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("SEARCH_WINES");
                out.println(name);

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (!(line = in.readLine()).equals("null")) {
                    System.out.println(line);
                }

                menu(socket);
                break;
            case 4:

            case 5:
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("SEARCH_PURCHASES");
                out.println(this.id);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (!(line = in.readLine()).equals("null")) {
                    System.out.println(line);
                }
                menu(socket);
                break;


            default:
                System.out.println("You have entered an invalid value");
                menu(socket);
                break;
        }
    }


}
