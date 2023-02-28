module com.example.progettodbfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires java.desktop;

    opens com.example.progettodbfx to javafx.fxml;
    exports com.example.progettodbfx;
}