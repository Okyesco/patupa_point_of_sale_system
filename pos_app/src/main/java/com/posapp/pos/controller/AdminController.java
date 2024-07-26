package com.posapp.pos.controller;

import com.posapp.pos.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
public class AdminController {

	@FXML
	private Button bt_cashier;

	@FXML
	private Button bt_report;

	@FXML
	private Button bt_popular;

	@FXML
	private Button bt_chart;

	@FXML
	private Button bt_product;

//	 @FXML
//	 private Button bt_card;

//	@FXML
//	private Button bt_promotion;

	@FXML
	private Button bt_category;

//	@FXML
//	private Button bt_customer;

	@FXML
	private Button bt_logout;

	@FXML
	private AnchorPane common_pane;

	@FXML
    private Button bt_supplier;

//	String css = Objects.requireNonNull(getClass().getResource("styles/admin_product.css")).toExternalForm();



	@FXML
	void initialize() throws IOException {
		assert bt_cashier != null : "fx:id=\"bt_cashier\" was not injected: check your FXML file 'Admin_panel.fxml'.";
		assert bt_report != null : "fx:id=\"bt_report\" was not injected: check your FXML file 'Admin_panel.fxml'.";
		assert bt_popular != null : "fx:id=\"bt_popular\" was not injected: check your FXML file 'Admin_panel.fxml'.";
		assert bt_chart != null : "fx:id=\"bt_chart\" was not injected: check your FXML file 'Admin_panel.fxml'.";
		assert bt_product != null : "fx:id=\"bt_product\" was not injected: check your FXML file 'Admin_panel.fxml'.";
//		assert bt_promotion != null : "fx:id=\"bt_promotion\" was not injected: check your FXML file 'Admin_panel.fxml'.";
		assert bt_category != null : "fx:id=\"bt_category\" was not injected: check your FXML file 'Admin_panel.fxml'.";
//		assert bt_customer != null : "fx:id=\"bt_customer\" was not injected: check your FXML file 'Admin_panel.fxml'.";
		assert bt_logout != null : "fx:id=\"bt_logout\" was not injected: check your FXML file 'Admin_panel.fxml'.";
		assert common_pane != null : "fx:id=\"common_pane\" was not injected: check your FXML file 'Admin_panel.fxml'.";
		assert bt_supplier != null : "fx:id=\"bt_supplier\" was not injected: check your FXML file 'Admin_panel.fxml'.";
//		assert bt_card != null : "fx:id=\"bt_card\" was not injected: check your FXML file 'Admin_panel.fxml'.";


		AnchorPane pane = null;
		try {
			pane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("ui/admin_product.fxml")));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		common_pane.getChildren().add(pane);
		// bt_product.setStyle("-fx-background-color : #6e6e6e");
		bt_product.setDisable(true);
	}


	@FXML
	void onCashierAction(ActionEvent event) {

		AnchorPane pane = null;
		try {
			pane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("ui/Admin_cashier.fxml")));
		} catch (IOException e) {
            System.out.println(e.getMessage());
		}
		common_pane.getChildren().clear();
		common_pane.getChildren().add(pane);

		// bt_product.setStyle("-fx-background-color : #6e6e6e");
		bt_cashier.setDisable(true);
		bt_report.setDisable(false);
		bt_popular.setDisable(false);
		bt_chart.setDisable(false);
		bt_product.setDisable(false);
//		bt_promotion.setDisable(false);
		bt_category.setDisable(false);
//		bt_customer.setDisable(false);
		bt_logout.setDisable(false);
		bt_supplier.setDisable(false);
//		bt_card.setDisable(false);
	}

	@FXML
	void onProductAction(ActionEvent event) {
		AnchorPane pane = null;
		try {
			pane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("ui/Admin_product.fxml")));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		common_pane.getChildren().clear();
		common_pane.getChildren().add(pane);

		// bt_product.setStyle("-fx-background-color : #6e6e6e");
		bt_product.setDisable(true);
		bt_report.setDisable(false);
		bt_popular.setDisable(false);
		bt_chart.setDisable(false);
		bt_cashier.setDisable(false);
//		bt_promotion.setDisable(false);
		bt_category.setDisable(false);
//		bt_customer.setDisable(false);
		bt_logout.setDisable(false);
		bt_supplier.setDisable(false);
//		bt_card.setDisable(false);
	}

	@FXML
	void onCategoryAction(ActionEvent event) {

		AnchorPane pane = null;
		try {
			pane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("ui/Admin_category.fxml")));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		common_pane.getChildren().clear();
		common_pane.getChildren().add(pane);

		// bt_product.setStyle("-fx-background-color : #6e6e6e");
		bt_category.setDisable(true);
		bt_report.setDisable(false);
		bt_popular.setDisable(false);
		bt_chart.setDisable(false);
		bt_cashier.setDisable(false);
//		bt_promotion.setDisable(false);
		bt_product.setDisable(false);
//		bt_customer.setDisable(false);
		bt_logout.setDisable(false);
		bt_supplier.setDisable(false);
//		bt_card.setDisable(false);
	}

	@FXML
	void onCustomerAction(ActionEvent event) {

		AnchorPane pane = null;
		try {
			pane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("ui/Admin_customer.fxml")));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		common_pane.getChildren().clear();
		common_pane.getChildren().add(pane);

		// bt_product.setStyle("-fx-background-color : #6e6e6e");
