package com.posapp.pos.model;

import java.sql.Date;

public class Supplier {
    private int supplier_id;
    private String supplier_name;
    private String supplier_address;
    private String supplier_phone;
    private String supplier_email;
    private Date last_supplied_date;

    public Supplier(int id, String name, String address, String phone, String email, Date date) {
        this.supplier_id = id;
        this.supplier_name = name;
        this.supplier_address = address;
        this.supplier_phone = phone;
        this.supplier_email = email;
        this.last_supplied_date = date;
    }

    public String getSupplierId() {
        return String.valueOf(supplier_id);
    }


    public String getSupplierName() {
        return supplier_name;
    }


    public String getSupplierAddress() {
        return supplier_address;
    }


    public String getSupplierPhone() {
        return supplier_phone;

    }

    public String getSupplierEmail() {
        return supplier_email;
    }

    public String getLastSuppliedDate() {
        return String.valueOf(last_supplied_date);
    }




}
