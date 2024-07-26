package com.posapp.pos;

import com.posapp.pos.common.Common;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loginfxmlLoader = new FXMLLoader(getClass().getResource("ui/login.fxml"));
        Scene scene = new Scene(loginfxmlLoader.load());
        stage.setTitle(Common.appName);
        Image storeIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/new_logo.png")));
        stage.getIcons().add(storeIcon);
        stage.setScene(scene);
        stage.setOnCloseRequest(windowEvent -> {
            windowEvent.consume();
            closeAppAlert(stage);

        });
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void closeAppAlert(Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Closing Application");
        alert.setHeaderText("Close Application");
        alert.setContentText("Are you sure you want to close the application?");

        if(alert.showAndWait().get() == ButtonType.OK){
            System.out.println("Closing application");
            primaryStage.close();
        }
    }

}