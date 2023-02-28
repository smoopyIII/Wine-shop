package com.example.progettodbfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class CreditcardProposalFXController implements Initializable {

    @FXML
    public Button logout_btn;

    @FXML
    public Button home_btn;
    private VBox vbox;
    @FXML
    private DatePicker date_picker;
    @FXML
    private TextField number_textfield;
    @FXML
    private TextField name_textfield;
    @FXML
    private TextField CVV_textfield;
    @FXML
    public Button buy_wine_btn;
    @FXML
    private Button account_btn;

    @FXML
    public Button buy_creditcard_btn;
    @FXML
    private Label client_name_label;

    MainFXController mfxc = new MainFXController();

    public void initialize(URL url, ResourceBundle resourceBundle) {

        MainFXController mfxc = new MainFXController();
        int clientId = mfxc.clientID;

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

        out.println("GET_CLIENT_USERNAME");
        out.println(clientId);
        try {
            client_name_label.setVisible(true);
            client_name_label.setText(in.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logoutIsClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Login_page.fxml"));
        Stage window = (Stage) logout_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void homeIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_client.fxml"));
        Stage window = (Stage) home_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void alarmIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("View_proposal.fxml"));
        Stage window = (Stage) buy_wine_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void accountIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("ClientData_page.fxml"));
        Stage window = (Stage) account_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
    @FXML
    public Button mail_btn;
    public void mailIsClicked(ActionEvent actionEvent) throws IOException{
        //FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Notifica.fxml"));
        //Stage window = (Stage) mail_btn.getScene().getWindow();
        //window.setScene(new Scene(fxmlLoader.load()));

    }

    public void buy_creditcard_btnIsClicked() throws IOException {

        LocalDate dp = date_picker.getValue();
        String number = number_textfield.getText();
        String name = name_textfield.getText();
        String CVV = CVV_textfield.getText();

        boolean inputValid = true;

        if (name.isEmpty()) {
            name_textfield.setStyle("-fx-prompt-text-fill: red;");
            name_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (number.isEmpty()) {
            number_textfield.setStyle("-fx-prompt-text-fill: red;");
            number_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (CVV.isEmpty()) {
            CVV_textfield.setStyle("-fx-prompt-text-fill: red;");
            CVV_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (date_picker.getValue() == null) {
            date_picker.setStyle("-fx-prompt-text-fill: red;");
            date_picker.setPromptText("Missing data");
            inputValid = false;
        }
        else if (dp.isBefore(LocalDate.now())) {
            date_picker.setStyle("-fx-prompt-text-fill: red;");
            date_picker.setPromptText("Missing data");
            inputValid = false;
        }

        if (CVV.length() != 3) {
            inputValid = false;
            CVV_textfield.setStyle("-fx-prompt-text-fill: red;");
            CVV_textfield.setPromptText("Invalid CVV");

        }

        if (inputValid) {

            int clientId = mfxc.clientID;
            Socket socket = mfxc.getSocket();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("CONT_PURCHASES");
            int contpurchase = Integer.parseInt(in.readLine());

            for (List<Integer> innerList : ViewProposalFXController.listOfList) {
                if (innerList.get(1) != 0) {
                    out.println("PRICE_WINE");
                    out.println(innerList.get(0));         //QUANTITA'
                    float price = Float.parseFloat(in.readLine());

                    int num_casse = innerList.get(1) / 12;
                    int num_bottiglie_app= innerList.get(1) % 12;
                    int num_cassette = num_bottiglie_app / 6;
                    int num_bottiglie = num_bottiglie_app % 6;

                    float price_casse = 0;
                    float price_cassette = 0;
                    float price_bottle = 0;
                    float price_tot=0;

                    if (num_casse == 1) {
                        price_casse = price * 12;
                        price_casse = price_casse - (price_casse * 10 / 100);
                    }
                    if (num_casse > 1) {
                        price_casse = price * 12 *num_casse;
                        price_casse = price_casse - (price_casse * 10 / 100);
                        price_casse = price_casse - (price_casse * 3 / 100);
                    }
                    if (num_cassette == 1) {
                        price_cassette = price * 6;
                        price_cassette = price_cassette - (price_cassette * 5 / 100);
                    }
                    if (num_cassette > 1) {
                        price_cassette = price * 6 * num_cassette;
                        price_cassette = price_cassette - (price_cassette * 5 / 100);
                        price_cassette = price_cassette - (price_cassette * 2 / 100);
                    }
                    price_bottle = price * num_bottiglie;
                    price_tot = price_casse + price_cassette + price_bottle;

                    out.println("BUY_WINES");
                    out.println(innerList.get(0));
                    out.println(innerList.get(1));
                    out.println(clientId);
                    out.println(contpurchase);
                    out.println(price_tot);
                }


            }
            out.println("DELETE_PROPOSAL_TMP");
            out.println(ViewProposalFXController.listOfList.get(0).get(2));

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_client.fxml"));
                Stage window = (Stage) buy_creditcard_btn.getScene().getWindow();
                window.setScene(new Scene(fxmlLoader.load()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
