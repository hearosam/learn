package com.sam.dubbo.demo.consumer;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.util.List;

/**
 * 服务消费者测试类
 * @author sam.liang
 */
public class MainApplication {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext cxt = new ClassPathXmlApplicationContext("consumer.xml");
        cxt.start();
        DemoServiceConsumer service = cxt.getBean(DemoServiceConsumer.class);
        List<String> allPermissions = service.getAllPermissions(1);
        allPermissions.forEach(System.out::println);
    }
}
