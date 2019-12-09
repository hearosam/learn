//package com.sam.cn.mylearnproject.main;
//
//import com.lmax.disruptor.RingBuffer;
//import com.lmax.disruptor.dsl.Disruptor;
//import com.lmax.disruptor.util.DaemonThreadFactory;
//import com.sam.cn.mylearnproject.bean.MyDataEvent;
//import com.sam.cn.mylearnproject.factory.MyDataEventFactory;
//import com.sam.cn.mylearnproject.handler.MyDataEventHandler;
//import com.sam.cn.mylearnproject.producer.MyDataEventProducer;
//
//import java.nio.ByteBuffer;
//import java.util.concurrent.*;
//
///**
// * disruptor 示例
// * @author sam.liang
// */
//public class Main {
//
//    public static void main(String[] args) {
//        //1.创建工厂
//        MyDataEventFactory factory = new MyDataEventFactory();
//        //2.定义bufferSize，也就是ringBuffer的大小，必须是2的N次方
//        int ringBufferSize = 1024 * 1024;
//        //3.创建 disruptor
//        Disruptor<MyDataEvent> disruptor = new Disruptor<MyDataEvent>(factory, ringBufferSize, DaemonThreadFactory.INSTANCE);
//        //4.连接消费者事件方法
//        disruptor.handleEventsWith(new MyDataEventHandler());
//        //5.启动
//        disruptor.start();
//        RingBuffer<MyDataEvent> ringBuffer = disruptor.getRingBuffer();
//        //6.生产者发布事件
//        MyDataEventProducer producer = new MyDataEventProducer(ringBuffer);
//        //创建一个128字节的字节缓存
//        ByteBuffer buffer = ByteBuffer.allocate(128);
//
//        // 不管是打印100，1000，10000，基本上都是一秒内输出。
//        int count = 10000;
//        for (long data = 1; data <= count ; data++) {
//            // 在下标为零的位置存储值
//            buffer.putLong(0, data);
//            producer.publishData(buffer);
//        }
//        disruptor.shutdown(); // 关闭 disruptor，方法会堵塞，直至所有的事件都得到处理；
//    }
//
//}
