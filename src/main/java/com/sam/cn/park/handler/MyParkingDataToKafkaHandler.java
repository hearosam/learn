//package com.sam.cn.park.handler;
//
//import com.lmax.disruptor.EventHandler;
//import com.lmax.disruptor.WorkHandler;
//import com.sam.cn.park.bean.MyInParkingDataEvent;
//
///**
// * 第二个消费者，这个消费者负责将消息发送到kafka然后kafka消费者消费这个kafka的消息通知工作人员
// * @author sam.liang
// */
//public class MyParkingDataToKafkaHandler implements EventHandler<MyInParkingDataEvent>, WorkHandler<MyInParkingDataEvent> {
//
//    @Override
//    public void onEvent(MyInParkingDataEvent event, long sequence, boolean endOfBatch) throws Exception {
//        this.onEvent(event);
//    }
//
//    @Override
//    public void onEvent(MyInParkingDataEvent event) throws Exception {
//        //获取当前线程id
//        long id = Thread.currentThread().getId();
//        //获取车牌号
//        String carLicense = event.getCarLicense();
//        System.out.println(String.format("Thread : %s 向kafka发送%s进入停车场的消息",id,carLicense));
//    }
//}
