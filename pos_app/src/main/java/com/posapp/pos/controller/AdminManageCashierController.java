package com.posapp.pos.controller;

import com.posapp.pos.common.Common;
import com.posapp.pos.database.DataAccessObject;
import com.posapp.pos.model.Cashier;
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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AdminManageCashierController implements Initializable {
    @FXML
    private Button bt_add;

    @FXML
    private Button bt_delete;

    @FXML
    private Button bt_new;

    @FXML
    private Button bt_update;

    @FXML
    private TableColumn<Cashier, String> col_address;

    @FXML
    private TableColumn<Cashier, String> col_age;

    @FXML
    private TableColumn<Cashier, String> col_date_created;

    @FXML
    private TableColumn<Cashier, String> col_dob;

    @FXML
    private TableColumn<Cashier, String> col_email;

    @FXML
    private TableColumn<Cashier, String> col_gender;

    @FXML
    private TableColumn<Cashier, String> col_id;

    @FXML
    private TableColumn<Cashier, String> col_name;

    @FXML
    private TableColumn<Cashier, String> col_password;

    @FXML
    private TableColumn<Cashier, String> col_phone;

    @FXML
    private DatePicker df_dob;

    @FXML
    private ToggleGroup gender;

    @FXML
    private RadioButton rdo_female;

    @FXML
    private RadioButton rdo_male;

    @FXML
    private TableView<Cashier> tb_cashier;

    @FXML
    private TextField tf_address;

    @FXML
    private TextField tf_age;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_id;

    @FXML
    private TextField tf_name;

    @FXML
    private TextField tf_password;

    @FXML
    private TextField tf_phone;

    @FXML
    private TextField tf_search;

    private String query;
    private DataAccessObject db;
    private String ID;
    private String newCashierID;
    private final String todayDate = String.valueOf(new Date(System.currentTimeMillis()));



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        db = new DataAccessObject();

        tf_id.setEditable(false);
        tf_age.setEditable(false);


        Label placeholder = new Label("No Data Available");
        tb_cashier.setPlaceholder(placeholder);

        try {
            searchCashierByIDNamePhone();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            setDefaultIDAndDate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            generateNewCashierID();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        try {
            refreshTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        bt_new.setOnAction(actionEvent -> {
            try {
                setDefaultIDAndDate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });


        bt_add.setOnAction(event -> {
            try {
                insertNewCashier();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        tb_cashier.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                getTableDataInTextField();
            }
        });

        bt_update.setOnAction(event -> {
            try {
                updateCashier();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        bt_delete.setOnAction(event -> {
            try {
                deleteCashier();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private void insertNewCashier() throws SQLException {
        RadioButton selectedRadioBtn = (RadioButton) gender.getSelectedToggle();

        String cashierID = tf_id.getText();
        String cashierName = tf_name.getText();
        String cashierAddress = tf_address.getText();
        String cashierPhone = tf_phone.getText();
        String cashierEmail = tf_email.getText();
        String password = tf_password.getText();
        String dobDateString = df_dob.getEditor().getText();
        LocalDate dobDate = df_dob.getValue();


        if (cashierName.isEmpty() || cashierAddress.isEmpty() || cashierPhone.isEmpty() || !selectedRadioBtn.isSelected() ||
                cashierEmail.isEmpty() || password.isEmpty() || dobDateString.isEmpty() || Date.valueOf(dobDate).after(Date.valueOf(todayDate))) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill all required fields, (NB)Date of birth cannot be greater than today !", ButtonType.OK);
            alert.setHeaderText("Fields cannot be empty");
            alert.showAndWait();
            return;
        }

        Date dob = Date.valueOf(df_dob.getValue());

        Map<String, Integer> todayMap = getDateYearDayAndMonth(Date.valueOf(todayDate));
        int todayYear = todayMap.get("year");

        Map<String, Integer> userDOB = getDateYearDayAndMonth(dob);
        int userYear = userDOB.get("year");

        int cashierAge = todayYear - userYear;

        String encryptedPwd = Common.encrypt(password);
        String cashierGender = selectedRadioBtn.getText();



        query = "INSERT INTO cashier (cashier_id, name, age, gender, address, phone, email, password, date_of_birth) VALUES('" + cashierID
                + "', '"+cashierName+"', "+cashierAge+", '"+cashierGender+"', '"+cashierAddress+"', '"+cashierPhone+"'," +
                " '"+cashierEmail+"', '"+ encryptedPwd +"', '"+ Date.valueOf(dobDate)+"')";

        db.saveData(query);
        tf_name.clear();
        refreshTable();
    }

    private ObservableList<Cashier> getAllCashier() throws SQLException {
        String query = "SELECT * FROM cashier ORDER BY name ASC";
        ObservableList<Cashier> dataList = FXCollections.observableArrayList();
        try {
            ResultSet result = db.getData(query);
            while (result.next()) {
                dataList.add(new Cashier(result.getString(1), result.getString(2), result.getInt(3),
                        result.getString(4), result.getString(5), result.getString(6),
                        result.getString(7), result.getString(8), result.getDate(9), result.getDate(10)));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return dataList;
    }

    private void initTableColumn(){
        col_id.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_name.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_age.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_gender.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_address.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_phone.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_email.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_password.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_dob.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_date_created.setStyle("-fx-alignment: center; -fx-font-size: 15;");


        col_id.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getId()));
        col_name.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        col_age.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAge()));
        col_gender.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getGender()));
        col_address.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAddress()));
        col_phone.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPhone()));
        col_email.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));
        col_password.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPassword()));
        col_dob.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDateOfBirth()));
        col_date_created.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDateCreated()));
    }

    private void refreshTable() throws SQLException {
        initTableColumn();
        ObservableList<Cashier> cashiers = getAllCashier();
        tb_cashier.setItems(cashiers);
        setDefaultIDAndDate();
    }

    public Map<String, Integer> getDateYearDayAndMonth(Date inputDate){
        Map<String, Integer> date = new HashMap<>();

        String dob = String.valueOf(inputDate);

        String year = dob.substring(0, 4);
        String month = dob.substring(5, 7);
        String day = dob.substring(8, 10);

        date.put("year", Integer.parseInt(year));
        date.put("month", Integer.parseInt(month));
        date.put("day", Integer.parseInt(day));
        return date;
    }

    private void getTableDataInTextField(){
        Cashier selectedItem = tb_cashier.getSelectionModel().getSelectedItem();

        String cashierID = selectedItem.getId();
        String cashierName = selectedItem.getName();
        String cashierAge = selectedItem.getAge();
        String cashierGender = selectedItem.getGender();
        String cashierAddress = selectedItem.getAddress();
        String cashierPhone = selectedItem.getPhone();
        String cashierEmail = selectedItem.getEmail();
        String password = selectedItem.getPassword();
        LocalDate dobDate = LocalDate.parse(selectedItem.getDateOfBirth());

        String selectedGender = selectedItem.getGender();

        Toggle selectRadio = null;

        if (selectedGender.equals("Male")){
            selectRadio = rdo_male;
        }else if (selectedGender.equals("Female")){
            selectRadio = rdo_female;
        }

        tf_id.setText(cashierID);
        tf_name.setText(cashierName);
        tf_age.setText(cashierAge);
        gender.getSelectedToggle();
        gender.selectToggle(selectRadio);
        tf_address.setText(cashierAddress);
        tf_phone.setText(cashierPhone);
        tf_email.setText(cashierEmail);
        tf_password.setText("");
        tf_password.setPromptText("Enter New Password");
        df_dob.setValue(dobDate);
    }


    private void updateCashier() throws SQLException {
        if (tb_cashier.getSelectionModel().getSelectedItem() == null){
            return;
        }

        Cashier selectedItem = tb_cashier.getSelectionModel().getSelectedItem();

        RadioButton selectedRadioBtn = (RadioButton) gender.getSelectedToggle();

        ID = selectedItem.getId();
        String newCashierName = tf_name.getText();
        String newCashierAddress = tf_address.getText();
        String newCashierPhone = tf_phone.getText();
        String newCashierEmail = tf_email.getText();
        String password = tf_password.getText();
        String dobDateString = df_dob.getEditor().getText();
        LocalDate dobDate = df_dob.getValue();



        if (newCashierName.isEmpty() || newCashierAddress.isEmpty() || newCashierPhone.isEmpty() || !selectedRadioBtn.isSelected() ||
                newCashierEmail.isEmpty() || password.isEmpty() || dobDateString.isEmpty() || Date.valueOf(dobDate).after(Date.valueOf(todayDate))) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill all required fields, (NB)Date of birth cannot be greater than today !", ButtonType.OK);
            alert.setHeaderText("Fields cannot be empty");
            alert.showAndWait();
            return;
        }

        Date dob = Date.valueOf(df_dob.getValue());

        Map<String, Integer> todayMap = getDateYearDayAndMonth(Date.valueOf(todayDate));
        int todayYear = todayMap.get("year");

        Map<String, Integer> userDOB = getDateYearDayAndMonth(dob);
        int userYear = userDOB.get("year");

        int newCashierAge = todayYear - userYear;

        String newEncryptedPwd = Common.encrypt(password);
        String newCashierGender = selectedRadioBtn.getText();

        query = "UPDATE cashier SET name = '"+newCashierName+"', age = "+newCashierAge+", gender = '"+newCashierGender+"'," +
                " address = '"+newCashierAddress+"', phone = '"+newCashierPhone+"', email = '"+newCashierEmail+"', " +
                "password = '"+newEncryptedPwd+"', date_of_birth = '"+Date.valueOf(dobDate)+"' WHERE cashier_id = '" + ID + "';";
        db.saveData(query);
        refreshTable();
    }


    private void deleteCashier() throws SQLException {
        Cashier selectedItem = tb_cashier.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            return;
        }

