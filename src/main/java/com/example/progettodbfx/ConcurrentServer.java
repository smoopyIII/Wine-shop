package com.example.progettodbfx;

import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.*;

import static java.lang.String.valueOf;

public class ConcurrentServer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        final String DBURL = "jdbc:mysql://127.0.0.1:3306/Wineshop?";
        final String ARGS = "createDatabaseIfNotExist=true&serverTimezone=UTC";
        final String LOGIN = "root";
        final String PASSWORD = "";

        try (Connection conn = DriverManager.getConnection(DBURL + ARGS, LOGIN, PASSWORD); Statement stmt = conn.createStatement();) {
            ServerSocket serverSocket = new ServerSocket(4444, 20);

            while (true) {
                final Socket socket = serverSocket.accept();
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Scanner in = new Scanner(socket.getInputStream());
                            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                            while (true) {
                                String line = in.nextLine();

                                if (line.equals("SHOW_WINES")) {
                                    String strSelect = "SELECT * FROM wines";
                                    ResultSet rset = stmt.executeQuery(strSelect);

                                    while (rset.next()) {
                                        String data = rset.getInt("id") + "/" + rset.getString("name") + "/" + rset.getString("producer") + "/" + rset.getString("origin") + "/" + rset.getInt("year") + "/" + rset.getString("tecnical_notes") + "/" + rset.getString("grapes") + "/" + rset.getInt("prize") + "/" + rset.getInt("availability");
                                        out.println(data);
                                    }
                                    out.println("null");
                                } else if (line.equals("SHOW_WINES_TO_ORDER")) {
                                    String strSelect = "SELECT winetoorder.*, wines.* FROM winetoorder INNER JOIN wines ON winetoorder.wineid = wines.id";
                                    ResultSet rset = stmt.executeQuery(strSelect);

                                    while (rset.next()) {
                                        String data = rset.getInt("winetoorder.id") + "/" + rset.getString("wines.name") + "/" + rset.getString("wines.producer") + "/" + rset.getString("wines.origin") + "/" + rset.getInt("wines.year") + "/" + rset.getString("wines.tecnical_notes") + "/" + rset.getString("wines.grapes") + "/" + rset.getInt("wines.prize") + "/" + rset.getInt("winetoorder.quantityToOrder");
                                        out.println(data);
                                    }
                                    out.println("null");
                                } else if (line.equals("BUY_STOCK")) {
                                    String code = in.nextLine();
                                    String employeeID = in.nextLine();

                                    //PRENDO I DATI DA WINETOORDER
                                    String strselect = "SELECT * FROM winetoorder WHERE id='" + code + "'";
                                    ResultSet rset = stmt.executeQuery(strselect);
                                    rset.next();
                                    String wineid = rset.getString("wineid");
                                    String quantity = rset.getString("quantityToOrder");
                                    System.out.println("wineid - quantity" + wineid + " " + quantity);

                                    //AGGIUNGO QUANTITA' IN WINES
                                    strselect = "SELECT * FROM wines WHERE wines.id='" + wineid + "'";
                                    rset = stmt.executeQuery(strselect);
                                    if (!rset.next()) {
                                        out.println("null");
                                    } else {
                                        int q, newQ;
                                        q = Integer.parseInt(rset.getString("availability"));
                                        newQ = q + (Integer.parseInt(quantity));

                                        String updateSql = "UPDATE wines SET availability = ? WHERE wines.id='" + wineid + "'";
                                        PreparedStatement pstmt = conn.prepareStatement(updateSql);
                                        pstmt.setString(1, valueOf(newQ));
                                        pstmt.executeUpdate();
                                        pstmt.close();
                                    }

                                    //aggiungere alla tabella purchase_employee l'acquisto prendendo il prezzo dalla taballe wines_employee
                                    String strSelect2 = "SELECT * FROM wines_employee WHERE wines_employee.id='" + wineid + "'";
                                    ResultSet rset2 = stmt.executeQuery(strSelect2);
                                    float price = 0;
                                    while (rset2.next()) {
                                        price = rset2.getFloat("prize");
                                    }

                                    String strselect3 = "SELECT max(id) as id FROM purchases_employee";
                                    ResultSet rset3 = stmt.executeQuery(strselect3);
                                    int maxID = 0;
                                    while (rset3.next()) {
                                        maxID = rset3.getInt("id");
                                        maxID = maxID + 1;
                                    }

                                    //aggiungere l'acquisto alla tabella purchase_employee
                                    String insertSql2 = "insert into purchases_employee values (?, ?, ?, ?, ?, ?)";
                                    PreparedStatement pstmt2 = conn.prepareStatement(insertSql2);
                                    int intQuantity = Integer.parseInt(quantity);
                                    Purchase p2 = new Purchase(maxID, Integer.parseInt(employeeID), wineid, valueOf(LocalDate.now()), Integer.parseInt(quantity), (intQuantity * price));
                                    pstmt2.setInt(1, p2.getId());
                                    pstmt2.setInt(2, p2.getCid());
                                    pstmt2.setInt(3, Integer.parseInt(p2.getProduct()));
                                    pstmt2.setString(4, p2.getDate());
                                    pstmt2.setInt(5, p2.getQuantity());
                                    pstmt2.setDouble(6, p2.getPrice());

                                    pstmt2.addBatch();
                                    pstmt2.executeBatch();

                                    //ELIMINO LA RIGA IN WINETOORDER
                                    String deleteSql = "DELETE FROM winetoorder WHERE id='" + code + "'";
                                    PreparedStatement pstmt3 = conn.prepareStatement(deleteSql);
                                    pstmt3.addBatch();
                                    pstmt3.executeBatch();
                                } else if (line.equals("RESET_PASSWORD")) {

                                    String username = in.nextLine();
                                    String phone_number = in.nextLine();

                                    String strSelect = "SELECT * FROM clients WHERE clients.username='" + username + "' AND clients.phone_number='" + phone_number + "'";
                                    ResultSet rset = stmt.executeQuery(strSelect);
                                    int cont = 0;
                                    String password = null;
                                    while (rset.next()) {
                                        password = rset.getString("clients.password");
                                        cont++;
                                    }
                                    if (cont != 0)
                                        out.println(password);
                                    else {
                                        strSelect = "SELECT * FROM employees WHERE employees.username='" + username + "' AND employees.phone_number='" + phone_number + "'";
                                        rset = stmt.executeQuery(strSelect);
                                        cont = 0;
                                        while (rset.next()) {
                                            password = rset.getString("employees.password");
                                            cont++;
                                        }
                                        if (cont != 0)
                                            out.println(password);
                                        else
                                            out.println("null");
                                    }
                                } else if (line.equals("SHOW_WINES_BY_NAME")) {
                                    String strSelect = "";
                                    int yearwinetosearch = 0;
                                    String namewinetosearch = in.nextLine();
                                    String year = in.nextLine();

                                    if (!year.equals("null")) {
                                        yearwinetosearch = Integer.parseInt(year);
                                    }


                                    if ((!namewinetosearch.equals("")) && (yearwinetosearch > 1989 && yearwinetosearch < 2024)) {
                                        strSelect = "SELECT * FROM wines WHERE wines.name LIKE '%" + namewinetosearch + "%' AND wines.year = '" + yearwinetosearch + "'";
                                    } else if (yearwinetosearch > 1989 && yearwinetosearch < 2024) {
                                        strSelect = "SELECT * FROM wines WHERE wines.year = '" + yearwinetosearch + "'";
                                    } else {
                                        strSelect = "SELECT * FROM wines WHERE wines.name LIKE '%" + namewinetosearch + "%'";
                                    }

                                    ResultSet rset = stmt.executeQuery(strSelect);

                                    while (rset.next()) {
                                        String data = rset.getInt("id") + "/" + rset.getString("name") + "/" + rset.getString("producer") + "/" + rset.getString("origin") + "/" + rset.getInt("year") + "/" + rset.getString("tecnical_notes") + "/" + rset.getString("grapes") + "/" + rset.getInt("prize") + "/" + rset.getInt("availability");
                                        out.println(data);
                                    }
                                    out.println("null");
                                } else if (line.equals("SEARCH_WINES")) {
                                    int trovato = 0;
                                    line = in.nextLine();
                                    String strselect = "SELECT * FROM wines WHERE wines.name='" + line + "'";
                                    ResultSet rset = stmt.executeQuery(strselect);

                                    while (rset.next()) {
                                        trovato = 1;
                                        String data = rset.getInt("id") + " " + rset.getString("name") + " " + rset.getString("producer") + " " + rset.getString("origin") + " " + rset.getInt("year") + " " + rset.getString("tecnical_notes") + " " + rset.getString("grapes") + " " + rset.getInt("prize");
                                        out.println(data);
                                    }
                                    if (trovato == 0)
                                        out.println("La sua ricerca non ha prodotto risultati");
                                    out.println("null");

                                } else if (line.equals("SHOW_CLIENT")) {
                                    int trovato = 0;
                                    line = in.nextLine();
                                    String strselect = "SELECT * FROM clients WHERE clients.surname='" + line + "'";
                                    ResultSet rset = stmt.executeQuery(strselect);

                                    while (rset.next()) {
                                        trovato = 1;
                                        String data = rset.getInt("id") + "/" + rset.getString("name") + "/" + rset.getString("surname") + "/" + rset.getString("fiscal_code") + "/" + rset.getString("email") + "/" + rset.getString("phone_number") + "/" + rset.getString("delivery_address");
                                        out.println(data);
                                    }
                                    if (trovato == 0)
                                        out.println("La sua ricerca non ha prodotto risultati");
                                    out.println("null");

                                } else if (line.equals("SHOW_EMPLOYEE")) {
                                    int trovato = 0;
                                    String strselect = "SELECT * FROM employees";
                                    ResultSet rset = stmt.executeQuery(strselect);

                                    while (rset.next()) {
                                        trovato = 1;
                                        String data = rset.getInt("id") + "/" + rset.getString("name") + "/" + rset.getString("surname") + "/" + rset.getString("fiscal_code") + "/" + rset.getString("email") + "/" + rset.getString("phone_number") + "/" + rset.getString("address") + "/" + rset.getString("username");
                                        out.println(data);
                                    }
                                    if (trovato == 0)
                                        out.println("La sua ricerca non ha prodotto risultati");
                                    out.println("null");
                                } else if (line.equals("SEARCH_EMPLOYEE_BY_USERNAME")) {
                                    int trovato = 0;
                                    String username = in.nextLine();
                                    String strselect = "SELECT * FROM employees WHERE employees.username='" + username + "'";
                                    ResultSet rset = stmt.executeQuery(strselect);

                                    while (rset.next()) {
                                        trovato = 1;
                                        String data = rset.getInt("id") + "/" + rset.getString("name") + "/" + rset.getString("surname") + "/" + rset.getString("fiscal_code") + "/" + rset.getString("email") + "/" + rset.getString("phone_number") + "/" + rset.getString("address") + "/" + rset.getString("username") + "/" + rset.getString("password");
                                        out.println(data);
                                    }
                                    if (trovato == 0)
                                        out.println("La sua ricerca non ha prodotto risultati");
                                    out.println("null");
                                } else if (line.equals("EDIT_EMPLOYEE")) {
                                    String name = in.nextLine();
                                    String surname = in.nextLine();
                                    String fiscalcode = in.nextLine();
                                    String email = in.nextLine();
                                    String phone_number = in.nextLine();
                                    String address = in.nextLine();
                                    String username = in.nextLine();
                                    String password = in.nextLine();
                                    String oldusername = in.nextLine();

                                    if (!username.equals(oldusername)) {
                                        String strselect = "SELECT * FROM employees WHERE employees.username='" + username + "'";
                                        ResultSet rset = stmt.executeQuery(strselect);
                                        if (rset.next()) {
                                            out.println("null");
                                        } else {
                                            out.println("ok");
                                            String updateSql = "UPDATE employees SET name = ?, surname = ?, fiscal_code = ?, email = ?, phone_number = ?, address = ?, username = ?, password = ? WHERE username = ?";
                                            PreparedStatement pstmt = conn.prepareStatement(updateSql);
                                            pstmt.setString(1, name);
                                            pstmt.setString(2, surname);
                                            pstmt.setString(3, fiscalcode);
                                            pstmt.setString(4, email);
                                            pstmt.setString(5, phone_number);
                                            pstmt.setString(6, address);
                                            pstmt.setString(7, username);
                                            pstmt.setString(8, password);
                                            pstmt.setString(9, oldusername);
                                            pstmt.executeUpdate();
                                            pstmt.close();
                                        }
                                    } else {
                                        out.println("ok");
                                        String updateSql = "UPDATE employees SET name = ?, surname = ?, fiscal_code = ?, email = ?, phone_number = ?, address = ?, username = ?, password = ? WHERE username = ?";
                                        PreparedStatement pstmt = conn.prepareStatement(updateSql);
                                        pstmt.setString(1, name);
                                        pstmt.setString(2, surname);
                                        pstmt.setString(3, fiscalcode);
                                        pstmt.setString(4, email);
                                        pstmt.setString(5, phone_number);
                                        pstmt.setString(6, address);
                                        pstmt.setString(7, username);
                                        pstmt.setString(8, password);
                                        pstmt.setString(9, oldusername);
                                        pstmt.executeUpdate();
                                        pstmt.close();
                                    }

                                } else if (line.equals("DELETE_EMPLOYEE")) {
                                    line = in.nextLine();
                                    String deleteSql = "DELETE FROM employees WHERE employees.username='" + line + "'";
                                    PreparedStatement pstmt = conn.prepareStatement(deleteSql);
                                    pstmt.addBatch();
                                    pstmt.executeBatch();
                                } else if (line.equals("SEARCH_PURCHASES")) {
                                    int trovato = 0;

                                    String id = in.nextLine();
                                    String strSelect = "SELECT * FROM clients WHERE clients.id='" + id + "'";
                                    ResultSet rset = stmt.executeQuery(strSelect);
                                    String name = "", surname = "";
                                    while (rset.next()) {
                                        name = rset.getString("name");
                                        surname = rset.getString("surname");
                                    }
                                    //strSelect = "SELECT * FROM purchases WHERE purchases.cid='" + id + "'";
                                    strSelect = "SELECT purchases.id, wines.name, purchases.date, purchases.value, purchases.quantity FROM purchases INNER JOIN clients ON purchases.cid = clients.id INNER JOIN wines ON purchases.product = wines.id WHERE purchases.cid='" + id + "'";

                                    rset = stmt.executeQuery(strSelect);

                                    while (rset.next()) {
                                        trovato = 1;
                                        String data = rset.getInt("purchases.id") + "/" + rset.getString("wines.name") + "/" + rset.getString("purchases.date") + "/" + rset.getDouble("purchases.value") + "/" + rset.getInt("purchases.quantity");
                                        out.println(data);
                                    }
                                    if (trovato == 0)
                                        out.println("NO_RESULT");
                                    out.println("null");

                                } else if (line.equals("SEARCH_CLIENT")) {
                                    int trovato = 0;
                                    line = in.nextLine();
                                    String strselect = "SELECT * FROM clients WHERE clients.surname LIKE '%" + line + "%'";
                                    ResultSet rset = stmt.executeQuery(strselect);

                                    while (rset.next()) {
                                        trovato = 1;

                                        String data = rset.getInt("id") + "/" + rset.getString("name") + "/" + rset.getString("surname") + "/" + rset.getString("delivery_address") + "/" + rset.getString("phone_number") + "/" + rset.getString("fiscal_code") + "/" + rset.getString("email");
                                        out.println(data);

                                    }
                                    if (trovato == 0)
                                        out.println("La sua ricerca non ha prodotto risultati");
                                    out.println("null");
                                } else if (line.equals("GET_ADMIN_DATA")) {
                                    int id = 0;
                                    id = Integer.parseInt(in.nextLine());
                                    String strselect = "SELECT * FROM admins WHERE admins.id='" + id + "'";
                                    ResultSet rset = stmt.executeQuery(strselect);

                                    rset.next();
                                    String name = rset.getString("name");
                                    String surname = rset.getString("surname");
                                    String delivery_address = rset.getString("address");
                                    String phone_number = rset.getString("phone_number");
                                    String fiscal_code = rset.getString("fiscal_code");
                                    String email = rset.getString("email");
                                    String username = rset.getString("username");
                                    //String password = rset.getString("password");
                                    out.println(name);
                                    out.println(surname);
                                    out.println(delivery_address);
                                    out.println(phone_number);
                                    out.println(fiscal_code);
                                    out.println(email);
                                    out.println(username);
                                    //out.println(password);
                                } else if (line.equals("GET_CLIENT_DATA")) {
                                    int id = 0;
                                    id = Integer.parseInt(in.nextLine());
                                    String strselect = "SELECT * FROM clients WHERE clients.id='" + id + "'";
                                    ResultSet rset = stmt.executeQuery(strselect);

                                    rset.next();
                                    String name = rset.getString("name");
                                    String surname = rset.getString("surname");
                                    String delivery_address = rset.getString("delivery_address");
                                    String phone_number = rset.getString("phone_number");
                                    String fiscal_code = rset.getString("fiscal_code");
                                    String email = rset.getString("email");
                                    String username = rset.getString("username");
                                    //String password = rset.getString("password");
                                    out.println(name);
                                    out.println(surname);
                                    out.println(delivery_address);
                                    out.println(phone_number);
                                    out.println(fiscal_code);
                                    out.println(email);
                                    out.println(username);
                                    //out.println(password);
                                } else if (line.equals("GET_EMPLOYEE_DATA")) {
                                    int id = 0;
                                    id = Integer.parseInt(in.nextLine());
                                    String strselect = "SELECT * FROM employees WHERE employees.id='" + id + "'";
                                    ResultSet rset = stmt.executeQuery(strselect);

                                    rset.next();
                                    String name = rset.getString("name");
                                    String surname = rset.getString("surname");
                                    String address = rset.getString("address");
                                    String phone_number = rset.getString("phone_number");
                                    String fiscal_code = rset.getString("fiscal_code");
                                    String email = rset.getString("email");
                                    String username = rset.getString("username");
                                    //String password = rset.getString("password");
                                    out.println(name);
                                    out.println(surname);
                                    out.println(address);
                                    out.println(phone_number);
                                    out.println(fiscal_code);
                                    out.println(email);
                                    out.println(username);
                                    //out.println(password);
                                } else if (line.equals("GET_CLIENT_USERNAME")) {
                                    int id = 0;
                                    id = Integer.parseInt(in.nextLine());
                                    String strselect = "SELECT * FROM clients WHERE clients.id='" + id + "'";
                                    ResultSet rset = stmt.executeQuery(strselect);

                                    rset.next();

                                    String username = rset.getString("username");
                                    out.println(username);
                                } else if (line.equals("GET_EMPLOYEE_USERNAME")) {
                                    int id = 0;
                                    id = Integer.parseInt(in.nextLine());
                                    String strselect = "SELECT * FROM employees WHERE employees.id='" + id + "'";
                                    ResultSet rset = stmt.executeQuery(strselect);

                                    rset.next();

                                    String username = rset.getString("username");
                                    out.println(username);
                                } else if (line.equals("CHECK_OLD_PASSWORD")) {
                                    int id = 0;
                                    id = Integer.parseInt(in.nextLine());
                                    String strselect = "SELECT * FROM employees WHERE employees.id='" + id + "'";
                                    ResultSet rset = stmt.executeQuery(strselect);

                                    rset.next();

                                    String password = rset.getString("password");
                                    out.println(password);
                                } else if (line.equals("CHECK_OLD_ADMIN_PASSWORD")) {
                                    int id = 0;
                                    id = Integer.parseInt(in.nextLine());
                                    String strselect = "SELECT * FROM admins WHERE admins.id='" + id + "'";
                                    ResultSet rset = stmt.executeQuery(strselect);

                                    rset.next();

                                    String password = rset.getString("password");
                                    out.println(password);
                                } else if (line.equals("UPDATE_PASSWORD")) {
                                    int id = 0;
                                    id = Integer.parseInt(in.nextLine());
                                    String newPassword = in.nextLine();
                                    String strselect = "UPDATE employees SET password='" + newPassword + "' WHERE employees.id='" + id + "'";
                                    stmt.executeUpdate(strselect);
                                } else if (line.equals("UPDATE_ADMIN_PASSWORD")) {
                                    int id = 0;
                                    id = Integer.parseInt(in.nextLine());
                                    String newPassword = in.nextLine();
                                    String strselect = "UPDATE admins SET password='" + newPassword + "' WHERE admins.id='" + id + "'";
                                    stmt.executeUpdate(strselect);
                                } else if (line.equals("SHOW_PROPOSAL")) {
                                    int trovato = 0;
                                    int id = Integer.parseInt(in.nextLine());
                                    String strselect = "SELECT proposal_tmp.id, wines.name ,wines.producer, proposal_tmp.quantity, proposal_tmp.value, clients.name FROM proposal_tmp JOIN wines ON proposal_tmp.product = wines.id JOIN clients ON proposal_tmp.cid = clients.id WHERE proposal_tmp.cid = '" + id + "' ORDER BY proposal_tmp.id";
                                    ResultSet rset = stmt.executeQuery(strselect);

                                    while (rset.next()) {
                                        trovato = 1;
                                        String data = rset.getInt("proposal_tmp.id") + "/" + rset.getString("wines.name") + "/" + rset.getString("wines.producer") + "/" + rset.getInt("proposal_tmp.quantity") + "/" + rset.getFloat("proposal_tmp.value") + "/" + rset.getString("clients.name");
                                        out.println(data);
                                    }
                                    if (trovato == 0)
                                        out.println("NO_PROPOSALS");
                                    out.println("null");
                                } else if (line.equals("SHOW_ALL_PROPOSAL")) {
                                    int trovato = 0;
                                    line = in.nextLine();
                                    String strselect = "SELECT proposal2.id, wines.name ,wines.producer, proposal2.quantity, proposal2.value, clients.name FROM proposal2 JOIN wines ON proposal2.product = wines.id JOIN clients ON proposal2.cid = clients.id ORDER BY proposal2.id";
                                    ResultSet rset = stmt.executeQuery(strselect);

                                    while (rset.next()) {
                                        trovato = 1;
                                        String data = rset.getInt("proposal2.id") + "/" + rset.getString("wines.name") + "/" + rset.getString("wines.producer") + "/" + rset.getInt("proposal2.quantity") + "/" + rset.getFloat("proposal2.value") + "/" + rset.getString("clients.name");
                                        out.println(data);
                                    }
                                    if (trovato == 0)
                                        out.println("NO_PROPOSALS");
                                    out.println("null");
                                } else if (line.equals("CHECK_VALUE")) {
                                    String id = in.nextLine();
                                    int quantityrecived = Integer.parseInt(in.nextLine());
                                    String strSelect = "SELECT wines.* FROM wines WHERE wines.id='" + id + "'";
                                    ResultSet rset = stmt.executeQuery(strSelect);

                                    int quantity = 0;
                                    while (rset.next()) {
                                        quantity = Integer.parseInt(rset.getString("availability"));
                                        if (quantity - quantityrecived >= 0) {
                                            out.println("ENOUGHT");
                                        } else {
                                            out.println("PURCHASE_PROPOSAL");
                                        }
                                    }
                                } else if (line.equals("SEARCH_USERNAME")) {
                                    String username = in.nextLine();
                                    String strSelect = "SELECT * FROM clients WHERE clients.username='" + username + "'";
                                    ResultSet rset = stmt.executeQuery(strSelect);
                                    int cont = 0;
                                    while (rset.next()) {
                                        cont++;
                                    }
                                    if (cont == 0)
                                        out.println("null");
                                    else
                                        out.println("finded");
                                } else if (line.equals("SEARCH_ADMIN_USERNAME")) {
                                    String username = in.nextLine();
                                    String strSelect = "SELECT * FROM employees WHERE employees.username='" + username + "'";
                                    ResultSet rset = stmt.executeQuery(strSelect);
                                    int cont = 0;
                                    while (rset.next()) {
                                        cont++;
                                    }
                                    if (cont == 0)
                                        out.println("null");
                                    else
                                        out.println("finded");
                                } else if (line.equals("SIGNUP")) {

                                    int contclients = 0;

                                    String name = in.nextLine();
                                    String surname = in.nextLine();
                                    String fiscalcode = in.nextLine();
                                    String email = in.nextLine();
                                    String address = in.nextLine();
                                    String phonenumber = in.nextLine();
                                    String username = in.nextLine();
                                    String password = in.nextLine();

                                    String strSelect = "SELECT max(id) as id FROM clients";
                                    ResultSet rset = stmt.executeQuery(strSelect);

                                    while (rset.next()) {
                                        contclients = rset.getInt("id") + 1;
                                    }
                                    String insertSql = "insert into clients values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                                    PreparedStatement pstmt = conn.prepareStatement(insertSql);
                                    Client c = new Client(contclients, name, surname, fiscalcode, email, address, phonenumber, username, password);

                                    pstmt.setInt(1, contclients);
                                    pstmt.setString(2, c.getName());
                                    pstmt.setString(3, c.getSurname());
                                    pstmt.setString(4, c.getFiscalCode());
                                    pstmt.setString(5, c.getEmail());
                                    pstmt.setString(6, c.getDeliveryAddress());
                                    pstmt.setString(7, c.getPhoneNumber());
                                    pstmt.setString(8, c.getUsername());
                                    pstmt.setString(9, c.getPassword());
                                    pstmt.addBatch();
                                    pstmt.executeBatch();


                                } else if (line.equals("SIGNUP_ADMIN")) {

                                    int contadmin = 0;
                                    String name = in.nextLine();
                                    String surname = in.nextLine();
                                    String fiscalcode = in.nextLine();
                                    String email = in.nextLine();
                                    String address = in.nextLine();
                                    String phonenumber = in.nextLine();
                                    String username = in.nextLine();
                                    String password = in.nextLine();

                                    String strSelect = "SELECT max(id) as id FROM employees";
                                    ResultSet rset = stmt.executeQuery(strSelect);

                                    while (rset.next()) {
                                        contadmin = rset.getInt("id") + 1;
                                    }
                                    String insertSql = "insert into employees values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                                    PreparedStatement pstmt = conn.prepareStatement(insertSql);
                                    Employee a = new Employee(contadmin, name, surname, fiscalcode, email, address, phonenumber, username, password);

                                    pstmt.setInt(1, contadmin);
                                    pstmt.setString(2, a.getName());
                                    pstmt.setString(3, a.getSurname());
                                    pstmt.setString(4, a.getFiscalCode());
                                    pstmt.setString(5, a.getEmail());
                                    pstmt.setString(6, a.getAddress());
                                    pstmt.setString(7, a.getPhoneNumber());
                                    pstmt.setString(8, a.getUsername());
                                    pstmt.setString(9, a.getPassword());
                                    pstmt.addBatch();
                                    pstmt.executeBatch();
                                } else if (line.equals("ADD_PRODUCT")) {

                                    int contproduct = 0;

                                    String name = in.nextLine();
                                    String producer = in.nextLine();
                                    String origin = in.nextLine();
                                    int year = Integer.parseInt(in.nextLine());
                                    String tecnicalnotes = in.nextLine();
                                    String grapes = in.nextLine();
                                    float price = Float.parseFloat(in.nextLine());
                                    int availability = Integer.parseInt(in.nextLine());
                                    int min = Integer.parseInt(in.nextLine());

                                    //controllare se il prodotto è già presente
                                    String strSelect0 = "SELECT * FROM wines WHERE wines.name='" + name + "' AND wines.producer='" + producer + "' AND wines.origin='" + origin + "' AND wines.year='" + year + "' AND wines.grapes='" + grapes + "'";
                                    ResultSet rset0 = stmt.executeQuery(strSelect0);
                                    int cont = 0;
                                    while (rset0.next()) {
                                        cont++;
                                    }
                                    if (cont == 0) {

                                        String strSelect = "SELECT max(id) as id FROM wines";
                                        ResultSet rset = stmt.executeQuery(strSelect);

                                        while (rset.next()) {
                                            contproduct = rset.getInt("id") + 1;
                                        }

                                        String insertSql = "insert into wines values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                                        PreparedStatement pstmt = conn.prepareStatement(insertSql);
                                        Wine w = new Wine(contproduct, name, producer, origin, year, tecnicalnotes, grapes, price, availability);

                                        pstmt.setInt(1, contproduct);
                                        pstmt.setString(2, w.getName());
                                        pstmt.setString(3, w.getProducer());
                                        pstmt.setString(4, w.getOrigin());
                                        pstmt.setInt(5, w.getYear());
                                        pstmt.setString(6, w.getTechnicalNotes());
                                        pstmt.setString(7, w.getGrapes());
                                        pstmt.setFloat(8, w.getPrice());
                                        pstmt.setInt(9, w.getAvailability());

                                        pstmt.addBatch();

                                        pstmt.executeBatch();

                                        String insertSql2 = "insert into wines_employee values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                                        PreparedStatement pstmt2 = conn.prepareStatement(insertSql2);
                                        Wine w2 = new Wine(contproduct, name, producer, origin, year, tecnicalnotes, grapes, price / 3, availability);

                                        pstmt2.setInt(1, contproduct);
                                        pstmt2.setString(2, w2.getName());
                                        pstmt2.setString(3, w2.getProducer());
                                        pstmt2.setString(4, w2.getOrigin());
                                        pstmt2.setInt(5, w2.getYear());
                                        pstmt2.setString(6, w2.getTechnicalNotes());
                                        pstmt2.setString(7, w2.getGrapes());
                                        pstmt2.setFloat(8, w2.getPrice());
                                        pstmt2.setInt(9,0);
                                        pstmt2.addBatch();

                                        pstmt2.executeBatch();

                                        String insertSql3 = "insert into minquantity values (?, ?, ?)";
                                        PreparedStatement pstmt3 = conn.prepareStatement(insertSql3);
                                        pstmt3.setInt(1, contproduct);
                                        pstmt3.setInt(2, min);
                                        pstmt3.setInt(3, 0);

                                        pstmt3.addBatch();

                                        pstmt3.executeBatch();

                                        String insertSql4 = "insert into notify values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                                        PreparedStatement pstmt4 = conn.prepareStatement(insertSql4);
                                        Wine w4 = new Wine(contproduct, name, producer, origin, year, tecnicalnotes, grapes, price, availability);

                                        String date = valueOf(LocalDate.now());

                                        pstmt4.setInt(1, contproduct);
                                        pstmt4.setString(2, w4.getName());
                                        pstmt4.setString(3, date);
                                        pstmt4.setString(4, date + " - Sono state aggiunte delle bottiglie di " + name + " al prezzo di: " + price + " €");
                                        pstmt4.setInt(5, w4.getYear());
                                        pstmt4.setString(6, w4.getTechnicalNotes());
                                        pstmt4.setString(7, w4.getGrapes());
                                        pstmt4.setFloat(8, w4.getPrice());
                                        pstmt4.setInt(9, w4.getAvailability());
                                        pstmt4.addBatch();

                                        pstmt4.executeBatch();

                                        out.println("OK");
                                        System.out.println("ok");
                                    } else {
                                        out.println("ERROR");
                                        System.out.println("error");
                                    }


                                } else if (line.equals("BUY_WINES")) {

                                    String id = in.nextLine();
                                    int quantity = Integer.parseInt(in.nextLine());
                                    int cid = Integer.parseInt(in.nextLine());
                                    int contpurchases = Integer.parseInt(in.nextLine());
                                    float price_tot = Float.parseFloat(in.nextLine());
                                    price_tot = (float) (Math.round(price_tot * 100.0) / 100.0);

                                    String date = valueOf(LocalDate.now());

                                    String insertSql = "insert into purchases values (?, ?, ?, ?, ?, ?)";
                                    PreparedStatement pstmt = conn.prepareStatement(insertSql);
                                    Purchase p = new Purchase(contpurchases, cid, id, date, quantity, price_tot);

                                    pstmt.setInt(1, contpurchases);
                                    pstmt.setInt(2, p.getCid());
                                    pstmt.setInt(3, Integer.parseInt(p.getProduct()));
                                    pstmt.setString(4, p.getDate());
                                    pstmt.setInt(5, p.getQuantity());
                                    pstmt.setDouble(6, p.getPrice());

                                    pstmt.addBatch();
                                    pstmt.executeBatch();

                                    String strSelect = "SELECT * FROM wines WHERE wines.id='" + id + "'";
                                    ResultSet rset = stmt.executeQuery(strSelect);
                                    int quantityrem = 0;
                                    while (rset.next()) {
                                        quantityrem = rset.getInt("availability");
                                    }
                                    quantityrem = quantityrem - quantity;
                                    String updateSql = "UPDATE wines SET availability = ? WHERE id = ?";
                                    PreparedStatement pstmt2 = conn.prepareStatement(updateSql);
                                    pstmt2.setInt(1, quantityrem);
                                    pstmt2.setInt(2, Integer.parseInt(id));
                                    pstmt2.executeUpdate();

                                    String strSelect1 = "SELECT * FROM minquantity WHERE minquantity.id='" + id + "'";
                                    ResultSet rset1 = stmt.executeQuery(strSelect1);
                                    int minquantity = 0;
                                    while (rset1.next()) {
                                        minquantity = rset1.getInt("minquantity");
                                    }

                                    if (quantityrem < minquantity) {
                                        String insertSql2 = "insert into wineToOrder values (?, ?, ?)";
                                        PreparedStatement pstmt3 = conn.prepareStatement(insertSql2);
                                        int counter = 0;
                                        String strSelect2 = "SELECT max(id) as id FROM wineToOrder";
                                        ResultSet rset2 = stmt.executeQuery(strSelect2);

                                        while (rset2.next()) {
                                            counter = rset2.getInt("id") + 1;
                                        }

                                        pstmt3.setInt(1, counter);
                                        pstmt3.setInt(2, Integer.parseInt(id));
                                        pstmt3.setInt(3, 10 - quantityrem);
                                        pstmt3.addBatch();
                                        pstmt3.executeBatch();

                                    }


                                } else if (line.equals("PRICE_WINE")) {
                                    float price = 0;
                                    String id = in.nextLine();
                                    String strSelect = "SELECT * FROM wines WHERE wines.id='" + id + "'";
                                    ResultSet rset = stmt.executeQuery(strSelect);

                                    while (rset.next()) {
                                        price = rset.getInt("prize");
                                    }
                                    out.println(price);
                                } else if (line.equals("CONT_PROPOSAL")) {

                                    String strSelect = "SELECT max(id) as id FROM proposal2";
                                    ResultSet rset = stmt.executeQuery(strSelect);
                                    int contpurchases = 0;

                                    rset.next();
                                    contpurchases = rset.getInt("id") + 1;
                                    out.println(contpurchases);
                                } else if (line.equals("CONT_PURCHASES")) {

                                    String strSelect = "SELECT max(id) as id FROM purchases";
                                    ResultSet rset = stmt.executeQuery(strSelect);
                                    int contpurchases = 0;

                                    rset.next();
                                    contpurchases = rset.getInt("id") + 1;
                                    out.println(contpurchases);
                                } else if (line.equals("TMP_PURCHASE")) {
                                    int idtmp = 0;
                                    int idProp = 0;
                                    int cid = 0;
                                    int quantity = 0;
                                    float price;
                                    String date = "";
                                    String product = "";

                                    List<String> list = new ArrayList<String>();
                                    //listOfList.add(list);

                                    //TROVA IL MASSIMO IN PROPOSAL_TMP
                                    String strSelect = "SELECT max(id) as id FROM proposal_tmp";
                                    ResultSet rset1 = stmt.executeQuery(strSelect);

                                    while (rset1.next()) {
                                        idtmp = rset1.getInt("id") + 1;
                                    }

                                    //MI MOSTRA TUTTI I DATI IN PROPOSAL2 CHE HANNO LO STESSO ID
                                    String id = in.nextLine();
                                    String strselect = "SELECT proposal2.* FROM proposal2 WHERE proposal2.id='" + id + "'";
                                    ResultSet rset = stmt.executeQuery(strselect);

                                    while (rset.next()) {
                                        idProp = rset.getInt("proposal2.id");
                                        cid = rset.getInt("proposal2.cid");
                                        quantity = rset.getInt("proposal2.quantity");
                                        price = rset.getFloat("proposal2.value");
                                        date = rset.getString("proposal2.date");
                                        product = rset.getString("proposal2.product");

                                        String insertSql = "insert into proposal_tmp values (?, ?, ?, ?, ?, ?)";
                                        PreparedStatement pstmt = conn.prepareStatement(insertSql);

                                        Purchase p = new Purchase(idtmp, cid, product, date, quantity, price);

                                        pstmt.setInt(1, p.getId());
                                        pstmt.setInt(2, p.getCid());
                                        pstmt.setInt(3, Integer.parseInt(p.getProduct()));
                                        pstmt.setString(4, p.getDate());
                                        pstmt.setInt(5, p.getQuantity());
                                        pstmt.setDouble(6, p.getPrice());

                                        list.add(product);

                                        pstmt.addBatch();
                                        pstmt.executeBatch();
                                    }


                                    for (int i = 0; i < list.size(); i++) {
                                        //aggiungere alla tabella purchase_employee l'acquisto prendendo il prezzo dalla taballe wines_employee
                                        String strSelect2 = "SELECT * FROM wines_employee WHERE wines_employee.id='" + list.get(i) + "'";
                                        ResultSet rset2 = stmt.executeQuery(strSelect2);
                                        float price2 = 0;
                                        while (rset2.next()) {
                                            price2 = rset2.getFloat("prize");
                                        }

                                        //aggiungere l'acquisto alla tabella purchase_employee
                                        String insertSql2 = "insert into purchases_employee values (?, ?, ?, ?, ?, ?)";
                                        PreparedStatement pstmt2 = conn.prepareStatement(insertSql2);

                                        Purchase p2 = new Purchase(idtmp, cid, product, date, quantity, (quantity * price2));

                                        pstmt2.setInt(1, p2.getId());
                                        pstmt2.setInt(2, p2.getCid());
                                        pstmt2.setInt(3, Integer.parseInt(p2.getProduct()));
                                        pstmt2.setString(4, p2.getDate());
                                        pstmt2.setInt(5, p2.getQuantity());
                                        pstmt2.setDouble(6, p2.getPrice());

                                        pstmt2.addBatch();
                                        pstmt2.executeBatch();
                                    }

                                    String deleteSql = "delete from proposal2 where id ='" + id + "'";
                                    PreparedStatement pstmt = conn.prepareStatement(deleteSql);
                                    pstmt.addBatch();
                                    pstmt.executeBatch();
                                } else if (line.equals("REFUSE_PURCHASE")) {
                                    List<List<Integer>> listOfList = new ArrayList<>();

                                    String id = in.nextLine();
                                    String strselect = "SELECT proposal_tmp.* FROM proposal_tmp WHERE proposal_tmp.id='" + id + "'";
                                    ResultSet rset = stmt.executeQuery(strselect);
                                    while (rset.next()) {
                                        int quantity = rset.getInt("proposal_tmp.quantity");
                                        String product = rset.getString("proposal_tmp.product");

                                        List<Integer> list = Arrays.asList(quantity, Integer.parseInt(product));
                                        listOfList.add(list);
                                    }
                                    rset.close();

                                    for (List<Integer> list : listOfList) {
                                        int quantity = list.get(0);
                                        int product = list.get(1);

                                        String strselect2 = "SELECT wines.* FROM wines WHERE wines.id='" + product + "'";
                                        ResultSet rset2 = stmt.executeQuery(strselect2);
                                        int quantityrem = 0;
                                        if (rset2.next()) {
                                            quantityrem = rset2.getInt("availability");
                                        }
                                        quantityrem = quantityrem + quantity;

                                        rset2.close();

                                        String updateSql = "UPDATE wines SET availability = ? WHERE id = ?";
                                        PreparedStatement pstmt2 = conn.prepareStatement(updateSql);
                                        pstmt2.setInt(1, quantityrem);
                                        pstmt2.setString(2, valueOf(product));
                                        pstmt2.executeUpdate();
                                        pstmt2.close();

                                    }
                                } else if (line.equals("SEARCH_WINES_ON_SALE")) {
                                    String strselect = "SELECT discount.* FROM discount";
                                    ResultSet rset = stmt.executeQuery(strselect);

                                    while (rset.next()) {
                                        String data = rset.getString("discount.wine") + "/" + rset.getInt("discount.discounts")+ "/" + rset.getFloat("discount.oldValue");
                                        out.println(data);
                                    }
                                    out.println("null");
                                } else if(line.equals("CREATE_DISCOUNT")) {

                                    String deleteSql = "DELETE FROM discount";
                                    PreparedStatement pstmt2 = conn.prepareStatement(deleteSql);
                                    pstmt2.addBatch();
                                    pstmt2.executeBatch();

                                    int i=0;
                                    List<Integer> spinnerVal = new ArrayList<>();
                                    String spinnerSize = in.nextLine();


                                    for (i = 0; i < (Integer.parseInt(spinnerSize)); i++) {
                                        String strselect = "SELECT name,prize FROM wines WHERE wines.id='"+ (i+1) +"' ";
                                        ResultSet rset = stmt.executeQuery(strselect);
                                        rset.next();
                                        String nome = rset.getString("name");
                                        Float price = rset.getFloat("prize");

                                        spinnerVal.add(Integer.parseInt(in.nextLine()));
                                        System.out.println("Index: " + i + " -  valore: " + spinnerVal.get(i));

                                        String insertSql = "insert into discount values (?, ?, ?)";
                                        PreparedStatement pstmt = conn.prepareStatement(insertSql);
                                        Discount d = new Discount(nome, Integer.toString(spinnerVal.get(i)), Float.toString(price));

                                        pstmt.setString(1, d.getWine());
                                        pstmt.setInt(2, Integer.parseInt(d.getDiscount()));
                                        pstmt.setFloat(3,  Float.parseFloat(d.getoldvalue()));

                                        pstmt.addBatch();
                                        pstmt.executeBatch();
                                    }

                                    for(i=0;i<(Integer.parseInt(spinnerSize));i++){
                                        String strselect = "SELECT oldValue FROM minquantity WHERE minquantity.id='"+ (i+1) +"' ";
                                        ResultSet rset = stmt.executeQuery(strselect);
                                        rset.next();
                                        Float oldValue = rset.getFloat("oldValue");

                                        int discount = spinnerVal.get(i);
                                        if(discount != 0){
                                            oldValue = oldValue - ((oldValue * discount)/100);
                                        }

                                        String updateSql = "UPDATE wines SET prize='" + oldValue + "' WHERE wines.id='" + (i+1) + "'";
                                        PreparedStatement pstmt = conn.prepareStatement(updateSql);
                                        pstmt.executeUpdate();

                                    }
                                } else if(line.equals("GET_DISCOUNT")){

                                        String strselect = "SELECT discounts FROM discount";
                                        ResultSet rset = stmt.executeQuery(strselect);

                                        while(rset.next()){
                                            int data = rset.getInt("discounts");
                                            out.println(data);
                                        }
                                        out.println("null");

                                }else if (line.equals("ACCEPT_PROPOSAL_CLIENT")) {
                                    String id = in.nextLine();
                                    String strselect = "SELECT proposal_tmp.* FROM proposal_tmp WHERE proposal_tmp.id='" + id + "'";
                                    ResultSet rset = stmt.executeQuery(strselect);

                                    while (rset.next()) {
                                        String data = rset.getString("proposal_tmp.product") + "/" + rset.getInt("proposal_tmp.quantity") + "/" + id;
                                        out.println(data);
                                    }
                                    out.println("null");

                                    /*while (rset.next()) {
                                        int idProp = rset.getInt("proposal_tmp.id") ;
                                        int cid = rset.getInt("proposal_tmp.cid");
                                        int quantity = rset.getInt("proposal_tmp.quantity");
                                        float price = rset.getFloat("proposal_tmp.value");
                                        String date = rset.getString("proposal_tmp.date");
                                        String product = rset.getString("proposal_tmp.product");

                                        String insertSql = "insert into purchases values (?, ?, ?, ?, ?, ?)";
                                        PreparedStatement pstmt = conn.prepareStatement(insertSql);
                                        Purchase p = new Purchase(idProp,cid,product,date,quantity,price);

                                        pstmt.setInt(1, idProp);
                                        pstmt.setInt(2, p.getCid());
                                        pstmt.setInt(3, Integer.parseInt(p.getProduct()));
                                        pstmt.setString(4, p.getDate());
                                        pstmt.setInt(5, p.getQuantity());
                                        pstmt.setDouble(6, p.getPrice());

                                        pstmt.addBatch();
                                        pstmt.executeBatch();
                                    }
                                    String deleteSql = "delete from proposal_tmp where id ='" + id + "'";
                                    PreparedStatement pstmt = conn.prepareStatement(deleteSql);
                                    pstmt.addBatch();
                                    pstmt.executeBatch();*/
                                } else if (line.equals("DELETE_PROPOSAL_TMP")) {
                                    String id = in.nextLine();
                                    String deleteSql = "delete from proposal_tmp where id ='" + id + "'";
                                    PreparedStatement pstmt = conn.prepareStatement(deleteSql);
                                    pstmt.addBatch();
                                    pstmt.executeBatch();
                                } else if (line.equals("DELETE_PROPOSAL_2")) {
                                    String id = in.nextLine();
                                    String deleteSql = "delete from proposal2 where id ='" + id + "'";
                                    PreparedStatement pstmt = conn.prepareStatement(deleteSql);
                                    pstmt.addBatch();
                                    pstmt.executeBatch();
                                } else if (line.equals("REFUSE_PROPOSAL_CLIENT")) {
                                    String id = in.nextLine();
                                    String strselect = "SELECT proposal_tmp.product ,proposal_tmp.quantity FROM proposal_tmp WHERE proposal_tmp.id='" + id + "'";
                                    ResultSet rset = stmt.executeQuery(strselect);

                                    while (rset.next()) {
                                        int quantity = rset.getInt("proposal_tmp.quantity");
                                        String product = rset.getString("proposal_tmp.product");

                                        String insertSql = "UPDATE wines SET avaibility='" + quantity + "' WHERE wines.id='" + product + "'";
                                        PreparedStatement pstmt = conn.prepareStatement(insertSql);
                                    }

                                    String deleteSql = "delete from proposal_tmp where id ='" + id + "'";
                                    PreparedStatement pstmt = conn.prepareStatement(deleteSql);
                                    pstmt.addBatch();
                                    pstmt.executeBatch();
                                } else if (line.equals("SALES")) {
                                    String d1 = in.nextLine();
                                    String d2 = in.nextLine();
                                    String strselect;

                                    if (!d1.equals("null") && !d2.equals("null")) {

                                        strselect = "SELECT purchases.id, clients.name, wines.name, purchases.date, purchases.value, purchases.quantity FROM purchases INNER JOIN clients ON purchases.cid = clients.id INNER JOIN wines ON purchases.product = wines.id WHERE purchases.date BETWEEN '" + d1 + "' AND '" + d2 + "'";

                                        ResultSet rset = stmt.executeQuery(strselect);

                                        while (rset.next()) {
                                            String data = rset.getInt("purchases.id") + "/" + rset.getString("clients.name") + "/" + rset.getString("wines.name") + "/" + rset.getString("purchases.date") + "/" + rset.getFloat("purchases.value") + "/" + rset.getInt("purchases.quantity");
                                            out.println(data);
                                        }
                                        out.println("null");
                                    } else if (d1.equals("null") && !d2.equals("null")) {

                                        strselect = "SELECT purchases.id, clients.name, wines.name, purchases.date, purchases.value, purchases.quantity FROM purchases INNER JOIN clients ON purchases.cid = clients.id INNER JOIN wines ON purchases.product = wines.id WHERE purchases.date <= '" + d2 + "'";

                                        ResultSet rset = stmt.executeQuery(strselect);

                                        while (rset.next()) {
                                            String data = rset.getInt("purchases.id") + "/" + rset.getString("clients.name") + "/" + rset.getString("wines.name") + "/" + rset.getString("purchases.date") + "/" + rset.getFloat("purchases.value") + "/" + rset.getInt("purchases.quantity");
                                            out.println(data);
                                        }
                                        out.println("null");
                                    } else if (!d1.equals("null") && d2.equals("null")) {

                                        strselect = "SELECT purchases.id, clients.name, wines.name, purchases.date, purchases.value, purchases.quantity FROM purchases INNER JOIN clients ON purchases.cid = clients.id INNER JOIN wines ON purchases.product = wines.id WHERE purchases.date >= '" + d1 + "'";

                                        ResultSet rset = stmt.executeQuery(strselect);

                                        while (rset.next()) {
                                            String data = rset.getInt("purchases.id") + "/" + rset.getString("clients.name") + "/" + rset.getString("wines.name") + "/" + rset.getString("purchases.date") + "/" + rset.getFloat("purchases.value") + "/" + rset.getInt("purchases.quantity");
                                            out.println(data);
                                        }
                                        out.println("null");
                                    } else {

                                        strselect = "SELECT purchases.id, clients.name, wines.name, purchases.date, purchases.value, purchases.quantity FROM purchases INNER JOIN clients ON purchases.cid = clients.id INNER JOIN wines ON purchases.product = wines.id";
                                        ResultSet rset = stmt.executeQuery(strselect);

                                        while (rset.next()) {
                                            String data = rset.getInt("purchases.id") + "/" + rset.getString("clients.name") + "/" + rset.getString("wines.name") + "/" + rset.getString("purchases.date") + "/" + rset.getFloat("purchases.value") + "/" + rset.getInt("purchases.quantity");
                                            out.println(data);
                                        }
                                        out.println("null");
                                        out.println("null");
                                    }
                                } else if (line.equals("PURCHASE")) {
                                    String d1 = in.nextLine();
                                    String d2 = in.nextLine();
                                    String strselect;

                                    if (!d1.equals("null") && !d2.equals("null")) {

                                        strselect = "SELECT purchases_employee.id, employees.username, wines.name, purchases_employee.date, purchases_employee.value, purchases_employee.quantity FROM purchases_employee INNER JOIN employees ON purchases_employee.cid = employees.id INNER JOIN wines ON purchases_employee.product = wines.id WHERE purchases_employee.date BETWEEN '" + d1 + "' AND '" + d2 + "'";
                                        ResultSet rset = stmt.executeQuery(strselect);

                                        while (rset.next()) {
                                            String data = rset.getInt("purchases_employee.id") + "/" + rset.getString("employees.username") + "/" + rset.getString("wines.name") + "/" + rset.getString("purchases_employee.date") + "/" + rset.getFloat("purchases_employee.value") + "/" + rset.getInt("purchases_employee.quantity");
                                            out.println(data);
                                        }
                                        out.println("null");
                                    } else if (d1.equals("null") && !d2.equals("null")) {

                                        strselect = "SELECT purchases_employee.id, employees.username, wines.name, purchases_employee.date, purchases_employee.value, purchases_employee.quantity FROM purchases_employee INNER JOIN employees ON purchases_employee.cid = employees.id INNER JOIN wines ON purchases_employee.product = wines.id WHERE purchases_employee.date <= '" + d2 + "'";
                                        ResultSet rset = stmt.executeQuery(strselect);

                                        while (rset.next()) {
                                            String data = rset.getInt("purchases_employee.id") + "/" + rset.getString("employees.username") + "/" + rset.getString("wines.name") + "/" + rset.getString("purchases_employee.date") + "/" + rset.getFloat("purchases_employee.value") + "/" + rset.getInt("purchases_employee.quantity");
                                            out.println(data);
                                        }
                                        out.println("null");
                                    } else if (!d1.equals("null") && d2.equals("null")) {

                                        strselect = "SELECT purchases_employee.id, employees.username, wines.name, purchases_employee.date, purchases_employee.value, purchases_employee.quantity FROM purchases_employee INNER JOIN employees ON purchases_employee.cid = employees.id INNER JOIN wines ON purchases_employee.product = wines.id WHERE purchases_employee.date >= '" + d1 + "'";
                                        ResultSet rset = stmt.executeQuery(strselect);

                                        while (rset.next()) {
                                            String data = rset.getInt("purchases_employee.id") + "/" + rset.getString("employees.username") + "/" + rset.getString("wines.name") + "/" + rset.getString("purchases_employee.date") + "/" + rset.getFloat("purchases_employee.value") + "/" + rset.getInt("purchases_employee.quantity");
                                            out.println(data);
                                        }
                                        out.println("null");
                                    } else {

                                        strselect = "SELECT purchases_employee.id, employees.username, wines.name, purchases_employee.date, purchases_employee.value, purchases_employee.quantity FROM purchases_employee INNER JOIN employees ON purchases_employee.cid = employees.id INNER JOIN wines ON purchases_employee.product = wines.id";
                                        ResultSet rset = stmt.executeQuery(strselect);

                                        while (rset.next()) {
                                            String data = rset.getInt("purchases_employee.id") + "/" + rset.getString("employees.username") + "/" + rset.getString("wines.name") + "/" + rset.getString("purchases_employee.date") + "/" + rset.getFloat("purchases_employee.value") + "/" + rset.getInt("purchases_employee.quantity");
                                            out.println(data);
                                        }
                                        out.println("null");
                                    }
                                } else if (line.equals("PROPOSAL_BUY_WINES")) {

                                    String id = in.nextLine();
                                    int quantity = Integer.parseInt(in.nextLine());
                                    int cid = Integer.parseInt(in.nextLine());
                                    int contproposal = Integer.parseInt(in.nextLine());
                                    float price_tot = Float.parseFloat(in.nextLine());

                                    String date = valueOf(LocalDate.now());

                                    String insertSql = "insert into proposal2 values (?, ?, ?, ?, ?, ?)";
                                    PreparedStatement pstmt = conn.prepareStatement(insertSql);
                                    Purchase p = new Purchase(contproposal, cid, id, date, quantity, price_tot);

                                    pstmt.setInt(1, contproposal);
                                    pstmt.setInt(2, p.getCid());
                                    pstmt.setInt(3, Integer.parseInt(p.getProduct()));
                                    pstmt.setString(4, p.getDate());
                                    pstmt.setInt(5, p.getQuantity());
                                    pstmt.setDouble(6, p.getPrice());

                                    pstmt.addBatch();
                                    pstmt.executeBatch();

                                } else if (line.equals("REPORT")) {

                                    String d1 = in.nextLine();
                                    String d2 = in.nextLine();

                                    if (!d1.equals("null") && !d2.equals("null")) {

                                        String strselect = "SELECT SUM(value) FROM purchases WHERE date BETWEEN '" + d1 + "' AND '" + d2 + "'";
                                        ResultSet rset = stmt.executeQuery(strselect);
                                        rset.next();
                                        float guadagno = rset.getFloat(1);
                                        //ottenere le spese da purchase_employee
                                        strselect = "SELECT SUM(value) FROM purchases_employee WHERE date BETWEEN '" + d1 + "' AND '" + d2 + "'";
                                        rset = stmt.executeQuery(strselect);
                                        rset.next();
                                        float spese = rset.getFloat(1);
                                        //bottiglie vendute
                                        strselect = "SELECT SUM(quantity) FROM purchases WHERE date BETWEEN '" + d1 + "' AND '" + d2 + "'";
                                        rset = stmt.executeQuery(strselect);
                                        rset.next();
                                        int bottiglie_vendute = rset.getInt(1);
                                        //bottiglie rimaste in magazzino
                                        strselect = "SELECT SUM(availability) FROM wines";
                                        rset = stmt.executeQuery(strselect);
                                        rset.next();
                                        int bottiglie_rimaste = rset.getInt(1);


                                        out.println(guadagno);
                                        out.println(spese);
                                        out.println(bottiglie_vendute);
                                        out.println(bottiglie_rimaste);

                                        strselect = "SELECT wines.name, SUM(purchases.quantity) as tot FROM purchases INNER JOIN wines ON purchases.product = wines.id GROUP BY wines.name";
                                        rset = stmt.executeQuery(strselect);
                                        while (rset.next()) {

                                            String data = rset.getString("wines.name") + "/" + rset.getInt("tot");
                                            out.println(data);
                                        }
                                        out.println("null");
                                    } else if (d1.equals("null") && !d2.equals("null")) {

                                        String strselect = "SELECT SUM(value) FROM purchases WHERE date <= '" + d2 + "'";
                                        ResultSet rset = stmt.executeQuery(strselect);
                                        rset.next();
                                        float guadagno = rset.getFloat(1);
                                        //ottenere le spese da purchase_employee
                                        strselect = "SELECT SUM(value) FROM purchases_employee WHERE date <= '" + d2 + "'";
                                        rset = stmt.executeQuery(strselect);
                                        rset.next();
                                        float spese = rset.getFloat(1);
                                        //bottiglie vendute
                                        strselect = "SELECT SUM(quantity) FROM purchases WHERE date <= '" + d2 + "'";
                                        rset = stmt.executeQuery(strselect);
                                        rset.next();
                                        int bottiglie_vendute = rset.getInt(1);
                                        //bottiglie rimaste in magazzino
                                        strselect = "SELECT SUM(availability) FROM wines";
                                        rset = stmt.executeQuery(strselect);
                                        rset.next();
                                        int bottiglie_rimaste = rset.getInt(1);

                                        out.println(guadagno);
                                        out.println(spese);
                                        out.println(bottiglie_vendute);
                                        out.println(bottiglie_rimaste);

                                        strselect = "SELECT wines.name, SUM(purchases.quantity) as tot FROM purchases INNER JOIN wines ON purchases.product = wines.id GROUP BY wines.name";
                                        rset = stmt.executeQuery(strselect);
                                        while (rset.next()) {

                                            String data = rset.getString("wines.name") + "/" + rset.getInt("tot");
                                            out.println(data);
                                        }
                                        out.println("null");
                                    } else if (!d1.equals("null") && d2.equals("null")) {

                                        String strselect = "SELECT SUM(value) FROM purchases WHERE date >= '" + d1 + "'";
                                        ResultSet rset = stmt.executeQuery(strselect);
                                        rset.next();
                                        float guadagno = rset.getFloat(1);
                                        //ottenere le spese da purchase_employee
                                        strselect = "SELECT SUM(value) FROM purchases_employee WHERE date >= '" + d1 + "'";
                                        rset = stmt.executeQuery(strselect);
                                        rset.next();
                                        float spese = rset.getFloat(1);
                                        //bottiglie vendute
                                        strselect = "SELECT SUM(quantity) FROM purchases WHERE date >= '" + d1 + "'";
                                        rset = stmt.executeQuery(strselect);
                                        rset.next();
                                        int bottiglie_vendute = rset.getInt(1);
                                        //bottiglie rimaste in magazzino
                                        strselect = "SELECT SUM(availability) FROM wines ";
                                        rset = stmt.executeQuery(strselect);
                                        rset.next();
                                        int bottiglie_rimaste = rset.getInt(1);

                                        out.println(guadagno);
                                        out.println(spese);
                                        out.println(bottiglie_vendute);
                                        out.println(bottiglie_rimaste);

                                        strselect = "SELECT wines.name, SUM(purchases.quantity) as tot FROM purchases INNER JOIN wines ON purchases.product = wines.id GROUP BY wines.name";
                                        rset = stmt.executeQuery(strselect);
                                        while (rset.next()) {

                                            String data = rset.getString("wines.name") + "/" + rset.getInt("tot");
                                            out.println(data);
                                        }
                                        out.println("null");
                                    }
                                    if (d1.equals("null") && d2.equals("null")) {

                                        String strselect = "SELECT SUM(value) FROM purchases";
                                        ResultSet rset = stmt.executeQuery(strselect);
                                        rset.next();
                                        float guadagno = rset.getFloat(1);
                                        //ottenere le spese da purchase_employee
                                        strselect = "SELECT SUM(value) FROM purchases_employee";
                                        rset = stmt.executeQuery(strselect);
                                        rset.next();
                                        float spese = rset.getFloat(1);
                                        //bottiglie vendute
                                        strselect = "SELECT SUM(quantity) FROM purchases";
                                        rset = stmt.executeQuery(strselect);
                                        rset.next();
                                        int bottiglie_vendute = rset.getInt(1);
                                        //bottiglie rimaste in magazzino
                                        strselect = "SELECT SUM(availability) FROM wines";
                                        rset = stmt.executeQuery(strselect);
                                        rset.next();
                                        int bottiglie_rimaste = rset.getInt(1);

                                        out.println(guadagno);
                                        out.println(spese);
                                        out.println(bottiglie_vendute);
                                        out.println(bottiglie_rimaste);

                                        strselect = "SELECT wines.name, SUM(purchases.quantity) as tot FROM purchases INNER JOIN wines ON purchases.product = wines.id GROUP BY wines.name";
                                        rset = stmt.executeQuery(strselect);
                                        while (rset.next()) {

                                            String data = rset.getString("wines.name") + "/" + rset.getInt("tot");
                                            out.println(data);
                                        }
                                        out.println("null");
                                    }
                                } else if (line.equals("REPORT_YEAR")) {

                                    String month = in.nextLine();

                                    String strselect = "SELECT SUM(value) FROM purchases WHERE MONTH(date) = '" + month + "'";
                                    ResultSet rset = stmt.executeQuery(strselect);
                                    rset.next();
                                    float guadagno = rset.getFloat(1);

                                    strselect = "SELECT SUM(value) FROM purchases_employee WHERE MONTH(date) = '" + month + "'";
                                    rset = stmt.executeQuery(strselect);
                                    rset.next();
                                    float spese = rset.getFloat(1);

                                    out.println(guadagno);
                                    out.println(spese);
                                }
                                else if (line.equals("CHECK_DATE")) {

                                    String strselect = "SELECT * FROM notify";
                                    ResultSet rset = stmt.executeQuery(strselect);
                                    while (rset.next()) {

                                        String date = rset.getString("date_add");
                                        String[] parts = date.split("-");
                                        int anno = Integer.parseInt(parts[0]);
                                        int mese = Integer.parseInt(parts[1]);
                                        int giorno = Integer.parseInt(parts[2]);

                                        System.out.println(date + " - " + LocalDate.now());

                                        if (LocalDate.of(anno, mese, giorno).isBefore(LocalDate.now())) {
                                            String strdelete = "DELETE FROM notify WHERE date_add = '" + date + "'";
                                            stmt.executeUpdate(strdelete);
                                        }
                                    }


                                }
                                else if (line.equals("CHECK_WINE_TO_ORDER")) {

                                    int cont = in.nextInt();

                                    String strselect = "SELECT COUNT(*) FROM winetoorder";
                                    ResultSet rset = stmt.executeQuery(strselect);
                                    rset.next();
                                    int count = rset.getInt(1);

                                    if (cont == count) {
                                        out.println("FALSE");
                                    } else {
                                        out.println("TRUE");
                                    }
                                }
                                else if (line.equals("CHECK_PROPOSAL")) {

                                    int cont = in.nextInt();

                                    String strselect = "SELECT COUNT(*) FROM proposal_tmp";
                                    ResultSet rset = stmt.executeQuery(strselect);
                                    rset.next();
                                    int count = rset.getInt(1);
                                    if (cont == count) {
                                        out.println("FALSE");
                                    } else {
                                        out.println("TRUE");
                                    }
                                }
                                else if (line.equals("CHECK_NOTIFY")) {

                                    int cont = in.nextInt();

                                    String strselect = "SELECT COUNT(*) FROM notify";
                                    ResultSet rset = stmt.executeQuery(strselect);
                                    rset.next();
                                    int count = rset.getInt(1);

                                    if (cont == count) {
                                        out.println("FALSE");
                                    } else {
                                        out.println("TRUE");
                                    }
                                } else if(line.equals("GET_NOTIFY")){
                                    String strselect = "SELECT message FROM notify";
                                    ResultSet rset = stmt.executeQuery(strselect);
                                    while (rset.next()) {
                                        String data = rset.getString("message");
                                        out.println(data);
                                    }
                                    out.println("null");
                                }
                            }
                        } catch (SQLException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (IOException | SQLException e) {

        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}