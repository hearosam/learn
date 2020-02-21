package com.sam.cn.methodHandle;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;

/**
 * MethodHandler 基本使用演示
 * @author sam.liang
 */
public class MethodHandleTest {
    static class ClassA {
        public void println(String s) {
            System.out.println("---->"+s);
        }
    }

    public static MethodHandle getPrintlnMH(Object reveiver) throws NoSuchMethodException, IllegalAccessException {
        /*
            MethodType:表示方法类型--->包含了方法的返回值(methodType()的第一个参数)和具体参数(methodType()的第二个参数)
         */
        MethodType methodType = MethodType.methodType(void.class,String.class);
        /*
            lookup()方法来自MethodHandles.lookup()，这个方法的作用是指定类中查找指定方法名称，方法类型，并且符合调用权限的方法句柄
            因为这里调用了一个虚方法，按照java语言的规则，方法第一个参数是隐式的，代表这方法的接收者，也即是this指向对象，这个参数以前放在
            参数列表中进行传递的，而现在提供了bindTo方法来完成这件事情
         */
        return lookup().findVirtual(reveiver.getClass(),"println",methodType).bindTo(reveiver);
    }

    public static void main(String[] args) throws Throwable{
        Object obj = System.currentTimeMillis() % 2 == 0 ? new ClassA() : System.out;
        //无论obj是那个实现类的实例，println都能正确调用
        getPrintlnMH(obj).invokeExact("xixixix");
    }
}
