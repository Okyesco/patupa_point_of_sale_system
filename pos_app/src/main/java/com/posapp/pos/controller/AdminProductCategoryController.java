package com.posapp.pos.controller;

import com.posapp.pos.database.DataAccessObject;
import com.posapp.pos.model.Category;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminProductCategoryController implements Initializable {
    @FXML
    private TextField tf_name, tf_date, tf_search;

    @FXML
    private Button bt_add, bt_new, bt_update, bt_delete;

    @FXML
    private TableView<Category> tb_category;

    @FXML
    TableColumn<Category, String> column_name, column_id, column_date;



    private String query;
    private DataAccessObject db;
    private int ID;
//    private String newCategoryID;
    private final String todayDate = String.valueOf(new Date(System.currentTimeMillis()));


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        db = new DataAccessObject();

//        tf_id.setEditable(false);
        tf_date.setEditable(false);

        Label placeholder = new Label("No Data Available");
        tb_category.setPlaceholder(placeholder);

        searchCategoryName();


//        try {
//            getLastCategoryID();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }


        try {
            setDefaultIDAndDate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        tb_category.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                if (tb_category.getSelectionModel().getSelectedItem() != null) {
                    getTableDataInTextField();
                }

            }
        });


        bt_new.setOnAction(event -> {
            try {
                resetTextField();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });


        bt_add.setOnAction(event -> {
            try {
                insertNewCategory();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });


        try {
            refreshTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        bt_update.setOnAction(event -> {

            try {
                updateCategory();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });


        bt_delete.setOnAction(event -> {
            try {
                deleteCategory();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void insertNewCategory() throws SQLException {
        String cat_name = tf_name.getText();
        if (cat_name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter category name !", ButtonType.OK);
            alert.setHeaderText("Category name cannot be empty");
            alert.showAndWait();
            return;
        }
        query = "INSERT INTO category(name) VALUES('" + cat_name + "')";
        db.saveData(query);
        tf_name.clear();
        refreshTable();
    }

    private ObservableList<Category> getAllCategory() throws SQLException {
        String query = "SELECT * FROM category ORDER BY name ASC;";
        ObservableList<Category> dataList = FXCollections.observableArrayList();
        try {
            ResultSet result = db.getData(query);
            while (result.next()) {
                dataList.add(new Category(result.getInt(1), result.getString(2), result.getDate(3)));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return dataList;
    }

    private void initTableColumn(){
//        column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        column_name.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        column_id.setStyle("-fx-alignment: center; -fx-font-size: 15;");
        column_date.setStyle("-fx-alignment: center; -fx-font-size: 15;");

        column_id.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategoryId()));
        column_name.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategoryName()));
        column_date.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDateCreated()));

    }

    private void refreshTable() throws SQLException {
        initTableColumn();
        ObservableList<Category> categories = getAllCategory();
        tb_category.setItems(categories);
        setDefaultIDAndDate();
    }

    private void getTableDataInTextField(){
        Category selectedItem = tb_category.getSelectionModel().getSelectedItem();
        String cat_name = selectedItem.getCategoryName();
        String cat_id = selectedItem.getCategoryId();
        ID = Integer.parseInt(cat_id);
        String cat_date = selectedItem.getDateCreated();

//        tf_id.setText(cat_id);
        tf_name.setText(cat_name);
        tf_date.setText(cat_date);
    }

    private void updateCategory() throws SQLException {
        Category selectedItem = tb_category.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }

        String newName = tf_name.getText();
        if (newName.isEmpty()) {
            return;
        }
        query = "UPDATE category SET name = '"+newName+"' WHERE id = '" + ID + "';";
        db.saveData(query);
        tf_name.clear();
        tf_search.clear();
//        tf_id.setText(newCategoryID);
        tf_date.setText(todayDate);
        refreshTable();
    }

    private void deleteCategory() throws SQLException {
        Category selectedItem = tb_category.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }

        int categoryCount = 0;
        query = "select count(*) from product, category where product.category_id = category.id and product.category_id = "+selectedItem.getCategoryId()+";";
        ResultSet result = db.getData(query);
        while (result.next()) {
            categoryCount = result.getInt(1);
        }

        if (categoryCount > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot delete! This Category has been used in product items. Thanks!");
            alert.setHeaderText("Cannot delete this Category !!!");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete '" +
                selectedItem.getCategoryName()+ "' from category?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Delete Category Item");

        if (alert.showAndWait().get() == ButtonType.YES) {
            String cat_id = selectedItem.getCategoryId();
            query = "DELETE FROM category WHERE id = '" + cat_id + "';";
            db.saveData(query);
            refreshTable();
            resetTextField();
        }
    }

    private void resetTextField() throws SQLException {
//        getLastCategoryID();
//        tf_id.setText(newCategoryID);
        tf_search.clear();
        tf_name.clear();
        tf_date.setText(todayDate);
    }

//    private void getLastCategoryID() throws SQLException {
//        query = "SELECT id FROM category ORDER BY id DESC LIMIT 1;";
//        ResultSet result = db.getData(query);
//
//        if (!result.next()){
//            newCategoryID = "1";
//            return;
//        }
//
//        while (result.next()) {
//            newCategoryID = String.valueOf(result.getInt(1) + 1);
//
//        }
//    }

    private void setDefaultIDAndDate() throws SQLException {
//        getLastCategoryID();
//        tf_id.setText(newCategoryID);
        tf_search.clear();
        tf_name.clear();
        tf_date.setText(todayDate);
    }


    private void searchCategoryName(){
        tf_search.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Category> searchList = FXCollections.observableArrayList();
            query = "SELECT * FROM category WHERE name like '%"+newValue+"%';";
            try {
                ResultSet result = db.getData(query);
                searchList.clear();
                while (result.next()) {
                    searchList.add(new Category(result.getInt(1), result.getString(2), result.getDate(3)));
                }

                tb_category.setItems(searchList);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
