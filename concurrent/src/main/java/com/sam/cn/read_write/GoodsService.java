package com.sam.cn.read_write;

/**
 * 商品服务接口
 * @author sam.liang
 */
public interface GoodsService {

    //获取商品信息
    GoodsInfo getGoodsInfo();

    //根据商品购买数量计算总价
    void computeAmount(int goodsNum);

}

