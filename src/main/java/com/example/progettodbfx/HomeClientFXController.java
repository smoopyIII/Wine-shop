package com.example.progettodbfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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


public class HomeClientFXController implements Initializable {

    private static final String DBURL = "jdbc:mysql://127.0.0.1:3306/wineshop?";
    private static final String ARGS = "createDatabaseIfNotExist=true&serverTimezone=UTC";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "";
    @FXML
    public Button logout_btn;
    @FXML
    public Button wine_list_btn;
    @FXML
    public Button buy_wine_btn;
    @FXML
    public Button search_wine_btn;
    @FXML
    private Label client_name_label;
    @FXML
    private Button show_purchase_btn;
    @FXML
    private Button account_btn;
    @FXML
    private Button alert_btn;
    @FXML
    public TableColumn <Discount,String> wineColumn;
    @FXML
    public TableColumn <Discount,Integer> discountColumn;
    @FXML
    public TableView <Discount> onSaleTable;
    @FXML
    private Button closeOffer;
    @FXML
    private Label noOfferLabel;
    @FXML
    private AnchorPane funzioniAnchorPane, barAnchorPane,offerteAnchorPane;
    @FXML
    public ImageView bell;
    @FXML
    public ImageView mail;

    private int flags=0;
    String wine;
    int discount;
    ObservableList<Discount> offert = FXCollections.observableArrayList();
    public HomeClientFXController()  {
    }

    @Override
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
        int cont2 = 0;
        BufferedReader finalIn = in;
        PrintWriter finalOut = out;
        Thread t = new Thread(() -> {

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                finalOut.println("CHECK_PROPOSAL");
                finalOut.println(cont2);
                try {
                    if (finalIn.readLine().equals("TRUE")) {

                        String imagePath = "file:///" + System.getProperty("user.dir") + "/target/classes/com/example/progettodbfx/notification.png";
                        Image image = new Image(imagePath);
                        bell.setImage(image);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                finalOut.println("CHECK_NOTIFY");
                finalOut.println(cont2);
                try {
                    if (finalIn.readLine().equals("TRUE")) {

                        String imagePath = "file:///" + System.getProperty("user.dir") + "/target/classes/com/example/progettodbfx/notification.png";
                        Image image = new Image(imagePath);
                        mail.setImage(image);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
        });
        t.start();

        if(MainFXController.offertCounter==0){
            MainFXController.offertCounter++;
            funzioniAnchorPane.setDisable(true);
            barAnchorPane.setDisable(true);

            out.println("SEARCH_WINES_ON_SALE");
            String line;
            int cont=0;
            wineColumn.setCellValueFactory(new PropertyValueFactory<Discount,String>("wine"));
            discountColumn.setCellValueFactory(new PropertyValueFactory<Discount,Integer>("discount"));

            try {
                while (!(line = in.readLine()).equals("null")) {
                    String[] lineList = line.split("/");
                    cont++;
                    wine = lineList[0];
                    discount = Integer.parseInt(lineList[1]);
                    float oldValue = Float.parseFloat(lineList[2]);
                    String app = discount + "%";
                    if(discount!=0){
                        offert.add(new Discount(wine, app, Float.toString(oldValue)));
                    }
                }
                if(cont > 0) {
                    noOfferLabel.setVisible(false);
                    onSaleTable.setItems(offert);
                }else{
                    noOfferLabel.setVisible(true);
                    onSaleTable.setVisible(false);
                }
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else
        {
            offerteAnchorPane.setVisible(false);
        }

    }


    public void logoutIsClicked() throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Login_page.fxml"));
            Stage window = (Stage) logout_btn.getScene().getWindow();
            window.setScene(new Scene(fxmlLoader.load()));
    }

    public void closeOfferIsClicked() throws IOException{
        //System.out.println("Ci arrivo: "+ MainFXController.offertCounter);
        barAnchorPane.setDisable(false);
        funzioniAnchorPane.setDisable(false);

        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_client.fxml"));
        Stage window = (Stage) closeOffer.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void searchWineIsClicked( ) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Show_wine_by_search.fxml"));
        Stage window = (Stage) search_wine_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void showPurchaseIsClicked( )throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Show_purchase.fxml"));
        Stage window = (Stage) show_purchase_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
    public void wineListIsClicked( ) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Show_wine.fxml"));
        Stage window = (Stage) wine_list_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void buyWineIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Buy_wine.fxml"));
        Stage window = (Stage) buy_wine_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void accountIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("ClientData_page.fxml"));
        Stage window = (Stage) account_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    @FXML
    public Button cart_btn;
    public void mailIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Notify.fxml"));
        Stage window = (Stage) cart_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void alertIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("View_proposal.fxml"));
        Stage window = (Stage) alert_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));

    }
    @FXML
    public Button home_btn;
    public void homeIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_client.fxml"));
        Stage window = (Stage) home_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
}