package com.example.progettodbfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MainFXController implements Initializable {
    int flag = 0;
    private static final String DBURL = "jdbc:mysql://127.0.0.1:3306/wineshop?";
    private static final String ARGS = "createDatabaseIfNotExist=true&serverTimezone=UTC";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "";
    @FXML
    private Button btn;
    @FXML
    private TextField username_textfield;
    @FXML
    private PasswordField password_textfield;
    @FXML
    private Hyperlink forgot_password_link;
    @FXML
    private Hyperlink sign_up_link;

    @FXML
    private RadioButton client_radio,employee_radio,admin_radio;
    @FXML
    private Label nouserfound_label;

    public Client client;
    public static int clientID;
    public static int offertCounter = 0;
    public Employee employee;
    public static int employeeID;
    public Admin admin;
    public static int adminID;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Socket s = getSocket();
            System.out.println("Socket in MainFX: " + s);
        } catch (IOException e) {
            System.out.println(e);
        }


    }
    @FXML
    public void btnSignIsClicked() throws Exception{
        try (Connection conn = DriverManager.getConnection(
                DBURL + ARGS , LOGIN, PASSWORD);
             Statement stmt = conn.createStatement();) {
            String user = username_textfield.getText();
            String pass = password_textfield.getText();

            if( (!user.equals("")) && (!pass.equals("")) ) {
                if (client_radio.isSelected()) {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_client.fxml"));
                    client = Gestione_Client.login(user, pass);
                    nouserfound_label.setVisible(false);

                    if (client != null) {
                        clientID = client.getId();
                        Stage window = (Stage) btn.getScene().getWindow();
                        window.setScene(new Scene(fxmlLoader.load()));
                    } else {
                        nouserfound_label.setVisible(true);
                        nouserfound_label.setText("No user found");
                    }

                } else if (employee_radio.isSelected()) {

                    FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("home_employee.fxml"));
                    employee = Gestione_Employee.login(user, pass);
                    nouserfound_label.setVisible(false);

                    if (employee != null) {
                        employeeID = employee.getId();
                        Stage window = (Stage) btn.getScene().getWindow();
                        window.setScene(new Scene(fxmlLoader.load()));
                    }
                    else {
                        nouserfound_label.setVisible(true);
                        nouserfound_label.setText("No user found");
                    }
                } else if (admin_radio.isSelected()) {

                    FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_admin.fxml"));
                    admin = Gestione_admin.login(user, pass);
                    nouserfound_label.setVisible(false);

                    if (admin != null) {
                        adminID = admin.getId();
                        Stage window = (Stage) btn.getScene().getWindow();
                        window.setScene(new Scene(fxmlLoader.load()));
                    } else {
                        nouserfound_label.setVisible(true);
                        nouserfound_label.setText("No user found");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void forgotLinkClicked() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Forgotpassword.fxml"));
        Stage window = (Stage) forgot_password_link.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void signUpLinkClicked() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Sign_up_page.fxml"));
        Stage window = (Stage) sign_up_link.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public Socket getSocket() throws IOException{
        Socket socket = new Socket("localhost", 4444);
        return socket;
    }

    public void setClient(Client c){
        client = c;
    }

    public Client getClient(){
        return client;
    }

}