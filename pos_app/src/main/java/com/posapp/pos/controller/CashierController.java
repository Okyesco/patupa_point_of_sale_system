package com.posapp.pos.controller;

import com.posapp.pos.Main;
import com.posapp.pos.common.Common;
import com.posapp.pos.database.DataAccessObject;
import com.posapp.pos.model.Product;
import com.posapp.pos.model.Sale;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class CashierController implements Initializable {
    @FXML
    private Label app_name_label;

    @FXML
    private Button btPrint;

    @FXML
    private Button bt_create_card;

    @FXML
    private Button bt_logout;

    @FXML
    private Button bt_refresh_product_table;

    @FXML
    private Button bt_new;

    @FXML
    private Button bt_pay;

    @FXML
    private Button bt_redeem;

    @FXML
    private Label lb_cashier_name;

    @FXML
    private Label lb_slip_no;

    @FXML
    private Label lb_store_name;

    @FXML
    private TableColumn<Product, String> product_col_barcode;

    @FXML
    private TableColumn<Product, String> product_col_cat;

    @FXML
    private TableColumn<Product, String> product_col_name;

    @FXML
    private TableColumn<Product, String> product_col_price;

    @FXML
    private TableColumn<Product, String> product_col_stock;

    @FXML
    private TableColumn<Sale, String> sale_col_barcode;

    @FXML
    private TableColumn<Sale, String> sale_col_discount;

    @FXML
    private TableColumn<Sale, String> sale_col_name;

    @FXML
    private TableColumn<Sale, String> sale_col_price;

    @FXML
    private TableColumn<Sale, String> sale_col_quantity;

    @FXML
    private TableColumn<Sale, String> sale_col_total;

    @FXML
    private TableView<Sale> tb_sale;

    @FXML
    private TableView<Product> tb_total_item;


    @FXML
    private TextField tf_change;

    @FXML
    private TextField tf_name_search;

    @FXML
    private TextField tf_pay_amount;

    @FXML
    private TextField tf_total;

    private Stage stage;
    private DataAccessObject db;
    private String query;
    private int ID;
    private final String todayDate = String.valueOf(new Date(System.currentTimeMillis()));
    private final String time = String.valueOf(new Timestamp(System.currentTimeMillis()));
    ObservableList<Sale> sales = FXCollections.observableArrayList();
    double totalAmount = 0;
    double payAmount;
    double change;
    int dbStock;
    int slipNumber;
    private static final int MAX_LINES_PER_PAGE = 40;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        db = new DataAccessObject();
        app_name_label.setText(Common.appName);
        lb_cashier_name.setText(Common.cashierRecord.getName());
        lb_store_name.setText(Common.storeName);

        tb_sale.setEditable(true);

        tf_total.setStyle("-fx-text-fill: red; -fx-font-size: 15; -fx-font-weight: bolder;");
        tf_pay_amount.setStyle("-fx-text-fill: blue; -fx-font-size: 15; -fx-font-weight: bolder;");
        tf_change.setStyle("-fx-text-fill: green; -fx-font-size: 15; -fx-font-weight: bolder;");

        searchProductNameAndBarcode();

        Label placeholder = new Label("No Data Available");
        tb_total_item.setPlaceholder(placeholder);
        tb_sale.setPlaceholder(placeholder);

        sale_col_quantity.setCellFactory(TextFieldTableCell.forTableColumn());
        editQuantityCell();

        try {
            getSlipNumber();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        bt_refresh_product_table.setOnAction(event -> {
            try {
                tf_name_search.clear();
                refreshTable();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            ;
        });

        btPrint.setOnAction(event -> {
            try {
                printSalesReceipt();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            refreshTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        tb_total_item.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                createSaleItem();
            }
        });

        bt_pay.setOnAction(event -> {
            try {
                payWithoutReceipt();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        setChangeAmount();

        bt_new.setOnAction(event -> {
            clearSaleCart();
        });

        setChangeAmount();

        setupDeleteContextMenu();

    }

    private void getSlipNumber() throws SQLException {
        query = "select count(*) from transaction;";
        ResultSet result = db.getData(query);

        if (result.next()) {
            slipNumber = result.getInt(1) + 1;
        }else {
            slipNumber = 1;
        }

        lb_slip_no.setText(String.valueOf(slipNumber));


    }

    private void setupDeleteContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Remove from cart");
        deleteItem.setOnAction(event -> {
            Sale selectedSale = tb_sale.getSelectionModel().getSelectedItem();
            if (selectedSale != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to remove " + selectedSale.getName() + "?");
                if (alert.showAndWait().get() == ButtonType.OK) {
                    sales.remove(selectedSale);
                    getSaleCartTotal();
                }
            }
        });
        contextMenu.getItems().add(deleteItem);
        tb_sale.setContextMenu(contextMenu);
    }

    private void editQuantityCell() {
        sale_col_quantity.setOnEditCommit(event -> {
            Sale sale = event.getRowValue();
            String newQuantity = event.getNewValue();
            try {
                int quantity = Integer.parseInt(newQuantity);
                sale.setQuantity(quantity);
                sale.setTotalAmount(quantity * Double.parseDouble(sale.getUnitAmount()));
                tb_sale.refresh();
                getSaleCartTotal();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid integer for quantity.");
                alert.showAndWait();
            }
        });
    }


    @FXML
    void onLogoutAction(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("You will be logged out of the program");
            alert.setContentText("Are you sure you want to logout?");
            if (alert.showAndWait().get() == ButtonType.OK){
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                new Main().start(stage);
            }

        } catch (Exception e) {
        System.out.println(e.getMessage());
        }

        setChangeAmount();
    }

    private ObservableList<Product> getAllProducts() throws SQLException {
        String query = "select p.id, p.barcode, p.product_name, p.category_id, c.name as category_name, p.price, p.supplier_id, s.company_name as supplier_name, p.date_added, p.stock, p.expiry_date\n" +
                "from product as p inner join category as c on p.category_id = c.id inner join supplier as s on p.supplier_id=s.id where p.stock >= 1 order by p.product_name;";
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
        product_col_barcode.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        product_col_name.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        product_col_cat.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        product_col_price.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        product_col_stock.setStyle("-fx-alignment: center; -fx-font-size: 15;");

        product_col_barcode.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProductBarcode()));
        product_col_name.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProductName()));
        product_col_cat.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProductCategoryName()));
        product_col_price.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProductPrice()));
        product_col_stock.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProductQuantity()));

    }

    private void refreshTable() throws SQLException {
        initTableColumn();
        ObservableList<Product> products = getAllProducts();
        tb_total_item.setItems(products);
    }


    private void createSaleItem() {
        ObservableList<Sale> dataList = FXCollections.observableArrayList();

        Product selectedProduct = tb_total_item.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Product Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a product to add to the sale.");
            alert.showAndWait();
            return;
        }

        String productName = selectedProduct.getProductName();
        String barcode = selectedProduct.getProductBarcode();
        String productID = selectedProduct.getProductId();
        ID = Integer.parseInt(productID);
        double productPrice = Double.parseDouble(selectedProduct.getProductPrice());
        int productStock = Integer.parseInt(selectedProduct.getProductQuantity());
        int productQuantity = 1;
        double discount = 0;
        double totalAmount = productPrice;

        boolean productExists = false;

        for (Sale sale : sales) {
            if (Objects.equals(sale.getName(), productName) && Objects.equals(sale.getBarcode(), barcode) && (productStock > 0)) {
                if (Integer.parseInt(sale.getQuantity()) >= productStock) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Cannot add anymore");
                    alert.setHeaderText(null);
                    alert.setContentText("You have not enough product to add any more.");
                    alert.showAndWait();
                    return;
                }

                int newQuantity = Integer.parseInt(sale.getQuantity()) + 1;
                sale.setQuantity(newQuantity);
                sale.setTotalAmount(newQuantity * Double.parseDouble(sale.getUnitAmount()));
                productExists = true;
                tb_sale.refresh();
                break;
            }
        }

        if (!productExists) {
            if (productStock <= 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Out of stock");
                alert.setHeaderText(null);
                alert.setContentText("Product is out of stock !!!");
                alert.showAndWait();
                return;
            }
            dataList.add(new Sale(ID, barcode, productName, productStock, productPrice, productQuantity, discount, totalAmount));
            sales.addAll(dataList);
        }

        sale_col_barcode.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        sale_col_name.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        sale_col_price.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        sale_col_quantity.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        sale_col_discount.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        sale_col_total.setStyle("-fx-alignment: center; -fx-font-size: 15;");

        sale_col_barcode.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBarcode()));
        sale_col_name.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        sale_col_price.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getUnitAmount()));
        sale_col_quantity.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getQuantity()));
        sale_col_discount.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDiscount()));
        sale_col_total.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTotalAmount()));

        tb_sale.setItems(sales);
        getSaleCartTotal();
    }


    private void removeItemFromSalesCart(){
        Sale selectedSale = tb_sale.getSelectionModel().getSelectedItem();

        sales.remove(selectedSale);

        sale_col_barcode.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        sale_col_name.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        sale_col_price.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        sale_col_quantity.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        sale_col_discount.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        sale_col_total.setStyle("-fx-alignment: center; -fx-font-size: 15;");

        sale_col_barcode.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBarcode()));
        sale_col_name.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        sale_col_price.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getUnitAmount()));
        sale_col_quantity.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getQuantity()));
        sale_col_discount.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDiscount()));
        sale_col_total.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTotalAmount()));



        tb_sale.setItems(sales);
        getSaleCartTotal();

    }


    private void clearSaleCart(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Clear Cart Item(s)");
        alert.setContentText("Are you sure you want to delete this sale cart? This will remove all cart items.");
        if (alert.showAndWait().get() == ButtonType.OK){
            sales.clear();
            tb_sale.setItems(sales);
            getSaleCartTotal();
            change = 0;
            totalAmount = 0;
            payAmount = 0;
            tf_pay_amount.clear();
            tf_change.clear();
            tf_total.clear();

        }
    }



    private void searchProductNameAndBarcode(){
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
                            result.getDouble(6), result.getInt(7), result.getString(8),
                            result.getDate(9), result.getInt(10), result.getDate(11)));
                }

                tb_total_item.setItems(searchList);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private void getSaleCartTotal() {
        totalAmount = 0;
        for (Sale sale : sales) {
            totalAmount += Double.parseDouble(sale.getTotalAmount());
        }
        tf_total.setText(String.valueOf(totalAmount));
        updateChangeAmount();
    }

    private void updateChangeAmount() {
        try {
            payAmount = Double.parseDouble(tf_pay_amount.getText());
        } catch (NumberFormatException e) {
            payAmount = 0;
        }
        change = payAmount - totalAmount;
        tf_change.setText(String.format("%.2f%n", change));
        System.out.printf("%.2f%n", change);
    }

    private void setChangeAmount() {
        tf_pay_amount.textProperty().addListener((observable, oldValue, newValue) -> {
            updateChangeAmount();
        });
    }

    private ImageView loadCompanyLogo() {
        Image logoImage = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/new_logo.png")));
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(40);
        logoImageView.setPreserveRatio(true);
        return logoImageView;
    }


