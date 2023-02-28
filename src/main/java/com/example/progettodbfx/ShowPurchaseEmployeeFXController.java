package com.example.progettodbfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ShowPurchaseEmployeeFXController implements Initializable {

    private static final String DBURL = "jdbc:mysql://127.0.0.1:3306/wineshop?";
    private static final String ARGS = "createDatabaseIfNotExist=true&serverTimezone=UTC";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "";
    ObservableList<PurchaseNew> w = FXCollections.observableArrayList();

    @FXML
    public TableColumn<PurchaseNew, Integer> id;
    @FXML
    public TableColumn<PurchaseNew, String> client_name;
    @FXML
    public TableColumn<PurchaseNew, String> product;
    @FXML
    public TableColumn<PurchaseNew, String> date;
    @FXML
    public TableColumn<PurchaseNew, Integer> quantity;
    @FXML
    public TableColumn<PurchaseNew, Float> price;
    @FXML
    public TableView<PurchaseNew> table_view;
    @FXML
    private Label employee_name_label;
    @FXML
    private DatePicker from_date, to_date;
    @FXML
    private RadioButton sales_radio, purchase_radio;

    MainFXController mfxc = new MainFXController();
    int employeeId = mfxc.employeeID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try (Connection conn = DriverManager.getConnection(DBURL + ARGS, LOGIN, PASSWORD);
             Statement stmt = conn.createStatement();) {

            Socket socket = mfxc.getSocket();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("GET_EMPLOYEE_USERNAME");
            out.println(employeeId);
            try {
                employee_name_label.setVisible(true);
                employee_name_label.setText(in.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Button logout_btn;

    public void logoutIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Login_page.fxml"));
        Stage window = (Stage) logout_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    @FXML
    public Button alarm_btn;

    public void alarmIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("View_proposal_employee.fxml"));
        Stage window = (Stage) alarm_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    @FXML
    public Button mail_btn;
    public void mailIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Wine_to_order.fxml"));
        Stage window = (Stage) mail_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    @FXML
    public Button account_btn;

    public void accountIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("EmployeeData_page.fxml"));
        Stage window = (Stage) account_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public Button home_btn;

    public void homeIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_employee.fxml"));
        Stage window = (Stage) home_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void searchIsClicked(ActionEvent actionEvent) throws IOException {
        try (Connection conn = DriverManager.getConnection(DBURL + ARGS, LOGIN, PASSWORD);
             Statement stmt = conn.createStatement();) {
            w.clear();
            id.setCellValueFactory(new PropertyValueFactory<PurchaseNew, Integer>("id"));
            client_name.setCellValueFactory(new PropertyValueFactory<PurchaseNew, String>("cid"));
            product.setCellValueFactory(new PropertyValueFactory<PurchaseNew, String>("product"));
            date.setCellValueFactory(new PropertyValueFactory<PurchaseNew, String>("date"));
            quantity.setCellValueFactory(new PropertyValueFactory<PurchaseNew, Integer>("quantity"));
            price.setCellValueFactory(new PropertyValueFactory<PurchaseNew, Float>("val"));

            Socket socket = mfxc.getSocket();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            LocalDate d1 = from_date.getValue();
            LocalDate d2 = to_date.getValue();
            int check=0;

            if ((d1 != null) && (d2 != null)) {
                if (d1.isAfter(d2)) {
                    from_date.setStyle("-fx-prompt-text-fill: red;");
                    from_date.setPromptText("Date error");
                    check = 1;
                }
            }

            if(check==0){
                if (sales_radio.isSelected())
                {
                    out.println("SALES"); //Client buy from me
                    System.out.println("ci sono");
                    out.println(d1);
                    out.println(d2);
                } else {
                    out.println("PURCHASE"); //I buy from someone
                    out.println(d1);
                    out.println(d2);
                }

                String line;
                while (!(line = in.readLine()).equals("null")) {
                    System.out.println(line);
                    String[] lineList = line.split("/");
                    int id = Integer.parseInt(lineList[0]);
                    String cid = lineList[1];
                    String product = lineList[2];
                    String date = lineList[3];
                    float value = Float.parseFloat(lineList[4]);
                    int quantity = Integer.parseInt(lineList[5]);
                    w.add(new PurchaseNew(id, cid, date, value, quantity, product));
                }
                table_view.setItems(w);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}