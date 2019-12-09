//package com.sam.cn.mylearnproject.handler;
//
//
//import com.lmax.disruptor.EventHandler;
//import com.sam.cn.mylearnproject.bean.MyDataEvent;
//
///**
// * 创建一个事件处理器，也就是消费者,
// * @author sam.liang
// */
//public class MyDataEventHandler implements EventHandler<MyDataEvent> {
//    @Override
//    public void onEvent(MyDataEvent event, long sequence, boolean endOfBatch) throws Exception {
//        System.out.println("处理事件，打印数据：------->"+event.getValue());
//    }
//}
