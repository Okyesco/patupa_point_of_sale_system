module com.posapp.pos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;
//    requires java.dotenv;

    opens com.posapp.pos to javafx.fxml;
    exports com.posapp.pos;
    exports com.posapp.pos.controller;
    opens com.posapp.pos.controller to javafx.fxml;
}