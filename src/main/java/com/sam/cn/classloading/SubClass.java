package com.sam.cn.classloading;

/**
 * 子类
 */
public class SubClass  extends SuperClass{
    static {
        System.out.println("SubClass init!");
    }
}
