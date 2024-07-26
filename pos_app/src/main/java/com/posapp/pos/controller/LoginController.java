package com.posapp.pos.controller;

import com.posapp.pos.common.Common;
import com.posapp.pos.major.MainAdmin;
import com.posapp.pos.major.MainCashier;
import com.posapp.pos.model.Cashier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.posapp.pos.database.DataAccessObject;

public class LoginController implements Initializable {
    @FXML
    private TextField userId;

    @FXML
    private PasswordField userPwd;

    @FXML
    private RadioButton bt_rdo_cashier, bt_rdo_admin;

    @FXML
    private Button bt_login;

    @FXML
    private Label app_name_label;

    DataAccessObject db;
    private final String todayDate = String.valueOf(new Date(System.currentTimeMillis()));


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        db = new DataAccessObject();
        app_name_label.setText(Common.appName);
    }



//public void bt_login_action(ActionEvent event) throws IOException {
//
//
//	    	if(bt_rdo_admin.isSelected()) {
//	    		System.out.println("Admin is selected");
//                try {
//                    new MainAdmin().start((Stage)bt_login.getScene().getWindow());
//                } catch (Exception e){
//                    System.out.println(e.getMessage());
//                }
//
//	    	} else if (bt_rdo_cashier.isSelected()){
//		    		System.out.println("Cashier is selected");
//
//                    try {
//                        new MainCashier().start((Stage)bt_login.getScene().getWindow());
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//
//                }
//    }


//}


    public void bt_login_action(ActionEvent event) throws IOException {
        try {
	    	String id = userId.getText();
	    	String pwd = userPwd.getText();

            System.out.println("id: " + id);
            System.out.println("pwd: " + pwd);


	    	if(bt_rdo_admin.isSelected()) {
	    		System.out.println("Admin is selected");

		    	String adminId = "admin";
		    	String adminPwd = "admin";

                if(id.equals(adminId) && pwd.equals(adminPwd)) {

                    System.out.println("Success!");

                    try {
                        new MainAdmin().start((Stage)bt_login.getScene().getWindow());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, " Login Fail !");
                    alert.showAndWait();
                    userId.clear();
                    userPwd.clear();

                    System.out.println("login fail Error showed");
                }

	    	} else if (bt_rdo_cashier.isSelected()){
                System.out.println("Cashier is selected");

                String cashierId = "don";
                String cashierPwd = "1don";
                String query = "SELECT * from cashier WHERE cashier_id = '" + id + "';";

                System.out.println(query);

                ResultSet result = db.getData(query);


                if (!result.next()){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Login fail! Incorrect Credentials");
                    alert.showAndWait();
                    userId.clear();
                    userPwd.clear();

                    System.out.println("login fail Error showed");
                    return;
                }

                Cashier cashier = Common.cashierRecord;
                Cashier loginCashier = new Cashier(result.getString(1), result.getString(2), result.getInt(3),
                        result.getString(4), result.getString(5), result.getString(6),
                        result.getString(7), result.getString(8), result.getDate(9), result.getDate(10));

                cashier.setId(loginCashier.getId());
                cashier.setName(loginCashier.getName());
                cashier.setAge(Integer.parseInt(loginCashier.getAge()));
                cashier.setGender(loginCashier.getGender());
                cashier.setAddress(loginCashier.getAddress());
                cashier.setPhone(loginCashier.getPhone());
                cashier.setEmail(loginCashier.getEmail());
                cashier.setDateOfBirth(Date.valueOf(loginCashier.getDateOfBirth()));


                String decryptedCashierPwd = Common.decrypt(loginCashier.getPassword());

                if(id.equals(loginCashier.getId()) && pwd.equals(decryptedCashierPwd)) {

                    System.out.println("Success!");
                    System.out.println("cashier name is : "+ loginCashier.getName());

                    try {
                        new MainCashier().start((Stage)bt_login.getScene().getWindow());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Login fail! Incorrect Credentials");
                    alert.showAndWait();
                    userId.clear();
                    userPwd.clear();

                    System.out.println("login fail Error showed");
                }
	    	}else{
	    		Alert alert = new Alert(Alert.AlertType.ERROR, " Login Fail !");
                alert.setHeaderText("Please Select The User Type !");
				alert.showAndWait();
				userId.clear();
				userPwd.clear();

				System.out.println("login fail Error, Please Select The User Type !");
	    	}
        } catch(NumberFormatException nfe) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input!");
            alert.showAndWait();

            System.out.println("input Error showed"+nfe.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}