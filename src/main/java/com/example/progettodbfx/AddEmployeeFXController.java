package com.example.progettodbfx;

import com.example.progettodbfx.MainFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AddEmployeeFXController {
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
    public Button signup_btn;
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
    private TextField repeat_textfield;

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
    MainFXController mfxc = new MainFXController();


    public void signUpIsClicked(ActionEvent actionEvent) throws IOException{

        String name = name_textfield.getText();
        String surname = surname_textfield.getText();
        String fiscalcode = fiscalcode_textfield.getText();
        String email = email_textfield.getText();
        String address = address_textfield.getText();
        String phonenumber = phonenumber_textfield.getText();
        String username = username_textfield.getText();
        String password = password_textfield.getText();
        String repeatpassword = repeat_textfield.getText();

        Socket socket = mfxc.getSocket();
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        boolean inputValid = true;

        if (name.isEmpty()) {
            name_textfield.setText("");
            name_textfield.setStyle("-fx-prompt-text-fill: red;");
            name_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (surname.isEmpty()) {
            surname_textfield.setText("");
            surname_textfield.setStyle("-fx-prompt-text-fill: red;");
            surname_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (fiscalcode.isEmpty()) {
            fiscalcode_textfield.setText("");
            fiscalcode_textfield.setStyle("-fx-prompt-text-fill: red;");
            fiscalcode_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (email.isEmpty()) {
            email_textfield.setText("");
            email_textfield.setStyle("-fx-prompt-text-fill: red;");
            email_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (address.isEmpty()) {
            address_textfield.setText("");
            address_textfield.setStyle("-fx-prompt-text-fill: red;");
            address_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (phonenumber.isEmpty()) {
            phonenumber_textfield.setText("");
            phonenumber_textfield.setStyle("-fx-prompt-text-fill: red;");
            phonenumber_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (username.isEmpty()) {
            username_textfield.setText("");
            username_textfield.setStyle("-fx-prompt-text-fill: red;");
            username_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (password.isEmpty()) {
            password_textfield.setText("");
            password_textfield.setStyle("-fx-prompt-text-fill: red;");
            password_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (repeatpassword.isEmpty()) {
            repeat_textfield.setText("");
            repeat_textfield.setStyle("-fx-prompt-text-fill: red;");
            repeat_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (!password.equals(repeatpassword)) {
            password_textfield.setText("");
            password_textfield.setStyle("-fx-prompt-text-fill: red;");
            password_textfield.setPromptText("Password doesn't match");

            repeat_textfield.setText("");
            repeat_textfield.setStyle("-fx-prompt-text-fill: red;");
            repeat_textfield.setPromptText("Password doesn't match");
            inputValid = false;
        }

        if (inputValid) {
            out.println("SEARCH_ADMIN_USERNAME");
            out.println(username);

            String line;

            if((line = in.readLine()).equals("null"))
            {
                if(password.equals(repeatpassword))
                {
                    out.println("SIGNUP_ADMIN");
                    out.println(name);
                    out.println(surname);
                    out.println(fiscalcode);
                    out.println(email);
                    out.println(address);
                    out.println(phonenumber);
                    out.println(username);
                    out.println(password);
                }
                FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_admin.fxml"));
                Stage window = (Stage) signup_btn.getScene().getWindow();
                window.setScene(new Scene(fxmlLoader.load()));
            }
            else
            {
                username_textfield.setText("");
                username_textfield.setStyle("-fx-prompt-text-fill: red;");
                username_textfield.setPromptText("Username alredy used");
            }

        }
    }
}
