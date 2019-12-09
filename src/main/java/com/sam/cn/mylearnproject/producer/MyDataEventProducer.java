//package com.sam.cn.mylearnproject.producer;
//
//import com.lmax.disruptor.RingBuffer;
//import com.sam.cn.mylearnproject.bean.MyDataEvent;
//
//import java.nio.ByteBuffer;
//
///**
// * 生产者
// * @author sam.liang
// */
//public class MyDataEventProducer {
//    /**
//     *环形缓冲区
//     */
//    private RingBuffer<MyDataEvent> ringBuffer;
//
//    public MyDataEventProducer(RingBuffer<MyDataEvent> ringBuffer) {
//        this.ringBuffer = ringBuffer;
//    }
//
//    /**
//     * 发布事件，每调用一次就发布一次事件，它的参数buffer会通过事件传递给消费者
//     * @param buffer 用 byteBuffer传参 是考虑到 Disruptor 是消息框架，而ByteBuffer又是读取时信道 (SocketChannel)最常用的缓冲区
//     */
//    public void publishData(ByteBuffer buffer) {
//        //获取环形缓冲区下一个索引值
//        long sequence = ringBuffer.next();
//        try {
//            //通过索引值获取其对象
//            MyDataEvent myDataEvent = ringBuffer.get(sequence);
//            //给event对象赋值
//            myDataEvent.setValue(buffer.getLong(0));
//        }catch (Exception e) {
//            System.out.println(e.getMessage());
//        }finally {
//            //发布事件,其实就是发布索引，发布方法必须放在finally中，避免出现阻塞的情况
//            ringBuffer.publish(sequence);
//        }
//    }
//
//}
