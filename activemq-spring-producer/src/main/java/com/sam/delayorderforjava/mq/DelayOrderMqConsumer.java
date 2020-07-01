package com.sam.delayorderforjava.mq;

import com.google.gson.Gson;
import com.sam.delayorderforjava.component.DelayOrderProcessor;
import com.sam.delayorderforjava.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 延时订单消息队列消费者
 */
@Component
public class DelayOrderMqConsumer implements MessageListener {

    @Autowired
    private DelayOrderProcessor processor;

    @Override
    public void onMessage(Message message) {
        TextMessage msg = (TextMessage) message;
        try {
            String orderStr = msg.getText();
            System.out.println("消息消费者接收到订单消息："+orderStr);
            Gson gson = new Gson();
            OrderEntity entity = gson.fromJson(orderStr, OrderEntity.class);
            processor.checkDelayOrder(entity);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
