package com.sam.cn.feature.DefaultKeyWordDemo;

/**
 * default 关键字使用
 * @author sam.liang
 */
public interface DefaultKeyWordDemo {

    double conculate(int a);

    /**
     * default关键字可以给接口的方法一个默认实现
     * 求平方根
     * @param a
     * @return
     */
    default double sqrt(int a) {
        return Math.sqrt(a);
    }
}
