package com.sam.cn.visual.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 演示visualVM的BTrace插件的使用
 */
public class BTraceTest {

    public static void main(String[] args) throws IOException {
//        BTraceTest bTraceTest = new BTraceTest();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        for (int i = 0; i < 10; i++) {
//            reader.readLine();
//            int a = (int) Math.round(Math.random()*1000);
//            int b = (int) Math.round(Math.random()*1000);
//            System.out.println(bTraceTest.add(a,b));
//        }
        //類型轉換
//        float a = 1.9f;
//        int b = (int) a;
//        long c = (long)a;
//        System.out.println(c);
//        System.out.println(b);
        int a = -1;
        long b = a;
    }

    public int add(int a,int b){
        return a+b;
    }

}
