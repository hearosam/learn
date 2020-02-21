package com.sam.cn.delay_queue;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.concurrent.DelayQueue;

/**
 * 消息生产者,将延时订单放入延时队列
 * @author sam.liang
 */
public class PutOrder implements Runnable {

    private DelayQueue<ItemVO<Order>> delayQueue;

    public PutOrder(DelayQueue<ItemVO<Order>> delayQueue) {
        this.delayQueue = delayQueue;
    }

    @Override
    public void run() {
        //5秒超时
        Order order = new Order();
        order.setOrderNo("00001");
        order.setPrice(11.5);
        ItemVO<Order> itemVO = new ItemVO<>(5000,order);
        this.delayQueue.offer(itemVO);
        System.out.println("5秒超时订单放入延时队列");

        //8秒超时
        Order order2 = new Order();
        order2.setOrderNo("00002");
        order2.setPrice(22.3);
        ItemVO<Order> orderItemVO = new ItemVO<>(8000,order2);
        this.delayQueue.offer(orderItemVO);
        System.out.println("8秒超时订单放入延时队列");
    }
}
