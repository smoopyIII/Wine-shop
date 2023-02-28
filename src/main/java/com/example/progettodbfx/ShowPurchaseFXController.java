package com.example.progettodbfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

public class ShowPurchaseFXController implements Initializable {

    private static final String DBURL = "jdbc:mysql://127.0.0.1:3306/wineshop?";
    private static final String ARGS = "createDatabaseIfNotExist=true&serverTimezone=UTC";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "";
    ObservableList<PurchaseNew> w = FXCollections.observableArrayList();

    @FXML
    public TableColumn <PurchaseNew, Integer> cId;
    @FXML
    public TableColumn <PurchaseNew, String> cCid;
    @FXML
    public TableColumn <Purchase, String> cProduct;
    @FXML
    public TableColumn <PurchaseNew, String> cDate;
    @FXML
    public TableColumn <PurchaseNew, Double> cValue;
    @FXML
    public TableColumn <Purchase, Double> cPrice;
    @FXML
    public TableColumn <PurchaseNew, Integer> cQuantity;
    @FXML
    public TableColumn <Purchase, Integer> cDiscount;
    @FXML
    public TableColumn <Purchase, Integer> cAdditionalvalue;
    @FXML
    public TableColumn <Purchase, Double> cAppvalue;
    @FXML
    public TableColumn <Purchase, Double> cFinalvalue;
    @FXML
    public TableView <PurchaseNew> table;
    @FXML
    private Button account_btn;
    @FXML
    private Label client_name_label;

    MainFXController mfxc = new MainFXController();
    int clientId = mfxc.clientID;
    String clientusername;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        try (Connection conn = DriverManager.getConnection(DBURL + ARGS , LOGIN, PASSWORD);
             Statement stmt = conn.createStatement();)
        {
            cId.setCellValueFactory(new PropertyValueFactory<PurchaseNew,Integer>("id"));
            cCid.setCellValueFactory(new PropertyValueFactory<PurchaseNew,String>("cid"));
            cDate.setCellValueFactory(new PropertyValueFactory<PurchaseNew,String>("date"));
            cValue.setCellValueFactory(new PropertyValueFactory<PurchaseNew,Double>("val"));
            cQuantity.setCellValueFactory(new PropertyValueFactory<PurchaseNew,Integer>("quantity"));

            Socket socket = mfxc.getSocket();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("GET_CLIENT_USERNAME");
            out.println(clientId);
            try {
                client_name_label.setVisible(true);
                clientusername=in.readLine();
                client_name_label.setText(clientusername);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            out.println("SEARCH_PURCHASES");
            out.println(MainFXController.clientID);
            String line;

            while (!(line = in.readLine()).equals("null")) {
                System.out.println(line);
                if (line.equals("NO_RESULT")){

                }else{
                    String[] lineList = line.split("/");
                    int id = Integer.parseInt(lineList[0]);
                    //String cid = lineList[1];
                    String product = lineList[1];
                    String date = lineList[2];
                    float value= Float.parseFloat(lineList[3]);
                    int quantity = Integer.parseInt(lineList[4]);


                    w.add(new PurchaseNew(id,clientusername,date,value,quantity,product) );
                }
            }
            if ( !(line.equals("NO_RESULT")) )
                table.setItems(w);

        }catch (SQLException e){
            e.printStackTrace();
        }catch (IOException i){
            System.out.println(i);
        }
    }

    public Button logout_btn;
    public void logoutIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Login_page.fxml"));
        Stage window = (Stage) logout_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    @FXML
    public Button alarm_btn;
    public void alarmIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("View_proposal.fxml"));
        Stage window = (Stage) alarm_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void mailIsClicked(ActionEvent actionEvent) {
        //FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Notifica.fxml"));
        //Stage window = (Stage) mail_btn.getScene().getWindow();
        //window.setScene(new Scene(fxmlLoader.load()));
    }

    public void accountIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("ClientData_page.fxml"));
        Stage window = (Stage) account_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
    public Button home_btn;
    public void homeIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_client.fxml"));
        Stage window = (Stage) home_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
}