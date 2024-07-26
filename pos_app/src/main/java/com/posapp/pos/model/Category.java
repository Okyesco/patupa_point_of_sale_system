package com.posapp.pos.model;

import java.sql.Date;


public class Category {
    private final Integer categoryId;
    private String categoryName;
    private final Date dateCreated;

    public Category(Integer categoryId, String categoryName, Date dateCreated) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.dateCreated = dateCreated;
    }

    public String getCategoryId() {
        return String.valueOf(categoryId);
    }

    public String getCategoryName() {
        return categoryName;
    }
    public String getDateCreated() {
        return String.valueOf(dateCreated);
    }


    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
