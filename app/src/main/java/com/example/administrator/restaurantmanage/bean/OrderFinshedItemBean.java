package com.example.administrator.restaurantmanage.bean;

/**
 * Created by Administrator on 2017/8/11 0011.
 */

public  class OrderFinshedItemBean {
    private String imagePath;
    private String name;
    private String price;
    private String count;
    private String orderState;
    private String orderID;


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }


    @Override
    public String toString() {
        return "OrderFinshedItemBean{" +
                "imagePath='" + imagePath + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", count='" + count + '\'' +
                ", orderState='" + orderState + '\'' +
                ", orderID='" + orderID + '\'' +
                '}';
    }
}
