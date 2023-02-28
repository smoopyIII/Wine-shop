package com.example.progettodbfx;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Scene;
        import javafx.scene.control.*;
        import javafx.stage.Stage;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.io.PrintWriter;
        import java.net.Socket;


public class ResetAdminPasswordFXController {

    @FXML
    private Button reset_password_btn;
    @FXML
    private Button alarm_btn;
    @FXML
    private Button home_btn;
    @FXML
    private Button account_btn;
    @FXML
    private Button mail_btn;
    @FXML
    private Button logout_btn;

    @FXML
    private TextField oldpassword_textfield;
    @FXML
    private TextField newPassword_textfield;
    @FXML
    private TextField repeat_textfield;
    MainFXController mfxc = new MainFXController();
    int adminID = mfxc.adminID;


    public void reset_password_btnIsClicked(ActionEvent actionEvent) throws IOException {

        Socket socket = mfxc.getSocket();
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String oldpassword = oldpassword_textfield.getText();
        String newpassword = newPassword_textfield.getText();
        String repeatpassword = repeat_textfield.getText();

        if (oldpassword.isEmpty()) {
            oldpassword_textfield.setStyle("-fx-prompt-text-fill: red;");
            oldpassword_textfield.setPromptText("Insert old password");
        }
        if (newpassword.isEmpty()) {
            newPassword_textfield.setStyle("-fx-prompt-text-fill: red;");
            newPassword_textfield.setPromptText("Insert new password");
        }
        if (repeatpassword.isEmpty()) {
            repeat_textfield.setStyle("-fx-prompt-text-fill: red;");
            repeat_textfield.setPromptText("Repeat new password");
        } else if (newpassword.equals(repeatpassword)) {
            out.println("CHECK_OLD_ADMIN_PASSWORD");
            out.println(adminID);
            String oldpasswordServer = in.readLine();

            if (oldpassword.equals(oldpasswordServer)) {
                if (oldpassword.equals(newpassword)) {
                    newPassword_textfield.clear();
                    repeat_textfield.clear();
                    newPassword_textfield.setStyle("-fx-prompt-text-fill: red;");
                    newPassword_textfield.setPromptText("Password already used");
                    repeat_textfield.setStyle("-fx-prompt-text-fill: red;");
                    repeat_textfield.setPromptText("Password already used");
                } else {
                    out.println("UPDATE_ADMIN_PASSWORD");
                    out.println(adminID);
                    out.println(newpassword);

                    FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home_admin.fxml"));
                    Stage window = (Stage) account_btn.getScene().getWindow();
                    window.setScene(new Scene(fxmlLoader.load()));
                }
            } else {
                oldpassword_textfield.clear();
                oldpassword_textfield.setStyle("-fx-prompt-text-fill: red;");
                oldpassword_textfield.setPromptText("Wrong password");
            }

        } else {
            newPassword_textfield.clear();
            repeat_textfield.clear();
            newPassword_textfield.setStyle("-fx-prompt-text-fill: red;");
            newPassword_textfield.setPromptText("Password doesn't match");
            repeat_textfield.setStyle("-fx-prompt-text-fill: red;");
            repeat_textfield.setPromptText("Password doesn't match");
        }
    }

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
}
