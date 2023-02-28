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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class SearchClientAdminFXController implements Initializable {

    private static final String DBURL = "jdbc:mysql://127.0.0.1:3306/wineshop?";
    private static final String ARGS = "createDatabaseIfNotExist=true&serverTimezone=UTC";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "";

    ObservableList<ClientNew> w = FXCollections.observableArrayList();

    @FXML
    private Button account_btn;
    @FXML
    public Button mail_btn;
    @FXML
    public TableColumn<Client, Integer> Id;
    @FXML
    public TableColumn <Client, String> Name;
    @FXML
    public TableColumn <Client, String> Surname;
    @FXML
    public TableColumn <Client, String> FiscalCode;
    @FXML
    public TableColumn <Client, String> Email;
    @FXML
    public TableColumn <Client, String> PhoneNumber;
    @FXML
    public TableColumn <Client, String> DeliveryAddress;
    @FXML
    public Button src_client_btn;
     @FXML
    public TableView<ClientNew> date_textfield;
    @FXML
    public TextField search_client_textfield;
    MainFXController mfxc = new MainFXController();
    int employeeId = mfxc.employeeID;

    public void initialize(URL url, ResourceBundle resourceBundle) {

        MainFXController mfxc = new MainFXController();

        Socket socket = null;
        try {
            socket = mfxc.getSocket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PrintWriter out = null;
        BufferedReader in = null;

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void buttonClick(ActionEvent actionEvent){

        try (Connection conn = DriverManager.getConnection(DBURL + ARGS , LOGIN, PASSWORD);
             Statement stmt = conn.createStatement();)
        {
            w.clear();
            Id.setCellValueFactory(new PropertyValueFactory<Client,Integer>("id"));
            Name.setCellValueFactory(new PropertyValueFactory<Client,String>("name"));
            Surname.setCellValueFactory(new PropertyValueFactory<Client,String>("surname"));
            FiscalCode.setCellValueFactory(new PropertyValueFactory<Client,String>("fiscalCode"));
            Email.setCellValueFactory(new PropertyValueFactory<Client,String>("email"));
            PhoneNumber.setCellValueFactory(new PropertyValueFactory<Client,String>("phoneNumber"));
            DeliveryAddress.setCellValueFactory(new PropertyValueFactory<Client,String>("deliveryAddress"));

            Socket socket = mfxc.getSocket();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            out.println("SEARCH_CLIENT");
            out.println(search_client_textfield.getText());

            String line;

            while (!(line = in.readLine()).equals("null")) {
                System.out.println(line);

                String[] lineList = line.split("/");
                int id = Integer.parseInt(lineList[0]);
                String name = lineList[1];
                String surname =lineList[2];
                String fiscalCode = lineList[3];
                String email = lineList[4];
                String phoneNumber = lineList[5];
                String deliveryAddress = lineList[6];
                w.add(new ClientNew(id, name, surname, fiscalCode, email, phoneNumber, deliveryAddress));

            }
            date_textfield.setItems(w);

        }catch (SQLException | IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public Button logout_btn;
    public void logoutIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Login_page.fxml"));
        Stage window = (Stage) logout_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    @FXML
    public Button alarm_btn;
    public void alarmIsClicked(ActionEvent actionEvent) throws IOException{

    }
    public void mailIsClicked(ActionEvent actionEvent) throws IOException{

    }

    public void accountIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("AdminData.fxml"));
        Stage window = (Stage) account_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
    public Button home_btn;
    public void homeIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_admin.fxml"));
        Stage window = (Stage) home_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
}
