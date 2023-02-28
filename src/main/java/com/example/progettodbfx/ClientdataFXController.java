package com.example.progettodbfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Label;


import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientdataFXController implements Initializable {

    @FXML
    public Button Back_btn;

    @FXML
    private Label name_label;
    @FXML
    private Label surname_label;
    @FXML
    private Label fiscalcode_label;
    @FXML
    private Label email_label;
    @FXML
    private Label address_label;
    @FXML
    private Label username_label;
    @FXML
    private Label password_label;
    @FXML
    private Label phonenumber_label;

    MainFXController mfxc = new MainFXController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            Socket socket = mfxc.getSocket();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            int clientId = mfxc.clientID;

            out.println("GET_CLIENT_DATA");
            out.println(clientId);

            name_label.setText(in.readLine());
            surname_label.setText(in.readLine());
            address_label.setText(in.readLine());
            phonenumber_label.setText(in.readLine());
            fiscalcode_label.setText(in.readLine());
            email_label.setText(in.readLine());
            username_label.setText(in.readLine());
            //password_label.setText(in.readLine());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void back_btnIsClicked(ActionEvent actionEvent) throws IOException{

        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_client.fxml"));
        Stage window = (Stage) Back_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }


}
