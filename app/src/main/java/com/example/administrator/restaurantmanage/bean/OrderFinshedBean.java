package com.example.administrator.restaurantmanage.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/9 0009.
 */

public class OrderFinshedBean {
    /**
     * Created by Administrator on 2017/8/9 0009.
     */

    private String imagePath;
    private String shop_name;
    private List<OrderFinshedItemBean> beanList;
    private String orderState;
    private String time;
    private String total_fee;
    private   List<OrderFinshedItemBean> itemBeanList;


    public List<OrderFinshedItemBean> getItemBeanList() {
        return itemBeanList;
    }

    public void setItemBeanList(List<OrderFinshedItemBean> itemBeanList) {
        this.itemBeanList = itemBeanList;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }





}
