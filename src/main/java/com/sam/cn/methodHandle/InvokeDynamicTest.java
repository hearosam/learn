package com.sam.cn.methodHandle;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * InvokeDynamic指令演示代码
 * @author sam.liang
 */
public class InvokeDynamicTest {

    public static void testMethod(String s) {
        System.out.println("hello string "+s);
    }

    public static CallSite bootStrapMethod(MethodHandles.Lookup lookup, String name, MethodType mt) throws Throwable{
        return new ConstantCallSite(lookup.findStatic(InvokeDynamicTest.class,name,mt));
    }

    private static MethodType MT_bootStrap() {
        return MethodType.fromMethodDescriptorString("(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;",
        null);
    }
}
