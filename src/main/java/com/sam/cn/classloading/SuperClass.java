package com.sam.cn.classloading;

/**
 * 父类
 */
public class SuperClass {
    static {
        System.out.println("SuperClass init !");
    }
    public static int value = 123;
}
