package com.sam.cn.delay_queue;

import java.util.concurrent.DelayQueue;

/**
 * 消息消费者，从延时队列中取出延时订单
 * @author sam.liang
 */
public class FetchOrder implements Runnable{

    private DelayQueue<ItemVO<Order>> delayQueue;

    public FetchOrder(DelayQueue<ItemVO<Order>> delayQueue) {
        this.delayQueue = delayQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ItemVO take = this.delayQueue.take();
                Order order = (Order) take.getData();
                System.out.println("处理延时订单："+order.getOrderNo());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