//    private VBox createPrintContent() {
//        VBox printContent = new VBox();
//        printContent.setSpacing(10);
//        printContent.setPadding(new Insets(10));
//
//
//        ImageView logoImageView = loadCompanyLogo();
//        printContent.getChildren().add(logoImageView);
//
//
//        StringBuilder content = new StringBuilder();
//        content.append("         ").append(Common.storeName.toUpperCase()).append("\n");
//        content.append("========================================\n");
//        content.append("             Sales Receipt\n");
//        content.append("========================================\n");
//        content.append("Transaction ID: ").append(Common.transactionId).append("\n");
//        content.append("Cashier: ").append(Common.cashierRecord.getName().toUpperCase()).append("\n");
//        content.append("Time: ").append(time).append("\n");
//        content.append("========================================\n");
//        for (Sale sale : sales) {
//            content.append(String.format("Product: %s\n", sale.getName()));
//            content.append(String.format("Barcode: %s\n", sale.getBarcode()));
//            content.append(String.format("Unit Price: GHS%.2f\n", Double.parseDouble(sale.getUnitAmount())));
//            content.append(String.format("Quantity: %d\n", Integer.parseInt(sale.getQuantity())));
//            content.append(String.format("Discount: %.2f\n", Double.parseDouble(sale.getDiscount())));
//            content.append(String.format("Total: GHS%.2f\n", Double.parseDouble(sale.getTotalAmount())));
//            content.append("----------------------------------------\n");
//        }
//        content.append(String.format("Total Amount: GHS%.2f\n", totalAmount));
//        content.append(String.format("Pay Amount: GHS%.2f\n", payAmount));
//        content.append(String.format("Change: GHS%.2f\n", change));
//        content.append("========================================\n\n");
//        content.append("Thank you for shopping with us!\n");
//
//        TextArea printTextArea = new TextArea(content.toString());
//        printTextArea.setWrapText(true);
//        printTextArea.setFont(new Font("Courier New", 12));
//        printTextArea.setEditable(false);
//
//        printContent.getChildren().add(printTextArea);
//
//        return printContent;
//    }


