package com.posapp.pos.model;

public class DailyLineChart {
    private final Number date;
    private final Number sales;

    public DailyLineChart(Number date, Number sales) {
        this.date = date;
        this.sales = sales;
    }

    public Number getDate() {
        return date;
    }

    public Number getSales() {
        return sales;
    }
}
