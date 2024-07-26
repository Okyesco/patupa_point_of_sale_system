package com.posapp.pos.model;

public class Sale {
    private int productID;
    private String barcode;
    private String name;
    private int productStock;
    private double unitAmount;
    private int quantity;
    private double discount;
    private double totalAmount;

    public Sale(int productID, String barcode, String name, int productStock, double amount, int quantity, double discount, double total) {
        this.productID = productID;
        this.barcode = barcode;
        this.name = name;
        this.productStock = productStock;
        this.unitAmount = amount;
        this.quantity = quantity;
        this.discount = discount;
        this.totalAmount = total;

    }

    public Sale() {

    }



    public Sale(String name, double amount, int quantity, double total) {
        this.name = name;
        this.unitAmount = amount;
        this.quantity = quantity;
        this.totalAmount = total;
    }

    public int getProductID() {
        return productID;
    }


    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductStock() {
        return String.valueOf(productStock);
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public String getUnitAmount() {
        return String.valueOf(unitAmount);
    }

    public void setUnitAmount(double amount) {
        this.unitAmount = amount;
    }

    public String getQuantity() {
        return String.valueOf(quantity);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return String.valueOf(discount);
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getTotalAmount() {
        return String.valueOf(totalAmount);
    }

    public void setTotalAmount(double total) {
        this.totalAmount = total;
    }
}
