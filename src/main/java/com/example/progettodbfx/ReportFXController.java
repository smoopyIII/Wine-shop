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

public class ReportFXController  implements Initializable{
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
    public Label income_label;
    @FXML
    public Label costs_label;
    @FXML
    public Label bottle_sold_label;
    @FXML
    public Label remaining_bottles_label;
    @FXML
    private DatePicker from_date, to_date;
    @FXML
    public TableView<Report> table;

    ObservableList<Report> w = FXCollections.observableArrayList();

    @FXML
    public TableColumn <Report, String> Name;
    @FXML
    public TableColumn <Report, String> quantity;
    @FXML
    public LineChart<String, Number> lineChart = new LineChart<>(new CategoryAxis(), new NumberAxis());

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
    public void mailIsClicked() throws IOException
    {

    }

    public void goIsClicked() throws IOException
    {
        MainFXController mfxc = new MainFXController();

        Socket socket = mfxc.getSocket();

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        LocalDate d1 = from_date.getValue();
        LocalDate d2 = to_date.getValue();
        int check=0;
        w.clear();

        if ((d1 != null) && (d2 != null)) {
            if (d1.isAfter(d2)) {
                from_date.setStyle("-fx-prompt-text-fill: red;");
                from_date.setPromptText("Date error");
                to_date.setStyle("-fx-prompt-text-fill: red;");
                to_date.setPromptText("Date error");
                check = 1;
            }
        }

        if(check==0) {
            out.println("REPORT");
            out.println(d1);
            out.println(d2);
            try {

                String income = in.readLine();
                String costs = in.readLine();
                String bottle_sold = in.readLine();
                String remaining_bottles = in.readLine();

                String app = income + "€";
                income_label.setText(app);
                app = costs + "€";
                costs_label.setText(app);
                bottle_sold_label.setText(bottle_sold);
                remaining_bottles_label.setText(remaining_bottles);

                String line = in.readLine();
                while (!line.equals("null")) {
                    String[] parts = line.split("/");
                    w.add(new Report(parts[1], parts[0]));
                    line = in.readLine();
                }
                table.setItems(w);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        MainFXController mfxc = new MainFXController();

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

        Name.setCellValueFactory(new PropertyValueFactory<Report,String>("name"));
        quantity.setCellValueFactory(new PropertyValueFactory<Report,String>("contwine"));

        LocalDate d1 = from_date.getValue();
        LocalDate d2 = to_date.getValue();
        int check=0;

        if ((d1 != null) && (d2 != null)) {
            if (d1.isAfter(d2)) {
                from_date.setStyle("-fx-prompt-text-fill: red;");
                from_date.setPromptText("Date error");
                from_date.setValue(null);
                to_date.setStyle("-fx-prompt-text-fill: red;");
                to_date.setPromptText("Date error");
                to_date.setValue(null);
                check = 1;
            }
        }
        if(check==0) {
            out.println("REPORT");
            out.println(d1);
            out.println(d2);
            try {

                String income = in.readLine();
                String costs = in.readLine();
                String bottle_sold = in.readLine();
                String remaining_bottles = in.readLine();

                String app = income + "€";
                income_label.setText(app);
                app = costs + "€";
                costs_label.setText(app);
                bottle_sold_label.setText(bottle_sold);
                remaining_bottles_label.setText(remaining_bottles);

                String line = in.readLine();
                while (!line.equals("null")) {
                    String[] parts = line.split("/");
                    w.add(new Report(parts[1], parts[0]));
                    line = in.readLine();
                }
                table.setItems(w);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("LineChart");
        lineChart.setTitle("Report mensile");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Incomes");
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Costs");

        try {
            for (int i = 1; i <= 12; i++) {
                out.println("REPORT_YEAR");
                out.println(i);

                String month = "null";

                if(i == 1) { month = "Gennaio"; }
                if(i == 2) { month = "Febbraio"; }
                if(i == 3) { month = "Marzo"; }
                if(i == 4) { month = "Aprile"; }
                if(i == 5) { month = "Maggio"; }
                if(i == 6) { month = "Giugno"; }
                if(i == 7) { month = "Luglio"; }
                if(i == 8) { month = "Agosto"; }
                if(i == 9) { month = "Settembre"; }
                if(i == 10) { month = "Ottobre"; }
                if(i == 11) { month = "Novembre"; }
                if(i == 12) { month = "Dicembre"; }

                series.getData().add(new XYChart.Data<>(month, Float.parseFloat(in.readLine())));
                series2.getData().add(new XYChart.Data<>(month, Float.parseFloat(in.readLine())));
            }
        }
        catch (IOException e)
        {

        }


        lineChart.getXAxis().setLabel("Month");
        lineChart.getData().addAll(series,series2);

    }
}
