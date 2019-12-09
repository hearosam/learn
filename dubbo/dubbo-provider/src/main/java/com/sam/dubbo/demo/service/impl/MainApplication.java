package com.sam.dubbo.demo.service.impl;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 服务提供方启动类
 * @author sam.liang
 */
public class MainApplication {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext cxt = new ClassPathXmlApplicationContext("provider.xml");
        cxt.start();
        System.out.println("服务提供方启动完成");
//        //阻塞程序
        System.in.read();
    }
}
