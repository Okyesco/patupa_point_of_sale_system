package com.posapp.pos.controller;

import com.posapp.pos.database.DataAccessObject;
import com.posapp.pos.model.Product;
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

public class AdminProductItemController implements Initializable {
    @FXML
    private Button bt_add;

    @FXML
    private Button bt_new;

    @FXML
    private Button bt_update;

    @FXML
    private Button bt_delete;

    @FXML
    private TableColumn<Product, String> col_date_added;

    @FXML
    private TableColumn<Product, String> col_price;

    @FXML
    private TableColumn<Product, String> col_supplier;

    @FXML
    private ComboBox<String> cbo_category;

    @FXML
    private ComboBox<String> cbo_supplier;

    @FXML
    private TableColumn<Product, String> col_bar_code;

    @FXML
    private TableColumn<Product, String> col_cat;

    @FXML
    private TableColumn<Product, String> col_expiry_date;

    @FXML
    private TableColumn<Product, String> col_name;

    @FXML
    private TableColumn<Product, String> col_stock;

    @FXML
    private TableView<Product> tb_product_item;

    @FXML
    private TextField tf_barcode;

//    @FXML
//    private TextField tf_barcode_search;

    @FXML
    private TextField tf_date_added;

    @FXML
    private DatePicker tf_expiry_date;

    @FXML
    private TextField tf_name;

    @FXML
    private TextField tf_name_search;

    @FXML
    private TextField tf_price;

    @FXML
    private TextField tf_stock;


    private String query;
    private DataAccessObject db;
    private int ID;
    private final String todayDate = String.valueOf(new Date(System.currentTimeMillis()));


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        db = new DataAccessObject();
        tf_date_added.setEditable(false);

        Label placeholder = new Label("No Data Available");
        tb_product_item.setPlaceholder(placeholder);

