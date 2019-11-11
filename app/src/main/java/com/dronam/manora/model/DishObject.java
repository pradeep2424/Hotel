package com.dronam.manora.model;

/**
 * Created by Wolf Soft on 8/3/2017.
 */

public class DishObject {

    Integer image;
    String dishName;
    String dishType;
    String price;

    public DishObject(Integer image, String dishName, String dishType, String price) {
        this.image = image;
        this.dishName = dishName;
        this.dishType = dishType;
        this.price = price;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDishType() {
        return dishType;
    }

    public void setDishType(String dishType) {
        this.dishType = dishType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
