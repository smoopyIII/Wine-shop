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
import java.util.ResourceBundle;



public class ResetPasswordFXController {

    @FXML
    private Button reset_password_btn;
    @FXML
    private Button alarm_btn;
    @FXML
    private Button account_btn;
    @FXML
    private Button mail_btn;
    @FXML
    private Button logout_btn;

    @FXML
    private TextField oldpassword_textfield;
    @FXML
    private TextField newPassword_textfield;
    @FXML
    private TextField repeat_textfield;
    MainFXController mfxc = new MainFXController();
    int employeeId = mfxc.employeeID;


    public void reset_password_btnIsClicked(ActionEvent actionEvent) throws IOException {

        Socket socket = mfxc.getSocket();
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String oldpassword = oldpassword_textfield.getText();
        String newpassword = newPassword_textfield.getText();
        String repeatpassword = repeat_textfield.getText();

        if (oldpassword.isEmpty()) {
            oldpassword_textfield.setStyle("-fx-prompt-text-fill: red;");
            oldpassword_textfield.setPromptText("Insert old password");
        }
        if (newpassword.isEmpty()) {
            newPassword_textfield.setStyle("-fx-prompt-text-fill: red;");
            newPassword_textfield.setPromptText("Insert new password");
        }
        if (repeatpassword.isEmpty()) {
            repeat_textfield.setStyle("-fx-prompt-text-fill: red;");
            repeat_textfield.setPromptText("Repeat new password");
        } else if (newpassword.equals(repeatpassword)) {
            out.println("CHECK_OLD_PASSWORD");
            out.println(employeeId);
            String oldpasswordServer = in.readLine();

            if (oldpassword.equals(oldpasswordServer)) {
                //verificare se la password è stata cambiata rispetto a quella precedente
                if (oldpassword.equals(newpassword)) {
                    newPassword_textfield.clear();
                    repeat_textfield.clear();
                    newPassword_textfield.setStyle("-fx-prompt-text-fill: red;");
                    newPassword_textfield.setPromptText("Password already used");
                    repeat_textfield.setStyle("-fx-prompt-text-fill: red;");
                    repeat_textfield.setPromptText("Password already used");
                } else {
                    out.println("UPDATE_PASSWORD");
                    out.println(employeeId);
                    out.println(newpassword);

                    FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("home_employee.fxml"));
                    Stage window = (Stage) account_btn.getScene().getWindow();
                    window.setScene(new Scene(fxmlLoader.load()));
                }
            } else {
                oldpassword_textfield.clear();
                oldpassword_textfield.setStyle("-fx-prompt-text-fill: red;");
                oldpassword_textfield.setPromptText("Wrong password");
            }

        } else {
            newPassword_textfield.clear();
            repeat_textfield.clear();
            newPassword_textfield.setStyle("-fx-prompt-text-fill: red;");
            newPassword_textfield.setPromptText("Password doesn't match");
            repeat_textfield.setStyle("-fx-prompt-text-fill: red;");
            repeat_textfield.setPromptText("Password doesn't match");
        }
    }
    public void accountIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("EmployeeData_page.fxml"));
        Stage window = (Stage) account_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void mailIsClicked(ActionEvent actionEvent) {

    }

    public void alarmIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("View_proposal_employee.fxml"));
        Stage window = (Stage) alarm_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));

    }
    public void logoutIsClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Login_page.fxml"));
        Stage window = (Stage) logout_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
    @FXML
    public Button home_btn;
    public void homeIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("home_employee.fxml"));
        Stage window = (Stage) home_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
}
