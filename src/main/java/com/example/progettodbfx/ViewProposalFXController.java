package com.example.progettodbfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ViewProposalFXController implements Initializable {

    private static final String DBURL = "jdbc:mysql://127.0.0.1:3306/wineshop?";
    private static final String ARGS = "createDatabaseIfNotExist=true&serverTimezone=UTC";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "";
    @FXML
    public TabPane mainTabPane;
    @FXML
    public Tab tab1;
    @FXML
    private Label client_name_label;
    @FXML
    public AnchorPane anchor1;
    ObservableList<Proposal> w = FXCollections.observableArrayList();

    MainFXController mfxc = new MainFXController();
    VBox v1,v2,v3,v4;
    Tab tab;
    int oldCode=0;
    int flag=0;
    int posv1=60;
    int posv2 =265;
    int posv3 = 470;
    int posv4 = 675;
    float price=0;
    public static List<List<Integer>> listOfList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        try (Connection conn = DriverManager.getConnection(DBURL + ARGS , LOGIN, PASSWORD);
             Statement stmt = conn.createStatement();)
        {
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

            out.println("SHOW_PROPOSAL");
            out.println(MainFXController.clientID);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            Pane pane = null;

            ArrayList<Tab> tabs = new ArrayList<>();
            while (!(line = in.readLine()).equals("null")) {
                System.out.println(line);
                if (line.equals("NO_PROPOSALS")){
                    System.out.println("No results");
                    flag=100;
                }else{
                    String[] lineList = line.split("/");

                    int code = Integer.parseInt(lineList[0]);
                    String winename = lineList[1];
                    String producer = lineList[2];
                    int quantity= Integer.parseInt(lineList[3]);
                    float value = Float.parseFloat(lineList[4]);
                    String clientname = lineList[5];

                    if((code!=oldCode) && (flag!=0)){
                        Label l4 = new Label();
                        l4.setText(Float.toString(price) + " $");
                        setLabel(l4);
                        v4.getChildren().add(l4);

                        pane.getChildren().addAll(v1,v2,v3,v4);
                        tab.setContent(pane);
                        tabs.add(tab);
                        price = 0;
                    }

                    price = price + value;

                    System.out.println(code + " " + oldCode);

                    if(code != oldCode){
                        tab = new Tab();
                        tab.setText("Code: " + code);
                        pane = new Pane();
                        setPane(pane);

                        v1 = new VBox();
                        setVboxPosition(v1,posv1);
                        v2 = new VBox();
                        setVboxPosition(v2,posv2);
                        v3 = new VBox();
                        setVboxPosition(v3,posv3);
                        v4 = new VBox();
                        setVboxPosition(v4,posv4);

                        Button yesBtn = new Button();
                        setBtn(yesBtn,310,"Accept",code);
                        Button noBtn = new Button();
                        setBtn(noBtn,460,"Decline",code);
                        pane.getChildren().addAll(yesBtn,noBtn);
                        oldCode = code;
                        flag++;
                    }

                    Label l1 = new Label();
                    l1.setText(winename);
                    setLabel(l1);
                    v1.getChildren().add(l1);

                    Label l2 = new Label();
                    l2.setText(producer);
                    setLabel(l2);
                    v2.getChildren().add(l2);

                    Label l3 = new Label();
                    l3.setText(Integer.toString(quantity));
                    setLabel(l3);
                    v3.getChildren().add(l3);
                }
            }

            if(flag!=100){
                Label l4 = new Label();
                l4.setText(Float.toString(price) + " $");
                setLabel(l4);
                v4.getChildren().add(l4);

                pane.getChildren().addAll(v1,v2,v3,v4);
                tab.setContent(pane);
                tabs.add(tab);
                mainTabPane.getTabs().addAll(tabs);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }catch (IOException i){
            System.out.println(i);
        }
    }

    public void setLabel(Label l){
        l.setStyle("-fx-font-size: 15;");
        l.setPadding(new Insets(0,0,0,10));
        l.setPrefHeight(30);
    }
    public void setPane(Pane pane){
        pane.setLayoutX(0);
        pane.setLayoutY(0);
        pane.setPrefHeight(380);
        pane.setPrefWidth(900);
    }

    public void setBtn(Button b, int valX, String s, int id){
        b.setId(Integer.toString(id));
        b.setLayoutX(valX);
        b.setLayoutY(270);
        b.setText(s);
        b.setPrefWidth(125);
        b.setPrefHeight(50);
        if(s.equals("Accept"))
            b.setOnAction(actionEvent -> {
                try {
                    acceptIsClicked(id,b);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        else
            b.setOnAction(actionEvent -> {
                try {
                    declineIsClicked(id,b);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    public void setVboxPosition(VBox v, int valX){
        v.setStyle("-fx-border-color: black;");
        if(valX==posv4){

        }else{
            v.setPrefHeight(60);
            v.setPrefWidth(170);
        }

        v.setLayoutY(25);
        v.setLayoutX(valX);

        Label lbl = new Label();
        if(valX==posv1)
            lbl.setText("WINE");
        else if(valX == posv2)
            lbl.setText("PRODUCER");
        else if(valX == posv3)
            lbl.setText("QUANTITY");
        else
            lbl.setText("PRICE");
        lbl.setStyle("-fx-font-weight: bold; -fx-background-color: #A2B6C4; -fx-font-size: 18; -fx-padding: 0 0 0 10");
        lbl.setPrefWidth(170);
        lbl.setPrefHeight(30);
        v.getChildren().add(lbl);
    }

    public Button logout_btn;
    public void logoutIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Login_page.fxml"));
        Stage window = (Stage) logout_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
    @FXML
    public Button alarm_btn;
    public void alarmIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("View_proposal.fxml"));
        Stage window = (Stage) alarm_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    @FXML
    public Button mail_btn;
    public void mailIsClicked(ActionEvent actionEvent) throws IOException{
        //FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource(".fxml"));
        //Stage window = (Stage) mail_btn.getScene().getWindow();
        //window.setScene(new Scene(fxmlLoader.load()));
    }
    @FXML
    public Button account_btn;
    public void accountIsClicked(ActionEvent actionEvent) throws IOException{
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

    public void acceptIsClicked(int id ,Button b) throws IOException {
        listOfList.clear();
        Socket socket = mfxc.getSocket();

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        out.println("ACCEPT_PROPOSAL_CLIENT");
        out.println(id);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String line;

        while (!(line = in.readLine()).equals("null")) {
            String[] lineList = line.split("/");
            int idWine = Integer.parseInt(lineList[0]);
            int quantity = Integer.parseInt(lineList[1]);
            int idProp = Integer.parseInt(lineList[2]);

            List<Integer> list = Arrays.asList(idWine,quantity,idProp);
            listOfList.add(list);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Payment_method_proposal.fxml"));
        Stage window = (Stage) b.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));

    }

    public void declineIsClicked(int id, Button b) throws IOException {

        Socket socket = mfxc.getSocket();

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        out.println("REFUSE_PURCHASE");
        out.println(id);
        out.println("DELETE_PROPOSAL_TMP");
        out.println(id);

        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("View_proposal.fxml"));
        Stage window = (Stage) b.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
}