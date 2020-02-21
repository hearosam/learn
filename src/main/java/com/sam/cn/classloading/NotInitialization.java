package com.sam.cn.classloading;

/**
 *  * 被动使用类字段演示
 *  *
 */
public class NotInitialization {

    public static void main(String[] args) {
        //通过子类引用父类的静态字段不会导致子类初始化
//        System.out.println(SubClass.value);
        //通过数组定义来引用类，不会触发此类的初始化
//        SuperClass[] superClasses = new SuperClass[10];
        {
            byte[] placeHolder = new byte[64*1024*1024];
        }
//        int a = 0 ;
        System.gc();
    }
}
