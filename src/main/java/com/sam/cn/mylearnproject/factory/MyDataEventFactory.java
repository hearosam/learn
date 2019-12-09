package com.sam.cn.mylearnproject.factory;

import com.lmax.disruptor.EventFactory;
import com.sam.cn.mylearnproject.bean.MyDataEvent;

/**
 * 创建工厂类实例化Event
 * EventFactory 工厂用于实例化Event类
 * @author sam
 */
public class MyDataEventFactory implements EventFactory<MyDataEvent> {

    @Override
    public MyDataEvent newInstance() {
        return new MyDataEvent();
    }
}
