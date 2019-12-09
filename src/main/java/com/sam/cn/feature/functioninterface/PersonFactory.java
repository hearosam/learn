package com.sam.cn.feature.functioninterface;

/**
 * 函数式接口
 * @author sam.liang
 * @param <P>
 */
@FunctionalInterface
public interface PersonFactory<P extends Person> {
    P create(String name,int age);
}
