package com.example.progettodbfx;

import com.example.progettodbfx.MainFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;

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

public class HomeAdminFXController {
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
    public Button AddProduct_btn;
    @FXML
    public Button Add_employee_btn;
    @FXML
    public Button Edit_employee_data_btn;
    @FXML
    public Button Delete_employee_btn;
    @FXML
    public Button make_report_btn;
    @FXML
    public Button Make_discount_btn;
    @FXML
    public Button search_wine_btn;
    @FXML
    private Button Search_people_btn;
    @FXML
    public Button idk;

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

    public void DeleteEmployeeIsClicked() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("DeleteEmployee.fxml"));
        Stage window = (Stage) Delete_employee_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));

    }
    public void AddEmployeeIsClicked() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("AddEmployee.fxml"));
        Stage window = (Stage) Add_employee_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
    public void EditdataIsClicked() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("EditEmployee.fxml"));
        Stage window = (Stage) Edit_employee_data_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
    public void MakeReportIsClicked() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Report.fxml"));
        Stage window = (Stage) make_report_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
    public void MakediscountIsClicked() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Discount.fxml"));
        Stage window = (Stage) Make_discount_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

    public void AddProductIsClicked() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Add_product.fxml"));
        Stage window = (Stage) AddProduct_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
    public void searchWineIsClicked( ) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Show_wine_by_search_admin.fxml"));
        Stage window = (Stage) search_wine_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
    public void SearchPeopleIsClicked( ) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Search_client_admin.fxml"));
        Stage window = (Stage) Search_people_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }

}
