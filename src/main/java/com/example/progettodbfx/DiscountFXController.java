package com.example.progettodbfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.Float.parseFloat;

public class DiscountFXController implements Initializable {

    ObservableList<Wine> w = FXCollections.observableArrayList();
    ObservableList<String> finale = FXCollections.observableArrayList();

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
    public TableColumn <Wine, Integer>  cAvailability;
    @FXML
    public TableColumn <Wine, String> cGrape;
    @FXML
    public TableColumn <Wine, Float> cPrice;

    TableColumn<Wine, Void> clickableCol = new TableColumn<>("DISCOUNT %");

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
    List<Integer> spinnerID = new ArrayList<>();
    List<Integer> spinnerValue = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try {

            cId.setCellValueFactory(new PropertyValueFactory<Wine,Integer>("id"));
            cName.setCellValueFactory(new PropertyValueFactory<Wine,String>("name"));
            cProducer.setCellValueFactory(new PropertyValueFactory<Wine,String>("producer"));
            cOrigin.setCellValueFactory(new PropertyValueFactory<Wine,Integer>("origin"));
            cYear.setCellValueFactory(new PropertyValueFactory<Wine,Integer>("year"));
            cAvailability.setCellValueFactory(new PropertyValueFactory<Wine,Integer>("availability"));
            cGrape.setCellValueFactory(new PropertyValueFactory<Wine,String>("grapes"));
            cPrice.setCellValueFactory(new PropertyValueFactory<Wine,Float>("price"));

            Socket socket = mfxc.getSocket();

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("SHOW_WINES");
            String line;
            int row=0;
            String namewine="";
            String technicalNotes="";

            while (!(line = in.readLine()).equals("null")) {
                String[] lineList = line.split("/");
                int id = Integer.parseInt(lineList[0]);
                namewine = lineList[1];
                String producer =lineList[2];
                String origin = lineList[3];
                String year = lineList[4];
                String grapes = lineList[6];
                float price = parseFloat(lineList[7]);

                finale.add(String.valueOf(price));

                int quantity = Integer.parseInt(lineList[8]);
                spinnerID.add(id);

                w.add(new Wine(id,namewine,producer,origin,(Integer.parseInt(year)),String.valueOf(price),grapes,price,quantity));
                row++;
            }

            out.println("GET_DISCOUNT");
            spinnerValue.clear();
            while (!(line = in.readLine()).equals("null")) {
                System.out.println("STAMPO LO SCONTO: " + line);
                spinnerValue.add(Integer.valueOf(line));
            }
            table.setItems(w);


            Callback<TableColumn<Wine, Void>, TableCell<Wine, Void>> cellFactory = new Callback<TableColumn<Wine, Void>, TableCell<Wine, Void>>() {
                @Override
                public TableCell<Wine, Void> call(final TableColumn<Wine, Void> param){
                    final TableCell<Wine, Void> cell = new TableCell<Wine, Void>() {
                        final Spinner<Integer> spinner = new Spinner<>(0, 100, 0, 5);
                        {
                            spinner.valueProperty().addListener((obs, oldValue, newValue) ->{
                                //System.out.println("Spinner: " + spinner.getId());
                                spinnerValue.set(Integer.parseInt(spinner.getId())-1,newValue);

                                String cellValue = table.getColumns().get(7).getCellObservableValue(Integer.parseInt(spinner.getId())-1).toString();
                                String cleanValue = cellValue.replaceAll("[^\\d.]", ""); // elimina il testo non numerico
                                float floatValue = Float.parseFloat(cleanValue); // converte la stringa pulita in float
                                floatValue = floatValue - ((floatValue * newValue)/100);
                                for(int i=0;i<spinnerValue.size();i++){
                                    System.out.println("Sconto sul vino "+  spinner.getId()+": " + spinnerValue.get(i));
                                }
                            });
                        }
                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                spinner.setId(String.valueOf(spinnerID.get(getIndex())));
                                spinner.getValueFactory().setValue( spinnerValue.get(spinnerID.get(getIndex())-1) );
                                setGraphic(spinner);
                            }
                        }
                    };
                    return cell;
                }
            };

            clickableCol.setCellFactory(cellFactory);
            clickableCol.setMaxWidth(140);
            table.getColumns().add(clickableCol);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void discountIsClicked(ActionEvent actionEvent) throws IOException{
        try {
            Socket socket = mfxc.getSocket();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("CREATE_DISCOUNT");
            out.println(spinnerValue.size());
            for(int i=0;i<spinnerValue.size();i++){
                out.println(spinnerValue.get(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_admin.fxml"));
        Stage window = (Stage) btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void logoutIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Login_page.fxml"));
        Stage window = (Stage) btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    @FXML
    public Button alarm_btn;
    public void alarmIsClicked(ActionEvent actionEvent) throws IOException{
        /*FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("View_proposal_employee.fxml"));
        Stage window = (Stage) alarm_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));*/
    }

    @FXML
    public Button mail_btn;
    public void mailIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Wine_to_order.fxml"));
        Stage window = (Stage) alarm_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void accountIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("AdminData.fxml"));
        Stage window = (Stage) account_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
    public void homeIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_admin.fxml"));
        Stage window = (Stage) btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

}