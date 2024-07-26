package com.posapp.pos.database;

import com.posapp.pos.common.Common;
import com.posapp.pos.model.Sale;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DataAccessObject {
    public static boolean completed = false;
    private DBConnection database= new DBConnection();
    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;

    public DataAccessObject() {}

    public void saveData(String query) throws SQLException {
        try {
            conn = database.getConnection();
            pst = conn.prepareStatement(query);
            pst.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            database.closeConnection(conn, pst);
        }
    }

    public ResultSet getData(String query) throws SQLException {
        try{
            conn = database.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return rs;
    }

    public void saveSaleInfo(String query) throws SQLException {
        conn = database.getConnection();
        conn.setAutoCommit(false);
        pst = conn.prepareStatement(query);
    }

    private void insertSaleIntoDatabase(Sale sale, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, Common.transactionId);
        pstmt.setString(2, Common.cashierRecord.getId());
        pstmt.setInt(3,sale.getProductID());
        pstmt.setInt(4, Integer.parseInt(sale.getQuantity()));
        pstmt.setDouble(5, Double.parseDouble(sale.getTotalAmount()));
        pstmt.addBatch();
    }



    public void saveSalesToDatabase(ObservableList<Sale> sales) throws SQLException {
    String query = "INSERT INTO purchase (transactionID, cashierId, product_id, quantity, totalAmount) VALUES (?, ?, ?, ?, ?)";
    try {
        conn = database.getConnection();
        conn.setAutoCommit(false);

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (Sale sale : sales) {
                insertSaleIntoDatabase(sale, pstmt);
            }
            pstmt.executeBatch();
        }

       conn.commit();

//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Success");
//        alert.setHeaderText(null);
//        alert.setContentText("All sales items have been successfully saved to the database.");
//        alert.showAndWait();

        completed = true;

    } catch (SQLException e) {
        try {
            conn.rollback();
        } catch (SQLException rollbackEx) {
            System.out.println(rollbackEx.getMessage());
        }
        System.out.println(e.getMessage());
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("An error occurred while saving sales items to the database.");
        alert.showAndWait();
    } finally {
        try {
            conn.setAutoCommit(true); // Reset auto-commit to true
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            }
        }
    }

    public InputStream getReport(String reportName, String columnName){
        InputStream input = null;
        String query = "SELECT "+columnName+" FROM reports WHERE report_name = '"+reportName+"';";
        try{
            conn = database.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()) {
                input = rs.getBinaryStream(1);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return input;
    }

}

