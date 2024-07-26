package com.posapp.pos.controller;

import com.posapp.pos.database.DataAccessObject;
import com.posapp.pos.model.CategoryPieChart;
import com.posapp.pos.model.DailyLineChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AdminViewChartsController implements Initializable {
    @FXML
    private AnchorPane ch_category;

    @FXML
    private AnchorPane ch_dailySale;

    @FXML
    private AnchorPane ch_monthlySale;

    private String query;
    private DataAccessObject db;
    private int ID;
    private final String todayDate = String.valueOf(new Date(System.currentTimeMillis()));


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        db = new DataAccessObject();
        try {
            pieChart();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            dailyLineChart();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            monthlyLineChart();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    private void pieChart() throws SQLException {
        ObservableList<PieChart.Data> chart = FXCollections.observableArrayList();
        ObservableList<CategoryPieChart> list = FXCollections.observableArrayList();

        query = "select c.name as category, sum(p.totalAmount) as totalCatSales from purchase as p " +
                "join product as pr on pr.id = p.product_id join category as c on pr.category_id = c.id group by c.name;";

        ResultSet result = db.getData(query);

        while (result.next()) {
            String category = result.getString("category");
            String totalCatSales = result.getString("totalCatSales");

            list.add(new CategoryPieChart(category, Double.parseDouble(totalCatSales)));
        }

        for (CategoryPieChart model : list) {
            chart.add(new PieChart.Data(model.getCategoryName(), model.getTotalSales()));
        }

        PieChart pieChart = new PieChart(chart);

        pieChart.setTitle("Category Sales");

        pieChart.setPrefHeight(400);
        pieChart.setMinHeight(400);
        pieChart.setMaxHeight(400);

        pieChart.setPrefWidth(300);
        pieChart.setMinWidth(300);
        pieChart.setMaxWidth(300);

        pieChart.setPrefSize(500, 380);
        pieChart.setMinSize(500, 380);
        pieChart.setMaxSize(500, 380);

        pieChart.setClockwise(true);

        pieChart.setLabelLineLength(50);

        pieChart.setLabelsVisible(true);

        pieChart.setStartAngle(180);
        pieChart.setAnimated(true);
//        pieChart.animatedProperty();
        ch_category.getChildren().add(pieChart);

    }

    private void dailyLineChart() throws SQLException {
        NumberAxis xAxis = new NumberAxis(1, 31, 1);
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Days");
        yAxis.setLabel("Sale Amount");

        LineChart<Number, Number> linechart = new LineChart<Number, Number>(xAxis, yAxis);
        linechart.setTitle("Daily Sale");

        ObservableList<DailyLineChart> sales = FXCollections.observableArrayList();

        Map<String, Integer> dateValue = getDateYearDayAndMonth(Date.valueOf(todayDate));
        int year = dateValue.get("year");
        int month = dateValue.get("month");

        query = "select date, sum(amount) as sales  from transaction where date like '"+year+"-%"+month+"-%' group by date;";
        ResultSet result = db.getData(query);
        while (result.next()) {
            String date = result.getString("date");
            Long totalSales = result.getLong("sales");

            Map<String, Integer> resDate = getDateYearDayAndMonth(Date.valueOf(date));
            int resDay = resDate.get("day");

            sales.add(new DailyLineChart(resDay, totalSales));

        }


        XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

        for (DailyLineChart sale : sales) {
            series.getData().add(new XYChart.Data<Number, Number>(sale.getDate(), sale.getSales()));
        }

        series.setName("Daily sale progress within this month");

        linechart.getData().add(series);
        linechart.setAnimated(true);
        linechart.animatedProperty();

        linechart.setPrefHeight(515);
        linechart.setMinHeight(515);
        linechart.setMaxHeight(515);

        linechart.setPrefWidth(400);
        linechart.setMinWidth(400);
        linechart.setMaxWidth(400);

        linechart.setPrefSize(500, 380);
        linechart.setMinSize(500, 380);
        linechart.setMaxSize(500, 380);

        ch_dailySale.getChildren().add(linechart);
    }


    private void monthlyLineChart() throws SQLException {
        NumberAxis xAxis = new NumberAxis(1, 12, 1);
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Months");
        yAxis.setLabel("Sale Amount");

        LineChart<Number, Number> linechart = new LineChart<Number, Number>(xAxis, yAxis);
        linechart.setTitle("Monthly Sale");

        ObservableList<DailyLineChart> sales = FXCollections.observableArrayList();

        Map<String, Integer> dateValue = getDateYearDayAndMonth(Date.valueOf(todayDate));
        int year = dateValue.get("year");
        int month = dateValue.get("month");

        query = "select date, sum(amount) as sales from transaction where date like '"+year+"-%"+month+"-%';";
        ResultSet result = db.getData(query);

        while (result.next()) {
            String date = result.getString("date");
            Long totalSales = result.getLong("sales");


            Map<String, Integer> resDate = getDateYearDayAndMonth(Date.valueOf(date));
            int resMonth = resDate.get("month");

            sales.add(new DailyLineChart(resMonth, totalSales));

        }


        XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

        for (DailyLineChart sale : sales) {
            series.getData().add(new XYChart.Data<Number, Number>(sale.getDate(), sale.getSales()));
        }

        series.setName("Monthly sale progress within this year");

        linechart.getData().add(series);
        linechart.setAnimated(true);
        linechart.animatedProperty();

        linechart.setPrefHeight(515);
        linechart.setMinHeight(515);
        linechart.setMaxHeight(515);

        linechart.setPrefWidth(400);
        linechart.setMinWidth(400);
        linechart.setMaxWidth(400);

        linechart.setPrefSize(500, 380);
        linechart.setMinSize(500, 380);
        linechart.setMaxSize(500, 380);

        ch_monthlySale.getChildren().add(linechart);
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

}



//private void fillLineChart() {
//    ObservableList<Age> list = FXCollections.observableArrayList();
//
//    list.add(new Age("Edward", 200));
//    list.add(new Age("Nyarko", 30));
//    list.add(new Age("Asare", 400));
//    list.add(new Age("Ama", 0));
//    list.add(new Age("Kofi", 6));
//    list.add(new Age("Mari", 7000));
//
//    XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
//    for (Age age : list) {
//        series.getData().add(new XYChart.Data<String, Integer>(age.getName(), age.getAge()));
//    }
//
////        series.setName("Names");
//    NumberAxis xAxis = new NumberAxis(1, 31, 5);
//    NumberAxis yAxis = new NumberAxis();
//    xAxis.setLabel("Days");
//    yAxis.setLabel("Sale Amount");
//
//    LineChart<Number, Number> linechart = new LineChart<Number, Number>(xAxis, yAxis);
//
//    linechart.setTitle("Daily Sale");
//
//    ObservableList<Sales> sales = FXCollections.observableArrayList();
//
//    sales.add(new Sales(2500, 1));
//    sales.add(new Sales(500, 2));
//    sales.add(new Sales(1500, 3));
//    sales.add(new Sales(2500, 4));
//    sales.add(new Sales(120, 5));
//
//
//    XYChart.Series<Number, Number> series1 = new XYChart.Series<Number, Number>();
//
//    for (Sales sale : sales) {
//        series1.getData().add(new XYChart.Data<Number, Number>(sale.day, sale.sale));
//    }
//
//    series1.setName("Daily sale progress within this month");
//
//    linechart.getData().add(series1);
//    linechart.setAnimated(true);
//    linechart.animatedProperty();
//
//    linechart.setPrefHeight(515);
//    linechart.setMinHeight(515);
//    linechart.setMaxHeight(515);
//
//    linechart.setPrefWidth(400);
//    linechart.setMinWidth(400);
//    linechart.setMaxWidth(400);
//
//    linechart.setPrefSize(510, 300);
//    linechart.setMinSize(510, 300);
//    linechart.setMaxSize(510, 300);
//
//    line_pane.getChildren().add(linechart);
//    line_pane.setStyle("-fx-background-color:  #e2edff");
////        linechart.getData().add(series);
//}
