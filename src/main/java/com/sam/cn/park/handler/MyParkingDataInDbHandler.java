//package com.sam.cn.park.handler;
//
//import com.lmax.disruptor.EventHandler;
//import com.lmax.disruptor.WorkHandler;
//import com.sam.cn.park.bean.MyInParkingDataEvent;
//
///**
// * 第一个消费者，负责保存汽车信息到数据库
// * @author sam.liang
// */
//public class MyParkingDataInDbHandler implements EventHandler<MyInParkingDataEvent>, WorkHandler<MyInParkingDataEvent> {
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
//        //获取车牌
//        String carLicense = event.getCarLicense();
//        System.out.println(String.format("Thread id: %s,保存车牌：%s,到数据库",id,carLicense));
//    }
//}
