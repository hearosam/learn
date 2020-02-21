package com.sam.cn.delay_queue;

import java.util.concurrent.DelayQueue;

/**
 * 使用延时队列实现延时订单
 * @author sam.liang
 */
public class MainApp {

    public static void main(String[] args) throws InterruptedException {

        DelayQueue<ItemVO<Order>> delayQueue = new DelayQueue<>();
        new Thread(new PutOrder(delayQueue)).start();
        new Thread(new FetchOrder(delayQueue)).start();

        //500毫秒打印一次
        for (;;) {
            Thread.sleep(500);
            System.out.println("flag");
        }
    }
}
