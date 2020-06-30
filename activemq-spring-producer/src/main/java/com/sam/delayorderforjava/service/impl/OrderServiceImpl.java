package com.sam.delayorderforjava.service.impl;

import com.sam.delayorderforjava.entity.OrderEntity;
import com.sam.delayorderforjava.service.IDelayOrder;
import com.sam.delayorderforjava.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private static final String UNPAY = "1";
    private static final String PAY = "0";
    private static final String TIMEOUT = "2";

    @Autowired
    private IDelayOrder delayOrder;

    @Override
    public void insertOrder() {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            long expireTime = random.nextInt(20) + 5;
            OrderEntity entity = new OrderEntity(UUID.randomUUID().toString(),"订单："+i,UNPAY);
            //保存订单到数据库
            System.out.println("正在保存订单："+i+"到数据库");
            //同时保存订单到消息队列中
            delayOrder.orderDelay(entity,expireTime);
            System.out.println("正在添加订单："+i+"到delayQueue中");
        }
    }

    /**
     * 应用重启后处理数据库中已过期及未过期的订单
     */
    @PostConstruct
    public void initDelayOrder() {
        System.out.println("系统启动扫描订单表中过期未支付的订单");
        System.out.println("update 支付订单表 set payStatus=超期未支付 where payStatus=未支付 and expireTime > now() 返回影响行数");
        System.out.println("系统启动扫描订单表中未过期未支付的订单");
        System.out.println("select * from 支付订单表 where payStatus=未支付");
        System.out.println("重新计算超时时间，然后放入延时队列中");
    }
}
