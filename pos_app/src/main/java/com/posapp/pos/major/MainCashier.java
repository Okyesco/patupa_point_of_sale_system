package com.posapp.pos.major;

import com.posapp.pos.Main;
import com.posapp.pos.common.Common;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainCashier extends Application{

	public void start(Stage primaryStage) throws Exception {
		FXMLLoader root = new FXMLLoader(Main.class.getResource("ui/cashier_main.fxml"));
		
	//	LoginController logincontroller = root.getController();
		 
		
		Scene scene = new Scene(root.load());
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
