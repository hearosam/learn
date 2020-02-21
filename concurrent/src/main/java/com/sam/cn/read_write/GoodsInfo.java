package com.sam.cn.read_write;

/**
 * 商品实体类
 * @author sam.liang
 */
public class GoodsInfo {
    //商品名称
    private String name;
    //商品价格
    private double price;
    //商品总价
    private double totalAmount;
    //商品数量
    private int goodsNum;


    public GoodsInfo(String name, double price, int goodsNum) {
        this.name = name;
        this.price = price;
        this.goodsNum = goodsNum;
    }

    /**
     * 根据商品数量计算总价
     * @param goodsNum 购买商品数量
     */
    public void computeAmount(int goodsNum) {
        this.totalAmount = this.price * goodsNum;
        this.goodsNum = goodsNum;
    }
}
