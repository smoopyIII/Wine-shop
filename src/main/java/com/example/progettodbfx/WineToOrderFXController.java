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
import javafx.util.Callback;

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

public class WineToOrderFXController implements Initializable {

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
    TableColumn<Wine, Void> clickableCol = new TableColumn<>("BUY WINE");

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
            int employeeId = mfxc.employeeID;

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("GET_EMPLOYEE_USERNAME");
            out.println(employeeId);
            client_name_label.setVisible(true);
            client_name_label.setText(in.readLine());

            out.println("SHOW_WINES_TO_ORDER");
            String line;

            int row=0;
            String namewine="";
            List<Integer> valori = new ArrayList<>();

            while (!(line = in.readLine()).equals("null")) {
                String[] lineList = line.split("/");
                int id = Integer.parseInt(lineList[0]);
                namewine = lineList[1];
                String producer =lineList[2];
                String origin = lineList[3];
                String year = lineList[4];
                String technicalNotes = lineList[5];
                String grapes = lineList[6];
                float price =Float.parseFloat(lineList[7]);
                int quantity = Integer.parseInt(lineList[8]);
                valori.add(id);
                System.out.println("Availability: " + quantity);

                w.add(new Wine(id,namewine,producer,origin,(Integer.parseInt(year)),technicalNotes,grapes,price,quantity));
                row++;
            }
            table.setItems(w);

            Callback<TableColumn<Wine, Void>, TableCell<Wine, Void>> cellFactory = new Callback<TableColumn<Wine, Void>, TableCell<Wine, Void>>() {
                @Override
                public TableCell<Wine, Void> call(final TableColumn<Wine, Void> param){
                    final TableCell<Wine, Void> cell = new TableCell<Wine, Void>() {
                        Button bottone = new Button();
                        {
                            bottone.setOnAction((ActionEvent event) -> {
                                // Azione da eseguire quando il pulsante viene premuto
                                System.out.println("Bottone: " + bottone.getId());
                                out.println("BUY_STOCK");
                                out.println(bottone.getId());
                                out.println(employeeId);

                                try {
                                    FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Wine_to_order.fxml"));
                                    Stage window = (Stage) alarm_btn.getScene().getWindow();
                                    window.setScene(new Scene(fxmlLoader.load()));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                            });
                        }
                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                bottone.setText("BUY WINE");
                                bottone.setId(String.valueOf(valori.get(getIndex())));
                                bottone.setPrefWidth(110);
                                setGraphic(bottone);
                            }
                        }
                    };
                    return cell;
                }
            };

            clickableCol.setCellFactory(cellFactory);
            table.getColumns().add(clickableCol);

        }catch (IOException e){
            e.printStackTrace();
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
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("View_proposal_employee.fxml"));
        Stage window = (Stage) alarm_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    @FXML
    public Button mail_btn;
    public void mailIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Wine_to_order.fxml"));
        Stage window = (Stage) alarm_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void accountIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("EmployeeData_page.fxml"));
        Stage window = (Stage) account_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
    public void homeIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_Employee.fxml"));
        Stage window = (Stage) btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

}