//package com.sam.cn.park.producer;
//
//import com.lmax.disruptor.EventTranslator;
//import com.lmax.disruptor.RingBuffer;
//import com.lmax.disruptor.dsl.Disruptor;
//import com.sam.cn.park.bean.MyInParkingDataEvent;
//
//import java.util.concurrent.CountDownLatch;
//
///**
// * 消息生产者
// * @author sam.liang
// */
//public class MyInParkingDataEventPublisher implements Runnable{
//
//    /**
//     * 用于监听初始化操作，等初始化执行完毕后，通知主线程继续工作
//     */
//    private CountDownLatch downLatch;
//    private Disruptor<MyInParkingDataEvent> disruptor;
//    private static final Integer NUM = 1;
//
//    public MyInParkingDataEventPublisher(CountDownLatch downLatch, Disruptor<MyInParkingDataEvent> disruptor) {
//        this.downLatch = downLatch;
//        this.disruptor = disruptor;
//    }
//
//    @Override
//    public void run() {
//        MyInParkingDataEventTranslator translator = new MyInParkingDataEventTranslator();
//        try{
//            for (int i = 0; i < NUM; i++) {
//                disruptor.publishEvent(translator);
//                //1秒钟进入一辆车
//                Thread.sleep( 1000);
//            }
//        }catch (Exception e) {
//            System.out.println(e.getMessage());
//        }finally {
//            downLatch.countDown();
//            System.out.println(NUM + "辆车已经全部进入停车场内！");
//        }
//    }
//}
//
//class MyInParkingDataEventTranslator implements EventTranslator<MyInParkingDataEvent> {
//
//    @Override
//    public void translateTo(MyInParkingDataEvent event, long sequence) {
//        event.setCarLicense("车牌号：粤A"+(int)(Math.random()*100000));
//        System.out.println("生产者 Thread ID "+Thread.currentThread().getId()+"写完了一个event");
//    }
//}