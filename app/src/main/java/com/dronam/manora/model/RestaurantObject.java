package com.dronam.manora.model;

import java.io.Serializable;

public class RestaurantObject implements Serializable {
    String categoryID;
    String categoryName;
    String clientID;
    String restaurantName;
    String foodTypeName;
    String foodTypeID;
    String logo;
    String includeTax;
    String taxID;
    String taxable;

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getFoodTypeName() {
        return foodTypeName;
    }

    public void setFoodTypeName(String foodTypeName) {
        this.foodTypeName = foodTypeName;
    }

    public String getFoodTypeID() {
        return foodTypeID;
    }

    public void setFoodTypeID(String foodTypeID) {
        this.foodTypeID = foodTypeID;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIncludeTax() {
        return includeTax;
    }

    public void setIncludeTax(String includeTax) {
        this.includeTax = includeTax;
    }

    public String getTaxID() {
        return taxID;
    }

    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    public String getTaxable() {
        return taxable;
    }

    public void setTaxable(String taxable) {
        this.taxable = taxable;
    }


//    Integer image1;
//
//    public RestaurantObject(Integer image1) {
//        this.image1 = image1;
//    }
//
//    public Integer getImage1() {
//        return image1;
//    }
//
//    public void setImage1(Integer image1) {
//        this.image1 = image1;
//    }
}
