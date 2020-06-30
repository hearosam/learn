package com.sam.delayorderforjava;

import com.sam.delayorderforjava.service.OrderService;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

/**
 * 程序入口类
 * @author sam.liang
 */
public class APP {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:applicationContext-delayQueue.xml");
        OrderService orderService = ctx.getBean(OrderService.class);
        orderService.insertOrder();
    }
}
