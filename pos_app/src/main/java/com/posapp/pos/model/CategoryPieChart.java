package com.posapp.pos.model;

public class CategoryPieChart {
    private final String categoryName;
    private final double totalSales;

    public CategoryPieChart(String categoryName, double totalSales){
        this.categoryName = categoryName;
        this.totalSales = totalSales;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Double getTotalSales() {
        return totalSales;
    }
}
