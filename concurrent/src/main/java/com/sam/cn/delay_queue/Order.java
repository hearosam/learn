package com.sam.cn.delay_queue;

/**
 * 订单实体
 * @author sam.liang
 */
public class Order {

    //订单编号
    private String orderNo;
    //商品价格
    private double price;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
