package com.posapp.pos.model;

import java.sql.Date;

public class PopularItem {
    private final String barcode;
    private final String productName;
    private final String category;
    private final double price;
    private final String supplierName;
    private final Date dateAdded;
    private final int stock;
    private final Date expiryDate;
    private final int count;

    public PopularItem(String barcode, String product, String category, double price, String supplierName, Date dateAdded, int stock, Date expiryDate, int count) {
        this.barcode = barcode;
        this.productName = product;
        this.category = category;
        this.price = price;
        this.supplierName = supplierName;
        this.dateAdded = dateAdded;
        this.stock = stock;
        this.expiryDate = expiryDate;
        this.count = count;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return String.valueOf(price);
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getDateAdded() {
        return String.valueOf(dateAdded);
    }

    public String getStock() {
        return String.valueOf(stock);
    }

    public String getExpiryDate() {
        return String.valueOf(expiryDate);
    }

    public String getCount() {
        return String.valueOf(count);
    }


}
