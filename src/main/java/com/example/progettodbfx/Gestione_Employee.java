package com.example.progettodbfx;

import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.Scanner;

public class Gestione_Employee {
    private static final String DBURL = "jdbc:mysql://127.0.0.1:3306/Wineshop?";
    private static final String ARGS = "createDatabaseIfNotExist=true&serverTimezone=UTC";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "";

    public static Employee login(String username, String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DBURL + ARGS, LOGIN, PASSWORD); Statement stmt = conn.createStatement();) {

            int flag;

            String strSelect = "SELECT * FROM employees WHERE employees.username='" + username + "' and employees.password='" + password + "'";
            ResultSet rset = stmt.executeQuery(strSelect);
            while (rset.next()) {
                flag = 0;
                int id = rset.getInt("id");
                String name = rset.getString("name");
                String surname = rset.getString("surname");
                String fiscalCode = rset.getString("fiscal_code");
                String email = rset.getString("email");
                String phoneNumber = rset.getString("phone_number");
                String address = rset.getString("address");
                Employee employee = new Employee(id, name, surname, fiscalCode, email, phoneNumber, address, username, password);
                if (flag == 0) {
                    return employee;
                } else {
                    return null;
                }
            }
        }
        return null;
    }
}