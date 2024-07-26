package com.posapp.pos.model;

import java.sql.Time;
import java.util.Date;

public class Purchase {
    int id;
    String transactionID;
    String cashierId;
    Date date;
    Time time;
    String barcode;
    int quantity;
    double totalAmount;
    String desc;

    public Purchase(int id, String transactionId,  String cashierid, Date date, Time time, String barcode, int quantity, double totalAmount, String desc) {
        this.id = id;
        this.transactionID = transactionId;
        this.cashierId = cashierid;
        this.date = date;
        this.time = time;
        this.barcode = barcode;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.desc = desc;
    }

    public Purchase() {

    }

    public String getId() {
        return String.valueOf(id);
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getCashierId() {
        return cashierId;
    }

    public void setCashierId(String cashierId) {
        this.cashierId = cashierId;
    }

    public String getDate() {
        return String.valueOf(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return String.valueOf(time);
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getQuantity() {
        return String.valueOf(quantity);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTotalAmount() {
        return String.valueOf(totalAmount);
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
