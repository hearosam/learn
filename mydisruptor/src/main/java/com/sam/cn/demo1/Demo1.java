package com.sam.cn.demo1;

import com.lmax.disruptor.*;

import java.util.UUID;
import java.util.concurrent.*;

/**
 * 使用disruptor原生api创建消费者和生产者
 * @author sam.liang
 */
public class Demo1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final int BUFFER_SITE = 1024;
        final int THREAD_NUMBERS = 2;
        /*
            使用createSingleProducer创建一个单个生产者的ringbuffer
            EventFactory参数:职责就是产生数据块（event）填充RingBuffer的区块
            第二个参数：ringbuffer的大小，必须是2的指数倍
            第三个参数：ringbuffer没有可用区块的时候，ringbuffer对生产者的处理策略
         */
        RingBuffer<TradeTransaction> singleRingBuffer = RingBuffer.createSingleProducer(new EventFactory<TradeTransaction>() {

            @Override
            public TradeTransaction newInstance() {
                return new TradeTransaction();
            }
        }, BUFFER_SITE, new YieldingWaitStrategy());

        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUMBERS);
        //创建屏障器
        SequenceBarrier sequenceBarrier = singleRingBuffer.newBarrier();
        //创建消息处理器
        BatchEventProcessor<TradeTransaction> tradeTransactionBatchEventProcessor = new BatchEventProcessor<>(singleRingBuffer, sequenceBarrier, new TradeTransactionEventHandler());
        //ringBuffer消费者可知晓的状态（环形缓冲区最新的下标）
        singleRingBuffer.addGatingSequences(tradeTransactionBatchEventProcessor.getSequence());
        executorService.submit(tradeTransactionBatchEventProcessor);

        Future<Void> future = executorService.submit(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                long seq;
                for (int i = 0; i < 1000; i++) {
                    seq = singleRingBuffer.next();
                    singleRingBuffer.get(seq).setPrice(Math.random() * 9999);
                    singleRingBuffer.publish(seq);//发布环形缓冲区的seq区块，使得EventHandler(消费者)可见
                }
                return null;
            }
        });
        future.get();
        Thread.sleep(1000);
        tradeTransactionBatchEventProcessor.halt();
        executorService.shutdown();
    }
}

/**
 * 消费者，实际处理事件的类
 */
class TradeTransactionEventHandler implements EventHandler<TradeTransaction> {

    @Override
    public void onEvent(TradeTransaction event, long sequence, boolean endOfBatch) throws Exception {
        event.setId(UUID.randomUUID().toString());
        System.out.println(event.getId());
    }
}

/**
 * 普通交易类
 */
class TradeTransaction{
    //交易id
    private String id;
    //交易金额
    private double price;

    public TradeTransaction() {
    }

    public TradeTransaction(String id, double price) {
        this.id = id;
        this.price = price;
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
