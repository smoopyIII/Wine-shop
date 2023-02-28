package com.example.progettodbfx;

        import com.example.progettodbfx.MainFX;
        import com.example.progettodbfx.MainFXController;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.fxml.Initializable;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;

        import javafx.stage.Stage;
        import javafx.scene.control.Label;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.io.PrintWriter;
        import java.net.Socket;
        import java.net.URL;
        import java.util.ResourceBundle;

public class AdminDataFXController implements Initializable {

    @FXML
    public Button Back_btn;
    @FXML
    public Button Edit_btn;
    @FXML
    private Label name_label;
    @FXML
    private Label surname_label;
    @FXML
    private Label fiscalcode_label;
    @FXML
    private Label email_label;
    @FXML
    private Label address_label;
    @FXML
    private Label username_label;
    @FXML
    private Label password_label;
    @FXML
    private Label phonenumber_label;

    MainFXController mfxc = new MainFXController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            Socket socket = mfxc.getSocket();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            int adminId = mfxc.adminID;

            out.println("GET_ADMIN_DATA");
            out.println(adminId);

            name_label.setText(in.readLine());
            surname_label.setText(in.readLine());
            address_label.setText(in.readLine());
            phonenumber_label.setText(in.readLine());
            fiscalcode_label.setText(in.readLine());
            email_label.setText(in.readLine());
            username_label.setText(in.readLine());
            //password_label.setText(in.readLine());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void back_btnIsClicked(ActionEvent actionEvent) throws IOException{

        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_admin.fxml"));
        Stage window = (Stage) Back_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }
    public void edit_btnIsClicked(ActionEvent actionEvent) throws IOException{

        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Reset_admin_password_page.fxml"));
        Stage window = (Stage) Edit_btn.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load()));
    }


}
