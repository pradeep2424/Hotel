package com.dronam.manora.model;

public class OrderDetailsObject {
    String restaurantName;
    String orderAddress;
    String restaurantReviews;
    String orderDate;
    String orderPrice;
    int foodImage;

    public OrderDetailsObject(String restaurantName, String orderAddress, String restaurantReviews, String orderDate, String orderPrice, int foodImage) {
        this.restaurantName = restaurantName;
        this.orderAddress = orderAddress;
        this.restaurantReviews = restaurantReviews;
        this.orderDate = orderDate;
        this.orderPrice = orderPrice;
        this.foodImage = foodImage;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getRestaurantReviews() {
        return restaurantReviews;
    }

    public void setRestaurantReviews(String restaurantReviews) {
        this.restaurantReviews = restaurantReviews;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(int foodImage) {
        this.foodImage = foodImage;
    }
}
