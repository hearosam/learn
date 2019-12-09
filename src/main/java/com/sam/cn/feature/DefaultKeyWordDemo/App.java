package com.sam.cn.feature.DefaultKeyWordDemo;

/**
 * 演示入口
 * @author sam.liang
 */
public class App {

    public static void main(String[] args) {
        DefaultKeyWordDemo demo = new DefaultKeyWordDemo() {
            @Override
            public double conculate(int a) {
                return a * 3;
            }
            //手动实现接口的default方法，会覆盖默认实现
            @Override
            public double sqrt(int b) {
                return b * 2;
            }
        };
        System.out.println(demo.sqrt(4));
    }
}
