package com.miracle.dronam.model;

import java.io.Serializable;

/**
 * Created by Wolf Soft on 8/3/2017.
 */

public class DishObject implements Serializable {
    String dishID;
    String dishName;
    String dishDescription;
    String dishCategory;
    String dishImage;
    String dishPrice;

    public String getDishCategory() {
        return dishCategory;
    }

    public void setDishCategory(String dishCategory) {
        this.dishCategory = dishCategory;
    }

    public String getDishID() {
        return dishID;
    }

    public void setDishID(String dishID) {
        this.dishID = dishID;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDishDescription() {
        return dishDescription;
    }

    public void setDishDescription(String dishDescription) {
        this.dishDescription = dishDescription;
    }

    public String getDishImage() {
        return dishImage;
    }

    public void setDishImage(String dishImage) {
        this.dishImage = dishImage;
    }

    public String getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(String dishPrice) {
        this.dishPrice = dishPrice;
    }

//    Integer image;
//    String dishName;
//    String dishType;
//    String price;
//
//    public DishObject(Integer image, String dishName, String dishType, String price) {
//        this.image = image;
//        this.dishName = dishName;
//        this.dishType = dishType;
//        this.price = price;
//    }
//
//    public Integer getImage() {
//        return image;
//    }
//
//    public void setImage(Integer image) {
//        this.image = image;
//    }
//
//    public String getDishName() {
//        return dishName;
//    }
//
//    public void setDishName(String dishName) {
//        this.dishName = dishName;
//    }
//
//    public String getDishType() {
//        return dishType;
//    }
//
//    public void setDishType(String dishType) {
//        this.dishType = dishType;
//    }
//
//    public String getPrice() {
//        return price;
//    }
//
//    public void setPrice(String price) {
//        this.price = price;
//    }
}