//		bt_customer.setDisable(true);
		bt_report.setDisable(false);
		bt_popular.setDisable(false);
		bt_chart.setDisable(false);
		bt_cashier.setDisable(false);
//		bt_promotion.setDisable(false);
		bt_product.setDisable(false);
		bt_category.setDisable(false);
		bt_logout.setDisable(false);
		bt_supplier.setDisable(false);
//		bt_card.setDisable(false);
	}

	@FXML
	void onPromotionAction(ActionEvent event) {

		AnchorPane pane = null;
		try {
			pane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("ui/Admin_promotion.fxml")));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		common_pane.getChildren().clear();
		common_pane.getChildren().add(pane);

		// bt_product.setStyle("-fx-background-color : #6e6e6e");
//		bt_promotion.setDisable(true);
		bt_report.setDisable(false);
		bt_popular.setDisable(false);
		bt_chart.setDisable(false);
		bt_cashier.setDisable(false);
//		bt_customer.setDisable(false);
		bt_product.setDisable(false);
		bt_category.setDisable(false);
		bt_logout.setDisable(false);
		bt_supplier.setDisable(false);
//		bt_card.setDisable(false);

	}

	@FXML
    void onSupplierAction(ActionEvent event) {

		AnchorPane pane = null;
		try {
			pane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("ui/Admin_supplier.fxml")));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		common_pane.getChildren().clear();
		common_pane.getChildren().add(pane);

		// bt_product.setStyle("-fx-background-color : #6e6e6e");
		bt_supplier.setDisable(true);
		bt_report.setDisable(false);
		bt_popular.setDisable(false);
		bt_chart.setDisable(false);
		bt_cashier.setDisable(false);
//		bt_customer.setDisable(false);
		bt_product.setDisable(false);
//		bt_promotion.setDisable(false);
		bt_category.setDisable(false);
		bt_logout.setDisable(false);
//		bt_card.setDisable(false);
    }

	@FXML
	void onPopularAction(ActionEvent event) {

		AnchorPane pane = null;
		try {
			pane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("ui/Admin_popular_item.fxml")));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		common_pane.getChildren().clear();
		common_pane.getChildren().add(pane);

		// bt_product.setStyle("-fx-background-color : #6e6e6e");
		bt_popular.setDisable(true);
		bt_report.setDisable(false);
		bt_supplier.setDisable(false);
		bt_chart.setDisable(false);
		bt_cashier.setDisable(false);
//		bt_customer.setDisable(false);
		bt_product.setDisable(false);
//		bt_promotion.setDisable(false);
		bt_category.setDisable(false);
		bt_logout.setDisable(false);
//		bt_card.setDisable(false);
	}



	   @FXML
	    void onManageCardAction(ActionEvent event) {

		   AnchorPane pane = null;
			try {
				pane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("ui/Admin_card.fxml")));
			} catch (IOException e) {
			System.out.println(e.getMessage());
			}
			common_pane.getChildren().clear();
			common_pane.getChildren().add(pane);

			// bt_product.setStyle("-fx-background-color : #6e6e6e");
//			bt_card.setDisable(true);
			bt_report.setDisable(false);
			bt_supplier.setDisable(false);
			bt_chart.setDisable(false);
			bt_cashier.setDisable(false);
//			bt_customer.setDisable(false);
			bt_product.setDisable(false);
//			bt_promotion.setDisable(false);
			bt_category.setDisable(false);
			bt_logout.setDisable(false);
			bt_popular.setDisable(false);
	    }

		@FXML
		void onReportAction(ActionEvent event) {

			AnchorPane pane = null;
			try {
				pane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("ui/Admin_view_report.fxml")));
			} catch (IOException e) {
			System.out.println(e.getMessage());
			}
			common_pane.getChildren().clear();
			common_pane.getChildren().add(pane);

			// bt_product.setStyle("-fx-background-color : #6e6e6e");
			bt_report.setDisable(true);
//			bt_card.setDisable(false);
			bt_supplier.setDisable(false);
			bt_chart.setDisable(false);
			bt_cashier.setDisable(false);
//			bt_customer.setDisable(false);
			bt_product.setDisable(false);
//			bt_promotion.setDisable(false);
			bt_category.setDisable(false);
			bt_logout.setDisable(false);
			bt_popular.setDisable(false);
		}


		@FXML
		void onChartAction(ActionEvent event) {

			AnchorPane pane = null;
			try {
				pane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("ui/Admin_view_chart.fxml")));
			} catch (IOException e) {
			System.out.println(e.getMessage());
			}
			common_pane.getChildren().clear();
			common_pane.getChildren().add(pane);

			// bt_product.setStyle("-fx-background-color : #6e6e6e");
            bt_chart.setDisable(true);
//			bt_card.setDisable(false);
			bt_supplier.setDisable(false);
			bt_report.setDisable(false);
			bt_cashier.setDisable(false);
//			bt_customer.setDisable(false);
			bt_product.setDisable(false);
//			bt_promotion.setDisable(false);
			bt_category.setDisable(false);
			bt_logout.setDisable(false);
			bt_popular.setDisable(false);

		}

		@FXML
		void onLogoutAction(ActionEvent event) {
			// scene transaction
			try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Logout");
                alert.setHeaderText("You will be logged out of the program");
                alert.setContentText("Are you sure you want to logout?");
                if (alert.showAndWait().get() == ButtonType.OK){
                    new Main().start((Stage) bt_logout.getScene().getWindow());
                }

			} catch (Exception e) {
			System.out.println(e.getMessage());
			}
		}

}
