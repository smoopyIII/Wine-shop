package com.example.progettodbfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeEmployeeFXController  implements Initializable {

    private static final String DBURL = "jdbc:mysql://127.0.0.1:3306/wineshop?";
    private static final String ARGS = "createDatabaseIfNotExist=true&serverTimezone=UTC";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "";
    @FXML
    public Button logout_btn;
    @FXML
    public Button Search_purchase_btn;
    @FXML
    public ImageView bell;
    @FXML
    public Button buy_wine_btn;
    @FXML
    public Button alarm_btn;
    @FXML
    public Button search_wine_btn;
    @FXML
    private Label employee_name_label;
    @FXML
    private Button Search_people_btn;
    @FXML
    private Button account_btn;
    @FXML
    public Button mail_btn;

    public void HomeClientFXController()  {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        MainFXController mfxc = new MainFXController();
        int employeeId = mfxc.employeeID;

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

        out.println("GET_EMPLOYEE_USERNAME");
        out.println(employeeId);
        try {
            employee_name_label.setVisible(true);
            employee_name_label.setText(in.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        out.println("CHECK_DATE");


        int cont = 0;
        BufferedReader finalIn = in;
        PrintWriter finalOut = out;
        Thread t = new Thread(() -> {

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                finalOut.println("CHECK_WINE_TO_ORDER");

                finalOut.println(cont);
                try {
                    if (finalIn.readLine().equals("TRUE")) {

                        String imagePath = "file:///" + System.getProperty("user.dir") + "/target/classes/com/example/progettodbfx/notification.png";
                        Image image = new Image(imagePath);
                        bell.setImage(image);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t.start();

    }
    public void logoutIsClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Login_page.fxml"));
        Stage window = (Stage) logout_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void searchWineIsClicked( ) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Show_wine_by_search_employee.fxml"));
        Stage window = (Stage) search_wine_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void SearchPurchaseIsClicked( )throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Show_purchase_employee.fxml"));
        Stage window = (Stage) Search_purchase_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
    public void SearchPeopleIsClicked( ) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Search_client.fxml"));
        Stage window = (Stage) Search_people_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void accountIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("EmployeeData_page.fxml"));
        Stage window = (Stage) account_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void mailIsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Wine_to_order.fxml"));
        Stage window = (Stage) mail_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void alarmIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("View_proposal_employee.fxml"));
        Stage window = (Stage) alarm_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));

    }
    @FXML
    public Button home_btn;
    public void homeIsClicked(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("home_employee.fxml"));
        Stage window = (Stage) home_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
}