//        int supplierCount = 0;
//        query = "select count(*) from product, supplier where product.supplier_id = supplier.id and product.supplier_id = "+selectedItem.getSupplierId()+";";
//        ResultSet result = db.getData(query);
//        while (result.next()) {
//            supplierCount = result.getInt(1);
//        }

//        if (supplierCount > 0) {
//            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot delete! This Supplier has been used in product items. Thanks!");
//            alert.setHeaderText("Cannot delete this Supplier !!!");
//            alert.showAndWait();
//            return;
//        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the account of '" +
                selectedItem.getName()+ "'?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Delete Cashier Account");

        if (alert.showAndWait().get() == ButtonType.YES) {
            String cashierID = selectedItem.getId();
            query = "DELETE FROM cashier WHERE cashier_id = '" + cashierID + "';";
            System.out.println(query);
            db.saveData(query);
            refreshTable();
        }
    }

    private void setDefaultIDAndDate() throws SQLException {
        generateNewCashierID();
        tf_search.clear();
        rdo_female.setSelected(false);
        rdo_male.setSelected(false);
        tf_id.setText(newCashierID);
        tf_name.clear();
        tf_age.clear();
        tf_address.clear();
        tf_phone.clear();
        tf_email.clear();
        tf_password.clear();
        tf_password.setPromptText(" Enter Cashier Password");
        df_dob.setValue(null);
    }


    private void generateNewCashierID() throws SQLException {
        newCashierID = Common.generateCashierID();
    }


    private void searchCashierByIDNamePhone() throws SQLException {
        tf_search.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Cashier> searchList = FXCollections.observableArrayList();
            query = "SELECT * FROM cashier WHERE name like '%"+newValue+"%' or cashier_id like '%"+newValue+"%' or phone like '%"+newValue+"%';";
            System.out.println(query);
            try {
                ResultSet result = db.getData(query);
                searchList.clear();
                while (result.next()) {
                    searchList.add(new Cashier(result.getString(1), result.getString(2), result.getInt(3),
                            result.getString(4), result.getString(5), result.getString(6),
                            result.getString(7), result.getString(8), result.getDate(9), result.getDate(10)));
                }

                tb_cashier.setItems(searchList);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
