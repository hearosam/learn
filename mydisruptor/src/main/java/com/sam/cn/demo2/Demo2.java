package com.sam.cn.demo2;


import com.lmax.disruptor.*;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 使用workerpool方式演示disruptor生产者消费者使用
 * 博文：https://blog.csdn.net/qian_348840260/article/details/38314825
 * 博文2：https://www.cnblogs.com/haiq/p/4112689.html
 * 博文3：https://blog.csdn.net/qian_348840260/article/details/38317743
 * @author sam.liang
 */
public class Demo2 {

    public static void main(String[] args) throws InterruptedException {
        final int BUFFER_SITE = 1024;
        final int THREAD_NUMBERS = 4;
        //事件工厂
        EventFactory<TradeTransaction> factory = new EventFactory<TradeTransaction>(){
            @Override
            public TradeTransaction newInstance() {
                return new TradeTransaction();
            }
        };
        //使用createSingleProducer创建一个单个生产者的ringbuffer
        RingBuffer<TradeTransaction> singleRingBuffer = RingBuffer.createSingleProducer(factory, BUFFER_SITE, new YieldingWaitStrategy());
        //创建屏障器
        SequenceBarrier sequenceBarrier = singleRingBuffer.newBarrier();
        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUMBERS);
        //WorkHandler数组
        WorkHandler<TradeTransaction>[] workHandlers = new WorkHandler[3];
        for (int i = 0; i < 3; i++) {
            WorkHandler<TradeTransaction> workHandler = new TradeTransactionEventHandler();
            workHandlers[i] =  workHandler;
        }

        WorkerPool<TradeTransaction> workerPool = new WorkerPool<>(singleRingBuffer, sequenceBarrier, new IgnoreExceptionHandler(), workHandlers);

        workerPool.start(executorService);
        for (int i = 0; i < 800; i++) {
            long seq = singleRingBuffer.next();
            singleRingBuffer.get(seq).setPrice(Math.random()*9999);
            singleRingBuffer.publish(seq);
            if(i % 10 == 0) {
                System.out.println(((ThreadPoolExecutor) executorService).getActiveCount() + "-----------------------");
            }
        }

        Thread.sleep(1000);
        workerPool.halt();
        Thread.sleep(1);
        executorService.shutdown();
    }

}
/**
 * 时间消费者
 */
class TradeTransactionEventHandler implements WorkHandler<TradeTransaction> {

    @Override
    public void onEvent(TradeTransaction event) throws Exception {
        event.setId(UUID.randomUUID().toString());
        System.out.println(event.getPrice());
    }
}
/**
 * 普通交易类
 */
class TradeTransaction {
    //交易id
    private String id;
    //价格
    private double price;

    public TradeTransaction(String id, double price) {
        this.id = id;
        this.price = price;
    }

    public TradeTransaction() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}