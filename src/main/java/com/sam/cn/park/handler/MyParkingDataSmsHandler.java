//package com.sam.cn.park.handler;
//
//import com.lmax.disruptor.EventHandler;
//import com.lmax.disruptor.WorkHandler;
//import com.sam.cn.park.bean.MyInParkingDataEvent;
//
///**
// * 第三个消费者负责向车主发送短信消息
// * @author sam.liang
// */
//public class MyParkingDataSmsHandler implements EventHandler<MyInParkingDataEvent>, WorkHandler<MyInParkingDataEvent> {
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
//        System.out.println(String.format("Thread :%s向用户发送了%s进入停车场的欢迎消息",id,carLicense));
//    }
//}
