package com.posapp.pos.controller;

import com.posapp.pos.database.DataAccessObject;
import com.posapp.pos.model.PopularItem;
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

public class AdminPopularItemController implements Initializable {

    @FXML
    private TableColumn<PopularItem, String> col_barcode;

    @FXML
    private TableColumn<PopularItem, String> col_cat;

    @FXML
    private TableColumn<PopularItem, String> col_count;

    @FXML
    private TableColumn<PopularItem, String> col_date_added;

    @FXML
    private TableColumn<PopularItem, String> col_expiry_date;

    @FXML
    private TableColumn<PopularItem, String> col_name;

    @FXML
    private TableColumn<PopularItem, String> col_price;

    @FXML
    private TableColumn<PopularItem, String> col_stock;

    @FXML
    private TableColumn<PopularItem, String> col_sup;

    @FXML
    private TableView<PopularItem> tb_popular;

    private DataAccessObject db;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        db = new DataAccessObject();

        Label placeholder = new Label("No Data Available");
        tb_popular.setPlaceholder(placeholder);

        try {
            refreshTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private ObservableList<PopularItem> getPopularItems() throws SQLException {
        String query = "select pr.barcode, pr.product_name, c.name as CatName, pr.price, s.company_name, pr.date_added, pr.stock, pr.expiry_date, count(p.product_id) as count  from purchase as p join product as pr on " +
                "p.product_id = pr.id join category as c on pr.category_id = c.id join supplier as s on pr.supplier_id = s.id group by pr.product_name order by count desc limit 25;";
        
        ObservableList<PopularItem> dataList = FXCollections.observableArrayList();
        try {
            ResultSet result = db.getData(query);
            while (result.next()) {
                dataList.add(new PopularItem(result.getString(1), result.getString(2), result.getString(3),
                        result.getDouble(4), result.getString(5), result.getDate(6), result.getInt(7), result.getDate(8), result.getInt(9)));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return dataList;
    }

    private void initTableColumn(){
        col_barcode.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_name.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_cat.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_price.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_sup.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_date_added.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_stock.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_expiry_date.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        col_count.setStyle("-fx-alignment: center; -fx-font-size: 15;");


        col_barcode.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBarcode()));
        col_name.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProductName()));
        col_cat.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategory()));
        col_price.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPrice()));
        col_sup.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSupplierName()));
        col_date_added.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDateAdded()));
        col_stock.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStock()));
        col_expiry_date.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getExpiryDate()));
        col_count.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCount()));




    }

    private void refreshTable() throws SQLException {
        initTableColumn();
        ObservableList<PopularItem> allPopularItems = getPopularItems();
        tb_popular.setItems(allPopularItems);
    }

}



