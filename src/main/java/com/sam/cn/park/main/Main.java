//package com.sam.cn.park.main;
//
//import com.lmax.disruptor.EventFactory;
//import com.lmax.disruptor.YieldingWaitStrategy;
//import com.lmax.disruptor.dsl.Disruptor;
//import com.lmax.disruptor.dsl.EventHandlerGroup;
//import com.lmax.disruptor.dsl.ProducerType;
//import com.lmax.disruptor.util.DaemonThreadFactory;
//import com.sam.cn.park.bean.MyInParkingDataEvent;
//import com.sam.cn.park.handler.MyParkingDataInDbHandler;
//import com.sam.cn.park.handler.MyParkingDataSmsHandler;
//import com.sam.cn.park.handler.MyParkingDataToKafkaHandler;
//import com.sam.cn.park.producer.MyInParkingDataEventPublisher;
//
//import java.util.concurrent.CountDownLatch;
//
///**
// * Disruptor 停车场案例演示
// * 执行的Main方法 ，
// * 一个生产者（汽车进入停车场）；
// * 三个消费者（一个记录汽车信息，一个发送消息给系统，一个发送消息告知司机）
// * 前两个消费者同步执行，都有结果了再执行第三个消费者
// * @author sam.liang
// */
//public class Main {
//
//    public static void main(String[] args) {
//        //2的N次方
//        int ringBufferSize = 2048;
//        try{
//
//            //1.初始化disruptor
//            Disruptor<MyInParkingDataEvent> disruptor = new Disruptor<MyInParkingDataEvent>(new EventFactory<MyInParkingDataEvent>() {
//                @Override
//                public MyInParkingDataEvent newInstance() {
//                    return new MyInParkingDataEvent();
//                }
//            }, ringBufferSize, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new YieldingWaitStrategy());
//            //使用disruptor创建消费者组，MyParkingDataInDbHandler和MyParkingDataToKafkaHandler
//            EventHandlerGroup<MyInParkingDataEvent> group = disruptor.handleEventsWith(new MyParkingDataInDbHandler(), new MyParkingDataToKafkaHandler());
//            //在上面两个消费者消费结束后再调用smsHandler
//            group.then(new MyParkingDataSmsHandler());
//            //启动disruptor
//            disruptor.start();
//
//            //生产者线程准备好消息就可以通知主线程继续工作了
//            CountDownLatch downLatch = new CountDownLatch(1);
//            int count = 4;
//            for (int i = 0; i < count; i++) {
//                new Thread(new MyInParkingDataEventPublisher(downLatch,disruptor)).start();
//            }
//            downLatch.await();//等待生产者线程通知
//            disruptor.shutdown();
//        }catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//}
