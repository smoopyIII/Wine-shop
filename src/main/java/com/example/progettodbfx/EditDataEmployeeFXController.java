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

public class EditDataEmployeeFXController implements Initializable {
    private static final String DBURL = "jdbc:mysql://127.0.0.1:3306/wineshop?";
    private static final String ARGS = "createDatabaseIfNotExist=true&serverTimezone=UTC";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "";

    @FXML
    private Button account_btn;
    @FXML
    public Button logout_btn;
    @FXML
    public Button home_btn;
    @FXML
    public Button alarm_btn;
    @FXML
    public Button mail_btn;
    @FXML
    private TextField name_textfield;
    @FXML
    private TextField surname_textfield;
    @FXML
    private TextField fiscalcode_textfield;
    @FXML
    private TextField email_textfield;
    @FXML
    private TextField address_textfield;
    @FXML
    private TextField phonenumber_textfield;
    @FXML
    private TextField username_textfield;
    @FXML
    private TextField password_textfield;
    @FXML
    private Button edit_btn;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        String username=EditEmployeeFXController.usernameSelected;
        MainFXController mfxc = new MainFXController();

        Socket socket = null;
        try {
            socket = mfxc.getSocket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PrintWriter out = null;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        out.println("SEARCH_EMPLOYEE_BY_USERNAME");
        out.println(username);
        String line;

        while (true) {
            try {
                if (!!(line = in.readLine()).equals("null")) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(line);

            String[] lineList = line.split("/");
            int id = Integer.parseInt(lineList[0]);
            String name = lineList[1];
            String surname =lineList[2];
            String fiscalCode = lineList[3];
            String email = lineList[4];
            String phoneNumber = lineList[5];
            String address = lineList[6];
            username = lineList[7];
            String password = lineList[8];

            name_textfield.setText(name);
            surname_textfield.setText(surname);
            fiscalcode_textfield.setText(fiscalCode);
            email_textfield.setText(email);
            phonenumber_textfield.setText(phoneNumber);
            address_textfield.setText(address);
            username_textfield.setText(username);
            password_textfield.setText(password);

        }
    }

    public void alarmIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("View_proposal.fxml"));
        Stage window = (Stage) alarm_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void homeIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_admin.fxml"));
        Stage window = (Stage) home_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void accountIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Admin_data_page.fxml"));
        Stage window = (Stage) account_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void logoutIsClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Login_page.fxml"));
        Stage window = (Stage) logout_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
    public void mailIsClicked() throws IOException
    {

    }
    public void EditDataIsClicked() throws IOException
    {
        String name = name_textfield.getText();
        String surname = surname_textfield.getText();
        String fiscalCode = fiscalcode_textfield.getText();
        String email = email_textfield.getText();
        String phoneNumber = phonenumber_textfield.getText();
        String address = address_textfield.getText();
        String username = username_textfield.getText();
        String password = password_textfield.getText();
        int controllo=0;

        if(name.equals("")){
            name_textfield.setStyle("-fx-prompt-text-fill: red;");
            name_textfield.setText("");
            name_textfield.setPromptText("Name can't be empty");
            controllo = 1;
        }
        if(surname.equals("")){
            surname_textfield.setStyle("-fx-prompt-text-fill: red;");
            surname_textfield.setText("");
            surname_textfield.setPromptText("Surname can't be empty");
            controllo = 1;
        }
        if(fiscalCode.equals("")){
            fiscalcode_textfield.setStyle("-fx-prompt-text-fill: red;");
            fiscalcode_textfield.setText("");
            fiscalcode_textfield.setPromptText("Fiscal code can't be empty");
            controllo = 1;
        }
        if(email.equals("")){
            email_textfield.setStyle("-fx-prompt-text-fill: red;");
            email_textfield.setText("");
            email_textfield.setPromptText("Email can't be empty");
            controllo = 1;
        }
        if(phoneNumber.equals("")){
            phonenumber_textfield.setStyle("-fx-prompt-text-fill: red;");
            phonenumber_textfield.setText("");
            phonenumber_textfield.setPromptText("Phone number can't be empty");
            controllo = 1;
        }
        if(address.equals("")){
            address_textfield.setStyle("-fx-prompt-text-fill: red;");
            address_textfield.setText("");
            address_textfield.setPromptText("Address can't be empty");
            controllo = 1;
        }
        if(username.equals("")){
            username_textfield.setStyle("-fx-prompt-text-fill: red;");
            username_textfield.setText("");
            username_textfield.setPromptText("Username can't be empty");
            controllo = 1;
        }
        if(password.equals("")){
            password_textfield.setStyle("-fx-prompt-text-fill: red;");
            password_textfield.setText("");
            password_textfield.setPromptText("Password can't be empty");
            controllo = 1;
        }

        if(controllo == 0)
        {
            MainFXController mfxc = new MainFXController();
            Socket socket = mfxc.getSocket();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in= new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("EDIT_EMPLOYEE");
            out.println(name);
            out.println(surname);
            out.println(fiscalCode);
            out.println(email);
            out.println(phoneNumber);
            out.println(address);
            out.println(username);
            out.println(password);
            out.println(EditEmployeeFXController.usernameSelected);

            if(in.readLine().equals("null")){
                username_textfield.setStyle("-fx-prompt-text-fill: red;");

                username_textfield.setText("");
                username_textfield.setPromptText("Username already used");
                System.out.println("sono qui");
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_admin.fxml"));
                Stage window = (Stage) edit_btn.getScene().getWindow();
                window.setScene(new Scene(fxmlLoader.load()));
            }
        }
    }
}
