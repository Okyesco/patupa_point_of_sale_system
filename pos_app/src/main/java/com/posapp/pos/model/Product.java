package com.posapp.pos.model;

import java.sql.Date;

public class Product {
    private int productId;
    private String productBarcode;
    private String productName;
    private int productCategory;
    private double productPrice;
    private int productQuantity;
    private int productSupplier;
    private Date productAdded_date;
    private Date productExpiry_date;
    private String productCategoryName;
    private String productSupplierName;


    public Product(Integer id, String bar_code, String name, int category, String categoryName, double price, int supplier,
                   String supplierName, Date added_date, int quantity, Date expiry_date) {
        this.productId = id;
        this.productBarcode = bar_code;
        this.productName = name;
        this.productCategory = category;
        this.productPrice = price;
        this.productQuantity = quantity;
        this.productSupplier = supplier;
        this.productAdded_date = added_date;
        this.productExpiry_date = expiry_date;
        this.productCategoryName = categoryName;
        this.productSupplierName = supplierName;

    }

    public String getProductId() {
        return String.valueOf(productId);
    }


    public String getProductBarcode() {
        return productBarcode;
    }

    public String getProductName() {
        return productName;
    }


    public String getProductCategory() {
        return String.valueOf(productCategory);
    }


    public String getProductPrice() {
        return String.valueOf(productPrice);
    }

    public String getProductSupplierName() {
        return productSupplierName;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }


    public String getProductQuantity() {
        return String.valueOf(productQuantity);
    }


    public String getProductSupplier() {
        return String.valueOf(productSupplier);

    }


    public String getProductAddedDate() {
        return String.valueOf(productAdded_date);
    }

    public String getProductExpiryDate() {
        return String.valueOf(productExpiry_date);
    }

}
