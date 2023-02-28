package com.example.progettodbfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SignUpFXController {

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
    private TextField repeatpassword_textfield;
    @FXML
    private Hyperlink sign_in_link;

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
        String repeatpassword = repeatpassword_textfield.getText();

        Socket socket = mfxc.getSocket();
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        boolean inputValid = true;

        if (name.isEmpty()) {
            name_textfield.setStyle("-fx-prompt-text-fill: red;");
            name_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (surname.isEmpty()) {
            surname_textfield.setStyle("-fx-prompt-text-fill: red;");
            surname_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (fiscalcode.isEmpty()) {
            fiscalcode_textfield.setStyle("-fx-prompt-text-fill: red;");
            fiscalcode_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (email.isEmpty()) {
            email_textfield.setStyle("-fx-prompt-text-fill: red;");
            email_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (address.isEmpty()) {
            address_textfield.setStyle("-fx-prompt-text-fill: red;");
            address_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (phonenumber.isEmpty()) {
            phonenumber_textfield.setStyle("-fx-prompt-text-fill: red;");
            phonenumber_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (username.isEmpty()) {
            username_textfield.setStyle("-fx-prompt-text-fill: red;");
            username_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (password.isEmpty()) {
            password_textfield.setStyle("-fx-prompt-text-fill: red;");
            password_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (repeatpassword.isEmpty()) {
            repeatpassword_textfield.setStyle("-fx-prompt-text-fill: red;");
            repeatpassword_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (!password.equals(repeatpassword)) {
            password_textfield.setStyle("-fx-prompt-text-fill: red;");
            password_textfield.setPromptText("Password doesn't match");

            repeatpassword_textfield.setStyle("-fx-prompt-text-fill: red;");
            repeatpassword_textfield.setPromptText("Password doesn't match");
            inputValid = false;
        }

        if (inputValid) {
            out.println("SEARCH_USERNAME");
            out.println(username);

            String line;

            if((line = in.readLine()).equals("null"))
            {
                if(password.equals(repeatpassword))
                {
                    out.println("SIGNUP");
                    out.println(name);
                    out.println(surname);
                    out.println(fiscalcode);
                    out.println(email);
                    out.println(address);
                    out.println(phonenumber);
                    out.println(username);
                    out.println(password);
                }
            }
            else
            {
                System.out.println("Username già esistente");
            }
        }
    }
    public void signInClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Login_page.fxml"));
        Stage window = (Stage) sign_in_link.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
}
