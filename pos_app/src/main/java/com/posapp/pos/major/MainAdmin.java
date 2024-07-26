package com.posapp.pos.major;

import com.posapp.pos.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainAdmin extends Application{

	public void start(Stage primaryStage) throws IOException {
		FXMLLoader root = new FXMLLoader(Main.class.getResource("ui/Admin_panel.fxml"));

	//	LoginController logincontroller = root.getController();

		Scene scene = new Scene(root.load());
//		scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("styles/admin_product.css")).toExternalForm());
		primaryStage.setScene(scene);
		Image storeIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/logo.png")));
        primaryStage.getIcons().add(storeIcon);
		primaryStage.setResizable(true);
		primaryStage.setMaximized(true);
		primaryStage.show();

	}

	public static void main(String[] args) {

		Application.launch(args);
	}
}
