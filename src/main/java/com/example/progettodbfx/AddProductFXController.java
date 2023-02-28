package com.example.progettodbfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddProductFXController {
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
    public TextField name_textfield;
    @FXML
    public TextField tecnicalnotes_textfield;
    @FXML
    public TextField origin_textfield;
    @FXML
    public TextField grape_textfield;
    @FXML
    public TextField producer_textfield;
    @FXML
    public TextField price_textfield;
    @FXML
    public TextField year_textfield;
    @FXML
    public TextField min_textfield;
    @FXML
    public Button AddProduct_btn;
    @FXML
    public Label productexist;


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
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("AdminData.fxml"));
        Stage window = (Stage) account_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void logoutIsClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Login_page.fxml"));
        Stage window = (Stage) logout_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void mailIsClicked() throws IOException {

    }

    public void AddProductIsClicked() throws IOException {
        MainFXController mfxc = new MainFXController();

        Socket socket = mfxc.getSocket();

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String name = name_textfield.getText();
        String tecnicalnotes = tecnicalnotes_textfield.getText();
        String origin = origin_textfield.getText();
        String grape = grape_textfield.getText();
        String producer = producer_textfield.getText();
        String price = price_textfield.getText();
        String year = year_textfield.getText();
        String min = min_textfield.getText();

        boolean inputValid = true;

        if (name.isEmpty()) {
            name_textfield.setStyle("-fx-prompt-text-fill: red;");
            name_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (tecnicalnotes.isEmpty()) {
            tecnicalnotes_textfield.setStyle("-fx-prompt-text-fill: red;");
            tecnicalnotes_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (origin.isEmpty()) {
            origin_textfield.setStyle("-fx-prompt-text-fill: red;");
            origin_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (grape.isEmpty()) {
            grape_textfield.setStyle("-fx-prompt-text-fill: red;");
            grape_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (producer.isEmpty()) {
            producer_textfield.setStyle("-fx-prompt-text-fill: red;");
            producer_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (price.isEmpty()) {
            price_textfield.setStyle("-fx-prompt-text-fill: red;");
            price_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (year.isEmpty()) {
            year_textfield.setStyle("-fx-prompt-text-fill: red;");
            year_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (min.isEmpty()) {
            min_textfield.setStyle("-fx-prompt-text-fill: red;");
            min_textfield.setPromptText("Missing data");
            inputValid = false;
        }

        if (inputValid) {
            out.println("ADD_PRODUCT");
            out.println(name);
            out.println(producer);
            out.println(origin);
            out.println(year);
            out.println(tecnicalnotes);
            out.println(grape);
            out.println(price);
            out.println(min);
            out.println(min);

            String response = in.readLine();
            if (response.equals("ERROR")) {
                productexist.setVisible(true);
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_admin.fxml"));
                Stage window = (Stage) AddProduct_btn.getScene().getWindow();
                window.setScene(new Scene(fxmlLoader.load()));
            }

        }
    }
}