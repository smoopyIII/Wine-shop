package com.example.progettodbfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class BuyWineFXController implements Initializable {

    ObservableList<Wine> w = FXCollections.observableArrayList();
    public Button btn;
    @FXML
    public TableColumn <Wine, Integer> cId;
    @FXML
    public TableColumn <Wine, String> cName;
    @FXML
    public TableColumn <Wine, String> cProducer;
    @FXML
    public TableColumn <Wine, Integer> cOrigin;
    @FXML
    public TableColumn <Wine, Integer> cYear;
    @FXML
    public TableColumn <Wine, String> cTecnicalNotes;
    @FXML
    public TableColumn <Wine, Integer>  cAvailability;
    @FXML
    public TableColumn <Wine, String> cGrape;
    @FXML
    public TableColumn <Wine, Float> cPrice;
    @FXML
    public TableView <Wine>table;
    @FXML
    private VBox vbox;
    @FXML
    public Pane proposalPane;
    @FXML
    public AnchorPane mainAnchorPane;
    @FXML
    public Button yes_btn;
    @FXML
    public Button no_btn;
    @FXML
    public Button shop_btn;
    @FXML
    private Button account_btn;
    @FXML
    private Label client_name_label;
    MainFXController mfxc = new MainFXController();
    public static List<List<Integer>> listOfList = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            cId.setCellValueFactory(new PropertyValueFactory<Wine,Integer>("id"));
            cName.setCellValueFactory(new PropertyValueFactory<Wine,String>("name"));
            cProducer.setCellValueFactory(new PropertyValueFactory<Wine,String>("producer"));
            cOrigin.setCellValueFactory(new PropertyValueFactory<Wine,Integer>("origin"));
            cYear.setCellValueFactory(new PropertyValueFactory<Wine,Integer>("year"));
            cTecnicalNotes.setCellValueFactory(new PropertyValueFactory<Wine,String>("technicalNotes"));
            cAvailability.setCellValueFactory(new PropertyValueFactory<Wine,Integer>("availability"));
            cGrape.setCellValueFactory(new PropertyValueFactory<Wine,String>("grapes"));
            cPrice.setCellValueFactory(new PropertyValueFactory<Wine,Float>("price"));

            Socket socket = mfxc.getSocket();
            int clientId = mfxc.clientID;

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("GET_CLIENT_USERNAME");
            out.println(clientId);
            try {
                client_name_label.setVisible(true);
                client_name_label.setText(in.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            out.println("SHOW_WINES");
            String line;

            while (!(line = in.readLine()).equals("null")) {
                String[] lineList = line.split("/");
                int id = Integer.parseInt(lineList[0]);
                String namewine = lineList[1];
                String producer =lineList[2];
                String origin = lineList[3];
                String year = lineList[4];
                String technicalNotes = lineList[5];
                String grapes = lineList[6];
                float price =Float.parseFloat(lineList[7]);
                int availability = Integer.parseInt(lineList[8]);
                System.out.println("Availability: " + availability);

                TextField tf = new TextField();
                tf.setMaxWidth(100.0);
                tf.setMaxHeight(24.0);
                tf.setMinWidth(100.0);
                tf.setMinHeight(24.0);
                tf.setText("0");
                tf.setId(String.valueOf(id));
                vbox.getChildren().addAll(tf);

                w.add(new Wine(id,namewine,producer,origin,(Integer.parseInt(year)),technicalNotes,grapes,price,availability));
            }
            table.setItems(w);
            shop_btn.setOnAction(actionEvent1 -> {
                try {
                    shopIsClicked();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        }catch (IOException e){
            e.printStackTrace();

        }
    }

    public void shopIsClicked() throws IOException {

        BuyWineFXController.listOfList.clear();

        ObservableList<Node> children = vbox.getChildren();

        int cont=0;
        for (Node child : children) {
            if (child instanceof TextField) {
                TextField tf = (TextField) child;
                String value = tf.getText();
                if(!value.equals("0")) {
                    cont++;
                }
            }
        }
        if (cont!=0)
        {
            int notEnought=0;
            for (Node child : children) {
                if (child instanceof TextField) {
                    TextField tf = (TextField) child;
                    String value = tf.getText();
                    String id = tf.getId();

                    Socket socket = mfxc.getSocket();
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    List<Integer> list = Arrays.asList(Integer.parseInt(id),Integer.parseInt(value));

                    listOfList.add(list);

                    out.println("CHECK_VALUE");
                    out.println(id);
                    out.println(value);


                    if (!in.readLine().equals("ENOUGHT")) {
                        notEnought = 1;
                    }
                }
            }
            if(notEnought==1)
            {
                proposalPane.setVisible(true);
                mainAnchorPane.setDisable(true);
                yes_btn.setOnAction(actionEvent1 -> {
                    try
                    {
                        yesIsClicked(vbox);
                    }
                    catch (IOException e) { }
                });
                no_btn.setOnAction(actionEvent1 -> noIsClicked());
            }
            else
            {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Payment_method.fxml"));
                    Stage window = (Stage) btn.getScene().getWindow();
                    window.setScene(new Scene(fxmlLoader.load()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public void yesIsClicked(VBox v) throws IOException {

        ObservableList<Node> children = v.getChildren();

        int clientId = mfxc.clientID;
        Socket socket = mfxc.getSocket();
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        //DEVO PASSARE DALLA PAGINA CHE CONTIENE LA CARTA
        out.println("CONT_PROPOSAL");
        int contproposal= Integer.parseInt(in.readLine());

        for (Node child : children) {
            if (child instanceof TextField) {
                TextField tf = (TextField) child;
                String value = tf.getText();
                if(!value.equals("0")) {
                    String id = tf.getId();

                    out.println("PRICE_WINE");
                    out.println(Integer.parseInt(id));         //QUANTITA'
                    float price = Float.parseFloat(in.readLine());
                    int num_casse = Integer.parseInt(value) / 12;
                    int num_bottiglie_app=  Integer.parseInt(value) % 12;
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
                        price_casse = price * 12 * num_casse;
                        price_casse = price_casse - (price_casse * 10 / 100);
                        price_casse = price_casse - (price_casse * 3 / 100);
                    }
                    if (num_cassette == 1) {
                        price_cassette = price * 6 ;
                        price_cassette = price_cassette - (price_cassette * 5 / 100);

                    }
                    if (num_cassette > 1) {
                        price_cassette = price * 6 * num_cassette;
                        price_cassette = price_cassette - (price_cassette * 5 / 100);
                        price_cassette = price_cassette - (price_cassette * 2 / 100);
                    }
                    price_bottle = price * num_bottiglie;
                    price_tot = price_casse + price_cassette + price_bottle;
                    price_tot = (float) (Math.round(price_tot * 100.0) / 100.0);

                    out.println("PROPOSAL_BUY_WINES");
                    out.println(id);            //ID VINO
                    out.println(value);         //QUANTITA'
                    out.println(clientId);
                    out.println(contproposal);
                    out.println(price_tot);

                }

            }
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_client.fxml"));
            Stage window = (Stage) btn.getScene().getWindow();
            window.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void noIsClicked(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Buy_wine.fxml"));
            Stage window = (Stage) btn.getScene().getWindow();
            window.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void logoutIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Login_page.fxml"));
        Stage window = (Stage) btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    @FXML
    public Button alarm_btn;
    public void alarmIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("View_proposal.fxml"));
        Stage window = (Stage) alarm_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    @FXML
    public Button mail_btn;
    public void mailIsClicked(ActionEvent actionEvent) throws IOException{
        //FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Notifica.fxml"));
        //Stage window = (Stage) alarm_btn.getScene().getWindow();
        //window.setScene(new Scene(fxmlLoader.load()));
    }

    public void accountIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("ClientData_page.fxml"));
        Stage window = (Stage) account_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
    public void homeIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_client.fxml"));
        Stage window = (Stage) btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

}