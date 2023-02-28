package com.example.progettodbfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TransferProposalFXController implements Initializable {
    @FXML
    public Button logout_btn;
    @FXML
    public Button home_btn;
    @FXML
    private Button account_btn;
    @FXML
    private TextField Name_textfield;
    @FXML
    private TextField Surname_textfield;
    @FXML
    private TextField Beneficiary_name_textfield;
    @FXML
    private TextField beneficiary_surname_textfield;
    @FXML
    private TextField IBAN_textfield;
    @FXML
    private Label Value_label;

    @FXML
    public Button buy_wine_btn;


    @FXML
    public Button buy_creditcard_btn;
    MainFXController mfxc = new MainFXController();

    int clientId = mfxc.clientID;
    @FXML
    private Label client_name_label;

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

        for (List<Integer> innerList2 : ViewProposalFXController.listOfList) {
            float price_tot = 0;
            if (innerList2.get(1) != 0) {
                out.println("PRICE_WINE");
                out.println(innerList2.get(0));         //QUANTITA'
                float price = 0;
                try {
                    price = Float.parseFloat(in.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                int num_casse = innerList2.get(1) / 12;
                int num_bottiglie_app = innerList2.get(1) % 12;
                int num_cassette = num_bottiglie_app / 6;
                int num_bottiglie = num_bottiglie_app % 6;

                float price_casse = 0;
                float price_cassette = 0;

                if (num_casse == 1) {
                    price_casse = price * 12;
                    price_casse = price_casse - (price_casse * 10 / 100);

                }
                if (num_casse > 1) {
                    price_casse = price * 12 * num_casse;
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
                float price_bottle = price * num_bottiglie;
                price_tot = price_casse + price_cassette + price_bottle;
                Value_label.setText(String.valueOf(price_tot));
                System.out.println("sono qui: " + price_tot);
            }
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

    public void alertIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("View_proposal.fxml"));
        Stage window = (Stage) buy_wine_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void accountIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("ClientData_page.fxml"));
        Stage window = (Stage) account_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void cartIsClicked(ActionEvent actionEvent) {
    }

    public void buyIsClicked() throws IOException {

        String name = Name_textfield.getText();
        String surname = Surname_textfield.getText();
        String beneficiary_name = Beneficiary_name_textfield.getText();
        String beneficiary_surname = beneficiary_surname_textfield.getText();
        String iban = IBAN_textfield.getText();

        int clientId = mfxc.clientID;
        Socket socket = mfxc.getSocket();
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        boolean inputValid = true;
        if (name.isEmpty()) {
            Name_textfield.setStyle("-fx-prompt-text-fill: red;");
            Name_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (surname.isEmpty()) {
            Surname_textfield.setStyle("-fx-prompt-text-fill: red;");
            Surname_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (beneficiary_name.isEmpty()) {
            Beneficiary_name_textfield.setStyle("-fx-prompt-text-fill: red;");
            Beneficiary_name_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (beneficiary_surname.isEmpty()) {
            beneficiary_surname_textfield.setStyle("-fx-prompt-text-fill: red;");
            beneficiary_surname_textfield.setPromptText("Missing data");
            inputValid = false;
        }
        if (iban.isEmpty()) {
            IBAN_textfield.setStyle("-fx-prompt-text-fill: red;");
            IBAN_textfield.setPromptText("Missing data");
            inputValid = false;
        }

        if (inputValid) {

            out.println("CONT_PURCHASES");
            int contpurchase = Integer.parseInt(in.readLine());


            for (List<Integer> innerList : ViewProposalFXController.listOfList) {
                if (innerList.get(1) != 0) {

                    out.println("PRICE_WINE");
                    out.println(innerList.get(0));         //QUANTITA'
                    float price = Float.parseFloat(in.readLine());

                    int num_casse = innerList.get(1) / 12;
                    int num_bottiglie_app = innerList.get(1) % 12;
                    int num_cassette = num_bottiglie_app / 6;
                    int num_bottiglie = num_bottiglie_app % 6;

                    float price_casse = 0;
                    float price_cassette = 0;
                    float price_bottle = 0;
                    float price_tot = 0;

                    if (num_casse == 1) {
                        price_casse = price * 12;
                        price_casse = price_casse - (price_casse * 10 / 100);

                    }
                    if (num_casse > 1) {
                        price_casse = price * 12 * num_casse;
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
                Stage window = (Stage) buy_wine_btn.getScene().getWindow();
                window.setScene(new Scene(fxmlLoader.load()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