//    private void printSalesReceipt() throws SQLException {
//        PrinterJob printerJob = PrinterJob.createPrinterJob();
//        if (payAmount - totalAmount < 0 || totalAmount == 0){
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Payment Error");
//            alert.setHeaderText(null);
//            alert.setContentText("Please enter a valid amount. Payment amount should be greater or equal to total amount !!!");
//            alert.showAndWait();
//            return;
//        }
//
//        if (printerJob != null) {
//            Common.transactionId = Common.generateTransactionID();
//
//            for (Sale sale: sales){
//                String productInStockQuery = "SELECT stock FROM product WHERE id = "+sale.getProductID()+";";
//                ResultSet result = db.getData(productInStockQuery);
//                int dbAvailableStock = 0;
//                if (result.next()) {
//                    dbAvailableStock = result.getInt(1);
//                }
//                dbStock = dbAvailableStock;
////
//                if (Integer.parseInt(sale.getProductStock()) <= 0 || dbAvailableStock <= 0 || (dbAvailableStock - Integer.parseInt(sale.getQuantity()) < 0)){
//                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setTitle("Product Not Available");
//                    alert.setHeaderText(null);
//                    alert.setContentText("Sorry, Product Not Available");
//                    alert.setContentText("Sorry, '"+ sale.getName().toUpperCase()+ "' is out of stock !!!. We only have "+dbAvailableStock+" in stock.");
//                    alert.showAndWait();
//                    return;
//                }
//
//                ID = sale.getProductID();
//                int quantity = Integer.parseInt(sale.getQuantity());
//                int productStock = Integer.parseInt(sale.getProductStock());
//
//                int newStock = productStock - quantity;
//
//                String updateQuery = "UPDATE product set stock = "+newStock+" WHERE id = "+ID+";";
//
//                db.saveData(updateQuery);
//
//            }
//
//            query = "INSERT INTO transaction(id, cashier_id, amount) values ('" + Common.transactionId + "', '"+Common.cashierRecord.getId()+"',  " + totalAmount + ");";
//            db.saveData(query);
//            db.saveSalesToDatabase(sales);
//            refreshTable();
//
//            if (!DataAccessObject.completed)
//                return;
//            boolean proceed = printerJob.showPrintDialog(stage);
//            if (proceed) {
//                VBox printContent = createPrintContent();
//                boolean printed = printerJob.printPage(printContent);
//                if (printed) {
//                    resetAfterPayment();
//                    printerJob.endJob();
//                } else {
//                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setTitle("Print Error");
//                    alert.setHeaderText(null);
//                    alert.setContentText("Failed to print the sales receipt.");
//                    alert.showAndWait();
//                }
//            }
//        }
//    }

    private void payWithoutReceipt() throws SQLException {
        if (payAmount - totalAmount < 0 || totalAmount == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Payment Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid amount. Payment amount should be greater or equal to total amount !!!");
            alert.showAndWait();
            return;
        }


        Common.transactionId = Common.generateTransactionID();

        for (Sale sale: sales){
            String productInStockQuery = "SELECT stock FROM product WHERE id = "+sale.getProductID()+";";
            ResultSet result = db.getData(productInStockQuery);
            int dbAvailableStock = 0;
            if (result.next()) {
                dbAvailableStock = result.getInt(1);
            }
            dbStock = dbAvailableStock;

            if (Integer.parseInt(sale.getProductStock()) <= 0 || dbAvailableStock <= 0 || (dbAvailableStock - Integer.parseInt(sale.getQuantity()) < 0)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Product Not Available");
                alert.setHeaderText(null);
                alert.setContentText("Sorry, Product Not Available");
                alert.setContentText("Sorry, '"+ sale.getName().toUpperCase()+ "' is out of stock !!!. We only have "+dbAvailableStock+" in stock.");
                alert.showAndWait();
                return;
            }

            ID = sale.getProductID();
            int quantity = Integer.parseInt(sale.getQuantity());
            int productStock = Integer.parseInt(sale.getProductStock());

            int newStock = Math.max(productStock - quantity, 0);

            String updateQuery = "UPDATE product set stock = "+newStock+" WHERE id = "+ID+";";

            db.saveData(updateQuery);

        }

        query = "INSERT INTO transaction(id, cashier_id, amount) values ('" + Common.transactionId + "', '"+Common.cashierRecord.getId()+"',  " + totalAmount + ");";
        db.saveData(query);
        db.saveSalesToDatabase(sales);
        refreshTable();

        if (!DataAccessObject.completed)
            return;

        resetAfterPayment();


    }

    private void resetAfterPayment() throws SQLException {
        sales.clear();
        tb_sale.setItems(sales);
        getSaleCartTotal();
        change = 0;
        totalAmount = 0;
        payAmount = 0;
        tf_pay_amount.clear();
        tf_change.clear();
        tf_total.clear();
        getSlipNumber();
    }


    private void printSalesReceipt() throws SQLException {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (payAmount - totalAmount < 0 || totalAmount == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Payment Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid amount. Payment amount should be greater or equal to total amount !!!");
            alert.showAndWait();
            return;
        }

        if (printerJob != null) {
            Common.transactionId = Common.generateTransactionID();

            for (Sale sale : sales) {
                String productInStockQuery = "SELECT stock FROM product WHERE id = " + sale.getProductID() + ";";
                ResultSet result = db.getData(productInStockQuery);
                int dbAvailableStock = 0;
                if (result.next()) {
                    dbAvailableStock = result.getInt(1);
                }
                dbStock = dbAvailableStock;

                if (Integer.parseInt(sale.getProductStock()) <= 0 || dbAvailableStock <= 0 || (dbAvailableStock - Integer.parseInt(sale.getQuantity()) < 0)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Product Not Available");
                    alert.setHeaderText(null);
                    alert.setContentText("Sorry, Product Not Available");
                    alert.setContentText("Sorry, '" + sale.getName().toUpperCase() + "' is out of stock !!!. We only have " + dbAvailableStock + " in stock.");
                    alert.showAndWait();
                    return;
                }

                ID = sale.getProductID();
                int quantity = Integer.parseInt(sale.getQuantity());
                int productStock = Integer.parseInt(sale.getProductStock());

                int newStock = Math.max(productStock - quantity, 0);

                String updateQuery = "UPDATE product set stock = " + newStock + " WHERE id = " + ID + ";";
                db.saveData(updateQuery);
            }

            query = "INSERT INTO transaction(id, cashier_id, amount) values ('" + Common.transactionId + "', '" + Common.cashierRecord.getId() + "', " + totalAmount + ");";
            db.saveData(query);
            db.saveSalesToDatabase(sales);
            refreshTable();

            if (!DataAccessObject.completed) return;

            boolean proceed = printerJob.showPrintDialog(stage);
            if (proceed) {
                List<String> pages = splitContentIntoPages(createPrintContentString());
                for (String pageContent : pages) {
                    VBox printContent = createPrintContent(pageContent);
                    boolean printed = printerJob.printPage(printContent);
                    if (!printed) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Print Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to print the sales receipt.");
                        alert.showAndWait();
                        break;
                    }
                }
                resetAfterPayment();
                printerJob.endJob();
            }
        }
    }

    private List<String> splitContentIntoPages(String content) {
        List<String> pages = new ArrayList<>();
        String[] lines = content.split("\n");
        StringBuilder currentPage = new StringBuilder();
        int lineCount = 0;

        for (String line : lines) {
            if (lineCount >= MAX_LINES_PER_PAGE) {
                pages.add(currentPage.toString());
                currentPage = new StringBuilder();
                lineCount = 0;
            }
            currentPage.append(line).append("\n");
            lineCount++;
        }
        if (currentPage.length() > 0) {
            pages.add(currentPage.toString());
        }
        return pages;
    }

    private VBox createPrintContent(String pageContent) {
        VBox printContent = new VBox();
        printContent.setSpacing(10);
        printContent.setPadding(new Insets(10));

        ImageView logoImageView = loadCompanyLogo();
        printContent.getChildren().add(logoImageView);

        TextArea printTextArea = new TextArea(pageContent);
        printTextArea.setWrapText(true);
        printTextArea.setFont(new Font("Courier New", 12));
        printTextArea.setEditable(false);

        printContent.getChildren().add(printTextArea);
        return printContent;
    }

    private String createPrintContentString() {
        StringBuilder content = new StringBuilder();
        content.append("         ").append(Common.storeName.toUpperCase()).append("\n");
        content.append("========================================\n");
        content.append("             Sales Receipt\n");
        content.append("========================================\n");
        content.append("Transaction ID: ").append(Common.transactionId).append("\n");
        content.append("Cashier: ").append(Common.cashierRecord.getName().toUpperCase()).append("\n");
        content.append("Time: ").append(time).append("\n");
        content.append("========================================\n");
        for (Sale sale : sales) {
            content.append(String.format("Product: %s\n", sale.getName()));
            content.append(String.format("Barcode: %s\n", sale.getBarcode()));
            content.append(String.format("Unit Price: GHS%.2f\n", Double.parseDouble(sale.getUnitAmount())));
            content.append(String.format("Quantity: %d\n", Integer.parseInt(sale.getQuantity())));
            content.append(String.format("Discount: %.2f\n", Double.parseDouble(sale.getDiscount())));
            content.append(String.format("Total: GHS%.2f\n", Double.parseDouble(sale.getTotalAmount())));
            content.append("----------------------------------------\n");
        }
        content.append(String.format("Total Amount: GHS%.2f\n", totalAmount));
        content.append(String.format("Pay Amount: GHS%.2f\n", payAmount));
        content.append(String.format("Change: GHS%.2f\n", change));
        content.append("========================================\n\n");
        content.append("Thank you for shopping with us!\n");
        return content.toString();
    }



}
