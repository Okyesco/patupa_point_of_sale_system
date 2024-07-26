package com.posapp.pos.report;

import com.posapp.pos.database.DBConnection;
import com.posapp.pos.database.DataAccessObject;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;


public abstract class Report {
    private static JasperReport jreport;
    private static JasperViewer jviewer;
    private static JasperPrint jprint;
    private static DBConnection database;
    private static DataAccessObject db;
    private static Connection conn;



    public static void createReport(Connection connect, Map<String, Object> map, InputStream by) {
        try{
            jreport = (JasperReport)JRLoader.loadObject(by);
            jprint = JasperFillManager.fillReport(jreport, map, connect);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    public static void showReport() {
        jviewer = new JasperViewer(jprint, false);
        jviewer.setVisible(true);
        jviewer.setFitPageZoomRatio();
        jviewer.setTitle("PatuPa POS System: Printing service");
    }



    public static void jasperPrinter(InputStream report) throws JRException {
        String sql = "select c.name as 'Category Name', count(c.name) as 'Sales Count', sum(p.totalAmount) as `Total Amount` from purchase as p " +
                "join product as pr on pr.id = p.product_id join category as c on pr.category_id = c.id group by c.name ;";
        try{

            database = new DBConnection();

            conn = database.getConnection();

            JasperReport jasperReport = JasperCompileManager.compileReport(report);

            JasperPrint jp = JasperFillManager.fillReport(jasperReport, null, conn);
            JasperViewer jv = new JasperViewer(jp, false);
            jv.setZoomRatio(0.6F);
            jv.setVisible(true);


        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }



    //    public static void newJasperPrinter(String filePath) throws JRException {
//        String sql = "select c.name as 'Category Name', count(c.name) as 'Sales Count', sum(p.totalAmount) as `Total Amount` from purchase as p " +
//                "join product as pr on pr.id = p.product_id join category as c on pr.category_id = c.id group by c.name ;";
////        System.out.println(sql);
//        try{
//            String path = filePath;
//            JasperDesign jasperDesign = JRXmlLoader.load(path);
////            JRDesignQuery query = new JRDesignQuery();
//
////            query.setText(sql);
////            jasperDesign.setQuery(query);
//
//            database = new DBConnection();
//
//            conn = database.getConnection();
//            JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("/path/to/your/report.jrxml"));
//
//
//            JasperReport jr = JasperCompileManager.compileReport(jasperDesign);
//            JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);
//            JasperViewer jv = new JasperViewer(jp, false);
//            jv.setZoomRatio(0.6F);
//            jv.setVisible(true);
//
//
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }



//    public static void newJasperPrinter(String resourcePath) {
//        String sql = "SELECT c.name AS 'Category Name', COUNT(c.name) AS 'Sales Count', SUM(p.totalAmount) AS 'Total Amount' " +
//                "FROM purchase AS p " +
//                "JOIN product AS pr ON pr.id = p.product_id " +
//                "JOIN category AS c ON pr.category_id = c.id " +
//                "GROUP BY c.name;";
//
//        Connection conn = null;
//
//        try {
//            // Load the JRXML file from the classpath
//            URL resourceUrl = Objects.requireNonNull(Main.class.getResource(resourcePath), "Resource not found");
//            String path = Paths.get(resourceUrl.toURI()).toString();
//
//            // Load the JasperDesign from the JRXML file
//            JasperDesign jasperDesign = JRXmlLoader.load(path);
//
//            // Set the SQL query dynamically
//            JRDesignQuery query = new JRDesignQuery();
//            query.setText(sql);
//            jasperDesign.setQuery(query);
//
//            // Compile the JasperDesign to JasperReport
//            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
//
//            // Establish a database connection
//            conn = database.getConnection();
//
//            // Fill the report with data
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, conn);
//
//            // View the report
//            JasperViewer viewer = new JasperViewer(jasperPrint, false);
//            viewer.setVisible(true);
//
//        } catch (JRException | SQLException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            // Close the database connection
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }


}
