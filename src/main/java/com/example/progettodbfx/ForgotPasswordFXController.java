package com.example.progettodbfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ForgotPasswordFXController {

    @FXML
    public Button show_password_btn;
    @FXML
    private TextField username_textfield;
    @FXML
    private TextField phone_number_textfield;
    @FXML
    private Label showpassword_text;
    @FXML
    private Label showpassword_text1;
    @FXML
    private Hyperlink sign_in_link;

    MainFXController mfxc = new MainFXController();



    public void show_password_btnIsClicked() throws Exception {
        String username = username_textfield.getText();
        String phone_number = phone_number_textfield.getText();

        Socket socket = mfxc.getSocket();
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.println("RESET_PASSWORD");
        out.println(username);
        out.println(phone_number);

        String line;
        showpassword_text1.setVisible(false);

        if((line = in.readLine()).equals("null"))
        {
            showpassword_text.setText("No user found");
        }
        else
        {
            showpassword_text1.setVisible(true);
            showpassword_text.setText(line);
        }

    }

    public void signInClicked() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Login_page.fxml"));
        Stage window = (Stage) sign_in_link.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
}
