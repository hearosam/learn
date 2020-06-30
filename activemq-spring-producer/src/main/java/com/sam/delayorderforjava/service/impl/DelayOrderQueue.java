package com.sam.delayorderforjava.service.impl;

import com.sam.delayorderforjava.component.DelayOrderProcessor;
import com.sam.delayorderforjava.entity.OrderEntity;
import com.sam.delayorderforjava.service.IDelayOrder;
import com.sam.delayorderforjava.vo.ItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.DelayQueue;

/**
 * 阻塞队列实现
 * @author sam.liang
 */
@Component
public class DelayOrderQueue implements IDelayOrder {

    private static final DelayQueue<ItemVO<OrderEntity>> delayQueue = new DelayQueue<>();

    @Autowired
    private DelayOrderProcessor processor;
    private Thread takeOrder;

    @Override
    public void orderDelay(OrderEntity order, long expireTime) {
        ItemVO<OrderEntity> vo = new ItemVO<>(expireTime,order);
        //将延时订单加入到延时队列中
        delayQueue.put(vo);
        System.out.println("订单超时时长"+expireTime+"秒，被推入检查队列，订单详情："+order.toString());
    }

    /**
     * 取超时订单的线程实现类(生产环境这里可以用线程池弄多个线程)
     */
    private class TakeOrder implements Runnable{

        private DelayOrderProcessor processor;

        public TakeOrder(DelayOrderProcessor processor) {
            this.processor = processor;
        }

        @Override
        public void run() {
            System.out.println("处理订单线程"+Thread.currentThread().getName()+"已经启动");
            //只要不中断，当前线程会一直运行下去
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    //take()方法阻塞
                    ItemVO<OrderEntity> orderItem = delayQueue.take();
                    processor.checkDelayOrder(orderItem.getData());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("处理到期订单线程准备关闭");
        }
    }

    /**
     * @PostConstruct 这个注解就是xml中的bean节点的init-method属性,在类初始化成功之后执行
     */
    @PostConstruct
    public void init(){
        takeOrder = new Thread(new TakeOrder(processor));
        takeOrder.start();
    }

    /**
     * @PreDestroy 这个注解就是xml中的bean节点的destroy-method属性,在应用停止之前调用释放资源
     */
    @PreDestroy
    public void close(){takeOrder.interrupt();}

}
