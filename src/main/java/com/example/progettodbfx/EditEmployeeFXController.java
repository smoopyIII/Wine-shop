package com.example.progettodbfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class EditEmployeeFXController implements Initializable {
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
    public TableColumn<Employee, String> Username;
    @FXML
    public TableColumn <Employee, String> Name;
    @FXML
    public TableColumn <Employee, String> Surname;
    @FXML
    public TableColumn <Employee, String> FiscalCode;
    @FXML
    public TableColumn <Employee, String> Email;
    @FXML
    public TableColumn <Employee, String> PhoneNumber;
    @FXML
    public TableColumn <Employee, String> Address;
    @FXML
    public Button edit_employee_btn;
    @FXML
    public TableView<Employee> date_textfield;
    @FXML
    public TextField edit_employee_textfield;

    ObservableList<Employee> w = FXCollections.observableArrayList();

    public static String usernameSelected;

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
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Admin_data_page.fxml"));
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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MainFXController mfxc = new MainFXController();

        try (Connection conn = DriverManager.getConnection(DBURL + ARGS , LOGIN, PASSWORD);
             Statement stmt = conn.createStatement();)
        {
            w.clear();
            Username.setCellValueFactory(new PropertyValueFactory<Employee,String>("username"));
            Name.setCellValueFactory(new PropertyValueFactory<Employee,String>("name"));
            Surname.setCellValueFactory(new PropertyValueFactory<Employee,String>("surname"));
            FiscalCode.setCellValueFactory(new PropertyValueFactory<Employee,String>("fiscalCode"));
            Email.setCellValueFactory(new PropertyValueFactory<Employee,String>("email"));
            PhoneNumber.setCellValueFactory(new PropertyValueFactory<Employee,String>("phoneNumber"));
            Address.setCellValueFactory(new PropertyValueFactory<Employee,String>("address"));

            Socket socket = mfxc.getSocket();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            out.println("SHOW_EMPLOYEE");

            String line;

            while (!(line = in.readLine()).equals("null")) {
                System.out.println(line);

                String[] lineList = line.split("/");
                int id = Integer.parseInt(lineList[0]);
                String name = lineList[1];
                String surname =lineList[2];
                String fiscalCode = lineList[3];
                String email = lineList[4];
                String phoneNumber = lineList[5];
                String address = lineList[6];
                String username = lineList[7];
                w.add(new Employee(id, name, surname, fiscalCode, email, phoneNumber, address,username,""));

            }
            date_textfield.setItems(w);

        }catch (SQLException | IOException e){
            e.printStackTrace();
        }
    }
    public void EditEmployeeIsClicked() throws IOException
    {
        usernameSelected = edit_employee_textfield.getText();
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("EditDataEmployeeByAdmin.fxml"));
        Stage window = (Stage) edit_employee_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }


}
