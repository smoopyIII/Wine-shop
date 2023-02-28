package com.example.progettodbfx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Employee {
    private static final String DBURL = "jdbc:mysql://127.0.0.1:3306/Wineshop?";
    private static final String ARGS = "createDatabaseIfNotExist=true&serverTimezone=UTC";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "";
    private int id;
    private int contemployee;
    private static int contproduct = 0;
    ResultSet rset;
    String strSelect;
    private String name;
    private String surname;
    private String address;
    private String phoneNumber;
    private String fiscalCode;
    private String email;
    private String username;
    private String password;
    private int port;
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    int flagthread = 0;

    //employee constructor
    public Employee(int id, String name, String surname, String fiscalCode, String email, String phoneNumber, String address, String username, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.fiscalCode = fiscalCode;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
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
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

   /* public void menu(Socket socket) throws IOException, SQLException {
        Scanner scanner = new Scanner(System.in);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        String line;

        System.out.println("\n1. Logout");
        System.out.println("2. Add product");
        System.out.println("3. Search client");
        System.out.println("4. Search wine");

        System.out.print("Enter the number of your choice: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                System.out.println("You have logged out");
                System.out.println("\n>");

                Gestione_Employee.login();
                break;
            case 2:
                System.out.print("\nInsert the name of the wine: ");
                String name = scanner.nextLine();
                System.out.print("Insert the year of the wine: ");
                int year = Integer.parseInt(reader.readLine());
                System.out.print("Insert the producer of the wine: ");
                String producer = reader.readLine();
                System.out.print("Insert the origin of the wine: ");
                String origin = reader.readLine();
                System.out.print("Insert the tecnical notes of the wine: ");
                String tecnicalnotes = reader.readLine();
                System.out.print("Insert the grapes of the wine: ");
                String grapes = reader.readLine();
                System.out.print("Insert the price of the wine: ");
                Float price = Float.parseFloat(reader.readLine());
                System.out.print("Insert the number of available bottles: ");
                int availability = Integer.parseInt(reader.readLine());

                out.println("ADD_PRODUCT");
                out.println(name);
                out.println(year);
                out.println(producer);
                out.println(origin);
                out.println(tecnicalnotes);
                out.println(grapes);
                out.println(price);
                out.println(availability);

                break;
            case 3:
                System.out.print("Insert the surame of the client you want to search: ");
                name = scanner.nextLine();
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("SEARCH_CLIENT");
                out.println(name);

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (!(line = in.readLine()).equals("null")) {
                    System.out.println(line);
                }

                menu(socket);
                break;
            case 4:
                System.out.print("Insert the name of the wine you want to search: ");
                name = scanner.nextLine();
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("SEARCH_WINES");
                out.println(name);

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (!(line = in.readLine()).equals("null")) {
                    System.out.println(line);
                }

                menu(socket);
                break;
        }
    }*/
}