package com.posapp.pos.controller;

import com.posapp.pos.Main;
import com.posapp.pos.database.DBConnection;
import com.posapp.pos.database.DataAccessObject;
import com.posapp.pos.report.Report;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRException;



import static com.posapp.pos.report.Report.jasperPrinter;

public class AdminReportsController implements Initializable {
    @FXML
    private Button bt_daily_cat_report;

    @FXML
    private Button bt_daily_product_report;

    @FXML
    private Button bt_month_cat_report;

    @FXML
    private Button bt_month_product_report;

    @FXML
    private Button bt_popular_report;

    private DBConnection database;
    private DataAccessObject db;
    private Connection conn;
    private Map<String, Object> map;


//    String dailyProductPath = "C:\\Users\\GEORGE\\Desktop\\NEW_POS_APP\\pos_app\\src\\main\\resources\\com\\posapp\\pos\\report_templates\\daily_product.jrxml";
//    String monthlyProductPath = "C:\\Users\\GEORGE\\Desktop\\NEW_POS_APP\\pos_app\\src\\main\\resources\\com\\posapp\\pos\\report_templates\\monthly_product.jrxml";
//    String dailyCategoryPath = "C:\\Users\\GEORGE\\Desktop\\NEW_POS_APP\\pos_app\\src\\main\\resources\\com\\posapp\\pos\\report_templates\\daily_category.jrxml";
//    String monthlyCategoryPath = "C:\\Users\\GEORGE\\Desktop\\NEW_POS_APP\\pos_app\\src\\main\\resources\\com\\posapp\\pos\\report_templates\\monthly_category.jrxml";
//    String popularItemPath = "C:\\Users\\GEORGE\\Desktop\\NEW_POS_APP\\pos_app\\src\\main\\resources\\com\\posapp\\pos\\report_templates\\popular_item.jrxml";

InputStream dailyProductPath = Main.class.getResourceAsStream("report_templates/daily_product.jrxml");
InputStream monthlyProductPath = Main.class.getResourceAsStream("report_templates/monthly_product.jrxml");
InputStream dailyCategoryPath = Main.class.getResourceAsStream("report_templates/daily_category.jrxml");
InputStream monthlyCategoryPath = Main.class.getResourceAsStream("report_templates/monthly_category.jrxml");
InputStream popularItemPath = Main.class.getResourceAsStream("report_templates/popular_item.jrxml");



    public AdminReportsController() throws URISyntaxException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        db = new DataAccessObject();

        bt_daily_product_report.setOnAction(actionEvent -> {
            try {
                jasperPrinter(dailyProductPath);
            } catch (JRException e) {
                throw new RuntimeException(e);
            }
        });

        bt_month_product_report.setOnAction(actionEvent -> {
            try {
                jasperPrinter(monthlyProductPath);
            } catch (JRException e) {
                throw new RuntimeException(e);
            }
        });

        bt_daily_cat_report.setOnAction(actionEvent -> {
            try {
                jasperPrinter(dailyCategoryPath);
            } catch (JRException e) {
                throw new RuntimeException(e);
            }
        });

        bt_month_cat_report.setOnAction(actionEvent -> {
            try {
//                newJasperPrinter(monthlyCategoryPath);
                jasperPrinter(monthlyCategoryPath);
            } catch (JRException e) {
                throw new RuntimeException(e);
            }
        });

        bt_popular_report.setOnAction(actionEvent -> {
            try {
//                newJasperPrinter(popularItemPath);
                jasperPrinter(popularItemPath);
            } catch (JRException e) {
                throw new RuntimeException(e);
            }
        });

//        bt_popular_report.setOnAction(actionEvent -> {
//            try {
//                printReport();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        });


//        bt_daily_report.setOnAction(actionEvent -> {
//            try {
//                newJasperPrinter();
//            } catch (JRException e) {
//                throw new RuntimeException(e);
//            }
//        });
    }


    public void printReport() throws SQLException {
         database = new DBConnection();
         conn = database.getConnection();
         map = new HashMap<String, Object>();

        Report.createReport(conn, map, db.getReport("popular", "report_jasper"));
        Report.showReport();
    }

}