        try {
            getAllCategory();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            getAllSupplier();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            refreshTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        bt_add.setOnAction(event -> {
            try {
                insertNewProduct();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        tb_product_item.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                getTableDataInTextField();
            }
        });

        bt_new.setOnAction(event -> {
            try {
                setDefaultIDAndDate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        bt_update.setOnAction(event -> {
            try {
                updateProduct();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        bt_delete.setOnAction(event -> {
            try {
                deleteProduct();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        searchProductNameAndBarCode();
//        searchProductBarCode();

    }

    private void getAllCategory() throws SQLException {
        String query = "SELECT name FROM category ORDER BY name ASC;";
        ObservableList<String> dataList = FXCollections.observableArrayList();
        try {
            ResultSet result = db.getData(query);
            while (result.next()) {
                dataList.add(result.getString(1));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        cbo_category.setItems(dataList);
    }

    private void getAllSupplier() throws SQLException {
        String query = "SELECT company_name FROM supplier ORDER BY company_name ASC;";
        ObservableList<String> dataList = FXCollections.observableArrayList();
        try {
            ResultSet result = db.getData(query);
            while (result.next()) {
                dataList.add(result.getString(1));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        cbo_supplier.setItems(dataList);
    }

    public int getCategoryID(String categoryName) throws SQLException {
        String query = "SELECT id FROM category WHERE name = '" + categoryName + "';";
        try{
            ResultSet result = db.getData(query);
            while (result.next()) {
                ID = result.getInt(1);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return ID;
    }

    public int getSupplierID(String supplierName) throws SQLException {
        String query = "SELECT id from supplier WHERE company_name = '" + supplierName + "';";
        try {
            ResultSet result = db.getData(query);
            while (result.next()) {
                ID = result.getInt(1);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return ID;
    }

    private void insertNewProduct() throws SQLException {
        String productName = tf_name.getText();
        String barcode = tf_barcode.getText();
        String productCategory = cbo_category.getSelectionModel().getSelectedItem();
        String price = tf_price.getText();
        String supplierName = cbo_supplier.getSelectionModel().getSelectedItem();
        String stock = tf_stock.getText();
        String rawExpiryDate = tf_expiry_date.getEditor().getText();
        LocalDate expiryDate = tf_expiry_date.getValue();
        int supplierID = getSupplierID(supplierName);
        int categoryID = getCategoryID(productCategory);

        if (inputValidationChecker(barcode, productName, productCategory, price, supplierName, stock, rawExpiryDate, expiryDate))
            return;
        query = "INSERT INTO product(barcode, product_name, category_id, price, supplier_id, stock, expiry_date) " +
                "VALUES('" + barcode + "', '"+productName+"', '"+categoryID+"', '"+Double.parseDouble(price)+"', '"+supplierID+"', " +
                "'"+Integer.parseInt(stock)+"', '"+Date.valueOf(expiryDate)+"');";
        db.saveData(query);
        tf_name.clear();
        refreshTable();
    }

    private ObservableList<Product> getAllProducts() throws SQLException {
//        String query = "SELECT * FROM product;";
        String query = "select p.id, p.barcode, p.product_name, p.category_id, c.name as category_name, p.price, p.supplier_id, s.company_name as supplier_name, p.date_added, p.stock, p.expiry_date\n" +
                "from product as p inner join category as c on p.category_id = c.id inner join supplier as s on p.supplier_id=s.id order by p.product_name;";
        ObservableList<Product> dataList = FXCollections.observableArrayList();
        try {
            ResultSet result = db.getData(query);
            while (result.next()) {
                dataList.add(new Product(result.getInt(1), result.getString(2),
                        result.getString(3), result.getInt(4), result.getString(5),
                        result.getDouble(6), result.getInt(7), result.getString(8),
                        result.getDate(9), result.getInt(10), result.getDate(11)));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return dataList;
    }

    private void initTableColumn(){
        col_bar_code.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_name.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_cat.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_supplier.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_price.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_date_added.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_expiry_date.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_stock.setStyle("-fx-alignment: center; -fx-font-size: 15;");


        col_bar_code.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProductBarcode()));
        col_name.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProductName()));
        col_cat.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProductCategoryName()));
        col_price.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProductPrice()));
        col_supplier.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProductSupplierName()));
        col_date_added.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProductAddedDate()));
        col_expiry_date.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProductExpiryDate()));
        col_stock.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProductQuantity()));

    }

    private void refreshTable() throws SQLException {
        initTableColumn();
        ObservableList<Product> products = getAllProducts();
        tb_product_item.setItems(products);
        setDefaultIDAndDate();
    }

    private void getTableDataInTextField(){
        Product selectedItem = tb_product_item.getSelectionModel().getSelectedItem();
        String productName = selectedItem.getProductName();
        String barcode = selectedItem.getProductBarcode();
        String productID = selectedItem.getProductId();
        ID = Integer.parseInt(productID);
        String  productCategory = selectedItem.getProductCategoryName();
        String  productPrice = selectedItem.getProductPrice();
        String  supplierName = selectedItem.getProductSupplierName();
        String stock = selectedItem.getProductQuantity();
        String dateAdded = selectedItem.getProductAddedDate();
        String expiryDate = selectedItem.getProductExpiryDate();


        tf_barcode.setText(barcode);
        tf_name.setText(productName);
        cbo_category.getSelectionModel().select(productCategory);
        tf_price.setText(productPrice);
        cbo_supplier.getSelectionModel().select(supplierName);
        tf_date_added.setText(dateAdded);
        tf_stock.setText(stock);
        tf_expiry_date.setValue(LocalDate.parse(expiryDate));
    }

    private void updateProduct() throws SQLException {
        if (tb_product_item.getSelectionModel().getSelectedItem() == null){
            return;
        }

        Product selectedItem = tb_product_item.getSelectionModel().getSelectedItem();
        String newBarcode = tf_barcode.getText();
        String newProductName = tf_name.getText();
        String newCategory = cbo_category.getSelectionModel().getSelectedItem();
        String newPrice = tf_price.getText();
        String newSupplier = cbo_supplier.getSelectionModel().getSelectedItem();
        String newStock = tf_stock.getText();
        String newRawExpiryDate = tf_expiry_date.getEditor().getText();
        LocalDate newExpiryDate = tf_expiry_date.getValue();
        int newSupplierID = getSupplierID(newSupplier);
        int newCategoryID = getCategoryID(newCategory);


        if (inputValidationChecker(newBarcode, newProductName, newCategory, newPrice, newSupplier, newStock, newRawExpiryDate, newExpiryDate))
            return;
        ID = Integer.parseInt(selectedItem.getProductId());
        query = "UPDATE product SET barcode = '"+newBarcode+"', product_name = '"+newProductName+"', category_id = "+ newCategoryID +", " + "price = "+Double.parseDouble(newPrice)+", supplier_id = "+newSupplierID+", stock = "+Integer.parseInt(newStock)+", expiry_date = '"+newExpiryDate+"' WHERE id = " + ID + ";";
        db.saveData(query);
        refreshTable();
    }

    private boolean inputValidationChecker(String newBarcode, String newProductName, String newCategory, String newPrice, String newSupplier, String newStock, String newRawExpiryDate, LocalDate newExpiryDate) {
        if (newProductName.isEmpty() || newBarcode.isEmpty() || newCategory.isEmpty() || Double.parseDouble(newPrice) <= 0 ||
                newSupplier.isEmpty() || Integer.parseInt(newStock) <= 0 || newPrice.isEmpty() || newStock.isEmpty() || newRawExpiryDate.isEmpty() ||
                Date.valueOf(newExpiryDate).before(Date.valueOf(todayDate))) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter appropriate expiry date and fields !", ButtonType.OK);
            alert.setHeaderText("Fields cannot be empty");
            alert.showAndWait();
            return true;
        }
        return false;
    }

    private void deleteProduct() throws SQLException {
        Product selectedItem = tb_product_item.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete '" +
                selectedItem.getProductName()+ "' from products?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Delete Product Item");

        if (alert.showAndWait().get() == ButtonType.YES) {
            String productID = selectedItem.getProductId();
            query = "DELETE FROM product WHERE id = '" + productID + "';";
            db.saveData(query);
            refreshTable();
        }
    }



    private void setDefaultIDAndDate() throws SQLException {
        tf_name_search.clear();
//        tf_barcode_search.clear();
        tf_barcode.clear();
        tf_name.clear();
        tf_price.clear();
        tf_stock.clear();
        cbo_supplier.getSelectionModel().clearSelection();
        cbo_category.getSelectionModel().clearSelection();
        tf_date_added.setText(String.valueOf(todayDate));
        tf_expiry_date.setValue(null);
    }


    private void searchProductNameAndBarCode(){
        tf_name_search.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Product> searchList = FXCollections.observableArrayList();
            query = "select p.id, p.barcode, p.product_name, p.category_id, c.name as category_name, p.price, p.supplier_id, s.company_name as supplier_name, p.date_added, p.stock, p.expiry_date\n" +
                "from product as p inner join category as c on p.category_id = c.id inner join supplier as s on p.supplier_id=s.id WHERE product_name like '%"+newValue+"%' or barcode like '%"+newValue+"%' order by p.product_name;";

            try {
                ResultSet result = db.getData(query);
                searchList.clear();

                while (result.next()) {
                    searchList.add(new Product(result.getInt(1), result.getString(2),
                        result.getString(3), result.getInt(4), result.getString(5),
                        result.getLong(6), result.getInt(7), result.getString(8),
                        result.getDate(9), result.getInt(10), result.getDate(11)));
                }

                tb_product_item.setItems(searchList);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }


//        private void searchProductBarCode(){
//        tf_barcode_search.textProperty().addListener((observable, oldValue, newValue) -> {
//            ObservableList<Product> searchList = FXCollections.observableArrayList();
//            query = "select p.id, p.barcode, p.product_name, p.category_id, c.name as category_name, p.price, p.supplier_id, s.company_name as supplier_name, p.date_added, p.stock, p.expiry_date\n" +
//                "from product as p inner join category as c on p.category_id = c.id inner join supplier as s on p.supplier_id=s.id WHERE barcode like '%"+newValue+"%' order by p.product_name;";
//
//            try {
//                ResultSet result = db.getData(query);
//                searchList.clear();
//
//                while (result.next()) {
//                    searchList.add(new Product(result.getInt(1), result.getString(2),
//                        result.getString(3), result.getInt(4), result.getString(5),
//                        result.getLong(6), result.getInt(7), result.getString(8),
//                        result.getDate(9), result.getInt(10), result.getDate(11)));
//                }
//
//                tb_product_item.setItems(searchList);
//
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }

}
