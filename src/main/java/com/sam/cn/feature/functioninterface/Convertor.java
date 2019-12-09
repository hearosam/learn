package com.sam.cn.feature.functioninterface;

/**
 * 函数式接口
 * @author sam.liang
 */
@FunctionalInterface
public interface Convertor<F,T> {
    T conver(F f);
//    default T xixi(F f){
//        return null;
//    }
}
