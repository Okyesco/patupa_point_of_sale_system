package com.posapp.pos.controller;

import com.posapp.pos.database.DataAccessObject;
import com.posapp.pos.model.Supplier;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AdminSupplierController implements Initializable {

    @FXML
    private TextField search_name;

    @FXML
    private Button bt_add;

    @FXML
    private Button bt_delete;

    @FXML
    private Button bt_new;

    @FXML
    private Button bt_update;

    @FXML
    private TableColumn<Supplier, String> col_address;

    @FXML
    private TableColumn<Supplier, String> col_date;

    @FXML
    private TableColumn<Supplier, String> col_email;

    @FXML
    private TableColumn<Supplier, String> col_id;

    @FXML
    private TableColumn<Supplier, String> col_name;

    @FXML
    private TableColumn<Supplier, String> col_phone;

    @FXML
    private DatePicker df_last_date;

    @FXML
    private TableView<Supplier> tb_supplier;

    @FXML
    private TextField tf_address;

    @FXML
    private TextField tf_email;

//    @FXML
//    private TextField tf_id;

    @FXML
    private TextField tf_name;

    @FXML
    private TextField tf_phone;

    private String query;
    private DataAccessObject db;
    private int ID;
//    private String newSupplierID;
    private final String todayDate = String.valueOf(new Date(System.currentTimeMillis()));


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        db = new DataAccessObject();

        searchSupplierNameAndPhone();

//        try {
//            getLastSupplierID();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }


        try {
            refreshTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        tb_supplier.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                getTableDataInTextField();
            }
        });

        bt_add.setOnAction(actionEvent -> {
            try {
                insertNewSupplier();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        bt_update.setOnAction(actionEvent -> {
            try {
                updateSupplier();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        bt_new.setOnAction(actionEvent -> {
            try {
                setDefaultIDAndDate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        bt_delete.setOnAction(actionEvent -> {
            try {
                deleteSupplier();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }


    private void insertNewSupplier() throws SQLException {
        String supplierName = tf_name.getText();
        String supplierAddress = tf_address.getText();
        String supplierPhone = tf_phone.getText();
        String supplierEmail = tf_email.getText();
//        String SupplierID = tf_id.getText();
//        ID = Integer.parseInt(SupplierID);
        String lastDateString = df_last_date.getEditor().getText();
        LocalDate lastDate = df_last_date.getValue();


        if (inputValidationChecker(supplierName, supplierAddress, supplierPhone, supplierEmail, lastDateString, lastDate)) return;
        query = "INSERT INTO supplier(company_name, phone, address, email, last_supplied_date) VALUES('" + supplierName
                + "', '"+supplierPhone+"', '"+supplierAddress+"', '"+supplierEmail+"', '"+Date.valueOf(lastDate)+"')";
        db.saveData(query);
        tf_name.clear();
        refreshTable();
    }

    private ObservableList<Supplier> getAllSupplier() throws SQLException {
        String query = "SELECT * FROM supplier ORDER BY company_name ASC";
        ObservableList<Supplier> dataList = FXCollections.observableArrayList();
        try {
            ResultSet result = db.getData(query);
            while (result.next()) {
                dataList.add(new Supplier(result.getInt(1), result.getString(2), result.getString(3),
                        result.getString(4), result.getString(5),  result.getDate(6)));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return dataList;
    }

    private void initTableColumn(){
        col_id.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_name.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_phone.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_date.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_address.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_email.setStyle("-fx-alignment: center; -fx-font-size: 15;");

        col_id.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSupplierId()));
        col_name.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSupplierName()));
        col_address.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSupplierAddress()));
        col_phone.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSupplierPhone()));
        col_email.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSupplierEmail()));
        col_date.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLastSuppliedDate()));

    }

    private void refreshTable() throws SQLException {
        initTableColumn();
        ObservableList<Supplier> suppliers = getAllSupplier();
        tb_supplier.setItems(suppliers);
        setDefaultIDAndDate();
    }

    private void getTableDataInTextField(){
        Supplier selectedItem = tb_supplier.getSelectionModel().getSelectedItem();
        String supplierName = selectedItem.getSupplierName();
        String supplierAddress = selectedItem.getSupplierAddress();
        String supplierPhone = selectedItem.getSupplierPhone();
        String supplierEmail = selectedItem.getSupplierEmail();
        String SupplierID = selectedItem.getSupplierId();
        ID = Integer.parseInt(SupplierID);
        String LastDate = selectedItem.getLastSuppliedDate();

//        tf_id.setText(SupplierID);
        tf_name.setText(supplierName);
        tf_phone.setText(supplierPhone);
        tf_email.setText(supplierEmail);
        tf_address.setText(supplierAddress);
        df_last_date.setValue(LocalDate.parse(LastDate));
    }


    private void updateSupplier() throws SQLException {
        if (tb_supplier.getSelectionModel().getSelectedItem() == null){
            return;
        }

        String newSupplierName = tf_name.getText();
        String newSupplierAddress = tf_address.getText();
        String newSupplierPhone = tf_phone.getText();
        String newSupplierEmail = tf_email.getText();
        String newLastDateString = df_last_date.getEditor().getText();
        LocalDate newLastDate = df_last_date.getValue();


        if (inputValidationChecker(newSupplierName, newSupplierAddress, newSupplierPhone, newSupplierEmail, newLastDateString, newLastDate))
            return;

//        String newSupplierID = tf_id.getText();
//        ID = Integer.parseInt(newSupplierID);

        query = "UPDATE supplier SET company_name = '"+newSupplierName+"', phone = '"+newSupplierPhone+"', address = '"+newSupplierAddress+"'," +
                " email = '"+newSupplierEmail+"', last_supplied_date = '"+newLastDate+"' WHERE id = " + ID + ";";
        db.saveData(query);
        refreshTable();
    }

    private boolean inputValidationChecker(String newSupplierName, String newSupplierAddress, String newSupplierPhone, String newSupplierEmail, String newLastDateString, LocalDate newLastDate) {
        if (newSupplierName.isEmpty() || newSupplierAddress.isEmpty() || newSupplierPhone.isEmpty() || newSupplierEmail.isEmpty() ||
                newLastDateString.isEmpty() || Date.valueOf(newLastDate).after(Date.valueOf(todayDate))) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill all required fields, Last supplied date cannot be greater than today !", ButtonType.OK);
            alert.setHeaderText("Fields cannot be empty");
            alert.showAndWait();
            return true;
        }
        return false;
    }

    private void deleteSupplier() throws SQLException {
        Supplier selectedItem = tb_supplier.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }

        int supplierCount = 0;
        query = "select count(*) from product, supplier where product.supplier_id = supplier.id and product.supplier_id = "+selectedItem.getSupplierId()+";";
        ResultSet result = db.getData(query);
        while (result.next()) {
            supplierCount = result.getInt(1);
        }

        if (supplierCount > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot delete! This Supplier has been used in product items. Thanks!");
            alert.setHeaderText("Cannot delete this Supplier !!!");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete '" +
                selectedItem.getSupplierName()+ "' from supplier?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Delete Supplier Item");

        if (alert.showAndWait().get() == ButtonType.YES) {
            String supplierID = selectedItem.getSupplierId();
            query = "DELETE FROM supplier WHERE id = '" + supplierID + "';";
            db.saveData(query);
            refreshTable();
        }
    }

    private void setDefaultIDAndDate() throws SQLException {
//        getLastSupplierID();
        search_name.clear();
//        tf_id.setText(newSupplierID);
        tf_address.clear();
        tf_name.clear();
        tf_phone.clear();
        tf_email.clear();
        df_last_date.setValue(null);
    }


//    private void getLastSupplierID() throws SQLException {
//        query = "SELECT id FROM supplier ORDER BY id DESC LIMIT 1;";
//        ResultSet result = db.getData(query);
//
//        if (!result.next()){
//            newSupplierID = "1";
//            return;
//        }
//        while (result.next()) {
//            newSupplierID = String.valueOf(result.getInt(1) + 1);
//
//        }
//    }


    private void searchSupplierNameAndPhone(){
        search_name.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Supplier> searchList = FXCollections.observableArrayList();
            query = "SELECT * FROM supplier WHERE company_name like '%"+newValue+"%' or phone like '%"+newValue+"%';";
            try {
                ResultSet result = db.getData(query);
                searchList.clear();
                while (result.next()) {
                    searchList.add(new Supplier(result.getInt(1), result.getString(2), result.getString(3),
                        result.getString(4), result.getString(5),  result.getDate(6)));
                }

                tb_supplier.setItems(searchList);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
